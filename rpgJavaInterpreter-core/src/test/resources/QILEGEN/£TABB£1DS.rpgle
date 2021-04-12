      /IF NOT DEFINED(£TABB£1DS)
      /DEFINE £TABB£1DS
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
      /ENDIF
