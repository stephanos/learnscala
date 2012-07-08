#######################################################################################################################
# create a source code snippet
initSnippet = (elem) ->

  # extract code
  content = readRawCode(elem)

  # create editor
  createSnippet(content, elem)

  # add
  if(!$(elem).hasClass("console"))
    toolbar = $('<div class="toolbar">').appendTo($(elem))
    btn = $("<button class='btn btn-large '>Load</button>").appendTo($(toolbar))
    $(btn).bind("click",
      (evt) ->
        snippet = $(evt.target).parent().parent()
        content = readRawCode(snippet)
        initModalEditor(content)
    )

createSnippet = (data, elem, status, clear, spin) ->
  noData = _.str.isBlank(data)

  if(clear == true || noData)
    $(elem).empty()

  if(status)
    $(elem).parent().removeClass("success error").addClass(status)

  if(!noData)
    code = $("<pre/>", {'class': "pretty cm-s-ambiance "}).appendTo($(elem))
    CodeMirror.runMode(data, "text/x-scala", code[0])

  if(status == "wait")
    opts = {
      lines: 13, length: 7, width: 4, radius: 10, rotate: 0,
      color: '#000', speed: 1, trail: 60, shadow: false,  hwaccel: false,
      className: 'spinner', zIndex: 2e9,
    }
    window.spinner = new Spinner(opts).spin($(elem).parent()[0])


#######################################################################################################################
# initialize a code editor in a modal
initModalEditor = (data, m) ->

  # setup modal
  m ?= $('#ideModal')
  body = m.find(".modal-body")
  body.find(".pane>div").empty()

  # editor (with console)
  initEditor($(body), data)

  # show modal
  m.modal()


# initialize a code editor
initEditor = (elem, data) ->

  input = $(elem).find(".input-wrap div")
  output = $(elem).find(".output-wrap div")

  # create the editor
  textarea = $("<textarea/>").appendTo($(input))
  textarea.val(data ? readRawCode(elem))
  editor =
    CodeMirror.fromTextArea(textarea[0], {
      autofocus: true,
      theme: "ambiance",
      mode: "text/x-scala",
      indentWithTabs: true,
      smartIndent: false,
      indentUnit: 4,
      matchBrackets: true,
      autoClearEmptyLines: true
    })

  # add editor toolbar
  editDom = editor.getWrapperElement()
  toolbar = $('<div class="toolbar">').appendTo($(editDom))
  $('<button class="btn btn-success">Execute</button>')
    .bind("click", (evt) -> callAPI("interpret", editor, output))
    .appendTo($(toolbar))

  $('<button class="btn">Inspect</button>')
    .bind("click", (evt) -> callAPI("decompile", editor, output))
    .appendTo($(toolbar))

callAPI = (target, editor, output) ->
  # clear console
  createSnippet("", output, "wait")

  # issue call
  $.ajax(
    type: 'POST',
    timeout: 15000,
    url: "/api/" + target,
    data: "code=" + encodeURIComponent(editor.getValue()),
    success: (data, status) ->
      window.spinner?.stop()
      text = if(_.str.isBlank(data)) then "compiled and executed successfully" else data
      createSnippet(text, output, "success", true)
      console.log(data)
    error: (jqXHR, status, err) ->
      window.spinner?.stop()
      data = jqXHR.responseText
      text =
        if(status == "timeout") then "request timed out"
        else if(_.str.isBlank(data)) then "an unkown error occurred"
        else data
      createSnippet(text, output, "error", true)
      console.log(text)
      console.log(status)
      console.log(err)
  );

#######################################################################################################################
# raw source code data from snippet
readRawCode = (elem) ->

  # extract code
  raw = $(elem).find(".raw")
  lines = _.str.lines(_.str.trim($(raw).text()))
  $(raw).hide()

  # format code
  content = ""
  _.forEach(lines,
    (l, i) ->
      if(i != 0) then content += "\n"
      content += _.str.strRight(l, "|")
  )

  content


#######################################################################################################################
# init snippets on page load
initSlides = ->
  subtleToolbar();

  # init static snippets
  $('.slides div.snippet, .slides div.console').each(
    (idx, elem) -> initSnippet(elem)
  )

  # init dynamic editors
  $('.slides div.ide').each(
    (idx, elem) -> initEditor(elem)
  )

  # hook-up modal editor
  $(".slides a.openEditor").bind("click",
    (evt) ->
      initModalEditor()
      evt.preventDefault()
  )

subtleToolbar = ->
  $("#navi").addClass("subtle")