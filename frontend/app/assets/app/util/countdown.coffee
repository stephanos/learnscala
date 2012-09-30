define [
  "jquery", "lib/util/underscore",
  "lib/util/moment", "app/util/time"
], ($, _, moment, Time) ->

    class Countdown

      constructor: ->

        self = @

        # insert in navi
        $('<li id="naviCountdown">
            <span class="divider">|</span>
            <a href="#" class="openCountdown">
                <span>Countdown</span>
            </a>
          </li>').appendTo($("#navi>ul"))

        # bind click event
        $("#naviCountdown").bind("click", () ->
          m = $("#countdownModal")
          m.bind("shown", (evt) ->
            mbody = $(m).find(".modal-body")
            # TODO
          )
          m.modal()
          false
        )

        # others
        $('div.countdown').each(
          (idx, elem) ->
            key = "countdown_" + idx

            upd = ->
              time = Time.getTime(key)
              ms_end = time[1]
              ms_start = time[0]
              if(ms_start)
                now = moment()
                end = moment(ms_end)
                state = $(elem).data("state")
                diff = if(state == "running") then end.diff(now, 'seconds') else $(elem).data("start")
                $(elem).data("start", diff)
                #console.log(diff)

                if(diff < 0)
                  $(elem).addClass("over")
                else
                  $(elem).removeClass("over")

                mm = diff / 60
                mm = if(diff >= 0) then Math.floor(mm) else Math.ceil(mm)
                $(elem).find(".mm").text(mm)

                ss = Math.abs(diff % 60)
                $(elem).find(".ss").text((if(ss < 10) then "0" else "") + ss)
                state == "running"

              else
                false

            $(elem).find(".minus").bind("click", () ->
              $(elem).data("start", $(elem).data("start") - 60)
              Time.addTime(key, -60)
              upd()
            )

            $(elem).find(".plus").bind("click", () ->
              $(elem).data("start", $(elem).data("start") + 60)
              Time.addTime(key, 60)
              upd()
            )

            $(elem).find(".toggle").bind("click", () ->
              state = $(elem).data("state")
              if(state == "running")
                $(elem).data("state", "stopped")
              else
                Time.setTime(key, $(elem).data("start"))
                $(elem).data("state", "running")
                Time.updateClockSchedule(upd, 500)
            )
        )