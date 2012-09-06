#!/bin/sh

# cleanup
rm -Rf tmp/
mkdir -p tmp/

rm -Rf out/
mkdir -p out/

# generate PDFs
./slides.sh
./glossary.sh

# merge PDFs
./slides-merge.sh
./glossary-merge.sh
cp -R ./ext/* out/extra/

# bundle to ZIP
./bundle