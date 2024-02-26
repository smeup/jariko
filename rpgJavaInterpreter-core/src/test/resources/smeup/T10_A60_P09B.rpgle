     D £DBG_Str        S             10
     D* A60_P9_1        S             10
     D* A60_P9_2        S             10
     D* A60_P9_3        S             10
     D* A60_P9_4        S             10
      *
     C     *ENTRY        PLIST
     C                   PARM                    A60_P9_1          10
     C                   PARM                    A60_P9_2          10
     C                   PARM                    A60_P9_3          10
     C                   PARM                    A60_P9_4          10
      *
     C                   EVAL      £DBG_Str=%trim(A60_P9_1)+','+
     C                                      %trim(A60_P9_2)+','+
     C                                      %trim(A60_P9_3)+','+
     C                                      %trim(A60_P9_4)
      *
     C     £DBG_Str      DSPLY
      *
     C                   MOVEL(P)  '4'           A60_P9_1
     C                   MOVEL(P)  '3'           A60_P9_2
     C                   MOVEL(P)  '2'           A60_P9_3
     C                   MOVEL(P)  '1'           A60_P9_4
      *
     C                   SETON                                        LR