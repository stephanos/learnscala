var Rickshaw={namespace:function(e,t){var n=e.split("."),r=Rickshaw;for(var i=1,s=n.length;i<s;i++){var o=n[i];r[o]=r[o]||{},r=r[o]}return r},keys:function(e){var t=[];for(var n in e)t.push(n);return t},extend:function(e,t){for(var n in t)e[n]=t[n];return e}};if(typeof module!="undefined"&&module.exports){var d3=require("d3");module.exports=Rickshaw}(function(e){function f(e){return t.call(e)===a}function l(e,t){for(var n in t)t.hasOwnProperty(n)&&(e[n]=t[n]);return e}function c(e){if(h(e)!==u)throw new TypeError;var t=[];for(var n in e)e.hasOwnProperty(n)&&t.push(n);return t}function h(e){switch(e){case null:return n;case void 0:return r}var t=typeof e;switch(t){case"boolean":return i;case"number":return s;case"string":return o}return u}function p(e){return typeof e=="undefined"}function v(e){var t=e.toString().match(/^[\s\(]*function[^(]*\(([^)]*)\)/)[1].replace(/\/\/.*?[\r\n]|\/\*(?:.|[\r\n])*?\*\//g,"").replace(/\s+/g,"").split(",");return t.length==1&&!t[0]?[]:t}function m(e,t){var n=e;return function(){var e=g([b(n,this)],arguments);return t.apply(this,e)}}function g(e,t){var n=e.length,r=t.length;while(r--)e[n+r]=t[r];return e}function y(e,t){return e=d.call(e,0),g(e,t)}function b(e,t){if(arguments.length<2&&p(arguments[0]))return this;var n=e,r=d.call(arguments,2);return function(){var e=y(r,arguments);return n.apply(t,e)}}var t=Object.prototype.toString,n="Null",r="Undefined",i="Boolean",s="Number",o="String",u="Object",a="[object Function]",d=Array.prototype.slice,w=function(){},E=function(){function t(){}function n(){function r(){this.initialize.apply(this,arguments)}var e=null,n=[].slice.apply(arguments);f(n[0])&&(e=n.shift()),l(r,E.Methods),r.superclass=e,r.subclasses=[];if(e){t.prototype=e.prototype,r.prototype=new t;try{e.subclasses.push(r)}catch(i){}}for(var s=0,o=n.length;s<o;s++)r.addMethods(n[s]);return r.prototype.initialize||(r.prototype.initialize=w),r.prototype.constructor=r,r}function r(t){var n=this.superclass&&this.superclass.prototype,r=c(t);e&&(t.toString!=Object.prototype.toString&&r.push("toString"),t.valueOf!=Object.prototype.valueOf&&r.push("valueOf"));for(var i=0,s=r.length;i<s;i++){var o=r[i],u=t[o];if(n&&f(u)&&v(u)[0]=="$super"){var a=u;u=m(function(e){return function(){return n[e].apply(this,arguments)}}(o),a),u.valueOf=b(a.valueOf,a),u.toString=b(a.toString,a)}this.prototype[o]=u}return this}var e=function(){for(var e in{toString:1})if(e==="toString")return!1;return!0}();return{create:n,Methods:{addMethods:r}}}();e.exports?e.exports.Class=E:e.Class=E})(Rickshaw),Rickshaw.namespace("Rickshaw.Compat.ClassList"),Rickshaw.Compat.ClassList=function(){typeof document!="undefined"&&!("classList"in document.createElement("a"))&&function(e){"use strict";var t="classList",n="prototype",r=(e.HTMLElement||e.Element)[n],i=Object,s=String[n].trim||function(){return this.replace(/^\s+|\s+$/g,"")},o=Array[n].indexOf||function(e){var t=0,n=this.length;for(;t<n;t++)if(t in this&&this[t]===e)return t;return-1},u=function(e,t){this.name=e,this.code=DOMException[e],this.message=t},a=function(e,t){if(t==="")throw new u("SYNTAX_ERR","An invalid or illegal string was specified");if(/\s/.test(t))throw new u("INVALID_CHARACTER_ERR","String contains an invalid character");return o.call(e,t)},f=function(e){var t=s.call(e.className),n=t?t.split(/\s+/):[],r=0,i=n.length;for(;r<i;r++)this.push(n[r]);this._updateClassName=function(){e.className=this.toString()}},l=f[n]=[],c=function(){return new f(this)};u[n]=Error[n],l.item=function(e){return this[e]||null},l.contains=function(e){return e+="",a(this,e)!==-1},l.add=function(e){e+="",a(this,e)===-1&&(this.push(e),this._updateClassName())},l.remove=function(e){e+="";var t=a(this,e);t!==-1&&(this.splice(t,1),this._updateClassName())},l.toggle=function(e){e+="",a(this,e)===-1?this.add(e):this.remove(e)},l.toString=function(){return this.join(" ")};if(i.defineProperty){var h={get:c,enumerable:!0,configurable:!0};try{i.defineProperty(r,t,h)}catch(p){p.number===-2146823252&&(h.enumerable=!1,i.defineProperty(r,t,h))}}else i[n].__defineGetter__&&r.__defineGetter__(t,c)}(window)},(typeof RICKSHAW_NO_COMPAT!="undefined"&&!RICKSHAW_NO_COMPAT||typeof RICKSHAW_NO_COMPAT=="undefined")&&new Rickshaw.Compat.ClassList,Rickshaw.namespace("Rickshaw.Graph"),Rickshaw.Graph=function(e){this.element=e.element,this.series=e.series,this.defaults={interpolation:"cardinal",offset:"zero",min:undefined,max:undefined},Rickshaw.keys(this.defaults).forEach(function(t){this[t]=e[t]||this.defaults[t]},this),this.window={},this.updateCallbacks=[];var t=this;this.initialize=function(e){this.validateSeries(e.series),this.series.active=function(){return t.series.filter(function(e){return!e.disabled})},this.setSize({width:e.width,height:e.height}),this.element.classList.add("rickshaw_graph"),this.vis=d3.select(this.element).append("svg:svg").attr("width",this.width).attr("height",this.height);var n=[Rickshaw.Graph.Renderer.Stack,Rickshaw.Graph.Renderer.Line,Rickshaw.Graph.Renderer.Bar,Rickshaw.Graph.Renderer.Area,Rickshaw.Graph.Renderer.ScatterPlot];n.forEach(function(e){if(!e)return;t.registerRenderer(new e({graph:t}))}),this.setRenderer(e.renderer||"stack",e),this.discoverRange()},this.validateSeries=function(e){if(!(e instanceof Array||e instanceof Rickshaw.Series)){var t=Object.prototype.toString.apply(e);throw"series is not an array: "+t}var n;e.forEach(function(e){if(!(e instanceof Object))throw"series element is not an object: "+e;if(!e.data)throw"series has no data: "+JSON.stringify(e);if(!(e.data instanceof Array))throw"series data is not an array: "+JSON.stringify(e.data);var t=e.data[0].x,n=e.data[0].y;if(typeof t!="number"||typeof n!="number"&&n!==null)throw"x and y properties of points should be numbers instead of "+typeof t+" and "+typeof n},this)},this.dataDomain=function(){var e=this.series[0].data;return[e[0].x,e.slice(-1).shift().x]},this.discoverRange=function(){var e=this.renderer.domain();this.x=d3.scale.linear().domain(e.x).range([0,this.width]),this.y=d3.scale.linear().domain(e.y).range([this.height,0]),this.y.magnitude=d3.scale.linear().domain([e.y[0]-e.y[0],e.y[1]-e.y[0]]).range([0,this.height])},this.render=function(){var e=this.stackData();this.discoverRange(),this.renderer.render(),this.updateCallbacks.forEach(function(e){e()})},this.update=this.render,this.stackData=function(){var e=this.series.active().map(function(e){return e.data}).map(function(e){return e.filter(function(e){return this._slice(e)},this)},this);this.stackData.hooks.data.forEach(function(n){e=n.f.apply(t,[e])});var n;if(!this.renderer.unstack){this._validateStackable();var r=d3.layout.stack();r.offset(t.offset),n=r(e)}n=n||e,this.stackData.hooks.after.forEach(function(r){n=r.f.apply(t,[e])});var i=0;return this.series.forEach(function(e){if(e.disabled)return;e.stack=n[i++]}),this.stackedData=n,n},this._validateStackable=function(){var e=this.series,t;e.forEach(function(e){t=t||e.data.length;if(t&&e.data.length!=t)throw"stacked series cannot have differing numbers of points: "+t+" vs "+e.data.length+"; see Rickshaw.Series.fill()"},this)},this.stackData.hooks={data:[],after:[]},this._slice=function(e){if(this.window.xMin||this.window.xMax){var t=!0;return this.window.xMin&&e.x<this.window.xMin&&(t=!1),this.window.xMax&&e.x>this.window.xMax&&(t=!1),t}return!0},this.onUpdate=function(e){this.updateCallbacks.push(e)},this.registerRenderer=function(e){this._renderers=this._renderers||{},this._renderers[e.name]=e},this.configure=function(e){(e.width||e.height)&&this.setSize(e),Rickshaw.keys(this.defaults).forEach(function(t){this[t]=t in e?e[t]:t in this?this[t]:this.defaults[t]},this),this.setRenderer(e.renderer||this.renderer.name,e)},this.setRenderer=function(e,t){if(!this._renderers[e])throw"couldn't find renderer "+e;this.renderer=this._renderers[e],typeof t=="object"&&this.renderer.configure(t)},this.setSize=function(e){e=e||{};if(typeof window!==undefined)var t=window.getComputedStyle(this.element,null),n=parseInt(t.getPropertyValue("width")),r=parseInt(t.getPropertyValue("height"));this.width=e.width||n||400,this.height=e.height||r||250,this.vis&&this.vis.attr("width",this.width).attr("height",this.height)},this.initialize(e)},Rickshaw.namespace("Rickshaw.Fixtures.Color"),Rickshaw.Fixtures.Color=function(){this.schemes={},this.schemes.spectrum14=["#ecb796","#dc8f70","#b2a470","#92875a","#716c49","#d2ed82","#bbe468","#a1d05d","#e7cbe6","#d8aad6","#a888c2","#9dc2d3","#649eb9","#387aa3"].reverse(),this.schemes.spectrum2000=["#57306f","#514c76","#646583","#738394","#6b9c7d","#84b665","#a7ca50","#bfe746","#e2f528","#fff726","#ecdd00","#d4b11d","#de8800","#de4800","#c91515","#9a0000","#7b0429","#580839","#31082b"],this.schemes.spectrum2001=["#2f243f","#3c2c55","#4a3768","#565270","#6b6b7c","#72957f","#86ad6e","#a1bc5e","#b8d954","#d3e04e","#ccad2a","#cc8412","#c1521d","#ad3821","#8a1010","#681717","#531e1e","#3d1818","#320a1b"],this.schemes.classic9=["#423d4f","#4a6860","#848f39","#a2b73c","#ddcb53","#c5a32f","#7d5836","#963b20","#7c2626","#491d37","#2f254a"].reverse(),this.schemes.httpStatus={503:"#ea5029",502:"#d23f14",500:"#bf3613",410:"#efacea",409:"#e291dc",403:"#f457e8",408:"#e121d2",401:"#b92dae",405:"#f47ceb",404:"#a82a9f",400:"#b263c6",301:"#6fa024",302:"#87c32b",307:"#a0d84c",304:"#28b55c",200:"#1a4f74",206:"#27839f",201:"#52adc9",202:"#7c979f",203:"#a5b8bd",204:"#c1cdd1"},this.schemes.colorwheel=["#b5b6a9","#858772","#785f43","#96557e","#4682b4","#65b9ac","#73c03a","#cb513a"].reverse(),this.schemes.cool=["#5e9d2f","#73c03a","#4682b4","#7bc3b8","#a9884e","#c1b266","#a47493","#c09fb5"],this.schemes.munin=["#00cc00","#0066b3","#ff8000","#ffcc00","#330099","#990099","#ccff00","#ff0000","#808080","#008f00","#00487d","#b35a00","#b38f00","#6b006b","#8fb300","#b30000","#bebebe","#80ff80","#80c9ff","#ffc080","#ffe680","#aa80ff","#ee00cc","#ff8080","#666600","#ffbfff","#00ffcc","#cc6699","#999900"]},Rickshaw.namespace("Rickshaw.Fixtures.RandomData"),Rickshaw.Fixtures.RandomData=function(e){var t;e=e||1;var n=200,r=Math.floor((new Date).getTime()/1e3);this.addData=function(t){var i=Math.random()*100+15+n,s=t[0].length,o=1;t.forEach(function(t){var n=Math.random()*20,u=i/25+o++ +(Math.cos(s*o*11/960)+2)*15+(Math.cos(s/7)+2)*7+(Math.cos(s/17)+2)*1;t.push({x:s*e+r,y:u+n})}),n=i*.85}},Rickshaw.namespace("Rickshaw.Fixtures.Time"),Rickshaw.Fixtures.Time=function(){var e=(new Date).getTimezoneOffset()*60,t=this;this.months=["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"],this.units=[{name:"decade",seconds:315576e3,formatter:function(e){return parseInt(e.getUTCFullYear()/10)*10}},{name:"year",seconds:31557600,formatter:function(e){return e.getUTCFullYear()}},{name:"month",seconds:2635200,formatter:function(e){return t.months[e.getUTCMonth()]}},{name:"week",seconds:604800,formatter:function(e){return t.formatDate(e)}},{name:"day",seconds:86400,formatter:function(e){return e.getUTCDate()}},{name:"6 hour",seconds:21600,formatter:function(e){return t.formatTime(e)}},{name:"hour",seconds:3600,formatter:function(e){return t.formatTime(e)}},{name:"15 minute",seconds:900,formatter:function(e){return t.formatTime(e)}},{name:"minute",seconds:60,formatter:function(e){return e.getUTCMinutes()}},{name:"15 second",seconds:15,formatter:function(e){return e.getUTCSeconds()+"s"}},{name:"second",seconds:1,formatter:function(e){return e.getUTCSeconds()+"s"}}],this.unit=function(e){return this.units.filter(function(t){return e==t.name}).shift()},this.formatDate=function(e){return e.toUTCString().match(/, (\w+ \w+ \w+)/)[1]},this.formatTime=function(e){return e.toUTCString().match(/(\d+:\d+):/)[1]},this.ceil=function(e,t){if(t.name=="month"){var n=new Date((e+t.seconds-1)*1e3),r=new Date(0);return r.setUTCFullYear(n.getUTCFullYear()),r.setUTCMonth(n.getUTCMonth()),r.setUTCDate(1),r.setUTCHours(0),r.setUTCMinutes(0),r.setUTCSeconds(0),r.setUTCMilliseconds(0),r.getTime()/1e3}if(t.name=="year"){var n=new Date((e+t.seconds-1)*1e3),r=new Date(0);return r.setUTCFullYear(n.getUTCFullYear()),r.setUTCMonth(0),r.setUTCDate(1),r.setUTCHours(0),r.setUTCMinutes(0),r.setUTCSeconds(0),r.setUTCMilliseconds(0),r.getTime()/1e3}return Math.ceil(e/t.seconds)*t.seconds}},Rickshaw.namespace("Rickshaw.Fixtures.Number"),Rickshaw.Fixtures.Number.formatKMBT=function(e){return e>=1e12?e/1e12+"T":e>=1e9?e/1e9+"B":e>=1e6?e/1e6+"M":e>=1e3?e/1e3+"K":e<1&&e>0?e.toFixed(2):e==0?"":e},Rickshaw.Fixtures.Number.formatBase1024KMGTP=function(e){return e>=0x4000000000000?e/0x4000000000000+"P":e>=1099511627776?e/1099511627776+"T":e>=1073741824?e/1073741824+"G":e>=1048576?e/1048576+"M":e>=1024?e/1024+"K":e<1&&e>0?e.toFixed(2):e==0?"":e},Rickshaw.namespace("Rickshaw.Color.Palette"),Rickshaw.Color.Palette=function(e){var t=new Rickshaw.Fixtures.Color;e=e||{},this.schemes={},this.scheme=t.schemes[e.scheme]||e.scheme||t.schemes.colorwheel,this.runningIndex=0,this.generatorIndex=0;if(e.interpolatedStopCount){var n=this.scheme.length-1,r,i,s=[];for(r=0;r<n;r++){s.push(this.scheme[r]);var o=d3.interpolateHsl(this.scheme[r],this.scheme[r+1]);for(i=1;i<e.interpolatedStopCount;i++)s.push(o(1/e.interpolatedStopCount*i))}s.push(this.scheme[this.scheme.length-1]),this.scheme=s}this.rotateCount=this.scheme.length,this.color=function(e){return this.scheme[e]||this.scheme[this.runningIndex++]||this.interpolateColor()||"#808080"},this.interpolateColor=function(){if(!Array.isArray(this.scheme))return;var e;return this.generatorIndex==this.rotateCount*2-1?(e=d3.interpolateHsl(this.scheme[this.generatorIndex],this.scheme[0])(.5),this.generatorIndex=0,this.rotateCount*=2):(e=d3.interpolateHsl(this.scheme[this.generatorIndex],this.scheme[this.generatorIndex+1])(.5),this.generatorIndex++),this.scheme.push(e),e}},Rickshaw.namespace("Rickshaw.Graph.Ajax"),Rickshaw.Graph.Ajax=Rickshaw.Class.create({initialize:function(e){this.dataURL=e.dataURL,this.onData=e.onData||function(e){return e},this.onComplete=e.onComplete||function(){},this.onError=e.onError||function(){},this.args=e,this.request()},request:function(){$.ajax({url:this.dataURL,dataType:"json",success:this.success.bind(this),error:this.error.bind(this)})},error:function(){console.log("error loading dataURL: "+this.dataURL),this.onError(this)},success:function(e,t){e=this.onData(e),this.args.series=this._splice({data:e,series:this.args.series}),this.graph=new Rickshaw.Graph(this.args),this.graph.render(),this.onComplete(this)},_splice:function(e){var t=e.data,n=e.series;return e.series?(n.forEach(function(e){var n=e.key||e.name;if(!n)throw"series needs a key or a name";t.forEach(function(t){var r=t.key||t.name;if(!r)throw"data needs a key or a name";if(n==r){var i=["color","name","data"];i.forEach(function(n){e[n]=e[n]||t[n]})}})}),n):t}}),Rickshaw.namespace("Rickshaw.Graph.Annotate"),Rickshaw.Graph.Annotate=function(e){var t=this.graph=e.graph;this.elements={timeline:e.element};var n=this;this.data={},this.elements.timeline.classList.add("rickshaw_annotation_timeline"),this.add=function(e,t,r){n.data[e]=n.data[e]||{boxes:[]},n.data[e].boxes.push({content:t,end:r})},this.update=function(){Rickshaw.keys(n.data).forEach(function(e){var t=n.data[e],r=n.graph.x(e);if(r<0||r>n.graph.x.range()[1]){t.element&&(t.line.classList.add("offscreen"),t.element.style.display="none"),t.boxes.forEach(function(e){e.rangeElement&&e.rangeElement.classList.add("offscreen")});return}if(!t.element){var i=t.element=document.createElement("div");i.classList.add("annotation"),this.elements.timeline.appendChild(i),i.addEventListener("click",function(e){i.classList.toggle("active"),t.line.classList.toggle("active"),t.boxes.forEach(function(e){e.rangeElement&&e.rangeElement.classList.toggle("active")})},!1)}t.element.style.left=r+"px",t.element.style.display="block",t.boxes.forEach(function(e){var i=e.element;i||(i=e.element=document.createElement("div"),i.classList.add("content"),i.innerHTML=e.content,t.element.appendChild(i),t.line=document.createElement("div"),t.line.classList.add("annotation_line"),n.graph.element.appendChild(t.line),e.end&&(e.rangeElement=document.createElement("div"),e.rangeElement.classList.add("annotation_range"),n.graph.element.appendChild(e.rangeElement)));if(e.end){var s=r,o=Math.min(n.graph.x(e.end),n.graph.x.range()[1]);s>o&&(o=r,s=Math.max(n.graph.x(e.end),n.graph.x.range()[0]));var u=o-s;e.rangeElement.style.left=s+"px",e.rangeElement.style.width=u+"px",e.rangeElement.classList.remove("offscreen")}t.line.classList.remove("offscreen"),t.line.style.left=r+"px"})},this)},this.graph.onUpdate(function(){n.update()})},Rickshaw.namespace("Rickshaw.Graph.Axis.Time"),Rickshaw.Graph.Axis.Time=function(e){var t=this;this.graph=e.graph,this.elements=[],this.ticksTreatment=e.ticksTreatment||"plain",this.fixedTimeUnit=e.timeUnit;var n=new Rickshaw.Fixtures.Time;this.appropriateTimeUnit=function(){var e,t=n.units,r=this.graph.x.domain(),i=r[1]-r[0];return t.forEach(function(t){Math.floor(i/t.seconds)>=2&&(e=e||t)}),e||n.units[n.units.length-1]},this.tickOffsets=function(){var e=this.graph.x.domain(),t=this.fixedTimeUnit||this.appropriateTimeUnit(),r=Math.ceil((e[1]-e[0])/t.seconds),i=e[0],s=[];for(var o=0;o<r;o++){var u=n.ceil(i,t);i=u+t.seconds/2,s.push({value:u,unit:t})}return s},this.render=function(){this.elements.forEach(function(e){e.parentNode.removeChild(e)}),this.elements=[];var e=this.tickOffsets();e.forEach(function(e){if(t.graph.x(e.value)>t.graph.x.range()[1])return;var n=document.createElement("div");n.style.left=t.graph.x(e.value)+"px",n.classList.add("x_tick"),n.classList.add(t.ticksTreatment);var r=document.createElement("div");r.classList.add("title"),r.innerHTML=e.unit.formatter(new Date(e.value*1e3)),n.appendChild(r),t.graph.element.appendChild(n),t.elements.push(n)})},this.graph.onUpdate(function(){t.render()})},Rickshaw.namespace("Rickshaw.Graph.Axis.Y"),Rickshaw.Graph.Axis.Y=function(e){var t=this,n=.1;this.initialize=function(e){this.graph=e.graph,this.orientation=e.orientation||"right";var n=e.pixelsPerTick||75;this.ticks=e.ticks||Math.floor(this.graph.height/n),this.tickSize=e.tickSize||4,this.ticksTreatment=e.ticksTreatment||"plain",e.element?(this.element=e.element,this.vis=d3.select(e.element).append("svg:svg").attr("class","rickshaw_graph y_axis"),this.element=this.vis[0][0],this.element.style.position="relative",this.setSize({width:e.width,height:e.height})):this.vis=this.graph.vis,this.graph.onUpdate(function(){t.render()})},this.setSize=function(e){e=e||{};if(!this.element)return;if(typeof window!="undefined"){var t=window.getComputedStyle(this.element.parentNode,null),r=parseInt(t.getPropertyValue("width"));if(!e.auto)var i=parseInt(t.getPropertyValue("height"))}this.width=e.width||r||this.graph.width*n,this.height=e.height||i||this.graph.height,this.vis.attr("width",this.width).attr("height",this.height*(1+n));var s=this.height*n;this.element.style.top=-1*s+"px"},this.render=function(){this.graph.height!==this._renderHeight&&this.setSize({auto:!0});var t=d3.svg.axis().scale(this.graph.y).orient(this.orientation);t.tickFormat(e.tickFormat||function(e){return e});if(this.orientation=="left")var r=this.height*n,i="translate("+this.width+", "+r+")";this.element&&this.vis.selectAll("*").remove(),this.vis.append("svg:g").attr("class",["y_ticks",this.ticksTreatment].join(" ")).attr("transform",i).call(t.ticks(this.ticks).tickSubdivide(0).tickSize(this.tickSize));var s=(this.orientation=="right"?1:-1)*this.graph.width;this.graph.vis.append("svg:g").attr("class","y_grid").call(t.ticks(this.ticks).tickSubdivide(0).tickSize(s)),this._renderHeight=this.graph.height},this.initialize(e)},Rickshaw.namespace("Rickshaw.Graph.Behavior.Series.Highlight"),Rickshaw.Graph.Behavior.Series.Highlight=function(e){this.graph=e.graph,this.legend=e.legend;var t=this,n={};this.addHighlightEvents=function(e){e.element.addEventListener("mouseover",function(r){t.legend.lines.forEach(function(t){if(e===t)return;n[t.series.name]=n[t.series.name]||t.series.color,t.series.color=d3.interpolateRgb(t.series.color,d3.rgb("#d8d8d8"))(.8).toString()}),t.graph.update()},!1),e.element.addEventListener("mouseout",function(e){t.legend.lines.forEach(function(e){n[e.series.name]&&(e.series.color=n[e.series.name])}),t.graph.update()},!1)},this.legend&&this.legend.lines.forEach(function(e){t.addHighlightEvents(e)})},Rickshaw.namespace("Rickshaw.Graph.Behavior.Series.Order"),Rickshaw.Graph.Behavior.Series.Order=function(e){this.graph=e.graph,this.legend=e.legend;var t=this;$(function(){$(t.legend.list).sortable({containment:"parent",tolerance:"pointer",update:function(e,n){var r=[];$(t.legend.list).find("li").each(function(e,t){if(!t.series)return;r.push(t.series)});for(var i=t.graph.series.length-1;i>=0;i--)t.graph.series[i]=r.shift();t.graph.update()}}),$(t.legend.list).disableSelection()}),this.graph.onUpdate(function(){var e=window.getComputedStyle(t.legend.element).height;t.legend.element.style.height=e})},Rickshaw.namespace("Rickshaw.Graph.Behavior.Series.Toggle"),Rickshaw.Graph.Behavior.Series.Toggle=function(e){this.graph=e.graph,this.legend=e.legend;var t=this;this.addAnchor=function(e){var n=document.createElement("a");n.innerHTML="&#10004;",n.classList.add("action"),e.element.insertBefore(n,e.element.firstChild),n.onclick=function(t){e.series.disabled?(e.series.enable(),e.element.classList.remove("disabled")):(e.series.disable(),e.element.classList.add("disabled"))};var r=e.element.getElementsByTagName("span")[0];r.onclick=function(n){var r=e.series.disabled;if(!r)for(var i=0;i<t.legend.lines.length;i++){var s=t.legend.lines[i];if(e.series!==s.series&&!s.series.disabled){r=!0;break}}r?(e.series.enable(),e.element.classList.remove("disabled"),t.legend.lines.forEach(function(t){e.series!==t.series&&(t.series.disable(),t.element.classList.add("disabled"))})):t.legend.lines.forEach(function(e){e.series.enable(),e.element.classList.remove("disabled")})}},this.legend&&($(this.legend.list).sortable({start:function(e,t){t.item.bind("no.onclick",function(e){e.preventDefault()})},stop:function(e,t){setTimeout(function(){t.item.unbind("no.onclick")},250)}}),this.legend.lines.forEach(function(e){t.addAnchor(e)})),this._addBehavior=function(){this.graph.series.forEach(function(e){e.disable=function(){if(t.graph.series.length<=1)throw"only one series left";e.disabled=!0,t.graph.update()},e.enable=function(){e.disabled=!1,t.graph.update()}})},this._addBehavior(),this.updateBehaviour=function(){this._addBehavior()}},Rickshaw.namespace("Rickshaw.Graph.HoverDetail"),Rickshaw.Graph.HoverDetail=Rickshaw.Class.create({initialize:function(e){var t=this.graph=e.graph;this.xFormatter=e.xFormatter||function(e){return(new Date(e*1e3)).toUTCString()},this.yFormatter=e.yFormatter||function(e){return e===null?e:e.toFixed(2)};var n=this.element=document.createElement("div");n.className="detail",this.visible=!0,t.element.appendChild(n),this.lastEvent=null,this._addListeners(),this.onShow=e.onShow,this.onHide=e.onHide,this.onRender=e.onRender,this.formatter=e.formatter||this.formatter},formatter:function(e,t,n,r,i,s){return e.name+":&nbsp;"+i},update:function(e){e=e||this.lastEvent;if(!e)return;this.lastEvent=e;if(!e.target.nodeName.match(/^(path|svg|rect)$/))return;var t=this.graph,n=e.offsetX||e.layerX,r=e.offsetY||e.layerY,i=0,s=[],o;this.graph.series.active().forEach(function(e){var u=this.graph.stackedData[i++],a=t.x.invert(n),f=d3.scale.linear().domain([u[0].x,u.slice(-1)[0].x]).range([0,u.length]),l=Math.floor(f(a)),c=Math.min(l||0,u.length-1);for(var h=l;h<u.length-1;){if(!u[h]||!u[h+1])break;if(u[h].x<=a&&u[h+1].x>a){c=h;break}u[h+1].x<=a?h++:h--}var p=u[c],d=Math.sqrt(Math.pow(Math.abs(t.x(p.x)-n),2)+Math.pow(Math.abs(t.y(p.y+p.y0)-r),2)),v=e.xFormatter||this.xFormatter,m=e.yFormatter||this.yFormatter,g={formattedXValue:v(p.x),formattedYValue:m(p.y),series:e,value:p,distance:d,order:i,name:e.name};if(!o||d<o.distance)o=g;s.push(g)},this),o.active=!0;var u=o.value.x,a=o.formattedXValue;this.element.innerHTML="",this.element.style.left=t.x(u)+"px",this.visible&&this.render({points:s,detail:s,mouseX:n,mouseY:r,formattedXValue:a,domainX:u})},hide:function(){this.visible=!1,this.element.classList.add("inactive"),typeof this.onHide=="function"&&this.onHide()},show:function(){this.visible=!0,this.element.classList.remove("inactive"),typeof this.onShow=="function"&&this.onShow()},render:function(e){var t=e.points,n=t.filter(function(e){return e.active}).shift();if(n.value.y===null)return;var r=this.xFormatter(n.value.x),i=this.yFormatter(n.value.y);this.element.innerHTML="",this.element.style.left=graph.x(n.value.x)+"px";var s=document.createElement("div");s.className="x_label",s.innerHTML=r,this.element.appendChild(s);var o=document.createElement("div");o.className="item",o.innerHTML=this.formatter(n.series,n.value.x,n.value.y,r,i),o.style.top=this.graph.y(n.value.y0+n.value.y)+"px",this.element.appendChild(o);var u=document.createElement("div");u.className="dot",u.style.top=o.style.top,u.style.borderColor=n.series.color,this.element.appendChild(u),n.active&&(o.className="item active",u.className="dot active"),this.show(),typeof this.onRender=="function"&&this.onRender(e)},_addListeners:function(){this.graph.element.addEventListener("mousemove",function(e){this.visible=!0,this.update(e)}.bind(this),!1),this.graph.onUpdate(function(){this.update()}.bind(this)),this.graph.element.addEventListener("mouseout",function(e){e.relatedTarget&&!(e.relatedTarget.compareDocumentPosition(this.graph.element)&Node.DOCUMENT_POSITION_CONTAINS)&&this.hide()}.bind(this),!1)}}),Rickshaw.namespace("Rickshaw.Graph.JSONP"),Rickshaw.Graph.JSONP=Rickshaw.Class.create(Rickshaw.Graph.Ajax,{request:function(){$.ajax({url:this.dataURL,dataType:"jsonp",success:this.success.bind(this),error:this.error.bind(this)})}}),Rickshaw.namespace("Rickshaw.Graph.Legend"),Rickshaw.Graph.Legend=function(e){var t=this.element=e.element,n=this.graph=e.graph,r=this;t.classList.add("rickshaw_legend");var i=this.list=document.createElement("ul");t.appendChild(i);var s=n.series.map(function(e){return e}).reverse();this.lines=[],this.addLine=function(e){var t=document.createElement("li");t.className="line";var n=document.createElement("div");n.className="swatch",n.style.backgroundColor=e.color,t.appendChild(n);var s=document.createElement("span");s.className="label",s.innerHTML=e.name,t.appendChild(s),i.appendChild(t),t.series=e,e.noLegend&&(t.style.display="none");var o={element:t,series:e};r.shelving&&(r.shelving.addAnchor(o),r.shelving.updateBehaviour()),r.highlighter&&r.highlighter.addHighlightEvents(o),r.lines.push(o)},s.forEach(function(e){r.addLine(e)}),n.onUpdate(function(){})},Rickshaw.namespace("Rickshaw.Graph.RangeSlider"),Rickshaw.Graph.RangeSlider=function(e){var t=this.element=e.element,n=this.graph=e.graph;$(function(){$(t).slider({range:!0,min:n.dataDomain()[0],max:n.dataDomain()[1],values:[n.dataDomain()[0],n.dataDomain()[1]],slide:function(e,t){n.window.xMin=t.values[0],n.window.xMax=t.values[1],n.update(),n.dataDomain()[0]==t.values[0]&&(n.window.xMin=undefined),n.dataDomain()[1]==t.values[1]&&(n.window.xMax=undefined)}})}),t[0].style.width=n.width+"px",n.onUpdate(function(){var e=$(t).slider("option","values");$(t).slider("option","min",n.dataDomain()[0]),$(t).slider("option","max",n.dataDomain()[1]),n.window.xMin==undefined&&(e[0]=n.dataDomain()[0]),n.window.xMax==undefined&&(e[1]=n.dataDomain()[1]),$(t).slider("option","values",e)})},Rickshaw.namespace("Rickshaw.Graph.Renderer"),Rickshaw.Graph.Renderer=Rickshaw.Class.create({initialize:function(e){this.graph=e.graph,this.tension=e.tension||this.tension,this.graph.unstacker=this.graph.unstacker||new Rickshaw.Graph.Unstacker({graph:this.graph}),this.configure(e)},seriesPathFactory:function(){},seriesStrokeFactory:function(){},defaults:function(){return{tension:.8,strokeWidth:2,unstack:!0,padding:{top:.01,right:0,bottom:.01,left:0},stroke:!1,fill:!1}},domain:function(){var e={xMin:[],xMax:[],y:[]},t=this.graph.stackedData||this.graph.stackData(),n=t[0][0],r=n.x,i=n.x,s=n.y+n.y0,o=n.y+n.y0;t.forEach(function(e){e.forEach(function(e){var t=e.y+e.y0;t<s&&(s=t),t>o&&(o=t)}),e[0].x<r&&(r=e[0].x),e[e.length-1].x>i&&(i=e[e.length-1].x)}),r-=(i-r)*this.padding.left,i+=(i-r)*this.padding.right,s=this.graph.min==="auto"?s:this.graph.min||0,o=this.graph.max||o;if(this.graph.min==="auto"||s<0)s-=(o-s)*this.padding.bottom;return this.graph.max===undefined&&(o+=(o-s)*this.padding.top),{x:[r,i],y:[s,o]}},render:function(){var e=this.graph;e.vis.selectAll("*").remove();var t=e.vis.selectAll("path").data(this.graph.stackedData).enter().append("svg:path").attr("d",this.seriesPathFactory()),n=0;e.series.forEach(function(e){if(e.disabled)return;e.path=t[0][n++],this._styleSeries(e)},this)},_styleSeries:function(e){var t=this.fill?e.color:"none",n=this.stroke?e.color:"none";e.path.setAttribute("fill",t),e.path.setAttribute("stroke",n),e.path.setAttribute("stroke-width",this.strokeWidth),e.path.setAttribute("class",e.className)},configure:function(e){e=e||{},Rickshaw.keys(this.defaults()).forEach(function(t){if(!e.hasOwnProperty(t)){this[t]=this[t]||this.graph[t]||this.defaults()[t];return}typeof this.defaults()[t]=="object"?Rickshaw.keys(this.defaults()[t]).forEach(function(n){this[t][n]=e[t][n]!==undefined?e[t][n]:this[t][n]!==undefined?this[t][n]:this.defaults()[t][n]},this):this[t]=e[t]!==undefined?e[t]:this[t]!==undefined?this[t]:this.graph[t]!==undefined?this.graph[t]:this.defaults()[t]},this)},setStrokeWidth:function(e){e!==undefined&&(this.strokeWidth=e)},setTension:function(e){e!==undefined&&(this.tension=e)}}),Rickshaw.namespace("Rickshaw.Graph.Renderer.Line"),Rickshaw.Graph.Renderer.Line=Rickshaw.Class.create(Rickshaw.Graph.Renderer,{name:"line",defaults:function($super){return Rickshaw.extend($super(),{unstack:!0,fill:!1,stroke:!0})},seriesPathFactory:function(){var e=this.graph,t=d3.svg.line().x(function(t){return e.x(t.x)}).y(function(t){return e.y(t.y)}).interpolate(this.graph.interpolation).tension(this.tension);return t.defined(function(e){return e.y!==null}),t}}),Rickshaw.namespace("Rickshaw.Graph.Renderer.Stack"),Rickshaw.Graph.Renderer.Stack=Rickshaw.Class.create(Rickshaw.Graph.Renderer,{name:"stack",defaults:function($super){return Rickshaw.extend($super(),{fill:!0,stroke:!1,unstack:!1})},seriesPathFactory:function(){var e=this.graph,t=d3.svg.area().x(function(t){return e.x(t.x)}).y0(function(t){return e.y(t.y0)}).y1(function(t){return e.y(t.y+t.y0)}).interpolate(this.graph.interpolation).tension(this.tension);return t.defined&&t.defined(function(e){return e.y!==null}),t}}),Rickshaw.namespace("Rickshaw.Graph.Renderer.Bar"),Rickshaw.Graph.Renderer.Bar=Rickshaw.Class.create(Rickshaw.Graph.Renderer,{name:"bar",defaults:function($super){var e=Rickshaw.extend($super(),{gapSize:.05,unstack:!1});return delete e.tension,e},initialize:function($super,e){e=e||{},this.gapSize=e.gapSize||this.gapSize,$super(e)},domain:function($super){var e=$super(),t=this._frequentInterval();return e.x[1]+=parseInt(t.magnitude),e},barWidth:function(){var e=this.graph.stackedData||this.graph.stackData(),t=e.slice(-1).shift(),n=this._frequentInterval(),r=this.graph.x(t[0].x+n.magnitude*(1-this.gapSize));return r},render:function(){var e=this.graph;e.vis.selectAll("*").remove();var t=this.barWidth(),n=0,r=e.series.filter(function(e){return!e.disabled}).length,i=this.unstack?t/r:t,s=function(t){var n=[1,0,0,t.y<0?-1:1,0,t.y<0?e.y.magnitude(Math.abs(t.y))*2:0];return"matrix("+n.join(",")+")"};e.series.forEach(function(t){if(t.disabled)return;var r=e.vis.selectAll("path").data(t.stack).enter().append("svg:rect").attr("x",function(t){return e.x(t.x)+n}).attr("y",function(t){return e.y(t.y0+Math.abs(t.y))*(t.y<0?-1:1)}).attr("width",i).attr("height",function(t){return e.y.magnitude(Math.abs(t.y))}).attr("transform",s);Array.prototype.forEach.call(r[0],function(e){e.setAttribute("fill",t.color)}),this.unstack&&(n+=i)},this)},_frequentInterval:function(){var e=this.graph.stackedData||this.graph.stackData(),t=e.slice(-1).shift(),n={};for(var r=0;r<t.length-1;r++){var i=t[r+1].x-t[r].x;n[i]=n[i]||0,n[i]++}var s={count:0};return Rickshaw.keys(n).forEach(function(e){s.count<n[e]&&(s={count:n[e],magnitude:e})}),this._frequentInterval=function(){return s},s}}),Rickshaw.namespace("Rickshaw.Graph.Renderer.Area"),Rickshaw.Graph.Renderer.Area=Rickshaw.Class.create(Rickshaw.Graph.Renderer,{name:"area",defaults:function($super){return Rickshaw.extend($super(),{unstack:!1,fill:!1,stroke:!1})},seriesPathFactory:function(){var e=this.graph,t=d3.svg.area().x(function(t){return e.x(t.x)}).y0(function(t){return e.y(t.y0)}).y1(function(t){return e.y(t.y+t.y0)}).interpolate(e.interpolation).tension(this.tension);return t.defined(function(e){return e.y!==null}),t},seriesStrokeFactory:function(){var e=this.graph,t=d3.svg.line
().x(function(t){return e.x(t.x)}).y(function(t){return e.y(t.y+t.y0)}).interpolate(e.interpolation).tension(this.tension);return t.defined(function(e){return e.y!==null}),t},render:function(){var e=this.graph;e.vis.selectAll("*").remove();var t=e.vis.selectAll("path").data(this.graph.stackedData).enter().insert("svg:g","g");t.append("svg:path").attr("d",this.seriesPathFactory()).attr("class","area"),this.stroke&&t.append("svg:path").attr("d",this.seriesStrokeFactory()).attr("class","line");var n=0;e.series.forEach(function(e){if(e.disabled)return;e.path=t[0][n++],this._styleSeries(e)},this)},_styleSeries:function(e){if(!e.path)return;d3.select(e.path).select(".area").attr("fill",e.color),this.stroke&&d3.select(e.path).select(".line").attr("fill","none").attr("stroke",e.stroke||d3.interpolateRgb(e.color,"black")(.125)).attr("stroke-width",this.strokeWidth),e.className&&e.path.setAttribute("class",e.className)}}),Rickshaw.namespace("Rickshaw.Graph.Renderer.ScatterPlot"),Rickshaw.Graph.Renderer.ScatterPlot=Rickshaw.Class.create(Rickshaw.Graph.Renderer,{name:"scatterplot",defaults:function($super){return Rickshaw.extend($super(),{unstack:!0,fill:!0,stroke:!1,padding:{top:.01,right:.01,bottom:.01,left:.01},dotSize:4})},initialize:function($super,e){$super(e)},render:function(){var e=this.graph;e.vis.selectAll("*").remove(),e.series.forEach(function(t){if(t.disabled)return;var n=e.vis.selectAll("path").data(t.stack.filter(function(e){return e.y!==null})).enter().append("svg:circle").attr("cx",function(t){return e.x(t.x)}).attr("cy",function(t){return e.y(t.y)}).attr("r",function(t){return"r"in t?t.r:e.renderer.dotSize});Array.prototype.forEach.call(n[0],function(e){e.setAttribute("fill",t.color)})},this)}}),Rickshaw.namespace("Rickshaw.Graph.Smoother"),Rickshaw.Graph.Smoother=function(e){this.graph=e.graph,this.element=e.element;var t=this;this.aggregationScale=1,this.element&&$(function(){$(t.element).slider({min:1,max:100,slide:function(e,n){t.setScale(n.value),t.graph.update()}})}),t.graph.stackData.hooks.data.push({name:"smoother",orderPosition:50,f:function(e){if(t.aggregationScale==1)return e;var n=[];return e.forEach(function(e){var r=[];while(e.length){var i=0,s=0,o=e.splice(0,t.aggregationScale);o.forEach(function(e){i+=e.x/o.length,s+=e.y/o.length}),r.push({x:i,y:s})}n.push(r)}),n}}),this.setScale=function(e){if(e<1)throw"scale out of range: "+e;this.aggregationScale=e,this.graph.update()}},Rickshaw.namespace("Rickshaw.Graph.Unstacker"),Rickshaw.Graph.Unstacker=function(e){this.graph=e.graph;var t=this;this.graph.stackData.hooks.after.push({name:"unstacker",f:function(e){return t.graph.renderer.unstack?(e.forEach(function(e){e.forEach(function(e){e.y0=0})}),e):e}})},Rickshaw.namespace("Rickshaw.Series"),Rickshaw.Series=Rickshaw.Class.create(Array,{initialize:function(e,t,n){n=n||{},this.palette=new Rickshaw.Color.Palette(t),this.timeBase=typeof n.timeBase=="undefined"?Math.floor((new Date).getTime()/1e3):n.timeBase;var r=typeof n.timeInterval=="undefined"?1e3:n.timeInterval;this.setTimeInterval(r),e&&typeof e=="object"&&e instanceof Array&&e.forEach(function(e){this.addItem(e)},this)},addItem:function(e){if(typeof e.name=="undefined")throw"addItem() needs a name";e.color=e.color||this.palette.color(e.name),e.data=e.data||[],e.data.length==0&&this.length&&this.getIndex()>0?this[0].data.forEach(function(t){e.data.push({x:t.x,y:0})}):e.data.length==0&&e.data.push({x:this.timeBase-(this.timeInterval||0),y:0}),this.push(e),this.legend&&this.legend.addLine(this.itemByName(e.name))},addData:function(e){var t=this.getIndex();Rickshaw.keys(e).forEach(function(e){this.itemByName(e)||this.addItem({name:e})},this),this.forEach(function(n){n.data.push({x:(t*this.timeInterval||1)+this.timeBase,y:e[n.name]||0})},this)},getIndex:function(){return this[0]&&this[0].data&&this[0].data.length?this[0].data.length:0},itemByName:function(e){for(var t=0;t<this.length;t++)if(this[t].name==e)return this[t]},setTimeInterval:function(e){this.timeInterval=e/1e3},setTimeBase:function(e){this.timeBase=e},dump:function(){var e={timeBase:this.timeBase,timeInterval:this.timeInterval,items:[]};return this.forEach(function(t){var n={color:t.color,name:t.name,data:[]};t.data.forEach(function(e){n.data.push({x:e.x,y:e.y})}),e.items.push(n)}),e},load:function(e){e.timeInterval&&(this.timeInterval=e.timeInterval),e.timeBase&&(this.timeBase=e.timeBase),e.items&&e.items.forEach(function(e){this.push(e),this.legend&&this.legend.addLine(this.itemByName(e.name))},this)}}),Rickshaw.Series.zeroFill=function(e){console.log("zeroFill"),Rickshaw.Series.fill(e,0)},Rickshaw.Series.fill=function(e,t){var n,r=0,i=e.map(function(e){return e.data});while(r<Math.max.apply(null,i.map(function(e){return e.length})))n=Math.min.apply(null,i.filter(function(e){return e[r]}).map(function(e){return e[r].x})),i.forEach(function(e){(!e[r]||e[r].x!=n)&&e.splice(r,0,{x:n,y:t})}),r++},Rickshaw.namespace("Rickshaw.Series.FixedDuration"),Rickshaw.Series.FixedDuration=Rickshaw.Class.create(Rickshaw.Series,{initialize:function(e,t,n){var n=n||{};if(typeof n.timeInterval=="undefined")throw new Error("FixedDuration series requires timeInterval");if(typeof n.maxDataPoints=="undefined")throw new Error("FixedDuration series requires maxDataPoints");this.palette=new Rickshaw.Color.Palette(t),this.timeBase=typeof n.timeBase=="undefined"?Math.floor((new Date).getTime()/1e3):n.timeBase,this.setTimeInterval(n.timeInterval),this[0]&&this[0].data&&this[0].data.length?(this.currentSize=this[0].data.length,this.currentIndex=this[0].data.length):(this.currentSize=0,this.currentIndex=0),this.maxDataPoints=n.maxDataPoints,e&&typeof e=="object"&&e instanceof Array&&(e.forEach(function(e){this.addItem(e)},this),this.currentSize+=1,this.currentIndex+=1),this.timeBase-=(this.maxDataPoints-this.currentSize)*this.timeInterval;if(typeof this.maxDataPoints!="undefined"&&this.currentSize<this.maxDataPoints)for(var r=this.maxDataPoints-this.currentSize-1;r>0;r--)this.currentSize+=1,this.currentIndex+=1,this.forEach(function(e){e.data.unshift({x:((r-1)*this.timeInterval||1)+this.timeBase,y:0,i:r})},this)},addData:function($super,e){$super(e),this.currentSize+=1,this.currentIndex+=1;if(this.maxDataPoints!==undefined)while(this.currentSize>this.maxDataPoints)this.dropData()},dropData:function(){this.forEach(function(e){e.data.splice(0,1)}),this.currentSize-=1},getIndex:function(){return this.currentIndex}});