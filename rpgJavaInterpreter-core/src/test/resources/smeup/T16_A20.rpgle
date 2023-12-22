     D £DBG_Str        S             52
     D A20_A50         S             50    VARYING
     D A20_A2          S             10A   INZ('Ontario') VARYING
     D A20_A3          S             10A   INZ('Canada') VARYING
     D A20_A4          S             15A   INZ('California')
      *
     C                   EVAL      A20_A50=A20_A2+', ON'
      * Expected '(Ontario, Canada)'
     C                   EVAL      £DBG_Str='('+%REPLACE(A20_A3:A20_A50:10)+')'
     C     £DBG_Str      DSPLY
      *
     C                   EVAL      A20_A50=A20_A2+', '+A20_A3
     C                   EVAL      A20_A50=%REPLACE(A20_A4:A20_A50:
     C                                      %SCAN(',':A20_A50)+2)
     C                   EVAL      £DBG_Str='('+A20_A50+')'
      * Expected '(Ontario, California     )'
     C     £DBG_Str      DSPLY
      *
     C                   EVAL      A20_A50=%REPLACE(', ' + A20_A2: A20_A50:
     C                                      %SCAN(',': A20_A50): 0)
     C                   EVAL      £DBG_Str='('+A20_A50+')'
      * Expected '(Ontario, Ontario, California     )'
     C     £DBG_Str      DSPLY
      *
     C                   EVAL      £DBG_Str='('+%REPLACE('Somewhere else: ':
     C                                       A20_A50: 1: 0)+')'
      * Expected '(Somewhere else: Ontario, Ontario, California     )'
     C     £DBG_Str      DSPLY
      *
     C                   SETON                                        LR