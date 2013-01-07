#!/bin/sh

# cleanup
rm -Rf out/slides
mkdir -p out/slides

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
        outdir="out/slides/$dirname"
        mkdir -p $outdir
        out="$outdir/$name.pdf"

        # merge
        echo " - $out"
        pdftk $f/*.pdf2 output $PWD/$out
done