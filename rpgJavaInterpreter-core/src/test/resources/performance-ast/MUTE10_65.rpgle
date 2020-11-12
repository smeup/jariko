     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 06/11/20  002323  BERNI Creato
     V*=====================================================================
     D FIRAR           S          10000    DIM(500)                             First Array
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
      *---------------------------------------------------------------
     D $N              S             10  0
     D $A              S             10
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
     C     *ENTRY        PLIST
     C                   PARM                    £FUNFU           10
     C                   PARM                    £FUNME           10
     C                   PARM                    £FUNMS            7
     C                   PARM                    £FUNFI           10
     C                   PARM                    £FUNCM            2
     C                   PARM                    £FUND1
     C                   PARM                    FIRAR
      *
     C                   ADD       1             $N
     C                   EVAL      $N=$N-1
     C                   MOVEL     1             $N
     C                   EVAL      $A='A'
     C                   MOVEL     'B'           $A
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
    RD* Initial subroutine
      *--------------------------------------------------------------*
     C     *INZSR        BEGSR
      *
      *
     C                   ENDSR
