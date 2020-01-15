     D A1              S              4    VARYING
     D A2              S              4    VARYING
     D A3              S              4    VARYING
     D ARR1            S              4    DIM(3) VARYING
     D MSG             S             12
     **-------------------------------------------------------------------
     C                   EVAL      A1      = 'AB'
     C                   EVAL      A2      = 'CD'
     C                   EVAL      A3      = 'EF'
     C                   EVAL      MSG = A1      + A2      + A3
     C                   DSPLY                   MSG
     **-------------------------------------------------------------------
     C                   EVAL      MSG = *BLANKS
     C                   EVAL      ARR1(1) = 'AB'
     C                   EVAL      ARR1(2) = 'CD'
     C                   EVAL      ARR1(3) = 'EF'
     C                   EVAL      MSG = ARR1(1) + ARR1(2) + ARR1(3)
     C                   DSPLY                   MSG
     **-------------------------------------------------------------------
     C                   SETON                                          LR
