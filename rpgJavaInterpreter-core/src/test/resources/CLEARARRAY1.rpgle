      *-----------------------------------------------------------
      * Simple execution of CLEAR statement on ARRAY
      *-----------------------------------------------------------
     D $ARR            S              1    DIM(5)
     D**£JSP            S              7  0 DIM(50)
      *-----------------------------------------------------------
      * Main
      *-----------------------------------------------------------
     C                   EVAL      $ARR(1) = ''
     C                   EVAL      $ARR(2) = ' '
     C                   EVAL      $ARR(3) = 'A'
     C                   EVAL      $ARR(4) = 'B'
     C                   EVAL      $ARR(5) = 'C'
      *
     C                   EXSR      $DSP
      *
     C                   CLEAR                   $ARR(4)
     C                   EXSR      $DSP
      *
     C                   CLEAR                   $ARR
     C                   EXSR      $DSP
      *
     C                   SETON                                          LR
      *-----------------------------------------------------------
      * Print results
      *-----------------------------------------------------------
     C     $DSP          BEGSR
      *
     C     $ARR(1)       DSPLY
     C     $ARR(2)       DSPLY
     C     $ARR(3)       DSPLY
     C     $ARR(4)       DSPLY
     C     $ARR(5)       DSPLY
      *
     C                   ENDSR
      *-----------------------------------------------------------