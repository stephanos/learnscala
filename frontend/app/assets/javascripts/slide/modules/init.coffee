initSlides = ->
  # toolbar
  subtleToolbar()
  embedEditor()
  embedDocs()
  embedStyle()
  embedNavi()

  # change li order
  $("#naviLogout").remove()
  li = $("#naviSlides")
  li.parent().append(li)
  li.find(".divider").remove()

  # slide
  initCountdowns()
  initSnippets()
  initTimer()


#######################################################################################################################
embedNavi = ->
  # init navi
  $('<li id="naviGoTo" class="dropdown">
       <a class="dropdown-toggle" data-toggle="dropdown" href="#naviGoTo">
         <span>Go To</span>
         <b class="caret"></b>
       </a>
       <span class="divider">|</span>
       <ul class="dropdown-menu"></ul>
     </li>').insertAfter($("#naviSlides"))

  titles = []
  subtitles = []
  naviElemLinks = $("#naviGoTo").find("ul")
  $(".reveal section").each(
    (idx, elem) ->
      title = $(elem).data("title")
      subtitle = $(elem).data("subtitle")

      if(title && !_.include(titles, title))
        titles.push(title)
        $(naviElemLinks).append('
          <li>
            <a href="#/' + idx + '">' + title + '</a>
            <ul class="dropdown-menu sub-menu"></ul>
          </li>
        ')

      if(subtitle && !_.include(subtitles, subtitle))
        subtitles.push(subtitle)
        $(naviElemLinks).append('
          <li>
            <a href="#/' + idx + '"> - ' + subtitle + '</a>
            <ul class="dropdown-menu sub-menu"></ul>
          </li>
        ')

        #$(naviElemLinks).find(">li").last().find("ul")
        #  .append('<li><a href="#/' + idx + '">' + subtitle + '</a></li>')
  )


#######################################################################################################################
embedEditor = ->
  $("#navi a.openEditor").bind("click",
    (evt) ->
      initModalEditor()
      evt.preventDefault()
  )


#######################################################################################################################
initSnippets = ->
  $('.slides div.snippet').each(
    (idx, elem) -> initSnippet(elem)
  )


#######################################################################################################################
embedStyle = ->
  # insert in navi
  $('<li id="naviStyle">
      <a href="#" class="openStyle">
          <span>Style</span>
      </a>
      <span class="divider">|</span>
    </li>').insertAfter($("#naviDocs"))

  # bind click event
  $("#naviStyle").bind("click", () ->
    m = $("#styleModal")
    m.bind("shown", (evt) ->
      mbody = $(m).find(".modal-body")
      # TODO
    )
    m.modal()
    false
  )


#######################################################################################################################
embedDocs = ->
  # insert in navi
  $('<li id="naviDocs">
        <a href="#" class="openDocs">
            <span>Docs</span>
        </a>
        <span class="divider">|</span>
    </li>').insertAfter($("#naviEditor"))

  # bind click event
  $("#naviDocs").bind("click", () ->
    m = $("#docsModal")
    m.bind("shown", (evt) ->
      mbody = $(m).find(".modal-body")
      if($(mbody).is(":empty"))
        $(mbody).html('
          <iframe src="http://www.scala-lang.org/api/current/index.html" width="99%" height="99%"
                  style="position: absolute; top: 5px; bottom: 5px; left: 5px; right: 5px; overflow: hidden"></iframe>
        ')
    )
    m.modal()
    false
  )


#######################################################################################################################
initCountdowns = ->
  $('div.countdown').each(
    (idx, elem) ->
      key = "countdown_" + idx

      upd = ->
        time = getTime(key)
        ms_end = time[1]
        ms_start = time[0]
        if(ms_start)
          now = moment()
          end = moment(ms_end)
          state = $(elem).data("state")
          diff = if(state == "running") then end.diff(now, 'seconds') else $(elem).data("start")
          if(diff >= 0)
            $(elem).data("start", diff)
            $(elem).find(".mm").text(Math.floor(diff / 60))
            mm = diff % 60
            console.log("diff: " + diff)
            $(elem).find(".ss").text((if(mm < 10) then "0" else "") + mm)
            state == "running"
          else
            $(elem).data("active", "false")
            false
        else
          false

      $(elem).find(".minus").bind("click", () ->
        $(elem).data("start", $(elem).data("start") - 60)
        addTime(key, -60)
        upd()
      )

      $(elem).find(".plus").bind("click", () ->
        $(elem).data("start", $(elem).data("start") + 60)
        addTime(key, 60)
        upd()
      )

      $(elem).find(".toggle").bind("click", () ->
        state = $(elem).data("state")
        if(state == "running")
          $(elem).data("state", "stopped")
        else
          setTime(key, $(elem).data("start"))
          $(elem).data("state", "running")
          updateClockSchedule(upd, 500)
      )
  )


#######################################################################################################################
initTimer = ->
  target = $('#timer')

  $('<li class="dropdown" id="naviTimer">
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
          <a href="#" data-min="-1">reset</a>
        </li>
        <li>
          <a href="#" class="toggle">toggle</a>
        </li>
      </ul>
      <span class="divider">|</span>
    </li>').insertAfter($("#naviDocs"))

  $("#naviTimer ul a").bind("click", (evt) ->
    min = $(this).data("min")
    if(min)
      setTime("timer", min * 60)
    else
      addmin = $(this).data("addmin")
      if(addmin)
        addTime("timer", addmin * 60)
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
    time = getTime("timer")
    ms_end = time[1]
    ms_start = time[0]
  
    # calculate
    if(ms_start)
      now = moment()
      end = moment(ms_end)
      start = moment(ms_start)
  
      total = end.diff(start, 'minutes')
      togo = Math.max(0, end.diff(now, 'minutes'))
      elapsed = now.diff(start, 'minutes')
      #console.log(elapsed + "m of " + total + "m elapsed, " + togo + "m to go")
      percent = Math.min(100, Math.max(1, 100 * elapsed / total))
  
    # update pie and label
    #console.log(percent + "%")
    $(target).data("easyPieChart").update(percent || 100)
    $(target).find("div").text(togo || "")
    true

  # update every 10s
  updateClockSchedule(updateTimer, 10000)
  updateTimer()


updateClockSchedule = (upd, t) ->
  setTimeout(() ->
      if(upd())
        updateClockSchedule(upd, t)
  , t)


