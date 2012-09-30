define [
  "lib/util/moment"
], (moment) ->

  class Time

    setTime: (key, s) ->
      console.log("[TIMER] set '" + key + "' to " + s + " seconds")
      if(s <= -1000000)
        localStorage[key + ".start"] = null
        localStorage[key + ".end"] = null
      else
        localStorage[key + ".start"] = moment().toDate()
        localStorage[key + ".end"] = moment().add('seconds', s).toDate()

    addTime: (key, s) ->
      console.log("[TIMER] add to '" + key + "' " + s + " seconds")
      end = localStorage[key + ".end"]
      if(end)
        localStorage[key + ".end"] = moment(end).add('seconds', s).toDate()

    getTime: (key) ->
      [localStorage[key + ".start"], localStorage[key + ".end"]]

    updateClockSchedule: (upd, t) ->
      if(upd())
        setTimeout(() =>
            @updateClockSchedule(upd, t)
        , t)

  new Time