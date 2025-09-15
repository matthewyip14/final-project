package com.bootcamp.project_heatmap_ui.service.impl;

import java.util.ArrayList;
import java.util.Collections;
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
import com.bootcamp.project_heatmap_ui.mapper.DataMapper;
import com.bootcamp.project_heatmap_ui.exception.ExceptionHandler;

@Service
public class UiServiceImpl implements UiService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${stock.data.url}")
    private String stockDataUrl;

    @Override
    public List<Map<String, Object>> getHeatmapData() {
        try {
            ResponseEntity<List<Map<String, Object>>> quotesResponse = restTemplate.exchange(
                    stockDataUrl + "/quotes",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );
            List<Map<String, Object>> quotes = quotesResponse.getBody();

            ResponseEntity<List<Map<String, Object>>> companiesResponse = restTemplate.exchange(
                    stockDataUrl + "/companies",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );
            List<Map<String, Object>> companies = companiesResponse.getBody();

            Map<String, Map<String, Object>> companyMap = companies.stream()
                    .collect(Collectors.toMap(c -> (String) c.get("ticker"), c -> c));

            List<Map<String, Object>> mergedData = new ArrayList<>();
            for (Map<String, Object> quote : quotes) {
                String symbol = (String) quote.get("symbol");
                Map<String, Object> company = companyMap.get(symbol);
                if (company != null) {
                    Map<String, Object> dto = DataMapper.buildHeatmapDto(symbol, company, quote);
                    if (dto != null) {
                        mergedData.add(dto);
                    }
                }
            }
            return mergedData;
        } catch (Exception e) {
            return ExceptionHandler.handleRestException("fetching heatmap data", null, e) ? Collections.emptyList() : null;
        }
    }

    @Override
    public List<Map<String, Object>> getOhlcData(String symbol) {
        try {
            ResponseEntity<List<Map<String, Object>>> ohlcResponse = restTemplate.exchange(
                    stockDataUrl + "/ohlc/" + symbol,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );
            return ohlcResponse.getBody();
        } catch (Exception e) {
            return ExceptionHandler.handleRestException("fetching OHLC data", symbol, e) ? Collections.emptyList() : null;
        }
    }
}
