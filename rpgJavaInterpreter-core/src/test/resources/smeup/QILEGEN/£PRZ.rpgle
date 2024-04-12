      *====================================================================
      * smeup V6R1.021DV
      * Nome sorgente       : £PRZ
      * Sorgente di origine : SMEDEV/QILEGEN(£PRZ)
      * Esportato il        : 20240411 144729
      *====================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 23/05/98  01.00   GG Aggiunto livello di chiamata
     V* 29/05/98  01.00   GG Non pulisce in input il messaggio
     V* 10/11/98  02.00   GG Corretta segnalaz. pgm errato
     V* 03/12/98  02.00   BC Aggiunto indicatore 37 alla CALL
     V* 28/10/01  02.00   CS Allungato campo £PRZQC a 10
     V* 05/08/02  05.00   TA Allungato campo £PRZTC a 13
     V* 06/02/04   V2R1   GR Ottimizzazione lunghezza query con numerici
     V* 25/11/04   V2R1   GG Corretta definizione campi: tolti $A e $B
     V* 17/12/04   V2R1   PV Gestione numeri negativi
     V* 10/02/06   V2R2   CS In ottimizzazione della costruzione QRYSLT
     V*                      considerati anche valori *LOVAL e *HIVAL
     V* 28/12/07   V2R3   BS Aggiunto nuovo parametro per gestione liste
     V* B£00722A  V3R1    CM Rimozione Gestione Interna degli errori
     V* 08/03/17  V5R1    CM Sostituito cartattere x'FF' con £HEXFF
     V* ==============================================================
     D* OBIETTIVO
     D*  Eseguire tutte le funzioni svolte nel controllo dei campi
     D*  di parzializzazione:
     D*  - Ricerca alfabetica                                          ---
     D*  - Controllo formale                                           ---
     D*  - Controllo congruenza limiti                                 ---
     D*  - Impostazione valori minimo e massimo di default in assenza  ---
     D*    di valori impostati
     D*  - Impostazione messaggi d'errore                              ---
     D*
     D*  Inoltre, se si utilizza la routine £PRZQ (vedi sotto), si puo'
     D*  anche impostare la stringa qryslt, ottimizzata, per l'opnqryf.
     D*                                                                ---
     D* N.B: E'richiesta la /COPY £DMS                                 ---
     D*                                                                ---
     D* FLUSSO
     D*  Input :
     D*   8 a    £PRZTC - Tipo campo:
     D*          I primi 2 caratteri definiscono il tipo oggetto (da
     D*          tabella *CNTT) e se omessi definiscono 'caratteri     ---
     D*          ad immissione libera'                                 ---
     D*
     D*          Il terzo carattere, definisce se i campi minimo e
     D*          massimo devono essere controllati, secondo questa
     D*          regola:
     D*                   'D'        : è obbligatorio il campo Da
     D*                   'A'        : è obbligatorio il campo A
     D*                   Altro carattere: sono obbligatori entrambi.
     D*          Ad esempio, xxD significa che il valore minimo
     D*          impostato non deve essere a blanks ed inoltre deve
     D*          esistere in tabella; DTx significa che le date minima
     D*          e massima non possono essere a blanks, oltre ad essere
     D*          formalmente valide (condizione che viene sempre
     D*          controllata).
     D*
     D*          I caratteri da 4 a 8, definiscono il parametro del
     D*          tipo oggetto definito nei primi due caratteri
     D*
     D*  30    a £PRZWN - Valore alfanumerico minimo video
     D*  30    a £PRZWX - Valore alfanumerico massimo video
     D*  30  9 n £PRZVN - Valore numerico minimo video
     D*  30  9 n £PRZVX - Valore numerico massimo video
     D*   2  0 n £PRZE1 - N.ro indicatore campo minimo
     D*   2  0 n £PRZE2 - N.ro indicatore campo massimo
     D*                   (se 0 è uguale al minimo)
     D*   2  0 n £PRZE3 - N.ro indicatore ricerca alfabetica
     D*                   (se 0 è impostato a 10)
     D*   2  0 n £PRZE4 - N.ro indicatore errore
     D*                   (se 0 è impostato a 60)
     D*   1    a £PRZLC - Livello di chiamata
     D*
     D*  Output:
     D*  30    a £PRZAN - Valore alfanumerico minimo da muovere in LDA
     D*  30    a £PRZAX - Valore alfanumerico massimo da muovere in LDA
     D*  30  9 n £PRZNN - Valore numerico minimo da muovere in LDA
     D*  30  9 n £PRZNX - Valore numerico massimo da muoverein LDA
     D*  40    a £PRZDN - Decodifica valore minimo
     D*  40    a £PRZDX - Decodifica valore massimo
     D*   7    a £PRZME - Codice messaggio d'errore
     D*   1    a £PRZES - 'T' se non impostati estremi
     D* Esempio
     D*C                     MOVEL<TpDPar>£PRZTC
     D*C                     MOVEL<In.Vid>£PRZWN      P
     D*C                     MOVEL<Fi.Vid>£PRZWX      P
     D*C                     Z-ADD<Ind.E1>£PRZE1
     D*C                     Z-ADD<Ind.E2>£PRZE2
     D*C                     EXSR £PRZ
     D*C                     MOVEL£PRZWN  <In.Vid>
     D*C                     MOVEL£PRZWX  <Fi.Vid>
     D*C                     MOVEL£PRZAN  <In.LDA>
     D*C                     MOVEL£PRZAX  <Fi.LDA>
     D*C Istruzioni per versioni fino alla V2R1M1 compresa, in cui non
     D*C è supportata la MOVEL con 'P' dei campi
     D*C                     MOVEL*BLANKS <In.LDA>
     D*C                     MOVEL*BLANKS <Fi.LDA>
     D*
     D*-------------------------------------------------------------------
      /COPY QILEGEN,£HEX
     C     £PRZ          BEGSR
      *
      * Pulizia campi di output
     C                   MOVEL     *BLANKS       £PRZAN
     C                   MOVEL     *BLANKS       £PRZAX
     C                   Z-ADD     0             £PRZNN
     C                   Z-ADD     0             £PRZNX
     C                   MOVEL     *BLANKS       £PRZDN
     C                   MOVEL     *BLANKS       £PRZDX
     C                   MOVEL     *BLANKS       £PRZES
      *
      * Preparazione indicatori
     C                   Z-ADD     £PRZE1        $1                5 0          Errore da
     C                   Z-ADD     £PRZE2        $2                5 0          Errore a
     C                   Z-ADD     £PRZE3        $3                5 0          Ricerca alf.
     C                   Z-ADD     £PRZE4        $4                5 0          Errore gen.
