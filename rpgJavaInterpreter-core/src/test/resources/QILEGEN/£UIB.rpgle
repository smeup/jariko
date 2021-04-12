      /IF NOT DEFINED(UIB_INCLUDED)
      /DEFINE UIB_INCLUDED
     D* ==============================================================
     D* MODIFICHE Ril.  T Au Descrizione
     D* gg/mm/aa  nn.mm i xx Breve descrizione
     D* ==============================================================
     D* 18/11/14         BMA Creazione
     V* B£70524A  V5R1   BMA Rilascio modifiche precedenti
     D* 05/06/17  V5R1   BMA Non reimposto il 35
     V* 20/09/17  V5R1   BMA Valorizzato programma chiamante
     D* ==============================================================
     D*----------------------------------------------------------------
     D* OBIETTIVO
     D*
     D* FUNZIONI/METODI
     D*
     D* PREREQUISITI
     D*----------------------------------------------------------------
     D*I/COPY QILEGEN,£UIBDS
     D*
     D*
     D*  Input:  £UIBFN: Funzione                 (10)
     D*          £UIBMT: Metodo                   (10)
     D*          £UIBDQ: Coda dati                (10)
     D*          £UIBLB: Libreria coda dati       (10)
     D*          £UIBDS: Funzione (se invio su coda)
     D*          £UIBIB: ' '=Risultato in £UIBDS; '1'=Risultato in £UIBD1 (equivale a £JaxIB)
     D*
     D*  Output
     D*          £UIBER: Indicatore errore
     D*          £UIBLU: Lunghezza restituita £UIBDS (se lettura su coda) in DS £UIBDO
     D*          £UIBLI: Lunghezza restituita £UIBD1 (se lettura su coda) in DS £UIBDO
     D*          £UIBDS: Funzione (se lettura su coda)
     D*
     D*
     D*----------------------------------------------------------------
     C     £UIB          BEGSR
      *
     C                   EVAL      £UIBDI_P1=£PDSNP
      *
    >C                   CALL      'B£UIB0'
    >C                   PARM                    £UIBFN           10            -->
    >C                   PARM                    £UIBMT           10            -->
    >C                   PARM                    £UIBDQ           10            -->
    >C                   PARM                    £UIBLB           10            -->
     C                   PARM                    £UIBIB            1            -->
    >C                   PARM                    £UIBWE            5 0          -->
    >C                   PARM                    £UIBDS                         <->
    >C                   PARM                    £UIBDI                         <--
    >C                   PARM      '0'           £UIBER            1            <--
    >C                   PARM                    £UIBDO                         <--
      *
      * Non reimposto il 35 per non sovrascrivere usi del chiamante
      * Per testare l'errore dovrà essere controllato £UIBER
     C****               EVAL      *IN35=£UIBER
      *
     C                   ENDSR
      *----------------------------------------------------------------
      /ENDIF
