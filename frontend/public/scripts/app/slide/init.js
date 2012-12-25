
define(["jquery", "lib/util/underscore", "lib/reveal", "app/editor/init", "app/util/timer", "app/util/countdown", "app/util/overlay", "app/util/chart"], function($, _, Reveal, Editor, Timer, Countdown, Overlay) {
  var Slide;
  return Slide = (function() {

    function Slide(clazz) {
      var li;
      this.initSnippets();
      if (!$("body").hasClass("light")) {
        this.subtleToolbar();
        this.embedEditor();
        this.embedDocs();
        this.embedGlossary();
        this.embedNavi();
        $("#naviLogout").remove();
        li = $("#naviSlides");
        li.parent().append(li);
        li.find(".divider").remove();
        new Countdown();
        new Timer();
        $(".fragments").each(function(idx, elem) {
          return $(elem).children().each(function(idx2, elem2) {
            if (!$(elem2).is('hr')) {
              return $(elem2).addClass("fragment");
            }
          });
        });
      } else {
        $(".fragment").removeClass("fragment");
        $(".slide-end").remove();
      }
      if (!$("body").hasClass("public")) {
        new Overlay();
      }
    }

    Slide.prototype.embedNavi = function() {
      var naviElemLinks, subtitles, titles;
      $('<li id="naviGoTo" class="dropdown">\
           <a class="dropdown-toggle" data-toggle="dropdown" href="#naviGoTo">\
             <span>Go To</span>\
             <b class="caret"></b>\
           </a>\
           <ul class="dropdown-menu"></ul>\
           <span class="divider">|</span>\
         </li>').prependTo($("#navi>ul"));
      titles = [];
      subtitles = [];
      naviElemLinks = $("#naviGoTo").find("ul");
      $(naviElemLinks).append('\
        <li class="main">\
          <a href="#">Start</a>\
        </li>\
      ');
      return $(".reveal section").each(function(idx, elem) {
        var subtitle, title;
        title = $(elem).data("title");
        subtitle = $(elem).data("subtitle");
        if (title && _.last(titles) !== title) {
          titles.push(title);
          $(naviElemLinks).append('\
              <li class="main">\
                <a href="#/' + idx + '">' + title + '</a>\
              </li>\
            ');
        }
        if (subtitle && _.last(subtitles) !== subtitle) {
          subtitles.push(subtitle);
          return $(naviElemLinks).append('\
              <li class="sub">\
                <a href="#/' + idx + '"> - ' + subtitle + '</a>\
              </li>\
            ');
        } else {
          return subtitles.push("<empty>");
        }
      });
    };

    Slide.prototype.embedEditor = function() {
      return $("#navi a.openEditor").bind("click", function(evt) {
        Editor.initModalEditor();
        return evt.preventDefault();
      });
    };

    Slide.prototype.embedGlossary = function() {
      return $("#naviGlossary").bind("click", function() {
        var m;
        m = $("#glossaryModal");
        m.bind("shown", function(evt) {
          var mbody;
          mbody = $(m).find(".modal-body");
          return $(mbody).html('\
            <iframe src="/app/glossary" width="100%" height="100%"\
                    style="position: absolute; top: 0; bottom: 0; left: 0; right: 0; overflow: hidden"></iframe>\
          ');
        });
        m.modal();
        return false;
      });
    };

    Slide.prototype.initSnippets = function() {
      return $('.slides div.snippet').each(function(idx, elem) {
        return Editor.initSnippet(elem);
      });
    };

    Slide.prototype.embedStyle = function() {
      $('<li id="naviStyle">\
          <a href="#" class="openStyle">\
              <span>Style</span>\
          </a>\
          <span class="divider">|</span>\
        </li>').appendTo($("#navi>ul"));
      return $("#naviStyle").bind("click", function() {
        var m;
        m = $("#styleModal");
        m.bind("shown", function(evt) {
          var mbody;
          return mbody = $(m).find(".modal-body");
        });
        m.modal();
        return false;
      });
    };

    Slide.prototype.embedDocs = function() {
      var showDocs;
      $('<li id="naviDocs">\
            <a href="#" class="openDocs">\
                <span>Docs</span>\
            </a>\
            <span class="divider">|</span>\
        </li>').appendTo($("#navi>ul"));
      showDocs = function(uri) {
        var m, url;
        url = "http://www.scala-lang.org/api/current/index.html#" + (uri != null ? uri : "");
        console.log(url);
        m = $("#docsModal");
        m.bind("shown", function() {
          var mbody;
          mbody = $(m).find(".modal-body");
          if ($(mbody).is(":empty")) {
            return $(mbody).html('\
              <iframe src="' + url + '" width="99.5%" height="99.5%"\
                      style="position: absolute; top: 5px; bottom: 5px; left: 5px; right: 5px; overflow: hidden"></iframe>\
            ');
          } else {
            return $("#docsModal iframe").attr("src", url);
          }
        });
        return m.modal();
      };
      $("#naviDocs").bind("click", function() {
        showDocs();
        return false;
      });
      return $("a.api-link").bind("click", function(evt) {
        var elem, uri;
        elem = $(evt.target);
        uri = $(elem).data("uri");
        showDocs(uri);
        return false;
      });
    };

    Slide.prototype.subtleToolbar = function() {
      return $("#navi").addClass("subtle");
    };

    return Slide;

  })();
});