1    C     $2            IFEQ      0
     C                   Z-ADD     $1            $2
1e   C                   ENDIF
1    C     $3            IFEQ      0
     C                   Z-ADD     10            $3
1e   C                   ENDIF
1    C     $4            IFEQ      0
     C                   Z-ADD     60            $4
1e   C                   ENDIF
      *
     C     'B£PRZ0'      CAT(P)    £PRZLC        £GGPNP           10
      *
  MO>C                   IF        ££B£2J = '1'
  MO>C                   CALL      £GGPNP                               37
  MO>C                   PARM                    £PRZTC           13
  MO>C                   PARM                    £PRZWN           30
  MO>C                   PARM                    £PRZWX           30
  MO>C                   PARM                    £PRZVN           30 9
  MO>C                   PARM                    £PRZVX           30 9
  MO>C                   PARM                    £PRZAN           30
  MO>C                   PARM                    £PRZAX           30
  MO>C                   PARM                    £PRZNN           30 9
  MO>C                   PARM                    £PRZNX           30 9
  MO>C                   PARM                    £PRZDN           40
  MO>C                   PARM                    £PRZDX           40
  MO>C                   PARM                    £PRZME            7
  MO>C                   PARM                    £PRZEN            1            Errore da
  MO>C                   PARM                    £PRZEX            1            Errore a
  MO>C                   PARM                    £PRZRA            1            Ric.Alfab.
  MO>C                   PARM                    £PRZES            1
  MO>C                   PARM                    £PRZLI            1
  MO> *
  MO>C                   ELSE
     C                   EVAL      *IN37=*OFF
     C                   CALL      £GGPNP
     C                   PARM                    £PRZTC           13
     C                   PARM                    £PRZWN           30
     C                   PARM                    £PRZWX           30
     C                   PARM                    £PRZVN           30 9
     C                   PARM                    £PRZVX           30 9
     C                   PARM                    £PRZAN           30
     C                   PARM                    £PRZAX           30
     C                   PARM                    £PRZNN           30 9
     C                   PARM                    £PRZNX           30 9
     C                   PARM                    £PRZDN           40
     C                   PARM                    £PRZDX           40
     C                   PARM                    £PRZME            7
     C                   PARM                    £PRZEN            1            Errore da
     C                   PARM                    £PRZEX            1            Errore a
     C                   PARM                    £PRZRA            1            Ric.Alfab.
     C                   PARM                    £PRZES            1
     C                   PARM                    £PRZLI            1
      *
  MO>C                   ENDIF
  MO>C     *IN37         IFEQ      *ON
  MO>C                   CALL      'B£GGP0  '
  MO>C                   PARM                    £GGPNP
  MO>C                   PARM      *BLANKS       £GGPTP           10
  MO>C                   PARM      *BLANKS       £GGPPA          100
  MO>C                   ENDIF
     C                   MOVEL     £PRZLC        £PRZLC            1
      *
      * Impostazione indicatori (NB: solo accesi, non reimpostati !!)
