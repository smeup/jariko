     D £DBG_Str        S             10
     D A6X_P9_3        S                   LIKE(A6X_P9_1)
     D A6X_P9_4        S                   LIKE(A6X_P9_2)
      *
     C                   MOVEL(P)  '1'           A6X_P9_1
     C                   MOVEL(P)  '2'           A6X_P9_2
     C                   MOVEL(P)  '3'           A6X_P9_3
     C                   MOVEL(P)  '4'           A6X_P9_4
      *
     C*                   IF        1=1
     C                   CALL      'T10_A60_P09B'                         37
     C                   PARM                    A6X_P9_1         10
     C                   PARM                    A6X_P9_2         10
     C                   PARM                    A6X_P9_3
     C                   PARM                    A6X_P9_4
     C*                   ENDIF
      *
     C                   EVAL      £DBG_Str=%trim(A6X_P9_1)+','+
     C                                      %trim(A6X_P9_2)+','+
     C                                      %trim(A6X_P9_3)+','+
     C                                      %trim(A6X_P9_4)
      *
     C     £DBG_Str      DSPLY
      *
     C                   SETON                                          LR