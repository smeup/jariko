      /TITLE Italian fiscal code check
      * Program by Claudi Neroni: https://www.neroni.it/
      *
      *+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * Definitions.
      *-------------------------------------------------------------------
      * Codice fiscale ricevuto spezzato nei suoi caratteri.
     D CX              S              1    DIM(16)                              Cod fis comodo
      * Codice fiscale di comodo.
      * Le cifre che negli omonimi sono sostituite da lettere
      * sono qui ripristinate.
     D SN              S              1    DIM(16) CTDATA PERRCD(16)            Sintassi
      * Sintassi del codice fiscale definitivo di persona fisica.
      * Il singolo elemento di schiera vale 0 se il corrispondente
      * elemento del codice fiscale deve essere un carattere
      * alfabetico. Vale invece 1 per carattere numerico.
     D NU              S              2  0 DIM(15)                              Numeri
      * Numeri di lavoro.
      * Ognuno dei primi 15 caratteri del codice fiscale definitivo
      * di persona fisica genera nel corrispondente elemento
      * della schiera numeri un proprio peso.
     D CP              S              1    DIM(36) CTDATA PERRCD(36)            Caratteri permess
      * Caratteri permessi nel codice fiscale definitivo
      * di persona fisica.
     D LN              S              1    DIM(36) CTDATA PERRCD(36)            Lettera o numero
      * Per ognuno dei caratteri permessi viene codificata la
      * caratteristica alfabetica con 0 o la caratteristica
      * numerica con 1.
     D PA              S              2  0 DIM(36) CTDATA PERRCD(36)            Pari
      * Peso da attribuire ai caratteri permessi che nel
      * codice fiscale definitivo di persona fisica hanno
      * posizione pari.
     D DI              S              2  0 DIM(36) CTDATA PERRCD(36)            Dispari
      * Peso da attribuire ai caratteri permessi che nel
      * codice fiscale definitivo di persona fisica hanno
      * posizione dispari.
     D ME              S              1    DIM(12) CTDATA PERRCD(12)            Mese di nascita
      * Lettere permesse nella posizione significativa
      * del mese di nascita.
     D TN              S              1    DIM(10) CTDATA PERRCD(10)            Numeri/Lettere
     D TL              S              1    DIM(10) ALT(TN)
      * Le cifre TN sono, per gli omonimi, sostituite dalle lettere TL.
     D CFDS            DS
      * Spezzamenti di comodo del codice fiscale in immissione.
     D  CF                     1     16
     D                                     DIM(16)                              Codice fiscale
     D  CF0105                 1      5
     D  CF0615                 6     15
      *+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * Routine principale.
      *-------------------------------------------------------------------
      * Scambia parametri.
      * Riceve il codice fiscale da esaminare.
      * Restituisce 1 in FISICA se esegue il controllo per
      *    persona fisica.
      * Restituisce 1 in OMONIM se durante il controllo per persona
      *    fisica riscontra dalla sintassi trattarsi di un omonimo.
      *    L'indicazione non viene data se il codice viene giudicato
      *    in errore.
      * Restituisce 1 in SINTAX se riscontra errore di sintassi.
      * Restituisce 1 in CHKDIG se riscontra errore nel carattere
      *    di controllo.
      *    L'indicatore di errore nel carattere di controllo viene
      *    sempre acceso anche per errore di sintassi perché
      *    il carattere di controllo non ha senso in presenza di
      *    un errore di sintassi.
     C     *ENTRY        PLIST
     C                   PARM                    CFDS                           I Codice fiscal
     C                   PARM                    FISICA            1            O Persona fisic
     C                   PARM                    OMONIM            1            O Omonimo
     C                   PARM                    SINTAX            1            O Error sintass
     C                   PARM                    CHKDIG            1            O Errore chkdig
      * Inizializza i parametri di ritorno.
     C                   MOVE      *ZERO         FISICA
     C                   MOVE      *ZERO         OMONIM
     C                   MOVE      *ZERO         SINTAX
     C                   MOVE      *ZERO         CHKDIG
      * Se il primo carattere del codice fiscale è diverso da blank,
      * lo considera un codice definitivo di persona fisica ed esegue
      * l'apposita routine di controllo.
      * Altrimenti esegue la routine di controllo degli altri tipi
      * di codice.
     C     CF(1)         COMP      *BLANK                             5858
     C   58              MOVE      '1'           FISICA
     C   58              EXSR      FIDE
     C  N58              EXSR      ALTI
     C                   GOTO      FINE
      * Label di fine con errore di sintassi.
     C     FINESN        TAG
     C                   MOVE      '1'           SINTAX
      * Label di fine con errore sul carattere di controllo.
     C     FINECD        TAG
     C                   MOVE      '1'           CHKDIG
     C                   MOVE      *ZERO         OMONIM
      * Label di fine felice.
     C     FINE          TAG
     C                   RETURN
      *+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     C     FIDE          BEGSR
      * Controlla il codice fiscale definitivo di persona fisica.
      *-------------------------------------------------------------------
      * Riempie il codice fiscale di comodo.
     C                   MOVE      CF            CX
      * Cerca nell'apposita schiera la lettera significativa
      * del mese di nascita.
      * Se non la trova, termina subito con errore di sintassi.
     C     CF(9)         LOOKUP    ME                                     50
     C  N50              GOTO      FINESN
      * Spegne l'indicatore di posizione pari e quello di impedita
      * sostituzione.
     C                   SETOFF                                       5155
      * Esamina la schiera del codice fiscale partendo dal quindicesimo
      * carattere e tornando al primo.
     C                   DO        15            X                 3 0
     C     16            SUB       X             XC                3 0
      *    Cerca il carattere tra i caratteri permessi.
     C                   Z-ADD     1             Y                 3 0
     C     CF(XC)        LOOKUP    CP(Y)                                  50
      *    Se non lo trova, termina subito con errore di sintassi.
     C  N50              GOTO      FINESN
      *    Se il carattere ha posizione pari, gli attribuisce
      *    il peso prelevato dall'apposita schiera.
     C   51              Z-ADD     PA(Y)         NU(XC)
      *    Se il carattere ha posizione dispari, gli attribuisce
      *    il peso prelevato dall'apposita schiera.
     C  N51              Z-ADD     DI(Y)         NU(XC)
      *    Trascrive in un indicatore che il controllo di sintassi,
      *    nella posizione corrente del codice fiscale, vuole
      *    un carattere numerico (57 on) o alfabetico (57 off).
     C                   MOVE      SN(XC)        *IN57
      *    Quando, durante l'esplorazione del codice da destra
      *    a sinistra, viene incontrato il primo carattere numerico,
      *    annota con 55 on che non è più permesso che un carattere
      *    chiesto numerico dal controllo di sintassi sia invece
      *    alfabetico. Si ricorda che la sostituzione di un carattere
      *    numerico con uno alfabetico riguarda le persone che altrimenti
      *    avrebbero il medesimo codice fiscale di un'altra persona
      *    con gli stessi dati anagrafici.
     C  N55              MOVE      LN(Y)         *IN55
      *    Mette on 56 se il carattere non rispetta la sintassi.
     C     SN(XC)        COMP      LN(Y)                              5656
      *    Se nella posizione esaminata il controllo di sintassi vuole
      *    un carattere numerico e ne trova invece uno alfabetico
      *    ma tale sostituzione è permessa dal fatto che a destra del
      *    carattere esaminato non ci sono caratteri numerici,
      *    annota che si tratta di un omonimo,
      *    esegue la routine di sostituzione
      *    ed evita di terminare in errore.
     C**   57
     C**AN 56
     C**ANN55              DO
     C*                  if        *in57 and *in56 and (not *in55)
