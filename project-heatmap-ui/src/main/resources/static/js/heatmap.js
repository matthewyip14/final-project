function refreshHeatmap() {
    fetch('/data/heatmap')
        .then(response => response.json())
        .then(data => {
            if (!data.children || data.children.length === 0) {
                d3.select("#heatmap").html("<p>No data available. Check logs or market status.</p>");
                return;
            }
            d3.select("#heatmap").html("");
            const width = 1000, height = 600;
            const svg = d3.select("#heatmap").append("svg").attr("width", width).attr("height", height);
            const root = d3.hierarchy(data).sum(d => d.value);
            const treemap = d3.treemap().size([width, height])(root);
            // Darken colors slightly and add midpoint for smoother gradient
            const color = d3.scaleLinear()
                .domain([-5, -1, 0, 1, 5])
                .range(["#8b0000", "#b22222", "#f0f0f0", "#228b22", "#006400"]);

            svg.selectAll("rect")
                .data(treemap.leaves())
                .enter().append("rect")
                .attr("x", d => d.x0)
                .attr("y", d => d.y0)
                .attr("width", d => d.x1 - d.x0)
                .attr("height", d => d.y1 - d.y0)
                .attr("fill", d => color(d.data.dp))
                .on("click", d => showCandlestick(d.data.name));

            // Centered labels within each tile
            const formatChange = d3.format("+.2f");
            svg.selectAll("text")
                .data(treemap.leaves())
                .enter()
                .append("g")
                .attr("transform", d => `translate(${(d.x0 + d.x1) / 2}, ${(d.y0 + d.y1) / 2})`)
                .attr("text-anchor", "middle")
                .attr("dominant-baseline", "middle")
                .each(function(d) {
                    const group = d3.select(this);
                    group.append("text")
                        .attr("class", "tile-label")
                        .attr("y", -4)
                        .text(d.data.name);
                    group.append("text")
                        .attr("class", "tile-label")
                        .attr("y", 12)
                        .text(`${formatChange(d.data.dp)}%`);
                });

            // 行業標籤（每個板塊左上角）
            svg.selectAll("titles")
                .data(root.descendants().filter(d => d.depth === 1))
                .enter().append("text")
                .attr("x", d => d.x0 + 5)
                .attr("y", d => d.y0 + 15)
                .text(d => d.data.name)
                .attr("font-size", "14px")
                .attr("font-weight", "600")
                .attr("fill", "#e6edf3")
                .attr("paint-order", "stroke")
                .attr("stroke", "#0a1a2b")
                .attr("stroke-width", 2);
        })
        .catch(error => console.error('Fetch error:', error));
}

setInterval(refreshHeatmap, 30000);
refreshHeatmap();