setTime = (key, min) ->
  if(min <= 0)
    localStorage[key + ".start"] = undefined
    localStorage[key + ".end"] = undefined
  else
    localStorage[key + ".start"] = moment().toDate()
    localStorage[key + ".end"] = moment().add('minutes', min).toDate()


addTime = (key, min) ->
  end = localStorage[key + ".end"]
  if(end)
    localStorage[key + ".end"] = moment(end).add('minutes', min).toDate()


getTime = (key) ->
  [localStorage[key + ".start"], localStorage[key + ".end"]]