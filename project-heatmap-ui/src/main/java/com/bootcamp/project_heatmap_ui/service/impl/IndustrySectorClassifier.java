package com.bootcamp.project_heatmap_ui.service.impl;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class IndustrySectorClassifier {
	private final Map<String, String> industryToSector;

	public IndustrySectorClassifier() {
		this.industryToSector = new HashMap<>();
		industryToSector.put("Technology", "Information Technology");
		industryToSector.put("Semiconductors", "Information Technology");
		industryToSector.put("Software", "Information Technology");
		industryToSector.put("Communications", "Communication Services");
		industryToSector.put("Media", "Communication Services");
		industryToSector.put("Retail", "Consumer Discretionary");
		industryToSector.put("Consumer products", "Consumer Staples");
		industryToSector.put("Beverages", "Consumer Staples");
		industryToSector.put("Pharmaceuticals", "Health Care");
		industryToSector.put("Health Care", "Health Care");
		industryToSector.put("Energy", "Energy");
		industryToSector.put("Financial Services", "Financials");
		industryToSector.put("Banking", "Financials");
	}

	public String toSector(String industry) {
		if (industry == null || industry.isBlank()) return "Other";
		return industryToSector.getOrDefault(industry, "Other");
	}
}
