      *====================================================================
      * smeup V6R1.021DV
      * Nome sorgente       : MU041004
      * Sorgente di origine : QTEMP/SRC(MU041004)
      * Esportato il        : 20240411 144710
      *====================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 08/04/24  MUTEST  BERNI Creazione
     V*=====================================================================
    O *  OBIETTIVO
    O * Questo test vuole verificare che prima di eseguire una subroutine inclusa in una /copy,
      * ne venga verificata l'esistenza
     V* ==============================================================
      * --------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_D
      /COPY QILEGEN,£TABJATDS
      /COPY QILEGEN,£TABB£1DS
      /COPY QILEGEN,£JAX_D
      /COPY QILEGEN,£DECDS
      /COPY QILEGEN,£PDS
      *---------------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------------
     C                   EVAL      £DBG_Pgm = 'MU041004'
     C                   EVAL      £DBG_Sez = 'A10'
     C                   EVAL      £DBG_Fun = '*INZ'
     C                   EXSR      £DBG
     C                   EXSR      SEZ_A10
     C                   EXSR      £DBG
     C                   EVAL      £DBG_Fun = '*END'
     C                   EXSR      £DBG
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Test richiamo routine in /copy
      *---------------------------------------------------------------------
     C     SEZ_A10       BEGSR
    OA* A£.CDOP(EXSR  )
     D* Test su stringa /COPY in array
     C                   EVAL      £DBG_Pas='P04'
     C                   EXSR      £JAX_ACOL
     C                   EVAL      £DBG_Str='EXSR £JAX_ACOL'                    COSTANTE
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* JAX_LOG
      *---------------------------------------------------------------------
     C     £JAX_LOG      BEGSR
     C                   ENDSR
      *---------------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_C
      /COPY QILEGEN,£DEC
      /COPY QILEGEN,£RITES
      /COPY QILEGEN,£JAX_C
      /COPY QILEGEN,£JAX_O
