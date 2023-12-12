     FBRARTI0F  IF   E           K DISK
     D MSG             S             52
      *
     C                   CLEAR                   BRARTIR
      * String 15
      * Decimal 12 5
      * Decimal 8 0
     C                   EVAL       MSG='A§ARTI('+A§ARTI+') '
     C                              +'A§PESO('+%CHAR(A§PESO)+') '
     C                              +'A§DT01('+%CHAR(A§DT01)+')'
      * Expect 'A§ARTI(               ) A§PESO(.00000) A§DT01(0)'
     C     MSG           DSPLY
      *
     C                   SETON                                        LR