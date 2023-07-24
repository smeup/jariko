      * CONST is defined as const and must be resolved
     D VAR             S              5  0
     D CONST           C                   1000

     C                   EVAL      VAR=100
      * given CONST = 1000 it should be displayed 100
     C                   IF        VAR < CONST
     C     VAR           DSPLY
     C                   ENDIF