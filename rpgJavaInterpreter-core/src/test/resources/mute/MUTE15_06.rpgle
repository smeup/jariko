     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 17/02/20  001574 BERNI Creazione
     V* 18/02/20  001574 BERNI Modifiche
     V* 19/02/20  V5R1   BMA   Check-out 001574 in SMEDEV
     V*=====================================================================
     DP_EseStr         PR            30
     D  XSTR                         30         CONST
     D DSP             S             52
     D RISP            S             52
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      *
     C                   EXSR      ELAB
      *
     C                   SETON                                        RT
      *---------------------------------------------------------------
     D* Elaborazione
      *---------------------------------------------------------------
     C     ELAB          BEGSR
      *
      * When the parameter is EXECUTION the procedure return EVIDENCE1 as result
    MU* VAL1(RISP) VAL2('EVIDENCE1') COMP(EQ)
     C                   EVAL      RISP= P_EseStr('EXECUTION')
     C                   EVAL      DSP=RISP
     C     DSP           DSPLY     £PDSNU
      *
      * When the parameter is LAUNCH the procedure return EVIDENCE123 as result
    MU* VAL1(RISP) VAL2('EVIDENCE123') COMP(EQ)
     C                   EVAL      RISP= P_EseStr('LAUNCH')
     C                   EVAL      DSP=RISP
     C     DSP           DSPLY     £PDSNU
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    PD* Procedure with subroutine
      *--------------------------------------------------------------*
     PP_EseStr         B
     D P_EseStr        Pi            30
     D  XSTR                         30    CONST
      *
     D XXSTR           S             30
     D XSTRL           S             15  5
      * execute a subroutine
     C                   EXSR      RT_SUB
      * return the result
     C                   RETURN    XXSTR
      *---------------------------------------------------------------
     D* SUBROUTINE
      *---------------------------------------------------------------
     C     RT_SUB        BEGSR
      *
      * If the procedure receive 'EXECUTION' then return 'EVIDENCE1' and the length
     C                   IF        XSTR='EXECUTION'
     C                   EVAL      XXSTR='EVIDENCE1'
     C                   EVAL      XSTRL=%LEN(XXSTR)
     C                   ELSE
      * .. else return 'EVIDENCE123' and the length
     C                   EVAL      XXSTR='EVIDENCE123'
     C                   EVAL      XSTRL=%LEN(XXSTR)
     C                   ENDIF
      *
     C                   ENDSR
      *---------------------------------------------------------------
     P                 E
