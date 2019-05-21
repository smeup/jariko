     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 21/05/19  V5R1   BMA Created
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
     C                   EVAL      $X=0
     C                   FOR       $I=1 TO 20000000
     C                   EVAL      $X=$X+$I
     C                   ENDFOR
      * Display result
     C                   EVAL      $MSG='Result: '+%EDITC($X:'K')               COSTANTE
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
