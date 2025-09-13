package com.bootcamp.project_stock_data.service;

import java.util.List;
import java.util.Map;

public interface StockService {
  List<String> getAllSymbols();
  List<Map<String, Object>> getAllQuotes();
  List<Map<String, Object>> getAllCompanies();
  List<Map<String, Object>> getOhlcData(String symbol);
}
