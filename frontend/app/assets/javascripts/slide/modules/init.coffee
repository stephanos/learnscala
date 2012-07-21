initSlides = ->
  subtleToolbar();

  # init navi
  naviElem = $("#naviSlides")
  $(naviElem).addClass("dropdown")
  naviElemLink = $(naviElem).find("a")
  naviElemLink.replaceWith(
    '<a class="dropdown-toggle" data-toggle="dropdown" href="#naviSlides">
        <span>Slides</span>
        <b class="caret"></b>
     </a>
     <ul class="dropdown-menu">'
  )
  naviElemLinks = naviElem.find("ul")
  $(".reveal section").each(
    (idx, elem) ->
      title = $(elem).data("title")
      chapter = $(elem).data("chapter")

      if(chapter)
        $(naviElemLinks).append('
          <li>
            <a href="#">' + chapter + '</a>
            <ul class="dropdown-menu sub-menu"></ul>
          </li>
        ')

      if(title)
        $(naviElemLinks).find(">li").last().find("ul")
          .append('<li><a href="#/' + idx + '">' + title + '</a></li>')
  )

  # init snippets
  $('.slides div.snippet').each(
    (idx, elem) -> initSnippet(elem)
  )

  # hook-up modal editor
  $("#navi a.openEditor").bind("click",
    (evt) ->
      initModalEditor()
      evt.preventDefault()
  )