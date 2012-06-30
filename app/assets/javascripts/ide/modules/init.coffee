$ ->
  $('div.editor').each(
    (idx, div) ->

      # read and trim content
      content = _.str.trim($(div).text())

      # create DIV container
      $(div).html($('<div/>').css({position: 'relative', width: '100%', height: 300}))
      target = $(div).find("div")[0]

      # add editor (see https://github.com/ajaxorg/ace/wiki/Embedding---API)
      editor = ace.edit(target)

      # config
      editor.setHighlightActiveLine(false)
      editor.getSession().setTabSize(4)
      #editor.setReadOnly(true)
      editor.setShowPrintMargin(false)
      editor.getSession().setUseSoftTabs(true)
      target.style.fontSize='20px'

      # style
      editor.setTheme("ace/theme/solarized_light");

      # language
      myMode = require("ace/mode/" + ($(div).data("lang") ? "scala")).Mode
      editor.getSession().setMode(new myMode())

      # content
      editor.getSession().setValue(content)

      #editor.getSession().on('change', function(){
      #  textarea.val(editor.getSession().getValue());
      #});
  )
