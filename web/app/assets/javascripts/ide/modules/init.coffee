$ ->
  $('textarea.editor').each(
    (idx, elem) ->
      editor = CodeMirror.fromTextArea(elem, {
        autofocus: false,
        theme: "ambiance",
        mode: "text/x-scala",
        matchBrackets: true,
        autoClearEmptyLines: true
      })
  )
