function refreshHeatmap() {
    fetch('/data/heatmap')
        .then(response => response.json())
        .then(data => {
            // 清空舊圖
            d3.select("#heatmap").html("");
            // 使用 D3 treemap 繪製
            const width = 1000, height = 600;
            const svg = d3.select("#heatmap").append("svg").attr("width", width).attr("height", height);
            const root = d3.hierarchy({children: data}).sum(d => d.market_cap);
            const treemap = d3.treemap().size([width, height])(root);
            const color = d3.scaleLinear().domain([-5, 0, 5]).range(["red", "white", "green"]);

            svg.selectAll("rect")
                .data(treemap.leaves())
                .enter().append("rect")
                .attr("x", d => d.x0)
                .attr("y", d => d.y0)
                .attr("width", d => d.x1 - d.x0)
                .attr("height", d => d.y1 - d.y0)
                .attr("fill", d => color(d.data.dp))
                .on("click", d => showCandlestick(d.data.symbol));

            svg.selectAll("text")
                .data(treemap.leaves())
                .enter().append("text")
                .attr("x", d => d.x0 + 5)
                .attr("y", d => d.y0 + 20)
                .text(d => d.data.symbol);
        });
}