     FEXFMT1V   CF   E             WORKSTN USROPN
     D MSG             S             50          VARYING
     C                   EVAL       STR='UPPERCASE'
     C                   EVAL       INT=123
     C                   EVAL       DEC=123.45
     C                   EXFMT      FMT1
     C                   EVAL       MSG='FLD01:'+FLD01
     C     MSG           DSPLY
     C                   EVAL       MSG='STR:'+STR
     C     MSG           DSPLY
     C                   EVAL       MSG='INT:'+%CHAR(INT)
     C     MSG           DSPLY
     C                   EVAL       MSG='DEC:'+%CHAR(DEC)
     C     MSG           DSPLY