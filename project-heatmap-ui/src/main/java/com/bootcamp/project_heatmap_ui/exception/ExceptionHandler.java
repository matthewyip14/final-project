package com.bootcamp.project_heatmap_ui.exception;

import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

public class ExceptionHandler {
  
  private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

  /**
   * 處理 RestTemplate 相關異常並記錄日誌
   * @param operation 描述操作的字串（如 "fetching quote"）
   * @param symbol 相關符號（如股票代碼）
   * @param e 異常對象
   * @return 是否成功處理（true 表示成功，false 表示失敗）
   */
  public static boolean handleRestException(String operation, String symbol, Exception e) {
      if (e instanceof HttpClientErrorException) {
          HttpClientErrorException httpEx = (HttpClientErrorException) e;
          logger.error("HTTP client error {} for symbol {}: {}", operation, symbol, httpEx.getStatusCode(), e);
          return false;
      } else if (e instanceof HttpServerErrorException) {
          HttpServerErrorException httpEx = (HttpServerErrorException) e;
          logger.error("HTTP server error {} for symbol {}: {}", operation, symbol, httpEx.getStatusCode(), e);
          return false;
      } else if (e instanceof ResourceAccessException) {
          logger.error("Resource access error (timeout/network) {} for symbol: {}", operation, symbol, e);
          return false;
      } else if (e instanceof InterruptedException) {
          Thread.currentThread().interrupt();
          logger.error("Thread interrupted during {} for symbol: {}", operation, symbol, e);
          return false;
      } else {
          logger.error("Unexpected error {} for symbol: {}", operation, symbol, e);
          return false;
      }
  }

  /**
   * 處理資料庫相關異常並記錄日誌
   * @param operation 描述操作的字串（如 "fetching symbols"）
   * @param symbol 相關符號（如股票代碼，可為 null）
   * @param e 異常對象
   * @return 空列表作為預設返回值
   */
  public static <T> List<T> handleDatabaseException(String operation, String symbol, DataAccessException e) {
      logger.error("Database access error {} for symbol: {}", operation, symbol, e);
      return Collections.emptyList();
  }

  /**
   * 通用異常處理，適用於未明確分類的異常
   * @param operation 描述操作的字串
   * @param symbol 相關符號（如股票代碼，可為 null）
   * @param e 異常對象
   */
  public static void handleGeneralException(String operation, String symbol, Exception e) {
      logger.error("Unexpected error {} for symbol: {}", operation, symbol, e);
  }
}
