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
        btn = $("<button class='btn btn-mini'>Load</button>").appendTo($(toolbar))
        $(btn).bind("click",
          (evt) ->
            # find block (pre)
            pre = $(evt.target).closest("pre")
            # read snippet content
            blocks = readRawCode($(pre), true)
            # trigger modal
            initModalEditor(blocks)
        )
  )

createCodeBlock = (block, elem, status, clear, spin) ->
  if(_.isString(block))
    num = ""
    type = ""
    text = block
  else
    num = block["num"]
    type = block["type"]
    text = block["text"]

  noText = _.str.isBlank(text)

  if(clear == true || noText)
    $(elem).empty()

  if(status)
    $(elem).parent().removeClass("success error").addClass(status)

  if(!noText)
    code = $("<pre/>", {'class': "cm-s-ambiance " + type, "data-num": num}).appendTo($(elem))
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
initModalEditor = (blocks, m) ->

  # setup modal
  m ?= $('#ideModal')
  body = m.find(".modal-body")
  body.find(".pane>div").empty()

  # editor (with console)
  editor = initEditor($(body), blocks)

  # show modal
  m.modal()

  # adapt editor to modal
  editor.refresh()


# initialize a code editor
initEditor = (elem, blocks) ->

  input = $(elem).find(".input-wrap div")
  output = $(elem).find(".output-wrap div")

  # content
  content =
    _.str.strip(_.reduce(blocks ? readRawCode(elem),
      (r, b) -> r += b["text"] + "\n"
    , ""))

  # create the editor
  textarea = $("<textarea/>").appendTo($(input))
  textarea.val(content)
  editor =
    CodeMirror.fromTextArea(textarea[0], {
      autofocus: true,
      theme: "ambiance",
      mode: "text/x-scala",
      indentWithTabs: false,
      #lineWrapping: true,
      smartIndent: false,
      indentUnit: 3,
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

  editor


# call server API
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
readRawCode = (elem, editable) ->
  if($(elem)[0].tagName.toLowerCase() == "pre") # find and read previous blocks
    split = $(elem).closest(".codesplit")
    if(split.length > 0)
      readRawCode(split, editable)
    else
      num = $(elem).data("num")
      _.filter(readRawCode($(elem).closest('div.snippet'), editable),
        (b) -> isCodeBlock(b) && b["num"] <= num
      )
  else
    if($(elem).hasClass("raw-block"))
      [readRawBlock(elem, editable)] # read a single block
    else
      readRawBlocks(elem, editable) # read a bunch of blocks

readRawBlocks = (elem, editable) ->
  blocks = []
  $(elem).find(".raw-block").each(
    (idx, blck) -> # iterate through blocks
      blocks[idx] = readRawBlock(blck, editable)
      blocks[idx]["num"] = idx
  )
  blocks

readRawBlock = (elem, editable) ->
  content = ""
  type = $(elem).data("type")

  # read snippet's include content
  content += readRawBlockRef($(elem).data("include"))

  # read snippet's reference content
  if(editable)
    content += readRawBlockRef($(elem).data("reference"))

  # extract text
  lines = _.str.lines(_.str.trim($(elem).text()))
  _.forEach(lines,
    (l, i) ->
      # append newline
      if(i != 0) then content += "\n"
      # mark call scripts with a '> '
      if(_.str.contains(type, "call") && editable != true) then content += "> "
      # append content right from '|'
      content += _.str.strRight(l, "|")
  )

  {
    num: 0
    type: type
    text: content
  }

readRawBlockRef = (id) ->
  content = ""
  if(id)
    blocks = readRawCode($("#" + id))
    if(!_.isEmpty(blocks))
      _.forEach(blocks, (b) -> if(isCodeBlock(b)) then content += b.text + "\n")
  content

isCodeBlock = (b) ->
  !_.str.contains(b["type"], "output")

#######################################################################################################################
# init snippets on page load
initSlides = ->
  subtleToolbar();

  $('.slides div.snippet').each(
    (idx, elem) -> initSnippet(elem)
  )

  # init dynamic editors
  #$('.slides div.ide').each(
  #  (idx, elem) -> initEditor(elem)
  #)

  # hook-up modal editor
  $("#navi a.openEditor").bind("click",
    (evt) ->
      initModalEditor()
      evt.preventDefault()
  )

subtleToolbar = ->
  $("#navi").addClass("subtle")