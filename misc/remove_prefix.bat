rem Utility for removing a prefix from file names.
rem We can use it to remove the file name from members downloaded from AS400
rem Usage:
rem Remove_Prefix.bat  prefix  fileMask
rem For example, in order to remove the QILEGEN. prefix, use:
rem remove_prefix.bat QILEGEN. *.*

setlocal
for %%A in ("%~1%~2") do (
  set "fname=%%~A"
  call ren "%%fname%%" "%%fname:*%~1=%%"
)