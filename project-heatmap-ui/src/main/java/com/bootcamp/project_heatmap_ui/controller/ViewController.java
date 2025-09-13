package com.bootcamp.project_heatmap_ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
  @GetMapping("/heatmap")
  public String heatmap() {
      return "index";
  }
}
