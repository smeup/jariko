     D  A40_DS1        DS            30
     D  DS1_FL1                       2
     D  DS1_FL2                      10
     D  DS1_FL3                      15
     D  DS1_FL4                       3  0

     D  A40_DS9        DS
     D  DS9_FL1                            LIKE(DS1_FL1)
     D  DS9_FL2                      10
     D  DS9_FL3                            LIKE(DS1_FL3)
     D  DS9_FL4                       3  0
     D £DBG_Str        S             50          VARYING

     D* DS definita con campi in LIKE e campi normali
     C                   EVAL      DS9_FL1 = 'CN'
     C                   EVAL      DS9_FL2 = 'CLI'
     C                   EVAL      DS9_FL3 = 'AAAAAA'
     C                   EVAL      DS9_FL4 = 333
     C                   EVAL      £DBG_Str=%TRIMR(A40_DS9)
     C     £DBG_Str      DSPLY
