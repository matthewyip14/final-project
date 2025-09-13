package com.bootcamp.project_data_provider.service;

import java.util.List;
import com.bootcamp.project_data_provider.model.dto.CompanyDTO;
import com.bootcamp.project_data_provider.model.dto.StockQuoteDTO;

public interface CallService {
  List<StockQuoteDTO> getAllStockQuotes(String symbol);
  List<CompanyDTO> getAllCompanies(String ticker);
  StockQuoteDTO getStockQuotesBySymbol(String symbol);
  CompanyDTO getCompaniesByTicker(String ticker);
}
