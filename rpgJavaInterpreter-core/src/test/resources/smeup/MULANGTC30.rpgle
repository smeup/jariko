     D B10_P1          S             10
     D B10_P2          S             50
      *
     D B10_A50         S             50
      *---------------------------------------------------------------------
     C     *ENTRY        PLIST
     C                   PARM                    B10_P1
     C                   PARM      B10_A50       B10_P2
      *
     C                   EVAL      B10_A50 ='PASSO: '+%TRIMR(B10_P1)
      *
     C                   SETON                                          LR