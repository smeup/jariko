     DSTR1             S              1    INZ('A')
     DSTR5             S              5    INZ('ABCDE')
     DSTR10            S             10    INZ('ACBDEFGHIJ')
     DSTR15            S             15    INZ('XXXX')
      *
     DNUMZ3            S              3  0 INZ(123)
     DNUMP3            S              3P 0 INZ(456)
      *
     DSTRVAR15         S             15    INZ('YY') VARYING
      *
     DRES1             S              5    INZ('ZYXWV')
     DRES2             S              5    INZ('ZYXWV')
     DRES3             S              5    INZ('ZYXWV')
     DRES4             S              5    INZ('ZYXWV')
     DRES5             S              5    INZ('ZYXWV')
     DRES6             S              5    INZ('ZYXWV')
     DRES7             S              5  0 INZ(99999)
     DRES8             S              5P 0 INZ(99999)
     DRES9             S              5    INZ('ZYXWV')
      *
      * String-String MOVE('A', 'ZYXWV') => 'ZYXWA'
     C                   MOVE      STR1          RES1
     C     RES1          DSPLY
      *
      * String-String MOVE('ABCDE', 'ZYXWV') => 'ABCDE')
     C                   MOVE      STR5          RES2
     C     RES2          DSPLY
      *
      * String-String MOVE('ABCDEFGHIJ', 'ZYXWV') => 'FGHIJ')
     C                   MOVE      STR10         RES3
     C     RES3          DSPLY
      *
      * String-String MOVE('XXXX           ', 'ZYXWV') => '     ')
     C                   MOVE      STR15         RES4
     C     RES4          DSPLY
      *
      * Number(Zoned)-String MOVE(00123, 'ZYXWV') => 'ZY123')
     C                   MOVE      NUMZ3         RES5
     C     RES5          DSPLY
      *
      * Number(Packed)-String MOVE(00456, 'ZYXWV') => 'ZY456')
     C                   MOVE      NUMP3         RES6
     C     RES6          DSPLY
      *
      * Number(Zoned)-Number(Zoned) MOVE(00123, 99999) => '99123')
     C                   MOVE      NUMZ3         RES7
     C     RES7          DSPLY
      *
      * Number(Packed)-Number(Packed) MOVE(00456, 99999) => '99456')
     C                   MOVE      NUMP3         RES8
     C     RES8          DSPLY
      *
      * String-String MOVE('YY', 'ZYXWV') => 'ZYXYY')
     C                   MOVE      STRVAR15      RES9
     C     RES9          DSPLY
      *
     C                   SETON                                          LR