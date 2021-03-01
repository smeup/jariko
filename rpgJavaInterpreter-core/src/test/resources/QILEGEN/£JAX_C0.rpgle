     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* 24/10/03          CM Sostituita £UIBSC con £JAXSC e pulito buffer
     V*                      in ricezione
     V* 19/05/04  V2R1    AA Sostituito *INU1 con *IN01
     V* 03/06/04  V2R1    BS Sostituito controllo *IN01
     V* 18/12/06  V2R2    AS Aggiunti monitor su QRCVDTAQ, QSNDDTAQ
     V*                      Settata attesa su coda in £JAX_INZ
     V*                      Aggiunte istruzioni per compilazione condizionale
     V* 01/02/07  V2R2    AS Corretto editcode in £JAX_INZ_T
     V* 16/04/07  V2R2    AS Impostato timeout code da tabella UI1 (default 500)
     V* 20/04/07  V2R2    AS Abilitazione di £Jax_FlushEn all'init dei servizi
     V* 09/05/07  V2R2    AS Irrobustimento settaggio timeout code
     V* 30/05/07  V2R2    GR Gestione chiamate cieche pop.up (£JaxNS='2')
     V* 14/06/07  V2R2    CM Aggiunta gestione Momenti con £G96
     V* 12/07/07  V2R2    CM Se assente /COPY £G96 ne inserisco una fantasme
     V* 31/07/07  V2R3    GG Rilasciata modifica del 30/05/07
     V* 27/11/07  V2R3    GR Gestione momenti anche su chiamate cieche
     V* 10/07/09  V2R3    AS Gestione overflow contatore "finestre" XML
     V* 22/07/09  V2R3    BS Spostato settaggio £JAXNS se emulazione o componente EMU da £JAX_IMP0
     V*                      Controllo Routine finale quando £JAXNS<>''
     V* 03/08/09  V2R3    CM Anticipata gestione del log e dei momenti, non veniva inizializzato il
     V*                      momento iniziale causando un'errore durante la stampa del log finale
     V* 17/10/09  V2R3    BS Esternalizzate in routine £JAX_INZC specifiche inizializzazione coda
     V* 19/11/09  V3R1    AS Aggiunta gestione di INPUT nella Funzione di £UIBPR
     V* 20/11/09  V3R1    AS Aggiunto flag per omettere campo INPUT da risposta XML
     V* 02/03/12  V3R2    GR Correzione settaggio JaxNS='2' per G99 per nuovi passaggi parametri
     V* 08/05/12  V3R2    AS Correzione in composizione attributo INPUT molto lungo
     V* 25/07/12  V3R2    CM Aggiunta gestione JaxNS='3' al servizio della /COPY £J03
     V* 12/06/13  V3R2    BS Rettifica Sostituzione Speciali dell'INPUT
     V* A£30417A  V3R2    MF Gestione multilingua - Inserimento Commenti
     V* B£30901B  V4R1    CM Recupero flag di impostazione del Setup di Default
     V* 19/09/13  V4R1   BMA Pulito £JaxBF dopo restituzione in £UIBD1
     V* 07/11/13  V4R1    CM Pulito £JaxCP dopo averla aggiunta al buffer (£JAX_ADD)
     V* 27/05/16  V4R1   BMA Sostituzione 3F per caratteri non validi header funzione
     V* 06/10/16  V5R1    PEDSTE Eliminata open PRT198 e sostituite EXCEPT con chiamate a JAJAX3
     V* 02/02/17         BMA Sostituita QRCVDTAQ £UIBDS con £UIB - Aggiunta /INCLUDE £UIB
     V* 17/02/17  V5R1    BMA Per identificare richiamo proveniente da £G99 passato £G99(1) nel
     V*                       £UIBSS oltre a G99 nei primi 3 caratteri di £UIBD1
     V* 01/03/17         BMA Sostituita QSNDDTAQ con £UIC
     V* B£70524A  V5R1   BMA Rilascio modifiche precedenti
     V* 04/01/18  V5R1   BMA Sostituzione 3F con carattere impostato da B£UT67 nella £JAX_INZ
     V* 05/09/18  V5R1   BMA Aggiunto contesto e pgm chiamante B£UT67
     V* 25/09/18  V5R1    ZS Modificata entry di chiamata al B£UT67
     V* ==============================================================
     D*-------------------------------------------------------------------
     D* OBIETTIVO
     D*
     D* Esternizzare le funzioni di prepazazione stringa XML
     D*
     D* Prerequisiti
     D*
     D* Se impostata la variabile £JAXIB verrà inviato/ricevuto il
     D*    solo buffer senza informazioni di architettura.
     C*----------------------------------------------------------------
    RD* Funzione Buffeizzazione a 29000
     C*----------------------------------------------------------------
     C     £JAX_ADD      BEGSR
      * Verifico se devo scrivere il buffer (no per servizi ciechi!!!)
