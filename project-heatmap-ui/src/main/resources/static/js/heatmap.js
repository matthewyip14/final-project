function createColorScale() {
    return d3.scaleLinear()
        .domain([-5, -1, 0, 1, 5])
        .range(["#8b0000", "#b22222", "#f0f0f0", "#228b22", "#006400"]);
}

function drawTiles(svg, leaves, color) {
    svg.selectAll("rect")
        .data(leaves)
        .enter().append("rect")
        .attr("x", d => d.x0)
        .attr("y", d => d.y0)
        .attr("width", d => d.x1 - d.x0)
        .attr("height", d => d.y1 - d.y0)
        .attr("fill", d => color(d.data.dp))
        .on("click", d => showCandlestick(d.data.name));
}

function drawLabels(svg, leaves) {
    const formatChange = d3.format("+.2f");
    const groups = svg.selectAll("text")
        .data(leaves)
        .enter()
        .append("g")
        .attr("transform", d => `translate(${(d.x0 + d.x1) / 2}, ${(d.y0 + d.y1) / 2})`)
        .attr("text-anchor", "middle")
        .attr("dominant-baseline", "middle");

    groups.each(function(d) {
        const group = d3.select(this);
        const tileWidth = d.x1 - d.x0;
        const tileHeight = d.y1 - d.y0;
        const base = Math.min(tileWidth, tileHeight);
        const symbolSize = Math.max(10, Math.min(22, base * 0.14));
        const changeSize = Math.max(9, Math.min(18, base * 0.11));

        group.append("text")
            .attr("class", "tile-label")
            .attr("y", -4)
            .attr("font-size", symbolSize + "px")
            .text(d.data.name);
        group.append("text")
            .attr("class", "tile-label")
            .attr("y", 12)
            .attr("font-size", changeSize + "px")
            .text(`${formatChange(d.data.dp)}%`);
    });
}

function drawGroupTitles(svg, root) {
    svg.selectAll("sector-titles")
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

    svg.selectAll("industry-titles")
        .data(root.descendants().filter(d => d.depth === 2))
        .enter().append("text")
        .attr("x", d => d.x0 + 4)
        .attr("y", d => d.y0 + 14)
        .text(d => d.data.name)
        .attr("font-size", "12px")
        .attr("font-weight", "600")
        .attr("fill", "#f3f6f9")
        .attr("paint-order", "stroke")
        .attr("stroke", "#0a1a2b")
        .attr("stroke-width", 1.5);
}

function renderHeatmap(data) {
    if (!data.children || data.children.length === 0) {
        d3.select("#heatmap").html("<p>No data available. Check logs or market status.</p>");
        return;
    }
    d3.select("#heatmap").html("");
    const width = 1000, height = 600;
    const svg = d3.select("#heatmap").append("svg").attr("width", width).attr("height", height);
    const root = d3.hierarchy(data).sum(d => d.value);
    const treemap = d3.treemap().size([width, height])(root);

    const color = createColorScale();
    drawTiles(svg, treemap.leaves(), color);
    drawLabels(svg, treemap.leaves());
    drawGroupTitles(svg, root);
}

function refreshHeatmap() {
    fetch('/data/heatmap')
        .then(r => r.json())
        .then(renderHeatmap)
        .catch(error => console.error('Fetch error:', error));
}

setInterval(refreshHeatmap, 30000);
refreshHeatmap();