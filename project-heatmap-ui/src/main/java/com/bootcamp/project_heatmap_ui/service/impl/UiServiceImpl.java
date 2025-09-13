package com.bootcamp.project_heatmap_ui.service.impl;

import java.util.ArrayList;
import java.util.Collections;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import com.bootcamp.project_heatmap_ui.service.UiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UiServiceImpl implements UiService {

    private static final Logger logger = LoggerFactory.getLogger(UiServiceImpl.class);

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
                    Map<String, Object> dto = new HashMap<>();
                    dto.put("symbol", symbol);
                    dto.put("name", company.get("name"));
                    dto.put("price", quote.get("c"));
                    dto.put("change", quote.get("d"));
                    dto.put("dp", quote.get("dp"));
                    dto.put("marketCap", company.get("marketCapitalization"));
                    dto.put("industry", company.get("finnhubIndustry"));
                    dto.put("logo", company.get("logo"));
                    dto.put("sharesOutstanding", company.get("shareOutstanding"));
                    mergedData.add(dto);
                }
            }
            return mergedData;
        } catch (HttpClientErrorException e) {
            logger.error("HTTP client error fetching heatmap data: {}", e.getStatusCode(), e);
            return Collections.emptyList();
        } catch (HttpServerErrorException e) {
            logger.error("HTTP server error fetching heatmap data: {}", e.getStatusCode(), e);
            return Collections.emptyList();
        } catch (ResourceAccessException e) {
            logger.error("Resource access error (timeout/network) fetching heatmap data: {}", e.getMessage(), e);
            return Collections.emptyList();
        } catch (Exception e) {
            logger.error("Unexpected error fetching heatmap data: {}", e.getMessage(), e);
            return Collections.emptyList();
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
        } catch (HttpClientErrorException e) {
            logger.error("HTTP client error fetching OHLC data for symbol {}: {}", symbol, e.getStatusCode(), e);
            return Collections.emptyList();
        } catch (HttpServerErrorException e) {
            logger.error("HTTP server error fetching OHLC data for symbol {}: {}", symbol, e.getStatusCode(), e);
            return Collections.emptyList();
        } catch (ResourceAccessException e) {
            logger.error("Resource access error (timeout/network) fetching OHLC data for symbol {}: {}", symbol, e.getMessage(), e);
            return Collections.emptyList();
        } catch (Exception e) {
            logger.error("Unexpected error fetching OHLC data for symbol {}: {}", symbol, e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
