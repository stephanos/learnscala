scale = 1.0
zoom = scale #* 0.99

utils = require('utils')
casper = require("casper").create(
  viewportSize: {width: 1024 * scale, height: 786 * scale},
  verbose: true,
  logLevel: "debug",
  onError: (self, m) ->
    console.log('FATAL: ' + m)
    self.exit()
)
#casper.on('http.status.404', pageerror);

#######################################
## HELPER

getUrl = (path) ->
  "http://localhost:9000" + path

getLinks = (sel) ->
  links = document.querySelectorAll ("a")
  Array::map.call links, (e) -> e.getAttribute "href"

snapshot = (self, links) ->
  casper.each(links, (casper, link, i) ->
    url = getUrl(link) + "#/"
    if(link.indexOf("/glossary") >= 0)
      self.thenOpen(url, ->
        self.wait(500, ->
          name = link.split("/")[3]
          self.evaluate(-> $("body").addClass("print"))
          self.capture("tmp/glossary/" + name + ".png")
        )
      )
  )

#######################################
## GLOSSARY

# login
casper.start getUrl("/users/login"), ->
  @fill "form[action='/users/do_login']", { name: "stephanos", pass: "stephanos" }, true

# visit glossary
casper.then ->
  casper.open(getUrl("/app/glossary"))

# extract links
casper.then ->
  links = @evaluate(getLinks)
  snapshot(@, links[..1])

# exit
casper.run ->
  #@echo(@getTitle())
  #@echo(@getCurrentUrl())
  @exit()


