      *====================================================================
      * smeup V6R1.021DV
      * Nome sorgente       : MU025014
      * Sorgente di origine : QTEMP/SRC(MU025014)
      * Esportato il        : 20240409 121030
      *====================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 08/04/24  MUTEST  BERNI Creazione
     V*=====================================================================
    O *  OBIETTIVO
    O * DEFINE di varibili definite INLINE in/COPY non di definizione (es: £G40)
     V* ==============================================================
      /COPY QILEGEN,MULANG_D_D
      /COPY QILEGEN,£TABB£1DS
      /COPY QILEGEN,£G40E
      /COPY QILEGEN,£PDS
      *---------------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------------
     C                   EVAL      £DBG_Pgm = 'MU025014'
     C                   EVAL      £DBG_Sez = 'A50'
     C                   EVAL      £DBG_Fun = '*INZ'
     C                   EXSR      £DBG
     C                   EXSR      SEZ_A50
     C                   EXSR      £DBG
     C                   EVAL      £DBG_Fun = '*END'
     C                   EXSR      £DBG
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Test atomico DEFINE
      *---------------------------------------------------------------------
     C     SEZ_A50       BEGSR
    OA* A£.CDOP(DEFINE  )
     D* DEFINE variabili definite INLINE in /COPY
     C                   EVAL      £DBG_Pas='P14'
     C     *LIKE         DEFINE    £G40LC        A50_A14
     C     *LIKE         DEFINE    £G40PG        A50_B14
     C                   EVAL      A50_A14='A'
     C                   EVAL      A50_B14='ABCDEFGHIJ'
     C                   EVAL      £DBG_Str= 'A50_A14('+A50_A14+')'
     C                                     +' A50_B14('+A50_B14+')'
     C                   ENDSR
      *---------------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_C
      /COPY QILEGEN,£G40
