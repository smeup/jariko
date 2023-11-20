     DSTR3             S              3    INZ('ABC')
      *
     DNUMZ3            S              3  0 INZ(123)
     DNUMP3            S              3P 0 INZ(456)
      *
     DRES1             S              5    INZ('ZYXWV')
     DRES2             S              5    INZ('ZYXWV')
     DRES3             S              5    INZ('ZYXWV')
     DRES4             S              5P 0 INZ(99999)
      *
      * String-String - MOVE(P)('ABC', 'ZYXWV') => '  ABC'
     C                   MOVE(P)   STR3          RES1
     C     RES1          DSPLY
      *
      * Number(Zoned)-String MOVE(P)(00123, 'ZYXWV') => '00123')
     C                   MOVE(P)    NUMZ3         RES2
     C     RES2          DSPLY
      *
      * Number(Packed)-String MOVE(P)(00456, 'ZYXWV') => '00456')
     C                   MOVE(P)    NUMP3         RES3
     C     RES3          DSPLY
      *
      * Number(Packed)-Number(packed) MOVE(P)(00456, 99999) => 00456)
     C                   MOVE(P)    NUMP3         RES4
     C     RES4          DSPLY
      *
     C                   SETON                                          LR