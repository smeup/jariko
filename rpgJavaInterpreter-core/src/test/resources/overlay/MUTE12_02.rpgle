   COP* *NOUI
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 20/08/19  001071  BMA Creazione
     V*=====================================================================
     V* OBIETTIVO
     V*  Programma finalizzato ai test su campi di tipo DS Qualificate
     V*
     V*=====================================================================
     D £V5PDS          DS           512    INZ
     D  £V5PSA                 1      2
     D  £V5PAZ                 3      7
     D  £V5PPA                 8     12
     D  £V5PTD                13     15
     D  £V5PMO                16     18
     D  £V5PND                19     28
     D  £V5PNR                29     32  0
     D  £V5POT                33     35
     D  £V5POM                36     38
     D  £V5PON                39     48
     D  £V5POR                49     52  0
     D  £V5PTC                53     55
     D  £V5PEN                56     70
     D  £V5PTR                71     73
     D  £V5PTT                74     85
     D  £V5POG                86    100
     D  £V5PDT               101    135
     D  £V5PMG               136    138
     D  £V5PQM               139    139  0
     D  £V5PQT               140    145P 3
     D  £V5PQA               146    151P 3
     D  £V5PTA               152    153
     D  £V5PAT               154    158
     D  £V5PUM               159    160
     D  £V5PUA               161    162
     D  £V5PFT               163    170P 5
     D  £V5PLT               171    171
     D  £V5PST               172    173
     D  £V5PLR               174    174
     D  £V5PSR               175    176
     D  £V5PR3               177    177
     D  £V5PR4               178    178
     D  £V5PR5               179    179
     D  £V5PR7               180    180
     D  £V5PR8               181    181
     D  £V5PR9               182    182
     D  £V5PCV               183    186
     D  £V5PCA               187    193P 6
     D  £V5PCQ               194    196
     D  £V5PEM               197    198
     D  £V5PSS               199    200
     D  £V5PLS               201    203
     D  £V5PSZ               204    206
     D  £V5PTS               207    211
     D  £V5PCR               212    221
     D  £V5PCM               222    231
     D  £V5PLO               232    246
     D  £V5PCO               247    266
     D  £V5POP               267    281
     D  £V5PCT               282    296
     D  £V5PAG               297    299
     D  £V5PS1               300    310P 6
     D  £V5PS2               311    321P 6
     D  £V5PS3               322    332P 6
     D  £V5PS4               333    343P 6
     D  £V5PS5               344    354P 6
     D  £V5PP1               355    355
     D  £V5PGF               356    365
     D  £V5PN1               366    369P 0
     D  £V5PN2               370    373P 0
     D  £V5PN3               374    377P 0
     D  £V5PN4               378    381P 0
     D  £V5PN5               382    385P 0
     D  £V5PAC               386    386
     D  £V5PRT               387    389
     D  £V5PRN               390    399
     D  £V5PRS               400    401
     D  £V5PRM               402    406P 0
     D  £V5PVG               407    421
     D  £V5PDC               422    429  0
     D  £V5PD1               430    437
     D  £V5PDV               438    445  0
     D  £V5PD2               446    453
     D  £V5PPE               454    464P 6
     D  £V5PQR               465    470P 3
     D  £V5PTG               471    471
     D  £V5PEX               472    472
     D  £V5PRR               473    473
     D  £V5P29               474    474
     D  £V5PRC               475    484
     D  £V5PSC               485    499
      *
      * Se sulla DS metto INZ vengono inizializzati al valore di default tutti i suoi sottocampi
      * Quindi i campi numerici sono inizializzati a 0
     D DS00DS          DS                  INZ
     D  DS0000                01     50
      * Non è obbligatorio che le posizioni siano sequenziali.
      * In questo caso c'è un filler vuoto da 51 a 100 che non è referenziato tramite un sottocampo
     D  DS0001               101    135
     D  DS0002               136    138
     D  DS0003               139    139  0
      * Per capire qual è la precisione di un packed definito con posizione iniziale e finale :
      * precisione = 2n-1 , quindi nel caso sottostante 2*6-1 ; è quindi un numero con
      * precisione 11 e scala 3.
      * Se invece volessi sapere che dimensione occupa un packed 11,3 devo fare:
      * dimensione = precisione/2 +1  (quindi 6)
     D  DS0004               140    145P 3
      * Se specificato PACKEVEN la formula è 2(n-1) invece che 2n-1, quindi questo è un 10,3
     D  DS0005               146    151P 3 PACKEVEN
     D  DS0006               152    153
     D  DS0007               154    158
     D  DS0008               159    160
     D  DS0009               161    162
     D  DS0010               163    170P 5
      * Binary da 2 byte
     D  DS0011               171    172B 0
      * Binary da 4 byte
     D  DS0012               173    176B 0
      * Integer da 1 byte
     D  DS0013               177    177I 0
      * Integer da 1 byte unsigned
     D  DS0014               178    178U 0
      * Integer da 2 byte
     D  DS0015               179    180I 0
      * Integer da 2 byte unsigned
     D  DS0016               181    182U 0
      * Integer da 4 byte
     D  DS0017               183    186I 0
      * Integer da 4 byte unsigned
     D  DS0018               187    190U 0
      * Integer da 8 byte
     D  DS0019               191    198I 0
      * Integer da 8 byte unsigned
     D  DS0020               199    206U 0
      *
      * Se sulla DS metto non metto INZ e non c'è INZ sui sottocampi, la DS è complessivamente
      * inizializzata a blank e i sottocampi numerici non contengono un valore valido
      * (quindi se vengono utilizzati in un espressione, come un IF, o in una funzione,
      * prima di essere assegnati generano una eccezione)
     D DS01DS          DS                  LIKEDS(DS00DS)
      *
      * Riferimento £G40
     D £40FDS          DS                  INZ
     D  £40FDE                 1     30                                         Descrizione
     D  £40FRE                31     80                                         Tipo Record
     D  £40FNT                81     85  0                                      N° Oggetti Gestiti
     D  £40F_MSO              86     86                                         Gestione OG_SCA
     D  £40F_MWL              87     87                                         Gestione WHR_LI
     D  £40F_MWC              88     88                                         Gestione WHR_CO
     D  £40FLU                89     90                                         Livello Utente
     D  £40F_MSR              91     91                                         Gestione RE_SCA
     D  £40FNS                92     92                                         NO SQL
     D  £40FTO              1096   7095    DIM(500)                             Oggetti Gestiti
      *
     D  NN1            S              5  0
      *
    MU* VAL1(£V5PDS)   VAL2('')                                  COMP(EQ)
    MU* VAL1(£V5PSA)   VAL2('  ')                                COMP(EQ)
    MU* VAL1(£V5PAZ)   VAL2('     ')                             COMP(EQ)
    MU* VAL1(£V5PPA)   VAL2('     ')                             COMP(EQ)
    MU* VAL1(£V5PTD)   VAL2('   ')                               COMP(EQ)
    MU* VAL1(£V5PMO)   VAL2('   ')                               COMP(EQ)
    MU* VAL1(£V5PND)   VAL2('          ')                        COMP(EQ)
    MU* VAL1(£V5PNR)   VAL2(0000)                                COMP(EQ)
    MU* VAL1(£V5POT)   VAL2('   ')                               COMP(EQ)
    MU* VAL1(£V5POM)   VAL2('   ')                               COMP(EQ)
    MU* VAL1(£V5PON)   VAL2('          ')                        COMP(EQ)
    MU* VAL1(£V5POR)   VAL2(0000)                                COMP(EQ)
    MU* VAL1(£V5PTC)   VAL2('   ')                               COMP(EQ)
    MU* VAL1(£V5PEN)   VAL2('               ')                   COMP(EQ)
    MU* VAL1(£V5PTR)   VAL2('   ')                               COMP(EQ)
    MU* VAL1(£V5PTT)   VAL2('            ')                      COMP(EQ)
    MU* VAL1(£V5POG)   VAL2('               ')                   COMP(EQ)
    MU* VAL1(£V5PDT)   VAL2('               ')                   COMP(EQ)
    MU* VAL1(£V5PMG)   VAL2('   ')                               COMP(EQ)
    MU* VAL1(£V5PQM)   VAL2(0)                                   COMP(EQ)
    MU* VAL1(£V5PQT)   VAL2(00000000,000)                        COMP(EQ)
    MU* VAL1(£V5PQA)   VAL2(00000000,000)                        COMP(EQ)
    MU* VAL1(£V5PTA)   VAL2('  ')                                COMP(EQ)
    MU* VAL1(£V5PAT)   VAL2('     ')                             COMP(EQ)
    MU* VAL1(£V5PUM)   VAL2('  ')                                COMP(EQ)
    MU* VAL1(£V5PUA)   VAL2('  ')                                COMP(EQ)
    MU* VAL1(£V5PFT)   VAL2(0000000000,00000)                    COMP(EQ)
    MU* VAL1(£V5PLT)   VAL2(' ')                                 COMP(EQ)
    MU* VAL1(£V5PST)   VAL2('  ')                                COMP(EQ)
    MU* VAL1(£V5PLR)   VAL2(' ')                                 COMP(EQ)
    MU* VAL1(£V5PSR)   VAL2('  ')                                COMP(EQ)
    MU* VAL1(£V5PR3)   VAL2(' ')                                 COMP(EQ)
    MU* VAL1(£V5PR4)   VAL2(' ')                                 COMP(EQ)
    MU* VAL1(£V5PR5)   VAL2(' ')                                 COMP(EQ)
    MU* VAL1(£V5PR7)   VAL2(' ')                                 COMP(EQ)
    MU* VAL1(£V5PR8)   VAL2(' ')                                 COMP(EQ)
    MU* VAL1(£V5PR9)   VAL2(' ')                                 COMP(EQ)
    MU* VAL1(£V5PCV)   VAL2('    ')                              COMP(EQ)
    MU* VAL1(£V5PCA)   VAL2(0000000,000000)                      COMP(EQ)
    MU* VAL1(£V5PCQ)   VAL2('   ')                               COMP(EQ)
    MU* VAL1(£V5PEM)   VAL2('  ')                                COMP(EQ)
    MU* VAL1(£V5PSS)   VAL2('  ')                                COMP(EQ)
    MU* VAL1(£V5PLS)   VAL2('   ')                               COMP(EQ)
    MU* VAL1(£V5PSZ)   VAL2('   ')                               COMP(EQ)
    MU* VAL1(£V5PTS)   VAL2('     ')                             COMP(EQ)
    MU* VAL1(£V5PCR)   VAL2('          ')                        COMP(EQ)
    MU* VAL1(£V5PCM)   VAL2('          ')                        COMP(EQ)
    MU* VAL1(£V5PLO)   VAL2('               ')                   COMP(EQ)
    MU* VAL1(£V5PCO)   VAL2('                    ')              COMP(EQ)
    MU* VAL1(£V5POP)   VAL2('               ')                   COMP(EQ)
    MU* VAL1(£V5PCT)   VAL2('               ')                   COMP(EQ)
    MU* VAL1(£V5PAG)   VAL2('   ')                               COMP(EQ)
    MU* VAL1(£V5PS1)   VAL2(000000000000000,000000)              COMP(EQ)
    MU* VAL1(£V5PS2)   VAL2(000000000000000,000000)              COMP(EQ)
    MU* VAL1(£V5PS3)   VAL2(000000000000000,000000)              COMP(EQ)
    MU* VAL1(£V5PS4)   VAL2(000000000000000,000000)              COMP(EQ)
    MU* VAL1(£V5PS5)   VAL2(000000000000000,000000)              COMP(EQ)
    MU* VAL1(£V5PP1)   VAL2(' ')                                 COMP(EQ)
    MU* VAL1(£V5PGF)   VAL2('          ')                        COMP(EQ)
    MU* VAL1(£V5PN1)   VAL2(0000000)                             COMP(EQ)
    MU* VAL1(£V5PN2)   VAL2(0000000)                             COMP(EQ)
    MU* VAL1(£V5PN3)   VAL2(0000000)                             COMP(EQ)
    MU* VAL1(£V5PN4)   VAL2(0000000)                             COMP(EQ)
    MU* VAL1(£V5PN5)   VAL2(0000000)                             COMP(EQ)
    MU* VAL1(£V5PAC)   VAL2(' ')                                 COMP(EQ)
    MU* VAL1(£V5PRT)   VAL2('   ')                               COMP(EQ)
    MU* VAL1(£V5PRN)   VAL2('          ')                        COMP(EQ)
    MU* VAL1(£V5PRS)   VAL2('  ')                                COMP(EQ)
    MU* VAL1(£V5PRM)   VAL2(000000000)                           COMP(EQ)
    MU* VAL1(£V5PVG)   VAL2('               ')                   COMP(EQ)
    MU* VAL1(£V5PDC)   VAL2(00000000)                            COMP(EQ)
    MU* VAL1(£V5PD1)   VAL2('        ')                          COMP(EQ)
    MU* VAL1(£V5PDV)   VAL2(00000000)                            COMP(EQ)
    MU* VAL1(£V5PD2)   VAL2('        ')                          COMP(EQ)
    MU* VAL1(£V5PPE)   VAL2(000000000000000,000000)              COMP(EQ)
    MU* VAL1(£V5PQR)   VAL2(00000000,000)                        COMP(EQ)
    MU* VAL1(£V5PTG)   VAL2(' ')                                 COMP(EQ)
    MU* VAL1(£V5PEX)   VAL2(' ')                                 COMP(EQ)
    MU* VAL1(£V5PRR)   VAL2(' ')                                 COMP(EQ)
    MU* VAL1(£V5P29)   VAL2(' ')                                 COMP(EQ)
    MU* VAL1(£V5PRC)   VAL2('          ')                        COMP(EQ)
    MU* VAL1(£V5PSC)   VAL2('               ')                   COMP(EQ)
    MU* VAL1(NN1)      VAL2(512)                                 COMP(EQ)
     C                   EVAL      NN1=%LEN(£V5PDS)
    MU* VAL1(DS00DS) VAL2('') COMP(EQ)
    MU* VAL1(DS0000) VAL2('') COMP(EQ)
    MU* VAL1(DS0001) VAL2('') COMP(EQ)
    MU* VAL1(DS0002) VAL2('') COMP(EQ)
    MU* VAL1(DS0003) VAL2(0) COMP(EQ)
    MU* VAL1(DS0004) VAL2(0) COMP(EQ)
    MU* VAL1(DS0005) VAL2(0) COMP(EQ)
    MU* VAL1(DS0006) VAL2('') COMP(EQ)
    MU* VAL1(DS0007) VAL2('') COMP(EQ)
    MU* VAL1(DS0008) VAL2('') COMP(EQ)
    MU* VAL1(DS0009) VAL2('') COMP(EQ)
    MU* VAL1(DS0010) VAL2(0) COMP(EQ)
    MU* VAL1(DS0011) VAL2(0) COMP(EQ)
    MU* VAL1(DS0012) VAL2(0) COMP(EQ)
    MU* VAL1(DS0013) VAL2(0) COMP(EQ)
    MU* VAL1(DS0014) VAL2(0) COMP(EQ)
    MU* VAL1(DS0015) VAL2(0) COMP(EQ)
    MU* VAL1(DS0016) VAL2(0) COMP(EQ)
    MU* VAL1(DS0017) VAL2(0) COMP(EQ)
    MU* VAL1(DS0018) VAL2(0) COMP(EQ)
    MU* VAL1(DS0019) VAL2(0) COMP(EQ)
    MU* VAL1(DS0020) VAL2(0) COMP(EQ)
    MU* VAL1(NN1)      VAL2(206)                                 COMP(EQ)
     C                   EVAL      NN1=%LEN(DS00DS)
      * Utilizzo *HIVAL e *LOVAL insieme alle annotazioni per testare il range di valori validi
    MU* VAL1(DS0004) VAL2(99999999,999) COMP(EQ)
     C                   EVAL      DS0004=*HIVAL
    MU* VAL1(DS0004) VAL2(-99999999,999) COMP(EQ)
     C                   EVAL      DS0004=*LOVAL
    MU* VAL1(DS0005) VAL2(9999999,999) COMP(EQ)
     C                   EVAL      DS0005=*HIVAL
    MU* VAL1(DS0005) VAL2(-9999999,999) COMP(EQ)
     C                   EVAL      DS0005=*LOVAL
    MU* VAL1(DS0010) VAL2(9999999999,99999) COMP(EQ)
     C                   EVAL      DS0010=*HIVAL
    MU* VAL1(DS0010) VAL2(-9999999999,99999) COMP(EQ)
     C                   EVAL      DS0010=*LOVAL
      * TODO incorrect, DS0011 is 2 bytes
    MU* VAL1(DS0011) VAL2(99) COMP(EQ)
     C                   EVAL      DS0011=*HIVAL
    MU* VAL1(DS0011) VAL2(-99) COMP(EQ)
     C                   EVAL      DS0011=*LOVAL
      * TODO incorrect, DS0012 is 2 bytes
    MU* VAL1(DS0012) VAL2(99) COMP(EQ)
     C                   EVAL      DS0012=*HIVAL
    MU* VAL1(DS0012) VAL2(-99) COMP(EQ)
     C                   EVAL      DS0012=*LOVAL
    MU* VAL1(DS0013) VAL2(127) COMP(EQ)
     C                   EVAL      DS0013=*HIVAL
    MU* VAL1(DS0013) VAL2(-128) COMP(EQ)
     C                   EVAL      DS0013=*LOVAL
    MU* VAL1(DS0014) VAL2(255) COMP(EQ)
     C                   EVAL      DS0014=*HIVAL
    MU* VAL1(DS0014) VAL2(0) COMP(EQ)
     C                   EVAL      DS0014=*LOVAL
    MU* VAL1(DS0015) VAL2(32767) COMP(EQ)
     C                   EVAL      DS0015=*HIVAL
    MU* VAL1(DS0015) VAL2(-32768) COMP(EQ)
     C                   EVAL      DS0015=*LOVAL
    MU* VAL1(DS0016) VAL2(65535) COMP(EQ)
     C                   EVAL      DS0016=*HIVAL
    MU* VAL1(DS0016) VAL2(0) COMP(EQ)
     C                   EVAL      DS0016=*LOVAL
    MU* VAL1(DS0017) VAL2(2147483647) COMP(EQ)
     C                   EVAL      DS0017=*HIVAL
    MU* VAL1(DS0017) VAL2(-2147483648) COMP(EQ)
     C                   EVAL      DS0017=*LOVAL
    MU* VAL1(DS0018) VAL2(4294967295) COMP(EQ)
     C                   EVAL      DS0018=*HIVAL
    MU* VAL1(DS0018) VAL2(0) COMP(EQ)
     C                   EVAL      DS0018=*LOVAL
      * TODO MU* VAL1(DS0019) VAL2(9223372036854775807) COMP(EQ)
      *C                   EVAL      DS0019=*HIVAL
      *MU* VAL1(DS0019) VAL2(-9223372036854775808) COMP(EQ)
      *C                   EVAL      DS0019=*LOVAL
      * TODO This number is too big
      * MU* VAL1(DS0020) VAL2(18446744073709551615) COMP(EQ)
      * C                   EVAL      DS0020=*HIVAL
      *MU* VAL1(DS0020) VAL2(0) COMP(EQ)
      *C                   EVAL      DS0020=*LOVAL
    MU* VAL1(DS01DS) VAL2('') COMP(EQ)
    MU* VAL1(DS01DS.DS0000) VAL2('') COMP(EQ)
    MU* VAL1(DS01DS.DS0001) VAL2('') COMP(EQ)
    MU* VAL1(DS01DS.DS0002) VAL2('') COMP(EQ)
    MU* VAL1(DS01DS.DS0003) VAL2(0) COMP(NE)
    MU* VAL1(DS01DS.DS0004) VAL2(0) COMP(NE)
    MU* VAL1(DS01DS.DS0005) VAL2(0) COMP(NE)
    MU* VAL1(DS01DS.DS0006) VAL2('') COMP(EQ)
    MU* VAL1(DS01DS.DS0007) VAL2('') COMP(EQ)
    MU* VAL1(DS01DS.DS0008) VAL2('') COMP(EQ)
    MU* VAL1(DS01DS.DS0009) VAL2('') COMP(EQ)
    MU* VAL1(DS01DS.DS0010) VAL2(0) COMP(NE)
    MU* VAL1(DS01DS.DS0011) VAL2(0) COMP(NE)
    MU* VAL1(DS01DS.DS0012) VAL2(0) COMP(NE)
    MU* VAL1(DS01DS.DS0013) VAL2(0) COMP(NE)
    MU* VAL1(DS01DS.DS0014) VAL2(0) COMP(NE)
    MU* VAL1(DS01DS.DS0015) VAL2(0) COMP(NE)
    MU* VAL1(DS01DS.DS0016) VAL2(0) COMP(NE)
    MU* VAL1(DS01DS.DS0017) VAL2(0) COMP(NE)
    MU* VAL1(DS01DS.DS0018) VAL2(0) COMP(NE)
    MU* VAL1(DS01DS.DS0019) VAL2(0) COMP(NE)
    MU* VAL1(DS01DS.DS0020) VAL2(0) COMP(NE)
    MU* VAL1(NN1)      VAL2(206)                                 COMP(EQ)
     C                   EVAL      NN1=%LEN(DS01DS)
      *
    MU* VAL1(NN1)      VAL2(7095)                                COMP(EQ)
     C                   EVAL      NN1=%LEN(£40FDS)
    MU* VAL1(NN1)      VAL2(12)                               COMP(EQ)
     C                   EVAL      NN1=%LEN(£40FTO(1))
      *
     C                   SETON                                        LR
