   COP* *NOUI
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 13/08/19  001059  BMA Created
     V* 19/08/19  V5R1    CM Check-out 001059 in SMEDEV
     V* 22/08/19  001071  BMA Aggiunti commenti e riviste annotazioni
     V*=====================================================================
     V* OBIETTIVO
     V*  Programma finalizzato ai test slla definizione delle DS
     V*
     V*=====================================================================
     D                 DS                  INZ
     D SSFLD                        600
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
    MU* VAL1(APPNAME)  VAL2('AP') COMP(EQ)
     C                   EVAL APPNAME =  'AP'
    MU* VAL1(CFILNAME) VAL2('X') COMP(EQ)
     C                   EVAL CFILNAME =  'XX'
    MU* VAL1(FILNAME)  VAL2('FILNAME') COMP(EQ)
     C                   EVAL FILNAME =  'FILNAME'
    MU* VAL1(CFLDNAME) VAL2('#') COMP(EQ)
     C                   EVAL      CFLDNAME='#'
    MU* VAL1(FLDNAME)  VAL2('FLDNAME') COMP(EQ)
     C                   EVAL      FLDNAME='FLDNAME'
    MU* VAL1(FLDDESC)  VAL2('FLDDESC') COMP(EQ)
     C                   EVAL      FLDDESC='FLDDESC'
    MU* VAL1(COBJTYPE) VAL2('#') COMP(EQ)
     C                   EVAL      COBJTYPE='#'
    MU* VAL1(OBJTYPE) VAL2('OBJTYPE') COMP(EQ)
      * TODO get an actual OVERLAY
    MU* VAL1(OBJTP) VAL2('OB') COMP(EQ)
     C                   EVAL      OBJTYPE='OBJTYPE'
      * TODO set an actual OVERLAY
    MU* VAL1(OBJTP) VAL2('BO') COMP(EQ)
    MU* VAL1(OBJTYPE) VAL2('BOJTYPE') COMP(EQ)
     C                   EVAL      OBJTP='BO'
    MU* VAL1(OBJDESC) VAL2('OBJDESC') COMP(EQ)
     C                   EVAL      OBJDESC='OBJDESC'
    MU* VAL1(CSEGNALA) VAL2('X') COMP(EQ)
     C                   EVAL      CSEGNALA='X'
    MU* VAL1(SEGNALA) VAL2('SEGNALA') COMP(EQ)
     C                   EVAL      SEGNALA='SEGNALA'
      * TODO packed type
    MU* VAL1(PDINA01) VAL2(1234567) COMP(EQ)
     C                   EVAL      PDINA01=1234567
    MU* VAL1(PDINA02) VAL2(1234555) COMP(EQ)
     C                   EVAL      PDINA02=1234555
    MU* VAL1(PDINA03) VAL2(-1234567) COMP(EQ)
     C                   EVAL      PDINA03=-1234567
      * TODO set an actual OVERLAY
    MU* VAL1(FLDPROP) VAL2('AABCDDEEFFF') COMP(EQ)
    MU* VAL1(FLDPKEY) VAL2('AA') COMP(EQ)
    MU* VAL1(FLDPOBB) VAL2('B') COMP(EQ)
    MU* VAL1(FLDPCHK) VAL2('C') COMP(EQ)
    MU* VAL1(FLDPAUT) VAL2('DD') COMP(EQ)
    MU* VAL1(FLDPCTE) VAL2('EE') COMP(EQ)
    MU* VAL1(FLDEPAR) VAL2('FFF') COMP(EQ)
     C                   EVAL      FLDPROP='AABCDDEEFFF'
     *
    MU* VAL1(FLDPROP) VAL2('FFBCDDEEFFF') COMP(EQ)
    MU* VAL1(FLDPKEY) VAL2('FF') COMP(EQ)
     C                   EVAL      FLDPKEY='FF'
     *
    MU* VAL1(FLDPROP) VAL2('FFECDDEEFFF') COMP(EQ)
    MU* VAL1(FLDPOBB) VAL2('E') COMP(EQ)
     C                   EVAL      FLDPOBB='E'
    MU* VAL1(FLDPROP) VAL2('FFEDDDEEFFF') COMP(EQ)
    MU* VAL1(FLDPCHK) VAL2('D') COMP(EQ)
     C                   EVAL      FLDPCHK='D'
     *
    MU* VAL1(FLDPROP) VAL2('FFEDCCEEFFF') COMP(EQ)
    MU* VAL1(FLDPAUT) VAL2('CC') COMP(EQ)
     C                   EVAL      FLDPAUT='CC'
     *
    MU* VAL1(FLDPROP) VAL2('FFEDCCBBFFF') COMP(EQ)
    MU* VAL1(FLDPCTE) VAL2('BB') COMP(EQ)
     C                   EVAL      FLDPCTE='BB'
     *
    MU* VAL1(FLDPROP) VAL2('FFEDCCBBAAA') COMP(EQ)
    MU* VAL1(FLDEPAR) VAL2('AAA') COMP(EQ)
     C                   EVAL      FLDEPAR='AAA'

     *
     C                   DSPLY                   SSFLD
    MU* Type="NOXMI"
     C                   SETON                                        LR
