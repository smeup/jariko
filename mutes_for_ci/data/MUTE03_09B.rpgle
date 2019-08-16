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
      * applicazione
     D  APPNAME                      02    OVERLAY(SSFLD:1)
      * attributo colonna G (non usato)
     D  CFILNAME                     01    OVERLAY(SSFLD:*NEXT)
      * file
     D  FILNAME                      10    OVERLAY(SSFLD:*NEXT)
      * attributo colonna G '9' per campo dinamico
     D  CFLDNAME                     01    OVERLAY(SSFLD:*NEXT)
      * campo
     D  FLDNAME                      10    OVERLAY(SSFLD:*NEXT)
      * descrizione
     D  FLDDESC                      30    OVERLAY(SSFLD:*NEXT)
      * rettificato tramite B£EQRY_AO ; attributo colonna G '6' :
     D  COBJTYPE                     01    OVERLAY(SSFLD:*NEXT)
      * oggetto
     D  OBJTYPE                      30    OVERLAY(SSFLD:*NEXT)
     D  OBJTP                        02    OVERLAY(OBJTYPE:1)
      * descrizione oggetto
     D  OBJDESC                      40    OVERLAY(SSFLD:*NEXT)
      * segnalazione : attributo colonna G  '7' / '8' / '9'
     D  CSEGNALA                     01    OVERLAY(SSFLD:*NEXT)
      * segnalazione : decodifica 'Errato' / 'Dinamico' / *blanks
     D  SEGNALA                      08    OVERLAY(SSFLD:*NEXT)
      * puntatore campo dipendenza oggettizzazione dinamica
     D  PDINA01                      07  0 OVERLAY(SSFLD:*NEXT)
      * puntatore campo dipendenza oggettizzazione dinamica
     D  PDINA02                      07  0 OVERLAY(SSFLD:*NEXT)
      * puntatore campo dipendenza oggettizzazione dinamica
     D  PDINA03                      07  0 OVERLAY(SSFLD:*NEXT)
      * proprietà
     D  FLDPROP                      30    OVERLAY(SSFLD:*NEXT)
      * . campo chiave univoca (K1, K2 ...)
     D   FLDPKEY                     02    OVERLAY(FLDPROP:1)
      * . campo obbligatorio
     D   FLDPOBB                     01    OVERLAY(FLDPROP:*NEXT)
      * . controllare
     D   FLDPCHK                     01    OVERLAY(FLDPROP:*NEXT)
      * . livello autorizzazione
     D   FLDPAUT                     02    OVERLAY(FLDPROP:*NEXT)
      * . campo tecnico V2A£TF
     D   FLDPCTE                     02    OVERLAY(FLDPROP:*NEXT)
      * . parametro interno associato Oav
     D   FLDEPAR                     03    OVERLAY(FLDPROP:*NEXT)
      * esito autorizzazione
     D  FLDEAUT                      02    OVERLAY(SSFLD:*NEXT)
      * lunghezza
     D  FLDLUNG                      05    OVERLAY(SSFLD:*NEXT)
      * numero decimali
     D  FLDNDEC                      01    OVERLAY(SSFLD:*NEXT)
      * numero di sequenza
     D  FLDNSEQ                      03  0 OVERLAY(SSFLD:*NEXT)
      * posizione iniziale buffer
     D  FLDBINI                      05  0 OVERLAY(SSFLD:*NEXT)
      * posizione finale buffer
     D  FLDBFIN                      05  0 OVERLAY(SSFLD:*NEXT)
      * tipo dato campo (£IR1AC)
     D  FLDTDTC                      01    OVERLAY(SSFLD:*NEXT)
      * tipo campo (£IR1TC)
     D  FLDTIPC                      01    OVERLAY(SSFLD:*NEXT)
      * sezione
     D  FLDSSEZ                      15    OVERLAY(SSFLD:*NEXT)
      * Descrizione sezione
     D  FLDDSEZ                      35    OVERLAY(SSFLD:*NEXT)
      * SQL campo
     D  FLDSQLS                     256    OVERLAY(SSFLD:*NEXT)
      * Campo varying
     D  FLDVARY                       1    OVERLAY(SSFLD:*NEXT)
      *--------------------------------------------
     D                 DS
     D YFLD                         100    DIM(50)   CTDATA PERRCD(1)           _TXT_S81,20
     D  YFIL_FLD                     20    OVERLAY(YFLD:01)                     FILECAMPO
      *--------------------------------------------
     D                 DS
     D SFLD                         100    DIM(1500) DESCEND
     D  SFLD_FIL                     10    OVERLAY(SFLD:01)                     NOMEFILE
     D  SFLD_FLD                     10    OVERLAY(SFLD:11)                     NOME CAMPO
     D  SFLD_TOG                     30    OVERLAY(SFLD:21)                     TIPO OGGETTO
     D  SFLD_PRO                     30    OVERLAY(SFLD:51)                     PROPRIETà
     D  SFLD_DSC                     20    OVERLAY(SFLD:81)                     DESCRIZIONE
      *--------------------------------------------
      *
     C     *ENTRY        PLIST
     C                   PARM                    $SFLD
      * muovo la DS dalla schiera a tempo di compilazione in fondo a SFLD
     C                   EVAL      SFLD=YFLD
    MU* VAL1(SFLD_FIL(01)) VAL2('V5STAT0F') COMP(EQ)
    MU* VAL1(SFLD_FIL(02)) VAL2('REREFE0F') COMP(EQ)
    MU* VAL1(SFLD_FIL(03)) VAL2('C5TREG0F') COMP(EQ)
    MU* VAL1(SFLD_FIL(04)) VAL2('C5ICEE0F') COMP(EQ)
    MU* VAL1(SFLD_FIL(05)) VAL2('C5ICEE0F') COMP(EQ)
    MU* VAL1(SFLD_FIL(06)) VAL2('        ') COMP(EQ)
     C                   SORTA     SFLD
    MU* VAL1(FILNAME(01)) VAL2('V5STAT0F') COMP(EQ)
    MU* VAL1(FILNAME(02)) VAL2('REREFE0F') COMP(EQ)
    MU* VAL1(FILNAME(03)) VAL2('C5TREG0F') COMP(EQ)
    MU* VAL1(FILNAME(04)) VAL2('C5ICEE0F') COMP(EQ)
    MU* VAL1(FILNAME(05)) VAL2('C5ICEE0F') COMP(EQ)
    MU* VAL1(FILNAME(06)) VAL2('        ') COMP(EQ)
     C                   EVAL      FILNAME=SFLD_FIL
    MU* VAL1(FLDNAME(01)) VAL2('D6CONT  ') COMP(EQ)
    MU* VAL1(FLDNAME(02)) VAL2('R1COEN  ') COMP(EQ)
    MU* VAL1(FLDNAME(03)) VAL2('T5NU01  ') COMP(EQ)
    MU* VAL1(FLDNAME(04)) VAL2('I5MTRA  ') COMP(EQ)
    MU* VAL1(FLDNAME(05)) VAL2('I5COCO  ') COMP(EQ)
    MU* VAL1(FLDNAME(06)) VAL2('        ') COMP(EQ)
     C                   EVAL      FLDNAME=SFLD_FLD
    MU* VAL1(OBJTYPE(01)) VAL2('        ') COMP(EQ)
    MU* VAL1(OBJTYPE(02)) VAL2('CNNOM      COMP(EQ)
    MU* VAL1(OBJTYPE(03)) VAL2('           COMP(EQ)
    MU* VAL1(OBJTYPE(04)) VAL2('TAV§*IC    COMP(EQ)
    MU* VAL1(OBJTYPE(05)) VAL2('TAV§*IE    COMP(EQ)
    MU* VAL1(OBJTYPE(06)) VAL2('        ') COMP(EQ)
    MU* VAL1(OBJTP(01)) VAL2('  ') COMP(EQ)
    MU* VAL1(OBJTP(02)) VAL2('CN') COMP(EQ)
    MU* VAL1(OBJTP(03)) VAL2('  ') COMP(EQ)
    MU* VAL1(OBJTP(04)) VAL2('TA') COMP(EQ)
    MU* VAL1(OBJTP(05)) VAL2('TA') COMP(EQ)
    MU* VAL1(OBJTP(06)) VAL2('  ') COMP(EQ)
     C                   EVAL      OBJTYPE=SFLD_TOG
    MU* VAL1(FLDDESC(01)) VAL2('                              ') COMP(EQ)
    MU* VAL1(FLDDESC(02)) VAL2('Account                       ') COMP(EQ)
    MU* VAL1(FLDDESC(03)) VAL2('Importo XML FE                ') COMP(EQ)
    MU* VAL1(FLDDESC(04)) VAL2('                              ') COMP(EQ)
    MU* VAL1(FLDDESC(05)) VAL2('                              ') COMP(EQ)
    MU* VAL1(FLDDESC(06)) VAL2('                              ') COMP(EQ)
     C                   EVAL      FLDDESC=SFLD_DSC
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
     C                   EVAL      FLDPROP=SFLD_PRO
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
