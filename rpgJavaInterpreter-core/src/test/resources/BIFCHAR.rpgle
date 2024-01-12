     D VAR             S             10     VARYING INZ('ABC       ')
     D STR             S             10     INZ('ABC       ')
     D VAR2            S             10     VARYING INZ('ABC')
     D STR2            S             10     INZ('ABC')
     D N1              S              5  0
     D N2              S              5  2
     D P1              S              5P 0
     D P2              S              5P 2
     D RESULT          S             12
     D CONCAT          S             30
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
      *********************************************************************
     C                   Eval       CONCAT = '('+%CHAR(VAR)+'|'+%CHAR(STR)+')'
      * Expected:
      *  CONCAT = '(ABC       |ABC       )       '
     C     CONCAT        dsply
      *********************************************************************
     C                   Eval       CONCAT = '('+%CHAR(NUM)+'|'+%CHAR(DEC)+')'
      * Expected:
      *  CONCAT = '(1|1.30)                      '
     C     CONCAT        dsply
      *********************************************************************
     C                   Eval       CONCAT = '('+%CHAR(VAR2)+'|'+%CHAR(STR2)+')'
      * Expected:
      *  CONCAT = '(ABC|ABC       )              '
     C     CONCAT        dsply
      **********************************************************************
     C                   Z-ADD     0             ZER               5 2
     C                   Eval       RESULT = '('+%CHAR(ZER)+')'
      * Expected:
      *  RESULT = '(.00)       '
     C     RESULT        dsply
      *********************************************************************
      * Expected:
     C                   Eval       RESULT = 'N1('+%CHAR(N1)+')'
      *  RESULT = 'N1(0)       '
     C     RESULT        dsply
      *********************************************************************
      * Expected:
     C                   Eval       RESULT = 'N2('+%CHAR(N2)+')'
      *  RESULT = 'N2(.00)     '
     C     RESULT        dsply
      *********************************************************************
      * Expected:
     C                   Eval       RESULT = 'P1('+%CHAR(P1)+')'
      *  RESULT = 'P1(0)       '
     C     RESULT        dsply
      *********************************************************************
      * Expected:
     C                   Eval       RESULT = 'P2('+%CHAR(P2)+')'
      *  RESULT = 'P2(.00)      '
     C     RESULT        dsply
      *********************************************************************
     C                   Seton                                        LR