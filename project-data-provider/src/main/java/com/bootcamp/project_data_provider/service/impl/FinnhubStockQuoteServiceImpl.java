package com.bootcamp.project_data_provider.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.bootcamp.project_data_provider.model.dto.CandlesDTO;
import com.bootcamp.project_data_provider.model.dto.CompanyDTO;
import com.bootcamp.project_data_provider.model.dto.QuoteDTO;
import com.bootcamp.project_data_provider.service.StockQuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Service
public class FinnhubStockQuoteServiceImpl implements StockQuoteService {

  @Value("${finnhub.api.key}")
  private String apiKey;

  @Autowired
  private RestTemplate restTemplate;

  @Override
  public QuoteDTO getQuote(String symbol) {
      String url = "https://finnhub.io/api/v1/quote?symbol=" + symbol + "&token=" + apiKey;
      QuoteDTO quote = restTemplate.getForObject(url, QuoteDTO.class);
      if (quote != null) {
          quote.setSymbol(symbol); // 设置 symbol 属性
      }
      return quote;
  }

  @Override
  public CompanyDTO getCompanyProfile(String symbol) {
      String url = "https://finnhub.io/api/v1/stock/profile2?symbol=" + symbol + "&token=" + apiKey;
      CompanyDTO company = restTemplate.getForObject(url, CompanyDTO.class);
      if (company != null) {
          company.setSymbol(symbol); // 设置 symbol 属性
      }
      return company;
  }

  @Override
  public CandlesDTO getCandles(String symbol, String resolution, long from, long to) {
      String url = "https://finnhub.io/api/v1/stock/candle?symbol=" + symbol + "&resolution=" + resolution + "&from=" + from + "&to=" + to + "&token=" + apiKey;
      CandlesDTO candles = restTemplate.getForObject(url, CandlesDTO.class);
      if (candles != null) {
          candles.setSymbol(symbol); // 设置 symbol 属性
      }
      return candles;
  }
}
