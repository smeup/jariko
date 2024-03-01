     D  A40_DS1        DS            30
     D  DS1_FL1                       2
     D  DS1_FL2                      10
     D  DS1_FL3                      15
     D  DS1_FL4                       3  0

     D  A40_DS8        DS
     D  DS8_FL1                            LIKE(DS1_FL1)
     D  DS8_FL2                            LIKE(DS1_FL2)
     D  DS8_FL3                            LIKE(DS1_FL3)
     D  DS8_FL4                            LIKE(DS1_FL4)
     D £DBG_Str        S             50          VARYING

     D* DS con overlay e campi definiti singolarmente
     C                   EVAL      DS8_FL1 = 'CN'
     C                   EVAL      DS8_FL2 = 'CLI'
     C                   EVAL      DS8_FL3 = 'AAAAAA'
     C                   EVAL      DS8_FL4 = 333
     C                   EVAL      £DBG_Str=%TRIMR(A40_DS8)
     C     £DBG_Str      DSPLY
