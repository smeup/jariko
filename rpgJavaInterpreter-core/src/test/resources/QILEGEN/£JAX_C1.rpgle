     V*================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* A£30417A  V3R2    MF Gestione multilingua - Inserimento Commenti
     V*================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 03/06/04  V2R1    BS Controllo caratteri speciali £JAXCP in agg.riga
     V* 19/03/04  V2R1    CM Eliminata sostituzione caratteri speciali in CDATA
     V* 11/10/04  V2R1    GR Decodifica automatica in £JAX_ADDO
     V* 28/10/04  V2R1    CM Aggiunto TRIMR su Oggetti.
     V* 18/11/04  V2R1    GR Aggiunge Exec se non c'è in £JaxOP
     V* 02/12/04  V2R1    GR £JAX_ADDE
     V* 10/12/04  V2R1    GR Chiusura automatica oggetti se non specificata
     V* 27/01/05  V2R1    CM Aggiunta chiusura oggetto
     V* 30/09/05  V2R1    GR Pulizia £JAXCP dopo ARIG_I/F AGRI_I/F
     V* 09/12/05  V2R1    BS Gestione Tabelle Correlate
     V* 25/01/05  V2R1    GR Estensione messaggi, creazione variabili
     V* 05/04/06  V2R2    GR Chiusura opzionale nodo messaggio
     V* 27/04/06  V2R2    AS Aggiunta gesiotne campi £JaxFL e £JaxLF
     V* 24/11/06  V2R2    RM Errore in costruzione griglia per numeor colonne = 0
     V* 13/12/06  V2R2    AS Aggiunte istruzioni per compilazione condizionale
     V* 23/02/07  V2R2    AS Aggiunta possibilità di specificare il tipo messaggio CONF/INFO
     V* 30/05/07  V2R2    GR Gestione chiamate cieche pop.up (£JaxNS='2')
     V* 31/07/07  V2R3    GG Rilasciata modifica del 30/05/07
     V* 11/10/07  V2R3    GR Gestione tipo variabili in £JAX_AVAR
     V* 14/11/07  V2R3    GR Riempie con il codice azione £JaxDsPop
     V* 19/01/09  V2R3    AS Corretto limite ciclo in £JAX_BVAR
     V* 20/01/09  V2R3    LS Gestione formule
     V* 21/01/09  V2R2    AS Aggiunta gestione campo £JaxGP in £JAX_ADDO
     V* 06/03/09  V2R3    AS Irrobustita £JAX_ADDO
     V* 02/04/09  V2R3    BMA Modiifica per sfondamento indici schiera in £JAX_AGRI
     V* 07/04/09  V2R3    PC Altezza cella Gantt
     V* 23/04/09  V2R3    BS Aggiunto attributo £JAXKMO nella routine £JAX_AKEY
     V* 21/09/09  V2R3    RM Attributo Mode su <Oggetto
     V* 31/03/10  V2R3    BMA Sostituito %TRIM con %TRIMR per £JAXCFI
     V* 11/06/10  V2R3    BS Gestione Campo Formula
     V* 16/11/10  V3R1    GR Valore Ssc per variabili di sottoscheda
     V* 16/02/11  V3R1    CM Aggiunto tipo messaggio Q (QUEST), in questi casi non verrà
     V*                      ritornato errore se lo stato e minore o uguale a 30
     V*                      Nella gestione del messaggio QUEST, è stata aggiunta la possibilità
     V*                      di passare una funzione ed il proprio testo, questa possibilità non
     V*                      è gestibile nei messaggi gestiti nel richiamo finale
     V* 15/03/11  V3R1    AS Gestione attributo £JaxRowHt (altezza di riga)
     V* 22/03/11  V3R1    AS Corretta gestione attributo £JaxRowHt (altezza di riga)
     V* 15/03/12  V3R2    BS In Modalità Costruzione Albero PopUP Aggiunta Forzatura su Ordinamento
     V* 15/03/12  V3R2    BS Se Settaggio JaxNS='2' (per G99) non esegue chiusura dei nodi
     V* 20/03/12  V3R2    BS Introdotta Variabile £JaxpICo
     V* 21/05/12  V3R2   BMA Gestito attributo Grp nella colonna di matrice
     V* 04/07/12  V3R2    BS Implementato Attributo Stato sull'Albero (Forza Espanso/Collassato)
     V* 19/09/12  V3R2    BS Aggiunto Attributo Style su DSOgg
     V* 10/05/13  V3R2    BS Aggiunto Richiamo K04
     V* A£30417A  V3R2    MF Gestione multilingua - Inserimento Commenti
     V* 29/05/13  V3R2    BS Rilascio modifica del 10/05/13
     V* A£30417A  V3R2    MF Gestione multilingua - Inserimento Commenti
     V* 30/05/12  V3R2   BMA Gestite schiera £JAXSW2 e DS £JAXDSCO2 per desc. aggiuntiva (tooltip)
     V* 05/06/13  V3R2    BS Asteriscate clear schiere messaggi/variabili
     V* 21/06/13  V3R2    BS Sostituzione Caratteri speciali per £JaxOP e £JaxFL
     V* 24/06/13  V3R2    BS Rettifica alla modifica precedente
     V* 18/07/14  V4R1    BS Aggiunto messaggio di raggiungimento lunghezza massima popup
     V* 09/12/14  V4R1    CM Durante la costruzione della colonna se il tetso è vuoto assume il
     V*                      testo esteso JAXCTXE
     V* 29/09/15         BMA Gestite schiera £JAXSW3 e DS £JAXDSCO3 per componente grafico
     V* 02/10/15         BMA Evito di aggiungere proprietà vuote alla griglia
     V* 04/02/16  V4R1   BMA In £JAX_BMES corretta gestione overflow schiera £JaxMBF
     V* 06/04/16  V4R1   BMA Rilasciate modifiche di settembre e ottobre 2015
     V* 30/06/16  V4R1   BMA Aggiunti T1/P1/K1 a messaggi
     V* 08/07/16  V4R1   BMA Aggiunti T2/P2/K2 a messaggi
     V* 14/09/16  V5R1   BMA Gestiti 3 tasti funzionali/funzioni in £JAX_AMES
     V* 07/06/17  V5R1   BMA Gestita lunghezza massima
     V* 16/06/17         BMA Gestita paginazione £JAX_ARIG, aggiunta £JAX_ARIG_L (che usa £JAXCL
     V*                      invece di £JAXCP) per aggiungere righe lunghe fino a 35000
     V* 19/06/17  V5R1   BMA Rilasciata modifica precedente
     V* 27/06/17  V5R1   BMA Apertura aggiunta riga lunga con £JAX_ARIG_LI
     V*                      e chiusura con £JAX_ARIG_LF. Portato £JAXCL a 40000
     V* B£70329A  V5R1    CM Sostituito OJ*USRPRF con UT
     V* 13/10/17  V5R1    BS Risalita a forma grafica oggetto
     V* 17/10/17  V5R1    BP Aggiunto gestione Setup Bottoni
     V* 17/10/17  V5R1    BS Aggiunte variabili funzione di controllo a £JAXDSCO3
     V* 06/12/17  V5R1   BMA Aumentato £JaxOP a 25000 e £JAXOP_LEN a 5 0
     V* 17/10/17  V5R1    BS Aggiunte variabili funzione di controllo a £JAXDSCO3
     V* 13/12/17  V5R1    BS Aggiunta variabile funzione verifica di controllo a £JAXDSCO3
     V* 12/04/18  V5R1   BMA Confronto con *HI con variabile trimmata
     V* JA80807A  V5R1   BMA Revisione gestione messaggi
     V* 09/08/18  V5R1   BMA Forzature temporanee
     V* 04/09/18  V5R1   BMA Forzatura Mode PM per tipo QUEST e CONF
     V* 17/09/18  V5R1   BMA Tolte forzature temporanee
     V* 03/10/18  V5R1   BMA Correzione su esito errore
     V* 06/11/18  000200 PEDSTE Forzatura Mode PM per tipo QUEST e CONF se non è PT
     V* 08/11/18  000200  BS Check-out 000200
     V* 16/12/19  000200 PEDSTE Check-out 000200 in SMEDEV
     V* ==============================================================
     D*-------------------------------------------------------------------
     D* OBIETTIVO
     D*
     D* Esternizzare le funzioni di prepazazione stringa XML orientato
     D* all'oggetto.
     C*----------------------------------------------------------------
      /COPY QILEGEN,£K04
     C*----------------------------------------------------------------
    RD* Aggiungo al buffer l'oggetto
     C*----------------------------------------------------------------
     C     £JAX_ADDO     BEGSR
      *
      * Ritorno a G99
