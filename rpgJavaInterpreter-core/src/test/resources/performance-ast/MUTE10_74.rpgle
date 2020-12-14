     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 14/12/20  V5R1    BENMAR Creato
     V*=====================================================================
      * This is UIBDS
     D £UIBDS          DS         31000
     D  £UIBTM                       10
     D  £UIBMS                       10
     D  £UIBPG                       10
     D  £UIBFU                       10
     D  £UIBME                       10
      *
     D  £UIBT1                        2
     D  £UIBP1                       10
     D  £UIBK1                       15
     D  £UIBT2                        2
     D  £UIBP2                       10
     D  £UIBK2                       15
     D  £UIBT3                        2
     D  £UIBP3                       10
     D  £UIBK3                       15
      *
     D  £UIBPA                      256
      *
     D  £UIB35                        1
     D  £UIB36                        1
     D  £UIBCM                        7
     D  £UIBFM                       10
     D  £UIBSC                       10
      *
     D  £UIBT4                        2
     D  £UIBP4                       10
     D  £UIBK4                       15
     D  £UIBT5                        2
     D  £UIBP5                       10
     D  £UIBK5                       15
     D  £UIBT6                        2
     D  £UIBP6                       10
     D  £UIBK6                       15
      * Campo server LoocUp di 15 caratteri
     D  £UIBSR                       15
      * Campo client LoocUp di 15 caratteri
     D  £UIBCL                       15
      * Setup Richiesto
     D  £UIBRI                       58
      * Setup Grafico
     D  £UIBSG                        4
      * Setup del Setup
     D  £UIBSS                      256
      * Numero di sequenza
     D  £UIBSQ                        3
      *
     D  £UIBD1              1001  31000
      *
      *
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
     D  £V5PD2               438    445
     D* posizioni libere     446    453  da recupero £V5PD2 mappata erroneamente a seguire
     D  £V5PPE               454    464P 6
     D  £V5PQR               465    470P 3
     D  £V5PTG               471    471
     D  £V5PEX               472    472
     D  £V5PRR               473    473
     D  £V5P29               474    474
     D  £V5PRC               475    484
     D  £V5PSC               485    499
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
      * DS with OVERLAY
     D SUPP_DS         DS
     D SUPP                         130    DIM(9999)
     D  SUPP_TIP                     15    OVERLAY(SUPP:01)
     D  SUPP_COD                     15    OVERLAY(SUPP:*NEXT)
     D  SUPP_DES                     50    OVERLAY(SUPP:*NEXT)
     D  SUPP_NUU                      5  0 OVERLAY(SUPP:*NEXT) INZ
     D  SUPP_NUG                      5  0 OVERLAY(SUPP:*NEXT) INZ
     D  SUPP_NUR                      5  0 OVERLAY(SUPP:*NEXT) INZ
     D  SUPP_ELB                      1    OVERLAY(SUPP:*NEXT)
     D  SUPP_ERL                      1    OVERLAY(SUPP:*NEXT)
     D  SUPP_NRI                      7  0 OVERLAY(SUPP:*NEXT) INZ
     D  SUPP_NRF                      7  0 OVERLAY(SUPP:*NEXT) INZ
     D  SUPP_I_F                      7  0 OVERLAY(SUPP:*NEXT) INZ
     D  SUPP_F_F                      7  0 OVERLAY(SUPP:*NEXT) INZ
     D U000001         DS                  LIKEDS(£UIBDS)
     D P000001         DS                  LIKEDS(£V5PDS)
     D F000001         DS                  LIKEDS(£40FDS)
     D D000001         DS                  LIKEDS(SUPP_DS)
     D U000002         DS                  LIKEDS(£UIBDS)
     D P000002         DS                  LIKEDS(£V5PDS)
     D F000002         DS                  LIKEDS(£40FDS)
     D D000002         DS                  LIKEDS(SUPP_DS)
     D U000003         DS                  LIKEDS(£UIBDS)
     D P000003         DS                  LIKEDS(£V5PDS)
     D F000003         DS                  LIKEDS(£40FDS)
     D D000003         DS                  LIKEDS(SUPP_DS)
     D U000004         DS                  LIKEDS(£UIBDS)
     D P000004         DS                  LIKEDS(£V5PDS)
     D F000004         DS                  LIKEDS(£40FDS)
     D D000004         DS                  LIKEDS(SUPP_DS)
     D U000005         DS                  LIKEDS(£UIBDS)
     D P000005         DS                  LIKEDS(£V5PDS)
     D F000005         DS                  LIKEDS(£40FDS)
     D D000005         DS                  LIKEDS(SUPP_DS)
     D U000006         DS                  LIKEDS(£UIBDS)
     D P000006         DS                  LIKEDS(£V5PDS)
     D F000006         DS                  LIKEDS(£40FDS)
     D D000006         DS                  LIKEDS(SUPP_DS)
     D U000007         DS                  LIKEDS(£UIBDS)
     D P000007         DS                  LIKEDS(£V5PDS)
     D F000007         DS                  LIKEDS(£40FDS)
     D D000007         DS                  LIKEDS(SUPP_DS)
     D U000008         DS                  LIKEDS(£UIBDS)
     D P000008         DS                  LIKEDS(£V5PDS)
     D F000008         DS                  LIKEDS(£40FDS)
     D D000008         DS                  LIKEDS(SUPP_DS)
     D U000009         DS                  LIKEDS(£UIBDS)
     D P000009         DS                  LIKEDS(£V5PDS)
     D F000009         DS                  LIKEDS(£40FDS)
     D D000009         DS                  LIKEDS(SUPP_DS)
     D U000010         DS                  LIKEDS(£UIBDS)
     D P000010         DS                  LIKEDS(£V5PDS)
     D F000010         DS                  LIKEDS(£40FDS)
     D D000010         DS                  LIKEDS(SUPP_DS)
     D U000011         DS                  LIKEDS(£UIBDS)
     D P000011         DS                  LIKEDS(£V5PDS)
     D F000011         DS                  LIKEDS(£40FDS)
     D D000011         DS                  LIKEDS(SUPP_DS)
     D U000012         DS                  LIKEDS(£UIBDS)
     D P000012         DS                  LIKEDS(£V5PDS)
     D F000012         DS                  LIKEDS(£40FDS)
     D D000012         DS                  LIKEDS(SUPP_DS)
     D U000013         DS                  LIKEDS(£UIBDS)
     D P000013         DS                  LIKEDS(£V5PDS)
     D F000013         DS                  LIKEDS(£40FDS)
     D D000013         DS                  LIKEDS(SUPP_DS)
     D U000014         DS                  LIKEDS(£UIBDS)
     D P000014         DS                  LIKEDS(£V5PDS)
     D F000014         DS                  LIKEDS(£40FDS)
     D D000014         DS                  LIKEDS(SUPP_DS)
     D U000015         DS                  LIKEDS(£UIBDS)
     D P000015         DS                  LIKEDS(£V5PDS)
     D F000015         DS                  LIKEDS(£40FDS)
     D D000015         DS                  LIKEDS(SUPP_DS)
     D U000016         DS                  LIKEDS(£UIBDS)
     D P000016         DS                  LIKEDS(£V5PDS)
     D F000016         DS                  LIKEDS(£40FDS)
     D D000016         DS                  LIKEDS(SUPP_DS)
     D U000017         DS                  LIKEDS(£UIBDS)
     D P000017         DS                  LIKEDS(£V5PDS)
     D F000017         DS                  LIKEDS(£40FDS)
     D D000017         DS                  LIKEDS(SUPP_DS)
     D U000018         DS                  LIKEDS(£UIBDS)
     D P000018         DS                  LIKEDS(£V5PDS)
     D F000018         DS                  LIKEDS(£40FDS)
     D D000018         DS                  LIKEDS(SUPP_DS)
     D U000019         DS                  LIKEDS(£UIBDS)
     D P000019         DS                  LIKEDS(£V5PDS)
     D F000019         DS                  LIKEDS(£40FDS)
     D D000019         DS                  LIKEDS(SUPP_DS)
     D U000020         DS                  LIKEDS(£UIBDS)
     D P000020         DS                  LIKEDS(£V5PDS)
     D F000020         DS                  LIKEDS(£40FDS)
     D D000020         DS                  LIKEDS(SUPP_DS)
     D U000021         DS                  LIKEDS(£UIBDS)
     D P000021         DS                  LIKEDS(£V5PDS)
     D F000021         DS                  LIKEDS(£40FDS)
     D D000021         DS                  LIKEDS(SUPP_DS)
     D U000022         DS                  LIKEDS(£UIBDS)
     D P000022         DS                  LIKEDS(£V5PDS)
     D F000022         DS                  LIKEDS(£40FDS)
     D D000022         DS                  LIKEDS(SUPP_DS)
     D U000023         DS                  LIKEDS(£UIBDS)
     D P000023         DS                  LIKEDS(£V5PDS)
     D F000023         DS                  LIKEDS(£40FDS)
     D D000023         DS                  LIKEDS(SUPP_DS)
     D U000024         DS                  LIKEDS(£UIBDS)
     D P000024         DS                  LIKEDS(£V5PDS)
     D F000024         DS                  LIKEDS(£40FDS)
     D D000024         DS                  LIKEDS(SUPP_DS)
     D U000025         DS                  LIKEDS(£UIBDS)
     D P000025         DS                  LIKEDS(£V5PDS)
     D F000025         DS                  LIKEDS(£40FDS)
     D D000025         DS                  LIKEDS(SUPP_DS)
     D U000026         DS                  LIKEDS(£UIBDS)
     D P000026         DS                  LIKEDS(£V5PDS)
     D F000026         DS                  LIKEDS(£40FDS)
     D D000026         DS                  LIKEDS(SUPP_DS)
     D U000027         DS                  LIKEDS(£UIBDS)
     D P000027         DS                  LIKEDS(£V5PDS)
     D F000027         DS                  LIKEDS(£40FDS)
     D D000027         DS                  LIKEDS(SUPP_DS)
     D U000028         DS                  LIKEDS(£UIBDS)
     D P000028         DS                  LIKEDS(£V5PDS)
     D F000028         DS                  LIKEDS(£40FDS)
     D D000028         DS                  LIKEDS(SUPP_DS)
     D U000029         DS                  LIKEDS(£UIBDS)
     D P000029         DS                  LIKEDS(£V5PDS)
     D F000029         DS                  LIKEDS(£40FDS)
     D D000029         DS                  LIKEDS(SUPP_DS)
     D U000030         DS                  LIKEDS(£UIBDS)
     D P000030         DS                  LIKEDS(£V5PDS)
     D F000030         DS                  LIKEDS(£40FDS)
     D D000030         DS                  LIKEDS(SUPP_DS)
     D U000031         DS                  LIKEDS(£UIBDS)
     D P000031         DS                  LIKEDS(£V5PDS)
     D F000031         DS                  LIKEDS(£40FDS)
     D D000031         DS                  LIKEDS(SUPP_DS)
     D U000032         DS                  LIKEDS(£UIBDS)
     D P000032         DS                  LIKEDS(£V5PDS)
     D F000032         DS                  LIKEDS(£40FDS)
     D D000032         DS                  LIKEDS(SUPP_DS)
     D U000033         DS                  LIKEDS(£UIBDS)
     D P000033         DS                  LIKEDS(£V5PDS)
     D F000033         DS                  LIKEDS(£40FDS)
     D D000033         DS                  LIKEDS(SUPP_DS)
     D U000034         DS                  LIKEDS(£UIBDS)
     D P000034         DS                  LIKEDS(£V5PDS)
     D F000034         DS                  LIKEDS(£40FDS)
     D D000034         DS                  LIKEDS(SUPP_DS)
     D U000035         DS                  LIKEDS(£UIBDS)
     D P000035         DS                  LIKEDS(£V5PDS)
     D F000035         DS                  LIKEDS(£40FDS)
     D D000035         DS                  LIKEDS(SUPP_DS)
     D U000036         DS                  LIKEDS(£UIBDS)
     D P000036         DS                  LIKEDS(£V5PDS)
     D F000036         DS                  LIKEDS(£40FDS)
     D D000036         DS                  LIKEDS(SUPP_DS)
     D U000037         DS                  LIKEDS(£UIBDS)
     D P000037         DS                  LIKEDS(£V5PDS)
     D F000037         DS                  LIKEDS(£40FDS)
     D D000037         DS                  LIKEDS(SUPP_DS)
     D U000038         DS                  LIKEDS(£UIBDS)
     D P000038         DS                  LIKEDS(£V5PDS)
     D F000038         DS                  LIKEDS(£40FDS)
     D D000038         DS                  LIKEDS(SUPP_DS)
     D U000039         DS                  LIKEDS(£UIBDS)
     D P000039         DS                  LIKEDS(£V5PDS)
     D F000039         DS                  LIKEDS(£40FDS)
     D D000039         DS                  LIKEDS(SUPP_DS)
     D U000040         DS                  LIKEDS(£UIBDS)
     D P000040         DS                  LIKEDS(£V5PDS)
     D F000040         DS                  LIKEDS(£40FDS)
     D D000040         DS                  LIKEDS(SUPP_DS)
     D U000041         DS                  LIKEDS(£UIBDS)
     D P000041         DS                  LIKEDS(£V5PDS)
     D F000041         DS                  LIKEDS(£40FDS)
     D D000041         DS                  LIKEDS(SUPP_DS)
     D U000042         DS                  LIKEDS(£UIBDS)
     D P000042         DS                  LIKEDS(£V5PDS)
     D F000042         DS                  LIKEDS(£40FDS)
     D D000042         DS                  LIKEDS(SUPP_DS)
     D U000043         DS                  LIKEDS(£UIBDS)
     D P000043         DS                  LIKEDS(£V5PDS)
     D F000043         DS                  LIKEDS(£40FDS)
     D D000043         DS                  LIKEDS(SUPP_DS)
     D U000044         DS                  LIKEDS(£UIBDS)
     D P000044         DS                  LIKEDS(£V5PDS)
     D F000044         DS                  LIKEDS(£40FDS)
     D D000044         DS                  LIKEDS(SUPP_DS)
     D U000045         DS                  LIKEDS(£UIBDS)
     D P000045         DS                  LIKEDS(£V5PDS)
     D F000045         DS                  LIKEDS(£40FDS)
     D D000045         DS                  LIKEDS(SUPP_DS)
     D U000046         DS                  LIKEDS(£UIBDS)
     D P000046         DS                  LIKEDS(£V5PDS)
     D F000046         DS                  LIKEDS(£40FDS)
     D D000046         DS                  LIKEDS(SUPP_DS)
     D U000047         DS                  LIKEDS(£UIBDS)
     D P000047         DS                  LIKEDS(£V5PDS)
     D F000047         DS                  LIKEDS(£40FDS)
     D D000047         DS                  LIKEDS(SUPP_DS)
     D U000048         DS                  LIKEDS(£UIBDS)
     D P000048         DS                  LIKEDS(£V5PDS)
     D F000048         DS                  LIKEDS(£40FDS)
     D D000048         DS                  LIKEDS(SUPP_DS)
     D U000049         DS                  LIKEDS(£UIBDS)
     D P000049         DS                  LIKEDS(£V5PDS)
     D F000049         DS                  LIKEDS(£40FDS)
     D D000049         DS                  LIKEDS(SUPP_DS)
     D U000050         DS                  LIKEDS(£UIBDS)
     D P000050         DS                  LIKEDS(£V5PDS)
     D F000050         DS                  LIKEDS(£40FDS)
     D D000050         DS                  LIKEDS(SUPP_DS)
     D U000051         DS                  LIKEDS(£UIBDS)
     D P000051         DS                  LIKEDS(£V5PDS)
     D F000051         DS                  LIKEDS(£40FDS)
     D D000051         DS                  LIKEDS(SUPP_DS)
     D U000052         DS                  LIKEDS(£UIBDS)
     D P000052         DS                  LIKEDS(£V5PDS)
     D F000052         DS                  LIKEDS(£40FDS)
     D D000052         DS                  LIKEDS(SUPP_DS)
     D U000053         DS                  LIKEDS(£UIBDS)
     D P000053         DS                  LIKEDS(£V5PDS)
     D F000053         DS                  LIKEDS(£40FDS)
     D D000053         DS                  LIKEDS(SUPP_DS)
     D U000054         DS                  LIKEDS(£UIBDS)
     D P000054         DS                  LIKEDS(£V5PDS)
     D F000054         DS                  LIKEDS(£40FDS)
     D D000054         DS                  LIKEDS(SUPP_DS)
     D U000055         DS                  LIKEDS(£UIBDS)
     D P000055         DS                  LIKEDS(£V5PDS)
     D F000055         DS                  LIKEDS(£40FDS)
     D D000055         DS                  LIKEDS(SUPP_DS)
     D U000056         DS                  LIKEDS(£UIBDS)
     D P000056         DS                  LIKEDS(£V5PDS)
     D F000056         DS                  LIKEDS(£40FDS)
     D D000056         DS                  LIKEDS(SUPP_DS)
     D U000057         DS                  LIKEDS(£UIBDS)
     D P000057         DS                  LIKEDS(£V5PDS)
     D F000057         DS                  LIKEDS(£40FDS)
     D D000057         DS                  LIKEDS(SUPP_DS)
     D U000058         DS                  LIKEDS(£UIBDS)
     D P000058         DS                  LIKEDS(£V5PDS)
     D F000058         DS                  LIKEDS(£40FDS)
     D D000058         DS                  LIKEDS(SUPP_DS)
     D U000059         DS                  LIKEDS(£UIBDS)
     D P000059         DS                  LIKEDS(£V5PDS)
     D F000059         DS                  LIKEDS(£40FDS)
     D D000059         DS                  LIKEDS(SUPP_DS)
     D U000060         DS                  LIKEDS(£UIBDS)
     D P000060         DS                  LIKEDS(£V5PDS)
     D F000060         DS                  LIKEDS(£40FDS)
     D D000060         DS                  LIKEDS(SUPP_DS)
     D U000061         DS                  LIKEDS(£UIBDS)
     D P000061         DS                  LIKEDS(£V5PDS)
     D F000061         DS                  LIKEDS(£40FDS)
     D D000061         DS                  LIKEDS(SUPP_DS)
     D U000062         DS                  LIKEDS(£UIBDS)
     D P000062         DS                  LIKEDS(£V5PDS)
     D F000062         DS                  LIKEDS(£40FDS)
     D D000062         DS                  LIKEDS(SUPP_DS)
     D U000063         DS                  LIKEDS(£UIBDS)
     D P000063         DS                  LIKEDS(£V5PDS)
     D F000063         DS                  LIKEDS(£40FDS)
     D D000063         DS                  LIKEDS(SUPP_DS)
     D U000064         DS                  LIKEDS(£UIBDS)
     D P000064         DS                  LIKEDS(£V5PDS)
     D F000064         DS                  LIKEDS(£40FDS)
     D D000064         DS                  LIKEDS(SUPP_DS)
     D U000065         DS                  LIKEDS(£UIBDS)
     D P000065         DS                  LIKEDS(£V5PDS)
     D F000065         DS                  LIKEDS(£40FDS)
     D D000065         DS                  LIKEDS(SUPP_DS)
     D U000066         DS                  LIKEDS(£UIBDS)
     D P000066         DS                  LIKEDS(£V5PDS)
     D F000066         DS                  LIKEDS(£40FDS)
     D D000066         DS                  LIKEDS(SUPP_DS)
     D U000067         DS                  LIKEDS(£UIBDS)
     D P000067         DS                  LIKEDS(£V5PDS)
     D F000067         DS                  LIKEDS(£40FDS)
     D D000067         DS                  LIKEDS(SUPP_DS)
     D U000068         DS                  LIKEDS(£UIBDS)
     D P000068         DS                  LIKEDS(£V5PDS)
     D F000068         DS                  LIKEDS(£40FDS)
     D D000068         DS                  LIKEDS(SUPP_DS)
     D U000069         DS                  LIKEDS(£UIBDS)
     D P000069         DS                  LIKEDS(£V5PDS)
     D F000069         DS                  LIKEDS(£40FDS)
     D D000069         DS                  LIKEDS(SUPP_DS)
     D U000070         DS                  LIKEDS(£UIBDS)
     D P000070         DS                  LIKEDS(£V5PDS)
     D F000070         DS                  LIKEDS(£40FDS)
     D D000070         DS                  LIKEDS(SUPP_DS)
     D U000071         DS                  LIKEDS(£UIBDS)
     D P000071         DS                  LIKEDS(£V5PDS)
     D F000071         DS                  LIKEDS(£40FDS)
     D D000071         DS                  LIKEDS(SUPP_DS)
     D U000072         DS                  LIKEDS(£UIBDS)
     D P000072         DS                  LIKEDS(£V5PDS)
     D F000072         DS                  LIKEDS(£40FDS)
     D D000072         DS                  LIKEDS(SUPP_DS)
     D U000073         DS                  LIKEDS(£UIBDS)
     D P000073         DS                  LIKEDS(£V5PDS)
     D F000073         DS                  LIKEDS(£40FDS)
     D D000073         DS                  LIKEDS(SUPP_DS)
     D U000074         DS                  LIKEDS(£UIBDS)
     D P000074         DS                  LIKEDS(£V5PDS)
     D F000074         DS                  LIKEDS(£40FDS)
     D D000074         DS                  LIKEDS(SUPP_DS)
     D U000075         DS                  LIKEDS(£UIBDS)
     D P000075         DS                  LIKEDS(£V5PDS)
     D F000075         DS                  LIKEDS(£40FDS)
     D D000075         DS                  LIKEDS(SUPP_DS)
     D U000076         DS                  LIKEDS(£UIBDS)
     D P000076         DS                  LIKEDS(£V5PDS)
     D F000076         DS                  LIKEDS(£40FDS)
     D D000076         DS                  LIKEDS(SUPP_DS)
     D U000077         DS                  LIKEDS(£UIBDS)
     D P000077         DS                  LIKEDS(£V5PDS)
     D F000077         DS                  LIKEDS(£40FDS)
     D D000077         DS                  LIKEDS(SUPP_DS)
     D U000078         DS                  LIKEDS(£UIBDS)
     D P000078         DS                  LIKEDS(£V5PDS)
     D F000078         DS                  LIKEDS(£40FDS)
     D D000078         DS                  LIKEDS(SUPP_DS)
     D U000079         DS                  LIKEDS(£UIBDS)
     D P000079         DS                  LIKEDS(£V5PDS)
     D F000079         DS                  LIKEDS(£40FDS)
     D D000079         DS                  LIKEDS(SUPP_DS)
     D U000080         DS                  LIKEDS(£UIBDS)
     D P000080         DS                  LIKEDS(£V5PDS)
     D F000080         DS                  LIKEDS(£40FDS)
     D D000080         DS                  LIKEDS(SUPP_DS)
     D U000081         DS                  LIKEDS(£UIBDS)
     D P000081         DS                  LIKEDS(£V5PDS)
     D F000081         DS                  LIKEDS(£40FDS)
     D D000081         DS                  LIKEDS(SUPP_DS)
     D U000082         DS                  LIKEDS(£UIBDS)
     D P000082         DS                  LIKEDS(£V5PDS)
     D F000082         DS                  LIKEDS(£40FDS)
     D D000082         DS                  LIKEDS(SUPP_DS)
     D U000083         DS                  LIKEDS(£UIBDS)
     D P000083         DS                  LIKEDS(£V5PDS)
     D F000083         DS                  LIKEDS(£40FDS)
     D D000083         DS                  LIKEDS(SUPP_DS)
     D U000084         DS                  LIKEDS(£UIBDS)
     D P000084         DS                  LIKEDS(£V5PDS)
     D F000084         DS                  LIKEDS(£40FDS)
     D D000084         DS                  LIKEDS(SUPP_DS)
     D U000085         DS                  LIKEDS(£UIBDS)
     D P000085         DS                  LIKEDS(£V5PDS)
     D F000085         DS                  LIKEDS(£40FDS)
     D D000085         DS                  LIKEDS(SUPP_DS)
     D U000086         DS                  LIKEDS(£UIBDS)
     D P000086         DS                  LIKEDS(£V5PDS)
     D F000086         DS                  LIKEDS(£40FDS)
     D D000086         DS                  LIKEDS(SUPP_DS)
     D U000087         DS                  LIKEDS(£UIBDS)
     D P000087         DS                  LIKEDS(£V5PDS)
     D F000087         DS                  LIKEDS(£40FDS)
     D D000087         DS                  LIKEDS(SUPP_DS)
     D U000088         DS                  LIKEDS(£UIBDS)
     D P000088         DS                  LIKEDS(£V5PDS)
     D F000088         DS                  LIKEDS(£40FDS)
     D D000088         DS                  LIKEDS(SUPP_DS)
     D U000089         DS                  LIKEDS(£UIBDS)
     D P000089         DS                  LIKEDS(£V5PDS)
     D F000089         DS                  LIKEDS(£40FDS)
     D D000089         DS                  LIKEDS(SUPP_DS)
     D U000090         DS                  LIKEDS(£UIBDS)
     D P000090         DS                  LIKEDS(£V5PDS)
     D F000090         DS                  LIKEDS(£40FDS)
     D D000090         DS                  LIKEDS(SUPP_DS)
     D U000091         DS                  LIKEDS(£UIBDS)
     D P000091         DS                  LIKEDS(£V5PDS)
     D F000091         DS                  LIKEDS(£40FDS)
     D D000091         DS                  LIKEDS(SUPP_DS)
     D U000092         DS                  LIKEDS(£UIBDS)
     D P000092         DS                  LIKEDS(£V5PDS)
     D F000092         DS                  LIKEDS(£40FDS)
     D D000092         DS                  LIKEDS(SUPP_DS)
     D U000093         DS                  LIKEDS(£UIBDS)
     D P000093         DS                  LIKEDS(£V5PDS)
     D F000093         DS                  LIKEDS(£40FDS)
     D D000093         DS                  LIKEDS(SUPP_DS)
     D U000094         DS                  LIKEDS(£UIBDS)
     D P000094         DS                  LIKEDS(£V5PDS)
     D F000094         DS                  LIKEDS(£40FDS)
     D D000094         DS                  LIKEDS(SUPP_DS)
     D U000095         DS                  LIKEDS(£UIBDS)
     D P000095         DS                  LIKEDS(£V5PDS)
     D F000095         DS                  LIKEDS(£40FDS)
     D D000095         DS                  LIKEDS(SUPP_DS)
     D U000096         DS                  LIKEDS(£UIBDS)
     D P000096         DS                  LIKEDS(£V5PDS)
     D F000096         DS                  LIKEDS(£40FDS)
     D D000096         DS                  LIKEDS(SUPP_DS)
     D U000097         DS                  LIKEDS(£UIBDS)
     D P000097         DS                  LIKEDS(£V5PDS)
     D F000097         DS                  LIKEDS(£40FDS)
     D D000097         DS                  LIKEDS(SUPP_DS)
     D U000098         DS                  LIKEDS(£UIBDS)
     D P000098         DS                  LIKEDS(£V5PDS)
     D F000098         DS                  LIKEDS(£40FDS)
     D D000098         DS                  LIKEDS(SUPP_DS)
     D U000099         DS                  LIKEDS(£UIBDS)
     D P000099         DS                  LIKEDS(£V5PDS)
     D F000099         DS                  LIKEDS(£40FDS)
     D D000099         DS                  LIKEDS(SUPP_DS)
     D U000100         DS                  LIKEDS(£UIBDS)
     D P000100         DS                  LIKEDS(£V5PDS)
     D F000100         DS                  LIKEDS(£40FDS)
     D D000100         DS                  LIKEDS(SUPP_DS)
      *---------------------------------------------------------------
    RD* Main
      *--------------------------------------------------------------*
      * This is main
     C                   SETON                                        LR
      *---------------------------------------------------------------
    RD* Initial subroutine
      *--------------------------------------------------------------*
     C     *INZSR        BEGSR
      *
      *
     C                   ENDSR