1    C     $1            IFNE      0
     C     £PRZEN        ANDEQ     *ON
     C                   MOVEL     £PRZEN        *IN($1)
1e   C                   ENDIF
      *
1    C     $2            IFNE      0
     C     £PRZEX        ANDEQ     *ON
     C                   MOVEL     £PRZEX        *IN($2)
1e   C                   ENDIF
      *
1    C     $3            IFNE      0
     C     £PRZRA        ANDEQ     *ON
     C                   MOVEL     £PRZRA        *IN($3)
1e   C                   ENDIF
      *
1    C     $4            IFNE      0
2    C     £PRZEN        IFEQ      *ON
     C     £PRZEX        OREQ      *ON
     C                   MOVEL     '1'           *IN($4)
2e   C                   ENDIF
1e   C                   ENDIF
      *
      * Registrazione campo d'errore
1    C     £PRZME        IFNE      *BLANKS
     C                   MOVEL     £PRZME        £DMSME
     C                   MOVEL     'MSGBA'       £DMSFI
     C                   EXSR      £DMSC2
1e   C                   ENDIF
      *
      * Pulizia campi di input
     C                   MOVEL     *BLANKS       £PRZTC
     C                   Z-ADD     0             £PRZE1            2 0
     C                   Z-ADD     0             £PRZE2            2 0
     C                   Z-ADD     0             £PRZE3            2 0
     C                   Z-ADD     0             £PRZE4            2 0
     C                   CLEAR                   £PRZLI
      *
     C                   ENDSR
     D*-------------------------------------------------------------------
     D* £PRZQ
     D*-------------------------------------------------------------------
     D* OBIETTIVO
     D*  Costruire la stringa qryslt £PRZQS, ottimizzata per l'opnqryf.
     D*  Questa stringa dovrà poi essere memorizzata nella lda per
     D*  poter essere utilizzata dall'opnqryf in un programma clp.
     D*
     D* FLUSSO
     D*  Ad ogni richiamo costruisce nel campo £PRZQW la condizione
     D*  da verificare: *LE, *GE, *EQ o %RANGE, con i valori opportuni.
     D*  (Es: '%RANGE=("AAA" "GGG")', oppure '>=940101', '=10', ecc.).
     D*  Inoltre, se ha ricevuto il nome del campo del file, accoda
     D*  '*AND + nome_campo + condizione' alla stringa qryslt £PRZQS
     D*  (altrimenti l'accodamento dovra' essere gestito dal pgm).
     D*
     D*  NB: la routine NON puo' gestire campi numerici con DECIMALI.
     D*
     D*  Input :
     D*    £PRZQC - (10a) Nome campo del file.
     D*        a) Se impostato, la routine accoda il nome del campo e
     D*           la condizione £przqw a £przqs.
     D*        b) Se non impostato, la routine si limita ad impostare
     D*           la condizione in £przqw, senza accodarla a £przqs.
     D*           (L'accodamento dovrà essere gestito nel pgm).
     D*    I due campi seguenti devono essere impostati solo se £przqc
     D*    è un campo numerico (NB: non deve avere decimali)
     D*    £PRZQI - (30a) Valore UIxxxx = campo min lda, allin. a sx
     D*    £PRZQF - (30a) Valore UFxxxx = campo max lda, allin. a sx
     D*  In/Out:
     D*    £PRZQS - (300a) Stringa qryslt (costruita progressivamente)
     D*                    E' definita nella /COPY £PDS.
     D*  Output:
     D*    £PRZQW - (80a) Condizione (es: ="ABC", >=940101, ecc)
     D*    £PRZQE - (1a)  Se = 'E', errore supero lunghezza £przqs
     D*
     D* Esempio di utilizzo (pgm RPG)
     D*
     D*  Inizializzazione £przqs (prima del primo richiamo)
     D*C                     MOVE *BLANK    £PRZQS
     D*C     (oppure)
     D*C           'XXANNU'  CAT  '=" "'    £PRZQS    P
     D*
     D*  Aggiunta condizione
     D*C                     ...
     D*C                     EXSR £PRZ
     D*C                     ...
     D*C                     Z-ADD£PRZNN    UIDATA
     D*C                     Z-ADD£PRZNX    UFDATA
     D*C           £PRZES    IFNE 'T'
     D*C                     MOVEL'XXDATA'  £PRZQC
     D*C                     MOVELUIDATA    £PRZQI  (se £przqc è num)
     D*C                     MOVELUFDATA    £PRZQF  ( "   "    "  " )
     D*C                     EXSR £PRZQ
     D*C                     END
     D*
     D*  (oppure: Aggiunta di condizioni particolari)
     D*C           £PRZQS    CAT  '& (':1     £PRZQS
     D*C           £PRZQS    CAT  'XXCODI':0  £PRZQS
     D*C           £PRZQS    CAT  £PRZQW:0    £PRZQS
     D*C                     ....
     D*C           £PRZQS    CAT  '*OR':1     £PRZQS
     D*C                     ....
     D*
     D*  Memorizzazione £przqs in lda (dopo l'ultimo richiamo)
     D*C           £PRZQE    IFEQ ' '
     D*C           £PRZQS    IFEQ *BLANK
     D*C                     MOVEL'*ALL'    £PRZQS
     D*C                     ENDIF
     D*C                     MOVEL£PRZQS    £PRZQ1
     D*C                     MOVE £PRZQS    £PRZQ2
     D*C                     OUT  £UDLDA
     D*C                     ELSE
     D*C                     EXSR <errore>
     D*C                     END
     D*
     D* Esempio di utilizzo (pgm CLP)
     D*
     D*      OPNQRYF    FILE(FILEX00F) QRYSLT(%SST(*LDA 597 300)  +
     D*    (facoltativo)           *TCAT '& XXCODI="ABC"') +
     D*                 KEYFLD(...)
     D*-------------------------------------------------------------------
     C     £PRZQ         BEGSR
     C* Costruisce in £PRZQW la condizione più opportuna
     C                   MOVE      *BLANK        £PRZQW           80
     C                   CLEAR                   £PRZQII           3 0
     C* A) Campi alfabetici
1    C     £PRZQI        IFEQ      *BLANK
      * Ricerco valore *HIVAL per lmitare lunghezza QRYSLT
     C                   EVAL      £PRZQII=%SCAN(£HEXFF:£PRZAX:1)
     C                   IF        £PRZQII>0
     C                   EVAL      £PRZAX=%SUBST(£PRZAX:1:£PRZQII)
     C                   ENDIF
      *
2    C                   SELECT
     C* --- no limite inf.: *LE
     C                   WHEN      £PRZAN=*BLANK
     C                             OR %SUBST(£PRZAN:1:1)=*LOVAL
     C     '<="'         CAT       £PRZAX        £PRZQW
     C     £PRZQW        CAT       '"':0         £PRZQW
     C* --- no limite sup.: *GE
     C                   WHEN      £PRZAX=*ALL'9'
     C                             OR %SUBST(£PRZAX:1:1)=*HIVAL
     C     '>="'         CAT       £PRZAN        £PRZQW
     C     £PRZQW        CAT       '"':0         £PRZQW
     C* --- limiti uguali:  *EQ
2x   C                   WHEN      £PRZAN=£PRZAX
     C     '="'          CAT       £PRZAX        £PRZQW
     C     £PRZQW        CAT       '"':0         £PRZQW
     C* --- nessun limite:  *NONE
2x   C                   WHEN      %SUBST(£PRZAN:1:1)=*LOVAL
     C                             AND  %SUBST(£PRZAX:1:1)=*HIVAL
     C                   CLEAR                   £PRZQC
     C* --- limiti diversi: %RANGE
2x   C                   OTHER
     C     '=%RANGE('    CAT       '"'           £PRZQW
     C     £PRZQW        CAT       £PRZAN:0      £PRZQW
     C     £PRZQW        CAT       '" "':0       £PRZQW
     C     £PRZQW        CAT       £PRZAX:0      £PRZQW
     C     £PRZQW        CAT       '")':0        £PRZQW
2e   C                   ENDSL
     C* B) Campi numerici
