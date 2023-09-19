     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 12/03/20  001678  FP Creato
     V* 12/03/20  V5R1    BERNI Check-out 001678 in SMEDEV
     V* 13/03/20  001689  BMA Renamed MUTE16_04 into MUTE13_20
     V* 13/03/20  V5R1    BERNI Check-out 001689 in SMEDEV
     V* 25/08/20  002091  BUSFIO Renamed MUTE13_20 into MUTE13_22
     V* 25/08/20  002091  BUSFIO Sostituzione di SETON e SETOFF e esplicitazione IF
     V* 31/08/20  V5R1    BMA   Check-out 002091 in SMEDEV
     V* 07/09/23  005098  BERNI  Ampliato aggiungendo l'esempio del MUTE13_22B
     V* 07/09/23  V6R1    BMA Check-out 005098 in SMEDEV
     V*=====================================================================
     D FACTOR2         S              1  0
      *---------------------------------------------------------------
      * M A I N
      *---------------------------------------------------------------
     C                   EVAL      *IN34=*OFF
     C                   Z-ADD     0             FACTOR2
     C                   IF        *IN34
     C                   Z-ADD     1             FACTOR2
     C                   ENDIF
    MU* VAL1(FACTOR2) VAL2(1) COMP(EQ)
     C                   EVAL      FACTOR2+=1
      *
     C                   EVAL      *IN34=*OFF
     C                   Z-ADD     0             FACTOR2
     C                   IF        *IN34=*OFF
     C                   Z-ADD     1             FACTOR2
     C                   ENDIF
    MU* VAL1(FACTOR2) VAL2(2) COMP(EQ)
     C                   EVAL      FACTOR2+=1
      *
     C                   EVAL      *IN34=*OFF
     C                   EVAL      *IN35=*OFF
     C                   Z-ADD     0             FACTOR2
     C                   IF        *IN34 OR *IN35
     C                   Z-ADD     1             FACTOR2
     C                   ENDIF
    MU* VAL1(FACTOR2) VAL2(1) COMP(EQ)
     C                   EVAL      FACTOR2+=1
      *
     C                   EVAL      *IN34=*OFF
     C                   EVAL      *IN35=*OFF
     C                   Z-ADD     0             FACTOR2
     C                   IF        *IN34 OR NOT *IN35
     C                   Z-ADD     1             FACTOR2
     C                   ENDIF
    MU* VAL1(FACTOR2) VAL2(2) COMP(EQ)
     C                   EVAL      FACTOR2+=1
      *
     C                   EVAL      *IN34=*OFF
     C                   EVAL      *IN35=*OFF
     C                   Z-ADD     0             FACTOR2
     C                   IF        *IN34 AND NOT *IN35
     C                   Z-ADD     1             FACTOR2
     C                   ENDIF
    MU* VAL1(FACTOR2) VAL2(1) COMP(EQ)
     C                   EVAL      FACTOR2+=1
      *
     C                   EVAL      *IN34=*OFF
     C                   EVAL      *IN35=*OFF
     C                   Z-ADD     0             FACTOR2
     C                   IF        *IN34 AND *IN35
     C                   Z-ADD     1             FACTOR2
     C                   ENDIF
    MU* VAL1(FACTOR2) VAL2(1) COMP(EQ)
     C                   EVAL      FACTOR2+=1
      *
     C                   EVAL      *IN34=*OFF
     C                   EVAL      *IN35=*OFF
     C                   Z-ADD     0             FACTOR2
     C                   IF        *IN34=*OFF AND NOT *IN35
     C                   Z-ADD     1             FACTOR2
     C                   ENDIF
    MU* VAL1(FACTOR2) VAL2(2) COMP(EQ)
     C                   EVAL      FACTOR2+=1
      *
     C                   EVAL      *IN34=*ON
     C                   EVAL      *IN35=*ON
     C                   Z-ADD     0             FACTOR2
     C                   IF        *IN34=*OFF AND NOT *IN35
     C                   Z-ADD     1             FACTOR2
     C                   ENDIF
    MU* VAL1(FACTOR2) VAL2(1) COMP(EQ)
     C                   EVAL      FACTOR2+=1
      *
     C                   EVAL      *IN34=*OFF
     C                   EVAL      *IN35=*OFF
     C                   EVAL      *IN36=*OFF
     C                   EVAL      *IN37=*OFF
     C                   EVAL      *IN38=*OFF
     C                   EVAL      *IN39=*OFF
     C                   Z-ADD     0             FACTOR2
     C                   IF        *IN34 AND *IN35 AND NOT (*IN36) OR *IN37
     C                             AND *IN38 AND NOT (*IN39)
     C                   Z-ADD     1             FACTOR2
     C                   ENDIF
    MU* VAL1(FACTOR2) VAL2(1) COMP(EQ)
     C                   EVAL      FACTOR2+=1
      *
     C                   EVAL      *IN99=*ON
     C                   Z-ADD     0             FACTOR2
      *
     C                   IF        *IN99=*OFF
     C                   Z-ADD     1             FACTOR2
     C                   ENDIF
      *
    MU* VAL1(FACTOR2) VAL2(1) COMP(EQ)
     C                   EVAL      FACTOR2+=1
      *
     C                   IF        *IN99=*OFF
     C                   Z-ADD     1             FACTOR2
     C                   ENDIF
      *
    MU* VAL1(FACTOR2) VAL2(2) COMP(EQ)
     C                   EVAL      FACTOR2+=1
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
