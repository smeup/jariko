     V* ==============================================================
     V* 05/11/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * MOVEA between a DS field declared as array and a standalone
    O *  array. Both as integer.
    O * Size of DS field as array is greater than standalone.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix:
    O *  `ProjectedArrayValue cannot be cast to
    O *   class ConcreteArrayValue`
     V* ==============================================================
     D DS1             DS           100
     D  DS1_ARR1                      2  0 DIM(5) INZ(1)
     D ARR1            S              2  0 DIM(3) INZ(2)
     D TMP             S              2
     D COUNT           S              2  0 INZ(1)

     C                   FOR       COUNT=1 TO %ELEM(ARR1)
     C                   EVAL      TMP=%CHAR(ARR1(COUNT))
     C     TMP           DSPLY
     C                   ENDFOR

     C                   MOVEA(P)  DS1_ARR1      ARR1                           #ProjectedArrayValue cannot be cast to class ConcreteArrayValue

     C                   FOR       COUNT=1 TO %ELEM(ARR1)
     C                   EVAL      TMP=%CHAR(ARR1(COUNT))
     C     TMP           DSPLY
     C                   ENDFOR

     C                   SETON                                          LR