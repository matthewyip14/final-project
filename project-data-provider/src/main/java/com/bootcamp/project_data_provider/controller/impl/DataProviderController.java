package com.bootcamp.project_data_provider.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.bootcamp.project_data_provider.controller.DataProviderOperation;
import com.bootcamp.project_data_provider.model.dto.CompanyDTO;
import com.bootcamp.project_data_provider.model.dto.StockQuoteDTO;
import com.bootcamp.project_data_provider.service.CallService;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class DataProviderController implements DataProviderOperation{
  @Autowired
  private CallService callServices;

  @Override
  @GetMapping(value="/quote")
  public StockQuoteDTO getQuotes(@RequestParam String symbol) throws JsonProcessingException {
    return callServices.getAllStockQuotes(symbol).get(0);
  }

  @Override
  @GetMapping(value="/company")
  public CompanyDTO getCompany(@RequestParam String ticker) throws JsonProcessingException {
    return callServices.getAllCompanies(ticker).get(0);
  }

}
