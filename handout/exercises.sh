#!/bin/sh

# create ZIP from master
cd ../exercises
git archive --format zip --output ../handout/out/exercises.zip master
cd ../handout/out
rm -Rf exercises/
unzip -o -d exercises exercises.zip 
rm exercises.zip
cd exercises

# compile to make sure it works
sbt test:compile

# pre-compile 'internal'
sbt "project internal" package
mkdir -p public/lib
cp internal/target/*/*.jar public/lib/

rm -Rf internal/
rm -Rf public/src/test/scala/de/learnscala/test/loesungen/

# compile to make sure it (still) works
sbt clean-all test:compile

# clean-up
sbt clean-all
rm -Rf target
rm -Rf project/target
rm -Rf project/project