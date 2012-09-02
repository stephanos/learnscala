#!/bin/sh

# cleanup
rm -Rf tmp/
mkdir -p tmp/

# generate PDFs
#casperjs script.coffee
phantomjs script2.coffee
#wkhtmltopdf --javascript-delay 1000 --image-dpi 720 --image-quality 100 --page-width 1024 --page-height 768 --disable-smart-shrinking --orientation Landscape --print-media-type --zoom 1.5 http://localhost:9000/app/glossary/annotationen/#/ test.pdf

# merge PDFs
pdftk tmp/glossary/*.pdf cat output tmp/glossary.pdf