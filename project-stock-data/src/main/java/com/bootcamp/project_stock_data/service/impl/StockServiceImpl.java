package com.bootcamp.project_stock_data.service.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.bootcamp.project_stock_data.entity.StockEntity;
import com.bootcamp.project_stock_data.entity.StockOhlcEntity;
import com.bootcamp.project_stock_data.entity.StockProfileEntity;
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

  @Value("${project.data.provider.url}")
  private String dataProviderUrl;

  private static final Duration COMPANY_TTL = Duration.ofDays(1);

  @Override
  public List<String> getAllSymbols() {
    return stockRepository.findAll()
        .stream()
        .map(stock -> stock.getSymbol())
        .collect(Collectors.toList());
  }

  @Override
  public List<Map<String, Object>> getAllQuotes() {
    List<String> symbols = getAllSymbols();
    List<Map<String, Object>> quotes = new ArrayList<>();
    for (String symbol : symbols) {
        try {
            Map<String, Object> quote = this.restTemplate.getForObject(dataProviderUrl + "/quote/" + symbol, Map.class);
            quote.put("symbol", symbol);
            quotes.add(quote);
        } catch (Exception e) {
            // Log error, continue to next
        }
    }
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
      Object cached = redisTemplate.opsForValue().get(key);
      if (cached != null) {
        return (Map<String, Object>) cached;
      }

      // Read-through: fetch from data-provider and store
      try {
        Map<String, Object> company = this.restTemplate.getForObject(dataProviderUrl + "/company/" + symbol, Map.class);
        if (company != null) {
          // Save to DB if needed (daily refresh)
          Optional<StockEntity> stockOpt = stockRepository.findBySymbol(symbol);
          if (stockOpt.isPresent()) {
              StockProfileEntity profile = stockProfileRepository.findByStockId(stockOpt.get().getId());
              if (profile == null) {
                  profile = new StockProfileEntity();
                  profile.setStock(stockOpt.get());
                }
                profile.setCompanyName((String) company.get("name"));
                profile.setIndustry((String) company.get("finnhubIndustry"));
                profile.setMarketCap((Long) company.get("marketCapitalization"));
                profile.setLogoUrl((String) company.get("logo"));
                profile.setSharesOutstanding((Long) company.get("shareOutstanding"));
                profile.setLastUpdated(LocalDateTime.now());
                stockProfileRepository.save(profile);
            }

            // Cache in Redis
            redisTemplate.opsForValue().set(key, company, COMPANY_TTL);
            company.put("symbol", symbol);
            return company;
        }
      } catch (Exception e) {
          // Log error
      }
      return null;
  }

    @Override
    public List<Map<String, Object>> getOhlcData(String symbol) {
        Optional<StockEntity> stockOpt = stockRepository.findBySymbol(symbol);
        if (stockOpt.isPresent()) {
            List<StockOhlcEntity> ohlcList = stockOhlcRepository.findByStockIdOrderByDateAsc(stockOpt.get().getId());
            return ohlcList.stream().map(ohlc -> {
                Map<String, Object> map = new HashMap<>();
                map.put("date", ohlc.getDate());
                map.put("open", ohlc.getOpen());
                map.put("high", ohlc.getHigh());
                map.put("low", ohlc.getLow());
                map.put("close", ohlc.getClose());
                map.put("volume", ohlc.getVolume());
                return map;
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
