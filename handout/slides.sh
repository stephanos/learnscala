#!/bin/sh

# cleanup
rm -Rf tmp/slides
mkdir -p tmp/slides

# create PDFs
phantomjs --disk-cache=no script.coffee "/app/slides"