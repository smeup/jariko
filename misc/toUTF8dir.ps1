# Windows Power Shell script to convert RPG sources downloaded from AS400 to UTF8
# The first parameter is the name of the file to convert
param( [string] $dirName = $(throw "Please specify a directory name.") )

cd $dirName

Get-ChildItem $dirName |
        Foreach-Object {
            $outfile = "$_.txt"
            get-content -Path $_ | out-file $outfile -encoding utf8
        }