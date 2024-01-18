     D C10_P1          S             50
     D C10_P2          S              2  0
     D C10_P3          S             50
      *
     D B10_A50         S             50
      *---------------------------------------------------------------------
     C     *ENTRY        PLIST
     C                   PARM                    C10_P1
     C                   PARM                    C10_P2
     C                   PARM                    C10_P3
      *
     C                   IF        C10_P2=1
     C                   EVAL      C10_P1 ='C10_P1: '+%TRIMR(C10_P1)
     C                   EVAL      C10_P2 = 2
     C                   ENDIF
      *
     C                   SETON                                          LR