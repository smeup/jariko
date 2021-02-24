     V*===============================================================
     D* Purpose of this source is testing if we are able to execute
     D* an API program.
     D* Original source: MUMU1G
     V*===============================================================
     D*
     D* OBIETTIVO
     D* Programma per API di esmepio £MU1
     D*
     D*-------------------------------------------------------------------
      /COPY £MU1DSPEC
      *
     C     *ENTRY        PLIST
     C                   PARM                    £MU1DSI
     C                   PARM                    £MU1DSO
     C     £MU1I_FU      DSPLY
      *
     C                   EXSR      IMP0
      * Scelta funzione
1    C                   SELECT
1x   C                   WHEN      £MU1I_FU='001'
     C                   EXSR      F001
1x   C                   WHEN      £MU1I_FU='002'
     C                   EXSR      F002
1e   C                   ENDSL
      *
     C                   EXSR      FIN0
      *
     C                   SETON                                        LR
      *--------------------------------------------------------------*
    RD* Funzione 001
      *--------------------------------------------------------------*
     C     F001          BEGSR
      *
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Funzione 002
      *--------------------------------------------------------------*
     C     F002          BEGSR
      *
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Impostazioni iniziali
      *--------------------------------------------------------------*
     C     IMP0          BEGSR
      *
     C                   CLEAR                   £MU1DSO
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Impostazioni finali
      *--------------------------------------------------------------*
     C     FIN0          BEGSR
      *
     C                   CLEAR                   £MU1DSI
      *
     C                   ENDSR
      *--------------------------------------------------------------*
