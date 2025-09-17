package com.bootcamp.project_heatmap_ui.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestStockDataClient implements StockDataClient {

	private final RestTemplate restTemplate;

	@Value("${project-stock-data.base.url}")
	private String stockDataUrl;

	public RestStockDataClient(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public List<Map<String, Object>> fetchQuotes() {
		ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
				stockDataUrl + "/quotes", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Map<String, Object>>>() {});
		return response.getBody();
	}

	@Override
	public List<Map<String, Object>> fetchCompanies() {
		ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
				stockDataUrl + "/companies", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Map<String, Object>>>() {});
		return response.getBody();
	}

	@Override
	public List<Map<String, Object>> fetchOhlc(String symbol) {
		ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
				stockDataUrl + "/ohlc/" + symbol, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Map<String, Object>>>() {});
		return response.getBody();
	}
}
