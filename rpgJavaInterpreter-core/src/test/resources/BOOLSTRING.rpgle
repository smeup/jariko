     D  B              S              1N
     D  VAR1           S              1    INZ('X')
     C                   EVAL      B=VAR1='AAAAAA'
     C                   IF        B='1'
     C     'B=1'         dsply
     C                   ELSE
     C     'B<>1'        dsply
     C                   ENDIF
     *********************************************************************
     C                   IF        B='0 '
     C     'B=0'         dsply
     C                   ELSE
     C     'B<>0'        dsply
     C                   ENDIF
     C     B             dsply
     *********************************************************************
     C                   SETON                                          LR
