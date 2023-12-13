     FBRARTI0F  IF   E           K DISK
     D MSG             S            100     VARYING
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
      * Now set the new values
     C                   EVAL       A§ARTI='123456789012345'
     C                   EVAL       A§PESO=123.456
     C                   EVAL       A§DT01=12345678
     C                   EVAL       MSG='A§ARTI('+A§ARTI+') '
     C                              +'A§PESO('+%CHAR(A§PESO)+') '
     C                              +'A§DT01('+%CHAR(A§DT01)+')'
      * Expect 'A§ARTI(123456789012345) A§PESO(123.45600) A§DT01(12345678)'
     C     MSG           DSPLY
      *
      * Now re-blank
     C                   CLEAR                   BRARTIR
     C                   EVAL       MSG='A§ARTI('+A§ARTI+') '
     C                              +'A§PESO('+%CHAR(A§PESO)+') '
     C                              +'A§DT01('+%CHAR(A§DT01)+')'
      * Expect 'A§ARTI(               ) A§PESO(.00000) A§DT01(0)'
     C     MSG           DSPLY
     C                   SETON                                        LR