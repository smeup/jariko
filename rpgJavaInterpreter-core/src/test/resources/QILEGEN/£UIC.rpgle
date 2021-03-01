      /IF NOT DEFINED(UIC_INCLUDED)
      /DEFINE UIC_INCLUDED
     D* ==============================================================
     D* MODIFICHE Ril.  T Au Descrizione
     D* gg/mm/aa  nn.mm i xx Breve descrizione
     D* ==============================================================
     D* 01/03/17         BMA Creazione
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
     D*I/COPY QILEGEN,£UICDS
     D*
     D*
     D*  Input:  £UICFN: Funzione                 (10)
     D*          £UICMT: Metodo                   (10)
     D*          £UICDQ: Coda dati                (10)
     D*          £UICLB: Libreria coda dati       (10)
     D*          £UICDS: Funzione (se invio su coda)
     D*          £UICIB: ' '=Risultato in £UICDS; '1'=Risultato in £UICD1 (equivale a £JaxIB)
     D*
     D*  Output
     D*          £UICER: Indicatore errore
     D*          £UICLU: Lunghezza restituita £UICDS (se lettura su coda) in DS £UICDO
     D*          £UICLI: Lunghezza restituita £UICD1 (se lettura su coda) in DS £UICDO
     D*          £UICDS: Funzione (se lettura su coda)
     D*
     D*
     D*----------------------------------------------------------------
     C     £UIC          BEGSR
      *
     C                   EVAL      £UICDI_P1=£PDSNP
      *
    >C                   CALL      'B£UIC0'
    >C                   PARM                    £UICFN           10            -->
    >C                   PARM                    £UICMT           10            -->
    >C                   PARM                    £UICDQ           10            -->
    >C                   PARM                    £UICLB           10            -->
     C                   PARM                    £UICIB            1            -->
    >C                   PARM                    £UICWE            5 0          -->
    >C                   PARM                    £UICDS                         <->
    >C                   PARM                    £UICDI                         -->
    >C                   PARM      '0'           £UICER            1            <--
    >C                   PARM                    £UICDO                         <--
      *
      * Non reimposto il 35 per non sovrascrivere usi del chiamante
      * Per testare l'errore dovrà essere controllato £UICER
     C****               EVAL      *IN35=£UICER
      *
     C                   ENDSR
      *----------------------------------------------------------------
      /ENDIF
