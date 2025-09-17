package com.bootcamp.project_heatmap_ui.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class AppConfig {
  @Bean
  public RestTemplate restTemplate() {
    RestTemplate rt = new RestTemplate();
    rt.setUriTemplateHandler(new DefaultUriBuilderFactory());
    return rt;
  }
}
