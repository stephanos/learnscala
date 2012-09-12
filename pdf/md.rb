#!/usr/bin/env ruby

require 'github/markup'

css = File.read(Dir.getwd + "/github.css")
css2 = "<style type='text/css'>" + css + "</style>"

text = GitHub::Markup.render("README.md", File.read(Dir.getwd + "/../exercises/LINKS.md"))
text2 = text.gsub("ö", "&ouml;").gsub("Ö", "&Ouml;").gsub("ä", "&auml;").gsub("Ä", "&Auml;").gsub("ü", "&uuml;").gsub("Ü", "&Uuml;").gsub("ß", "&szlig;")

File.open(Dir.getwd + "/out/linksammlung.html", 'w') { |file| file.write(css2 + text2) }