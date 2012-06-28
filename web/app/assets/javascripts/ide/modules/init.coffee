$ ->
  $('textarea.editor').each(
    (idx, elem) ->
      CodeMirror.fromTextArea(elem, {
        autofocus: false,
        theme: 'ambiance',
        autoClearEmptyLines: true
      })
  )