#!/bin/sh

# cleanup
rm -Rf tmp/
mkdir -p tmp/

# generate PDFs
./slides.sh
./glossary.sh

#casperjs script.coffee
#wkhtmltopdf --javascript-delay 1000 --image-dpi 720 --image-quality 100 --page-width 1024 --page-height 768 --disable-smart-shrinking --orientation Landscape --print-media-type --zoom 1.5 http://localhost:9000/app/glossary/annotationen/#/ test.pdf

# merge
./merge.sh