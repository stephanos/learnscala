require.config({"shim":{"lib/dom/jquery":{"exports":"$"},"lib/util/underscore":{"exports":"_"},"lib/chart/d3":{"exports":"d3"},"lib/util/underscore.str":{"deps":["lib/util/underscore"],"exports":"_"},"lib/init/modernizr":{"exports":"Modernizr"},"lib/slide/reveal":{"deps":["app/util/log","lib/slide/notes"],"exports":"Reveal"},"lib/chart/piechart":{"deps":["jquery"]},"lib/editor/codemirror":{"exports":"CodeMirror"},"lib/util/moment":{"exports":"moment"},"lib/editor/clike":{"deps":["lib/editor/codemirror"],"exports":"CodeMirror"},"lib/editor/runmode":{"deps":["lib/editor/codemirror"],"exports":"CodeMirror"},"lib/dom/iframe":["jquery"],"lib/util/spin":["jquery"],"lib/dom/vanadium":["jquery"],"lib/dom/splitter":["jquery"],"lib/dom/bootstrap":["jquery"]},"paths":{"jquery":"lib/dom/jquery"},"baseUrl":"/base/"});
//console.log(['./app/app.test']);
require(['./app/app.test'], function() {window.__testacular__.start();}, function (err) {
    console.error(err.message);
    throw err;
});