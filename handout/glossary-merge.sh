#!/bin/sh

# cleanup
rm -Rf out/glossar
mkdir -p out/glossar

echo "merging glossary"

# extract first pages (sometimes a blank page sneaks in)
#for file in tmp/glossary/*/*.pdf ; do pdftk "$file" cat 1 output "${file}2" ; done
#for file in *.pdf ; do pdftk "$file" cat 1 output "${file%.pdf}-page1.pdf" ; done

# combine each
outdir="$PWD/out/glossar/eintraege"
mkdir -p $outdir
for f in `ls -d tmp/glossary/*`
    do
        # dir and file name
        dir=`dirname $f`
        name=`basename $f`
        dirname=`basename $dir`

        # extract first pages
        for pdf in `ls $f` ; do pdftk "$f/$pdf" cat 1 output "$f/${pdf}2" ; done

        # merge
        out="$outdir/$name.pdf"
        echo " - $out"
        pdftk $f/*.pdf2 output $out
done

# combine all
pdftk out/glossar/eintraege/*.pdf output out/glossar/glossar.pdf