1    C                   IF        £JaxNS='2'
     C                   ADD       1             £JaxPopN
     C                   CLEAR                   £JaxDSPop
     C                   EVAL      £JaxPOrd=£JaxK1
     C                   EVAL      £JaxPCod=£JaxK1
     C                   EVAL      £JaxPDes=£JaxD1
     C                   EVAL      £JaxPFun=£JaxOP
2    C                   SELECT
2x   C                   WHEN      £JAXEC='EXP'
     C                   EVAL      £JaxPFog='E'
2x   C                   WHEN      £JAXEC='COL'
     C                   EVAL      £JaxPFog='C'
2x   C                   WHEN      £JaxLF='Yes'                                 COSTANTE
     C                   EVAL      £JaxPFog='1'
2e   C                   ENDSL
     C                   EVAL      £JaxPIco=£JaxIc
     C                   EVAL      £JaxPRic=£JaxRI
     C                   EVAL      £JaxBF=£JaxBF+£JaxDSPop
     C                   EXSR      £JAX_IMPO
2    C                   IF        £JaxPopN=58
     C                   EVAL      £UIBCM='POPNXT'
2e   C                   ENDIF
     C                   GOTO      G9JAXADDO
1e   C                   ENDIF
      * Analisi delle informazioni
1    C                   IF        £JaxD1='' and £JaxT1<>*BLANKS
     C                   EVAL      £DECTP=£JaxT1
     C                   EVAL      £DECPA=£JaxP1
     C                   EVAL      £DECCD=£JaxK1
     C                   EXSR      £DEC
     C  N35              EVAL      £JaxD1=£DECDE
1e   C                   ENDIF
      * Costruzione
     C                   EVAL      £JaxCP=
     C                             '<Oggetto '+
     C                             'Nome="'+%TRIM(£JaxNM)+'" '+
     C                             'Tipo="'+%TRIM(£JaxT1)+'" '+
     C                             'Parametro="'+%TRIM(£JaxP1)+'" '+
     C                             'Codice="'+%TRIMR(£JaxK1)+'" '+
     C                             'Testo="'+%TRIM(£JaxD1)+'"'
      *
1    C                   SELECT
1x   C                   WHEN      £JaxOP<>*BLANKS AND
     C                             %LEN(%TRIM(£JaxOP))>4 AND
     C                             %SUBST(%TRIM(£JaxOP):1:4)='Exec'             COSTANTE
      *C                   CLEAR                   £JAXOP_LEN        4 0
     C                   CLEAR                   £JAXOP_LEN        5 0
     C                   EVAL      £JAXOP_LEN=%LEN(%TRIMR(£JAXOP))
      *
2    C                   IF        £JAXOP_LEN>6
3    C                   IF        %SUBST(£JAXOP:£JAXOP_LEN:1)='"' AND
     C                             %SUBST(£JAXOP:1:6)='Exec="'
     C                   EVAL      £JAXOP='Exec="'
     C                             +        %SUBST(£JAXOP:7:£JAXOP_LEN-7) +'"'
3e   C                   ENDIF
2e   C                   ENDIF
      *
     C                   EVAL      £JaxCP=%TRIM(£JaxCP)+' '+
     C                             %TRIM(£JaxOP)
1x   C                   WHEN      £JaxOP<>*BLANKS
     C                   EVAL      £JaxCP=%TRIM(£JaxCP)+' '+
     C                             'Exec="'+%TRIM(        £JaxOP) +'"'          COSTANTE
1e   C                   ENDSL
      * Modo esecuzione
1    C                   IF        £JaxMD<>*BLANKS
     C                   EVAL      £JaxCP=%TRIM(£JaxCP)+' '+
     C                             'Mode="'+%TRIM(£JaxMD)+'"'
1e   C                   ENDIF
      * Chiusura automatica
1    C                   IF        £JaxEN=*BLANKS
     C                   EVAL      £JaxEN='/>'
1e   C                   ENDIF
      * Gruppo di bottoni
1    C                   IF        £JaxGP<>*BLANKS
     C                   EVAL      £JaxCP=%TRIM(£JaxCP)+' '+
     C                             'Grp="'+%TRIM(£JaxGP)+'"'
1e   C                   ENDIF
      * Mostra Icona (per bottoni)
1    C                   IF        £JaxSI<>*BLANKS
     C                   EVAL      £JaxCP=%TRIM(£JaxCP)+' '+
     C                             'ShowIcon="'+%TRIM(£JaxSI)+'"'
1e   C                   ENDIF
      * Mostra Testo (per bottoni)
1    C                   IF        £JaxST<>*BLANKS
     C                   EVAL      £JaxCP=%TRIM(£JaxCP)+' '+
     C                             'ShowText="'+%TRIM(£JaxST)+'"'
1e   C                   ENDIF
      * Stato (EXP=Forza espanso, COL=Forza collassato)
1    C                   IF        £JaxEC<>*BLANKS
     C                   EVAL      £JaxCP=%TRIM(£JaxCP)+' '+
     C                             'Stato="'+%TRIM(£JaxEC)+'"'
1e   C                   ENDIF
      * Style
1    C                   IF        £JaxSY<>*BLANKS
     C                   EVAL      £JaxCP=%TRIM(£JaxCP)+' '+
     C                             'Style="'+%TRIM(£JaxSY)+'"'
1e   C                   ENDIF
      * Setup Bottoni
1    C                   IF        £JaxSB<>*BLANKS
     C                   EVAL      £JaxCP=%TRIM(£JaxCP)+' '+
     C                             'BtnSet="'+%TRIM(£JaxSB)+'"'
