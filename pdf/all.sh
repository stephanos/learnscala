#!/bin/sh

# cleanup
rm -Rf tmp/
mkdir -p tmp/

rm -Rf out/
mkdir -p out/

# generate PDFs
./slides.sh
#./glossary.sh

# merge PDFs
./slides-merge.sh
#./glossary-merge.sh

# copy extras
mkdir -p out/extra
cp -R ext/* out/extra/

# render markdown
./md.rb

# bundle to ZIP
./bundle.sh