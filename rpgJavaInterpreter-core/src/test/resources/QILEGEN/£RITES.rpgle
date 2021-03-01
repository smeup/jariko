     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* B£00722A  V3R1    CM Rimozione Gestione Interna degli errori
     D* ==============================================================
     D* MODIFICHE Ril.  T Au Descrizione
     D* gg/mm/aa  nn.mm i xx Breve descrizione
     D* ==============================================================
     D* 10/09/07  V2R3    BS Aggiunte variabili di contesto
     D* 25/09/07  V2R3    BS Aggiunto campo per annullamento
     D* 03/10/12  V3R2    SP Aggiunti utente/data/ora inserimento e aggiornamento
     D* 15/11/12  V3R2   BMA Rilasciata da TST
     D*----------------------------------------------------------------
     D* OBIETTIVO
     D*  Eseguire il controllo validità/decodifica dell'elemento di una
     D*  tabella SMEUP, oppure eseguire la ricerca alfabetica, con la
     D*  possibilità di parzializzare.
     D*  Inoltre, impostando il campo £RITRS, si possono eseguire altre
     D*  funzioni quali posizionamento e scansione o aggiornamento.
     D*
     D* PREREQUISITI
     D* Per avere le parzializzazioni su TTLIBE:
     D* D/COPY QILEGEN,£RITESDS
     D*
     D* Input : £RITST : Settore
     D*         £RITEL : Elemento
     D*         £RITMA : Se diverso da ' ', viene permessa solo la
     D*                  ricerca alfabetica (lancia il pgm B£AR10A, che
     D*                  è una copia del pgm B£AR10, per permettere di
     D*                  eseguire una ricerca tabelle durante la manut.
     D*                  di una tabella, evitando la ricorsività)
     D*         £RITRS : Funzione sulle tabelle:
     D*                  Funzioni standard
     D*                            Ricerca / decodifica / lettura
     D*                  C         Lettura senza ricerca  (CHAIN)
     D*                  Posizionam. e scansione per codice
     D*                  P         Posiz. e lettura (SETLL+READE)
     D*                  S         Posizionamento         (SETLL)
     D*                  G         Posizion. su maggiore  (SETGT)
     D*                  L         Lettura successivo     (READE)
     D*                  R         Lettura precedente     (REDPE)
     D*                  Posizionam. e scansione per descrizione
     D*                  p         Posiz. e lettura (SETLL+READE)
     D*                  s         Posizionamento         (SETLL)
     D*                  g         Posizion. su maggiore  (SETGT)
     D*                  l         Lettura successivo     (READE)
     D*                  r         Lettura precedente     (REDPE)
     D*                  Aggiornamento tabella
     D*                  W         Scrittura nuovo record (WRITE)
     D*                  U         Aggiornamento record   (UPDAT)
     D*                  D         Eliminazione record    (DELET)
     D*                  K         Lock su record per agg.(CHAIN)
     D*                  O         Rilascio record in lock(UNLCK)
     D*         £RITPA : Stringa di parzializzazione su TTLIBE
     D*         £RITLC : Livello di chiamata ( evita la ricorsione )
     D* Output: £RITDS : Descrizione elemento
     D*         £RITLI : TTLIBE
     D*         £RITLU : TTUSER
     D*         £RITFL : 20 flag (in un campo unico lungo 20 car)
     D*         *IN35  : Errore
     D*         *IN36  : Richiesta ricerca
     D*
     D* ESEMPIO DI CHIAMATA
     D*C                     MOVEL<settore> £RITST
     D*C                     MOVEL<elemento>£RITEL
     D*C                     MOVEL<Parzial.>£RITPA
     D*C                     EXSR £RITES
     D*C  N35                MOVEL£RITDS    <campo descrizione>
     D*C  N35                MOVEL£RITLI    <DS Format TTLIBE>
     D*C  N35 36             MOVEL£RITEL    <campo elemento>
     D*----------------------------------------------------------------
     C     £RITES        BEGSR
      *
1    C                   IF        ££ATCO<>''
2    C                   IF        £RITAM=*BLANK
     C                   EVAL      £RITAM=££AMBI
2e   C                   ENDIF
2    C                   IF        £RITCO=''
     C                   MOVEL     ££CONT        £RITCO
2e   C                   ENDIF
2    C                   IF        £RITDT=0
     C                   MOVEL     ££DATE        £RITDT
