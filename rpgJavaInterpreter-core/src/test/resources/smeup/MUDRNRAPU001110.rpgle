     V* ==============================================================
     V* 28/03/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Assign a number greater than destination by using Z-ADD.
    O * In this test the source is decimal with 5 digits,
    O *  two of these as decimals; destination is decimal
    O *  with 3 digits, one of these as decimal.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was:
    O *     `434.6 cannot be assigned to IVAR of type
    O *      NumberType(entireDigits=2, decimalDigits=1, rpgType=)`
     V* ==============================================================
     D DS1             DS            10    INZ
     D  DS1_F1                 1      5  2
     D RES             S             10

     C                   EVAL      DS1_F1=434.67
     C                   EVAL      RES=%CHAR(DS1_F1)
     C     RES           DSPLY

     C                   EVAL      RES=%CHAR(IVAR)
     C     RES           DSPLY

     C                   Z-ADD     DS1_F1        IVAR              3 1          # 434.6 cannot be assigned to IVAR
     C                   EVAL      RES=%CHAR(IVAR)
     C     RES           DSPLY

     C                   SETON                                        RT