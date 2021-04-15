      *---------------------------------------------------------------
      * Tested features:
      * RIGHT HANDLING OF RECURSIVE CALLING
      * (a procedure can recursively call itself)
      *---------------------------------------------------------------
     D PROC            PR
     D P_NCALL                        3  0
      *
     D NCALL           S              3  0
      *
     D NCALL_CHAR      S              3
      *---------------------------------------------------------------
      * Main
      *---------------------------------------------------------------
     C                   EVAL      NCALL=0
     C                   CALLP     PROC(NCALL)
      * Must be '10'
     C                   EVAL      NCALL_CHAR=%CHAR(NCALL)
     C     NCALL_CHAR    DSPLY
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
     P PROC            B
     D PROC            PI
     D P_NCALL                        3  0
      *
     C                   IF        P_NCALL<10
     C                   EVAL      P_NCALL+=1
     C                   CALLP     PROC(P_NCALL)
     C                   ENDIF
      *
     P PROC            E
      *---------------------------------------------------------------