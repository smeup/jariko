      *---------------------------------------------------------------
      * Tested features:
      * BASIC 'CALLP' STATEMENT
      * 1. Four parameters are passed to procedure;
      * 2. Procedure changes values of 3rd and 4th parameter;
      * 3. Check changes into procedure will affect 3rd and 4th
      *    variables of main program, due to 'reference' behaviour;
      *---------------------------------------------------------------
     DCALL1            PR
     Dp                               2  0
     Dq                               2  0
     Dr                               2  0
     Ds                               2  0
      *
     DTXT              S            50
      *---------------------------------------------------------------
      * Main
      *---------------------------------------------------------------
     C                   Z-ADD     11            a                 2 0
     C                   Z-ADD     22            b                 2 0
     C                   Z-ADD     *zeros        c                 2 0
     C                   Z-ADD     *zeros        d                 2 0
      *
     C                   CALLP     CALL1(a:b:c:d)
      *
     C                   EVAL      TXT='c was *zeros, now must be 33, is:' +
     C                                %CHAR(c)
     C     TXT           DSPLY
      *
     C                   EVAL      TXT='d was *zeros, now must be 44, is:' +
     C                                %CHAR(d)
     C     TXT           DSPLY
     C                   SETON                                        LR
      *---------------------------------------------------------------
     PCALL1            B
     DCALL1            PI
     Dp                               2  0
     Dq                               2  0
     Dr                               2  0
     Ds                               2  0
      *
     DMSG              S            50
      *
     C                   EVAL      MSG='p received must be 11, is:' +
     C                                %CHAR(p)
     C     MSG           DSPLY
      *
     C                   EVAL      MSG='q received must be 22, is:' +
     C                                %CHAR(q)
     C     MSG           DSPLY
      *
     C                   EVAL      MSG='r received must be 0, is:' +
     C                                %CHAR(r)
     C     MSG           DSPLY
      *
     C                   EVAL      r=p+q
     C                   EVAL      MSG='r=p+q must be 33, is:' +
     C                                %CHAR(r)
     C     MSG           DSPLY
      *
     C                   EVAL      s=q*2
     C                   EVAL      MSG='s=q*2 must be 44, is:' +
     C                                %CHAR(s)
     C     MSG           DSPLY
      *
     PCALL1            E