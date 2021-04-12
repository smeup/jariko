     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 18/12/06  V2R2    AS Creazione
     V* 20/04/07  V2R2    AS Aggiunte £JAX_FLUSH, £JAX_FIN0, £JAX_IMP0
     V* 25/02/08  V2R3    CC Aggiunta £JAX_BSETUP
     V* 12/03/08  V2R3    CC Gestione invio setup diversi per ogni tipo componente
     V* 20/03/08  V2R3    CC Elim. metodo £JAX_BSETUP. Gestione campo XS di setup fatto da LOOCUP
     V* 19/04/09  V2R3    BS Forzatura £JAX_NS='2' se JOB di emulazione
     V* 20/04/09  V2R3    BS Forzatura £JAX_NS='2' se Componente EMU
     V* 29/05/09  V2R3    AS Corretto settaggio £JAXNS se emulazione o componente EMU
     V* 22/07/09  V2R3    BS Spostato settaggio £JAXNS se emulazione o componente EMU in £JAX_IMP
     V* 14/06/09  V2R3    BS Richiamo £JAX_INZC se non eseguita nella £INIZI
     V* A£30417A  V3R2    MF Gestione multilingua - Inserimento Commenti
     V* B£30901B  V4R1    CM Gestione del Setup di Default
     V*                      La gestione è attiva solo sul componente EXB (Matrice).
     V*                      Il campo £JAXCS viene derivato dalla tabella PGM e in risalita alla
     V*                      tabella B£5, viene indicato se si vuole attivare la gestione del setup
     V*                      di default 1=Si
     V*                      Nel caso in cui il contesto sia definito dal servizio, dopo averlo
     V*                      genearato, bisogna impostare il campo £JAXCSC='1'.
     V* 05/10/16  V5R1    PEDSTE Sostituite EXCEPT con chiamate a JAJAX3
     V* 02/02/17         BMA Sostituita QRCVDTAQ £UIBDS con £UIB
     V* B£70524A  V5R1   BMA Rilascio modifiche precedenti
     V* JA80807A  V5R1   BMA Revisione gestione messaggi
     V* 02/01/20  001408 BMA Sostituzione caratteri speciali nel contesto
     V* 03/01/20  V5R1    BS Check-out 001408 in SMEDEV
     V* ==============================================================
     D*-------------------------------------------------------------------
     D* NOTA:
     D*
     D* Questa /COPY è un'estensione della £JAX_C0.
     D*
     C*----------------------------------------------------------------
    RD* Funzione Ricezione con ciclo di attesa completo (tralascio PING, chiudo lavoro ...)
     C*----------------------------------------------------------------
     C     £JAX_DTARCV   BEGSR
      *
      * imposto flag
     C                   CLEAR                   £JaxDtRc
      *
      * Resto in attesa fino a quando non ho ricevuto dei dati da processare
1    C                   DOW       £JaxDtRc=*BLANKS
2     *C                   MONITOR
      *C                   CALL      'QRCVDTAQ'
      *C                   PARM                    £JaxRC
      *C                   PARM      'SMEUPUIDQ'   £JaxLB
      *C                   PARM      31000         £JaxLU
      *C                   PARM      *BLANKS       £UibDS
      *C                   PARM      £JaxWT        £JaxWE
      * * Se errore di ricezione pulisco la DS e imposto la lunghezza a 0
2x    *C                   ON-ERROR  *ALL
      *C                   CLEAR                   £UibDS
      *C                   EVAL      £JAXLU=0
2e    *C                   ENDMON
      *
     C                   EVAL      £JaxWE=£JaxWT
     C                   EVAL      £JaxLB='SMEUPUIDQ'
     C                   EVAL      £UIBFN='RCV'
     C                   EVAL      £UIBMT=''
     C                   EVAL      £UIBDQ=£JaxRC
     C                   EVAL      £UIBLB=£JaxLB
     C                   EVAL      £UIBWE=£JaxWE
     C                   CLEAR                   £UIBDS
     C                   CLEAR                   £UIBDI_LU
     C                   EVAL      £UIBIB=' '
     C                   EXSR      £UIB
     C                   EVAL      £JaxLU=£UIBDO_LU
      *
      * Se scatta il timeout, oppure la ricezione ha dato un errore,
      * oppure ho ricevuto il messaggio di chiusura
2    C                   IF        £JAXLU = 0 OR
     C                             (£UIBMS='COM' AND £UIBPG='COL' AND
     C                             £UIBFU='F-E')
      * . Chiudo i servizi base
      * . . Elimino Coda Server/Client
     C                   EVAL      £SBM='DLTDTAQ DTAQ(SMEUPUIDQ/'+              COSTANTE
     C                             %TRIM(£JaxSC)+')'                            COSTANTE