1    C                   IF        %Len(£JaxCP)+%Len(£JaxBF) > 30000 AND
     C                             £JaxNS=*BLANKS
     C                   EXSR      £JAX_SND
1e   C                   ENDIF
      * Log di controllo
1    C                   IF        T$PGMB='1'
     C*  OF              EXCEPT    £JaxLgTe
     C*                  EVAL      *INOF = *OFF
     C                   Eval      £JaxLTr = 'Aggiungo'
     C                   Eval      £JaxLLb=%Len(£JaxBF)
     C                   Eval      £JaxLLc=%Len(£JaxCP)
     C                   Eval      £JaxLBu=£JaxCP
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
     C                   Eval      £JaxBF=£JaxBF+£JaxCP
      *
     C                   EVAL      £JaxCP=''
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Funzione Invio
     C*----------------------------------------------------------------
     C     £JAX_SND      BEGSR
     C
1    C                   IF        £JaxSc <> *Blanks and (£JaxCT = 'FINE' or
     C                             £JaxCT <> 'FINE' and £JaxBF <> *Blanks)
      *C                   IF        £JaxSQ=999
      *C                   Eval      £JaxSQ = 1
      *C                   ELSE
      *C                   Eval      £JaxSQ = £JaxSQ + 1
      *C                   ENDIF
     C                   CLEAR                   £UICDS
     C                   EVAL      £UicIR=£UibPG
     C                   EVAL      £UicT1=£UibT1
     C                   EVAL      £UicP1=£UibP1
     C                   EVAL      £UicK1=£UibK1
     C                   EVAL      £UicK2=£UibK2
     C                   EVAL      £UicLV=*BLANKS
      *C                   MOVEL     £JaxSQ        £UicSQ
     C                   EVAL      £UicCT=£JaxCT
     C                   EVAL      £UicD1=£JaxBF
     C                   EVAL      £JaxLU=%Len(£JaxBF)+73
      * Log di controllo
2    C                   IF        T$PGMB='1'
     C*  OF              EXCEPT    £JaxLgTe
     C*                  EVAL      *INOF = *OFF
     C                   EVAL      £UICFN='SEQ'
     C                   EVAL      £UICMT='RET'
     C                   EXSR      £UIC
     C                   EVAL      £JaxSQ=%INT(£UicSQ)
     C                   Eval      £JaxLTr = 'Invio '+%Trim(£JaxCT)+
     C                                       %EditC(£JaxSQ:'Z')
3    C                   IF        £JaxIB = ' '
     C                   Eval      £JaxLLb=£JaxLU
3x   C                   Else
     C                   EVAL      £JaxLLb=%Len(£JaxBF)
3e   C                   ENDIF
     C                   Eval      £JaxLLc=0
3    C                   If        %Len(£JaxBF) >= 140
     C                   Eval      £JaxLBu=%SubST(£UicD1:%Len(£JaxBF)-139)
3x   C                   Else
     C                   Eval      £JaxLBu=£UicD1
3e   C                   ENDIF
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
2e   C                   EndIF
      *
     C                   MOVEL(P)  'SMEUPUIDQ'   £JaxLB
      *
2    C                   IF        £JaxIB<>' '
     C                   EVAL      £JaxLU=%Len(£JaxBF)
