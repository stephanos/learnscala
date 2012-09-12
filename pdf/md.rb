#!/usr/bin/env ruby

require 'pdfkit'
require 'github/markup'

pwd = Dir.getwd

# read stylesheet
css = File.read(Dir.getwd + "/github.css")
css2 = "<style type='text/css'>" + css + "</style>"

# read MD and convert to HTML
text = File.read(pwd + "/../exercises/LINKS.md")
text2 = GitHub::Markup.render("README.md", text).
        gsub("ö", "&ouml;").gsub("Ö", "&Ouml;").gsub("ä", "&auml;").gsub("Ä", "&Auml;").
        gsub("ü", "&uuml;").gsub("Ü", "&Uuml;").gsub("ß", "&szlig;")

File.open(pwd + "/out/linksammlung.html", 'w') { |file| file.write(css2 + text2) }

# read HTML and convert to PDF
#kit = PDFKit.new(text2, :page_size => 'A4')
#kit.stylesheets << pwd + "/github.css"
#kit.stylesheets << pwd + "/github.my.css"
#file = kit.to_file(pwd + "/out/linksammlung.pdf")