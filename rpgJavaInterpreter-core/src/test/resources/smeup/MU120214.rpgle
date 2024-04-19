     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 18/04/24  MUTEST  APU001 Creazione
     V*=====================================================================
    O *  OBIETTIVO
    O * L'obiettivo di questo test è quello di avere una condizione complessa
    O *  e lunga fino alla colonna 80. Dalla colonna 81 vi sarà un commento.
     V* ==============================================================
     D* Sezione delle variabili.
     D A02_A13         S             13    INZ('HELLOTHERE')
     D A02_A2          S              2    INZ('GK')
      * --------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_D
      /COPY QILEGEN,£TABB£1DS
      /COPY QILEGEN,£PDS
      *---------------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------------
     C                   EVAL      £DBG_Pgm = 'MU120214'
     C                   EVAL      £DBG_Sez = 'A02'
     C                   EVAL      £DBG_Fun = '*INZ'
     C                   EXSR      £DBG
     C                   EXSR      SEZ_A02
     C                   EXSR      £DBG
     C                   EVAL      £DBG_Fun = '*END'
     C                   EXSR      £DBG
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Test atomico IF condizione complessa fino col 80
      *---------------------------------------------------------------------
     C     SEZ_A02       BEGSR
    OA* A£.COOP()
     C                   EVAL      £DBG_Pas='P14'
      *
     C                   IF        A02_A13 = 'HELLOTHERE' AND A02_A2 <> 'QG' ANDCOSTANTE
     C                             10 > 1
     C                   EVAL      £DBG_Str= 'OK'
     C                   ENDIF
      *
     C                   ENDSR
      *---------------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_C