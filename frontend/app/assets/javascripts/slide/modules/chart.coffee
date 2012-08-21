scheduleChartUpdate = (id) ->
  updateChart(id)
  setInterval((() -> updateChart(id)), 15 * 1000)


updateChart = (id) ->
  $.get('/app/exercises/' + id + "/status", (data) ->
      console.log(data)
  )