1x   C                   ELSE
      * elimina gli spazi a sx
     C     ' '           CHECK     £PRZQI        £PRZ$A            5 0
     C                   MOVEL     *BLANKS       PRZINI           30
     C*****              Z-ADD     £PRZ$A        $A                5 0
     C                   SUBST     £PRZQI:£PRZ$A PRZINI
     C     ' '           CHECK     £PRZQF        £PRZ$B            5 0
     C                   MOVEL     *BLANKS       PRZFIN           30
     C*****              Z-ADD     £PRZ$B        $B                5 0
     C                   SUBST     £PRZQF:£PRZ$B PRZFIN
      * Stringhe per convertire eventuali caratteri che mi rappresentano
      * cifre negative
     C                   EVAL      £PRZQI='èJKLMNOPQR'
     C                   EVAL      £PRZQF='0123456789'
      * I numeri negativi vanno chiusi tra parentesi
     C     £PRZNN        IFLT      0
     C     £PRZQI:£PRZQF XLATE     PRZINI        PRZINI
     C                   EVAL      PRZINI='(-'+%TRIM(PRZINI)+')'
     C                   ENDIF
     C     £PRZNX        IFLT      0
     C     £PRZQI:£PRZQF XLATE     PRZFIN        PRZFIN
     C                   EVAL      PRZFIN='(-'+%TRIM(PRZFIN)+')'
     C                   ENDIF
