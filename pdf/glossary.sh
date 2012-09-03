#!/bin/sh

# cleanup
rm -Rf tmp/glossary
mkdir -p tmp/glossary

# create PDFs
phantomjs script.coffee "/app/glossary"