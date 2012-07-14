#######################################################################################################################
initSnippet = (elem) ->
  # create code block(s)
  code = $('<div class="code">').appendTo($(elem))
  _.forEach(readRawCode(elem),
    (b) ->
      createCodeBlock(b, code)
  )

  # add "load"-buttons
  $(elem).find("pre").each(
    (idx, pre) ->
      if(!$(pre).hasClass("output"))
        toolbar = $('<div class="toolbar">').appendTo($(pre))
        btn = $("<button class='btn btn-large '>Load</button>").appendTo($(toolbar))
        $(btn).bind("click",
          (evt) ->
            # find snippet
            snippet = $(evt.target).parent().parent()
            # read snippet content
            content = readRawCode(snippet, true)
            # trigger modal
            initModalEditor(content)
        )
  )

createCodeBlock = (block, elem, status, clear, spin) ->
  type = block["type"]
  text = block["text"]
  noText = _.str.isBlank(text)

  if(clear == true || noText)
    $(elem).empty()

  if(status)
    $(elem).parent().removeClass("success error").addClass(status)

  if(!noText)
    code = $("<pre/>", {'class': "cm-s-ambiance " + type}).appendTo($(elem))
    CodeMirror.runMode(text, "text/x-scala", code[0])

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
  createCodeBlock("", output, "wait")

  # issue call
  $.ajax(
    type: 'POST',
    timeout: 15000,
    url: "/api/" + target,
    data: "code=" + encodeURIComponent(editor.getValue()),
    success: (data, status) ->
      window.spinner?.stop()
      text = if(_.str.isBlank(data)) then "compiled and executed successfully" else data
      createCodeBlock(text, output, "success", true)
      console.log(data)
    error: (jqXHR, status, err) ->
      window.spinner?.stop()
      data = jqXHR.responseText
      text =
        if(status == "timeout") then "request timed out"
        else if(_.str.isBlank(data)) then "an unkown error occurred"
        else data
      createCodeBlock(text, output, "error", true)
      console.log(text)
      console.log(status)
      console.log(err)
  );

#######################################################################################################################
# raw source code data from snippet
readRawCode = (elem, resolveRef) ->

  blocks = []

  # extract code
  raw = $(elem).find(".raw")
  $(raw).find("div").each(
    (idx, elem) ->
      content = ""
      type = $(elem).data("type")

      # read snippet include content
      include = $(elem).data("include")
      if(include) then content += readRawCode($("#" + include)) + "\n"

      # read snippet reference content
      if(resolveRef)
        reference = $(elem).data("reference")
        if(reference) then content += readRawCode($("#" + reference)) + "\n"

      # extract code
      lines = _.str.lines(_.str.trim($(elem).text()))
      _.forEach(lines,
        (l, i) ->
          if(i != 0) then content += "\n"
          if(_.str.contains(type, "call")) then content += "> "
          content += _.str.strRight(l, "|")
      )

      blocks[idx] =
        type: type
        text: content
  )

  blocks


#######################################################################################################################
# init snippets on page load
initSlides = ->
  subtleToolbar();

  $('.slides div.snippet').each(
    (idx, elem) -> initSnippet(elem)
  )

  # init dynamic editors
  $('.slides div.ide').each(
    (idx, elem) -> initEditor(elem)
  )

  # hook-up modal editor
  $("#navi a.openEditor").bind("click",
    (evt) ->
      initModalEditor()
      evt.preventDefault()
  )

subtleToolbar = ->
  $("#navi").addClass("subtle")