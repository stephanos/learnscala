
define(["jquery", "lib/util/underscore.str", "lib/editor/codemirror", "lib/util/mousetrap", "lib/util/spin", "lib/dom/splitter", "lib/editor/clike", "lib/editor/runmode"], function($, _, CodeMirror) {
  var Editor;
  Editor = (function() {

    function Editor() {}

    Editor.prototype.initSnippet = function(elem) {
      var code, resetHighlights, self;
      self = this;
      code = $('<div class="code">').appendTo($(elem));
      _.forEach(self.readRawCode(elem), function(b) {
        return self.createCodeBlock(b, code);
      });
      if ($(elem).find(".raw-block").data("lang") === "java") {
        $("<div class='icon-annotation'><span>7</span></div>").appendTo($(elem));
      }
      resetHighlights = function(evt) {
        if (!evt || evt.shiftKey || evt.ctrlKey || evt.altKey || evt.metaKey) {
          return $(elem).find("pre").removeClass("highlight");
        }
      };
      return $(elem).find("pre").each(function(idx, pre) {
        var loadBtn, markBtn;
        markBtn = $("<div class='btn-group btn-group-left'><button class='btn btn-icon btn-mini mark'>&nbsp;</button></div>").prependTo($(pre));
        $(markBtn).click(function(evt) {
          resetHighlights(evt);
          pre = $(evt.target).closest("pre");
          return $(pre).toggleClass("highlight");
        });
        $(pre).click(function(evt) {
          if (evt.shiftKey || evt.ctrlKey || evt.altKey || evt.metaKey) {
            return markBtn.trigger("click");
          }
        });
        $(pre).dblclick(function(evt) {
          resetHighlights(evt);
          return markBtn.trigger("click");
        });
        if (!$(pre).hasClass("output")) {
          loadBtn = $("<div class='btn-group btn-group-right'><button class='btn btn-icon btn-mini load'>6</button></div>").appendTo($(pre));
          return $(loadBtn).click(function(evt) {
            var blocks;
            pre = $(evt.target).closest("pre");
            blocks = self.readRawCode($(pre), true);
            return self.initModalEditor(blocks);
          });
        }
      });
    };

    Editor.prototype.createCodeBlock = function(block, elem, status, clear, spin) {
      var code, frag, hl, hlight, hlights, lang, linebyline, noText, num, opts, self, text, type, _fn, _i, _len, _ref;
      self = this;
      if (_.isString(block)) {
        num = "";
        type = "";
        lang = null;
        text = block;
        frag = true;
        hlight = null;
        linebyline = false;
      } else {
        num = block["num"];
        lang = block["lang"];
        type = block["type"];
        text = block["text"];
        frag = block["frag"];
        hlights = block["hlight"];
        linebyline = block["linebyline"];
      }
      noText = _.str.isBlank(text);
      if (clear === true || noText) {
        $(elem).empty();
      }
      if (status) {
        $(elem).parent().removeClass("success error").addClass(status);
      }
      if (!noText) {
        type += " cm-s-ambiance ";
        code = $("<div/>", {
          'class': "wrapper " + type + (frag ? " fragment" : void 0),
          "data-num": num
        }).appendTo($(elem));
        CodeMirror.runMode(text, "text/x-" + (lang != null ? lang : "scala"), code[0], {
          "class": type,
          "num": num,
          "linebyline": linebyline
        });
      }
      if (hlights) {
        _ref = hlights.toString().split(' ');
        _fn = function(hl) {
          var lines, lines_c;
          lines = $(code).find(".codeline");
          lines_c = lines.length;
          if (hl === "first" || hl === "start") {
            hl = 1;
          }
          if (hl === "last" || hl === "end") {
            hl = lines_c;
          }
          if (_.isString(hl)) {
            hl = parseInt(hl, 10);
          }
          if (_.isNumber(hl) && hl >= 1 && hl <= lines_c) {
            return $(lines.get(hl - 1)).addClass("highlight");
          }
        };
        for (_i = 0, _len = _ref.length; _i < _len; _i++) {
          hl = _ref[_i];
          _fn(hl);
        }
      }
      if (status === "wait") {
        opts = {
          lines: 13,
          length: 7,
          width: 4,
          radius: 10,
          rotate: 0,
          color: '#000',
          speed: 1,
          trail: 60,
          shadow: false,
          hwaccel: false,
          className: 'spinner',
          zIndex: 2e9
        };
        return window.spinner = new window.Spinner(opts).spin($(elem).parent()[0]);
      }
    };

    Editor.prototype.initModalEditor = function(blocks, m) {
      var body, editors, self;
      self = this;
      if (m == null) {
        m = $('#ideModal');
      }
      body = m.find(".modal-body");
      body.find(".input, .output").empty();
      editors = this.initEditors($(body), blocks);
      m.bind("shown", function(evt) {
        _.forEach(editors, function(e) {
          return e.refresh();
        });
        self.initEditorSplitters(body);
        return m.find(".pane, .pane div").bind("resize", function() {
          return _.forEach(editors, function(e) {
            return e.refresh();
          });
        });
      });
      return m.modal();
    };

    Editor.prototype.initEditors = function(elem, blocks) {
      var callEditor, callEditorDom, output, self, srcEditor, srcEditorDom;
      self = this;
      output = $(elem).find(".output-wrap div");
      srcEditor = this.initEditor($(elem).find(".source .input"), blocks, "source");
      srcEditorDom = srcEditor.getWrapperElement();
      $('\
        <div class="btn-group btn-group-left">\
          <button class="btn btn-icon btn-changeview hide"></button>\
        </div>\
        <div class="btn-group btn-group-right">\
          <button class="btn btn-icon btn-success" id="btn-decompile-scala">6</button>\
          <button class="btn btn-icon btn-success" id="btn-decompile-java">7</button>\
        </div>').appendTo($(srcEditorDom));
      $('#btn-decompile-java').bind("click", function(evt) {
        return self.callAPI("decompile/java", output, srcEditor.getValue());
      });
      $('#btn-decompile-scala').bind("click", function(evt) {
        return self.callAPI("decompile/scala", output, srcEditor.getValue());
      });
      callEditor = this.initEditor($(elem).find(".call .input"), blocks, "call");
      callEditorDom = callEditor.getWrapperElement();
      $('\
        <div class="btn-group btn-group-left">\
          <button class="btn btn-icon btn-changeview hide"></button>\
        </div>\
        <div class="btn-group">\
            <button class="btn btn-icon btn-success" id="btn-exec">1</button>\
        </div>').appendTo($(callEditorDom));
      $('#btn-exec').bind("click", function(evt) {
        return self.callAPI("execute", output, srcEditor.getValue(), callEditor.getValue());
      });
      window.Mousetrap.bind('ctrl+r', function() {
        return $('#btn-exec').trigger("click");
      });
      return [srcEditor, callEditor];
    };

    Editor.prototype.initEditorSplitters = function(elem) {
      var hsplit, vsplit;
      if ($(elem).find(".hsplitbar").length === 0) {
        vsplit = $(elem).find(".input-wrap").splitter({
          outline: true,
          splitHorizontal: true,
          sizeBottom: 160,
          minTop: 40,
          minBottom: 40
        });
        return hsplit = $(elem).find(".topbottom").splitter({
          outline: true,
          splitHorizontal: true,
          sizeBottom: 160,
          minTop: 90,
          minBottom: 40
        });
      }
    };

    Editor.prototype.initStandaloneEditor = function(elem) {
      this.initEditors(elem);
      return this.initEditorSplitters(elem);
    };

    Editor.prototype.initEditor = function(elem, blocks, typeOf) {
      var content, editor, getBlocks, textarea;
      getBlocks = function(bs) {
        var res;
        res = [];
        _.forEach(bs, function(b) {
          var subs;
          subs = b["subs"];
          if (!_.isEmpty(subs)) {
            res.push(getBlocks(subs));
          }
          if (_.str.contains(b["type"], typeOf)) {
            return res.push(b);
          }
        });
        return _.flatten(res);
      };
      blocks = getBlocks(blocks);
      content = _.str.strip(_.reduce(blocks != null ? blocks : readRawCode(elem), function(r, b) {
        return r += _.str.trim(b["text"], "\n") + "\n" + (typeOf === "source" ? "\n" : "");
      }, ""));
      textarea = $("<textarea/>").appendTo($(elem));
      textarea.val(content);
      editor = CodeMirror.fromTextArea(textarea[0], {
        autofocus: false,
        theme: "ambiance",
        mode: "text/x-scala",
        indentWithTabs: false,
        lineWrapping: typeOf === "call",
        smartIndent: false,
        indentUnit: 3,
        lineNumbers: typeOf === "call",
        lineNumberFormatter: function(n) {
          return ">";
        },
        onCursorActivity: function() {
          editor.setLineClass(window.hlLine, null, null);
          return window.hlLine = editor.setLineClass(editor.getCursor().line, null, "activeLine");
        },
        matchBrackets: true,
        autoClearEmptyLines: true
      });
      window.hlLine = editor.setLineClass(0, "activeLine");
      return editor;
    };

    Editor.prototype.callAPI = function(target, output, source, call) {
      var apiBase, self, uri;
      self = this;
      this.createCodeBlock("", output, "wait");
      uri = window.location.hostname;
      apiBase = uri.indexOf("localhost") > -1 ? "http://localhost:8080" : "https://api.learnscala.de";
      $.support.cors = true;
      return $.ajax({
        type: 'POST',
        timeout: 6000,
        url: apiBase + "/api/" + target,
        data: "source=" + encodeURIComponent(source != null ? source : "") + "&call=" + encodeURIComponent(call != null ? call : ""),
        success: function(data, status) {
          var text, _ref;
          if ((_ref = window.spinner) != null) {
            _ref.stop();
          }
          $(".spinner").remove();
          text = _.str.isBlank(data) ? "compiled and executed successfully" : data;
          self.createCodeBlock(text, output, "success", true);
          return console.log(data);
        },
        error: function(jqXHR, status, err) {
          var data, text, _ref;
          if ((_ref = window.spinner) != null) {
            _ref.stop();
          }
          data = jqXHR.responseText;
          text = status === "timeout" ? "request timed out" : _.str.isBlank(data) ? "an unkown error occurred" : data;
          self.createCodeBlock(text, output, "error", true);
          console.log(text);
          console.log(status);
          return console.log(err);
        }
      });
    };

    Editor.prototype.readRawCode = function(elem, editable) {
      var num, self, split;
      self = this;
      if ($(elem).length > 0) {
        if ($(elem)[0].tagName.toLowerCase() === "pre") {
          split = $(elem).closest(".codesplit");
          if (split.length > 0) {
            return this.readRawCode(split, editable);
          } else {
            num = $(elem).data("num");
            return _.filter(self.readRawCode($(elem).closest('div.snippet'), editable), function(b) {
              return self.isCodeBlock(b) && b["num"] <= num;
            });
          }
        } else {
          if ($(elem).hasClass("raw-block")) {
            return [this.readRawBlock(elem, editable)];
          } else {
            return this.readRawBlocks(elem, editable);
          }
        }
      } else {
        return [];
      }
    };

    Editor.prototype.readRawBlocks = function(elem, editable) {
      var blocks, self;
      self = this;
      blocks = [];
      $(elem).find(".raw-block").each(function(idx, blck) {
        blocks[idx] = self.readRawBlock(blck, editable);
        return blocks[idx]["num"] = idx;
      });
      return blocks;
    };

    Editor.prototype.readRawBlock = function(elem, editable) {
      var content, hlight, incl, isFragment, lang, linebyline, lines, ref, self, subs, type;
      self = this;
      subs = [];
      content = "";
      type = _.str.trim($(elem).data("type"));
      isFragment = $(elem).data("fragment") === true;
      hlight = $(elem).data("hlight");
      linebyline = $(elem).data("linebyline");
      incl = this.readRawCode($("#" + $(elem).data("include")));
      if (!_.isEmpty(incl)) {
        subs.push(incl);
      }
      if (editable) {
        ref = this.readRawCode($("#" + $(elem).data("reference")), editable);
        if (!_.isEmpty(ref)) {
          subs.push(ref);
        }
      }
      lang = $(elem).data("lang");
      lines = _.str.lines(_.str.trim($(elem).text()));
      _.forEach(lines, function(l, i) {
        var linedata;
        linedata = _.str.strRight(l, "|");
        if (i !== 0) {
          content += "\n";
        }
        return content += linedata;
      });
      return {
        num: 0,
        lang: lang,
        type: type,
        text: content,
        frag: isFragment,
        hlight: hlight,
        linebyline: linebyline,
        subs: _.flatten(subs)
      };
    };

    Editor.prototype.isCodeBlock = function(b) {
      return !_.str.contains(b["type"], "output");
    };

    Editor.prototype.subtleToolbar = function() {
      return $("#navi").addClass("subtle");
    };

    return Editor;

  })();
  return new Editor;
});
