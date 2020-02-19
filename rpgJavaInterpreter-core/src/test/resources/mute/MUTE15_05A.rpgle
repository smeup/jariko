     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 17/02/20  001574 BERNI Creazione
     V* 18/02/20  001574 BERNI Modifiche
     V* 19/02/20  V5R1   BMA   Check-out 001574 in SMEDEV
     V*=====================================================================
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      *
      * Entry Parameters
     C     *ENTRY        PLIST
     C                   PARM                    XXSTR            30
     C                   PARM                    XSTRL            15 5
      *
      * If the procedure receive 'EXECUTION' then return 'EVIDENCE1' and the length
     C                   IF        XXSTR='EXECUTION'
     C                   EVAL      XXSTR='EVIDENCE1'
     C                   EVAL      XSTRL=%LEN(XXSTR)
     C                   ELSE
      * .. else return 'EVIDENCE123' and the length
     C                   EVAL      XXSTR='EVIDENCE123'
     C                   EVAL      XSTRL=%LEN(XXSTR)
     C                   ENDIF
      *
     C                   SETON                                        RT
