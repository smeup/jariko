     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 10/12/20  002090  BUSFIO Creato nuovo servizio nativo
     V* 14/12/20  002090  BUSFIO Modificato metodo cancellazione
     V* 14/12/20  002090  BUSFIO Formattata data estesa
     V* ==============================================================
     V* NB: tutte le /COPY incluse (DEC, RITES, £TABJATDS...) servono
     V* ==============================================================
     FVERAPG0F  IF   E           K DISK
     FVERAPG0L  UF A E           K DISK    RENAME(VERAPGR:VERAPG0) PREFIX(V0:2)
     FVERAPG1L  IF   E           K DISK    RENAME(VERAPGR:VERAPG1) PREFIX(V1:2)
     FVERAPG9L  IF   E           K DISK    RENAME(VERAPGR:VERAPG9)
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
      *-Array - Lista Sedi-------------------------------------------------------------------------*
     D TRESED          S            100    DIM(14) CTDATA PERRCD(1)             _NOTXT
     D $NSED           S              2  0                                       Numeratore Array
      *--------------------------------------------------------------------------------------------*
     D §DATEST         S             30
     D $NDAT           S              2  0
      *-Button dinamico----------------------------------------------------------------------------*
     D §DESC           S             30
     D §STRFUN         S            100
      *-Variabili Gauge ---------------------------------------------------------------------------*
     D $VAL            S              3  0
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
      *-SQLD variabile +
