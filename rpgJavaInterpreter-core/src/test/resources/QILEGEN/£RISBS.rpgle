      *====================================================================
      * smeup V6R1.023DV
      * Nome sorgente       : £RISBS
      * Sorgente di origine : SMEUP_DEV/QILEGEN(£RISBS)
      * Esportato il        : 20240613 143719
      *====================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 13/03/01  04.00   GG Sostituito £RITST con £RISA5
     V* 22/10/04  V2R1    PV Aggiunto livello di chiamata
     D*----------------------------------------------------------------
     D* OBIETTIVO
     D*  Eseguire la ricerca alfabetica e/o il controllo validità/deco-
     D*  difica di subsettori tabelle SMEUP
     D*
     D* FLUSSO
     D* Input : £COSET : Codice settore
     D*         £COSBS : Codice subsettore
     D* Output: £DESBS : Descrizione subsettore
     D*         *IN35  : Se ON subsettore errato
     D*         *IN36  : Se ON eseguita ricerca
     D*
     D* ESEMPIO DI CHIAMATA
     D*C                     MOVEL<settore> £COSET
     D*C                     MOVEL<subsett.>£COSBS
     D*C                     EXSR £RISBS
     D*C                     MOVEL£DESBS    <campo descrizione>
     D*C   35   N60          MOVE 'BAS0001' £MSGCO
     D*C   35                SETON                     60<ind.err.>
2    D*C  N35 36             SETON                     10
     D*C  N35 36             MOVEL£COSBS    <campo subsettore>
     D*----------------------------------------------------------------
     C     £RISBS        BEGSR
     C     'A'           IFEQ      'B'
     C                   MOVEL     £COSET        £COSET            3
     C                   MOVE      £COSBS        £COSBS            2
     C                   MOVE      £RSSLC        £RSSLC            1
     C                   ENDIF
     C                   MOVEL     £COSET        £RISA5            5
     C                   MOVE      £COSBS        £RISA5
     C     'B£AR80'      CAT(P)    £RSSLC:0      £RSSPG           10
     C                   CALL      £RSSPG
     C                   PARM                    £RISA5
     C                   PARM                    £DESBS           30
     C                   PARM                    £IN35             1
     C                   PARM                    £IN36             1
     C     £IN35         COMP      '1'                                    35
     C     £IN36         COMP      '1'                                    36
     C                   MOVEL     £RISA5        £COSET
     C                   MOVE      £RISA5        £COSBS
     C                   ENDSR
