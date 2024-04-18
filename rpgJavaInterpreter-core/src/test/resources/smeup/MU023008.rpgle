      *====================================================================
      * smeup V6R1.021DV
      * Nome sorgente       : MU023008
      * Sorgente di origine : QTEMP/SRC(MU023008)
      * Esportato il        : 20240415 082653
      *====================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 08/04/24  MUTEST  BERNI Creazione
     V*=====================================================================
    O *  OBIETTIVO
    O * Questo test vuole verificare che se viene scritta /COPY nella stessa posizione delle
      * definzioni non venga considerato come /copy da importare
     V* ==============================================================
      * Variabili per definire delle schiere a fine programma
     DA30_AR8          S             50    DIM(4) CTDATA PERRCD(1)              _TXT
      * --------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_D
      /COPY QILEGEN,£TABB£1DS
      /COPY QILEGEN,£PDS
      *---------------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------------
     C                   EVAL      £DBG_Pgm = 'MU023008'
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
     C                   EVAL      £DBG_Pas='P08'
     C                   EVAL      £DBG_Str=A30_AR8(01)+';'+A30_AR8(02)+';'
     C                                     +A30_AR8(03)+';'+A30_AR8(04)
      *
     C                   ENDSR
      *---------------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_C
** A30_AR8
Prova /COPY
Prova /COPY numero
Prova /COPY 12
      /COPY QILEGEN, AAA
