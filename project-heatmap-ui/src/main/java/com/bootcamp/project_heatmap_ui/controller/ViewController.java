package com.bootcamp.project_heatmap_ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ViewController {
  @GetMapping("/heatmap")
  public String heatmap() {
      return "index";
  }

  @GetMapping("/ohlc-view/{symbol}")
  public String ohlc(@PathVariable String symbol) {
      return "ohlc";
  }
}
