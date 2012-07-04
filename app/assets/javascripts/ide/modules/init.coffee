#######################################################################################################################
# create a source code snippet
initSnippet = (elem) ->

  # extract code
  content = readRawCode(elem)

  # create editor
  createSnippet(content, elem)

  # add
  toolbar = $('<div class="toolbar">').appendTo($(elem))
  btn = $("<button class='btn btn-large '>Load</button>").appendTo($(toolbar))
  $(btn).bind("click",
    (evt) ->
      snippet = $(evt.target).parent().parent()
      content = readRawCode(snippet)
      initModalEditor(content)
  )

createSnippet = (data, elem, status, clear) ->
  if(clear == true) then $(elem).empty()
  if(status) then $(elem).parent().removeClass("success error").addClass(status)
  code = $("<pre/>", {'class': "pretty cm-s-ambiance "}).appendTo($(elem))
  CodeMirror.runMode(data ? "", "text/x-scala", code[0])


#######################################################################################################################
# initialize a code editor in a modal
initModalEditor = (data, m) ->

  # setup modal
  m ?= $('#editorModal')
  body = m.find(".modal-body")
  body.find(".pane>div").empty()

  # editor (with console)
  initEditor(data ? "", $(body))

  # show modal
  m.modal()


# initialize a code editor
initEditor = (data, elem) ->

  input = $(elem).find(".input-wrap div")
  output = $(elem).find(".output-wrap div")

  # create the editor
  textarea = $("<textarea/>").appendTo($(input))
  textarea.val(data ? "")
  editor =
    CodeMirror.fromTextArea(textarea[0], {
      autofocus: false,
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
  $.ajax(
    type: 'POST',
    timeout: 15000,
    url: "/api/" + target,
    data: "code=" + encodeURIComponent(editor.getValue()),
    success: (data, status) ->
      text = if(_.str.isBlank(data)) then "compiled and executed successfully" else data
      createSnippet(text, output, "success", true)
      console.log(data)
    error: (jqXHR, status, err) ->
      data = jqXHR.responseText
      text = if(_.str.isBlank(data)) then "unable to compile" else data
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
$ ->
  $('div.snippet').each(
    (idx, elem) -> initSnippet(elem)
  )
  $("a.openEditor").bind("click",
    () ->
      initModalEditor()
  )