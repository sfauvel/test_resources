#!/bin/bash

cp -r ./scripts/images ./reports
FILENAME=$1

unset STYLE_CSS
NB_CSS=$(ls *.css | wc -l)
case $NB_CSS in
  0)
    echo "No CSS file";;
  1)
    echo "One CSS file:$(ls *.css)"
     STYLE_CSS=$(ls *.css)
     ;;
  *) echo "Too many CSS files: $NB_CSS";;
esac


if [ -z $STYLE_CSS ]
then
  asciidoctor -r asciidoctor-diagram $FILENAME
else
  asciidoctor -r asciidoctor-diagram -a linkcss -a stylesheet=${STYLE_CSS} -a stylesdir=. ${FILENAME}
fi

asciidoctor -r asciidoctor-pdf -r asciidoctor-diagram -b pdf -o ${FILENAME%.*}.pdf ${FILENAME}