1e   C                   ENDIF
      *
      *
     C                   EVAL      £JaxCP=%TRIM(£JaxCP)+' '+
     C                             %TRIM(£JaxWK)+' '+
     C                             %TRIM(£JaxGR)+' '+
     C                             %TRIM(£JaxIC)+' '+
     C                             'Fld="'+%TRIM(£JaxFL)+'" '+
     C                             'Leaf="'+%TRIM(£JaxLF)+'"'+
     C                             %TRIM(£JaxEN)
      * Pulisco oggetto
     C                   EXSR      £JAX_IMPO
      * In buffer
     C                   EXSR      £JAX_ADD
      *
     C     G9JAXADDO     ENDSR
     C*----------------------------------------------------------------
    RD* Chiudo oggetto
     C*----------------------------------------------------------------
     C     £JAX_CLOO     BEGSR
      *
1    C                   IF        £JAXNS='2'
     C                   LEAVESR
1e   C                   ENDIF
      *
     C                   EVAL      £JaxCP='</Oggetto>'
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Aggiungo al buffer un elemento di semaforo o gauge
     C*----------------------------------------------------------------
     C     £JAX_ADDE     BEGSR
      *
      * Costruzione
     C                   EVAL      £JaxCP=
     C                             '<Elemento '+
     C                             'Valore="'+%TRIM(£JaxVA)+'" '+
     C                             'Soglia1="'+%TRIM(£JaxS1)+'" '+
     C                             'Soglia2="'+%TRIM(£JaxS2)+'" '
      * Minimo (serve solo per gauge)
1    C                   IF        £JaxMn<>*BLANKS
     C                   EVAL      £JaxCP=%TRIM(£JaxCP)+' '+
     C                             'Min="'+%TRIM(£JaxMN)+'" '
1e   C                   ENDIF
      * Massimo (serve solo per gauge)
1    C                   IF        £JaxMX<>*BLANKS
     C                   EVAL      £JaxCP=%TRIM(£JaxCP)+' '+
     C                             'Max="'+%TRIM(£JaxMX)+'" '
1e   C                   ENDIF
      * Inversione (serve solo per gauge)
1    C                   IF        £JaxIN<>*BLANKS
     C                   EVAL      £JaxCP=%TRIM(£JaxCP)+' '+
     C                             'Inv="Yes"'
1e   C                   ENDIF
     C                   EVAL      £JaxCP=%TRIM(£JaxCP)+' '+'/>'
      * Pulisco elemento
     C                   EXSR      £JAX_IMPO
      * In buffer
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Impostazioni iniziali
     C*----------------------------------------------------------------
     C     £JAX_IMPO     BEGSR
     C
     C                   CLEAR                   £JaxDSOgg
     C                   CLEAR                   £JaxDSEle
     C                   CLEAR                   £JAX_$CO          3 0
     C
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Inizializzazione griglia
     C*----------------------------------------------------------------
     C     £JAX_AGRI_I   BEGSR
      *
     C                   EVAL      £JAXCP='<Griglia>'
     C                   EXSR      £JAX_ADD
     C                   CLEAR                   £JAXCP
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Aggiungo griglia
     C*----------------------------------------------------------------
     C     £JAX_AGRI     BEGSR
      *
     C                   EXSR      £JAX_AGRI_I
      *
     C                   CLEAR                   £JAX_NMAX         5 0
     C                   EVAL      £JAX_NMAX=%ELEM(£JAXSWK)
     C                   CLEAR                   £JAX2NFUL         5 0
1    C                   DO        £JAX_NMAX     £JAX_N50          5 0
2    C                   IF        £JAXSW2Key(£JAX_N50)<>''
     C                   EVAL      £JAX2NFUL=£JAX2NFUL+1
2e   C                   ENDIF
1e   C                   ENDDO
     C                   CLEAR                   £JAX3NFUL         5 0
1    C                   DO        £JAX_NMAX     £JAX_N50          5 0
2    C                   IF        £JAXSW3Key(£JAX_N50)<>''
     C                   EVAL      £JAX3NFUL=£JAX3NFUL+1
2e   C                   ENDIF
1e   C                   ENDDO
1     *C                   DO        *HIVAL        £JAX_N50          5 0
1    C                   DO        £JAX_NMAX     £JAX_N50          5 0
2    C                   IF        £JAXSWK(£JAX_N50)=*BLANKS
     C                   LEAVE
2e   C                   ENDIF
      * Eventali XML inclusi
2    C                   IF        %SUBST(£JAXSWK(£JAX_N50):01:01)='.'
     C                   EVAL      £JAXCP=%TRIM(%SUBST(£JAXSWK(£JAX_N50):02))
     C                   EXSR      £JAX_ADD
2x   C                   ELSE
3    C                   IF        £JAX_N50<>1
     C                   EVAL      £JAXCP='</Colonna>'
     C                   EXSR      £JAX_ADD
3e   C                   ENDIF
     C                   EVAL      £JAXDSCOL=£JAXSWK(£JAX_N50)
     C                   CLEAR                   £JAX2N50          5 0
3    C                   IF        £JAXCCD<>'' AND £JAX2NFUL>0
     C                   EVAL      £JAX2N50=%LOOKUP(£JAXCCD:£JAXSW2Key:1)
3e   C                   ENDIF
3    C                   IF        £JAX2N50>0
     C                   EVAL      £JAXDSCO2=£JAXSW2(£JAX2N50)
     C                   EVAL      £JAX2NFUL=£JAX2NFUL-1
3x   C                   ELSE
     C                   CLEAR                   £JAXDSCO2
3e   C                   ENDIF
     C                   CLEAR                   £JAX3N50          5 0
3    C                   IF        £JAXCCD<>'' AND £JAX3NFUL>0
     C                   EVAL      £JAX3N50=%LOOKUP(£JAXCCD:£JAXSW3Key:1)
3e   C                   ENDIF
3    C                   IF        £JAX3N50>0
     C                   EVAL      £JAXDSCO3=£JAXSW3(£JAX3N50)
     C                   EVAL      £JAX3NFUL=£JAX3NFUL-1
3x   C                   ELSE
     C                   CLEAR                   £JAXDSCO3
3e   C                   ENDIF
      * Segnala di lasciare aperta la colonna
     C                   MOVEL     '1'           £JAXAC
     C                   EXSR      £JAX_ACOL
2e   C                   ENDIF
1e   C                   ENDDO
      *
1    C                   IF        £JAX_N50<>1
     C                   EVAL      £JAXCP='</Colonna>'
     C                   EXSR      £JAX_ADD
1e   C                   ENDIF
      *
     C                   EXSR      £JAX_AGRI_F
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Finalizzazione Griglia
     C*----------------------------------------------------------------
     C     £JAX_AGRI_F   BEGSR
      *
     C                   EVAL      £JAXCP='</Griglia>'
     C                   EXSR      £JAX_ADD
     C                   CLEAR                   £JAXCP
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Aggiungo colonna
     C*----------------------------------------------------------------
     C     £JAX_ACOL     BEGSR
      *
     C                   ADD       1             £JAX_$CO
      *
