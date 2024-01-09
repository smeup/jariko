     D VAR             S             10     VARYING INZ('ABC       ')
     D STR             S             10     INZ('ABC       ')
     D RESULT          S             12
     *********************************************************************
     C                   Eval       RESULT = %CHAR('Parma')
      * Expected:
      *  RESULT = 'Parma'
     C     RESULT        dsply
     *********************************************************************
     C                   Eval       RESULT = %CHAR('Parma ')
      * Expected:
      *  RESULT = 'Parma '
     C     RESULT        dsply
     *********************************************************************
     C                   Eval       RESULT = %CHAR(' Parma ')
      * Expected:
      *  RESULT = ' Parma '
     C     RESULT        dsply
     *********************************************************************
     C                   Eval       RESULT = '('+%CHAR(STR)+')'
      * Expected:
      *  RESULT = '(ABC          )'
     C     RESULT        dsply
     *********************************************************************
     C                   Eval       RESULT = '('+%CHAR(VAR)+')'
      * Expected:
      *  RESULT = '(ABC          )'
     C     RESULT        dsply
     *********************************************************************
     C                   Z-ADD     1             NUM               2 0
     C                   Eval       RESULT = '('+%CHAR(NUM)+')'
      * Expected:
      *  RESULT = '(1)         '
     C     RESULT        dsply
      **********************************************************************
     C                   Z-ADD     1,3           DEC               3 2
     C                   Eval       RESULT = '('+%CHAR(DEC)+')'
      * Expected:
      *  RESULT = '(1.30)      '
     C     RESULT        dsply
      **********************************************************************
     C                   Seton                                        LR