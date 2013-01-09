package views.html.blog

abstract class BlogEntry {

    val langs = List("de")
    val title: String

    lazy val url = encode(title)

    private def encode(s: String) =
        s.toLowerCase
            .replaceAllLiterally(" ", "-")
            .replaceAllLiterally("_", "-")
            .replaceAllLiterally(".", "-")
            .replaceAllLiterally("?", "")
            .replaceAllLiterally("!", "")
            .replaceAllLiterally(".", "")
            .replaceAllLiterally(",", "")
            .replaceAllLiterally(":", "")
            .replaceAllLiterally("#", "")
            .replaceAllLiterally("ö", "oe")
            .replaceAllLiterally("ä", "ae")
            .replaceAllLiterally("ü", "ue")
            .replaceAllLiterally("ß", "ss")
            .replaceAllLiterally("---", "-")
            .replaceAllLiterally("--", "-")
}
