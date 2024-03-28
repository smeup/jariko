     D £DBG_Str        S             50          VARYING
     FMLNGT60AV CF   E             WORKSTN USROPN
     FMLNGT60BV CF   E             WORKSTN SFILE(SFL1:RRN01)

     C                   Z-ADD     5             RRN01             4 0
     C                   EVAL      £DBG_Str=%CHAR(RRN01)
     C     £DBG_Str      DSPLY