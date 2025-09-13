package com.bootcamp.project_heatmap_ui.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bootcamp.project_heatmap_ui.service.UiService;

@RestController
@RequestMapping(value = "/data")
public class ApiController {
  @Autowired
  private UiService uiService;

  @GetMapping("/heatmap")
  public List<Map<String, Object>> getHeatmapData() {
      return uiService.getHeatmapData(); // 合併 quote + company
  }

  @GetMapping("/ohlc/{symbol}")
  public List<Map<String, Object>> getOhlcData(@PathVariable String symbol) {
      return uiService.getOhlcData(symbol);
  }
}
