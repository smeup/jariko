     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 12/08/21  003119  BUSFIO Creazione
     V* 13/08/21  003119  BUSFIO Aggiunti nuove specifiche MUTE
     V* 13/08/21  V5R1    BMA    Check-out 003119 in SMEDEV
     V* 18/08/21  003121  BERNI  Ridenominato da MUTE10_77
     V*=====================================================================
     D*  OBIETTIVO
     D*  Programma finalizzato funzionamento del codice operativo SORTA
     V*=====================================================================
     D TXT             S             20    CTDATA PERRCD(1)  DIM(06)             _NOTXT
      * Struttura DS
     D                 DS
     D §TAB                          14    DIM(07) INZ
     D  §CODI                         2    OVERLAY(§TAB:1)                      Codice
     D  §FILE                         8    OVERLAY(§TAB:*NEXT)                  Nome file
     D  §TIME                         4    OVERLAY(§TAB:*NEXT)                  Tempo
      *
     D $X              S              2  0
      *
     D  $$COD          S              2
     D  $$FILENAME     S              8
     D  $$TIME         S              4
     I/COPY QILEGEN,£TABB£1DS
     I/COPY QILEGEN,£PDS
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      * Valorizzo DS
     C                   FOR       $X = 1 TO %ELEM(TXT)
     C                   EVAL      §TAB($X) = TXT($X)
     C                   ENDFOR
      * Riordino per CODI
     C                   SORTA     §CODI
      *
    MU* VAL1($$COD) VAL2('  ') COMP(EQ)
     C                   EVAL      $$COD=§CODI(01)
    MU* VAL1($$FILENAME) VAL2('        ') COMP(EQ)
     C                   EVAL      $$FILENAME=§FILE(01)
    MU* VAL1($$TIME) VAL2('    ') COMP(EQ)
     C                   EVAL      $$TIME=§TIME(01)
      *
    MU* VAL1($$COD) VAL2('A1') COMP(EQ)
     C                   EVAL      $$COD=§CODI(02)
    MU* VAL1($$FILENAME) VAL2('VERAPG0F') COMP(EQ)
     C                   EVAL      $$FILENAME=§FILE(02)
    MU* VAL1($$TIME) VAL2('9999') COMP(EQ)
     C                   EVAL      $$TIME=§TIME(02)
      *
    MU* VAL1($$COD) VAL2('01') COMP(EQ)
     C                   EVAL      $$COD=§CODI(03)
    MU* VAL1($$FILENAME) VAL2('VERAPG0F') COMP(EQ)
     C                   EVAL      $$FILENAME=§FILE(03)
    MU* VAL1($$TIME) VAL2('1473') COMP(EQ)
     C                   EVAL      $$TIME=§TIME(03)
      *
      * Riordino per FILE
     C                   SORTA     §FILE
      *
    MU* VAL1($$COD) VAL2('  ') COMP(EQ)
     C                   EVAL      $$COD=§CODI(01)
    MU* VAL1($$FILENAME) VAL2('        ') COMP(EQ)
     C                   EVAL      $$FILENAME=§FILE(01)
    MU* VAL1($$TIME) VAL2('    ') COMP(EQ)
     C                   EVAL      $$TIME=§TIME(01)
      *
    MU* VAL1($$COD) VAL2('02') COMP(EQ)
     C                   EVAL      $$COD=§CODI(02)
    MU* VAL1($$FILENAME) VAL2('BRARTI0F') COMP(EQ)
     C                   EVAL      $$FILENAME=§FILE(02)
    MU* VAL1($$TIME) VAL2(' 974') COMP(EQ)
     C                   EVAL      $$TIME=§TIME(02)
      *
    MU* VAL1($$COD) VAL2('A1') COMP(EQ)
     C                   EVAL      $$COD=§CODI(05)
    MU* VAL1($$FILENAME) VAL2('VERAPG0F') COMP(EQ)
     C                   EVAL      $$FILENAME=§FILE(05)
    MU* VAL1($$TIME) VAL2('9999') COMP(EQ)
     C                   EVAL      $$TIME=§TIME(05)
      *
      * Riordino per TIME
     C                   SORTA     §TIME
      *
    MU* VAL1($$COD) VAL2('  ') COMP(EQ)
     C                   EVAL      $$COD=§CODI(01)
    MU* VAL1($$FILENAME) VAL2('        ') COMP(EQ)
     C                   EVAL      $$FILENAME=§FILE(01)
    MU* VAL1($$TIME) VAL2('    ') COMP(EQ)
     C                   EVAL      $$TIME=§TIME(01)
      *
    MU* VAL1($$COD) VAL2('04') COMP(EQ)
     C                   EVAL      $$COD=§CODI(02)
    MU* VAL1($$FILENAME) VAL2('BRARTI0F') COMP(EQ)
     C                   EVAL      $$FILENAME=§FILE(02)
    MU* VAL1($$TIME) VAL2(' 848') COMP(EQ)
     C                   EVAL      $$TIME=§TIME(02)
      *
    MU* VAL1($$COD) VAL2('A1') COMP(EQ)
     C                   EVAL      $$COD=§CODI(07)
    MU* VAL1($$FILENAME) VAL2('VERAPG0F') COMP(EQ)
     C                   EVAL      $$FILENAME=§FILE(07)
    MU* VAL1($$TIME) VAL2('9999') COMP(EQ)
     C                   EVAL      $$TIME=§TIME(07)
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
** TXT
05VERAPG0F1158
02BRARTI0F 974
03BRARTI0F1569
01VERAPG0F1473
04BRARTI0F 848
A1VERAPG0F9999