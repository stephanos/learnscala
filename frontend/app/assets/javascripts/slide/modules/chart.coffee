createChart = (id) ->

  target = "#chart-" + id

  pad = 5
  width = $(target).parent().width()
  height = $(target).parent().height()

  svg = d3.select(target)
    .append("svg:svg")
    .attr("width", width)
    .attr("height", height)

  updateChart = (init) ->
    $.get('/app/exercises/' + id, (data) ->

      console.log(data)

      # find max value
      max = _.max(_.map(data, (v) -> v.pending + v.success + v.fail + v.skip + v.error))

      # sort data
      dataset = _(_(data).sortBy((v) -> v.user)).map((v) -> {"success": v.success, "error": v.error + v.fail})

      #console.log(dataset)

      # init
      if(!init)
        svg.selectAll(".bar.success").remove()
        svg.selectAll(".bar.fail").remove()

      # render
      w_bar = width / dataset.length
      getHeight = (d) -> height * (d / max)

      svg.selectAll(".bar.success")
        .data(dataset)
        .enter()
        .append("rect")
        .attr("class", "bar success")
        .attr("x", (d, i) -> i * w_bar)
        .attr("y", (d) ->	height - getHeight(d.success))
        .attr("width", w_bar - pad)
        .attr("height", (d) -> getHeight(d.success))

      svg.selectAll(".bar.fail")
        .data(dataset)
        .enter()
        .append("rect")
        .attr("class", "bar fail")
        .attr("x", (d, i) -> i * w_bar)
        .attr("y", (d) ->	Math.max(0, height - getHeight(d.error) - getHeight(d.success)))
        .attr("width", w_bar - pad)
        .attr("height", (d) -> getHeight(d.error))
    )

  # update (regularly)
  updateChart(true)
  setInterval((() -> updateChart()), 5 * 1000) #15