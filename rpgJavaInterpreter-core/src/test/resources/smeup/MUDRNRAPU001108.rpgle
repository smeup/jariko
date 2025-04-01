     V* ==============================================================
     V* 01/04/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Pass an array declared as DS field to a program which
    O *  declares same program entry as Standalone.
    O * Is similar to `MUDRNRAPU001107` but the DS field is declared
    O *  as array of decimals instead integers.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was:
    O *     `An operation is not implemented:
    O *      'ProjectedArrayValue.concatenate' is not yet implemented.`
     V* ==============================================================
     D DS1             DS
     D  DS1_F1                        2  1 DIM(3) INZ
     D INX             S              1  0
     D RES             S              3
     D PGM             S             17    INZ('MUDRNRAPU001108_P')

     C                   FOR       INX=1 TO 3
     C                   EVAL      DS1_F1(INX)=INX + 0.5
     C                   EVAL      RES=%CHAR(DS1_F1(INX))
     C     RES           DSPLY
     C                   ENDFOR

     C                   CALL      PGM                                          # An operation is not implemented: 'ProjectedArrayValue.concatenate' is not yet implemented.
     C                   PARM                    DS1_F1

     C                   SETON                                          LR