2e   C                   ENDIF
      *
     C                   EVAL      £UICFN='SND'
     C                   EVAL      £UICMT=''
     C                   EVAL      £UICDQ=£JaxSc
     C                   EVAL      £UICLB=£JaxLB
     C                   EVAL      £UICIB=£JaxIB
     C                   EVAL      £UICDI_LU=£JaxLU
     C                   EXSR      £UIC
      *C                   IF        £JaxIB = ' '
      *C                   MONITOR
      *C                   CALL      'QSNDDTAQ'
      *C                   PARM                    £JaxSc
      *C                   PARM      'SMEUPUIDQ'   £JaxLB
      *C                   PARM                    £JaxLU
      *C                   PARM                    £UicDS
      *C                   ON-ERROR  *ALL
      *C                   ENDMON
      *C                   Else
      *C                   EVAL      £JaxLU=%Len(£JaxBF)
      *C                   MONITOR
      *C                   CALL      'QSNDDTAQ'
      *C                   PARM                    £JaxSc
      *C                   PARM      'SMEUPUIDQ'   £JaxLB
      *C                   PARM                    £JaxLU
      *C                   PARM                    £JaxBF
      *C                   ON-ERROR  *ALL
      *C                   ENDMON
      *C                   EndIF
     C
1e   C                   EndIF
     C
     C                   EVAL      £JaxBF = ''
     C
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Funzione Ricezione
     C*----------------------------------------------------------------
     C     £JAX_RCV      BEGSR
     C     G0JAXRCV      TAG
     C
     C                   Eval      £Jax35 = *ON
1    C                   IF        £JaxRC <> *Blanks and £UibCM='CONT'
     C                             or £JaxIB <> ' '
     C                   Eval      £Jax35 = *OFF
2    C                   IF        £JaxIB = ' '
      *C                   MONITOR
      *C                   CALL      'QRCVDTAQ'
      *C                   PARM                    £JaxRC
      *C                   PARM      'SMEUPUIDQ'   £JaxLB
      *C                   PARM      31000         £JaxLU
      *C                   PARM      *BLANKS       £UibDS
      *C                   PARM      £JaxWT        £JaxWE
      *C                   ON-ERROR  *ALL
      *C                   CLEAR                   £UibDS
      *C                   EVAL      £JAXLU=0
      *C                   ENDMON
     C                   MOVEL(P)  'SMEUPUIDQ'   £JaxLB
     C                   EVAL      £JaxWE=£JaxWT
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
3    C                   IF        £JaxLU>0
     C                   EVAL      £UibDS=%SUBST(£UibDS:1:£JaxLU)
3e   C                   ENDIF
2x   C                   Else
      *C                   MONITOR
      *C                   CALL      'QRCVDTAQ'
      *C                   PARM                    £JaxRC
      *C                   PARM      'SMEUPUIDQ'   £JaxLB
      *C                   PARM      31000         £JaxLU
      *C                   PARM      *BLANKS       £UibD1
      *C                   PARM      £JaxWT        £JaxWE
      *C                   ON-ERROR  *ALL
      *C                   CLEAR                   £UibD1
      *C                   EVAL      £JAXLU=0
      *C                   ENDMON
     C                   EVAL      £JaxWE=£JaxWT
     C                   EVAL      £JaxLB='SMEUPUIDQ'
     C                   EVAL      £UIBFN='RCV'
     C                   EVAL      £UIBMT=''
     C                   EVAL      £UIBDQ=£JaxRC
     C                   EVAL      £UIBLB=£JaxLB
     C                   EVAL      £UIBWE=£JaxWE
     C                   CLEAR                   £UIBD1
     C                   CLEAR                   £UIBDI_LU
     C                   EVAL      £UIBIB='1'
     C                   EXSR      £UIB
     C                   EVAL      £JaxLU=£UIBDO_LI
3    C                   IF        £JaxLU>0
     C                   EVAL      £UibD1=%SUBST(£UibD1:1:£JaxLU)
