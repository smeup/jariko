      *---------------------------------------------------------------
      * Test OCCURS support
      *---------------------------------------------------------------
     D DS1             DS                  OCCURS(50)
     D  FLDA                   1      5
     D  FLDB                   6     80
      *
     D DS2             DS
     D  FLDC                   1      6
     D  FLDD                   7     11

     C     3             OCCUR     DS1
     C                   EVAL      FLDA = '00003'

     C     10            OCCUR     DS1
     C                   EVAL      FLDA = '00010'
     C                   EVAL      FLDB = 'FLDB_00010'

      * Regression test on standard DS
    MU* VAL1(FLDC) VAL2('FLDC') COMP(EQ)
     C                   EVAL      FLDC = 'FLDC'
     C     FLDC          DSPLY


      * Point to 3rd occurrence
    MU* VAL1(FLDA) VAL2('00003') COMP(EQ)
     C     3             OCCUR     DS1
     C     FLDA          DSPLY


      * Point to 10th occurrence
    MU* VAL1(FLDA) VAL2('00010') COMP(EQ)
    MU* VAL1(FLDB) VAL2('FLDB_00010') COMP(EQ)
     C     10            OCCUR     DS1
     C     FLDA          DSPLY
     C     FLDB          DSPLY
