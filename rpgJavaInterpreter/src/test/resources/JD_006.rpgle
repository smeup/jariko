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
      * ENTRY
      * . Function
     D U$FUNZ          S             10
      * . Method
     D U$METO          S             10
      * Duration
     D U$TDUR          S             20I 0
      * . Return Code ('1'=ERROR / blank=OK)
     D U$IN35          S              1
      *---------------------------------------------------------------
      * Initial timestamp
     D $TIMEINI        S               Z
      * Final timestamp
     D $TIMEEND        S               Z
      * Duration
     D $TIMEDUR        S             20I 0
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      *
      * Initial settings
     C                   EXSR      IMP0
      * Function / Method
1    C                   SELECT
      * Init
1x   C                   WHEN      U$FUNZ='INI'
     C                   EXSR      FINI
      * Final time
1x   C                   WHEN      U$FUNZ='END'
     C                   EXSR      FEND
      * Calculate duration
1x   C                   WHEN      U$FUNZ='CAL'
     C                   EXSR      FCAL
     C                   ENDSL
      * Final settings
     C                   EXSR      FIN0
      * End
     C                   SETON                                        RT
      *---------------------------------------------------------------
     C/COPY QILEGEN,£INZSR
      *--------------------------------------------------------------*
    RD* Take initial time
      *--------------------------------------------------------------*
     C     FINI          BEGSR
      * take initial time
     C                   TIME                    $TIMEINI
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Take final time
      *--------------------------------------------------------------*
     C     FEND          BEGSR
      * take final time
     C                   TIME                    $TIMEEND
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Calculate duration
      *--------------------------------------------------------------*
     C     FCAL          BEGSR
      * Calculate duration in microsenconds
     C     $TIMEEND      SUBDUR    $TIMEINI      $TIMEDUR:*MS
      * Calculate duration in millisenconds
     C                   EVAL      U$TDUR=$TIMEDUR/1000
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Initial subroutine (as *INZSR)
      *--------------------------------------------------------------*
     C     £INIZI        BEGSR
      * Every Sme.UP program encapsulates *INZSR in a /COPY.
      * So we provide £INIZI subroutine to do the same job
      *
     C     *ENTRY        PLIST
     C                   PARM                    U$FUNZ
     C                   PARM                    U$METO
     C                   PARM                    U$TDUR
     C                   PARM                    U$IN35
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Initial settings
      *--------------------------------------------------------------*
     C     IMP0          BEGSR
      *
     C                   CLEAR                   U$TDUR
     C                   CLEAR                   U$IN35
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
