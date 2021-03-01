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
     D* 02/10/07  V2R3    AS Eliminata £CRI su B£DEC5
     D* 10/09/13  V4R1    GG Aggiunte DS di input e output
     D*-------------------------------------------------------------------
     D* OBIETTIVO
     D*  Decodifica un codice, dato un tipo codice (tab. *CNTT)
     D*  eseguendo la realtiva ricerca alfabetica e decodifica
     D*                                                                ---
     D* PREREQUISITI
     D* D/COPY QILEGEN,£DECDS
     D* Aggiunta in automatico dal precompilatore
     D*
     D*
     D* FLUSSO
     D*  Input : £DECCD - Codice da decodificare
     D*          £DECTP - Tipo codice (da tabella *CNTT)
     D*          £DECPA - Parametro nel caso serva ('T_' -> settore)
     D*                                            ('OJ' -> objtype)
     D*          £DECR1 - S = Eseguire le decodifiche su *CNTT
     D*                       (default = 'N')                          ---
     D*          N.B. se scelto S il programma controlla la lunghezza
     D*               del campo passato (lunghezza dichiarata nella
     D*               tabella *CNTT)
     D*               Esempio : Vds 123456........  (campo video 15)   ---
     D*                  richiesto  12345678......   errore            ---
     D*                                                                ---
     D*                                                                ---
     D*          £DECR2 - S = Eseguire la ricerca tabelle con la possi-
     D*                       bilità di aggiornamento:
     D*                       se NON specificato 'S' la routine £RITSM
     D*                        effettua la sola ricerca senza modifica,
     D*                       necessario se £DEC è richiamata dal pgm
     D*                       gestione tabelle (vedi nota sotto)
     D*                       (default = 'N')                          ---
     D*                   C = Solo memorizzare per oggetti di 2°livello
     D*                       Es. Ubicazioni di un articolo
     D*                           Articoli di un ente
     D*                       Normalmente i seguenti oggetti lasciano  ---
     D*                       automaticamente traccia per interfaccia a---
     D*                       oggetti multipli:                        ---
     D*                       - Articolo                               ---
     D*                       - Magazzino                              ---
     D*                       - Contatto                               ---
     D*          £DECR3 - Livello di richiamo
     D*                   Suffisso della copia di B£DEC da richiamare
     D*                   al fine di evitare la ricorsione
     D*                                                                ---
     D*          £DECR4 - Se diverso da blanks esegue solo la
     D*                   presentazione delle funzioni                 ---
     D*                                                                ---
     D*          £OAVDI - DS di input
     D*                                                                ---
     D*  Output: £DECCD - Codice da ricerca alfabetica
     D*          £DECDE - Descrizione codice
     D*          £DECIN - Intestazione tipo campo (da tabella *CNTT)
     D*          *IN35  - On=Errore
     D*          *IN36  - On=Eseguita ricerca alfabetica
     D*          £OAVDO - DS di output
     D*
     D* ESEMPIO DI RICHIAMO
     D*C                     MOVEL<codice>  £DECCD
     D*C                     MOVEL<tipo cod>£DECTP
     D*C                     MOVEL<param.>  £DECPA
     D*C                     MOVEL<S/N>     £DECR1
     D*C                     MOVEL<S/N>     £DECR2
     D*C                     MOVEL< /A/B/C> £DECR3
     D*C                     EXSR £DEC
     D*C     N35             MOVEL£DECDE    <campo decodifica>
     D*C     N35 36          MOVEL£DECCD    <campo codice>
     D*C     N35 36          SETON                     10
     D*-------------------------------------------------------------------
     C     £DEC          BEGSR
      *
1    C                   IF        ££ATCO<>''
2    C                   IF        £DECAM=''
     C                   MOVEL     ££AMBI        £DECAM
2e   C                   ENDIF
2    C                   IF        £DECCO=''
     C                   MOVEL     ££CONT        £DECCO
2e   C                   ENDIF
2    C                   IF        £DECDT=0
     C                   MOVEL     ££DATE        £DECDT
2e   C                   ENDIF
1e   C                   ENDIF
      *
1    C                   IF        £DECAM=''
     C                   MOVEL     £PDSNP        £DECAM
1e   C                   ENDIF
      *
1    C     £DECR4        IFEQ      *BLANKS
     C                   MOVEL     £DECCD        £DECRS            1
2    C     £DECRS        IFEQ      '%'
     C                   MOVE      £DECCD        £DECRD           14
     C                   MOVEL(P)  £DECRD        £DECCD
     C                   MOVEL     'I'           £DECR4
2e   C                   ENDIF
     C     'B£DEC0'      CAT(P)    £DECR3:0      AAA010           10
     C                   MOVEL     *BLANKS       £DECR3            1
     C                   SETOFF                                       3536
     C                   CALL      AAA010
     C                   PARM                    £DECCD           15
     C                   PARM                    £DECTP            2
     C                   PARM                    £DECPA           10
     C                   PARM                    £DECR1            1
     C                   PARM                    £DECR2            1
     C                   PARM                    £DECDE           35
     C                   PARM                    £DECIN           15
     C                   PARM                    £DEC35            1
     C                   PARM                    £DEC36            1
     C                   PARM                    £DECAM           10
     C                   PARM                    £DECCO           10
     C                   PARM                    £DECDT            8 0
     C                   PARM                    £DECDI
     C                   PARM                    £DECDO
     C                   MOVE      £DEC35        *IN35
     C                   MOVE      £DEC36        *IN36
1e   C                   ENDIF
     C*
1    C     £DECRS        IFEQ      '%'
     C                   MOVEL     *ON           *IN36
     C                   MOVEL     *ON           £DEC36
1e   C                   ENDIF
     C*
     C* Lancio funzioni su oggetto
1    C     £DECR4        IFNE      *BLANKS
     C     £DECRS        OREQ      '%'
     C     *IN35         ANDEQ     *OFF
     C*
2   >C                   IF        ££B£2J = '1'
    >C                   CALL      'B£DEC5'                             37
    >C                   PARM                    £DECR4
    >C                   PARM                    £DECCD
    >C                   PARM                    £DECTP
    >C                   PARM                    £DECPA
2x  >C                   ELSE
     C                   EVAL      *IN37=*OFF
     C                   CALL      'B£DEC5'
     C                   PARM                    £DECR4
     C                   PARM                    £DECCD
     C                   PARM                    £DECTP
     C                   PARM                    £DECPA
2e  >C                   ENDIF
1e   C                   ENDIF
     C*
     C                   MOVEL     *BLANKS       £DECR4            1
     C                   MOVEL     *BLANKS       £DECRS            1
     C                   CLEAR                   £DECCO
     C                   CLEAR                   £DECDT
     C*
     C                   ENDSR
     C*-------------------------------------------------------------
