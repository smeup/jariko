      * Benchmark of RPG procedure calls (interpreter cost per call).
      * Modeled on a real test-data generator: for every record, about
      * 500 calls to small procedures (local and STATIC variables, packed
      * arithmetic, MOVE/MOVEL/Z-ADD/MULT/DIV, a DOWGT loop, nested calls)
      * with values picked from compile-time arrays.
      * The clock (%TIMESTAMP, TIME) is replaced by fixed values: the
      * DSPLY checksum must not change with an optimization.
     DexecCounter      S             10U 0 INZ(1)
     DTsBase           S             26A   INZ('2024-01-15-10.20.30.123456')
     DSCH_Let          S              1    DIM(026) CTDATA PERRCD(1)
     DSCH_Dat          S             10    DIM(040) CTDATA PERRCD(1)
     DSCH_Fra          S            100    DIM(100) CTDATA PERRCD(1)
     DSCH_Ogg          S            100    DIM(029) CTDATA PERRCD(1)
     DSCH_Liv          S            100    DIM(004) CTDATA PERRCD(1)
     DSCH_Sta          S            100    DIM(009) CTDATA PERRCD(1)
     DSCH_Chr          S            100    DIM(102) CTDATA PERRCD(1)
     DNum              S              9  0
     DRec              S             10  0
     DNREC             S             10  0 INZ(75)
     DMNUMR            S             10  0
     DVALORE           S             21  6 DIM(99) INZ
     DTot              S             21  6 INZ
     DTotChar          S             30A
     DFD03A            S             10  0
     DFD03B            S              1A
     DFD03C            S              9  0
     DFD03D            S            100A
     DFD03E            S             15  0
     DFD03F            S             21  6
     DFD03G1           S            100A
     DFD03G2           S            100A
     DFD03G3           S            100A
     DFD03G4           S            100A
     DFD03G5           S            100A
     DFD03H            S            100A
     DFTIPA            S            100A
     DFLIVE            S            100A
     DFSTAT            S            100A
     DFDTIN            S              8  0
     DFORIN            S              6  0
     DFUSIN            S             10A
     DFPRIN            S             10A
     DFDTAG            S              8  0
     DFORAG            S              6  0
     DFUSAG            S             10A
     DFPRAG            S             10A
     DFC001            S             21  6
     DFC002            S             21  6
     DFC003            S             21  6
     DFC004            S             21  6
     DFC005            S             21  6
     DFC006            S             21  6
     DFC007            S             21  6
     DFC008            S             21  6
     DFC009            S             21  6
     DFC010            S             21  6
     DFC011            S             21  6
     DFC012            S             21  6
     DFC013            S             21  6
     DFC014            S             21  6
     DFC015            S             21  6
     DFC016            S             21  6
     DFC017            S             21  6
     DFC018            S             21  6
     DFC019            S             21  6
     DFC020            S             21  6
     DFC021            S             21  6
     DFC022            S             21  6
     DFC023            S             21  6
     DFC024            S             21  6
     DFC025            S             21  6
     DFC026            S             21  6
     DFC027            S             21  6
     DFC028            S             21  6
     DFC029            S             21  6
     DFC030            S             21  6
     DFC031            S             21  6
     DFC032            S             21  6
     DFC033            S             21  6
     DFC034            S             21  6
     DFC035            S             21  6
     DFC036            S             21  6
     DFC037            S             21  6
     DFC038            S             21  6
     DFC039            S             21  6
     DFC040            S             21  6
     DFC041            S             21  6
     DFC042            S             21  6
     DFC043            S             21  6
     DFC044            S             21  6
     DFC045            S             21  6
     DFC046            S             21  6
     DFC047            S             21  6
     DFC048            S             21  6
     DFC049            S             21  6
     DFC050            S             21  6
     DFC051            S             21  6
     DFC052            S             21  6
     DFC053            S             21  6
     DFC054            S             21  6
     DFC055            S             21  6
     DFC056            S             21  6
     DFC057            S             21  6
     DFC058            S             21  6
     DFC059            S             21  6
     DFC060            S             21  6
     DFC061            S             21  6
     DFC062            S             21  6
     DFC063            S             21  6
     DFC064            S             21  6
     DFC065            S             21  6
     DFC066            S             21  6
     DFC067            S             21  6
     DFC068            S             21  6
     DFC069            S             21  6
     DFC070            S             21  6
     DFC071            S             21  6
     DFC072            S             21  6
     DFC073            S             21  6
     DFC074            S             21  6
     DFC075            S             21  6
     DFC076            S             21  6
     DFC077            S             21  6
     DFC078            S             21  6
     DFC079            S             21  6
     DFC080            S             21  6
     DFC081            S             21  6
     DFC082            S             21  6
     DFC083            S             21  6
     DFC084            S             21  6
     DFC085            S             21  6
     DFC086            S             21  6
     DFC087            S             21  6
     DFC088            S             21  6
     DFC089            S             21  6
     DFC090            S             21  6
     DFC091            S             21  6
     DFC092            S             21  6
     DFC093            S             21  6
     DFC094            S             21  6
     DFC095            S             21  6
     DFC096            S             21  6
     DFC097            S             21  6
     DFC098            S             21  6
     DFC099            S             21  6
     DP_RND            PR            10I 0
     D pMin                          10I 0 VALUE
     D pMax                          10I 0 VALUE
     DRandNext         PR            30P 9
     D pMin                          10I 0 VALUE
     D pMax                          10I 0 VALUE
     DRandInt          PR            10I 0
     D pMin                          10I 0 VALUE
     D pMax                          10I 0 VALUE
     DRandDec          PR            15P 5
     D pMin                          15P 5 VALUE
     D pMax                          15P 5 VALUE
     D pDec                          10I 0 VALUE
     C                   EVAL      Tot = 0
     C                   FOR       Rec = 1 TO NREC
     C                   EXSR      SCRREC
     C                   ENDFOR
     C                   EVAL      TotChar = %CHAR(Tot)
     C     TotChar       DSPLY
    MU* TIMEOUT(20)
     C                   SETON                                        LR
     C     SCRREC        BEGSR
     C                   EVAL      MNUMR += 1
     C                   EVAL      FD03A = MNUMR
     C                   EVAL      FD03B = SCH_Let(P_RND(1:%ELEM(SCH_Let)))
     C                   EVAL      FD03C=%INT(SCH_Dat(P_RND(1:%ELEM(SCH_Dat))))
     C                   EVAL      FD03D = SCH_Fra(P_RND(1:%ELEM(SCH_Fra)))
     C                   EVAL      FD03E = RandInt(P_RND(1:%ELEM(SCH_Fra)):
     C                                             P_RND(1:%ELEM(SCH_Fra)))
     C                   EVAL      FD03F = RandDec(P_RND(1:%ELEM(SCH_Fra)):
     C                                             P_RND(1:%ELEM(SCH_Fra)):6)
     C                                    * P_RND(1:%ELEM(SCH_Fra)) * 3.145678
     C                   EVAL      FD03G1 = SCH_Fra(P_RND(1:%ELEM(SCH_Fra)))
     C                   EVAL      FD03G2 = SCH_Fra(P_RND(1:%ELEM(SCH_Fra)))
     C                   EVAL      FD03G3 = SCH_Fra(P_RND(1:%ELEM(SCH_Fra)))
     C                   EVAL      FD03G4 = SCH_Fra(P_RND(1:%ELEM(SCH_Fra)))
     C                   EVAL      FD03G5 = SCH_Fra(P_RND(1:%ELEM(SCH_Fra)))
     C                   EVAL      FD03H = SCH_Chr(P_RND(1:%ELEM(SCH_Chr)))
     C                   EVAL      FTIPA = SCH_Ogg(P_RND(1:%ELEM(SCH_Ogg)))
     C                   EVAL      FLIVE = SCH_Liv(P_RND(1:%ELEM(SCH_Liv)))
     C                   EVAL      FSTAT = SCH_Sta(P_RND(1:%ELEM(SCH_Sta)))
     C                   EVAL      FDTIN = 0
     C                   EVAL      FORIN = 0
     C                   EVAL      FUSIN = ' '
     C                   EVAL      FPRIN = ' '
     C                   EVAL      FDTAG = 0
     C                   EVAL      FORAG = 0
     C                   EVAL      FUSAG = ' '
     C                   EVAL      FPRAG = ' '
     C                   FOR       Num = 1 TO %ELEM(VALORE)
     C                   EVAL      VALORE(Num)=RandDec(P_RND(1:%ELEM(SCH_Fra)):
     C                                             P_RND(1:%ELEM(SCH_Fra)):6)
     C                                    * P_RND(1:%ELEM(SCH_Fra)) * 3.145678
     C                   ENDFOR
     C                   EVAL      FC001 = VALORE(01)
     C                   EVAL      FC002 = VALORE(02)
     C                   EVAL      FC003 = VALORE(03)
     C                   EVAL      FC004 = VALORE(04)
     C                   EVAL      FC005 = VALORE(05)
     C                   EVAL      FC006 = VALORE(06)
     C                   EVAL      FC007 = VALORE(07)
     C                   EVAL      FC008 = VALORE(08)
     C                   EVAL      FC009 = VALORE(09)
     C                   EVAL      FC010 = VALORE(10)
     C                   EVAL      FC011 = VALORE(11)
     C                   EVAL      FC012 = VALORE(12)
     C                   EVAL      FC013 = VALORE(13)
     C                   EVAL      FC014 = VALORE(14)
     C                   EVAL      FC015 = VALORE(15)
     C                   EVAL      FC016 = VALORE(16)
     C                   EVAL      FC017 = VALORE(17)
     C                   EVAL      FC018 = VALORE(18)
     C                   EVAL      FC019 = VALORE(19)
     C                   EVAL      FC020 = VALORE(20)
     C                   EVAL      FC021 = VALORE(21)
     C                   EVAL      FC022 = VALORE(22)
     C                   EVAL      FC023 = VALORE(23)
     C                   EVAL      FC024 = VALORE(24)
     C                   EVAL      FC025 = VALORE(25)
     C                   EVAL      FC026 = VALORE(26)
     C                   EVAL      FC027 = VALORE(27)
     C                   EVAL      FC028 = VALORE(28)
     C                   EVAL      FC029 = VALORE(29)
     C                   EVAL      FC030 = VALORE(30)
     C                   EVAL      FC031 = VALORE(31)
     C                   EVAL      FC032 = VALORE(32)
     C                   EVAL      FC033 = VALORE(33)
     C                   EVAL      FC034 = VALORE(34)
     C                   EVAL      FC035 = VALORE(35)
     C                   EVAL      FC036 = VALORE(36)
     C                   EVAL      FC037 = VALORE(37)
     C                   EVAL      FC038 = VALORE(38)
     C                   EVAL      FC039 = VALORE(39)
     C                   EVAL      FC040 = VALORE(40)
     C                   EVAL      FC041 = VALORE(41)
     C                   EVAL      FC042 = VALORE(42)
     C                   EVAL      FC043 = VALORE(43)
     C                   EVAL      FC044 = VALORE(44)
     C                   EVAL      FC045 = VALORE(45)
     C                   EVAL      FC046 = VALORE(46)
     C                   EVAL      FC047 = VALORE(47)
     C                   EVAL      FC048 = VALORE(48)
     C                   EVAL      FC049 = VALORE(49)
     C                   EVAL      FC050 = VALORE(50)
     C                   EVAL      FC051 = VALORE(51)
     C                   EVAL      FC052 = VALORE(52)
     C                   EVAL      FC053 = VALORE(53)
     C                   EVAL      FC054 = VALORE(54)
     C                   EVAL      FC055 = VALORE(55)
     C                   EVAL      FC056 = VALORE(56)
     C                   EVAL      FC057 = VALORE(57)
     C                   EVAL      FC058 = VALORE(58)
     C                   EVAL      FC059 = VALORE(59)
     C                   EVAL      FC060 = VALORE(60)
     C                   EVAL      FC061 = VALORE(61)
     C                   EVAL      FC062 = VALORE(62)
     C                   EVAL      FC063 = VALORE(63)
     C                   EVAL      FC064 = VALORE(64)
     C                   EVAL      FC065 = VALORE(65)
     C                   EVAL      FC066 = VALORE(66)
     C                   EVAL      FC067 = VALORE(67)
     C                   EVAL      FC068 = VALORE(68)
     C                   EVAL      FC069 = VALORE(69)
     C                   EVAL      FC070 = VALORE(70)
     C                   EVAL      FC071 = VALORE(71)
     C                   EVAL      FC072 = VALORE(72)
     C                   EVAL      FC073 = VALORE(73)
     C                   EVAL      FC074 = VALORE(74)
     C                   EVAL      FC075 = VALORE(75)
     C                   EVAL      FC076 = VALORE(76)
     C                   EVAL      FC077 = VALORE(77)
     C                   EVAL      FC078 = VALORE(78)
     C                   EVAL      FC079 = VALORE(79)
     C                   EVAL      FC080 = VALORE(80)
     C                   EVAL      FC081 = VALORE(81)
     C                   EVAL      FC082 = VALORE(82)
     C                   EVAL      FC083 = VALORE(83)
     C                   EVAL      FC084 = VALORE(84)
     C                   EVAL      FC085 = VALORE(85)
     C                   EVAL      FC086 = VALORE(86)
     C                   EVAL      FC087 = VALORE(87)
     C                   EVAL      FC088 = VALORE(88)
     C                   EVAL      FC089 = VALORE(89)
     C                   EVAL      FC090 = VALORE(90)
     C                   EVAL      FC091 = VALORE(91)
     C                   EVAL      FC092 = VALORE(92)
     C                   EVAL      FC093 = VALORE(93)
     C                   EVAL      FC094 = VALORE(94)
     C                   EVAL      FC095 = VALORE(95)
     C                   EVAL      FC096 = VALORE(96)
     C                   EVAL      FC097 = VALORE(97)
     C                   EVAL      FC098 = VALORE(98)
     C                   EVAL      FC099 = VALORE(99)
     C                   EVAL      Tot = Tot + FD03F + FD03E + FC001 + FC099
     C                   ENDSR
     PP_RND            B
     DP_RND            PI            10I 0
     D pMin                          10I 0 VALUE
     D pMax                          10I 0 VALUE
     DnumeroRandom     S              5P 0
     Drange            S              5P 0
     Dseed             S             10U 0
     DtimeStamp        S               Z
     DtimeChar         S             26A
     DseedPart         S              8A
     Dmultiplier       S             10U 0 INZ(1103515245)
     Dincrement        S             10U 0 INZ(12345)
     Dmodulus          S             10U 0 INZ(2147483647)
     DtempNum          S             15P 0
     DcombinedSeed     S             15P 0
     C                   EVAL      timeChar = TsBase
     C                   EVAL      seedPart = %SUBST(timeChar:18:8)
     C                   EVAL      combinedSeed = %INT(seedPart)
     C                   MULT      10000         combinedSeed
     C                   ADD       execCounter   combinedSeed
     C                   EVAL      seed = %REM(combinedSeed : modulus)
     C                   ADD       1             execCounter
     C                   EVAL      range = pMax - pMin + 1
     C                   EVAL      seed = %REM((seed * multiplier + increment +
     C                                   execCounter * 7919) : modulus)
     C                   EVAL      tempNum = %REM(seed : range)
     C                   EVAL      numeroRandom = pMin + tempNum
     C                   RETURN    numeroRandom
     PP_RND            E
     PRandNext         B
     DRandNext         PI            30P 9
     D pMin                          10I 0 VALUE
     D pMax                          10I 0 VALUE
     DSeed             S             10I 0 STATIC
     DInit             S              1N   STATIC INZ(*OFF)
     DRan              S             30P 9
     DTs               S               Z
     Ds                S             26
     Dsec              S              2S 0
     Dmsec             S              6S 0
     C                   IF        NOT Init
     C                   EVAL      s = TsBase
     C                   EVAL      sec = %DEC(%SUBST(s : 18 : 2) : 2 : 0)
     C                   EVAL      msec = %DEC(%SUBST(s : 21 : 6) : 6 : 0)
     C                   EVAL      Seed = msec
     C                   EVAL      Seed = Seed + sec * 1000000
     C                   EVAL      Seed = Seed + 1
     C                   EVAL      Init = *ON
     C                   Z-ADD     102030150124  CLOCK            12 0
     C                   MOVEL     CLOCK         NNN060            6 0
     C                   MOVE      CLOCK         MMM060            6 0
     C                   MOVEL     MMM060        CLOCK
     C                   MOVE      NNN060        CLOCK
     C                   Z-ADD     CLOCK         CLOCK1           12 0
     C                   Z-ADD     CLOCK         CLOCK2           12 0
     C                   MOVE      CLOCK         NNN020            2 0
     C     NNN020        MULT      CLOCK         CLOCK1           12 0
     C     NNN020        MULT      CLOCK         CLOCK2           12 0
     C                   ENDIF
     C     pMin          IFGT      pMax
     C                   Z-ADD     pMin          NNN100           10 0
     C                   Z-ADD     pMax          pMin
     C                   Z-ADD     NNN100        pMax
     C                   ENDIF
     C                   Z-ADD     pMax          SUP              10 0
     C                   SUB       pMin          SUP
     C     pMin          IFEQ      pMax
     C                   Z-ADD     pMin          Ran
     C                   ELSE
     C     CLOCK1        MULT      CLOCK2        CLOCK3           22 8
     C     CLOCK3        DOWGT     SUP
     C     CLOCK3        DIV       3.1729        CLOCK3
     C                   ENDDO
     C                   Z-ADD     CLOCK3        Ran
     C                   ADD       pMin          Ran
     C                   Z-ADD     CLOCK2        CLOCK1
     C                   Z-ADD     CLOCK3        CLOCK2
     C                   MULT      NNN020        CLOCK1
     C                   MULT      NNN020        CLOCK2
     C                   ENDIF
     C                   RETURN    Ran
     PRandNext         E
     PRandInt          B
     DRandInt          PI            10I 0
     D pMin                          10I 0 VALUE
     D pMax                          10I 0 VALUE
     DTmp              S             10I 0
     DNeg              S              1N
     DFattore          S             15P 0
     C                   MOVE      *OFF          Neg
     C                   IF        pMin > pMax
     C                   EVAL      Tmp  = pMin
     C                   EVAL      pMin = pMax
     C                   EVAL      pMax = Tmp
     C                   MOVE      *ON           Neg
     C                   ENDIF
     C                   EVAL      fattore=1
     C                   IF        Neg
     C                   EVAL      fattore=-1
     C                   ENDIF
     C                   RETURN    pMin + %INT(RandNext(pMin:pMax) *
     C                                   (pMax - pMin + 1)) * Fattore
     PRandInt          E
     PRandDec          B
     DRandDec          PI            15P 5
     D pMin                          15P 5 VALUE
     D pMax                          15P 5 VALUE
     D pDec                          10I 0 VALUE
     DVal              S             31P 9
     DFattore          S             15P 0
     DTmp              S             15P 5
     DNeg              S              1N
     C                   MOVE      *OFF          Neg
     C                   IF        pMin > pMax
     C                   EVAL      Tmp  = pMin
     C                   EVAL      pMin = pMax
     C                   EVAL      pMax = Tmp
     C                   MOVE      *ON           Neg
     C                   ENDIF
     C                   EVAL      Val = pmin + RandNext(pMin:pMax) *
     C                                   (pMax - pMin)
     C                   IF        Neg
     C                   EVAL      val*=-1
     C                   ENDIF
     C                   EVAL      Fattore = 10 ** pDec
     C                   EVAL(RH)  Tmp=%DEC(Val * Fattore : 31 : 0) / Fattore
     C                   RETURN    Tmp
     PRandDec          E
