webpage = require('webpage')
viewport = { width: 1024, height: 768 }

# TODO: https://github.com/Seldaek/slippy/blob/master/bin/phantom-slippy-to-pdf.js

#######################################
## HELPER

createUrl = (path) ->
  "http://localhost:9000" + path

createScreenshot = (page, filename) ->
  page.render filename, ->
    child_process.exec "open #{filename}"

getUrl = (page, cb) ->
  cb(page.evaluate (->
    document.location.href
  ))

getTitle = (page, cb) ->
  cb(page.evaluate (->
    document.title
  ))

String.prototype.endsWith = (suffix) ->
    this.indexOf(suffix, this.length - suffix.length) != -1

snapshot = (links, cb) ->
  if(links.length == 0) then cb()
  link = links[0]
  page = webpage.create()
  page.open(createUrl(link + "#/"), () ->
    name = link.split("/")[3]
    pscale = 2.2
    page.paperSize = { width: viewport.width * pscale, height: viewport.height * pscale, orientation: 'portrait', margin: '0px' }
    page.viewportSize = { width: viewport.width, height: viewport.height }
    setTimeout(
      page.render("tmp/glossary/" + name + ".pdf")
      ,1000
    )
    snapshot(links[1..], cb)
  )

#######################################
## GLOSSARY

page = webpage.create()

page.onConsoleMessage =
  (msg) -> console.log msg

page.onLoadFinished = (status) ->
  getUrl(page, (url) ->
    console.log(url)
    #if(url.endsWith("/login"))
    #  page.evaluate (->
    #    jQuery('input[name="name"]').val('stephanos')
    #    jQuery('input[name="pass"]').val('stephanos')
    #    jQuery('form').submit()
    #  )
    if(url.endsWith("/app"))
      page.open(createUrl("/app/glossary"))
    else if(url.indexOf("/glossary") >= 0)
      if(url.endsWith("/glossary"))
        links = page.evaluate (-> Array::map.call (jQuery("#glossary a")), (e) -> jQuery(e).attr("href"))
        snapshot(links, () ->
          phantom.exit()
        )
      else
        # TODO
    else
      phantom.exit()
  )

page.open(createUrl("/app"))


###

#page.set 'viewportSize', width: 1000, height: 1000
#page.set 'clipRect', top: 0, left: 0, width: 1000, height: 1000
page.open , (status) ->
  page.evaluate (->
    jQuery('input#name').val('stephanos')
    jQuery('input#pass').val('stephanos')
    jQuery('form[action="/users/do_login"]').submit()
    true
  ), (result) ->
    ph.exit()

  #setTimeout (-> createScreenshot(page, 'output.png')), 1000
  #page.evaluate (-> document.title), (result) ->
  #  console.log 'Page title is ' + result
  #  #document.querySelector ".banner .duration"

####