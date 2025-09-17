function refreshHeatmap() {
    fetch('/data/heatmap')
        .then(response => response.json())
        .then(data => {
            d3.select("#heatmap").html("");
            const width = 1000, height = 600;
            const svg = d3.select("#heatmap").append("svg").attr("width", width).attr("height", height);
            const root = d3.hierarchy(data).sum(d => d.value); // sum on value (volume for sector, marketCap for stock)
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
                .on("click", d => showCandlestick(d.data.name)); // name is symbol

            svg.selectAll("text")
                .data(treemap.leaves())
                .enter().append("text")
                .attr("x", d => d.x0 + 5)
                .attr("y", d => d.y0 + 20)
                .text(d => d.data.name + " (" + d.data.pc + ")"); // add last close price

            // Sector labels for parents
            svg.selectAll("titles")
                .data(root.descendants().filter(d => d.depth === 1))
                .enter().append("text")
                .attr("x", d => d.x0 + 5)
                .attr("y", d => d.y0 + 5)
                .text(d => d.data.name)
                .attr("font-size", "12px")
                .attr("fill", "white");
        });
}