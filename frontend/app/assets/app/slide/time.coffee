define [
  "lib/util/moment"
], (moment) ->

  class Time

    setTime = (key, s) ->
      if(s <= 0)
        localStorage[key + ".start"] = undefined
        localStorage[key + ".end"] = undefined
      else
        localStorage[key + ".start"] = moment().toDate()
        localStorage[key + ".end"] = moment().add('seconds', s).toDate()


    addTime = (key, s) ->
      end = localStorage[key + ".end"]
      if(end)
        localStorage[key + ".end"] = moment(end).add('seconds', s).toDate()


    getTime = (key) ->
      [localStorage[key + ".start"], localStorage[key + ".end"]]

  new Time