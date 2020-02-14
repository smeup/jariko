     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 11/02/20  001529 BERNI Creazione
     V* 14/02/20  V5R1   BMA   Check-out 001529 in SMEDEV
     V*=====================================================================
     D FORMATADDRESS   PR            45
     D   CITY                        20A   CONST
     D   PROVINCE                    20A   CONST OPTIONS(*NOPASS)
      *---------------------------------------------------------------
     D RIS1            S             45
     D RIS2            S             45
     D DSP             S             52
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      *
     C                   EXSR      ELAB
      *
     C                   SETON                                        RT
      *--------------------------------------------------------------*
    RD* Routine
      *--------------------------------------------------------------*
     C     ELAB          BEGSR
      * First example with one parameter
    MU* VAL1(RIS1) VAL2('Milano, Lombardia') COMP(EQ)
     C                   EVAL      RIS1=FORMATADDRESS('Milano')                 COSTANTE
     C                   EVAL      DSP=RIS1
     C     DSP           DSPLY     £PDSNU
      * Second example with two parameters
    MU* VAL1(RIS2) VAL2('Napoli, Campania') COMP(EQ)
     C                   EVAL      RIS2=FORMATADDRESS('Napoli': 'Campania')     COSTANTE
     C                   EVAL      DSP=RIS2
     C     DSP           DSPLY     £PDSNU
      *
     C                   ENDSR
      *---------------------------------------------------------------*
    RD* Procedure
      *---------------------------------------------------------------*
      *
     P FORMATADDRESS   B
     D FORMATADDRESS   PI            45A
     D   CITY                        20A   CONST
     D   PROVPARM                    20A   CONST OPTIONS(*NOPASS)
     D PROVINCE        S             20A   INZ('Lombardia')                     COSTANTE
      *
      * Check number of parameters
1    C                   IF        %PARMS > 1
     C                   EVAL      PROVINCE = PROVPARM
1x   C                   ENDIF
      *
      * Compose result
     C                   RETURN    %TRIMR(CITY)+', '+PROVINCE
     P FORMATADDRESS   E
