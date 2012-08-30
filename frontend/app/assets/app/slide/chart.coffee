define [
  "jquery", "lib/util/underscore",  "lib/chart/d3"
], ($, _, d3) ->

  class Chart

    constructor: (id) ->

      pad = 5
      target = "#chart-" + id
      svg = d3.select(target).append("svg:svg")

      updateChart = (init) ->
        status = $("#progress-" + id + " .btn").hasClass("btn-success")

        if (!status || !init)
          svg.selectAll(".bar.success").remove()
          svg.selectAll(".bar.fail").remove()

        if (status)
          $.get('/app/exercises/' + id, (data) ->

            # find width / height (WEIRD: height() did not work properly)
            width = 280
            height = 150
            svg.attr("width", width).attr("height", height)

            # find max value
            max = _.max(_.map(data, (v) -> v.pending + v.success + v.fail + v.skip + v.error))

            # sort data
            dataset = _(_(data).sortBy((v) -> v.user)).map((v) -> {"success": v.success, "error": v.error + v.fail})

            console.log(data)
            console.log(dataset)

            # render
            w_bar = Math.max(pad, width / dataset.length)
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

      # on/off switch
      $("#progress-" + id + " .btn").click((evt) ->
          target = $(evt.target)
          if($(target).hasClass("btn-success"))
            $(target).removeClass("btn-success").addClass("btn-danger").text("Off")
          else
            $(target).removeClass("btn-danger").addClass("btn-success").text("On")
            updateChart(true)
      )