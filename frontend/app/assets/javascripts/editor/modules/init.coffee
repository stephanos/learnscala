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
        btn = $("<div class='btn-group'><button class='btn btn-icon btn-mini'>6</button></div>").appendTo($(pre))
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
  body.find(".input, .output").empty()

  # editors + console
  editors = initEditors($(body), blocks)

  # show modal
  m.modal()

  # adapt editor to modal
  _.forEach(editors, (e) -> e.refresh())


# initialize a code editors
initEditors = (elem, blocks) ->

  output = $(elem).find(".output-wrap div")

  # init source editor
  srcEditor = initEditor($(elem).find(".left .input"), blocks, "source")
  srcEditorDom = srcEditor.getWrapperElement()
  $('<div class="btn-group">
    <button class="btn btn-icon btn-success" id="btn-decompile-scala"></button>
    <button class="btn btn-icon btn-success" id="btn-decompile-java"></button>
    </div>').appendTo($(srcEditorDom))
  $('#btn-decompile-java').bind("click", (evt) -> callAPI("decompile/java", output, srcEditor.getValue()))
  $('#btn-decompile-scala').bind("click", (evt) -> callAPI("decompile/scala", output, srcEditor.getValue()))

  # init call editor
  callEditor = initEditor($(elem).find(".right .input"), blocks, "call")
  callEditorDom = callEditor.getWrapperElement()
  $('<div class="btn-group"><button class="btn btn-icon btn-success">1</button></div>')
        .bind("click", (evt) -> callAPI("execute", output, srcEditor.getValue(), callEditor.getValue()))
        .appendTo($(callEditorDom))

  [srcEditor, callEditor]


# initialize a code editor
initEditor = (elem, blocks, typeOf) ->

  # content
  console.log(typeOf)
  console.log(blocks)
  getBlocks = (bs) ->
    res = []
    _.forEach(bs,
      (b) ->
        subs = b["subs"]
        if(!_.isEmpty(subs)) then res.push(getBlocks(subs))
        if(_.str.contains(b["type"], typeOf)) then res.push(b)
    )
    _.flatten(res)
  blocks = getBlocks(blocks)
  console.log(blocks)

  content =
    _.str.strip(_.reduce(blocks ? readRawCode(elem),
      (r, b) -> r += _.str.trim(b["text"], "\n") + "\n" + (if(typeOf == "source") then "\n" else "")
    , ""))

  # create the editor
  textarea = $("<textarea/>").appendTo($(elem))
  textarea.val(content)
  editor =
    CodeMirror.fromTextArea(textarea[0], {
      autofocus: false,
      theme: "ambiance",
      mode: "text/x-scala",
      indentWithTabs: false,
      lineWrapping: typeOf == "call",
      smartIndent: false,
      indentUnit: 3,
      lineNumbers: typeOf == "call",
      lineNumberFormatter: (n) -> ">"
      matchBrackets: true,
      autoClearEmptyLines: true
    })

  editor


# call server API
callAPI = (target, output, source, call) ->
  # clear console
  createCodeBlock("", output, "wait")

  # issue call
  $.ajax(
    type: 'POST',
    timeout: 15000,
    url: "/api/" + target,
    data: "source=" + encodeURIComponent(source ? "") + "&call=" + encodeURIComponent(call ? ""),

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
  if($(elem).length > 0)
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
  else
    []

readRawBlocks = (elem, editable) ->
  blocks = []
  $(elem).find(".raw-block").each(
    (idx, blck) -> # iterate through blocks
      blocks[idx] = readRawBlock(blck, editable)
      blocks[idx]["num"] = idx
  )
  blocks

readRawBlock = (elem, editable) ->
  subs = []
  content = ""
  type = _.str.trim($(elem).data("type"))

  # read snippet's include content
  incl = readRawCode($("#" + $(elem).data("include")))
  if(!_.isEmpty(incl)) then subs.push(incl)

  # read snippet's reference content
  if(editable)
    ref = readRawCode($("#" + $(elem).data("reference")), editable)
    if(!_.isEmpty(ref)) then subs.push(ref)

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
    subs: _.flatten(subs)
  }

#readRawBlockRef = (id) ->
#  content = ""
#  if(id)
#    blocks = readRawCode($("#" + id))
#    if(!_.isEmpty(blocks))
#      _.forEach(blocks, (b) -> if(isCodeBlock(b)) then content += b.text + "\n")
#  content

isCodeBlock = (b) ->
  !_.str.contains(b["type"], "output")

subtleToolbar = ->
  $("#navi").addClass("subtle")