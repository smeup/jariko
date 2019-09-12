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
     V* 13/08/19  001059  BMA Gestita schiera con overlay
     V* 19/08/19  V5R1    CM Check-out 001059 in SMEDEV
     V* 22/08/19  001071  BMA Rinominate variabili per maggior chiarezza
     V*=====================================================================
     V* OBIETTIVO
     V*  Programma finalizzato ai test slla definizione delle DS
     V*
     V*=====================================================================
      *--------------------------------------------
     D $SFLD           S                   DIM(%ELEM(SSFLD)) LIKE(SSFLD)
      *--------------------------------------------
     D                 DS                  INZ
     D SSFLD                        600    DIM(1000)
     D  APPNAME                      02    OVERLAY(SSFLD:1)
     D  CFILNAME                     01    OVERLAY(SSFLD:*NEXT)
     D  FILNAME                      10    OVERLAY(SSFLD:*NEXT)
     D  CFLDNAME                     01    OVERLAY(SSFLD:*NEXT)
     D  FLDNAME                      10    OVERLAY(SSFLD:*NEXT)
     D  FLDDESC                      30    OVERLAY(SSFLD:*NEXT)
     D  COBJTYPE                     01    OVERLAY(SSFLD:*NEXT)
     D  OBJTYPE                      30    OVERLAY(SSFLD:*NEXT)
     D  OBJTP                        02    OVERLAY(OBJTYPE:1)
     D  OBJDESC                      40    OVERLAY(SSFLD:*NEXT)
     D  CSEGNALA                     01    OVERLAY(SSFLD:*NEXT)
     D  SEGNALA                      08    OVERLAY(SSFLD:*NEXT)
     D  PDINA01                      07  0 OVERLAY(SSFLD:*NEXT)
     D  PDINA02                      07  0 OVERLAY(SSFLD:*NEXT)
     D  PDINA03                      07  0 OVERLAY(SSFLD:*NEXT)
     D  FLDPROP                      30    OVERLAY(SSFLD:*NEXT)
     D   FLDPKEY                     02    OVERLAY(FLDPROP:1)
     D   FLDPOBB                     01    OVERLAY(FLDPROP:*NEXT)
     D   FLDPCHK                     01    OVERLAY(FLDPROP:*NEXT)
     D   FLDPAUT                     02    OVERLAY(FLDPROP:*NEXT)
     D   FLDPCTE                     02    OVERLAY(FLDPROP:*NEXT)
     D   FLDEPAR                     03    OVERLAY(FLDPROP:*NEXT)
     D  FLDEAUT                      02    OVERLAY(SSFLD:*NEXT)
     D  FLDLUNG                      05    OVERLAY(SSFLD:*NEXT)
     D  FLDNDEC                      01    OVERLAY(SSFLD:*NEXT)
     D  FLDNSEQ                      03  0 OVERLAY(SSFLD:*NEXT)
     D  FLDBINI                      05  0 OVERLAY(SSFLD:*NEXT)
     D  FLDBFIN                      05  0 OVERLAY(SSFLD:*NEXT)
     D  FLDTDTC                      01    OVERLAY(SSFLD:*NEXT)
     D  FLDTIPC                      01    OVERLAY(SSFLD:*NEXT)
     D  FLDSSEZ                      15    OVERLAY(SSFLD:*NEXT)
     D  FLDDSEZ                      35    OVERLAY(SSFLD:*NEXT)
     D  FLDSQLS                     256    OVERLAY(SSFLD:*NEXT)
     D  FLDVARY                       1    OVERLAY(SSFLD:*NEXT)
      *--------------------------------------------
     D                 DS
     D YFLD                         100    DIM(50)   CTDATA PERRCD(1)           _TXT_S81,20
     D  YFIL_FLD                     20    OVERLAY(YFLD:01)                     FILECAMPO
      *--------------------------------------------
     D                 DS
     D TFLD                         100    DIM(1500) DESCEND
     D  TFLD_FIL                     10    OVERLAY(TFLD:01)                     NOMEFILE
     D  TFLD_FLD                     10    OVERLAY(TFLD:11)                     NOME CAMPO
     D  TFLD_TOG                     30    OVERLAY(TFLD:21)                     TIPO OGGETTO
     D  TFLD_PRO                     30    OVERLAY(TFLD:51)                     PROPRIETà
     D  TFLD_DSC                     20    OVERLAY(TFLD:81)                     DESCRIZIONE
      *--------------------------------------------
      *
      * SFLD non può stare nell'entry perché ha INZ sulla definizione
      * quindi definiamo una schiera LIKE da utilizzare nell'entry
     C     *ENTRY        PLIST
     C                   PARM                    $SFLD
      *
      * muovo la DS dalla schiera a tempo di compilazione in fondo a TFLD
    MU* VAL1(TFLD_FIL(01)) VAL2('C5ICEE0F') COMP(EQ)
    MU* VAL1(TFLD_FIL(02)) VAL2('C5ICEE0F') COMP(EQ)
    MU* VAL1(TFLD_FIL(03)) VAL2('C5TREG0F') COMP(EQ)
    MU* VAL1(TFLD_FIL(04)) VAL2('REREFE0F') COMP(EQ)
    MU* VAL1(TFLD_FIL(05)) VAL2('V5STAT0F') COMP(EQ)
    MU* VAL1(TFLD_FIL(1000)) VAL2('') COMP(EQ)
      * l'ordine degli elementi è lo stesso della schiera a tempo di compilazione in fondo (YFLD).
      * dato che YFLD ha meno elementi di TFLD, gli elementi in più di TFLD sono valorizzati a blank
     C                   EVAL      TFLD=YFLD
    MU* VAL1(TFLD_FIL(01)) VAL2('V5STAT0F') COMP(EQ)
    MU* VAL1(TFLD_FIL(02)) VAL2('REREFE0F') COMP(EQ)
    MU* VAL1(TFLD_FIL(03)) VAL2('C5TREG0F') COMP(EQ)
    MU* VAL1(TFLD_FIL(04)) VAL2('C5ICEE0F') COMP(EQ)
    MU* VAL1(TFLD_FIL(05)) VAL2('C5ICEE0F') COMP(EQ)
    MU* VAL1(TFLD_FIL(06)) VAL2('        ') COMP(EQ)
     C                   SORTA     TFLD
    MU* VAL1(FILNAME(01)) VAL2('V5STAT0F') COMP(EQ)
    MU* VAL1(FILNAME(02)) VAL2('REREFE0F') COMP(EQ)
    MU* VAL1(FILNAME(03)) VAL2('C5TREG0F') COMP(EQ)
    MU* VAL1(FILNAME(04)) VAL2('C5ICEE0F') COMP(EQ)
    MU* VAL1(FILNAME(05)) VAL2('C5ICEE0F') COMP(EQ)
    MU* VAL1(FILNAME(06)) VAL2('        ') COMP(EQ)
     C                   EVAL      FILNAME=TFLD_FIL
    MU* VAL1(FLDNAME(01)) VAL2('D6CONT  ') COMP(EQ)
    MU* VAL1(FLDNAME(02)) VAL2('R1COEN  ') COMP(EQ)
    MU* VAL1(FLDNAME(03)) VAL2('T5NU01  ') COMP(EQ)
    MU* VAL1(FLDNAME(04)) VAL2('I5MTRA  ') COMP(EQ)
    MU* VAL1(FLDNAME(05)) VAL2('I5COCO  ') COMP(EQ)
    MU* VAL1(FLDNAME(06)) VAL2('        ') COMP(EQ)
     C                   EVAL      FLDNAME=TFLD_FLD
    MU* VAL1(OBJTYPE(01)) VAL2('        ') COMP(EQ)
    MU* VAL1(OBJTYPE(02)) VAL2('CNNOM   ') COMP(EQ)
    MU* VAL1(OBJTYPE(03)) VAL2('        ') COMP(EQ)
    MU* VAL1(OBJTYPE(04)) VAL2('TAV§*IC ') COMP(EQ)
    MU* VAL1(OBJTYPE(05)) VAL2('TAV§*IE    COMP(EQ)
    MU* VAL1(OBJTYPE(06)) VAL2('        ') COMP(EQ)
    MU* VAL1(OBJTP(01)) VAL2('  ') COMP(EQ)
    MU* VAL1(OBJTP(02)) VAL2('CN') COMP(EQ)
    MU* VAL1(OBJTP(03)) VAL2('  ') COMP(EQ)
    MU* VAL1(OBJTP(04)) VAL2('TA') COMP(EQ)
    MU* VAL1(OBJTP(05)) VAL2('TA') COMP(EQ)
    MU* VAL1(OBJTP(06)) VAL2('  ') COMP(EQ)
     C                   EVAL      OBJTYPE=TFLD_TOG
    MU* VAL1(FLDDESC(01)) VAL2('                              ') COMP(EQ)
    MU* VAL1(FLDDESC(02)) VAL2('Account                       ') COMP(EQ)
    MU* VAL1(FLDDESC(03)) VAL2('Importo XML FE                ') COMP(EQ)
    MU* VAL1(FLDDESC(04)) VAL2('                              ') COMP(EQ)
    MU* VAL1(FLDDESC(05)) VAL2('                              ') COMP(EQ)
    MU* VAL1(FLDDESC(06)) VAL2('                              ') COMP(EQ)
     C                   EVAL      FLDDESC=TFLD_DSC
    MU* VAL1(FLDPKEY(01)) VAL2('  ') COMP(EQ)
    MU* VAL1(FLDPKEY(02)) VAL2('  ') COMP(EQ)
    MU* VAL1(FLDPKEY(03)) VAL2('  ') COMP(EQ)
    MU* VAL1(FLDPKEY(04)) VAL2('  ') COMP(EQ)
    MU* VAL1(FLDPKEY(05)) VAL2('  ') COMP(EQ)
    MU* VAL1(FLDPKEY(06)) VAL2('  ') COMP(EQ)
    MU* VAL1(FLDPAUT(01)) VAL2('51') COMP(EQ)
    MU* VAL1(FLDPAUT(02)) VAL2('  ') COMP(EQ)
    MU* VAL1(FLDPAUT(03)) VAL2('  ') COMP(EQ)
    MU* VAL1(FLDPAUT(04)) VAL2('  ') COMP(EQ)
    MU* VAL1(FLDPAUT(05)) VAL2('  ') COMP(EQ)
    MU* VAL1(FLDPAUT(06)) VAL2('  ') COMP(EQ)
     C                   EVAL      FLDPROP=TFLD_PRO
      *
     C                   EVAL      $SFLD=SSFLD
    MU* Type="NOXMI"
     C                   SETON                                        LR
**CTDATA YFLD
C5ICEE0F  I5COCO    TAV§*IE
C5ICEE0F  I5MTRA    TAV§*IC
C5TREG0F  T5NU01                                                                Importo XML FE
REREFE0F  R1COEN    CNNOM                                                       Account
V5STAT0F  D6CONT                                      51