3    C*                   MONITOR
     C                   CALL      'QCMDEXC'
     C                   PARM                    £SBM            256
     C                   PARM      256           $LGH             15 5
3x   C*                   ON-ERROR  *ALL
3e   C*                   ENDMON
      * . . Elimino Coda Client/Server
     C                   EVAL      £SBM='DLTDTAQ DTAQ(SMEUPUIDQ/'+              COSTANTE
     C                             %TRIM(£JaxRC)+')'                            COSTANTE
3    C*                   MONITOR
     C                   CALL      'QCMDEXC'
     C                   PARM                    £SBM            256
     C                   PARM      256           $LGH             15 5
3x   C*                   ON-ERROR  *ALL
3e   C*                   ENDMON
      * Se non è scattato il timeout, non c'è stato errore di ricezione e
      * non è arrivato il messaggio di chiusura
2x   C                   ELSE
      * . Se non ho ricevuto un PING, imposto l'uscita dal ciclo di attesa (ho ricevuto dati da
      *   processare)
3    C                   IF        Not(£JaxLU=4 AND £UIBDS='PING')
     C                   EVAL      £JaxDtRc='1'
3e   C                   ENDIF
2e   C                   ENDIF
1e   C                   ENDDO
      *
     C                   EVAL      £UibDS=%SUBST(£UibDS:1:£JaxLU)
      *
      * Log di controllo
1    C                   IF        T$PGMB='1'
     C*  OF              EXCEPT    £JaxLgTe
     C*                  EVAL      *INOF = *OFF
     C                   Eval      £JaxLTr = 'Ricevuto '+%Trim(£UibCM)
     C                   Eval      £JaxLLb=£JaxLU
     C                   Eval      £JaxLLc=0
2    C                   If        £JaxLU >= 140
     C                   Eval      £JaxLBu=%SubST(£UibD1:£JaxLU-139)
2x   C                   Else
2e   C                   ENDIF
     C*                  EXCEPT    £JaxLgRi
     C                   CALL      'JAJAX3'
     C                   PARM                    £Jax3Fun         10
     C                   PARM                    £Jax3Met         10
     C                   PARM                    £PDSNP
     C                   PARM      '£JaxLgRi'    £Jax3Exc         10
     C                   PARM                    £JaxDSLog
     C                   PARM                    £Jax3Msg         10
     C                   Eval      £JaxLTrU=''
     C                   Exsr      £JAX_LOG
1e   C                   ENDIF
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Flushing della coda
     C*----------------------------------------------------------------
     C     £JAX_FLUSH    BEGSR
      *
      * Se flushing abilitato e XML ancora presente nella CODA
1    C                   IF        £Jax_FlushEn='1' AND £UIBCM='CONT'
      * . Messaggio di flushing della coda
     C                   EVAL      £JaxMTxt='Some data ignored'                 COSTANTE
      *C                   EVAL      £JaxMLiv='00'
     C                   EVAL      £JaxMGra=£Jax_GraInf
     C                   EVAL      £JaxMMod=£Jax_ModHH
     C                   EVAL      £JaxMTyp='I'
     C                   EXSR      £JAX_BMES
      *
      * . Flushing della coda
2    C                   DOW       £UIBCM='CONT'
     C                   EXSR      £JAX_DTARCV
2e   C                   ENDDO
1e   C                   ENDIF
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Funzioni standard di chiusura dei servizi
     C*----------------------------------------------------------------
     C     £JAX_FIN0     BEGSR
      *
      * Cancellazione dell'eventuale XML eccedente non gestito
     C                   EXSR      £JAX_FLUSH
      * Inserimento messaggi
     C                   EXSR      £JAX_BMES_F
      * Chiusure finali (spedizione residuo XML, ecc..)
     C                   EXSR      £JAX_FIN
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Funzioni standard di inizializzazione dei servizi
     C*----------------------------------------------------------------
     C     £JAX_IMP0     BEGSR
      * Creazione del contesto manuale
     C                   EVAL      £JAXCSC=''
      *
1    C                   IF        £UIBSC<>'' AND £JAXSC=' '
     C                   EXSR      £JAX_INZC
1e   C                   ENDIF
      * Impostazioni iniziali
     C                   EXSR      £JAX_IMP
     C                   EXSR      £JAX_IMPO
      * Inizializzazione messaggi
     C                   EXSR      £JAX_BMES_I
      *
     C                   ENDSR
      *
