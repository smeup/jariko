   COP* *NOUI
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 21/08/19  001071  BMA Creazione
     V*=====================================================================
     V* OBIETTIVO
     V*  Programma finalizzato ai test su campi numerici (zoned, packed, integer, binary)
     V*
     V*=====================================================================
      * Qui alcuni esempi dei packed più usati in smeup. In realtà possono esistere tutte le
      * combinazioni con precisione massima 63
     D  PAC040         S              4P 0
     D  PAC060         S              6P 0
     D  PAC080         S              8P 0
     D  PAC090         S              9P 0
     D  PAC113         S             11P 3
     D  PAC122         S             12P 2
     D  PAC155         S             15P 5
     D  PAC216         S             21P 6
     D  PAC309         S             30P 9
     D  PAC5020        S             50P20
      * Qui alcuni esempi degli zoned più usati in smeup. In realtà possono esistere tutte le
      * combinazioni con precisione massima 63
     D  ZON040         S              4S 0
     D  ZON060         S              6S 0
     D  ZON080         S              8S 0
     D  ZON090         S              9S 0
     D  ZON113         S             11S 3
     D  ZON122         S             12S 2
     D  ZON155         S             15S 5
     D  ZON216         S             21S 6
     D  ZON309         S             30S 9
     D  ZON5020        S             50S20
      * Binary da 2 byte
     D  BIN002         S              2B 0
      * Binary da 4 byte
     D  BIN004         S              4B 0
      * Integer da 1 byte
     D  INT001         S              3I 0
      * Integer da 1 byte unsigned
     D  UNS001         S              3U 0
      * Integer da 2 byte
     D  INT002         S              5I 0
      * Integer da 2 byte unsigned
     D  UNS002         S              5U 0
      * Integer da 4 byte
     D  INT004         S             10I 0
      * Integer da 4 byte unsigned
     D  UNS004         S             10U 0
      * Integer da 8 byte
     D  INT008         S             20I 0
      * Integer da 8 byte unsigned
     D  UNS008         S             20U 0
      *
      * Utilizzo *HIVAL e *LOVAL insieme alle annotazioni per testare il range di valori validi
    MU* VAL1(PAC040) VAL2(9999) COMP(EQ)
     C                   EVAL      PAC040=*HIVAL
    MU* VAL1(PAC040) VAL2(-9999) COMP(EQ)
     C                   EVAL      PAC040=-9999
    MU* VAL1(PAC060) VAL2(999999) COMP(EQ)
     C                   EVAL      PAC060=999999
    MU* VAL1(PAC060) VAL2(-999999) COMP(EQ)
     C                   EVAL      PAC060=-999999
    MU* VAL1(PAC080) VAL2(99999999) COMP(EQ)
     C                   EVAL      PAC080=99999999
    MU* VAL1(PAC080) VAL2(-99999999) COMP(EQ)
     C                   EVAL      PAC080=-99999999
    MU* VAL1(PAC090) VAL2(999999999) COMP(EQ)
     C                   EVAL      PAC090=*HIVAL
    MU* VAL1(PAC090) VAL2(-999999999) COMP(EQ)
     C                   EVAL      PAC090=*LOVAL
    MU* VAL1(PAC113) VAL2(99999999,999) COMP(EQ)
     C                   EVAL      PAC113=*HIVAL
    MU* VAL1(PAC113) VAL2(-99999999,999) COMP(EQ)
     C                   EVAL      PAC113=*LOVAL
    MU* VAL1(PAC122) VAL2(9999999999,99) COMP(EQ)
     C                   EVAL      PAC122=*HIVAL
    MU* VAL1(PAC122) VAL2(-9999999999,99) COMP(EQ)
     C                   EVAL      PAC122=*LOVAL
    MU* VAL1(PAC155) VAL2(9999999999,99999) COMP(EQ)
     C                   EVAL      PAC155=*HIVAL
    MU* VAL1(PAC155) VAL2(-9999999999,99999) COMP(EQ)
     C                   EVAL      PAC155=*LOVAL
    MU* VAL1(PAC216) VAL2(999999999999999,999999) COMP(EQ)
     C                   EVAL      PAC216=*HIVAL
    MU* VAL1(PAC216) VAL2(-999999999999999,999999) COMP(EQ)
     C                   EVAL      PAC216=*LOVAL
    MU* VAL1(PAC309) VAL2(999999999999999999999,999999999) COMP(EQ)
     C                   EVAL      PAC309=*HIVAL
    MU* VAL1(PAC309) VAL2(-999999999999999999999,999999999) COMP(EQ)
     C                   EVAL      PAC309=*LOVAL
    MU* VAL1(PAC5020) VAL2(999999999999999999999999999999,99999999999999999999) COMP(EQ)
     C                   EVAL      PAC5020=*HIVAL
    MU* VAL1(PAC5020) VAL2(-999999999999999999999999999999,99999999999999999999) COMP(EQ)
     C                   EVAL      PAC5020=*LOVAL
    MU* VAL1(ZON040) VAL2(9999) COMP(EQ)
     C                   EVAL      ZON040=*HIVAL
    MU* VAL1(ZON040) VAL2(-9999) COMP(EQ)
     C                   EVAL      ZON040=*LOVAL
    MU* VAL1(ZON060) VAL2(999999) COMP(EQ)
     C                   EVAL      ZON060=*HIVAL
    MU* VAL1(ZON060) VAL2(-999999) COMP(EQ)
     C                   EVAL      ZON060=*LOVAL
    MU* VAL1(ZON080) VAL2(99999999) COMP(EQ)
     C                   EVAL      ZON080=*HIVAL
    MU* VAL1(ZON080) VAL2(-99999999) COMP(EQ)
     C                   EVAL      ZON080=*LOVAL
    MU* VAL1(ZON090) VAL2(999999999) COMP(EQ)
     C                   EVAL      ZON090=*HIVAL
    MU* VAL1(ZON090) VAL2(-999999999) COMP(EQ)
     C                   EVAL      ZON090=*LOVAL
    MU* VAL1(ZON113) VAL2(99999999,999) COMP(EQ)
     C                   EVAL      ZON113=*HIVAL
    MU* VAL1(ZON113) VAL2(-99999999,999) COMP(EQ)
     C                   EVAL      ZON113=*LOVAL
    MU* VAL1(ZON122) VAL2(9999999999,99) COMP(EQ)
     C                   EVAL      ZON122=*HIVAL
    MU* VAL1(ZON122) VAL2(-9999999999,99) COMP(EQ)
     C                   EVAL      ZON122=*LOVAL
    MU* VAL1(ZON155) VAL2(9999999999,99999) COMP(EQ)
     C                   EVAL      ZON155=*HIVAL
    MU* VAL1(ZON155) VAL2(-9999999999,99999) COMP(EQ)
     C                   EVAL      ZON155=*LOVAL
    MU* VAL1(ZON216) VAL2(999999999999999,999999) COMP(EQ)
     C                   EVAL      ZON216=*HIVAL
    MU* VAL1(ZON216) VAL2(-999999999999999,999999) COMP(EQ)
     C                   EVAL      ZON216=*LOVAL
    MU* VAL1(ZON309) VAL2(999999999999999999999,999999999) COMP(EQ)
     C                   EVAL      ZON309=*HIVAL
    MU* VAL1(ZON309) VAL2(-999999999999999999999,999999999) COMP(EQ)
     C                   EVAL      ZON309=*LOVAL
    MU* VAL1(ZON5020) VAL2(999999999999999999999999999999,99999999999999999999) COMP(EQ)
     C                   EVAL      ZON5020=*HIVAL
    MU* VAL1(ZON5020) VAL2(-999999999999999999999999999999,99999999999999999999) COMP(EQ)
     C                   EVAL      ZON5020=*LOVAL
    MU* VAL1(BIN002) VAL2(99) COMP(EQ)
     C                   EVAL      BIN002=*HIVAL
    MU* VAL1(BIN002) VAL2(-99) COMP(EQ)
     C                   EVAL      BIN002=*LOVAL
    MU* VAL1(BIN004) VAL2(9999) COMP(EQ)
     C                   EVAL      BIN004=*HIVAL
    MU* VAL1(BIN004) VAL2(-9999) COMP(EQ)
     C                   EVAL      BIN004=*LOVAL
    MU* VAL1(INT001) VAL2(127) COMP(EQ)
     C                   EVAL      INT001=*HIVAL
    MU* VAL1(INT001) VAL2(-128) COMP(EQ)
     C                   EVAL      INT001=*LOVAL
    MU* VAL1(UNS001) VAL2(255) COMP(EQ)
     C                   EVAL      UNS001=*HIVAL
    MU* VAL1(UNS001) VAL2(0) COMP(EQ)
     C                   EVAL      UNS001=*LOVAL
    MU* VAL1(INT002) VAL2(32767) COMP(EQ)
     C                   EVAL      INT002=*HIVAL
    MU* VAL1(INT002) VAL2(-32768) COMP(EQ)
     C                   EVAL      INT002=*LOVAL
    MU* VAL1(UNS002) VAL2(65535) COMP(EQ)
     C                   EVAL      UNS002=*HIVAL
    MU* VAL1(UNS002) VAL2(0) COMP(EQ)
     C                   EVAL      UNS002=*LOVAL
    MU* VAL1(INT004) VAL2(2147483647) COMP(EQ)
     C                   EVAL      INT004=*HIVAL
    MU* VAL1(INT004) VAL2(-2147483648) COMP(EQ)
     C                   EVAL      INT004=*LOVAL
    MU* VAL1(UNS004) VAL2(4294967295) COMP(EQ)
     C                   EVAL      UNS004=*HIVAL
    MU* VAL1(UNS004) VAL2(0) COMP(EQ)
     C                   EVAL      UNS004=*LOVAL
    MU* VAL1(INT008) VAL2(9223372036854775807) COMP(EQ)
     C                   EVAL      INT008=*HIVAL
    MU* VAL1(INT008) VAL2(-9223372036854775808) COMP(EQ)
     C                   EVAL      INT008=*LOVAL
      *
     C                   SETON                                        LR
