   COP* *NOUI
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* 09/03/16  V4.R1   GIAGIU Creato
     V* 19/10/16  V5.R1   GIAGIU Test vari
     V* B£61020C  V5R1    BMA Aggiunta COP* *NOUI
     V* 21/02/17  V5.R1   GIAGIU Implementazione test con byte
     V* 12/08/19  001059  BMA Tolti test BITON / BITOF e test su dati esadecimali
     V*=====================================================================
     D* OBIETTIVO
     D*  Programma finalizzato ai test su campi di tipo CHARACTER
     V*---------------------------------------------------------------------
      * Considerare i seguenti codici operativi
      *+----------+--+---------!--+
      *!RPGLE     !ST!BUILT-IN !ST!
      *+-------------+ --------!--+
      *!CAT       !ok!+        !ok!
      *!CHECK     !ok!%CHECK   !ok!
      *!CHECKR    !ok!%CHECKR  !ok!
      *!CLEAR     !ok!         !  !
      *!          !  !%EDITC   !  !
      *!MOVE      !ok!EVALR    !ok!
      *!MOVEA     !ok!         !  !
      *!MOVEL     !ok!EVAL     !ok!
      *!RESET     !ok!         !  !
      *!SCAN      !ok!%SCAN    !ok!
      *!SUBST     !ok!%SUBST   !ok!
      *!TESTN     !ok!         !  !
      *!          !  !%REPLACE !ok!
      *!          !  !%STR     !  !
      *!          !  !%TRIM    !ok!
      *!          !  !%TRIMR   !ok!
      *!          !  !%TRIML   !ok!
      *!XLATE     !ok!%XLATE   !  !
      *+----------+--+---------+--+
     DS0               S             15A
    MU* VAL1(S1) VAL2('A') COMP(EQ)
     DS1               S             15A   INZ('A')
    MU* VAL1(S2) VAL2('B') COMP(EQ)
     DS2               S             15A   INZ('B')
     DS3               S             12A   INZ(*BLANKS)
     DS4               S             15A   INZ(*BlAnkS)
     DS5               S              3A   INZ( *BLANKS)
     DS6               S               N
     DS7               S              1
     DS8               S              2
     DN1               S              5S 0
     DN2               S              5P 0
     DN3               S              5S 2
     DN4               S              5P 2
     DA1               S             10A   DIM(10) INZ('ABCDEFGHIL')
     DA2               S              5S 0 DIM(10)
     DN5               S              3S 0 DIM(10)
     DN6               S             15S 5
     DS9               S             20
      *
     D Up              C                   'ABCDEFGHIJKLMNOPQRS-                COSTANTE
     D                                     TUVWXYZ'                             COSTANTE
     D Lo              C                   'abcdefghijklmnopqrs-                COSTANTE
     D                                     tuvwxyz'                             COSTANTE
     D Lo1             C                   'zyxwvutsrqponmlkjih-                COSTANTE
     D                                     gfedcba'                             COSTANTE
     D*DATE            S               D   INZ(D'1997-02-03')
     D C1              C                   '0123456789'
     D C2              C                   X'C1'
      *
      *D £F06            C                   CONST(X'36')
      *D £KEY            S              1A
      *
      *C                   EXSR      F_BIT
     C                   EXSR      F_CAT
     C                   EXSR      F_CHECK
     C                   EXSR      F_CLEAR
     C                   EXSR      F_EVAL
     C                   EXSR      F_MOVE
     C                   EXSR      F_MOVEL
     C                   EXSR      F_MOVEA
     C                   EXSR      F_RESET
     C                   EXSR      F_SCAN
     C                   EXSR      F_SUBST
     C                   EXSR      F_TESTN
     C                   EXSR      F_XLATE
     C                   EXSR      F_BUILTIN
      *C                   EXSR      F_BYTE
      *
    MU* Type="NOXMI"
     C                   SETON                                        LR
      * *---------------------------------------------------------------------
    R *D* Routine test CAT
      * *---------------------------------------------------------------------
      *C     F_BIT         BEGSR
      * *
    M *U* VAL1(S7) VAL2(x'00') COMP(EQ)
      *C                   BITOFF    '01234567'    S7
    M *U* VAL1(S7) VAL2(x'80') COMP(EQ)
      *C                   BITON     '0'           S7
      *C                   BITOFF    '01234567'    S7
    M *U* VAL1(S7) VAL2(x'40') COMP(EQ)
      *C                   BITON     '1'           S7
      *C                   BITOFF    '01234567'    S7
    M *U* VAL1(S7) VAL2(x'20') COMP(EQ)
      *C                   BITON     '2'           S7
      *C                   BITOFF    '01234567'    S7
    M *U* VAL1(S7) VAL2(x'10') COMP(EQ)
      *C                   BITON     '3'           S7
      *C                   BITOFF    '01234567'    S7
    M *U* VAL1(S7) VAL2(x'08') COMP(EQ)
      *C                   BITON     '4'           S7
      *C                   BITOFF    '01234567'    S7
    M *U* VAL1(S7) VAL2(x'04') COMP(EQ)
      *C                   BITON     '5'           S7
      *C                   BITOFF    '01234567'    S7
    M *U* VAL1(S7) VAL2(x'02') COMP(EQ)
      *C                   BITON     '6'           S7
      *C                   BITOFF    '01234567'    S7
    M *U* VAL1(S7) VAL2(x'01') COMP(EQ)
      *C                   BITON     '7'           S7
      *C                   BITOFF    '01234567'    S7
      * *
      *C                   CLEAR                   S7
    M *U* VAL1(S7) VAL2(x'FF') COMP(EQ)
      *C                   BITON     '01234567'    S7
      *C                   BITOFF    '01234567'    S7
      * *
    M *U* VAL1(S7) VAL2(x'C1') COMP(EQ)
      *C                   BITON     C2            S7
    M *U* VAL1(S7) VAL2('A') COMP(EQ)
      *C                   BITON     C2            S7
      * *
      *C                   BITOFF    '01234567'    S7
    M *U* VAL1(S7) VAL2('0') COMP(EQ)
      *C                   BITON     X'F0'         S7
      * *
      *C                   BITOFF    '01234567'    S7
    M *U* VAL1(*IN(01)) VAL2('1') COMP(EQ)
      *C                   TESTB     '0'           S7                   01  01
      * *
      *C                   BITON     C2            S7
    M *U* VAL1(*IN(01)) VAL2('1') COMP(EQ)
      *C                   TESTB     '01'          S7                   01  01
    M *U* VAL1(*IN(02)) VAL2('1') COMP(EQ)
      *C                   TESTB     '01'          S7                     0202
    M *U* VAL1(*IN(01)) VAL2('0') COMP(EQ)
      *C                   TESTB     '6'           S7                       01
    M *U* VAL1(*IN(04)) VAL2('1') COMP(EQ)
      *C                   TESTB     C2            S7                   04  04
    M *U* VAL1(*IN(05)) VAL2('1') COMP(EQ)
      *C                   TESTB     X'C1'         S7                   05  05
      * *
      *C                   CLEAR                   AAA001
      *C                   EVAL      AAA001='6'
    M *U* VAL1(*IN(06)) VAL2('0') COMP(EQ)
      *C                   TESTB     AAA001        S7                   06  06
      * *
      *C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test CAT
      *---------------------------------------------------------------------
     C     F_CAT         BEGSR
      *
     C                   CLEAR                   S0
     C                   CLEAR                   S1
     C                   CLEAR                   S2
      *
     C                   CLEAR                   AAA005            5
     C                   CLEAR                   BBB005            5
     C                   MOVEL     '01234'       AAA005
     C                   MOVEL     '56789'       BBB005
    MU* VAL1(S0) VAL2('0123456789') COMP(EQ)
     C                   EVAL      S0=AAA005 + BBB005
    MU* VAL1(S0) VAL2('01234 56789') COMP(EQ)
     C                   EVAL      S0=AAA005 + ' ' + BBB005
      *
     C                   CLEAR                   S0
    MU* VAL1(S0) VAL2('0123456789') COMP(EQ)
     C     AAA005        CAT(P)    BBB005:0      S0
    MU* VAL1(S0) VAL2('5678901234') COMP(EQ)
     C     BBB005        CAT       AAA005:0      S0
      *
    MU* VAL1(S1) VAL2('01234 56789') COMP(EQ)
     C     AAA005        CAT       BBB005:1      S1
    MU* VAL1(S1) VAL2('56789 01234') COMP(EQ)
     C     BBB005        CAT       AAA005:1      S1
      *
     C                   MOVEL(P)  'ABC'         FLD2              9
     C                   MOVE      'XYZ'         FLD1              3
    MU* VAL1(FLD2) VAL2('ABC  XYZ ') COMP(EQ)
     C                   CAT       FLD1:2        FLD2
      *
     C                   MOVEL(P)  'ABC'         FLD2              9
     C                   MOVE      'XYZ'         FLD1              3
    MU* VAL1(FLD2) VAL2('ABC  XYZ ') COMP(EQ)
     C                   CAT(P)    FLD1:2        FLD2
      *
     C                   MOVE      'MR.'         NAME              3
     C                   MOVE      ' SMITH'      FIRST             6
    MU* VAL1(RESULT) VAL2('MR. SMITH') COMP(EQ)
     C     NAME          CAT       FIRST         RESULT            9
    MU* VAL1(RESULT) VAL2('MR. SMITH') COMP(EQ)
     C     NAME          CAT(P)    FIRST         RESULT            9
      *
     C                   MOVE      'Mr.   '      NAME1             6            COSTANTE
     C                   MOVE      'Smith '      LAST              6            COSTANTE
    MU* VAL1(TEMP) VAL2('Mr. Smith') COMP(EQ)
     C     NAME1         CAT       LAST:1        TEMP              9
      *
     C                   MOVE      '/400'        STRING            4
    MU* VAL1(TEMP1) VAL2('RPG/400') COMP(EQ)
     C     'RPG'         CAT       STRING        TEMP1             7
    MU* VAL1(TEMP1) VAL2('RPG/400') COMP(EQ)
     C     'RPG'         CAT(P)    STRING        TEMP1             7
      *
     C                   MOVEL(P)  'A'           $$C01             1
     C                   MOVEL(P)  'M5M5W0_'     $$C10            10
     C                   CLEAR                   $$C11            10
    MU* VAL1($$C11) VAL2('M5M5W0_A') COMP(EQ)
     C     $$C10         CAT       $$C01:0       $$C11
    MU* VAL1($$C11) VAL2('M5M5W0_A') COMP(EQ)
     C     $$C10         CAT(P)    $$C01:0       $$C11
    MU* VAL1($$C11) VAL2('M5M5W0_A') COMP(EQ)
     C     $$C10         CAT       'A'  :0       $$C11
    MU* VAL1($$C11) VAL2('M5M5W0_A') COMP(EQ)
     C     $$C10         CAT(P)    'A'  :0       $$C11
    MU* VAL1($$C11) VAL2('M5M5W0_A') COMP(EQ)
     C     'M5M5W0_'     CAT       'A'  :0       $$C11
    MU* VAL1($$C11) VAL2('M5M5W0_A') COMP(EQ)
     C     'M5M5W0_'     CAT(P)    'A'  :0       $$C11
    MU* VAL1($$C11) VAL2('M5M5W0_A') COMP(EQ)
     C     'M5M5W0_'     CAT       $$C01:0       $$C11
    MU* VAL1($$C11) VAL2('M5M5W0_A') COMP(EQ)
     C     'M5M5W0_'     CAT(P)    $$C01:0       $$C11
      *
     C                   MOVEL(P)  'B£IA'        £IFAR5            7
     C                   MOVEL(P)  'SM'          ££ANAR            2
     C                   CLEAR                   ££RESU           15
    MU* VAL1(££RESU) VAL2('B£IASM') COMP(EQ)
     C     £IFAR5        CAT       ££ANAR:0      ££RESU
     C                   MOVEL(P)  'B£IA'        £IFAR5
     C                   MOVEL(P)  'SM'          ££ANAR
    MU* VAL1(££RESU) VAL2('B£IA SM') COMP(EQ)
     C     £IFAR5        CAT       ££ANAR:1      ££RESU
     C                   MOVEL(P)  'B£IA'        £IFAR5
     C                   MOVEL(P)  'SM'          ££ANAR
    MU* VAL1(££RESU) VAL2('B£IA  SM') COMP(EQ)
     C     £IFAR5        CAT       ££ANAR:2      ££RESU
     C                   MOVEL(P)  'B£IA'        £IFAR5
     C                   MOVEL(P)  'SM'          ££ANAR
    MU* VAL1(££RESU) VAL2('B£IA   SM') COMP(EQ)
     C     £IFAR5        CAT       ££ANAR        ££RESU
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test CHECK
      *---------------------------------------------------------------------
     C     F_CHECK       BEGSR
      *
     C                   CLEAR                   S4
    MU* VAL1(S4) VAL2('ABC.DFG-012,3!?') COMP(EQ)
     C                   EVAL      S4 = 'ABC.DFG-012,3!?'                       COSTANTE
      *
     C                   CLEAR                   NN                6 0
    MU* VAL1(NN) VAL2(2) COMP(EQ)
     C     'A'           CHECK     S4            NN
     C                   CLEAR                   NN
    MU* VAL1(*IN(50)) VAL2('1') COMP(EQ)
     C     'A'           CHECK     S4            NN                       50
      *
     C                   CLEAR                   AAA001            1
     C                   EVAL      AAA001='A'
     C                   CLEAR                   NN
    MU* VAL1(NN) VAL2(2) COMP(EQ)
     C     AAA001        CHECK     S4            NN                       50
     C                   CLEAR                   NN
    MU* VAL1(*IN(50)) VAL2('1') COMP(EQ)
     C     AAA001        CHECK     S4            NN                       50
      *
     C                   CLEAR                   NN
    MU* VAL1(NN) VAL2(1) COMP(EQ)
     C     'B'           CHECK     S4            NN
     C                   CLEAR                   NN
    MU* VAL1(*IN(50)) VAL2('1') COMP(EQ)
     C     'B'           CHECK     S4                                     50
      *
     C                   CLEAR                   NN
    MU* VAL1(NN) VAL2(0) COMP(EQ)
     C     '?'           CHECK     S4:15         NN
     C                   CLEAR                   NN
    MU* VAL1(*IN(50)) VAL2('0') COMP(EQ)
     C     '?'           CHECK     S4:15                                  50
      *
     C                   CLEAR                   NN
    MU* VAL1(NN) VAL2(2) COMP(EQ)
     C     'A'           CHECK     S4:1          NN
     C                   CLEAR                   NN
    MU* VAL1(*IN(50)) VAL2('1') COMP(EQ)
     C     'A'           CHECK     S4:1                                   50
      *
     C                   CLEAR                   NN
    MU* VAL1(NN) VAL2(5) COMP(EQ)
     C     '.'           CHECK     S4:4          NN
     C                   CLEAR                   NN
    MU* VAL1(*IN(50)) VAL2('1') COMP(EQ)
     C     '.'           CHECK     S4:4                                   50
      *
     C                   CLEAR                   S4
     C                   MOVEL     '$2000.'      S4
    MU* VAL1(NN) VAL2(6) COMP(EQ)
     C     '0123456789'  CHECK     S4    :2      NN
    MU* VAL1(NN) VAL2(6) COMP(EQ)
     C     C1            CHECK     S4    :2      NN
      *
     C**                 CLEAR                   S4
     C**                 MOVEL     '$2000.'      S4
     C**   '0123456789'  CHECK     S4    :2      N5
    M * VAL1(N5(01)) VAL2(6) COMP(EQ)
     C**                 EVAL      N5(01)=N5(01)
    M * VAL1(N5(02)) VAL2(7) COMP(EQ)
     C**                 EVAL      N5(02)=N5(02)
    M * VAL1(N5(03)) VAL2(8) COMP(EQ)
     C**                 EVAL      N5(03)=N5(03)
      *
     C                   CLEAR                   S4
     C                   MOVEL     '1A=BC*'      S4
     C     'ABCDEFGHIJ'  CHECK     S4            NN                       90
     C                   CLEAR                   S4
     C                   MOVEL     '1A=BC*'      S4
     C**   'ABCDEFGHIJ'  CHECK     S4            N5                       90
    M * VAL1(N5(01)) VAL2(1) COMP(EQ)
     C**                 EVAL      N5(01)=N5(01)
    M * VAL1(N5(02)) VAL2(3) COMP(EQ)
     C**                 EVAL      N5(02)=N5(02)
    M * VAL1(N5(03)) VAL2(6) COMP(EQ)
     C**                 EVAL      N5(03)=N5(03)
      *
     C                   CLEAR                   S4
     C                   MOVEL     '$2000.'      S4
    MU* VAL1(NN) VAL2(1) COMP(EQ)
     C     '0123456789'  CHECKR    S4    :2      NN
     C                   SUBST     S4:2          S5
      *
     C                   EVAL      S4 = 'ABC.DFG-012,3!?'                       COSTANTE
     C                   CLEAR                   NN
    MU* VAL1(NN) VAL2(0) COMP(EQ)
     C     'A'           CHECKR    S4:1          NN
     C                   CLEAR                   NN
    MU* VAL1(*IN(50)) VAL2('0') COMP(EQ)
     C     'A'           CHECKR    S4:1                                   50
      *
     C                   CLEAR                   NN
    MU* VAL1(NN) VAL2(15) COMP(EQ)
     C     'A'           CHECKR    S4            NN
     C                   CLEAR                   NN
    MU* VAL1(*IN(50)) VAL2('1') COMP(EQ)
     C     'A'           CHECKR    S4                                     50
      *
     C                   CLEAR                   NN
    MU* VAL1(NN) VAL2(15) COMP(EQ)
     C     'B'           CHECKR    S4            NN
     C                   CLEAR                   NN
    MU* VAL1(*IN(50)) VAL2('1') COMP(EQ)
     C     'B'           CHECKR    S4            NN                       50
      *
     C                   CLEAR                   NN
    MU* VAL1(NN) VAL2(14) COMP(EQ)
     C     '?'           CHECKR    S4:15         NN
     C                   CLEAR                   NN
    MU* VAL1(*IN(50)) VAL2('1') COMP(EQ)
     C     '?'           CHECKR    S4:15         NN                       50
      *
     C                   CLEAR                   NN
    MU* VAL1(NN) VAL2(3) COMP(EQ)
     C     '.'           CHECKR    S4:4          NN
     C                   CLEAR                   NN
    MU* VAL1(*IN(50)) VAL2('1') COMP(EQ)
     C     '.'           CHECKR    S4:4          NN                       50
      *
     C                   CLEAR                   NN
    MU* VAL1(NN) VAL2(2) COMP(EQ)
     C                   EVAL      NN = %CHECK('A':S4)
      *
     C                   CLEAR                   NN
    MU* VAL1(NN) VAL2(1) COMP(EQ)
     C                   EVAL      NN = %CHECK('B':S4)
      *
     C                   CLEAR                   NN
    MU* VAL1(NN) VAL2(0) COMP(EQ)
     C                   EVAL      NN = %CHECK('?':S4:15)
      *
     C                   CLEAR                   NN
    MU* VAL1(NN) VAL2(5) COMP(EQ)
     C                   EVAL      NN = %CHECK('.':S4:5)
      ****
     C                   CLEAR                   NN
    MU* VAL1(NN) VAL2(0) COMP(EQ)
     C                   EVAL      NN = %CHECKR('A':S4:1)
      *
     C                   CLEAR                   NN
    MU* VAL1(NN) VAL2(15) COMP(EQ)
     C                   EVAL      NN = %CHECKR('A':S4)
      *
     C                   CLEAR                   NN
    MU* VAL1(NN) VAL2(15) COMP(EQ)
     C                   EVAL      NN = %CHECKR('B':S4)
      *
     C                   CLEAR                   NN
    MU* VAL1(NN) VAL2(14) COMP(EQ)
     C                   EVAL      NN = %CHECKR('?':S4:15)
      *
     C                   CLEAR                   NN
    MU* VAL1(NN) VAL2(3) COMP(EQ)
     C                   EVAL      NN = %CHECKR('.':S4:4)
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test CLEAR
      *---------------------------------------------------------------------
     C     F_CLEAR       BEGSR
    MU* VAL1(S0) VAL2('') COMP(EQ)
     C                   CLEAR                   S0
    MU* VAL1(S1) VAL2('') COMP(EQ)
     C                   CLEAR                   S1
    MU* VAL1(S2) VAL2('') COMP(EQ)
     C                   CLEAR                   S2
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test EVAL
      *---------------------------------------------------------------------
     C     F_EVAL        BEGSR
      *
     C                   CLEAR                   S0
     C                   CLEAR                   S1
     C                   CLEAR                   S2
     C                   CLEAR                   AAA021           21
    MU* VAL1(S0) VAL2('111111111111111')  COMP(EQ)
     C                   EVAL      S0=*ON
    MU* VAL1(S0) VAL2('000000000000000')  COMP(EQ)
     C                   EVAL      S0=*OFF
      *
     C                   EVAL      AAA021='ABCDEFGHILMNOPQRSTUVZ'               COSTANTE
    MU* VAL1(S1) VAL2('ABCDEFGHILMNOPQ') COMP(EQ)
     C                   EVAL      S1=AAA021
    MU* VAL1(S2) VAL2('GHILMNOPQRSTUVZ') COMP(EQ)
     C                   EVALR     S2=AAA021
      *
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test MOVE
      *---------------------------------------------------------------------
     C     F_MOVE        BEGSR
      *
     C                   CLEAR                   S0
     C                   CLEAR                   S1
     C                   CLEAR                   S2
      *
    MU* VAL1(S1) VAL2('BABABABABABABAB') COMP(EQ)
     C                   MOVE      *ALL'AB'      S1
     C                   CLEAR                   S1
      *
    MU* VAL1(S0) VAL2('            051') COMP(EQ)
     C                   MOVE      05.1          S0
      *
    MU* VAL1(S0) VAL2('         TEST_0') COMP(EQ)
     C                   MOVE      'TEST_0'      S0
      *
    MU* VAL1(S1) VAL2('         TEST_1') COMP(EQ)
     C                   MOVE      'TEST_1'      S1
      *
    MU* VAL1(S2) VAL2('         TEST_2') COMP(EQ)
     C                   MOVE      'TEST_2'      S2
    MU* VAL1(S2) VAL2('AAAAAAAAAAAAAAA') COMP(EQ)
     C                   MOVE      *ALL'A'       S2
    MU* VAL1(S2) VAL2('AAAAAAAAAABBBBB') COMP(EQ)
     C                   MOVE      'BBBBB'       S2
    MU* VAL1(S2) VAL2('          BBBBB') COMP(EQ)
     C                   MOVE (P)  'BBBBB'       S2
    MU* VAL1(S2) VAL2('               ') COMP(EQ)
     C                   MOVEL     *ALL' '       S2
    MU* VAL1(S2) VAL2('ABCDABCDABCDABC') COMP(EQ)
     C                   MOVEL     *ALL'ABCD'    S2
