     V* ==============================================================
     V* 28/03/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Assign a number greater than destination by using Z-ADD.
    O * In this test the source is integer with 5 digits;
    O *  destination is decimal with 5 digits, two of these
    O *  as decimals.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was:
    O *     `Value DecimalValue(value=43467.00) cannot be assigned
    O *       to data: InStatementDataDefinition name=IVAR,
    O *       type=NumberType(entireDigits=3, decimalDigits=2, rpgType=)`
     V* ==============================================================
     D DS1             DS            10    INZ
     D  DS1_F1                 1      5  0
     D RES             S             10

     C                   EVAL      DS1_F1=43467
     C                   EVAL      RES=%CHAR(DS1_F1)
     C     DS1_F1        DSPLY

     C                   EVAL      RES=%CHAR(IVAR)
     C     RES           DSPLY

     C                   Z-ADD     123           IVAR              5 2          # Value DecimalValue(value=43467.00) cannot be assigned to data
     C                   EVAL      RES=%CHAR(IVAR)
     C     RES           DSPLY

     C                   Z-ADD     DS1_F1        IVAR
     C                   EVAL      RES=%CHAR(IVAR)
     C     RES           DSPLY

     C                   SETON                                        RT