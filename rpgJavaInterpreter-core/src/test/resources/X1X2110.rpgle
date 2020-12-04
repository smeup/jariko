     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 24/06/19  V5R1    AS Creazione
     V* 28/06/19  000942  AS Aggiunta funzione per matrice e clear
     V* 28/06/19  V5R1   BMA Check-out 000942 in SMEUP_SVI
     V* 28/06/19  000946  AS Trasformata ENTRY da Ds a campi singoli
     V* 28/06/19  V5R1   BMA Check-out 000946 in SMEUP_SVI
     V* 02/07/19  000954  AS Sostituita MOVEL con routine rudimentale per imitare Â£TNU
     V* 02/07/19  V5R1    CM Check-out 000954 in SMEUP_SVI
     V* 02/07/19  000957  AS Corretto XML, elimnato TAG (non supportato)
     V* 03/07/19  V5R1    CM Check-out 000957 in SMEUP_SVI
     V* 05/07/19  V5R1    BMA Renamed JD_010 in MUTE11_10
     V* 11/07/19  000983  AS Aggiunta Fu/Me con CALL a Fibonacci
     V* 11/07/19  V5R1   BMA Check-out 000983 in SMEUP_TST
     V* 11/07/19  000987  AS Eliminata Â£TNU
     V* 11/07/19  V5R1   BMA Check-out 000987 in SMEUP_TST
     V* 11/07/19  000988  AS Corretto errore
     V* 11/07/19  V5R1   BMA Check-out 000988 in SMEUP_TST
     V* ==============================================================
      * Nota sulle COPY
      * Avrei voluto lasciare l'inclusione di tutte le COPY, dato che vengono ignorate
      * dall'interprete. Non Ã¨ possibile farlo con tutte. Ad esempio le Â£JAX contengono la
      * ENTRY e questo andrebbe in conflitto (comilando in AS) con la ENTRY inserita appositamente
      * Commento l'EXSR delle COPY dato che genererebbe errore in interpretato
      * ==============================================================
      *--------------------------------------------------------------------------------------------*
      * ENTRY DS FUN
      *--------------------------------------------------------------------------------------------*
     D £UIBDS          DS         30448
     D $UIBPG                        10A
     D $UIBFU                        10A
     D $UIBME                        10A
     D $UIBT1                         2A
     D $UIBP1                        10A
     D $UIBK1                        15A
     D $UIBT2                         2A
     D $UIBP2                        10A
     D $UIBK2                        15A
     D $UIBT3                         2A
     D $UIBP3                        10A
     D $UIBK3                        15A
     D $UIBPA                       256A
     D $UIBT4                         2A
     D $UIBP4                        10A
     D $UIBK4                        15A
     D $UIBT5                         2A
     D $UIBP5                        10A
     D $UIBK5                        15A
     D $UIBT6                         2A
     D $UIBP6                        10A
     D $UIBK6                        15A
     D $UIBD1                      3000A
      *--------------------------------------------------------------------------------------------*
      * Contatore temporaneo
     D  $X             S              5  0
      * Contatore temporaneo
     D  $Y             S              5  0
      * Contatore di chiamate
     D  $COUNT         S              5  0
      * Collaboratore ricevuto in input
     D  $$COL          S             15
      * Numero ricevuto in input
     D  $$NUM          S             21
      * Parte di XML con il "payload" (senza header, ecc.)
     D  $$XMLPAYLOAD   S          30000
      * XML da spedire sulla coda
     D  $$XML          S          30000
      * Chiudere in LR?
     D  CLOLR          S              1N
      *--------------------------------------------------------------------------------------------*
      * ENTRY MUTE11_11A
     D  U$INNR         S             21  0
     D  U$FIBO         S             21  0
      *--------------------------------------------------------------------------------------------*
      * Impostazioni iniziali
     C                   EXSR      IMP0
      *
1    C                   SELECT
      * Funzione esempio
1x   C                   WHEN      %SUBST($UIBME:1:3)='ESE'
1    C                   SELECT
      * Metodo albero
1x   C                   WHEN      %SUBST($UIBME:5:3)='TRE'
     C                   EXSR      FESETRE
      * Metodo matrice
1x   C                   WHEN      %SUBST($UIBME:5:3)='MAT'
     C                   EXSR      FESEMAT
      * Metodo CLR (chiudi in LR)
1x   C                   WHEN      %SUBST($UIBME:5:3)='CLR'
     C                   EXSR      FESECLR
1e   C                   ENDSL
      * Richiamo di Fibonacci
1x   C                   WHEN      %SUBST($UIBME:1:3)='FIB'
     C                   EXSR      FFIB
