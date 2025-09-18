function showCandlestick(symbol) {
    const url = `/ohlc-view/${symbol}`;
    window.open(url, '_blank');
}