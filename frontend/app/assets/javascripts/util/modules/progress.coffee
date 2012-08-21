resetProgress = () ->
  if(confirm('Reset progress - are you sure?'))

    # remove keys
    for key of localStorage
      if(_.str.contains(key, "progress."))
        localStorage.removeItem(key)

    # reload
    document.location.reload()


initProgress = () ->
  $(".slidedecks .status").each(
    (idx, input) ->
      # bind click
      $(input).click(updateProgress)

      # read status
      key = getProgressKey(input)
      if(localStorage[key])
        setProgress(input, true)
  )


updateProgress = (evt) ->
  if($(this).is(':checked'))
    setProgress($(this), true)
  else
    setProgress($(this), undefined)


setProgress = (input, val) ->
  key = getProgressKey(input)
  li = $(input).parent().parent()

  # reset
  $(li).removeClass("done")
  localStorage.removeItem(key)
  $(input).attr("checked", false)

  # set ?
  if(val)
    $(li).addClass("done")
    localStorage[key] = true
    $(input).attr("checked", true)



getProgressKey = (input) ->
  "progress." + $(input).data("key")