var Loader =
function load(base, fs, cb) {
var conf = {"shim":{"lib/dom/jquery":{"exports":"$"},"lib/util/underscore":{"exports":"_"},"lib/chart/d3":{"exports":"d3"},"lib/util/underscore.str":{"deps":["lib/util/underscore"],"exports":"_"},"lib/init/modernizr":{"exports":"Modernizr"},"lib/reveal":{"deps":["app/util/log"],"exports":"Reveal"},"lib/editor/codemirror":{"exports":"CodeMirror"},"lib/util/moment":{"exports":"moment"},"lib/editor/clike":{"deps":["lib/editor/codemirror"],"exports":"CodeMirror"},"lib/editor/runmode":{"deps":["lib/editor/codemirror"],"exports":"CodeMirror"},"lib/editor/matchbrackets":{"deps":["lib/editor/codemirror"],"exports":"CodeMirror"},"lib/dom/iframe":["jquery"],"lib/util/spin":["jquery"],"lib/dom/vanadium":["jquery"],"lib/dom/splitter":["jquery"],"lib/dom/bootstrap":["jquery"]},"paths":{"jquery":"lib/dom/jquery"}};
var modules = [{"name":"base","include":["lib/dom/vanadium"]},{"name":"util","include":["lib/init/modernizr","lib/util/underscore","lib/util/underscore.str","lib/dom/bootstrap","app/util/timer","app/util/overlay","app/util/progress","app/util/countdown"],"exclude":["base"]},{"name":"editor","include":["app/editor/init"],"exclude":["base","util"]},{"name":"slide","include":["app/slide/init"],"exclude":["base","util","editor"]}];
conf.baseUrl = base;
var load = [];
for (var i = 0; i < modules.length; i++) {
    var mod = modules[i];
    var m_name = mod.name;
    for (var j = 0; j < fs.length; j++) {
        var f_name = fs[j].replace(".js", "");;
        if(f_name == m_name) {
            var m_incl = mod.include;
            for (var k = 0; k < m_incl.length; k++) {
                load.push(m_incl[k]);
            }
        }
    }
}
require.config(conf);
//console.log(load);
require(load, function() {if (cb) { cb(); }}, function (err) {
    console.error(err.message);
    throw err;
});};