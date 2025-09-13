function showCandlestick(symbol) {
    fetch(`/data/ohlc/${symbol}`)
        .then(response => response.json())
        .then(data => {
            d3.select("#candlestick").style("display", "block").html("");
            const width = 800, height = 400;
            const svg = d3.select("#candlestick").append("svg").attr("width", width).attr("height", height);
            // 簡化 candlestick 邏輯，使用 D3 繪製 OHLC
            // 省略詳細實現，假設使用線條和矩形表示蠟燭
            // ...
        });
}