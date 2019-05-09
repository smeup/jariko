# Windows Power Shell script to convert RPG sources downloaded from AS400 to UTF8
# The first parameter is the name of the file to convert
param( [string] $dirName )

If ([string]::IsNullOrEmpty($dirName)) { $dirName = Read-Host 'Please enter a directory name' }
If ([string]::IsNullOrEmpty($dirName)) { throw "A directory name is mandatory" }

Set-Location $dirName

Get-ChildItem $dirName |
        Foreach-Object {
            $outfile = "$_.rpgle"
            get-content -Path $_ | out-file $outfile -encoding utf8
        }

pause "Press any key to continue..."