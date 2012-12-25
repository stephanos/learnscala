
define(["jquery", "lib/util/underscore", "lib/util/moment", "app/util/time"], function($, _, moment, Time) {
  var Countdown;
  return Countdown = (function() {

    function Countdown() {
      var self;
      self = this;
      $('<li id="naviCountdown">\
            <span class="divider">|</span>\
            <a href="#" class="openCountdown">\
                <span>Countdown</span>\
            </a>\
          </li>').appendTo($("#navi>ul"));
      $("#naviCountdown").bind("click", function() {
        var m;
        m = $("#countdownModal");
        m.bind("shown", function(evt) {
          var mbody;
          return mbody = $(m).find(".modal-body");
        });
        m.modal();
        return false;
      });
      $('div.countdown').each(function(idx, elem) {
        var key, upd;
        key = "countdown_" + idx;
        upd = function() {
          var diff, end, mm, ms_end, ms_start, now, ss, state, time;
          time = Time.getTime(key);
          ms_end = time[1];
          ms_start = time[0];
          if (ms_start) {
            now = moment();
            end = moment(ms_end);
            state = $(elem).data("state");
            diff = state === "running" ? end.diff(now, 'seconds') : $(elem).data("start");
            $(elem).data("start", diff);
            if (diff < 0) {
              $(elem).addClass("over");
            } else {
              $(elem).removeClass("over");
            }
            mm = diff / 60;
            mm = diff >= 0 ? Math.floor(mm) : Math.ceil(mm);
            $(elem).find(".mm").text(mm);
            ss = Math.abs(diff % 60);
            $(elem).find(".ss").text((ss < 10 ? "0" : "") + ss);
            return state === "running";
          } else {
            return false;
          }
        };
        $(elem).find(".minus").bind("click", function() {
          $(elem).data("start", $(elem).data("start") - 60);
          Time.addTime(key, -60);
          return upd();
        });
        $(elem).find(".plus").bind("click", function() {
          $(elem).data("start", $(elem).data("start") + 60);
          Time.addTime(key, 60);
          return upd();
        });
        return $(elem).find(".toggle").bind("click", function() {
          var state;
          state = $(elem).data("state");
          if (state === "running") {
            return $(elem).data("state", "stopped");
          } else {
            Time.setTime(key, $(elem).data("start"));
            $(elem).data("state", "running");
            return Time.updateClockSchedule(upd, 500);
          }
        });
      });
    }

    return Countdown;

  })();
});
