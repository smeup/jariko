      *====================================================================
      * smeup V6R1.021DV
      * Nome sorgente       : MU105501
      * Sorgente di origine : QTEMP/SRC(MU105501)
      * Esportato il        : 20240423 164817
      *====================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 23/04/24  MUTEST  BERNI  Creazione
     V*=====================================================================
    O *  OBIETTIVO
    O * Definizione inline su codice operativo KFLD
     V* ==============================================================
      * --------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_D
      /COPY QILEGEN,£TABB£1DS
      /COPY QILEGEN,£PDS
      *---------------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------------
     C                   EVAL      £DBG_Pgm = 'MU105501'
     C                   EVAL      £DBG_Sez = 'A55'
     C                   EVAL      £DBG_Fun = '*INZ'
     C                   EXSR      £DBG
     C                   EXSR      SEZ_A55
     C                   EXSR      £DBG
     C                   EVAL      £DBG_Fun = '*END'
     C                   EXSR      £DBG
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Test atomico Definizione inline su KFLD
      *---------------------------------------------------------------------
     C     SEZ_A55       BEGSR
    OA* A£.CDOP(KFLD;KLIST)
     D* Definizine inline su KFLD
     C                   EVAL      £DBG_Pas='P01'
      *
     C     KEYLIS        KLIST
     C                   KFLD                    A55_A01          10
     C                   KFLD                    A55_N01           2 0
      *
     C                   EVAL      A55_A01='ABCDEFGHIL'
     C                   EVAL      A55_N01= 12
      *
     C                   EVAL      £DBG_Str=A55_A01+', '+%CHAR(A55_N01)
      *
     C                   ENDSR
      *---------------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_C
