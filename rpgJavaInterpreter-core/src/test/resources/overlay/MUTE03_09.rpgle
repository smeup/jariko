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
      * lancio il pgm MUTE03_09B e verifico che gli elementi dell'array restituiti contengano
      * i valori attesi
    MU* VAL1(FILNAME(01)) VAL2('V5STAT0F') COMP(EQ)
    MU* VAL1(FILNAME(02)) VAL2('REREFE0F') COMP(EQ)
    MU* VAL1(FILNAME(03)) VAL2('C5TREG0F') COMP(EQ)
    MU* VAL1(FILNAME(04)) VAL2('C5ICEE0F') COMP(EQ)
    MU* VAL1(FILNAME(05)) VAL2('C5ICEE0F') COMP(EQ)
    MU* VAL1(FILNAME(06)) VAL2('        ') COMP(EQ)
    MU* VAL1(FLDNAME(01)) VAL2('D6CONT  ') COMP(EQ)
    MU* VAL1(FLDNAME(02)) VAL2('R1COEN  ') COMP(EQ)
    MU* VAL1(FLDNAME(03)) VAL2('T5NU01  ') COMP(EQ)
    MU* VAL1(FLDNAME(04)) VAL2('I5MTRA  ') COMP(EQ)
    MU* VAL1(FLDNAME(05)) VAL2('I5COCO  ') COMP(EQ)
    MU* VAL1(FLDNAME(06)) VAL2('        ') COMP(EQ)
    MU* VAL1(OBJTYPE(01)) VAL2('        ') COMP(EQ)
    MU* VAL1(OBJTYPE(02)) VAL2('CNNOM   ') COMP(EQ)
    MU* VAL1(OBJTYPE(03)) VAL2('        ') COMP(EQ)
    MU* VAL1(OBJTYPE(04)) VAL2('TAV§*IC ') COMP(EQ)
    MU* VAL1(OBJTYPE(05)) VAL2('TAV§*IE ') COMP(EQ)
    MU* VAL1(OBJTYPE(06)) VAL2('        ') COMP(EQ)
    MU* VAL1(OBJTP(01)) VAL2('  ') COMP(EQ)
    MU* VAL1(OBJTP(02)) VAL2('CN') COMP(EQ)
    MU* VAL1(OBJTP(03)) VAL2('  ') COMP(EQ)
    MU* VAL1(OBJTP(04)) VAL2('TA') COMP(EQ)
    MU* VAL1(OBJTP(05)) VAL2('TA') COMP(EQ)
    MU* VAL1(OBJTP(06)) VAL2('  ') COMP(EQ)
    MU* VAL1(FLDDESC(01)) VAL2('                              ') COMP(EQ)
    MU* VAL1(FLDDESC(02)) VAL2('Account                       ') COMP(EQ)
    MU* VAL1(FLDDESC(03)) VAL2('Importo XML FE                ') COMP(EQ)
    MU* VAL1(FLDDESC(04)) VAL2('                              ') COMP(EQ)
    MU* VAL1(FLDDESC(05)) VAL2('                              ') COMP(EQ)
    MU* VAL1(FLDDESC(06)) VAL2('                              ') COMP(EQ)
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
     C                   CALL      'MUTE03_09B'
     C                   PARM                    SSFLD
    MU* Type="NOXMI"
     C                   SETON                                        LR
