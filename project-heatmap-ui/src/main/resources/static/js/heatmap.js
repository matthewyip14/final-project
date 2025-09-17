function refreshHeatmap() {
    fetch('/data/heatmap')
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            d3.select("#heatmap").html("");
            const width = 1000, height = 600;
            const svg = d3.select("#heatmap").append("svg").attr("width", width).attr("height", height);
            const root = d3.hierarchy({children: data}).sum(d => d.marketCap);
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
        })
        .catch(error => {
            console.error('Fetch error:', error);
            // Optional: display error message on page
            d3.select("#heatmap").html("<p>Error loading data: " + error.message + "</p>");
        });
}