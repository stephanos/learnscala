define [
  "jquery"
], ($) ->

  class Progress

    init: () ->
      $(".slidedecks .status").each(
        (idx, input) =>
          # bind click
          $(input).click(@update)

          # read status
          key = @getKey(input)
          if(localStorage[key])
            @set(input, true)
      )

    reset: () ->
      if(confirm('Reset progress - are you sure?'))

        # remove keys
        for key of localStorage
          if(_.str.contains(key, "progress."))
            localStorage.removeItem(key)

        # reload
        document.location.reload()

    update: (evt) ->
      if($(this).is(':checked'))
        window.Progress.set($(this), true)
      else
        window.Progress.set($(this), undefined)

    set: (input, val) ->
      key = window.Progress.getKey(input)
      li = $(input).parent().parent()

      # reset
      $(li).removeClass("done")
      localStorage.removeItem(key)
      $(input).attr("checked", false)

      # set ?
      if(val)
        $(li).addClass("done")
        @write(key, true)
        $(input).attr("checked", true)

    write: (key, val) ->
      console.log("[STORAGE] '" + key + "' = " + val)
      localStorage[key] = val

    getKey: (input) ->
      "progress." + $(input).data("key")

  p = new Progress
  window.Progress = p
  p