3    C******             IF        £JAXCAL<>' '
     C******             EVAL      £JAX_A50='Als="Decodifica"'
3x   C******             ELSE
     C******             CLEAR                   £JAX_A50         50
3e   C******             ENDIF
      *
1    C                   IF        £JAXCIO<>''
     C                   EVAL      £JAX_A1=£JAXCIO
1x   C                   ELSE
     C                   MOVEL     'O'           £JAX_A1           1
1e   C                   ENDIF
      * Se previsto campo in input panel risalgo a comportamento standard
     C*******            IF        £JAX_A1='B' AND £UIBPG='INP'
1    C                   IF        £UIBPG='INP'
     C                   EVAL      £K04FU='DAT'
     C                   EVAL      £K04ME=' '
     C                   EVAL      £K04I_OG=£JAXCOG
     C                   EXSR      £K04
2    C                   IF        £JAX_A1='B'
     C                   EVAL      £JAX_A1=£K04O_IP
2e   C                   ENDIF
1e   C                   ENDIF
      * Se ho voluto forzare di non utilizzare le combo ripristino la modalità B
1    C                   IF        £JAX_A1='b'
     C                   EVAL      £JAX_A1='B'
1e   C                   ENDIF
      *
     C                   EVAL      £JAXCP='<Colonna'
      *
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' Cod="'
     C                             +%TRIM(£JAXCCD)
     C                             +'"'
      *
1    C                   IF        £JAXCTX = '' AND £JAXCTXE <> ''
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' Txt="'
     C                             +%TRIM(£JAXCTXE)
     C                             +'"'
     C                   EVAL      £JAXCTXE=''
1x   C                   ELSE
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' Txt="'
     C                             +%TRIM(£JAXCTX)
     C                             +'"'
1e   C                   ENDIF
      * Formule
1    C                   IF        £JAXCAL='F'
2    C                   IF        £JAXCFO<>' '
     C                   EVAL      £JAXCP=£JAXCP+' Formula="'
     C                             +%TRIMR(£JAXCFO)+'"'
2x   C                   ELSE
     C                   EVAL      £JAXCP=£JAXCP+' Formula="'
     C                             +%TRIMR(£JAXCFI+£JAXCTP)+'"'
     C                   CLEAR                   £JAXCFI
     C                   CLEAR                   £JAXCTP
2e   C                   ENDIF
     C                   CLEAR                   £JAXCAL
     C                   CLEAR                   £JAXCFO
1e   C                   ENDIF
      * Esteso
1    C                   IF        £JAXCAL='E' AND £JAXCFI+£JAXCTP <> ''
     C                   EVAL      £JAXCP=£JAXCP+' Extended="'
     C                             +%TRIMR(£JAXCFI+£JAXCTP)+'"'
     C                   CLEAR                   £JAXCFI
     C                   CLEAR                   £JAXCTP
1e   C                   ENDIF
      * Grafica
1    C                   IF        £JAXCAL='G' AND £JAXCFI+£JAXCTP <> ''
      * . mettere GLUN(nn) in £JAXCFI+£JAXCTP per sovrascrivere la larghezza del campo dell'input
      * . panel in caratteri (Lunghezza grafica)
     C                   EVAL      £JAXCP=£JAXCP+' Grp="'
     C                             +%TRIMR(£JAXCFI+£JAXCTP)+'"'
     C                   CLEAR                   £JAXCFI
     C                   CLEAR                   £JAXCTP
1e   C                   ENDIF
      *
1    C                   IF        £JAXCTP<>''
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' Tip="'
     C                             +%TRIM(£JAXCTP)
     C                             +'"'
1e   C                   ENDIF
      * Se *HI imposto 30000
1    C                   IF        %trim(£JAXCLU)=£JaxMaxStr
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' Lun="'+£JaxMaxLen
     C                             +'"'
1x   C                   ELSE
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' Lun="'
     C                             +%TRIM(£JAXCLU)
     C                             +'"'
1e   C                   ENDIF
      *
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' IO="'
     C                             +£JAX_A1
     C                             +'"'
      *
1    C                   IF        £JAXCOG<>''
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' Ogg="'
     C                             +%TRIMR(£JAXCOG)
     C                             +'"'
1e   C                   ENDIF
      *
1    C                   IF        £JAXCDY<>''
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' Dpy="'
     C                             +%TRIM(£JAXCDY )
     C                             +'"'
1e   C                   ENDIF
      * Altezza cella Gantt
1    C                   IF        £JAXCAL='H'
     C                   EVAL      £JAXCP=£JAXCP+' Fill="'
     C                             +%TRIMR(£JAXCFI+£JAXCTP+£JAXCLA)+'"'
     C                   CLEAR                   £JAXCAL
     C                   CLEAR                   £JAXCFI
     C                   CLEAR                   £JAXCTP
     C                   CLEAR                   £JAXCLA
1x   C                   ELSE
      *
2    C                   IF        £JAXCFI<>''
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' Fill="'
     C                             +%TRIMR(£JAXCFI)
     C                             +'"'
     C******                       +' '+%TRIM(£JAX_A50)
2e   C                   ENDIF
1e   C                   ENDIF
      *
1    C                   IF        £JAXCLA<>''
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' Aut="'
     C                             +%TRIM(£JAXCLA)
     C                             +'"'
1e   C                   ENDIF
      * Campi aggiuntivi
1    C                   IF        £JAX2ET<>''
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' ETxt="'
     C                             +%TRIM(£JAX2ET)
     C                             +'"'
1e   C                   ENDIF
      *
1    C                   IF        £JAX3CO<>''
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' Cmp="'
     C                             +%TRIM(£JAX3CO)
     C                             +'"'
1e   C                   ENDIF
      *
1    C                   IF        £JAX3EC<>''
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' Ext="'
     C                             +%TRIM(£JAX3EC)
     C                             +'"'
1e   C                   ENDIF
      *
1    C                   IF        £UIBPG='INP' AND £JAX3CO=''
     C                             AND £JAX3CD=''
2    C                   SELECT
2x   C                   WHEN      £JAX_A1<>'O' AND £JAX_A1<>'H' AND
     C                             £K04O_FG<>''
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' Cmp="'
     C                             +%TRIM(£K04O_FG)
     C                             +'"'
3    C                   IF        £K04O_PG<>''
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' Ext="'
     C                             +%TRIM(£K04O_PG)
     C                             +'"'
3e   C                   ENDIF
2x   C                   WHEN      £JAX_A1='O' AND
     C                             £K04O_FO<>''
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' Cmp="'
     C                             +%TRIM(£K04O_FO)
     C                             +'"'
3    C                   IF        £K04O_PO<>''
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' Ext="'
     C                             +%TRIM(£K04O_PO)
     C                             +'"'
3e   C                   ENDIF
2e   C                   ENDSL
1e   C                   ENDIF
      *
1    C                   IF        £JAX3TK<>''
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' TfK="'
     C                             +%TRIM(£JAX3TK)
     C                             +'"'
2    C                   IF        £JAX3PK<>''
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' PfK="'
     C                             +%TRIM(£JAX3PK)
     C                             +'"'
