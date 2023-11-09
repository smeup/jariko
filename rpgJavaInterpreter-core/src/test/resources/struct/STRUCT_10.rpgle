      *---------------------------------------------------------------
      * Test that OCCUR without both factor1 and result does nothing
      *---------------------------------------------------------------
     D DS1             DS                  OCCURS(50)
     D  FLDA                   1      5
     D  FLDB                   6     10

     C                   EVAL      FLDA = '00001'

    MU* VAL1(FLDA) VAL2('00001') COMP(EQ)
     C                   OCCUR     DS1