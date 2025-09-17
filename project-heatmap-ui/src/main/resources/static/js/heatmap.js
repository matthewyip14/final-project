function createColorScale() {
    // RdYlGn inspired, clamped between -5% and +5%
    return d3.scaleLinear()
        .domain([-5, -2.5, 0, 2.5, 5])
        .range(["#7f0000", "#d7301f", "#f7f7f7", "#1a9850", "#00441b"]) // deeper ends
        .clamp(true);
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

function drawLegend(svg) {
    const legendWidth = 200, legendHeight = 10;
    const x = 780, y = 560; // bottom-right corner within 1000x600
    const color = createColorScale();
    const gradientId = "legend-gradient";

    const defs = svg.append("defs");
    const gradient = defs.append("linearGradient").attr("id", gradientId).attr("x1", "0%").attr("x2", "100%");
    const stops = [-5, -2.5, 0, 2.5, 5];
    gradient.selectAll("stop")
        .data(stops)
        .enter().append("stop")
        .attr("offset", d => ((d + 5) / 10) * 100 + "%")
        .attr("stop-color", d => color(d));

    svg.append("rect")
        .attr("x", x).attr("y", y)
        .attr("width", legendWidth).attr("height", legendHeight)
        .attr("fill", `url(#${gradientId})`).attr("stroke", "#333").attr("stroke-width", 0.5);

    const scale = d3.scaleLinear().domain([-5, 5]).range([x, x + legendWidth]);
    const axis = d3.axisBottom(scale).ticks(5).tickFormat(d => `${d}%`).tickSize(3);
    svg.append("g").attr("transform", `translate(0, ${y + legendHeight})`).call(axis)
        .selectAll("text").attr("fill", "#e6edf3");
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
    drawLegend(svg);
}

function refreshHeatmap() {
    fetch('/data/heatmap')
        .then(r => r.json())
        .then(renderHeatmap)
        .catch(error => console.error('Fetch error:', error));
}

setInterval(refreshHeatmap, 30000);
refreshHeatmap();