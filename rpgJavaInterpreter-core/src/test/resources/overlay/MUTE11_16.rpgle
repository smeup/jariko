     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 14/08/19  001059 BMA Creazione
     V* 19/08/19  V5R1    CM Check-out 001059 in SMEDEV
     V* 22/08/19  001071 BMA Migliorati commenti esplicativi
     V* ==============================================================
      *
      * Questo programma serve a verificare il corretto passaggio di parametri numerici decimali
      * tra un programma chiamante e uno chiamato
      *
      ****************************************************************
      *
     D DSP             S             50
      *----------------------------------------------------------------
    MU* VAL1(NUM001) VAL2(45.62) COMP(EQ)
     C                   EVAL      NUM001=45.62
    MU* VAL1(NUM002) VAL2(3.11) COMP(EQ)
     C                   EVAL      NUM002=3.11
    MU* VAL1(NUM003) VAL2(0) COMP(EQ)
     C                   EVAL      NUM003=0
      *
     C                   CALL      'MUTE11_16A'
     C                   PARM                    NUM001           30 9
     C                   PARM                    NUM002           30 9
     C                   PARM                    NUM003           30 9
      *
    MU* VAL1(NUM003) VAL2(14.668810289) COMP(EQ)
     C                   EVAL      DSP=%CHAR(NUM003)
     C                   DSPLY                   DSP
      *
     C                   SETON                                        LR
     C*-------------------------------------------------------------------
     C*
     C*