2e   C                   ENDIF
2    C                   IF        £JAX3SK<>''
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' SfK="'
     C                             +%TRIM(£JAX3SK)
     C                             +'"'
2e   C                   ENDIF
1e   C                   ENDIF
      *
1    C                   SELECT
1x   C                   WHEN      £JAXAC<>'' AND £JAXCAL<>'C'
     C                   EVAL      £JAXCP=£JAXCP+'>'
1x   C                   WHEN      £JAXAC='' AND £JAXCAL<>'C'
     C                   EVAL      £JAXCP=£JAXCP+'/>'
1x   C                   WHEN      £JAXCAL='C'
     C                   EVAL      £JAXCP=£JAXCP+'>'
     C                   EVAL      £JAXCP=£JAXCP+' '
     C                             +'<Relazioni Tabella="'
     C                             +%TRIM(%EDITW(£JAX_$CO:'0   '))+' '
     C                             +%TRIM(£JAXCTX)
     C                             +'"> '
     C                             +'<Relazione Campo="C'
     C                             +%TRIM(%EDITW(£JAX_$CO:'0   '))
     C                             +'" Valore="['
     C                             +%TRIM(£JAXCCD)+']"/> '
     C                             +'<Relazione Campo="T'
     C                             +%TRIM(%EDITW(£JAX_$CO:'0   '))
     C                             +'" Valore="'+%TRIM(£JAXCOG)
     C                             +'"/></Relazioni>'
     C                             +' </Colonna>'
1e   C                   ENDSL
      * Sblanka il flag di 'non chiudere la colonna'
     C                   MOVEL     *BLANKS       £JAXAC            1
      *
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Inizializzazione scrittura delle righe
     C*----------------------------------------------------------------
     C     £JAX_ARIG_I   BEGSR
      *
     C                   EVAL      £JAXCP='<Righe>'
     C                   EXSR      £JAX_ADD
     C                   CLEAR                   £JAXCP
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Aggiungo riga
     C*----------------------------------------------------------------
     C     £JAX_ARIG     BEGSR
      *
      * Nome tag ed eventuale tipo par
1    C                   IF        £JAXT1<>*BLANKS
     C                   EVAL      £JAXWS='<Riga '
     C                             +'Tipo="'+%TRIM(£JaxT1)+'" '
     C                             +'Parametro="'+%TRIM(£JaxP1)+'" '
     C                             +'Codice="'+%TRIMR(£JaxK1)+'" '
     C                             +'Testo="'+%TRIM(£JaxD1)+'" '
     C                             +'Fld="'+£JAXCP +'"'
1x   C                   ELSE
     C                   EVAL      £JAXWS='<Riga '
     C                             +'Fld="'+£JAXCP+'"'
1e   C                   ENDIF
      * Eventuale altezza di riga
1    C                   IF        £JaxRowHt<>*BLANKS
     C                   EVAL      £JAXWS=%TRIM(£JAXWS)
     C                             +' RowHeight="'+%TRIM(£JaxRowHt)+'"'
     C                   CLEAR                   £JaxRowHt
1e   C                   ENDIF
      *
     C                   EVAL      £JAXWS=%TRIM(£JAXWS)+'/>'
      *
1    C                   IF        %LEN(£JAXWS)>30000
     C                   EVAL      £JAXCP=%SUBST(£JAXWS:1:30000)
     C                   EXSR      £JAX_ADD
     C                   EVAL      £JAXCP=%SUBST(£JAXWS:30001)
     C                   EXSR      £JAX_ADD
2    C                   IF        %LEN(£JAXWS)>60000
     C                   EVAL      £JAXCP=%SUBST(£JAXWS:60001)
     C                   EXSR      £JAX_ADD
2e   C                   ENDIF
1x   C                   ELSE
     C                   EVAL      £JAXCP=£JAXWS
     C                   EXSR      £JAX_ADD
1e   C                   ENDIF
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Aggiungo riga lunga (Inizio)
     C*----------------------------------------------------------------
     C     £JAX_ARIG_LI  BEGSR
      *
      * Nome tag ed eventuale tipo par
1    C                   IF        £JAXT1<>*BLANKS
     C                   EVAL      £JAXCP='<Riga '
     C                             +'Tipo="'+%TRIM(£JaxT1)+'" '
     C                             +'Parametro="'+%TRIM(£JaxP1)+'" '
     C                             +'Codice="'+%TRIMR(£JaxK1)+'" '
     C                             +'Testo="'+%TRIM(£JaxD1)+'" '
     C                             +'Fld="'
1x   C                   ELSE
     C                   EVAL      £JAXCP='<Riga '
     C                             +'Fld="'
1e   C                   ENDIF
      *
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Aggiungo riga lunga (Fine)
     C*----------------------------------------------------------------
     C     £JAX_ARIG_LF  BEGSR
      *
      * Chiudo Fld="
     C                   EVAL      £JAXCP='"'
      * Eventuale altezza di riga
1    C                   IF        £JaxRowHt<>*BLANKS
     C                   EVAL      £JAXCP=%TRIM(£JAXCP)
     C                             +' RowHeight="'+%TRIM(£JaxRowHt)+'"'
     C                   CLEAR                   £JaxRowHt
1e   C                   ENDIF
      *
     C                   EVAL      £JAXCP=%TRIM(£JAXCP)+'/>'
      *
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Aggiungo riga lunga (contenuto della riga in £JAXCL invece che in £JAXCP)
     C*----------------------------------------------------------------
     C     £JAX_ARIG_L   BEGSR
      *
     C                   EVAL      £JAXWS=£JAXCL
      *
1    C                   IF        %LEN(£JAXWS)>30000
     C                   EVAL      £JAXCP=%SUBST(£JAXWS:1:30000)
     C                   EXSR      £JAX_ADD
     C                   EVAL      £JAXCP=%SUBST(£JAXWS:30001)
     C                   EXSR      £JAX_ADD
2    C                   IF        %LEN(£JAXWS)>60000
     C                   EVAL      £JAXCP=%SUBST(£JAXWS:60001)
     C                   EXSR      £JAX_ADD
2e   C                   ENDIF
1x   C                   ELSE
     C                   EVAL      £JAXCP=£JAXWS
     C                   EXSR      £JAX_ADD
1e   C                   ENDIF
     C                   EVAL      £JAXCL=''
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Finalizzazione scrittura delle righe
     C*----------------------------------------------------------------
     C     £JAX_ARIG_F   BEGSR
      *
     C                   EVAL      £JaxCP='</Righe>'
     C                   EXSR      £JAX_ADD
     C                   CLEAR                   £JAXCP
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Inizializzazione Buffer messaggi
     C*----------------------------------------------------------------
     C     £JAX_BMES_I   BEGSR
      *
     C                   Z-ADD     0             £JaxMBfI
     C******             CLEAR                   £JaxMBf
     C                   CLEAR                   £JaxDSMsg
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Aggiungo a Buffer messaggi
     C*----------------------------------------------------------------
     C     £JAX_BMES     BEGSR
      *
