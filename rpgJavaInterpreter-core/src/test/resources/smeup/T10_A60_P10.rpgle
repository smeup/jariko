     D £DBG_Str        S             150         VARYING
     D A60_D1          S             10
     D A60_D2          S              2  0
     D A60_D3          DS
     D  A60_DA1                       5
     D  A60_DA2                      45

     C                   CALL      'MULANGTB10'
     C                   PARM                    A60_D1           10
     C                   PARM      1             A60_D2            2 0
     C                   PARM                    A60_D3           50
     C                   EVAL      £DBG_Str='CALL_1('+A60_D1+', '
     C                                     +%CHAR(A60_D2)
     C                                     +', '+A60_D3+') '
     C     £DBG_Str      DSPLY