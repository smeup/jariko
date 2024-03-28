     D A20_A20A        S             20    INZ('!ABC!ABCDEF')
     D A20_N20R        S              2  0
     D £DBG_Str        S             100          VARYING

     C                   SETOFF                                       202122
     C     '!':1         SCAN      A20_A20A      A20_N20R                 20
     C                   EVAL      £DBG_Str='RicercaDaPos01('
     C                               +%CHAR(A20_N20R)
     C                               +')_Trovato('+%CHAR(*IN20)+');'
     C     '!':1         SCAN      A20_A20A:2    A20_N20R                 21
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)
     C                               +' RicercaDaPos02('+%CHAR(A20_N20R)
     C                               +')_Trovato('+%CHAR(*IN21)+');'
     C     '!':1         SCAN      A20_A20A:5    A20_N20R                 22
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)
     C                               +' RicercaDaPos05('+%CHAR(A20_N20R)
     C                               +')_Trovato('+%CHAR(*IN22)+');'
     C                   SETOFF                                           20
     C     '!':1         SCAN      A20_A20A:7    A20_N20R                 20
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)
     C                               +' RicercaDaPos07('+%CHAR(A20_N20R)
     C                               +')_Trovato('+%CHAR(*IN20)+');'
     C     £DBG_Str      DSPLY

     C                   SETOFF                                       202122
     C     '!ABCD'       SCAN      A20_A20A      A20_N20R                 20
     C                   EVAL      £DBG_Str='RicercaDaPos01('
     C                               +%CHAR(A20_N20R)
     C                               +')_Trovato('+%CHAR(*IN20)+');'
     C     '!ABCD':2     SCAN      A20_A20A      A20_N20R                 21
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)
     C                               +' RicercaDaPos01('+%CHAR(A20_N20R)
     C                               +')_Trovato('+%CHAR(*IN21)+');'
     C     '!ABCD':5     SCAN      A20_A20A      A20_N20R                 22
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)
     C                               +' RicercaDaPos01('+%CHAR(A20_N20R)
     C                               +')_Trovato('+%CHAR(*IN22)+');'
     C     £DBG_Str      DSPLY


     C                   SETOFF                                           20
     C     '!':1         SCAN      '?!/'         NW20_N20          2 0    20
     C                   EVAL      £DBG_Str='RicercaDaPos01('
     C                               +%CHAR(NW20_N20)
     C                               +')_Trovato('+%CHAR(*IN20)+');'
     C     £DBG_Str      DSPLY