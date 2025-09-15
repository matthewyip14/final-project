package com.bootcamp.project_heatmap_ui.mapper;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

public class DataMapper {
  private static final Logger logger = LoggerFactory.getLogger(DataMapper.class);

  /**
   * 從指定 URL 獲取數據並轉為 Map
   * @param restTemplate RestTemplate 實例
   * @param url 請求 URL
   * @return 解析後的 Map 對象
   */
  public static Map<String, Object> fetchDataAsMap(RestTemplate restTemplate, String url) {
      try {
          return restTemplate.getForObject(url, Map.class);
      } catch (Exception e) {
          logger.error("Error fetching data from URL: {}", url, e);
          return null;
      }
  }

  /**
   * 組裝 Heatmap DTO
   * @param symbol 股票代碼
   * @param company 公司數據 Map
   * @param quote 報價數據 Map
   * @return 組裝後的 DTO Map
   */
  public static Map<String, Object> buildHeatmapDto(String symbol, Map<String, Object> company, Map<String, Object> quote) {
      Map<String, Object> dto = new HashMap<>();
      try {
          dto.put("symbol", symbol);
          dto.put("name", company.get("name"));
          dto.put("price", quote.get("c"));
          dto.put("change", quote.get("d"));
          dto.put("dp", quote.get("dp"));
          dto.put("marketCap", company.get("marketCapitalization"));
          dto.put("industry", company.get("finnhubIndustry"));
          dto.put("logo", company.get("logo"));
          dto.put("sharesOutstanding", company.get("shareOutstanding"));
          return dto;
      } catch (Exception e) {
          logger.error("Error building heatmap DTO for symbol: {}", symbol, e);
          return null;
      }
  }
}