2e   C                   ENDIF
1e   C                   ENDIF
      *
1    C                   IF        £RITAM=*BLANK
     C                   EVAL      £RITAM=£PDSNP
1e   C                   ENDIF
      * Salva campo funzione su tabelle (x scelta se ritornare in36)
     C                   MOVEL     £RITRS        £RITR§            1
      * Imposta nome pgm da lanciare
1    C     £RITMA        IFEQ      ' '
     C     'B£AR10'      CAT(P)    £RITLC        £RITPG           10
1x   C                   ELSE
     C                   MOVEL(P)  'B£AR10A'     £RITPG
1e   C                   END
      * Lancio
     C                   CALL      £RITPG
     C                   PARM                    £RITST            5            Settore
     C                   PARM                    £RITEL           15            Elemento
     C                   PARM                    £RITDS           30            Descrizione
     C                   PARM      0             £RIN35            1 0          Ind. errore
     C                   PARM      0             £RIN36            1 0          Ind. ricerca
     C                   PARM                    £RITMA            1            Flag manutenzione
     C                   PARM                    £RITRS            1            Funzione base
     C                   PARM                    £RITLI          100            Campo dati std
     C                   PARM                    £RITPA          100            DS parzializzazione
     C                   PARM                    £RITLU          200            Campo dati utente
     C                   PARM                    £RITFL           20            Flags
     C                   PARM                    £RITFU           10            Funzione estesa
     C                   PARM                    £RITME           10            Metodo funz.estesa
     C                   PARM                    £RITAM           10            Ambiente (pgm call)
     C                   PARM                    £RITCO           10            Contesto
     C                   PARM                    £RITRI            9 0          Nrr input
     C                   PARM                    £RITRO            9 0          Nrr output
     C                   PARM                    £RITMS            7            Rtn code (messaggio)
     C                   PARM                    £RITFI           10            File messaggi
     C                   PARM                    £RITVA           45            Valore rtn code
     C                   PARM                    £RITCM            2            Comando
     C                   PARM                    £RITDT            8 0          Data Riferimento
     C                   PARM                    £RITTA            1            Annullamento
     C                   PARM                    £RITUI           10            Utente Inserim
     C                   PARM                    £RITDI            8 0          Data   Inserim
     C                   PARM                    £RITOI            6 0          Ora    Inserim
     C                   PARM                    £RITUA           10            Utente Aggiornam
     C                   PARM                    £RITDA            8 0          Data   Aggiornam
     C                   PARM                    £RITOA            6 0          Ora    Aggiornam
      * .. riporta in35
     C                   MOVE      £RIN35        *IN(35)
      * .. riporta in36 solo se non eseguita una funzione su file
1    C     £RITR§        IFEQ      ' '
     C                   MOVE      £RIN36        *IN(36)
1e   C                   ENDIF
      *
      * LOG se attivo da tabella B£2 e/o da programma
1    C  N35££B£2L        IFEQ      '11'
     C                   MOVEL     ££B£2L        ££B£2L            2
2   >C                   IF        ££B£2J = '1'
    >C                   CALL      'B£DMS7'                             37
    >C                   PARM      'COS'         £DMS7F           10
    >C                   PARM                    £DMS7M           10
    >C                   PARM      'TA'          £DMS7T            2
    >C                   PARM                    £RITST
    >C                   PARM                    £RITEL
    >C                   PARM                    £DMS75            1
2x  >C                   ELSE
     C                   EVAL      *IN37=*OFF
     C                   CALL      'B£DMS7'
     C                   PARM      'COS'         £DMS7F           10
     C                   PARM                    £DMS7M           10
     C                   PARM      'TA'          £DMS7T            2
     C                   PARM                    £RITST
     C                   PARM                    £RITEL
     C                   PARM                    £DMS75            1
2e  >C                   ENDIF
     C                   MOVEL     *BLANKS       £DMS7F           10
     C                   MOVEL     *BLANKS       £DMS7M           10
1e   C                   ENDIF
      * Pulisce campo parzializzazioni e livello di chiamata
     C                   MOVE      *BLANK        £RITPA
     C                   MOVE      *BLANK        £RITLC            1
     C                   CLEAR                   £RITCO
     C                   CLEAR                   £RITDT
      *
     C                   ENDSR
     C*-----------------------------------------------------------