3e   C                   ENDIF
2e   C                   EndIF
      * Verifico se PING
2    C                   IF        £JaxIB=' ' AND £JAXLU=4 AND
     C                             £UIBDS='PING' OR
     C                             £JaxIB<>' ' AND £JAXLU=4 AND
     C                             £UIBD1='PING'
     C                   GOTO      G0JAXRCV
2e   C                   ENDIF
      * Log di controllo
2    C                   IF        T$PGMB='1'
     C*  OF              EXCEPT    £JaxLgTe
     C*                  EVAL      *INOF = *OFF
     C                   Eval      £JaxLTr = 'Ricevuto '+%Trim(£UibCM)
     C                   Eval      £JaxLLb=£JaxLU
     C                   Eval      £JaxLLc=0
3    C                   If        £JaxLU >= 140
     C                   Eval      £JaxLBu=%SubST(£UibD1:£JaxLU-139)
3x   C                   Else
3e   C                   ENDIF
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
2e   C                   ENDIF
     C
1e   C                   EndIF
     C
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Impostazioni iniziali
     C*----------------------------------------------------------------
     C     £JAX_IMP      BEGSR
     C
     C                   CLEAR                   £JaxDSGen
     C                   CLEAR                   £JaxDSCoda
     C                   CLEAR                   £JaxNS
      * Se richiesta la stampa del log
1    C                   IF        T$PGMB='1'
     C                   TIME                    £JaxLSt
      *. Inizio
     C                   Eval      £JaxLTr = 'Inizio'
     C*  OF              EXCEPT    £JaxLgTe
     C*                  EVAL      *INOF = *OFF
     C                   Eval      £JaxLBu = £UIBPG+' '+£UIBFU+' '+£UIBME
     C                             + £UIBT1+£UIBP1+£UIBK1
     C                             + £UIBT2+£UIBP2+£UIBK2
     C                             + £UIBT3+£UIBP3+£UIBK3
     C                             + £UIBT4+£UIBP4+£UIBK4
     C                             + £UIBT5+£UIBP5+£UIBK5
     C                             + £UIBT6+£UIBP6+£UIBK6
     C*                  EXCEPT    £JaxLgRi
     C                   CALL      'JAJAX3'
     C                   PARM                    £Jax3Fun         10
     C                   PARM                    £Jax3Met         10
     C                   PARM                    £PDSNP
     C                   PARM      '£JaxLgRi'    £Jax3Exc         10
     C                   PARM                    £JaxDSLog
     C                   PARM                    £Jax3Msg         10
      *. Parametri
2    C                   IF        £UibPA<>' '
     C                   Eval      £JaxLTr = 'Parametro'
     C                   Eval      £JaxLBu = £UibPA
     C*  OF              EXCEPT    £JaxLgTe
     C*                  EVAL      *INOF = *OFF
     C*                  EXCEPT    £JaxLgRi
     C                   CALL      'JAJAX3'
     C                   PARM                    £Jax3Fun         10
     C                   PARM                    £Jax3Met         10
     C                   PARM                    £PDSNP
     C                   PARM      '£JaxLgRi'    £Jax3Exc         10
     C                   PARM                    £JaxDSLog
     C                   PARM                    £Jax3Msg         10
2e   C                   ENDIF
      *. Dati
2    C                   IF        £UibD1<>' '
     C                   Eval      £JaxLTr = 'Dati Coda'
     C                   Eval      £JaxLBu = £UibD1
     C*  OF              EXCEPT    £JaxLgTe
     C*                  EVAL      *INOF = *OFF
     C*                  EXCEPT    £JaxLgRi
     C                   CALL      'JAJAX3'
     C                   PARM                    £Jax3Fun         10
     C                   PARM                    £Jax3Met         10
     C                   PARM                    £PDSNP
     C                   PARM      '£JaxLgRi'    £Jax3Exc         10
     C                   PARM                    £JaxDSLog
     C                   PARM                    £Jax3Msg         10
