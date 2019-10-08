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
