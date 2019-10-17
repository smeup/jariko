   COP* *NOUI
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 26/09/19  001142  BMA Creazione
     V* 26/09/19  V5R1    BERNI Check-out 001142 in SMEDEV
     V*=====================================================================
     V* OBIETTIVO
     V*  Programma finalizzato ai test sulla bif %INT
     V*
     V*=====================================================================
     D*----------------------------------------------------------------
     D  AAA100         S            100
     D DSP             S             50
      *
     D  N1             S              9  0
     D  N2             S             21  6
      *
    MU* VAL1(AAA100) VAL2('910') COMP(EQ)
     C                   EVAL      AAA100='910'
    MU* VAL1(N1) VAL2(910) COMP(EQ)
     C                   EVAL      N1=%INT(AAA100)
     C                   EVAL      DSP=%CHAR(N1)
     C                   DSPLY                   DSP
    MU* VAL1(N1) VAL2(312) COMP(EQ)
     C                   EVAL      N1=%INT('312')
     C                   EVAL      DSP=%CHAR(N1)
     C                   DSPLY                   DSP
    MU* VAL1(AAA100) VAL2('5602,78') COMP(EQ)
     C                   EVAL      AAA100='5602,78'
    MU* VAL1(N1) VAL2(5602) COMP(EQ)
     C                   EVAL      N1=%INT(AAA100)
     C                   EVAL      DSP=%CHAR(N1)
     C                   DSPLY                   DSP
    MU* VAL1(N1) VAL2(12) COMP(EQ)
     C                   EVAL      N1=%INT('12,15')
     C                   EVAL      DSP=%CHAR(N1)
     C                   DSPLY                   DSP
    MU* VAL1(AAA100) VAL2('-910') COMP(EQ)
     C                   EVAL      AAA100='-910'
    MU* VAL1(N1) VAL2(-910) COMP(EQ)
     C                   EVAL      N1=%INT(AAA100)
     C                   EVAL      DSP=%CHAR(N1)
     C                   DSPLY                   DSP
    MU* VAL1(N1) VAL2(-312) COMP(EQ)
     C                   EVAL      N1=%INT('-312')
     C                   EVAL      DSP=%CHAR(N1)
     C                   DSPLY                   DSP
    MU* VAL1(AAA100) VAL2('-5602,78') COMP(EQ)
     C                   EVAL      AAA100='-5602,78'
    MU* VAL1(N1) VAL2(-5602) COMP(EQ)
     C                   EVAL      N1=%INT(AAA100)
     C                   EVAL      DSP=%CHAR(N1)
     C                   DSPLY                   DSP
    MU* VAL1(N1) VAL2(-12) COMP(EQ)
     C                   EVAL      N1=%INT('-12,15')
     C                   EVAL      DSP=%CHAR(N1)
     C                   DSPLY                   DSP
    MU* VAL1(AAA100) VAL2('910-') COMP(EQ)
     C                   EVAL      AAA100='910-'
    MU* VAL1(N1) VAL2(-910) COMP(EQ)
     C                   EVAL      N1=%INT(AAA100)
     C                   EVAL      DSP=%CHAR(N1)
     C                   DSPLY                   DSP
    MU* VAL1(N1) VAL2(-312) COMP(EQ)
     C                   EVAL      N1=%INT('312-')
     C                   EVAL      DSP=%CHAR(N1)
     C                   DSPLY                   DSP
    MU* VAL1(AAA100) VAL2('5602,78-') COMP(EQ)
     C                   EVAL      AAA100='5602,78-'
    MU* VAL1(N1) VAL2(-5602) COMP(EQ)
     C                   EVAL      N1=%INT(AAA100)
     C                   EVAL      DSP=%CHAR(N1)
     C                   DSPLY                   DSP
    MU* VAL1(N1) VAL2(-12) COMP(EQ)
     C                   EVAL      N1=%INT('12,15-')
     C                   EVAL      DSP=%CHAR(N1)
     C                   DSPLY                   DSP
    MU* VAL1(N2) VAL2(12326,789) COMP(EQ)
     C                   EVAL      N2=12326,789
    MU* VAL1(N1) VAL2(12326) COMP(EQ)
     C                   EVAL      N1=%INT(N2)
     C                   EVAL      DSP=%CHAR(N1)
     C                   DSPLY                   DSP
    MU* VAL1(N2) VAL2(-12326,789) COMP(EQ)
     C                   EVAL      N2=-12326,789
    MU* VAL1(N1) VAL2(-12326) COMP(EQ)
     C                   EVAL      N1=%INT(N2)
     C                   EVAL      DSP=%CHAR(N1)
     C                   DSPLY                   DSP
      *
     C                   SETON                                        LR