2e   C                   ENDIF
1e   C                   ENDIF
      * Gestione dei momenti
      *
     C****               IF        £UIBD1='G99'
     C****               EVAL      £JaxNS='2'
     C****               CLEAR                   £JaxDSPop
     C****               CLEAR                   £JaxPopN
     C****               GOTO      G9JAXIMP
     C****               ENDIF
      *
1    C                   SELECT
1x   C                   WHEN      £UIBPG='J03'
     C                   EVAL      £JaxNS='3'
     C                   CLEAR                   £JaxDSPop
     C                   CLEAR                   £JaxPopN
     C                   LEAVESR
1x   C                   WHEN      £INZJT='I' OR £INZJT='L' OR
     C                             £UIBPG='EMU'
     C                   EVAL      £JaxNS='1'
     C                   CLEAR                   £JaxDSPop
     C                   CLEAR                   £JaxPopN
     C                   LEAVESR
1e   C                   ENDSL
      *
     C                   Eval      £JaxCT = 'CONTINUA'
      *
1    C**                 IF        T$PGMB='1'
     C**                 TIME                    £JaxLSt
      *
     C**                 Eval      £JaxLTr = 'Inizio'
     C** OF              EXCEPT    £JaxLgTe
     C**                 EVAL      *INOF = *OFF
     C**                 Eval      £JaxLBu = £UIBPG+' '+£UIBFU+' '+£UIBME
     C**                           + £UIBT1+£UIBP1+£UIBK1
     C**                           + £UIBT2+£UIBP2+£UIBK2
     C**                           + £UIBT3+£UIBP3+£UIBK3
     C**                           + £UIBT4+£UIBP4+£UIBK4
     C**                           + £UIBT5+£UIBP5+£UIBK5
     C**                           + £UIBT6+£UIBP6+£UIBK6
     C**                 EXCEPT    £JaxLgRi
      *
     C**                 IF        £UibPA<>' '
     C**                 Eval      £JaxLTr = 'Parametro'
     C**                 Eval      £JaxLBu = £UibPA
     C** OF              EXCEPT    £JaxLgTe
     C**                 EVAL      *INOF = *OFF
     C**                 EXCEPT    £JaxLgRi
1e   C**                 ENDIF
      *
     C**                 IF        £UibD1<>' '
     C**                 Eval      £JaxLTr = 'Dati Coda'
     C**                 Eval      £JaxLBu = £UibD1
     C** OF              EXCEPT    £JaxLgTe
     C**                 EVAL      *INOF = *OFF
     C**                 EXCEPT    £JaxLgRi
1e   C**                 ENDIF
1e   C**                 ENDIF
      * Buffer Previous
      * . Se attivo Flag £JAXNI (no input), non trascrivo l'INPUT nell'XML di risposta
1    C                   IF        £JaxNI='1'
      *C                   EVAL      £UibPR=P_RxLATE(£UibPR:'INPUT(_&_INPUT_&_)'  COSTANTE
      *C                                    :' ')
      *C                   EVAL      £UibPR=%XLATE(£UIB_C_3F:'?':£UibPR)
     C                   EVAL      £UibPR=%XLATE(£UIB_C_3F:£UIB_S_3F:£UibPR)
1e   C                   ENDIF

     C**********         EVAL      £UibPR_Long=%TRIMR(P_RxSOS(£UIBD1))
     C                   EVAL      £UibPR_Long=%TRIMR(£UIBD1)
      * Se il £UIBD1 è maggiore di 30000, devo troncarlo perché la RxLATE gestisce
      * un input da 30000 e potrebbe troncarlo in un posto che rende l'XML finale
      * invalido
1    C                   IF        %LEN(£UibPR_Long)>30000
      * La troncatura la devo fare all'ultimo blank (o ;)all'interno dei 30000, per evitare che
      * risultino cose del tipo &quo che non sono valide per l'XML
      * in realtà la faccio all'interno dei 29987 (30000-13) perché in coda aggiungo
      * *NotComplete*
     C                   EVAL      £UibPR_Long=%SUBST(£UibPR_Long:1:29987)
