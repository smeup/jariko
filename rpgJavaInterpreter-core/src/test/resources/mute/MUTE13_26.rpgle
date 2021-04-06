     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 29/03/21  002768  BERNI Creato
     V* 29/03/21  V5R1    BMA   Check-out 002768 in SMEDEV
     V*=====================================================================
     DS1               S             15A
     DS3               S              3A
     DN1               S              5S 0
     DN2               S              5P 0
     DN3               S              5S 2
     DN4               S              5P 2
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      *
      * Declare variable
    MU* VAL1(RESULT) VAL2('               ') COMP(EQ)
     C                   MOVE      *BLANKS       RESULT           15
      *
      * Factor 2 and Result same variable
    MU* VAL1(RESULT) VAL2('               ') COMP(EQ)
     C                   MOVE      RESULT        RESULT
      *
     C                   CLEAR                   S1
     C                   CLEAR                   S3
      *
    MU* VAL1(S1) VAL2('ABABABABABABABA') COMP(EQ)
     C                   MOVE      *ALL'AB'      S1
    MU* VAL1(S2) VAL2('ABABABABABABABA') COMP(EQ)
     C                   EVAL      S2=S1
    MU* VAL1(S2) VAL2('ABABABABABABABA') COMP(EQ)
     C                   MOVE      S2            S2               15
     C                   CLEAR                   S1
      *
    MU* VAL1(S1) VAL2('            051') COMP(EQ)
     C                   MOVE      05.1          S1
      *
    MU* VAL1(S1) VAL2('         TEST_0') COMP(EQ)
     C                   MOVE      'TEST_0'      S1
      *
    MU* VAL1(S1) VAL2('         TEST_1') COMP(EQ)
     C                   MOVE      'TEST_1'      S1
      *
    MU* VAL1(S1) VAL2('         TEST_2') COMP(EQ)
     C                   MOVE      'TEST_2'      S1
    MU* VAL1(S1) VAL2('AAAAAAAAAAAAAAA') COMP(EQ)
     C                   MOVE      *ALL'A'       S1
    MU* VAL1(S1) VAL2('AAAAAAAAAABBBBB') COMP(EQ)
     C                   MOVE      'BBBBB'       S1
    MU* VAL1(S1) VAL2('          BBBBB') COMP(EQ)
     C                   MOVE (P)  'BBBBB'       S1
    MU* VAL1(S1) VAL2('               ') COMP(EQ)
     C                   MOVE      *ALL' '       S1
    MU* VAL1(S1) VAL2('ABCDABCDABCDABC') COMP(EQ)
     C                   MOVE      *ALL'ABCD'    S1
1    C                   IF        S1=*ALL'ABCD'
     C                   MOVE      *ALL' '       S1
1e   C                   ENDIF
    MU* VAL1(S1) VAL2('000000000000000') COMP(EQ)
     C                   MOVE      *OFF          S1
    MU* VAL1(S3) VAL2('000') COMP(EQ)
     C                   MOVE      *OFF          S3
    MU* VAL1(S0) VAL2('111111111111111') COMP(EQ)
     C                   MOVE      *ON           S1
    MU* VAL1(S3) VAL2('111') COMP(EQ)
     C                   MOVE      *ON           S3
    MU* VAL1(S0) VAL2('000000000000000') COMP(EQ)
     C                   MOVE      *ZEROS        S1
    MU* VAL1(S3) VAL2('000') COMP(EQ)
     C                   MOVE      *ZEROS        S3
    MU* VAL1(S0) VAL2('               ') COMP(EQ)
     C                   MOVE      *BLANK        S1
    MU* VAL1(S3) VAL2('   ') COMP(EQ)
     C                   MOVE      *BLANK        S3
      *
     C                   EVAL      N1=12345
     C                   EVAL      N2=67890
     C                   EVAL      N3=123,45
     C                   EVAL      N4=678,90
    MU* VAL1(S1) VAL2('          12345') COMP(EQ)
     C                   MOVE      N1            S1
    MU* VAL1(S1) VAL2('          67890') COMP(EQ)
     C                   MOVE      N2            S1
    MU* VAL1(S1) VAL2('          12345') COMP(EQ)
     C                   MOVE      N3            S1
    MU* VAL1(S1) VAL2('          67890') COMP(EQ)
     C                   MOVE      N4            S1
    MU* VAL1(S3) VAL2('345') COMP(EQ)
     C                   MOVE      N1            S3
    MU* VAL1(S3) VAL2('890') COMP(EQ)
     C                   MOVE      N2            S3
    MU* VAL1(S3) VAL2('345') COMP(EQ)
     C                   MOVE      N3            S3
    MU* VAL1(S3) VAL2('890') COMP(EQ)
     C                   MOVE      N4            S3
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
