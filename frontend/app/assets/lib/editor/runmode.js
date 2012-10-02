// HACK: wrap every line in <pre>
CodeMirror.runMode = function(string, modespec, callback, options) {
  var row = 1;
  var mode = CodeMirror.getMode(CodeMirror.defaults, modespec);
  var isNode = callback.nodeType == 1;
  var tabSize = (options && options.tabSize) || CodeMirror.defaults.tabSize;
  if (isNode) {
    var node = callback, tmp = [], accum = [], col = 0;
    var wrap = function(arr, clazz) {
        var content;
        if(arr.length == 0)
            content = "&nbsp;";
        else
            content = arr.join("");
        return "<pre class='codeline " + options.class + " " + (clazz || "") +
                    "' data-num='" + options.num +
                    "' data-row='" + (row++) +
                    "' >" + content + "</pre>"
    };
    callback = function(text, style) {
      if (text == "\n") {
        accum.push (wrap(tmp));
        tmp = [];
        col = 0;
        return;
      }
      var escaped = "";
      // HTML-escape and replace tabs
      for (var pos = 0;;) {
        var idx = text.indexOf("\t", pos);
        if (idx == -1) {
          escaped += CodeMirror.htmlEscape(text.slice(pos));
          col += text.length - pos;
          break;
        } else {
          col += idx - pos;
          escaped += CodeMirror.htmlEscape(text.slice(pos, idx));
          var size = tabSize - col % tabSize;
          col += size;
          for (var i = 0; i < size; ++i) escaped += " ";
          pos = idx + 1;
        }
      }

      if (style)
        tmp.push("<span class=\"cm-" + CodeMirror.htmlEscape(style) + "\">" + escaped + "</span>");
      else
        tmp.push(escaped);
    }
  }
  var lines = CodeMirror.splitLines(string), state = CodeMirror.startState(mode);
  for (var i = 0, e = lines.length; i < e; ++i) {
    if (i) callback("\n");
    var stream = new CodeMirror.StringStream(lines[i]);
    while (!stream.eol()) {
      var style = mode.token(stream, state);
      callback(stream.current(), style, i, stream.start);
      stream.start = stream.pos;
    }
  }
  if (isNode)
    if(tmp.length > 0)
      accum.push (wrap(tmp, "last"));
    node.innerHTML = accum.join("");
};