package com.bootcamp.project_data_provider.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.bootcamp.project_data_provider.model.dto.CompanyDTO;
import com.bootcamp.project_data_provider.model.dto.StockQuoteDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface DataProviderOperation {
  @GetMapping(value="/quote")
  public StockQuoteDTO getQuotes(@RequestParam String symbol)
    throws JsonProcessingException;

  @GetMapping(value="/company")
  public CompanyDTO getCompany(@RequestParam String ticker)
    throws JsonProcessingException;
    
}
