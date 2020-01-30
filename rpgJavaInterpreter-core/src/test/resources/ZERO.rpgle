     DS3               S              5S 0 INZ(*Zeros)
     DMsg              S             20
     **********************************************************************
     C                   EVAL      Msg = %CHAR(S3)
     C     msg           DSPLY
     **********************************************************************
     C                   EVAL      S3 = 69
     C                   EVAL      Msg = %CHAR(S3)
     C     msg           DSPLY
     **********************************************************************
     C                   EVAL      S3 = *ZERO
     C                   EVAL      Msg = %CHAR(S3)
     C     msg           DSPLY
     C                   SETON                                          LR
