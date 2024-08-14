     D £DBG_Str        S             2

     ********** PREPROCESSOR COPYSTART QILEGEN,£UIBDS
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 10/05/21  V5R1    BONMAI Creazione
     V* 18/05/24  V6R1    FORDAR Estensione
     V* /COPY £UIBDS
     V* ==============================================================
      *--------------------------------------------------------------------------------------------*
      *IF NOT DEFINED(UIBDS_INCLUDED)
      *DEFINE UIBDS_INCLUDED
     D £UIBDS          DS            10
     D £UIBME                          0
      *--------------------------------------------------------------------------------------------*
     ********** PREPROCESSOR COPYEND QILEGEN,£UIBDS

     DXUIBME           S                   LIKE(£UIBME)

      *--------------------------------------------------------------*
    RD*  Impostazioni iniziali
      *--------------------------------------------------------------*
     C     IMP0          BEGSR
     C                   IF        %SUBST(£UIBME:1:3)='STA'
     C                   MOVEL(P)  'STA'         XUIBME           10
     C                   ELSE
     C                   MOVEL(P)  £UIBME        XUIBME           10
     C                   ENDIF
     C                   ENDSR

     C                   EVAL      £DBG_Str='ok'
     C     £DBG_Str      DSPLY
