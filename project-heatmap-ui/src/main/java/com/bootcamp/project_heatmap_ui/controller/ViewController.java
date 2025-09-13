package com.bootcamp.project_heatmap_ui.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ViewController {
  @GetMapping("/heatmap")
  public String heatmap() {
      return "index";
  }
}
