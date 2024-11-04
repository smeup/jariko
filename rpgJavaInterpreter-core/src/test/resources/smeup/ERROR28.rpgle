      * This program currently cause a stackoverflow error in compilation
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : MU711003
      * Sorgente di origine : SMEUP_DEM/SRC(MU711003)
      * Esportato il        : 20240711 153121
      *====================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 23/03/24  MUTEST  GUAGIA Creazione
     V*=====================================================================
    O *  OBIETTIVO
    O * Testare la sostituzione di /API con /COPY
    O *
    O * NB: Su AS400 per compilarli sostituire l'istruzione /API con /COPY e aggiungere
      *     la copy £HEX (compilando con il 14 non viene inclusa dal precompilatore).
      *     Una volta creato l'oggetto, rimodificare il sorgente ed esportarlo
    O *
     V* ==============================================================
     D A71_01          S              1
     D A71_02          S              1
     D A71_03          S              1
      * --------------------------------------------------------------
********** PREPROCESSOR COPYSTART QILEGEN,MULANG_D_D
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : MULANG_D_D
      * Sorgente di origine : SMEUP_DEM/QILEGEN(MULANG_D_D)
      * Esportato il        : 20240712 164923
      *====================================================================
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 04/10/23  V6.R1   LS Creato
     V* 30/11/26  005315  BERNI aggiunto campo a DS per gestione MDV
     V* 30/11/23  V6R1    ARRSTE Merge 005315 in SMEUP_DEM
     V* 04/12/23  DEM     BUSFIO Modificate £DBG_Str e £DBG_O_Str in varying
     V*=====================================================================
      * Per registrazione dei dati di un passo
     D £DBG_DS         DS
     D £DBG_Fun                      10                                         Funzione
     D £DBG_Pgm                      10                                         Programma
     D £DBG_Sez                      10                                         Sezione
     D £DBG_Pas                      10                                         Passo
     D £DBG_Str                    2560    VARYING                              Stringa
     D £DBG_Ind                      99                                         Indicatori
     D £DBG_Mdv                      18                                         Gestione MDV
      * Per chiamata dei passi singoli dove previsto
     D £DBG_I_Fun      S             10                                         Funzione chiamata
     D £DBG_I_Num      S              7  0                                      Numero esecuzioni
     D £DBG_I_Par      S            256                                         Parametri
     D £DBG_O_Str      S           2560    VARYING                              Risultato
     D £DBG_O_Msg      S             10                                         Messaggio
      *
     D £DBG_TIMINI     S               Z
     D £DBG_TIMEND     S               Z
     D £DBG_SAVSEZ     S             10
     D £DBG_SAVSCHPGM  S              7  0 DIM(10)
     D £DBG_SAVINI     S              1
      *
********** PREPROCESSOR COPYEND QILEGEN,MULANG_D_D
********** PREPROCESSOR COPYSTART QILEGEN,£TABB£1DS
      *IF NOT DEFINED(£TABB£1DS)
      *DEFINE £TABB£1DS
      *      PERSONALIZZAZIONE B£
     D B£1$DS          DS           100
      * N° 03 - Nome azienda                       15
     D  £RASDI                 1     15
      * N° 04 - Clienti              TA *CNAA       2
     D  ££ANCL                16     17
      * N° 05 - Fornitori            TA *CNAA       2
     D  ££ANFO                18     19
      * N° 06 - Conti Co.Ge.         TA *CNAA       2
     D  ££PICO                20     21
      * N° 07 - Articoli             TA *CNAA       2
     D  ££ANAR                22     23
      * N° 08 - St.magaz.            TA *CNAA       2
     D  ££SAMG                24     25
      * N° 09 - Costi std            TA *CNAA       2
     D  ££COST                26     27
      * N° 10 - Ge.vendite           TA *CNAA       2
     D  ££STVE                28     29
      * N° 11 - Ge.acquisti          TA *CNAA       2
     D  ££STAC                30     31
      * N° 12 - St.produz.           TA *CNAA       2
     D  ££STPR                32     33
      * N° 13 - Ge.produz.           TA *CNAA       2
     D  ££GEPR                34     35
      * N° 14 - Ce.costo             TA *CNAA       2
     D  ££CDCO                36     37
      * N° 15 - Vo.spesa             TA *CNAA       2
     D  ££VDSP                38     39
      * N° 16 - Ce.lavoro            TA *CNAA       2
     D  ££CDLA                40     41
      * N° 17 - Ge.personale         TA *CNAA       2
     D  ££GEPE                42     43
      * N° 18 - Macchine             TA *CNAA       2
     D  ££MACC                44     45
      * N° 19 - Ubicazioni           TA *CNAA       2
     D  ££UBIC                46     47
      * N° 20 - Lotti/rintrac.       TA *CNAA       2
     D  ££LOTT                48     49
      * N° 21 - Attrezzature         TA *CNAA       2
     D  ££ATTR                50     51
      * N° 22 - D/Base               TA *CNAA       2
     D  ££DIBA                52     53
      * N° 23 - Cicli lav.           TA *CNAA       2
     D  ££CILA                54     55
      * N° 24 - Commesse             TA *CNAA       2
     D  ££GCOM                56     57
      * N° 25 - Giacenza             TA *CNAA       2
     D  ££ANSA                58     59
      * N° 26 - Colli/confez.        TA *CNAA       2
     D  ££IFLC                60     61
      * N° 27 - Magazzino            TA *CNAA       2
     D  ££IFMG                62     63
      * N° 28 - R.acquisto           TA *CNAA       2
     D  ££IFPC                64     65
      * N° 29 - Matricole            TA *CNAA       2
     D  ££IFMT                66     67
      * N° 30 - Esp.mod.             TA *CNAA       2
     D  ££ESMO                68     69
      * N° 31 - Impianti             TA *CNAA       2
     D  ££IFMM                70     71
      * N° 32 - Cespiti              TA *CNAA       2
     D  ££IFCE                72     73
      * N° 33 - Cambi                TA *CNAA       2
     D  ££CMBV                74     75
      * N° 34 - Par.Pianif.          TA *CNAA       2
     D  ££B£1A                76     77
      * N° 35 - Rich.movim.          TA *CNAA       2
     D  ££B£1B                78     79
      * N° 36 - Viaggi               TA *CNAA       2
     D  ££B£1C                80     81
      * N° 37 - Co.Generale          TA *CNAA       2
     D  ££B£1D                82     83
      * N° 38 - Co.Analitica         TA *CNAA       2
     D  ££B£1E                84     85
      * N° 39 - Non conformità       TA *CNAA       2
     D  ££B£1F                86     87
      * N° 40 - Imp.Risorse          TA *CNAA       2
     D  ££B£1G                88     89
      * N° 41 - Rich.Interv.         TA *CNAA       2
     D  ££B£1H                90     91
      *      PERSONALIZ. (IN CODA A B£1)
     D B£2$DS          DS           100
      * N° 03 - U.Mis.fraz.tempo C/S                1
     D  ££UMHR                 1      1
      * N° 04 - Arrot. ore (minuti)  NR             2
     D  ££PAMI                 2      3  0
      * N° 05 - Magazzino unico      MG             3
     D  ££MAGD                 4      6
      * N° 06 - Att.LOG util.oggetti                2
     D  ££B£2L                 7      8
      * N° 07 - Att.PROTEZ.Liv.campo                2
     D  ££B£2P                 9     10
      * N° 08 - Att.SCENARI multipli                2
     D  ££B£2S                11     12
      * N° 09 - Azienda              AZ             2
     D  ££B£2A                13     14
      * N° 10 - Modo present.istanti V2 TI_I1       1    1
     D  ££B£2B                16     16
      * N° 11 - Escl.contr.comp.st.  V2 SI/NO       1
     D  ££B£2C                17     17
      * N° 12 - Cod.divisa corrente  TA VAL         4
     D  ££B£2D                18     21
      * N° 13 - Cod.divisa Euro      TA VAL         4
     D  ££B£2E                22     25
      * N° 14 - Descr.estesa assunta TA LIN         3
     D  ££B£2F                26     28
      * N° 15 - Magazzino competenza MG             3
     D  ££B£2G                29     31
      * N° 16 - Nazione assunta      TA V§N         6
     D  ££B£2H                32     37
      * N° 17 - Lingua assunta       TA V§L         3
     D  ££B£2I                38     40
      * N° 18 - Formato data         V2 FMDAT       1
     D  ££B£2M                41     41
      * N° 19 - Divisa alternativa   TA VAL         4
     D  ££B£2N                42     45
      * N° 20 - Divisore             V2 SI/NO       1
     D  ££B£2O                46     46
      * N° 21 - Tipo cambio          TA TCA         3
     D  ££B£2Q                47     49
      * N° 22 - Separatore decimale  **             1
     D  ££B£2R                50     50
      * N° 23 - Time Zone As400      NR             4 2
     D  ££B£2T                51     54  2
      * N° 24 - Time Zone Utenti     NR             4 2
     D  ££B£2U                55     58  2
      * N° 25 - Suff.Agg.     £INZDS **             1
     D  ££B£2V                59     59
      * N° 26 - Costruz.automatica   V2 SI/NO       1
     D  ££B£2W                60     60
      * N° 27 - Nuova gest.azioni    **             1
     D  ££B£2X                61     61
      * N° 28 - Gest.Contesto        V2 SI/NO       1
     D  ££B£2Y                62     62
      * N° 29 - Ambiente di Test     V2 SI/NO       1
     D  ££B£2Z                63     63
      * N° 30 - Abilita Monitor API  V2 SI/NO       1
     D  ££B£2J                64     64
      * N° 31 - Suff.Agg.Amb. B£1/2  **             1
     D  ££B£2K                65     65
      * N° 32 - Attiva RCLRSC        V2 SI/NO       1
     D  ££B£20                66     66
      * N° 33 - Attiva mascheramento V2 SI/NO       1
     D  ££B£21                67     67
      * N° 34 - Fuso orario utenti   OJ *TIMZON    10
     D  ££B£22                68     77
      *ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£TABB£1DS
********** PREPROCESSOR COPYSTART QILEGEN,£DMSE
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £DMSE
      * Sorgente di origine : SMEDEV/QILEGEN(£DMSE)
      * Esportato il        : 20240715 131100
      *====================================================================
      *IF NOT DEFINED(DMSE_INCLUDED)
      *DEFINE DMSE_INCLUDED
      *
      *
      *  /COPY da inserire insieme con: £DMS
      *
      *----------------------------------------------------------------
     D £D1             S             17    DIM(100)                             Cod.Msg + Msgf
     D £D2             S             45    DIM(100)                             Variabili ass.
     D £D3             S             80    DIM(100)                             Txt schiera
     D £D4             S              1    DIM(45)                              Segmenta in 45
      *ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£DMSE
********** PREPROCESSOR COPYSTART QILEGEN,£DMSI
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £DMSI
      * Sorgente di origine : SMEDEV/QILEGEN(£DMSI)
      * Esportato il        : 20240715 131100
      *====================================================================
      *IF NOT DEFINED(DMSI_INCLUDED)
      *DEFINE DMSI_INCLUDED
      *
      * 1° Messaggio di errore
      *
     D £DMS1M          DS           132
      *----------------------------------------------------------------
      *
      * Messaggio 1 livello
      *
     D £DMS1L          DS           132
      *----------------------------------------------------------------
      *
      * Messaggio 2 livello
      *
     D £DMS2L          DS           500
      *----------------------------------------------------------------
      *
      * STRINGA VARIABILI:
      * 512 bytes per gestire le variabili interne al messaggio (1o/2o)
      *
     D £DMSST          DS           512
      *----------------------------------------------------------------
      *ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£DMSI
********** PREPROCESSOR COPYSTART QILEGEN,£PDS
      *IF NOT DEFINED(£PDS)
      *DEFINE £PDS
     D* ==============================================================
     D* MODIFICHE Ril.  T Au Descrizione
     D* gg/mm/aa  nn.mm i xx Breve descrizione
     D* ==============================================================
     D* B£90630A  02.00   MA Aggiunto campo £PDSDS
     D* B£20806A  05.00   ZZ Aggiunta ds £INZDS inizializzazione pgm
     D* 16/04/03  05.00   ZZ Agg. identificativi job che ha sottomesso
     D* 24/12/04  V2R1    MA il 02.12.03 sono stati inseriti i campi £PDSSI
     D*                      e £PDSNL ed i campi 986/992 non sono liberi
     D*                    ->  Definiti campi £UDU5T e £UDU5A
     D* 10/09/07  V2R3    BS Aggiunte variabili di contesto/ambiente/data riferimento
     D* 17/09/07  V2R3    BS Variabile di attivazione del contesto
     D* 21/03/08  V2R3    BS Aggiunta Variabile VARYING solo per ripresa definizione (Vedi £G25)
     D* 15/09/10  V3R1    MA Aggiunta Variabile £VSELE (numero elementi campi visualizzatori)
     D* 03/12/15  V4R1    AS Eliminata variabile inutilizzta £PDSVY
     D* 23/09/16  V5R1    AS Aggiunta £INZMU (esecuzione in multipiattaforma)
     D* 28/10/16  V5R1   BMA Aggiunto £PDSMI (Message ID)
     V* A£61103A  V5R1    AS Disaccoppiamento utente di sistema da utente applicativo
     V* A£61103A  V5R1   BMA Modificato commento
     V* 21/06/17  V5R1    AS Aggiunto £INZDB (Esecuzione in ambiente con shift-in e shift-out)
     D*----------------------------------------------------------------
     D* OBIETTIVO
     D*  Copy che definisce i campi della DS che contiene
     D*  i dati principali dello stato del programma, la definizione
     D*  della UDS ed i suoi campi standard.
     D*
     D*  Va inserita in tutti i programmi, tra le specifiche I, nella
     D*  zona delle DS, prima dei sottocampi della LDA.
     D*----------------------------------------------------------------
      * Inz
     D £INZFU          S             10    INZ('INZ')
     D £INZME          S             10
     D £INZCO          S             10
      * Variabili di contesto
     D ££ATCO          S              1                                         Attivazione contesto
     D ££CONT          S             10                                         Contesto
     D ££AMBI          S             10                                         Ambiente
     D ££DATE          S              8  0 INZ                                  Data Riferimento
     D*----------------------------------------------------------------
      * Utente applicativo (valorizzato da B£INZ0/£INZSR)
     D £PDSNU          S             10
     D*----------------------------------------------------------------
      * DS esterna per dialogo pgm
     D £PDSDS          DS          2000
      * Variabile di definizione VARYING
     D**** £PDSVY          S          30000    VARYING
      * Nr elementi nelle schiere campi gestite nei visualizzatori £VS (V5TDOC etc.)
     D £VSELE          C                   CONST(400)
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
     D  £UDSMG               223    230  0
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
      *ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£PDS
      *---------------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------------
     C                   EVAL      £DBG_Pgm = 'MU711003'
     C                   EVAL      £DBG_Sez = 'A10'
     C                   EVAL      £DBG_Fun = '*INZ'
     C                   EXSR      £DBG
     C                   EXSR      SEZ_A10
     C                   EXSR      £DBG
     C                   EVAL      £DBG_Fun = '*END'
     C                   EXSR      £DBG
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Test atomico /COPY £OAV
      *---------------------------------------------------------------------
     C     SEZ_A10       BEGSR
    OA* A£.CDOP(/API)
     D* Testare la sostituzione di /API con /COPY nel caso £PRZ (deve includere £HEX)
     C                   EVAL      £DBG_Pas='P03'
      * testo che le definizioni della £HEX ci siano
     C                   EVAL      A71_01=£HEX0E
     C                   EVAL      A71_02=£HEX3F
     C                   EVAL      A71_03=£HEXFF
     C                   EVAL      £DBG_Str='A71_01('+A71_01+') '+
     C                                      'A71_02('+A71_02+') '+
     C                                      'A71_03('+A71_03+') '
      *
     C                   ENDSR
      *---------------------------------------------------------------------
********** PREPROCESSOR COPYSTART QILEGEN,MULANG_D_C
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : MULANG_D_C
      * Sorgente di origine : SMEUP_DEM/QILEGEN(MULANG_D_C)
      * Esportato il        : 20240712 164923
      *====================================================================
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 04/10/23  V6.R1   LS Creato
     V*=====================================================================
      *---------------------------------------------------------------------
    RD* Registrazione del debug
      *---------------------------------------------------------------------
     C     £DBG          BEGSR
     C* TODO Gestire *IN in JARIKO
     C*                  MOVEA(P)  *IN           £DBG_Ind
      *
