define [
  "jquery", "lib/util/underscore"
], ($, _) ->

  class Progress

    init: () ->
      $(".slidedecks a").each(
        (idx, link) =>

          # calc status
          deps = $(link).data("deps")
          if(deps)
            deps = deps.split("/")
            missing = _.filter(deps, (id) =>
              localStorage[@getKey(@getItem(id).find("input"))] != true
            )
            if(missing.length > 0)
              li = $(link).parent()
              li.addClass("req")
              reqs = ""
              _.each(missing, (id) => reqs += "<li>" + @getItem(id).data("label") + "</li>")
              li.attr("title", "Voraussetzungen: <ul>" + reqs + "</ul>")
              $(li).tooltip({
                html: true,
                placement: "bottom",
                delay: { show: 500, hide: 100 }
              })
              _.each(missing, (md) ->
              )

          # bind click
          input = $(link).find("input")
          $(input).click(@update)

          # read status
          key = @getKey(input)
          if(localStorage[key])
            @set(input, true)
      )

    getItem: (id) ->
      $("a[data-id='" + id + "']")

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