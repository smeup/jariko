     V* ==============================================================
     V* 11/11/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * This is a mock RPG program designed to demonstrate a simple SQL FETCH
    O * operation within the Jariko environment, which emulates AS400 behavior.
    O * The program uses embedded SQL to fetch data and display the SQL status
    O * code (SQLCOD) both before and after execution.
    O *
    O * Variables:
    O * - SQLCOD: Mocked SQL return code variable (5-digit packed numeric), explicitly
    O *           defined here for compatibility with the Jariko interpreter.
    O * - TMP:    Temporary character variable to display the value of SQLCOD.
    O *
    O * Note:
    O * - This program is specifically tailored for use with Jariko, where SQLCOD
    O *   must be defined explicitly and temporarily. In an actual AS400
    O *  `.sqlrpgle` environment, SQLCOD would be implicitly available and
    O *   managed by the system.
    O *
     V* ==============================================================
     D SQL_DAT         DS
     D  SQL_CODI                     15
     D  SQL_COD1                     15
     D  SQL_COD2                     15
     D  SQL_COD3                     15
     D SQLCOD          S              5P 0 INZ(0)
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