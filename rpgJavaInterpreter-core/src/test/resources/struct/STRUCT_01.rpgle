      *---------------------------------------------------------------
      * This test defines a data structure with two fields
      * It should possible to reference the fields using the unqualified
      * name.
      *---------------------------------------------------------------
     DMYDS             DS
     D  FILLER1                       6
     D  PAC030                        3P 0
     D  FILLER2                       6
     D  PAC040                        4P 0
     D  FILLER3                       6
     D  PAC060                        6P 0
     D  FILLER4                       6
     D  PAC080                        8P 0
     D  FILLER5                       6
     D  PAC090                        9P 0
     D  FILLER6                       6
     D  PAC113                       11P 3
     D  FILLER7                       6
     D  PAC122                       12P 2
     D  FILLER8                       6
     D  PAC155                       15P 5
     D  FILLER9                       6
     D  ZON040                        4S 0
     D  FILLER10                      6
     D  ZON060                        6S 0
     D  FILLER11                      6
     D  ZON080                        8S 0
     D  FILLER12                      6
     D  ZON090                        9S 0
     D  FILLER13                      6
     D  ZON113                       11S 3
     D  FILLER14                      6
     D  ZON122                       12S 2
     D  FILLER15                      6
     D  ZON155                       15S 5
     D  FILLER16                      6
     D  INT001                        3I 0
     D  FILLER17                      6
     D  UNS001                        3U 0
     D  FILLER18                      6
     D  INT002                        5I 0
     D  FILLER19                      6
     D  UNS002                        5U 0
     D  FILLER20                      6
     D  INT004                       10I 0
     D  FILLER21                      6
     D  UNS004                       10U 0
     D  FILLER22                      6
     D  INT008                       20I 0
     D  FILLER23                      6
     D  UNS008                       20U 0
     D  FILLER24                      6
      * Binary da 2 byte
     D  BIN002                        2B 0
     D  FILLER25                      6
      * Binary da 4 byte
     D  BIN004                        4B 0

      *---------------------------------------------------------------
      * M A I N
      *---------------------------------------------------------------
     C                   EVAL FILLER1 =  'PAC030'
     C                   EVAL FILLER2 =  'PAC040'
     C                   EVAL FILLER3 =  'PAC060'
     C                   EVAL FILLER4 =  'PAC080'
     C                   EVAL FILLER5 =  'PAC090'
     C                   EVAL FILLER6 =  'PAC122'
     C                   EVAL FILLER7 =  'PAC113'
     C                   EVAL FILLER8 =  'PAC155'
     C                   EVAL FILLER9 =  'ZON040'
     C                   EVAL FILLER10 = 'ZON060'
     C                   EVAL FILLER11 = 'ZON080'
     C                   EVAL FILLER12 = 'ZON090'
     C                   EVAL FILLER13 = 'ZON113'
     C                   EVAL FILLER14 = 'ZON122'
     C                   EVAL FILLER15 = 'ZON155'
     C                   EVAL FILLER16 = 'INT001'
     C                   EVAL FILLER17 = 'UNS001'
     C                   EVAL FILLER18 = 'INT002'
     C                   EVAL FILLER19 = 'UNS002'
     C                   EVAL FILLER20 = 'INT004'
     C                   EVAL FILLER21 = 'UNS004'
     C                   EVAL FILLER22 = 'INT008'
     C                   EVAL FILLER23 = 'UNS008'
     C                   EVAL FILLER24 = 'BIN002'
     C                   EVAL FILLER25 = 'BIN004'
      * Packed 3.0
    MU* VAL1(PAC030) VAL2(999) COMP(EQ)
     C                   EVAL PAC030 = *HIVAL
    MU* VAL1(PAC030) VAL2(-999) COMP(EQ)
     C                   EVAL PAC030 = *LOVAL
      * Packed 4.0
    MU* VAL1(PAC040) VAL2(9999) COMP(EQ)
     C                   EVAL PAC040 = *HIVAL
    MU* VAL1(PAC040) VAL2(-9999) COMP(EQ)
     C                   EVAL PAC040 = *LOVAL
      * Packed 6.0
    MU* VAL1(PAC060) VAL2(999999) COMP(EQ)
     C                   EVAL PAC060 = *HIVAL
    MU* VAL1(PAC060) VAL2(-999999) COMP(EQ)
     C                   EVAL PAC060 = *LOVAL
      * Packed 8.0
    MU* VAL1(PAC080) VAL2(99999999) COMP(EQ)
     C                   EVAL PAC080 = *HIVAL
    MU* VAL1(PAC080) VAL2(-99999999) COMP(EQ)
     C                   EVAL PAC080 = *LOVAL
      * Packed 9.0
    MU* VAL1(PAC090) VAL2(999999999) COMP(EQ)
     C                   EVAL PAC090 = *HIVAL
    MU* VAL1(PAC090) VAL2(-999999999) COMP(EQ)
     C                   EVAL PAC090 = *LOVAL
      * Packed 11.3
    MU* VAL1(PAC113) VAL2(99999999,999) COMP(EQ)
     C                   EVAL      PAC113=*HIVAL
    MU* VAL1(PAC113) VAL2(-99999999,999) COMP(EQ)
      * Packed 12.2
     C                   EVAL      PAC113=*LOVAL
    MU* VAL1(PAC122) VAL2(9999999999,99) COMP(EQ)
     C                   EVAL      PAC122=*HIVAL
    MU* VAL1(PAC122) VAL2(-9999999999,99) COMP(EQ)
      * Packed 15.5
     C                   EVAL      PAC122=*LOVAL
    MU* VAL1(PAC155) VAL2(9999999999,99999) COMP(EQ)
     C                   EVAL      PAC155=*HIVAL
    MU* VAL1(PAC155) VAL2(-9999999999,99999) COMP(EQ)
      * Zoned 4.0
     C                   EVAL      PAC155=*LOVAL
    MU* VAL1(ZON040) VAL2(9999) COMP(EQ)
     C                   EVAL      ZON040=*HIVAL
    MU* VAL1(ZON040) VAL2(-9999) COMP(EQ)
     C                   EVAL      ZON040=*LOVAL
      * Zoned 6.0
    MU* VAL1(ZON060) VAL2(999999) COMP(EQ)
     C                   EVAL      ZON060=*HIVAL
    MU* VAL1(ZON060) VAL2(-999999) COMP(EQ)
     C                   EVAL      ZON060=*LOVAL
      * Zoned 8.0
    MU* VAL1(ZON080) VAL2(99999999) COMP(EQ)
     C                   EVAL      ZON080=*HIVAL
    MU* VAL1(ZON080) VAL2(-99999999) COMP(EQ)
     C                   EVAL      ZON080=*LOVAL
      * Zoned 9.0
    MU* VAL1(ZON090) VAL2(999999999) COMP(EQ)
     C                   EVAL      ZON090=*HIVAL
    MU* VAL1(ZON090) VAL2(-999999999) COMP(EQ)
     C                   EVAL      ZON090=*LOVAL
      * Zoned 11.3
    MU* VAL1(ZON113) VAL2(99999999,999) COMP(EQ)
     C                   EVAL      ZON113=*HIVAL
    MU* VAL1(ZON113) VAL2(-99999999,999) COMP(EQ)
     C                   EVAL      ZON113=*LOVAL
      * Zoned 12.2
    MU* VAL1(ZON122) VAL2(9999999999,99) COMP(EQ)
     C                   EVAL      ZON122=*HIVAL
    MU* VAL1(ZON122) VAL2(-9999999999,99) COMP(EQ)
     C                   EVAL      ZON122=*LOVAL
      * Zoned 15.5
    MU* VAL1(ZON155) VAL2(9999999999,99999) COMP(EQ)
     C                   EVAL      ZON155=*HIVAL
    MU* VAL1(ZON155) VAL2(-9999999999,99999) COMP(EQ)
     C                   EVAL      ZON155=*LOVAL
      * INT 3.0
    MU* VAL1(INT001) VAL2(127) COMP(EQ)
     C                   EVAL      INT001=*HIVAL
    MU* VAL1(INT001) VAL2(-128) COMP(EQ)
     C                   EVAL      INT001=*LOVAL
      * UINT 3.0
    MU* VAL1(UNS001) VAL2(255) COMP(EQ)
     C                   EVAL      UNS001=*HIVAL
    MU* VAL1(UNS001) VAL2(0) COMP(EQ)
     C                   EVAL      UNS001=*LOVAL
      * INT 5.0
    MU* VAL1(INT002) VAL2(32767) COMP(EQ)
     C                   EVAL      INT002=*HIVAL
    MU* VAL1(INT002) VAL2(-32768) COMP(EQ)
     C                   EVAL      INT002=*LOVAL
      * UINT 5.0
    MU* VAL1(UNS002) VAL2(65535) COMP(EQ)
     C                   EVAL      UNS002=*HIVAL
    MU* VAL1(UNS002) VAL2(0) COMP(EQ)
     C                   EVAL      UNS002=*LOVAL
      * INT 10.0
    MU* VAL1(INT004) VAL2(2147483647) COMP(EQ)
     C                   EVAL      INT004=*HIVAL
    MU* VAL1(INT004) VAL2(-2147483648) COMP(EQ)
     C                   EVAL      INT004=*LOVAL
      * UINT 10.0
    MU* VAL1(UNS004) VAL2(4294967295) COMP(EQ)
     C                   EVAL      UNS004=*HIVAL
    MU* VAL1(UNS004) VAL2(0) COMP(EQ)
     C                   EVAL      UNS004=*LOVAL
      * BINARY 2.0
    MU* VAL1(BIN002) VAL2(9999) COMP(EQ)
     C                   EVAL      BIN002=*HIVAL
    MU* VAL1(BIN002) VAL2(-9999) COMP(EQ)
     C                   EVAL      BIN002=*LOVAL
    MU* VAL1(BIN004) VAL2(999999999) COMP(EQ)
     C                   EVAL      BIN004=*HIVAL
    MU* VAL1(BIN004) VAL2(-999999999) COMP(EQ)
     C                   EVAL      BIN004=*LOVAL
     C                   DSPLY                   MYDS
      *
     C                   SETON                                        RT
