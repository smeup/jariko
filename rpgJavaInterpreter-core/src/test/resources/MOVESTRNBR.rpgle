     DINT1             S              1  0 INZ(1)
     DINT5             S              5  0 INZ(12345)
     DINT10            S             10  0 INZ(1234567890)
     DINT15            S             15  0 INZ(1234)
      *
     DRES1             S              5    INZ('ABCD')
     DRES2             S              5    INZ('ABCD')
     DRES3             S              5    INZ('ABCD')
     DRES4             S              5    INZ('ABCD')
      *
      * MOVE(1, 'ABCD') => 'ABCD1'
     C                   MOVE      INT1          RES1
     C     RES1          DSPLY
      * MOVE(12345, 'ABCD') => '12345'
     C                   MOVE      INT5          RES2
     C     RES2          DSPLY
      * MOVE(1234567890, 'ABCD') => '67890'
     C                   MOVE      INT10         RES3
     C     RES3          DSPLY
      * MOVE(000000000001234, 'ABCD') => '01234'
     C                   MOVE      INT15         RES4
     C     RES4          DSPLY
      *
     C                   SETON                                        LR