1     *C                   IF        £JaxMBfI<200
1    C                   IF        £JaxMBfI<%ELEM(£JaxMBF)
     C                   EVAL      £JaxMBfI=£JaxMBfI+1
      * Scrittura diretta, mantenuta per compatibilità
2    C                   IF        £JaxDSMsg=*BLANKS
     C                   EVAL      £JaxMLiv=%SUBST(£JaxCP:1:2)
     C                   EVAL      £JaxMTxt=%SUBST(£JaxCP:3)
2e   C                   ENDIF
     C                   EVAL      £JaxMBF(£JaxMBfI)=£JaxDSMsg
     C                   CLEAR                   £JaxDSMsg
1e   C                   ENDIF
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Svuoto Buffer messaggi
     C*----------------------------------------------------------------
     C     £JAX_BMES_F   BEGSR
      *
1    C                   IF        £JaxMBFI>0
     C                   EXSR      £JAX_AMES_I
2    C                   DO        £JaxMBFI      £JAX_N50
     C                   EVAL      £JaxDSMsg=£JaxMBf(£JAX_N50)
     C                   EXSR      £JAX_AMES
2e   C                   ENDDO
     C                   EXSR      £JAX_AMES_F
1e   C                   ENDIF
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Inizializzazione scrittura messaggi
     C*----------------------------------------------------------------
     C     £JAX_AMES_I   BEGSR
      *
     C                   CLEAR                   £JAX_MXT          1
     C                   CLEAR                   £JAX_MX           2
      * Se a '1' lascia aperto il nodo
     C                   CLEAR                   £JaxMOpn          1
      * Funzione su messaggio QUEST
     C                   CLEAR                   £JaxMKFun        10
     C                   CLEAR                   £JaxMTFun        30
     C                   CLEAR                   £JaxMFun        512
     C                   CLEAR                   £JaxMKFu2        10
     C                   CLEAR                   £JaxMTFu2        30
     C                   CLEAR                   £JaxMFu2        512
     C                   CLEAR                   £JaxMKFu3        10
     C                   CLEAR                   £JaxMTFu3        30
     C                   CLEAR                   £JaxMFu3        512
     C                   CLEAR                   £JaxMTx2
      *
     C                   EVAL      £JAXCP='<Messaggi>'
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Aggiungo messaggio
     C*----------------------------------------------------------------
     C     £JAX_AMES     BEGSR
      *
      * Compatibilità con vecchia chiamata diretta
1    C                   IF        £JaxDSMsg=*BLANKS
     C                   EVAL      £JaxMLiv=£JaxLV
     C                   EVAL      £JaxMTxt=£JaxCP
1e   C                   ENDIF
1    C                   IF        £JaxDSMsg<>*BLANKS
      * Imposto il tipo di messaggio
     C                   CLEAR                   £JaxMsgType       5
2    C                   SELECT
      * . I=Messaggio informativo
2x   C                   WHEN      £JaxMTyp='I'
     C                   EVAL      £JaxMsgType='INFO'
      * . C=Messaggio con richiesta di conferma
2x   C                   WHEN      £JaxMTyp='C'
     C                   EVAL      £JaxMsgType='CONF'
      * . Q=Messaggio con richiesta di conferma con funzioni associate
2x   C                   WHEN      £JaxMTyp='Q'
     C                   EVAL      £JaxMsgType='QUEST'
     C                   EVAL      £Jax_MXT='Q'
3    C                   IF        £JaxMFun <> '' AND £JaxMOpn=''
     C                   EVAL      £JaxMOpn='Q'
3e   C                   ENDIF
      * . N=Notifica con esecuzione di funzione
2x   C                   WHEN      £JaxMTyp='N'
     C                   EVAL      £JaxMsgType='NOTIF'
     C                   EVAL      £Jax_MXT='Q'
3    C                   IF        £JaxMFun <> '' AND £JaxMOpn=''
     C                   EVAL      £JaxMOpn='Q'
3e   C                   ENDIF
      * . Default: Messaggio informnativo
2x   C                   OTHER
     C                   EVAL      £JaxMsgType='INFO'
2e   C                   ENDSL
      * Compatibilità con utilizzo livello (invece che gravità e modalità)
2    C                   SELECT
2x   C                   WHEN      £JaxMLiv<>'' AND  £JaxMGra=''
3    C                   IF        £JaxMLiv>£JAX_MX
     C                   EVAL      £JAX_MX=£JaxMLiv
3e   C                   ENDIF
      * retrocompatibilità loocup: > 20 WARNING, > 60 ERROR, resto INFO
3    C                   SELECT
3x   C                   WHEN      £JaxMLiv>'60'
     C                   EVAL      £JaxMGra=£Jax_GraErr
3x   C                   WHEN      £JaxMLiv>'20'
     C                   EVAL      £JaxMGra=£Jax_GraWrn
3x   C                   OTHER
     C                   EVAL      £JaxMGra=£Jax_GraInf
3e   C                   ENDSL
2x   C                   WHEN      £JaxMGra<>''
3    C                   SELECT
3x   C                   WHEN      £JaxMGra=£Jax_GraErr
      * Forzo il livello solo se errore se gestione nuova (serve per esito)
4    C                   IF        £Jax_LvlErr>£JAX_MX
     C                   EVAL      £JAX_MX=£Jax_LvlErr
4e   C                   ENDIF
3e   C                   ENDSL
2e   C                   ENDSL
2    C                   IF        £JaxMLiv<>'' AND  £JaxMMod=''
      * retrocompatibilità loocup: > 25 modale, 00 non visibile, resto notifica a scomparsa
3    C                   SELECT
3x   C                   WHEN      £JaxMLiv='00'
      * messaggio nascosto
     C                   EVAL      £JaxMMod=£Jax_ModHH
3x   C                   WHEN      £JaxMLiv>'25'
      * messaggio permanente (modale)
     C                   EVAL      £JaxMMod=£Jax_ModPM
3x   C                   WHEN      £JaxMLiv>'00'
      * notifica temporanea (a scomparsa)
     C                   EVAL      £JaxMMod=£Jax_ModTN
3e   C                   ENDSL
2e   C                   ENDIF
2    C                   IF        £JaxMGra=''
     C                   EVAL      £JaxMGra=£Jax_GraInf
2e   C                   ENDIF
2    C                   IF        £JaxMMod=''
      * messaggio nascosto
     C                   EVAL      £JaxMMod=£Jax_ModHH
2e   C                   ENDIF
      * Forzatura Mode PM per tipo QUEST e CONF (se devo cliccare su un pulsante è più sicuro
      * forzare un messaggio modale)
2    C                   IF        £JaxMsgType='QUEST' OR £JaxMsgType='CONF'
      * Forzatura Mode PM per tipo QUEST e CONF se non è PT
3    C                   IF        £JaxMMod<>'PT'
     C                   EVAL      £JaxMMod=£Jax_ModPM
3e   C                   ENDIF
2e   C                   ENDIF
      *
     C                   EVAL      £JAXCP='<Messaggio Testo="'
     C                             +%TRIM(£JaxMTxt )
     C                             +'" TestoCompleto="'
     C                             +%TRIMR(£JaxMTx2)
     C                             +'" Gravity="'+%TRIM(£JaxMGra)
     C                             +'" Mode="'+%TRIM(£JaxMMod)
     C                             +'" Tipo="'+%TRIM(£JaxMsgType)+'"'
