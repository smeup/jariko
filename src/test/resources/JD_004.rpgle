     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 07/05/19  V5R1   BMA Created
     V*=====================================================================
     H/COPY QILEGEN,£INIZH
      *---------------------------------------------------------------
     I/COPY QILEGEN,£TABB£1DS
     I/COPY QILEGEN,£PDS
      *---------------------------------------------------------------
      * Loop index
     D $I              S             10I 0
      * Numeric result
     D $X              S             20I 0
      * Message
     D $MSG            S             50
      *---------------------------------------------------------------
      * ENTRY JD_006
      * . Function
     D U$FUNZ          S             10
      * . Method
     D U$METO          S             10
      * Duration
     D U$TDUR          S             20I 0
      * . Return Code ('1'=ERROR / blank=OK)
     D U$IN35          S              1
      *---------------------------------------------------------------
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      *
      * Initial settings
     C                   EXSR      IMP0
      * Elaboration
     C                   EXSR      FESE
      * Final settings
     C                   EXSR      FIN0
      * End
     C                   SETON                                        LR
      *---------------------------------------------------------------
     C/COPY QILEGEN,£INZSR
      *--------------------------------------------------------------*
    RD* Elaboration
      *--------------------------------------------------------------*
     C     FESE          BEGSR
      * take initial time
     C                   EVAL      U$FUNZ='INI'
     C                   EXSR      JD006
     C                   EVAL      $X=0
     C                   FOR       $I=1 TO 20000000
     C                   EVAL      $X=$X+$I
     C                   ENDFOR
      * take final time
     C                   EVAL      U$FUNZ='END'
     C                   EXSR      JD006
      * Calculate duration in millisenconds
     C                   EVAL      U$FUNZ='CAL'
     C                   EXSR      JD006
      * Display result
     C                   EVAL      $MSG='Result: '+%EDITC($X:'K')               COSTANTE
     C     $MSG          DSPLY
      * Display duration in millisenconds
     C                   EVAL      $MSG='Milliseconds: '+%EDITC(U$TDUR:'K')     COSTANTE
     C     $MSG          DSPLY
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Initial subroutine (as *INZSR)
      *--------------------------------------------------------------*
     C     £INIZI        BEGSR
      * Every Sme.UP program encapsulates *INZSR in a /COPY.
      * So we provide £INIZI subroutine to do the same job
      *
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Initial settings
      *--------------------------------------------------------------*
     C     IMP0          BEGSR
      *
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Final settings
      *--------------------------------------------------------------*
     C     FIN0          BEGSR
      *
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Call JD_006 for time calculation
      *--------------------------------------------------------------*
     C     JD006         BEGSR
     C                   CALL      'JD_006'
     C                   PARM                    U$FUNZ
     C                   PARM                    U$METO
     C                   PARM                    U$TDUR
     C                   PARM                    U$IN35
     C                   ENDSR
