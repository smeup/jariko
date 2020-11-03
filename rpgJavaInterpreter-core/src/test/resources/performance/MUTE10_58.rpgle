     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 20/10/20  001859 LANMAM Created
     V*=====================================================================
     V*=====================================================================
     V* PURPOSE
     V*  Test program for X1_X21_03
     V*=====================================================================
     V* 20/07/07  V2R2    AS Creazione
     V* 08/10/10  V3R1    CM Aggiunto metodo invio SETUP attraverso la nuova metodologia
     V* B£30901B  V4R1    CM Soistituita JAX02 con J15
     V* 16/07/13  V4R1    CM Mancata pulizia campo JAXCP prima del richiam £JAX_ISET
     V* 19/07/13  V4R1 BERNI Aggiunta definizione di costante per lingue
     V* 15/04/15  V4R1    AS Eliminati riferimenti a vecchia gestione lingue
     V* 07/07/16  V4R1    AS Aggiunto esempio di albero multilivello
     V* 31/07/20  BUSFIO  BUSFIO Esempio con lettura, scrittura e aggiornamento da DB
     V* 04/08/20  BUSFIO  BUSFIO Esempio per Accesso Sedi
     V* 05/08/20  BUSFIO  BUSFIO Spostato G9MAIN
     V* 07/08/20  BUSFIO  BUSFIO Copiate /COPY all'interno del servizio
     V* 12/08/20  BUSFIO  BERNI  Correzioni
     V* 24/08/20  BUSFIO  BUSFIO Sostituito provvisoriamente VERAPG con DS
     V* 25/08/20  002090  BUSFIO Assegnato Branch e rinominato
     V* 01/09/20  002090  BUSFIO Aggiunta DS provvisoria per mokkare i dati
     V* 02/09/20  002090  BUSFIO Ricompilato a seguito di modifica dell'array
     V* 09/09/20  002090  BUSFIO Ricompilato dopo modifiche alla DS e sistemato spazi in XML
     V* 29/09/20  002090  BUSFIO Implementato Metodo che restituisce Lista Sedi
     V* 02/10/20  002090  BUSFIO Pulito il codice
     V* 06/10/20  002090  BUSFIO Adattato codice al multipiattaforma
     V* 13/10/20  002090  BUSFIO Modificato codice per multipiattaforma
     V* 14/10/20  002090  BUSFIO Modificato script
     V* ==============================================================
     V* NB: tutte le /COPY incluse (DEC, RITES, £TABJATDS...) servono
     V* ==============================================================
      *Creazione Array Collaboratori
     D CODCOL          S              6    DIM(20)                                dim(999)
     D $N              S              2  0                                         n 3 0
     D $A              S              2  0
     D $$COMM          S             15                                           Commessa
      *--------------------------------------------------------------------------------------------*
     D$POSTI           S              3  0 INZ(30)
     D$PRNT            S              3  0                                       Posti Prenotati
     D$DSPN            S              3  0                                       Posti Disponibili
     D$UTER            S              3  0                                       Record Utente
      *-Creazione XAIDOJ---------------------------------------------------------------------------*
     D £CRN1           S             10  0
      *--------------------------------------------------------------------------------------------*
     D $$MESE          S              8  0
     D $$GG            S              2  0                                        Numero Giorni
     D $$DATA          S              8  0
     D $$NOME          S             15                                           Collaboratore
     D $$SEDE          S              3
     D $$TPPT          S             15                                           Tipo Prenotazione
     D $$USER          S              1                                           Prenotazione 0-1
      *-Array - Lista Sedi-------------------------------------------------------------------------*
     D TRESED          S            100    DIM(14) CTDATA PERRCD(1)             _NOTXT
     D $NSED           S              2  0                                       Numeratore Array
      *--------------------------------------------------------------------------------------------*
     D §DATEST         S             30
     D $NDAT           S              2  0
      *-Button dinamico----------------------------------------------------------------------------*
     D §DESC           S             30
     D §STRFUN         S            100
     D $NDO            S              2  0                                       Numero volte cicla
      *-Variabili Gauge ---------------------------------------------------------------------------*
     D $MAX            S              3  0                                       Max per Gauge
     D $MIN            S              3  0                                       Min per Gauge
      *-Variabili per Log -------------------------------------------------------------------------*
      *MAIN
     D $TIMST          S               Z   INZ                                   Tempo iniziale
     D $TIMEN          S               Z   INZ                                   Tempo finale
     D $TIMMS          S             10I 0                                       Tempo millisecondi
      *£IXA
     D $TIMSTIXA       S               Z   INZ                                   Tempo iniziale
     D $TIMENIXA       S               Z   INZ                                   Tempo finale
     D $TIMMSIXA       S             10I 0                                       Tempo millisecondi
      *
     D $MSG            S             52                                          Output
      *-SQLD variabile -----------------------------------------------------------------------------*
     D §SQL            S           1000                                          Stringa SQL
     D AP              S              1     INZ('''')
      *-DS TEMPORANEA VERAPG0F---------------------------------------------------------------------*
     D VERAPGR         DS           941
     D V£IDOJ                  1     10A
     D V£ATV0                 11     11A
     D V£DATA                 12     19  0
     D V£NOME                 20     34A
     D V£CDC                  35     49A
     D V£TIPO                 50     52A
     D V£ORE                  53     57  2
     D V£VIAC                 58     62  2
     D V£VIAS                 63     67  2
     D V£KM                   68     72  0
     D V£KMSE                 73     77  0
     D V£PEDC                 78     88  2
     D V£PEDS                 89     99  2
     D V£RISC                100    110  2
     D V£RISS                111    121  2
     D V£ALBC                122    132  2
     D V£ALBS                133    143  2
     D V£SPEC                144    154  2
     D V£SPES                155    165  2
     D V£CAUS                166    168A
     D V£TCOM                169    169A
     D V£COM1                170    379A
     D V£COM2                380    589A
     D V£DIAR                590    594  2
     D V£DIUS                595    599  2
     D V£NFAT                600    609A
     D V£DFAT                610    617  0
     D V£COD1                618    632A
     D V£COD2                633    647A
     D V£COD3                648    662A
     D V£COD4                663    677A
     D V£COD5                678    692A
     D V£COD6                693    707A
     D V£COD7                708    722A
     D V£COD8                723    737A
     D V£COD9                738    752A
     D V£COD0                753    767A
     D V£NUM1                768    788  6
     D V£NUM2                789    809  6
     D V£NUM3                810    830  6
     D V£NUM4                831    851  6
     D V£NUM5                852    872  6
     D V£FL01                873    873A
     D V£FL02                874    874A
     D V£FL03                875    875A
     D V£FL04                876    876A
     D V£FL05                877    877A
     D V£FL06                878    878A
     D V£FL07                879    879A
     D V£FL08                880    880A
     D V£FL09                881    881A
     D V£FL10                882    882A
     D V£FL11                883    883A
     D V£FL12                884    884A
     D V£FL13                885    885A
     D V£FL14                886    886A
     D V£FL15                887    887A
     D V£FL16                888    888A
     D V£FL17                889    889A
     D V£FL18                890    890A
     D V£FL19                891    891A
     D V£FL20                892    892A
     D V£USIN                893    903A
     D V£DTIN                904    911  0
     D V£ORIN                912    917  0
     D V£USAG                918    927A
     D V£DTAG                928    935  0
     D V£ORAG                936    941  0
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
      *--------------------------------------------------------------------------------------------*
      * Componente:
     D  $UIBPG         S             10
      * Programma:
     D  $UIBFU         S             10
      * Funzione/metodo:
     D  $UIBME         S             10
      *
      * T1:
     D  $UIBT1         S              2
      * P1:
     D  $UIBP1         S             10
      * K1:
     D  $UIBK1         S             15
      * T2:
     D  $UIBT2         S              2
      * P2:
     D  $UIBP2         S             10
      * K2:
     D  $UIBK2         S             15
      * T3:
     D  $UIBT3         S              2
      * P3:
     D  $UIBP3         S             10
      * K3:
     D  $UIBK3         S             15
      *
      * P:
     D  $UIBPA         S            256
      *
      * T4:
     D  $UIBT4         S              2
      * P4:
     D  $UIBP4         S             10
      * K4:
     D  $UIBK4         S             15
      * T5:
     D  $UIBT5         S              2
      * P5:
     D  $UIBP5         S             10
      * K5:
     D  $UIBK5         S             15
      * T6:
     D  $UIBT6         S              2
      * P6:
     D  $UIBP6         S             10
      * K6:
     D  $UIBK6         S             15
      *
      * INPUT:
     D  $UIBD1         S          30000
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
      *-/COPY £IXA---------------------------------------------------------------------------------*
     D £IXAFU          S             10
     D £IXAME          S             10
     D £IXANK          S             10
     D £IXACO          S          30000
     D £IXA35          S              1
     D £IXACOD         S              4
      *-/COPY £SQLD--------------------------------------------------------------------------------*
     D £SQLD_FUNFU     S             50
     D £SQLD_FUNME     S             50
     D £SQLDString     S           5000
     D £SQLDTable      S            100
     D £SQLD35         S              1
     D £SQLDCOD        S              4
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
1x   C                   WHEN      %SUBST($UIBME:1:6)='MATPRV'
     C                   EXSR      MATPRV
      * Ricerca record e singola riga Matrice
1x   C                   WHEN      %SUBST($UIBME:1:6)='READRW'
     C                   EXSR      READRW
      * Scrittura Record in DB
1x   C                   WHEN      %SUBST($UIBME:1:6)='WRIADD'
     C                   EXSR      WRIADD
      * Delete Record in DB
1x   C                   WHEN      %SUBST($UIBME:1:6)='DELREC'
     C                   EXSR      DELREC
      * Matrice Collaboratori in Sede
1x   C                   WHEN      %SUBST($UIBME:1:6)='MATCOL'
     C                   EXSR      MATCOL
      * Albero Lista sedi
1x   C                   WHEN      %SUBST($UIBME:1:6)='LSTSED'
     C                   EXSR      LSTSED
      * Riga Accesso Collaboratore
     C                   WHEN      %SUBST($UIBME:1:6)='ACCCOL'
     C                   EXSR      ACCCOL
      * Albero per Sede appartenenza
     C                   WHEN      %SUBST($UIBME:1:6)='YRSSED'
     C                   EXSR      YRSSED
      * Button dinamici Conferma/Cancella
     C                   WHEN      %SUBST($UIBME:1:6)='TREBTN'
     C                   EXSR      TREBTN
      * Dati per Gauge
     C                   WHEN      %SUBST($UIBME:1:6)='GAU'
     C                   EXSR      GAU
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
     C                                 +' Metodo: '+%TRIM(%SUBST($UIBME:1:6))
     C                                 +' Tempo: '+%TRIM(%EDITC($TIMMS:'Q'))
     C                                 +'ms'
     C     $MSG          DSPLY     £PDSSU
      *
    MU* TIMEOUT(100)
     C                   SETON                                        RT
      *--------------------------------------------------------------*
    RD*  Impostazioni iniziali
      *--------------------------------------------------------------*
     C     IMP0          BEGSR
      *
     C                   CALL      '£JAX_IMP0'
     C                   PARM                    £JAXXML
     C                   EVAL      $$XML=%TRIM(£JAXXML)
      *
      *Controllo VARIABILI in ingresso
     C                   SELECT
     C                   WHEN      %SUBST($UIBME:1:6)='MATPRV'
     C                   IF        $UIBK2='' OR $UIBK3='' OR $UIBK4=''
     C                   GOTO      G9MAIN
     C                   ENDIF
      *
     C                   WHEN      %SUBST($UIBME:1:6)='READRW'
     C                   IF        $UIBK2='' OR $UIBK3=''
     C                   GOTO      G9MAIN
     C                   ENDIF
      *
     C                   WHEN      %SUBST($UIBME:1:6)='WRIADD'
     C                   IF        $UIBK2='' OR $UIBK3='' OR $UIBK4=''
     C                   GOTO      G9MAIN
     C                   ENDIF
      *
     C                   WHEN      %SUBST($UIBME:1:6)='DELREC'
     C                   IF        $UIBK2='' OR $UIBK3='' OR $UIBK4=''
     C                   GOTO      G9MAIN
     C                   ENDIF
      *
     C                   WHEN      %SUBST($UIBME:1:6)='MATCOL'
     C                   IF        $UIBK2=''
     C                   GOTO      G9MAIN
     C                   ENDIF
     C                   WHEN      %SUBST($UIBME:1:6)='ACCCOL'
     C                   IF        $UIBK2=''
     C                   GOTO      G9MAIN
     C                   ENDIF
     C                   WHEN      %SUBST($UIBME:1:6)='LSTSED'
     C                   IF        $UIBK2=''
     C                   GOTO      G9MAIN
     C                   ENDIF
     C                   WHEN      %SUBST($UIBME:1:6)='YRSSED'
     C                   IF        $UIBK2=''
     C                   GOTO      G9MAIN
     C                   ENDIF
     C                   WHEN      %SUBST($UIBME:1:6)='TREBTN'
     C                   IF        $UIBK2='' OR $UIBK3='' OR $UIBK4=''
     C                   GOTO      G9MAIN
     C                   ENDIF
     C                   ENDSL
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD*  Comunico Dati in matrice e proprio Setup
      *--------------------------------------------------------------*
     C     MATPRV        BEGSR
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
      * Select per MESE
     C                   SELECT
     C     $UIBK3        WHENEQ    'GEN'
     C                   EVAL      $$MESE=20200101
     C                   EVAL      $$GG=31
     C     $UIBK3        WHENEQ    'FEB'
     C                   EVAL      $$MESE=20200201
     C                   EVAL      $$GG=28
     C     $UIBK3        WHENEQ    'MAR'
     C                   EVAL      $$MESE=20200301
     C                   EVAL      $$GG=31
     C     $UIBK3        WHENEQ    'APR'
     C                   EVAL      $$MESE=20200401
     C                   EVAL      $$GG=30
     C     $UIBK3        WHENEQ    'MAG'
     C                   EVAL      $$MESE=20200501
     C                   EVAL      $$GG=31
     C     $UIBK3        WHENEQ    'GIU'
     C                   EVAL      $$MESE=20200601
     C                   EVAL      $$GG=30
     C     $UIBK3        WHENEQ    'LUG'
     C                   EVAL      $$MESE=20200701
     C                   EVAL      $$GG=31
     C     $UIBK3        WHENEQ    'AGO'
     C                   EVAL      $$MESE=20200801
     C                   EVAL      $$GG=31
     C     $UIBK3        WHENEQ    'SET'
     C                   EVAL      $$MESE=20200901
     C                   EVAL      $$GG=30
     C     $UIBK3        WHENEQ    'OTT'
     C                   EVAL      $$MESE=20201001
     C                   EVAL      $$GG=31
     C     $UIBK3        WHENEQ    'NOV'
     C                   EVAL      $$MESE=20201101
     C                   EVAL      $$GG=30
     C     $UIBK3        WHENEQ    'DIC'
     C                   EVAL      $$MESE=20201201
     C                   EVAL      $$GG=31
     C                   ENDSL
      *
2x   C                   DO        $$GG
      * settaggio variabili
     C                   EVAL      $$COMM=''
     C                   EVAL      $PRNT=0
     C                   EVAL      $DSPN=0
     C                   CLEAR                   CODCOL
     C                   EVAL      $N=1
     C                   EVAL      $$USER='0'
      *
      * Valorizzazione campi £IXA
     C                   EVAL      V£DATA=$$MESE
     C                   EVAL      V£CDC=$$COMM
      * Log £IXA - Start time
     C                   TIME                    $TIMSTIXA
      * Posizionamento £IXA
     C                   EVAL      £IXAFU='SETLL'
     C                   EVAL      £IXAME='9L'
     C                   EVAL      £IXANK='2'
      *
     C                   CALL      '£IXA'
     C                   PARM                    £IXAFU
     C                   PARM                    £IXAME
     C                   PARM                    £IXANK
     C                   PARM                    £IXACO
     C                   PARM                    £IXA35
     C                   PARM                    £IXACOD
     C                   PARM                    VERAPGR
      * Log IXA - End time
     C                   TIME                    $TIMENIXA
      * Elapsed time
     C     $TIMENIXA     SUBDUR    $TIMSTIXA     $TIMMSIXA:*MS
     C                   EVAL      $TIMMSIXA=$TIMMSIXA/1000
      * Display message
     C                   EVAL      $MSG='£IXA'
     C                                +' - Metodo: '+%TRIM(%SUBST($UIBME:1:6))
     C                                +' - Funzione: '+%TRIM(£IXAFU)
     C                                +' - Tempo: '+%TRIM(%EDITC($TIMMSIXA:'Q'))
     C                                +'ms'
     C     $MSG          DSPLY     £PDSSU
      *
3x   C                   DO        *HIVAL
      * Valorizzazione campi £IXA
     C                   EVAL      V£DATA=$$MESE
      * Log £IXA - Start time
     C                   TIME                    $TIMSTIXA
      * Lettura £IXA
     C                   EVAL      £IXAFU='READE'
     C                   EVAL      £IXAME='9L'
     C                   EVAL      £IXANK='2'
      *
     C                   CALL      '£IXA'
     C                   PARM                    £IXAFU
     C                   PARM                    £IXAME
     C                   PARM                    £IXANK
     C                   PARM                    £IXACO
     C                   PARM                    £IXA35
     C                   PARM                    £IXACOD
     C                   PARM                    VERAPGR
      * Log IXA - End time
     C                   TIME                    $TIMENIXA
      * Elapsed time
     C     $TIMENIXA     SUBDUR    $TIMSTIXA     $TIMMSIXA:*MS
     C                   EVAL      $TIMMSIXA=$TIMMSIXA/1000
      * Display message
     C                   EVAL      $MSG='£IXA'
     C                                +' - Metodo: '+%TRIM(%SUBST($UIBME:1:6))
     C                                +' - Funzione: '+%TRIM(£IXAFU)
     C                                +' - Tempo: '+%TRIM(%EDITC($TIMMSIXA:'Q'))
     C                                +'ms'
     C     $MSG          DSPLY     £PDSSU
      * Check indicatore
     C                   IF        £IXA35='1'
     C                   LEAVE
     C                   ENDIF
      * Fine del file - esce
4x   C                   IF        £IXACOD='100'
     C                   LEAVE
4e   C                   ENDIF
      *Condizione sulla SEDE
     C                   IF        %TRIM(V£COD1)<>%TRIM($UIBK2)
     C                   ITER
     C                   ENDIF
      *Condizione su utente
     C                   IF        V£NOME=%TRIM($UIBK4)
     C                   EVAL      $$USER='1'
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
      * Controllo posti in ATTESA
     C                   IF        V£ATV0='2'
     C                   EVAL      $PRNT=$PRNT+1
     C                   ENDIF
      *
3e   C                   ENDDO
      * Aumenti contatori
     C                   EVAL      $DSPN=$POSTI-$PRNT
      * Conversione Data
     C*                  EVAL      £DECTP='D8'
     C*                  EVAL      £DECPA='*YYMD'
     C*                  EVAL      £DECCD=%EDITC($$MESE:'X')
     C*                  EXSR      £DEC
      *Controllo indicatore 35
     C*                  IF        £DEC35='1'
     C*                  LEAVE
     C*                  ENDIF
      *Estrazione Giorno
     C*                  EVAL      $NDAT=%SCAN('/':£DECDE)-1
     C*                  EVAL      §DATEST=%SUBST(£DECDE:1:$NDAT)
      * Scrittura riga
     C                   EVAL      $$XMLPAYLOAD=%TRIM($$XMLPAYLOAD)
     C                              +'<Riga Fld="'+%EDITC($$MESE:'Z')
     C                                   +'|'+'Posti Sede:'+%EDITC($DSPN:'Z')   COSTANTE
     C*                                  +'|'+§DATEST
     C                                   +'|'+%EDITC($$MESE:'Z')
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
    RD*  Ricerco record in DB e restituisco singola riga Matrice
      *--------------------------------------------------------------*
     C     READRW        BEGSR
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
     C                             +'</Griglia>'
      * Aggiungi righe - inizializzazione
     C                   EVAL      $$XMLPAYLOAD=%TRIM($$XMLPAYLOAD)
     C                              +'<Righe>'
      * settaggio variabili
     C                   EVAL      $$COMM=''
     C                   EVAL      $$DATA=%INT($UIBK2)
     C                   EVAL      $PRNT=0
     C                   EVAL      $DSPN=0
     C                   CLEAR                   CODCOL
     C                   EVAL      $N=1
      * Ciclo lettura
      * Log £IXA - Start time
     C                   TIME                    $TIMSTIXA
      * Valorizzazione campi £IXA
     C                   EVAL      V£DATA=$$DATA
     C                   EVAL      V£CDC=$$COMM
      * Posizionamento £IXA
     C                   EVAL      £IXAFU='SETLL'
     C                   EVAL      £IXAME='9L'
     C                   EVAL      £IXANK='2'
      *
     C                   CALL      '£IXA'
     C                   PARM                    £IXAFU
     C                   PARM                    £IXAME
     C                   PARM                    £IXANK
     C                   PARM                    £IXACO
     C                   PARM                    £IXA35
     C                   PARM                    £IXACOD
     C                   PARM                    VERAPGR
      * Log IXA - End time
     C                   TIME                    $TIMENIXA
      * Elapsed time
     C     $TIMENIXA     SUBDUR    $TIMSTIXA     $TIMMSIXA:*MS
     C                   EVAL      $TIMMSIXA=$TIMMSIXA/1000
      * Display message
     C                   EVAL      $MSG='£IXA'
     C                                +' - Metodo: '+%TRIM(%SUBST($UIBME:1:6))
     C                                +' - Funzione: '+%TRIM(£IXAFU)
     C                                +' - Tempo: '+%TRIM(%EDITC($TIMMSIXA:'Q'))
     C                                +'ms'
     C     $MSG          DSPLY     £PDSSU
      *
3x   C                   DO        *HIVAL
      * Log £IXA - Start time
     C                   TIME                    $TIMSTIXA
      * Lettura £IXA
     C                   EVAL      £IXAFU='READE'
     C                   EVAL      £IXAME='9L'
     C                   EVAL      £IXANK='2'
      *
     C                   CALL      '£IXA'
     C                   PARM                    £IXAFU
     C                   PARM                    £IXAME
     C                   PARM                    £IXANK
     C                   PARM                    £IXACO
     C                   PARM                    £IXA35
     C                   PARM                    £IXACOD
     C                   PARM                    VERAPGR
      * Log IXA - End time
     C                   TIME                    $TIMENIXA
      * Elapsed time
     C     $TIMENIXA     SUBDUR    $TIMSTIXA     $TIMMSIXA:*MS
     C                   EVAL      $TIMMSIXA=$TIMMSIXA/1000
      * Display message
     C                   EVAL      $MSG='£IXA'
     C                                +' - Metodo: '+%TRIM(%SUBST($UIBME:1:6))
     C                                +' - Funzione: '+%TRIM(£IXAFU)
     C                                +' - Tempo: '+%TRIM(%EDITC($TIMMSIXA:'Q'))
     C                                +'ms'
     C     $MSG          DSPLY     £PDSSU
      * Check indicatore
     C                   IF        £IXA35='1'
     C                   LEAVE
     C                   ENDIF
      * Fine del file - esce
4x   C                   IF        £IXACOD='100'
     C                   LEAVE
4e   C                   ENDIF
      *Condizione sulla SEDE
     C                   IF        %TRIM(V£COD1)<>%TRIM($UIBK3)
     C                   ITER
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
      * Controllo posti in ATTESA
     C                   IF        V£ATV0='2'
     C                   EVAL      $PRNT=$PRNT+1
     C                   ENDIF

      *
3e   C                   ENDDO
      * Aumenti contatori
     C                   EVAL      $DSPN=$POSTI-$PRNT
      *Conversione Data
     C*                  EVAL      £DECTP='D8'
     C*                  EVAL      £DECPA='*YYMD'
     C*                  EVAL      £DECCD=%EDITC($$DATA:'X')
     C*                  EXSR      £DEC
      * Scrittura riga
     C                   EVAL      $$XMLPAYLOAD=%TRIM($$XMLPAYLOAD)
     C                              +'<Riga Fld="'+%EDITC($$DATA:'Z')
     C                                   +'|'+'Posti Sede:'+%EDITC($DSPN:'Z')   COSTANTE
     C*                                  +'|'+£DECDE
     C                                   +'|'+%EDITC($$DATA:'Z')
     C                                   +'|'+$UIBK3
     C                                   +'|'+%EDITC($POSTI:'Z')
     C                                   +'|'+%EDITC($DSPN:'Z')
     C                                   +'|'+%EDITC($PRNT:'Z')
     C                                   +'"/>'
      * Aggiungi righe - finalizzazione
     C                   EVAL      $$XMLPAYLOAD=%TRIM($$XMLPAYLOAD)
     C                              +'</Righe>'
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD*  Dati per inserire Record in DB
      *--------------------------------------------------------------*
     C     WRIADD        BEGSR
      *
      * settaggio variabili
     C                   EVAL      $$DATA=%INT($UIBK3)
     C                   EVAL      $$NOME=%TRIM($UIBK2)
     C                   EVAL      $UTER=0
      * Ciclo lettura
      * Valorizzazione campi £IXA
     C                   EVAL      V£DATA=$$DATA
     C                   EVAL      V£NOME=$$NOME
      * Log £IXA - Start time
     C                   TIME                    $TIMSTIXA
      * Posizionamento £IXA
     C                   EVAL      £IXAFU='SETLL'
     C                   EVAL      £IXAME='1L'
     C                   EVAL      £IXANK='2'
      *
     C                   CALL      '£IXA'
     C                   PARM                    £IXAFU
     C                   PARM                    £IXAME
     C                   PARM                    £IXANK
     C                   PARM                    £IXACO
     C                   PARM                    £IXA35
     C                   PARM                    £IXACOD
     C                   PARM                    VERAPGR
      * Log IXA - End time
     C                   TIME                    $TIMENIXA
      * Elapsed time
     C     $TIMENIXA     SUBDUR    $TIMSTIXA     $TIMMSIXA:*MS
     C                   EVAL      $TIMMSIXA=$TIMMSIXA/1000
      * Display message
     C                   EVAL      $MSG='£IXA'
     C                                +' - Metodo: '+%TRIM(%SUBST($UIBME:1:6))
     C                                +' - Funzione: '+%TRIM(£IXAFU)
     C                                +' - Tempo: '+%TRIM(%EDITC($TIMMSIXA:'Q'))
     C                                +'ms'
     C     $MSG          DSPLY     £PDSSU
      *
3x   C                   DO        *HIVAL
      * Log £IXA - Start time
     C                   TIME                    $TIMSTIXA
      * Lettura £IXA
     C                   EVAL      £IXAFU='READE'
     C                   EVAL      £IXAME='1L'
     C                   EVAL      £IXANK='2'
      *
     C                   CALL      '£IXA'
     C                   PARM                    £IXAFU
     C                   PARM                    £IXAME
     C                   PARM                    £IXANK
     C                   PARM                    £IXACO
     C                   PARM                    £IXA35
     C                   PARM                    £IXACOD
     C                   PARM                    VERAPGR
      * Log IXA - End time
     C                   TIME                    $TIMENIXA
      * Elapsed time
     C     $TIMENIXA     SUBDUR    $TIMSTIXA     $TIMMSIXA:*MS
     C                   EVAL      $TIMMSIXA=$TIMMSIXA/1000
      * Display message
     C                   EVAL      $MSG='£IXA'
     C                                +' - Metodo: '+%TRIM(%SUBST($UIBME:1:6))
     C                                +' - Funzione: '+%TRIM(£IXAFU)
     C                                +' - Tempo: '+%TRIM(%EDITC($TIMMSIXA:'Q'))
     C                                +'ms'
     C     $MSG          DSPLY     £PDSSU
      * Check indicatore
     C                   IF        £IXA35='1'
     C                   LEAVE
     C                   ENDIF
      *Fine del file - esce
4x   C                   IF        £IXACOD='100'
     C                   LEAVE
4e   C                   ENDIF
      * Controllo sulla sede
     C                   IF        V£COD1=%TRIM($UIBK4)
     C                   IF        %TRIM(V£CDC)=''
     C                   EVAL      $UTER=$UTER+1
     C                   ENDIF
     C                   ENDIF
      *
2e   C                   ENDDO
     C                   IF        $UTER=0
      * Inserisco riga prenotazione
     C                   DO        10
     C                   EXSR      £XAIDOJ
     C                   EVAL      V£ATV0='2'
     C                   EVAL      V£DATA=%INT($UIBK3)
     C                   EVAL      V£NOME=$UIBK2
     C                   EVAL      V£CDC=''
     C                   EVAL      V£COD1=$UIBK4
     C                   EVAL      V£COM2='$$EXT Prenotazione Sede EXT$$'       COSTANTE
      * Log £IXA - Start time
     C                   TIME                    $TIMSTIXA
      * Scrittura £IXA
     C                   EVAL      £IXAFU='WRI'
     C                   EVAL      £IXAME=''
     C                   EVAL      £IXANK=''
      *
     C                   CALL      '£IXA'
     C                   PARM                    £IXAFU
     C                   PARM                    £IXAME
     C                   PARM                    £IXANK
     C                   PARM                    £IXACO
     C                   PARM                    £IXA35
     C                   PARM                    £IXACOD
     C                   PARM                    VERAPGR
      * Log IXA - End time
     C                   TIME                    $TIMENIXA
      * Elapsed time
     C     $TIMENIXA     SUBDUR    $TIMSTIXA     $TIMMSIXA:*MS
     C                   EVAL      $TIMMSIXA=$TIMMSIXA/1000
      * Display message
     C                   EVAL      $MSG='£IXA'
     C                                +' - Metodo: '+%TRIM(%SUBST($UIBME:1:6))
     C                                +' - Funzione: '+%TRIM(£IXAFU)
     C                                +' - Tempo: '+%TRIM(%EDITC($TIMMSIXA:'Q'))
     C                                +'ms'
     C     $MSG          DSPLY     £PDSSU
      * Check indicatore
     C                   IF        £IXA35='1'
     C                   LEAVE
     C                   ENDIF
      *Se la scrittura va a buon fine esco, altrimenti ciclo
     C                   IF        £IXACOD='0'
     C                   LEAVE
     C                   ENDIF
      *
     C                   ENDDO
     C                   ENDIF
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD*  Eliminazione record nel DB
      *--------------------------------------------------------------*
     C     DELREC        BEGSR
      *
      * settaggio variabili
     C                   EVAL      $$DATA=%INT($UIBK3)
     C                   EVAL      $$NOME=%TRIM($UIBK2)
      * Ciclo lettura
      * Log £IXA - Start time
     C                   TIME                    $TIMSTIXA
      * Valorizzazione campi £IXA
     C                   EVAL      V£DATA=$$DATA
     C                   EVAL      V£NOME=$$NOME
      * Posizionamento £IXA
     C                   EVAL      £IXAFU='SETLL'
     C                   EVAL      £IXAME='1L'
     C                   EVAL      £IXANK='2'
      *
     C                   CALL      '£IXA'
     C                   PARM                    £IXAFU
     C                   PARM                    £IXAME
     C                   PARM                    £IXANK
     C                   PARM                    £IXACO
     C                   PARM                    £IXA35
     C                   PARM                    £IXACOD
     C                   PARM                    VERAPGR
      * Log IXA - End time
     C                   TIME                    $TIMENIXA
      * Elapsed time
     C     $TIMENIXA     SUBDUR    $TIMSTIXA     $TIMMSIXA:*MS
     C                   EVAL      $TIMMSIXA=$TIMMSIXA/1000
      * Display message
     C                   EVAL      $MSG='£IXA'
     C                                +' - Metodo: '+%TRIM(%SUBST($UIBME:1:6))
     C                                +' - Funzione: '+%TRIM(£IXAFU)
     C                                +' - Tempo: '+%TRIM(%EDITC($TIMMSIXA:'Q'))
     C                                +'ms'
     C     $MSG          DSPLY     £PDSSU
      *
3x   C                   DO        *HIVAL
      * Log £IXA - Start time
     C                   TIME                    $TIMSTIXA
      * Lettura £IXA
     C                   EVAL      £IXAFU='READE'
     C                   EVAL      £IXAME='1L'
     C                   EVAL      £IXANK='2'
      *
     C                   CALL      '£IXA'
     C                   PARM                    £IXAFU
     C                   PARM                    £IXAME
     C                   PARM                    £IXANK
     C                   PARM                    £IXACO
     C                   PARM                    £IXA35
     C                   PARM                    £IXACOD
     C                   PARM                    VERAPGR
      * Log IXA - End time
     C                   TIME                    $TIMENIXA
      * Elapsed time
     C     $TIMENIXA     SUBDUR    $TIMSTIXA     $TIMMSIXA:*MS
     C                   EVAL      $TIMMSIXA=$TIMMSIXA/1000
      * Display message
     C                   EVAL      $MSG='£IXA'
     C                                +' - Metodo: '+%TRIM(%SUBST($UIBME:1:6))
     C                                +' - Funzione: '+%TRIM(£IXAFU)
     C                                +' - Tempo: '+%TRIM(%EDITC($TIMMSIXA:'Q'))
     C                                +'ms'
     C     $MSG          DSPLY     £PDSSU
      * Check indicatore
     C                   IF        £IXA35='1'
     C                   LEAVE
     C                   ENDIF
      * Fine del file - esce
4x   C                   IF        £IXACOD='100'
     C                   LEAVE
4e   C                   ENDIF
      * Controllo sulla sede
     C                   IF        %TRIM(V£COD1)<>%TRIM($UIBK4)
     C                   ITER
     C                   ENDIF
      *Controllo sulla commessa
     C                   IF        V£CDC <>''
     C                   ITER
     C                   ENDIF
      * Log £IXA - Start time
     C                   TIME                    $TIMSTIXA
      * Chain £IXA
     C                   EVAL      £IXAFU='CHA'
     C                   EVAL      £IXAME='0L'
     C                   EVAL      £IXANK=''
      *
     C                   CALL      '£IXA'
     C                   PARM                    £IXAFU
     C                   PARM                    £IXAME
     C                   PARM                    £IXANK
     C                   PARM                    £IXACO
     C                   PARM                    £IXA35
     C                   PARM                    £IXACOD
     C                   PARM                    VERAPGR
      * Log IXA - End time
     C                   TIME                    $TIMENIXA
      * Elapsed time
     C     $TIMENIXA     SUBDUR    $TIMSTIXA     $TIMMSIXA:*MS
     C                   EVAL      $TIMMSIXA=$TIMMSIXA/1000
      * Display message
     C                   EVAL      $MSG='£IXA'
     C                                +' - Metodo: '+%TRIM(%SUBST($UIBME:1:6))
     C                                +' - Funzione: '+%TRIM(£IXAFU)
     C                                +' - Tempo: '+%TRIM(%EDITC($TIMMSIXA:'Q'))
     C                                +'ms'
     C     $MSG          DSPLY     £PDSSU
      * Check indicatore
     C                   IF        £IXA35='1'
     C                   LEAVE
     C                   ENDIF
      *
     C                   IF        £IXACOD='1'
      * Elimino riga prenotazione
      * Log £IXA - Start time
     C                   TIME                    $TIMSTIXA
      * Delete £IXA
     C                   EVAL      £IXAFU='DEL'
     C                   EVAL      £IXAME=''
     C                   EVAL      £IXANK=''
      *
     C                   CALL      '£IXA'
     C                   PARM                    £IXAFU
     C                   PARM                    £IXAME
     C                   PARM                    £IXANK
     C                   PARM                    £IXACO
     C                   PARM                    £IXA35
     C                   PARM                    £IXACOD
     C                   PARM                    VERAPGR
      * Log IXA - End time
     C                   TIME                    $TIMENIXA
      * Elapsed time
     C     $TIMENIXA     SUBDUR    $TIMSTIXA     $TIMMSIXA:*MS
     C                   EVAL      $TIMMSIXA=$TIMMSIXA/1000
      * Display message
     C                   EVAL      $MSG='£IXA'
     C                                +' - Metodo: '+%TRIM(%SUBST($UIBME:1:6))
     C                                +' - Funzione: '+%TRIM(£IXAFU)
     C                                +' - Tempo: '+%TRIM(%EDITC($TIMMSIXA:'Q'))
     C                                +'ms'
     C     $MSG          DSPLY     £PDSSU
      * Check indicatore
     C                   IF        £IXA35='1'
     C                   LEAVE
     C                   ENDIF
      *
     C                   IF        £IXACOD='1'
     C                   LEAVE
     C                   ENDIF
      *
     C                   ENDIF
      *
2e   C                   ENDDO
     C                   ENDSR
      *--------------------------------------------------------------*
    RD*  Dati in matrice - Elenco Collaboratori
      *--------------------------------------------------------------*
     C     MATCOL        BEGSR
      * Aggiungi griglia
     C                   EVAL      $$XMLPAYLOAD='<Griglia><Colonna Cod="XXICOL"'
     C                             +' Txt="Immagine Collaboratore" Lun="15"'
     C                             +' IO="O" Ogg="J4IMG" Fill="ASSE"/>'
     C                             +'<Colonna Cod="XXNCOL" Txt="Collaboratore"'
     C                             +' Lun="15" IO="O" Ogg="CNCOL" Fill="ASSE"/>'
     C                             +'<Colonna Cod="XXTPRN"'
     C                             +' Txt="Tipo Prenotazione"'
     C                             +' Lun="15" IO="O" Fill="  SERIE"/>'
     C                             +'</Griglia>'
      * Aggiungi righe - inizializzazione
     C                   EVAL      $$XMLPAYLOAD=%TRIM($$XMLPAYLOAD)
     C                              +'<Righe>'
      * settaggio variabili
     C                   EVAL      $$COMM=''
     C                   EVAL      $$DATA=%INT($UIBK2)
     C                   EVAL      $$SEDE=%TRIM($UIBK3)
     C                   CLEAR                   CODCOL
     C                   EVAL      $N=1
      * Ciclo lettura
      * Log £IXA - Start time
     C                   TIME                    $TIMSTIXA
      * Valorizzazione campi £IXA
     C                   EVAL      V£DATA=$$DATA
     C                   EVAL      V£CDC=$$COMM
      * Posizionamento £IXA
     C                   EVAL      £IXAFU='SETLL'
     C                   EVAL      £IXAME='9L'
     C                   EVAL      £IXANK='2'
      *
     C                   CALL      '£IXA'
     C                   PARM                    £IXAFU
     C                   PARM                    £IXAME
     C                   PARM                    £IXANK
     C                   PARM                    £IXACO
     C                   PARM                    £IXA35
     C                   PARM                    £IXACOD
     C                   PARM                    VERAPGR
      * Log IXA - End time
     C                   TIME                    $TIMENIXA
      * Elapsed time
     C     $TIMENIXA     SUBDUR    $TIMSTIXA     $TIMMSIXA:*MS
     C                   EVAL      $TIMMSIXA=$TIMMSIXA/1000
      * Display message
     C                   EVAL      $MSG='£IXA'
     C                                +' - Metodo: '+%TRIM(%SUBST($UIBME:1:6))
     C                                +' - Funzione: '+%TRIM(£IXAFU)
     C                                +' - Tempo: '+%TRIM(%EDITC($TIMMSIXA:'Q'))
     C                                +'ms'
     C     $MSG          DSPLY     £PDSSU
      *
3x   C                   DO        *HIVAL
      * Log £IXA - Start time
     C                   TIME                    $TIMSTIXA
      * Lettura £IXA
     C                   EVAL      £IXAFU='READE'
     C                   EVAL      £IXAME='9L'
     C                   EVAL      £IXANK='2'
      *
     C                   CALL      '£IXA'
     C                   PARM                    £IXAFU
     C                   PARM                    £IXAME
     C                   PARM                    £IXANK
     C                   PARM                    £IXACO
     C                   PARM                    £IXA35
     C                   PARM                    £IXACOD
     C                   PARM                    VERAPGR
      * Log IXA - End time
     C                   TIME                    $TIMENIXA
      * Elapsed time
     C     $TIMENIXA     SUBDUR    $TIMSTIXA     $TIMMSIXA:*MS
     C                   EVAL      $TIMMSIXA=$TIMMSIXA/1000
      * Display message
     C                   EVAL      $MSG='£IXA'
     C                                +' - Metodo: '+%TRIM(%SUBST($UIBME:1:6))
     C                                +' - Funzione: '+%TRIM(£IXAFU)
     C                                +' - Tempo: '+%TRIM(%EDITC($TIMMSIXA:'Q'))
     C                                +'ms'
     C     $MSG          DSPLY     £PDSSU
      * Check indicatore
     C                   IF        £IXA35='1'
     C                   LEAVE
     C                   ENDIF
      * Fine del file - esce
4x   C                   IF        £IXACOD='100'
     C                   LEAVE
4e   C                   ENDIF
      *Condizione sulla SEDE
     C                   IF        %TRIM(V£COD1)<>%TRIM($UIBK3)
     C                   ITER
     C                   ENDIF
      *Pulizia Variabile Nome
     C                   EVAL      $$NOME=''
      *Controllo presenza collaboratore
     C                   EVAL      $A=%LOOKUP(V£NOME:CODCOL:1:$N)
     C                   IF        $A<>0
     C                   ITER
     C                   ELSE
     C                   EVAL      CODCOL($N)=V£NOME
     C                   EVAL      $$NOME=V£NOME
     C                   EVAL      $N=$N+1
     C                   ENDIF
      *
      * Controllo posti in ATTESA
     C                   IF        V£ATV0='2'
     C                   EVAL      $$TPPT='Prenotato'                           COSTANTE
     C                   ENDIF
      *
      * Scrittura riga
     C                   EVAL      $$XMLPAYLOAD=%TRIM($$XMLPAYLOAD)
     C                              +'<Riga Fld="CN;COL;'+%TRIM($$NOME)
     C                                   +'|'+%TRIM($$NOME)
     C                                   +'|'+%TRIM($$TPPT)
     C                                   +'"/>'
3e   C                   ENDDO
      * Aggiungi righe - finalizzazione
     C                   EVAL      $$XMLPAYLOAD=%TRIM($$XMLPAYLOAD)
     C                              +'</Righe>'
      *
     C                   ENDSR
      *--------------------------------------------------------------*
     D*  Albero - Lista Sedi (codice e descrizione)
      *--------------------------------------------------------------*
     C     LSTSED        BEGSR
      *
     C                   EVAL      $NSED=0
     C                   DO        14
     C                   EVAL      $NSED=$NSED+1
      *
     C                   IF        TRESED($NSED)=''
     C                   LEAVE
     C                   ENDIF
      *
     C                   IF        %SUBST(TRESED($NSED):1:10)='ERB       '
     C                   ITER
     C                   ENDIF

      *
     C                   EVAL      $$XMLPAYLOAD=%TRIM($$XMLPAYLOAD)
     C                             +'<Oggetto Tipo="CN"'
     C                             +' Parametro="SED" Codice="'
     C                             +%SUBST(TRESED($NSED):1:10)+'"'
     C                             +' Testo=" '
     C                             +%SUBST(TRESED($NSED):11)+'"'
     C                             +' />'
     C                   ENDDO
      *
      *
     C                   ENDSR
      *--------------------------------------------------------------*
     D*  Albero - Sede Appartenenza
      *--------------------------------------------------------------*
     C     YRSSED        BEGSR
      * Sede Appartenenza
     C                   EVAL      $$XMLPAYLOAD=%TRIM($$XMLPAYLOAD)
     C                             +'<Oggetto Tipo="CN"'
     C                             +' Parametro="SED" Codice="'
     C*                            +%TRIM(£OAVOV)+'"'
     C                             + 'ERB"'
     C                             +' Testo="'
     C*                            +%TRIM(£OAVSI)+' Ultimo accesso: '
     C                             + 'Erbusco - Ultimo accesso: '
     C                             + '09/10/2020"'
     C                             +' />'
      *
     C                   ENDSR
      *--------------------------------------------------------------*
     D*  Albero - Button Conferma/Cancella
      *--------------------------------------------------------------*
     C     TREBTN        BEGSR
      *
      * Inizializazzione Variabili
     C                   EVAL      §DESC= 'Conferma Prenotazione'
     C                   EVAL      §STRFUN='F(FBK;X1_X21_03;WRIADD)'
     C                             + ' 2(;;' + %TRIM($UIBK1) + ')'
     C                             + ' 3(;;' + %TRIM($UIBK2) + ')'
     C                             + ' 4(;;' + %TRIM($UIBK3) + ')'
      *
      * Valorizzazione campi £IXA
     C                   EVAL      V£DATA=%INT($UIBK2)
     C                   EVAL      V£CDC=''
      * Log £IXA - Start time
     C                   TIME                    $TIMSTIXA
      * Posizionamento £IXA
     C                   EVAL      £IXAFU='SETLL'
     C                   EVAL      £IXAME='9L'
     C                   EVAL      £IXANK='2'
      *
     C                   CALL      '£IXA'
     C                   PARM                    £IXAFU
     C                   PARM                    £IXAME
     C                   PARM                    £IXANK
     C                   PARM                    £IXACO
     C                   PARM                    £IXA35
     C                   PARM                    £IXACOD
     C                   PARM                    VERAPGR
      * Log IXA - End time
     C                   TIME                    $TIMENIXA
      * Elapsed time
     C     $TIMENIXA     SUBDUR    $TIMSTIXA     $TIMMSIXA:*MS
     C                   EVAL      $TIMMSIXA=$TIMMSIXA/1000
      * Display message
     C                   EVAL      $MSG='£IXA'
     C                                +' - Metodo: '+%TRIM(%SUBST($UIBME:1:6))
     C                                +' - Funzione: '+%TRIM(£IXAFU)
     C                                +' - Tempo: '+%TRIM(%EDITC($TIMMSIXA:'Q'))
     C                                +'ms'
     C     $MSG          DSPLY     £PDSSU
      *
     C                   DO        *HIVAL
      * Log £IXA - Start time
     C                   TIME                    $TIMSTIXA
      * Lettura £IXA
     C                   EVAL      £IXAFU='READE'
     C                   EVAL      £IXAME='9L'
     C                   EVAL      £IXANK='2'
      *
     C                   CALL      '£IXA'
     C                   PARM                    £IXAFU
     C                   PARM                    £IXAME
     C                   PARM                    £IXANK
     C                   PARM                    £IXACO
     C                   PARM                    £IXA35
     C                   PARM                    £IXACOD
     C                   PARM                    VERAPGR
      * Log IXA - End time
     C                   TIME                    $TIMENIXA
      * Elapsed time
     C     $TIMENIXA     SUBDUR    $TIMSTIXA     $TIMMSIXA:*MS
     C                   EVAL      $TIMMSIXA=$TIMMSIXA/1000
      * Display message
     C                   EVAL      $MSG='£IXA'
     C                                +' - Metodo: '+%TRIM(%SUBST($UIBME:1:6))
     C                                +' - Funzione: '+%TRIM(£IXAFU)
     C                                +' - Tempo: '+%TRIM(%EDITC($TIMMSIXA:'Q'))
     C                                +'ms'
     C     $MSG          DSPLY     £PDSSU
      * Check indicatore
     C                   IF        £IXA35='1'
     C                   LEAVE
     C                   ENDIF
      * Fine del file - esce
     C                   IF        £IXACOD='100'
     C                   LEAVE
     C                   ENDIF
      *Condizione sulla SEDE
     C                   IF        V£COD1<>$UIBK3
     C                   ITER
     C                   ENDIF
      * Se trova il record dell'utente
     C                   IF        V£NOME=%TRIM($UIBK1)
     C                   IF        V£ATV0='2'
     C                   EVAL      §DESC= 'Cancella Prenotazione'
     C                   EVAL      §STRFUN='F(FBK;X1_X21_03;DELREC)'
     C                             + ' 2(;;' + %TRIM($UIBK1) + ')'
     C                             + ' 3(;;' + %TRIM($UIBK2) + ')'
     C                             + ' 4(;;' + %TRIM($UIBK3) + ')'
     C                   ENDIF
     C                   ENDIF
      *
     C                   ENDDO
      *
     C*                   EVAL      $$XMLPAYLOAD=%TRIM($$XMLPAYLOAD)
     C*                             +'<Oggetto Codice="'+%TRIM(§STRFUN)+'" '
     C*                             +'Testo="' +%TRIM(§DESC)+ '" '
     C* D                           +'Exec="' + %TRIM(§STRFUN)+ '" '
     C*                             +' />'
      *
     C                   EVAL      $$XMLPAYLOAD=%TRIM($$XMLPAYLOAD)             COSTANTE
     C                             +'<Oggetto Codice="'+%TRIM(§STRFUN)+'" '
     C                             +'Testo="' +%TRIM(§DESC)+ '"/> '
     C                             +'<Oggetto Codice="F(EXD;*SCO;)'
     C                             +' 2(MB;SCP_SCH;X1_X21_04) 4(;;COL)'
     C                             +' P('
     C                             +' Sede('+%TRIM($UIBK4)+')'
     C                             +' Data('+%TRIM($UIBK2)+')'
     C                             +' DataEstesa('+%TRIM($UIBK5)+')'
     C                             +' CodSede('+%TRIM($UIBK3)+')'
     C                             +' CodMese('+%TRIM($UIBK6)+')"'
     C                             +' Testo="'+%TRIM('Elenco Collaboratori')
     C                             + '"/>'
     C                             +'<Oggetto Codice="F(EXD;*SCO;)'
     C                             +'2(MB;SCP_SCH;X1_X21_04) 4(;;CAL)'
     C                             +' P(Sede('+%TRIM($UIBK4)+')'
     C                             +' Data('+%TRIM($UIBK2)+')'
     C                             +' CodSede('+%TRIM($UIBK3)+')'
     C                             +' CodMese('+%TRIM($UIBK6)+')"'
     C                             +' Testo="' +%TRIM('Annulla')
     C                             +'"/>'
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD*  Riga matrice - Dati Collaboratore
      *--------------------------------------------------------------*
     C     ACCCOL        BEGSR
      * Aggiungi griglia
     C                   EVAL      $$XMLPAYLOAD='<Griglia><Colonna Cod='
     C                             +'"XXIUTE"  Txt="Immagine Collaboratore"'
     C                             +' Lun="15" IO="O" Ogg="J4IMG"></Colonna>'
     C                             +'<Colonna Cod="XXNUTE" Txt="Collaboratore"'
     C                             +' Lun="15" IO="O" Ogg="CNCOL"></Colonna>'
     C                             +'<Colonna Cod="XXUACC" Txt="Ultimo Accesso"'
     C                             +' Lun="15" IO="O"></Colonna>'
     C                             +'<Colonna Cod="XXSAPP" Txt="Sede'
     C                             +' Appartenenza" Lun="15" IO="O"></Colonna>'
     C                             +'<Colonna Cod="XXSCAP" Txt="Codice Sede'
     C                             +' Appartenenza" Lun="15" IO="H"></Colonna>'
     C                             +'</Griglia>'
      * Aggiungi righe - inizializzazione
     C                   EVAL      $$XMLPAYLOAD=%TRIM($$XMLPAYLOAD)
     C                              +'<Righe>'
      * Stringa SQL
     C                   EVAL      §SQL='SELECT E§RAGS FROM brenti0f'
     C                              +' WHERE E§TRAG='+%TRIM(AP)+'COL'
     C                              +%TRIM(AP)+' AND'
     C                              +' E§CRAG='+%TRIM(AP)+%TRIM($UIBK2)
     C                              +%TRIM(AP)
     C     §SQL          DSPLY     £PDSSU
      * Apertura cursore £SQLD
     C                   EVAL      £SQLDString=%TRIM(§SQL)
     C                   EVAL      £SQLDTable='BRENTI0F'
     C                   EVAL      £SQLD35='0'
      *
     C*                  EVAL      £SQLDRows=0
     C                   EVAL      £SQLD_FUNFU='OPEN'
     C                   EVAL      £SQLD_FUNME='NAM'
      *
     C                   CALL      '£SQLD'
     C                   PARM                    £SQLD_FUNFU
     C                   PARM                    £SQLD_FUNME
     C                   PARM                    £SQLDString
     C                   PARM                    £SQLDTable
     C                   PARM                    £SQLD35
     C                   PARM                    £SQLDCOD
      * Lettura Record
      * Fetch record cursore
     C                   EVAL      £SQLD_FUNFU='NXTREC'
     C                   EVAL      £SQLD_FUNME='MAT'
      *
     C                   CALL      '£SQLD'
     C                   PARM                    £SQLD_FUNFU
     C                   PARM                    £SQLD_FUNME
     C                   PARM                    £SQLDString
     C                   PARM                    £SQLDTable
     C                   PARM                    £SQLD35
     C                   PARM                    £SQLDCOD
     C     £SQLDString   DSPLY     £PDSSU
      * test errore
     C                   IF        £SQLD35='1'  OR £SQLDCOD='100'
     C                   GOTO      G9ACCCOL
     C                   ENDIF
      *
     C     £SQLDString   DSPLY     £PDSSU
      * Chiusura Puntatore
     C                   EVAL      £SQLD_FUNFU='CLOSE'
     C                   EVAL      £SQLD_FUNME=*BLANKS
      *
     C                   CALL      '£SQLD'
     C                   PARM                    £SQLD_FUNFU
     C                   PARM                    £SQLD_FUNME
     C                   PARM                    £SQLDString
     C                   PARM                    £SQLDTable
     C                   PARM                    £SQLD35
     C                   PARM                    £SQLDCOD
      * Scrittura riga con JAXCP
     C                   EVAL      $$XMLPAYLOAD=%TRIM($$XMLPAYLOAD)
     C                             +'<Riga Fld="CN;COL;'+%TRIM($UIBK2)
     C                                  +'|'+%TRIM(£SQLDString)
     C                                  +'|'+%EDITC(20201009:'X')
     C                                  +'|'+%TRIM('Erbusco')
     C                                  +'|'+%TRIM('ERB')
     C                                  +'"/>'
      *
     C     G9ACCCOL      TAG
      * Aggiungi righe - finalizzazione
     C                   EVAL      $$XMLPAYLOAD=%TRIM($$XMLPAYLOAD)
     C                              +'</Righe>'
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD*  Dati per Gauge
      *--------------------------------------------------------------*
     C     GAU           BEGSR
      *Inizializzazione Variabili
     C                   EVAL      $MIN=0
     C                   EVAL      $MAX=100
      *
     C                   EVAL      $$XMLPAYLOAD=%TRIM($$XMLPAYLOAD)
     C                             +'<Elemento Valore="40"'
     C                                  +' Soglia1="60" Soglia2="80"'
     C                                  +' Min="'+%EDITC($MIN:'X')+'"'
     C                                  +' Max="'+%EDITC($MAX:'X')+'"'
     C                                  +'/>'
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD*  Copia /COPY £XAIDOJ
      *--------------------------------------------------------------*
     C     £XAIDOJ       BEGSR
      *
     C                   CLEAR                   V£IDOJ
     C                   CLEAR                   £CRN1
      * ... cerca in archivio
      * Log £IXA - Start time
     C                   TIME                    $TIMSTIXA
      * Posizionamento £IXA
     C                   EVAL      £IXAFU='SETGT'
     C                   EVAL      £IXAME=''
     C                   EVAL      £IXANK=''
      *
     C                   CALL      '£IXA'
     C                   PARM                    £IXAFU
     C                   PARM                    £IXAME
     C                   PARM                    £IXANK
     C                   PARM                    £IXACO
     C                   PARM                    £IXA35
     C                   PARM                    £IXACOD
     C                   PARM                    VERAPGR
      * Log IXA - End time
     C                   TIME                    $TIMENIXA
      * Elapsed time
     C     $TIMENIXA     SUBDUR    $TIMSTIXA     $TIMMSIXA:*MS
     C                   EVAL      $TIMMSIXA=$TIMMSIXA/1000
      * Display message
     C                   EVAL      $MSG='£IXA'
     C                                +' - Metodo: '+%TRIM(%SUBST($UIBME:1:6))
     C                                +' - Funzione: '+%TRIM(£IXAFU)
     C                                +' - Tempo: '+%TRIM(%EDITC($TIMMSIXA:'Q'))
     C                                +'ms'
     C     $MSG          DSPLY     £PDSSU
      *
      * Log £IXA - Start time
     C                   TIME                    $TIMSTIXA
      * Chiain £IXA
     C                   EVAL      £IXAFU='CHA'
     C                   EVAL      £IXAME=''
     C                   EVAL      £IXANK=''
      *
     C                   CALL      '£IXA'
     C                   PARM                    £IXAFU
     C                   PARM                    £IXAME
     C                   PARM                    £IXANK
     C                   PARM                    £IXACO
     C                   PARM                    £IXA35
     C                   PARM                    £IXACOD
     C                   PARM                    VERAPGR
      * Log IXA - End time
     C                   TIME                    $TIMENIXA
      * Elapsed time
     C     $TIMENIXA     SUBDUR    $TIMSTIXA     $TIMMSIXA:*MS
     C                   EVAL      $TIMMSIXA=$TIMMSIXA/1000
      * Display message
     C                   EVAL      $MSG='£IXA'
     C                                +' - Metodo: '+%TRIM(%SUBST($UIBME:1:6))
     C                                +' - Funzione: '+%TRIM(£IXAFU)
     C                                +' - Tempo: '+%TRIM(%EDITC($TIMMSIXA:'Q'))
     C                                +'ms'
     C     $MSG          DSPLY     £PDSSU
      *
     C                   IF        V£IDOJ<>''
     C                   EVAL      £CRN1=%INT(V£IDOJ)+1
     C                   ELSE
     C                   EVAL      £CRN1=1
     C                   ENDIF
      *
     C                   EVAL      V£IDOJ=%CHAR(£CRN1)
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD*  Chiusura e invio su coda del buffer residuo
      *--------------------------------------------------------------*
     C     FIN0          BEGSR
      * Aggiungo header e footer a payload
     C                   EVAL      $$XML=%TRIM($$XML)
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
      *
      * ENTRY
     C     *ENTRY        PLIST
     C***                PARM                    £UIBDS_MU
     C                   PARM                    $UIBPG
     C                   PARM                    $UIBFU
     C                   PARM                    $UIBME
     C                   PARM                    $UIBT1
     C                   PARM                    $UIBP1
     C                   PARM                    $UIBK1
     C                   PARM                    $UIBT2
     C                   PARM                    $UIBP2
     C                   PARM                    $UIBK2
     C                   PARM                    $UIBT3
     C                   PARM                    $UIBP3
     C                   PARM                    $UIBK3
     C                   PARM                    $UIBPA
     C                   PARM                    $UIBT4
     C                   PARM                    $UIBP4
     C                   PARM                    $UIBK4
     C                   PARM                    $UIBT5
     C                   PARM                    $UIBP5
     C                   PARM                    $UIBK5
     C                   PARM                    $UIBT6
     C                   PARM                    $UIBP6
     C                   PARM                    $UIBK6
     C                   PARM                    $UIBD1
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Log Specifico applicazione
      *--------------------------------------------------------------*
     C     £JAX_LOG      BEGSR
     C                   ENDSR
** CTDATA TRESED
BRE       Brescia
ERB       Erbusco
LEC       Lecco  Ultimo accesso: 01/10/2020
MOD       Modena
NOV       Nova Milanese
PAR       Parma
REG       Reggio Emilia  Ultimo accesso: 06/10/2020
TOR       Rivoli - Torino
ROM       Roma  Ultimo accesso: 19/09/2020
SAV       Savigliano
UDI       Udine - Sinte.sys
VER       Vercelli
VIF       Vigonza
VIL       Villaverla Vicenza  Ultimo accesso: 06/09/2020