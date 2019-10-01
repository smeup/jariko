     D DSP             S             50
     D  AAA100         S            100
     D  N1             S              9  0
     D  N2             S             21  6
     C                   EVAL      AAA100='910'
     C                   EVAL      N1=%INT(AAA100)
     C                   EVAL      DSP=%CHAR(N1)
     C                   DSPLY                   DSP
     **********************************************************************
     C                   EVAL      AAA100='5602,78'
     C                   EVAL      N1=%INT(AAA100)
     C                   EVAL      DSP=%CHAR(N1)
     C                   DSPLY                   DSP
     **********************************************************************
     C                   EVAL      AAA100='1234.78'
     C                   EVAL      N1=%INT(AAA100)
     C                   EVAL      DSP=%CHAR(N1)
     C                   DSPLY                   DSP
      **********************************************************************
     C                   EVAL      AAA100='-910'
     C                   EVAL      N1=%INT(AAA100)
     C                   EVAL      DSP=%CHAR(N1)
     C                   DSPLY                   DSP
      **********************************************************************
     C                   EVAL      AAA100='-5602,78'
     C                   EVAL      N1=%INT(AAA100)
     C                   EVAL      DSP=%CHAR(N1)
     C                   DSPLY                   DSP
      **********************************************************************
     C                   EVAL      AAA100='910-'
     C                   EVAL      N1=%INT(AAA100)
     C                   EVAL      DSP=%CHAR(N1)
     C                   DSPLY                   DSP
      **********************************************************************
     C                   EVAL      N2=12326,789
    MU* VAL1(N1) VAL2(12326) COMP(EQ)
     C                   EVAL      N1=%INT(N2)
     C                   EVAL      DSP=%CHAR(N1)
     C                   DSPLY                   DSP
      **********************************************************************
     C                   SETON                                          LR
