package com.bootcamp.project_data_provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.bootcamp.project_data_provider.model.dto.CandlesDTO;
import com.bootcamp.project_data_provider.model.dto.CompanyDTO;
import com.bootcamp.project_data_provider.model.dto.QuoteDTO;
import com.bootcamp.project_data_provider.service.StockQuoteService;

@RestController
public class StockController {
  @Autowired
  private StockQuoteService stockQuoteService;

  @GetMapping("/quote/{symbol}")
  public QuoteDTO getQuote(@PathVariable String symbol) {
      return stockQuoteService.getQuote(symbol);
  }

  @GetMapping("/company/{symbol}")
  public CompanyDTO getCompany(@PathVariable String symbol) {
      return stockQuoteService.getCompanyProfile(symbol);
  }

  @GetMapping("/candles/{symbol}")
  public CandlesDTO getCandles(@PathVariable String symbol) {
      // 假設從現在起過去1年
      long to = System.currentTimeMillis() / 1000;
      long from = to - 31536000;
      return stockQuoteService.getCandles(symbol, "D", from, to);
  }
}
