#!/bin/sh

mkdir -p out/bundle
rm -Rf out/bundle/*
cd out

# BUNDLE links
zip bundle/links.zip *.html

# BUNDLE extras
cd ./extra
zip ../bundle/extra.zip *.pdf

# BUNDLE slides
cd ../slides

cd ./1
zip ../../bundle/slides-A.zip *.pdf

cd ../2
zip ../../bundle/slides-B.zip *.pdf

cd ../3
zip ../../bundle/slides-C.zip *.pdf

# BUNDLE exercise
cd ../../../../exercises
git archive --format zip --output ../pdf/out/bundle/exercises.zip master