webpage = require('webpage')

delay = 500
renderers = []
viewport = { width: 1024, height: 768 }

startPath =
  if phantom.args.length > 0
    phantom.args[0]
  else
    console.log("missing start path!")
    phantom.exit()
console.log("starting at " + startPath)

#######################################
## HELPER

createUrl = (path) ->
  "http://localhost:9000" + path

getPage = (path, cb) ->
  url = createUrl(path)
  page = webpage.create()

  page.onError = (msg, trace) ->
      console.log(msg)
      phantom.exit()

  page.onLoadFinished = (status) ->

    # load page again
    page.open(url, () ->

      # setup page & PDF
      pscale = 2.2
      page.paperSize = {
        width: viewport.width * pscale, height: viewport.height * pscale,
        orientation: 'portrait', margin: '0cm'
      }
      page.viewportSize = viewport

      # trigger "print" mode
      page.evaluate(-> $("body").addClass("print"))

      # remove animations
      page.evaluate(-> $(".fragment").removeClass("fragment"))

      # return page (after short timeout)
      setTimeout(
        () -> cb(page),
        delay)
    )

  page.open(url)

captureSlides = (page, slides, slide, outpath, cb) ->
  if(slide == 0)
    cb()
  else
    # make snapshot
    num = slides - slide
    if(num < 10) then num = "0" + num
    getPDF(page, outpath + "/" + num, () ->

      # move to next slide
      page.evaluate(-> Reveal.navigateRight())

      # call recursively
      captureSlides(page, slides, slide - 1, outpath, cb)
    )

captureLinks = (links, cb) ->
  if(links.length == 0)
    cb()
  else
    link = links[0]
    next = () -> captureLinks(links[1..], cb)
    if(link.indexOf("/") == 0)
      getPage(link, (page) ->

        # read title
        title = getTitle(page)
        if(!title) then throw new Error("link " + link + " has no title (h3)!")
        title = title.replace(/ä/g, "ae").replace(/ö/g, "oe").replace(/ü/g, "ue").replace(/\s/g, "_")

        # read slides
        slides = Math.max(1, page.evaluate(-> $('.slides section').length))
        console.log("rendering " + link + " with " + slides + " slide(s)")

        # calc output file name
        if(link.indexOf("/glossary") >= 0)
          outpath = "glossary/" + link.split("/")[3]
        else
          url = page.evaluate(-> document.location.href).replace(/\/#\//g, "")
          code = url.substr(url.lastIndexOf("/") + 1)
          outpath = link.split("/")[2] + "/" + link.split("/")[3] + "/" + code + "_" + title

        # capture each slide
        captureSlides(page, slides, slides, outpath, () ->
          page.release()
          next()
        )
      )
    else
      next()

getPDF = (page, outpath, cb) ->
  outpath = "tmp/" + outpath + ".pdf"
  console.log(" - " + outpath)
  page.render(outpath)
  cb()

getUrl = (page) ->
  page.evaluate (-> document.location.href)

getTitle = (page) ->
  page.evaluate (-> document.title)

String.prototype.endsWith = (suffix) ->
    this.indexOf(suffix, this.length - suffix.length) != -1


#######################################
## MAIN

page = webpage.create()

#page.onConsoleMessage = (msg) ->
#  console.log msg

page.onLoadFinished = (status) ->
  url = getUrl(page)
  if(url.endsWith("/glossary") || url.endsWith("/slides"))
    links = page.evaluate (-> Array::map.call ($(".slides a:not(.todo)")), (e) -> $(e).attr("href"))
    captureLinks(links, () ->
      phantom.exit()
    )
  else
    if(status == "success")
      if(url.indexOf("/glossary") >= 0 || url.indexOf("/slides") >= 0)
        captureLinks([startPath], () ->
          phantom.exit()
        )
    else
      phantom.exit()

page.open(createUrl(startPath))