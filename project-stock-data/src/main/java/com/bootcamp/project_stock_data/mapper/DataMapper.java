package com.bootcamp.project_stock_data.mapper;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import com.bootcamp.project_stock_data.entity.StockOhlcEntity;
import com.bootcamp.project_stock_data.entity.StockProfileEntity;
import com.bootcamp.project_stock_data.repository.StockProfileRepository;

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
   * 更新並儲存 StockProfileEntity 實體
   * @param profile StockProfileEntity 實例
   * @param company 包含公司數據的 Map
   * @param stockProfileRepository StockProfileRepository 實例
   * @return 更新後的 StockProfileEntity
   */
  public static StockProfileEntity updateAndSaveProfile(StockProfileEntity profile, Map<String, Object> company, StockProfileRepository stockProfileRepository) {
      try {
          profile.setCompanyName((String) company.get("name"));
          profile.setIndustry((String) company.get("finnhubIndustry"));
          profile.setMarketCap((Long) company.get("marketCapitalization"));
          profile.setLogoUrl((String) company.get("logo"));
          profile.setSharesOutstanding((Long) company.get("shareOutstanding"));
          profile.setLastUpdated(LocalDateTime.now());
          return stockProfileRepository.save(profile);
      } catch (Exception e) {
          logger.error("Error updating and saving profile: {}", profile, e);
          return null;
      }
  }

  /**
   * 將 OHLC 數據映射到 Map
   * @param ohlc StockOhlcEntity 實例
   * @return 包含 OHLC 數據的 Map
   */
  public static Map<String, Object> mapOhlcToMap(StockOhlcEntity ohlc) {
      Map<String, Object> map = new HashMap<>();
      try {
          map.put("date", ohlc.getDate());
          map.put("open", ohlc.getOpen());
          map.put("high", ohlc.getHigh());
          map.put("low", ohlc.getLow());
          map.put("close", ohlc.getClose());
          map.put("volume", ohlc.getVolume());
          return map;
      } catch (Exception e) {
          logger.error("Error mapping OHLC data: {}", ohlc, e);
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
