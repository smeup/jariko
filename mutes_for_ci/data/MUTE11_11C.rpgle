     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 13/08/19  001059 BMA Creazione
     V* 14/08/19  001059 BMA Restituisco risultato in £FUNQT
     V* ==============================================================
      *
      * Calculates number of Fibonacci and return the result
      *
      ****************************************************************
     D £FUNW1          S                   LIKE(£FUND1)
     D £FUNW2          S                   LIKE(£FUND2)
      *----------------------------------------------------------------
     D £FUND1          DS           512    INZ
      * Contesto (V5MOVI / GMCONF / Ecc)
     D  £FUNCO                 1     10
      * Numero relativo di record
     D  £FUNNR                11     19  0
      * Griglia (Tabella B£G)
     D  £FUNGR                20     22
      * Tipo Codice 1
     D  £FUNT1                23     25
      * Par. Codice 1
     D  £FUNP1                26     35
      * Codice 1
     D  £FUNK1                36     50
      * Intestaz. Codice 1
     D  £FUNI1                51     65
      * Descriz. Codice 1
     D  £FUNS1                66    100
      * Tipo Codice 2
     D  £FUNT2               101    103
      * Par. Codice 2
     D  £FUNP2               104    113
      * Codice 2
     D  £FUNK2               114    128
      * Intestaz. Codice 2
     D  £FUNI2               129    143
      * Descriz. Codice 2
     D  £FUNS2               144    178
      * Tipo Codice 3
     D  £FUNT3               179    181
      * Par. Codice 3
     D  £FUNP3               182    191
      * Codice 3
     D  £FUNK3               192    206
      * Intestaz. Codice 3
     D  £FUNI3               207    221
      * Descriz. Codice 3
     D  £FUNS3               222    255
      * Codice 4
     D  £FUNK4               256    270
      * Intestaz. Codice 4
     D  £FUNI4               271    285
      * Codice 5
     D  £FUNK5               286    300
      * Intestaz. Codice 5
     D  £FUNI5               301    315
      * Codice 6
     D  £FUNK6               316    330
      * Intestaz. Codice 6
     D  £FUNI6               331    345
      * Parametro di passaggio funzioni
     D  £FUNP4               346    346
      * Tabella NSC
     D  £FUN0A               347    349
      * PARAMETRI Tabella C£E
     D  £FUN0B               350    352
      * LISTINI   Area listini     (SSC£L)
     D  £FUN0C               353    354
      * LISTINI   Capitolo listini (C£L+SS)
     D  £FUN0D               355    357
      * LISTINI   Sezione listini  (C£V+SS)
     D  £FUN0E               358    360
      * LEGAMI    Tipo Legami      (C£U)
     D  £FUN0F               361    365
      * DOM/CONF. Tipo Domanda     (C£B)
     D  £FUN0G               366    370
      * REGOLE    Tipo Regola      (C£A)
     D  £FUN0H               371    373
      * SVILUPPO  Tipo Sviluppo    (C£Q)
     D  £FUN0I               374    376
      * RISORSE   Tipo Risorsa     (TRG)
     D  £FUN0L               377    379
      * COSTI     Tipo Costo       (TCO)
     D  £FUN0M               380    382
      * DISTINTE  Tipo Distinta    (BRL)
     D  £FUN0N               383    385
      * T.CICLI   Tipo Ciclo       (BRT)
     D  £FUN0O               386    388
      * CONTATTI  Tipo Contatto    (BRE)
     D  £FUN0P               389    391
      * PARAMETRO per colloquio tra funzioni
     D  £FUNX1               392    392
      * Parametri specifici (LDA della B£J)
     D  £FUNPS               393    432
      * Indicatore 35
     D  £FUN35               433    433N
      * Indicatore 36
     D  £FUN36               434    434N
      * Data (AAAAMMGG)
     D  £FUNDT               435    442  0
      * Quantità  (15.5)
     D  £FUNQT               443    457  5
      * Sottosettore B£J
     D  £FUNJS               458    459
      * Elemento B£J
     D  £FUNJE               460    464
      * PARAMETRO per colloquio tra funzioni (lancio da  B£AR30)
     D  £FUNX2               465    465
      * Vincolo 1 B£J
     D  £FUNV1               466    470
      * Parametro oggetto principale
     D  £FUNPO               471    480
      * Periodo
     D  £FUNPE               481    490
      * Cond. di esecuzione
     D  £FUNV2               491    491
      * Elemento B£H
     D  £FUNJH               492    501
      * Attivazione
     D  £FUNAT               502    502
      * Righe di separazione
     D  £FUNRS               503    503  0
      * Tipo di flusso di transazione (V5) e di impostazioni specifiche
      * flussi oggetti (1-DO ecc.)
     D  £FUNFT               504    504
      * Stato esecuzione  ( B£J )
     D  £FUNES               505    505
      *
      *
      * Campo di sicurezza per prevenire CPF
     D  £FUNQ1               443    443
      *
      *----------------------------------------------------------------
     D £FUND2          DS           512    INZ
      *----------------------------------------------------------------
      * Input Number
      *
     D NBR             S              9  0
     D RESULT          S              9  0 INZ(0)
     D COUNT           S              9  0
     D A               S              9  0 INZ(0)
     D B               S              9  0 INZ(1)
      *
     C     *ENTRY        PLIST
     C                   PARM                    £FUNNP           10
     C                   PARM                    £FUNFU           10
     C                   PARM                    £FUNME           10
     C                   PARM                    £FUNMS            7
     C                   PARM                    £FUNFI           10
     C                   PARM                    £FUNCM            2
     C                   PARM                    £FUNW1
     C                   PARM                    £FUNW2
      *
     C                   EVAL      £FUND1=£FUNW1
     C                   EVAL      £FUND2=£FUNW2
      *
     C                   Eval      NBR = £FUNNR
     C                   EXSR      FIB
     C                   eval      £FUND2='FIBONACCI OF: ' +                    COSTANTE
     C                                 %CHAR(£FUNNR) +                          COSTANTE
     C                                 ' IS: ' + %CHAR(RESULT)                  COSTANTE
     C                   Eval      £FUNQT=RESULT
      *
     C                   EVAL      £FUNW1=£FUND1
     C                   EVAL      £FUNW2=£FUND2
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
