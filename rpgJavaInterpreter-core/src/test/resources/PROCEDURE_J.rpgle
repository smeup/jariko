      *---------------------------------------------------------------
     DCALL1            PR
     Dp                               2  0
      *---------------------------------------------------------------
      * Main
      *---------------------------------------------------------------
     C                   Z-ADD     1             a                 2 0
     C                   CALLP     CALL1(a)
      * Must be 1
     C     a             DSPLY
      *
     C                   Z-ADD     2             a                 2 0
     C                   CALLP     CALL1(a)
      * Must be 4
     C     a             DSPLY
      *
     C                   Z-ADD     3             a                 2 0
     C                   CALLP     CALL1(a)
      * Must be 9
     C     a             DSPLY
      *
     C                   Z-ADD     4             a                 2 0
     C                   CALLP     CALL1(a)
      * Must be 16-
     C     a             DSPLY
      *
      * Subroutine 1 (same name of one into procedure) but never called
     C     SUBR_1        BEGSR
     C                   EVAL      a=99
     C                   ENDSR
      * Must be 16- again
     C     a             DSPLY
      *
      * Subroutine 2 (same name of one into procedure) but never called
     C     SUBR_2        BEGSR
     C                   EVAL      a=88
     C                   ENDSR
      * Must be 16- again
     C     a             DSPLY
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
     PCALL1            B
     DCALL1            PI
     Dp                               2  0
      *
     C                   SELECT
      * .
     C                   WHEN      p=1
     C                   RETURN
      * .
     C                   WHEN      p=2
     C                   EVAL      p=p*2
      * .
     C                   WHEN      p=3
     C                   EXSR      SUBR_1
      * .
     C                   WHEN      p=4
     C                   EXSR      SUBR_2
      *
     C                   ENDSL
      * Subroutine 1
     C     SUBR_1        BEGSR
     C                   EVAL      p=p*3
     C                   ENDSR
      * Subroutine 2
     C     SUBR_2        BEGSR
     C                   EVAL      p=p*4*-1
     C                   RETURN
      * N.B. following is UNREACHABLE CODE!
     C                   EVAL      p=p*5
     C                   ENDSR
      *
     PCALL1            E