package com.bootcamp.project_stock_data.service.impl;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.bootcamp.project_stock_data.entity.StockEntity;
import com.bootcamp.project_stock_data.entity.StockOhlcEntity;
import com.bootcamp.project_stock_data.entity.StockProfileEntity;
import com.bootcamp.project_stock_data.exception.ExceptionHandler;
import com.bootcamp.project_stock_data.mapper.DataMapper;
import com.bootcamp.project_stock_data.repository.StockOhlcRepository;
import com.bootcamp.project_stock_data.repository.StockProfileRepository;
import com.bootcamp.project_stock_data.repository.StockRepository;
import com.bootcamp.project_stock_data.service.StockService;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockProfileRepository stockProfileRepository;

    @Autowired
    private StockOhlcRepository stockOhlcRepository;

    @Value("${project-data-provider.base.url}")
    private String dataProviderUrl;

    private static final Duration COMPANY_TTL = Duration.ofDays(1);
    private static final long API_DELAY_MS = 500; // 調整為 500ms (120 calls/min, 安全於 150/min)

    @Override
    public List<String> getAllSymbols() {
        try {
            return stockRepository.findAll().stream().map(StockEntity::getSymbol).collect(Collectors.toList());
        } catch (Exception e) {
            return ExceptionHandler.handleDatabaseException("fetching all symbols", null, (e instanceof DataAccessException) ? (DataAccessException) e : null);
        }
    }

    @Override
    public List<Map<String, Object>> getAllQuotes() {
        List<String> symbols = getAllSymbols();
        List<Map<String, Object>> quotes = new ArrayList<>();
        for (String symbol : symbols) {
            try {
                String url = dataProviderUrl + "/quote/" + symbol;
                Map<String, Object> quote = DataMapper.fetchDataAsMap(restTemplate, url);
                if (quote != null) {
                    quote.put("symbol", symbol);
                    quotes.add(quote);
                    Thread.sleep(API_DELAY_MS);
                }
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                ExceptionHandler.handleRestException("fetching quote", symbol, ie);
            }
        }
        ExceptionHandler.logInfo("Fetched {} quotes", quotes.size()); // 使用封裝方法記錄 info
        return quotes;
    }

    @Override
    public List<Map<String, Object>> getAllCompanies() {
        List<String> symbols = getAllSymbols();
        List<Map<String, Object>> companies = new ArrayList<>();
        for (String symbol : symbols) {
            Map<String, Object> company = getCompanyData(symbol);
            if (company != null) {
                companies.add(company);
            }
        }
        return companies;
    }

    private Map<String, Object> getCompanyData(String symbol) {
        String key = "company::" + symbol;
        try {
            Object companies = redisTemplate.opsForValue().get(key);
            if (companies != null) {
                return (Map<String, Object>) companies;
            }

            String url = dataProviderUrl + "/company/" + symbol;
            Map<String, Object> company = DataMapper.fetchDataAsMap(restTemplate, url);
            if (company != null) {
                Optional<StockEntity> stockOpt = stockRepository.findBySymbol(symbol);
                if (stockOpt.isPresent()) {
                    StockProfileEntity profile = stockProfileRepository.findByStockId(stockOpt.get().getId());
                    if (profile == null) {
                        profile = new StockProfileEntity();
                        profile.setStock(stockOpt.get());
                    }
                    DataMapper.updateAndSaveProfile(profile, company, stockProfileRepository);
                }

                // Cache in Redis
                redisTemplate.opsForValue().set(key, company, COMPANY_TTL);
                company.put("symbol", symbol);

                // add delay after fetching to respect rate limits
                Thread.sleep(API_DELAY_MS);
                return company;
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            ExceptionHandler.handleRestException("fetching company data", symbol, ie);
        } catch (Exception e) {
            if (e instanceof RedisConnectionFailureException) {
                ExceptionHandler.handleRedisException("caching company data", symbol, (RedisConnectionFailureException) e);
            } else {
                ExceptionHandler.handleRestException("fetching company data", symbol, e);
            }
            return null;
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getOhlcData(String symbol) {
        try {
            Optional<StockEntity> stockOpt = stockRepository.findBySymbol(symbol);
            if (stockOpt.isPresent()) {
                List<StockOhlcEntity> ohlcList = stockOhlcRepository.findByStockIdOrderByDateAsc(stockOpt.get().getId());
                return ohlcList.stream().map(ohlc -> DataMapper.mapOhlcToMap(ohlc)).collect(Collectors.toList());
            }
        } catch (Exception e) {
            return ExceptionHandler.handleDatabaseException("fetching OHLC data", symbol, (e instanceof DataAccessException) ? (DataAccessException) e : null);
        }
        return Collections.emptyList();
    }
}
