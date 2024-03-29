define [
  "jquery", "lib/util/underscore.str",
  "lib/editor/codemirror", "lib/util/mousetrap",
  "lib/util/spin", "lib/dom/splitter",
  "lib/editor/clike", "lib/editor/runmode"
], ($, _, CodeMirror) ->

  class Editor
  
    initSnippet: (elem) ->
      self = @
      # create code block(s)
      code = $('<div class="code">').appendTo($(elem))
      _.forEach(self.readRawCode(elem),
        (b) ->
          self.createCodeBlock(b, code)
      )

      if($(elem).find(".raw-block").data("lang") == "java")
        $("<div class='icon-annotation'><span>7</span></div>").appendTo($(elem))

      resetHighlights = (evt) ->
        if(!evt || evt.shiftKey || evt.ctrlKey || evt.altKey || evt.metaKey)
          $(elem).find("pre").removeClass("highlight")

      $(elem).find("pre").each(
        (idx, pre) ->
            # add "mark"-buttons
            markBtn = $("<div class='btn-group btn-group-left'><button class='btn btn-icon btn-mini mark'>&nbsp;</button></div>").prependTo($(pre))
            $(markBtn).click (evt) ->
              resetHighlights(evt)
              # find block (pre)
              pre = $(evt.target).closest("pre")
              # toggle class
              $(pre).toggleClass("highlight")

            $(pre).click (evt) ->
              if(evt.shiftKey || evt.ctrlKey || evt.altKey || evt.metaKey)
                markBtn.trigger("click")

            $(pre).dblclick (evt) ->
              resetHighlights(evt)
              markBtn.trigger("click")

            if(!$(pre).hasClass("output"))
              # add "load"-buttons
              loadBtn = $("<div class='btn-group btn-group-right'><button class='btn btn-icon btn-mini load'>6</button></div>").appendTo($(pre))
              $(loadBtn).click (evt) ->
                # find block (pre)
                pre = $(evt.target).closest("pre")
                # read snippet content
                blocks = self.readRawCode($(pre), true)
                # trigger modal
                self.initModalEditor(blocks)
      )

      #$(elem).mousedown((b) ->
      #    if(b.which==2)
      #      alert("MIDDLE")
      #)
  
    createCodeBlock: (block, elem, status, clear, spin) ->
      self = @
      if(_.isString(block))
        num = ""
        type = ""
        lang = null
        text = block
        frag = true
        hlight = null
        linebyline = false
      else
        num = block["num"]
        lang = block["lang"]
        type = block["type"]
        text = block["text"]
        frag = block["frag"]
        hlights = block["hlight"]
        linebyline = block["linebyline"]

      noText = _.str.isBlank(text)
  
      if(clear == true || noText)
        $(elem).empty()
  
      if(status)
        $(elem).parent().removeClass("success error").addClass(status)

      if(!noText)
        type += " cm-s-ambiance "
        code = $("<div/>", {'class': "wrapper " + type + (if(frag) then " fragment"), "data-num": num}).appendTo($(elem))
        CodeMirror.runMode(text, "text/x-" + (if(lang == "java") then lang else "scala"), code[0], { "class": type, "num": num, "linebyline": linebyline })
        #btn = $("<div class='btn-group'><button class='btn btn-icon btn-fullscreen'></button></div>").appendTo($(code))

      if(hlights)
        for hl in hlights.toString().split(' ')
          do (hl) ->
            lines = $(code).find(".codeline")
            lines_c = lines.length
            if(hl == "first" || hl == "start") then hl = 1
            if(hl == "last" || hl == "end") then hl = lines_c
            if(_.isString(hl)) then hl = parseInt(hl, 10)
            if(_.isNumber(hl) && hl >= 1 && hl <= lines_c)
              $(lines.get(hl - 1)).addClass("highlight")
  
      if(status == "wait")
        opts = {
          lines: 13, length: 7, width: 4, radius: 10, rotate: 0,
          color: '#000', speed: 1, trail: 60, shadow: false,  hwaccel: false,
          className: 'spinner', zIndex: 2e9,
        }
        window.spinner = new window.Spinner(opts).spin($(elem).parent()[0])
  
  
    #######################################################################################################################
    # initialize a code editor in a modal
    initModalEditor: (blocks, m) ->
      self = @

      # setup modal
      m ?= $('#ideModal')
      body = m.find(".modal-body")
      body.find(".input, .output").empty()
  
      # editors + console
      editors = @initEditors($(body), blocks)
      m.bind("shown", (evt) ->
        _.forEach(editors, (e) -> e.refresh())
        self.initEditorSplitters(body)
  
        # resize editor on pane resize
        m.find(".pane, .pane div").bind("resize", () ->
          _.forEach(editors, (e) -> e.refresh())
        )
      )
  
      # show modal
      m.modal()
  
  
    # initialize a code editors
    initEditors: (elem, blocks) ->
      self = @

      output = $(elem).find(".output-wrap div")
  
      # init source editor
      srcEditor = @initEditor($(elem).find(".source .input"), blocks, "source")
      srcEditorDom = srcEditor.getWrapperElement()
      $('
        <div class="btn-group btn-group-left">
          <button class="btn btn-icon btn-changeview hide"></button>
        </div>
        <div class="btn-group btn-group-right">
          <button class="btn btn-icon btn-success" id="btn-decompile-scala">6</button>
          <button class="btn btn-icon btn-success" id="btn-decompile-java">7</button>
        </div>').appendTo($(srcEditorDom))
      $('#btn-decompile-java').bind("click", (evt) -> self.callAPI("decompile/java", output, srcEditor.getValue()))
      $('#btn-decompile-scala').bind("click", (evt) -> self.callAPI("decompile/scala", output, srcEditor.getValue()))
  
      # init call editor
      callEditor = @initEditor($(elem).find(".call .input"), blocks, "call")
      callEditorDom = callEditor.getWrapperElement()
      $('
        <div class="btn-group btn-group-left">
          <button class="btn btn-icon btn-changeview hide"></button>
        </div>
        <div class="btn-group">
            <button class="btn btn-icon btn-success" id="btn-exec">1</button>
        </div>').appendTo($(callEditorDom))
      $('#btn-exec').bind("click", (evt) -> self.callAPI("execute", output, srcEditor.getValue(), callEditor.getValue()))
      window.Mousetrap.bind('ctrl+r', () -> $('#btn-exec').trigger("click"))
  
      [srcEditor, callEditor]
  
  
    initEditorSplitters: (elem) ->
      if($(elem).find(".hsplitbar").length == 0)
        vsplit = $(elem).find(".input-wrap").splitter(outline: true, splitHorizontal: true, sizeBottom: 160, minTop: 40, minBottom: 40)
        hsplit = $(elem).find(".topbottom").splitter(outline: true, splitHorizontal: true, sizeBottom: 160, minTop: 90, minBottom: 40)
  
  
    initStandaloneEditor: (elem) ->
      @initEditors(elem)
      @initEditorSplitters(elem)
  
  
    # initialize a code editor
    initEditor: (elem, blocks, typeOf) ->
  
      # content
      #console.log(typeOf)
      #console.log(blocks)
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
      #console.log(blocks)
  
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
          onCursorActivity: () ->
            editor.setLineClass(window.hlLine, null, null)
            window.hlLine = editor.setLineClass(editor.getCursor().line, null, "activeLine")
          matchBrackets: true,
          autoClearEmptyLines: true
        })
      window.hlLine = editor.setLineClass(0, "activeLine")
  
      editor
  
  
    # call server API
    callAPI: (target, output, source, call) ->
      self = @

      # clear console
      @createCodeBlock("", output, "wait")

      uri = window.location.hostname
      apiBase =
        if(uri.indexOf("localhost") > -1)
          "http://localhost:8080"
        else
          "https://api.learnscala.de"

      # issue call
      $.support.cors = true
      $.ajax(
        type: 'POST',
        timeout: 6000,
        url: apiBase + "/api/" + target,
        data: "source=" + encodeURIComponent(source ? "") + "&call=" + encodeURIComponent(call ? ""),
  
        success: (data, status) ->
          window.spinner?.stop()
          $(".spinner").remove()
          text = if(_.str.isBlank(data)) then "compiled and executed successfully" else data
          self.createCodeBlock(text, output, "success", true)
          console.log(data)
  
        error: (jqXHR, status, err) ->
          window.spinner?.stop()
          data = jqXHR.responseText
          text =
            if(status == "timeout") then "request timed out"
            else if(_.str.isBlank(data)) then "an unkown error occurred"
            else data
          self.createCodeBlock(text, output, "error", true)
          console.log(text)
          console.log(status)
          console.log(err)
      );
  
    #######################################################################################################################
    # raw source code data from snippet
    readRawCode: (elem, editable) ->
      self = @

      if($(elem).length > 0)
        if($(elem)[0].tagName.toLowerCase() == "pre") # find and read previous blocks
          split = $(elem).closest(".codesplit")
          if(split.length > 0)
            @readRawCode(split, editable)
          else
            num = $(elem).data("num")
            _.filter(self.readRawCode($(elem).closest('div.snippet'), editable),
              (b) -> self.isCodeBlock(b) && b["num"] <= num
            )
        else
          if($(elem).hasClass("raw-block"))
            [@readRawBlock(elem, editable)] # read a single block
          else
            @readRawBlocks(elem, editable) # read a bunch of blocks
      else
        []
  
    readRawBlocks: (elem, editable) ->
      self = @
      blocks = []
      $(elem).find(".raw-block").each(
        (idx, blck) -> # iterate through blocks
          blocks[idx] = self.readRawBlock(blck, editable)
          blocks[idx]["num"] = idx
      )
      blocks
  
    readRawBlock: (elem, editable) ->
      self = @
      subs = []
      content = ""
      type = _.str.trim($(elem).data("type"))
      isFragment = $(elem).data("fragment") == true
      hlight = $(elem).data("hlight")
      linebyline = $(elem).data("linebyline")
  
      # read snippet's include content
      incl = @readRawCode($("#" + $(elem).data("include")))
      if(!_.isEmpty(incl)) then subs.push(incl)
  
      # read snippet's reference content
      if(editable)
        ref = @readRawCode($("#" + $(elem).data("reference")), editable)
        if(!_.isEmpty(ref)) then subs.push(ref)

      # read language
      lang = $(elem).data("lang")
  
      # extract text
      lines = _.str.lines(_.str.trim($(elem).text()))
      _.forEach(lines,
        (l, i) ->
          linedata = _.str.strRight(l, "|")
          # append newline
          if(i != 0) then content += "\n"
          # mark call scripts with a '> '
          #console.log(l)
          #console.log(!_.str.isBlank(_.str.trim(l)))
          #if(_.str.contains(type, "call") && editable != true && !_.str.isBlank(_.str.trim(linedata))) then content += "> "
          # append content right from '|'
          content += linedata
      )
  
      {
        num: 0
        lang: lang
        type: type
        text: content
        frag: isFragment
        hlight: hlight
        linebyline: linebyline
        subs: _.flatten(subs)
      }
  
    #readRawBlockRef = (id) ->
    #  content = ""
    #  if(id)
    #    blocks = readRawCode($("#" + id))
    #    if(!_.isEmpty(blocks))
    #      _.forEach(blocks, (b) -> if(isCodeBlock(b)) then content += b.text + "\n")
    #  content
  
    isCodeBlock: (b) ->
      !_.str.contains(b["type"], "output")
  
    subtleToolbar: ->
      $("#navi").addClass("subtle")

  new Editor