     D B10_P1          S             10
     D B10_P2          S              2  0
     D B10_P3          S             50
      *
     D B10_A50         S             50
      *---------------------------------------------------------------------
     C     *ENTRY        PLIST
     C                   PARM                    B10_P1
     C                   PARM                    B10_P2
     C                   PARM                    B10_P3
      *
     C                   IF        B10_P2=1
     C                   EVAL      B10_A50 ='MULANGTB10: '+%TRIMR(B10_P1)
     C                                     +' chiamata '+%CHAR(B10_P2)
     C                   ENDIF
      *
     C                   EVAL      B10_P3 = B10_A50
      *
     C*                   RETURN
     C                   SETON                                        RT
