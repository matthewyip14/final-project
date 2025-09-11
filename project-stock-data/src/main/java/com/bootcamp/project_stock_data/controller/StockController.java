package com.bootcamp.project_stock_data.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.bootcamp.project_stock_data.model.dto.CompanyDTO;
import com.bootcamp.project_stock_data.model.dto.OhlcDTO;
import com.bootcamp.project_stock_data.model.dto.StockQuoteDTO;
import com.bootcamp.project_stock_data.service.StockService;

@RestController
public class StockController {
  @Autowired
  private StockService stockService;

  @GetMapping("/symbols")
  public List<String> getAllSymbols() {
      return this.stockService.getAllSymbols();
  }

  @GetMapping("/realtime")
  public List<StockQuoteDTO> getAllRealTimeData() {
      return this.stockService.getAllRealTimeQuotes();
  }

  @GetMapping("/company")
  public List<CompanyDTO> getAllCompanyData() {
      return this.stockService.getAllCompanyData();
  }

  @GetMapping("/ohlc")
  public List<OhlcDTO> getOhlc(@RequestParam String symbol) {
      return this.stockService.getOhlcPerStock(symbol);
  }
}
