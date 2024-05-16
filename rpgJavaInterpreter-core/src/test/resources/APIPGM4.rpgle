      * Test multiple api inclusion
      * SUBROUTINEA is already defined in APISR but does not must cause a conflict
     D GREETING        S             50          VARYING

     C     SUBROUTINEB   BEGSR
     C                   EVAL      GREETING='General Kenobi'
     C     GREETING      DSPLY
     C                   ENDSR

     C     SUBROUTINEA   BEGSR
     C                   EVAL      GREETING='Hello there!'
     C     GREETING      DSPLY
     C                   ENDSR
      *
       /API APISR
      *
     C                   EXSR      SUBROUTINEA
     C                   EXSR      SUBROUTINEB
     C                   SETON                                        LR