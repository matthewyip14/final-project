package com.bootcamp.project_data_provider.service;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.bootcamp.project_data_provider.dto.CompanyDTO;
import com.bootcamp.project_data_provider.dto.StockQuoteDTO;

@Service
public class FinnhubService {
  @Autowired
  private RestTemplate restTemplate;
  
  @Value("${finnhub.api.key}")
  private String apiKey;

  public List<StockQuoteDTO> getQuotes(String symbol) {
    String url = "https://finnhub.io/api/v1/quote?symbol=" + symbol + "&token=" + apiKey;
    return Arrays.asList(this.restTemplate.getForObject(url, StockQuoteDTO[].class));
  }

  public List<CompanyDTO> getCompanies(String symbol) {
    String url = "https://finnhub.io/api/v1/stock/profile2?symbol=" + symbol + "&token=" + apiKey;
    return Arrays.asList(this.restTemplate.getForObject(url, CompanyDTO[].class));
  }
}
