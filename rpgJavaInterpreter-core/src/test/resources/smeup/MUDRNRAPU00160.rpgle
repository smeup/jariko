     V* ==============================================================
     V* 11/11/2024 APU001 Creation
     V* 12/11/2024 APU001 Removed SQLCOD and updated description
     V* ==============================================================
    O * PROGRAM GOAL
    O * This is a mock RPG program designed to demonstrate a simple SQL FETCH
    O * operation within the Jariko environment, which emulates AS400 behavior.
    O * The program uses embedded SQL to fetch data and display the SQL status
    O * code (SQLCOD) both before and after execution.
    O *
     V* ==============================================================
     D SQL_DAT         DS
     D  SQL_CODI                     15
     D  SQL_COD1                     15
     D  SQL_COD2                     15
     D  SQL_COD3                     15
     D TMP             S             10

     C                   EVAL      TMP=%CHAR(SQLCOD)
     C     TMP           DSPLY
     C                   EXSR      EXEC_SQL
     C                   EVAL      TMP=%CHAR(SQLCOD)
     C     TMP           DSPLY

     C                   SETON                                          LR

     C     EXEC_SQL      BEGSR
     C/EXEC SQL
     C+ FETCH C1 INTO :SQL_DAT
     C/END-EXEC
     C                   ENDSR