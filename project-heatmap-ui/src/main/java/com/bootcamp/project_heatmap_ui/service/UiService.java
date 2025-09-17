package com.bootcamp.project_heatmap_ui.service;

import java.util.List;
import java.util.Map;

public interface UiService {
  Map<String, Object> getHeatmapData();
  List<Map<String, Object>> getOhlcData(String symbol);
}
