     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 19/04/24  MUTEST  APU001 Creazione
     V*=====================================================================
    O *  OBIETTIVO
    O * L'obiettivo di questo test è l'utilizzo della LIKE di un field
    O *  di un'altra DS dichiarata successivamente. Inoltre, vi è
    O *  l'utilizzo di OVERLAY.
     V* ==============================================================
     D* Sezione delle variabili.
     D                 DS
     DAXDS                                 DIM(9999)
     D XAK                                 LIKE(£095R_CN) OVERLAY(AXDS:1)
     D XAD                                 LIKE(£095R_DP) INZ(0)
     D                                     OVERLAY(AXDS:*NEXT)

      * --------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_D
      /COPY QILEGEN,£TABB£1DS
      /COPY QILEGEN,£PDS
      /COPY QILEGEN,£D5_095DS
      *---------------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------------
     C                   EVAL      £DBG_Pgm = 'MU401013'
     C                   EVAL      £DBG_Sez = 'A40'
     C                   EVAL      £DBG_Fun = '*INZ'
     C                   EXSR      £DBG
     C                   EXSR      SEZ_A40
     C                   EXSR      £DBG
     C                   EVAL      £DBG_Fun = '*END'
     C                   EXSR      £DBG
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Test atomico di OVERLAY e LIKE, field di una successiva DS
      *---------------------------------------------------------------------
     C     SEZ_A40       BEGSR
    OA* A£.TPDA(LIKE)
     C                   EVAL      £DBG_Pas='P13'
      *
     C                   EVAL      £DBG_Str= 'HELLOTHERE'
      *
     C                   ENDSR
      *---------------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_C