2    C                   SELECT
     C* --- no limite inf.: *LE
2x   C                   WHEN      £PRZNN=0 OR (%ABS(£PRZNN)=
     C                             *ALL'9' AND £PRZNN<0)
     C     '<='          CAT       PRZFIN        £PRZQW
     C* --- no limite sup.: *GE
2x   C     £PRZNX        WHENEQ    *ALL'9'
     C     '>='          CAT       PRZINI        £PRZQW
     C* --- limiti uguali:  *EQ
2x   C     £PRZNN        WHENEQ    £PRZNX
     C     '='           CAT       PRZFIN        £PRZQW
     C* --- limiti diversi: %RANGE
2x   C                   OTHER
     C     '=%RANGE('    CAT       PRZINI:0      £PRZQW
     C     £PRZQW        CAT       PRZFIN:1      £PRZQW
     C     £PRZQW        CAT       ')':0         £PRZQW
2e   C                   ENDSL
1e   C                   ENDIF
     C* Se ricevuto nome campo, modifica stringa qryslt (£przqs)
1    C     £PRZQC        IFNE      *BLANK
     C* --- se non è prima condizione, accoda '&'
2    C     £PRZQS        IFNE      *BLANK
     C     £PRZQS        CAT       '&':1         £PRZQS
2e   C                   END
     C* --- accoda nome campo
     C     £PRZQS        CAT       £PRZQC:1      £PRZQS
     C* --- accoda condizione
     C     £PRZQS        CAT       £PRZQW:0      £PRZQS
     C* --- verifica overflow
     C                   MOVE      £PRZQS        £PRZQC
2    C     £PRZQC        IFEQ      *BLANK
     C                   MOVE      ' '           £PRZQE            1
2x   C                   ELSE
     C                   MOVE      'E'           £PRZQE
     C                   MOVEL     'BAS1010'     £DMSME
     C                   EXSR      £DMSC2
3    C     $4            IFNE      0
     C                   MOVE      '1'           *IN($4)
3x   C                   ELSE
     C                   SETON                                        60
3e   C                   ENDIF
2e   C                   ENDIF
1e   C                   ENDIF
     C* Pulisce campi input
     C                   MOVE      *BLANK        £PRZQC           10
     C                   MOVE      *BLANK        £PRZQI           30
     C                   MOVE      *BLANK        £PRZQF           30
     C                   ENDSR
