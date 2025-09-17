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
import org.springframework.web.client.RestTemplate;
import com.bootcamp.project_heatmap_ui.service.UiService;
import com.bootcamp.project_heatmap_ui.mapper.DataMapper;
import com.bootcamp.project_heatmap_ui.exception.ExceptionHandler;

@Service
public class UiServiceImpl implements UiService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${project-stock-data.base.url}")
    private String stockDataUrl;

    @Override
    public Map<String, Object> getHeatmapData() { // 改為 Map 支援 hierarchy
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

            // 聚合 by industry
            Map<String, Map<String, Object>> industryMap = new HashMap<>();
            Map<String, List<Map<String, Object>>> industryStocks = new HashMap<>();

            for (Map<String, Object> company : companies) {
                String industry = (String) company.get("finnhubIndustry");
                if (industry == null) industry = "Other";
                industryMap.computeIfAbsent(industry, k -> new HashMap<>()).put("name", industry);
                industryMap.get(industry).put("value", 0L); // 初始化 sum volume
                industryStocks.computeIfAbsent(industry, k -> new ArrayList<>()).add(company);
            }

            // 合併 quote 和 company，計算 volume sum
            for (Map<String, Object> quote : quotes) {
                String symbol = (String) quote.get("symbol");
                for (Map<String, Object> company : companies) {
                    if (symbol.equals(company.get("ticker"))) {
                        String industry = (String) company.get("finnhubIndustry");
                        if (industry == null) industry = "Other";
                        long volume = ((Number) quote.getOrDefault("v", 0L)).longValue(); // volume from quote
                        long currentSum = (Long) industryMap.get(industry).get("value");
                        industryMap.get(industry).put("value", currentSum + volume);

                        Map<String, Object> stock = new HashMap<>();
                        stock.put("name", symbol);
                        stock.put("value", company.get("marketCapitalization")); // size by marketCap
                        stock.put("dp", quote.get("dp")); // color by dp
                        stock.put("pc", quote.get("pc")); // last close price
                        industryStocks.get(industry).add(stock); // add to children
                        break;
                    }
                }
            }

            // 建構 hierarchy
            Map<String, Object> root = new HashMap<>();
            root.put("name", "S&P 500");
            List<Map<String, Object>> children = new ArrayList<>();
            for (String industry : industryMap.keySet()) {
                Map<String, Object> sector = industryMap.get(industry);
                sector.put("children", industryStocks.get(industry));
                children.add(sector);
            }
            root.put("children", children);
            return root;
        } catch (Exception e) {
            // log error
            return new HashMap<>(); // empty root
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
