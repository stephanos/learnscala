define [
  "jquery", "lib/util/underscore", "lib/reveal", "app/editor/init",
  "app/util/timer", "app/util/countdown", "app/util/overlay", "app/util/chart"
], ($, _, Reveal, Editor, Timer, Countdown, Overlay) ->

  class Slide

    constructor: (clazz) ->

      @initSnippets()

      if(!$("body").hasClass("light"))

        # toolbar
        @subtleToolbar()
        @embedEditor()
        @embedDocs()
        @embedGlossary()
        #embedStyle()
        @embedNavi()

        # change li order
        $("#naviLogout").remove()
        li = $("#naviSlides")
        li.parent().append(li)
        li.find(".divider").remove()

        # slide
        new Countdown()
        new Timer()

      else
        $(".fragment").removeClass("fragment")
        $(".slide-end").remove()

      if(!$("body").hasClass("public"))
        new Overlay()


    #######################################################################################################################
    embedNavi: ->
      # init navi
      $('<li id="naviGoTo" class="dropdown">
           <a class="dropdown-toggle" data-toggle="dropdown" href="#naviGoTo">
             <span>Go To</span>
             <b class="caret"></b>
           </a>
           <ul class="dropdown-menu"></ul>
           <span class="divider">|</span>
         </li>').prependTo($("#navi>ul"))

      titles = []
      subtitles = []
      naviElemLinks = $("#naviGoTo").find("ul")

      $(naviElemLinks).append('
        <li class="main">
          <a href="#">Start</a>
        </li>
      ')

      $(".reveal section").each(
        (idx, elem) ->
          title = $(elem).data("title")
          subtitle = $(elem).data("subtitle")

          if(title && _.last(titles) != title)
            titles.push(title)
            $(naviElemLinks).append('
              <li class="main">
                <a href="#/' + idx + '">' + title + '</a>
              </li>
            ') # <ul class="dropdown-menu sub-menu"></ul>

          if(subtitle && _.last(subtitles) != subtitle)
            subtitles.push(subtitle)
            $(naviElemLinks).append('
              <li class="sub">
                <a href="#/' + idx + '"> - ' + subtitle + '</a>
              </li>
            ')
          else
            subtitles.push("<empty>")

            #$(naviElemLinks).find(">li").last().find("ul")
            #  .append('<li><a href="#/' + idx + '">' + subtitle + '</a></li>')
      )


    #######################################################################################################################
    embedEditor: ->
      $("#navi a.openEditor").bind("click",
        (evt) ->
          Editor.initModalEditor()
          evt.preventDefault()
      )


    #######################################################################################################################
    embedGlossary: ->
      $("#naviGlossary").bind("click", () ->
        m = $("#glossaryModal")
        m.bind("shown", (evt) ->
          mbody = $(m).find(".modal-body")
          $(mbody).html('
            <iframe src="/app/glossary" width="100%" height="100%"
                    style="position: absolute; top: 0; bottom: 0; left: 0; right: 0; overflow: hidden"></iframe>
          ')
        )
        m.modal()
        false
      )


    #######################################################################################################################
    initSnippets: ->
      $('.slides div.snippet').each(
        (idx, elem) -> Editor.initSnippet(elem)
      )


    #######################################################################################################################
    embedStyle: ->
      # insert in navi
      $('<li id="naviStyle">
          <a href="#" class="openStyle">
              <span>Style</span>
          </a>
          <span class="divider">|</span>
        </li>').appendTo($("#navi>ul"))

      # bind click event
      $("#naviStyle").bind("click", () ->
        m = $("#styleModal")
        m.bind("shown", (evt) ->
          mbody = $(m).find(".modal-body")
          # TODO
        )
        m.modal()
        false
      )


    #######################################################################################################################
    embedDocs: ->
      # insert in navi
      $('<li id="naviDocs">
            <a href="#" class="openDocs">
                <span>Docs</span>
            </a>
            <span class="divider">|</span>
        </li>').appendTo($("#navi>ul"))

      showDocs = (uri) ->
        url = "http://www.scala-lang.org/api/current/index.html#" + (uri ? "")
        console.log(url)
        m = $("#docsModal")
        m.bind("shown", () ->
          mbody = $(m).find(".modal-body")
          if($(mbody).is(":empty"))
            $(mbody).html('
              <iframe src="' + url + '" width="99.5%" height="99.5%"
                      style="position: absolute; top: 5px; bottom: 5px; left: 5px; right: 5px; overflow: hidden"></iframe>
            ')
          else
            $("#docsModal iframe").attr("src", url)
        )
        m.modal()

      # bind click event
      $("#naviDocs").bind("click", () ->
          showDocs()
          false
      )

      # wire API links
      $("a.api-link").bind("click", (evt) ->
          elem = $(evt.target)
          uri = $(elem).data("uri")
          showDocs(uri)
          false
      )

    subtleToolbar: ->
      $("#navi").addClass("subtle")