2    C                   IF        £JaxMT1<>'' OR £JaxMP1<>'' OR £JaxMK1<>''
     C                   EVAL      £JAXCP=%TRIM(£JAXCP)
     C                             +' T1="'+%TRIMR(£JaxMT1)+'" '
     C                             +' P1="'+%TRIMR(£JaxMP1)+'" '
     C                             +' K1="'+%TRIMR(£JaxMK1)+'" '
2e   C                   ENDIF
2    C                   IF        £JaxMT2<>'' OR £JaxMP2<>'' OR £JaxMK2<>''
     C                   EVAL      £JAXCP=%TRIM(£JAXCP)
     C                             +' T2="'+%TRIMR(£JaxMT2)+'" '
     C                             +' P2="'+%TRIMR(£JaxMP2)+'" '
     C                             +' K2="'+%TRIMR(£JaxMK2)+'" '
2e   C                   ENDIF
2    C                   IF        £JaxMOpn=*BLANKS
     C                   EVAL      £JAXCP=%TRIM(£JAXCP)+'/>'
2x   C                   ELSE
     C                   EVAL      £JAXCP=%TRIM(£JAXCP)+'>'
2e   C                   ENDIF
     C                   EXSR      £JAX_ADD
      * . Default: Messaggio QUEST con funzione
2    C                   IF        £JaxMFun <> ''
     C                   EXSR      £JAX_APOP_I
     C                   EVAL      £JaxT1='J1'
     C                   EVAL      £JaxP1='KEY'
3    C                   IF        £JaxMKFun=''
     C                   EVAL      £JaxK1='*ENTER'
3x   C                   ELSE
     C                   EVAL      £JaxK1=£JaxMKFun
3e   C                   ENDIF
     C                   EVAL      £JaxD1=£JaxMTFun
     C                   EVAL      £JaxOP=£JaxMFun
     C                   EXSR      £JAX_APOP
3    C                   IF        £JaxMFu2 <> '' AND £JaxMKFu2 <> ''
     C                   EVAL      £JaxT1='J1'
     C                   EVAL      £JaxP1='KEY'
     C                   EVAL      £JaxK1=£JaxMKFu2
     C                   EVAL      £JaxD1=£JaxMTFu2
     C                   EVAL      £JaxOP=£JaxMFu2
     C                   EXSR      £JAX_APOP
3e   C                   ENDIF
3    C                   IF        £JaxMFu3 <> '' AND £JaxMKFu3 <> ''
     C                   EVAL      £JaxT1='J1'
     C                   EVAL      £JaxP1='KEY'
     C                   EVAL      £JaxK1=£JaxMKFu3
     C                   EVAL      £JaxD1=£JaxMTFu3
     C                   EVAL      £JaxOP=£JaxMFu3
     C                   EXSR      £JAX_APOP
3e   C                   ENDIF
     C                   EXSR      £JAX_APOP_F
2e   C                   ENDIF
     C                   EVAL      £JaxMFun=''
     C                   EVAL      £JaxMTFun=''
     C                   EVAL      £JaxMKFun=''
     C                   EVAL      £JaxMFu2=''
     C                   EVAL      £JaxMTFu2=''
     C                   EVAL      £JaxMKFu2=''
     C                   EVAL      £JaxMFu3=''
     C                   EVAL      £JaxMTFu3=''
     C                   EVAL      £JaxMKFu3=''
2    C                   IF        £JaxMOpn = 'Q'
     C                   EXSR      £JAX_CMES
2e   C                   ENDIF
     C                   CLEAR                   £JaxMOpn
      *
     C                   CLEAR                   £JaxDSMsg
     C                   CLEAR                   £JaxMTx2
1e   C                   ENDIF
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Chiudo nodo messaggio
     C*----------------------------------------------------------------
     C     £JAX_CMES     BEGSR
      *
     C                   EVAL      £JAXCP='</Messaggio>'
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Finalizzazione scrittura dei messaggi
     C*----------------------------------------------------------------
     C     £JAX_AMES_F   BEGSR
      *
1    C                   IF        £JAX_MX>'30' AND £JAX_MXT  = 'Q' OR
     C                             £JAX_MX>'10' AND £JAX_MXT <> 'Q'
     C                   EVAL      £JAXCP='<Esito Stato="ERRORE"/>'
     C                   EXSR      £JAX_ADD
1x   C                   ELSE
     C                   EVAL      £JAXCP='<Esito Stato="OK"/>'
     C                   EXSR      £JAX_ADD
1e   C                   ENDIF
      *
     C                   EVAL      £JaxCP='</Messaggi>'
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
      *----------------------------------------------------------------
    RD* Inizializzazione KEY
      *----------------------------------------------------------------
     C     £JAX_AKEY_I   BEGSR
      *
     C                   EVAL      £JaxKIn=0
     C                   EVAL      £JAXCP='<Key>'
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Aggiungo griglia
     C*----------------------------------------------------------------
     C     £JAX_AKEY     BEGSR
     C                   EVAL      £JaxKIn=£JaxKIN+1
      * Analisi delle informazioni
1    C                   IF        £JaxD1='' and £JaxT1<>*BLANKS
     C                   EVAL      £DECTP='OG'
     C                   EVAL      £DECCD=%TRIM(£JaxT1)+%TRIM(£JaxP1)
     C                   EXSR      £DEC
     C  N35              EVAL      £JaxD1=£DECDE
1e   C                   ENDIF
1    C                   IF        %TRIM(£JaxKId)=''
     C                   EVAL      £JaxKId='K'+%TRIM(%EDITC(£JaxKIn:'Z'))
1e   C                   ENDIF
      * Assumo oggetto automatico
1    C                   IF        £JaxKOb='3'
2    C                   SELECT
2x   C                   WHEN      £JaxKT1='OJ' AND £JaxKP1='*USRPRF'
     C                   EVAL      £JaxKK1=%TRIM(£PDSNU)
2x   C                   WHEN      £JaxKT1='UT'
     C                   EVAL      £JaxKK1=%TRIM(£PDSNU)
2x   C                   WHEN      £JaxKT1='TA' AND £JaxKP1='B£U'
     C                   EVAL      £JaxKK1=%TRIM(£PDSNU)
2x   C                   WHEN      £JaxKT1='UP'
     C                   EVAL      £JaxKK1=%TRIM(£PDSNU)
2x   C                   WHEN      £JaxKT1='OJ' AND £JaxKP1='*PGM'
     C                   EVAL      £JaxKK1=%TRIM(£PDSNP)
2x   C                   WHEN      £JaxKT1='V2' AND £JaxKP1='STAR'
     C                   EVAL      £JaxKK1='**'
