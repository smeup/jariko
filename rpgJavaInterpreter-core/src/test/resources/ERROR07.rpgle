     V* ==============================================================
     D* Operaton not implemented inside subroutine
     D* This program contains three errors in D specifications
     D* Jariko must fire an ErrorEvent for each error
     V* ==============================================================
     C     IMP0          BEGSR
     C/EXEC SQL
     C+ DECLARE C1 CURSOR FOR S1
     C/END-EXEC
     C/EXEC SQL
     C+ DECLARE C2 CURSOR FOR S2
     C/END-EXEC
     C                   ENDSR
      *--------------------------------------------------------------*