2    C                   FOR       £JAXNI_POS=29987 DOWNTO 1
3    C                   IF        %SUBST(£UibPR_Long:£JAXNI_POS:1)=*BLANKS
     C                             OR %SUBST(£UibPR_Long:£JAXNI_POS:1)=';'
     C                   LEAVE
3e   C                   ENDIF
2e   C                   ENDFOR
     C                   EVAL      £UibPR_Long=%SUBST(£UibPR_Long:1:£JAXNI_POS) COSTANTE
     C                                         +'*NotComplete*'                 COSTANTE
1e   C                   ENDIF
      *C                   EVAL      £UibPR_Long=P_RxLATE(£UibPR:'_&_INPUT_&_':   COSTANTE
      *C                                                  £UibPR_Long)
      *C                   EVAL      £UibPR_Long=%XLATE(£UIB_C_3F:'?':£UibPR_Long)
     C                   EVAL      £UibPR_Long=%XLATE(£UIB_C_3F
     C                             :£UIB_S_3F:£UibPR_Long)
1    C                   IF        £UibPR_Long <> ''
      * Anche la RxLATE potrebbe far aumentare la dimensione oltre i 30000, in caso
      * spezzo l'invio
2    C                   IF        %LEN(£uibPR_Long)>30000
     C                   Eval      £JaxCP = %TRIM(%SUBST(£UibPR_Long:1:30000))
     C                   EXSR      £JAX_ADD
     C                   Eval      £JaxCP = %SUBST(£UibPR_Long:30001)
     C                   EXSR      £JAX_ADD
2x   C                   ELSE
     C                   Eval      £JaxCP = £UibPR_Long
     C                   EXSR      £JAX_ADD
2e   C                   ENDIF
1e   C                   ENDIF
      *
     C**   G9JAXIMP      TAG
      * Gestione dei momenti
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Start program
     C*----------------------------------------------------------------
     C     £JAX_INZ      BEGSR
      *
     C                   EXSR      £JAX_INZC
     C                   CLEAR                   £JaxDSLog
      *
     C                   CALL      'B£UT67'
     C                   PARM                    £UIB_S_3F
     C                   PARM      '£JAX'        £JAXUT67CO       10
     C                   PARM      £PDSNP        £JAXUT67PG       10
     C                   PARM                    SCHCHK
     C                   PARM                    SCHSOS
      *
     C                   Eval      £RITST = 'PGM'
     C                   Eval      £RITEL = £PDSNP
     C                   Exsr      £Rites
     C  N35              Eval      PGM$DS = £RitLi
     C   35              Eval      PGM$DS = ''
      * Tipo gestione del setup
     C                   EVAL      £RITST='B£5'
     C                   EVAL      £RITEL='*'
     C                   EXSR      £RITES
     C  N35              EVAL      B£5$DS=£RITLI
     C   35              CLEAR                   B£5$DS
     C                   EVAL      £JAXCS=T$B£5P
     C*****T$PGMB*****   COMP      '1'                                    01
     C
1    C*                  IF        T$PGMB='1'
     C*                  OPEN      PRT198
     C*                  EVAL      *INOF = *ON
     C*                  MOVEL     *ALL'-'       £S1198          198
1e   C*                  ENDIF
      * Imposto tempo di attesa
     C                   Eval      £RITST = 'UI1'
     C                   Eval      £RITEL = '*'
     C                   Exsr      £Rites
     C  N35              Eval      UI1$DS = £RitLi
     C   35              CLEAR                   UI1$DS
      *
1    C                   SELECT
1x   C                   WHEN      T$UI1C=*BLANKS
     C                   EVAL      £JAXWT=500
1x   C                   WHEN      T$UI1C='A'
     C                   EVAL      £JAXWT=120
1x   C                   WHEN      T$UI1C='B'
     C                   EVAL      £JAXWT=2000
1x   C                   WHEN      T$UI1C='C'
     C                   EVAL      £JAXWT=30000
1x   C                   WHEN      T$UI1C='D'
     C                   EVAL      £JAXWT=-1
1x   C                   OTHER
     C                   EVAL      £JAXWT=500