2e   C                   ENDSL
1e   C                   ENDIF
      * Costruzione
     C                   EVAL      £JaxCP=
     C                             '<'+%TRIM(£JaxKId)+' '+
     C                             'Tipo="'+%TRIM(£JaxKT1)+'" '+
     C                             'Parametro="'+%TRIM(£JaxKP1)+'" '+
     C                             'Codice="'+%TRIMR(£JaxKK1)+'" '+
     C                             'Testo="'+%TRIM(£JaxKD1)+'" ' +
     C                             'Obb="'+%TRIM(£JaxKOb)+'" ' +
     C                             'Mod="'+%TRIM(£JaxKMO)+'" ' +
     C                             '/>'
      * Pulisco Key
     C                   CLEAR                   £JaxDSKey
      * In buffer
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Finalizzazione Griglia
     C*----------------------------------------------------------------
     C     £JAX_AKEY_F   BEGSR
      *
     C                   EVAL      £JAXCP='</Key>'
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
      *----------------------------------------------------------------
    RD* Inizializzazione POPUP
      *----------------------------------------------------------------
     C     £JAX_APOP_I   BEGSR
      *
     C                   EVAL      £JAXCP='<UIPopup>'
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
      *----------------------------------------------------------------
    RD* Inizializzazione POPUP
      *----------------------------------------------------------------
     C     £JAX_APOP     BEGSR
      *
      * Analisi delle informazioni
1    C                   IF        £JaxD1='' and £JaxT1<>*BLANKS
     C                   EVAL      £DECTP='OG'
     C                   EVAL      £DECCD=%TRIM(£JaxT1)+%TRIM(£JaxP1)
     C                   EXSR      £DEC
     C  N35              EVAL      £JaxD1=£DECDE
1e   C                   ENDIF
      * Scrivo la riga
     C                   EXSR      £JAX_ADDO
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Finalizzazione Griglia
     C*----------------------------------------------------------------
     C     £JAX_APOP_F   BEGSR
      *
     C                   EVAL      £JAXCP='</UIPopup>'
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Aggiungo griglia
     C*----------------------------------------------------------------
     C     £JAX_ABUT     BEGSR
      *
     C                   EVAL      £JAXCP='<Buttons>'
     C                   EXSR      £JAX_ADD
      *
1    C                   IF        £JaxBSv='1'
     C                   EVAL      £JaxCP=
     C                             '<Button '+
     C                             'Name="Salva" '+
     C                             'Status="1" '+
     C                             '/>'
     C                   EXSR      £JAX_ADD
1e   C                   ENDIF
1    C                   IF        £JaxBRn='1'
     C                   EVAL      £JaxCP=
     C                             '<Button '+
     C                             'Name="Salva con nome" '+
     C                             'Status="1" '+
     C                             '/>'
     C                   EXSR      £JAX_ADD
1e   C                   ENDIF
1    C                   IF        £JaxBDe='1'
     C                   EVAL      £JaxCP=
     C                             '<Button '+
     C                             'Name="Cancella" '+
     C                             'Status="1" '+
     C                             '/>'
     C                   EXSR      £JAX_ADD
1e   C                   ENDIF
      *
     C                   EVAL      £JAXCP='</Buttons>'
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Aggiungo riga editor
     C*----------------------------------------------------------------
     C     £JAX_ARIGE    BEGSR
      *
      * Costruzione
     C                   EVAL      £JaxCP=
     C                             '<Riga '+
     C                             'Dat="'+%TRIM(£JaxRDt)+'">'+
     C                             '<![CDATA['+%TRIM(£JaxRBf)+']]></Riga>'
      * Pulisco Key
     C                   CLEAR                   £JaxDSBut
      * In buffer
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
      *----------------------------------------------------------------
    RD* Inizializzazione Lista valori
      *----------------------------------------------------------------
     C     £JAX_ALIS_I   BEGSR
      *
     C                   EVAL      £JAXCP='<LisValori>'
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
      *----------------------------------------------------------------
    RD* Inizializzazione lista valori
      *----------------------------------------------------------------
     C     £JAX_ALIS     BEGSR
      *
      * Costruzione
     C                   EVAL      £JaxCP=
     C                             '<Val '+
     C                             'Dat="'+%TRIM(£JaxRDt)+'">'+
     C                             '<![CDATA['+%TRIM(£JaxRBf)+']]></Val>'
      * In buffer
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Finalizzazione Lista valori
     C*----------------------------------------------------------------
     C     £JAX_ALIS_F   BEGSR
      *
     C                   EVAL      £JAXCP='</LisValori>'
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Inizializzazione Buffer variabili
     C*----------------------------------------------------------------
     C     £JAX_BVAR_I   BEGSR
      *
     C****               CLEAR                   £JaxVBf
     C                   Z-ADD     0             £JaxVBfI
     C                   CLEAR                   £JaxDSVar
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Aggiungo a Buffer variabili
     C*----------------------------------------------------------------
     C     £JAX_BVAR     BEGSR
      *
1    C                   IF        £JaxVBfI<100
     C                   EVAL      £JaxVBfI=£JaxVBfI+1
     C                   EVAL      £JaxVBf(£JaxVBfI)=£JaxDSVar
     C                   CLEAR                   £JaxDSVar
1e   C                   ENDIF
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Svuoto Buffer variabili
     C*----------------------------------------------------------------
     C     £JAX_BVAR_F   BEGSR
      *
1    C                   IF        £JaxVBfI>0
     C                   EXSR      £JAX_AVAR_I
2    C                   DO        £JaxVBfI      £JAX_N50
     C                   EVAL      £JaxDSVar=£JaxVBf(£JAX_N50)
     C                   EXSR      £JAX_AVAR
2e   C                   ENDDO
     C                   EXSR      £JAX_AVAR_F
1e   C                   ENDIF
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Inizializzazione scrittura variabili
     C*----------------------------------------------------------------
     C     £JAX_AVAR_I   BEGSR
      *
     C                   EVAL      £JAXCP='<Variables>'
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Aggiungo variabile
     C*----------------------------------------------------------------
     C     £JAX_AVAR     BEGSR
      *
1    C                   IF        £JaxDSVar<>*BLANKS
      * Tipo variabile assunto: Sch
2    C                   IF        £JaxVarTip=*BLANKS OR                        COSTANTE
     C                             (£JaxVarTip<>'Sec' AND                       COSTANTE
     C                              £JaxVarTip<>'Sch' AND                       COSTANTE
     C                              £JaxVarTip<>'Ssc' AND                       COSTANTE
     C                              £JaxVarTip<>'Com' AND                       COSTANTE
     C                              £JaxVarTip<>'Loo')                          COSTANTE
     C                   EVAL      £JaxVarTip='Sch'                             COSTANTE
2e   C                   ENDIF
     C
     C                   EVAL      £JaxCP='<SetVar '+£JaxVarTip+
     C                             '.Var="'+
     C                             %TRIM(£JaxVarNam)+
     C                             '('+%TRIM(£JaxVarVal)
     C                             +')" />'
     C                   EXSR      £Jax_ADD
      *
     C                   CLEAR                   £JaxDSVar
1e   C                   ENDIF
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Finalizzazione scrittura variabili
     C*----------------------------------------------------------------
     C     £JAX_AVAR_F   BEGSR
      *
     C                   EVAL      £JaxCP='</Variables>'
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
     C*----------------------------------------------------------------
