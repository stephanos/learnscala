$ ->
  $('textarea.editor').each(
    (idx, elem) ->
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
