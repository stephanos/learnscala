
define(["jquery", "lib/util/underscore.str", "lib/dom/bootstrap"], function($, _) {
  var Progress, p;
  Progress = (function() {

    function Progress() {
      this.init();
      $(document).tooltip({
        html: true,
        delay: {
          show: 500,
          hide: 10
        },
        selector: ".with-tooltip",
        placement: "bottom"
      });
    }

    Progress.prototype.init = function() {
      var _this = this;
      return $(".slidedecks a").each(function(idx, link) {
        var deps, input, key, li, missing, reqs;
        if ($(link).data("id")) {
          li = $(link).parent();
          input = $(li).find("input");
          $(li).removeClass("with-tooltip");
          $(input).unbind();
          li.removeClass("req");
          deps = $(link).data("deps");
          if (deps) {
            deps = _.filter(deps.split(","), function(id) {
              return _this.getItem(id)[0];
            });
            missing = _.reject(deps, function(id) {
              return localStorage[_this.getKey(_this.getItem(id).parent().find("input"))];
            });
            if (missing.length > 0) {
              li.addClass("req");
              reqs = "";
              _.each(missing, function(id) {
                return reqs += "<li>" + _this.getItem(id).data("label") + "</li>";
              });
              li.attr("title", "Voraussetzungen: <ul>" + reqs + "</ul>");
              $(li).addClass("with-tooltip");
            }
          }
          $(input).click(_this.update);
          key = _this.getKey(input);
          if (localStorage[key] !== void 0) {
            return _this.apply(input, true);
          }
        }
      });
    };

    Progress.prototype.getItem = function(id) {
      return $("a[data-id='" + id + "']");
    };

    Progress.prototype.reset = function() {
      var key;
      if (confirm('Reset progress - are you sure?')) {
        for (key in localStorage) {
          if (_.str.contains(key, "progress.")) {
            localStorage.removeItem(key);
          }
        }
        return document.location.reload();
      }
    };

    Progress.prototype.update = function(evt) {
      if ($(this).is(':checked')) {
        window.Progress.set($(this), true);
      } else {
        window.Progress.set($(this), void 0);
      }
      return window.Progress.init();
    };

    Progress.prototype.set = function(input, val) {
      var key;
      key = window.Progress.getKey(input);
      localStorage.removeItem(key);
      if (val) {
        this.write(key, true);
      }
      return this.apply(input, val);
    };

    Progress.prototype.apply = function(input, val) {
      var li;
      li = $(input).parent();
      $(li).removeClass("done");
      $(input).attr("checked", false);
      if (val) {
        $(li).addClass("done");
        return $(input).attr("checked", true);
      }
    };

    Progress.prototype.write = function(key, val) {
      console.log("[STORAGE] '" + key + "' = " + val);
      return localStorage[key] = val;
    };

    Progress.prototype.getKey = function(input) {
      return "progress." + $(input).data("key");
    };

    return Progress;

  })();
  p = new Progress;
  window.Progress = p;
  return p;
});
