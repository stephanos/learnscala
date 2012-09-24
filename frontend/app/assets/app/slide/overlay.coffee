define [
  "jquery", "lib/util/underscore", "lib/util/fokus"
], ($, _, d3) ->

  class Overlay

    constructor: () ->
      # setup canvas
      canvas = document.getElementById("board")
      canvas.width = window.innerWidth
      canvas.height = window.innerHeight

      # setup context
      context = canvas.getContext("2d")
      context.lineWidth = 4
      context.lineCap = "round"
      context.strokeStyle = "red"

      drawLine = (mouseEvent, canvas, context) ->
        canvas.style.cursor = "pointer"
        position = getPosition(mouseEvent, canvas)
        context.lineTo(position.X, position.Y)
        context.stroke()

      finishDrawing = (mouseEvent, canvas, context) ->
        drawLine(mouseEvent, canvas, context)
        context.closePath()
        $(canvas).unbind("mousemove").unbind("mouseup").unbind("mouseout")

      getPosition = (mouseEvent, canvas) ->
        if (mouseEvent.pageX && mouseEvent.pageY)
          x = mouseEvent.pageX
          y = mouseEvent.pageY
        else
          x = mouseEvent.clientX + document.body.scrollLeft + document.documentElement.scrollLeft
          y = mouseEvent.clientY + document.body.scrollTop + document.documentElement.scrollTop

        { X: x - canvas.offsetLeft, Y: y - canvas.offsetTop }

      $("#board").mousedown((mouseEvent) ->
        position = getPosition(mouseEvent, canvas)
        console.log(position)
        context.moveTo(position.X, position.Y)
        context.beginPath()

        # attach event handlers
        $(this).mousemove((mouseEvent) ->
          drawLine(mouseEvent, canvas, context)
        ).mouseup((mouseEvent) ->
          finishDrawing(mouseEvent, canvas, context)
        ).mouseout((mouseEvent) ->
          finishDrawing(mouseEvent, canvas, context)
        )
      )