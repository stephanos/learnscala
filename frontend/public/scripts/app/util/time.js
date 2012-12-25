
define(["lib/util/moment"], function(moment) {
  var Time;
  Time = (function() {

    function Time() {}

    Time.prototype.setTime = function(key, s) {
      console.log("[TIMER] set '" + key + "' to " + s + " seconds");
      if (s <= -1000000) {
        localStorage[key + ".start"] = null;
        return localStorage[key + ".end"] = null;
      } else {
        localStorage[key + ".start"] = moment().toDate();
        return localStorage[key + ".end"] = moment().add('seconds', s).toDate();
      }
    };

    Time.prototype.addTime = function(key, s) {
      var end;
      console.log("[TIMER] add to '" + key + "' " + s + " seconds");
      end = localStorage[key + ".end"];
      if (end) {
        return localStorage[key + ".end"] = moment(end).add('seconds', s).toDate();
      }
    };

    Time.prototype.getTime = function(key) {
      return [localStorage[key + ".start"], localStorage[key + ".end"]];
    };

    Time.prototype.updateClockSchedule = function(upd, t) {
      var _this = this;
      if (upd()) {
        return setTimeout(function() {
          return _this.updateClockSchedule(upd, t);
        }, t);
      }
    };

    return Time;

  })();
  return new Time;
});