--- - *--------------------------------------------------------------------------------------------*
     D §SQL            S           1000                                          Stringa SQL
     D AP              S              1     INZ('''')
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
      *-DS TEMPORANEA VERAPG0L PREFIX(V0:2)--------------------------------------------------------*
      *D VERAPG0         DS           823
      *D V0IDOJ                  1     10A
      *D V0ATV0                 11     11A
      *D V0DATA                 12     19S 0
      *D V0NOME                 20     34A
      *D V0CDC                  35     49A
      *D V0TIPO                 50     52A
      *D V0ORE                  53     55P 2
      *D V0VIAC                 56     58P 2
      *D V0VIAS                 59     61P 2
      *D V0KM                   62     64P 0
      *D V0KMSE                 65     67P 0
      *D V0PEDC                 68     73P 2
      *D V0PEDS                 74     79P 2
      *D V0RISC                 80     85P 2
      *D V0RISS                 86     91P 2
      *D V0ALBC                 92     97P 2
      *D V0ALBS                 98    103P 2
      *D V0SPEC                104    109P 2
      *D V0SPES                110    115P 2
      *D V0CAUS                116    118A
      *D V0TCOM                119    119A
      *D V0COM1                120    329A
      *D V0COM2                330    539A
      *D V0DIAR                540    542P 2
      *D V0DIUS                543    545P 2
      *D V0NFAT                546    555A
      *D V0DFAT                556    560P 0
      *D V0COD1                561    575A
      *D V0COD2                576    590A
      *D V0COD3                591    605A
      *D V0COD4                606    620A
      *D V0COD5                621    635A
      *D V0COD6                636    650A
      *D V0COD7                651    665A
      *D V0COD8                666    680A
      *D V0COD9                681    695A
      *D V0COD0                696    710A
      *D V0NUM1                711    721P 6
      *D V0NUM2                722    732P 6
      *D V0NUM3                733    743P 6
      *D V0NUM4                744    754P 6
      *D V0NUM5                755    765P 6
      *D V0FL01                766    766A
      *D V0FL02                767    767A
      *D V0FL03                768    768A
      *D V0FL04                769    769A
      *D V0FL05                770    770A
      *D V0FL06                771    771A
      *D V0FL07                772    772A
      *D V0FL08                773    773A
      *D V0FL09                774    774A
      *D V0FL10                775    775A
      *D V0FL11                776    776A
      *D V0FL12                777    777A
      *D V0FL13                778    778A
      *D V0FL14                779    779A
      *D V0FL15                780    780A
      *D V0FL16                781    781A
      *D V0FL17                782    782A
      *D V0FL18                783    783A
      *D V0FL19                784    784A
      *D V0FL20                785    785A
      *D V0USIN                786    795A
      *D V0DTIN                796    800P 0
      *D V0ORIN                801    804P 0
      *D V0USAG                805    814A
      *D V0DTAG                815    819P 0
      *D V0ORAG                820    823P 0
      *-DS TEMPORANEA VERAPG1L PREFIX(V1:2)--------------------------------------------------------*
      *D VERAPG1         DS           823
      *D V1IDOJ                  1     10A
      *D V1ATV0                 11     11A
      *D V1DATA                 12     19S 0
      *D V1NOME                 20     34A
      *D V1CDC                  35     49A
      *D V1TIPO                 50     52A
      *D V1ORE                  53     55P 2
      *D V1VIAC                 56     58P 2
      *D V1VIAS                 59     61P 2
      *D V1KM                   62     64P 0
      *D V1KMSE                 65     67P 0
      *D V1PEDC                 68     73P 2
      *D V1PEDS                 74     79P 2
      *D V1RISC                 80     85P 2
      *D V1RISS                 86     91P 2
      *D V1ALBC                 92     97P 2
      *D V1ALBS                 98    103P 2
      *D V1SPEC                104    109P 2
      *D V1SPES                110    115P 2
      *D V1CAUS                116    118A
      *D V1TCOM                119    119A
      *D V1COM1                120    329A
      *D V1COM2                330    539A
      *D V1DIAR                540    542P 2
      *D V1DIUS                543    545P 2
      *D V1NFAT                546    555A
      *D V1DFAT                556    560P 0
      *D V1COD1                561    575A
      *D V1COD2                576    590A
      *D V1COD3                591    605A
      *D V1COD4                606    620A
      *D V1COD5                621    635A
      *D V1COD6                636    650A
      *D V1COD7                651    665A
      *D V1COD8                666    680A
      *D V1COD9                681    695A
      *D V1COD0                696    710A
      *D V1NUM1                711    721P 6
      *D V1NUM2                722    732P 6
      *D V1NUM3                733    743P 6
      *D V1NUM4                744    754P 6
      *D V1NUM5                755    765P 6
      *D V1FL01                766    766A
      *D V1FL02                767    767A
      *D V1FL03                768    768A
      *D V1FL04                769    769A
      *D V1FL05                770    770A
      *D V1FL06                771    771A
      *D V1FL07                772    772A
      *D V1FL08                773    773A
      *D V1FL09                774    774A
      *D V1FL10                775    775A
      *D V1FL11                776    776A
      *D V1FL12                777    777A
      *D V1FL13                778    778A
      *D V1FL14                779    779A
      *D V1FL15                780    780A
      *D V1FL16                781    781A
      *D V1FL17                782    782A
      *D V1FL18                783    783A
      *D V1FL19                784    784A
      *D V1FL20                785    785A
      *D V1USIN                786    795A
      *D V1DTIN                796    800P 0
      *D V1ORIN                801    804P 0
      *D V1USAG                805    814A
      *D V1DTAG                815    819P 0
      *D V1ORAG                820    823P 0
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
     D* £PDSNU          S             10
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
      *
*-- - *---------------------------------------------------------------------------------------*
      * VARIABILI £DEC
      *--------------------------------------------------------------------------------------------*
     D £DECCD          S             15
     D £DECTP          S              2
     D £DECPA          S             10
     D £DECR1          S              1
     D £DECR2          S              1
     D £DECDE          S             35
     D £DECIN          S             15
     D £DEC35          S              1
     D £DEC36          S              1
     D £DECAM          S             10
     D £DECCO          S             10
     D £DECDT          S              8  0
      *
     D £DECDI          DS           512
     D £DECI_DELI                     1
     D £DECI_ROES                     1
      *
     D £DECDO          DS          2048
     D £DECO_TPAR                    12
     D £DECO_CPAR                    15
     D £DECO_DESC                   256
     D £DECO_LIVE                     1
     D £DECO_DTIN                     8
     D £DECO_ORIN                     6
     D £DECO_USIN                    10
     D £DECO_OGIN                    10
     D £DECO_DTAG                     8
     D £DECO_ORAG                     6
     D £DECO_USAG                    10
     D £DECO_OGAG                    10
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
      *-/COPY £SQLD--------------------------------------------------------------------------------*
     D £SQLD_FUNFU     S             50
     D £SQLD_FUNME     S             50
     D £SQLDString     S           5000
     D £SQLDTable      S            100
     D £SQLD35         S              1
     D £SQLDCOD        S              4
      *-/COPY £PDS---------------------------------------------------------------------------------*
     D  £PDSNU         S             50
     D  £PDSDE         S             50
     D  £PDSEN         S             50
     D  £PDSSE         S             50
     D  £UDSMG         S              8  0
      *-/COPY £OAV---------------------------------------------------------------------------------*
     D £OAVFU          S             10
     D £OAVME          S             10
     D £OAVT1          S              2
     D £OAVP1          S             10
     D £OAVC1          S             15
     D £OAVT2          S              2
     D £OAVP2          S             10
     D £OAVC2          S             15
     D £OAVAT          S             15
     D £OAVDA          S              8  0
      *
     D £OAVOV          S             15
     D £OAVON          S             15  5
     D £OAVOD          S              8  0
     D £OAVOA          S              8  0
     D £OAVOT          S              2
     D £OAVOP          S             10
     D £OAVCT          S             15
     D £OAVLI          S              2  0
     D £OAVDV          S             15
     D £OAVIN          S             35
     D £OAVSI          S             35
     D £OAVM1          S              7
     D £OAVM2          S              7
     D £OAVMS          S              7
     D £OAVFI          S             10
     D £OAVCM          S              2
     D £OAV35          S              1
     D £OAV36          S              1
      *
     D £OAVDI          DS          1024
     D £OAVI_CTRA                     1
     D £OAVI_PARM                   100
      *
     D £OAVDO          DS         32000
     D £OAVO_NOAU                     1
     D £OAVO_VAES                 30000    VARYING
     D £OAVO_FLDN                    10
     D £OAVO_OGGA                    50
     D £OAVO_LUNG                     5
     D £OAVO_DECI                     2
     D £OAVO_OBBL                     1
     D £OAVO_NCTP                     1
     D £OAVO_NCCO                     1
     D £OAVO_INOU                     1
     D £OAVO_CASE                     2
     D £OAVO_TEFI                     1
     D £OAVO_LUGR                     5
     D £OAVO_LOGM                     1
     D £OAVO_FLKY                     1
     D £OAVO_AUEN                     1
     D £OAVO_SPAR                     1
     D £OAVO_PARC                    10

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
      * Riga Accesso Collaboratore
     C                   WHEN      %SUBST($UIBME:1:7)='ACC.COL'
     C                   EXSR      ACCCOL
      * Albero per Sede appartenenza
     C                   WHEN      %SUBST($UIBME:1:7)='YRS.SED'
     C                   EXSR      YRSSED
      * Albero Lista sedi
1x   C                   WHEN      %SUBST($UIBME:1:7)='LST.SED'
     C                   EXSR      LSTSED
      * Generazione matrice
1x   C                   WHEN      %SUBST($UIBME:1:7)='MAT.CAL'
     C                   EXSR      MATCAL
      * Ricerca record e singola riga Matrice
1x   C                   WHEN      %SUBST($UIBME:1:7)='READ.RW'
     C                   EXSR      READRW
      * Dati per Gauge
     C                   WHEN      %SUBST($UIBME:1:7)='GAU'
     C                   EXSR      GAU
      * Button dinamici Conferma/Cancella
     C                   WHEN      %SUBST($UIBME:1:7)='TRE.BTN'
     C                   EXSR      TREBTN
      * Scrittura Record in DB
1x   C                   WHEN      %SUBST($UIBME:1:7)='WRI.REC'
     C                   EXSR      WRIREC
      * Delete Record in DB
1x   C                   WHEN      %SUBST($UIBME:1:7)='DEL.REC'
     C                   EXSR      DELREC
      * Matrice Collaboratori in Sede
1x   C                   WHEN      %SUBST($UIBME:1:7)='MAT.COL'
     C                   EXSR      MATCOL
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
     C                   CALL      '£JAX_IMP0'
     C                   PARM                    £JAXXML
     C                   EVAL      $$XML=%TRIM(£JAXXML)
      *
      *Recupero Nome Utente £PDS
     C                   CALL      '£PDS'
     C                   PARM                    £PDSNU
     C                   PARM                    £PDSDE
     C                   PARM                    £PDSEN
     C                   PARM                    £PDSSE
     C                   PARM                    £UDSMG
     C     £PDSNU        DSPLY     £PDSSU
     C     £UDSMG        DSPLY     £PDSSU
      *Controllo VARIABILI in ingresso
     C                   SELECT
     C                   WHEN      %SUBST($UIBME:1:7)='MAT.CAL'
     C                   IF        $UIBK2=''
     C                   GOTO      G9MAIN
     C                   ENDIF
      *
     C                   WHEN      %SUBST($UIBME:1:7)='READ.RW'
     C                   IF        $UIBK2='' OR $UIBK3=''
     C                   GOTO      G9MAIN
     C                   ENDIF
      *
     C                   WHEN      %SUBST($UIBME:1:7)='WRI.REC'
     C                   IF        $UIBK2='' OR $UIBK3=''
     C                   GOTO      G9MAIN
     C                   ENDIF
      *
     C                   WHEN      %SUBST($UIBME:1:7)='DEL.REC'
     C                   IF        $UIBK2='' OR $UIBK3=''
     C                   GOTO      G9MAIN
     C                   ENDIF
      *
     C                   WHEN      %SUBST($UIBME:1:7)='MAT.COL'
     C                   IF        $UIBK2=''
     C                   GOTO      G9MAIN
     C                   ENDIF
      *
     C                   WHEN      %SUBST($UIBME:1:7)='TRE.BTN'
     C                   IF        $UIBK2='' OR $UIBK3=''
     C                   GOTO      G9MAIN
     C                   ENDIF
     C                   ENDSL
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
     C                             +' Lun="15" Ogg="D8*YYMD" IO="O"></Colonna>'
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
     C                              +' E§CRAG='+%TRIM(AP)+%TRIM(£PDSNU)
     C                              +%TRIM(AP)
     C                   EVAL      $MSG=§SQL
     C     $MSG          DSPLY     £PDSSU
      * Apertura cursore £SQLD
     C                   EVAL      £SQLDString=%TRIM(§SQL)
     C                   EVAL      £SQLDTable='BRENTI0F'
     C                   EVAL      £SQLD35='0'
      *
     C                   EVAL      £SQLD_FUNFU='OPEN'
     C                   EVAL      £SQLD_FUNME='NAM'
      *
     C                   EXSR      £SQLD
      * Lettura Record
      * Fetch record cursore
     C                   EVAL      £SQLD_FUNFU='NXTREC'
     C                   EVAL      £SQLD_FUNME='MAT'
      *
     C                   EXSR      £SQLD
      *
     C                   EVAL      $MSG=£SQLDString
     C     $MSG          DSPLY     £PDSSU
      * test errore
     C                   IF        £SQLD35='1'  OR £SQLDCOD='100'
      * Scrittura riga con campi vuoti
     C                   EVAL      $$XMLPAYLOAD=%TRIM($$XMLPAYLOAD)
     C                             +'<Riga Fld="CN;COL;'+%TRIM(£PDSNU)
     C                                  +'|'
     C                                  +'|'
     C                                  +'|'
     C                                  +'|'
     C                                  +'"/>'
      *
     C                   GOTO      G9ACCCOL
     C                   ENDIF
      *
     C                   EVAL      $MSG=£SQLDString
     C     $MSG          DSPLY     £PDSSU
     C                   EVAL      $$NOME=£SQLDString
      * Chiusura Puntatore
     C                   EVAL      £SQLD_FUNFU='CLOSE'
     C                   EVAL      £SQLD_FUNME=*BLANKS
      *
     C                   EXSR      £SQLD
      *Sede Appartenenza
     C                   EVAL      £OAVFU='ATT'
     C                   EVAL      £OAVME='VTS'
     C                   EVAL      £OAVT1='CN'
     C                   EVAL      £OAVP1='COL'
     C                   EVAL      £OAVC1=%TRIM(£PDSNU)
     C                   EVAL      £OAVAT='U/011'
      *
     C                   EXSR      £OAV
      *
     C     £OAVOV        DSPLY     £PDSSU
     C     £OAVSI        DSPLY     £PDSSU
      * Recuperare ultimo accesso
      * Settaggio Variabili
     C                   EVAL      $$COMM=''
      * Stringa SQL
     C                   EVAL      §SQL='SELECT MAX(V£DTAG) FROM verapg0f'
     C                              +' WHERE V£CDC='+%TRIM(AP)+%TRIM($$COMM)
     C                              + %TRIM(AP)+' AND'
     C                              +' V£NOME='+%TRIM(AP)+%TRIM(£PDSNU)
     C                              +%TRIM(AP)
     C                   EVAL      $MSG=§SQL
     C     $MSG          DSPLY     £PDSSU
      * Apertura cursore £SQLD
      *
     C                   EVAL      £SQLD_FUNFU='OPEN'
     C                   EVAL      £SQLD_FUNME='NAM'
     C                   EVAL      £SQLDString=%TRIM(§SQL)
     C                   EVAL      £SQLDTable='VERAPG0F'
      *
     C                   EXSR      £SQLD
      * Lettura Record
      * Fetch record cursore
     C                   EVAL      £SQLD_FUNFU='NXTREC'
     C                   EVAL      £SQLD_FUNME='MAT'
      *
     C                   EXSR      £SQLD
     C                   EVAL      $MSG=£SQLDString
     C     $MSG          DSPLY     £PDSSU
      * test errore
     C                   IF        £SQLD35='1' OR £SQLDCOD='100'
     C                   EVAL      $$DATA=0
     C                   ELSE
      *
     C                   EVAL      $MSG=£SQLDString
     C     $MSG          DSPLY     £PDSSU
     C                   EVAL      $$DATA=%INT(£SQLDString)
      *
     C                   ENDIF
      *
      * Scrittura riga con JAXCP
     C                   EVAL      $$XMLPAYLOAD=%TRIM($$XMLPAYLOAD)
     C                             +'<Riga Fld="CN;COL;'+%TRIM(£PDSNU)
     C                                  +'|'+%TRIM($$NOME)
     C                                  +'|'+%EDITC($$DATA:'X')
     C                                  +'|'+%TRIM(£OAVSI)
     C                                  +'|'+%TRIM(£OAVOV)
     C                                  +'"/>'
      *
     C     G9ACCCOL      TAG
      * Chiusura Puntatore
     C                   EVAL      £SQLD_FUNFU='CLOSE'
     C                   EVAL      £SQLD_FUNME=*BLANKS
      *
     C                   EXSR      £SQLD
      * Aggiungi righe - finalizzazione
     C                   EVAL      $$XMLPAYLOAD=%TRIM($$XMLPAYLOAD)
     C                              +'</Righe>'
      *
     C                   ENDSR
      *--------------------------------------------------------------*
     D*  Albero - Sede Appartenenza
      *--------------------------------------------------------------*
     C     YRSSED        BEGSR
      *Quando £OAV - utente con £PDSNU
     C                   EVAL      £OAVFU='ATT'
     C                   EVAL      £OAVME='VTS'
     C                   EVAL      £OAVT1='CN'
     C                   EVAL      £OAVP1='COL'
     C                   EVAL      £OAVC1=%TRIM(£PDSNU)
     C                   EVAL      £OAVAT='U/011'
      *
     C                   EXSR      £OAV
      *
     C     £OAVOV        DSPLY     £PDSSU
     C     £OAVSI        DSPLY     £PDSSU
     C                   IF        £OAVOV<>'' AND £OAVSI=''
     C                   FOR       $NSED=1 to %ELEM(TRESED)
      * Quando finisce l'array, esce
     C                   IF        TRESED($NSED)=''
     C                   LEAVE
     C                   ENDIF
      * Check con sede di appartenenza
     C                   IF        %SUBST(TRESED($NSED):1:10)= £OAVOV
     C                   EVAL      £OAVSI=%SUBST(TRESED($NSED):11)
     C                   LEAVE
     C                   ENDIF
     C                   ENDFOR
     C                   ENDIF
      * Sede Appartenenza
     C                   EVAL      $$XMLPAYLOAD=%TRIM($$XMLPAYLOAD)
     C                             +'<Oggetto Tipo="CN"'
     C                             +' Parametro="SED" Codice="'
     C                             +%TRIM(£OAVOV)+'"'
     C*                             + 'ERB"'
     C                             +' Testo="'
     C                             +%TRIM(£OAVSI)+'"'
     C*                             + 'Erbusco "'
     C                             +' />'
      *
     C                   ENDSR
      *--------------------------------------------------------------*
     D*  Albero - Lista Sedi (codice e descrizione)
      *--------------------------------------------------------------*
     C     LSTSED        BEGSR
      *
     C                   EVAL      £OAVFU='ATT'
     C                   EVAL      £OAVME='VTS'
     C                   EVAL      £OAVT1='CN'
     C                   EVAL      £OAVP1='COL'
     C                   EVAL      £OAVC1=%TRIM(£PDSNU)
     C                   EVAL      £OAVAT='U/011'
      *
     C                   EXSR      £OAV
      *
     C     £OAVOV        DSPLY     £PDSSU
      *
     C                   EVAL      $NSED=0
     C                   DO        14
     C                   EVAL      $NSED=$NSED+1
      *
     C                   IF        TRESED($NSED)=''
     C                   LEAVE
     C                   ENDIF
      *
     C                   IF        %SUBST(TRESED($NSED):1:10)=£OAVOV
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
      *
     C                   EVAL      $$MM=%INT(%SUBST(%EDITC(£UDSMG:'X'):5:2))
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
     C                   EVAL      $$MESE=%INT(%SUBST(%EDITC(£UDSMG:'X'):1:6)+
     C                             '01')
     C                   EVAL      $MSG=%EDITC(£UDSMG:'X')
     C     $MSG          DSPLY     £PDSSU
     C                   EVAL      $MSG=%EDITC($$MESE:'X')
     C     $MSG          DSPLY     £PDSSU
     C                   EVAL      $MSG=%EDITC($$GG:'X')
     C     $MSG          DSPLY     £PDSSU
      * Creazione Chiave
     C     K9L           KLIST
     C                   KFLD                    $$MESE
     C                   KFLD                    $$COMM
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
     C                   IF        V£NOME=%TRIM(£PDSNU)
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
      * Creazione Chiave
     C     K9R           KLIST
     C                   KFLD                    $$DATA
     C                   KFLD                    $$COMM
      * settaggio variabili
     C                   EVAL      $$COMM=''
     C                   EVAL      $$DATA=%INT($UIBK2)
     C                   EVAL      $PRNT=0
     C                   EVAL      $DSPN=0
     C                   CLEAR                   CODCOL
     C                   EVAL      $N=1
      * Ciclo lettura
     C     K9R           SETLL     VERAPG9L
3x   C                   DO        *HIVAL
     C     K9R           READE     VERAPG9L
      * Fine del file - esce
4x   C                   IF        %EOF
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
      * Conta posti Prenotati
     C                   IF        V£ATV0='2'
     C                   EVAL      $PRNT=$PRNT+1
     C                   ENDIF
      *
3e   C                   ENDDO
      * Aumenti contatori
     C                   EVAL      $DSPN=$POSTI-$PRNT
      * Scrittura riga
     C                   EVAL      $$XMLPAYLOAD=%TRIM($$XMLPAYLOAD)
     C                              +'<Riga Fld="'+%EDITC($$DATA:'Z')
     C                                   +'|'+'Posti Sede:'+%EDITC($DSPN:'Z')   COSTANTE
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
    RD*  Dati per Gauge
      *--------------------------------------------------------------*
     C     GAU           BEGSR
      * Aggiungi griglia
     C                   EVAL      $$XMLPAYLOAD='<Griglia><Colonna Cod='
     C                             +'"XXPOCC"  Txt="Perc Occupazione"'
     C                             +' Lun="03;0" Ogg="NR" IO="O"></Colonna>'
     C                             +'</Griglia>'
      * Aggiungi righe - inizializzazione
     C                   EVAL      $$XMLPAYLOAD=%TRIM($$XMLPAYLOAD)
     C                              +'<Righe>'
      * Creazione Chiave
     C     K9G           KLIST
     C                   KFLD                    $$DATA
     C                   KFLD                    $$COMM
      * settaggio variabili
     C                   EVAL      $$COMM=''
     C                   EVAL      $$DATA=%INT($UIBK2)
     C                   EVAL      $PRNT=0
     C                   EVAL      $DSPN=0
     C                   CLEAR                   CODCOL
     C                   EVAL      $N=1
      * Ciclo lettura
     C     K9G           SETLL     VERAPG9L
     C                   DO        *HIVAL
     C     K9G           READE     VERAPG9L
      * Fine del file - esce
     C                   IF        %EOF
     C                   LEAVE
     C                   ENDIF
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
      * Conta posti Prenotati
     C                   IF        V£ATV0='2'
     C                   EVAL      $PRNT=$PRNT+1
     C                   ENDIF
      *
     C                   ENDDO
      *
     C     $PRNT         DSPLY     £PDSSU
      *
      * Inizializzazione Variabili
     C                   EVAL      $VAL=0
      *
     C                   IF        $PRNT<>0
     C                   EVAL(H)   $VAL=($PRNT*100)/$POSTI
     C                   ENDIF
      *
     C     $VAL          DSPLY     £PDSSU
      * Scrittura riga
     C                   EVAL      $$XMLPAYLOAD=%TRIM($$XMLPAYLOAD)
     C                              +'<Riga Fld="'+%EDITC($VAL:'P')
     C                                   +'"/>'
      * Aggiungi righe - finalizzazione
     C                   EVAL      $$XMLPAYLOAD=%TRIM($$XMLPAYLOAD)
     C                              +'</Righe>'
      *
     C                   ENDSR
      *--------------------------------------------------------------*
     D*  Albero - Button Conferma/Cancella
      *--------------------------------------------------------------*
     C     TREBTN        BEGSR
      *
      * Inizializazzione Variabili
     C                   EVAL      §DESC= 'Conferma Prenotazione'
     C                   EVAL      §STRFUN='F(FBK;X1_X21_03;WRI.REC)'
     C                             + ' 2(;;' + %TRIM($UIBK2) + ')'
     C                             + ' 3(;;' + %TRIM($UIBK3) + ')'
     C                             + ' NOTIFY(DSH02\BTN15\GAU01)'
      *
      * Creazione Chiave
     C     K9T           KLIST
     C                   KFLD                    $$DATA
     C                   KFLD                    $$COMM
      * Inizializzazione variabili
     C                   EVAL      $$DATA=%INT($UIBK2)
     C                   EVAL      $$COMM=''
      * Ciclo Lettura
     C     K9T           SETLL     VERAPG9L
     C                   DO        *HIVAL
     C     K9T           READE     VERAPG9L
      * Fine del file - esce
     C                   IF        %EOF
     C                   LEAVE
     C                   ENDIF
      *Condizione sulla SEDE
     C                   IF        V£COD1<>$UIBK3
     C                   ITER
     C                   ENDIF
      * Se trova il record dell'utente
     C                   IF        V£NOME=%TRIM(£PDSNU)
     C                   IF        V£ATV0='2'
     C                   EVAL      §DESC= 'Cancella Prenotazione'
     C                   EVAL      §STRFUN='F(FBK;X1_X21_03;DEL.REC)'
     C                             + ' 2(;;' + %TRIM($UIBK2) + ')'
     C                             + ' 3(;;' + %TRIM($UIBK3) + ')'
     C                             + ' NOTIFY(DSH02\BTN15\GAU01)'
     C                   ENDIF
     C                   ENDIF
      *
     C                   ENDDO
      *
     C                   EVAL      £DECTP='CN'
     C                   EVAL      £DECPA='SED'
     C                   EVAL      £DECCD=$UIBK3
      *
     C                   EXSR      £DEC
      *      *
     C                   EVAL      $$XMLPAYLOAD=%TRIM($$XMLPAYLOAD)             COSTANTE
     C                             +'<Oggetto Codice="'+%TRIM(§STRFUN)+'" '
     C                             +'Testo="' +%TRIM(§DESC)+ '"/> '
     C                             +'<Oggetto Codice="F(EXD;*SCO;)'
     C                             +' 2(MB;SCP_SCH;X1_X21_04) 4(;;COL)'
     C                             +' P('
     C                             +' Sede('+%TRIM(£DECO_DESC)+')'
     C                             +' Data('+%TRIM($UIBK2)+')'
     C                             +' DataEstesa('+%TRIM($UIBK5)+')'
     C                             +' CodSede('+%TRIM($UIBK3)+')'
     C                             +' CodMese('+%TRIM($UIBK6)+'))"'
     C                             +' Testo="'+%TRIM('Elenco Collaboratori')
     C                             + '"/>'
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD*  Dati per inserire Record in DB
      *--------------------------------------------------------------*
     C     WRIREC        BEGSR
      *
      * Creazione Chiave
     C     K1W           KLIST
     C                   KFLD                    $$DATA
     C                   KFLD                    $$NOME
      * settaggio variabili
     C                   EVAL      $$DATA=%INT($UIBK2)
     C                   EVAL      $$NOME=%TRIM(£PDSNU)
     C                   EVAL      $UTER=0
      * Ciclo lettura
     C     K1W           SETLL     VERAPG1
     C                   DO        *HIVAL
     C     K1W           READE     VERAPG1
      * Fine del file - esce
4x   C                   IF        %EOF
     C                   LEAVE
4e   C                   ENDIF
      * Controllo sulla sede
     C                   IF        V1COD1=%TRIM($UIBK3)
     C                   IF        %TRIM(V1CDC)=''
     C                   EVAL      $UTER=$UTER+1
     C                   ENDIF
     C                   ENDIF
      *
2e   C                   ENDDO
     C                   IF        $UTER=0
      * Inserisco riga prenotazione
     C                   DO        10
     C                   EXSR      £XAIDOJ
     C                   EVAL      V0IDOJ=V£IDOJ
     C                   EVAL      V0ATV0='2'
     C                   EVAL      V0DATA=%INT($UIBK2)
     C                   EVAL      V0NOME=%TRIM(£PDSNU)
     C                   EVAL      V0CDC=''
     C                   EVAL      V0COD1=$UIBK3
     C                   EVAL      V0COM2='$$EXT Prenotazione Sede EXT$$'       COSTANTE
     C                   EVAL      V0USIN=%TRIM(£PDSNU)
     C                   EVAL      V0DTIN=£UDSMG
     C                   EVAL      V0USAG=%TRIM(£PDSNU)
     C                   EVAL      V0DTAG=£UDSMG
      * Scrivo riga
     C*                   WRITE     VERAPG0                              50
      *Se la scrittura va a buon fine esco, altrimenti ciclo
     C                   IF        *IN50=*OFF
      * Formattazione data
     C                   EVAL      $$DATAFMT=%SUBST(%EDITC($$DATA:'Z'):7:2)+'/'
     C                                      + %SUBST(%EDITC($$DATA:'Z'):5:2)+'/'
     C                                      + %SUBST(%EDITC($$DATA:'Z'):1:4)
      *
     C                   EVAL      $$XMLPAYLOAD=%TRIM($$XMLPAYLOAD)
     C                              +'<Messaggi><Messaggio'
     C                              +' Testo="La prenotazione per il'
     C                              +' giorno '+%TRIM($$DATAFMT)
     C                              +' è stata CONFERMATA"'
     C                              +' Gravity="INFO" Mode="TN" Tipo="INFO"/>'
     C                              +' </Messaggi>'
      *
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
      * Creazione Chiave
     C     K1D           KLIST
     C                   KFLD                    $$DATA
     C                   KFLD                    $$NOME
      * settaggio variabili
     C                   EVAL      $$DATA=%INT($UIBK2)
     C                   EVAL      $$NOME=%TRIM(£PDSNU)
      * Ciclo Lettura
     C     K1D           SETLL     VERAPG1
     C                   DO        *HIVAL
     C     K1D           READE     VERAPG1
      * Fine del file - esce
4x   C                   IF        %EOF
     C                   LEAVE
4e   C                   ENDIF
      * Controllo sulla sede
     C                   IF        %TRIM(V1COD1)<>%TRIM($UIBK3)
     C                   ITER
     C                   ENDIF
      *Controllo sulla commessa
     C                   IF        V1CDC <>''
     C                   ITER
     C                   ENDIF
      *
     C     V1IDOJ        CHAIN     VERAPG0L
      *
     C                   IF        %FOUND
      * Elimino riga prenotazione
     C*                   DELETE    VERAPG0
      *
      * Formattazione data
     C                   EVAL      $$DATAFMT=%SUBST(%EDITC($$DATA:'Z'):7:2)+'/'
     C                                      + %SUBST(%EDITC($$DATA:'Z'):5:2)+'/'
     C                                      + %SUBST(%EDITC($$DATA:'Z'):1:4)
      *
     C                   EVAL      $$XMLPAYLOAD=%TRIM($$XMLPAYLOAD)
     C                              +'<Messaggi><Messaggio'
     C                              +' Testo="La prenotazione per il'
     C                              +' giorno '+%TRIM($$DATAFMT)
     C                              +' è stata CANCELLATA"'
     C                              +' Gravity="INFO" Mode="TN" Tipo="INFO"/>'
     C                              +' </Messaggi>'
      *
     C                   LEAVE
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
      * Creazione Chiave
     C     K9C           KLIST
     C                   KFLD                    $$DATA
     C                   KFLD                    $$COMM
      * settaggio variabili
     C                   EVAL      $$COMM=''
     C                   EVAL      $$DATA=%INT($UIBK2)
     C                   EVAL      $$SEDE=%TRIM($UIBK3)
     C                   CLEAR                   CODCOL
     C                   EVAL      $N=1
      * Ciclo lettura
     C     K9C           SETLL     VERAPG9L
     C                   DO        *HIVAL
     C     K9C           READE     VERAPG9L
      * Fine del file - esce
4x   C                   IF        %EOF
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
     C     $$NOME        DSPLY     £PDSSU
      *
      * Conta posti Prenotati
     C                   IF        V£ATV0='2'
     C                   EVAL      $$TPPT='Prenotato'                           COSTANTE
     C                   ENDIF
     C     $$NOME        DSPLY     £PDSSU
      *
      * Stringa SQL
     C                   EVAL      §SQL='SELECT E§RAGS FROM brenti0f'
     C                              +' WHERE E§TRAG='+%TRIM(AP)+'COL'
     C                              +%TRIM(AP)+' AND'
     C                              +' E§CRAG='+%TRIM(AP)+%TRIM($$NOME)
     C                              +%TRIM(AP)
     C                   EVAL      $MSG=§SQL
     C     $MSG          DSPLY     £PDSSU
      * Apertura cursore £SQLD
     C                   EVAL      £SQLDString=%TRIM(§SQL)
     C                   EVAL      £SQLDTable='BRENTI0F'
     C                   EVAL      £SQLD35='0'
      *
     C*                  EVAL      £SQLDRows=0
     C                   EVAL      £SQLD_FUNFU='OPEN'
     C                   EVAL      £SQLD_FUNME='NAM'
      *
     C                   EXSR      £SQLD
      * Lettura Record
      * Fetch record cursore
     C                   EVAL      £SQLD_FUNFU='NXTREC'
     C                   EVAL      £SQLD_FUNME='MAT'
      *
     C                   EXSR      £SQLD
      *
     C                   EVAL      $MSG=£SQLDString
     C     $MSG          DSPLY     £PDSSU
      * test errore
     C                   IF        £SQLD35='1'  OR £SQLDCOD='100'
     C                   GOTO      G9MATCOL
     C                   ENDIF
      *
     C                   EVAL      $MSG=£SQLDString
     C     $MSG          DSPLY     £PDSSU
      * Chiusura Puntatore
     C                   EVAL      £SQLD_FUNFU='CLOSE'
     C                   EVAL      £SQLD_FUNME=*BLANKS
      *
     C                   EXSR      £SQLD
      * Scrittura riga
     C                   EVAL      $$XMLPAYLOAD=%TRIM($$XMLPAYLOAD)
     C                              +'<Riga Fld="CN;COL;'+%TRIM($$NOME)
     C*                                   +'|'+%TRIM($$NOME)
     C                                   +'|'+%TRIM(£SQLDString)
     C                                   +'|'+%TRIM($$TPPT)
     C                                   +'"/>'
3e   C                   ENDDO
      *
     C     G9MATCOL      TAG
      * Aggiungi righe - finalizzazione
     C                   EVAL      $$XMLPAYLOAD=%TRIM($$XMLPAYLOAD)
     C                              +'</Righe>'
      *
     C                   ENDSR
      *--------------------------------------------------------------*
     D*  Richiamo £DEC
      *--------------------------------------------------------------*
     C     £DEC          BEGSR
      *
     C                   CALL      'B£DEC0'
     C                   PARM                    £DECCD
     C                   PARM                    £DECTP
     C                   PARM                    £DECPA
     C                   PARM                    £DECR1
     C                   PARM                    £DECR2
     C                   PARM                    £DECDE
     C                   PARM                    £DECIN
     C                   PARM                    £DEC35
     C                   PARM                    £DEC36
     C                   PARM                    £DECAM
     C                   PARM                    £DECCO
     C                   PARM                    £DECDT
     C                   PARM                    £DECDI
     C                   PARM                    £DECDO
      *
     C                   ENDSR
      *--------------------------------------------------------------*
     D*  Richiamo £SQLD
      *--------------------------------------------------------------*
     C     £SQLD         BEGSR
      *
     C                   CALL      '£SQLD'
     C                   PARM                    £SQLD_FUNFU
     C                   PARM                    £SQLD_FUNME
     C                   PARM                    £SQLDString
     C                   PARM                    £SQLDTable
     C                   PARM                    £SQLD35
     C                   PARM                    £SQLDCOD
      *
     C                   ENDSR
      *--------------------------------------------------------------------------------------------*
      * /COPY £OAV
      *--------------------------------------------------------------------------------------------*
     C     £OAV          BEGSR
      *
     C                   CALL      'B£OAV0'
     C                   PARM                    £OAVFU
     C                   PARM                    £OAVME
     C                   PARM                    £OAVT1
     C                   PARM                    £OAVP1
     C                   PARM                    £OAVC1
     C                   PARM                    £OAVT2
     C                   PARM                    £OAVP2
     C                   PARM                    £OAVC2
     C                   PARM                    £OAVAT
     C                   PARM                    £OAVDA
     C                   PARM                    £OAVOV
     C                   PARM                    £OAVON
     C                   PARM                    £OAVOD
     C                   PARM                    £OAVOA
     C                   PARM                    £OAVOT
     C                   PARM                    £OAVOP
     C                   PARM                    £OAVCT
     C                   PARM                    £OAVLI
     C                   PARM                    £OAVDV
     C                   PARM                    £OAVIN
     C                   PARM                    £OAVSI
     C                   PARM                    £OAVM1
     C                   PARM                    £OAVM2
     C                   PARM                    £OAVMS
     C                   PARM                    £OAVFI
     C                   PARM                    £OAVCM
     C                   PARM                    £OAV35
     C                   PARM                    £OAV36
     C                   PARM                    £OAVDI
     C                   PARM                    £OAVDO
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD*  Copia /COPY £XAIDOJ
      *--------------------------------------------------------------*
     C     £XAIDOJ       BEGSR
      *
     C                   CLEAR                   V£IDOJ
     C                   CLEAR                   $IDOJ            15 0
      * ... cerca in archivio
     C*    *HIVAL        SETGT     VERAPG0L
     C                   READP     VERAPG0L
     C                   IF        NOT %EOF
     C                   EVAL      $IDOJ=%INT(V£IDOJ)+1
     C                   ELSE
     C                   EVAL      $IDOJ=1
     C                   ENDIF
     C                   MOVEL(P)  $IDOJ         V£IDOJ
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
     C                   PARM                    £UIBDS
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
LEC       Lecco
MOD       Modena
NOV       Nova Milanese
PAR       Parma
REG       Reggio Emilia
TOR       Rivoli - Torino
ROM       Roma
SAV       Savigliano
UDI       Udine - Sinte.sys
VER       Vercelli
VIF       Vigonza
VIL       Villaverla Vicenza
