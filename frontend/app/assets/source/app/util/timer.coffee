define [
  "jquery", "lib/util/underscore",
  "lib/util/moment", "app/util/time", "lib/chart/piechart"
], ($, _, moment, Time) ->

    class Timer

      constructor: ->

        self = @
        target = $('#timer')

        $('<li id="naviTimer" class="dropdown">
            <span class="divider">|</span>
            <a class="dropdown-toggle" data-toggle="dropdown" href="#naviTimer">
              <span>Timer</span>
              <b class="caret"></b>
            </a>
            <ul class="dropdown-menu">
              <li>
                <a href="#" data-min="90">90m</a>
              </li>
              <li>
                <a href="#" data-min="60">60m</a>
              </li>
              <li>
                <a href="#" data-min="45">45m</a>
              </li>
              <li>
                <a href="#" data-min="30">30m</a>
              </li>
              <li>
                <a href="#" data-min="15">15m</a>
              </li>
              <li>
                <a href="#" data-addmin="5">+5m</a>
              </li>
              <li>
                <a href="#" data-addmin="-5">-5m</a>
              </li>
              <li>
                <a href="#" data-min="-1000001">reset</a>
              </li>
              <li>
                <a href="#" class="toggle">toggle</a>
              </li>
            </ul>
          </li>').appendTo($("#navi>ul"))

        $("#naviTimer ul a").bind("click", (evt) ->
          min = $(this).data("min")
          if(min)
            Time.setTime("timer", min * 60)
          else
            addmin = $(this).data("addmin")
            if(addmin)
              Time.addTime("timer", addmin * 60)
            else if($(this).hasClass("toggle"))
              $(target).toggle()
          updateTimer()
          false
        )

        # init
        target.easyPieChart(
          barColor: (p) -> "#ddd" #if(p >= 95) then "#fc6b35" else "#ddd"
          trackColor: "#eee"
          scaleColor: false
          animate: false
          lineCap: 'butt'
          lineWidth: 15
          size: 30
        )

        # update manually
        updateTimer = ->

          time = Time.getTime("timer")
          ms_end = time[1]
          ms_start = time[0]

          # calculate
          if(ms_start)
            now = moment()
            end = moment(ms_end)
            start = moment(ms_start)
            total = end.diff(start, 'minutes')
            if !isNaN(total)
              togo = end.diff(now, 'minutes')
              elapsed = now.diff(start, 'minutes')
              #console.log(elapsed + "m of " + total + "m elapsed, " + togo + "m to go")
              percent = Math.min(100, Math.max(1, 100 * elapsed / total))

          # update pie and label
          $(target).data("easyPieChart").update(percent ? 100)
          $(target).find("div").text(togo ? "")
          if(togo && togo == 0)
            $(target).addClass("zero")
          else if(togo && togo < 0)
            $(target).addClass("over")
          else
            $(target).removeClass("over")
          true

        # update every 10s
        Time.updateClockSchedule(updateTimer, 10000)