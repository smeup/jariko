     D £DBG_Str        S             2
     D £C6JDSO         DS           512
     D  £C6JO_TO                      2  0 DIM(10)
     D $X              S              5  0
     D §§IMPO          S              6  0
     D T$AG_I          S             21  6 DIM(15)
     C                   EVAL      $X=2
     C                   EVAL      £C6JO_TO($X)=2
     C                   EVAL      T$AG_I(£C6JO_TO($X))=
     C                             T$AG_I(£C6JO_TO($X))+§§IMPO
     C                   EVAL      £DBG_Str='ok'
     C     £DBG_Str      DSPLY
