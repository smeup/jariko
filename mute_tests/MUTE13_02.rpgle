     V*=====================================================================
     V*  Tests for %EDITW
     V*=====================================================================
     D*----------------------------------------------------------------
     D DSP             S             50
      *
     D  NNN020         S              2  0
     D  NNN040         S              4  0
     D  D6             S              6  0
     D  D8             S              8  0
     D  O6             S              6  0
     D  NNN062         S              6  2
     D  NNN070         S              7  0
     D  NNN064         S              6  4
     D  NNN112         S             11  2
     D  NNN102         S             10  2
     D  NNN051         S              5  1
     D  NNN132         S             13  2
     D  NNN150         S             15  0
     D  NNN142         S             14  2
      *
      *
  019C                   EVAL      NNN020=2
    MU* VAL1(DSP) VAL2(' 2                ') COMP(EQ)
  019C                   EVAL      DSP=%EDITW(NNN020:'  ')
     C     DSP           DSPLY     
  019C                   EVAL      NNN040=217
    MU* VAL1(DSP) VAL2(' 217              ') COMP(EQ)
  061C                   EVAL      DSP=%EDITW(NNN040:'0   ')
     C     DSP           DSPLY     
  019C                   EVAL      D6=011299
    MU* VAL1(DSP) VAL2(' 1/12/99          ') COMP(EQ)
  061C                   EVAL      DSP=%EDITW(D6:'  /  /  ')
     C     DSP           DSPLY     
  019C                   EVAL      D8=20181231
    MU* VAL1(DSP) VAL2('2018/12/31        ') COMP(EQ)
  008C                   EVAL      DSP=%EDITW(D8:'    /  /  ')
     C     DSP           DSPLY     
    MU* VAL1(DSP) VAL2('2018-12-31        ') COMP(EQ)
  008C                   EVAL      DSP=%EDITW(D8:'    -  -  ')
     C     DSP           DSPLY     
  019C                   EVAL      O6=082345
    MU* VAL1(DSP) VAL2(' 8:23:45          ') COMP(EQ)
  008C                   EVAL      DSP=%EDITW(O6:'  :  :  ')
     C     DSP           DSPLY     
    MU* VAL1(DSP) VAL2(' 08:23:45         ') COMP(EQ)
  008C                   EVAL      DSP=%EDITW(O6:'0  :  :  ')
     C     DSP           DSPLY     
    MU* VAL1(DSP) VAL2(' 8.23.45          ') COMP(EQ)
  008C                   EVAL      DSP=%EDITW(O6:'  .  .  ')
     C     DSP           DSPLY     
  019C                   EVAL      NNN070=2345
    MU* VAL1(DSP) VAL2('   2.345          ') COMP(EQ)
  221C                   EVAL      DSP=%EDITW(NNN070:'    .   -')
     C     DSP           DSPLY     
  019C                   EVAL      NNN062=-23,45
    MU* VAL1(DSP) VAL2('  23,45-          ') COMP(EQ)
  221C                   EVAL      DSP=%EDITW(NNN062:'    ,  -')
     C     DSP           DSPLY     
  019C                   EVAL      NNN064=-23,451
    MU* VAL1(DSP) VAL2('23,4510-          ') COMP(EQ)
  221C                   EVAL      DSP=%EDITW(NNN064:'  ,    -')
     C     DSP           DSPLY     
  019C                   EVAL      NNN112=-1234567,89
    MU* VAL1(DSP) VAL2('    1234.567,89-  ') COMP(EQ)
  221C                   EVAL      DSP=%EDITW(NNN112:'        . 0 ,  -')
     C     DSP           DSPLY     
  019C                   EVAL      NNN102=-1234567,89
    MU* VAL1(DSP) VAL2('   1.234.567,89-  ') COMP(EQ)
  221C                   EVAL      DSP=%EDITW(NNN102:'    .   . 0 ,  -')
     C     DSP           DSPLY     
  019C                   EVAL      NNN051=-21,4
    MU* VAL1(DSP) VAL2('  21,4-%          ') COMP(EQ)
  221C                   EVAL      DSP=%EDITW(NNN051:'   0, -%')
     C     DSP           DSPLY     
  019C                   EVAL      NNN132=-1234567,89
    MU* VAL1(DSP) VAL2('    1234,567.89   ') COMP(EQ)
  221C                   EVAL      DSP=%EDITW(NNN132:'        , 0 .  ')
     C     DSP           DSPLY     
  019C                   EVAL      NNN150=-123456789
    MU* VAL1(DSP) VAL2('      123.456.789-') COMP(EQ)
  221C                   EVAL      DSP=%EDITW(NNN150:'         .   .   -')
     C     DSP           DSPLY     
  019C                   EVAL      NNN142=-123456789,12
    MU* VAL1(DSP) VAL2('   123456.789,12- ') COMP(EQ)
  221C                   EVAL      DSP=%EDITW(NNN142:'         . 0 ,  -')
     C     DSP           DSPLY     
     C                   SETON                                        LR