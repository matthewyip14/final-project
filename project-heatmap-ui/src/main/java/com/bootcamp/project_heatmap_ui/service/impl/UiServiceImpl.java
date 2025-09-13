package com.bootcamp.project_heatmap_ui.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.bootcamp.project_heatmap_ui.service.UiService;

@Service
public class UiServiceImpl implements UiService {
  @Autowired
  private RestTemplate restTemplate;

  @Value("${stock.data.url}")
  private String stockDataUrl;

  @Override
  public List<Map<String, Object>> getHeatmapData() {
      // Fetch quotes
      ResponseEntity<List<Map<String, Object>>> quotesResponse = restTemplate.exchange(
              stockDataUrl + "/quotes",
              HttpMethod.GET,
              null,
              new ParameterizedTypeReference<List<Map<String, Object>>>() {}
      );
      List<Map<String, Object>> quotes = quotesResponse.getBody();

      // Fetch companies
      ResponseEntity<List<Map<String, Object>>> companiesResponse = restTemplate.exchange(
              stockDataUrl + "/companies",
              HttpMethod.GET,
              null,
              new ParameterizedTypeReference<List<Map<String, Object>>>() {}
      );
      List<Map<String, Object>> companies = companiesResponse.getBody();

      // Merge by symbol
      Map<String, Map<String, Object>> companyMap = companies.stream()
              .collect(Collectors.toMap(c -> (String) c.get("ticker"), c -> c));

      List<Map<String, Object>> mergedData = new ArrayList<>();
      for (Map<String, Object> quote : quotes) {
          String symbol = (String) quote.get("symbol");
          Map<String, Object> company = companyMap.get(symbol);
          if (company != null) {
              Map<String, Object> dto = new HashMap<>();
              dto.put("symbol", symbol);
              dto.put("name", company.get("name"));
              dto.put("price", quote.get("c")); // current price
              dto.put("change", quote.get("d"));
              dto.put("dp", quote.get("dp")); // percent change
              dto.put("marketCap", company.get("marketCapitalization"));
              dto.put("industry", company.get("finnhubIndustry"));
              dto.put("logo", company.get("logo"));
              dto.put("sharesOutstanding", company.get("shareOutstanding"));
              mergedData.add(dto);
          }
      }
      return mergedData;
  }

  @Override
  public List<Map<String, Object>> getOhlcData(String symbol) {
      ResponseEntity<List<Map<String, Object>>> ohlcResponse = restTemplate.exchange(
              stockDataUrl + "/ohlc/" + symbol,
              HttpMethod.GET,
              null,
              new ParameterizedTypeReference<List<Map<String, Object>>>() {}
      );
      return ohlcResponse.getBody();
  }
  
}
