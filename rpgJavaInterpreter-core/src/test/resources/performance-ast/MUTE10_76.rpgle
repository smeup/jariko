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
      * Routine R000001
     C                   EXSR      R000001
      * Routine R000002
     C                   EXSR      R000002
      * Routine R000003
     C                   EXSR      R000003
      * Routine R000004
     C                   EXSR      R000004
      * Routine R000005
     C                   EXSR      R000005
      * Routine R000006
     C                   EXSR      R000006
      * Routine R000007
     C                   EXSR      R000007
      * Routine R000008
     C                   EXSR      R000008
      * Routine R000009
     C                   EXSR      R000009
      * Routine R000010
     C                   EXSR      R000010
      * Routine R000011
     C                   EXSR      R000011
      * Routine R000012
     C                   EXSR      R000012
      * Routine R000013
     C                   EXSR      R000013
      * Routine R000014
     C                   EXSR      R000014
      * Routine R000015
     C                   EXSR      R000015
      * Routine R000016
     C                   EXSR      R000016
      * Routine R000017
     C                   EXSR      R000017
      * Routine R000018
     C                   EXSR      R000018
      * Routine R000019
     C                   EXSR      R000019
      * Routine R000020
     C                   EXSR      R000020
      * Routine R000021
     C                   EXSR      R000021
      * Routine R000022
     C                   EXSR      R000022
      * Routine R000023
     C                   EXSR      R000023
      * Routine R000024
     C                   EXSR      R000024
      * Routine R000025
     C                   EXSR      R000025
      * Routine R000026
     C                   EXSR      R000026
      * Routine R000027
     C                   EXSR      R000027
      * Routine R000028
     C                   EXSR      R000028
      * Routine R000029
     C                   EXSR      R000029
      * Routine R000030
     C                   EXSR      R000030
      * Routine R000031
     C                   EXSR      R000031
      * Routine R000032
     C                   EXSR      R000032
      * Routine R000033
     C                   EXSR      R000033
      * Routine R000034
     C                   EXSR      R000034
      * Routine R000035
     C                   EXSR      R000035
      * Routine R000036
     C                   EXSR      R000036
      * Routine R000037
     C                   EXSR      R000037
      * Routine R000038
     C                   EXSR      R000038
      * Routine R000039
     C                   EXSR      R000039
      * Routine R000040
     C                   EXSR      R000040
      * Routine R000041
     C                   EXSR      R000041
      * Routine R000042
     C                   EXSR      R000042
      * Routine R000043
     C                   EXSR      R000043
      * Routine R000044
     C                   EXSR      R000044
      * Routine R000045
     C                   EXSR      R000045
      * Routine R000046
     C                   EXSR      R000046
      * Routine R000047
     C                   EXSR      R000047
      * Routine R000048
     C                   EXSR      R000048
      * Routine R000049
     C                   EXSR      R000049
      * Routine R000050
     C                   EXSR      R000050
      * Routine R000051
     C                   EXSR      R000051
      * Routine R000052
     C                   EXSR      R000052
      * Routine R000053
     C                   EXSR      R000053
      * Routine R000054
     C                   EXSR      R000054
      * Routine R000055
     C                   EXSR      R000055
      * Routine R000056
     C                   EXSR      R000056
      * Routine R000057
     C                   EXSR      R000057
      * Routine R000058
     C                   EXSR      R000058
      * Routine R000059
     C                   EXSR      R000059
      * Routine R000060
     C                   EXSR      R000060
      * Routine R000061
     C                   EXSR      R000061
      * Routine R000062
     C                   EXSR      R000062
      * Routine R000063
     C                   EXSR      R000063
      * Routine R000064
     C                   EXSR      R000064
      * Routine R000065
     C                   EXSR      R000065
      * Routine R000066
     C                   EXSR      R000066
      * Routine R000067
     C                   EXSR      R000067
      * Routine R000068
     C                   EXSR      R000068
      * Routine R000069
     C                   EXSR      R000069
      * Routine R000070
     C                   EXSR      R000070
      * Routine R000071
     C                   EXSR      R000071
      * Routine R000072
     C                   EXSR      R000072
      * Routine R000073
     C                   EXSR      R000073
      * Routine R000074
     C                   EXSR      R000074
      * Routine R000075
     C                   EXSR      R000075
      * Routine R000076
     C                   EXSR      R000076
      * Routine R000077
     C                   EXSR      R000077
      * Routine R000078
     C                   EXSR      R000078
      * Routine R000079
     C                   EXSR      R000079
      * Routine R000080
     C                   EXSR      R000080
      * Routine R000081
     C                   EXSR      R000081
      * Routine R000082
     C                   EXSR      R000082
      * Routine R000083
     C                   EXSR      R000083
      * Routine R000084
     C                   EXSR      R000084
      * Routine R000085
     C                   EXSR      R000085
      * Routine R000086
     C                   EXSR      R000086
      * Routine R000087
     C                   EXSR      R000087
      * Routine R000088
     C                   EXSR      R000088
      * Routine R000089
     C                   EXSR      R000089
      * Routine R000090
     C                   EXSR      R000090
      * Routine R000091
     C                   EXSR      R000091
      * Routine R000092
     C                   EXSR      R000092
      * Routine R000093
     C                   EXSR      R000093
      * Routine R000094
     C                   EXSR      R000094
      * Routine R000095
     C                   EXSR      R000095
      * Routine R000096
     C                   EXSR      R000096
      * Routine R000097
     C                   EXSR      R000097
      * Routine R000098
     C                   EXSR      R000098
      * Routine R000099
     C                   EXSR      R000099
      * Routine R000100
     C                   EXSR      R000100
     C                   SETON                                        LR
      *---------------------------------------------------------------
    RD* Initial subroutine
      *--------------------------------------------------------------*
     C     *INZSR        BEGSR
      *
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000001
      *--------------------------------------------------------------*
     C     R000001       BEGSR
      * This is Subroutine  R000001
     C                   CLEAR                   U000001
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000001.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000001.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000001.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000001.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000001.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000001.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000001.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000001.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000001.£UIBK1
      *
     C                   CLEAR                   P000001
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000001.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000001.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000001.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000001.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000001.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000001.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000001.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000001.£V5PNR= 5
    MU* VAL1(P000001.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000001.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000001.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000001.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000001.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000001.£V5PQT=130,425
    MU* VAL1(P000001.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000001.£V5PTD
    MU* VAL1(P000001.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000001.£V5PMO
    MU* VAL1(P000001.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000001.£V5PND
    MU* VAL1(P000001.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000001.£V5PNR
    MU* VAL1(P000001.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000001.£V5PTC
    MU* VAL1(P000001.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000001.£V5PEN
    MU* VAL1(P000001.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000001.£V5PQT
      *
     C                   EVAL      F000001.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000001.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000001.£40FLU='03'
     C                   EVAL      F000001.£40F_MSO='1'
     C                   EVAL      F000001.£40F_MWL=' '
     C                   EVAL      F000001.£40F_MWC='1'
     C                   EVAL      F000001.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000001.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000001.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000001.£40FDE
     C                   EVAL      £40FRE=F000001.£40FRE
     C                   EVAL      £40FLU=F000001.£40FLU
     C                   EVAL      £40F_MSO=F000001.£40F_MSO
     C                   EVAL      £40F_MWL=F000001.£40F_MWL
     C                   EVAL      £40F_MWC=F000001.£40F_MWC
     C                   EVAL      £40FTO(1)=F000001.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000001.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000001.£40FTO(3)
      *
     C                   EVAL      D000001.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000001.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000001.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000001.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000001.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000001.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000001.SUPP_NUU(1)= 1
     C                   EVAL      D000001.SUPP_NUU(2)= 2
     C                   EVAL      D000001.SUPP_NUU(3)= 3
     C                   EVAL      D000001.SUPP_NUG(1)= 11
     C                   EVAL      D000001.SUPP_NUG(2)= 21
     C                   EVAL      D000001.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000001.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000001.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000001.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000001.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000001.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000001.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000001.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000001.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000001.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000001.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000001.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000001.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000002
      *--------------------------------------------------------------*
     C     R000002       BEGSR
      * This is Subroutine  R000002
     C                   CLEAR                   U000002
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000002.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000002.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000002.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000002.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000002.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000002.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000002.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000002.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000002.£UIBK1
      *
     C                   CLEAR                   P000002
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000002.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000002.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000002.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000002.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000002.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000002.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000002.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000002.£V5PNR= 5
    MU* VAL1(P000002.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000002.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000002.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000002.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000002.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000002.£V5PQT=130,425
    MU* VAL1(P000002.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000002.£V5PTD
    MU* VAL1(P000002.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000002.£V5PMO
    MU* VAL1(P000002.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000002.£V5PND
    MU* VAL1(P000002.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000002.£V5PNR
    MU* VAL1(P000002.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000002.£V5PTC
    MU* VAL1(P000002.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000002.£V5PEN
    MU* VAL1(P000002.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000002.£V5PQT
      *
     C                   EVAL      F000002.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000002.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000002.£40FLU='03'
     C                   EVAL      F000002.£40F_MSO='1'
     C                   EVAL      F000002.£40F_MWL=' '
     C                   EVAL      F000002.£40F_MWC='1'
     C                   EVAL      F000002.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000002.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000002.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000002.£40FDE
     C                   EVAL      £40FRE=F000002.£40FRE
     C                   EVAL      £40FLU=F000002.£40FLU
     C                   EVAL      £40F_MSO=F000002.£40F_MSO
     C                   EVAL      £40F_MWL=F000002.£40F_MWL
     C                   EVAL      £40F_MWC=F000002.£40F_MWC
     C                   EVAL      £40FTO(1)=F000002.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000002.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000002.£40FTO(3)
      *
     C                   EVAL      D000002.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000002.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000002.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000002.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000002.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000002.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000002.SUPP_NUU(1)= 1
     C                   EVAL      D000002.SUPP_NUU(2)= 2
     C                   EVAL      D000002.SUPP_NUU(3)= 3
     C                   EVAL      D000002.SUPP_NUG(1)= 11
     C                   EVAL      D000002.SUPP_NUG(2)= 21
     C                   EVAL      D000002.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000002.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000002.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000002.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000002.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000002.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000002.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000002.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000002.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000002.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000002.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000002.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000002.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000003
      *--------------------------------------------------------------*
     C     R000003       BEGSR
      * This is Subroutine  R000003
     C                   CLEAR                   U000003
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000003.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000003.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000003.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000003.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000003.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000003.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000003.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000003.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000003.£UIBK1
      *
     C                   CLEAR                   P000003
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000003.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000003.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000003.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000003.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000003.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000003.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000003.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000003.£V5PNR= 5
    MU* VAL1(P000003.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000003.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000003.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000003.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000003.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000003.£V5PQT=130,425
    MU* VAL1(P000003.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000003.£V5PTD
    MU* VAL1(P000003.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000003.£V5PMO
    MU* VAL1(P000003.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000003.£V5PND
    MU* VAL1(P000003.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000003.£V5PNR
    MU* VAL1(P000003.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000003.£V5PTC
    MU* VAL1(P000003.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000003.£V5PEN
    MU* VAL1(P000003.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000003.£V5PQT
      *
     C                   EVAL      F000003.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000003.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000003.£40FLU='03'
     C                   EVAL      F000003.£40F_MSO='1'
     C                   EVAL      F000003.£40F_MWL=' '
     C                   EVAL      F000003.£40F_MWC='1'
     C                   EVAL      F000003.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000003.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000003.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000003.£40FDE
     C                   EVAL      £40FRE=F000003.£40FRE
     C                   EVAL      £40FLU=F000003.£40FLU
     C                   EVAL      £40F_MSO=F000003.£40F_MSO
     C                   EVAL      £40F_MWL=F000003.£40F_MWL
     C                   EVAL      £40F_MWC=F000003.£40F_MWC
     C                   EVAL      £40FTO(1)=F000003.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000003.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000003.£40FTO(3)
      *
     C                   EVAL      D000003.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000003.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000003.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000003.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000003.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000003.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000003.SUPP_NUU(1)= 1
     C                   EVAL      D000003.SUPP_NUU(2)= 2
     C                   EVAL      D000003.SUPP_NUU(3)= 3
     C                   EVAL      D000003.SUPP_NUG(1)= 11
     C                   EVAL      D000003.SUPP_NUG(2)= 21
     C                   EVAL      D000003.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000003.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000003.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000003.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000003.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000003.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000003.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000003.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000003.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000003.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000003.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000003.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000003.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000004
      *--------------------------------------------------------------*
     C     R000004       BEGSR
      * This is Subroutine  R000004
     C                   CLEAR                   U000004
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000004.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000004.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000004.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000004.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000004.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000004.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000004.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000004.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000004.£UIBK1
      *
     C                   CLEAR                   P000004
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000004.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000004.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000004.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000004.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000004.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000004.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000004.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000004.£V5PNR= 5
    MU* VAL1(P000004.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000004.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000004.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000004.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000004.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000004.£V5PQT=130,425
    MU* VAL1(P000004.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000004.£V5PTD
    MU* VAL1(P000004.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000004.£V5PMO
    MU* VAL1(P000004.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000004.£V5PND
    MU* VAL1(P000004.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000004.£V5PNR
    MU* VAL1(P000004.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000004.£V5PTC
    MU* VAL1(P000004.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000004.£V5PEN
    MU* VAL1(P000004.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000004.£V5PQT
      *
     C                   EVAL      F000004.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000004.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000004.£40FLU='03'
     C                   EVAL      F000004.£40F_MSO='1'
     C                   EVAL      F000004.£40F_MWL=' '
     C                   EVAL      F000004.£40F_MWC='1'
     C                   EVAL      F000004.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000004.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000004.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000004.£40FDE
     C                   EVAL      £40FRE=F000004.£40FRE
     C                   EVAL      £40FLU=F000004.£40FLU
     C                   EVAL      £40F_MSO=F000004.£40F_MSO
     C                   EVAL      £40F_MWL=F000004.£40F_MWL
     C                   EVAL      £40F_MWC=F000004.£40F_MWC
     C                   EVAL      £40FTO(1)=F000004.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000004.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000004.£40FTO(3)
      *
     C                   EVAL      D000004.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000004.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000004.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000004.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000004.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000004.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000004.SUPP_NUU(1)= 1
     C                   EVAL      D000004.SUPP_NUU(2)= 2
     C                   EVAL      D000004.SUPP_NUU(3)= 3
     C                   EVAL      D000004.SUPP_NUG(1)= 11
     C                   EVAL      D000004.SUPP_NUG(2)= 21
     C                   EVAL      D000004.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000004.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000004.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000004.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000004.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000004.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000004.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000004.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000004.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000004.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000004.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000004.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000004.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000005
      *--------------------------------------------------------------*
     C     R000005       BEGSR
      * This is Subroutine  R000005
     C                   CLEAR                   U000005
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000005.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000005.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000005.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000005.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000005.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000005.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000005.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000005.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000005.£UIBK1
      *
     C                   CLEAR                   P000005
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000005.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000005.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000005.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000005.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000005.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000005.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000005.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000005.£V5PNR= 5
    MU* VAL1(P000005.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000005.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000005.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000005.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000005.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000005.£V5PQT=130,425
    MU* VAL1(P000005.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000005.£V5PTD
    MU* VAL1(P000005.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000005.£V5PMO
    MU* VAL1(P000005.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000005.£V5PND
    MU* VAL1(P000005.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000005.£V5PNR
    MU* VAL1(P000005.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000005.£V5PTC
    MU* VAL1(P000005.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000005.£V5PEN
    MU* VAL1(P000005.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000005.£V5PQT
      *
     C                   EVAL      F000005.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000005.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000005.£40FLU='03'
     C                   EVAL      F000005.£40F_MSO='1'
     C                   EVAL      F000005.£40F_MWL=' '
     C                   EVAL      F000005.£40F_MWC='1'
     C                   EVAL      F000005.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000005.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000005.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000005.£40FDE
     C                   EVAL      £40FRE=F000005.£40FRE
     C                   EVAL      £40FLU=F000005.£40FLU
     C                   EVAL      £40F_MSO=F000005.£40F_MSO
     C                   EVAL      £40F_MWL=F000005.£40F_MWL
     C                   EVAL      £40F_MWC=F000005.£40F_MWC
     C                   EVAL      £40FTO(1)=F000005.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000005.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000005.£40FTO(3)
      *
     C                   EVAL      D000005.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000005.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000005.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000005.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000005.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000005.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000005.SUPP_NUU(1)= 1
     C                   EVAL      D000005.SUPP_NUU(2)= 2
     C                   EVAL      D000005.SUPP_NUU(3)= 3
     C                   EVAL      D000005.SUPP_NUG(1)= 11
     C                   EVAL      D000005.SUPP_NUG(2)= 21
     C                   EVAL      D000005.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000005.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000005.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000005.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000005.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000005.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000005.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000005.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000005.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000005.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000005.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000005.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000005.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000006
      *--------------------------------------------------------------*
     C     R000006       BEGSR
      * This is Subroutine  R000006
     C                   CLEAR                   U000006
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000006.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000006.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000006.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000006.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000006.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000006.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000006.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000006.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000006.£UIBK1
      *
     C                   CLEAR                   P000006
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000006.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000006.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000006.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000006.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000006.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000006.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000006.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000006.£V5PNR= 5
    MU* VAL1(P000006.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000006.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000006.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000006.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000006.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000006.£V5PQT=130,425
    MU* VAL1(P000006.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000006.£V5PTD
    MU* VAL1(P000006.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000006.£V5PMO
    MU* VAL1(P000006.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000006.£V5PND
    MU* VAL1(P000006.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000006.£V5PNR
    MU* VAL1(P000006.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000006.£V5PTC
    MU* VAL1(P000006.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000006.£V5PEN
    MU* VAL1(P000006.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000006.£V5PQT
      *
     C                   EVAL      F000006.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000006.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000006.£40FLU='03'
     C                   EVAL      F000006.£40F_MSO='1'
     C                   EVAL      F000006.£40F_MWL=' '
     C                   EVAL      F000006.£40F_MWC='1'
     C                   EVAL      F000006.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000006.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000006.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000006.£40FDE
     C                   EVAL      £40FRE=F000006.£40FRE
     C                   EVAL      £40FLU=F000006.£40FLU
     C                   EVAL      £40F_MSO=F000006.£40F_MSO
     C                   EVAL      £40F_MWL=F000006.£40F_MWL
     C                   EVAL      £40F_MWC=F000006.£40F_MWC
     C                   EVAL      £40FTO(1)=F000006.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000006.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000006.£40FTO(3)
      *
     C                   EVAL      D000006.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000006.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000006.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000006.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000006.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000006.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000006.SUPP_NUU(1)= 1
     C                   EVAL      D000006.SUPP_NUU(2)= 2
     C                   EVAL      D000006.SUPP_NUU(3)= 3
     C                   EVAL      D000006.SUPP_NUG(1)= 11
     C                   EVAL      D000006.SUPP_NUG(2)= 21
     C                   EVAL      D000006.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000006.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000006.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000006.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000006.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000006.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000006.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000006.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000006.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000006.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000006.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000006.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000006.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000007
      *--------------------------------------------------------------*
     C     R000007       BEGSR
      * This is Subroutine  R000007
     C                   CLEAR                   U000007
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000007.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000007.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000007.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000007.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000007.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000007.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000007.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000007.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000007.£UIBK1
      *
     C                   CLEAR                   P000007
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000007.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000007.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000007.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000007.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000007.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000007.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000007.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000007.£V5PNR= 5
    MU* VAL1(P000007.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000007.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000007.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000007.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000007.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000007.£V5PQT=130,425
    MU* VAL1(P000007.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000007.£V5PTD
    MU* VAL1(P000007.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000007.£V5PMO
    MU* VAL1(P000007.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000007.£V5PND
    MU* VAL1(P000007.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000007.£V5PNR
    MU* VAL1(P000007.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000007.£V5PTC
    MU* VAL1(P000007.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000007.£V5PEN
    MU* VAL1(P000007.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000007.£V5PQT
      *
     C                   EVAL      F000007.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000007.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000007.£40FLU='03'
     C                   EVAL      F000007.£40F_MSO='1'
     C                   EVAL      F000007.£40F_MWL=' '
     C                   EVAL      F000007.£40F_MWC='1'
     C                   EVAL      F000007.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000007.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000007.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000007.£40FDE
     C                   EVAL      £40FRE=F000007.£40FRE
     C                   EVAL      £40FLU=F000007.£40FLU
     C                   EVAL      £40F_MSO=F000007.£40F_MSO
     C                   EVAL      £40F_MWL=F000007.£40F_MWL
     C                   EVAL      £40F_MWC=F000007.£40F_MWC
     C                   EVAL      £40FTO(1)=F000007.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000007.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000007.£40FTO(3)
      *
     C                   EVAL      D000007.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000007.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000007.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000007.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000007.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000007.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000007.SUPP_NUU(1)= 1
     C                   EVAL      D000007.SUPP_NUU(2)= 2
     C                   EVAL      D000007.SUPP_NUU(3)= 3
     C                   EVAL      D000007.SUPP_NUG(1)= 11
     C                   EVAL      D000007.SUPP_NUG(2)= 21
     C                   EVAL      D000007.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000007.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000007.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000007.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000007.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000007.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000007.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000007.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000007.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000007.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000007.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000007.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000007.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000008
      *--------------------------------------------------------------*
     C     R000008       BEGSR
      * This is Subroutine  R000008
     C                   CLEAR                   U000008
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000008.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000008.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000008.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000008.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000008.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000008.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000008.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000008.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000008.£UIBK1
      *
     C                   CLEAR                   P000008
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000008.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000008.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000008.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000008.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000008.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000008.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000008.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000008.£V5PNR= 5
    MU* VAL1(P000008.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000008.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000008.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000008.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000008.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000008.£V5PQT=130,425
    MU* VAL1(P000008.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000008.£V5PTD
    MU* VAL1(P000008.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000008.£V5PMO
    MU* VAL1(P000008.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000008.£V5PND
    MU* VAL1(P000008.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000008.£V5PNR
    MU* VAL1(P000008.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000008.£V5PTC
    MU* VAL1(P000008.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000008.£V5PEN
    MU* VAL1(P000008.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000008.£V5PQT
      *
     C                   EVAL      F000008.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000008.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000008.£40FLU='03'
     C                   EVAL      F000008.£40F_MSO='1'
     C                   EVAL      F000008.£40F_MWL=' '
     C                   EVAL      F000008.£40F_MWC='1'
     C                   EVAL      F000008.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000008.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000008.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000008.£40FDE
     C                   EVAL      £40FRE=F000008.£40FRE
     C                   EVAL      £40FLU=F000008.£40FLU
     C                   EVAL      £40F_MSO=F000008.£40F_MSO
     C                   EVAL      £40F_MWL=F000008.£40F_MWL
     C                   EVAL      £40F_MWC=F000008.£40F_MWC
     C                   EVAL      £40FTO(1)=F000008.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000008.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000008.£40FTO(3)
      *
     C                   EVAL      D000008.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000008.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000008.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000008.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000008.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000008.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000008.SUPP_NUU(1)= 1
     C                   EVAL      D000008.SUPP_NUU(2)= 2
     C                   EVAL      D000008.SUPP_NUU(3)= 3
     C                   EVAL      D000008.SUPP_NUG(1)= 11
     C                   EVAL      D000008.SUPP_NUG(2)= 21
     C                   EVAL      D000008.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000008.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000008.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000008.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000008.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000008.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000008.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000008.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000008.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000008.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000008.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000008.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000008.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000009
      *--------------------------------------------------------------*
     C     R000009       BEGSR
      * This is Subroutine  R000009
     C                   CLEAR                   U000009
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000009.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000009.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000009.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000009.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000009.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000009.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000009.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000009.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000009.£UIBK1
      *
     C                   CLEAR                   P000009
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000009.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000009.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000009.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000009.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000009.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000009.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000009.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000009.£V5PNR= 5
    MU* VAL1(P000009.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000009.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000009.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000009.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000009.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000009.£V5PQT=130,425
    MU* VAL1(P000009.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000009.£V5PTD
    MU* VAL1(P000009.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000009.£V5PMO
    MU* VAL1(P000009.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000009.£V5PND
    MU* VAL1(P000009.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000009.£V5PNR
    MU* VAL1(P000009.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000009.£V5PTC
    MU* VAL1(P000009.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000009.£V5PEN
    MU* VAL1(P000009.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000009.£V5PQT
      *
     C                   EVAL      F000009.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000009.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000009.£40FLU='03'
     C                   EVAL      F000009.£40F_MSO='1'
     C                   EVAL      F000009.£40F_MWL=' '
     C                   EVAL      F000009.£40F_MWC='1'
     C                   EVAL      F000009.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000009.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000009.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000009.£40FDE
     C                   EVAL      £40FRE=F000009.£40FRE
     C                   EVAL      £40FLU=F000009.£40FLU
     C                   EVAL      £40F_MSO=F000009.£40F_MSO
     C                   EVAL      £40F_MWL=F000009.£40F_MWL
     C                   EVAL      £40F_MWC=F000009.£40F_MWC
     C                   EVAL      £40FTO(1)=F000009.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000009.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000009.£40FTO(3)
      *
     C                   EVAL      D000009.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000009.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000009.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000009.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000009.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000009.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000009.SUPP_NUU(1)= 1
     C                   EVAL      D000009.SUPP_NUU(2)= 2
     C                   EVAL      D000009.SUPP_NUU(3)= 3
     C                   EVAL      D000009.SUPP_NUG(1)= 11
     C                   EVAL      D000009.SUPP_NUG(2)= 21
     C                   EVAL      D000009.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000009.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000009.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000009.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000009.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000009.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000009.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000009.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000009.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000009.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000009.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000009.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000009.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000010
      *--------------------------------------------------------------*
     C     R000010       BEGSR
      * This is Subroutine  R000010
     C                   CLEAR                   U000010
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000010.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000010.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000010.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000010.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000010.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000010.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000010.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000010.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000010.£UIBK1
      *
     C                   CLEAR                   P000010
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000010.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000010.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000010.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000010.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000010.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000010.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000010.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000010.£V5PNR= 5
    MU* VAL1(P000010.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000010.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000010.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000010.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000010.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000010.£V5PQT=130,425
    MU* VAL1(P000010.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000010.£V5PTD
    MU* VAL1(P000010.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000010.£V5PMO
    MU* VAL1(P000010.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000010.£V5PND
    MU* VAL1(P000010.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000010.£V5PNR
    MU* VAL1(P000010.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000010.£V5PTC
    MU* VAL1(P000010.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000010.£V5PEN
    MU* VAL1(P000010.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000010.£V5PQT
      *
     C                   EVAL      F000010.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000010.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000010.£40FLU='03'
     C                   EVAL      F000010.£40F_MSO='1'
     C                   EVAL      F000010.£40F_MWL=' '
     C                   EVAL      F000010.£40F_MWC='1'
     C                   EVAL      F000010.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000010.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000010.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000010.£40FDE
     C                   EVAL      £40FRE=F000010.£40FRE
     C                   EVAL      £40FLU=F000010.£40FLU
     C                   EVAL      £40F_MSO=F000010.£40F_MSO
     C                   EVAL      £40F_MWL=F000010.£40F_MWL
     C                   EVAL      £40F_MWC=F000010.£40F_MWC
     C                   EVAL      £40FTO(1)=F000010.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000010.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000010.£40FTO(3)
      *
     C                   EVAL      D000010.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000010.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000010.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000010.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000010.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000010.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000010.SUPP_NUU(1)= 1
     C                   EVAL      D000010.SUPP_NUU(2)= 2
     C                   EVAL      D000010.SUPP_NUU(3)= 3
     C                   EVAL      D000010.SUPP_NUG(1)= 11
     C                   EVAL      D000010.SUPP_NUG(2)= 21
     C                   EVAL      D000010.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000010.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000010.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000010.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000010.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000010.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000010.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000010.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000010.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000010.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000010.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000010.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000010.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000011
      *--------------------------------------------------------------*
     C     R000011       BEGSR
      * This is Subroutine  R000011
     C                   CLEAR                   U000011
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000011.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000011.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000011.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000011.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000011.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000011.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000011.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000011.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000011.£UIBK1
      *
     C                   CLEAR                   P000011
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000011.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000011.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000011.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000011.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000011.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000011.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000011.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000011.£V5PNR= 5
    MU* VAL1(P000011.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000011.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000011.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000011.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000011.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000011.£V5PQT=130,425
    MU* VAL1(P000011.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000011.£V5PTD
    MU* VAL1(P000011.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000011.£V5PMO
    MU* VAL1(P000011.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000011.£V5PND
    MU* VAL1(P000011.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000011.£V5PNR
    MU* VAL1(P000011.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000011.£V5PTC
    MU* VAL1(P000011.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000011.£V5PEN
    MU* VAL1(P000011.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000011.£V5PQT
      *
     C                   EVAL      F000011.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000011.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000011.£40FLU='03'
     C                   EVAL      F000011.£40F_MSO='1'
     C                   EVAL      F000011.£40F_MWL=' '
     C                   EVAL      F000011.£40F_MWC='1'
     C                   EVAL      F000011.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000011.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000011.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000011.£40FDE
     C                   EVAL      £40FRE=F000011.£40FRE
     C                   EVAL      £40FLU=F000011.£40FLU
     C                   EVAL      £40F_MSO=F000011.£40F_MSO
     C                   EVAL      £40F_MWL=F000011.£40F_MWL
     C                   EVAL      £40F_MWC=F000011.£40F_MWC
     C                   EVAL      £40FTO(1)=F000011.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000011.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000011.£40FTO(3)
      *
     C                   EVAL      D000011.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000011.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000011.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000011.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000011.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000011.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000011.SUPP_NUU(1)= 1
     C                   EVAL      D000011.SUPP_NUU(2)= 2
     C                   EVAL      D000011.SUPP_NUU(3)= 3
     C                   EVAL      D000011.SUPP_NUG(1)= 11
     C                   EVAL      D000011.SUPP_NUG(2)= 21
     C                   EVAL      D000011.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000011.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000011.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000011.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000011.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000011.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000011.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000011.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000011.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000011.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000011.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000011.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000011.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000012
      *--------------------------------------------------------------*
     C     R000012       BEGSR
      * This is Subroutine  R000012
     C                   CLEAR                   U000012
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000012.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000012.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000012.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000012.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000012.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000012.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000012.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000012.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000012.£UIBK1
      *
     C                   CLEAR                   P000012
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000012.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000012.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000012.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000012.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000012.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000012.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000012.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000012.£V5PNR= 5
    MU* VAL1(P000012.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000012.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000012.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000012.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000012.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000012.£V5PQT=130,425
    MU* VAL1(P000012.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000012.£V5PTD
    MU* VAL1(P000012.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000012.£V5PMO
    MU* VAL1(P000012.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000012.£V5PND
    MU* VAL1(P000012.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000012.£V5PNR
    MU* VAL1(P000012.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000012.£V5PTC
    MU* VAL1(P000012.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000012.£V5PEN
    MU* VAL1(P000012.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000012.£V5PQT
      *
     C                   EVAL      F000012.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000012.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000012.£40FLU='03'
     C                   EVAL      F000012.£40F_MSO='1'
     C                   EVAL      F000012.£40F_MWL=' '
     C                   EVAL      F000012.£40F_MWC='1'
     C                   EVAL      F000012.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000012.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000012.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000012.£40FDE
     C                   EVAL      £40FRE=F000012.£40FRE
     C                   EVAL      £40FLU=F000012.£40FLU
     C                   EVAL      £40F_MSO=F000012.£40F_MSO
     C                   EVAL      £40F_MWL=F000012.£40F_MWL
     C                   EVAL      £40F_MWC=F000012.£40F_MWC
     C                   EVAL      £40FTO(1)=F000012.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000012.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000012.£40FTO(3)
      *
     C                   EVAL      D000012.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000012.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000012.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000012.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000012.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000012.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000012.SUPP_NUU(1)= 1
     C                   EVAL      D000012.SUPP_NUU(2)= 2
     C                   EVAL      D000012.SUPP_NUU(3)= 3
     C                   EVAL      D000012.SUPP_NUG(1)= 11
     C                   EVAL      D000012.SUPP_NUG(2)= 21
     C                   EVAL      D000012.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000012.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000012.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000012.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000012.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000012.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000012.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000012.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000012.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000012.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000012.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000012.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000012.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000013
      *--------------------------------------------------------------*
     C     R000013       BEGSR
      * This is Subroutine  R000013
     C                   CLEAR                   U000013
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000013.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000013.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000013.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000013.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000013.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000013.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000013.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000013.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000013.£UIBK1
      *
     C                   CLEAR                   P000013
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000013.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000013.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000013.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000013.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000013.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000013.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000013.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000013.£V5PNR= 5
    MU* VAL1(P000013.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000013.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000013.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000013.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000013.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000013.£V5PQT=130,425
    MU* VAL1(P000013.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000013.£V5PTD
    MU* VAL1(P000013.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000013.£V5PMO
    MU* VAL1(P000013.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000013.£V5PND
    MU* VAL1(P000013.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000013.£V5PNR
    MU* VAL1(P000013.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000013.£V5PTC
    MU* VAL1(P000013.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000013.£V5PEN
    MU* VAL1(P000013.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000013.£V5PQT
      *
     C                   EVAL      F000013.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000013.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000013.£40FLU='03'
     C                   EVAL      F000013.£40F_MSO='1'
     C                   EVAL      F000013.£40F_MWL=' '
     C                   EVAL      F000013.£40F_MWC='1'
     C                   EVAL      F000013.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000013.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000013.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000013.£40FDE
     C                   EVAL      £40FRE=F000013.£40FRE
     C                   EVAL      £40FLU=F000013.£40FLU
     C                   EVAL      £40F_MSO=F000013.£40F_MSO
     C                   EVAL      £40F_MWL=F000013.£40F_MWL
     C                   EVAL      £40F_MWC=F000013.£40F_MWC
     C                   EVAL      £40FTO(1)=F000013.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000013.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000013.£40FTO(3)
      *
     C                   EVAL      D000013.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000013.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000013.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000013.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000013.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000013.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000013.SUPP_NUU(1)= 1
     C                   EVAL      D000013.SUPP_NUU(2)= 2
     C                   EVAL      D000013.SUPP_NUU(3)= 3
     C                   EVAL      D000013.SUPP_NUG(1)= 11
     C                   EVAL      D000013.SUPP_NUG(2)= 21
     C                   EVAL      D000013.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000013.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000013.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000013.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000013.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000013.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000013.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000013.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000013.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000013.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000013.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000013.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000013.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000014
      *--------------------------------------------------------------*
     C     R000014       BEGSR
      * This is Subroutine  R000014
     C                   CLEAR                   U000014
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000014.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000014.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000014.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000014.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000014.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000014.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000014.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000014.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000014.£UIBK1
      *
     C                   CLEAR                   P000014
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000014.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000014.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000014.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000014.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000014.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000014.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000014.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000014.£V5PNR= 5
    MU* VAL1(P000014.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000014.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000014.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000014.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000014.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000014.£V5PQT=130,425
    MU* VAL1(P000014.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000014.£V5PTD
    MU* VAL1(P000014.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000014.£V5PMO
    MU* VAL1(P000014.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000014.£V5PND
    MU* VAL1(P000014.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000014.£V5PNR
    MU* VAL1(P000014.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000014.£V5PTC
    MU* VAL1(P000014.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000014.£V5PEN
    MU* VAL1(P000014.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000014.£V5PQT
      *
     C                   EVAL      F000014.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000014.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000014.£40FLU='03'
     C                   EVAL      F000014.£40F_MSO='1'
     C                   EVAL      F000014.£40F_MWL=' '
     C                   EVAL      F000014.£40F_MWC='1'
     C                   EVAL      F000014.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000014.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000014.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000014.£40FDE
     C                   EVAL      £40FRE=F000014.£40FRE
     C                   EVAL      £40FLU=F000014.£40FLU
     C                   EVAL      £40F_MSO=F000014.£40F_MSO
     C                   EVAL      £40F_MWL=F000014.£40F_MWL
     C                   EVAL      £40F_MWC=F000014.£40F_MWC
     C                   EVAL      £40FTO(1)=F000014.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000014.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000014.£40FTO(3)
      *
     C                   EVAL      D000014.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000014.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000014.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000014.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000014.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000014.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000014.SUPP_NUU(1)= 1
     C                   EVAL      D000014.SUPP_NUU(2)= 2
     C                   EVAL      D000014.SUPP_NUU(3)= 3
     C                   EVAL      D000014.SUPP_NUG(1)= 11
     C                   EVAL      D000014.SUPP_NUG(2)= 21
     C                   EVAL      D000014.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000014.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000014.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000014.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000014.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000014.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000014.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000014.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000014.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000014.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000014.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000014.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000014.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000015
      *--------------------------------------------------------------*
     C     R000015       BEGSR
      * This is Subroutine  R000015
     C                   CLEAR                   U000015
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000015.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000015.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000015.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000015.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000015.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000015.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000015.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000015.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000015.£UIBK1
      *
     C                   CLEAR                   P000015
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000015.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000015.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000015.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000015.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000015.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000015.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000015.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000015.£V5PNR= 5
    MU* VAL1(P000015.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000015.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000015.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000015.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000015.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000015.£V5PQT=130,425
    MU* VAL1(P000015.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000015.£V5PTD
    MU* VAL1(P000015.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000015.£V5PMO
    MU* VAL1(P000015.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000015.£V5PND
    MU* VAL1(P000015.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000015.£V5PNR
    MU* VAL1(P000015.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000015.£V5PTC
    MU* VAL1(P000015.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000015.£V5PEN
    MU* VAL1(P000015.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000015.£V5PQT
      *
     C                   EVAL      F000015.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000015.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000015.£40FLU='03'
     C                   EVAL      F000015.£40F_MSO='1'
     C                   EVAL      F000015.£40F_MWL=' '
     C                   EVAL      F000015.£40F_MWC='1'
     C                   EVAL      F000015.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000015.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000015.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000015.£40FDE
     C                   EVAL      £40FRE=F000015.£40FRE
     C                   EVAL      £40FLU=F000015.£40FLU
     C                   EVAL      £40F_MSO=F000015.£40F_MSO
     C                   EVAL      £40F_MWL=F000015.£40F_MWL
     C                   EVAL      £40F_MWC=F000015.£40F_MWC
     C                   EVAL      £40FTO(1)=F000015.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000015.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000015.£40FTO(3)
      *
     C                   EVAL      D000015.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000015.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000015.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000015.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000015.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000015.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000015.SUPP_NUU(1)= 1
     C                   EVAL      D000015.SUPP_NUU(2)= 2
     C                   EVAL      D000015.SUPP_NUU(3)= 3
     C                   EVAL      D000015.SUPP_NUG(1)= 11
     C                   EVAL      D000015.SUPP_NUG(2)= 21
     C                   EVAL      D000015.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000015.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000015.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000015.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000015.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000015.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000015.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000015.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000015.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000015.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000015.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000015.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000015.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000016
      *--------------------------------------------------------------*
     C     R000016       BEGSR
      * This is Subroutine  R000016
     C                   CLEAR                   U000016
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000016.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000016.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000016.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000016.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000016.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000016.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000016.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000016.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000016.£UIBK1
      *
     C                   CLEAR                   P000016
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000016.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000016.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000016.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000016.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000016.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000016.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000016.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000016.£V5PNR= 5
    MU* VAL1(P000016.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000016.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000016.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000016.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000016.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000016.£V5PQT=130,425
    MU* VAL1(P000016.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000016.£V5PTD
    MU* VAL1(P000016.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000016.£V5PMO
    MU* VAL1(P000016.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000016.£V5PND
    MU* VAL1(P000016.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000016.£V5PNR
    MU* VAL1(P000016.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000016.£V5PTC
    MU* VAL1(P000016.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000016.£V5PEN
    MU* VAL1(P000016.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000016.£V5PQT
      *
     C                   EVAL      F000016.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000016.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000016.£40FLU='03'
     C                   EVAL      F000016.£40F_MSO='1'
     C                   EVAL      F000016.£40F_MWL=' '
     C                   EVAL      F000016.£40F_MWC='1'
     C                   EVAL      F000016.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000016.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000016.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000016.£40FDE
     C                   EVAL      £40FRE=F000016.£40FRE
     C                   EVAL      £40FLU=F000016.£40FLU
     C                   EVAL      £40F_MSO=F000016.£40F_MSO
     C                   EVAL      £40F_MWL=F000016.£40F_MWL
     C                   EVAL      £40F_MWC=F000016.£40F_MWC
     C                   EVAL      £40FTO(1)=F000016.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000016.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000016.£40FTO(3)
      *
     C                   EVAL      D000016.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000016.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000016.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000016.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000016.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000016.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000016.SUPP_NUU(1)= 1
     C                   EVAL      D000016.SUPP_NUU(2)= 2
     C                   EVAL      D000016.SUPP_NUU(3)= 3
     C                   EVAL      D000016.SUPP_NUG(1)= 11
     C                   EVAL      D000016.SUPP_NUG(2)= 21
     C                   EVAL      D000016.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000016.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000016.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000016.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000016.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000016.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000016.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000016.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000016.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000016.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000016.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000016.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000016.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000017
      *--------------------------------------------------------------*
     C     R000017       BEGSR
      * This is Subroutine  R000017
     C                   CLEAR                   U000017
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000017.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000017.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000017.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000017.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000017.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000017.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000017.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000017.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000017.£UIBK1
      *
     C                   CLEAR                   P000017
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000017.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000017.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000017.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000017.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000017.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000017.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000017.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000017.£V5PNR= 5
    MU* VAL1(P000017.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000017.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000017.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000017.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000017.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000017.£V5PQT=130,425
    MU* VAL1(P000017.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000017.£V5PTD
    MU* VAL1(P000017.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000017.£V5PMO
    MU* VAL1(P000017.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000017.£V5PND
    MU* VAL1(P000017.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000017.£V5PNR
    MU* VAL1(P000017.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000017.£V5PTC
    MU* VAL1(P000017.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000017.£V5PEN
    MU* VAL1(P000017.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000017.£V5PQT
      *
     C                   EVAL      F000017.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000017.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000017.£40FLU='03'
     C                   EVAL      F000017.£40F_MSO='1'
     C                   EVAL      F000017.£40F_MWL=' '
     C                   EVAL      F000017.£40F_MWC='1'
     C                   EVAL      F000017.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000017.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000017.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000017.£40FDE
     C                   EVAL      £40FRE=F000017.£40FRE
     C                   EVAL      £40FLU=F000017.£40FLU
     C                   EVAL      £40F_MSO=F000017.£40F_MSO
     C                   EVAL      £40F_MWL=F000017.£40F_MWL
     C                   EVAL      £40F_MWC=F000017.£40F_MWC
     C                   EVAL      £40FTO(1)=F000017.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000017.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000017.£40FTO(3)
      *
     C                   EVAL      D000017.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000017.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000017.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000017.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000017.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000017.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000017.SUPP_NUU(1)= 1
     C                   EVAL      D000017.SUPP_NUU(2)= 2
     C                   EVAL      D000017.SUPP_NUU(3)= 3
     C                   EVAL      D000017.SUPP_NUG(1)= 11
     C                   EVAL      D000017.SUPP_NUG(2)= 21
     C                   EVAL      D000017.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000017.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000017.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000017.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000017.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000017.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000017.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000017.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000017.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000017.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000017.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000017.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000017.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000018
      *--------------------------------------------------------------*
     C     R000018       BEGSR
      * This is Subroutine  R000018
     C                   CLEAR                   U000018
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000018.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000018.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000018.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000018.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000018.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000018.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000018.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000018.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000018.£UIBK1
      *
     C                   CLEAR                   P000018
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000018.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000018.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000018.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000018.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000018.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000018.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000018.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000018.£V5PNR= 5
    MU* VAL1(P000018.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000018.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000018.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000018.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000018.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000018.£V5PQT=130,425
    MU* VAL1(P000018.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000018.£V5PTD
    MU* VAL1(P000018.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000018.£V5PMO
    MU* VAL1(P000018.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000018.£V5PND
    MU* VAL1(P000018.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000018.£V5PNR
    MU* VAL1(P000018.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000018.£V5PTC
    MU* VAL1(P000018.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000018.£V5PEN
    MU* VAL1(P000018.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000018.£V5PQT
      *
     C                   EVAL      F000018.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000018.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000018.£40FLU='03'
     C                   EVAL      F000018.£40F_MSO='1'
     C                   EVAL      F000018.£40F_MWL=' '
     C                   EVAL      F000018.£40F_MWC='1'
     C                   EVAL      F000018.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000018.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000018.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000018.£40FDE
     C                   EVAL      £40FRE=F000018.£40FRE
     C                   EVAL      £40FLU=F000018.£40FLU
     C                   EVAL      £40F_MSO=F000018.£40F_MSO
     C                   EVAL      £40F_MWL=F000018.£40F_MWL
     C                   EVAL      £40F_MWC=F000018.£40F_MWC
     C                   EVAL      £40FTO(1)=F000018.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000018.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000018.£40FTO(3)
      *
     C                   EVAL      D000018.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000018.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000018.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000018.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000018.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000018.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000018.SUPP_NUU(1)= 1
     C                   EVAL      D000018.SUPP_NUU(2)= 2
     C                   EVAL      D000018.SUPP_NUU(3)= 3
     C                   EVAL      D000018.SUPP_NUG(1)= 11
     C                   EVAL      D000018.SUPP_NUG(2)= 21
     C                   EVAL      D000018.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000018.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000018.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000018.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000018.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000018.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000018.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000018.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000018.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000018.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000018.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000018.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000018.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000019
      *--------------------------------------------------------------*
     C     R000019       BEGSR
      * This is Subroutine  R000019
     C                   CLEAR                   U000019
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000019.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000019.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000019.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000019.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000019.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000019.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000019.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000019.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000019.£UIBK1
      *
     C                   CLEAR                   P000019
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000019.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000019.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000019.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000019.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000019.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000019.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000019.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000019.£V5PNR= 5
    MU* VAL1(P000019.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000019.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000019.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000019.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000019.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000019.£V5PQT=130,425
    MU* VAL1(P000019.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000019.£V5PTD
    MU* VAL1(P000019.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000019.£V5PMO
    MU* VAL1(P000019.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000019.£V5PND
    MU* VAL1(P000019.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000019.£V5PNR
    MU* VAL1(P000019.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000019.£V5PTC
    MU* VAL1(P000019.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000019.£V5PEN
    MU* VAL1(P000019.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000019.£V5PQT
      *
     C                   EVAL      F000019.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000019.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000019.£40FLU='03'
     C                   EVAL      F000019.£40F_MSO='1'
     C                   EVAL      F000019.£40F_MWL=' '
     C                   EVAL      F000019.£40F_MWC='1'
     C                   EVAL      F000019.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000019.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000019.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000019.£40FDE
     C                   EVAL      £40FRE=F000019.£40FRE
     C                   EVAL      £40FLU=F000019.£40FLU
     C                   EVAL      £40F_MSO=F000019.£40F_MSO
     C                   EVAL      £40F_MWL=F000019.£40F_MWL
     C                   EVAL      £40F_MWC=F000019.£40F_MWC
     C                   EVAL      £40FTO(1)=F000019.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000019.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000019.£40FTO(3)
      *
     C                   EVAL      D000019.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000019.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000019.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000019.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000019.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000019.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000019.SUPP_NUU(1)= 1
     C                   EVAL      D000019.SUPP_NUU(2)= 2
     C                   EVAL      D000019.SUPP_NUU(3)= 3
     C                   EVAL      D000019.SUPP_NUG(1)= 11
     C                   EVAL      D000019.SUPP_NUG(2)= 21
     C                   EVAL      D000019.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000019.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000019.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000019.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000019.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000019.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000019.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000019.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000019.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000019.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000019.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000019.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000019.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000020
      *--------------------------------------------------------------*
     C     R000020       BEGSR
      * This is Subroutine  R000020
     C                   CLEAR                   U000020
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000020.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000020.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000020.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000020.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000020.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000020.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000020.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000020.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000020.£UIBK1
      *
     C                   CLEAR                   P000020
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000020.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000020.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000020.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000020.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000020.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000020.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000020.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000020.£V5PNR= 5
    MU* VAL1(P000020.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000020.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000020.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000020.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000020.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000020.£V5PQT=130,425
    MU* VAL1(P000020.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000020.£V5PTD
    MU* VAL1(P000020.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000020.£V5PMO
    MU* VAL1(P000020.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000020.£V5PND
    MU* VAL1(P000020.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000020.£V5PNR
    MU* VAL1(P000020.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000020.£V5PTC
    MU* VAL1(P000020.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000020.£V5PEN
    MU* VAL1(P000020.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000020.£V5PQT
      *
     C                   EVAL      F000020.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000020.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000020.£40FLU='03'
     C                   EVAL      F000020.£40F_MSO='1'
     C                   EVAL      F000020.£40F_MWL=' '
     C                   EVAL      F000020.£40F_MWC='1'
     C                   EVAL      F000020.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000020.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000020.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000020.£40FDE
     C                   EVAL      £40FRE=F000020.£40FRE
     C                   EVAL      £40FLU=F000020.£40FLU
     C                   EVAL      £40F_MSO=F000020.£40F_MSO
     C                   EVAL      £40F_MWL=F000020.£40F_MWL
     C                   EVAL      £40F_MWC=F000020.£40F_MWC
     C                   EVAL      £40FTO(1)=F000020.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000020.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000020.£40FTO(3)
      *
     C                   EVAL      D000020.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000020.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000020.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000020.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000020.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000020.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000020.SUPP_NUU(1)= 1
     C                   EVAL      D000020.SUPP_NUU(2)= 2
     C                   EVAL      D000020.SUPP_NUU(3)= 3
     C                   EVAL      D000020.SUPP_NUG(1)= 11
     C                   EVAL      D000020.SUPP_NUG(2)= 21
     C                   EVAL      D000020.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000020.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000020.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000020.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000020.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000020.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000020.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000020.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000020.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000020.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000020.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000020.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000020.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000021
      *--------------------------------------------------------------*
     C     R000021       BEGSR
      * This is Subroutine  R000021
     C                   CLEAR                   U000021
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000021.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000021.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000021.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000021.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000021.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000021.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000021.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000021.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000021.£UIBK1
      *
     C                   CLEAR                   P000021
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000021.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000021.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000021.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000021.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000021.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000021.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000021.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000021.£V5PNR= 5
    MU* VAL1(P000021.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000021.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000021.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000021.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000021.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000021.£V5PQT=130,425
    MU* VAL1(P000021.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000021.£V5PTD
    MU* VAL1(P000021.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000021.£V5PMO
    MU* VAL1(P000021.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000021.£V5PND
    MU* VAL1(P000021.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000021.£V5PNR
    MU* VAL1(P000021.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000021.£V5PTC
    MU* VAL1(P000021.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000021.£V5PEN
    MU* VAL1(P000021.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000021.£V5PQT
      *
     C                   EVAL      F000021.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000021.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000021.£40FLU='03'
     C                   EVAL      F000021.£40F_MSO='1'
     C                   EVAL      F000021.£40F_MWL=' '
     C                   EVAL      F000021.£40F_MWC='1'
     C                   EVAL      F000021.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000021.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000021.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000021.£40FDE
     C                   EVAL      £40FRE=F000021.£40FRE
     C                   EVAL      £40FLU=F000021.£40FLU
     C                   EVAL      £40F_MSO=F000021.£40F_MSO
     C                   EVAL      £40F_MWL=F000021.£40F_MWL
     C                   EVAL      £40F_MWC=F000021.£40F_MWC
     C                   EVAL      £40FTO(1)=F000021.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000021.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000021.£40FTO(3)
      *
     C                   EVAL      D000021.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000021.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000021.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000021.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000021.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000021.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000021.SUPP_NUU(1)= 1
     C                   EVAL      D000021.SUPP_NUU(2)= 2
     C                   EVAL      D000021.SUPP_NUU(3)= 3
     C                   EVAL      D000021.SUPP_NUG(1)= 11
     C                   EVAL      D000021.SUPP_NUG(2)= 21
     C                   EVAL      D000021.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000021.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000021.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000021.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000021.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000021.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000021.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000021.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000021.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000021.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000021.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000021.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000021.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000022
      *--------------------------------------------------------------*
     C     R000022       BEGSR
      * This is Subroutine  R000022
     C                   CLEAR                   U000022
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000022.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000022.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000022.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000022.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000022.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000022.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000022.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000022.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000022.£UIBK1
      *
     C                   CLEAR                   P000022
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000022.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000022.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000022.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000022.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000022.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000022.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000022.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000022.£V5PNR= 5
    MU* VAL1(P000022.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000022.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000022.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000022.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000022.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000022.£V5PQT=130,425
    MU* VAL1(P000022.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000022.£V5PTD
    MU* VAL1(P000022.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000022.£V5PMO
    MU* VAL1(P000022.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000022.£V5PND
    MU* VAL1(P000022.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000022.£V5PNR
    MU* VAL1(P000022.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000022.£V5PTC
    MU* VAL1(P000022.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000022.£V5PEN
    MU* VAL1(P000022.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000022.£V5PQT
      *
     C                   EVAL      F000022.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000022.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000022.£40FLU='03'
     C                   EVAL      F000022.£40F_MSO='1'
     C                   EVAL      F000022.£40F_MWL=' '
     C                   EVAL      F000022.£40F_MWC='1'
     C                   EVAL      F000022.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000022.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000022.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000022.£40FDE
     C                   EVAL      £40FRE=F000022.£40FRE
     C                   EVAL      £40FLU=F000022.£40FLU
     C                   EVAL      £40F_MSO=F000022.£40F_MSO
     C                   EVAL      £40F_MWL=F000022.£40F_MWL
     C                   EVAL      £40F_MWC=F000022.£40F_MWC
     C                   EVAL      £40FTO(1)=F000022.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000022.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000022.£40FTO(3)
      *
     C                   EVAL      D000022.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000022.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000022.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000022.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000022.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000022.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000022.SUPP_NUU(1)= 1
     C                   EVAL      D000022.SUPP_NUU(2)= 2
     C                   EVAL      D000022.SUPP_NUU(3)= 3
     C                   EVAL      D000022.SUPP_NUG(1)= 11
     C                   EVAL      D000022.SUPP_NUG(2)= 21
     C                   EVAL      D000022.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000022.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000022.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000022.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000022.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000022.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000022.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000022.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000022.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000022.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000022.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000022.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000022.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000023
      *--------------------------------------------------------------*
     C     R000023       BEGSR
      * This is Subroutine  R000023
     C                   CLEAR                   U000023
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000023.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000023.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000023.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000023.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000023.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000023.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000023.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000023.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000023.£UIBK1
      *
     C                   CLEAR                   P000023
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000023.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000023.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000023.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000023.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000023.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000023.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000023.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000023.£V5PNR= 5
    MU* VAL1(P000023.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000023.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000023.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000023.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000023.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000023.£V5PQT=130,425
    MU* VAL1(P000023.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000023.£V5PTD
    MU* VAL1(P000023.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000023.£V5PMO
    MU* VAL1(P000023.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000023.£V5PND
    MU* VAL1(P000023.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000023.£V5PNR
    MU* VAL1(P000023.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000023.£V5PTC
    MU* VAL1(P000023.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000023.£V5PEN
    MU* VAL1(P000023.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000023.£V5PQT
      *
     C                   EVAL      F000023.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000023.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000023.£40FLU='03'
     C                   EVAL      F000023.£40F_MSO='1'
     C                   EVAL      F000023.£40F_MWL=' '
     C                   EVAL      F000023.£40F_MWC='1'
     C                   EVAL      F000023.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000023.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000023.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000023.£40FDE
     C                   EVAL      £40FRE=F000023.£40FRE
     C                   EVAL      £40FLU=F000023.£40FLU
     C                   EVAL      £40F_MSO=F000023.£40F_MSO
     C                   EVAL      £40F_MWL=F000023.£40F_MWL
     C                   EVAL      £40F_MWC=F000023.£40F_MWC
     C                   EVAL      £40FTO(1)=F000023.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000023.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000023.£40FTO(3)
      *
     C                   EVAL      D000023.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000023.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000023.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000023.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000023.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000023.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000023.SUPP_NUU(1)= 1
     C                   EVAL      D000023.SUPP_NUU(2)= 2
     C                   EVAL      D000023.SUPP_NUU(3)= 3
     C                   EVAL      D000023.SUPP_NUG(1)= 11
     C                   EVAL      D000023.SUPP_NUG(2)= 21
     C                   EVAL      D000023.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000023.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000023.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000023.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000023.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000023.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000023.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000023.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000023.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000023.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000023.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000023.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000023.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000024
      *--------------------------------------------------------------*
     C     R000024       BEGSR
      * This is Subroutine  R000024
     C                   CLEAR                   U000024
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000024.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000024.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000024.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000024.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000024.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000024.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000024.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000024.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000024.£UIBK1
      *
     C                   CLEAR                   P000024
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000024.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000024.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000024.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000024.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000024.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000024.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000024.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000024.£V5PNR= 5
    MU* VAL1(P000024.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000024.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000024.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000024.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000024.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000024.£V5PQT=130,425
    MU* VAL1(P000024.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000024.£V5PTD
    MU* VAL1(P000024.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000024.£V5PMO
    MU* VAL1(P000024.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000024.£V5PND
    MU* VAL1(P000024.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000024.£V5PNR
    MU* VAL1(P000024.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000024.£V5PTC
    MU* VAL1(P000024.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000024.£V5PEN
    MU* VAL1(P000024.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000024.£V5PQT
      *
     C                   EVAL      F000024.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000024.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000024.£40FLU='03'
     C                   EVAL      F000024.£40F_MSO='1'
     C                   EVAL      F000024.£40F_MWL=' '
     C                   EVAL      F000024.£40F_MWC='1'
     C                   EVAL      F000024.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000024.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000024.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000024.£40FDE
     C                   EVAL      £40FRE=F000024.£40FRE
     C                   EVAL      £40FLU=F000024.£40FLU
     C                   EVAL      £40F_MSO=F000024.£40F_MSO
     C                   EVAL      £40F_MWL=F000024.£40F_MWL
     C                   EVAL      £40F_MWC=F000024.£40F_MWC
     C                   EVAL      £40FTO(1)=F000024.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000024.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000024.£40FTO(3)
      *
     C                   EVAL      D000024.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000024.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000024.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000024.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000024.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000024.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000024.SUPP_NUU(1)= 1
     C                   EVAL      D000024.SUPP_NUU(2)= 2
     C                   EVAL      D000024.SUPP_NUU(3)= 3
     C                   EVAL      D000024.SUPP_NUG(1)= 11
     C                   EVAL      D000024.SUPP_NUG(2)= 21
     C                   EVAL      D000024.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000024.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000024.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000024.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000024.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000024.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000024.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000024.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000024.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000024.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000024.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000024.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000024.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000025
      *--------------------------------------------------------------*
     C     R000025       BEGSR
      * This is Subroutine  R000025
     C                   CLEAR                   U000025
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000025.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000025.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000025.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000025.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000025.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000025.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000025.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000025.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000025.£UIBK1
      *
     C                   CLEAR                   P000025
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000025.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000025.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000025.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000025.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000025.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000025.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000025.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000025.£V5PNR= 5
    MU* VAL1(P000025.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000025.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000025.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000025.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000025.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000025.£V5PQT=130,425
    MU* VAL1(P000025.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000025.£V5PTD
    MU* VAL1(P000025.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000025.£V5PMO
    MU* VAL1(P000025.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000025.£V5PND
    MU* VAL1(P000025.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000025.£V5PNR
    MU* VAL1(P000025.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000025.£V5PTC
    MU* VAL1(P000025.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000025.£V5PEN
    MU* VAL1(P000025.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000025.£V5PQT
      *
     C                   EVAL      F000025.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000025.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000025.£40FLU='03'
     C                   EVAL      F000025.£40F_MSO='1'
     C                   EVAL      F000025.£40F_MWL=' '
     C                   EVAL      F000025.£40F_MWC='1'
     C                   EVAL      F000025.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000025.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000025.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000025.£40FDE
     C                   EVAL      £40FRE=F000025.£40FRE
     C                   EVAL      £40FLU=F000025.£40FLU
     C                   EVAL      £40F_MSO=F000025.£40F_MSO
     C                   EVAL      £40F_MWL=F000025.£40F_MWL
     C                   EVAL      £40F_MWC=F000025.£40F_MWC
     C                   EVAL      £40FTO(1)=F000025.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000025.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000025.£40FTO(3)
      *
     C                   EVAL      D000025.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000025.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000025.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000025.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000025.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000025.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000025.SUPP_NUU(1)= 1
     C                   EVAL      D000025.SUPP_NUU(2)= 2
     C                   EVAL      D000025.SUPP_NUU(3)= 3
     C                   EVAL      D000025.SUPP_NUG(1)= 11
     C                   EVAL      D000025.SUPP_NUG(2)= 21
     C                   EVAL      D000025.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000025.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000025.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000025.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000025.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000025.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000025.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000025.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000025.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000025.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000025.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000025.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000025.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000026
      *--------------------------------------------------------------*
     C     R000026       BEGSR
      * This is Subroutine  R000026
     C                   CLEAR                   U000026
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000026.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000026.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000026.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000026.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000026.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000026.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000026.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000026.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000026.£UIBK1
      *
     C                   CLEAR                   P000026
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000026.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000026.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000026.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000026.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000026.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000026.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000026.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000026.£V5PNR= 5
    MU* VAL1(P000026.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000026.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000026.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000026.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000026.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000026.£V5PQT=130,425
    MU* VAL1(P000026.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000026.£V5PTD
    MU* VAL1(P000026.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000026.£V5PMO
    MU* VAL1(P000026.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000026.£V5PND
    MU* VAL1(P000026.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000026.£V5PNR
    MU* VAL1(P000026.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000026.£V5PTC
    MU* VAL1(P000026.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000026.£V5PEN
    MU* VAL1(P000026.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000026.£V5PQT
      *
     C                   EVAL      F000026.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000026.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000026.£40FLU='03'
     C                   EVAL      F000026.£40F_MSO='1'
     C                   EVAL      F000026.£40F_MWL=' '
     C                   EVAL      F000026.£40F_MWC='1'
     C                   EVAL      F000026.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000026.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000026.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000026.£40FDE
     C                   EVAL      £40FRE=F000026.£40FRE
     C                   EVAL      £40FLU=F000026.£40FLU
     C                   EVAL      £40F_MSO=F000026.£40F_MSO
     C                   EVAL      £40F_MWL=F000026.£40F_MWL
     C                   EVAL      £40F_MWC=F000026.£40F_MWC
     C                   EVAL      £40FTO(1)=F000026.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000026.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000026.£40FTO(3)
      *
     C                   EVAL      D000026.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000026.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000026.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000026.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000026.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000026.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000026.SUPP_NUU(1)= 1
     C                   EVAL      D000026.SUPP_NUU(2)= 2
     C                   EVAL      D000026.SUPP_NUU(3)= 3
     C                   EVAL      D000026.SUPP_NUG(1)= 11
     C                   EVAL      D000026.SUPP_NUG(2)= 21
     C                   EVAL      D000026.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000026.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000026.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000026.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000026.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000026.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000026.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000026.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000026.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000026.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000026.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000026.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000026.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000027
      *--------------------------------------------------------------*
     C     R000027       BEGSR
      * This is Subroutine  R000027
     C                   CLEAR                   U000027
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000027.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000027.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000027.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000027.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000027.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000027.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000027.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000027.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000027.£UIBK1
      *
     C                   CLEAR                   P000027
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000027.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000027.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000027.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000027.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000027.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000027.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000027.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000027.£V5PNR= 5
    MU* VAL1(P000027.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000027.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000027.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000027.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000027.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000027.£V5PQT=130,425
    MU* VAL1(P000027.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000027.£V5PTD
    MU* VAL1(P000027.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000027.£V5PMO
    MU* VAL1(P000027.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000027.£V5PND
    MU* VAL1(P000027.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000027.£V5PNR
    MU* VAL1(P000027.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000027.£V5PTC
    MU* VAL1(P000027.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000027.£V5PEN
    MU* VAL1(P000027.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000027.£V5PQT
      *
     C                   EVAL      F000027.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000027.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000027.£40FLU='03'
     C                   EVAL      F000027.£40F_MSO='1'
     C                   EVAL      F000027.£40F_MWL=' '
     C                   EVAL      F000027.£40F_MWC='1'
     C                   EVAL      F000027.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000027.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000027.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000027.£40FDE
     C                   EVAL      £40FRE=F000027.£40FRE
     C                   EVAL      £40FLU=F000027.£40FLU
     C                   EVAL      £40F_MSO=F000027.£40F_MSO
     C                   EVAL      £40F_MWL=F000027.£40F_MWL
     C                   EVAL      £40F_MWC=F000027.£40F_MWC
     C                   EVAL      £40FTO(1)=F000027.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000027.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000027.£40FTO(3)
      *
     C                   EVAL      D000027.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000027.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000027.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000027.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000027.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000027.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000027.SUPP_NUU(1)= 1
     C                   EVAL      D000027.SUPP_NUU(2)= 2
     C                   EVAL      D000027.SUPP_NUU(3)= 3
     C                   EVAL      D000027.SUPP_NUG(1)= 11
     C                   EVAL      D000027.SUPP_NUG(2)= 21
     C                   EVAL      D000027.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000027.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000027.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000027.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000027.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000027.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000027.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000027.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000027.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000027.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000027.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000027.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000027.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000028
      *--------------------------------------------------------------*
     C     R000028       BEGSR
      * This is Subroutine  R000028
     C                   CLEAR                   U000028
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000028.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000028.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000028.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000028.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000028.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000028.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000028.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000028.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000028.£UIBK1
      *
     C                   CLEAR                   P000028
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000028.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000028.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000028.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000028.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000028.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000028.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000028.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000028.£V5PNR= 5
    MU* VAL1(P000028.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000028.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000028.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000028.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000028.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000028.£V5PQT=130,425
    MU* VAL1(P000028.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000028.£V5PTD
    MU* VAL1(P000028.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000028.£V5PMO
    MU* VAL1(P000028.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000028.£V5PND
    MU* VAL1(P000028.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000028.£V5PNR
    MU* VAL1(P000028.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000028.£V5PTC
    MU* VAL1(P000028.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000028.£V5PEN
    MU* VAL1(P000028.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000028.£V5PQT
      *
     C                   EVAL      F000028.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000028.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000028.£40FLU='03'
     C                   EVAL      F000028.£40F_MSO='1'
     C                   EVAL      F000028.£40F_MWL=' '
     C                   EVAL      F000028.£40F_MWC='1'
     C                   EVAL      F000028.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000028.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000028.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000028.£40FDE
     C                   EVAL      £40FRE=F000028.£40FRE
     C                   EVAL      £40FLU=F000028.£40FLU
     C                   EVAL      £40F_MSO=F000028.£40F_MSO
     C                   EVAL      £40F_MWL=F000028.£40F_MWL
     C                   EVAL      £40F_MWC=F000028.£40F_MWC
     C                   EVAL      £40FTO(1)=F000028.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000028.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000028.£40FTO(3)
      *
     C                   EVAL      D000028.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000028.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000028.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000028.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000028.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000028.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000028.SUPP_NUU(1)= 1
     C                   EVAL      D000028.SUPP_NUU(2)= 2
     C                   EVAL      D000028.SUPP_NUU(3)= 3
     C                   EVAL      D000028.SUPP_NUG(1)= 11
     C                   EVAL      D000028.SUPP_NUG(2)= 21
     C                   EVAL      D000028.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000028.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000028.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000028.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000028.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000028.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000028.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000028.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000028.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000028.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000028.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000028.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000028.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000029
      *--------------------------------------------------------------*
     C     R000029       BEGSR
      * This is Subroutine  R000029
     C                   CLEAR                   U000029
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000029.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000029.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000029.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000029.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000029.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000029.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000029.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000029.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000029.£UIBK1
      *
     C                   CLEAR                   P000029
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000029.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000029.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000029.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000029.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000029.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000029.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000029.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000029.£V5PNR= 5
    MU* VAL1(P000029.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000029.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000029.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000029.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000029.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000029.£V5PQT=130,425
    MU* VAL1(P000029.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000029.£V5PTD
    MU* VAL1(P000029.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000029.£V5PMO
    MU* VAL1(P000029.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000029.£V5PND
    MU* VAL1(P000029.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000029.£V5PNR
    MU* VAL1(P000029.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000029.£V5PTC
    MU* VAL1(P000029.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000029.£V5PEN
    MU* VAL1(P000029.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000029.£V5PQT
      *
     C                   EVAL      F000029.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000029.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000029.£40FLU='03'
     C                   EVAL      F000029.£40F_MSO='1'
     C                   EVAL      F000029.£40F_MWL=' '
     C                   EVAL      F000029.£40F_MWC='1'
     C                   EVAL      F000029.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000029.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000029.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000029.£40FDE
     C                   EVAL      £40FRE=F000029.£40FRE
     C                   EVAL      £40FLU=F000029.£40FLU
     C                   EVAL      £40F_MSO=F000029.£40F_MSO
     C                   EVAL      £40F_MWL=F000029.£40F_MWL
     C                   EVAL      £40F_MWC=F000029.£40F_MWC
     C                   EVAL      £40FTO(1)=F000029.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000029.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000029.£40FTO(3)
      *
     C                   EVAL      D000029.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000029.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000029.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000029.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000029.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000029.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000029.SUPP_NUU(1)= 1
     C                   EVAL      D000029.SUPP_NUU(2)= 2
     C                   EVAL      D000029.SUPP_NUU(3)= 3
     C                   EVAL      D000029.SUPP_NUG(1)= 11
     C                   EVAL      D000029.SUPP_NUG(2)= 21
     C                   EVAL      D000029.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000029.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000029.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000029.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000029.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000029.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000029.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000029.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000029.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000029.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000029.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000029.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000029.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000030
      *--------------------------------------------------------------*
     C     R000030       BEGSR
      * This is Subroutine  R000030
     C                   CLEAR                   U000030
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000030.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000030.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000030.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000030.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000030.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000030.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000030.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000030.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000030.£UIBK1
      *
     C                   CLEAR                   P000030
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000030.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000030.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000030.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000030.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000030.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000030.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000030.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000030.£V5PNR= 5
    MU* VAL1(P000030.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000030.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000030.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000030.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000030.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000030.£V5PQT=130,425
    MU* VAL1(P000030.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000030.£V5PTD
    MU* VAL1(P000030.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000030.£V5PMO
    MU* VAL1(P000030.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000030.£V5PND
    MU* VAL1(P000030.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000030.£V5PNR
    MU* VAL1(P000030.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000030.£V5PTC
    MU* VAL1(P000030.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000030.£V5PEN
    MU* VAL1(P000030.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000030.£V5PQT
      *
     C                   EVAL      F000030.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000030.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000030.£40FLU='03'
     C                   EVAL      F000030.£40F_MSO='1'
     C                   EVAL      F000030.£40F_MWL=' '
     C                   EVAL      F000030.£40F_MWC='1'
     C                   EVAL      F000030.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000030.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000030.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000030.£40FDE
     C                   EVAL      £40FRE=F000030.£40FRE
     C                   EVAL      £40FLU=F000030.£40FLU
     C                   EVAL      £40F_MSO=F000030.£40F_MSO
     C                   EVAL      £40F_MWL=F000030.£40F_MWL
     C                   EVAL      £40F_MWC=F000030.£40F_MWC
     C                   EVAL      £40FTO(1)=F000030.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000030.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000030.£40FTO(3)
      *
     C                   EVAL      D000030.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000030.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000030.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000030.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000030.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000030.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000030.SUPP_NUU(1)= 1
     C                   EVAL      D000030.SUPP_NUU(2)= 2
     C                   EVAL      D000030.SUPP_NUU(3)= 3
     C                   EVAL      D000030.SUPP_NUG(1)= 11
     C                   EVAL      D000030.SUPP_NUG(2)= 21
     C                   EVAL      D000030.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000030.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000030.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000030.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000030.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000030.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000030.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000030.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000030.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000030.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000030.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000030.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000030.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000031
      *--------------------------------------------------------------*
     C     R000031       BEGSR
      * This is Subroutine  R000031
     C                   CLEAR                   U000031
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000031.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000031.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000031.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000031.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000031.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000031.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000031.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000031.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000031.£UIBK1
      *
     C                   CLEAR                   P000031
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000031.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000031.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000031.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000031.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000031.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000031.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000031.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000031.£V5PNR= 5
    MU* VAL1(P000031.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000031.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000031.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000031.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000031.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000031.£V5PQT=130,425
    MU* VAL1(P000031.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000031.£V5PTD
    MU* VAL1(P000031.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000031.£V5PMO
    MU* VAL1(P000031.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000031.£V5PND
    MU* VAL1(P000031.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000031.£V5PNR
    MU* VAL1(P000031.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000031.£V5PTC
    MU* VAL1(P000031.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000031.£V5PEN
    MU* VAL1(P000031.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000031.£V5PQT
      *
     C                   EVAL      F000031.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000031.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000031.£40FLU='03'
     C                   EVAL      F000031.£40F_MSO='1'
     C                   EVAL      F000031.£40F_MWL=' '
     C                   EVAL      F000031.£40F_MWC='1'
     C                   EVAL      F000031.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000031.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000031.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000031.£40FDE
     C                   EVAL      £40FRE=F000031.£40FRE
     C                   EVAL      £40FLU=F000031.£40FLU
     C                   EVAL      £40F_MSO=F000031.£40F_MSO
     C                   EVAL      £40F_MWL=F000031.£40F_MWL
     C                   EVAL      £40F_MWC=F000031.£40F_MWC
     C                   EVAL      £40FTO(1)=F000031.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000031.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000031.£40FTO(3)
      *
     C                   EVAL      D000031.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000031.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000031.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000031.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000031.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000031.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000031.SUPP_NUU(1)= 1
     C                   EVAL      D000031.SUPP_NUU(2)= 2
     C                   EVAL      D000031.SUPP_NUU(3)= 3
     C                   EVAL      D000031.SUPP_NUG(1)= 11
     C                   EVAL      D000031.SUPP_NUG(2)= 21
     C                   EVAL      D000031.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000031.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000031.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000031.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000031.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000031.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000031.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000031.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000031.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000031.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000031.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000031.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000031.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000032
      *--------------------------------------------------------------*
     C     R000032       BEGSR
      * This is Subroutine  R000032
     C                   CLEAR                   U000032
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000032.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000032.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000032.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000032.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000032.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000032.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000032.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000032.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000032.£UIBK1
      *
     C                   CLEAR                   P000032
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000032.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000032.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000032.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000032.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000032.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000032.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000032.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000032.£V5PNR= 5
    MU* VAL1(P000032.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000032.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000032.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000032.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000032.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000032.£V5PQT=130,425
    MU* VAL1(P000032.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000032.£V5PTD
    MU* VAL1(P000032.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000032.£V5PMO
    MU* VAL1(P000032.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000032.£V5PND
    MU* VAL1(P000032.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000032.£V5PNR
    MU* VAL1(P000032.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000032.£V5PTC
    MU* VAL1(P000032.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000032.£V5PEN
    MU* VAL1(P000032.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000032.£V5PQT
      *
     C                   EVAL      F000032.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000032.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000032.£40FLU='03'
     C                   EVAL      F000032.£40F_MSO='1'
     C                   EVAL      F000032.£40F_MWL=' '
     C                   EVAL      F000032.£40F_MWC='1'
     C                   EVAL      F000032.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000032.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000032.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000032.£40FDE
     C                   EVAL      £40FRE=F000032.£40FRE
     C                   EVAL      £40FLU=F000032.£40FLU
     C                   EVAL      £40F_MSO=F000032.£40F_MSO
     C                   EVAL      £40F_MWL=F000032.£40F_MWL
     C                   EVAL      £40F_MWC=F000032.£40F_MWC
     C                   EVAL      £40FTO(1)=F000032.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000032.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000032.£40FTO(3)
      *
     C                   EVAL      D000032.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000032.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000032.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000032.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000032.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000032.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000032.SUPP_NUU(1)= 1
     C                   EVAL      D000032.SUPP_NUU(2)= 2
     C                   EVAL      D000032.SUPP_NUU(3)= 3
     C                   EVAL      D000032.SUPP_NUG(1)= 11
     C                   EVAL      D000032.SUPP_NUG(2)= 21
     C                   EVAL      D000032.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000032.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000032.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000032.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000032.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000032.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000032.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000032.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000032.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000032.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000032.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000032.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000032.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000033
      *--------------------------------------------------------------*
     C     R000033       BEGSR
      * This is Subroutine  R000033
     C                   CLEAR                   U000033
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000033.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000033.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000033.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000033.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000033.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000033.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000033.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000033.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000033.£UIBK1
      *
     C                   CLEAR                   P000033
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000033.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000033.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000033.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000033.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000033.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000033.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000033.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000033.£V5PNR= 5
    MU* VAL1(P000033.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000033.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000033.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000033.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000033.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000033.£V5PQT=130,425
    MU* VAL1(P000033.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000033.£V5PTD
    MU* VAL1(P000033.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000033.£V5PMO
    MU* VAL1(P000033.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000033.£V5PND
    MU* VAL1(P000033.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000033.£V5PNR
    MU* VAL1(P000033.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000033.£V5PTC
    MU* VAL1(P000033.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000033.£V5PEN
    MU* VAL1(P000033.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000033.£V5PQT
      *
     C                   EVAL      F000033.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000033.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000033.£40FLU='03'
     C                   EVAL      F000033.£40F_MSO='1'
     C                   EVAL      F000033.£40F_MWL=' '
     C                   EVAL      F000033.£40F_MWC='1'
     C                   EVAL      F000033.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000033.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000033.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000033.£40FDE
     C                   EVAL      £40FRE=F000033.£40FRE
     C                   EVAL      £40FLU=F000033.£40FLU
     C                   EVAL      £40F_MSO=F000033.£40F_MSO
     C                   EVAL      £40F_MWL=F000033.£40F_MWL
     C                   EVAL      £40F_MWC=F000033.£40F_MWC
     C                   EVAL      £40FTO(1)=F000033.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000033.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000033.£40FTO(3)
      *
     C                   EVAL      D000033.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000033.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000033.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000033.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000033.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000033.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000033.SUPP_NUU(1)= 1
     C                   EVAL      D000033.SUPP_NUU(2)= 2
     C                   EVAL      D000033.SUPP_NUU(3)= 3
     C                   EVAL      D000033.SUPP_NUG(1)= 11
     C                   EVAL      D000033.SUPP_NUG(2)= 21
     C                   EVAL      D000033.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000033.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000033.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000033.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000033.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000033.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000033.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000033.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000033.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000033.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000033.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000033.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000033.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000034
      *--------------------------------------------------------------*
     C     R000034       BEGSR
      * This is Subroutine  R000034
     C                   CLEAR                   U000034
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000034.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000034.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000034.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000034.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000034.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000034.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000034.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000034.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000034.£UIBK1
      *
     C                   CLEAR                   P000034
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000034.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000034.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000034.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000034.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000034.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000034.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000034.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000034.£V5PNR= 5
    MU* VAL1(P000034.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000034.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000034.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000034.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000034.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000034.£V5PQT=130,425
    MU* VAL1(P000034.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000034.£V5PTD
    MU* VAL1(P000034.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000034.£V5PMO
    MU* VAL1(P000034.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000034.£V5PND
    MU* VAL1(P000034.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000034.£V5PNR
    MU* VAL1(P000034.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000034.£V5PTC
    MU* VAL1(P000034.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000034.£V5PEN
    MU* VAL1(P000034.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000034.£V5PQT
      *
     C                   EVAL      F000034.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000034.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000034.£40FLU='03'
     C                   EVAL      F000034.£40F_MSO='1'
     C                   EVAL      F000034.£40F_MWL=' '
     C                   EVAL      F000034.£40F_MWC='1'
     C                   EVAL      F000034.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000034.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000034.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000034.£40FDE
     C                   EVAL      £40FRE=F000034.£40FRE
     C                   EVAL      £40FLU=F000034.£40FLU
     C                   EVAL      £40F_MSO=F000034.£40F_MSO
     C                   EVAL      £40F_MWL=F000034.£40F_MWL
     C                   EVAL      £40F_MWC=F000034.£40F_MWC
     C                   EVAL      £40FTO(1)=F000034.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000034.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000034.£40FTO(3)
      *
     C                   EVAL      D000034.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000034.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000034.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000034.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000034.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000034.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000034.SUPP_NUU(1)= 1
     C                   EVAL      D000034.SUPP_NUU(2)= 2
     C                   EVAL      D000034.SUPP_NUU(3)= 3
     C                   EVAL      D000034.SUPP_NUG(1)= 11
     C                   EVAL      D000034.SUPP_NUG(2)= 21
     C                   EVAL      D000034.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000034.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000034.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000034.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000034.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000034.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000034.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000034.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000034.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000034.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000034.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000034.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000034.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000035
      *--------------------------------------------------------------*
     C     R000035       BEGSR
      * This is Subroutine  R000035
     C                   CLEAR                   U000035
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000035.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000035.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000035.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000035.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000035.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000035.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000035.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000035.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000035.£UIBK1
      *
     C                   CLEAR                   P000035
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000035.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000035.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000035.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000035.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000035.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000035.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000035.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000035.£V5PNR= 5
    MU* VAL1(P000035.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000035.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000035.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000035.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000035.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000035.£V5PQT=130,425
    MU* VAL1(P000035.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000035.£V5PTD
    MU* VAL1(P000035.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000035.£V5PMO
    MU* VAL1(P000035.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000035.£V5PND
    MU* VAL1(P000035.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000035.£V5PNR
    MU* VAL1(P000035.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000035.£V5PTC
    MU* VAL1(P000035.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000035.£V5PEN
    MU* VAL1(P000035.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000035.£V5PQT
      *
     C                   EVAL      F000035.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000035.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000035.£40FLU='03'
     C                   EVAL      F000035.£40F_MSO='1'
     C                   EVAL      F000035.£40F_MWL=' '
     C                   EVAL      F000035.£40F_MWC='1'
     C                   EVAL      F000035.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000035.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000035.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000035.£40FDE
     C                   EVAL      £40FRE=F000035.£40FRE
     C                   EVAL      £40FLU=F000035.£40FLU
     C                   EVAL      £40F_MSO=F000035.£40F_MSO
     C                   EVAL      £40F_MWL=F000035.£40F_MWL
     C                   EVAL      £40F_MWC=F000035.£40F_MWC
     C                   EVAL      £40FTO(1)=F000035.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000035.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000035.£40FTO(3)
      *
     C                   EVAL      D000035.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000035.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000035.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000035.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000035.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000035.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000035.SUPP_NUU(1)= 1
     C                   EVAL      D000035.SUPP_NUU(2)= 2
     C                   EVAL      D000035.SUPP_NUU(3)= 3
     C                   EVAL      D000035.SUPP_NUG(1)= 11
     C                   EVAL      D000035.SUPP_NUG(2)= 21
     C                   EVAL      D000035.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000035.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000035.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000035.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000035.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000035.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000035.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000035.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000035.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000035.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000035.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000035.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000035.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000036
      *--------------------------------------------------------------*
     C     R000036       BEGSR
      * This is Subroutine  R000036
     C                   CLEAR                   U000036
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000036.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000036.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000036.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000036.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000036.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000036.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000036.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000036.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000036.£UIBK1
      *
     C                   CLEAR                   P000036
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000036.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000036.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000036.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000036.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000036.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000036.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000036.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000036.£V5PNR= 5
    MU* VAL1(P000036.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000036.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000036.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000036.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000036.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000036.£V5PQT=130,425
    MU* VAL1(P000036.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000036.£V5PTD
    MU* VAL1(P000036.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000036.£V5PMO
    MU* VAL1(P000036.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000036.£V5PND
    MU* VAL1(P000036.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000036.£V5PNR
    MU* VAL1(P000036.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000036.£V5PTC
    MU* VAL1(P000036.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000036.£V5PEN
    MU* VAL1(P000036.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000036.£V5PQT
      *
     C                   EVAL      F000036.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000036.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000036.£40FLU='03'
     C                   EVAL      F000036.£40F_MSO='1'
     C                   EVAL      F000036.£40F_MWL=' '
     C                   EVAL      F000036.£40F_MWC='1'
     C                   EVAL      F000036.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000036.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000036.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000036.£40FDE
     C                   EVAL      £40FRE=F000036.£40FRE
     C                   EVAL      £40FLU=F000036.£40FLU
     C                   EVAL      £40F_MSO=F000036.£40F_MSO
     C                   EVAL      £40F_MWL=F000036.£40F_MWL
     C                   EVAL      £40F_MWC=F000036.£40F_MWC
     C                   EVAL      £40FTO(1)=F000036.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000036.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000036.£40FTO(3)
      *
     C                   EVAL      D000036.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000036.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000036.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000036.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000036.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000036.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000036.SUPP_NUU(1)= 1
     C                   EVAL      D000036.SUPP_NUU(2)= 2
     C                   EVAL      D000036.SUPP_NUU(3)= 3
     C                   EVAL      D000036.SUPP_NUG(1)= 11
     C                   EVAL      D000036.SUPP_NUG(2)= 21
     C                   EVAL      D000036.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000036.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000036.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000036.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000036.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000036.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000036.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000036.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000036.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000036.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000036.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000036.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000036.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000037
      *--------------------------------------------------------------*
     C     R000037       BEGSR
      * This is Subroutine  R000037
     C                   CLEAR                   U000037
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000037.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000037.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000037.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000037.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000037.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000037.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000037.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000037.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000037.£UIBK1
      *
     C                   CLEAR                   P000037
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000037.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000037.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000037.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000037.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000037.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000037.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000037.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000037.£V5PNR= 5
    MU* VAL1(P000037.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000037.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000037.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000037.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000037.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000037.£V5PQT=130,425
    MU* VAL1(P000037.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000037.£V5PTD
    MU* VAL1(P000037.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000037.£V5PMO
    MU* VAL1(P000037.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000037.£V5PND
    MU* VAL1(P000037.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000037.£V5PNR
    MU* VAL1(P000037.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000037.£V5PTC
    MU* VAL1(P000037.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000037.£V5PEN
    MU* VAL1(P000037.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000037.£V5PQT
      *
     C                   EVAL      F000037.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000037.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000037.£40FLU='03'
     C                   EVAL      F000037.£40F_MSO='1'
     C                   EVAL      F000037.£40F_MWL=' '
     C                   EVAL      F000037.£40F_MWC='1'
     C                   EVAL      F000037.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000037.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000037.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000037.£40FDE
     C                   EVAL      £40FRE=F000037.£40FRE
     C                   EVAL      £40FLU=F000037.£40FLU
     C                   EVAL      £40F_MSO=F000037.£40F_MSO
     C                   EVAL      £40F_MWL=F000037.£40F_MWL
     C                   EVAL      £40F_MWC=F000037.£40F_MWC
     C                   EVAL      £40FTO(1)=F000037.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000037.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000037.£40FTO(3)
      *
     C                   EVAL      D000037.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000037.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000037.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000037.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000037.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000037.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000037.SUPP_NUU(1)= 1
     C                   EVAL      D000037.SUPP_NUU(2)= 2
     C                   EVAL      D000037.SUPP_NUU(3)= 3
     C                   EVAL      D000037.SUPP_NUG(1)= 11
     C                   EVAL      D000037.SUPP_NUG(2)= 21
     C                   EVAL      D000037.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000037.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000037.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000037.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000037.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000037.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000037.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000037.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000037.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000037.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000037.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000037.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000037.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000038
      *--------------------------------------------------------------*
     C     R000038       BEGSR
      * This is Subroutine  R000038
     C                   CLEAR                   U000038
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000038.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000038.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000038.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000038.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000038.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000038.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000038.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000038.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000038.£UIBK1
      *
     C                   CLEAR                   P000038
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000038.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000038.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000038.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000038.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000038.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000038.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000038.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000038.£V5PNR= 5
    MU* VAL1(P000038.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000038.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000038.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000038.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000038.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000038.£V5PQT=130,425
    MU* VAL1(P000038.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000038.£V5PTD
    MU* VAL1(P000038.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000038.£V5PMO
    MU* VAL1(P000038.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000038.£V5PND
    MU* VAL1(P000038.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000038.£V5PNR
    MU* VAL1(P000038.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000038.£V5PTC
    MU* VAL1(P000038.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000038.£V5PEN
    MU* VAL1(P000038.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000038.£V5PQT
      *
     C                   EVAL      F000038.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000038.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000038.£40FLU='03'
     C                   EVAL      F000038.£40F_MSO='1'
     C                   EVAL      F000038.£40F_MWL=' '
     C                   EVAL      F000038.£40F_MWC='1'
     C                   EVAL      F000038.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000038.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000038.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000038.£40FDE
     C                   EVAL      £40FRE=F000038.£40FRE
     C                   EVAL      £40FLU=F000038.£40FLU
     C                   EVAL      £40F_MSO=F000038.£40F_MSO
     C                   EVAL      £40F_MWL=F000038.£40F_MWL
     C                   EVAL      £40F_MWC=F000038.£40F_MWC
     C                   EVAL      £40FTO(1)=F000038.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000038.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000038.£40FTO(3)
      *
     C                   EVAL      D000038.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000038.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000038.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000038.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000038.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000038.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000038.SUPP_NUU(1)= 1
     C                   EVAL      D000038.SUPP_NUU(2)= 2
     C                   EVAL      D000038.SUPP_NUU(3)= 3
     C                   EVAL      D000038.SUPP_NUG(1)= 11
     C                   EVAL      D000038.SUPP_NUG(2)= 21
     C                   EVAL      D000038.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000038.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000038.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000038.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000038.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000038.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000038.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000038.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000038.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000038.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000038.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000038.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000038.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000039
      *--------------------------------------------------------------*
     C     R000039       BEGSR
      * This is Subroutine  R000039
     C                   CLEAR                   U000039
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000039.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000039.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000039.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000039.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000039.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000039.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000039.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000039.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000039.£UIBK1
      *
     C                   CLEAR                   P000039
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000039.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000039.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000039.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000039.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000039.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000039.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000039.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000039.£V5PNR= 5
    MU* VAL1(P000039.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000039.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000039.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000039.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000039.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000039.£V5PQT=130,425
    MU* VAL1(P000039.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000039.£V5PTD
    MU* VAL1(P000039.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000039.£V5PMO
    MU* VAL1(P000039.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000039.£V5PND
    MU* VAL1(P000039.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000039.£V5PNR
    MU* VAL1(P000039.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000039.£V5PTC
    MU* VAL1(P000039.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000039.£V5PEN
    MU* VAL1(P000039.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000039.£V5PQT
      *
     C                   EVAL      F000039.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000039.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000039.£40FLU='03'
     C                   EVAL      F000039.£40F_MSO='1'
     C                   EVAL      F000039.£40F_MWL=' '
     C                   EVAL      F000039.£40F_MWC='1'
     C                   EVAL      F000039.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000039.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000039.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000039.£40FDE
     C                   EVAL      £40FRE=F000039.£40FRE
     C                   EVAL      £40FLU=F000039.£40FLU
     C                   EVAL      £40F_MSO=F000039.£40F_MSO
     C                   EVAL      £40F_MWL=F000039.£40F_MWL
     C                   EVAL      £40F_MWC=F000039.£40F_MWC
     C                   EVAL      £40FTO(1)=F000039.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000039.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000039.£40FTO(3)
      *
     C                   EVAL      D000039.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000039.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000039.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000039.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000039.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000039.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000039.SUPP_NUU(1)= 1
     C                   EVAL      D000039.SUPP_NUU(2)= 2
     C                   EVAL      D000039.SUPP_NUU(3)= 3
     C                   EVAL      D000039.SUPP_NUG(1)= 11
     C                   EVAL      D000039.SUPP_NUG(2)= 21
     C                   EVAL      D000039.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000039.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000039.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000039.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000039.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000039.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000039.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000039.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000039.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000039.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000039.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000039.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000039.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000040
      *--------------------------------------------------------------*
     C     R000040       BEGSR
      * This is Subroutine  R000040
     C                   CLEAR                   U000040
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000040.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000040.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000040.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000040.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000040.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000040.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000040.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000040.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000040.£UIBK1
      *
     C                   CLEAR                   P000040
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000040.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000040.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000040.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000040.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000040.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000040.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000040.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000040.£V5PNR= 5
    MU* VAL1(P000040.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000040.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000040.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000040.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000040.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000040.£V5PQT=130,425
    MU* VAL1(P000040.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000040.£V5PTD
    MU* VAL1(P000040.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000040.£V5PMO
    MU* VAL1(P000040.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000040.£V5PND
    MU* VAL1(P000040.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000040.£V5PNR
    MU* VAL1(P000040.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000040.£V5PTC
    MU* VAL1(P000040.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000040.£V5PEN
    MU* VAL1(P000040.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000040.£V5PQT
      *
     C                   EVAL      F000040.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000040.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000040.£40FLU='03'
     C                   EVAL      F000040.£40F_MSO='1'
     C                   EVAL      F000040.£40F_MWL=' '
     C                   EVAL      F000040.£40F_MWC='1'
     C                   EVAL      F000040.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000040.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000040.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000040.£40FDE
     C                   EVAL      £40FRE=F000040.£40FRE
     C                   EVAL      £40FLU=F000040.£40FLU
     C                   EVAL      £40F_MSO=F000040.£40F_MSO
     C                   EVAL      £40F_MWL=F000040.£40F_MWL
     C                   EVAL      £40F_MWC=F000040.£40F_MWC
     C                   EVAL      £40FTO(1)=F000040.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000040.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000040.£40FTO(3)
      *
     C                   EVAL      D000040.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000040.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000040.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000040.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000040.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000040.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000040.SUPP_NUU(1)= 1
     C                   EVAL      D000040.SUPP_NUU(2)= 2
     C                   EVAL      D000040.SUPP_NUU(3)= 3
     C                   EVAL      D000040.SUPP_NUG(1)= 11
     C                   EVAL      D000040.SUPP_NUG(2)= 21
     C                   EVAL      D000040.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000040.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000040.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000040.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000040.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000040.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000040.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000040.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000040.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000040.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000040.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000040.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000040.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000041
      *--------------------------------------------------------------*
     C     R000041       BEGSR
      * This is Subroutine  R000041
     C                   CLEAR                   U000041
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000041.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000041.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000041.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000041.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000041.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000041.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000041.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000041.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000041.£UIBK1
      *
     C                   CLEAR                   P000041
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000041.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000041.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000041.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000041.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000041.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000041.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000041.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000041.£V5PNR= 5
    MU* VAL1(P000041.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000041.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000041.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000041.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000041.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000041.£V5PQT=130,425
    MU* VAL1(P000041.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000041.£V5PTD
    MU* VAL1(P000041.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000041.£V5PMO
    MU* VAL1(P000041.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000041.£V5PND
    MU* VAL1(P000041.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000041.£V5PNR
    MU* VAL1(P000041.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000041.£V5PTC
    MU* VAL1(P000041.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000041.£V5PEN
    MU* VAL1(P000041.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000041.£V5PQT
      *
     C                   EVAL      F000041.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000041.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000041.£40FLU='03'
     C                   EVAL      F000041.£40F_MSO='1'
     C                   EVAL      F000041.£40F_MWL=' '
     C                   EVAL      F000041.£40F_MWC='1'
     C                   EVAL      F000041.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000041.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000041.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000041.£40FDE
     C                   EVAL      £40FRE=F000041.£40FRE
     C                   EVAL      £40FLU=F000041.£40FLU
     C                   EVAL      £40F_MSO=F000041.£40F_MSO
     C                   EVAL      £40F_MWL=F000041.£40F_MWL
     C                   EVAL      £40F_MWC=F000041.£40F_MWC
     C                   EVAL      £40FTO(1)=F000041.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000041.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000041.£40FTO(3)
      *
     C                   EVAL      D000041.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000041.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000041.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000041.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000041.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000041.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000041.SUPP_NUU(1)= 1
     C                   EVAL      D000041.SUPP_NUU(2)= 2
     C                   EVAL      D000041.SUPP_NUU(3)= 3
     C                   EVAL      D000041.SUPP_NUG(1)= 11
     C                   EVAL      D000041.SUPP_NUG(2)= 21
     C                   EVAL      D000041.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000041.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000041.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000041.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000041.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000041.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000041.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000041.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000041.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000041.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000041.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000041.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000041.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000042
      *--------------------------------------------------------------*
     C     R000042       BEGSR
      * This is Subroutine  R000042
     C                   CLEAR                   U000042
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000042.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000042.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000042.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000042.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000042.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000042.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000042.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000042.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000042.£UIBK1
      *
     C                   CLEAR                   P000042
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000042.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000042.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000042.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000042.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000042.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000042.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000042.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000042.£V5PNR= 5
    MU* VAL1(P000042.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000042.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000042.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000042.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000042.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000042.£V5PQT=130,425
    MU* VAL1(P000042.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000042.£V5PTD
    MU* VAL1(P000042.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000042.£V5PMO
    MU* VAL1(P000042.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000042.£V5PND
    MU* VAL1(P000042.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000042.£V5PNR
    MU* VAL1(P000042.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000042.£V5PTC
    MU* VAL1(P000042.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000042.£V5PEN
    MU* VAL1(P000042.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000042.£V5PQT
      *
     C                   EVAL      F000042.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000042.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000042.£40FLU='03'
     C                   EVAL      F000042.£40F_MSO='1'
     C                   EVAL      F000042.£40F_MWL=' '
     C                   EVAL      F000042.£40F_MWC='1'
     C                   EVAL      F000042.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000042.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000042.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000042.£40FDE
     C                   EVAL      £40FRE=F000042.£40FRE
     C                   EVAL      £40FLU=F000042.£40FLU
     C                   EVAL      £40F_MSO=F000042.£40F_MSO
     C                   EVAL      £40F_MWL=F000042.£40F_MWL
     C                   EVAL      £40F_MWC=F000042.£40F_MWC
     C                   EVAL      £40FTO(1)=F000042.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000042.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000042.£40FTO(3)
      *
     C                   EVAL      D000042.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000042.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000042.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000042.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000042.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000042.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000042.SUPP_NUU(1)= 1
     C                   EVAL      D000042.SUPP_NUU(2)= 2
     C                   EVAL      D000042.SUPP_NUU(3)= 3
     C                   EVAL      D000042.SUPP_NUG(1)= 11
     C                   EVAL      D000042.SUPP_NUG(2)= 21
     C                   EVAL      D000042.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000042.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000042.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000042.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000042.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000042.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000042.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000042.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000042.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000042.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000042.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000042.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000042.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000043
      *--------------------------------------------------------------*
     C     R000043       BEGSR
      * This is Subroutine  R000043
     C                   CLEAR                   U000043
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000043.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000043.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000043.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000043.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000043.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000043.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000043.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000043.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000043.£UIBK1
      *
     C                   CLEAR                   P000043
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000043.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000043.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000043.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000043.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000043.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000043.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000043.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000043.£V5PNR= 5
    MU* VAL1(P000043.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000043.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000043.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000043.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000043.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000043.£V5PQT=130,425
    MU* VAL1(P000043.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000043.£V5PTD
    MU* VAL1(P000043.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000043.£V5PMO
    MU* VAL1(P000043.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000043.£V5PND
    MU* VAL1(P000043.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000043.£V5PNR
    MU* VAL1(P000043.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000043.£V5PTC
    MU* VAL1(P000043.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000043.£V5PEN
    MU* VAL1(P000043.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000043.£V5PQT
      *
     C                   EVAL      F000043.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000043.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000043.£40FLU='03'
     C                   EVAL      F000043.£40F_MSO='1'
     C                   EVAL      F000043.£40F_MWL=' '
     C                   EVAL      F000043.£40F_MWC='1'
     C                   EVAL      F000043.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000043.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000043.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000043.£40FDE
     C                   EVAL      £40FRE=F000043.£40FRE
     C                   EVAL      £40FLU=F000043.£40FLU
     C                   EVAL      £40F_MSO=F000043.£40F_MSO
     C                   EVAL      £40F_MWL=F000043.£40F_MWL
     C                   EVAL      £40F_MWC=F000043.£40F_MWC
     C                   EVAL      £40FTO(1)=F000043.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000043.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000043.£40FTO(3)
      *
     C                   EVAL      D000043.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000043.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000043.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000043.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000043.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000043.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000043.SUPP_NUU(1)= 1
     C                   EVAL      D000043.SUPP_NUU(2)= 2
     C                   EVAL      D000043.SUPP_NUU(3)= 3
     C                   EVAL      D000043.SUPP_NUG(1)= 11
     C                   EVAL      D000043.SUPP_NUG(2)= 21
     C                   EVAL      D000043.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000043.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000043.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000043.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000043.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000043.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000043.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000043.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000043.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000043.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000043.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000043.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000043.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000044
      *--------------------------------------------------------------*
     C     R000044       BEGSR
      * This is Subroutine  R000044
     C                   CLEAR                   U000044
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000044.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000044.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000044.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000044.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000044.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000044.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000044.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000044.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000044.£UIBK1
      *
     C                   CLEAR                   P000044
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000044.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000044.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000044.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000044.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000044.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000044.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000044.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000044.£V5PNR= 5
    MU* VAL1(P000044.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000044.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000044.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000044.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000044.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000044.£V5PQT=130,425
    MU* VAL1(P000044.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000044.£V5PTD
    MU* VAL1(P000044.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000044.£V5PMO
    MU* VAL1(P000044.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000044.£V5PND
    MU* VAL1(P000044.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000044.£V5PNR
    MU* VAL1(P000044.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000044.£V5PTC
    MU* VAL1(P000044.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000044.£V5PEN
    MU* VAL1(P000044.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000044.£V5PQT
      *
     C                   EVAL      F000044.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000044.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000044.£40FLU='03'
     C                   EVAL      F000044.£40F_MSO='1'
     C                   EVAL      F000044.£40F_MWL=' '
     C                   EVAL      F000044.£40F_MWC='1'
     C                   EVAL      F000044.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000044.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000044.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000044.£40FDE
     C                   EVAL      £40FRE=F000044.£40FRE
     C                   EVAL      £40FLU=F000044.£40FLU
     C                   EVAL      £40F_MSO=F000044.£40F_MSO
     C                   EVAL      £40F_MWL=F000044.£40F_MWL
     C                   EVAL      £40F_MWC=F000044.£40F_MWC
     C                   EVAL      £40FTO(1)=F000044.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000044.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000044.£40FTO(3)
      *
     C                   EVAL      D000044.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000044.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000044.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000044.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000044.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000044.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000044.SUPP_NUU(1)= 1
     C                   EVAL      D000044.SUPP_NUU(2)= 2
     C                   EVAL      D000044.SUPP_NUU(3)= 3
     C                   EVAL      D000044.SUPP_NUG(1)= 11
     C                   EVAL      D000044.SUPP_NUG(2)= 21
     C                   EVAL      D000044.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000044.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000044.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000044.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000044.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000044.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000044.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000044.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000044.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000044.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000044.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000044.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000044.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000045
      *--------------------------------------------------------------*
     C     R000045       BEGSR
      * This is Subroutine  R000045
     C                   CLEAR                   U000045
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000045.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000045.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000045.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000045.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000045.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000045.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000045.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000045.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000045.£UIBK1
      *
     C                   CLEAR                   P000045
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000045.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000045.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000045.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000045.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000045.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000045.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000045.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000045.£V5PNR= 5
    MU* VAL1(P000045.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000045.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000045.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000045.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000045.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000045.£V5PQT=130,425
    MU* VAL1(P000045.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000045.£V5PTD
    MU* VAL1(P000045.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000045.£V5PMO
    MU* VAL1(P000045.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000045.£V5PND
    MU* VAL1(P000045.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000045.£V5PNR
    MU* VAL1(P000045.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000045.£V5PTC
    MU* VAL1(P000045.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000045.£V5PEN
    MU* VAL1(P000045.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000045.£V5PQT
      *
     C                   EVAL      F000045.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000045.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000045.£40FLU='03'
     C                   EVAL      F000045.£40F_MSO='1'
     C                   EVAL      F000045.£40F_MWL=' '
     C                   EVAL      F000045.£40F_MWC='1'
     C                   EVAL      F000045.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000045.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000045.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000045.£40FDE
     C                   EVAL      £40FRE=F000045.£40FRE
     C                   EVAL      £40FLU=F000045.£40FLU
     C                   EVAL      £40F_MSO=F000045.£40F_MSO
     C                   EVAL      £40F_MWL=F000045.£40F_MWL
     C                   EVAL      £40F_MWC=F000045.£40F_MWC
     C                   EVAL      £40FTO(1)=F000045.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000045.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000045.£40FTO(3)
      *
     C                   EVAL      D000045.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000045.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000045.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000045.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000045.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000045.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000045.SUPP_NUU(1)= 1
     C                   EVAL      D000045.SUPP_NUU(2)= 2
     C                   EVAL      D000045.SUPP_NUU(3)= 3
     C                   EVAL      D000045.SUPP_NUG(1)= 11
     C                   EVAL      D000045.SUPP_NUG(2)= 21
     C                   EVAL      D000045.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000045.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000045.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000045.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000045.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000045.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000045.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000045.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000045.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000045.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000045.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000045.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000045.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000046
      *--------------------------------------------------------------*
     C     R000046       BEGSR
      * This is Subroutine  R000046
     C                   CLEAR                   U000046
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000046.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000046.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000046.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000046.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000046.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000046.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000046.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000046.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000046.£UIBK1
      *
     C                   CLEAR                   P000046
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000046.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000046.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000046.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000046.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000046.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000046.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000046.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000046.£V5PNR= 5
    MU* VAL1(P000046.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000046.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000046.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000046.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000046.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000046.£V5PQT=130,425
    MU* VAL1(P000046.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000046.£V5PTD
    MU* VAL1(P000046.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000046.£V5PMO
    MU* VAL1(P000046.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000046.£V5PND
    MU* VAL1(P000046.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000046.£V5PNR
    MU* VAL1(P000046.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000046.£V5PTC
    MU* VAL1(P000046.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000046.£V5PEN
    MU* VAL1(P000046.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000046.£V5PQT
      *
     C                   EVAL      F000046.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000046.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000046.£40FLU='03'
     C                   EVAL      F000046.£40F_MSO='1'
     C                   EVAL      F000046.£40F_MWL=' '
     C                   EVAL      F000046.£40F_MWC='1'
     C                   EVAL      F000046.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000046.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000046.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000046.£40FDE
     C                   EVAL      £40FRE=F000046.£40FRE
     C                   EVAL      £40FLU=F000046.£40FLU
     C                   EVAL      £40F_MSO=F000046.£40F_MSO
     C                   EVAL      £40F_MWL=F000046.£40F_MWL
     C                   EVAL      £40F_MWC=F000046.£40F_MWC
     C                   EVAL      £40FTO(1)=F000046.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000046.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000046.£40FTO(3)
      *
     C                   EVAL      D000046.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000046.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000046.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000046.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000046.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000046.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000046.SUPP_NUU(1)= 1
     C                   EVAL      D000046.SUPP_NUU(2)= 2
     C                   EVAL      D000046.SUPP_NUU(3)= 3
     C                   EVAL      D000046.SUPP_NUG(1)= 11
     C                   EVAL      D000046.SUPP_NUG(2)= 21
     C                   EVAL      D000046.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000046.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000046.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000046.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000046.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000046.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000046.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000046.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000046.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000046.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000046.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000046.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000046.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000047
      *--------------------------------------------------------------*
     C     R000047       BEGSR
      * This is Subroutine  R000047
     C                   CLEAR                   U000047
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000047.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000047.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000047.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000047.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000047.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000047.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000047.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000047.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000047.£UIBK1
      *
     C                   CLEAR                   P000047
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000047.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000047.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000047.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000047.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000047.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000047.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000047.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000047.£V5PNR= 5
    MU* VAL1(P000047.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000047.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000047.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000047.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000047.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000047.£V5PQT=130,425
    MU* VAL1(P000047.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000047.£V5PTD
    MU* VAL1(P000047.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000047.£V5PMO
    MU* VAL1(P000047.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000047.£V5PND
    MU* VAL1(P000047.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000047.£V5PNR
    MU* VAL1(P000047.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000047.£V5PTC
    MU* VAL1(P000047.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000047.£V5PEN
    MU* VAL1(P000047.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000047.£V5PQT
      *
     C                   EVAL      F000047.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000047.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000047.£40FLU='03'
     C                   EVAL      F000047.£40F_MSO='1'
     C                   EVAL      F000047.£40F_MWL=' '
     C                   EVAL      F000047.£40F_MWC='1'
     C                   EVAL      F000047.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000047.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000047.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000047.£40FDE
     C                   EVAL      £40FRE=F000047.£40FRE
     C                   EVAL      £40FLU=F000047.£40FLU
     C                   EVAL      £40F_MSO=F000047.£40F_MSO
     C                   EVAL      £40F_MWL=F000047.£40F_MWL
     C                   EVAL      £40F_MWC=F000047.£40F_MWC
     C                   EVAL      £40FTO(1)=F000047.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000047.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000047.£40FTO(3)
      *
     C                   EVAL      D000047.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000047.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000047.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000047.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000047.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000047.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000047.SUPP_NUU(1)= 1
     C                   EVAL      D000047.SUPP_NUU(2)= 2
     C                   EVAL      D000047.SUPP_NUU(3)= 3
     C                   EVAL      D000047.SUPP_NUG(1)= 11
     C                   EVAL      D000047.SUPP_NUG(2)= 21
     C                   EVAL      D000047.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000047.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000047.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000047.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000047.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000047.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000047.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000047.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000047.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000047.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000047.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000047.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000047.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000048
      *--------------------------------------------------------------*
     C     R000048       BEGSR
      * This is Subroutine  R000048
     C                   CLEAR                   U000048
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000048.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000048.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000048.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000048.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000048.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000048.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000048.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000048.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000048.£UIBK1
      *
     C                   CLEAR                   P000048
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000048.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000048.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000048.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000048.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000048.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000048.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000048.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000048.£V5PNR= 5
    MU* VAL1(P000048.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000048.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000048.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000048.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000048.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000048.£V5PQT=130,425
    MU* VAL1(P000048.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000048.£V5PTD
    MU* VAL1(P000048.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000048.£V5PMO
    MU* VAL1(P000048.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000048.£V5PND
    MU* VAL1(P000048.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000048.£V5PNR
    MU* VAL1(P000048.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000048.£V5PTC
    MU* VAL1(P000048.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000048.£V5PEN
    MU* VAL1(P000048.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000048.£V5PQT
      *
     C                   EVAL      F000048.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000048.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000048.£40FLU='03'
     C                   EVAL      F000048.£40F_MSO='1'
     C                   EVAL      F000048.£40F_MWL=' '
     C                   EVAL      F000048.£40F_MWC='1'
     C                   EVAL      F000048.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000048.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000048.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000048.£40FDE
     C                   EVAL      £40FRE=F000048.£40FRE
     C                   EVAL      £40FLU=F000048.£40FLU
     C                   EVAL      £40F_MSO=F000048.£40F_MSO
     C                   EVAL      £40F_MWL=F000048.£40F_MWL
     C                   EVAL      £40F_MWC=F000048.£40F_MWC
     C                   EVAL      £40FTO(1)=F000048.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000048.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000048.£40FTO(3)
      *
     C                   EVAL      D000048.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000048.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000048.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000048.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000048.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000048.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000048.SUPP_NUU(1)= 1
     C                   EVAL      D000048.SUPP_NUU(2)= 2
     C                   EVAL      D000048.SUPP_NUU(3)= 3
     C                   EVAL      D000048.SUPP_NUG(1)= 11
     C                   EVAL      D000048.SUPP_NUG(2)= 21
     C                   EVAL      D000048.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000048.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000048.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000048.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000048.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000048.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000048.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000048.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000048.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000048.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000048.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000048.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000048.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000049
      *--------------------------------------------------------------*
     C     R000049       BEGSR
      * This is Subroutine  R000049
     C                   CLEAR                   U000049
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000049.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000049.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000049.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000049.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000049.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000049.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000049.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000049.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000049.£UIBK1
      *
     C                   CLEAR                   P000049
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000049.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000049.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000049.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000049.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000049.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000049.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000049.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000049.£V5PNR= 5
    MU* VAL1(P000049.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000049.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000049.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000049.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000049.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000049.£V5PQT=130,425
    MU* VAL1(P000049.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000049.£V5PTD
    MU* VAL1(P000049.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000049.£V5PMO
    MU* VAL1(P000049.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000049.£V5PND
    MU* VAL1(P000049.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000049.£V5PNR
    MU* VAL1(P000049.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000049.£V5PTC
    MU* VAL1(P000049.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000049.£V5PEN
    MU* VAL1(P000049.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000049.£V5PQT
      *
     C                   EVAL      F000049.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000049.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000049.£40FLU='03'
     C                   EVAL      F000049.£40F_MSO='1'
     C                   EVAL      F000049.£40F_MWL=' '
     C                   EVAL      F000049.£40F_MWC='1'
     C                   EVAL      F000049.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000049.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000049.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000049.£40FDE
     C                   EVAL      £40FRE=F000049.£40FRE
     C                   EVAL      £40FLU=F000049.£40FLU
     C                   EVAL      £40F_MSO=F000049.£40F_MSO
     C                   EVAL      £40F_MWL=F000049.£40F_MWL
     C                   EVAL      £40F_MWC=F000049.£40F_MWC
     C                   EVAL      £40FTO(1)=F000049.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000049.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000049.£40FTO(3)
      *
     C                   EVAL      D000049.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000049.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000049.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000049.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000049.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000049.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000049.SUPP_NUU(1)= 1
     C                   EVAL      D000049.SUPP_NUU(2)= 2
     C                   EVAL      D000049.SUPP_NUU(3)= 3
     C                   EVAL      D000049.SUPP_NUG(1)= 11
     C                   EVAL      D000049.SUPP_NUG(2)= 21
     C                   EVAL      D000049.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000049.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000049.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000049.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000049.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000049.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000049.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000049.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000049.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000049.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000049.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000049.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000049.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000050
      *--------------------------------------------------------------*
     C     R000050       BEGSR
      * This is Subroutine  R000050
     C                   CLEAR                   U000050
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000050.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000050.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000050.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000050.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000050.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000050.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000050.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000050.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000050.£UIBK1
      *
     C                   CLEAR                   P000050
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000050.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000050.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000050.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000050.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000050.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000050.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000050.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000050.£V5PNR= 5
    MU* VAL1(P000050.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000050.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000050.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000050.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000050.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000050.£V5PQT=130,425
    MU* VAL1(P000050.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000050.£V5PTD
    MU* VAL1(P000050.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000050.£V5PMO
    MU* VAL1(P000050.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000050.£V5PND
    MU* VAL1(P000050.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000050.£V5PNR
    MU* VAL1(P000050.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000050.£V5PTC
    MU* VAL1(P000050.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000050.£V5PEN
    MU* VAL1(P000050.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000050.£V5PQT
      *
     C                   EVAL      F000050.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000050.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000050.£40FLU='03'
     C                   EVAL      F000050.£40F_MSO='1'
     C                   EVAL      F000050.£40F_MWL=' '
     C                   EVAL      F000050.£40F_MWC='1'
     C                   EVAL      F000050.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000050.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000050.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000050.£40FDE
     C                   EVAL      £40FRE=F000050.£40FRE
     C                   EVAL      £40FLU=F000050.£40FLU
     C                   EVAL      £40F_MSO=F000050.£40F_MSO
     C                   EVAL      £40F_MWL=F000050.£40F_MWL
     C                   EVAL      £40F_MWC=F000050.£40F_MWC
     C                   EVAL      £40FTO(1)=F000050.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000050.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000050.£40FTO(3)
      *
     C                   EVAL      D000050.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000050.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000050.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000050.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000050.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000050.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000050.SUPP_NUU(1)= 1
     C                   EVAL      D000050.SUPP_NUU(2)= 2
     C                   EVAL      D000050.SUPP_NUU(3)= 3
     C                   EVAL      D000050.SUPP_NUG(1)= 11
     C                   EVAL      D000050.SUPP_NUG(2)= 21
     C                   EVAL      D000050.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000050.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000050.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000050.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000050.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000050.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000050.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000050.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000050.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000050.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000050.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000050.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000050.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000051
      *--------------------------------------------------------------*
     C     R000051       BEGSR
      * This is Subroutine  R000051
     C                   CLEAR                   U000051
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000051.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000051.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000051.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000051.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000051.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000051.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000051.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000051.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000051.£UIBK1
      *
     C                   CLEAR                   P000051
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000051.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000051.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000051.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000051.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000051.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000051.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000051.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000051.£V5PNR= 5
    MU* VAL1(P000051.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000051.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000051.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000051.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000051.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000051.£V5PQT=130,425
    MU* VAL1(P000051.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000051.£V5PTD
    MU* VAL1(P000051.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000051.£V5PMO
    MU* VAL1(P000051.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000051.£V5PND
    MU* VAL1(P000051.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000051.£V5PNR
    MU* VAL1(P000051.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000051.£V5PTC
    MU* VAL1(P000051.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000051.£V5PEN
    MU* VAL1(P000051.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000051.£V5PQT
      *
     C                   EVAL      F000051.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000051.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000051.£40FLU='03'
     C                   EVAL      F000051.£40F_MSO='1'
     C                   EVAL      F000051.£40F_MWL=' '
     C                   EVAL      F000051.£40F_MWC='1'
     C                   EVAL      F000051.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000051.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000051.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000051.£40FDE
     C                   EVAL      £40FRE=F000051.£40FRE
     C                   EVAL      £40FLU=F000051.£40FLU
     C                   EVAL      £40F_MSO=F000051.£40F_MSO
     C                   EVAL      £40F_MWL=F000051.£40F_MWL
     C                   EVAL      £40F_MWC=F000051.£40F_MWC
     C                   EVAL      £40FTO(1)=F000051.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000051.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000051.£40FTO(3)
      *
     C                   EVAL      D000051.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000051.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000051.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000051.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000051.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000051.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000051.SUPP_NUU(1)= 1
     C                   EVAL      D000051.SUPP_NUU(2)= 2
     C                   EVAL      D000051.SUPP_NUU(3)= 3
     C                   EVAL      D000051.SUPP_NUG(1)= 11
     C                   EVAL      D000051.SUPP_NUG(2)= 21
     C                   EVAL      D000051.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000051.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000051.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000051.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000051.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000051.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000051.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000051.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000051.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000051.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000051.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000051.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000051.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000052
      *--------------------------------------------------------------*
     C     R000052       BEGSR
      * This is Subroutine  R000052
     C                   CLEAR                   U000052
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000052.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000052.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000052.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000052.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000052.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000052.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000052.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000052.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000052.£UIBK1
      *
     C                   CLEAR                   P000052
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000052.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000052.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000052.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000052.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000052.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000052.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000052.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000052.£V5PNR= 5
    MU* VAL1(P000052.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000052.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000052.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000052.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000052.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000052.£V5PQT=130,425
    MU* VAL1(P000052.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000052.£V5PTD
    MU* VAL1(P000052.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000052.£V5PMO
    MU* VAL1(P000052.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000052.£V5PND
    MU* VAL1(P000052.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000052.£V5PNR
    MU* VAL1(P000052.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000052.£V5PTC
    MU* VAL1(P000052.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000052.£V5PEN
    MU* VAL1(P000052.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000052.£V5PQT
      *
     C                   EVAL      F000052.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000052.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000052.£40FLU='03'
     C                   EVAL      F000052.£40F_MSO='1'
     C                   EVAL      F000052.£40F_MWL=' '
     C                   EVAL      F000052.£40F_MWC='1'
     C                   EVAL      F000052.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000052.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000052.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000052.£40FDE
     C                   EVAL      £40FRE=F000052.£40FRE
     C                   EVAL      £40FLU=F000052.£40FLU
     C                   EVAL      £40F_MSO=F000052.£40F_MSO
     C                   EVAL      £40F_MWL=F000052.£40F_MWL
     C                   EVAL      £40F_MWC=F000052.£40F_MWC
     C                   EVAL      £40FTO(1)=F000052.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000052.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000052.£40FTO(3)
      *
     C                   EVAL      D000052.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000052.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000052.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000052.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000052.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000052.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000052.SUPP_NUU(1)= 1
     C                   EVAL      D000052.SUPP_NUU(2)= 2
     C                   EVAL      D000052.SUPP_NUU(3)= 3
     C                   EVAL      D000052.SUPP_NUG(1)= 11
     C                   EVAL      D000052.SUPP_NUG(2)= 21
     C                   EVAL      D000052.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000052.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000052.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000052.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000052.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000052.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000052.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000052.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000052.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000052.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000052.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000052.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000052.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000053
      *--------------------------------------------------------------*
     C     R000053       BEGSR
      * This is Subroutine  R000053
     C                   CLEAR                   U000053
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000053.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000053.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000053.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000053.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000053.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000053.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000053.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000053.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000053.£UIBK1
      *
     C                   CLEAR                   P000053
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000053.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000053.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000053.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000053.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000053.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000053.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000053.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000053.£V5PNR= 5
    MU* VAL1(P000053.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000053.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000053.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000053.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000053.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000053.£V5PQT=130,425
    MU* VAL1(P000053.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000053.£V5PTD
    MU* VAL1(P000053.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000053.£V5PMO
    MU* VAL1(P000053.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000053.£V5PND
    MU* VAL1(P000053.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000053.£V5PNR
    MU* VAL1(P000053.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000053.£V5PTC
    MU* VAL1(P000053.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000053.£V5PEN
    MU* VAL1(P000053.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000053.£V5PQT
      *
     C                   EVAL      F000053.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000053.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000053.£40FLU='03'
     C                   EVAL      F000053.£40F_MSO='1'
     C                   EVAL      F000053.£40F_MWL=' '
     C                   EVAL      F000053.£40F_MWC='1'
     C                   EVAL      F000053.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000053.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000053.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000053.£40FDE
     C                   EVAL      £40FRE=F000053.£40FRE
     C                   EVAL      £40FLU=F000053.£40FLU
     C                   EVAL      £40F_MSO=F000053.£40F_MSO
     C                   EVAL      £40F_MWL=F000053.£40F_MWL
     C                   EVAL      £40F_MWC=F000053.£40F_MWC
     C                   EVAL      £40FTO(1)=F000053.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000053.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000053.£40FTO(3)
      *
     C                   EVAL      D000053.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000053.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000053.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000053.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000053.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000053.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000053.SUPP_NUU(1)= 1
     C                   EVAL      D000053.SUPP_NUU(2)= 2
     C                   EVAL      D000053.SUPP_NUU(3)= 3
     C                   EVAL      D000053.SUPP_NUG(1)= 11
     C                   EVAL      D000053.SUPP_NUG(2)= 21
     C                   EVAL      D000053.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000053.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000053.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000053.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000053.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000053.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000053.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000053.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000053.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000053.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000053.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000053.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000053.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000054
      *--------------------------------------------------------------*
     C     R000054       BEGSR
      * This is Subroutine  R000054
     C                   CLEAR                   U000054
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000054.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000054.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000054.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000054.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000054.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000054.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000054.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000054.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000054.£UIBK1
      *
     C                   CLEAR                   P000054
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000054.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000054.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000054.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000054.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000054.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000054.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000054.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000054.£V5PNR= 5
    MU* VAL1(P000054.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000054.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000054.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000054.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000054.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000054.£V5PQT=130,425
    MU* VAL1(P000054.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000054.£V5PTD
    MU* VAL1(P000054.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000054.£V5PMO
    MU* VAL1(P000054.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000054.£V5PND
    MU* VAL1(P000054.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000054.£V5PNR
    MU* VAL1(P000054.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000054.£V5PTC
    MU* VAL1(P000054.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000054.£V5PEN
    MU* VAL1(P000054.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000054.£V5PQT
      *
     C                   EVAL      F000054.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000054.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000054.£40FLU='03'
     C                   EVAL      F000054.£40F_MSO='1'
     C                   EVAL      F000054.£40F_MWL=' '
     C                   EVAL      F000054.£40F_MWC='1'
     C                   EVAL      F000054.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000054.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000054.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000054.£40FDE
     C                   EVAL      £40FRE=F000054.£40FRE
     C                   EVAL      £40FLU=F000054.£40FLU
     C                   EVAL      £40F_MSO=F000054.£40F_MSO
     C                   EVAL      £40F_MWL=F000054.£40F_MWL
     C                   EVAL      £40F_MWC=F000054.£40F_MWC
     C                   EVAL      £40FTO(1)=F000054.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000054.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000054.£40FTO(3)
      *
     C                   EVAL      D000054.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000054.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000054.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000054.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000054.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000054.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000054.SUPP_NUU(1)= 1
     C                   EVAL      D000054.SUPP_NUU(2)= 2
     C                   EVAL      D000054.SUPP_NUU(3)= 3
     C                   EVAL      D000054.SUPP_NUG(1)= 11
     C                   EVAL      D000054.SUPP_NUG(2)= 21
     C                   EVAL      D000054.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000054.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000054.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000054.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000054.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000054.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000054.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000054.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000054.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000054.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000054.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000054.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000054.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000055
      *--------------------------------------------------------------*
     C     R000055       BEGSR
      * This is Subroutine  R000055
     C                   CLEAR                   U000055
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000055.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000055.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000055.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000055.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000055.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000055.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000055.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000055.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000055.£UIBK1
      *
     C                   CLEAR                   P000055
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000055.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000055.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000055.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000055.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000055.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000055.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000055.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000055.£V5PNR= 5
    MU* VAL1(P000055.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000055.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000055.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000055.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000055.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000055.£V5PQT=130,425
    MU* VAL1(P000055.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000055.£V5PTD
    MU* VAL1(P000055.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000055.£V5PMO
    MU* VAL1(P000055.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000055.£V5PND
    MU* VAL1(P000055.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000055.£V5PNR
    MU* VAL1(P000055.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000055.£V5PTC
    MU* VAL1(P000055.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000055.£V5PEN
    MU* VAL1(P000055.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000055.£V5PQT
      *
     C                   EVAL      F000055.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000055.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000055.£40FLU='03'
     C                   EVAL      F000055.£40F_MSO='1'
     C                   EVAL      F000055.£40F_MWL=' '
     C                   EVAL      F000055.£40F_MWC='1'
     C                   EVAL      F000055.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000055.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000055.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000055.£40FDE
     C                   EVAL      £40FRE=F000055.£40FRE
     C                   EVAL      £40FLU=F000055.£40FLU
     C                   EVAL      £40F_MSO=F000055.£40F_MSO
     C                   EVAL      £40F_MWL=F000055.£40F_MWL
     C                   EVAL      £40F_MWC=F000055.£40F_MWC
     C                   EVAL      £40FTO(1)=F000055.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000055.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000055.£40FTO(3)
      *
     C                   EVAL      D000055.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000055.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000055.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000055.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000055.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000055.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000055.SUPP_NUU(1)= 1
     C                   EVAL      D000055.SUPP_NUU(2)= 2
     C                   EVAL      D000055.SUPP_NUU(3)= 3
     C                   EVAL      D000055.SUPP_NUG(1)= 11
     C                   EVAL      D000055.SUPP_NUG(2)= 21
     C                   EVAL      D000055.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000055.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000055.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000055.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000055.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000055.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000055.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000055.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000055.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000055.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000055.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000055.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000055.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000056
      *--------------------------------------------------------------*
     C     R000056       BEGSR
      * This is Subroutine  R000056
     C                   CLEAR                   U000056
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000056.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000056.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000056.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000056.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000056.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000056.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000056.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000056.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000056.£UIBK1
      *
     C                   CLEAR                   P000056
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000056.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000056.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000056.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000056.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000056.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000056.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000056.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000056.£V5PNR= 5
    MU* VAL1(P000056.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000056.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000056.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000056.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000056.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000056.£V5PQT=130,425
    MU* VAL1(P000056.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000056.£V5PTD
    MU* VAL1(P000056.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000056.£V5PMO
    MU* VAL1(P000056.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000056.£V5PND
    MU* VAL1(P000056.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000056.£V5PNR
    MU* VAL1(P000056.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000056.£V5PTC
    MU* VAL1(P000056.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000056.£V5PEN
    MU* VAL1(P000056.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000056.£V5PQT
      *
     C                   EVAL      F000056.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000056.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000056.£40FLU='03'
     C                   EVAL      F000056.£40F_MSO='1'
     C                   EVAL      F000056.£40F_MWL=' '
     C                   EVAL      F000056.£40F_MWC='1'
     C                   EVAL      F000056.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000056.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000056.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000056.£40FDE
     C                   EVAL      £40FRE=F000056.£40FRE
     C                   EVAL      £40FLU=F000056.£40FLU
     C                   EVAL      £40F_MSO=F000056.£40F_MSO
     C                   EVAL      £40F_MWL=F000056.£40F_MWL
     C                   EVAL      £40F_MWC=F000056.£40F_MWC
     C                   EVAL      £40FTO(1)=F000056.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000056.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000056.£40FTO(3)
      *
     C                   EVAL      D000056.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000056.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000056.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000056.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000056.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000056.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000056.SUPP_NUU(1)= 1
     C                   EVAL      D000056.SUPP_NUU(2)= 2
     C                   EVAL      D000056.SUPP_NUU(3)= 3
     C                   EVAL      D000056.SUPP_NUG(1)= 11
     C                   EVAL      D000056.SUPP_NUG(2)= 21
     C                   EVAL      D000056.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000056.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000056.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000056.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000056.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000056.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000056.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000056.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000056.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000056.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000056.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000056.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000056.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000057
      *--------------------------------------------------------------*
     C     R000057       BEGSR
      * This is Subroutine  R000057
     C                   CLEAR                   U000057
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000057.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000057.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000057.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000057.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000057.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000057.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000057.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000057.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000057.£UIBK1
      *
     C                   CLEAR                   P000057
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000057.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000057.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000057.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000057.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000057.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000057.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000057.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000057.£V5PNR= 5
    MU* VAL1(P000057.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000057.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000057.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000057.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000057.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000057.£V5PQT=130,425
    MU* VAL1(P000057.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000057.£V5PTD
    MU* VAL1(P000057.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000057.£V5PMO
    MU* VAL1(P000057.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000057.£V5PND
    MU* VAL1(P000057.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000057.£V5PNR
    MU* VAL1(P000057.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000057.£V5PTC
    MU* VAL1(P000057.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000057.£V5PEN
    MU* VAL1(P000057.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000057.£V5PQT
      *
     C                   EVAL      F000057.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000057.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000057.£40FLU='03'
     C                   EVAL      F000057.£40F_MSO='1'
     C                   EVAL      F000057.£40F_MWL=' '
     C                   EVAL      F000057.£40F_MWC='1'
     C                   EVAL      F000057.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000057.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000057.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000057.£40FDE
     C                   EVAL      £40FRE=F000057.£40FRE
     C                   EVAL      £40FLU=F000057.£40FLU
     C                   EVAL      £40F_MSO=F000057.£40F_MSO
     C                   EVAL      £40F_MWL=F000057.£40F_MWL
     C                   EVAL      £40F_MWC=F000057.£40F_MWC
     C                   EVAL      £40FTO(1)=F000057.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000057.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000057.£40FTO(3)
      *
     C                   EVAL      D000057.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000057.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000057.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000057.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000057.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000057.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000057.SUPP_NUU(1)= 1
     C                   EVAL      D000057.SUPP_NUU(2)= 2
     C                   EVAL      D000057.SUPP_NUU(3)= 3
     C                   EVAL      D000057.SUPP_NUG(1)= 11
     C                   EVAL      D000057.SUPP_NUG(2)= 21
     C                   EVAL      D000057.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000057.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000057.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000057.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000057.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000057.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000057.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000057.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000057.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000057.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000057.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000057.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000057.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000058
      *--------------------------------------------------------------*
     C     R000058       BEGSR
      * This is Subroutine  R000058
     C                   CLEAR                   U000058
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000058.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000058.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000058.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000058.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000058.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000058.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000058.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000058.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000058.£UIBK1
      *
     C                   CLEAR                   P000058
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000058.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000058.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000058.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000058.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000058.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000058.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000058.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000058.£V5PNR= 5
    MU* VAL1(P000058.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000058.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000058.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000058.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000058.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000058.£V5PQT=130,425
    MU* VAL1(P000058.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000058.£V5PTD
    MU* VAL1(P000058.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000058.£V5PMO
    MU* VAL1(P000058.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000058.£V5PND
    MU* VAL1(P000058.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000058.£V5PNR
    MU* VAL1(P000058.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000058.£V5PTC
    MU* VAL1(P000058.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000058.£V5PEN
    MU* VAL1(P000058.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000058.£V5PQT
      *
     C                   EVAL      F000058.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000058.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000058.£40FLU='03'
     C                   EVAL      F000058.£40F_MSO='1'
     C                   EVAL      F000058.£40F_MWL=' '
     C                   EVAL      F000058.£40F_MWC='1'
     C                   EVAL      F000058.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000058.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000058.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000058.£40FDE
     C                   EVAL      £40FRE=F000058.£40FRE
     C                   EVAL      £40FLU=F000058.£40FLU
     C                   EVAL      £40F_MSO=F000058.£40F_MSO
     C                   EVAL      £40F_MWL=F000058.£40F_MWL
     C                   EVAL      £40F_MWC=F000058.£40F_MWC
     C                   EVAL      £40FTO(1)=F000058.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000058.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000058.£40FTO(3)
      *
     C                   EVAL      D000058.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000058.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000058.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000058.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000058.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000058.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000058.SUPP_NUU(1)= 1
     C                   EVAL      D000058.SUPP_NUU(2)= 2
     C                   EVAL      D000058.SUPP_NUU(3)= 3
     C                   EVAL      D000058.SUPP_NUG(1)= 11
     C                   EVAL      D000058.SUPP_NUG(2)= 21
     C                   EVAL      D000058.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000058.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000058.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000058.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000058.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000058.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000058.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000058.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000058.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000058.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000058.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000058.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000058.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000059
      *--------------------------------------------------------------*
     C     R000059       BEGSR
      * This is Subroutine  R000059
     C                   CLEAR                   U000059
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000059.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000059.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000059.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000059.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000059.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000059.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000059.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000059.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000059.£UIBK1
      *
     C                   CLEAR                   P000059
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000059.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000059.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000059.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000059.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000059.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000059.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000059.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000059.£V5PNR= 5
    MU* VAL1(P000059.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000059.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000059.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000059.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000059.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000059.£V5PQT=130,425
    MU* VAL1(P000059.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000059.£V5PTD
    MU* VAL1(P000059.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000059.£V5PMO
    MU* VAL1(P000059.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000059.£V5PND
    MU* VAL1(P000059.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000059.£V5PNR
    MU* VAL1(P000059.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000059.£V5PTC
    MU* VAL1(P000059.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000059.£V5PEN
    MU* VAL1(P000059.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000059.£V5PQT
      *
     C                   EVAL      F000059.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000059.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000059.£40FLU='03'
     C                   EVAL      F000059.£40F_MSO='1'
     C                   EVAL      F000059.£40F_MWL=' '
     C                   EVAL      F000059.£40F_MWC='1'
     C                   EVAL      F000059.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000059.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000059.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000059.£40FDE
     C                   EVAL      £40FRE=F000059.£40FRE
     C                   EVAL      £40FLU=F000059.£40FLU
     C                   EVAL      £40F_MSO=F000059.£40F_MSO
     C                   EVAL      £40F_MWL=F000059.£40F_MWL
     C                   EVAL      £40F_MWC=F000059.£40F_MWC
     C                   EVAL      £40FTO(1)=F000059.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000059.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000059.£40FTO(3)
      *
     C                   EVAL      D000059.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000059.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000059.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000059.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000059.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000059.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000059.SUPP_NUU(1)= 1
     C                   EVAL      D000059.SUPP_NUU(2)= 2
     C                   EVAL      D000059.SUPP_NUU(3)= 3
     C                   EVAL      D000059.SUPP_NUG(1)= 11
     C                   EVAL      D000059.SUPP_NUG(2)= 21
     C                   EVAL      D000059.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000059.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000059.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000059.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000059.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000059.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000059.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000059.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000059.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000059.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000059.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000059.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000059.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000060
      *--------------------------------------------------------------*
     C     R000060       BEGSR
      * This is Subroutine  R000060
     C                   CLEAR                   U000060
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000060.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000060.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000060.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000060.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000060.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000060.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000060.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000060.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000060.£UIBK1
      *
     C                   CLEAR                   P000060
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000060.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000060.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000060.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000060.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000060.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000060.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000060.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000060.£V5PNR= 5
    MU* VAL1(P000060.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000060.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000060.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000060.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000060.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000060.£V5PQT=130,425
    MU* VAL1(P000060.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000060.£V5PTD
    MU* VAL1(P000060.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000060.£V5PMO
    MU* VAL1(P000060.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000060.£V5PND
    MU* VAL1(P000060.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000060.£V5PNR
    MU* VAL1(P000060.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000060.£V5PTC
    MU* VAL1(P000060.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000060.£V5PEN
    MU* VAL1(P000060.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000060.£V5PQT
      *
     C                   EVAL      F000060.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000060.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000060.£40FLU='03'
     C                   EVAL      F000060.£40F_MSO='1'
     C                   EVAL      F000060.£40F_MWL=' '
     C                   EVAL      F000060.£40F_MWC='1'
     C                   EVAL      F000060.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000060.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000060.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000060.£40FDE
     C                   EVAL      £40FRE=F000060.£40FRE
     C                   EVAL      £40FLU=F000060.£40FLU
     C                   EVAL      £40F_MSO=F000060.£40F_MSO
     C                   EVAL      £40F_MWL=F000060.£40F_MWL
     C                   EVAL      £40F_MWC=F000060.£40F_MWC
     C                   EVAL      £40FTO(1)=F000060.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000060.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000060.£40FTO(3)
      *
     C                   EVAL      D000060.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000060.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000060.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000060.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000060.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000060.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000060.SUPP_NUU(1)= 1
     C                   EVAL      D000060.SUPP_NUU(2)= 2
     C                   EVAL      D000060.SUPP_NUU(3)= 3
     C                   EVAL      D000060.SUPP_NUG(1)= 11
     C                   EVAL      D000060.SUPP_NUG(2)= 21
     C                   EVAL      D000060.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000060.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000060.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000060.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000060.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000060.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000060.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000060.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000060.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000060.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000060.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000060.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000060.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000061
      *--------------------------------------------------------------*
     C     R000061       BEGSR
      * This is Subroutine  R000061
     C                   CLEAR                   U000061
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000061.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000061.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000061.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000061.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000061.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000061.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000061.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000061.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000061.£UIBK1
      *
     C                   CLEAR                   P000061
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000061.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000061.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000061.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000061.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000061.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000061.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000061.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000061.£V5PNR= 5
    MU* VAL1(P000061.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000061.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000061.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000061.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000061.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000061.£V5PQT=130,425
    MU* VAL1(P000061.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000061.£V5PTD
    MU* VAL1(P000061.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000061.£V5PMO
    MU* VAL1(P000061.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000061.£V5PND
    MU* VAL1(P000061.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000061.£V5PNR
    MU* VAL1(P000061.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000061.£V5PTC
    MU* VAL1(P000061.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000061.£V5PEN
    MU* VAL1(P000061.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000061.£V5PQT
      *
     C                   EVAL      F000061.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000061.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000061.£40FLU='03'
     C                   EVAL      F000061.£40F_MSO='1'
     C                   EVAL      F000061.£40F_MWL=' '
     C                   EVAL      F000061.£40F_MWC='1'
     C                   EVAL      F000061.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000061.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000061.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000061.£40FDE
     C                   EVAL      £40FRE=F000061.£40FRE
     C                   EVAL      £40FLU=F000061.£40FLU
     C                   EVAL      £40F_MSO=F000061.£40F_MSO
     C                   EVAL      £40F_MWL=F000061.£40F_MWL
     C                   EVAL      £40F_MWC=F000061.£40F_MWC
     C                   EVAL      £40FTO(1)=F000061.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000061.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000061.£40FTO(3)
      *
     C                   EVAL      D000061.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000061.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000061.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000061.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000061.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000061.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000061.SUPP_NUU(1)= 1
     C                   EVAL      D000061.SUPP_NUU(2)= 2
     C                   EVAL      D000061.SUPP_NUU(3)= 3
     C                   EVAL      D000061.SUPP_NUG(1)= 11
     C                   EVAL      D000061.SUPP_NUG(2)= 21
     C                   EVAL      D000061.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000061.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000061.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000061.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000061.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000061.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000061.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000061.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000061.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000061.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000061.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000061.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000061.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000062
      *--------------------------------------------------------------*
     C     R000062       BEGSR
      * This is Subroutine  R000062
     C                   CLEAR                   U000062
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000062.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000062.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000062.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000062.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000062.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000062.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000062.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000062.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000062.£UIBK1
      *
     C                   CLEAR                   P000062
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000062.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000062.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000062.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000062.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000062.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000062.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000062.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000062.£V5PNR= 5
    MU* VAL1(P000062.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000062.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000062.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000062.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000062.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000062.£V5PQT=130,425
    MU* VAL1(P000062.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000062.£V5PTD
    MU* VAL1(P000062.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000062.£V5PMO
    MU* VAL1(P000062.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000062.£V5PND
    MU* VAL1(P000062.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000062.£V5PNR
    MU* VAL1(P000062.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000062.£V5PTC
    MU* VAL1(P000062.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000062.£V5PEN
    MU* VAL1(P000062.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000062.£V5PQT
      *
     C                   EVAL      F000062.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000062.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000062.£40FLU='03'
     C                   EVAL      F000062.£40F_MSO='1'
     C                   EVAL      F000062.£40F_MWL=' '
     C                   EVAL      F000062.£40F_MWC='1'
     C                   EVAL      F000062.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000062.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000062.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000062.£40FDE
     C                   EVAL      £40FRE=F000062.£40FRE
     C                   EVAL      £40FLU=F000062.£40FLU
     C                   EVAL      £40F_MSO=F000062.£40F_MSO
     C                   EVAL      £40F_MWL=F000062.£40F_MWL
     C                   EVAL      £40F_MWC=F000062.£40F_MWC
     C                   EVAL      £40FTO(1)=F000062.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000062.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000062.£40FTO(3)
      *
     C                   EVAL      D000062.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000062.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000062.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000062.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000062.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000062.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000062.SUPP_NUU(1)= 1
     C                   EVAL      D000062.SUPP_NUU(2)= 2
     C                   EVAL      D000062.SUPP_NUU(3)= 3
     C                   EVAL      D000062.SUPP_NUG(1)= 11
     C                   EVAL      D000062.SUPP_NUG(2)= 21
     C                   EVAL      D000062.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000062.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000062.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000062.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000062.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000062.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000062.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000062.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000062.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000062.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000062.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000062.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000062.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000063
      *--------------------------------------------------------------*
     C     R000063       BEGSR
      * This is Subroutine  R000063
     C                   CLEAR                   U000063
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000063.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000063.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000063.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000063.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000063.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000063.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000063.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000063.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000063.£UIBK1
      *
     C                   CLEAR                   P000063
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000063.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000063.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000063.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000063.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000063.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000063.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000063.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000063.£V5PNR= 5
    MU* VAL1(P000063.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000063.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000063.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000063.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000063.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000063.£V5PQT=130,425
    MU* VAL1(P000063.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000063.£V5PTD
    MU* VAL1(P000063.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000063.£V5PMO
    MU* VAL1(P000063.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000063.£V5PND
    MU* VAL1(P000063.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000063.£V5PNR
    MU* VAL1(P000063.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000063.£V5PTC
    MU* VAL1(P000063.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000063.£V5PEN
    MU* VAL1(P000063.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000063.£V5PQT
      *
     C                   EVAL      F000063.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000063.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000063.£40FLU='03'
     C                   EVAL      F000063.£40F_MSO='1'
     C                   EVAL      F000063.£40F_MWL=' '
     C                   EVAL      F000063.£40F_MWC='1'
     C                   EVAL      F000063.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000063.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000063.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000063.£40FDE
     C                   EVAL      £40FRE=F000063.£40FRE
     C                   EVAL      £40FLU=F000063.£40FLU
     C                   EVAL      £40F_MSO=F000063.£40F_MSO
     C                   EVAL      £40F_MWL=F000063.£40F_MWL
     C                   EVAL      £40F_MWC=F000063.£40F_MWC
     C                   EVAL      £40FTO(1)=F000063.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000063.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000063.£40FTO(3)
      *
     C                   EVAL      D000063.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000063.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000063.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000063.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000063.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000063.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000063.SUPP_NUU(1)= 1
     C                   EVAL      D000063.SUPP_NUU(2)= 2
     C                   EVAL      D000063.SUPP_NUU(3)= 3
     C                   EVAL      D000063.SUPP_NUG(1)= 11
     C                   EVAL      D000063.SUPP_NUG(2)= 21
     C                   EVAL      D000063.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000063.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000063.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000063.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000063.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000063.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000063.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000063.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000063.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000063.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000063.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000063.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000063.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000064
      *--------------------------------------------------------------*
     C     R000064       BEGSR
      * This is Subroutine  R000064
     C                   CLEAR                   U000064
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000064.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000064.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000064.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000064.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000064.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000064.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000064.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000064.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000064.£UIBK1
      *
     C                   CLEAR                   P000064
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000064.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000064.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000064.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000064.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000064.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000064.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000064.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000064.£V5PNR= 5
    MU* VAL1(P000064.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000064.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000064.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000064.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000064.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000064.£V5PQT=130,425
    MU* VAL1(P000064.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000064.£V5PTD
    MU* VAL1(P000064.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000064.£V5PMO
    MU* VAL1(P000064.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000064.£V5PND
    MU* VAL1(P000064.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000064.£V5PNR
    MU* VAL1(P000064.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000064.£V5PTC
    MU* VAL1(P000064.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000064.£V5PEN
    MU* VAL1(P000064.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000064.£V5PQT
      *
     C                   EVAL      F000064.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000064.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000064.£40FLU='03'
     C                   EVAL      F000064.£40F_MSO='1'
     C                   EVAL      F000064.£40F_MWL=' '
     C                   EVAL      F000064.£40F_MWC='1'
     C                   EVAL      F000064.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000064.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000064.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000064.£40FDE
     C                   EVAL      £40FRE=F000064.£40FRE
     C                   EVAL      £40FLU=F000064.£40FLU
     C                   EVAL      £40F_MSO=F000064.£40F_MSO
     C                   EVAL      £40F_MWL=F000064.£40F_MWL
     C                   EVAL      £40F_MWC=F000064.£40F_MWC
     C                   EVAL      £40FTO(1)=F000064.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000064.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000064.£40FTO(3)
      *
     C                   EVAL      D000064.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000064.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000064.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000064.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000064.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000064.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000064.SUPP_NUU(1)= 1
     C                   EVAL      D000064.SUPP_NUU(2)= 2
     C                   EVAL      D000064.SUPP_NUU(3)= 3
     C                   EVAL      D000064.SUPP_NUG(1)= 11
     C                   EVAL      D000064.SUPP_NUG(2)= 21
     C                   EVAL      D000064.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000064.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000064.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000064.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000064.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000064.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000064.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000064.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000064.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000064.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000064.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000064.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000064.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000065
      *--------------------------------------------------------------*
     C     R000065       BEGSR
      * This is Subroutine  R000065
     C                   CLEAR                   U000065
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000065.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000065.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000065.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000065.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000065.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000065.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000065.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000065.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000065.£UIBK1
      *
     C                   CLEAR                   P000065
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000065.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000065.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000065.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000065.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000065.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000065.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000065.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000065.£V5PNR= 5
    MU* VAL1(P000065.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000065.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000065.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000065.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000065.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000065.£V5PQT=130,425
    MU* VAL1(P000065.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000065.£V5PTD
    MU* VAL1(P000065.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000065.£V5PMO
    MU* VAL1(P000065.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000065.£V5PND
    MU* VAL1(P000065.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000065.£V5PNR
    MU* VAL1(P000065.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000065.£V5PTC
    MU* VAL1(P000065.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000065.£V5PEN
    MU* VAL1(P000065.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000065.£V5PQT
      *
     C                   EVAL      F000065.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000065.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000065.£40FLU='03'
     C                   EVAL      F000065.£40F_MSO='1'
     C                   EVAL      F000065.£40F_MWL=' '
     C                   EVAL      F000065.£40F_MWC='1'
     C                   EVAL      F000065.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000065.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000065.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000065.£40FDE
     C                   EVAL      £40FRE=F000065.£40FRE
     C                   EVAL      £40FLU=F000065.£40FLU
     C                   EVAL      £40F_MSO=F000065.£40F_MSO
     C                   EVAL      £40F_MWL=F000065.£40F_MWL
     C                   EVAL      £40F_MWC=F000065.£40F_MWC
     C                   EVAL      £40FTO(1)=F000065.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000065.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000065.£40FTO(3)
      *
     C                   EVAL      D000065.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000065.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000065.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000065.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000065.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000065.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000065.SUPP_NUU(1)= 1
     C                   EVAL      D000065.SUPP_NUU(2)= 2
     C                   EVAL      D000065.SUPP_NUU(3)= 3
     C                   EVAL      D000065.SUPP_NUG(1)= 11
     C                   EVAL      D000065.SUPP_NUG(2)= 21
     C                   EVAL      D000065.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000065.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000065.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000065.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000065.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000065.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000065.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000065.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000065.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000065.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000065.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000065.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000065.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000066
      *--------------------------------------------------------------*
     C     R000066       BEGSR
      * This is Subroutine  R000066
     C                   CLEAR                   U000066
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000066.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000066.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000066.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000066.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000066.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000066.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000066.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000066.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000066.£UIBK1
      *
     C                   CLEAR                   P000066
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000066.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000066.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000066.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000066.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000066.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000066.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000066.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000066.£V5PNR= 5
    MU* VAL1(P000066.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000066.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000066.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000066.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000066.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000066.£V5PQT=130,425
    MU* VAL1(P000066.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000066.£V5PTD
    MU* VAL1(P000066.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000066.£V5PMO
    MU* VAL1(P000066.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000066.£V5PND
    MU* VAL1(P000066.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000066.£V5PNR
    MU* VAL1(P000066.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000066.£V5PTC
    MU* VAL1(P000066.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000066.£V5PEN
    MU* VAL1(P000066.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000066.£V5PQT
      *
     C                   EVAL      F000066.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000066.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000066.£40FLU='03'
     C                   EVAL      F000066.£40F_MSO='1'
     C                   EVAL      F000066.£40F_MWL=' '
     C                   EVAL      F000066.£40F_MWC='1'
     C                   EVAL      F000066.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000066.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000066.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000066.£40FDE
     C                   EVAL      £40FRE=F000066.£40FRE
     C                   EVAL      £40FLU=F000066.£40FLU
     C                   EVAL      £40F_MSO=F000066.£40F_MSO
     C                   EVAL      £40F_MWL=F000066.£40F_MWL
     C                   EVAL      £40F_MWC=F000066.£40F_MWC
     C                   EVAL      £40FTO(1)=F000066.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000066.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000066.£40FTO(3)
      *
     C                   EVAL      D000066.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000066.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000066.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000066.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000066.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000066.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000066.SUPP_NUU(1)= 1
     C                   EVAL      D000066.SUPP_NUU(2)= 2
     C                   EVAL      D000066.SUPP_NUU(3)= 3
     C                   EVAL      D000066.SUPP_NUG(1)= 11
     C                   EVAL      D000066.SUPP_NUG(2)= 21
     C                   EVAL      D000066.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000066.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000066.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000066.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000066.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000066.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000066.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000066.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000066.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000066.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000066.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000066.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000066.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000067
      *--------------------------------------------------------------*
     C     R000067       BEGSR
      * This is Subroutine  R000067
     C                   CLEAR                   U000067
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000067.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000067.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000067.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000067.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000067.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000067.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000067.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000067.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000067.£UIBK1
      *
     C                   CLEAR                   P000067
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000067.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000067.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000067.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000067.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000067.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000067.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000067.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000067.£V5PNR= 5
    MU* VAL1(P000067.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000067.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000067.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000067.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000067.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000067.£V5PQT=130,425
    MU* VAL1(P000067.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000067.£V5PTD
    MU* VAL1(P000067.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000067.£V5PMO
    MU* VAL1(P000067.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000067.£V5PND
    MU* VAL1(P000067.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000067.£V5PNR
    MU* VAL1(P000067.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000067.£V5PTC
    MU* VAL1(P000067.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000067.£V5PEN
    MU* VAL1(P000067.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000067.£V5PQT
      *
     C                   EVAL      F000067.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000067.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000067.£40FLU='03'
     C                   EVAL      F000067.£40F_MSO='1'
     C                   EVAL      F000067.£40F_MWL=' '
     C                   EVAL      F000067.£40F_MWC='1'
     C                   EVAL      F000067.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000067.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000067.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000067.£40FDE
     C                   EVAL      £40FRE=F000067.£40FRE
     C                   EVAL      £40FLU=F000067.£40FLU
     C                   EVAL      £40F_MSO=F000067.£40F_MSO
     C                   EVAL      £40F_MWL=F000067.£40F_MWL
     C                   EVAL      £40F_MWC=F000067.£40F_MWC
     C                   EVAL      £40FTO(1)=F000067.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000067.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000067.£40FTO(3)
      *
     C                   EVAL      D000067.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000067.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000067.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000067.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000067.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000067.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000067.SUPP_NUU(1)= 1
     C                   EVAL      D000067.SUPP_NUU(2)= 2
     C                   EVAL      D000067.SUPP_NUU(3)= 3
     C                   EVAL      D000067.SUPP_NUG(1)= 11
     C                   EVAL      D000067.SUPP_NUG(2)= 21
     C                   EVAL      D000067.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000067.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000067.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000067.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000067.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000067.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000067.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000067.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000067.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000067.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000067.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000067.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000067.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000068
      *--------------------------------------------------------------*
     C     R000068       BEGSR
      * This is Subroutine  R000068
     C                   CLEAR                   U000068
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000068.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000068.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000068.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000068.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000068.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000068.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000068.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000068.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000068.£UIBK1
      *
     C                   CLEAR                   P000068
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000068.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000068.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000068.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000068.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000068.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000068.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000068.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000068.£V5PNR= 5
    MU* VAL1(P000068.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000068.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000068.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000068.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000068.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000068.£V5PQT=130,425
    MU* VAL1(P000068.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000068.£V5PTD
    MU* VAL1(P000068.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000068.£V5PMO
    MU* VAL1(P000068.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000068.£V5PND
    MU* VAL1(P000068.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000068.£V5PNR
    MU* VAL1(P000068.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000068.£V5PTC
    MU* VAL1(P000068.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000068.£V5PEN
    MU* VAL1(P000068.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000068.£V5PQT
      *
     C                   EVAL      F000068.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000068.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000068.£40FLU='03'
     C                   EVAL      F000068.£40F_MSO='1'
     C                   EVAL      F000068.£40F_MWL=' '
     C                   EVAL      F000068.£40F_MWC='1'
     C                   EVAL      F000068.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000068.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000068.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000068.£40FDE
     C                   EVAL      £40FRE=F000068.£40FRE
     C                   EVAL      £40FLU=F000068.£40FLU
     C                   EVAL      £40F_MSO=F000068.£40F_MSO
     C                   EVAL      £40F_MWL=F000068.£40F_MWL
     C                   EVAL      £40F_MWC=F000068.£40F_MWC
     C                   EVAL      £40FTO(1)=F000068.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000068.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000068.£40FTO(3)
      *
     C                   EVAL      D000068.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000068.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000068.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000068.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000068.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000068.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000068.SUPP_NUU(1)= 1
     C                   EVAL      D000068.SUPP_NUU(2)= 2
     C                   EVAL      D000068.SUPP_NUU(3)= 3
     C                   EVAL      D000068.SUPP_NUG(1)= 11
     C                   EVAL      D000068.SUPP_NUG(2)= 21
     C                   EVAL      D000068.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000068.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000068.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000068.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000068.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000068.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000068.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000068.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000068.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000068.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000068.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000068.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000068.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000069
      *--------------------------------------------------------------*
     C     R000069       BEGSR
      * This is Subroutine  R000069
     C                   CLEAR                   U000069
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000069.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000069.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000069.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000069.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000069.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000069.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000069.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000069.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000069.£UIBK1
      *
     C                   CLEAR                   P000069
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000069.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000069.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000069.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000069.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000069.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000069.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000069.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000069.£V5PNR= 5
    MU* VAL1(P000069.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000069.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000069.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000069.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000069.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000069.£V5PQT=130,425
    MU* VAL1(P000069.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000069.£V5PTD
    MU* VAL1(P000069.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000069.£V5PMO
    MU* VAL1(P000069.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000069.£V5PND
    MU* VAL1(P000069.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000069.£V5PNR
    MU* VAL1(P000069.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000069.£V5PTC
    MU* VAL1(P000069.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000069.£V5PEN
    MU* VAL1(P000069.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000069.£V5PQT
      *
     C                   EVAL      F000069.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000069.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000069.£40FLU='03'
     C                   EVAL      F000069.£40F_MSO='1'
     C                   EVAL      F000069.£40F_MWL=' '
     C                   EVAL      F000069.£40F_MWC='1'
     C                   EVAL      F000069.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000069.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000069.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000069.£40FDE
     C                   EVAL      £40FRE=F000069.£40FRE
     C                   EVAL      £40FLU=F000069.£40FLU
     C                   EVAL      £40F_MSO=F000069.£40F_MSO
     C                   EVAL      £40F_MWL=F000069.£40F_MWL
     C                   EVAL      £40F_MWC=F000069.£40F_MWC
     C                   EVAL      £40FTO(1)=F000069.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000069.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000069.£40FTO(3)
      *
     C                   EVAL      D000069.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000069.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000069.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000069.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000069.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000069.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000069.SUPP_NUU(1)= 1
     C                   EVAL      D000069.SUPP_NUU(2)= 2
     C                   EVAL      D000069.SUPP_NUU(3)= 3
     C                   EVAL      D000069.SUPP_NUG(1)= 11
     C                   EVAL      D000069.SUPP_NUG(2)= 21
     C                   EVAL      D000069.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000069.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000069.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000069.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000069.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000069.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000069.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000069.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000069.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000069.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000069.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000069.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000069.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000070
      *--------------------------------------------------------------*
     C     R000070       BEGSR
      * This is Subroutine  R000070
     C                   CLEAR                   U000070
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000070.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000070.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000070.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000070.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000070.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000070.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000070.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000070.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000070.£UIBK1
      *
     C                   CLEAR                   P000070
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000070.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000070.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000070.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000070.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000070.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000070.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000070.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000070.£V5PNR= 5
    MU* VAL1(P000070.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000070.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000070.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000070.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000070.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000070.£V5PQT=130,425
    MU* VAL1(P000070.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000070.£V5PTD
    MU* VAL1(P000070.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000070.£V5PMO
    MU* VAL1(P000070.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000070.£V5PND
    MU* VAL1(P000070.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000070.£V5PNR
    MU* VAL1(P000070.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000070.£V5PTC
    MU* VAL1(P000070.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000070.£V5PEN
    MU* VAL1(P000070.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000070.£V5PQT
      *
     C                   EVAL      F000070.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000070.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000070.£40FLU='03'
     C                   EVAL      F000070.£40F_MSO='1'
     C                   EVAL      F000070.£40F_MWL=' '
     C                   EVAL      F000070.£40F_MWC='1'
     C                   EVAL      F000070.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000070.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000070.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000070.£40FDE
     C                   EVAL      £40FRE=F000070.£40FRE
     C                   EVAL      £40FLU=F000070.£40FLU
     C                   EVAL      £40F_MSO=F000070.£40F_MSO
     C                   EVAL      £40F_MWL=F000070.£40F_MWL
     C                   EVAL      £40F_MWC=F000070.£40F_MWC
     C                   EVAL      £40FTO(1)=F000070.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000070.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000070.£40FTO(3)
      *
     C                   EVAL      D000070.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000070.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000070.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000070.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000070.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000070.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000070.SUPP_NUU(1)= 1
     C                   EVAL      D000070.SUPP_NUU(2)= 2
     C                   EVAL      D000070.SUPP_NUU(3)= 3
     C                   EVAL      D000070.SUPP_NUG(1)= 11
     C                   EVAL      D000070.SUPP_NUG(2)= 21
     C                   EVAL      D000070.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000070.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000070.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000070.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000070.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000070.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000070.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000070.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000070.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000070.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000070.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000070.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000070.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000071
      *--------------------------------------------------------------*
     C     R000071       BEGSR
      * This is Subroutine  R000071
     C                   CLEAR                   U000071
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000071.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000071.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000071.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000071.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000071.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000071.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000071.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000071.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000071.£UIBK1
      *
     C                   CLEAR                   P000071
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000071.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000071.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000071.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000071.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000071.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000071.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000071.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000071.£V5PNR= 5
    MU* VAL1(P000071.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000071.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000071.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000071.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000071.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000071.£V5PQT=130,425
    MU* VAL1(P000071.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000071.£V5PTD
    MU* VAL1(P000071.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000071.£V5PMO
    MU* VAL1(P000071.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000071.£V5PND
    MU* VAL1(P000071.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000071.£V5PNR
    MU* VAL1(P000071.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000071.£V5PTC
    MU* VAL1(P000071.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000071.£V5PEN
    MU* VAL1(P000071.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000071.£V5PQT
      *
     C                   EVAL      F000071.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000071.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000071.£40FLU='03'
     C                   EVAL      F000071.£40F_MSO='1'
     C                   EVAL      F000071.£40F_MWL=' '
     C                   EVAL      F000071.£40F_MWC='1'
     C                   EVAL      F000071.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000071.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000071.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000071.£40FDE
     C                   EVAL      £40FRE=F000071.£40FRE
     C                   EVAL      £40FLU=F000071.£40FLU
     C                   EVAL      £40F_MSO=F000071.£40F_MSO
     C                   EVAL      £40F_MWL=F000071.£40F_MWL
     C                   EVAL      £40F_MWC=F000071.£40F_MWC
     C                   EVAL      £40FTO(1)=F000071.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000071.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000071.£40FTO(3)
      *
     C                   EVAL      D000071.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000071.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000071.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000071.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000071.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000071.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000071.SUPP_NUU(1)= 1
     C                   EVAL      D000071.SUPP_NUU(2)= 2
     C                   EVAL      D000071.SUPP_NUU(3)= 3
     C                   EVAL      D000071.SUPP_NUG(1)= 11
     C                   EVAL      D000071.SUPP_NUG(2)= 21
     C                   EVAL      D000071.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000071.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000071.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000071.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000071.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000071.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000071.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000071.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000071.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000071.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000071.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000071.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000071.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000072
      *--------------------------------------------------------------*
     C     R000072       BEGSR
      * This is Subroutine  R000072
     C                   CLEAR                   U000072
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000072.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000072.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000072.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000072.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000072.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000072.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000072.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000072.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000072.£UIBK1
      *
     C                   CLEAR                   P000072
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000072.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000072.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000072.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000072.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000072.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000072.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000072.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000072.£V5PNR= 5
    MU* VAL1(P000072.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000072.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000072.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000072.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000072.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000072.£V5PQT=130,425
    MU* VAL1(P000072.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000072.£V5PTD
    MU* VAL1(P000072.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000072.£V5PMO
    MU* VAL1(P000072.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000072.£V5PND
    MU* VAL1(P000072.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000072.£V5PNR
    MU* VAL1(P000072.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000072.£V5PTC
    MU* VAL1(P000072.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000072.£V5PEN
    MU* VAL1(P000072.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000072.£V5PQT
      *
     C                   EVAL      F000072.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000072.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000072.£40FLU='03'
     C                   EVAL      F000072.£40F_MSO='1'
     C                   EVAL      F000072.£40F_MWL=' '
     C                   EVAL      F000072.£40F_MWC='1'
     C                   EVAL      F000072.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000072.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000072.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000072.£40FDE
     C                   EVAL      £40FRE=F000072.£40FRE
     C                   EVAL      £40FLU=F000072.£40FLU
     C                   EVAL      £40F_MSO=F000072.£40F_MSO
     C                   EVAL      £40F_MWL=F000072.£40F_MWL
     C                   EVAL      £40F_MWC=F000072.£40F_MWC
     C                   EVAL      £40FTO(1)=F000072.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000072.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000072.£40FTO(3)
      *
     C                   EVAL      D000072.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000072.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000072.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000072.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000072.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000072.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000072.SUPP_NUU(1)= 1
     C                   EVAL      D000072.SUPP_NUU(2)= 2
     C                   EVAL      D000072.SUPP_NUU(3)= 3
     C                   EVAL      D000072.SUPP_NUG(1)= 11
     C                   EVAL      D000072.SUPP_NUG(2)= 21
     C                   EVAL      D000072.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000072.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000072.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000072.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000072.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000072.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000072.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000072.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000072.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000072.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000072.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000072.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000072.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000073
      *--------------------------------------------------------------*
     C     R000073       BEGSR
      * This is Subroutine  R000073
     C                   CLEAR                   U000073
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000073.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000073.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000073.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000073.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000073.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000073.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000073.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000073.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000073.£UIBK1
      *
     C                   CLEAR                   P000073
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000073.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000073.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000073.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000073.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000073.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000073.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000073.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000073.£V5PNR= 5
    MU* VAL1(P000073.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000073.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000073.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000073.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000073.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000073.£V5PQT=130,425
    MU* VAL1(P000073.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000073.£V5PTD
    MU* VAL1(P000073.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000073.£V5PMO
    MU* VAL1(P000073.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000073.£V5PND
    MU* VAL1(P000073.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000073.£V5PNR
    MU* VAL1(P000073.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000073.£V5PTC
    MU* VAL1(P000073.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000073.£V5PEN
    MU* VAL1(P000073.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000073.£V5PQT
      *
     C                   EVAL      F000073.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000073.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000073.£40FLU='03'
     C                   EVAL      F000073.£40F_MSO='1'
     C                   EVAL      F000073.£40F_MWL=' '
     C                   EVAL      F000073.£40F_MWC='1'
     C                   EVAL      F000073.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000073.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000073.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000073.£40FDE
     C                   EVAL      £40FRE=F000073.£40FRE
     C                   EVAL      £40FLU=F000073.£40FLU
     C                   EVAL      £40F_MSO=F000073.£40F_MSO
     C                   EVAL      £40F_MWL=F000073.£40F_MWL
     C                   EVAL      £40F_MWC=F000073.£40F_MWC
     C                   EVAL      £40FTO(1)=F000073.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000073.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000073.£40FTO(3)
      *
     C                   EVAL      D000073.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000073.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000073.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000073.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000073.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000073.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000073.SUPP_NUU(1)= 1
     C                   EVAL      D000073.SUPP_NUU(2)= 2
     C                   EVAL      D000073.SUPP_NUU(3)= 3
     C                   EVAL      D000073.SUPP_NUG(1)= 11
     C                   EVAL      D000073.SUPP_NUG(2)= 21
     C                   EVAL      D000073.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000073.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000073.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000073.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000073.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000073.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000073.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000073.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000073.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000073.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000073.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000073.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000073.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000074
      *--------------------------------------------------------------*
     C     R000074       BEGSR
      * This is Subroutine  R000074
     C                   CLEAR                   U000074
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000074.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000074.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000074.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000074.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000074.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000074.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000074.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000074.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000074.£UIBK1
      *
     C                   CLEAR                   P000074
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000074.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000074.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000074.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000074.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000074.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000074.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000074.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000074.£V5PNR= 5
    MU* VAL1(P000074.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000074.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000074.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000074.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000074.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000074.£V5PQT=130,425
    MU* VAL1(P000074.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000074.£V5PTD
    MU* VAL1(P000074.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000074.£V5PMO
    MU* VAL1(P000074.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000074.£V5PND
    MU* VAL1(P000074.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000074.£V5PNR
    MU* VAL1(P000074.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000074.£V5PTC
    MU* VAL1(P000074.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000074.£V5PEN
    MU* VAL1(P000074.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000074.£V5PQT
      *
     C                   EVAL      F000074.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000074.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000074.£40FLU='03'
     C                   EVAL      F000074.£40F_MSO='1'
     C                   EVAL      F000074.£40F_MWL=' '
     C                   EVAL      F000074.£40F_MWC='1'
     C                   EVAL      F000074.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000074.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000074.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000074.£40FDE
     C                   EVAL      £40FRE=F000074.£40FRE
     C                   EVAL      £40FLU=F000074.£40FLU
     C                   EVAL      £40F_MSO=F000074.£40F_MSO
     C                   EVAL      £40F_MWL=F000074.£40F_MWL
     C                   EVAL      £40F_MWC=F000074.£40F_MWC
     C                   EVAL      £40FTO(1)=F000074.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000074.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000074.£40FTO(3)
      *
     C                   EVAL      D000074.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000074.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000074.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000074.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000074.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000074.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000074.SUPP_NUU(1)= 1
     C                   EVAL      D000074.SUPP_NUU(2)= 2
     C                   EVAL      D000074.SUPP_NUU(3)= 3
     C                   EVAL      D000074.SUPP_NUG(1)= 11
     C                   EVAL      D000074.SUPP_NUG(2)= 21
     C                   EVAL      D000074.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000074.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000074.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000074.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000074.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000074.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000074.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000074.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000074.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000074.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000074.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000074.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000074.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000075
      *--------------------------------------------------------------*
     C     R000075       BEGSR
      * This is Subroutine  R000075
     C                   CLEAR                   U000075
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000075.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000075.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000075.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000075.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000075.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000075.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000075.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000075.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000075.£UIBK1
      *
     C                   CLEAR                   P000075
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000075.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000075.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000075.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000075.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000075.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000075.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000075.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000075.£V5PNR= 5
    MU* VAL1(P000075.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000075.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000075.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000075.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000075.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000075.£V5PQT=130,425
    MU* VAL1(P000075.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000075.£V5PTD
    MU* VAL1(P000075.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000075.£V5PMO
    MU* VAL1(P000075.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000075.£V5PND
    MU* VAL1(P000075.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000075.£V5PNR
    MU* VAL1(P000075.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000075.£V5PTC
    MU* VAL1(P000075.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000075.£V5PEN
    MU* VAL1(P000075.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000075.£V5PQT
      *
     C                   EVAL      F000075.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000075.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000075.£40FLU='03'
     C                   EVAL      F000075.£40F_MSO='1'
     C                   EVAL      F000075.£40F_MWL=' '
     C                   EVAL      F000075.£40F_MWC='1'
     C                   EVAL      F000075.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000075.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000075.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000075.£40FDE
     C                   EVAL      £40FRE=F000075.£40FRE
     C                   EVAL      £40FLU=F000075.£40FLU
     C                   EVAL      £40F_MSO=F000075.£40F_MSO
     C                   EVAL      £40F_MWL=F000075.£40F_MWL
     C                   EVAL      £40F_MWC=F000075.£40F_MWC
     C                   EVAL      £40FTO(1)=F000075.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000075.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000075.£40FTO(3)
      *
     C                   EVAL      D000075.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000075.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000075.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000075.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000075.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000075.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000075.SUPP_NUU(1)= 1
     C                   EVAL      D000075.SUPP_NUU(2)= 2
     C                   EVAL      D000075.SUPP_NUU(3)= 3
     C                   EVAL      D000075.SUPP_NUG(1)= 11
     C                   EVAL      D000075.SUPP_NUG(2)= 21
     C                   EVAL      D000075.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000075.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000075.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000075.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000075.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000075.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000075.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000075.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000075.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000075.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000075.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000075.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000075.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000076
      *--------------------------------------------------------------*
     C     R000076       BEGSR
      * This is Subroutine  R000076
     C                   CLEAR                   U000076
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000076.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000076.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000076.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000076.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000076.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000076.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000076.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000076.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000076.£UIBK1
      *
     C                   CLEAR                   P000076
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000076.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000076.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000076.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000076.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000076.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000076.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000076.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000076.£V5PNR= 5
    MU* VAL1(P000076.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000076.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000076.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000076.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000076.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000076.£V5PQT=130,425
    MU* VAL1(P000076.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000076.£V5PTD
    MU* VAL1(P000076.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000076.£V5PMO
    MU* VAL1(P000076.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000076.£V5PND
    MU* VAL1(P000076.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000076.£V5PNR
    MU* VAL1(P000076.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000076.£V5PTC
    MU* VAL1(P000076.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000076.£V5PEN
    MU* VAL1(P000076.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000076.£V5PQT
      *
     C                   EVAL      F000076.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000076.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000076.£40FLU='03'
     C                   EVAL      F000076.£40F_MSO='1'
     C                   EVAL      F000076.£40F_MWL=' '
     C                   EVAL      F000076.£40F_MWC='1'
     C                   EVAL      F000076.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000076.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000076.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000076.£40FDE
     C                   EVAL      £40FRE=F000076.£40FRE
     C                   EVAL      £40FLU=F000076.£40FLU
     C                   EVAL      £40F_MSO=F000076.£40F_MSO
     C                   EVAL      £40F_MWL=F000076.£40F_MWL
     C                   EVAL      £40F_MWC=F000076.£40F_MWC
     C                   EVAL      £40FTO(1)=F000076.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000076.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000076.£40FTO(3)
      *
     C                   EVAL      D000076.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000076.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000076.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000076.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000076.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000076.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000076.SUPP_NUU(1)= 1
     C                   EVAL      D000076.SUPP_NUU(2)= 2
     C                   EVAL      D000076.SUPP_NUU(3)= 3
     C                   EVAL      D000076.SUPP_NUG(1)= 11
     C                   EVAL      D000076.SUPP_NUG(2)= 21
     C                   EVAL      D000076.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000076.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000076.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000076.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000076.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000076.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000076.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000076.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000076.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000076.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000076.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000076.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000076.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000077
      *--------------------------------------------------------------*
     C     R000077       BEGSR
      * This is Subroutine  R000077
     C                   CLEAR                   U000077
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000077.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000077.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000077.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000077.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000077.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000077.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000077.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000077.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000077.£UIBK1
      *
     C                   CLEAR                   P000077
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000077.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000077.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000077.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000077.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000077.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000077.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000077.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000077.£V5PNR= 5
    MU* VAL1(P000077.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000077.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000077.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000077.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000077.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000077.£V5PQT=130,425
    MU* VAL1(P000077.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000077.£V5PTD
    MU* VAL1(P000077.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000077.£V5PMO
    MU* VAL1(P000077.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000077.£V5PND
    MU* VAL1(P000077.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000077.£V5PNR
    MU* VAL1(P000077.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000077.£V5PTC
    MU* VAL1(P000077.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000077.£V5PEN
    MU* VAL1(P000077.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000077.£V5PQT
      *
     C                   EVAL      F000077.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000077.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000077.£40FLU='03'
     C                   EVAL      F000077.£40F_MSO='1'
     C                   EVAL      F000077.£40F_MWL=' '
     C                   EVAL      F000077.£40F_MWC='1'
     C                   EVAL      F000077.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000077.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000077.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000077.£40FDE
     C                   EVAL      £40FRE=F000077.£40FRE
     C                   EVAL      £40FLU=F000077.£40FLU
     C                   EVAL      £40F_MSO=F000077.£40F_MSO
     C                   EVAL      £40F_MWL=F000077.£40F_MWL
     C                   EVAL      £40F_MWC=F000077.£40F_MWC
     C                   EVAL      £40FTO(1)=F000077.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000077.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000077.£40FTO(3)
      *
     C                   EVAL      D000077.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000077.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000077.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000077.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000077.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000077.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000077.SUPP_NUU(1)= 1
     C                   EVAL      D000077.SUPP_NUU(2)= 2
     C                   EVAL      D000077.SUPP_NUU(3)= 3
     C                   EVAL      D000077.SUPP_NUG(1)= 11
     C                   EVAL      D000077.SUPP_NUG(2)= 21
     C                   EVAL      D000077.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000077.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000077.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000077.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000077.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000077.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000077.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000077.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000077.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000077.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000077.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000077.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000077.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000078
      *--------------------------------------------------------------*
     C     R000078       BEGSR
      * This is Subroutine  R000078
     C                   CLEAR                   U000078
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000078.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000078.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000078.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000078.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000078.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000078.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000078.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000078.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000078.£UIBK1
      *
     C                   CLEAR                   P000078
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000078.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000078.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000078.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000078.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000078.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000078.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000078.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000078.£V5PNR= 5
    MU* VAL1(P000078.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000078.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000078.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000078.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000078.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000078.£V5PQT=130,425
    MU* VAL1(P000078.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000078.£V5PTD
    MU* VAL1(P000078.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000078.£V5PMO
    MU* VAL1(P000078.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000078.£V5PND
    MU* VAL1(P000078.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000078.£V5PNR
    MU* VAL1(P000078.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000078.£V5PTC
    MU* VAL1(P000078.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000078.£V5PEN
    MU* VAL1(P000078.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000078.£V5PQT
      *
     C                   EVAL      F000078.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000078.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000078.£40FLU='03'
     C                   EVAL      F000078.£40F_MSO='1'
     C                   EVAL      F000078.£40F_MWL=' '
     C                   EVAL      F000078.£40F_MWC='1'
     C                   EVAL      F000078.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000078.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000078.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000078.£40FDE
     C                   EVAL      £40FRE=F000078.£40FRE
     C                   EVAL      £40FLU=F000078.£40FLU
     C                   EVAL      £40F_MSO=F000078.£40F_MSO
     C                   EVAL      £40F_MWL=F000078.£40F_MWL
     C                   EVAL      £40F_MWC=F000078.£40F_MWC
     C                   EVAL      £40FTO(1)=F000078.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000078.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000078.£40FTO(3)
      *
     C                   EVAL      D000078.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000078.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000078.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000078.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000078.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000078.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000078.SUPP_NUU(1)= 1
     C                   EVAL      D000078.SUPP_NUU(2)= 2
     C                   EVAL      D000078.SUPP_NUU(3)= 3
     C                   EVAL      D000078.SUPP_NUG(1)= 11
     C                   EVAL      D000078.SUPP_NUG(2)= 21
     C                   EVAL      D000078.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000078.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000078.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000078.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000078.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000078.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000078.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000078.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000078.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000078.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000078.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000078.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000078.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000079
      *--------------------------------------------------------------*
     C     R000079       BEGSR
      * This is Subroutine  R000079
     C                   CLEAR                   U000079
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000079.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000079.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000079.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000079.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000079.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000079.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000079.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000079.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000079.£UIBK1
      *
     C                   CLEAR                   P000079
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000079.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000079.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000079.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000079.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000079.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000079.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000079.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000079.£V5PNR= 5
    MU* VAL1(P000079.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000079.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000079.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000079.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000079.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000079.£V5PQT=130,425
    MU* VAL1(P000079.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000079.£V5PTD
    MU* VAL1(P000079.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000079.£V5PMO
    MU* VAL1(P000079.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000079.£V5PND
    MU* VAL1(P000079.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000079.£V5PNR
    MU* VAL1(P000079.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000079.£V5PTC
    MU* VAL1(P000079.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000079.£V5PEN
    MU* VAL1(P000079.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000079.£V5PQT
      *
     C                   EVAL      F000079.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000079.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000079.£40FLU='03'
     C                   EVAL      F000079.£40F_MSO='1'
     C                   EVAL      F000079.£40F_MWL=' '
     C                   EVAL      F000079.£40F_MWC='1'
     C                   EVAL      F000079.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000079.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000079.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000079.£40FDE
     C                   EVAL      £40FRE=F000079.£40FRE
     C                   EVAL      £40FLU=F000079.£40FLU
     C                   EVAL      £40F_MSO=F000079.£40F_MSO
     C                   EVAL      £40F_MWL=F000079.£40F_MWL
     C                   EVAL      £40F_MWC=F000079.£40F_MWC
     C                   EVAL      £40FTO(1)=F000079.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000079.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000079.£40FTO(3)
      *
     C                   EVAL      D000079.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000079.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000079.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000079.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000079.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000079.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000079.SUPP_NUU(1)= 1
     C                   EVAL      D000079.SUPP_NUU(2)= 2
     C                   EVAL      D000079.SUPP_NUU(3)= 3
     C                   EVAL      D000079.SUPP_NUG(1)= 11
     C                   EVAL      D000079.SUPP_NUG(2)= 21
     C                   EVAL      D000079.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000079.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000079.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000079.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000079.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000079.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000079.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000079.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000079.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000079.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000079.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000079.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000079.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000080
      *--------------------------------------------------------------*
     C     R000080       BEGSR
      * This is Subroutine  R000080
     C                   CLEAR                   U000080
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000080.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000080.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000080.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000080.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000080.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000080.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000080.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000080.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000080.£UIBK1
      *
     C                   CLEAR                   P000080
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000080.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000080.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000080.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000080.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000080.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000080.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000080.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000080.£V5PNR= 5
    MU* VAL1(P000080.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000080.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000080.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000080.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000080.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000080.£V5PQT=130,425
    MU* VAL1(P000080.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000080.£V5PTD
    MU* VAL1(P000080.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000080.£V5PMO
    MU* VAL1(P000080.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000080.£V5PND
    MU* VAL1(P000080.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000080.£V5PNR
    MU* VAL1(P000080.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000080.£V5PTC
    MU* VAL1(P000080.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000080.£V5PEN
    MU* VAL1(P000080.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000080.£V5PQT
      *
     C                   EVAL      F000080.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000080.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000080.£40FLU='03'
     C                   EVAL      F000080.£40F_MSO='1'
     C                   EVAL      F000080.£40F_MWL=' '
     C                   EVAL      F000080.£40F_MWC='1'
     C                   EVAL      F000080.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000080.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000080.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000080.£40FDE
     C                   EVAL      £40FRE=F000080.£40FRE
     C                   EVAL      £40FLU=F000080.£40FLU
     C                   EVAL      £40F_MSO=F000080.£40F_MSO
     C                   EVAL      £40F_MWL=F000080.£40F_MWL
     C                   EVAL      £40F_MWC=F000080.£40F_MWC
     C                   EVAL      £40FTO(1)=F000080.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000080.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000080.£40FTO(3)
      *
     C                   EVAL      D000080.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000080.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000080.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000080.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000080.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000080.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000080.SUPP_NUU(1)= 1
     C                   EVAL      D000080.SUPP_NUU(2)= 2
     C                   EVAL      D000080.SUPP_NUU(3)= 3
     C                   EVAL      D000080.SUPP_NUG(1)= 11
     C                   EVAL      D000080.SUPP_NUG(2)= 21
     C                   EVAL      D000080.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000080.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000080.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000080.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000080.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000080.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000080.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000080.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000080.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000080.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000080.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000080.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000080.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000081
      *--------------------------------------------------------------*
     C     R000081       BEGSR
      * This is Subroutine  R000081
     C                   CLEAR                   U000081
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000081.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000081.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000081.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000081.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000081.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000081.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000081.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000081.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000081.£UIBK1
      *
     C                   CLEAR                   P000081
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000081.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000081.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000081.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000081.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000081.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000081.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000081.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000081.£V5PNR= 5
    MU* VAL1(P000081.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000081.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000081.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000081.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000081.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000081.£V5PQT=130,425
    MU* VAL1(P000081.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000081.£V5PTD
    MU* VAL1(P000081.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000081.£V5PMO
    MU* VAL1(P000081.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000081.£V5PND
    MU* VAL1(P000081.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000081.£V5PNR
    MU* VAL1(P000081.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000081.£V5PTC
    MU* VAL1(P000081.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000081.£V5PEN
    MU* VAL1(P000081.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000081.£V5PQT
      *
     C                   EVAL      F000081.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000081.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000081.£40FLU='03'
     C                   EVAL      F000081.£40F_MSO='1'
     C                   EVAL      F000081.£40F_MWL=' '
     C                   EVAL      F000081.£40F_MWC='1'
     C                   EVAL      F000081.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000081.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000081.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000081.£40FDE
     C                   EVAL      £40FRE=F000081.£40FRE
     C                   EVAL      £40FLU=F000081.£40FLU
     C                   EVAL      £40F_MSO=F000081.£40F_MSO
     C                   EVAL      £40F_MWL=F000081.£40F_MWL
     C                   EVAL      £40F_MWC=F000081.£40F_MWC
     C                   EVAL      £40FTO(1)=F000081.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000081.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000081.£40FTO(3)
      *
     C                   EVAL      D000081.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000081.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000081.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000081.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000081.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000081.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000081.SUPP_NUU(1)= 1
     C                   EVAL      D000081.SUPP_NUU(2)= 2
     C                   EVAL      D000081.SUPP_NUU(3)= 3
     C                   EVAL      D000081.SUPP_NUG(1)= 11
     C                   EVAL      D000081.SUPP_NUG(2)= 21
     C                   EVAL      D000081.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000081.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000081.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000081.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000081.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000081.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000081.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000081.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000081.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000081.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000081.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000081.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000081.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000082
      *--------------------------------------------------------------*
     C     R000082       BEGSR
      * This is Subroutine  R000082
     C                   CLEAR                   U000082
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000082.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000082.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000082.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000082.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000082.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000082.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000082.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000082.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000082.£UIBK1
      *
     C                   CLEAR                   P000082
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000082.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000082.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000082.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000082.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000082.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000082.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000082.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000082.£V5PNR= 5
    MU* VAL1(P000082.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000082.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000082.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000082.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000082.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000082.£V5PQT=130,425
    MU* VAL1(P000082.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000082.£V5PTD
    MU* VAL1(P000082.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000082.£V5PMO
    MU* VAL1(P000082.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000082.£V5PND
    MU* VAL1(P000082.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000082.£V5PNR
    MU* VAL1(P000082.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000082.£V5PTC
    MU* VAL1(P000082.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000082.£V5PEN
    MU* VAL1(P000082.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000082.£V5PQT
      *
     C                   EVAL      F000082.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000082.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000082.£40FLU='03'
     C                   EVAL      F000082.£40F_MSO='1'
     C                   EVAL      F000082.£40F_MWL=' '
     C                   EVAL      F000082.£40F_MWC='1'
     C                   EVAL      F000082.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000082.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000082.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000082.£40FDE
     C                   EVAL      £40FRE=F000082.£40FRE
     C                   EVAL      £40FLU=F000082.£40FLU
     C                   EVAL      £40F_MSO=F000082.£40F_MSO
     C                   EVAL      £40F_MWL=F000082.£40F_MWL
     C                   EVAL      £40F_MWC=F000082.£40F_MWC
     C                   EVAL      £40FTO(1)=F000082.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000082.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000082.£40FTO(3)
      *
     C                   EVAL      D000082.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000082.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000082.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000082.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000082.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000082.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000082.SUPP_NUU(1)= 1
     C                   EVAL      D000082.SUPP_NUU(2)= 2
     C                   EVAL      D000082.SUPP_NUU(3)= 3
     C                   EVAL      D000082.SUPP_NUG(1)= 11
     C                   EVAL      D000082.SUPP_NUG(2)= 21
     C                   EVAL      D000082.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000082.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000082.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000082.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000082.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000082.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000082.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000082.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000082.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000082.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000082.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000082.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000082.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000083
      *--------------------------------------------------------------*
     C     R000083       BEGSR
      * This is Subroutine  R000083
     C                   CLEAR                   U000083
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000083.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000083.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000083.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000083.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000083.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000083.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000083.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000083.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000083.£UIBK1
      *
     C                   CLEAR                   P000083
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000083.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000083.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000083.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000083.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000083.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000083.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000083.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000083.£V5PNR= 5
    MU* VAL1(P000083.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000083.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000083.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000083.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000083.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000083.£V5PQT=130,425
    MU* VAL1(P000083.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000083.£V5PTD
    MU* VAL1(P000083.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000083.£V5PMO
    MU* VAL1(P000083.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000083.£V5PND
    MU* VAL1(P000083.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000083.£V5PNR
    MU* VAL1(P000083.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000083.£V5PTC
    MU* VAL1(P000083.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000083.£V5PEN
    MU* VAL1(P000083.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000083.£V5PQT
      *
     C                   EVAL      F000083.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000083.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000083.£40FLU='03'
     C                   EVAL      F000083.£40F_MSO='1'
     C                   EVAL      F000083.£40F_MWL=' '
     C                   EVAL      F000083.£40F_MWC='1'
     C                   EVAL      F000083.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000083.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000083.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000083.£40FDE
     C                   EVAL      £40FRE=F000083.£40FRE
     C                   EVAL      £40FLU=F000083.£40FLU
     C                   EVAL      £40F_MSO=F000083.£40F_MSO
     C                   EVAL      £40F_MWL=F000083.£40F_MWL
     C                   EVAL      £40F_MWC=F000083.£40F_MWC
     C                   EVAL      £40FTO(1)=F000083.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000083.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000083.£40FTO(3)
      *
     C                   EVAL      D000083.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000083.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000083.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000083.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000083.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000083.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000083.SUPP_NUU(1)= 1
     C                   EVAL      D000083.SUPP_NUU(2)= 2
     C                   EVAL      D000083.SUPP_NUU(3)= 3
     C                   EVAL      D000083.SUPP_NUG(1)= 11
     C                   EVAL      D000083.SUPP_NUG(2)= 21
     C                   EVAL      D000083.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000083.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000083.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000083.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000083.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000083.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000083.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000083.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000083.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000083.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000083.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000083.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000083.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000084
      *--------------------------------------------------------------*
     C     R000084       BEGSR
      * This is Subroutine  R000084
     C                   CLEAR                   U000084
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000084.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000084.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000084.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000084.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000084.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000084.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000084.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000084.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000084.£UIBK1
      *
     C                   CLEAR                   P000084
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000084.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000084.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000084.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000084.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000084.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000084.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000084.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000084.£V5PNR= 5
    MU* VAL1(P000084.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000084.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000084.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000084.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000084.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000084.£V5PQT=130,425
    MU* VAL1(P000084.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000084.£V5PTD
    MU* VAL1(P000084.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000084.£V5PMO
    MU* VAL1(P000084.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000084.£V5PND
    MU* VAL1(P000084.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000084.£V5PNR
    MU* VAL1(P000084.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000084.£V5PTC
    MU* VAL1(P000084.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000084.£V5PEN
    MU* VAL1(P000084.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000084.£V5PQT
      *
     C                   EVAL      F000084.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000084.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000084.£40FLU='03'
     C                   EVAL      F000084.£40F_MSO='1'
     C                   EVAL      F000084.£40F_MWL=' '
     C                   EVAL      F000084.£40F_MWC='1'
     C                   EVAL      F000084.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000084.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000084.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000084.£40FDE
     C                   EVAL      £40FRE=F000084.£40FRE
     C                   EVAL      £40FLU=F000084.£40FLU
     C                   EVAL      £40F_MSO=F000084.£40F_MSO
     C                   EVAL      £40F_MWL=F000084.£40F_MWL
     C                   EVAL      £40F_MWC=F000084.£40F_MWC
     C                   EVAL      £40FTO(1)=F000084.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000084.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000084.£40FTO(3)
      *
     C                   EVAL      D000084.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000084.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000084.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000084.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000084.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000084.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000084.SUPP_NUU(1)= 1
     C                   EVAL      D000084.SUPP_NUU(2)= 2
     C                   EVAL      D000084.SUPP_NUU(3)= 3
     C                   EVAL      D000084.SUPP_NUG(1)= 11
     C                   EVAL      D000084.SUPP_NUG(2)= 21
     C                   EVAL      D000084.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000084.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000084.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000084.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000084.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000084.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000084.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000084.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000084.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000084.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000084.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000084.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000084.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000085
      *--------------------------------------------------------------*
     C     R000085       BEGSR
      * This is Subroutine  R000085
     C                   CLEAR                   U000085
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000085.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000085.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000085.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000085.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000085.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000085.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000085.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000085.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000085.£UIBK1
      *
     C                   CLEAR                   P000085
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000085.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000085.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000085.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000085.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000085.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000085.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000085.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000085.£V5PNR= 5
    MU* VAL1(P000085.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000085.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000085.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000085.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000085.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000085.£V5PQT=130,425
    MU* VAL1(P000085.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000085.£V5PTD
    MU* VAL1(P000085.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000085.£V5PMO
    MU* VAL1(P000085.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000085.£V5PND
    MU* VAL1(P000085.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000085.£V5PNR
    MU* VAL1(P000085.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000085.£V5PTC
    MU* VAL1(P000085.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000085.£V5PEN
    MU* VAL1(P000085.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000085.£V5PQT
      *
     C                   EVAL      F000085.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000085.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000085.£40FLU='03'
     C                   EVAL      F000085.£40F_MSO='1'
     C                   EVAL      F000085.£40F_MWL=' '
     C                   EVAL      F000085.£40F_MWC='1'
     C                   EVAL      F000085.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000085.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000085.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000085.£40FDE
     C                   EVAL      £40FRE=F000085.£40FRE
     C                   EVAL      £40FLU=F000085.£40FLU
     C                   EVAL      £40F_MSO=F000085.£40F_MSO
     C                   EVAL      £40F_MWL=F000085.£40F_MWL
     C                   EVAL      £40F_MWC=F000085.£40F_MWC
     C                   EVAL      £40FTO(1)=F000085.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000085.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000085.£40FTO(3)
      *
     C                   EVAL      D000085.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000085.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000085.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000085.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000085.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000085.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000085.SUPP_NUU(1)= 1
     C                   EVAL      D000085.SUPP_NUU(2)= 2
     C                   EVAL      D000085.SUPP_NUU(3)= 3
     C                   EVAL      D000085.SUPP_NUG(1)= 11
     C                   EVAL      D000085.SUPP_NUG(2)= 21
     C                   EVAL      D000085.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000085.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000085.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000085.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000085.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000085.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000085.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000085.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000085.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000085.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000085.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000085.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000085.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000086
      *--------------------------------------------------------------*
     C     R000086       BEGSR
      * This is Subroutine  R000086
     C                   CLEAR                   U000086
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000086.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000086.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000086.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000086.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000086.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000086.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000086.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000086.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000086.£UIBK1
      *
     C                   CLEAR                   P000086
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000086.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000086.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000086.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000086.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000086.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000086.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000086.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000086.£V5PNR= 5
    MU* VAL1(P000086.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000086.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000086.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000086.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000086.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000086.£V5PQT=130,425
    MU* VAL1(P000086.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000086.£V5PTD
    MU* VAL1(P000086.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000086.£V5PMO
    MU* VAL1(P000086.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000086.£V5PND
    MU* VAL1(P000086.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000086.£V5PNR
    MU* VAL1(P000086.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000086.£V5PTC
    MU* VAL1(P000086.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000086.£V5PEN
    MU* VAL1(P000086.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000086.£V5PQT
      *
     C                   EVAL      F000086.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000086.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000086.£40FLU='03'
     C                   EVAL      F000086.£40F_MSO='1'
     C                   EVAL      F000086.£40F_MWL=' '
     C                   EVAL      F000086.£40F_MWC='1'
     C                   EVAL      F000086.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000086.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000086.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000086.£40FDE
     C                   EVAL      £40FRE=F000086.£40FRE
     C                   EVAL      £40FLU=F000086.£40FLU
     C                   EVAL      £40F_MSO=F000086.£40F_MSO
     C                   EVAL      £40F_MWL=F000086.£40F_MWL
     C                   EVAL      £40F_MWC=F000086.£40F_MWC
     C                   EVAL      £40FTO(1)=F000086.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000086.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000086.£40FTO(3)
      *
     C                   EVAL      D000086.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000086.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000086.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000086.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000086.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000086.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000086.SUPP_NUU(1)= 1
     C                   EVAL      D000086.SUPP_NUU(2)= 2
     C                   EVAL      D000086.SUPP_NUU(3)= 3
     C                   EVAL      D000086.SUPP_NUG(1)= 11
     C                   EVAL      D000086.SUPP_NUG(2)= 21
     C                   EVAL      D000086.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000086.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000086.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000086.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000086.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000086.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000086.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000086.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000086.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000086.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000086.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000086.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000086.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000087
      *--------------------------------------------------------------*
     C     R000087       BEGSR
      * This is Subroutine  R000087
     C                   CLEAR                   U000087
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000087.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000087.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000087.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000087.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000087.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000087.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000087.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000087.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000087.£UIBK1
      *
     C                   CLEAR                   P000087
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000087.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000087.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000087.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000087.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000087.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000087.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000087.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000087.£V5PNR= 5
    MU* VAL1(P000087.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000087.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000087.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000087.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000087.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000087.£V5PQT=130,425
    MU* VAL1(P000087.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000087.£V5PTD
    MU* VAL1(P000087.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000087.£V5PMO
    MU* VAL1(P000087.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000087.£V5PND
    MU* VAL1(P000087.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000087.£V5PNR
    MU* VAL1(P000087.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000087.£V5PTC
    MU* VAL1(P000087.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000087.£V5PEN
    MU* VAL1(P000087.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000087.£V5PQT
      *
     C                   EVAL      F000087.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000087.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000087.£40FLU='03'
     C                   EVAL      F000087.£40F_MSO='1'
     C                   EVAL      F000087.£40F_MWL=' '
     C                   EVAL      F000087.£40F_MWC='1'
     C                   EVAL      F000087.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000087.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000087.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000087.£40FDE
     C                   EVAL      £40FRE=F000087.£40FRE
     C                   EVAL      £40FLU=F000087.£40FLU
     C                   EVAL      £40F_MSO=F000087.£40F_MSO
     C                   EVAL      £40F_MWL=F000087.£40F_MWL
     C                   EVAL      £40F_MWC=F000087.£40F_MWC
     C                   EVAL      £40FTO(1)=F000087.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000087.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000087.£40FTO(3)
      *
     C                   EVAL      D000087.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000087.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000087.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000087.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000087.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000087.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000087.SUPP_NUU(1)= 1
     C                   EVAL      D000087.SUPP_NUU(2)= 2
     C                   EVAL      D000087.SUPP_NUU(3)= 3
     C                   EVAL      D000087.SUPP_NUG(1)= 11
     C                   EVAL      D000087.SUPP_NUG(2)= 21
     C                   EVAL      D000087.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000087.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000087.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000087.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000087.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000087.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000087.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000087.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000087.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000087.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000087.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000087.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000087.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000088
      *--------------------------------------------------------------*
     C     R000088       BEGSR
      * This is Subroutine  R000088
     C                   CLEAR                   U000088
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000088.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000088.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000088.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000088.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000088.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000088.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000088.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000088.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000088.£UIBK1
      *
     C                   CLEAR                   P000088
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000088.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000088.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000088.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000088.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000088.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000088.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000088.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000088.£V5PNR= 5
    MU* VAL1(P000088.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000088.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000088.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000088.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000088.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000088.£V5PQT=130,425
    MU* VAL1(P000088.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000088.£V5PTD
    MU* VAL1(P000088.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000088.£V5PMO
    MU* VAL1(P000088.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000088.£V5PND
    MU* VAL1(P000088.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000088.£V5PNR
    MU* VAL1(P000088.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000088.£V5PTC
    MU* VAL1(P000088.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000088.£V5PEN
    MU* VAL1(P000088.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000088.£V5PQT
      *
     C                   EVAL      F000088.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000088.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000088.£40FLU='03'
     C                   EVAL      F000088.£40F_MSO='1'
     C                   EVAL      F000088.£40F_MWL=' '
     C                   EVAL      F000088.£40F_MWC='1'
     C                   EVAL      F000088.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000088.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000088.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000088.£40FDE
     C                   EVAL      £40FRE=F000088.£40FRE
     C                   EVAL      £40FLU=F000088.£40FLU
     C                   EVAL      £40F_MSO=F000088.£40F_MSO
     C                   EVAL      £40F_MWL=F000088.£40F_MWL
     C                   EVAL      £40F_MWC=F000088.£40F_MWC
     C                   EVAL      £40FTO(1)=F000088.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000088.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000088.£40FTO(3)
      *
     C                   EVAL      D000088.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000088.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000088.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000088.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000088.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000088.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000088.SUPP_NUU(1)= 1
     C                   EVAL      D000088.SUPP_NUU(2)= 2
     C                   EVAL      D000088.SUPP_NUU(3)= 3
     C                   EVAL      D000088.SUPP_NUG(1)= 11
     C                   EVAL      D000088.SUPP_NUG(2)= 21
     C                   EVAL      D000088.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000088.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000088.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000088.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000088.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000088.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000088.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000088.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000088.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000088.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000088.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000088.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000088.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000089
      *--------------------------------------------------------------*
     C     R000089       BEGSR
      * This is Subroutine  R000089
     C                   CLEAR                   U000089
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000089.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000089.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000089.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000089.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000089.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000089.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000089.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000089.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000089.£UIBK1
      *
     C                   CLEAR                   P000089
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000089.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000089.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000089.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000089.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000089.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000089.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000089.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000089.£V5PNR= 5
    MU* VAL1(P000089.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000089.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000089.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000089.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000089.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000089.£V5PQT=130,425
    MU* VAL1(P000089.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000089.£V5PTD
    MU* VAL1(P000089.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000089.£V5PMO
    MU* VAL1(P000089.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000089.£V5PND
    MU* VAL1(P000089.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000089.£V5PNR
    MU* VAL1(P000089.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000089.£V5PTC
    MU* VAL1(P000089.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000089.£V5PEN
    MU* VAL1(P000089.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000089.£V5PQT
      *
     C                   EVAL      F000089.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000089.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000089.£40FLU='03'
     C                   EVAL      F000089.£40F_MSO='1'
     C                   EVAL      F000089.£40F_MWL=' '
     C                   EVAL      F000089.£40F_MWC='1'
     C                   EVAL      F000089.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000089.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000089.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000089.£40FDE
     C                   EVAL      £40FRE=F000089.£40FRE
     C                   EVAL      £40FLU=F000089.£40FLU
     C                   EVAL      £40F_MSO=F000089.£40F_MSO
     C                   EVAL      £40F_MWL=F000089.£40F_MWL
     C                   EVAL      £40F_MWC=F000089.£40F_MWC
     C                   EVAL      £40FTO(1)=F000089.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000089.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000089.£40FTO(3)
      *
     C                   EVAL      D000089.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000089.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000089.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000089.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000089.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000089.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000089.SUPP_NUU(1)= 1
     C                   EVAL      D000089.SUPP_NUU(2)= 2
     C                   EVAL      D000089.SUPP_NUU(3)= 3
     C                   EVAL      D000089.SUPP_NUG(1)= 11
     C                   EVAL      D000089.SUPP_NUG(2)= 21
     C                   EVAL      D000089.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000089.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000089.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000089.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000089.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000089.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000089.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000089.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000089.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000089.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000089.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000089.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000089.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000090
      *--------------------------------------------------------------*
     C     R000090       BEGSR
      * This is Subroutine  R000090
     C                   CLEAR                   U000090
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000090.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000090.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000090.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000090.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000090.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000090.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000090.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000090.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000090.£UIBK1
      *
     C                   CLEAR                   P000090
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000090.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000090.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000090.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000090.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000090.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000090.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000090.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000090.£V5PNR= 5
    MU* VAL1(P000090.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000090.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000090.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000090.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000090.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000090.£V5PQT=130,425
    MU* VAL1(P000090.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000090.£V5PTD
    MU* VAL1(P000090.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000090.£V5PMO
    MU* VAL1(P000090.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000090.£V5PND
    MU* VAL1(P000090.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000090.£V5PNR
    MU* VAL1(P000090.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000090.£V5PTC
    MU* VAL1(P000090.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000090.£V5PEN
    MU* VAL1(P000090.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000090.£V5PQT
      *
     C                   EVAL      F000090.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000090.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000090.£40FLU='03'
     C                   EVAL      F000090.£40F_MSO='1'
     C                   EVAL      F000090.£40F_MWL=' '
     C                   EVAL      F000090.£40F_MWC='1'
     C                   EVAL      F000090.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000090.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000090.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000090.£40FDE
     C                   EVAL      £40FRE=F000090.£40FRE
     C                   EVAL      £40FLU=F000090.£40FLU
     C                   EVAL      £40F_MSO=F000090.£40F_MSO
     C                   EVAL      £40F_MWL=F000090.£40F_MWL
     C                   EVAL      £40F_MWC=F000090.£40F_MWC
     C                   EVAL      £40FTO(1)=F000090.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000090.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000090.£40FTO(3)
      *
     C                   EVAL      D000090.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000090.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000090.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000090.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000090.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000090.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000090.SUPP_NUU(1)= 1
     C                   EVAL      D000090.SUPP_NUU(2)= 2
     C                   EVAL      D000090.SUPP_NUU(3)= 3
     C                   EVAL      D000090.SUPP_NUG(1)= 11
     C                   EVAL      D000090.SUPP_NUG(2)= 21
     C                   EVAL      D000090.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000090.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000090.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000090.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000090.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000090.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000090.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000090.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000090.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000090.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000090.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000090.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000090.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000091
      *--------------------------------------------------------------*
     C     R000091       BEGSR
      * This is Subroutine  R000091
     C                   CLEAR                   U000091
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000091.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000091.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000091.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000091.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000091.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000091.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000091.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000091.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000091.£UIBK1
      *
     C                   CLEAR                   P000091
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000091.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000091.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000091.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000091.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000091.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000091.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000091.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000091.£V5PNR= 5
    MU* VAL1(P000091.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000091.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000091.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000091.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000091.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000091.£V5PQT=130,425
    MU* VAL1(P000091.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000091.£V5PTD
    MU* VAL1(P000091.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000091.£V5PMO
    MU* VAL1(P000091.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000091.£V5PND
    MU* VAL1(P000091.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000091.£V5PNR
    MU* VAL1(P000091.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000091.£V5PTC
    MU* VAL1(P000091.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000091.£V5PEN
    MU* VAL1(P000091.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000091.£V5PQT
      *
     C                   EVAL      F000091.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000091.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000091.£40FLU='03'
     C                   EVAL      F000091.£40F_MSO='1'
     C                   EVAL      F000091.£40F_MWL=' '
     C                   EVAL      F000091.£40F_MWC='1'
     C                   EVAL      F000091.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000091.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000091.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000091.£40FDE
     C                   EVAL      £40FRE=F000091.£40FRE
     C                   EVAL      £40FLU=F000091.£40FLU
     C                   EVAL      £40F_MSO=F000091.£40F_MSO
     C                   EVAL      £40F_MWL=F000091.£40F_MWL
     C                   EVAL      £40F_MWC=F000091.£40F_MWC
     C                   EVAL      £40FTO(1)=F000091.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000091.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000091.£40FTO(3)
      *
     C                   EVAL      D000091.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000091.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000091.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000091.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000091.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000091.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000091.SUPP_NUU(1)= 1
     C                   EVAL      D000091.SUPP_NUU(2)= 2
     C                   EVAL      D000091.SUPP_NUU(3)= 3
     C                   EVAL      D000091.SUPP_NUG(1)= 11
     C                   EVAL      D000091.SUPP_NUG(2)= 21
     C                   EVAL      D000091.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000091.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000091.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000091.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000091.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000091.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000091.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000091.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000091.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000091.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000091.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000091.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000091.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000092
      *--------------------------------------------------------------*
     C     R000092       BEGSR
      * This is Subroutine  R000092
     C                   CLEAR                   U000092
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000092.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000092.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000092.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000092.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000092.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000092.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000092.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000092.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000092.£UIBK1
      *
     C                   CLEAR                   P000092
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000092.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000092.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000092.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000092.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000092.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000092.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000092.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000092.£V5PNR= 5
    MU* VAL1(P000092.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000092.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000092.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000092.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000092.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000092.£V5PQT=130,425
    MU* VAL1(P000092.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000092.£V5PTD
    MU* VAL1(P000092.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000092.£V5PMO
    MU* VAL1(P000092.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000092.£V5PND
    MU* VAL1(P000092.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000092.£V5PNR
    MU* VAL1(P000092.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000092.£V5PTC
    MU* VAL1(P000092.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000092.£V5PEN
    MU* VAL1(P000092.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000092.£V5PQT
      *
     C                   EVAL      F000092.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000092.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000092.£40FLU='03'
     C                   EVAL      F000092.£40F_MSO='1'
     C                   EVAL      F000092.£40F_MWL=' '
     C                   EVAL      F000092.£40F_MWC='1'
     C                   EVAL      F000092.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000092.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000092.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000092.£40FDE
     C                   EVAL      £40FRE=F000092.£40FRE
     C                   EVAL      £40FLU=F000092.£40FLU
     C                   EVAL      £40F_MSO=F000092.£40F_MSO
     C                   EVAL      £40F_MWL=F000092.£40F_MWL
     C                   EVAL      £40F_MWC=F000092.£40F_MWC
     C                   EVAL      £40FTO(1)=F000092.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000092.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000092.£40FTO(3)
      *
     C                   EVAL      D000092.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000092.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000092.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000092.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000092.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000092.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000092.SUPP_NUU(1)= 1
     C                   EVAL      D000092.SUPP_NUU(2)= 2
     C                   EVAL      D000092.SUPP_NUU(3)= 3
     C                   EVAL      D000092.SUPP_NUG(1)= 11
     C                   EVAL      D000092.SUPP_NUG(2)= 21
     C                   EVAL      D000092.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000092.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000092.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000092.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000092.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000092.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000092.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000092.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000092.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000092.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000092.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000092.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000092.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000093
      *--------------------------------------------------------------*
     C     R000093       BEGSR
      * This is Subroutine  R000093
     C                   CLEAR                   U000093
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000093.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000093.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000093.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000093.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000093.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000093.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000093.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000093.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000093.£UIBK1
      *
     C                   CLEAR                   P000093
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000093.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000093.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000093.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000093.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000093.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000093.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000093.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000093.£V5PNR= 5
    MU* VAL1(P000093.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000093.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000093.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000093.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000093.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000093.£V5PQT=130,425
    MU* VAL1(P000093.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000093.£V5PTD
    MU* VAL1(P000093.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000093.£V5PMO
    MU* VAL1(P000093.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000093.£V5PND
    MU* VAL1(P000093.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000093.£V5PNR
    MU* VAL1(P000093.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000093.£V5PTC
    MU* VAL1(P000093.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000093.£V5PEN
    MU* VAL1(P000093.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000093.£V5PQT
      *
     C                   EVAL      F000093.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000093.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000093.£40FLU='03'
     C                   EVAL      F000093.£40F_MSO='1'
     C                   EVAL      F000093.£40F_MWL=' '
     C                   EVAL      F000093.£40F_MWC='1'
     C                   EVAL      F000093.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000093.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000093.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000093.£40FDE
     C                   EVAL      £40FRE=F000093.£40FRE
     C                   EVAL      £40FLU=F000093.£40FLU
     C                   EVAL      £40F_MSO=F000093.£40F_MSO
     C                   EVAL      £40F_MWL=F000093.£40F_MWL
     C                   EVAL      £40F_MWC=F000093.£40F_MWC
     C                   EVAL      £40FTO(1)=F000093.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000093.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000093.£40FTO(3)
      *
     C                   EVAL      D000093.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000093.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000093.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000093.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000093.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000093.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000093.SUPP_NUU(1)= 1
     C                   EVAL      D000093.SUPP_NUU(2)= 2
     C                   EVAL      D000093.SUPP_NUU(3)= 3
     C                   EVAL      D000093.SUPP_NUG(1)= 11
     C                   EVAL      D000093.SUPP_NUG(2)= 21
     C                   EVAL      D000093.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000093.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000093.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000093.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000093.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000093.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000093.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000093.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000093.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000093.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000093.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000093.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000093.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000094
      *--------------------------------------------------------------*
     C     R000094       BEGSR
      * This is Subroutine  R000094
     C                   CLEAR                   U000094
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000094.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000094.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000094.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000094.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000094.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000094.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000094.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000094.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000094.£UIBK1
      *
     C                   CLEAR                   P000094
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000094.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000094.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000094.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000094.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000094.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000094.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000094.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000094.£V5PNR= 5
    MU* VAL1(P000094.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000094.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000094.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000094.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000094.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000094.£V5PQT=130,425
    MU* VAL1(P000094.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000094.£V5PTD
    MU* VAL1(P000094.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000094.£V5PMO
    MU* VAL1(P000094.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000094.£V5PND
    MU* VAL1(P000094.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000094.£V5PNR
    MU* VAL1(P000094.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000094.£V5PTC
    MU* VAL1(P000094.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000094.£V5PEN
    MU* VAL1(P000094.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000094.£V5PQT
      *
     C                   EVAL      F000094.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000094.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000094.£40FLU='03'
     C                   EVAL      F000094.£40F_MSO='1'
     C                   EVAL      F000094.£40F_MWL=' '
     C                   EVAL      F000094.£40F_MWC='1'
     C                   EVAL      F000094.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000094.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000094.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000094.£40FDE
     C                   EVAL      £40FRE=F000094.£40FRE
     C                   EVAL      £40FLU=F000094.£40FLU
     C                   EVAL      £40F_MSO=F000094.£40F_MSO
     C                   EVAL      £40F_MWL=F000094.£40F_MWL
     C                   EVAL      £40F_MWC=F000094.£40F_MWC
     C                   EVAL      £40FTO(1)=F000094.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000094.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000094.£40FTO(3)
      *
     C                   EVAL      D000094.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000094.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000094.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000094.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000094.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000094.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000094.SUPP_NUU(1)= 1
     C                   EVAL      D000094.SUPP_NUU(2)= 2
     C                   EVAL      D000094.SUPP_NUU(3)= 3
     C                   EVAL      D000094.SUPP_NUG(1)= 11
     C                   EVAL      D000094.SUPP_NUG(2)= 21
     C                   EVAL      D000094.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000094.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000094.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000094.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000094.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000094.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000094.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000094.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000094.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000094.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000094.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000094.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000094.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000095
      *--------------------------------------------------------------*
     C     R000095       BEGSR
      * This is Subroutine  R000095
     C                   CLEAR                   U000095
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000095.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000095.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000095.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000095.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000095.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000095.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000095.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000095.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000095.£UIBK1
      *
     C                   CLEAR                   P000095
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000095.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000095.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000095.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000095.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000095.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000095.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000095.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000095.£V5PNR= 5
    MU* VAL1(P000095.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000095.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000095.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000095.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000095.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000095.£V5PQT=130,425
    MU* VAL1(P000095.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000095.£V5PTD
    MU* VAL1(P000095.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000095.£V5PMO
    MU* VAL1(P000095.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000095.£V5PND
    MU* VAL1(P000095.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000095.£V5PNR
    MU* VAL1(P000095.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000095.£V5PTC
    MU* VAL1(P000095.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000095.£V5PEN
    MU* VAL1(P000095.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000095.£V5PQT
      *
     C                   EVAL      F000095.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000095.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000095.£40FLU='03'
     C                   EVAL      F000095.£40F_MSO='1'
     C                   EVAL      F000095.£40F_MWL=' '
     C                   EVAL      F000095.£40F_MWC='1'
     C                   EVAL      F000095.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000095.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000095.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000095.£40FDE
     C                   EVAL      £40FRE=F000095.£40FRE
     C                   EVAL      £40FLU=F000095.£40FLU
     C                   EVAL      £40F_MSO=F000095.£40F_MSO
     C                   EVAL      £40F_MWL=F000095.£40F_MWL
     C                   EVAL      £40F_MWC=F000095.£40F_MWC
     C                   EVAL      £40FTO(1)=F000095.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000095.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000095.£40FTO(3)
      *
     C                   EVAL      D000095.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000095.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000095.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000095.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000095.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000095.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000095.SUPP_NUU(1)= 1
     C                   EVAL      D000095.SUPP_NUU(2)= 2
     C                   EVAL      D000095.SUPP_NUU(3)= 3
     C                   EVAL      D000095.SUPP_NUG(1)= 11
     C                   EVAL      D000095.SUPP_NUG(2)= 21
     C                   EVAL      D000095.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000095.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000095.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000095.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000095.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000095.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000095.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000095.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000095.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000095.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000095.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000095.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000095.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000096
      *--------------------------------------------------------------*
     C     R000096       BEGSR
      * This is Subroutine  R000096
     C                   CLEAR                   U000096
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000096.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000096.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000096.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000096.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000096.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000096.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000096.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000096.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000096.£UIBK1
      *
     C                   CLEAR                   P000096
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000096.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000096.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000096.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000096.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000096.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000096.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000096.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000096.£V5PNR= 5
    MU* VAL1(P000096.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000096.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000096.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000096.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000096.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000096.£V5PQT=130,425
    MU* VAL1(P000096.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000096.£V5PTD
    MU* VAL1(P000096.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000096.£V5PMO
    MU* VAL1(P000096.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000096.£V5PND
    MU* VAL1(P000096.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000096.£V5PNR
    MU* VAL1(P000096.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000096.£V5PTC
    MU* VAL1(P000096.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000096.£V5PEN
    MU* VAL1(P000096.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000096.£V5PQT
      *
     C                   EVAL      F000096.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000096.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000096.£40FLU='03'
     C                   EVAL      F000096.£40F_MSO='1'
     C                   EVAL      F000096.£40F_MWL=' '
     C                   EVAL      F000096.£40F_MWC='1'
     C                   EVAL      F000096.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000096.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000096.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000096.£40FDE
     C                   EVAL      £40FRE=F000096.£40FRE
     C                   EVAL      £40FLU=F000096.£40FLU
     C                   EVAL      £40F_MSO=F000096.£40F_MSO
     C                   EVAL      £40F_MWL=F000096.£40F_MWL
     C                   EVAL      £40F_MWC=F000096.£40F_MWC
     C                   EVAL      £40FTO(1)=F000096.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000096.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000096.£40FTO(3)
      *
     C                   EVAL      D000096.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000096.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000096.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000096.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000096.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000096.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000096.SUPP_NUU(1)= 1
     C                   EVAL      D000096.SUPP_NUU(2)= 2
     C                   EVAL      D000096.SUPP_NUU(3)= 3
     C                   EVAL      D000096.SUPP_NUG(1)= 11
     C                   EVAL      D000096.SUPP_NUG(2)= 21
     C                   EVAL      D000096.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000096.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000096.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000096.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000096.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000096.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000096.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000096.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000096.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000096.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000096.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000096.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000096.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000097
      *--------------------------------------------------------------*
     C     R000097       BEGSR
      * This is Subroutine  R000097
     C                   CLEAR                   U000097
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000097.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000097.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000097.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000097.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000097.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000097.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000097.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000097.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000097.£UIBK1
      *
     C                   CLEAR                   P000097
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000097.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000097.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000097.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000097.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000097.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000097.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000097.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000097.£V5PNR= 5
    MU* VAL1(P000097.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000097.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000097.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000097.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000097.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000097.£V5PQT=130,425
    MU* VAL1(P000097.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000097.£V5PTD
    MU* VAL1(P000097.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000097.£V5PMO
    MU* VAL1(P000097.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000097.£V5PND
    MU* VAL1(P000097.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000097.£V5PNR
    MU* VAL1(P000097.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000097.£V5PTC
    MU* VAL1(P000097.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000097.£V5PEN
    MU* VAL1(P000097.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000097.£V5PQT
      *
     C                   EVAL      F000097.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000097.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000097.£40FLU='03'
     C                   EVAL      F000097.£40F_MSO='1'
     C                   EVAL      F000097.£40F_MWL=' '
     C                   EVAL      F000097.£40F_MWC='1'
     C                   EVAL      F000097.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000097.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000097.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000097.£40FDE
     C                   EVAL      £40FRE=F000097.£40FRE
     C                   EVAL      £40FLU=F000097.£40FLU
     C                   EVAL      £40F_MSO=F000097.£40F_MSO
     C                   EVAL      £40F_MWL=F000097.£40F_MWL
     C                   EVAL      £40F_MWC=F000097.£40F_MWC
     C                   EVAL      £40FTO(1)=F000097.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000097.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000097.£40FTO(3)
      *
     C                   EVAL      D000097.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000097.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000097.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000097.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000097.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000097.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000097.SUPP_NUU(1)= 1
     C                   EVAL      D000097.SUPP_NUU(2)= 2
     C                   EVAL      D000097.SUPP_NUU(3)= 3
     C                   EVAL      D000097.SUPP_NUG(1)= 11
     C                   EVAL      D000097.SUPP_NUG(2)= 21
     C                   EVAL      D000097.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000097.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000097.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000097.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000097.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000097.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000097.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000097.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000097.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000097.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000097.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000097.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000097.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000098
      *--------------------------------------------------------------*
     C     R000098       BEGSR
      * This is Subroutine  R000098
     C                   CLEAR                   U000098
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000098.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000098.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000098.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000098.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000098.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000098.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000098.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000098.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000098.£UIBK1
      *
     C                   CLEAR                   P000098
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000098.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000098.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000098.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000098.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000098.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000098.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000098.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000098.£V5PNR= 5
    MU* VAL1(P000098.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000098.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000098.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000098.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000098.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000098.£V5PQT=130,425
    MU* VAL1(P000098.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000098.£V5PTD
    MU* VAL1(P000098.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000098.£V5PMO
    MU* VAL1(P000098.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000098.£V5PND
    MU* VAL1(P000098.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000098.£V5PNR
    MU* VAL1(P000098.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000098.£V5PTC
    MU* VAL1(P000098.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000098.£V5PEN
    MU* VAL1(P000098.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000098.£V5PQT
      *
     C                   EVAL      F000098.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000098.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000098.£40FLU='03'
     C                   EVAL      F000098.£40F_MSO='1'
     C                   EVAL      F000098.£40F_MWL=' '
     C                   EVAL      F000098.£40F_MWC='1'
     C                   EVAL      F000098.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000098.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000098.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000098.£40FDE
     C                   EVAL      £40FRE=F000098.£40FRE
     C                   EVAL      £40FLU=F000098.£40FLU
     C                   EVAL      £40F_MSO=F000098.£40F_MSO
     C                   EVAL      £40F_MWL=F000098.£40F_MWL
     C                   EVAL      £40F_MWC=F000098.£40F_MWC
     C                   EVAL      £40FTO(1)=F000098.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000098.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000098.£40FTO(3)
      *
     C                   EVAL      D000098.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000098.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000098.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000098.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000098.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000098.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000098.SUPP_NUU(1)= 1
     C                   EVAL      D000098.SUPP_NUU(2)= 2
     C                   EVAL      D000098.SUPP_NUU(3)= 3
     C                   EVAL      D000098.SUPP_NUG(1)= 11
     C                   EVAL      D000098.SUPP_NUG(2)= 21
     C                   EVAL      D000098.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000098.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000098.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000098.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000098.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000098.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000098.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000098.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000098.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000098.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000098.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000098.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000098.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000099
      *--------------------------------------------------------------*
     C     R000099       BEGSR
      * This is Subroutine  R000099
     C                   CLEAR                   U000099
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000099.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000099.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000099.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000099.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000099.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000099.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000099.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000099.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000099.£UIBK1
      *
     C                   CLEAR                   P000099
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000099.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000099.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000099.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000099.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000099.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000099.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000099.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000099.£V5PNR= 5
    MU* VAL1(P000099.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000099.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000099.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000099.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000099.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000099.£V5PQT=130,425
    MU* VAL1(P000099.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000099.£V5PTD
    MU* VAL1(P000099.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000099.£V5PMO
    MU* VAL1(P000099.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000099.£V5PND
    MU* VAL1(P000099.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000099.£V5PNR
    MU* VAL1(P000099.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000099.£V5PTC
    MU* VAL1(P000099.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000099.£V5PEN
    MU* VAL1(P000099.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000099.£V5PQT
      *
     C                   EVAL      F000099.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000099.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000099.£40FLU='03'
     C                   EVAL      F000099.£40F_MSO='1'
     C                   EVAL      F000099.£40F_MWL=' '
     C                   EVAL      F000099.£40F_MWC='1'
     C                   EVAL      F000099.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000099.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000099.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000099.£40FDE
     C                   EVAL      £40FRE=F000099.£40FRE
     C                   EVAL      £40FLU=F000099.£40FLU
     C                   EVAL      £40F_MSO=F000099.£40F_MSO
     C                   EVAL      £40F_MWL=F000099.£40F_MWL
     C                   EVAL      £40F_MWC=F000099.£40F_MWC
     C                   EVAL      £40FTO(1)=F000099.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000099.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000099.£40FTO(3)
      *
     C                   EVAL      D000099.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000099.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000099.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000099.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000099.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000099.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000099.SUPP_NUU(1)= 1
     C                   EVAL      D000099.SUPP_NUU(2)= 2
     C                   EVAL      D000099.SUPP_NUU(3)= 3
     C                   EVAL      D000099.SUPP_NUG(1)= 11
     C                   EVAL      D000099.SUPP_NUG(2)= 21
     C                   EVAL      D000099.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000099.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000099.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000099.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000099.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000099.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000099.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000099.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000099.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000099.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000099.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000099.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000099.SUPP_NUG(3)
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Subroutine  R000100
      *--------------------------------------------------------------*
     C     R000100       BEGSR
      * This is Subroutine  R000100
     C                   CLEAR                   U000100
     C                   CLEAR                   £UIBDS
    MU* VAL1(U000100.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      U000100.£UIBT1='CN'                          COSTANTE
    MU* VAL1(U000100.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      U000100.£UIBP1='CLI'                         COSTANTE
    MU* VAL1(U000100.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      U000100.£UIBK1='C0001'                       COSTANTE
    MU* VAL1(£UIBT1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBT1=U000100.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=U000100.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=U000100.£UIBK1
      *
     C                   CLEAR                   P000100
     C                   CLEAR                   £V5PDS
    MU* VAL1(P000100.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      P000100.£V5PTD='DA'                          COSTANTE
    MU* VAL1(P000100.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      P000100.£V5PMO='001'                         COSTANTE
    MU* VAL1(P000100.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      P000100.£V5PND='DA000001'                    COSTANTE
    MU* VAL1(P000100.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      P000100.£V5PNR= 5
    MU* VAL1(P000100.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      P000100.£V5PTC='CLI'                         COSTANTE
    MU* VAL1(P000100.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      P000100.£V5PEN='C0001'                       COSTANTE
    MU* VAL1(P000100.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      P000100.£V5PQT=130,425
    MU* VAL1(P000100.£V5PTD) VAL2('DA') COMP(EQ)
     C                   EVAL      £V5PTD=P000100.£V5PTD
    MU* VAL1(P000100.£V5PMO) VAL2('001') COMP(EQ)
     C                   EVAL      £V5PMO=P000100.£V5PMO
    MU* VAL1(P000100.£V5PND) VAL2('DA000001') COMP(EQ)
     C                   EVAL      £V5PND=P000100.£V5PND
    MU* VAL1(P000100.£V5PNR) VAL2(5) COMP(EQ)
     C                   EVAL      £V5PNR=P000100.£V5PNR
    MU* VAL1(P000100.£V5PTC) VAL2('CLI') COMP(EQ)
     C                   EVAL      £V5PTC=P000100.£V5PTC
    MU* VAL1(P000100.£V5PEN) VAL2('C0001') COMP(EQ)
     C                   EVAL      £V5PEN=P000100.£V5PEN
    MU* VAL1(P000100.£V5PQT) VAL2(130,425) COMP(EQ)
     C                   EVAL      £V5PQT=P000100.£V5PQT
      *
     C                   EVAL      F000100.£40FDE='Lista Inclusi'               COSTANTE
     C                   EVAL      F000100.£40FRE='S-LI_002/A'                  COSTANTE
     C                   EVAL      F000100.£40FLU='03'
     C                   EVAL      F000100.£40F_MSO='1'
     C                   EVAL      F000100.£40F_MWL=' '
     C                   EVAL      F000100.£40F_MWC='1'
     C                   EVAL      F000100.£40FTO(1)='CLI'                      COSTANTE
     C                   EVAL      F000100.£40FTO(2)='FOR'                      COSTANTE
     C                   EVAL      F000100.£40FTO(3)='AGE'                      COSTANTE
     C                   EVAL      £40FDE=F000100.£40FDE
     C                   EVAL      £40FRE=F000100.£40FRE
     C                   EVAL      £40FLU=F000100.£40FLU
     C                   EVAL      £40F_MSO=F000100.£40F_MSO
     C                   EVAL      £40F_MWL=F000100.£40F_MWL
     C                   EVAL      £40F_MWC=F000100.£40F_MWC
     C                   EVAL      £40FTO(1)=F000100.£40FTO(1)
     C                   EVAL      £40FTO(2)=F000100.£40FTO(2)
     C                   EVAL      £40FTO(3)=F000100.£40FTO(3)
      *
     C                   EVAL      D000100.SUPP_COD(1)='AAA'                    COSTANTE
     C                   EVAL      D000100.SUPP_COD(2)='BBB'                    COSTANTE
     C                   EVAL      D000100.SUPP_COD(3)='CCC'                    COSTANTE
     C                   EVAL      D000100.SUPP_DES(1)='Primo'                  COSTANTE
     C                   EVAL      D000100.SUPP_DES(2)='Secondo'                COSTANTE
     C                   EVAL      D000100.SUPP_DES(3)='Terzo'                  COSTANTE
     C                   EVAL      D000100.SUPP_NUU(1)= 1
     C                   EVAL      D000100.SUPP_NUU(2)= 2
     C                   EVAL      D000100.SUPP_NUU(3)= 3
     C                   EVAL      D000100.SUPP_NUG(1)= 11
     C                   EVAL      D000100.SUPP_NUG(2)= 21
     C                   EVAL      D000100.SUPP_NUG(3)= 31
     C                   EVAL      SUPP_COD(1)=D000100.SUPP_COD(1)
     C                   EVAL      SUPP_COD(2)=D000100.SUPP_COD(2)
     C                   EVAL      SUPP_COD(3)=D000100.SUPP_COD(3)
     C                   EVAL      SUPP_DES(1)=D000100.SUPP_DES(1)
     C                   EVAL      SUPP_DES(2)=D000100.SUPP_DES(2)
     C                   EVAL      SUPP_DES(3)=D000100.SUPP_DES(3)
     C                   EVAL      SUPP_NUU(1)=D000100.SUPP_NUU(1)
     C                   EVAL      SUPP_NUU(2)=D000100.SUPP_NUU(2)
     C                   EVAL      SUPP_NUU(3)=D000100.SUPP_NUU(3)
     C                   EVAL      SUPP_NUG(1)=D000100.SUPP_NUG(1)
     C                   EVAL      SUPP_NUG(2)=D000100.SUPP_NUG(2)
     C                   EVAL      SUPP_NUG(3)=D000100.SUPP_NUG(3)
      *
     C                   ENDSR
