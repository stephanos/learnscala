initSlides = ->
  subtleToolbar();

  # init navi
  naviElem = $("#naviSlides")
  $(naviElem).addClass("dropdown")
  naviElemLink = $(naviElem).find("a")
  naviElemLink.replaceWith(
    '<a class="dropdown-toggle" data-toggle="dropdown" href="#naviSlides">
        <span>Slides</span>
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

  # init docs
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

  # init snippets
  $('.slides div.snippet').each(
    (idx, elem) -> initSnippet(elem)
  )

  # hook-up modal editor
  $("#navi a.openEditor").bind("click",
    (evt) ->
      initModalEditor()
      evt.preventDefault()
  )

logSlide = (id) ->
  now = new Date()

  initFS = (fs) ->
    window.fs = fs

  errorFS = (where) ->
    (err) ->
      msg = 'An error occured: '
      switch (err.code)
        when FileError.NOT_FOUND_ERR
          msg += 'File or directory not found'
        when FileError.NOT_READABLE_ERR
          msg += 'File or directory not readable'
        when FileError.PATH_EXISTS_ERR
          msg += 'File or directory already exists'
        when FileError.TYPE_MISMATCH_ERR
          msg += 'Invalid filetype';
        else
          msg += 'Unknown Error';
      msg += " (" + where + ")"
      console.log(msg)

  if(!window.fs)
    try
      window.webkitStorageInfo.requestQuota(PERSISTENT, 5*1024*1024, (grantedBytes) ->
        window.fs = window.webkitRequestFileSystem(window.PERSISTENT, grantedBytes, initFS, errorFS("init"))
      , errorFS("quota"))
    catch error
      console.log("log file not supported")

  if(window.fs)
    fname = (now.getMonth()+1) + "m." +  now.getDate() + 'd.slides.txt'
    console.log("writing to file " + fname)
    fs.root.getFile(fname, {create: true},
      (fileEntry) ->
        fileEntry.createWriter((fileWriter) ->
          fileWriter.seek(fileWriter.length)
          fileWriter.write(new Blob([id + "," + now.getTime() + "\n"], { "type" : "text\/plain" }))
        , errorFS("write"));
    , errorFS("get"))