CN01  * Eliminato obbligo di tutte lettere dopo prima sostituzione numero.
     C                   if        *in57 and *in56
     C                   MOVE      '1'           OMONIM
     C                   EXSR      SOST
     C                   GOTO      FIDEOL
     C                   END
      *    Altrimenti, per carattere irriguardoso della sintassi,
      *    termina subito con errore di sintassi.
     C   56              GOTO      FINESN
     C     FIDEOL        TAG
      *    Manovra il flip flop dell'indicatore 51 che caratterizza
      *    la posizione dispari (51 off) o pari (51 on).
     C     *IN51         COMP      *ZERO                                  51
      * Fine del ciclo ripetitivo.
     C                   END
      * Il giorno di nascita, dopo eventuali sostituzioni di lettere
      * con cifre corrispondenti, deve essere compreso tra 01 e 31
      * oppure tra 41 e 71.
      * Altrimenti termina subito con errore di sintassi.
     C                   MOVEA     CX(10)        A2                2
     C     A2            COMP      '01'                               50  50
     C   50A2            COMP      '31'                                 5050
     C  N50              DO
     C     A2            COMP      '41'                               50  50
     C   50A2            COMP      '71'                                 5050
     C                   END
     C  N50              GOTO      FINESN
      * Somma i numeri espressivi del peso assegnato a ciascun carattere
      * durante il ciclo ripetitivo.
     C                   XFOOT     NU            IND               5 0
      * Divide per il numero dei possibili caratteri di controllo,
      * conserva il resto e tramite questo individua l'indice
      * di accesso alla schiera dei caratteri permessi che,
      * in questa occasione e in parte, diventa la schiera dei caratteri
      * di controllo permessi.
     C     IND           DIV       26            IND
     C                   MVR                     IND
     C                   ADD       1             IND
      * Se il carattere di controllo calcolato non coincide con il
      * carattere di controllo ricevuto, termina subito con errore
      * sul carattere di controllo.
     C     CP(IND)       CABNE     CF(16)        FINECD
     C                   ENDSR
      *+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     C     SOST          BEGSR
      * Sostituisce le lettere con cifre.
      *-------------------------------------------------------------------
      * Cerca la lettera sostitutrice di numero in una schiera apposita
      * e, se non la trova, termina subito con errore di sintassi.
     C                   Z-ADD     1             Z                 3 0
     C     CF(XC)        LOOKUP    TL(Z)                                  50
     C  N50              GOTO      FINESN
      * Nel codice fiscale di comodo sostituisce alla lettera
      * il numero corrispondente.
     C                   MOVE      TN(Z)         CX(XC)
     C                   ENDSR
      *+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     C     ALTI          BEGSR
      * Controlla gli altri tipi di codice fiscale.
      * Codice fiscale provvisorio di persona fisica.
      * Codice fiscale provvisorio e definitivo di persona non fisica.
      *-------------------------------------------------------------------
      * I primi 5 caratteri devono essere in bianco, i successivi 10
      * devono essere numerici e l'ultimo uguale al carattere calcolato
      * dalla presente routine.
     C     CF0105        CABNE     *BLANK        FINESN
     C                   TESTN                   CF0615               50
     C  N50              GOTO      FINESN
     C     CF(15)        CABLT     *ZERO         FINESN
      * Rifiuta anche 10 zeri allineati a destra.
     C     CF0615        CABEQ     *ZERO         FINESN
      * Azzera l'accumulatore dei risultati parziali.
     C                   Z-ADD     *ZERO         IND
      * Spegne l'indicatore di posizione pari.
     C                   SETOFF                                       51
      * Esamina la schiera del codice fiscale dal sesto al quindicesimo
      * carattere.
     C     6             DO        15            X
      *    Trascrive il carattere in un campo numerico di una posizione.
     C                   MOVE      CF(X)         N1                1 0
      *    Se il carattere ha posizione dispari, lo somma nell'accumulatore.
     C  N51              ADD       N1            IND
      *    Se il carattere ha posizione pari,
      *    lo moltiplica per due e somma nell'accumulatore
      *    ciascuna delle due cifre ottenute.
     C   51              DO
     C     N1            ADD       N1            N2                2 0
     C                   MOVEL     N2            N1
     C                   ADD       N1            IND
     C                   MOVE      N2            N1
     C                   ADD       N1            IND
     C                   END
      *    Manovra il flip flop dell'indicatore 51 che caratterizza
      *    la posizione dispari (51 off) o pari (51 on).
     C     *IN51         COMP      *ZERO                                  51
      * Fine del ciclo ripetitivo.
     C                   END
      * Preleva l'ultima cifra dell'accumulatore.
     C                   MOVE      IND           N1
      * Ne fa il complemento a 10.
     C     10            SUB       N1            N1
      * Trascrive la cifra calcolata in un campo alfanumerico.
     C                   MOVE      N1            A1                1
      * Se il carattere di controllo trovato non coincide con il
      * carattere di controllo ricevuto, termina subito con errore.
     C     A1            CABNE     CF(16)        FINECD
     C                   ENDSR
** SN Sintassi codice. 0=carattere alfabetico, 1=carattere numerico.
0000001101101110
** CP Caratteri permessi.
ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789
** LN Caratteristica alfabetica o numerica dei caratteri permessi.
000000000000000000000000001111111111
** PA Valori da attribuire ai caratteri di posizione pari.
000102030405060708091011121314151617181920212223242500010203040506070809
** DI Valori da attribuire ai caratteri di posizione dispari.
010005070913151719210204182011030608121416102225242301000507091315171921
** ME Lettere permesse nella posizione del mese di nascita.
ABCDEHLMPRST
** TN/TL Le cifre sono, per gli omonimi, sostituite dalle lettere accoppiate.
0L1M2N3P4Q5R6S7T8U9V