1    C                   IF        S2=*ALL'ABCD'
     C                   MOVEL     *ALL' '       S2
1e   C                   ENDIF
    MU* VAL1(S0) VAL2('000000000000000') COMP(EQ)
     C                   MOVE      *OFF          S0
    MU* VAL1(S5) VAL2('000') COMP(EQ)
     C                   MOVE      *OFF          S5
    MU* VAL1(S0) VAL2('111111111111111') COMP(EQ)
     C                   MOVE      *ON           S0
    MU* VAL1(S5) VAL2('111') COMP(EQ)
     C                   MOVE      *ON           S5
    MU* VAL1(S0) VAL2('000000000000000') COMP(EQ)
     C                   MOVE      *ZEROS        S0
    MU* VAL1(S5) VAL2('000') COMP(EQ)
     C                   MOVE      *ZEROS        S5
    MU* VAL1(S0) VAL2('               ') COMP(EQ)
     C                   MOVE      *BLANK        S0
    MU* VAL1(S5) VAL2('   ') COMP(EQ)
     C                   MOVE      *BLANK        S5
      *
     C                   EVAL      N1=12345
     C                   EVAL      N2=67890
     C                   EVAL      N3=123,45
     C                   EVAL      N4=678,90
    MU* VAL1(S2) VAL2('          12345') COMP(EQ)
     C                   MOVE      N1            S2
    MU* VAL1(S2) VAL2('          67890') COMP(EQ)
     C                   MOVE      N2            S2
    MU* VAL1(S2) VAL2('          12345') COMP(EQ)
     C                   MOVE      N3            S2
    MU* VAL1(S2) VAL2('          67890') COMP(EQ)
     C                   MOVE      N4            S2
    MU* VAL1(S5) VAL2('345') COMP(EQ)
     C                   MOVE      N1            S5
    MU* VAL1(S5) VAL2('890') COMP(EQ)
     C                   MOVE      N2            S5
    MU* VAL1(S5) VAL2('345') COMP(EQ)
     C                   MOVE      N3            S5
    MU* VAL1(S5) VAL2('890') COMP(EQ)
     C                   MOVE      N4            S5
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test MOVEA
      *---------------------------------------------------------------------
     C     F_MOVEA       BEGSR
      *
    MU* VAL1(S5) VAL2('ABC') COMP(EQ)
     C                   MOVEA     A1            S5
    MU* VAL1(S2) VAL2('ABCDEFGHILABCDE') COMP(EQ)
     C                   MOVEA     A1            S2
    MU* VAL1(S3) VAL2('ABCDEFGHILAB') COMP(EQ)
     C                   MOVEA     A1            S3
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test MOVEL
      *---------------------------------------------------------------------
     C     F_MOVEL       BEGSR
      *
     C                   CLEAR                   S0
     C                   CLEAR                   S1
     C                   CLEAR                   S2
    MU* VAL1(S1) VAL2('ABABABABABABABA') COMP(EQ)
     C                   MOVEL     *ALL'AB'      S1
     C                   CLEAR                   S1
      *
     C                   MOVEL     123           S0
     C                   MOVE      456           S0
    MU* VAL1(S0) VAL2('TEST_0      456') COMP(EQ)
     C                   MOVEL     'TEST_0'      S0
      *
      *
    MU* VAL1(S1) VAL2('TEST_1') COMP(EQ)
     C                   MOVEL     'TEST_1'      S1
      *
    MU* VAL1(S2) VAL2('TEST_2') COMP(EQ)
     C                   MOVEL     'TEST_2'      S2
    MU* VAL1(S2) VAL2('AAAAAAAAAAAAAAA') COMP(EQ)
     C                   MOVEL     *ALL'A'       S2
    MU* VAL1(S2) VAL2('BBBBBAAAAAAAAAA') COMP(EQ)
     C                   MOVEL     'BBBBB'       S2
    MU* VAL1(S2) VAL2('BBBBB') COMP(EQ)
     C                   MOVEL(P)  'BBBBB'       S2
    MU* VAL1(S2) VAL2('               ') COMP(EQ)
     C                   MOVEL     *ALL' '       S2
      *
    MU* VAL1(S0) VAL2('000000000000000') COMP(EQ)
     C                   MOVEL     *OFF          S0
    MU* VAL1(S5) VAL2('000') COMP(EQ)
     C                   MOVEL     *OFF          S5
    MU* VAL1(S0) VAL2('111111111111111') COMP(EQ)
     C                   MOVEL     *ON           S0
    MU* VAL1(S5) VAL2('111') COMP(EQ)
     C                   MOVEL     *ON           S5
    MU* VAL1(S0) VAL2('000000000000000') COMP(EQ)
     C                   MOVEL     *ZEROS        S0
    MU* VAL1(S5) VAL2('000') COMP(EQ)
     C                   MOVEL     *ZEROS        S5
    MU* VAL1(S0) VAL2('               ') COMP(EQ)
     C                   MOVEL     *BLANK        S0
    MU* VAL1(S5) VAL2('   ') COMP(EQ)
     C                   MOVEL     *BLANK        S5
      *
     C                   EVAL      N1=12345
     C                   EVAL      N2=67890
     C                   EVAL      N3=123,45
     C                   EVAL      N4=678,90
    MU* VAL1(S2) VAL2('12345          ') COMP(EQ)
     C                   MOVEL     N1            S2
    MU* VAL1(S2) VAL2('67890          ') COMP(EQ)
     C                   MOVEL     N2            S2
    MU* VAL1(S2) VAL2('12345          ') COMP(EQ)
     C                   MOVEL     N3            S2
    MU* VAL1(S2) VAL2('67890          ') COMP(EQ)
     C                   MOVEL     N4            S2
    MU* VAL1(S5) VAL2('123') COMP(EQ)
     C                   MOVEL     N1            S5
    MU* VAL1(S5) VAL2('678') COMP(EQ)
     C                   MOVEL     N2            S5
    MU* VAL1(S5) VAL2('123') COMP(EQ)
     C                   MOVEL     N3            S5
    MU* VAL1(S5) VAL2('678') COMP(EQ)
     C                   MOVEL     N4            S5
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test RESET
      *---------------------------------------------------------------------
     C     F_RESET       BEGSR
    MU* VAL1(S1) VAL2('') COMP(EQ)
     C                   CLEAR                   S1
    MU* VAL1(S2) VAL2('') COMP(EQ)
     C                   CLEAR                   S2
    MU* VAL1(S1) VAL2('A') COMP(EQ)
     C                   RESET                   S1
    MU* VAL1(S2) VAL2('B') COMP(EQ)
     C                   RESET                   S2
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test SCAN
      *---------------------------------------------------------------------
     C     F_SCAN        BEGSR
      *
     C                   CLEAR                   S0
     C                   CLEAR                   S1
     C                   CLEAR                   N1
     C                   EVAL      S0='**AA!!'
     C                   EVAL      S1='**'
    MU* VAL1(N1) VAL2(0) COMP(EQ)
     C     S1            SCAN      S0            N1
    MU* VAL1(N1) VAL2(1) COMP(EQ)
     C     S1:2          SCAN      S0            N1
    MU* VAL1(N1) VAL2(0) COMP(EQ)
     C     S1:2          SCAN      S0:3          N1
    MU* VAL1(N1) VAL2(5) COMP(EQ)
    MU* VAL1(*IN50) VAL2('1') COMP(EQ)
     C     '!':1         SCAN      S0            N1                       50
    MU* VAL1(N1) VAL2(5) COMP(EQ)
    MU* VAL1(*IN50) VAL2('1') COMP(EQ)
     C     '!'           SCAN      S0            N1                       50
      *
     C                   EVAL      N1=%SCAN('AA':S1:2)
     C                   CLEAR                   AAA               2
     C                   EVAL      AAA='AA'
     C                   EVAL      N1=%SCAN(AAA:S1:2)
      *
     C                   CLEAR                   AAA005            5
     C                   EVAL      AAA005='!'
    MU* VAL1(*IN50) VAL2('1') COMP(EQ)
     C     AAA005:1      SCAN      '?!/'                                  50
     C                   EVAL      AAA005=*BLANK
    MU* VAL1(*IN50) VAL2('0') COMP(EQ)
     C     AAA005:1      SCAN      '?!/'                                  50
      *
     C                   CLEAR                   AAA005            5
     C                   EVAL      AAA005='!'
    MU* VAL1(*IN50) VAL2(*ON) COMP(EQ)
     C     AAA005:1      SCAN      '?!/'         N1                       50
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test SUBST
      *---------------------------------------------------------------------
     C     F_SUBST       BEGSR
      *
     C                   CLEAR                   S0
     C                   CLEAR                   S1
     C                   CLEAR                   S2
      *                            \
     C                   EVAL      S0='ABCDEFGHILMNOPQ'                         COSTANTE
    MU* VAL1(S1) VAL2('ABCDEFG') COMP(EQ)
     C     7             SUBST     S0:1          S1
    MU* VAL1(S2) VAL2('HILMNOPQ') COMP(EQ)
     C                   SUBST     S0:8          S2
      *
     C                   CLEAR                   S2
    MU* VAL1(S2) VAL2('') COMP(EQ)
     C                   EVAL      S2=%SUBST(AAA021:1:0)
     C                   CLEAR                   S2
    MU* VAL1(S2) VAL2('A') COMP(EQ)
     C                   EVAL      %SUBST(S2:1:1)=AAA021
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test TESTN
      *---------------------------------------------------------------------
     C     F_TESTN       BEGSR
      *
     C                   CLEAR                   S5
      *
     C                   MOVE      '123'         S5
    MU* VAL1(*IN(21)) VAL2(*ON ) COMP(EQ)
     C                   TESTN                   S5                   21
      *
    MU* VAL1(*IN(22)) VAL2(*ON ) COMP(EQ)
     C                   TESTN                   S5                   22
    MU* VAL1(*IN(23)) VAL2(*OFF) COMP(EQ)
     C                   TESTN                   S5                     23
    MU* VAL1(*IN(24)) VAL2(*OFF) COMP(EQ)
     C                   TESTN                   S5                       24
      *
     C                   MOVE      '1X4'         S5
    MU* VAL1(*IN(25)) VAL2(*OFF) COMP(EQ)
     C                   TESTN                   S5                   25
    MU* VAL1(*IN(26)) VAL2(*OFF) COMP(EQ)
     C                   TESTN                   S5                     26
    MU* VAL1(*IN(27)) VAL2(*OFF) COMP(EQ)
     C                   TESTN                   S5                       27
      *
     C                   MOVE      '004'         S5
    MU* VAL1(*IN(28)) VAL2(*ON ) COMP(EQ)
     C                   TESTN                   S5                   28
    MU* VAL1(*IN(29)) VAL2(*OFF) COMP(EQ)
     C                   TESTN                   S5                     29
    MU* VAL1(*IN(30)) VAL2(*OFF) COMP(EQ)
     C                   TESTN                   S5                       30
      *
     C                   MOVE      *BLANK        S5
    MU* VAL1(*IN(31)) VAL2(*OFF) COMP(EQ)
     C                   TESTN                   S5                   31
    MU* VAL1(*IN(32)) VAL2(*OFF) COMP(EQ)
     C                   TESTN                   S5                     32
    MU* VAL1(*IN(33)) VAL2(*ON ) COMP(EQ)
     C                   TESTN                   S5                       33
      *
     C                   MOVE      '1 3'         S5
    MU* VAL1(*IN(34)) VAL2(*OFF) COMP(EQ)
     C                   TESTN                   S5                   34
    MU* VAL1(*IN(35)) VAL2(*OFF) COMP(EQ)
     C                   TESTN                   S5                     35
    MU* VAL1(*IN(36)) VAL2(*OFF) COMP(EQ)
     C                   TESTN                   S5                       36
      *
     C                   MOVE      ' 12'         S5
    MU* VAL1(*IN(37)) VAL2(*OFF) COMP(EQ)
     C                   TESTN                   S5                   37
    MU* VAL1(*IN(38)) VAL2(*ON ) COMP(EQ)
     C                   TESTN                   S5                     38
    MU* VAL1(*IN(39)) VAL2(*OFF) COMP(EQ)
     C                   TESTN                   S5                       39
      *
     C                   MOVE      'AAA'         S5
    MU* VAL1(*IN(40)) VAL2(*OFF) COMP(EQ)
     C                   TESTN                   S5                   40
    MU* VAL1(*IN(41)) VAL2(*OFF) COMP(EQ)
     C                   TESTN                   S5                     41
    MU* VAL1(*IN(42)) VAL2(*OFF) COMP(EQ)
     C                   TESTN                   S5                       42
      *
     C                   MOVE      '  A'         S5
    MU* VAL1(*IN(43)) VAL2(*OFF) COMP(EQ)
     C                   TESTN                   S5                   43
    MU* VAL1(*IN(44)) VAL2(*ON) COMP(EQ)
     C                   TESTN                   S5                     44
    MU* VAL1(*IN(45)) VAL2(*OFF) COMP(EQ)
     C                   TESTN                   S5                       45
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test XLATE
      *---------------------------------------------------------------------
     C     F_XLATE       BEGSR
      *
     C**                 EVAL      Result1 = %XLATE(Lo:Up:String1:$B)
     C**                 EVAL      Result1 = %XLATE(Lo:Up:String1:1)
     C**                 EVAL      Result1 = %XLATE(Lo:Up:%SUBST(String1:2:3))
      *
     C                   EVAL      RESULT2='AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA'COSTANTE
     C                   MOVEL     'rpg dept'    String1          20            COSTANTE
    MU* VAL1(RESULT1) VAL2('RPG DEPT') COMP(EQ)
     C     Lo:Up         XLATE(P)  String1       Result1          20
    MU* VAL1(RESULT2) VAL2('RPG Dept            AAAAAAAAAA') COMP(EQ)
     C     Up:Lo         XLATE     Result1:6     Result2
    MU* VAL1(RESULT2) VAL2('RPG Dept') COMP(EQ)
     C     Up:Lo         XLATE(P)  Result1:6     Result2
     C                   EVAL      String1= '**Test - xlate**'                  COSTANTE
    MU* VAL1(RESULT1) VAL2('__Test - xlate__') COMP(EQ)
     C     '*':'_'       XLATE(P)  String1       Result1
     C                   MOVEL(P)  'è'           $A                1
    MU* VAL1(RESULT1) VAL2('èèTest - xlateèè') COMP(EQ)
     C     '_':$A        XLATE(P)  Result1       Result1
      *
    MU* VAL1(RESULT1) VAL2('**TEST - XLATE**') COMP(EQ)
     C     Lo:Up         XLATE     String1       Result1
     C                   Z-ADD     7             $B                2 0
    MU* VAL1(RESULT1) VAL2('**Test - XLATE**') COMP(EQ)
     C     Lo:Up         XLATE     String1:$B    Result1
     U* VAL1(RESULT1) VAL2('**Test - XLATE**') COMP(EQ)
     C*    Lo:Up         XLATE(P)  String1:$B    Result1
    MU* VAL1(RESULT1) VAL2('**Test - XLATE**') COMP(EQ)
     C     Lo:Up         XLATE(P)  String1:$B    Result1
     U* VAL1(RESULT1) VAL2('**Test - XLATE**') COMP(EQ)
     C*    Lo:Up         XLATE     String1:$B    Result1
      *
    MU* VAL1(RESULT1) VAL2('**Test - xlate**') COMP(EQ)
     C     'C':'c'       XLATE     String1:$B    Result1                50
    MU* VAL1(RESULT1) VAL2('AAA  t - xlate**') COMP(EQ)
     C     'C':'c'       XLATE     'AAA  ':1     Result1
    MU* VAL1(RESULT1) VAL2('**Test - xlBte**') COMP(EQ)
     C     Lo:'B'        XLATE     String1:$B    Result1
     C                   MOVEL     'rpg dept'    String1          20            COSTANTE
      *
     C                   CLEAR                   String2          30
     C                   CLEAR                   Result2          30
     C                   EVAL      String2='ABCDEFGHIJKLMNOPQRSTUVWXYZ'         COSTANTE
    MU* VAL1(RESULT2) VAL2('zyxwvutsrqponmlkjihgfedcba') COMP(EQ)
     C     Up:Lo1        XLATE     String2       Result2
    MU* VAL1(RESULT2) VAL2('!?CDEFGHIJKLMNOPQRSTUVWXYZ') COMP(EQ)
     C     Up:'!?'       XLATE     String2       Result2
    MU* VAL1(RESULT2) VAL2('zyxwvFGHIJKLMNOPQRSTUVWXYZ') COMP(EQ)
     C     Up:Lo1        XLATE     'ABCDE'       Result2
    MU* VAL1(RESULT2) VAL2('!?CDEFGHIJKLMNOPQRSTUVWXYZ') COMP(EQ)
     C     Up:'!?'       XLATE     'ABCDE':1     Result2
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test BUILT-IN FUNCTION
      *---------------------------------------------------------------------
     C     F_BUILTIN     BEGSR
      * %TRIM
     C                   CLEAR                   VALUE           100
     C                   MOVEL(P)  '  AAA  '     VAR1             30
    MU* VAL1(VALUE) VAL2('AAA:') COMP(EQ)
     C                   EVAL      VALUE = %TRIM(VAR1) +':'
    MU* VAL1(VALUE) VAL2('AAA                         :') COMP(EQ)
     C                   EVAL      VALUE = %TRIML(VAR1) +':'
    MU* VAL1(VALUE) VAL2('  AAA:') COMP(EQ)
     C                   EVAL      VALUE = %TRIMR(VAR1) +':'
      * %REPLACE
     C                   CLEAR                   VALUE
     C                   MOVEL(P)  'WINDSOR'     VAR1
     C                   MOVEL(P)  'ONTARIO'     VAR2             30
     C                   MOVEL(P)  'CANADA'      VAR3             30
     C                   MOVEL(P)  'CALIFORNIA'  FIXED1           30
      *
    MU* VAL1(VALUE) VAL2('WINDSOR, ON               ') COMP(EQ)
     C                   EVAL      VALUE  = %TRIM(VAR1) + ', ' + 'ON'
    MU* VAL1(VALUE) VAL2('TORONTO, ON               ') COMP(EQ)
     C                   EVAL      VALUE  = %REPLACE ('TORONTO': %TRIM(VALUE))  COSTANTE
    MU* VAL1(VALUE) VAL2('TORONTO, CANADA           ') COMP(EQ)
     C                   EVAL      VALUE  = %REPLACE (VAR3: %TRIM(VALUE) :
     C                             %SCAN(',': %TRIM(VALUE) ) + 2)
    MU* VAL1(VALUE) VAL2('TORONTO, ONTARIO, CANADA        ') COMP(EQ)
     C                   EVAL      VALUE  = %REPLACE (', ' + %TRIM(VAR2):
     C                             %TRIM(VALUE):%SCAN (',': %TRIM(VALUE) ): 0)
    MU* VAL1(VALUE) VAL2('SCARBOROUGH, ONTARIO, CANADA    ') COMP(EQ)
     C                   EVAL      VALUE  = %REPLACE ('SCARBOROUGH':            COSTANTE
     C                             %TRIM(VALUE):1:
     C                             %SCAN (',': %TRIM(VALUE)) - 1)
    MU* VAL1(VALUE) VAL2('ONTARIO, CANADA    ') COMP(EQ)
     C                   EVAL      VALUE  = %REPLACE ('': %TRIM(VALUE) : 1:
     C                             %SCAN (',': %TRIM(VALUE) ) + 1)
     C**                 EVAL      VALUE  = %REPLACE (', ' + %CHAR(DATE):
     C**                           %TRIM(VALUE) :%LEN (VALUE ) + 1: 0)
    MU* VAL1(VALUE) VAL2('ONTARIO, CALIFORNIA    ') COMP(EQ)
     C                   EVAL      VALUE  = %REPLACE (FIXED1: %TRIM(VALUE) :
     C                             %SCAN (',': %TRIM(VALUE) ) + 2)
    MU* VAL1(VALUE) VAL2('SOMEWHERE ELSE: ONTARIO, CALIFORNIA') COMP(EQ)
     C                   EVAL      VALUE  = %REPLACE ('SOMEWHERE ELSE: ':       COSTANTE
     C                             %TRIM(VALUE) : 1: 0)
      *
     C                   EVAL      VALUE='  AAABBCCC   '
    MU* VAL1(VALUE) VAL2('AAABBCCC') COMP(EQ)
     C                   EVAL      VALUE=%trim(VALUE)
      *
     C                   EVAL      VALUE='AAABBCCC   '
    MU* VAL1(VALUE) VAL2('BBCCC') COMP(EQ)
     C                   EVAL      VALUE=%trim(VALUE: 'A')
    MU* VAL1(VALUE) VAL2('BBCCC') COMP(EQ)
     C                   EVAL      VALUE=%trim(VALUE: 'C')
    MU* VAL1(VALUE) VAL2('CCC') COMP(EQ)
     C                   EVAL      VALUE=%trim(VALUE: 'B')
    MU* VAL1(N1) VAL2(97) COMP(EQ)
     C                   EVAL      N1=%len(%trim(VALUE: 'C'))
    MU* VAL1(VALUE) VAL2('') COMP(EQ)
     C                   EVAL      VALUE=%trim(VALUE: 'C')
    MU* VAL1(N1) VAL2(100) COMP(EQ)
     C                   EVAL      N1=%len(%trim(VALUE: 'C'))
    MU* VAL1(N1) VAL2(0) COMP(EQ)
     C                   EVAL      N1=%len(%trim(VALUE))
    MU* VAL1(N1) VAL2(0) COMP(EQ)
     C                   EVAL      N1=%len(%trim(VALUE: 'C '))
      *
     C                   EVAL      VALUE='  AAABBCCC   '
    MU* VAL1(VALUE) VAL2('  AAABBCCC') COMP(EQ)
     C                   EVAL      VALUE=%trim(VALUE: 'A')
     C                   EVAL      VALUE='  AAABBCCC   '
    MU* VAL1(VALUE) VAL2('BBCCC') COMP(EQ)
     C                   EVAL      VALUE=%trim(VALUE: ' A')
      *
      * %INT
    MU* VAL1(N1) VAL2(1) COMP(EQ)
     C                   EVAL      N1=%int(' 1')
      *
     C**                 MONITOR
     C**                 EVAL      N1=%int(' ')
     C**                 ON-ERROR
     C**                 EVAL      N1=9
     C**                 ENDMON
    MU* VAL1(N1) VAL2(9) COMP(EQ)
     C                   EVAL      N1=N1
    MU* VAL1(N1) VAL2(5) COMP(EQ)
     C                   EVAL      N1=%int('05.32')
    MU* VAL1(N1) VAL2(5) COMP(EQ)
     C                   EVAL      N1=%int(' 5.32')
    MU* VAL1(N1) VAL2(2005) COMP(EQ)
     C                   EVAL      N1=%int('2005,32')
      *
     C                   ENDSR
      * *---------------------------------------------------------------------
    R *D* Routine test su campi byte
      * *---------------------------------------------------------------------
      *C     F_BYTE        BEGSR
      * *
    M *U* VAL1(£KEY) VAL2(x'36') COMP(EQ)
      *C                   EVAL      £KEY=£F06
    M *U* VAL1(£KEY) VAL2(x'36') COMP(EQ)
      *C                   MOVEL     £F06          £KEY
    M *U* VAL1(£KEY) VAL2(x'36') COMP(EQ)
      *C                   MOVE      £F06          £KEY
      * *
      *C                   ENDSR
