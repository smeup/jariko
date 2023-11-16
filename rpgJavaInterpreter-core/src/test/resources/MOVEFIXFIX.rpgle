     DSTR1             S              1    INZ('A')
     DSTR5             S              5    INZ('ABCDE')
     DSTR10            S             10    INZ('ACBDEFGHIJ')
     DSTR15            S             15    INZ('XXXX')
      *
     DRES1             S              5    INZ('ZYXWV')
     DRES2             S              5    INZ('ZYXWV')
     DRES3             S              5    INZ('ZYXWV')
     DRES4             S              5    INZ('ZYXWV')
      *
      * MOVE('A', 'ZYXWV') => 'ZYXWA'
     C                   MOVE      STR1          RES1
     C     RES1          DSPLY
      *
      * MOVE('ABCDE', 'ZYXWV') => 'ABCDE')
     C                   MOVE      STR5          RES2
     C     RES2          DSPLY
      *
      * MOVE('ABCDEFGHIJ', 'ZYXWV') => 'FGHIJ')
     C                   MOVE      STR10         RES3
     C     RES3          DSPLY
      *
      * MOVE('XXXX           ', 'ZYXWV') => '     ')
     C                   MOVE      STR15         RES4
     C     RES4          DSPLY
      *
     C                   SETON                                          LR