1e   C                   ENDSL
      * Abilito il flushing della coda
     C                   EVAL      £Jax_FlushEn='1'
     C
     C                   ENDSR
      *----------------------------------------------------------------
    RD* Inizializzazione Variabili della coda
      *----------------------------------------------------------------
     C     £JAX_INZC     BEGSR
      *
1    C                   If        £UibSC <> *Blanks
     C                   Eval      £JaxSc = £UibSC
     C                   Eval      £JaxRc = £UibSC
     C                   MOVEL     'ECTS'        £JaxRc
1e   C                   EndIF
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Start program per transazioni
     C*----------------------------------------------------------------
     C     £JAX_INZ_T    BEGSR
      *
      * Eseguo start comune
     C                   EXSR      £JAX_INZ
      * Imposto opportunamente le code
     C                   EVAL      £JaxRC='TCTS'+%TRIM(%EDITC(£PDSJZ:'X'))
     C                   EVAL      £JaxSC='TSTC'+%TRIM(%EDITC(£PDSJZ:'X'))
      * imposto tempo di attesa infinito (l'eventuale scatto del timeout è gestito dal JAJAS1
      * che all'occorrenza lo inoltra)
     C                   EVAL      £JaxWT=-1
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Fine programma
     C*----------------------------------------------------------------
     C     £JAX_FIN      BEGSR
      *
     C****               IF        £JaxNS='2'
     C****               EVAL      £UIBD1=£JaxBF
     C****               GOTO      G9JAXFIN
     C****               ENDIF
      *
1    C                   SELECT
1x   C                   WHEN      £JaxNS='1'
     C                   GOTO      G9JAXFIN
1x   C                   WHEN      £JaxNS='2'
     C                   EVAL      £UIBD1=£JaxBF
     C                   CLEAR                   £JAXBF
     C                   GOTO      G9JAXFIN
1x   C                   WHEN      £JaxNS='3'
     C                   EVAL      £UIBD1=£JaxBF
     C                   CLEAR                   £JAXBF
     C                   GOTO      G9JAXFIN
1e   C                   ENDSL
      *
1    C                   If        £UibSU <> ''
     C                   Eval      £JaxCP = £UibSU
     C                   EXSR      £JAX_ADD
1e   C                   EndIF
     C
     C                   Eval      £JaxCT = 'FINE'
     C                   EXSR      £JAX_SND
      *
     C     G9JAXFIN      TAG
1    C                   IF        T$PGMB='1'
     C                   TIME                    £JaxLEt
     C     £JaxLEt       SUBDUR    £JaxLSt       £JaxLAt:*MS
     C*  OF              EXCEPT    £JaxLgTe
     C*                  EVAL      *INOF = *OFF
     C*                  EXCEPT    £JaxLgTm
     C                   CALL      'JAJAX3'
     C                   PARM                    £Jax3Fun         10
     C                   PARM                    £Jax3Met         10
     C                   PARM                    £PDSNP
     C                   PARM      '£JaxLgTm'    £Jax3Exc         10
     C                   PARM                    £JaxDSLog
     C                   PARM                    £Jax3Msg         10
1e   C                   ENDIF
      * Gestione dei momenti
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Emetti Log del Tempo
     C*----------------------------------------------------------------
     C     £JAX_TIME     BEGSR
      *
1    C                   IF        T$PGMB='1'
     C                   TIME                    £JaxLEt
     C     £JaxLEt       SUBDUR    £JaxLSi       £JaxLAt:*MS
     C*  OF              EXCEPT    £JaxLgTe
     C*                  EVAL      *INOF = *OFF
     C*                  EXCEPT    £JaxLgTi
     C                   CALL      'JAJAX3'
     C                   PARM                    £Jax3Fun         10
     C                   PARM                    £Jax3Met         10
     C                   PARM                    £PDSNP
     C                   PARM      '£JaxLgTi'    £Jax3Exc         10
     C                   PARM                    £JaxDSLog
     C                   PARM                    £Jax3Msg         10
1e   C                   ENDIF
      *
     C                   ENDSR
