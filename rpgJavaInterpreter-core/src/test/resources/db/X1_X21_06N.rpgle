     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 10/12/20  002090  BUSFIO Creato nuovo servizio nativo
     V* 14/12/20  002090  BUSFIO Modificato metodo cancellazione
     V* 14/12/20  002090  BUSFIO Formattata data estesa
     V* 17/12/20  002090  BUSFIO Servizio semplificato con una funzione metodo con accesso nativo
     V* 22/12/20  002090  BUSFIO Riadattato servizio per l'accesso nativo
     V* ==============================================================
     V* NB: tutte le /COPY incluse (DEC, RITES, £TABJATDS...) servono
     V* ==============================================================
     FVERAPG9L  IF   E           K DISK
      *--------------------------------------------------------------------------------------------*
      *Creazione Array Collaboratori
     D CODCOL          S              6    DIM(20)                                dim(999)
     D $N              S              2  0                                         n 3 0
     D $A              S              2  0
     D $$COMM          S             15                                           Commessa
     D*$$COMM          S                   LIKE(V£CDC)                            Commessa
     D $$MM            S              2  0
     D §STRCAL         S             20                                          Stringa calendario
      *--------------------------------------------------------------------------------------------*
     D$POSTI           S              3  0 INZ(30)
     D$PRNT            S              3  0                                       Posti Prenotati
     D$DSPN            S              3  0                                       Posti Disponibili
     D$UTER            S              3  0                                       Record Utente
      *--------------------------------------------------------------------------------------------*
     D $$MESE          S              8  0
     D $$GG            S              2  0                                        Numero Giorni
     D $$DATAFMT       S             10                                           Data Formattata
     D $$DATA          S              8  0
     D*$$DATA          S                   LIKE(V£DATA)
     D $$NOME          S             15                                           Collaboratore
     D $$SEDE          S              3
     D $$TPPT          S             15                                           Tipo Prenotazione
     D $$USER          S              1                                           Prenotazione 0-1
      *-Variabili per Log -------------------------------------------------------------------------*
      *MAIN
     D $TIMST          S               Z   INZ                                   Tempo iniziale
     D $TIMEN          S               Z   INZ                                   Tempo finale
     D $TIMMS          S             10I 0                                       Tempo millisecondi
      *
     D $MSG            S             52                                          Output
      *-DS TEMPORANEA VERAPG0F---------------------------------------------------------------------*
      *D VERAPGR         DS           941
      *D V£IDOJ                  1     10A
      *D V£ATV0                 11     11A
      *D V£DATA                 12     19  0
      *D V£NOME                 20     34A
      *D V£CDC                  35     49A
      *D V£TIPO                 50     52A
      *D V£ORE                  53     57  2
      *D V£VIAC                 58     62  2
      *D V£VIAS                 63     67  2
      *D V£KM                   68     72  0
      *D V£KMSE                 73     77  0
      *D V£PEDC                 78     88  2
      *D V£PEDS                 89     99  2
      *D V£RISC                100    110  2
      *D V£RISS                111    121  2
      *D V£ALBC                122    132  2
      *D V£ALBS                133    143  2
      *D V£SPEC                144    154  2
      *D V£SPES                155    165  2
      *D V£CAUS                166    168A
      *D V£TCOM                169    169A
      *D V£COM1                170    379A
      *D V£COM2                380    589A
      *D V£DIAR                590    594  2
      *D V£DIUS                595    599  2
      *D V£NFAT                600    609A
      *D V£DFAT                610    617  0
      *D V£COD1                618    632A
      *D V£COD2                633    647A
      *D V£COD3                648    662A
      *D V£COD4                663    677A
      *D V£COD5                678    692A
      *D V£COD6                693    707A
      *D V£COD7                708    722A
      *D V£COD8                723    737A
      *D V£COD9                738    752A
      *D V£COD0                753    767A
      *D V£NUM1                768    788  6
      *D V£NUM2                789    809  6
      *D V£NUM3                810    830  6
      *D V£NUM4                831    851  6
      *D V£NUM5                852    872  6
      *D V£FL01                873    873A
      *D V£FL02                874    874A
      *D V£FL03                875    875A
      *D V£FL04                876    876A
      *D V£FL05                877    877A
      *D V£FL06                878    878A
      *D V£FL07                879    879A
      *D V£FL08                880    880A
      *D V£FL09                881    881A
      *D V£FL10                882    882A
      *D V£FL11                883    883A
      *D V£FL12                884    884A
      *D V£FL13                885    885A
      *D V£FL14                886    886A
      *D V£FL15                887    887A
      *D V£FL16                888    888A
      *D V£FL17                889    889A
      *D V£FL18                890    890A
      *D V£FL19                891    891A
      *D V£FL20                892    892A
      *D V£USIN                893    903A
      *D V£DTIN                904    911  0
      *D V£ORIN                912    917  0
      *D V£USAG                918    927A
      *D V£DTAG                928    935  0
      *D V£ORAG                936    941  0
      *--/COPY £TABJATDS---------------------------------------------------------------------------*
     D JAT$DS          DS           100
      * N° 03 - Costruttore          V3 PJ1         2
     D  T$JATA                 1      2
      * N° 04 - Param. costruttore   **            15
     D  T$JATB                 3     17
      * N° 05 - Oggetto riferimento  OG            12
     D  T$JATC                18     29
      * N° 06 - Modo gestione cache  V2 JATGC       1
     D  T$JATD                30     30
      * N° 07 - Forma grafica        V2 JATFG       1
     D  T$JATE                31     31
      * N° 08 - Par. forma grafica                 15
     D  T$JATF                32     46
      * N° 09 - Scadenza cache       V2 JATSC       1
     D  T$JATG                47     47
      *-----/COPY £TABB£1DS------------------------------------------------------------------------*
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
      *
      *-/COPY £PDS --------------------------------------------------------------------------------*
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
      *
      *--------------------------------------------------------------------------------------------*
      * ENTRY
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
      *--------------------------------------------------------------------------------------------*
      * Log MAIN - Start time
     C                   TIME                    $TIMST
      * Display message
     C                   EVAL      $MSG='Start MAIN RPG pgm'
     C     $MSG          DSPLY     £PDSSU
      * Impostazioni iniziali
     C                   EXSR      IMP0
      * Scelta di funzione e metodo
