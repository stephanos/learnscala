$ ->
  window.editors = []
  $('div.editor').each(
    (idx, div) ->

      # read and trim content
      raw = $(div).text()

      # create DIV container
      $(div).html($('<div/>').css({position: 'relative', width: '100%', height: 100}))
      target = $(div).find("div")[0]

      # add editor (see https://github.com/ajaxorg/ace/wiki/Embedding---API)
      editor = ace.edit(target)

      # config
      editor.setHighlightActiveLine(false)
      editor.getSession().setTabSize(4)
      editor.getSession().setUseWrapMode(false)
      editor.setReadOnly($(div).data("readonly") ? false)
      editor.setShowPrintMargin(false)
      editor.getSession().setUseSoftTabs(true)
      target.style.fontSize = $(div).data("fsize") ? '28px'

      # style
      editor.setTheme("ace/theme/solarized_light");

      # language mode
      myMode = require("ace/mode/" + ($(div).data("lang") ? "scala")).Mode
      editor.getSession().setMode(new myMode())

      # content
      lines = _.str.lines(_.str.trim(raw))
      _.forEach(lines,
        (l, i) ->
          if(i != 0) then editor.insert("\n")
          editor.insert(_.str.strRight(l, "|"))
      )

      # re-size
      editor.getSession().on('change',
        () ->
          # calc
          cell = $(target).find("div.ace_gutter-layer div.ace_gutter-cell:first")
          h_cell = cell.height()
          h_total = h_cell * (editor.getSession().getValue().split('\n').length + 1)

          # apply
          $(target).height(h_total)
          $(target).find(":first-child").height($(target).height())

          # trigger
          editor.renderer.onResize(true)
      )

      window.editors.push(editor)
  )

window.resizeEditors = () ->
  console.log("resizing " + window.editors.length + " editors")
  _.each(window.editors,
    (e) ->
      e.insert(" ")
  )