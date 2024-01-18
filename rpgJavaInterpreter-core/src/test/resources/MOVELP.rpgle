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
      *
      * String-String - MOVEL(P)('ABC', 'ZYXWV') => 'ABC  '
     C                   MOVEL(P)  STR3          RES1
     C     RES1          DSPLY
      *
      * Number(Zoned)-String MOVEL(P)(123, 'ZYXWV') => '123  ')
     C                   MOVEL(P)  NUMZ3         RES2
     C     RES2          DSPLY
      *
      * Number(Packed)-String MOVEL(P)(456, 'ZYXWV') => '456  ')
     C                   MOVEL(P)  NUMP3         RES3
     C     RES3          DSPLY
      *
      * Number(Packed)-Number(packed) MOVEL(P)(456, 99999) => 45600)
     C                   MOVEL(P)  NUMP3         RES4
     C     RES4          DSPLY
      *
      * String(Varying)-String MOVEL(P)('ABC', 'ZYXWV') => 'ABC  ')
     C                   MOVEL(P)  STRVAR5       RES5
     C     RES5          DSPLY
      *
      * String-String(Varying) MOVEL(P)('ABC', 'YY') => 'AB')
     C                   MOVEL(P)  STR3          RES6
     C     RES6          DSPLY
      *
     C                   SETON                                          LR