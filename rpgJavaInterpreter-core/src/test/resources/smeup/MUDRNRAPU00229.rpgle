     D NTIMES          S             20  0
     D timestamp       S               Z   INZ(Z'1970-01-01-00.00.00.000000')
     D date            S               D   INZ(D'1970-01-01')
     C                   EVAL      NTIMES=%DEC(timestamp)
     C     NTIMES        DSPLY
     C                   EVAL      NTIMES=%DEC(date)
     C     NTIMES        DSPLY