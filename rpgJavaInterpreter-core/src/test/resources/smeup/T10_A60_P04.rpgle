      **************************************************************************
     D £DBG_Str        S           2560                                         Stringa
      *
     D A60_P1          S             10    INZ('MULANGT10')
     D A60_P2          S              2  0
     D A60_P3          S             50
     C                   CLEAR                   A60_P3
     C                   CALL      'MULANGTC10'
     C                   PARM                    A60_P1
     C                   PARM      1             A60_P2
     C                   EVAL      £DBG_Str='CALL('+A60_P1+', '
     C                                     +%CHAR(A60_P2)
     C                                     +', '+A60_P3+') '