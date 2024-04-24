      *====================================================================
      * smeup V6R1.021DV
      * Nome sorgente       : MU701006
      * Sorgente di origine :
      * Esportato il        :
      *====================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 08/04/24  MUTEST  FORDAR  Creazione
     V*=====================================================================
    O *  OBIETTIVO
    O * Controlla il funzionamento delle direttive di compilazione annidate
     V* ==============================================================
     D £DBG_Str        S            100
     D £DBG_Pgm        S             15
     D £DBG_Sez        S             15
     D £DBG_Fun        S             15
     D £DBG_Pas        S             15
      * --------------------------------------------------------------
      */COPY QILEGEN,MULANG_D_D
      */COPY QILEGEN,£TABB£1DS
      */COPY QILEGEN,£PDS
      *---------------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------------
     C                   EVAL      £DBG_Pgm = 'MU701006'
     C                   EVAL      £DBG_Sez = 'A70'
     C                   EVAL      £DBG_Fun = '*INZ'
     C*                  EXSR      £DBG
     C                   EXSR      SEZ_A70
     C*                  EXSR      £DBG
     C                   EVAL      £DBG_Fun = '*END'
     C*                  EXSR      £DBG
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Test direttive di compilazione annidate
      *---------------------------------------------------------------------
     C     SEZ_A70       BEGSR
    OA* A£.CDOP(COMDIRNES)
     D* Direttive di compilazione annidate
     C                   EVAL      £DBG_Pas='P06'
      *
      * Test with 2 nested IF
      *
      /UNDEFINE DEFINE_1
      /UNDEFINE DEFINE_2
      /IF NOT DEFINED(DEFINE_1)
     C                   EVAL      £DBG_Str='OK'
     C     £DBG_Str      DSPLY
      /IF NOT DEFINED(DEFINE_2)
      /DEFINE DEFINE_2
     C                   EVAL      £DBG_Str='OK'
     C     £DBG_Str      DSPLY
      /ELSE
     C                   EVAL      £DBG_Str='ERROR'
     C     £DBG_Str      DSPLY
      /ENDIF
     C                   EVAL      £DBG_Str='OK'
     C     £DBG_Str      DSPLY
      /ELSE
     C                   EVAL      £DBG_Str='OK'
     C     £DBG_Str      DSPLY
      /ENDIF
     C                   EVAL      £DBG_Str='OK'
     C     £DBG_Str      DSPLY
      /IF NOT DEFINED(DEFINE_2)
     C                   EVAL      £DBG_Str='ERROR'
     C     £DBG_Str      DSPLY
      /ELSE
     C                   EVAL      £DBG_Str='OK'
     C     £DBG_Str      DSPLY
      /IF NOT DEFINED(DEFINE_1)
     C                   EVAL      £DBG_Str='OK'
     C     £DBG_Str      DSPLY
      /ELSE
     C                   EVAL      £DBG_Str='ERROR'
     C     £DBG_Str      DSPLY
      /ENDIF
      /ENDIF
     C                   EVAL      £DBG_Str='OK'
     C     £DBG_Str      DSPLY
      * Test with 3 nested IF
      /UNDEFINE DEFINE_1
      /UNDEFINE DEFINE_2
      /DEFINE DEFINE_3
      /IF DEFINED(DEFINE_1)
     C                   EVAL      £DBG_Str='ERROR'
     C     £DBG_Str      DSPLY
      /ELSE
     C                   EVAL      £DBG_Str='OK'
     C     £DBG_Str      DSPLY
      /IF NOT DEFINED(DEFINE_2)
      /IF DEFINED(DEFINE_3)
     C                   EVAL      £DBG_Str='OK'
     C     £DBG_Str      DSPLY
      /ENDIF
     C                   EVAL      £DBG_Str='OK'
     C     £DBG_Str      DSPLY
      /ELSE
     C                   EVAL      £DBG_Str='ERROR'
     C     £DBG_Str      DSPLY
      /ENDIF
      /ENDIF
      *
     C                   ENDSR
      *---------------------------------------------------------------------
      */COPY QILEGEN,MULANG_D_C
