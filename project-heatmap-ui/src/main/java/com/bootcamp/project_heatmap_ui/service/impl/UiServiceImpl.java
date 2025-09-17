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

    private final StockDataClient stockClient;
    private final HeatmapHierarchyBuilder hierarchyBuilder;

    public UiServiceImpl(StockDataClient stockClient, HeatmapHierarchyBuilder hierarchyBuilder) {
        this.stockClient = stockClient;
        this.hierarchyBuilder = hierarchyBuilder;
    }

    @Override
    public Map<String, Object> getHeatmapData() { // 改為 Map 支援 hierarchy
        try {
            List<Map<String, Object>> quotes = stockClient.fetchQuotes();
            List<Map<String, Object>> companies = stockClient.fetchCompanies();
            return hierarchyBuilder.build(quotes, companies);
        } catch (Exception e) {
            // log error
            return new HashMap<>(); // empty root
        }
    }

    @Override
    public List<Map<String, Object>> getOhlcData(String symbol) {
        try {
            return stockClient.fetchOhlc(symbol);
        } catch (Exception e) {
            return ExceptionHandler.handleRestException("fetching OHLC data", symbol, e) ? Collections.emptyList() : null;
        }
    }
}
