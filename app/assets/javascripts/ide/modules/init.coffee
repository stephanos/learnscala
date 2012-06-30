$ ->
  $('div.editor').each(
    (idx, elem) ->

      # extract content
      content = ""
      lines = _.str.lines(_.str.trim($(elem).text()))
      _.forEach(lines,
        (l, i) ->
          if(i != 0) then content += "\n"
          content += _.str.strRight(l, "|")
      )

      # create textarea
      $(elem).append("<textarea/>").val(content)

      # create editor
      editor = CodeMirror.fromTextArea(elem, {
        autofocus: false,
        theme: "ambiance",
        mode: "text/x-scala",
        indentWithTabs: true,
        smartIndent: false,
        indentUnit: 4,
        matchBrackets: true,
        autoClearEmptyLines: true
      })
  )