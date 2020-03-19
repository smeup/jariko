     D Msg             S             50
     D N               S              7  0
     D Start           S              1  0 INZ(5)
     D End             S              7  0 INZ(10)
     D I               S              7  0 INZ(0)
      *-----------------------------------------------------------------
     C     Start         DO        End           N
     C                   IF        N = 8
     C                   EVAl      End = 100
     C                   ENDIF
     C                   EVAL      I = I + 1
     C                   ENDDO
     C     'N = '        DSPLY                   N
     C     'I = '        DSPLY                   I
     C                   SETON                                        LR
