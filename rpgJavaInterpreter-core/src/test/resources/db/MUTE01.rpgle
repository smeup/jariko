     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 11/01/21  002090  BUSFIO Creation mute
     V* 11/01/21  002090  BUSFIO Add compration result of two logic file
     V* ==============================================================
     FVERAPG7L  IF   E           K DISK
     FVERAPG8L  IF   E           K DISK    RENAME(VERAPGR:VERAPG8) PREFIX(V8:2)
      *--------------------------------------------------------------------------------------------*
     D $$NOME          S             15                                           Collaborator
     D $$COMM          S             15                                           Order
     D ARRDT1          S              8  0 DIM(999)                               Array date
     D $N              S              3  0                                        Counter Array
     D $NDO            S              3  0                                        Counter DO
      *-/COPY £PDS --------------------------------------------------------------------------------*
      * Variabili di contesto
     D ££ATCO          S              1                                         Attivazione contesto
     D ££CONT          S             10                                         Contesto
     D ££AMBI          S             10                                         Ambiente
     D ££DATE          S              8  0 INZ                                  Data Riferimento
     D*----------------------------------------------------------------
      * Utente applicativo (valorizzato da B£INZ0/£INZSR)
     D*£PDSNU          S             10
     D*----------------------------------------------------------------
      * DS esterna per dialogo pgm
     D £PDSDS          DS          2000
      * Variabile di definizione VARYING
     D**** £PDSVY          S          30000    VARYING
      * Nr elementi nelle schiere campi gestite nei visualizzatori £VS (V5TDOC etc.)
     D*£VSELE          C                   CONST(400)
      *----------------------------------------------------------------
      * DS di inizializzazione ambiente programma
     D £INZDS          DS           512    INZ
      * Ds tabella B£1/B£2
     D  £INZ£1                 1    100
     D  £INZ£2               101    200
      * Dati temporali istantanei (considerano time zone da B£2)
      * ..ora HHMMSS (6,0)
     D  £UTIME               201    206  0
      * ..anno AA (2,0)
     D  UYEAR£               207    208  0
      * ..anno AAAA (4,0)
     D  UYEAS£               209    212  0
      * ..mese MM (2,0)
     D  UMONT£               213    214  0
      * ..giorno GG (2,0)
     D  UDAY£                215    216  0
      * ..data AAMMGG (6,0)
     D  £UDAMG               217    222  0
      * ..data AAAAMMGG (8,0)
     D*  £UDSMG               223    230  0
      * ..data GGMMAA/MMGGAA (6,0) in formato definito B£2
     D  £UDGMA               231    236  0
      * ..data GGMMAA (6,0) in formato fisso europeo
     D  £UDEUA               237    242  0
      * ..data GGMMAAAA/MMGGAAAA (8,0) in formato definito B£2
     D  £UDGMS               243    250  0
      * ..data GGMMAAAA (8,0) in formato fisso europeo
     D  £UDEUS               251    258  0
      * Job type / subtype
     D  £INZJT               259    259
     D  £INZJS               260    260
      * Nome job che ha effettuato la sottomissione
      * ..job
     D  £INZSJ               261    270
      * ..utente
     D  £INZSU               271    280
      * ..numero
     D  £INZSN               281    286
      * Esecuzione in multipiattaforma
     D  £INZMU               287    287
      * Utente applicativo
     D  £INZUA               288    297
      * Esecuzione in ambiente con shift-in e shift-out (tipicamente DBCS)
     D  £INZDB               298    298
      *----------------------------------------------------------------
      * DS di comodo per definizione stringa qryslt
      *  (impostata da £przq ed utilizzata da opnqryf)
     D £PRZQS          DS           300
      *----------------------------------------------------------------
      * Definizione campi fissi di LDA
      * ------------------------------
     D £UDLDA          DS
      *
      * Sottocampi di £PRZQS
     D  £PRZQ1               597    696
     D  £PRZQ2               697    896
      * Nome membro ambiente di lavoro
     D  £UDMBR               897    906
      * Descrizione ambiente di lavoro
     D  £UDDMB               907    936
      * Nome job lanciante (terminale)
     D  £UDNJL               937    946
      * Nome programma da lanciare
     D  £UDNPG               947    956
      * Tipo stampa del programma:
      *      0     : No stampa
      *      <>0   : Stampa
     D  £UDTPS               957    959  0
      * Esecuzione in interattivo
     D  £UDJON               960    960
      * Nome coda di lavoro
     D  £UDJQN               961    970
      * Priorità in coda lavori
     D  £UDJQP               971    971  0
      * Congelamento in coda lavori
     D  £UDJQH               972    972
      * Data schedulazione
     D  £UDJQD               973    981
      * Ora schedulazione
     D  £UDJQT               982    985
      *  986/987 usato dai flussi per SS B£J        B£GFP15
      *  988/992 usato dai flussi per codice azione
      * Tipo utilizzo B£UT50  ' ' Utente  'G' azione di gruppo
     D  £UDU5T               993    993
      * Codice ambiente B£UT55   Suffisso elemento  B§1
     D  £UDU5A               994    998
      * Coda di stampa
     D  £UDO2N               999   1008
      * Priorità stampa
     D  £UDO2P              1009   1009  0
      * Congelamento stampa
     D  £UDO2H              1010   1010
      * Salvataggio stampa
     D  £UDO2S              1011   1011
      * Numero copie
     D  £UDO2C              1012   1013  0
      * Nome modulo
     D  £UDO2F              1014   1023
      * Stampa immediata
     D  £UDO2I              1024   1024
      *--------------------------------------------------------------------------------------------*
      * Parte di XML con il "payload" (senza header, ecc.)
     D  $$XMLPAYLOAD   S          30000
      * XML da spedire sulla coda
     D  $$XML          S          30000
      * Parametro XML
     D  £JAXXML        S          15000
     D                SDS
     D*
      *                  Nome del programma
     D  £PDSNP                 1     10
      *                  Specifica ILE in errore
     D  £PDSSI                21     28
      *                  ..nome modulo per oggetti ILE
      *                    (viene invertito in £INZSR)
     D  £PDSNM               334    343
      *                  Libreria del programma
     D  £PDSNL                81     90
      *                  Message ID
     D  £PDSMI                40     46
      *                  Message Text
     D  £PDSTX                91    170
      *                  Status CODE
     D  £PDSSC           *STATUS
      *                  Nome del file per errore
     D  £PDSNF               201    208
      *                  Informazioni file
     D  £PDSIF               209    243
      *                  Nome del lavoro (terminale se interattivo)
     D  £PDSJN               244    253
      *                  Nome dell'utente (di sistema)
     D  £PDSSU               254    263
      *                  Numero del lavoro
     D  £PDSJZ               264    269  0
      *                  Numero parametri passati dall'esterno
     D  £PDSPR           *PARMS
      *--------------------------------------------------------------------------------------------*
      * Initial settings
     C                   EXSR      IMP0
      * Execute subroutine
     C                   EXSR      CHKRES
      *
     C     G9MAIN        TAG
      *
     C                   EXSR      FIN0
      *
     C                   SETON                                        RT
      *--------------------------------------------------------------*
    RD*  Initial Settings
      *--------------------------------------------------------------*
     C     IMP0          BEGSR
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD*  Compare result logic file
      *--------------------------------------------------------------*
     C     CHKRES        BEGSR
      * Create key
     C     K7L           KLIST
     C                   KFLD                    $$COMM
     C                   KFLD                    $$NOME
      *
      * Set variables
     C                   EVAL      $$COMM='SMEGL.001'
     C                   EVAL      $$NOME='BUSFIO'
     C                   CLEAR                   ARRDT1
     C                   EVAL      $N=1
     C                   EVAL      $NDO=0
      * Read cicle VERAPG7L
     C     K7L           SETLL     VERAPG7L
     C                   DO        *HIVAL
    MU* VAL1($$COMM) VAL2(V£CDC) COMP(EQ)
    MU* VAL1($$NOME) VAL2(V£NOME) COMP(EQ)
     C     K7L           READE     VERAPG7L
      * End of File - Exit
