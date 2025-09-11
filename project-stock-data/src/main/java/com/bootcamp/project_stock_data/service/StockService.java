package com.bootcamp.project_stock_data.service;

import com.bootcamp.project_stock_data.model.dto.CompanyDTO;
import com.bootcamp.project_stock_data.model.dto.OhlcDTO;
import com.bootcamp.project_stock_data.model.dto.StockQuoteDTO;
import com.bootcamp.project_stock_data.entity.StockEntity;
import com.bootcamp.project_stock_data.entity.StockOhlcEntity;
import com.bootcamp.project_stock_data.entity.CompanyEntity;
import com.bootcamp.project_stock_data.repository.StockOhlcRepository;
import com.bootcamp.project_stock_data.repository.CompanyRepository;
import com.bootcamp.project_stock_data.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockService {
    @Autowired
    private  RestTemplate restTemplate;

    @Autowired
    private  StockRepository stockRepository;

    @Autowired
    private  CompanyRepository companyRepository;

    @Autowired
    private  StockOhlcRepository stockOhlcRepository;

    @Autowired
    private  RedisTemplate<String, CompanyDTO> redisTemplate;

    @Value("${data.provider.url}")
    private String dataProviderUrl;

    // public StockService(StockRepository stockRepository, StockProfileRepository profileRepo, StockOhlcRepository ohlcRepo, RedisTemplate<String, CompanyResponse> redisTemplate) {
    //     this.stockRepository = stockRepository;
    //     this.profileRepo = profileRepo;
    //     this.ohlcRepo = ohlcRepo;
    //     this.redisTemplate = redisTemplate;
    // }

    public List<String> getAllSymbols() {
        return stockRepository.findAll().stream().map(StockEntity::getSymbol).collect(Collectors.toList());
    }

    public List<StockQuoteDTO> getAllRealTimeQuotes() {
        List<String> symbols = getAllSymbols();
        return symbols.stream().map(this::getRealTimeQuote).collect(Collectors.toList());
    }

    public StockQuoteDTO getRealTimeQuote(String symbol) {
        return restTemplate.getForObject(dataProviderUrl + "/quote?symbol=" + symbol, StockQuoteDTO.class);
    }

    public List<CompanyDTO> getAllCompanyData() {
        List<String> symbols = getAllSymbols();
        return symbols.stream().map(this::getCompanyData).collect(Collectors.toList());
    }

    public CompanyDTO getCompanyData(String symbol) {
        String key = "company::" + symbol;
        CompanyDTO cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return cached;
        }
        // 查 DB
        CompanyEntity profile = companyRepository.findById(symbol).orElse(null);
        if (profile != null) {
            CompanyDTO company = mapToCompanyDTO(profile);
            redisTemplate.opsForValue().set(key, company, Duration.ofDays(1));
            return company;
        }
        // 呼叫 data-provider 並儲存到 DB
        CompanyDTO company = restTemplate.getForObject(dataProviderUrl + "/company?symbol=" + symbol, CompanyDTO.class);
        if (company != null) {
            CompanyEntity newProfile = mapToCompanyEntity(symbol, company);
            companyRepository.save(newProfile);
            redisTemplate.opsForValue().set(key, company, Duration.ofDays(1));
        }
        return company;
    }

    private CompanyDTO mapToCompanyDTO(CompanyEntity companyEntities) {
        CompanyDTO Companies = new CompanyDTO();
        Companies.setFinnhubIndustry(companyEntities.getFinnhubIndustry());
        Companies.setMarketCapitalization(companyEntities.getMarketCapitalization());
        Companies.setLogo(companyEntities.getLogo());
        Companies.setName(companyEntities.getName());
        Companies.setShareOutstanding(companyEntities.getShareOutstanding());
        return Companies;
    }

    private CompanyEntity mapToCompanyEntity(String symbol, CompanyDTO company) {
        CompanyEntity CompanyEntities = new CompanyEntity();
        if (symbol == null) {
            throw new IllegalArgumentException("Ticker cannot be null");
        }
        CompanyEntities.setTicker(symbol);
        CompanyEntities.setFinnhubIndustry(company.getFinnhubIndustry());
        CompanyEntities.setMarketCapitalization(company.getMarketCapitalization());
        CompanyEntities.setLogo(company.getLogo());
        CompanyEntities.setName(company.getName());
        CompanyEntities.setShareOutstanding(company.getShareOutstanding());
        return CompanyEntities;
    }

    public List<OhlcDTO> getOhlcPerStock(String symbol) {
        List<StockOhlcEntity> data = stockOhlcRepository.findBySymbol(symbol);
        return data.stream().map(this::mapToOhlcDto).collect(Collectors.toList());
    }

    private OhlcDTO mapToOhlcDto(StockOhlcEntity data) {
        OhlcDTO dto = new OhlcDTO();
        dto.setDate(data.getDate());
        dto.setOpen(data.getOpen());
        dto.setHigh(data.getHigh());
        dto.setLow(data.getLow());
        dto.setClose(data.getClose());
        dto.setVolume(data.getVolume());
        return dto;
    }
}