1    C                   IF        £DBG_Fun='*INZ' OR
     C                             £DBG_SAVINI=''
     C                   TIME                    £DBG_TIMINI
     C                   EVAL      £DBG_SAVINI='1'
1e   C                   ENDIF
     C                   TIME                    £DBG_TIMEND
      *
     C                   CALL      'MULANG_DBG'
     C                   PARM                    £DBG_DS
     C                   PARM                    £DBG_TIMINI
     C                   PARM                    £DBG_TIMEND
     C                   PARM                    £DBG_SAVSEZ
     C                   PARM                    £DBG_SAVSCHPGM
      *
     C                   TIME                    £DBG_TIMINI
      *
     C                   CLEAR                   £DBG_Fun
     C                   CLEAR                   £DBG_Str
     C                   CLEAR                   £DBG_Ind
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Registrazione del debug
      *---------------------------------------------------------------------
     C     £DBG_CA1      BEGSR
     C                   CALL      'MULANGTA1'
     C                   PARM                    £DBG_I_Fun
     C                   PARM                    £DBG_I_Num
     C                   PARM                    £DBG_I_Par
     C                   PARM                    £DBG_O_Str
     C                   PARM                    £DBG_O_Msg
     C                   ENDSR
********** PREPROCESSOR COPYEND QILEGEN,MULANG_D_C
      /API QILEGEN,£PRZ
