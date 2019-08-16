   COP* *NOUI
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* 06/04/16  V4.R1   GIAGIU Creato
     V* 29/07/16  V5.R1   GIAGIU Inserimento problema relativo al BREN20D
     V* B£60415A  V4R1   BMA Ricompilato per modifica inizializzazione QUSEC
     V* B£61020C  V5R1    BMA Aggiunta COP* *NOUI
     V* 18/04/17  V5R1    GIAGIU Implementazione test definizione DS (B£G75G - DS MBR)
     V* 12/08/19  001059  BMA Gestita schiera con overlay posizionale
     V* 13/08/19  001059  BMA Tolto overlay scorretto
     V*=====================================================================
     V* OBIETTIVO
     V*  Programma finalizzato ai test slla definizione delle DS
     V*
     V*=====================================================================
     D                 DS
     D AR01                                DIM(100) ASCEND INZ(*HIVAL)
     D  FI01                         15    OVERLAY(AR01:1)
     D  FI02                         10    OVERLAY(AR01:*NEXT)
     D  FI03                         35    OVERLAY(AR01:*NEXT)
     D  FI04                          1    OVERLAY(AR01:*NEXT)
     D  FI05                          1    OVERLAY(AR01:*NEXT)
     D  FI06                          1    OVERLAY(AR01:*NEXT)
     D  FI07                          2  0 OVERLAY(AR01:*NEXT)
     D  FI08                          1    OVERLAY(AR01:*NEXT)
     D  FI09                          1    OVERLAY(AR01:*NEXT)
      *
     D                 DS
     D FIELD01
     D  SUBF11                       15    OVERLAY(FIELD01:1)
     D  SUBF12                       10    OVERLAY(FIELD01:*NEXT)
      *
     D                 DS
     D FIELD02
     D  SUBF21                       15    OVERLAY(FIELD02:1)
     D  SUBF22                       10    OVERLAY(FIELD02:*NEXT)
     D  SUBF23                        5  0 OVERLAY(FIELD02:*NEXT)
     D  SUBF24                        6  2 OVERLAY(FIELD02:*NEXT)
      *
     D                 DS
     D FIELD03
     D  SUBF31                       15    OVERLAY(FIELD03:1)
     D  SUBF32                       10    OVERLAY(FIELD03:*NEXT)
     D  SUBF33                        5S 0 OVERLAY(FIELD03:*NEXT)
     D  SUBF34                        6S 2 OVERLAY(FIELD03:*NEXT)
      *
     D                 DS
     D FIELD04
     D  SUBF41                       15    OVERLAY(FIELD04:1)
     D  SUBF42                       10    OVERLAY(FIELD04:*NEXT)
     D  SUBF43                        5P 0 OVERLAY(FIELD04:*NEXT)
     D  SUBF44                        6P 2 OVERLAY(FIELD04:*NEXT)
      *
     D                 DS
     D AR02                          23    DIM(10)
     D  SB01                          3    OVERLAY(AR02:1)
     D  SSB01                         1    OVERLAY(SB01:1)
     D  SSB02                         1    OVERLAY(SB01:*NEXT)
     D  SSB03                         1    OVERLAY(SB01:*NEXT)
     D  SB02                         10    OVERLAY(AR02:*NEXT)
     D  SB03                         10    OVERLAY(AR02:*NEXT)
      *
     D QUSEC           DS
     D QUSBPRV                        4B 0
     D QUSBAVL                        4B 0
     D QUSEI                          7
     D QUSERVED                       1
     D QUSED01                      256
      *
     DN1               S              5S 0
     DS1               S             14
      * Riferimento £G40
     D £40FDS          DS                  INZ
     D  £40FDE                 1     30                                         Descrizione
     D  £40FRE                31     80                                         Tipo Record
     D  £40FNT                81     85  0                                      N° Oggetti Gestiti
     D  £40F_MSO              86     86                                         Gestione OG_SCA
     D  £40F_MWL              87     87                                         Gestione WHR_LI
     D  £40F_MWC              88     88                                         Gestione WHR_CO
     D  £40FLU                89     90                                         Livello Utente
     D  £40F_MSR              91     91                                         Gestione RE_SCA
     D  £40FNS                92     92                                         NO SQL
      *
     D  £40FTO              1096   7095    DIM(500)                             Oggetti Gestiti
      * Riferimento £C£E
     D**               DS                  STATIC
     D                 DS
     D LOG1                          14    INZ('0F0L1L2L3L4L5L')                COSTANTE
     D LOG                            2    OVERLAY(LOG1:1) DIM(7)
      * Riferimento programma CRPG
     D DSPARM          DS            14
     D DSBIN                          2B 0
     D DSZON                          2S 0
     D DSSTR                         10
      * Riferimento programma P5P5H0
     D                 DS
     D KEV                                 DIM(200)
     D KEVRES                        12    OVERLAY(KEV:01)
     D KEVTEV                         3    OVERLAY(KEV:*NEXT)
     D KEVSCT                         2    OVERLAY(KEV:*NEXT)
     D KEVCAT                         3    OVERLAY(KEV:*NEXT)
     D KEVORD                        15    OVERLAY(KEV:1)
      * Riferimento programma LOA12_SE
     D                 DS
     D MNU                          897    DIM(999)
     D MNUSSK                        30    OVERLAY(MNU:1)
     D MNUPOS                        10    OVERLAY(MNU:*NEXT)
     D MNUTRE                        20    OVERLAY(MNU:*NEXT)
     D MNUORD                        20    OVERLAY(MNU:*NEXT) INZ(*HIVAL)
     D MNUNAM                        20    OVERLAY(MNU:*NEXT)
     D MNUTXT                        50    OVERLAY(MNU:*NEXT)
     D MNUGRU                        10    OVERLAY(MNU:*NEXT)
     D MNUICO                        50    OVERLAY(MNU:*NEXT)
     D MNULEA                        10    OVERLAY(MNU:*NEXT)
     D MNUESE                         1N   OVERLAY(MNU:*NEXT)
     D MNUCON                         1    OVERLAY(MNU:*NEXT)
     D MNUCND                         1N   OVERLAY(MNU:*NEXT)
     D MNUUSR                         2    OVERLAY(MNU:*NEXT)
     D MNUFUN                       512    OVERLAY(MNU:*NEXT)
     D MNUWIN                         1    OVERLAY(MNU:*NEXT)
     D MNULIB                        10    OVERLAY(MNU:*NEXT)
     D MNUFIL                        10    OVERLAY(MNU:*NEXT)
     D MNUMEM                        10    OVERLAY(MNU:*NEXT)
     D MNUDER                         1    OVERLAY(MNU:*NEXT)
     D MNUCMN                        50    OVERLAY(MNU:*NEXT)
     D MNUSTA                         3    OVERLAY(MNU:*NEXT)
     D MNUPER                         1    OVERLAY(MNU:*NEXT)
     D MNUTIP                        12    OVERLAY(MNU:*NEXT)
     D MNUCOD                        15    OVERLAY(MNU:*NEXT)
     D MNUDYN                         1    OVERLAY(MNU:*NEXT)
     D MNUNAU                         1    OVERLAY(MNU:*NEXT)
     D MNUODO                        12    OVERLAY(MNU:*NEXT)
     D MNUCDO                        15    OVERLAY(MNU:*NEXT)
     D MNUMOD                        10    OVERLAY(MNU:*NEXT)
     D MNUCLI                         1    OVERLAY(MNU:*NEXT)
     D MNUWEB                         1    OVERLAY(MNU:*NEXT)
     D MNUPHO                         1    OVERLAY(MNU:*NEXT)
     D MNUTBL                         1    OVERLAY(MNU:*NEXT)
     D MNUMGP                         4    OVERLAY(MNU:*NEXT)
     D/COPY QILEGEN,£BR2DS
     DX2ME             S                   DIM(500) LIKE(£2ME)
      *
     D AR1_N           S              2S 0 DIM(10)
     D AR2_S           S              2    DIM(10)
     D DS1             DS
     D DS1_AR1                        2  0 DIM(10)
     D DS1_AR2                        2    DIM(10)
     D DS2             DS
     D DS2_AR1                 1     20  0 DIM(10)
     D DS2_AR2                21     40    DIM(10)
      * Riferimento a B£G75G - Lunghezza errato DS
     D                 DS
     D MBR                                 DIM(5000)
     D  $MBRMB                       10    OVERLAY(MBR)
     D  $MBRLI                       10    OVERLAY(MBR:11)
     D  $MBRDE                       50    OVERLAY(MBR:21)
     D  $MBRTP                       10    OVERLAY(MBR:71)
     D  $MBRDC                       10    OVERLAY(MBR:81)
     D  $MBRDM                       10    OVERLAY(MBR:91)
      *--------------------------------------------
      * Lunghezza primo campo DS
    MU* VAL1(N1) VAL2(67) COMP(EQ)
     C                   EVAL      N1=%LEN(AR01(01))
    MU* VAL1(N1) VAL2(25) COMP(EQ)
     C                   EVAL      N1=%LEN(FIELD01)
    MU* VAL1(N1) VAL2(36) COMP(EQ)
     C                   EVAL      N1=%LEN(FIELD02)
    MU* VAL1(N1) VAL2(36) COMP(EQ)
     C                   EVAL      N1=%LEN(FIELD03)
    MU* VAL1(N1) VAL2(32) COMP(EQ)
     C                   EVAL      N1=%LEN(FIELD04)
      * Overlay di overlay
    MU* VAL1(AR02(01)) VAL2('AAAAAAAAAAAAAAAAAAAAAAA') COMP(EQ)
     C                   EVAL      AR02(01)=*ALL'A'
    MU* VAL1(AR02(02)) VAL2('BBBBBBBBBBBBBBBBBBBBBBB') COMP(EQ)
     C                   EVAL      AR02(02)=*ALL'B'
    MU* VAL1(AR02(03)) VAL2('CCCCCCCCCCCCCCCCCCCCCCC') COMP(EQ)
     C                   EVAL      AR02(03)=*ALL'C'
      *
    MU* VAL1(SB01(01)) VAL2('123') COMP(EQ)
     C                   EVAL      SB01(01)='123'
    MU* VAL1(SSB01(01)) VAL2('1') COMP(EQ)
     C                   EVAL      SSB01(01)=SSB01(01)
    MU* VAL1(SSB02(01)) VAL2('2') COMP(EQ)
     C                   EVAL      SSB02(01)=SSB02(01)
    MU* VAL1(SSB03(01)) VAL2('3') COMP(EQ)
     C                   EVAL      SSB03(01)=SSB03(01)
    MU* VAL1(AR02(01)) VAL2('123AAAAAAAAAAAAAAAAAAAA') COMP(EQ)
     C                   EVAL      AR02(01)=AR02(01)
      *
    MU* VAL1(QUSBPRV) VAL2(0) COMP(EQ)
     C                   CLEAR                   QUSEC
      *
    MU* VAL1(N1) VAL2(20) COMP(EQ)
     C                   EVAL      N1=%LEN(KEV(01))
      *
     C***                CLEAR                   S1
     U**VAL1(DSBIN) VAL2(0) COMP(EQ)
     U**VAL1(DSZON) VAL2(0) COMP(EQ)
     U**VAL1(DSSTR) VAL2('  ') COMP(EQ)
     C***                CLEAR                   DSPARM
      *
     C***                Z-ADD     2             DSBIN
     C***                MOVE      DSBIN         S1
      *
     U**VAL1(DSBIN) VAL2(0) COMP(EQ)
     U**VAL1(DSZON) VAL2(0) COMP(EQ)
     U**VAL1(DSSTR) VAL2('  ') COMP(EQ)
     C****               EVAL      DSPARM=S1
     C****               EVAL      S1='0267ABCDEFGHIL'
     C****               EVAL      DSPARM=S1
      *
    MU* VAL1(MNUDYN(01)) VAL2(' ') COMP(EQ)
    MU* VAL1(MNUDYN(01)) VAL2(' ') COMP(EQ)
    MU* VAL1(MNUORD(01)) VAL2(*HIVAL) COMP(EQ)
     C                   CLEAR                   S1
    MU* VAL1(MNUORD(01)) VAL2(*BLANK) COMP(EQ)
     C                   CLEAR                   MNU
    MU* VAL1(MNUDYN(01)) VAL2(' ') COMP(EQ)
    MU* VAL1(MNUCND(01)) VAL2(' ') COMP(EQ)
    MU* VAL1(MNUORD(01)) VAL2(*HIVAL) COMP(EQ)
     C                   EVAL      MNUORD=*HIVAL
    MU* VAL1(MNUDYN(01)) VAL2(' ') COMP(EQ)
    MU* VAL1(MNUCND(01)) VAL2(' ') COMP(EQ)
    MU* VAL1(MNUORD(01)) VAL2(*BLANK) COMP(EQ)
     C                   CLEAR                   MNU
    MU* VAL1(MNUDYN(01)) VAL2(' ') COMP(EQ)
    MU* VAL1(MNUCND(01)) VAL2(' ') COMP(EQ)
     C                   RESET                   MNUORD
    MU* VAL1(N1) VAL2(1) COMP(EQ)
     C                   EVAL      N1=%LEN(X2ME(01))
      *
     C                   EVAL      AR1_N=05
     C                   EVAL      AR2_S='F '
     C                   MOVEA     AR1_N         DS1_AR1
     C                   Z-ADD     AR1_N         DS2_AR1
     C                   EVAL      DS1_AR2=AR2_S
    MU* VAL1(DS1_AR1(01)) VAL2(05) COMP(EQ)
    MU* VAL1(DS2_AR1(01)) VAL2(05) COMP(EQ)
    MU* VAL1(DS1_AR2(01)) VAL2('F ') COMP(EQ)
    MU* VAL1(DS2_AR2(01)) VAL2('F ') COMP(EQ)
     C                   MOVEA     AR2_S         DS2_AR2
    MU* VAL1(DS1_AR1(01)) VAL2(05) COMP(EQ)
    MU* VAL1(DS2_AR1(01)) VAL2(05) COMP(EQ)
    MU* VAL1(DS1_AR2(01)) VAL2('F ') COMP(EQ)
     C                   CLEAR                   DS2_AR2
    MU* VAL1(DS2_AR2(01)) VAL2('  ') COMP(EQ)
    MU* VAL1(DS2_AR2(02)) VAL2('  ') COMP(EQ)
    MU* VAL1(DS2_AR2(03)) VAL2('F ') COMP(EQ)
    MU* VAL1(DS2_AR2(04)) VAL2('F ') COMP(EQ)
     C                   MOVEA     AR2_S(2)      DS2_AR2(3)
      *
    MU* VAL1(N1) VAL2(100) COMP(EQ)
     C                   EVAL      N1=%LEN(MBR(01))
      *
    MU* VAL1(£40FTO(01)) VAL2('ARART') COMP(EQ)
     C                   EVAL      £40FTO(1)='ARART'
    MU* VAL1(£40FTO(02)) VAL2('CNCOL') COMP(EQ)
     C                   EVAL      £40FTO(2)='CNCOL'
    MU* VAL1(£40FTO(03)) VAL2('CNCLI') COMP(EQ)
     C                   EVAL      £40FTO(3)='CNCLI'
    MU* VAL1(£40FTO(04)) VAL2('CNFOR') COMP(EQ)
     C                   EVAL      £40FTO(4)='CNFOR'
    MU* VAL1(£40FTO(05)) VAL2('DOOVE') COMP(EQ)
     C                   EVAL      £40FTO(5)='DOOVE'
    MU* VAL1(£40FTO(06)) VAL2('E1   ') COMP(EQ)
     C                   EVAL      £40FTO(6)='E1'
      *
    MU* Type="NOXMI"
     C                   SETON                                        LR
