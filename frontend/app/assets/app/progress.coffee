define [
  "jquery", "lib/util/underscore"
], ($, _) ->

  class Progress

    init: () ->
      $(".slidedecks a").each(
        (idx, link) =>
          li = $(link).parent()

          # reset
          $(link).parent().tooltip("destroy")
          $(link).find("input").unbind()
          li.removeClass("req")

          # calc status
          deps = $(link).data("deps")
          if(deps)
            deps = deps.split(",")
            missing = _.reject(deps, (id) =>
              localStorage[@getKey(@getItem(id).find("input"))]
            )
            if(missing.length > 0)
              li.addClass("req")
              reqs = ""
              _.each(missing, (id) => reqs += "<li>" + @getItem(id).data("label") + "</li>")
              li.attr("title", "Voraussetzungen: <ul>" + reqs + "</ul>")
              $(li).tooltip(
                html: true,
                placement: "bottom",
                delay: { show: 500, hide: 100 }
              )

          # bind click
          input = $(link).find("input")
          $(input).click(@update)

          # read status
          key = @getKey(input)
          if(localStorage[key])
            @apply(input, true)
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
      window.Progress.init()

    set: (input, val) ->
      key = window.Progress.getKey(input)
      localStorage.removeItem(key)
      if(val)
        @write(key, true)
      @apply(input, val)

    apply: (input, val) ->
      li = $(input).parent().parent()

      # reset
      $(li).removeClass("done")
      $(input).attr("checked", false)

      # set ?
      if(val)
        $(li).addClass("done")
        $(input).attr("checked", true)

    write: (key, val) ->
      console.log("[STORAGE] '" + key + "' = " + val)
      localStorage[key] = val

    getKey: (input) ->
      "progress." + $(input).data("key")

  p = new Progress
  window.Progress = p
  p