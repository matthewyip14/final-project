package com.bootcamp.project_data_provider.service.impl;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.bootcamp.project_data_provider.model.dto.CompanyDTO;
import com.bootcamp.project_data_provider.model.dto.StockQuoteDTO;
import com.bootcamp.project_data_provider.service.CallService;

@Service
public class CallServiceImpl implements CallService{
  @Autowired
  private RestTemplate restTemplate;
  
  @Value("${finnhub.api.key}")
  private String apiKey;

  @Override
  public List<StockQuoteDTO> getAllStockQuotes(String symbol) {
    String url = "https://finnhub.io/api/v1/quote?symbol=" + symbol + "&token=" + apiKey;
    return Arrays.asList(this.restTemplate.getForObject(url, StockQuoteDTO[].class));
  }

  @Override
  public List<CompanyDTO> getAllCompanies(String ticker) {
    String url = "https://finnhub.io/api/v1/stock/profile2?symbol=" + ticker + "&token=" + apiKey;
    return Arrays.asList(this.restTemplate.getForObject(url, CompanyDTO[].class));
  }

  // StockQuoteDTO getStockQuotesBySymbol(String symbol);
  // CompanyDTO getCompaniesByTicker(String ticker);

}
