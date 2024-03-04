     D £DBG_I_Fun      S             10                                         Funzione chiamata
     D £DBG_I_Num      S              7  0                                      Numero esecuzioni
     D £DBG_O_Str      S           2560    VARYING                              Risultato

     D A50_A81         S                   LIKE(£DBG_I_Fun)
     D A50_N81         S                   LIKE(£DBG_I_Num)
     D A50_V81         S                   LIKE(£DBG_O_Str)

     D £DBG_Str        S             150         VARYING

     D* DS con overlay e campi definiti singolarmente
     C                   EVAL      A50_A81='Funzione'
     C                   EVAL      A50_N81=1234567
     C                   EVAL      A50_V81='Funzione'
     C                   EVAL      £DBG_Str= 'A50_A81('+A50_A81+')'
     C                                     +' A50_N81('+%CHAR(A50_A81)+')'
     C                                     + 'A50_V81('+A50_V81+')'
     C     £DBG_Str      DSPLY