1    C                   SELECT
      * Generazione matrice
1x   C                   WHEN      %SUBST($UIBME:1:7)='MAT.CAL'
     C                   EXSR      MATCAL
1e   C                   ENDSL
      *
     C     G9MAIN        TAG
      *
     C                   EXSR      FIN0
      * Log MAIN - End time
     C                   TIME                    $TIMEN
      * Elapsed time
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
     C                   EVAL      $TIMMS=$TIMMS/1000
      * Display message
     C                   EVAL      $MSG='End MAIN RPG pgm - '
     C                                 +' Metodo: '+%TRIM(%SUBST($UIBME:1:7))
     C                                 +' Tempo: '+%TRIM(%EDITC($TIMMS:'Q'))
     C                                 +'ms'
     C     $MSG          DSPLY     £PDSSU
      *
     C                   SETON                                        RT
      *--------------------------------------------------------------*
    RD*  Impostazioni iniziali
      *--------------------------------------------------------------*
     C     IMP0          BEGSR
      *
     C                   EVAL      $$XML=%TRIM(£JAXXML)
      *
      *Controllo VARIABILI in ingresso
     C                   SELECT
     C                   WHEN      %SUBST($UIBME:1:7)='MAT.CAL'
     C                   IF        $UIBK2=''
     C                   GOTO      G9MAIN
     C                   ENDIF
     C                   ENDSL
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD*  Comunico Dati in matrice e proprio Setup
      *--------------------------------------------------------------*
     C     MATCAL        BEGSR
      * Aggiungi griglia
     C                   EVAL      $$XMLPAYLOAD='<Griglia><Colonna Cod='
     C                             +'"XXDAT1"  Txt="Data" Lun="08" IO="O"'
     C                             +' Ogg="D8*YYMD" Fill="ASSE"></Colonna>'
     C                             +'<Colonna Cod="XXDESC" Txt="Descrizione"'
     C                             +' Lun="15" IO="H" ></Colonna>'
     C                             +'<Colonna Cod="XXDDAT" Txt="Data Estesa"'
     C                             +' Lun="30" IO="H" ></Colonna>'
     C                             +'<Colonna Cod="XXCOD1" Txt="Codice sede"'
     C                             +' Lun="5" IO="H" Ogg="CNSED"></Colonna>'
     C                             +'<Colonna Cod="XXPTOT" Txt="Posti Totali"'
     C                             +' Lun="03" IO="H" Ogg="NR"></Colonna>'
     C                             +'<Colonna Cod="XXPATT" Txt="Posti Attuali"'
     C                             +' Lun="03" IO="O" Ogg="NR" Fill="SERIE" >'
     C                             +'</Colonna>'
     C                             +'<Colonna Cod="XXPPRE"'
     C                             +' Txt="Posti Prenotati"'
     C                             +' Lun="03" IO="H" Ogg="NR"></Colonna>'
     C                             +'<Colonna Cod="XXUSER" Txt="Prenotazione"'
     C                             +' Lun="01" IO="H" ></Colonna>'
     C                             +'</Griglia>'
      * Aggiungi righe - inizializzazione
     C                   EVAL      $$XMLPAYLOAD=%TRIM($$XMLPAYLOAD)
     C                              +'<Righe>'
      * Recupero Data
      * customization by lana: set MESE to 20190215
     C                   EVAL      $$MESE=20190215
      *
     C                   EVAL      $$MM=%INT(%SUBST(%EDITC($$MESE:'X'):5:2))
      *
     C                   IF        $$MM=01 OR $$MM=03 OR $$MM=05 OR $$MM=07
     C                             OR $$MM=08 OR $$MM=10 OR $$MM=12
     C                   EVAL      $$GG=31
     C                   ENDIF
     C                   IF        $$MM=04 OR $$MM=06 OR $$MM=09 OR $$MM=11
     C                   EVAL      $$GG=30
     C                   ENDIF
     C                   IF        $$MM=02
     C                   EVAL      $$GG=28
     C                   ENDIF
     C                   EVAL      $MSG=%EDITC($$MESE:'X')
     C     $MSG          DSPLY     £PDSSU
     C                   EVAL      $MSG=%EDITC($$GG:'X')
     C     $MSG          DSPLY     £PDSSU
      * Creazione Chiave
     C     K9L           KLIST
     C                   KFLD                    $$MESE
     C                   KFLD                    $$COMM
      *
      * customized by lana do one time and force $COMM to RDSSVI
