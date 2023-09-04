#! /bin/bash

rm -rf copied_project

cat base.yml > _quarto.yml

files=$(ls ../project/docs/*qmd | sed -e 's/^..\///' | sort)
echo $files

for file in $files
do
    target=chapters/$(echo $file | awk -F'/' '{print $NF}' | sed -e 's/_quarto\.qmd/.qmd/')
    source="../"$file
    cp $source $target
    echo "    - "$target >> _quarto.yml
done

cat _quarto.yml

quarto preview
