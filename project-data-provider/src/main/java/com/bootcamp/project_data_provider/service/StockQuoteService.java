package com.bootcamp.project_data_provider.service;

import com.bootcamp.project_data_provider.model.dto.CandlesDTO;
import com.bootcamp.project_data_provider.model.dto.CompanyDTO;
import com.bootcamp.project_data_provider.model.dto.QuoteDTO;

public interface StockQuoteService {
  QuoteDTO getQuote(String symbol);
  CompanyDTO getCompanyProfile(String symbol);
  CandlesDTO getCandles(String symbol, String resolution, long from, long to);
}
