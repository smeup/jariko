      * CABEQ factor 1 and 2 constant resolution
      * In this rpgle we will test the constant symbolic resolution when
      * the constant symbol is present in factor 1 or 2 definition
      * The issue is just when constant symbol is in factor 2
     D P$IN35          S              1N
     C                   EVAL      P$IN35=*OFF
     C     P$IN35        CABEQ     *OFF          G1MAIN
     C     G1MAIN        TAG
     C                   EVAL      P$IN35=*ON
      *Warning: *ON is not resolved but Jariko does not throw any error!!!
     C     *ON           CABEQ     P$IN35        G2MAIN
     C     G2MAIN        TAG
     C                   SETON                                          LR