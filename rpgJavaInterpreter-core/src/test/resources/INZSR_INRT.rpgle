      *---------------------------------------------------------------
      * Program containing *INZSR subroutine that display HELLO IN RT
      * HELLO IN RT has to be printed just the first time
      *---------------------------------------------------------------
     D  MSG            S             50
     C                   SETON                                          RT
      *--------------------------------------------------------------*
    RD* Initialization routine
      *--------------------------------------------------------------*
     C     *INZSR        BEGSR
     C                   EVAL      MSG='HELLO IN RT'
     C                   DSPLY                   MSG
     C                   ENDSR
