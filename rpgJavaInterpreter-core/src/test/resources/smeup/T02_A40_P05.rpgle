     D £DBG_Str        S             50
     D £DBG_Pas        S              3
      *
     D A40_DS1         DS            30
     D  DS1_FL1                       2
     D  DS1_FL4                       3  0
      *
     D A40_DS5         DS
     D  DS5_FL1                            LIKE(DS1_FL4)
     D  DS5_FL2                            LIKE(DS1_FL1)
      *
     C                   EXSR      T02_A40_P05
      *
     C                   SETON                                        LR
      *
      * DS with LIKE in the fields
     C     T02_A40_P05   BEGSR
      *
     C                   EVAL      £DBG_Pas='P05'
     C                   EVAL      DS5_FL1 = 333
     C                   EVAL      DS5_FL2 = 'zz'
     C                   EVAL      £DBG_Str=%EDITC(DS5_FL1:'X')
     C                                     +','+%TRIMR(DS5_FL2)
     C     £DBG_Str      DSPLY
      *
     C                   ENDSR
      *
