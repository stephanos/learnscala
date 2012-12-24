define [
  "jquery", "lib/util/underscore"
], ($, _, d3) ->

  class Overlay

    constructor: ->
      PADDING = 5

      # setup canvas
      canvas = document.createElement('canvas')
      canvas.id = "overlay"
      canvas.width = window.innerWidth
      canvas.height = window.innerHeight
      document.body.appendChild(canvas)

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

      clear = ->
        context.clearRect( 0, 0, canvas.width, canvas.height )

      $(document).mousedown (evt) ->
        if !$("#ideModal").is(':visible')
          if evt.which == 1 && (evt.shiftKey || evt.ctrlKey || evt.altKey || evt.metaKey)
            window.start_x = evt.pageX
            window.start_y = evt.pageY
          else
            clear()

      #$(document).dbclick((evt) ->
      #  target = $(evt.target)
      #)

      #$(document).bind("contextmenu", (evt) ->
      #    evt.preventDefault()
      #    return false
      #)

      $(document).mouseup((evt) ->
        if window.start_x && window.start_y
          window.end_x = evt.pageX
          window.end_y = evt.pageY

          if window.start_x != window.end_x && window.end_y != window.start_y
            # reset to a solid overlay
            clear()
            context.fillStyle = 'rgba( 0, 0, 0, 0.5)'
            context.fillRect( 0, 0, canvas.width, canvas.height )

            # cut out selection
            context.clearRect(
              window.start_x - window.scrollX - PADDING,
              window.start_y - window.scrollY - PADDING,
              ( window.end_x - window.start_x ) + ( PADDING * 2 ),
              ( window.end_y - window.start_y ) + ( PADDING * 2 )
            )

            window.start_x = null
            window.start_y = null
      )

      ###
      $("#overlay").mousedown((mouseEvent) ->
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
      ###