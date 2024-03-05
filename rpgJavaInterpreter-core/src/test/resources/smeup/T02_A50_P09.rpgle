       /COPY QILEGEN,MULANG_D_D
     D A50_A81         S                   LIKE(£DBG_I_Fun)
     D A50_N81         S                   LIKE(£DBG_I_Num)
     D A50_V81         S                   LIKE(£DBG_O_Str)

     D A50_A91         S                   LIKE(A50_A81)
     D A50_N91         S                   LIKE(A50_N81)
     D A50_V91         S                   LIKE(A50_V81)

     D* DS con overlay e campi definiti singolarmente
     C                   EVAL      A50_A91='Funzione'
     C                   EVAL      A50_N91=1234567
     C                   EVAL      A50_V91='Funzione'
     C                   EVAL      £DBG_Str= 'A50_A91('+A50_A91+')'
     C                                     +' A50_N91('+%CHAR(A50_A91)+')'
     C                                     + 'A50_V91('+A50_V91+')'
     C     £DBG_Str      DSPLY
