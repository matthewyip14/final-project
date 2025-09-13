final-project/
├── data-provider-app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/dataprovider/
│   │   │   │   ├── DataProviderApplication.java
│   │   │   │   ├── controller/StockController.java
│   │   │   │   ├── service/StockQuoteService.java
│   │   │   │   ├── service/impl/FinnhubStockQuoteServiceImpl.java
│   │   │   │   ├── dto/QuoteDTO.java
│   │   │   │   ├── dto/CompanyDTO.java
│   │   │   │   ├── dto/CandlesDTO.java
│   │   │   └── resources/application.properties
│   └── pom.xml
│   └── Dockerfile
├── stock-data-app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/stockdata/
│   │   │   │   ├── StockDataApplication.java
│   │   │   │   ├── controller/StockDataController.java
│   │   │   │   ├── service/StockService.java
│   │   │   │   ├── repository/StockRepository.java
│   │   │   │   ├── repository/StockProfileRepository.java
│   │   │   │   ├── repository/StockOhlcRepository.java
│   │   │   │   ├── entity/StockEntity.java
│   │   │   │   ├── entity/StockProfileEntity.java
│   │   │   │   ├── entity/StockOhlcEntity.java
│   │   │   │   └── config/RedisConfig.java
│   │   │   └── resources/application.properties
│   └── pom.xml
│   └── Dockerfile
├── heatmap-ui-app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/heatmapui/
│   │   │   │   ├── HeatmapUiApplication.java
│   │   │   │   ├── controller/ViewController.java
│   │   │   │   ├── controller/ApiController.java
│   │   │   │   └── service/UiService.java
│   │   │   └── resources/
│   │   │       ├── templates/index.html
│   │   │       ├── static/js/heatmap.js
│   │   │       ├── static/js/candlestick.js
│   │   │       ├── static/css/style.css
│   │   │       └── application.properties
│   └── pom.xml
│   └── Dockerfile
├── python/
│   ├── _1_load_snp500_symbol.ipynb
│   └── _2_load_ohlcv_data.ipynb
├── requirements.txt
├── python_env_setup.sh
├── docker_env_setup.sh
├── docker-compose.yml
└── .gitignore