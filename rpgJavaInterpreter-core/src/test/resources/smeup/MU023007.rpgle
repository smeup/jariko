      *====================================================================
      * smeup V6R1.021DV
      * Nome sorgente       : MU023007
      * Sorgente di origine : QTEMP/SRC(MU023007)
      * Esportato il        : 20240415 082653
      *====================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 08/04/24  MUTEST  BERNI Creazione
     V*=====================================================================
    O *  OBIETTIVO
    O * Questo test vuole verificare che se viene scritta /COPY come testo nelle schiere, non
      * venga considerato come definizione di una /copy
     V* ==============================================================
      * Variabili per definire delle schiere a fine programma
     DA30_AR7          S             50    DIM(6) CTDATA PERRCD(1)              _TXT
      * --------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_D
      /COPY QILEGEN,£TABB£1DS
      /COPY QILEGEN,£PDS
      *---------------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------------
     C                   EVAL      £DBG_Pgm = 'MU023007'
     C                   EVAL      £DBG_Sez = 'A30'
     C                   EVAL      £DBG_Fun = '*INZ'
     C                   EXSR      £DBG
     C                   EXSR      SEZ_A30
     C                   EXSR      £DBG
     C                   EVAL      £DBG_Fun = '*END'
     C                   EXSR      £DBG
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Test con stringa /copy in schiera
      *---------------------------------------------------------------------
     C     SEZ_A30       BEGSR
    OA* A£.TPDA(ARRAY  )
     D* Test su stringa /COPY in array
     C                   EVAL      £DBG_Pas='P07'
     C                   EVAL      £DBG_Str=A30_AR7(01)+';'+A30_AR7(02)+';'
     C                                     +A30_AR7(03)+';'+A30_AR7(04)+';'
     C                                     +A30_AR7(05)+';'+A30_AR7(06)
      *
     C                   ENDSR
      *---------------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_C
** A30_AR7
/COPY in prima posizione
/copy in prima posizione in minuscolo
Prova alla fine del testo /COPY
Prova alla fine del testo in minuscolo /copy
Prova con /COPY in mezzo al testo
Prova con /copy in mezzo al testo in minuscolo
