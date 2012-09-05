define [
  "jquery", "lib/util/underscore",  "lib/chart/d3"
], ($, _, d3) ->

  class Chart

    constructor: (id) ->

      pad = 10
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

            # find common lengths
            users = data.length
            tasks = _.max(_.map(_.groupBy(_.map(data, (v) -> v.results.length), (v) -> v), (v, k) -> { key: k, val: v.length }), (l) -> l.val).key

            console.log(data)

            # transform and sort
            dataset =
              _.sortBy(
                _.map(data, (ov) ->
                  v = {}
                  _.extend(v, ov)

                  # adapt length
                  _l = v.results.length
                  if(_l > tasks)
                    v.results = v.results[0..tasks-1]
                  else if(_l < tasks)
                    _([0..(tasks - _l - 1)]).each((r) -> v.results.push(-1))

                  v
                ),
              (v) -> v.user)

            console.log(dataset)

            # render
            grid_size = Math.min((width - 2) / users, (height - 2) / tasks)
            grid_p_y = grid_size * 0.05
            grid_p_x = grid_p_y * 3
            r = (grid_size - 2 * grid_p_x) / 2
            grid_m_x = (width - (2 * r * users) - (grid_p_x * 2 * users)) / 2
            grid_m_y = (height - (2 * r * tasks) - (grid_p_y * 2 * tasks)) / 2

            for t in [0..tasks-1]
              do (t) ->
                c = String.fromCharCode(65 + t)
                pdata = _.map(dataset, (v) -> {user: v.user, result: v.results[t]})
                #console.log(pdata)
                svg.selectAll(".circle." + c)
                  .data(pdata)
                  .enter()
                  .append("circle")
                  .attr("class", (obj) ->
                    v = obj.result
                    col =
                      if(v == 0)
                        "success"
                      else if(v == 2)
                        "failure"
                      else
                        "pending"
                    "circle " + c + " " + col
                  )
                  .attr("r", r)
                  .attr("cx", (d, i) -> grid_m_x + 1 + r + (i * grid_p_x * 2) + (i * r * 2))
                  .attr("cy", (d, i) -> height - grid_m_y - 1 - r - (t * grid_p_y * 2) - (t * r * 2))
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