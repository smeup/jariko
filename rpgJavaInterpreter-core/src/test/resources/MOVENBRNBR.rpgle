     DINT1             S              1  0 INZ(1)
     DINT5             S              5  0 INZ(12345)
     DINT10            S             10  0 INZ(1234567890)
     DINT15            S             15  0 INZ(1234)
      *
     DRES1             S              5  0 INZ(99999)
     DRES2             S              5  0 INZ(99999)
     DRES3             S              5  0 INZ(99999)
     DRES4             S              5  0 INZ(99999)
      *
      * MOVE(1, 99999) => 99991
     C                   MOVE      INT1          RES1
     C     RES1          DSPLY
      * MOVE(12345, 99999) => 12345
     C                   MOVE      INT5          RES2
     C     RES2          DSPLY
      * MOVE(1234567890, 99999) => 67890
     C                   MOVE      INT10         RES3
     C     RES3          DSPLY
      * MOVE(           1234, 99999) =>  1234
     C                   MOVE      INT15         RES4
     C     RES4          DSPLY