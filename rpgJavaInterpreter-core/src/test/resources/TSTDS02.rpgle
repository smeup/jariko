     V* ==============================================================
     D* 01/07/22  Issue #302
     V* ==============================================================
     
     D                 DS
     D£K10SK                       1050    DIM(200)
     D £K10S_NM                      50    OVERLAY(£K10SK:1)                    Nome
     D £K10S_VA                    1000    OVERLAY(£K10SK:*NEXT)                Valore

     C     *ENTRY        PLIST  
     C                   PARM                    £K10SK      