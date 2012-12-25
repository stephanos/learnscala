
define(["jquery", "lib/util/underscore", "lib/chart/d3"], function($, _, d3) {
  var Chart;
  return Chart = (function() {

    function Chart(id) {
      var pad, svg, target, updateChart;
      pad = 10;
      target = "#chart-" + id;
      svg = d3.select(target).append("svg:svg");
      updateChart = function(init) {
        var status, url;
        status = $("#progress-" + id + " .btn").hasClass("btn-danger");
        svg.selectAll(".circle").remove();
        if (status) {
          url = '/app/exercises/' + id;
          console.log("querying: " + url);
          return $.get(url, function(data) {
            var dataset, grid_m_x, grid_m_y, grid_p_x, grid_p_y, grid_size, height, r, t, tasks, users, width, _i, _ref, _results;
            width = 280;
            height = 150;
            svg.attr("width", width).attr("height", height);
            if (data && !_.isEmpty(data)) {
              users = data.length;
              tasks = _.max(_.map(_.groupBy(_.map(data, function(v) {
                return v.results.length;
              }), function(v) {
                return v;
              }), function(v, k) {
                return {
                  key: k,
                  val: v.length
                };
              }), function(l) {
                return l.val;
              }).key;
              console.log(data);
              dataset = _.sortBy(_.map(data, function(ov) {
                var v, _i, _l, _ref, _results;
                v = {};
                _.extend(v, ov);
                _l = v.results.length;
                if (_l > tasks) {
                  v.results = v.results.slice(0, +(tasks - 1) + 1 || 9e9);
                } else if (_l < tasks) {
                  _((function() {
                    _results = [];
                    for (var _i = 0, _ref = tasks - _l - 1; 0 <= _ref ? _i <= _ref : _i >= _ref; 0 <= _ref ? _i++ : _i--){ _results.push(_i); }
                    return _results;
                  }).apply(this)).each(function(r) {
                    return v.results.push(-1);
                  });
                }
                return v;
              }), function(v) {
                return v.user;
              });
              console.log(dataset);
              grid_size = Math.min((width - 2) / users, (height - 2) / tasks);
              grid_p_y = grid_size * 0.05;
              grid_p_x = grid_p_y * 3;
              r = (grid_size - 2 * grid_p_x) / 2;
              grid_m_x = (width - (2 * r * users) - (grid_p_x * 2 * users)) / 2;
              grid_m_y = (height - (2 * r * tasks) - (grid_p_y * 2 * tasks)) / 2;
              _results = [];
              for (t = _i = 0, _ref = tasks - 1; 0 <= _ref ? _i <= _ref : _i >= _ref; t = 0 <= _ref ? ++_i : --_i) {
                _results.push((function(t) {
                  var c, pdata;
                  c = String.fromCharCode(65 + t);
                  pdata = _.map(dataset, function(v) {
                    return {
                      user: v.user,
                      result: v.results[t]
                    };
                  });
                  return svg.selectAll(".circle." + c).data(pdata).enter().append("circle").attr("class", function(obj) {
                    var col, v;
                    v = obj.result;
                    col = v === 0 ? "success" : v === 2 ? "failure" : "pending";
                    return "circle " + col;
                  }).attr("r", r).attr("cx", function(d, i) {
                    return grid_m_x + 1 + r + (i * grid_p_x * 2) + (i * r * 2);
                  }).attr("cy", function(d, i) {
                    return height - grid_m_y - 1 - r - (t * grid_p_y * 2) - (t * r * 2);
                  });
                })(t));
              }
              return _results;
            } else {
              return console.log(" - no data found");
            }
          });
        }
      };
      updateChart(true);
      setInterval((function() {
        return updateChart();
      }), 5 * 1000);
      $("#progress-" + id + " .btn").click(function(evt) {
        target = $(evt.target);
        if ($(target).hasClass("btn-danger")) {
          return $(target).removeClass("btn-danger").addClass("btn-success").text("On");
        } else {
          $(target).removeClass("btn-success").addClass("btn-danger").text("Off");
          return updateChart(true);
        }
      });
    }

    return Chart;

  })();
});
