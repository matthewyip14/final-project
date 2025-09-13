package com.bootcamp.project_stock_data.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.bootcamp.project_stock_data.service.StockService;

@RestController
public class StockDataController {
  
  @Autowired
  private StockService stockService;

  @GetMapping("/stocks")
  public List<String> getAllSymbols() {
      return stockService.getAllSymbols();
  }

  @GetMapping("/quotes")
  public List<Map<String, Object>> getAllQuotes() {
      return stockService.getAllQuotes();
  }

  @GetMapping("/companies")
  public List<Map<String, Object>> getAllCompanies() {
      return stockService.getAllCompanies();
  }

  @GetMapping("/ohlc/{symbol}")
  public List<Map<String, Object>> getOhlc(@PathVariable String symbol) {
      return stockService.getOhlcData(symbol);
  }
}
