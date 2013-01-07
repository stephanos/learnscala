#!/bin/sh

cd ../exercises
git archive --format zip --output ../handout/out/exercises.zip master
cd ../handout/out
unzip -o -d exercises exercises.zip 
rm exercises.zip
cd exercises

sbt "project internal" package
mkdir -p lib
cp internal/target/*/*.jar lib/

rm -Rf internal/
rm -Rf src/test/scala/de/learnscala/test/loesungen/

sbt test:compile
sbt clean-all
rm -Rf target
rm -Rf project/target
rm -Rf project/project