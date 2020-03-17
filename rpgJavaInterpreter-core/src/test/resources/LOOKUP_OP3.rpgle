     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 12/03/20  001676  FP Creato
     V* 12/03/20  V5R1    BMA Check-out 001676 in SMEDEV
     V* 13/03/20  001689  BMA Renamed MUTE16_03 into MUTE13_19
     V*=====================================================================
     D  ARRAY          S              1    DIM(10) ASCEND
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
     C                   EVAL      ARRAY(1)='B'
     C                   EVAL      ARRAY(2)='C'
     C                   EVAL      ARRAY(3)='D'
     C                   EVAL      ARRAY(4)='G'
     C                   EVAL      ARRAY(5)='H'
     C                   EVAL      ARRAY(6)='I'
     C                   EVAL      ARRAY(7)='A'
     C                   EVAL      ARRAY(8)='B'
     C                   EVAL      ARRAY(9)='D'
     C                   EVAL      ARRAY(10)=' '
      * Search from index 6
     C     'I'           LOOKUP    ARRAY(8)                             6869
     C                   EXSR      SHOWRESULTS
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
     C     SHOWRESULTS   BEGSR
     C   68'68 ON-'      DSPLY
     C  N68'68 OFF'      DSPLY
     C   69'69 ON-'      DSPLY
     C  N69'69 OFF'      DSPLY
     C                   ENDSR
