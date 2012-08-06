initSlides = ->
  subtleToolbar()
  embedDocs()
  embedEditor()

  initSnippets()
  initBreakClock()


#######################################################################################################################
initNavi = ->
  # init navi
  naviElem = $("#naviSlides")
  $(naviElem).addClass("dropdown")
  naviElemLink = $(naviElem).find("a")
  naviElemLink.replaceWith(
    '<a class="dropdown-toggle" data-toggle="dropdown" href="#naviSlides">
        <span>Go To</span>
        <b class="caret"></b>
     </a>
     <ul class="dropdown-menu">'
  )
  naviElemLinks = naviElem.find("ul")
  $(".reveal section").each(
    (idx, elem) ->
      title = $(elem).data("title")
      chapter = $(elem).data("chapter")

      if(chapter)
        $(naviElemLinks).append('
          <li>
            <a href="#/' + idx + '">' + chapter + '</a>
            <ul class="dropdown-menu sub-menu"></ul>
          </li>
        ')

      if(title)
        $(naviElemLinks).find(">li").last().find("ul")
          .append('<li><a href="#/' + idx + '">' + title + '</a></li>')
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
embedDocs = ->
  $('<li id="naviDocs">
        <a href="#" class="openDocs">
            <span>Docs</span>
        </a>
        <span class="divider">|</span>
    </li>').insertAfter($("#naviEditor"))
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
initBreakClock = ->
  target = $('#breakclock')

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
      </ul>
      <span class="divider">|</span>
    </li>').insertAfter($("#naviEditor"))

  $("#naviTimer ul a").bind("click", (evt) ->
    min = $(this).data("min")
    if(min)
      setTime("breaktime", min)
    else
      addmin = $(this).data("addmin")
      if(addmin)
        addTime("breaktime", addmin)
    updateBreakClock()
    false
  )

  # init
  target.easyPieChart(
    barColor: (p) -> if(p >= 95) then "#fc6b35" else "#ddd"
    trackColor: "#eee"
    scaleColor: false
    animate: false
    lineCap: 'butt'
    lineWidth: 15
    size: 30
  )

  # update manually
  updateBreakClock = ->
    updateClock(target, "breaktime")

  # update every 10s
  updateClockSchedule = (once) ->
    setTimeout(() ->
      updateBreakClock()
      updateClockSchedule()
    , 10000)
  updateClockSchedule()
  updateBreakClock()


updateClock = (target, key) ->
  time = getTime(key)
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
    console.log(elapsed + "m of " + total + "m elapsed, " + togo + "m to go")
    percent = 100 * elapsed / total

  # update pie and label
  console.log(percent)
  $(target).data("easyPieChart").update(percent || 100)
  $(target).find("div").text(togo || "")


