     /**************************************************************************
      * This is a mock RPG program designed to demonstrate a simple SQL FETCH
      * operation within the Jariko environment, which emulates AS400 behavior.
      * The program uses embedded SQL to fetch data and display the SQL status
      * code (SQLCOD) both before and after execution.
      *
      * Variables:
      * - SQLCOD: Mocked SQL return code variable (5-digit packed numeric), explicitly
      *           defined here for compatibility with the Jariko interpreter.
      * - TMP:    Temporary character variable to display the value of SQLCOD.
      *
      * Note:
      * - This program is specifically tailored for use with Jariko, where SQLCOD
      *   must be defined explicitly and temporarily. In an actual AS400
      *  `.sqlrpgle` environment, SQLCOD would be implicitly available and
      *   managed by the system.
      *
      **************************************************************************/
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