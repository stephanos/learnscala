#!/bin/sh

# cleanup
rm -Rf out/
mkdir -p out/

# merge slides
echo "merging slides"
for f in `ls -d tmp/slides/*/*`
    do
        # dir and file name
        dir=`dirname $f`
        name=`basename $f`
        dirname=`basename $dir`

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
        pdftk $f/*.pdf output $PWD/$out
done

# merge glossary
echo "merging glossary"
pdftk tmp/glossary/*/*.pdf output out/glossary.pdf