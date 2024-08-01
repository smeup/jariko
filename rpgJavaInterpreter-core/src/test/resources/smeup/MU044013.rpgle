     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 22/03/24  MUTEST  APU001 Creazione
     V*=====================================================================
    O *  OBIETTIVO
    O * Trasferimento del contenuto di una DS in una Stringa.
     V* ==============================================================
     D A40_A100        S            100    DIM(300)
     D A40_DS          DS
     D  A40_DS_F1                    10
     D  A40_DS_F2                    29
     D  A40_DS_F3                    21
     D A40_A5          S              5  0
      * --------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_D
      /COPY QILEGEN,£TABB£1DS
      /COPY QILEGEN,£PDS
      *---------------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------------
     C                   EVAL      £DBG_Pgm = 'MU044013'
     C                   EVAL      £DBG_Sez = 'A40'
     C                   EVAL      £DBG_Fun = '*INZ'
     C                   EXSR      £DBG
     C                   EXSR      SEZ_A40
     C                   EXSR      £DBG
     C                   EVAL      £DBG_Fun = '*END'
     C                   EXSR      £DBG
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* DS a Stringa
      *---------------------------------------------------------------------
     C     SEZ_A40       BEGSR
    OA* A£.CDOP(EVAL  )
     D* DS a Stringa
     C                   EVAL      £DBG_Pas='P13'
      *
     C                   EVAL      A40_DS_F1='Lorem ipsu'
     C                   EVAL      A40_DS_F2='m dolor sit amet, consectetue'
     C                   EVAL      A40_DS_F3='r adipiscing elit. Ae'
     C                   EVAL      A40_A5=1
     C                   EVAL      A40_A100(A40_A5)=A40_DS
     C                   EVAL      £DBG_Str=A40_A100(A40_A5)
      *
     C                   ENDSR
      *---------------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_C