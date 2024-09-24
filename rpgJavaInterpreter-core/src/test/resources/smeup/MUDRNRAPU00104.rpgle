     D SQL_DAT         DS
     D  SQL_CODI                     15
     D  SQL_COD1                     15
     D  SQL_COD2                     15
     D  SQL_COD3                     15

     C                   DO        *HIVAL
      *
     C/EXEC SQL
     C+ Fetch C1 Into :SQL_DAT
     C/END-EXEC
      *
     C                   LEAVE
     C                   ENDDO

     C     'HELLO THERE' DSPLY