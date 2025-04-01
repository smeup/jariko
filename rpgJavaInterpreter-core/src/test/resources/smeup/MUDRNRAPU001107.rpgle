     V* ==============================================================
     V* 01/04/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Pass an array declared as DS field to a program which
    O *  declares same program entry as Standalone.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was:
    O *     `An operation is not implemented:
    O *      'ProjectedArrayValue.asString' is not yet implemented.`
     V* ==============================================================
     D DS1             DS
     D  DS1_F1                        1  0 DIM(3) INZ
     D INX             S              1  0
     D PGM             S             17    INZ('MUDRNRAPU001107_P')

     C                   FOR       INX=1 TO 3
     C                   EVAL      DS1_F1(INX)=INX
     C                   ENDFOR

     C                   CALL      PGM                                          # An operation is not implemented: 'ProjectedArrayValue.asString' is not yet implemented.
     C                   PARM                    DS1_F1

     C                   SETON                                          LR