1e   C                   ENDSL
      *
     C*                  EXSR      FIN0
      *
     C                   IF        CLOLR=*ON
     C                   SETON                                        LR
     C                   ELSE
     C                   SETON                                        RT
     C                   ENDIF
      *--------------------------------------------------------------*
    RD*  Impostazioni iniziali
      *--------------------------------------------------------------*
     C     IMP0          BEGSR
      *
      * Di default devo chiudermi in RT
     C                   EVAL      CLOLR=*OFF
     C                   ENDSR
      *--------------------------------------------------------------*
    RD*  Albero con collaboratore ricevuto in input e contatore incrementato
      *--------------------------------------------------------------*
     C     FESETRE       BEGSR
      * Estraggo collaboratore da INPUT
     C                   EXSR      DETCOL
      * Incremento contatore
     C                   EVAL      $COUNT=$COUNT+1
      * Emetto albero con collaboratore e contatore
     C                   EVAL      $$XMLPAYLOAD='<Oggetto Tipo="CN"'
     C                             +' Parametro="COL" Codice="'
     C                             +%TRIM($$COL)+'"'
     C                             +' Testo="Contatore: '
     C                             +%TRIM(%CHAR($COUNT))+'"'
     C                             +' />'
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD*  Matrice con collaboratore ricevuto in input e contatore incrementato
      *--------------------------------------------------------------*
     C     FESEMAT       BEGSR
      * Estraggo collaboratore da INPUT
     C                   EXSR      DETCOL
      * Incremento contatore
     C                   EVAL      $COUNT=$COUNT+1
      * Emetto matrice con collaboratore e contatore
     C                   EVAL      $$XMLPAYLOAD='<Griglia><Colonna Cod="XXCONT"'
     C                              +' Txt="Contatore" Lun="5" IO="O"'
     C                              +' Ogg="NR"/>'
     C                              +' <Colonna Cod="XXCOLL" Txt="Collaboratore"'
     C                              +' Lun="15" IO="O" Ogg="CNCOL"/>'
     C                              +' </Griglia>'
     C                   EVAL      $$XMLPAYLOAD=%TRIM($$XMLPAYLOAD)
     C                              +'<Righe>'
     C                   EVAL      $$XMLPAYLOAD=%TRIM($$XMLPAYLOAD)
     C                              +'<Riga Fld="'
     C                              +%TRIM(%CHAR($COUNT))
     C                              +'|'+%TRIM($$COL)+'"'
     C                   EVAL      $$XMLPAYLOAD=%TRIM($$XMLPAYLOAD)
     C                              +'/></Righe>'
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD*  Reset (chiudo in LR)
      *--------------------------------------------------------------*
     C     FESECLR       BEGSR
      *
      * Imposto chiusura in LR per resettare contatore
     C                   EVAL      CLOLR=*ON
      * Emetto albero con reset contatore
     C                   EVAL      $$XMLPAYLOAD='<Oggetto Tipo=""'
     C                             +' Parametro="" Codice=""'
     C                             +' Testo="SETON LR"'
     C                             +' />'
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD*  Estraggo collaboratore da INPUT
      *--------------------------------------------------------------*
     C     DETCOL        BEGSR
      *
     C                   CLEAR                   $$COL
     C                   EVAL      $X=%SCAN('COL(':$UIBD1)
     C                   IF        $X>0
     C                   EVAL      $Y=%SCAN(')':$UIBD1:$X+4)
     C                   IF        $Y>0
     C                   EVAL      $$COL=%SUBST($UIBD1:$X+4:$Y-$X-4)
     C                   ENDIF
     C                   ENDIF
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD*  Estraggo Numero da INPUT
      *--------------------------------------------------------------*
     C     DETNR         BEGSR
      *
     C                   CLEAR                   $$NUM
     C                   EVAL      $X=%SCAN('NUM(':$UIBD1)
     C                   IF        $X>0
     C                   EVAL      $Y=%SCAN(')':$UIBD1:$X+4)
     C                   IF        $Y>0
     C                   EVAL      $$NUM=%SUBST($UIBD1:$X+4:$Y-$X-4)
     C                   ENDIF
     C                   ENDIF
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD*  Esecuzione di Fibonacci
      *--------------------------------------------------------------*
     C     FFIB          BEGSR
      * Estraggo numero di cui calcolare il numero di Fibonacci
     C                   EXSR      DETNR
      * Trasformo in numerico
     C                   EVAL      U$INNR = %DEC($$NUM : 21 : 0)
      * Chiamo il programma che calcola il numero di fibonacci
     C                   CALL      'X1X2110FIB'
     C                   PARM                    U$INNR
     C                   PARM                    U$FIBO
      * Emetto albero con numero iniziale e suo fibonacci
     C                   EVAL      $$XMLPAYLOAD='<Oggetto Tipo="**"'
     C                             +' Parametro="" Codice="A"'
     C                             +' Testo="'
     C                             +'Fibonacci of: '                            COSTANTE
     C                             +%TRIM($$NUM)
     C                             +' is '
     C                             +%TRIM(%CHAR(U$FIBO))
     C                             +'"/>'
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD*  Chiusura e invio su coda del buffer residuo
      *--------------------------------------------------------------*
     C     FIN0          BEGSR
      * Aggiungo header e footer a payload
     C                   EVAL      $$XML='<?xml version="1.0"'
     C                             +' encoding="ISO-8859-15"?>'
     C                             +' <Base Testo="- ">'
     C                             +' <Service Titolo1="" Titolo2=""'
     C                             +' Funzione="F('
     C                             +%TRIM($UIBPG)+';'
     C                             +%TRIM($UIBFU)+';'
     C                             +%TRIM($UIBME)+') 1(;;)'
     C                             +' INPUT('
     C                             +%TRIM($UIBD1)+')'+'"'
     C                             +' Servizio="'
     C                             +%TRIM($UIBFU)
     C                             +'" TSep="." DSep=","'
     C                             +' IdFun="0000000000000" NumSes="000000" />'
     C                             +%TRIM($$XMLPAYLOAD)
      * Scrittura XML su coda
     C                   CALL      '£JAX_FIN0'
     C                   PARM                    $$XML
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Routine impostazioni di avvio programma
      *--------------------------------------------------------------*
     C     £INIZI        BEGSR
      *
      * ENTRY
     C     *ENTRY        PLIST
     C                   PARM                    £UIBDS
      *
     C                   CLEAR                   $COUNT
      *
     C                   ENDSR
      *--------------------------------------------------------------*