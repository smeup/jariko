      *---------------------------------------------------------------
      * Test result assignment by OCCUR
      *---------------------------------------------------------------
     D DS1             DS                  OCCURS(50)
     D  FLDA                   1      5
     D  FLDB                   6     80

     D RES             S              1  0

    MU* VAL1(RES) VAL2(1) COMP(EQ)
     C                   OCCUR     DS1           RES
     C     10            OCCUR     DS1
    MU* VAL1(RES) VAL2(10) COMP(EQ)
     C                   OCCUR     DS1           RES