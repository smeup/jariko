     V* ==============================================================
     D* 01/07/22  Issue #302
     V* ==============================================================
     
     D                 DS
     D £K10SK                      1050    DIM(200)
     D £K10S_NM                      50    OVERLAY(£K10SK:1)                    Name
     D £K10S_VA                    1000    OVERLAY(£K10SK:*NEXT)                Value

     D $K10SK          S                   LIKE(£K10SK) DIM(%ELEM(£K10SK))
     C                   EVAL     £K10SK = $K10SK
     C      £K10S_NM(1)  DSPLY
     C      £K10S_VA(1)  DSPLY

     C     *ENTRY        PLIST  
     C                   PARM                    $K10SK