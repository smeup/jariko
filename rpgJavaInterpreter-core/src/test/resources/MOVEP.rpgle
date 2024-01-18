     DSTR3             S              3    INZ('ABC')
     DSTRVAR5          S              5    INZ('ABC') VARYING
      *
     DNUMZ3            S              3  0 INZ(123)
     DNUMP3            S              3P 0 INZ(456)
      *
     DRES1             S              5    INZ('ZYXWV')
     DRES2             S              5    INZ('ZYXWV')
     DRES3             S              5    INZ('ZYXWV')
     DRES4             S              5P 0 INZ(99999)
     DRES5             S              5    INZ('ZYXWV')
     DRES6             S              5    INZ('YY') VARYING
     DRES7             S              5    DIM(2)
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
      * String(Varying)-String MOVE(P)('ABC', 'ZYXWV') => '  ABC')
     C                   MOVE(P)    STRVAR5       RES5
     C     RES5          DSPLY
      *
      * String-String(Varying) MOVE(P)('ABC', 'YY') => 'BC')
     C                   MOVE(P)    STR3          RES6
     C     RES6          DSPLY
      *
      * String-Array(String) MOVE('ABC', ARRAY('    ', '    ') => ARRAY('  ABC', '  ABC')
     C                   MOVE      STR3              RES7
     C     RES7(1)       DSPLY
     C     RES7(2)       DSPLY
      *
     C                   SETON                                          LR