** SCH_Let
A
B
C
D
E
F
G
H
I
J
K
L
M
N
O
P
Q
R
S
T
U
V
W
X
Y
Z
** SCH_Dat
20000101
20010202
20020303
20030404
20040505
20050606
20060707
20070808
20080909
20091010
20101111
20111212
20120113
20130214
20140315
20150416
20160517
20170618
20180719
20190820
20200921
20211022
20221123
20231224
20240125
20250226
20260327
20270428
20280501
20290602
20000703
20010804
20020905
20031006
20041107
20051208
20060109
20070210
20080311
20090412
** SCH_Fra
Frase 000 ALFA BETA GAMMA DELTA EPSILON ZETA
Frase 001 BETA GAMMA DELTA EPSILON ZETA ETA
Frase 002 GAMMA DELTA EPSILON ZETA ETA THETA
Frase 003 DELTA EPSILON ZETA ETA THETA IOTA
Frase 004 EPSILON ZETA ETA THETA IOTA KAPPA
Frase 005 ZETA ETA THETA IOTA KAPPA ALFA
Frase 006 ETA THETA IOTA KAPPA ALFA BETA
Frase 007 THETA IOTA KAPPA ALFA BETA GAMMA
Frase 008 IOTA KAPPA ALFA BETA GAMMA DELTA
Frase 009 KAPPA ALFA BETA GAMMA DELTA EPSILON
Frase 010 ALFA BETA GAMMA DELTA EPSILON ZETA
Frase 011 BETA GAMMA DELTA EPSILON ZETA ETA
Frase 012 GAMMA DELTA EPSILON ZETA ETA THETA
Frase 013 DELTA EPSILON ZETA ETA THETA IOTA
Frase 014 EPSILON ZETA ETA THETA IOTA KAPPA
Frase 015 ZETA ETA THETA IOTA KAPPA ALFA
Frase 016 ETA THETA IOTA KAPPA ALFA BETA
Frase 017 THETA IOTA KAPPA ALFA BETA GAMMA
Frase 018 IOTA KAPPA ALFA BETA GAMMA DELTA
Frase 019 KAPPA ALFA BETA GAMMA DELTA EPSILON
Frase 020 ALFA BETA GAMMA DELTA EPSILON ZETA
Frase 021 BETA GAMMA DELTA EPSILON ZETA ETA
Frase 022 GAMMA DELTA EPSILON ZETA ETA THETA
Frase 023 DELTA EPSILON ZETA ETA THETA IOTA
Frase 024 EPSILON ZETA ETA THETA IOTA KAPPA
Frase 025 ZETA ETA THETA IOTA KAPPA ALFA
Frase 026 ETA THETA IOTA KAPPA ALFA BETA
Frase 027 THETA IOTA KAPPA ALFA BETA GAMMA
Frase 028 IOTA KAPPA ALFA BETA GAMMA DELTA
Frase 029 KAPPA ALFA BETA GAMMA DELTA EPSILON
Frase 030 ALFA BETA GAMMA DELTA EPSILON ZETA
Frase 031 BETA GAMMA DELTA EPSILON ZETA ETA
Frase 032 GAMMA DELTA EPSILON ZETA ETA THETA
Frase 033 DELTA EPSILON ZETA ETA THETA IOTA
Frase 034 EPSILON ZETA ETA THETA IOTA KAPPA
Frase 035 ZETA ETA THETA IOTA KAPPA ALFA
Frase 036 ETA THETA IOTA KAPPA ALFA BETA
Frase 037 THETA IOTA KAPPA ALFA BETA GAMMA
Frase 038 IOTA KAPPA ALFA BETA GAMMA DELTA
Frase 039 KAPPA ALFA BETA GAMMA DELTA EPSILON
Frase 040 ALFA BETA GAMMA DELTA EPSILON ZETA
Frase 041 BETA GAMMA DELTA EPSILON ZETA ETA
Frase 042 GAMMA DELTA EPSILON ZETA ETA THETA
Frase 043 DELTA EPSILON ZETA ETA THETA IOTA
Frase 044 EPSILON ZETA ETA THETA IOTA KAPPA
Frase 045 ZETA ETA THETA IOTA KAPPA ALFA
Frase 046 ETA THETA IOTA KAPPA ALFA BETA
Frase 047 THETA IOTA KAPPA ALFA BETA GAMMA
Frase 048 IOTA KAPPA ALFA BETA GAMMA DELTA
Frase 049 KAPPA ALFA BETA GAMMA DELTA EPSILON
Frase 050 ALFA BETA GAMMA DELTA EPSILON ZETA
Frase 051 BETA GAMMA DELTA EPSILON ZETA ETA
Frase 052 GAMMA DELTA EPSILON ZETA ETA THETA
Frase 053 DELTA EPSILON ZETA ETA THETA IOTA
Frase 054 EPSILON ZETA ETA THETA IOTA KAPPA
Frase 055 ZETA ETA THETA IOTA KAPPA ALFA
Frase 056 ETA THETA IOTA KAPPA ALFA BETA
Frase 057 THETA IOTA KAPPA ALFA BETA GAMMA
Frase 058 IOTA KAPPA ALFA BETA GAMMA DELTA
Frase 059 KAPPA ALFA BETA GAMMA DELTA EPSILON
Frase 060 ALFA BETA GAMMA DELTA EPSILON ZETA
Frase 061 BETA GAMMA DELTA EPSILON ZETA ETA
Frase 062 GAMMA DELTA EPSILON ZETA ETA THETA
Frase 063 DELTA EPSILON ZETA ETA THETA IOTA
Frase 064 EPSILON ZETA ETA THETA IOTA KAPPA
Frase 065 ZETA ETA THETA IOTA KAPPA ALFA
Frase 066 ETA THETA IOTA KAPPA ALFA BETA
Frase 067 THETA IOTA KAPPA ALFA BETA GAMMA
Frase 068 IOTA KAPPA ALFA BETA GAMMA DELTA
Frase 069 KAPPA ALFA BETA GAMMA DELTA EPSILON
Frase 070 ALFA BETA GAMMA DELTA EPSILON ZETA
Frase 071 BETA GAMMA DELTA EPSILON ZETA ETA
Frase 072 GAMMA DELTA EPSILON ZETA ETA THETA
Frase 073 DELTA EPSILON ZETA ETA THETA IOTA
Frase 074 EPSILON ZETA ETA THETA IOTA KAPPA
Frase 075 ZETA ETA THETA IOTA KAPPA ALFA
Frase 076 ETA THETA IOTA KAPPA ALFA BETA
Frase 077 THETA IOTA KAPPA ALFA BETA GAMMA
Frase 078 IOTA KAPPA ALFA BETA GAMMA DELTA
Frase 079 KAPPA ALFA BETA GAMMA DELTA EPSILON
Frase 080 ALFA BETA GAMMA DELTA EPSILON ZETA
Frase 081 BETA GAMMA DELTA EPSILON ZETA ETA
Frase 082 GAMMA DELTA EPSILON ZETA ETA THETA
Frase 083 DELTA EPSILON ZETA ETA THETA IOTA
Frase 084 EPSILON ZETA ETA THETA IOTA KAPPA
Frase 085 ZETA ETA THETA IOTA KAPPA ALFA
Frase 086 ETA THETA IOTA KAPPA ALFA BETA
Frase 087 THETA IOTA KAPPA ALFA BETA GAMMA
Frase 088 IOTA KAPPA ALFA BETA GAMMA DELTA
Frase 089 KAPPA ALFA BETA GAMMA DELTA EPSILON
Frase 090 ALFA BETA GAMMA DELTA EPSILON ZETA
Frase 091 BETA GAMMA DELTA EPSILON ZETA ETA
Frase 092 GAMMA DELTA EPSILON ZETA ETA THETA
Frase 093 DELTA EPSILON ZETA ETA THETA IOTA
Frase 094 EPSILON ZETA ETA THETA IOTA KAPPA
Frase 095 ZETA ETA THETA IOTA KAPPA ALFA
Frase 096 ETA THETA IOTA KAPPA ALFA BETA
Frase 097 THETA IOTA KAPPA ALFA BETA GAMMA
Frase 098 IOTA KAPPA ALFA BETA GAMMA DELTA
Frase 099 KAPPA ALFA BETA GAMMA DELTA EPSILON
** SCH_Ogg
OGG00 ALFA
OGG01 BETA
OGG02 GAMMA
OGG03 DELTA
OGG04 EPSILON
OGG05 ZETA
OGG06 ETA
OGG07 THETA
OGG08 IOTA
OGG09 KAPPA
OGG10 ALFA
OGG11 BETA
OGG12 GAMMA
OGG13 DELTA
OGG14 EPSILON
OGG15 ZETA
OGG16 ETA
OGG17 THETA
OGG18 IOTA
OGG19 KAPPA
OGG20 ALFA
OGG21 BETA
OGG22 GAMMA
OGG23 DELTA
OGG24 EPSILON
OGG25 ZETA
OGG26 ETA
OGG27 THETA
OGG28 IOTA
** SCH_Liv
LIV0
LIV1
LIV2
LIV3
** SCH_Sta
STA0
STA1
STA2
STA3
STA4
STA5
STA6
STA7
STA8
** SCH_Chr
CHR000 ALFA ALFA
CHR001 DELTA THETA
CHR002 ETA EPSILON
CHR003 KAPPA BETA
CHR004 GAMMA IOTA
CHR005 ZETA ZETA
CHR006 IOTA GAMMA
CHR007 BETA KAPPA
CHR008 EPSILON ETA
CHR009 THETA DELTA
CHR010 ALFA ALFA
CHR011 DELTA THETA
CHR012 ETA EPSILON
CHR013 KAPPA BETA
CHR014 GAMMA IOTA
CHR015 ZETA ZETA
CHR016 IOTA GAMMA
CHR017 BETA KAPPA
CHR018 EPSILON ETA
CHR019 THETA DELTA
CHR020 ALFA ALFA
CHR021 DELTA THETA
CHR022 ETA EPSILON
CHR023 KAPPA BETA
CHR024 GAMMA IOTA
CHR025 ZETA ZETA
CHR026 IOTA GAMMA
CHR027 BETA KAPPA
CHR028 EPSILON ETA
CHR029 THETA DELTA
CHR030 ALFA ALFA
CHR031 DELTA THETA
CHR032 ETA EPSILON
CHR033 KAPPA BETA
CHR034 GAMMA IOTA
CHR035 ZETA ZETA
CHR036 IOTA GAMMA
CHR037 BETA KAPPA
CHR038 EPSILON ETA
CHR039 THETA DELTA
CHR040 ALFA ALFA
CHR041 DELTA THETA
CHR042 ETA EPSILON
CHR043 KAPPA BETA
CHR044 GAMMA IOTA
CHR045 ZETA ZETA
CHR046 IOTA GAMMA
CHR047 BETA KAPPA
CHR048 EPSILON ETA
CHR049 THETA DELTA
CHR050 ALFA ALFA
CHR051 DELTA THETA
CHR052 ETA EPSILON
CHR053 KAPPA BETA
CHR054 GAMMA IOTA
CHR055 ZETA ZETA
CHR056 IOTA GAMMA
CHR057 BETA KAPPA
CHR058 EPSILON ETA
CHR059 THETA DELTA
CHR060 ALFA ALFA
CHR061 DELTA THETA
CHR062 ETA EPSILON
CHR063 KAPPA BETA
CHR064 GAMMA IOTA
CHR065 ZETA ZETA
CHR066 IOTA GAMMA
CHR067 BETA KAPPA
CHR068 EPSILON ETA
CHR069 THETA DELTA
CHR070 ALFA ALFA
CHR071 DELTA THETA
CHR072 ETA EPSILON
CHR073 KAPPA BETA
CHR074 GAMMA IOTA
CHR075 ZETA ZETA
CHR076 IOTA GAMMA
CHR077 BETA KAPPA
CHR078 EPSILON ETA
CHR079 THETA DELTA
CHR080 ALFA ALFA
CHR081 DELTA THETA
CHR082 ETA EPSILON
CHR083 KAPPA BETA
CHR084 GAMMA IOTA
CHR085 ZETA ZETA
CHR086 IOTA GAMMA
CHR087 BETA KAPPA
CHR088 EPSILON ETA
CHR089 THETA DELTA
CHR090 ALFA ALFA
CHR091 DELTA THETA
CHR092 ETA EPSILON
CHR093 KAPPA BETA
CHR094 GAMMA IOTA
CHR095 ZETA ZETA
CHR096 IOTA GAMMA
CHR097 BETA KAPPA
CHR098 EPSILON ETA
CHR099 THETA DELTA
CHR100 ALFA ALFA
CHR101 DELTA THETA
