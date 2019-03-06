# Windows Power Shell script to convet RPG sources downloaded from AS400 to UTF8
# The first parameter is the name of the file to convert
param( [string] $infile = $(throw "Please specify a filename.") )

$outfile = "$infile.txt"

get-content -Path $infile | out-file $outfile -encoding utf8