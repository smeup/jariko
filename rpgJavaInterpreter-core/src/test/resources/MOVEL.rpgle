     DSTR1             S              1    INZ('A')
     DSTR5             S              5    INZ('ABCDE')
     DSTR10            S             10    INZ('ABCDEFGHIJ')
     DSTR15            S             15    INZ('XXXX')
      *
     DNUMZ3            S              3  0 INZ(123)
     DNUMP3            S              3P 0 INZ(456)
      *
     DSTRVAR15         S             15    INZ('YY') VARYING
      *
     DDEC52            S              5  2 INZ(123.45)
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
     DRES10            S             10    INZ('WV') VARYING
     DRES11            S              5    INZ('ZYXWV')
     DRES12            S              5P 2 INZ(999.99)
     DRES13            S              5P 1 INZ(0)
      *
      * String-String MOVEL('A', 'ZYXWV') => 'AYXWV'
     C                   MOVEL     STR1          RES1
     C     RES1          DSPLY
      *
      * String-String MOVEL('ABCDE', 'ZYXWV') => 'ABCDE'
     C                   MOVEL     STR5          RES2
     C     RES2          DSPLY
      *
      * String-String MOVEL('ABCDEFGHIJ', 'ZYXWV') => 'ABCDE'
     C                   MOVEL     STR10         RES3
     C     RES3          DSPLY
      *
      * String-String MOVEL('XXXX           ', 'ZYXWV') => 'XXXX '
     C                   MOVEL     STR15         RES4
     C     RES4          DSPLY
      *
      * Number(Zoned)-String MOVEL(123, 'ZYXWV') => '123WV'
     C                   MOVEL     NUMZ3         RES5
     C     RES5          DSPLY
      *
      * Number(Packed)-String MOVEL(456, 'ZYXWV') => '456WV'
     C                   MOVEL     NUMP3         RES6
     C     RES6          DSPLY
      *
      * Number(Zoned)-Number(Zoned) MOVEL(123, 99999) => '12399'
     C                   MOVEL     NUMZ3         RES7
     C     RES7          DSPLY
      *
      * Number(Packed)-Number(Packed) MOVEL(456, 99999) => '45699'
     C                   MOVEL     NUMP3         RES8
     C     RES8          DSPLY
      *
      * String(Varying)-String MOVEL('YY', 'ZYXWV') => 'YYYWV'
     C                   MOVEL     STRVAR15      RES9
     C     RES9          DSPLY
      *
      * String-String(Varying) MOVEL('ABCDE', 'YY') => 'AB'
     C                   MOVEL     STR5          RES10
     C     RES10         DSPLY
      *
      * Decimal-String MOVEL(123.45, 'ZYXWV') => '12345'
     C                   MOVEL     DEC52         RES11
     C     RES11         DSPLY
      *
      * Decimal-Decimal MOVEL(123.45, 999.99) => '123.45'
     C                   MOVEL     DEC52         RES12
     C     RES12         DSPLY
      *
      * Decimal-Decimal MOVEL(123.45, 0000.0) => '1234.5'
     C                   MOVEL     DEC52         RES13
     C     RES13         DSPLY
      *
     C                   SETON                                          LR