package com.bootcamp.project_heatmap_ui.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class HeatmapHierarchyBuilder {
	private final IndustrySectorClassifier classifier;

	public HeatmapHierarchyBuilder(IndustrySectorClassifier classifier) {
		this.classifier = classifier;
	}

	public Map<String, Object> build(List<Map<String, Object>> quotes, List<Map<String, Object>> companies) {
		Map<String, Map<String, Object>> companyByTicker = companies.stream()
				.filter(m -> m.get("ticker") != null)
				.collect(Collectors.toMap(m -> (String) m.get("ticker"), m -> m));

		Map<String, Map<String, Object>> sectorNodes = new HashMap<>();

		for (Map<String, Object> quote : quotes) {
			String symbol = (String) quote.get("symbol");
			Map<String, Object> company = companyByTicker.get(symbol);
			if (company == null) continue;

			String industry = (String) company.get("finnhubIndustry");
			String sectorName = classifier.toSector(industry);

			Map<String, Object> sectorNode = sectorNodes.computeIfAbsent(sectorName, k -> {
				Map<String, Object> s = new HashMap<>();
				s.put("name", k);
				s.put("children", new ArrayList<Map<String, Object>>());
				return s;
			});

			@SuppressWarnings("unchecked")
			List<Map<String, Object>> sectorChildren = (List<Map<String, Object>>) sectorNode.get("children");
			Map<String, Object> industryNode = null;
			for (Map<String, Object> node : sectorChildren) {
				if (industry != null && industry.equals(node.get("name"))) { industryNode = node; break; }
			}
			if (industryNode == null) {
				industryNode = new HashMap<>();
				industryNode.put("name", industry == null ? "Other" : industry);
				industryNode.put("children", new ArrayList<Map<String, Object>>());
				sectorChildren.add(industryNode);
			}

            Map<String, Object> stock = new HashMap<>();
            stock.put("name", symbol);
            Object marketCap = company.get("marketCapitalization");
            if (!(marketCap instanceof Number)) {
                marketCap = 1L; // fallback minimal size to remain visible
            }
            stock.put("value", marketCap);
			stock.put("dp", quote.get("dp"));
			stock.put("pc", quote.get("pc"));

			@SuppressWarnings("unchecked")
			List<Map<String, Object>> industryChildren = (List<Map<String, Object>>) industryNode.get("children");
			industryChildren.add(stock);
		}

		Map<String, Object> root = new HashMap<>();
		root.put("name", "S&P 500");
		root.put("children", new ArrayList<>(sectorNodes.values()));
		return root;
	}
}