2x   C                   DO        1
      * settaggio variabili
     C                   EVAL      $$COMM='RDSSVI'
     C                   EVAL      $PRNT=0
     C                   EVAL      $DSPN=0
     C                   CLEAR                   CODCOL
     C                   EVAL      $N=1
     C                   EVAL      $$USER='0'
      * Ciclo lettura
     C     K9L           SETLL     VERAPG9L
     C                   DO        *HIVAL
     C     K9L           READE     VERAPG9L
      *
      * Fine del file - esce
4x   C                   IF        %EOF
     C                   LEAVE
4e   C                   ENDIF
      *Condizione sulla SEDE
     C                   IF        V£COD1<>%TRIM($UIBK2)
     C                   ITER
     C                   ENDIF
      *Condizione su utente
      *customization by lana: search for SANBRU
     C                   IF        V£NOME=%TRIM('SANBRU')
     C                   EVAL      $UIBK6='OK'
     C                   LEAVE
     C                   ENDIF
      *Controllo presenza collaboratore
     C                   EVAL      $A=%LOOKUP(V£NOME:CODCOL:1:$N)
     C                   IF        $A<>0
     C                   ITER
     C                   ELSE
     C                   EVAL      CODCOL($N)=V£NOME
     C                   EVAL      $N=$N+1
     C                   ENDIF
      *
      * Conta dei posti Prenotati
     C                   IF        V£ATV0='2'
     C                   EVAL      $PRNT=$PRNT+1
     C                   ENDIF
      *
3e   C                   ENDDO
      * Aumenti contatori
     C                   EVAL      $DSPN=$POSTI-$PRNT
      * Verifico se l'utente è prenotato
     C                   IF        $$USER='1'
     C                   EVAL      §STRCAL='Prenotato'
     C                   ELSE
     C                   EVAL      §STRCAL='Posti Sede:'+%EDITC($DSPN:'Z')
     C                   ENDIF
      * Formattazione data
     C                   EVAL      $$DATAFMT=%SUBST(%EDITC($$MESE:'Z'):7:2)+'/'
     C                                      + %SUBST(%EDITC($$MESE:'Z'):5:2)+'/'
     C                                      + %SUBST(%EDITC($$MESE:'Z'):1:4)
      * Scrittura riga
     C                   EVAL      $$XMLPAYLOAD=%TRIM($$XMLPAYLOAD)
     C                              +'<Riga Fld="'+%EDITC($$MESE:'Z')
     C                                   +'|'+%TRIM(§STRCAL)
     C                                   +'|'+$$DATAFMT
     C                                   +'|'+$UIBK2
     C                                   +'|'+%EDITC($POSTI:'Z')
     C                                   +'|'+%EDITC($DSPN:'Z')
     C                                   +'|'+%EDITC($PRNT:'Z')
     C                                   +'|'+%TRIM($$USER)
     C                                   +'"/>'
      * Aggiungo 1 alla data
     C                   EVAL      $$MESE=$$MESE+1
      *
2e   C                   ENDDO
      * Aggiungi righe - finalizzazione
     C                   EVAL      $$XMLPAYLOAD=%TRIM($$XMLPAYLOAD)
     C                              +'</Righe>'
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD*  Chiusura e invio su coda del buffer residuo
      *--------------------------------------------------------------*
     C     FIN0          BEGSR
      * Aggiungo header e footer a payload
     C                   EVAL      $$XML=%TRIM($$XML)
     C                             +%TRIM($$XMLPAYLOAD)
      * Scrittura XML
     C     $$XML         DSPLY
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Routine impostazioni di avvio programma
      *--------------------------------------------------------------*
     C     £INIZI        BEGSR
      *
      *
      * ENTRY
     C     *ENTRY        PLIST
     C                   PARM                    £UIBDS
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Log Specifico applicazione
      *--------------------------------------------------------------*
     C     £JAX_LOG      BEGSR
     C                   ENDSR
