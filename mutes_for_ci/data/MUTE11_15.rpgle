     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 12/08/19  001059 BMA Creazione
     V* 13/08/19  001059 BMA Ricompilato
     V* ==============================================================
      *
      * Calculates number of Fibonacci and return the result
      *
      ****************************************************************
     D £FUNPG          S             10
      *----------------------------------------------------------------
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
     D DSP             S             50
      *
     C                   CLEAR                   £FUND1
     C                   CLEAR                   £FUND2
      *
     C                   EVAL      £FUNPG='MUTE11_11C'
     C                   Eval      £FUNNR=91
     C                   EXSR      £FUN02
     C                   EVAL      DSP=£FUND2
     C                   DSPLY                   DSP
      *
     C                   SETON                                        LR
     C*-------------------------------------------------------------------
     C     £FUN02        BEGSR
     C*
     C                   CALL      £FUNPG
     C     £FUNPG        PARM      *BLANKS       £FUNNP           10
     C                   PARM                    £FUNFU           10
     C                   PARM                    £FUNME           10
     C                   PARM      *BLANKS       £FUNMS            7
     C                   PARM      *BLANKS       £FUNFI           10
     C                   PARM      *BLANKS       £FUNCM            2
     C                   PARM                    £FUND1
     C                   PARM                    £FUND2
     C*
     C                   ENDSR
