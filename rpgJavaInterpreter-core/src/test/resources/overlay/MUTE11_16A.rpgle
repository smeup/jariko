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
     D NUM001          S             30 9
     D NUM002          S             30 9
     D NUM003          S             30 9
     D DSP             S             50

      ****************************************************************
      *
     C     *ENTRY        PLIST
     C                   PARM                    NUM001           30 9
     C                   PARM                    NUM002           30 9
     C                   PARM                    NUM003           30 9
      * con l'estender (H) il risultato viene arrotondato al giusto se necessario, anziché troncato
     C                   EVAL(H)   NUM003=NUM001/NUM002
      *
     C                   SETON                                        LR
