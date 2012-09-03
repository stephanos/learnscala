#!/bin/sh

# cleanup
rm -Rf out/
mkdir -p out/

# === merge slides

echo "merging slides"

for f in `ls -d tmp/slides/*/*`
    do
        # dir and file name
        dir=`dirname $f`
        name=`basename $f`
        dirname=`basename $dir`

        # extract first pages
        for pdf in `ls $f` ; do pdftk "$f/$pdf" cat 1 output "$f/${pdf}2" ; done

        # rewrite dir
        #if [[ dirname = "ext" ]]
        #    then dirname="external"
        #fi
        #if [[ dirname = "fp" ]]
        #    then dirname="funktional"
        #fi
        #if [[ dirname = "basic" ]]
        #    then dirname="basis"
        #fi

        # output dir & file
        outdir="out/$dirname"
        mkdir -p $outdir
        out="$outdir/$name.pdf"

        # merge
        echo " - $out"
        pdftk $f/*.pdf2 output $PWD/$out
done


# === glossary

echo "merging glossary"

# extract first pages (sometimes a blank page sneaks in)
for file in tmp/glossary/*/*.pdf ; do pdftk "$file" cat 1 output "${file}2" ; done
#for file in *.pdf ; do pdftk "$file" cat 1 output "${file%.pdf}-page1.pdf" ; done

# combine
pdftk tmp/glossary/*/*.pdf2 output out/glossary.pdf