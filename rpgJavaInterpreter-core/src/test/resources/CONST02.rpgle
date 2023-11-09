      * CONST is defined as const and must be resolved
     D VAR             S              5  0
     D CONS1           C                   1000
     D CONS2           C                   'A'
     D CONS3           C                   CONST(1000)
     D CONS4           C                   CONST('A')
      *
     C                   EVAL      VAR=80
      *
     C                   IF        VAR < CONS1
     C                   EVAL      VAR = VAR + 5
     C                   ENDIF
      *
     C                   IF        CONS2 = 'A'
     C                   EVAL      VAR = VAR + 5
     C                   ENDIF
      *
     C                   IF        VAR < CONS3
     C                   EVAL      VAR = VAR + 5
     C                   ENDIF
      *
     C                   IF        CONS4 = 'A'
     C                   EVAL      VAR = VAR + 5
     C                   ENDIF
      *
     C     VAR           DSPLY
