package com.bootcamp.project_heatmap_ui.service.impl;

import java.util.List;
import java.util.Map;

public interface StockDataClient {
	List<Map<String, Object>> fetchQuotes();
	List<Map<String, Object>> fetchCompanies();
	List<Map<String, Object>> fetchOhlc(String symbol);
}
