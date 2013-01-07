#!/bin/sh

# cleanup
rm -Rf tmp/slides
mkdir -p tmp/slides

# create PDFs
phantomjs script.coffee "/app/slides"