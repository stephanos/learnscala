#!/bin/sh

# cleanup
./clean.sh

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

# prepare exercises
./exercises.sh

# bundle to ZIP
./bundle.sh