     V* ==============================================================
     D* 01/07/22  Issue #302
     V* ==============================================================
     
     D                 DS
     D £K10SK                      1050    DIM(200)
     D £K10S_NM                      50    OVERLAY(£K10SK:1)                    Name
     D £K10S_VA                    1000    OVERLAY(£K10SK:*NEXT)                Value

     C                   EVAL      £K10S_NM(1) = 'Name'
     C                   EVAL      £K10S_VA(1) = 'Value'

     C                   CALL      'TSTDS02' 
     C                   PARM                    £K10SK