4x   C                   IF        %EOF
     C                   LEAVE
4e   C                   ENDIF
      * Counter equal 100
4x   C                   IF        $NDO=100
     C                   LEAVE
4e   C                   ENDIF
      *
     C     V£DATA        DSPLY     £PDSSU
     C     V£NOME        DSPLY     £PDSSU
     C     V£CDC         DSPLY     £PDSSU
      *
     C                   EVAL      ARRDT1($N)=V£DATA
     C                   EVAL      $N=$N+1
     C                   EVAL      $NDO=$NDO+1
      *
3e   C                   ENDDO
      *
      * Create key
     C     K8L           KLIST
     C                   KFLD                    $$NOME
     C                   KFLD                    $$COMM
      * Initialization variables
     C                   EVAL      $N=1
     C                   EVAL      $NDO=0
      * Read cicle VERAPG8L
     C     K8L           SETLL     VERAPG8
     C                   DO        *HIVAL
    MU* VAL1(V8DATA) VAL2(ARRDT1($N)) COMP(EQ)
     C     K8L           READE     VERAPG8
      *
     C     V8DATA        DSPLY     £PDSSU
     C     ARRDT1($N)    DSPLY     £PDSSU
      * End of File - Exit
4x   C                   IF        %EOF
     C                   LEAVE
4e   C                   ENDIF
      * Counter equal 100
4x   C                   IF        $NDO=100
     C                   LEAVE
4e   C                   ENDIF
      *
     C                   EVAL      $N=$N+1
     C                   EVAL      $NDO=$NDO+1
      *
3e   C                   ENDDO
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD*  Close and send missing buffer on queue
      *--------------------------------------------------------------*
     C     FIN0          BEGSR
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Settings routine for start program
      *--------------------------------------------------------------*
     C     *INZSR        BEGSR
      *
     C                   ENDSR
