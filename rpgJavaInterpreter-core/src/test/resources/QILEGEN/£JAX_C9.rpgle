     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 13/12/06  V2R2    AS Aggiunte istruzioni per compilazione condizionale
     V* 02/02/17         BMA Aggiunta £UIBDO a entry servizi
     V* B£70524A  V5R1   BMA Rilascio modifiche precedenti
     V* ==============================================================
     D*-------------------------------------------------------------------
     D* OBIETTIVO
     D*
     D* Esternizzare le funzioni di prepazazione stringa XML
     D*
     D* Prerequisiti
     D*
     C*----------------------------------------------------------------
    RD* Parametri in ingresso
     C*----------------------------------------------------------------
     C     £JAX_INZP     BEGSR
     C
     C     *ENTRY        PLIST
     C                   PARM                    £UIBDS
     C                   PARM                    JAT$DS
     C                   PARM                    £UibPR
     C                   PARM                    £UibSU
     C                   PARM                    £UIBDO
     C
     C                   ENDSR
