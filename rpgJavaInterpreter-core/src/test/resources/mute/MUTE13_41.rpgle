     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 24/09/24  V6R1   BMA Creato
     V*=====================================================================
     FJALOGT0F  UF A E             DISK
     DJALOGT         E DS                  EXTNAME(JALOGT0F) INZ
     DSJALOG           S                   LIKE(JALOGT)
      *
      /COPY QILEGEN,£TABB£1DS
      /COPY QILEGEN,£PDS
      *---------------------------------------------------------------------
    RD* MAIN
      *---------------------------------------------------------------------
      *
     C                   EVAL      J1DTIN=20230930
     C                   EVAL      J1ORIN=113123
     C                   EVAL      J1DTUL=20230930
     C                   EVAL      J1ORUL=113233
     C                   EVAL      J1DTFI=20230930
     C                   EVAL      J1ORFI=113312
     C                   EVAL      SJALOG=JALOGT
     C                   EVAL      JALOGT=SJALOG
      *
     C     J1DTIN        DSPLY
     C     J1ORIN        DSPLY
     C     J1DTUL        DSPLY
     C     J1ORUL        DSPLY
     C     J1DTFI        DSPLY
     C     J1ORFI        DSPLY
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------------