     D £DBG_Str        S             50          VARYING
     D A50_A1          S              7
     DCQNCOG         E DS                  EXTNAME(CQNCOG0F) INZ

     C     *LIKE         DEFINE    N§TINC        A50_A10
     C     *LIKE         DEFINE    A50_A10       A50_B10
     C                   EVAL      A50_A10='AAA'
     C                   EVAL      A50_B10='BBB'
     C                   EVAL      £DBG_Str= 'A50_A10('+A50_A10+')'
     C                                     +' A50_B10('+A50_B10+')'
     C     £DBG_Str      DSPLY
