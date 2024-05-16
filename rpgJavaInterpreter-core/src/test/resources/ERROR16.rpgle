     V* ==============================================================
     D* 02/05/24
     D* Purpose: In case of files inclusion containing fields defined
     D* with the same name but different size jariko must throw
     D* recognizable errors
     V* ==============================================================

     D* FILE01 and FILE02 meta data are programmatically created in JarikoCallBackTest
     FFILE01    IF   E           K DISK

     D* Jariko should throw an error related the below line of code
     FFILE02    IF   E           K DISK


     