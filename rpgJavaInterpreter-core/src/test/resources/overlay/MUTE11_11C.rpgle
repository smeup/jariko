     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 13/08/19  001059 BMA Creazione
     V* 14/08/19  001059 BMA Restituisco risultato in £FUNQT
     V* 19/08/19  V5R1    CM Check-out 001059 in SMEDEV
     V* 22/08/19  001071 BMA Migliorati commenti esplicativi
     V* ==============================================================
      *
      * Calculates number of Fibonacci and return the result
      *
      ****************************************************************
      * Il like di una DS ottiene un campo stringa della stessa lunghezza della DS.
      * Il punto più spinoso è riuscire a fare in modo che i dati vengano "rimappati" in modo
      * corretto quando passano da DS a campo stringa e viceversa, in particolare se ci sono
      * sottocampi numerici.
      * Valutare se non sia implementativamente più facile agire come il LIKEDS che crea una
      * DS qualificata copia di quella di origine.
     D £FUNW1          S                   LIKE(£FUND1)
     D £FUNW2          S                   LIKE(£FUND2)
      *----------------------------------------------------------------
     D £FUND1          DS           512
     D  £FUNCO                 1     10
     D  £FUNNR                11     19  0
     D  £FUNGR                20     22
     D  £FUNT1                23     25
     D  £FUNP1                26     35
     D  £FUNK1                36     50
     D  £FUNI1                51     65
     D  £FUNS1                66    100
     D  £FUNT2               101    103
     D  £FUNP2               104    113
     D  £FUNK2               114    128
     D  £FUNI2               129    143
     D  £FUNS2               144    178
     D  £FUNT3               179    181
     D  £FUNP3               182    191
     D  £FUNK3               192    206
     D  £FUNI3               207    221
     D  £FUNS3               222    255
     D  £FUNK4               256    270
     D  £FUNI4               271    285
     D  £FUNK5               286    300
     D  £FUNI5               301    315
     D  £FUNK6               316    330
     D  £FUNI6               331    345
     D  £FUNP4               346    346
     D  £FUN0A               347    349
     D  £FUN0B               350    352
     D  £FUN0C               353    354
     D  £FUN0D               355    357
     D  £FUN0E               358    360
     D  £FUN0F               361    365
     D  £FUN0G               366    370
     D  £FUN0H               371    373
     D  £FUN0I               374    376
     D  £FUN0L               377    379
     D  £FUN0M               380    382
     D  £FUN0N               383    385
     D  £FUN0O               386    388
     D  £FUN0P               389    391
     D  £FUNX1               392    392
     D  £FUNPS               393    432
     D  £FUN35               433    433N
     D  £FUN36               434    434N
     D  £FUNDT               435    442  0
     D  £FUNQT               443    457  5
     D  £FUNJS               458    459
     D  £FUNJE               460    464
     D  £FUNX2               465    465
     D  £FUNV1               466    470
     D  £FUNPO               471    480
     D  £FUNPE               481    490
     D  £FUNV2               491    491
     D  £FUNJH               492    501
     D  £FUNAT               502    502
     D  £FUNRS               503    503  0
     D  £FUNFT               504    504
     D  £FUNES               505    505
      *
      *
     D  £FUNQ1               443    443
      *
      *----------------------------------------------------------------
     D £FUND2          DS           512
      *----------------------------------------------------------------
      * Input Number
      *
     D NBR             S              9  0
     D RESULT          S              9  0 INZ(0)
     D COUNT           S              9  0
     D A               S              9  0 INZ(0)
     D B               S              9  0 INZ(1)
     D £FUNNP          S             10
     D £FUNFU          S             10
     D £FUNME          S             10
     D £FUNMS          S              7
     D £FUNFI          S             10
     D £FUNCM          S              2
      *
     C     *ENTRY        PLIST
     C                   PARM                    £FUNNP           10
     C                   PARM                    £FUNFU           10
     C                   PARM                    £FUNME           10
     C                   PARM                    £FUNMS            7
     C                   PARM                    £FUNFI           10
     C                   PARM                    £FUNCM            2
     C                   PARM                    £FUND1
     C                   PARM                    £FUND2
      * Il chiamate passa 2 DS che vengono qui ricevute come campi stringa e assegnati alle 2 DS
      * per rimapparli
      * Nell'entry non possono essere utilizzate direttamente le DS perché sono dichiarate INZ
     C*                   EVAL      £FUND1=£FUNW1
     C*                   EVAL      £FUND2=£FUNW2
      *
     C                   Eval      NBR = £FUNNR
     C                   EXSR      FIB
     C                   eval      £FUND2='FIBONACCI OF: ' +                    COSTANTE
     C                                 %CHAR(£FUNNR) +                          COSTANTE
     C                                 ' IS: ' + %CHAR(RESULT)                  COSTANTE
     C                   Eval      £FUNQT=RESULT
      *
      * Ripasso al chiamante le 2 DS tramite i campi stringa della entry
     C*                   EVAL      £FUNW1=£FUND1
     C*                   EVAL      £FUNW2=£FUND2
      *
     C                   SETON                                        LR
      *--------------------------------------------------------------*
     C     FIB           BEGSR
1    C                   SELECT
1x   C                   WHEN      NBR = 0
     C                   EVAL      RESULT = 0
1x   C                   WHEN      NBR = 1
     C                   EVAL      RESULT = 1
1x   C                   OTHER
2    C                   FOR       COUNT = 2 TO NBR
     C                   EVAL      RESULT = A + B
     C                   EVAL      A = B
     C                   EVAL      B = RESULT
2e   C                   ENDFOR
1e   C                   ENDSL
     C                   ENDSR
