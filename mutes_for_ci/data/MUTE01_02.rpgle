   COP* *NOUI
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* 11/03/16  V4.R1   GIAGIU Creato
     V* B£61020C  V5R1    BMA Aggiunta COP* *NOUI
     V*=====================================================================
     D* OBIETTIVO
     D*  Programma finalizzato ai test su campi di tipo CHARACTER VARYING
     V*---------------------------------------------------------------------
      * Considerare i seguenti codici operativi
      *+----------+--+---------!--+
      *!RPGLE     !ST!BUILT-IN !ST!
      *+-------------+ --------!--+
      *!CAT       !  !+        !  !
      *!CHECK     !  !%CHECK   !  !
      *!CHECKR    !  !%CHECKR  !  !
      *!CLEAR     !  !         !  !
      *!MOVE      !  !EVALR    !  !
      *!MOVEA     !  !         !  !
      *!MOVEL     !  !EVAL     !  !
      *!RESET     !  !         !  !
      *!SCAN      !  !%SCAN    !  !
      *!SUBST     !  !%SUBST   !  !
      *!TESTN     !  !         !  !
      *!          !  !%REPLACE !  !
      *!          !  !%STR     !  !
      *!          !  !%TRIM    !  !
      *!          !  !%TRIMR   !  !
      *!          !  !%TRIML   !  !
      *!XLATE     !  !%XLATE   !  !
      *+----------+--+---------+--+
     DS0               S             15A   VARYING INZ('ABC')
     DS1               S             15A   VARYING INZ('A')
     DS2               S             15A   VARYING INZ('B')
     DS3               S             12A   VARYING
     DS4               S             15A   VARYING
     DS5               S              3A   VARYING
     DS6               S             30A   VARYING
     DS7               S             20A   VARYING
     DS8               S            100A   VARYING
     DN1               S              5S 0
     DN2               S              5P 0
     DN3               S              5S 2
     DN4               S              5P 2
     DI1               S               N
      *
     D Up              C                   'ABCDEFGHIJKLMNOPQRS-                COSTANTE
     D                                     TUVWXYZ'                             COSTANTE
     D Lo              C                   'abcdefghijklmnopqrs-                COSTANTE
     D                                     tuvwxyz'                             COSTANTE
     D Lo1             C                   'zyxwvutsrqponmlkjih-                COSTANTE
     D                                     gfedcba'                             COSTANTE
      *
     DN0               S              5  2
     DDS0              DS
     D S9                            30    VARYING
     D O9                             2  0 OVERlAY(DS0:1)
     DA1               S             10A   DIM(10) INZ(*ALL'A')
      *
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
     C                   EXSR      F_OTHER
      *
    MU* Type="NOXMI"
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Routine test CAT
      *---------------------------------------------------------------------
     C     F_CAT         BEGSR
      *
     C                   CLEAR                   S0
    MU* VAL1(N1) VAL2(0) COMP(EQ)
     C                   EVAL      N1=%LEN(S0)
     C                   CLEAR                   S1
     C                   CLEAR                   S2
      *
     C                   CLEAR                   AAA005            5
     C                   CLEAR                   BBB005            5
     C                   MOVEL     '01234'       AAA005
     C                   MOVEL     '56789'       BBB005
    MU* VAL1(S0) VAL2('0123456789') COMP(EQ)
     C                   EVAL      S0=AAA005 + BBB005
    MU* VAL1(N0) VAL2(10) COMP(EQ)
     C                   EVAL      N0=%len(S0)
    MU* VAL1(S0) VAL2('01234 56789') COMP(EQ)
     C                   EVAL      S0=AAA005 + ' ' + BBB005
    MU* VAL1(N0) VAL2(11) COMP(EQ)
     C                   EVAL      N0=%len(S0)
      *
     C                   CLEAR                   S0
    MU* VAL1(S0) VAL2(' ') COMP(EQ)
     C     AAA005        CAT(P)    BBB005:0      S0
    MU* VAL1(N0) VAL2(0) COMP(EQ)
     C                   EVAL      N0=%len(S0)
      *
     C                   EVAL      S0=*BLANK
    MU* VAL1(S0) VAL2('0123456789') COMP(EQ)
     C     AAA005        CAT(P)    BBB005:0      S0
    MU* VAL1(N0) VAL2(15) COMP(EQ)
     C                   EVAL      N0=%len(S0)
    MU* VAL1(S0) VAL2('5678901234') COMP(EQ)
     C     BBB005        CAT(P)    AAA005:0      S0
    MU* VAL1(N0) VAL2(15) COMP(EQ)
     C                   EVAL      N0=%len(S0)
      *
     C                   EVAL      S0=*BLANK
     C                   EVAL      S1=*BLANK
     C                   EVAL      S2=*BLANK
     C                   MOVEL(P)  '01234'       S0
     C                   MOVEL(P)  '56789'       S1
    MU* VAL1(S2) VAL2('01234 56789') COMP(EQ)
     C     S0            CAT       S1    :1      S2
    MU* VAL1(S2) VAL2('56789 01234') COMP(EQ)
     C     S1            CAT       S0    :1      S2
    MU* VAL1(N0) VAL2(15) COMP(EQ)
     C                   EVAL      N0=%len(S2)
      *
     C                   MOVEL(P)  'ABC'         S2
     C                   MOVEL(P)  'XYZ'         S1
    MU* VAL1(S2) VAL2('ABC  XYZ ') COMP(EQ)
     C                   CAT       S1  :2        S2
      *
     C                   EVAL      S2='ABC'
     C                   EVAL      S1='XYZ'
    MU* VAL1(S2) VAL2('ABC') COMP(EQ)
     C                   CAT       S1  :2        S2
      *
     C                   EVAL      S0=*BLANK
     C                   EVAL      S1=*BLANK
     C                   EVAL      S2=*BLANK
     C                   MOVEL     'ABC'         S2
     C                   MOVEL     'XYZ'         S1
     C                   EVAL      S3=*ZEROS
    MU* VAL1(S3) VAL2('ABC  XYZ') COMP(EQ)
     C     S2            CAT       S1  :2        S3
      *
     C                   EVAL      S2='ABC'
     C                   EVAL      S1='XYZ'
     C                   EVAL      S3=*ZEROS
    MU* VAL1(S3) VAL2('ABC  XYZ0000') COMP(EQ)
     C     S2            CAT       S1  :2        S3
      *
     C                   EVAL      S1=*BLANK
     C                   EVAL      S2=*BLANK
     C                   MOVEL     'ABC'         S2
     C                   MOVEL     'XYZ'         S1
     C                   EVAL      S3=*ZEROS
    MU* VAL1(S3) VAL2('ABC  XYZ') COMP(EQ)
     C     S2            CAT(P)    S1  :2        S3
      *
     C                   EVAL      S2='ABC'
     C                   EVAL      S1='XYZ'
     C                   EVAL      S3=*ZEROS
    MU* VAL1(S3) VAL2('ABC  XYZ') COMP(EQ)
     C     S2            CAT(P)    S1  :2        S3
      *
     C                   EVAL      S0='/400'
     C                   EVAL      S1=*BLANK
    MU* VAL1(S1) VAL2('RPG/400') COMP(EQ)
     C     'RPG'         CAT       S0            S1
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
     C                   CLEAR                   NN                3 0
    MU* VAL1(NN) VAL2(2) COMP(EQ)
     C     'A'           CHECK     S4            NN
     C                   CLEAR                   NN                3 0
    MU* VAL1(*IN(50)) VAL2('1') COMP(EQ)
     C     'A'           CHECK     S4            NN                       50
      *
     C                   CLEAR                   NN                3 0
    MU* VAL1(NN) VAL2(1) COMP(EQ)
     C     'B'           CHECK     S4            NN
     C                   CLEAR                   NN                3 0
    MU* VAL1(*IN(50)) VAL2('1') COMP(EQ)
     C     'B'           CHECK     S4                                     50
      *
     C                   CLEAR                   NN                3 0
    MU* VAL1(NN) VAL2(0) COMP(EQ)
     C     '?'           CHECK     S4:15         NN
     C                   CLEAR                   NN                3 0
    MU* VAL1(*IN(50)) VAL2('0') COMP(EQ)
     C     '?'           CHECK     S4:15                                  50
      *
     C                   CLEAR                   NN                3 0
    MU* VAL1(NN) VAL2(5) COMP(EQ)
     C     '.'           CHECK     S4:4          NN
     C                   CLEAR                   NN                3 0
    MU* VAL1(*IN(50)) VAL2('1') COMP(EQ)
     C     '.'           CHECK     S4:4                                   50
      *
     C                   CLEAR                   NN                3 0
    MU* VAL1(NN) VAL2(0) COMP(EQ)
     C     'A'           CHECKR    S4:1          NN
     C                   CLEAR                   NN                3 0
    MU* VAL1(*IN(50)) VAL2('0') COMP(EQ)
     C     'A'           CHECKR    S4:1                                   50
      *
     C                   CLEAR                   NN                3 0
    MU* VAL1(NN) VAL2(15) COMP(EQ)
     C     'A'           CHECKR    S4            NN
     C                   CLEAR                   NN                3 0
    MU* VAL1(*IN(50)) VAL2('1') COMP(EQ)
     C     'A'           CHECKR    S4                                     50
      *
     C                   CLEAR                   NN                3 0
    MU* VAL1(NN) VAL2(15) COMP(EQ)
     C     'B'           CHECKR    S4            NN
     C                   CLEAR                   NN                3 0
    MU* VAL1(*IN(50)) VAL2('1') COMP(EQ)
     C     'B'           CHECKR    S4            NN                       50
      *
     C                   CLEAR                   NN                3 0
    MU* VAL1(NN) VAL2(14) COMP(EQ)
     C     '?'           CHECKR    S4:15         NN
     C                   CLEAR                   NN                3 0
    MU* VAL1(*IN(50)) VAL2('1') COMP(EQ)
     C     '?'           CHECKR    S4:15         NN                       50
      *
     C                   CLEAR                   NN                3 0
    MU* VAL1(NN) VAL2(3) COMP(EQ)
     C     '.'           CHECKR    S4:4          NN
     C                   CLEAR                   NN                3 0
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
    MU* VAL1(N0) VAL2(0) COMP(EQ)
     C                   EVAL      N0=%len(S0)
    MU* VAL1(S1) VAL2('') COMP(EQ)
     C                   CLEAR                   S1
    MU* VAL1(N0) VAL2(0) COMP(EQ)
     C                   EVAL      N0=%len(S1)
    MU* VAL1(S2) VAL2('') COMP(EQ)
     C                   CLEAR                   S2
    MU* VAL1(N0) VAL2(0) COMP(EQ)
     C                   EVAL      N0=%len(S2)
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
    M** VAL1(S2) VAL2('GHILMNOPQRSTUVZ') COMP(EQ)
     C**                 EVALR     S2=AAA021
      *
    MU* VAL1(S2) VAL2('000000000000000')  COMP(EQ)
     C                   EVAL      S2=S0
      *
     C                   EVAL      N1=15
     C                   IF        N1=%LEN(S1)
     C                   EVAL      N1=15
     C                   ELSE
     C                   EVAL      N1=0
     C                   ENDIF
    MU* VAL1(N1) VAL2(15)  COMP(EQ)
     C                   EVAL      N1=N1
      *
     C                   CLEAR                   *IN01
    MU* VAL1(S0) VAL2('AAAA 0 BBBB')  COMP(EQ)
     C                   EVAL      S0='AAAA ' + *IN01 + ' BBBB'
     C                   SETON                                        01
    MU* VAL1(S0) VAL2('AAAA 1 BBBB')  COMP(EQ)
     C                   EVAL      S0='AAAA ' + *IN01 + ' BBBB'
      *
    MU* VAL1(S0) VAL2('AAAA')  COMP(EQ)
     C                   EVAL      %len(S0)=4
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test MOVE
      *---------------------------------------------------------------------
     C     F_MOVE        BEGSR
      *
     C**                 CLEAR                   S0
     C**                 CLEAR                   S1
     C**                 CLEAR                   S2
     C                   EVAL      S0=*BLANK
     C                   EVAL      S1=*BLANK
     C                   EVAL      S2=*BLANK
     C                   EVAL      S5=*BLANK
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
     C                   IF        S2=*ALL'ABCD'
     C                   MOVEL     *ALL' '       S2
     C                   ENDIF
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
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test MOVEL
      *---------------------------------------------------------------------
     C     F_MOVEL       BEGSR
      *
     C**                 CLEAR                   S0
     C**                 CLEAR                   S1
     C**                 CLEAR                   S2
     C                   EVAL      S0=*BLANK
     C                   EVAL      S1=*BLANK
     C                   EVAL      S2=*BLANK
     C                   EVAL      S5=*BLANK
      *
     C                   MOVEL     123           S0
     C                   MOVE      456           S0
    MU* VAL1(S0) VAL2('TEST_0      456') COMP(EQ)
     C                   MOVEL     'TEST_0'      S0
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
    MU* VAL1(S0) VAL2('') COMP(EQ)
     C                   CLEAR                   S0
    MU* VAL1(S1) VAL2('') COMP(EQ)
     C                   CLEAR                   S1
    MU* VAL1(S2) VAL2('') COMP(EQ)
     C                   CLEAR                   S2
    MU* VAL1(S0) VAL2('ABC') COMP(EQ)
     C                   RESET                   S0
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
     C**                 CLEAR                   S0
     C**                 CLEAR                   S1
     C**                 CLEAR                   S2
     C                   EVAL      S0=*BLANK
     C                   EVAL      S1=*BLANK
     C                   EVAL      S2=*BLANK
      *
     C                   EVAL      S0='**AA!!'
     C                   EVAL      S1='**'
    MU* VAL1(N1) VAL2(1) COMP(EQ)
     C     S1            SCAN      S0            N1
    MU* VAL1(N1) VAL2(1) COMP(EQ)
     C     S1:2          SCAN      S0            N1
    MU* VAL1(N1) VAL2(0) COMP(EQ)
     C     S1:2          SCAN      S0:3          N1
      *
     C                   EVAL      N1=%SCAN('AA':S1:2)
      *
     C                   EVAL      S0='!'
    MU* VAL1(*IN50) VAL2('1') COMP(EQ)
     C     S0    :1      SCAN      '?!/'                                  50
     C                   EVAL      S0    =*BLANK
    MU* VAL1(*IN50) VAL2('0') COMP(EQ)
     C     S0    :1      SCAN      '?!/'                                  50
      *
     C                   EVAL      S0    =*BLANK
     C                   EVAL      S0    ='!'
    MU* VAL1(*IN50) VAL2(*ON) COMP(EQ)
     C     S0    :1      SCAN      '?!/'         N1                       50
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test SUBST
      *---------------------------------------------------------------------
     C     F_SUBST       BEGSR
      *
     C**                 CLEAR                   S0
     C**                 CLEAR                   S1
     C**                 CLEAR                   S2
     C                   EVAL      S0=*BLANK
     C                   EVAL      S1=*BLANK
     C                   EVAL      S2=*BLANK
      *
     C                   EVAL      S0='ABCDEFGHILMNOPQ'                         COSTANTE
    MU* VAL1(S1) VAL2('ABCDEFG') COMP(EQ)
     C     7             SUBST     S0:1          S1
    MU* VAL1(S2) VAL2('HILMNOPQ') COMP(EQ)
     C                   SUBST     S0:8          S2
      *
     C                   EVAL      S2=*BLANK
    MU* VAL1(S2) VAL2('') COMP(EQ)
     C                   EVAL      S2=%SUBST(AAA021:1:0)
      *
     C                   EVAL      S2=*BLANK
    MU* VAL1(S2) VAL2('A') COMP(EQ)
     C                   EVAL      %SUBST(S2:1:1)=AAA021
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test TESTN
      *---------------------------------------------------------------------
     C     F_TESTN       BEGSR
      *
     C                   EVAL      S5=*BLANK
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
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test XLATE
      *---------------------------------------------------------------------
     C     F_XLATE       BEGSR
      *
     C                   EVAL      S6     =*BLANK
     C                   EVAL      S7     =*BLANK
      *
     C                   EVAL      S6     ='AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA'COSTANTE
     C                   MOVEL     'rpg dept'    String1          20            COSTANTE
    MU* VAL1(S7) VAL2('RPG DEPT') COMP(EQ)
     C     Lo:Up         XLATE(P)  String1       S7
    MU* VAL1(S6) VAL2('RPG Dept            AAAAAAAAAA') COMP(EQ)
     C     Up:Lo         XLATE     S7     :6     S6
    MU* VAL1(S6) VAL2('RPG Dept') COMP(EQ)
     C     Up:Lo         XLATE(P)  S7     :6     S6
     C                   EVAL      String1= '**Test - xlate**'                  COSTANTE
    MU* VAL1(S7) VAL2('__Test - xlate__') COMP(EQ)
     C     '*':'_'       XLATE(P)  String1       S7
     C                   MOVEL(P)  'è'           $A                1
    MU* VAL1(S7) VAL2('èèTest - xlateèè') COMP(EQ)
     C     '_':$A        XLATE(P)  S7            S7
      *
    MU* VAL1(S7) VAL2('**TEST - XLATE**') COMP(EQ)
     C     Lo:Up         XLATE     String1       S7
     C                   Z-ADD     7             $B                2 0
    MU* VAL1(S7) VAL2('**Test - XLATE**') COMP(EQ)
     C     Lo:Up         XLATE     String1:$B    S7
     U* VAL1(S7) VAL2('**Test - XLATE**') COMP(EQ)
     C*    Lo:Up         XLATE(P)  String1:$B    S7
    MU* VAL1(S7) VAL2('**Test - XLATE**') COMP(EQ)
     C     Lo:Up         XLATE(P)  String1:$B    S7
     U* VAL1(S7) VAL2('**Test - XLATE**') COMP(EQ)
     C*    Lo:Up         XLATE     String1:$B    S7
      *
    MU* VAL1(S7) VAL2('**Test - xlate**') COMP(EQ)
     C     'C':'c'       XLATE     String1:$B    S7                     50
    MU* VAL1(S7) VAL2('AAA  t - xlate**') COMP(EQ)
     C     'C':'c'       XLATE     'AAA  ':1     S7
    MU* VAL1(S7) VAL2('**Test - xlBte**') COMP(EQ)
     C     Lo:'B'        XLATE     String1:$B    S7
     C                   MOVEL     'rpg dept'    String1          20            COSTANTE
      *
     C                   CLEAR                   String2          30
     C                   EVAL      S6=*BLANK
     C                   EVAL      String2='ABCDEFGHIJKLMNOPQRSTUVWXYZ'         COSTANTE
    MU* VAL1(S6) VAL2('zyxwvutsrqponmlkjihgfedcba') COMP(EQ)
     C     Up:Lo1        XLATE     String2       S6
    MU* VAL1(S6) VAL2('!?CDEFGHIJKLMNOPQRSTUVWXYZ') COMP(EQ)
     C     Up:'!?'       XLATE     String2       S6
    MU* VAL1(S6) VAL2('zyxwvFGHIJKLMNOPQRSTUVWXYZ') COMP(EQ)
     C     Up:Lo1        XLATE     'ABCDE'       S6
    MU* VAL1(S6) VAL2('!?CDEFGHIJKLMNOPQRSTUVWXYZ') COMP(EQ)
     C     Up:'!?'       XLATE     'ABCDE':1     S6
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test BUILT-IN FUNCTION
      *---------------------------------------------------------------------
     C     F_BUILTIN     BEGSR
      * %TRIM
     C                   EVAL      S8=*BLANK
     C                   MOVEL(P)  '  AAA  '     VAR1             30
    MU* VAL1(S8   ) VAL2('AAA:') COMP(EQ)
     C                   EVAL      S8    = %TRIM(VAR1) +':'
    MU* VAL1(S8   ) VAL2('AAA                         :') COMP(EQ)
     C                   EVAL      S8    = %TRIML(VAR1) +':'
    MU* VAL1(S8   ) VAL2('  AAA:') COMP(EQ)
     C                   EVAL      S8    = %TRIMR(VAR1) +':'
      * %REPLACE
     C                   EVAL      S8=*BLANK
     C                   MOVEL(P)  'WINDSOR'     VAR1
     C                   MOVEL(P)  'ONTARIO'     VAR2             30
     C                   MOVEL(P)  'CANADA'      VAR3             30
     C                   MOVEL(P)  'CALIFORNIA'  VAR4             30
      *
    MU* VAL1(S8) VAL2('WINDSOR, ON               ') COMP(EQ)
     C                   EVAL      S8     = %TRIM(VAR1) + ', ' + 'ON'
    MU* VAL1(S8) VAL2('TORONTO, ON               ') COMP(EQ)
     C                   EVAL      S8     = %REPLACE ('TORONTO': %TRIM(S8   ))  COSTANTE
    MU* VAL1(S8) VAL2('TORONTO, CANADA           ') COMP(EQ)
     C                   EVAL      S8     = %REPLACE (VAR3: %TRIM(S8   ) :
     C                             %SCAN(',': %TRIM(S8   ) ) + 2)
    MU* VAL1(S8) VAL2('TORONTO, ONTARIO, CANADA        ') COMP(EQ)
     C                   EVAL      S8     = %REPLACE (', ' + %TRIM(VAR2):
     C                             %TRIM(S8   ):%SCAN (',': %TRIM(S8   ) ): 0)
    MU* VAL1(S8) VAL2('SCARBOROUGH, ONTARIO, CANADA    ') COMP(EQ)
     C                   EVAL      S8     = %REPLACE ('SCARBOROUGH':            COSTANTE
     C                             %TRIM(S8   ):1:
     C                             %SCAN (',': %TRIM(S8   )) - 1)
    MU* VAL1(S8) VAL2('ONTARIO, CANADA    ') COMP(EQ)
     C                   EVAL      S8     = %REPLACE ('': %TRIM(S8   ) : 1:
     C                             %SCAN (',': %TRIM(S8   ) ) + 1)
    MU* VAL1(S8) VAL2('ONTARIO, CALIFORNIA') COMP(EQ)
     C                   EVAL      S8     = %REPLACE (VAR4: %TRIM(S8   ) :
     C                             %SCAN (',': %TRIM(S8   ) ) + 2)
    MU* VAL1(S8) VAL2('SOMEWHERE ELSE: ONTARIO, CALIFORNIA') COMP(EQ)
     C                   EVAL      S8     = %REPLACE ('SOMEWHERE ELSE: ':       COSTANTE
     C                             %TRIM(S8   ) : 1: 0)
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test
      *---------------------------------------------------------------------
     C     F_OTHER       BEGSR
      *
     C                   CLEAR                   S0
     C                   EVAL      S0='   '
    MU* VAL1(N0) VAL2(3) COMP(EQ)
     C                   EVAL      N0=%len(S0)
    MU* VAL1(S0) VAL2('   ') COMP(EQ)
     C                   MOVE      *BLANKS       S0
    MU* VAL1(S0) VAL2('   ') COMP(EQ)
     C                   MOVE(P)   *BLANKS       S0
    MU* VAL1(S0) VAL2('000') COMP(EQ)
     C                   MOVEL     *ZEROS        S0
    MU* VAL1(S0) VAL2('000') COMP(EQ)
     C                   MOVEL(P)  *ZEROS        S0
      *
     C                   CLEAR                   S0
    MU* VAL1(S0) VAL2('000000000000000') COMP(EQ)
     C                   EVAL      S0=*ZEROS
      *
    MU* VAL1(N0) VAL2(15) COMP(EQ)
     C                   EVAL      N0=%len(S0)
      *
     C                   CLEAR                   S0
    MU* VAL1(S0) VAL2('               ') COMP(EQ)
     C                   EVAL      S0=*BLANK
    MU* VAL1(N0) VAL2(15) COMP(EQ)
     C                   EVAL      N0=%len(S0)
     C                   CLEAR                   S0
    MU* VAL1(N0) VAL2(0) COMP(EQ)
     C                   EVAL      N0=%len(S0)
    MU* VAL1(S9) VAL2('ABCDEFGH') COMP(EQ)
     C                   EVAL      S9='ABCDEFGH'
    MU* VAL1(O9) VAL2(4) COMP(EQ)
     C                   EVAL      O9=4
      *
     C                   ENDSR
