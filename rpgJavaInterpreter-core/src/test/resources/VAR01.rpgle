     D Var1            S             12    INZ('  AAA ')
     D Value1          S             12
     C                   EVAL      VALUE1 = %TRIM(VAR1) +':'
     C                   IF        VALUE1 = '  AAA:'
     C     'EQ'          dsply
     C                   else
     C     'NOT EQ'      dsply
     C                   endif
     C                   SETON                                          LR
