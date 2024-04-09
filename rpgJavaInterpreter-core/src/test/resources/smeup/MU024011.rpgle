      *====================================================================
      * smeup V6R1.021DV
      * Nome sorgente       : MU024011
      * Sorgente di origine : QTEMP/SRC(MU024011)
      * Esportato il        : 20240408 095134
      *====================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 27/03/24  MUTEST  BERNI  Creazione
     V*=====================================================================
    O *  OBIETTIVO
    O * DS con overlay e campi in like
     V* ==============================================================
     D A40_A01         S              2
     D A40_A02         S              3
      *
     D                 DS
     D A40_DS11A                      5    DIM(3)
     D  DS11A_FL1                          LIKE(A40_A01)
     D                                     OVERLAY(A40_DS11A:01)
     D  DS11A_FL2                          LIKE(A40_A02)
     D                                     OVERLAY(A40_DS11A:*NEXT)
      * --------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_D
      /COPY QILEGEN,£TABB£1DS
      /COPY QILEGEN,£PDS
      *---------------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------------
     C                   EVAL      £DBG_Pgm = 'MU024011'
     C                   EVAL      £DBG_Sez = 'A40'
     C                   EVAL      £DBG_Fun = '*INZ'
     C                   EXSR      £DBG
     C                   EXSR      SEZ_A40
     C                   EXSR      £DBG
     C                   EVAL      £DBG_Fun = '*END'
     C                   EXSR      £DBG
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Test atomico DS
      *---------------------------------------------------------------------
     C     SEZ_A40       BEGSR
    OA* A£.TPDA(DS  )
     D* DS con in overlay e con campi definiti come in LIKE
     C                   EVAL      £DBG_Pas='P11'
     C                   EVAL      DS11A_FL1(1)='CN'
     C                   EVAL      DS11A_FL2(1)='CLI'
     C                   EVAL      DS11A_FL1(2)='CN'
     C                   EVAL      DS11A_FL2(2)='FOR'
     C                   EVAL      DS11A_FL1(3)='CN'
     C                   EVAL      DS11A_FL2(3)='COL'
     C                   EVAL      £DBG_Str=%TRIM(A40_DS11A(1))+
     C                                      %TRIM(DS11A_FL1(1))+
     C                                      %TRIM(DS11A_FL2(1))+
     C                                      %TRIM(A40_DS11A(2))+
     C                                      %TRIM(DS11A_FL1(2))+
     C                                      %TRIM(DS11A_FL2(2))+
     C                                      %TRIM(A40_DS11A(3))+
     C                                      %TRIM(DS11A_FL1(3))+
     C                                      %TRIM(DS11A_FL2(3))
     C                   ENDSR
      *---------------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_C
