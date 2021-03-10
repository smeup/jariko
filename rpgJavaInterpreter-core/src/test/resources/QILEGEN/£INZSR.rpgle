      /IF NOT DEFINED(£INZSR)
      /DEFINE £INZSR
     D*  ATTENZIONE!!!!
     D* Questa Copy deve essere sempre allineata (al netto del pgm richiamato) alla £INZSR_01
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 28/09/00  04.00   GG Correzione data
     V* 13/06/01  04.00   CS Considera il fuso orario
     V* 06/08/02  05.00   FL Aggiunto parm a B£GGP2 per gestire la
     V*                      richiesta di abbandono (impostare a "P")
     V* 21/08/02  05.00   ZZ Esternalizzata lettura B£1/B£2 ed imposta-
     V*                      zione campi data/ora
     V* 04/09/03  V2R1    GG Eliminata definizione §§IN
     V* 17/09/03  V2R3    BS Valorizzazione variabile ££ATCO da B£2
     V* 15/12/08  V2R3    AS Aggiunta possibilità di escludere *PSSR dall'inclusione
     V* 22/07/10  V3R1    CM Per poter utilizzare la *PSSR ne deve essere dichiarata l'intenzione
     V*                      attraverso la define SI_PSSR
     V* 11/10/10  V3R1    AS Eliminato tag ££FINE
     V* 18/10/10  V3R1    AS Eliminato RCLRSC
     V* 01/07/11          CM Abilitato RCLRSC condizionandolo alla tabella B£2
     V* 09/07/11  V3R2    AS Rilascio mod. precedenti non rilasciate (segnate con versione blank)
     V* A£61103A  V5R1    AS Disaccoppiamento utente di sistema da utente applicativo
     V* ==============================================================
     V* B£00722A  V3R1    CM Rimozione Gestione Interna degli errori
     D* OBIETTIVO
     D*  Eseguire la parte standard delle operazioni di inizio program-
     D*  ma e richiamare la routine di inizio del programma specifi-
     D*  co (£INIZI)
     D*
     D* NOTA IMPORTANTE
     D*  Questa /COPY deve essere messa subito dopo la fine del MAIN,
     D*  prima di qualsiasi routine o di altre /COPY di routines.
     D*----------------------------------------------------------------
     C*
     C* Scrittura della LDA a fine programma
     C**   ££FINE        TAG
     CLR                 OUT       £UDLDA
     C* Chiusura di tutti i files e pulizia memoria statica dopo
     C* eventuale esecuzione di programmi interfaccia/decodifica
1    CLR                 IF        ££B£20 = '1'
     CLR                 CALL      £EXCMD
     CLR                 PARM      'RCLRSC'      £RCLRS            6
     CLR                 PARM      6             NNN155_OLD       15 5
1e   CLR                 ENDIF
     C* Definizione fittizia dell'indicatore LR
     CLR                 SETON                                        LR
     C*----------------------------------------------------------------
     C*   ROUTINE INIZIALE DEL PROGRAMMA  (richiamata automaticamente)
     C*----------------------------------------------------------------
     C     *INZSR        BEGSR
     C* Imposta nome pgm per esecuzione comandi CL
     C                   MOVEL     'QCMDEXC'     £EXCMD           10
     C* Settaggio nome programma/modulo (compatibilità con ILE)
1    C     £PDSNM        IFNE      *BLANKS
     C                   MOVEL     £PDSNP        AAA010           10
     C                   MOVEL     £PDSNM        £PDSNP
     C                   MOVEL     AAA010        £PDSNM
     C                   CLEAR                   AAA010
1e   C                   ENDIF
     C* Inizializzazione programma
     C                   CALL      'B£INZ0'
     C                   PARM      'INZ'         £INZFU           10
     C                   PARM      *BLANKS       £INZME           10
     C                   PARM                    £INZDS
     C                   PARM      £PDSNP        £INZCO           10
     C                   MOVEL     £INZ£1        B£1$DS
     C                   MOVEL     £INZ£2        B£2$DS
      * Utente applicativo
     C                   EVAL      £PDSNU=£INZUA
      *
     C* Definizione della LDA
     C     *DTAARA       DEFINE    *LDA          £UDLDA
     C                   IN        £UDLDA
     C* Richiamo routine iniziale specifica del singolo programma
     C                   MOVEL(P)  ££B£2Y        ££ATCO
     C                   EXSR      £INIZI
     C*
     C                   ENDSR
      /ENDIF
      /IF DEFINED(SI_PSSR)
     C*----------------------------------------------------------------
     C*  Routine di gestione eccezioni / errori generici di programma
     C*  e di operazioni I/E.
     C*----------------------------------------------------------------
     C     *PSSR         BEGSR
     C                   MOVEL     *ALL'0'       £INZ02            5
     C                   MOVE      £PDSSC        £INZ02
1   >C                   IF        ££B£2J = '1'
    >C                   CALL      'B£GGP2  '                           37
    >C                   PARM                    £PDSNP
    >C                   PARM                    £INZ02
    >C                   PARM                    £PDSNF
    >C                   PARM                    £PDSIF
    >C                   PARM                    £PDSTX
    >C                   PARM      *BLANKS       £INZ01            6
    >C                   PARM      *BLANKS       £INZ00            1
    >C                   PARM                    £FLGRA            1
    >C                   PARM                    £PDSSI
1x  >C                   ELSE
     C                   EVAL      *IN37=*OFF
     C                   CALL      'B£GGP2  '
     C                   PARM                    £PDSNP
     C                   PARM                    £INZ02
     C                   PARM                    £PDSNF
     C                   PARM                    £PDSIF
     C                   PARM                    £PDSTX
     C                   PARM      *BLANKS       £INZ01            6
     C                   PARM      *BLANKS       £INZ00            1
     C                   PARM                    £FLGRA            1
     C                   PARM                    £PDSSI
1e  >C                   ENDIF
     C                   ENDSR     £INZ01
      /ENDIF