********** PREPROCESSOR COPYSTART QILEGEN,£DMS
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £DMS
      * Sorgente di origine : SMEDEV/QILEGEN(£DMS)
      * Esportato il        : 20240715 131102
      *====================================================================
      *IF NOT DEFINED(DMS_INCLUDED)
      *DEFINE DMS_INCLUDED
      *
      *****************************************************************
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* B£00722A  V3R1    CM Rimozione Gestione Interna degli errori
     V* 12/02/11  V3R1    BS Aggiunte Specifiche per controllo inclusione
    RD* £DMSC1 --> Pulizia iniziali variabili e aree di lavoro
     D*
     D* SCOPO DELLA ROUTINE:
     D* RINFRESCA TUTTE LE VARIABILI PRIMA DI INIZIARE I CONTROLLI
     D*
     D* Valori in uscita:
     D* -----------------
     D* . £D    : Puntatore
     D* . £2    : Puntatore £D2
     D* . £4    : Puntatore £D4
     D* . £D1   : schiera contenente i codici messaggio
     D* . £D2   : schiera contenente le variabili da associare al msg
     D* . £D3   : schiera contenente i testi dei messaggi INTERNI al pgm
     D* . £D4   : schiera contenente 45 bytes da concatenare
     D* . £DMSNR: Valore puntatore
     D* . £DMSTR: Retrive da Msgf (X o G)
     D* . £DMSTP: Reperimento messaggio (Da Msgf o da Pgm)
     D*           (Se i primi 3 byte sono = 'PGM' rilevo il messaggio
     D*            dalla schiera)
     D* . £DMSME: Codice msg
     D* . £DMSVA: Stringa valori delle variabili inserite nel messaggio
     D* . £DMSEL: Testo definito nella schiera messaggi
     D* . £DMSFI: Message file AS/400
     D* . £DMSVS: Flag guida per la segnalazione degli errori:
     D*           ' ' = A Video
     D*           'V' = A Video
     D*           'S' = Su Stampa
     D*
     D* Specifiche I inizializzazione DS:
     D* ---------------------------------
     D* . £DMS1M: Testo 1° messaggio di errore
     D* . £DMS1L: Variabile utilizzata per impostare testo 1 livello
     D* . £DMS2L: Variabile utilizzata per impostare testo 2 livello
     D* . £DMSST: Contenuto: concatenazione variabili in stringa
     D*
      *****************************************************************
      *
     C     £DMSC1        BEGSR
     C                   Z-ADD     0             £D                5 0          Puntatore
     C                   Z-ADD     0             £2                5 0          Puntatore
     C                   Z-ADD     0             £4                5 0          Puntatore
     C                   MOVEL     *BLANKS       £D1                            Sch. Msg
     C                   MOVEL     *BLANKS       £D2                            Sch. Var.
     C                   MOVEL     *BLANKS       £D3                            Sch. Txt pgm
     C                   MOVEL     *BLANKS       £D4                            Sch. 45 byts
     C                   Z-ADD     0             £DMSNR            5 0          Valore punt.
     C                   Z-ADD     0             £DMS2             5 0          "          "
     C                   Z-ADD     0             £DMS4             5 0          "          "
     C                   Z-ADD     0             £DMSLA            2 0          Lunghezza
     C                   Z-ADD     0             £DMSLN            2 0          "       "
     C                   MOVEL     *BLANKS       £DMSTR            1            Retrive Msgf
     C                   MOVEL     *BLANKS       £DMSTP            3            Msgf/schiera
     C                   MOVEL     *BLANKS       £DMSME            7            Codice msg
     C                   MOVEL     *BLANKS       £DMSVA           45            Stringa
     C                   MOVEL     *BLANKS       £DMSEL           80            Txt schiera
     C                   MOVEL     *BLANKS       £DMSFI           10            Msgf
     C                   MOVEL     *BLANKS       £DMSVS            1            Video/Stampa
      *
     C                   MOVEL     *BLANKS       £DMS1M                         DS
     C                   MOVEL     *BLANKS       £DMS2L                         DS
     C                   MOVEL     *BLANKS       £DMS2L                         DS
     C                   MOVEL     *BLANKS       £DMSST                         DS
      *
     C                   ENDSR
      *
      *****************************************************************
    RD* £DMSC2 --> Registrazione errori
     D*
     D* SCOPO DELLA ROUTINE:
     D* REGISTRAZIONE ERRORI
     D*
     D* Se richiesto reperimento da Msgf i primi 3 bytes non devono
     D* essere uguali a 'PGM'.
     D* . REGISTRA IL MESSAGGIO DI ERRORE E LE VARIABILI ASSOCIATE
     D* . REGISTRA IL TESTO DI PRIMO LIVELLO
     D* . REGISTRA LE VARIABILI ASSOCIATE
     D*   (£D1 e £D2)
     D*
     D* Se richiesto reperimento da schiera Messaggi, i primi tre
     D* bytes devono essere uguali a 'PGM'
     D* . REGISTRA NEL COD. MESSAGGIO I PRIMI 7 BYTES DELLA SCHIERA
     D* . REGISTRA NEL TESTO DI PRIMO LIVELLO IL TESTO DELLA SCHIERA
     D*   (£D1 e £D3)
     D*
     D* Se riceve un valore in £DMSIE si attiva la gestione delle
     D* segnalazioni WARNING (Finestra in funzione della gravita')
     D*
     D* Valori in entrata:
     D* ------------------
     D* . £DMSME: Codice messaggio (MSGF XXXnnnn)
     D* . £DMSFI: File messaggi (se blank, assume MSGBA)
     D* . £DMSVA: Stringa di 45 bytes contenente le variabili ancora
     D*           da concatenare nello stesso ordine in cui sono state
     D*           elencate nel testo del messaggio.
     D*           (Da &1 a &99)
     D* . £DMSEL: Testo definito nella schiera messaggi
     D* . £DMSIE: Indicatore di errore per gestione WARNING
     D*
     D* Valori di lavoro:
     D* ------------------
     D* . £D    : Puntatore schiera
     D* . £DMSNR: Valore Puntatore schiera
     D* . £DMSTP: Guida il tipo di reperimento del messaggio (Msgf/Pgm)
     D*           (Se i primi 3 byte sono = 'PGM' rilevo dalla schiera;
     D*            altrimenti dal Message file AS/400)
     D*****************************************************************
      *
     C     £DMSC2        BEGSR
1    C     £DMSNR        IFLT      100
2    C     £DMSFI        IFEQ      *BLANK
     C                   MOVEL(P)  'MSGBA'       £DMSFI
2e   C                   ENDIF
2    C     £DMSTP        IFEQ      *BLANKS
     C                   MOVEL     £DMSME        £DMSTP
2e   C                   ENDIF
     C                   ADD       1             £DMSNR            5 0          Puntatore
     C                   Z-ADD     £DMSNR        £D
2    C     £DMSTP        IFEQ      'PGM'
     C                   MOVEL     *BLANKS       £D1(£D)                        ------------
     C                   MOVEL     £DMSME        £D1(£D)                        Elemeto sch.
     C                   MOVEL     *BLANKS       £D2(£D)                        ------------
     C                   MOVEL     £DMSEL        £D3(£D)                        Txt schiera
2x   C                   ELSE
     C                   MOVEL     £DMSME        £D1(£D)                        Codice Msg
     C                   MOVE      £DMSFI        £D1(£D)                        Msgf
     C                   MOVEL     £DMSVA        £D2(£D)                        Variabili
     C                   MOVEL     *BLANKS       £D3(£D)                        ------------
2e   C                   ENDIF
      * Errori di tipo WARNING
2    C     £DMSIE        IFNE      0
     C     £DMSVS        ANDNE     'S'
     C                   CALL      'B£DMS1'
     C                   PARM                    £D1
     C                   PARM                    £D2
     C                   PARM                    £D3
     C                   PARM                    £PDSNP
     C                   PARM                    £D
     C                   PARM                    £DMSIE
3    C     £DMSIE        IFEQ      0
     C                   MOVEL     *BLANKS       £D1(£D)
     C                   MOVEL     *BLANKS       £D2(£D)
     C                   MOVEL     *BLANKS       £D3(£D)
     C                   SUB       1             £DMSNR
3x   C                   ELSE
     C                   Z-ADD     £DMSIE        £D
     C                   MOVE      '1'           *IN(£D)
3e   C                   ENDIF
2e   C                   ENDIF
     C                   Z-ADD     0             £DMSIE            2 0
      *
     C                   MOVEL     *BLANKS       £DMSME                         Codice msg
     C                   MOVEL     *BLANKS       £DMSTP                         Msgf/schiera
     C                   MOVEL     *BLANKS       £DMSFI                         Msgf
     C                   MOVEL     *BLANKS       £DMSVA                         Stringa
     C                   MOVEL     *BLANKS       £DMSEL                         Txt schiera
1e   C                   ENDIF
     C     £DMS2X        ENDSR
      *
      *****************************************************************
    RD* £DMSC3 --> Elabora messaggi memorizzati
     D*
     D* SCOPO DELLA ROUTINE:
     D* ELABORA TUTTI I MESSAGGI REGISTRATI NELLA SCHIERA £D1
     D*
     D* . £D1   : schiera contenente i codici messaggio
     D* . £D2   : schiera contenente le variabili da associare al msg
     D* . £D3   : schiera contenente i testi dei messaggi da pgm
     D* . £PDSNP: Nome programma
     D*
     D* Valori in entrata:
     D* ------------------
     D* . £DMSVS: Flag guida per la segnalazione degli errori:
     D*           ' ' = A Video
     D*           'V' = A Video
     D*           'S' = Su Stampa
     D*
      *****************************************************************
      *
     C     £DMSC3        BEGSR
1    C     £D1(1)        IFNE      *BLANKS
2    C     £DMSVS        IFEQ      'S'
     C                   CALL      'B£DMS2'
     C                   PARM                    £D1
     C                   PARM                    £D2
     C                   PARM                    £D3
     C                   PARM                    £PDSNP           10
2x   C                   ELSE
     C                   CALL      'B£DMS1'
     C                   PARM                    £D1
     C                   PARM                    £D2
     C                   PARM                    £D3
     C                   PARM                    £PDSNP           10
2e   C                   ENDIF
1x   C                   ELSE
     C                   MOVEL(P)  'VIS'         £DMS7F
     C                   EXSR      £DMSC7
     C                   EXSR      £DMSC8
1e   C                   ENDIF
     C     £DMS3X        ENDSR
      *
      *****************************************************************
    RD* £DMSC4 --> Emissione 1° messaggio
     D*
     D* SCOPO DELLA ROUTINE:
     D* PREPARA IL 1° MESSAGGIO PER L'EMISSIONE A VIDEO
     D*
     D* Valori di lavoro:
     D* -----------------
     D* . £DMSTR: Tipo retrive
     D* . £D    : Puntatore schiera
     D*
     D* Valori di uscita:
     D* -----------------
     D*
     D* - Sempre
     D*   . £DMS1M: testo del 1° messaggio di errore
     D*
     D* - A disposizione:
     D*   - Se richiesto reperimento da Msgf:
     D*     . £DMS1L: testo 1 livello
     D*     . £DMS2L: testo secondo livello
     D*
     D*   - Se richiesto reperimento da schiera Messaggi:
     D*     . £DMSME: codice messaggio
     D*     . £DMSEL: testo di errore (80 caratteri)
     D*
      *****************************************************************
      *
     C     £DMSC4        BEGSR
     C     £D1(1)        CABEQ     *BLANKS       £DMS4X
      *
     C                   MOVEL     *BLANKS       £DMS1M
      *
      * Da msgf
      *
1    C     £D3(1)        IFEQ      *BLANKS
     C                   MOVEL     'X'           £DMSTR
     C                   Z-ADD     1             £D
     C                   EXSR      £DMSC5
     C                   MOVEL     £DMS1L        £DMS1M
1x   C                   ELSE
      *
      * Da schiera messaggi
      *
     C                   MOVEL     *BLANKS       £DMSME
     C                   MOVEL     £D1(1)        £DMSME
     C                   MOVEL     *BLANKS       £DMSFI
     C                   MOVEL     *BLANKS       £DMSEL
     C                   MOVEL     £D3(1)        £DMS1L
     C                   MOVEL     £DMS1L        £DMS1M
1e   C                   ENDIF
      *
     C     £DMS4X        ENDSR
      *
      *****************************************************************
    RD* £DMSC5 --> Reperimento del messaggio
     D*
    HD*  SCOPO DELLA ROUTINE:
     D*  Esecuzione retrive message per:
     D*  Se 'X' visualizzazione finestre utente finale
     D*  Se 'G' gestione del messaggio con CHGMSGD
     D*
     D* Valori in entrata:
     D* ------------------
     D* . £DMSTR: Tipo retrive
     D* . £D    : Puntatore schiera
     D*
      *****************************************************************
      *
     CSR   £DMSC5        BEGSR
     C                   MOVEL     £DMSTR        £DMSOP            1            Opzione
     C                   MOVEL     £D1(£D)       £DMSCM            7            Codice Msg
     C                   MOVE      *BLANKS       £DMS1L                         1° Livello
     C                   MOVE      £D1(£D)       £DMSMF           10            Msgf
     C                   MOVE      *BLANKS       £DMS2L                         2° Livello
     C                   MOVEL     £D2(£D)       £DMSST                         Stringa Var.
      *
     C                   CALL      'B£DMS1CL'
     C                   PARM                    £DMSOP
     C                   PARM                    £DMSCM
     C                   PARM                    £DMS1L
     C                   PARM                    £DMSMF
     C                   PARM                    £DMS2L
     C                   PARM                    £DMSST
      *
     C                   MOVEL     *BLANKS       £DMSOP                         Opzione
     C                   MOVEL     *BLANKS       £DMSCM                         Codice Msg
     C                   MOVEL     *BLANKS       £DMSMF                         Msgf
     C                   MOVEL     *BLANKS       £DMSST                         Stringa Var.
      *
     C                   MOVE      *BLANKS       £DMSTR            1            Tipo Retrive
      *
     CSR   £DMS5X        ENDSR
      *
      *****************************************************************
    RD* £DMSC6 --> Concatenazione variabili
     D*
    HD*  SCOPO DELLA ROUTINE:
     D*  Concatena le variabili da passare al MESSAGGIO PARAMETRICO
     D*  rispettando la loro lunghezza originaria (Fisica).
     D*
     D* Regola di impostazione £DMSVA
     D* -----------------------------
     D* Attraverso delle CAT, la regola di scrittura di ciascuna
     D* variabile sara' la seguente:
     D*
     D* !-----!------!---------!
     D* ! ' ' !  DD  ! XXXXXXX !
     D* !-----!------!---------!
     D*
     D* . ' ' = Spazio obbligatorio per inizio dati associati alla
     D*         variabile da concatenare
     D* . DD  = Lunghezza fisica variabile (Come da Data base)
     D*         da concatenare.
     D*         Deve essere sempre epressa sempre su due caratteri
     D* . XXX = Variabile
     D*
     D* Valori in entrata:
     D* ------------------
     D* . £D    : Messaggio in elaborazione
     D* . £DMSVA: Stringa ( <Dimensione fisica e variabile> )
     D*
      *****************************************************************
      *
     CSR   £DMSC6        BEGSR
     C                   MOVEA     £DMSVA        £D4
     C                   Z-ADD     0             £DMS4
     C                   Z-ADD     0             £DMS2
     C                   Z-ADD     0             £DMSLA
     C                   Z-ADD     0             £DMSLN
      *
      * Ciclo 01
      *
     C     £DMS61        TAG
     C                   ADD       1             £DMS4
     C     £DMS4         CABGT     45            £DMS6X
     C                   Z-ADD     £DMS4         £4
      *
      * Caricamento Effettivo della variabile
      *
1    C     £D4(£4)       IFNE      *BLANKS
     C                   ADD       1             £DMS2
     C     £DMS2         CABGT     45            £DMS6X
     C                   Z-ADD     £DMS2         £2
     C                   MOVEL     £D4(£4)       £D2(£2)
     C                   SUB       1             £DMSLN
     C                   GOTO      £DMS61
1e   C                   ENDIF
      *
      * Ciclo 02
      *
     C     £DMS62        TAG
      *
      * Caricamento del restante spazio con blanks
      *
1    C     £DMSLN        IFGT      *ZEROS
     C                   ADD       1             £DMS2
     C     £DMS2         CABGT     45            £DMS6X
     C                   Z-ADD     £DMS2         £2
     C                   MOVEL     *BLANKS       £D2(£2)
     C                   SUB       1             £DMSLN
     C                   GOTO      £DMS62
1e   C                   ENDIF
      *
      * Memorizzazione lunghezza fisica variabile
      *
     C                   MOVE      *ALL'0'       £DMSLA
     C     £DMS63        TAG
      *
      * 1° Carattere
      *
     C                   ADD       1             £DMS4
     C     £DMS4         CABGT     45            £DMS6X
     C                   Z-ADD     £DMS4         £4
     C     £D4(£4)       CABEQ     *BLANKS       £DMS63
     C                   MOVEL     £D4(£DMS4)    £DMSLA
      *
      * 2° Carattere
      *
     C                   ADD       1             £DMS4
     C     £DMS4         CABGT     45            £DMS6X
     C                   Z-ADD     £DMS4         £4
     C                   MOVE      £D4(£DMS4)    £DMSLA
     C                   GOTO      £DMS61
      *
     CSR   £DMS6X        ENDSR
     D*----------------------------------------------------------------
     C     £DMSC7        BEGSR
      *
     C     ££B£2L        IFEQ      '11'
     C     £DMS7F        IFEQ      'INI'
     C                   MOVEL     ££B£2L        ££B£2L            2
     C                   BITOFF    '01234567'    £ATRIN            1
     C                   BITON     '27'          £ATRIN
     C     £ATRIN        CAT       '>':0         W$B£2L            2
     C                   ENDIF
      *
  MO>C                   IF        ££B£2J = '1'
  MO>C                   CALL      'B£DMS7'                             37
  MO>C                   PARM                    £DMS7F           10
  MO>C                   PARM                    £DMS7M           10
  MO>C                   PARM                    £DMS7T            2
  MO>C                   PARM                    £DMS7P           10
  MO>C                   PARM                    £DMS7C           15
  MO>C                   PARM                    £DMS75            1
  MO>C                   ELSE
     C                   EVAL      *IN37=*OFF
     C                   CALL      'B£DMS7'
     C                   PARM                    £DMS7F           10
     C                   PARM                    £DMS7M           10
     C                   PARM                    £DMS7T            2
     C                   PARM                    £DMS7P           10
     C                   PARM                    £DMS7C           15
     C                   PARM                    £DMS75            1
  MO>C                   ENDIF
     C                   MOVEL     *BLANKS       £DMS7F
     C                   MOVEL     *BLANKS       £DMS7M
     C                   ENDIF
     C                   ENDSR
     D*----------------------------------------------------------------
     C     £DMSC8        BEGSR
  MO>C                   IF        ££B£2J = '1'
  MO>C                   CALL      'B£DMS8'                             37
  MO>C                   PARM                    £PDSNP
  MO>C                   PARM                    £DMSRL           10
  MO>C                   PARM                    £DMSPT           10
  MO>C                   ELSE
     C                   EVAL      *IN37=*OFF
     C                   CALL      'B£DMS8'
     C                   PARM                    £PDSNP
     C                   PARM                    £DMSRL           10
     C                   PARM                    £DMSPT           10
  MO>C                   ENDIF
     C                   ENDSR
      *ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£DMS


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
********** PREPROCESSOR COPYSTART QILEGEN,£TABB£1DS
      *IF NOT DEFINED(£TABB£1DS)
      *DEFINE £TABB£1DS
      *      PERSONALIZZAZIONE B£
     D B£1$DS          DS           100
      * N° 03 - Nome azienda                       15
     D  £RASDI                 1     15
      * N° 04 - Clienti              TA *CNAA       2
     D  ££ANCL                16     17
      * N° 05 - Fornitori            TA *CNAA       2
     D  ££ANFO                18     19
      * N° 06 - Conti Co.Ge.         TA *CNAA       2
     D  ££PICO                20     21
      * N° 07 - Articoli             TA *CNAA       2
     D  ££ANAR                22     23
      * N° 08 - St.magaz.            TA *CNAA       2
     D  ££SAMG                24     25
      * N° 09 - Costi std            TA *CNAA       2
     D  ££COST                26     27
      * N° 10 - Ge.vendite           TA *CNAA       2
     D  ££STVE                28     29
      * N° 11 - Ge.acquisti          TA *CNAA       2
     D  ££STAC                30     31
      * N° 12 - St.produz.           TA *CNAA       2
     D  ££STPR                32     33
      * N° 13 - Ge.produz.           TA *CNAA       2
     D  ££GEPR                34     35
      * N° 14 - Ce.costo             TA *CNAA       2
     D  ££CDCO                36     37
      * N° 15 - Vo.spesa             TA *CNAA       2
     D  ££VDSP                38     39
      * N° 16 - Ce.lavoro            TA *CNAA       2
     D  ££CDLA                40     41
      * N° 17 - Ge.personale         TA *CNAA       2
     D  ££GEPE                42     43
      * N° 18 - Macchine             TA *CNAA       2
     D  ££MACC                44     45
      * N° 19 - Ubicazioni           TA *CNAA       2
     D  ££UBIC                46     47
      * N° 20 - Lotti/rintrac.       TA *CNAA       2
     D  ££LOTT                48     49
      * N° 21 - Attrezzature         TA *CNAA       2
     D  ££ATTR                50     51
      * N° 22 - D/Base               TA *CNAA       2
     D  ££DIBA                52     53
      * N° 23 - Cicli lav.           TA *CNAA       2
     D  ££CILA                54     55
      * N° 24 - Commesse             TA *CNAA       2
     D  ££GCOM                56     57
      * N° 25 - Giacenza             TA *CNAA       2
     D  ££ANSA                58     59
      * N° 26 - Colli/confez.        TA *CNAA       2
     D  ££IFLC                60     61
      * N° 27 - Magazzino            TA *CNAA       2
     D  ££IFMG                62     63
      * N° 28 - R.acquisto           TA *CNAA       2
     D  ££IFPC                64     65
      * N° 29 - Matricole            TA *CNAA       2
     D  ££IFMT                66     67
      * N° 30 - Esp.mod.             TA *CNAA       2
     D  ££ESMO                68     69
      * N° 31 - Impianti             TA *CNAA       2
     D  ££IFMM                70     71
      * N° 32 - Cespiti              TA *CNAA       2
     D  ££IFCE                72     73
      * N° 33 - Cambi                TA *CNAA       2
     D  ££CMBV                74     75
      * N° 34 - Par.Pianif.          TA *CNAA       2
     D  ££B£1A                76     77
      * N° 35 - Rich.movim.          TA *CNAA       2
     D  ££B£1B                78     79
      * N° 36 - Viaggi               TA *CNAA       2
     D  ££B£1C                80     81
      * N° 37 - Co.Generale          TA *CNAA       2
     D  ££B£1D                82     83
      * N° 38 - Co.Analitica         TA *CNAA       2
     D  ££B£1E                84     85
      * N° 39 - Non conformità       TA *CNAA       2
     D  ££B£1F                86     87
      * N° 40 - Imp.Risorse          TA *CNAA       2
     D  ££B£1G                88     89
      * N° 41 - Rich.Interv.         TA *CNAA       2
     D  ££B£1H                90     91
      *      PERSONALIZ. (IN CODA A B£1)
     D B£2$DS          DS           100
      * N° 03 - U.Mis.fraz.tempo C/S                1
     D  ££UMHR                 1      1
      * N° 04 - Arrot. ore (minuti)  NR             2
     D  ££PAMI                 2      3  0
      * N° 05 - Magazzino unico      MG             3
     D  ££MAGD                 4      6
      * N° 06 - Att.LOG util.oggetti                2
     D  ££B£2L                 7      8
      * N° 07 - Att.PROTEZ.Liv.campo                2
     D  ££B£2P                 9     10
      * N° 08 - Att.SCENARI multipli                2
     D  ££B£2S                11     12
      * N° 09 - Azienda              AZ             2
     D  ££B£2A                13     14
      * N° 10 - Modo present.istanti V2 TI_I1       1    1
     D  ££B£2B                16     16
      * N° 11 - Escl.contr.comp.st.  V2 SI/NO       1
     D  ££B£2C                17     17
      * N° 12 - Cod.divisa corrente  TA VAL         4
     D  ££B£2D                18     21
      * N° 13 - Cod.divisa Euro      TA VAL         4
     D  ££B£2E                22     25
      * N° 14 - Descr.estesa assunta TA LIN         3
     D  ££B£2F                26     28
      * N° 15 - Magazzino competenza MG             3
     D  ££B£2G                29     31
      * N° 16 - Nazione assunta      TA V§N         6
     D  ££B£2H                32     37
      * N° 17 - Lingua assunta       TA V§L         3
     D  ££B£2I                38     40
      * N° 18 - Formato data         V2 FMDAT       1
     D  ££B£2M                41     41
      * N° 19 - Divisa alternativa   TA VAL         4
     D  ££B£2N                42     45
      * N° 20 - Divisore             V2 SI/NO       1
     D  ££B£2O                46     46
      * N° 21 - Tipo cambio          TA TCA         3
     D  ££B£2Q                47     49
      * N° 22 - Separatore decimale  **             1
     D  ££B£2R                50     50
      * N° 23 - Time Zone As400      NR             4 2
     D  ££B£2T                51     54  2
      * N° 24 - Time Zone Utenti     NR             4 2
     D  ££B£2U                55     58  2
      * N° 25 - Suff.Agg.     £INZDS **             1
     D  ££B£2V                59     59
      * N° 26 - Costruz.automatica   V2 SI/NO       1
     D  ££B£2W                60     60
      * N° 27 - Nuova gest.azioni    **             1
     D  ££B£2X                61     61
      * N° 28 - Gest.Contesto        V2 SI/NO       1
     D  ££B£2Y                62     62
      * N° 29 - Ambiente di Test     V2 SI/NO       1
     D  ££B£2Z                63     63
      * N° 30 - Abilita Monitor API  V2 SI/NO       1
     D  ££B£2J                64     64
      * N° 31 - Suff.Agg.Amb. B£1/2  **             1
     D  ££B£2K                65     65
      * N° 32 - Attiva RCLRSC        V2 SI/NO       1
     D  ££B£20                66     66
      * N° 33 - Attiva mascheramento V2 SI/NO       1
     D  ££B£21                67     67
      * N° 34 - Fuso orario utenti   OJ *TIMZON    10
     D  ££B£22                68     77
      *ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£TABB£1DS
********** PREPROCESSOR COPYSTART QILEGEN,£HEX
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £HEX
      * Sorgente di origine : SMEDEV/QILEGEN(£HEX)
      * Esportato il        : 20240715 131107
      *====================================================================
      *IF NOT DEFINED(HEX_INCLUDED)
      *DEFINE HEX_INCLUDED
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 08/03/17  V5R1    CM Creazione
     V* 10/03/17  V5R1    AS Aggiunti 0E e 0F
     D* ==============================================================
     D*----------------------------------------------------------------
     D* OBIETTIVO
     D* Avere un unico punto in cui definire i valori esadefimali da utilizzare in Sme.UP
     D*
     D*----------------------------------------------------------------
     D£HEX_ST          PR         30000    VARYING
     D £HEX_LU                        5S 0 CONST
     D £HEX_CH                        1    CONST
     D*- Gruppo 0 -----------------------------------------------------
     D £HEX00          S              1    INZ(x'00')
     D £HEX01          S              1    INZ(x'01')
     D £HEX02          S              1    INZ(x'02')
     D £HEX03          S              1    INZ(x'03')
     D £HEX04          S              1    INZ(x'04')
     D £HEX05          S              1    INZ(x'05')
     D £HEX06          S              1    INZ(x'06')
     D £HEX07          S              1    INZ(x'07')
     D £HEX08          S              1    INZ(x'08')
     D £HEX09          S              1    INZ(x'09')
     D £HEX0A          S              1    INZ(x'0A')
     D £HEX0B          S              1    INZ(x'0B')
     D £HEX0C          S              1    INZ(x'0C')
     D £HEX0D          S              1    INZ(x'0D')
     D £HEX0E          S              1    INZ(x'0E')
     D £HEX0F          S              1    INZ(x'0F')
     D*- Gruppo 1 -----------------------------------------------------
     D £HEX10          S              1    INZ(x'10')
     D £HEX11          S              1    INZ(x'11')
     D £HEX12          S              1    INZ(x'12')
     D £HEX13          S              1    INZ(x'13')
     D £HEX14          S              1    INZ(x'14')
     D £HEX15          S              1    INZ(x'15')
     D £HEX16          S              1    INZ(x'16')
     D £HEX17          S              1    INZ(x'17')
     D £HEX18          S              1    INZ(x'18')
     D £HEX19          S              1    INZ(x'19')
     D £HEX1A          S              1    INZ(x'1A')
     D £HEX1B          S              1    INZ(x'1B')
     D £HEX1C          S              1    INZ(x'1C')
     D £HEX1D          S              1    INZ(x'1D')
     D £HEX1E          S              1    INZ(x'1E')
     D £HEX1F          S              1    INZ(x'1F')
     D*- Gruppo 2 -----------------------------------------------------
     D £HEX20          S              1    INZ(x'20')
     D £HEX21          S              1    INZ(x'21')
     D £HEX22          S              1    INZ(x'22')
     D £HEX23          S              1    INZ(x'23')
     D £HEX24          S              1    INZ(x'24')
     D £HEX25          S              1    INZ(x'25')
     D £HEX26          S              1    INZ(x'26')
     D £HEX27          S              1    INZ(x'27')
     D £HEX28          S              1    INZ(x'28')
     D £HEX29          S              1    INZ(x'29')
     D £HEX2A          S              1    INZ(x'2A')
     D £HEX2B          S              1    INZ(x'2B')
     D £HEX2C          S              1    INZ(x'2C')
     D £HEX2D          S              1    INZ(x'2D')
     D £HEX2E          S              1    INZ(x'2E')
     D £HEX2F          S              1    INZ(x'2F')
     D*- Gruppo 3 -----------------------------------------------------
     D £HEX30          S              1    INZ(x'30')
     D £HEX31          S              1    INZ(x'31')
     D £HEX32          S              1    INZ(x'32')
     D £HEX33          S              1    INZ(x'33')
     D £HEX34          S              1    INZ(x'34')
     D £HEX35          S              1    INZ(x'35')
     D £HEX36          S              1    INZ(x'36')
     D £HEX37          S              1    INZ(x'37')
     D £HEX38          S              1    INZ(x'38')
     D £HEX39          S              1    INZ(x'39')
     D £HEX3A          S              1    INZ(x'3A')
     D £HEX3B          S              1    INZ(x'3B')
     D £HEX3C          S              1    INZ(x'3C')
     D £HEX3D          S              1    INZ(x'3D')
     D £HEX3E          S              1    INZ(x'3E')
     D £HEX3F          S              1    INZ(x'3F')
     D*- Gruppo 4 -----------------------------------------------------
     D £HEX40          S              1    INZ(x'40')
     D £HEX41          S              1    INZ(x'41')
     D £HEX42          S              1    INZ(x'42')
     D £HEX43          S              1    INZ(x'43')
     D £HEX44          S              1    INZ(x'44')
     D £HEX45          S              1    INZ(x'45')
     D £HEX46          S              1    INZ(x'46')
     D £HEX47          S              1    INZ(x'47')
     D £HEX48          S              1    INZ(x'48')
     D £HEX49          S              1    INZ(x'49')
     D £HEX4A          S              1    INZ(x'4A')
     D £HEX4B          S              1    INZ(x'4B')
     D £HEX4C          S              1    INZ(x'4C')
     D £HEX4D          S              1    INZ(x'4D')
     D £HEX4E          S              1    INZ(x'4E')
     D £HEX4F          S              1    INZ(x'4F')
     D*- Gruppo 5 -----------------------------------------------------
     D £HEX50          S              1    INZ(x'50')
     D £HEX51          S              1    INZ(x'51')
     D £HEX52          S              1    INZ(x'52')
     D £HEX53          S              1    INZ(x'53')
     D £HEX54          S              1    INZ(x'54')
     D £HEX55          S              1    INZ(x'55')
     D £HEX56          S              1    INZ(x'56')
     D £HEX57          S              1    INZ(x'57')
     D £HEX58          S              1    INZ(x'58')
     D £HEX59          S              1    INZ(x'59')
     D £HEX5A          S              1    INZ(x'5A')
     D £HEX5B          S              1    INZ(x'5B')
     D £HEX5C          S              1    INZ(x'5C')
     D £HEX5D          S              1    INZ(x'5D')
     D £HEX5E          S              1    INZ(x'5E')
     D £HEX5F          S              1    INZ(x'5F')
     D*- Gruppo 6 -----------------------------------------------------
     D £HEX60          S              1    INZ(x'60')
     D £HEX61          S              1    INZ(x'61')
     D £HEX62          S              1    INZ(x'62')
     D £HEX63          S              1    INZ(x'63')
     D £HEX64          S              1    INZ(x'64')
     D £HEX65          S              1    INZ(x'65')
     D £HEX66          S              1    INZ(x'66')
     D £HEX67          S              1    INZ(x'67')
     D £HEX68          S              1    INZ(x'68')
     D £HEX69          S              1    INZ(x'69')
     D £HEX6A          S              1    INZ(x'6A')
     D £HEX6B          S              1    INZ(x'6B')
     D £HEX6C          S              1    INZ(x'6C')
     D £HEX6D          S              1    INZ(x'6D')
     D £HEX6E          S              1    INZ(x'6E')
     D £HEX6F          S              1    INZ(x'6F')
     D*- Gruppo 7 -----------------------------------------------------
     D £HEX70          S              1    INZ(x'70')
     D £HEX71          S              1    INZ(x'71')
     D £HEX72          S              1    INZ(x'72')
     D £HEX73          S              1    INZ(x'73')
     D £HEX74          S              1    INZ(x'74')
     D £HEX75          S              1    INZ(x'75')
     D £HEX76          S              1    INZ(x'76')
     D £HEX77          S              1    INZ(x'77')
     D £HEX78          S              1    INZ(x'78')
     D £HEX79          S              1    INZ(x'79')
     D £HEX7A          S              1    INZ(x'7A')
     D £HEX7B          S              1    INZ(x'7B')
     D £HEX7C          S              1    INZ(x'7C')
     D £HEX7D          S              1    INZ(x'7D')
     D £HEX7E          S              1    INZ(x'7E')
     D £HEX7F          S              1    INZ(x'7F')
     D*- Gruppo 8 -----------------------------------------------------
     D £HEX80          S              1    INZ(x'80')
     D £HEX81          S              1    INZ(x'81')
     D £HEX82          S              1    INZ(x'82')
     D £HEX83          S              1    INZ(x'83')
     D £HEX84          S              1    INZ(x'84')
     D £HEX85          S              1    INZ(x'85')
     D £HEX86          S              1    INZ(x'86')
     D £HEX87          S              1    INZ(x'87')
     D £HEX88          S              1    INZ(x'88')
     D £HEX89          S              1    INZ(x'89')
     D £HEX8A          S              1    INZ(x'8A')
     D £HEX8B          S              1    INZ(x'8B')
     D £HEX8C          S              1    INZ(x'8C')
     D £HEX8D          S              1    INZ(x'8D')
     D £HEX8E          S              1    INZ(x'8E')
     D £HEX8F          S              1    INZ(x'8F')
     D*- Gruppo 9 -----------------------------------------------------
     D £HEX90          S              1    INZ(x'90')
     D £HEX91          S              1    INZ(x'91')
     D £HEX92          S              1    INZ(x'92')
     D £HEX93          S              1    INZ(x'93')
     D £HEX94          S              1    INZ(x'94')
     D £HEX95          S              1    INZ(x'95')
     D £HEX96          S              1    INZ(x'96')
     D £HEX97          S              1    INZ(x'97')
     D £HEX98          S              1    INZ(x'98')
     D £HEX99          S              1    INZ(x'99')
     D £HEX9A          S              1    INZ(x'9A')
     D £HEX9B          S              1    INZ(x'9B')
     D £HEX9C          S              1    INZ(x'9C')
     D £HEX9D          S              1    INZ(x'9D')
     D £HEX9E          S              1    INZ(x'9E')
     D £HEX9F          S              1    INZ(x'9F')
     D*- Gruppo A -----------------------------------------------------
     D £HEXA0          S              1    INZ(x'A0')
     D £HEXA1          S              1    INZ(x'A1')
     D £HEXA2          S              1    INZ(x'A2')
     D £HEXA3          S              1    INZ(x'A3')
     D £HEXA4          S              1    INZ(x'A4')
     D £HEXA5          S              1    INZ(x'A5')
     D £HEXA6          S              1    INZ(x'A6')
     D £HEXA7          S              1    INZ(x'A7')
     D £HEXA8          S              1    INZ(x'A8')
     D £HEXA9          S              1    INZ(x'A9')
     D £HEXAA          S              1    INZ(x'AA')
     D £HEXAB          S              1    INZ(x'AB')
     D £HEXAC          S              1    INZ(x'AC')
     D £HEXAD          S              1    INZ(x'AD')
     D £HEXAE          S              1    INZ(x'AE')
     D £HEXAF          S              1    INZ(x'AF')
     D*- Gruppo B -----------------------------------------------------
     D £HEXB0          S              1    INZ(x'B0')
     D £HEXB1          S              1    INZ(x'B1')
     D £HEXB2          S              1    INZ(x'B2')
     D £HEXB3          S              1    INZ(x'B3')
     D £HEXB4          S              1    INZ(x'B4')
     D £HEXB5          S              1    INZ(x'B5')
     D £HEXB6          S              1    INZ(x'B6')
     D £HEXB7          S              1    INZ(x'B7')
     D £HEXB8          S              1    INZ(x'B8')
     D £HEXB9          S              1    INZ(x'B9')
     D £HEXBA          S              1    INZ(x'BA')
     D £HEXBB          S              1    INZ(x'BB')
     D £HEXBC          S              1    INZ(x'BC')
     D £HEXBD          S              1    INZ(x'BD')
     D £HEXBE          S              1    INZ(x'BE')
     D £HEXBF          S              1    INZ(x'BF')
     D*- Gruppo C -----------------------------------------------------
     D £HEXC0          S              1    INZ(x'C0')
     D £HEXC1          S              1    INZ(x'C1')
     D £HEXC2          S              1    INZ(x'C2')
     D £HEXC3          S              1    INZ(x'C3')
     D £HEXC4          S              1    INZ(x'C4')
     D £HEXC5          S              1    INZ(x'C5')
     D £HEXC6          S              1    INZ(x'C6')
     D £HEXC7          S              1    INZ(x'C7')
     D £HEXC8          S              1    INZ(x'C8')
     D £HEXC9          S              1    INZ(x'C9')
     D £HEXCA          S              1    INZ(x'CA')
     D £HEXCB          S              1    INZ(x'CB')
     D £HEXCC          S              1    INZ(x'CC')
     D £HEXCD          S              1    INZ(x'CD')
     D £HEXCE          S              1    INZ(x'CE')
     D £HEXCF          S              1    INZ(x'CF')
     D*- Gruppo D -----------------------------------------------------
     D £HEXD0          S              1    INZ(x'D0')
     D £HEXD1          S              1    INZ(x'D1')
     D £HEXD2          S              1    INZ(x'D2')
     D £HEXD3          S              1    INZ(x'D3')
     D £HEXD4          S              1    INZ(x'D4')
     D £HEXD5          S              1    INZ(x'D5')
     D £HEXD6          S              1    INZ(x'D6')
     D £HEXD7          S              1    INZ(x'D7')
     D £HEXD8          S              1    INZ(x'D8')
     D £HEXD9          S              1    INZ(x'D9')
     D £HEXDA          S              1    INZ(x'DA')
     D £HEXDB          S              1    INZ(x'DB')
     D £HEXDC          S              1    INZ(x'DC')
     D £HEXDD          S              1    INZ(x'DD')
     D £HEXDE          S              1    INZ(x'DE')
     D £HEXDF          S              1    INZ(x'DF')
     D*- Gruppo E -----------------------------------------------------
     D £HEXE0          S              1    INZ(x'E0')
     D £HEXE1          S              1    INZ(x'E1')
     D £HEXE2          S              1    INZ(x'E2')
     D £HEXE3          S              1    INZ(x'E3')
     D £HEXE4          S              1    INZ(x'E4')
     D £HEXE5          S              1    INZ(x'E5')
     D £HEXE6          S              1    INZ(x'E6')
     D £HEXE7          S              1    INZ(x'E7')
     D £HEXE8          S              1    INZ(x'E8')
     D £HEXE9          S              1    INZ(x'E9')
     D £HEXEA          S              1    INZ(x'EA')
     D £HEXEB          S              1    INZ(x'EB')
     D £HEXEC          S              1    INZ(x'EC')
     D £HEXED          S              1    INZ(x'ED')
     D £HEXEE          S              1    INZ(x'EE')
     D £HEXEF          S              1    INZ(x'EF')
     D*- Gruppo F -----------------------------------------------------
     D £HEXF0          S              1    INZ(x'F0')
     D £HEXF1          S              1    INZ(x'F1')
     D £HEXF2          S              1    INZ(x'F2')
     D £HEXF3          S              1    INZ(x'F3')
     D £HEXF4          S              1    INZ(x'F4')
     D £HEXF5          S              1    INZ(x'F5')
     D £HEXF6          S              1    INZ(x'F6')
     D £HEXF7          S              1    INZ(x'F7')
     D £HEXF8          S              1    INZ(x'F8')
     D £HEXF9          S              1    INZ(x'F9')
     D £HEXFA          S              1    INZ(x'FA')
     D £HEXFB          S              1    INZ(x'FB')
     D £HEXFC          S              1    INZ(x'FC')
     D £HEXFD          S              1    INZ(x'FD')
     D £HEXFE          S              1    INZ(x'FE')
     D £HEXFF          S              1    INZ(x'FF')
     D*----------------------------------------------------------------
      *ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£HEX
********** PREPROCESSOR COPYSTART QILEGEN,£DMSE
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £DMSE
      * Sorgente di origine : SMEDEV/QILEGEN(£DMSE)
      * Esportato il        : 20240715 131100
      *====================================================================
      *IF NOT DEFINED(DMSE_INCLUDED)
      *DEFINE DMSE_INCLUDED
      *
      *
      *  /COPY da inserire insieme con: £DMS
      *
      *----------------------------------------------------------------
     D £D1             S             17    DIM(100)                             Cod.Msg + Msgf
     D £D2             S             45    DIM(100)                             Variabili ass.
     D £D3             S             80    DIM(100)                             Txt schiera
     D £D4             S              1    DIM(45)                              Segmenta in 45
      *ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£DMSE
********** PREPROCESSOR COPYSTART QILEGEN,£DMSI
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £DMSI
      * Sorgente di origine : SMEDEV/QILEGEN(£DMSI)
      * Esportato il        : 20240715 131100
      *====================================================================
      *IF NOT DEFINED(DMSI_INCLUDED)
      *DEFINE DMSI_INCLUDED
      *
      * 1° Messaggio di errore
      *
     D £DMS1M          DS           132
      *----------------------------------------------------------------
      *
      * Messaggio 1 livello
      *
     D £DMS1L          DS           132
      *----------------------------------------------------------------
      *
      * Messaggio 2 livello
      *
     D £DMS2L          DS           500
      *----------------------------------------------------------------
      *
      * STRINGA VARIABILI:
      * 512 bytes per gestire le variabili interne al messaggio (1o/2o)
      *
     D £DMSST          DS           512
      *----------------------------------------------------------------
      *ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£DMSI
********** PREPROCESSOR COPYSTART QILEGEN,£PDS
      *IF NOT DEFINED(£PDS)
      *DEFINE £PDS
     D* ==============================================================
     D* MODIFICHE Ril.  T Au Descrizione
     D* gg/mm/aa  nn.mm i xx Breve descrizione
     D* ==============================================================
     D* B£90630A  02.00   MA Aggiunto campo £PDSDS
     D* B£20806A  05.00   ZZ Aggiunta ds £INZDS inizializzazione pgm
     D* 16/04/03  05.00   ZZ Agg. identificativi job che ha sottomesso
     D* 24/12/04  V2R1    MA il 02.12.03 sono stati inseriti i campi £PDSSI
     D*                      e £PDSNL ed i campi 986/992 non sono liberi
     D*                    ->  Definiti campi £UDU5T e £UDU5A
     D* 10/09/07  V2R3    BS Aggiunte variabili di contesto/ambiente/data riferimento
     D* 17/09/07  V2R3    BS Variabile di attivazione del contesto
     D* 21/03/08  V2R3    BS Aggiunta Variabile VARYING solo per ripresa definizione (Vedi £G25)
     D* 15/09/10  V3R1    MA Aggiunta Variabile £VSELE (numero elementi campi visualizzatori)
     D* 03/12/15  V4R1    AS Eliminata variabile inutilizzta £PDSVY
     D* 23/09/16  V5R1    AS Aggiunta £INZMU (esecuzione in multipiattaforma)
     D* 28/10/16  V5R1   BMA Aggiunto £PDSMI (Message ID)
     V* A£61103A  V5R1    AS Disaccoppiamento utente di sistema da utente applicativo
     V* A£61103A  V5R1   BMA Modificato commento
     V* 21/06/17  V5R1    AS Aggiunto £INZDB (Esecuzione in ambiente con shift-in e shift-out)
     D*----------------------------------------------------------------
     D* OBIETTIVO
     D*  Copy che definisce i campi della DS che contiene
     D*  i dati principali dello stato del programma, la definizione
     D*  della UDS ed i suoi campi standard.
     D*
     D*  Va inserita in tutti i programmi, tra le specifiche I, nella
     D*  zona delle DS, prima dei sottocampi della LDA.
     D*----------------------------------------------------------------
      * Inz
     D £INZFU          S             10    INZ('INZ')
     D £INZME          S             10
     D £INZCO          S             10
      * Variabili di contesto
     D ££ATCO          S              1                                         Attivazione contesto
     D ££CONT          S             10                                         Contesto
     D ££AMBI          S             10                                         Ambiente
     D ££DATE          S              8  0 INZ                                  Data Riferimento
     D*----------------------------------------------------------------
      * Utente applicativo (valorizzato da B£INZ0/£INZSR)
     D £PDSNU          S             10
     D*----------------------------------------------------------------
      * DS esterna per dialogo pgm
     D £PDSDS          DS          2000
      * Variabile di definizione VARYING
     D**** £PDSVY          S          30000    VARYING
      * Nr elementi nelle schiere campi gestite nei visualizzatori £VS (V5TDOC etc.)
     D £VSELE          C                   CONST(400)
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
     D  £UDSMG               223    230  0
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
      *ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£PDS
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
********** PREPROCESSOR COPYSTART QILEGEN,£DMS
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £DMS
      * Sorgente di origine : SMEDEV/QILEGEN(£DMS)
      * Esportato il        : 20240715 131102
      *====================================================================
      *IF NOT DEFINED(DMS_INCLUDED)
      *DEFINE DMS_INCLUDED
      *
      *****************************************************************
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* B£00722A  V3R1    CM Rimozione Gestione Interna degli errori
     V* 12/02/11  V3R1    BS Aggiunte Specifiche per controllo inclusione
    RD* £DMSC1 --> Pulizia iniziali variabili e aree di lavoro
     D*
     D* SCOPO DELLA ROUTINE:
     D* RINFRESCA TUTTE LE VARIABILI PRIMA DI INIZIARE I CONTROLLI
     D*
     D* Valori in uscita:
     D* -----------------
     D* . £D    : Puntatore
     D* . £2    : Puntatore £D2
     D* . £4    : Puntatore £D4
     D* . £D1   : schiera contenente i codici messaggio
     D* . £D2   : schiera contenente le variabili da associare al msg
     D* . £D3   : schiera contenente i testi dei messaggi INTERNI al pgm
     D* . £D4   : schiera contenente 45 bytes da concatenare
     D* . £DMSNR: Valore puntatore
     D* . £DMSTR: Retrive da Msgf (X o G)
     D* . £DMSTP: Reperimento messaggio (Da Msgf o da Pgm)
     D*           (Se i primi 3 byte sono = 'PGM' rilevo il messaggio
     D*            dalla schiera)
     D* . £DMSME: Codice msg
     D* . £DMSVA: Stringa valori delle variabili inserite nel messaggio
     D* . £DMSEL: Testo definito nella schiera messaggi
     D* . £DMSFI: Message file AS/400
     D* . £DMSVS: Flag guida per la segnalazione degli errori:
     D*           ' ' = A Video
     D*           'V' = A Video
     D*           'S' = Su Stampa
     D*
     D* Specifiche I inizializzazione DS:
     D* ---------------------------------
     D* . £DMS1M: Testo 1° messaggio di errore
     D* . £DMS1L: Variabile utilizzata per impostare testo 1 livello
     D* . £DMS2L: Variabile utilizzata per impostare testo 2 livello
     D* . £DMSST: Contenuto: concatenazione variabili in stringa
     D*
      *****************************************************************
      *
     C     £DMSC1        BEGSR
     C                   Z-ADD     0             £D                5 0          Puntatore
     C                   Z-ADD     0             £2                5 0          Puntatore
     C                   Z-ADD     0             £4                5 0          Puntatore
     C                   MOVEL     *BLANKS       £D1                            Sch. Msg
     C                   MOVEL     *BLANKS       £D2                            Sch. Var.
     C                   MOVEL     *BLANKS       £D3                            Sch. Txt pgm
     C                   MOVEL     *BLANKS       £D4                            Sch. 45 byts
     C                   Z-ADD     0             £DMSNR            5 0          Valore punt.
     C                   Z-ADD     0             £DMS2             5 0          "          "
     C                   Z-ADD     0             £DMS4             5 0          "          "
     C                   Z-ADD     0             £DMSLA            2 0          Lunghezza
     C                   Z-ADD     0             £DMSLN            2 0          "       "
     C                   MOVEL     *BLANKS       £DMSTR            1            Retrive Msgf
     C                   MOVEL     *BLANKS       £DMSTP            3            Msgf/schiera
     C                   MOVEL     *BLANKS       £DMSME            7            Codice msg
     C                   MOVEL     *BLANKS       £DMSVA           45            Stringa
     C                   MOVEL     *BLANKS       £DMSEL           80            Txt schiera
     C                   MOVEL     *BLANKS       £DMSFI           10            Msgf
     C                   MOVEL     *BLANKS       £DMSVS            1            Video/Stampa
      *
     C                   MOVEL     *BLANKS       £DMS1M                         DS
     C                   MOVEL     *BLANKS       £DMS2L                         DS
     C                   MOVEL     *BLANKS       £DMS2L                         DS
     C                   MOVEL     *BLANKS       £DMSST                         DS
      *
     C                   ENDSR
      *
      *****************************************************************
    RD* £DMSC2 --> Registrazione errori
     D*
     D* SCOPO DELLA ROUTINE:
     D* REGISTRAZIONE ERRORI
     D*
     D* Se richiesto reperimento da Msgf i primi 3 bytes non devono
     D* essere uguali a 'PGM'.
     D* . REGISTRA IL MESSAGGIO DI ERRORE E LE VARIABILI ASSOCIATE
     D* . REGISTRA IL TESTO DI PRIMO LIVELLO
     D* . REGISTRA LE VARIABILI ASSOCIATE
     D*   (£D1 e £D2)
     D*
     D* Se richiesto reperimento da schiera Messaggi, i primi tre
     D* bytes devono essere uguali a 'PGM'
     D* . REGISTRA NEL COD. MESSAGGIO I PRIMI 7 BYTES DELLA SCHIERA
     D* . REGISTRA NEL TESTO DI PRIMO LIVELLO IL TESTO DELLA SCHIERA
     D*   (£D1 e £D3)
     D*
     D* Se riceve un valore in £DMSIE si attiva la gestione delle
     D* segnalazioni WARNING (Finestra in funzione della gravita')
     D*
     D* Valori in entrata:
     D* ------------------
     D* . £DMSME: Codice messaggio (MSGF XXXnnnn)
     D* . £DMSFI: File messaggi (se blank, assume MSGBA)
     D* . £DMSVA: Stringa di 45 bytes contenente le variabili ancora
     D*           da concatenare nello stesso ordine in cui sono state
     D*           elencate nel testo del messaggio.
     D*           (Da &1 a &99)
     D* . £DMSEL: Testo definito nella schiera messaggi
     D* . £DMSIE: Indicatore di errore per gestione WARNING
     D*
     D* Valori di lavoro:
     D* ------------------
     D* . £D    : Puntatore schiera
     D* . £DMSNR: Valore Puntatore schiera
     D* . £DMSTP: Guida il tipo di reperimento del messaggio (Msgf/Pgm)
     D*           (Se i primi 3 byte sono = 'PGM' rilevo dalla schiera;
     D*            altrimenti dal Message file AS/400)
     D*****************************************************************
      *
     C     £DMSC2        BEGSR
1    C     £DMSNR        IFLT      100
2    C     £DMSFI        IFEQ      *BLANK
     C                   MOVEL(P)  'MSGBA'       £DMSFI
2e   C                   ENDIF
2    C     £DMSTP        IFEQ      *BLANKS
     C                   MOVEL     £DMSME        £DMSTP
2e   C                   ENDIF
     C                   ADD       1             £DMSNR            5 0          Puntatore
     C                   Z-ADD     £DMSNR        £D
2    C     £DMSTP        IFEQ      'PGM'
     C                   MOVEL     *BLANKS       £D1(£D)                        ------------
     C                   MOVEL     £DMSME        £D1(£D)                        Elemeto sch.
     C                   MOVEL     *BLANKS       £D2(£D)                        ------------
     C                   MOVEL     £DMSEL        £D3(£D)                        Txt schiera
2x   C                   ELSE
     C                   MOVEL     £DMSME        £D1(£D)                        Codice Msg
     C                   MOVE      £DMSFI        £D1(£D)                        Msgf
     C                   MOVEL     £DMSVA        £D2(£D)                        Variabili
     C                   MOVEL     *BLANKS       £D3(£D)                        ------------
2e   C                   ENDIF
      * Errori di tipo WARNING
2    C     £DMSIE        IFNE      0
     C     £DMSVS        ANDNE     'S'
     C                   CALL      'B£DMS1'
     C                   PARM                    £D1
     C                   PARM                    £D2
     C                   PARM                    £D3
     C                   PARM                    £PDSNP
     C                   PARM                    £D
     C                   PARM                    £DMSIE
3    C     £DMSIE        IFEQ      0
     C                   MOVEL     *BLANKS       £D1(£D)
     C                   MOVEL     *BLANKS       £D2(£D)
     C                   MOVEL     *BLANKS       £D3(£D)
     C                   SUB       1             £DMSNR
3x   C                   ELSE
     C                   Z-ADD     £DMSIE        £D
     C                   MOVE      '1'           *IN(£D)
3e   C                   ENDIF
2e   C                   ENDIF
     C                   Z-ADD     0             £DMSIE            2 0
      *
     C                   MOVEL     *BLANKS       £DMSME                         Codice msg
     C                   MOVEL     *BLANKS       £DMSTP                         Msgf/schiera
     C                   MOVEL     *BLANKS       £DMSFI                         Msgf
     C                   MOVEL     *BLANKS       £DMSVA                         Stringa
     C                   MOVEL     *BLANKS       £DMSEL                         Txt schiera
1e   C                   ENDIF
     C     £DMS2X        ENDSR
      *
      *****************************************************************
    RD* £DMSC3 --> Elabora messaggi memorizzati
     D*
     D* SCOPO DELLA ROUTINE:
     D* ELABORA TUTTI I MESSAGGI REGISTRATI NELLA SCHIERA £D1
     D*
     D* . £D1   : schiera contenente i codici messaggio
     D* . £D2   : schiera contenente le variabili da associare al msg
     D* . £D3   : schiera contenente i testi dei messaggi da pgm
     D* . £PDSNP: Nome programma
     D*
     D* Valori in entrata:
     D* ------------------
     D* . £DMSVS: Flag guida per la segnalazione degli errori:
     D*           ' ' = A Video
     D*           'V' = A Video
     D*           'S' = Su Stampa
     D*
      *****************************************************************
      *
     C     £DMSC3        BEGSR
1    C     £D1(1)        IFNE      *BLANKS
2    C     £DMSVS        IFEQ      'S'
     C                   CALL      'B£DMS2'
     C                   PARM                    £D1
     C                   PARM                    £D2
     C                   PARM                    £D3
     C                   PARM                    £PDSNP           10
2x   C                   ELSE
     C                   CALL      'B£DMS1'
     C                   PARM                    £D1
     C                   PARM                    £D2
     C                   PARM                    £D3
     C                   PARM                    £PDSNP           10
2e   C                   ENDIF
1x   C                   ELSE
     C                   MOVEL(P)  'VIS'         £DMS7F
     C                   EXSR      £DMSC7
     C                   EXSR      £DMSC8
1e   C                   ENDIF
     C     £DMS3X        ENDSR
      *
      *****************************************************************
    RD* £DMSC4 --> Emissione 1° messaggio
     D*
     D* SCOPO DELLA ROUTINE:
     D* PREPARA IL 1° MESSAGGIO PER L'EMISSIONE A VIDEO
     D*
     D* Valori di lavoro:
     D* -----------------
     D* . £DMSTR: Tipo retrive
     D* . £D    : Puntatore schiera
     D*
     D* Valori di uscita:
     D* -----------------
     D*
     D* - Sempre
     D*   . £DMS1M: testo del 1° messaggio di errore
     D*
     D* - A disposizione:
     D*   - Se richiesto reperimento da Msgf:
     D*     . £DMS1L: testo 1 livello
     D*     . £DMS2L: testo secondo livello
     D*
     D*   - Se richiesto reperimento da schiera Messaggi:
     D*     . £DMSME: codice messaggio
     D*     . £DMSEL: testo di errore (80 caratteri)
     D*
      *****************************************************************
      *
     C     £DMSC4        BEGSR
     C     £D1(1)        CABEQ     *BLANKS       £DMS4X
      *
     C                   MOVEL     *BLANKS       £DMS1M
      *
      * Da msgf
      *
1    C     £D3(1)        IFEQ      *BLANKS
     C                   MOVEL     'X'           £DMSTR
     C                   Z-ADD     1             £D
     C                   EXSR      £DMSC5
     C                   MOVEL     £DMS1L        £DMS1M
1x   C                   ELSE
      *
      * Da schiera messaggi
      *
     C                   MOVEL     *BLANKS       £DMSME
     C                   MOVEL     £D1(1)        £DMSME
     C                   MOVEL     *BLANKS       £DMSFI
     C                   MOVEL     *BLANKS       £DMSEL
     C                   MOVEL     £D3(1)        £DMS1L
     C                   MOVEL     £DMS1L        £DMS1M
1e   C                   ENDIF
      *
     C     £DMS4X        ENDSR
      *
      *****************************************************************
    RD* £DMSC5 --> Reperimento del messaggio
     D*
    HD*  SCOPO DELLA ROUTINE:
     D*  Esecuzione retrive message per:
     D*  Se 'X' visualizzazione finestre utente finale
     D*  Se 'G' gestione del messaggio con CHGMSGD
     D*
     D* Valori in entrata:
     D* ------------------
     D* . £DMSTR: Tipo retrive
     D* . £D    : Puntatore schiera
     D*
      *****************************************************************
      *
     CSR   £DMSC5        BEGSR
     C                   MOVEL     £DMSTR        £DMSOP            1            Opzione
     C                   MOVEL     £D1(£D)       £DMSCM            7            Codice Msg
     C                   MOVE      *BLANKS       £DMS1L                         1° Livello
     C                   MOVE      £D1(£D)       £DMSMF           10            Msgf
     C                   MOVE      *BLANKS       £DMS2L                         2° Livello
     C                   MOVEL     £D2(£D)       £DMSST                         Stringa Var.
      *
     C                   CALL      'B£DMS1CL'
     C                   PARM                    £DMSOP
     C                   PARM                    £DMSCM
     C                   PARM                    £DMS1L
     C                   PARM                    £DMSMF
     C                   PARM                    £DMS2L
     C                   PARM                    £DMSST
      *
     C                   MOVEL     *BLANKS       £DMSOP                         Opzione
     C                   MOVEL     *BLANKS       £DMSCM                         Codice Msg
     C                   MOVEL     *BLANKS       £DMSMF                         Msgf
     C                   MOVEL     *BLANKS       £DMSST                         Stringa Var.
      *
     C                   MOVE      *BLANKS       £DMSTR            1            Tipo Retrive
      *
     CSR   £DMS5X        ENDSR
      *
      *****************************************************************
    RD* £DMSC6 --> Concatenazione variabili
     D*
    HD*  SCOPO DELLA ROUTINE:
     D*  Concatena le variabili da passare al MESSAGGIO PARAMETRICO
     D*  rispettando la loro lunghezza originaria (Fisica).
     D*
     D* Regola di impostazione £DMSVA
     D* -----------------------------
     D* Attraverso delle CAT, la regola di scrittura di ciascuna
     D* variabile sara' la seguente:
     D*
     D* !-----!------!---------!
     D* ! ' ' !  DD  ! XXXXXXX !
     D* !-----!------!---------!
     D*
     D* . ' ' = Spazio obbligatorio per inizio dati associati alla
     D*         variabile da concatenare
     D* . DD  = Lunghezza fisica variabile (Come da Data base)
     D*         da concatenare.
     D*         Deve essere sempre epressa sempre su due caratteri
     D* . XXX = Variabile
     D*
     D* Valori in entrata:
     D* ------------------
     D* . £D    : Messaggio in elaborazione
     D* . £DMSVA: Stringa ( <Dimensione fisica e variabile> )
     D*
      *****************************************************************
      *
     CSR   £DMSC6        BEGSR
     C                   MOVEA     £DMSVA        £D4
     C                   Z-ADD     0             £DMS4
     C                   Z-ADD     0             £DMS2
     C                   Z-ADD     0             £DMSLA
     C                   Z-ADD     0             £DMSLN
      *
      * Ciclo 01
      *
     C     £DMS61        TAG
     C                   ADD       1             £DMS4
     C     £DMS4         CABGT     45            £DMS6X
     C                   Z-ADD     £DMS4         £4
      *
      * Caricamento Effettivo della variabile
      *
1    C     £D4(£4)       IFNE      *BLANKS
     C                   ADD       1             £DMS2
     C     £DMS2         CABGT     45            £DMS6X
     C                   Z-ADD     £DMS2         £2
     C                   MOVEL     £D4(£4)       £D2(£2)
     C                   SUB       1             £DMSLN
     C                   GOTO      £DMS61
1e   C                   ENDIF
      *
      * Ciclo 02
      *
     C     £DMS62        TAG
      *
      * Caricamento del restante spazio con blanks
      *
1    C     £DMSLN        IFGT      *ZEROS
     C                   ADD       1             £DMS2
     C     £DMS2         CABGT     45            £DMS6X
     C                   Z-ADD     £DMS2         £2
     C                   MOVEL     *BLANKS       £D2(£2)
     C                   SUB       1             £DMSLN
     C                   GOTO      £DMS62
1e   C                   ENDIF
      *
      * Memorizzazione lunghezza fisica variabile
      *
     C                   MOVE      *ALL'0'       £DMSLA
     C     £DMS63        TAG
      *
      * 1° Carattere
      *
     C                   ADD       1             £DMS4
     C     £DMS4         CABGT     45            £DMS6X
     C                   Z-ADD     £DMS4         £4
     C     £D4(£4)       CABEQ     *BLANKS       £DMS63
     C                   MOVEL     £D4(£DMS4)    £DMSLA
      *
      * 2° Carattere
      *
     C                   ADD       1             £DMS4
     C     £DMS4         CABGT     45            £DMS6X
     C                   Z-ADD     £DMS4         £4
     C                   MOVE      £D4(£DMS4)    £DMSLA
     C                   GOTO      £DMS61
      *
     CSR   £DMS6X        ENDSR
     D*----------------------------------------------------------------
     C     £DMSC7        BEGSR
      *
     C     ££B£2L        IFEQ      '11'
     C     £DMS7F        IFEQ      'INI'
     C                   MOVEL     ££B£2L        ££B£2L            2
     C                   BITOFF    '01234567'    £ATRIN            1
     C                   BITON     '27'          £ATRIN
     C     £ATRIN        CAT       '>':0         W$B£2L            2
     C                   ENDIF
      *
  MO>C                   IF        ££B£2J = '1'
  MO>C                   CALL      'B£DMS7'                             37
  MO>C                   PARM                    £DMS7F           10
  MO>C                   PARM                    £DMS7M           10
  MO>C                   PARM                    £DMS7T            2
  MO>C                   PARM                    £DMS7P           10
  MO>C                   PARM                    £DMS7C           15
  MO>C                   PARM                    £DMS75            1
  MO>C                   ELSE
     C                   EVAL      *IN37=*OFF
     C                   CALL      'B£DMS7'
     C                   PARM                    £DMS7F           10
     C                   PARM                    £DMS7M           10
     C                   PARM                    £DMS7T            2
     C                   PARM                    £DMS7P           10
     C                   PARM                    £DMS7C           15
     C                   PARM                    £DMS75            1
  MO>C                   ENDIF
     C                   MOVEL     *BLANKS       £DMS7F
     C                   MOVEL     *BLANKS       £DMS7M
     C                   ENDIF
     C                   ENDSR
     D*----------------------------------------------------------------
     C     £DMSC8        BEGSR
  MO>C                   IF        ££B£2J = '1'
  MO>C                   CALL      'B£DMS8'                             37
  MO>C                   PARM                    £PDSNP
  MO>C                   PARM                    £DMSRL           10
  MO>C                   PARM                    £DMSPT           10
  MO>C                   ELSE
     C                   EVAL      *IN37=*OFF
     C                   CALL      'B£DMS8'
     C                   PARM                    £PDSNP
     C                   PARM                    £DMSRL           10
     C                   PARM                    £DMSPT           10
  MO>C                   ENDIF
     C                   ENDSR
      *ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£DMS

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
********** PREPROCESSOR COPYSTART QILEGEN,£TABB£1DS
      *IF NOT DEFINED(£TABB£1DS)
      *DEFINE £TABB£1DS
      *      PERSONALIZZAZIONE B£
     D B£1$DS          DS           100
      * N° 03 - Nome azienda                       15
     D  £RASDI                 1     15
      * N° 04 - Clienti              TA *CNAA       2
     D  ££ANCL                16     17
      * N° 05 - Fornitori            TA *CNAA       2
     D  ££ANFO                18     19
      * N° 06 - Conti Co.Ge.         TA *CNAA       2
     D  ££PICO                20     21
      * N° 07 - Articoli             TA *CNAA       2
     D  ££ANAR                22     23
      * N° 08 - St.magaz.            TA *CNAA       2
     D  ££SAMG                24     25
      * N° 09 - Costi std            TA *CNAA       2
     D  ££COST                26     27
      * N° 10 - Ge.vendite           TA *CNAA       2
     D  ££STVE                28     29
      * N° 11 - Ge.acquisti          TA *CNAA       2
     D  ££STAC                30     31
      * N° 12 - St.produz.           TA *CNAA       2
     D  ££STPR                32     33
      * N° 13 - Ge.produz.           TA *CNAA       2
     D  ££GEPR                34     35
      * N° 14 - Ce.costo             TA *CNAA       2
     D  ££CDCO                36     37
      * N° 15 - Vo.spesa             TA *CNAA       2
     D  ££VDSP                38     39
      * N° 16 - Ce.lavoro            TA *CNAA       2
     D  ££CDLA                40     41
      * N° 17 - Ge.personale         TA *CNAA       2
     D  ££GEPE                42     43
      * N° 18 - Macchine             TA *CNAA       2
     D  ££MACC                44     45
      * N° 19 - Ubicazioni           TA *CNAA       2
     D  ££UBIC                46     47
      * N° 20 - Lotti/rintrac.       TA *CNAA       2
     D  ££LOTT                48     49
      * N° 21 - Attrezzature         TA *CNAA       2
     D  ££ATTR                50     51
      * N° 22 - D/Base               TA *CNAA       2
     D  ££DIBA                52     53
      * N° 23 - Cicli lav.           TA *CNAA       2
     D  ££CILA                54     55
      * N° 24 - Commesse             TA *CNAA       2
     D  ££GCOM                56     57
      * N° 25 - Giacenza             TA *CNAA       2
     D  ££ANSA                58     59
      * N° 26 - Colli/confez.        TA *CNAA       2
     D  ££IFLC                60     61
      * N° 27 - Magazzino            TA *CNAA       2
     D  ££IFMG                62     63
      * N° 28 - R.acquisto           TA *CNAA       2
     D  ££IFPC                64     65
      * N° 29 - Matricole            TA *CNAA       2
     D  ££IFMT                66     67
      * N° 30 - Esp.mod.             TA *CNAA       2
     D  ££ESMO                68     69
      * N° 31 - Impianti             TA *CNAA       2
     D  ££IFMM                70     71
      * N° 32 - Cespiti              TA *CNAA       2
     D  ££IFCE                72     73
      * N° 33 - Cambi                TA *CNAA       2
     D  ££CMBV                74     75
      * N° 34 - Par.Pianif.          TA *CNAA       2
     D  ££B£1A                76     77
      * N° 35 - Rich.movim.          TA *CNAA       2
     D  ££B£1B                78     79
      * N° 36 - Viaggi               TA *CNAA       2
     D  ££B£1C                80     81
      * N° 37 - Co.Generale          TA *CNAA       2
     D  ££B£1D                82     83
      * N° 38 - Co.Analitica         TA *CNAA       2
     D  ££B£1E                84     85
      * N° 39 - Non conformità       TA *CNAA       2
     D  ££B£1F                86     87
      * N° 40 - Imp.Risorse          TA *CNAA       2
     D  ££B£1G                88     89
      * N° 41 - Rich.Interv.         TA *CNAA       2
     D  ££B£1H                90     91
      *      PERSONALIZ. (IN CODA A B£1)
     D B£2$DS          DS           100
      * N° 03 - U.Mis.fraz.tempo C/S                1
     D  ££UMHR                 1      1
      * N° 04 - Arrot. ore (minuti)  NR             2
     D  ££PAMI                 2      3  0
      * N° 05 - Magazzino unico      MG             3
     D  ££MAGD                 4      6
      * N° 06 - Att.LOG util.oggetti                2
     D  ££B£2L                 7      8
      * N° 07 - Att.PROTEZ.Liv.campo                2
     D  ££B£2P                 9     10
      * N° 08 - Att.SCENARI multipli                2
     D  ££B£2S                11     12
      * N° 09 - Azienda              AZ             2
     D  ££B£2A                13     14
      * N° 10 - Modo present.istanti V2 TI_I1       1    1
     D  ££B£2B                16     16
      * N° 11 - Escl.contr.comp.st.  V2 SI/NO       1
     D  ££B£2C                17     17
      * N° 12 - Cod.divisa corrente  TA VAL         4
     D  ££B£2D                18     21
      * N° 13 - Cod.divisa Euro      TA VAL         4
     D  ££B£2E                22     25
      * N° 14 - Descr.estesa assunta TA LIN         3
     D  ££B£2F                26     28
      * N° 15 - Magazzino competenza MG             3
     D  ££B£2G                29     31
      * N° 16 - Nazione assunta      TA V§N         6
     D  ££B£2H                32     37
      * N° 17 - Lingua assunta       TA V§L         3
     D  ££B£2I                38     40
      * N° 18 - Formato data         V2 FMDAT       1
     D  ££B£2M                41     41
      * N° 19 - Divisa alternativa   TA VAL         4
     D  ££B£2N                42     45
      * N° 20 - Divisore             V2 SI/NO       1
     D  ££B£2O                46     46
      * N° 21 - Tipo cambio          TA TCA         3
     D  ££B£2Q                47     49
      * N° 22 - Separatore decimale  **             1
     D  ££B£2R                50     50
      * N° 23 - Time Zone As400      NR             4 2
     D  ££B£2T                51     54  2
      * N° 24 - Time Zone Utenti     NR             4 2
     D  ££B£2U                55     58  2
      * N° 25 - Suff.Agg.     £INZDS **             1
     D  ££B£2V                59     59
      * N° 26 - Costruz.automatica   V2 SI/NO       1
     D  ££B£2W                60     60
      * N° 27 - Nuova gest.azioni    **             1
     D  ££B£2X                61     61
      * N° 28 - Gest.Contesto        V2 SI/NO       1
     D  ££B£2Y                62     62
      * N° 29 - Ambiente di Test     V2 SI/NO       1
     D  ££B£2Z                63     63
      * N° 30 - Abilita Monitor API  V2 SI/NO       1
     D  ££B£2J                64     64
      * N° 31 - Suff.Agg.Amb. B£1/2  **             1
     D  ££B£2K                65     65
      * N° 32 - Attiva RCLRSC        V2 SI/NO       1
     D  ££B£20                66     66
      * N° 33 - Attiva mascheramento V2 SI/NO       1
     D  ££B£21                67     67
      * N° 34 - Fuso orario utenti   OJ *TIMZON    10
     D  ££B£22                68     77
      *ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£TABB£1DS
********** PREPROCESSOR COPYSTART QILEGEN,£HEX
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £HEX
      * Sorgente di origine : SMEDEV/QILEGEN(£HEX)
      * Esportato il        : 20240715 131107
      *====================================================================
      *IF NOT DEFINED(HEX_INCLUDED)
      *DEFINE HEX_INCLUDED
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 08/03/17  V5R1    CM Creazione
     V* 10/03/17  V5R1    AS Aggiunti 0E e 0F
     D* ==============================================================
     D*----------------------------------------------------------------
     D* OBIETTIVO
     D* Avere un unico punto in cui definire i valori esadefimali da utilizzare in Sme.UP
     D*
     D*----------------------------------------------------------------
     D£HEX_ST          PR         30000    VARYING
     D £HEX_LU                        5S 0 CONST
     D £HEX_CH                        1    CONST
     D*- Gruppo 0 -----------------------------------------------------
     D £HEX00          S              1    INZ(x'00')
     D £HEX01          S              1    INZ(x'01')
     D £HEX02          S              1    INZ(x'02')
     D £HEX03          S              1    INZ(x'03')
     D £HEX04          S              1    INZ(x'04')
     D £HEX05          S              1    INZ(x'05')
     D £HEX06          S              1    INZ(x'06')
     D £HEX07          S              1    INZ(x'07')
     D £HEX08          S              1    INZ(x'08')
     D £HEX09          S              1    INZ(x'09')
     D £HEX0A          S              1    INZ(x'0A')
     D £HEX0B          S              1    INZ(x'0B')
     D £HEX0C          S              1    INZ(x'0C')
     D £HEX0D          S              1    INZ(x'0D')
     D £HEX0E          S              1    INZ(x'0E')
     D £HEX0F          S              1    INZ(x'0F')
     D*- Gruppo 1 -----------------------------------------------------
     D £HEX10          S              1    INZ(x'10')
     D £HEX11          S              1    INZ(x'11')
     D £HEX12          S              1    INZ(x'12')
     D £HEX13          S              1    INZ(x'13')
     D £HEX14          S              1    INZ(x'14')
     D £HEX15          S              1    INZ(x'15')
     D £HEX16          S              1    INZ(x'16')
     D £HEX17          S              1    INZ(x'17')
     D £HEX18          S              1    INZ(x'18')
     D £HEX19          S              1    INZ(x'19')
     D £HEX1A          S              1    INZ(x'1A')
     D £HEX1B          S              1    INZ(x'1B')
     D £HEX1C          S              1    INZ(x'1C')
     D £HEX1D          S              1    INZ(x'1D')
     D £HEX1E          S              1    INZ(x'1E')
     D £HEX1F          S              1    INZ(x'1F')
     D*- Gruppo 2 -----------------------------------------------------
     D £HEX20          S              1    INZ(x'20')
     D £HEX21          S              1    INZ(x'21')
     D £HEX22          S              1    INZ(x'22')
     D £HEX23          S              1    INZ(x'23')
     D £HEX24          S              1    INZ(x'24')
     D £HEX25          S              1    INZ(x'25')
     D £HEX26          S              1    INZ(x'26')
     D £HEX27          S              1    INZ(x'27')
     D £HEX28          S              1    INZ(x'28')
     D £HEX29          S              1    INZ(x'29')
     D £HEX2A          S              1    INZ(x'2A')
     D £HEX2B          S              1    INZ(x'2B')
     D £HEX2C          S              1    INZ(x'2C')
     D £HEX2D          S              1    INZ(x'2D')
     D £HEX2E          S              1    INZ(x'2E')
     D £HEX2F          S              1    INZ(x'2F')
     D*- Gruppo 3 -----------------------------------------------------
     D £HEX30          S              1    INZ(x'30')
     D £HEX31          S              1    INZ(x'31')
     D £HEX32          S              1    INZ(x'32')
     D £HEX33          S              1    INZ(x'33')
     D £HEX34          S              1    INZ(x'34')
     D £HEX35          S              1    INZ(x'35')
     D £HEX36          S              1    INZ(x'36')
     D £HEX37          S              1    INZ(x'37')
     D £HEX38          S              1    INZ(x'38')
     D £HEX39          S              1    INZ(x'39')
     D £HEX3A          S              1    INZ(x'3A')
     D £HEX3B          S              1    INZ(x'3B')
     D £HEX3C          S              1    INZ(x'3C')
     D £HEX3D          S              1    INZ(x'3D')
     D £HEX3E          S              1    INZ(x'3E')
     D £HEX3F          S              1    INZ(x'3F')
     D*- Gruppo 4 -----------------------------------------------------
     D £HEX40          S              1    INZ(x'40')
     D £HEX41          S              1    INZ(x'41')
     D £HEX42          S              1    INZ(x'42')
     D £HEX43          S              1    INZ(x'43')
     D £HEX44          S              1    INZ(x'44')
     D £HEX45          S              1    INZ(x'45')
     D £HEX46          S              1    INZ(x'46')
     D £HEX47          S              1    INZ(x'47')
     D £HEX48          S              1    INZ(x'48')
     D £HEX49          S              1    INZ(x'49')
     D £HEX4A          S              1    INZ(x'4A')
     D £HEX4B          S              1    INZ(x'4B')
     D £HEX4C          S              1    INZ(x'4C')
     D £HEX4D          S              1    INZ(x'4D')
     D £HEX4E          S              1    INZ(x'4E')
     D £HEX4F          S              1    INZ(x'4F')
     D*- Gruppo 5 -----------------------------------------------------
     D £HEX50          S              1    INZ(x'50')
     D £HEX51          S              1    INZ(x'51')
     D £HEX52          S              1    INZ(x'52')
     D £HEX53          S              1    INZ(x'53')
     D £HEX54          S              1    INZ(x'54')
     D £HEX55          S              1    INZ(x'55')
     D £HEX56          S              1    INZ(x'56')
     D £HEX57          S              1    INZ(x'57')
     D £HEX58          S              1    INZ(x'58')
     D £HEX59          S              1    INZ(x'59')
     D £HEX5A          S              1    INZ(x'5A')
     D £HEX5B          S              1    INZ(x'5B')
     D £HEX5C          S              1    INZ(x'5C')
     D £HEX5D          S              1    INZ(x'5D')
     D £HEX5E          S              1    INZ(x'5E')
     D £HEX5F          S              1    INZ(x'5F')
     D*- Gruppo 6 -----------------------------------------------------
     D £HEX60          S              1    INZ(x'60')
     D £HEX61          S              1    INZ(x'61')
     D £HEX62          S              1    INZ(x'62')
     D £HEX63          S              1    INZ(x'63')
     D £HEX64          S              1    INZ(x'64')
     D £HEX65          S              1    INZ(x'65')
     D £HEX66          S              1    INZ(x'66')
     D £HEX67          S              1    INZ(x'67')
     D £HEX68          S              1    INZ(x'68')
     D £HEX69          S              1    INZ(x'69')
     D £HEX6A          S              1    INZ(x'6A')
     D £HEX6B          S              1    INZ(x'6B')
     D £HEX6C          S              1    INZ(x'6C')
     D £HEX6D          S              1    INZ(x'6D')
     D £HEX6E          S              1    INZ(x'6E')
     D £HEX6F          S              1    INZ(x'6F')
     D*- Gruppo 7 -----------------------------------------------------
     D £HEX70          S              1    INZ(x'70')
     D £HEX71          S              1    INZ(x'71')
     D £HEX72          S              1    INZ(x'72')
     D £HEX73          S              1    INZ(x'73')
     D £HEX74          S              1    INZ(x'74')
     D £HEX75          S              1    INZ(x'75')
     D £HEX76          S              1    INZ(x'76')
     D £HEX77          S              1    INZ(x'77')
     D £HEX78          S              1    INZ(x'78')
     D £HEX79          S              1    INZ(x'79')
     D £HEX7A          S              1    INZ(x'7A')
     D £HEX7B          S              1    INZ(x'7B')
     D £HEX7C          S              1    INZ(x'7C')
     D £HEX7D          S              1    INZ(x'7D')
     D £HEX7E          S              1    INZ(x'7E')
     D £HEX7F          S              1    INZ(x'7F')
     D*- Gruppo 8 -----------------------------------------------------
     D £HEX80          S              1    INZ(x'80')
     D £HEX81          S              1    INZ(x'81')
     D £HEX82          S              1    INZ(x'82')
     D £HEX83          S              1    INZ(x'83')
     D £HEX84          S              1    INZ(x'84')
     D £HEX85          S              1    INZ(x'85')
     D £HEX86          S              1    INZ(x'86')
     D £HEX87          S              1    INZ(x'87')
     D £HEX88          S              1    INZ(x'88')
     D £HEX89          S              1    INZ(x'89')
     D £HEX8A          S              1    INZ(x'8A')
     D £HEX8B          S              1    INZ(x'8B')
     D £HEX8C          S              1    INZ(x'8C')
     D £HEX8D          S              1    INZ(x'8D')
     D £HEX8E          S              1    INZ(x'8E')
     D £HEX8F          S              1    INZ(x'8F')
     D*- Gruppo 9 -----------------------------------------------------
     D £HEX90          S              1    INZ(x'90')
     D £HEX91          S              1    INZ(x'91')
     D £HEX92          S              1    INZ(x'92')
     D £HEX93          S              1    INZ(x'93')
     D £HEX94          S              1    INZ(x'94')
     D £HEX95          S              1    INZ(x'95')
     D £HEX96          S              1    INZ(x'96')
     D £HEX97          S              1    INZ(x'97')
     D £HEX98          S              1    INZ(x'98')
     D £HEX99          S              1    INZ(x'99')
     D £HEX9A          S              1    INZ(x'9A')
     D £HEX9B          S              1    INZ(x'9B')
     D £HEX9C          S              1    INZ(x'9C')
     D £HEX9D          S              1    INZ(x'9D')
     D £HEX9E          S              1    INZ(x'9E')
     D £HEX9F          S              1    INZ(x'9F')
     D*- Gruppo A -----------------------------------------------------
     D £HEXA0          S              1    INZ(x'A0')
     D £HEXA1          S              1    INZ(x'A1')
     D £HEXA2          S              1    INZ(x'A2')
     D £HEXA3          S              1    INZ(x'A3')
     D £HEXA4          S              1    INZ(x'A4')
     D £HEXA5          S              1    INZ(x'A5')
     D £HEXA6          S              1    INZ(x'A6')
     D £HEXA7          S              1    INZ(x'A7')
     D £HEXA8          S              1    INZ(x'A8')
     D £HEXA9          S              1    INZ(x'A9')
     D £HEXAA          S              1    INZ(x'AA')
     D £HEXAB          S              1    INZ(x'AB')
     D £HEXAC          S              1    INZ(x'AC')
     D £HEXAD          S              1    INZ(x'AD')
     D £HEXAE          S              1    INZ(x'AE')
     D £HEXAF          S              1    INZ(x'AF')
     D*- Gruppo B -----------------------------------------------------
     D £HEXB0          S              1    INZ(x'B0')
     D £HEXB1          S              1    INZ(x'B1')
     D £HEXB2          S              1    INZ(x'B2')
     D £HEXB3          S              1    INZ(x'B3')
     D £HEXB4          S              1    INZ(x'B4')
     D £HEXB5          S              1    INZ(x'B5')
     D £HEXB6          S              1    INZ(x'B6')
     D £HEXB7          S              1    INZ(x'B7')
     D £HEXB8          S              1    INZ(x'B8')
     D £HEXB9          S              1    INZ(x'B9')
     D £HEXBA          S              1    INZ(x'BA')
     D £HEXBB          S              1    INZ(x'BB')
     D £HEXBC          S              1    INZ(x'BC')
     D £HEXBD          S              1    INZ(x'BD')
     D £HEXBE          S              1    INZ(x'BE')
     D £HEXBF          S              1    INZ(x'BF')
     D*- Gruppo C -----------------------------------------------------
     D £HEXC0          S              1    INZ(x'C0')
     D £HEXC1          S              1    INZ(x'C1')
     D £HEXC2          S              1    INZ(x'C2')
     D £HEXC3          S              1    INZ(x'C3')
     D £HEXC4          S              1    INZ(x'C4')
     D £HEXC5          S              1    INZ(x'C5')
     D £HEXC6          S              1    INZ(x'C6')
     D £HEXC7          S              1    INZ(x'C7')
     D £HEXC8          S              1    INZ(x'C8')
     D £HEXC9          S              1    INZ(x'C9')
     D £HEXCA          S              1    INZ(x'CA')
     D £HEXCB          S              1    INZ(x'CB')
     D £HEXCC          S              1    INZ(x'CC')
     D £HEXCD          S              1    INZ(x'CD')
     D £HEXCE          S              1    INZ(x'CE')
     D £HEXCF          S              1    INZ(x'CF')
     D*- Gruppo D -----------------------------------------------------
     D £HEXD0          S              1    INZ(x'D0')
     D £HEXD1          S              1    INZ(x'D1')
     D £HEXD2          S              1    INZ(x'D2')
     D £HEXD3          S              1    INZ(x'D3')
     D £HEXD4          S              1    INZ(x'D4')
     D £HEXD5          S              1    INZ(x'D5')
     D £HEXD6          S              1    INZ(x'D6')
     D £HEXD7          S              1    INZ(x'D7')
     D £HEXD8          S              1    INZ(x'D8')
     D £HEXD9          S              1    INZ(x'D9')
     D £HEXDA          S              1    INZ(x'DA')
     D £HEXDB          S              1    INZ(x'DB')
     D £HEXDC          S              1    INZ(x'DC')
     D £HEXDD          S              1    INZ(x'DD')
     D £HEXDE          S              1    INZ(x'DE')
     D £HEXDF          S              1    INZ(x'DF')
     D*- Gruppo E -----------------------------------------------------
     D £HEXE0          S              1    INZ(x'E0')
     D £HEXE1          S              1    INZ(x'E1')
     D £HEXE2          S              1    INZ(x'E2')
     D £HEXE3          S              1    INZ(x'E3')
     D £HEXE4          S              1    INZ(x'E4')
     D £HEXE5          S              1    INZ(x'E5')
     D £HEXE6          S              1    INZ(x'E6')
     D £HEXE7          S              1    INZ(x'E7')
     D £HEXE8          S              1    INZ(x'E8')
     D £HEXE9          S              1    INZ(x'E9')
     D £HEXEA          S              1    INZ(x'EA')
     D £HEXEB          S              1    INZ(x'EB')
     D £HEXEC          S              1    INZ(x'EC')
     D £HEXED          S              1    INZ(x'ED')
     D £HEXEE          S              1    INZ(x'EE')
     D £HEXEF          S              1    INZ(x'EF')
     D*- Gruppo F -----------------------------------------------------
     D £HEXF0          S              1    INZ(x'F0')
     D £HEXF1          S              1    INZ(x'F1')
     D £HEXF2          S              1    INZ(x'F2')
     D £HEXF3          S              1    INZ(x'F3')
     D £HEXF4          S              1    INZ(x'F4')
     D £HEXF5          S              1    INZ(x'F5')
     D £HEXF6          S              1    INZ(x'F6')
     D £HEXF7          S              1    INZ(x'F7')
     D £HEXF8          S              1    INZ(x'F8')
     D £HEXF9          S              1    INZ(x'F9')
     D £HEXFA          S              1    INZ(x'FA')
     D £HEXFB          S              1    INZ(x'FB')
     D £HEXFC          S              1    INZ(x'FC')
     D £HEXFD          S              1    INZ(x'FD')
     D £HEXFE          S              1    INZ(x'FE')
     D £HEXFF          S              1    INZ(x'FF')
     D*----------------------------------------------------------------
      *ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£HEX
********** PREPROCESSOR COPYSTART QILEGEN,£DMSE
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £DMSE
      * Sorgente di origine : SMEDEV/QILEGEN(£DMSE)
      * Esportato il        : 20240715 131100
      *====================================================================
      *IF NOT DEFINED(DMSE_INCLUDED)
      *DEFINE DMSE_INCLUDED
      *
      *
      *  /COPY da inserire insieme con: £DMS
      *
      *----------------------------------------------------------------
     D £D1             S             17    DIM(100)                             Cod.Msg + Msgf
     D £D2             S             45    DIM(100)                             Variabili ass.
     D £D3             S             80    DIM(100)                             Txt schiera
     D £D4             S              1    DIM(45)                              Segmenta in 45
      *ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£DMSE
********** PREPROCESSOR COPYSTART QILEGEN,£DMSI
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £DMSI
      * Sorgente di origine : SMEDEV/QILEGEN(£DMSI)
      * Esportato il        : 20240715 131100
      *====================================================================
      *IF NOT DEFINED(DMSI_INCLUDED)
      *DEFINE DMSI_INCLUDED
      *
      * 1° Messaggio di errore
      *
     D £DMS1M          DS           132
      *----------------------------------------------------------------
      *
      * Messaggio 1 livello
      *
     D £DMS1L          DS           132
      *----------------------------------------------------------------
      *
      * Messaggio 2 livello
      *
     D £DMS2L          DS           500
      *----------------------------------------------------------------
      *
      * STRINGA VARIABILI:
      * 512 bytes per gestire le variabili interne al messaggio (1o/2o)
      *
     D £DMSST          DS           512
      *----------------------------------------------------------------
      *ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£DMSI
********** PREPROCESSOR COPYSTART QILEGEN,£PDS
      *IF NOT DEFINED(£PDS)
      *DEFINE £PDS
     D* ==============================================================
     D* MODIFICHE Ril.  T Au Descrizione
     D* gg/mm/aa  nn.mm i xx Breve descrizione
     D* ==============================================================
     D* B£90630A  02.00   MA Aggiunto campo £PDSDS
     D* B£20806A  05.00   ZZ Aggiunta ds £INZDS inizializzazione pgm
     D* 16/04/03  05.00   ZZ Agg. identificativi job che ha sottomesso
     D* 24/12/04  V2R1    MA il 02.12.03 sono stati inseriti i campi £PDSSI
     D*                      e £PDSNL ed i campi 986/992 non sono liberi
     D*                    ->  Definiti campi £UDU5T e £UDU5A
     D* 10/09/07  V2R3    BS Aggiunte variabili di contesto/ambiente/data riferimento
     D* 17/09/07  V2R3    BS Variabile di attivazione del contesto
     D* 21/03/08  V2R3    BS Aggiunta Variabile VARYING solo per ripresa definizione (Vedi £G25)
     D* 15/09/10  V3R1    MA Aggiunta Variabile £VSELE (numero elementi campi visualizzatori)
     D* 03/12/15  V4R1    AS Eliminata variabile inutilizzta £PDSVY
     D* 23/09/16  V5R1    AS Aggiunta £INZMU (esecuzione in multipiattaforma)
     D* 28/10/16  V5R1   BMA Aggiunto £PDSMI (Message ID)
     V* A£61103A  V5R1    AS Disaccoppiamento utente di sistema da utente applicativo
     V* A£61103A  V5R1   BMA Modificato commento
     V* 21/06/17  V5R1    AS Aggiunto £INZDB (Esecuzione in ambiente con shift-in e shift-out)
     D*----------------------------------------------------------------
     D* OBIETTIVO
     D*  Copy che definisce i campi della DS che contiene
     D*  i dati principali dello stato del programma, la definizione
     D*  della UDS ed i suoi campi standard.
     D*
     D*  Va inserita in tutti i programmi, tra le specifiche I, nella
     D*  zona delle DS, prima dei sottocampi della LDA.
     D*----------------------------------------------------------------
      * Inz
     D £INZFU          S             10    INZ('INZ')
     D £INZME          S             10
     D £INZCO          S             10
      * Variabili di contesto
     D ££ATCO          S              1                                         Attivazione contesto
     D ££CONT          S             10                                         Contesto
     D ££AMBI          S             10                                         Ambiente
     D ££DATE          S              8  0 INZ                                  Data Riferimento
     D*----------------------------------------------------------------
      * Utente applicativo (valorizzato da B£INZ0/£INZSR)
     D £PDSNU          S             10
     D*----------------------------------------------------------------
      * DS esterna per dialogo pgm
     D £PDSDS          DS          2000
      * Variabile di definizione VARYING
     D**** £PDSVY          S          30000    VARYING
      * Nr elementi nelle schiere campi gestite nei visualizzatori £VS (V5TDOC etc.)
     D £VSELE          C                   CONST(400)
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
     D  £UDSMG               223    230  0
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
      *ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£PDS
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
********** PREPROCESSOR COPYSTART QILEGEN,£DMS
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £DMS
      * Sorgente di origine : SMEDEV/QILEGEN(£DMS)
      * Esportato il        : 20240715 131102
      *====================================================================
      *IF NOT DEFINED(DMS_INCLUDED)
      *DEFINE DMS_INCLUDED
      *
      *****************************************************************
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* B£00722A  V3R1    CM Rimozione Gestione Interna degli errori
     V* 12/02/11  V3R1    BS Aggiunte Specifiche per controllo inclusione
    RD* £DMSC1 --> Pulizia iniziali variabili e aree di lavoro
     D*
     D* SCOPO DELLA ROUTINE:
     D* RINFRESCA TUTTE LE VARIABILI PRIMA DI INIZIARE I CONTROLLI
     D*
     D* Valori in uscita:
     D* -----------------
     D* . £D    : Puntatore
     D* . £2    : Puntatore £D2
     D* . £4    : Puntatore £D4
     D* . £D1   : schiera contenente i codici messaggio
     D* . £D2   : schiera contenente le variabili da associare al msg
     D* . £D3   : schiera contenente i testi dei messaggi INTERNI al pgm
     D* . £D4   : schiera contenente 45 bytes da concatenare
     D* . £DMSNR: Valore puntatore
     D* . £DMSTR: Retrive da Msgf (X o G)
     D* . £DMSTP: Reperimento messaggio (Da Msgf o da Pgm)
     D*           (Se i primi 3 byte sono = 'PGM' rilevo il messaggio
     D*            dalla schiera)
     D* . £DMSME: Codice msg
     D* . £DMSVA: Stringa valori delle variabili inserite nel messaggio
     D* . £DMSEL: Testo definito nella schiera messaggi
     D* . £DMSFI: Message file AS/400
     D* . £DMSVS: Flag guida per la segnalazione degli errori:
     D*           ' ' = A Video
     D*           'V' = A Video
     D*           'S' = Su Stampa
     D*
     D* Specifiche I inizializzazione DS:
     D* ---------------------------------
     D* . £DMS1M: Testo 1° messaggio di errore
     D* . £DMS1L: Variabile utilizzata per impostare testo 1 livello
     D* . £DMS2L: Variabile utilizzata per impostare testo 2 livello
     D* . £DMSST: Contenuto: concatenazione variabili in stringa
     D*
      *****************************************************************
      *
     C     £DMSC1        BEGSR
     C                   Z-ADD     0             £D                5 0          Puntatore
     C                   Z-ADD     0             £2                5 0          Puntatore
     C                   Z-ADD     0             £4                5 0          Puntatore
     C                   MOVEL     *BLANKS       £D1                            Sch. Msg
     C                   MOVEL     *BLANKS       £D2                            Sch. Var.
     C                   MOVEL     *BLANKS       £D3                            Sch. Txt pgm
     C                   MOVEL     *BLANKS       £D4                            Sch. 45 byts
     C                   Z-ADD     0             £DMSNR            5 0          Valore punt.
     C                   Z-ADD     0             £DMS2             5 0          "          "
     C                   Z-ADD     0             £DMS4             5 0          "          "
     C                   Z-ADD     0             £DMSLA            2 0          Lunghezza
     C                   Z-ADD     0             £DMSLN            2 0          "       "
     C                   MOVEL     *BLANKS       £DMSTR            1            Retrive Msgf
     C                   MOVEL     *BLANKS       £DMSTP            3            Msgf/schiera
     C                   MOVEL     *BLANKS       £DMSME            7            Codice msg
     C                   MOVEL     *BLANKS       £DMSVA           45            Stringa
     C                   MOVEL     *BLANKS       £DMSEL           80            Txt schiera
     C                   MOVEL     *BLANKS       £DMSFI           10            Msgf
     C                   MOVEL     *BLANKS       £DMSVS            1            Video/Stampa
      *
     C                   MOVEL     *BLANKS       £DMS1M                         DS
     C                   MOVEL     *BLANKS       £DMS2L                         DS
     C                   MOVEL     *BLANKS       £DMS2L                         DS
     C                   MOVEL     *BLANKS       £DMSST                         DS
      *
     C                   ENDSR
      *
      *****************************************************************
    RD* £DMSC2 --> Registrazione errori
     D*
     D* SCOPO DELLA ROUTINE:
     D* REGISTRAZIONE ERRORI
     D*
     D* Se richiesto reperimento da Msgf i primi 3 bytes non devono
     D* essere uguali a 'PGM'.
     D* . REGISTRA IL MESSAGGIO DI ERRORE E LE VARIABILI ASSOCIATE
     D* . REGISTRA IL TESTO DI PRIMO LIVELLO
     D* . REGISTRA LE VARIABILI ASSOCIATE
     D*   (£D1 e £D2)
     D*
     D* Se richiesto reperimento da schiera Messaggi, i primi tre
     D* bytes devono essere uguali a 'PGM'
     D* . REGISTRA NEL COD. MESSAGGIO I PRIMI 7 BYTES DELLA SCHIERA
     D* . REGISTRA NEL TESTO DI PRIMO LIVELLO IL TESTO DELLA SCHIERA
     D*   (£D1 e £D3)
     D*
     D* Se riceve un valore in £DMSIE si attiva la gestione delle
     D* segnalazioni WARNING (Finestra in funzione della gravita')
     D*
     D* Valori in entrata:
     D* ------------------
     D* . £DMSME: Codice messaggio (MSGF XXXnnnn)
     D* . £DMSFI: File messaggi (se blank, assume MSGBA)
     D* . £DMSVA: Stringa di 45 bytes contenente le variabili ancora
     D*           da concatenare nello stesso ordine in cui sono state
     D*           elencate nel testo del messaggio.
     D*           (Da &1 a &99)
     D* . £DMSEL: Testo definito nella schiera messaggi
     D* . £DMSIE: Indicatore di errore per gestione WARNING
     D*
     D* Valori di lavoro:
     D* ------------------
     D* . £D    : Puntatore schiera
     D* . £DMSNR: Valore Puntatore schiera
     D* . £DMSTP: Guida il tipo di reperimento del messaggio (Msgf/Pgm)
     D*           (Se i primi 3 byte sono = 'PGM' rilevo dalla schiera;
     D*            altrimenti dal Message file AS/400)
     D*****************************************************************
      *
     C     £DMSC2        BEGSR
1    C     £DMSNR        IFLT      100
2    C     £DMSFI        IFEQ      *BLANK
     C                   MOVEL(P)  'MSGBA'       £DMSFI
2e   C                   ENDIF
2    C     £DMSTP        IFEQ      *BLANKS
     C                   MOVEL     £DMSME        £DMSTP
2e   C                   ENDIF
     C                   ADD       1             £DMSNR            5 0          Puntatore
     C                   Z-ADD     £DMSNR        £D
2    C     £DMSTP        IFEQ      'PGM'
     C                   MOVEL     *BLANKS       £D1(£D)                        ------------
     C                   MOVEL     £DMSME        £D1(£D)                        Elemeto sch.
     C                   MOVEL     *BLANKS       £D2(£D)                        ------------
     C                   MOVEL     £DMSEL        £D3(£D)                        Txt schiera
2x   C                   ELSE
     C                   MOVEL     £DMSME        £D1(£D)                        Codice Msg
     C                   MOVE      £DMSFI        £D1(£D)                        Msgf
     C                   MOVEL     £DMSVA        £D2(£D)                        Variabili
     C                   MOVEL     *BLANKS       £D3(£D)                        ------------
2e   C                   ENDIF
      * Errori di tipo WARNING
2    C     £DMSIE        IFNE      0
     C     £DMSVS        ANDNE     'S'
     C                   CALL      'B£DMS1'
     C                   PARM                    £D1
     C                   PARM                    £D2
     C                   PARM                    £D3
     C                   PARM                    £PDSNP
     C                   PARM                    £D
     C                   PARM                    £DMSIE
3    C     £DMSIE        IFEQ      0
     C                   MOVEL     *BLANKS       £D1(£D)
     C                   MOVEL     *BLANKS       £D2(£D)
     C                   MOVEL     *BLANKS       £D3(£D)
     C                   SUB       1             £DMSNR
3x   C                   ELSE
     C                   Z-ADD     £DMSIE        £D
     C                   MOVE      '1'           *IN(£D)
3e   C                   ENDIF
2e   C                   ENDIF
     C                   Z-ADD     0             £DMSIE            2 0
      *
     C                   MOVEL     *BLANKS       £DMSME                         Codice msg
     C                   MOVEL     *BLANKS       £DMSTP                         Msgf/schiera
     C                   MOVEL     *BLANKS       £DMSFI                         Msgf
     C                   MOVEL     *BLANKS       £DMSVA                         Stringa
     C                   MOVEL     *BLANKS       £DMSEL                         Txt schiera
1e   C                   ENDIF
     C     £DMS2X        ENDSR
      *
      *****************************************************************
    RD* £DMSC3 --> Elabora messaggi memorizzati
     D*
     D* SCOPO DELLA ROUTINE:
     D* ELABORA TUTTI I MESSAGGI REGISTRATI NELLA SCHIERA £D1
     D*
     D* . £D1   : schiera contenente i codici messaggio
     D* . £D2   : schiera contenente le variabili da associare al msg
     D* . £D3   : schiera contenente i testi dei messaggi da pgm
     D* . £PDSNP: Nome programma
     D*
     D* Valori in entrata:
     D* ------------------
     D* . £DMSVS: Flag guida per la segnalazione degli errori:
     D*           ' ' = A Video
     D*           'V' = A Video
     D*           'S' = Su Stampa
     D*
      *****************************************************************
      *
     C     £DMSC3        BEGSR
1    C     £D1(1)        IFNE      *BLANKS
2    C     £DMSVS        IFEQ      'S'
     C                   CALL      'B£DMS2'
     C                   PARM                    £D1
     C                   PARM                    £D2
     C                   PARM                    £D3
     C                   PARM                    £PDSNP           10
2x   C                   ELSE
     C                   CALL      'B£DMS1'
     C                   PARM                    £D1
     C                   PARM                    £D2
     C                   PARM                    £D3
     C                   PARM                    £PDSNP           10
2e   C                   ENDIF
1x   C                   ELSE
     C                   MOVEL(P)  'VIS'         £DMS7F
     C                   EXSR      £DMSC7
     C                   EXSR      £DMSC8
1e   C                   ENDIF
     C     £DMS3X        ENDSR
      *
      *****************************************************************
    RD* £DMSC4 --> Emissione 1° messaggio
     D*
     D* SCOPO DELLA ROUTINE:
     D* PREPARA IL 1° MESSAGGIO PER L'EMISSIONE A VIDEO
     D*
     D* Valori di lavoro:
     D* -----------------
     D* . £DMSTR: Tipo retrive
     D* . £D    : Puntatore schiera
     D*
     D* Valori di uscita:
     D* -----------------
     D*
     D* - Sempre
     D*   . £DMS1M: testo del 1° messaggio di errore
     D*
     D* - A disposizione:
     D*   - Se richiesto reperimento da Msgf:
     D*     . £DMS1L: testo 1 livello
     D*     . £DMS2L: testo secondo livello
     D*
     D*   - Se richiesto reperimento da schiera Messaggi:
     D*     . £DMSME: codice messaggio
     D*     . £DMSEL: testo di errore (80 caratteri)
     D*
      *****************************************************************
      *
     C     £DMSC4        BEGSR
     C     £D1(1)        CABEQ     *BLANKS       £DMS4X
      *
     C                   MOVEL     *BLANKS       £DMS1M
      *
      * Da msgf
      *
1    C     £D3(1)        IFEQ      *BLANKS
     C                   MOVEL     'X'           £DMSTR
     C                   Z-ADD     1             £D
     C                   EXSR      £DMSC5
     C                   MOVEL     £DMS1L        £DMS1M
1x   C                   ELSE
      *
      * Da schiera messaggi
      *
     C                   MOVEL     *BLANKS       £DMSME
     C                   MOVEL     £D1(1)        £DMSME
     C                   MOVEL     *BLANKS       £DMSFI
     C                   MOVEL     *BLANKS       £DMSEL
     C                   MOVEL     £D3(1)        £DMS1L
     C                   MOVEL     £DMS1L        £DMS1M
1e   C                   ENDIF
      *
     C     £DMS4X        ENDSR
      *
      *****************************************************************
    RD* £DMSC5 --> Reperimento del messaggio
     D*
    HD*  SCOPO DELLA ROUTINE:
     D*  Esecuzione retrive message per:
     D*  Se 'X' visualizzazione finestre utente finale
     D*  Se 'G' gestione del messaggio con CHGMSGD
     D*
     D* Valori in entrata:
     D* ------------------
     D* . £DMSTR: Tipo retrive
     D* . £D    : Puntatore schiera
     D*
      *****************************************************************
      *
     CSR   £DMSC5        BEGSR
     C                   MOVEL     £DMSTR        £DMSOP            1            Opzione
     C                   MOVEL     £D1(£D)       £DMSCM            7            Codice Msg
     C                   MOVE      *BLANKS       £DMS1L                         1° Livello
     C                   MOVE      £D1(£D)       £DMSMF           10            Msgf
     C                   MOVE      *BLANKS       £DMS2L                         2° Livello
     C                   MOVEL     £D2(£D)       £DMSST                         Stringa Var.
      *
     C                   CALL      'B£DMS1CL'
     C                   PARM                    £DMSOP
     C                   PARM                    £DMSCM
     C                   PARM                    £DMS1L
     C                   PARM                    £DMSMF
     C                   PARM                    £DMS2L
     C                   PARM                    £DMSST
      *
     C                   MOVEL     *BLANKS       £DMSOP                         Opzione
     C                   MOVEL     *BLANKS       £DMSCM                         Codice Msg
     C                   MOVEL     *BLANKS       £DMSMF                         Msgf
     C                   MOVEL     *BLANKS       £DMSST                         Stringa Var.
      *
     C                   MOVE      *BLANKS       £DMSTR            1            Tipo Retrive
      *
     CSR   £DMS5X        ENDSR
      *
      *****************************************************************
    RD* £DMSC6 --> Concatenazione variabili
     D*
    HD*  SCOPO DELLA ROUTINE:
     D*  Concatena le variabili da passare al MESSAGGIO PARAMETRICO
     D*  rispettando la loro lunghezza originaria (Fisica).
     D*
     D* Regola di impostazione £DMSVA
     D* -----------------------------
     D* Attraverso delle CAT, la regola di scrittura di ciascuna
     D* variabile sara' la seguente:
     D*
     D* !-----!------!---------!
     D* ! ' ' !  DD  ! XXXXXXX !
     D* !-----!------!---------!
     D*
     D* . ' ' = Spazio obbligatorio per inizio dati associati alla
     D*         variabile da concatenare
     D* . DD  = Lunghezza fisica variabile (Come da Data base)
     D*         da concatenare.
     D*         Deve essere sempre epressa sempre su due caratteri
     D* . XXX = Variabile
     D*
     D* Valori in entrata:
     D* ------------------
     D* . £D    : Messaggio in elaborazione
     D* . £DMSVA: Stringa ( <Dimensione fisica e variabile> )
     D*
      *****************************************************************
      *
     CSR   £DMSC6        BEGSR
     C                   MOVEA     £DMSVA        £D4
     C                   Z-ADD     0             £DMS4
     C                   Z-ADD     0             £DMS2
     C                   Z-ADD     0             £DMSLA
     C                   Z-ADD     0             £DMSLN
      *
      * Ciclo 01
      *
     C     £DMS61        TAG
     C                   ADD       1             £DMS4
     C     £DMS4         CABGT     45            £DMS6X
     C                   Z-ADD     £DMS4         £4
      *
      * Caricamento Effettivo della variabile
      *
1    C     £D4(£4)       IFNE      *BLANKS
     C                   ADD       1             £DMS2
     C     £DMS2         CABGT     45            £DMS6X
     C                   Z-ADD     £DMS2         £2
     C                   MOVEL     £D4(£4)       £D2(£2)
     C                   SUB       1             £DMSLN
     C                   GOTO      £DMS61
1e   C                   ENDIF
      *
      * Ciclo 02
      *
     C     £DMS62        TAG
      *
      * Caricamento del restante spazio con blanks
      *
1    C     £DMSLN        IFGT      *ZEROS
     C                   ADD       1             £DMS2
     C     £DMS2         CABGT     45            £DMS6X
     C                   Z-ADD     £DMS2         £2
     C                   MOVEL     *BLANKS       £D2(£2)
     C                   SUB       1             £DMSLN
     C                   GOTO      £DMS62
1e   C                   ENDIF
      *
      * Memorizzazione lunghezza fisica variabile
      *
     C                   MOVE      *ALL'0'       £DMSLA
     C     £DMS63        TAG
      *
      * 1° Carattere
      *
     C                   ADD       1             £DMS4
     C     £DMS4         CABGT     45            £DMS6X
     C                   Z-ADD     £DMS4         £4
     C     £D4(£4)       CABEQ     *BLANKS       £DMS63
     C                   MOVEL     £D4(£DMS4)    £DMSLA
      *
      * 2° Carattere
      *
     C                   ADD       1             £DMS4
     C     £DMS4         CABGT     45            £DMS6X
     C                   Z-ADD     £DMS4         £4
     C                   MOVE      £D4(£DMS4)    £DMSLA
     C                   GOTO      £DMS61
      *
     CSR   £DMS6X        ENDSR
     D*----------------------------------------------------------------
     C     £DMSC7        BEGSR
      *
     C     ££B£2L        IFEQ      '11'
     C     £DMS7F        IFEQ      'INI'
     C                   MOVEL     ££B£2L        ££B£2L            2
     C                   BITOFF    '01234567'    £ATRIN            1
     C                   BITON     '27'          £ATRIN
     C     £ATRIN        CAT       '>':0         W$B£2L            2
     C                   ENDIF
      *
  MO>C                   IF        ££B£2J = '1'
  MO>C                   CALL      'B£DMS7'                             37
  MO>C                   PARM                    £DMS7F           10
  MO>C                   PARM                    £DMS7M           10
  MO>C                   PARM                    £DMS7T            2
  MO>C                   PARM                    £DMS7P           10
  MO>C                   PARM                    £DMS7C           15
  MO>C                   PARM                    £DMS75            1
  MO>C                   ELSE
     C                   EVAL      *IN37=*OFF
     C                   CALL      'B£DMS7'
     C                   PARM                    £DMS7F           10
     C                   PARM                    £DMS7M           10
     C                   PARM                    £DMS7T            2
     C                   PARM                    £DMS7P           10
     C                   PARM                    £DMS7C           15
     C                   PARM                    £DMS75            1
  MO>C                   ENDIF
     C                   MOVEL     *BLANKS       £DMS7F
     C                   MOVEL     *BLANKS       £DMS7M
     C                   ENDIF
     C                   ENDSR
     D*----------------------------------------------------------------
     C     £DMSC8        BEGSR
  MO>C                   IF        ££B£2J = '1'
  MO>C                   CALL      'B£DMS8'                             37
  MO>C                   PARM                    £PDSNP
  MO>C                   PARM                    £DMSRL           10
  MO>C                   PARM                    £DMSPT           10
  MO>C                   ELSE
     C                   EVAL      *IN37=*OFF
     C                   CALL      'B£DMS8'
     C                   PARM                    £PDSNP
     C                   PARM                    £DMSRL           10
     C                   PARM                    £DMSPT           10
  MO>C                   ENDIF
     C                   ENDSR
      *ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£DMS

