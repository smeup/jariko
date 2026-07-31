     V* ==============================================================
     D* Test OPEN/CLOSE/%OPEN on a VIDEO file simulated via reload
     D* metadata (dspfConfig == null fallback), reproducing the crash
     D* reported for B£AR800V ("File definition ... not found").
     V* ==============================================================

     FB£AR800V  CF   E             WORKSTN USROPN

     D STR             S             10

     C                   IF        %OPEN(B£AR800V)
     C                   EVAL      STR='OPENED'
     C                   ELSE
     C                   EVAL      STR='CLOSED'
     C                   ENDIF
     C     STR           DSPLY

     C                   OPEN      B£AR800V
     C                   IF        %OPEN(B£AR800V)
     C                   EVAL      STR='OPENED'
     C     STR           DSPLY
     C                   ENDIF

     C                   CLOSE     B£AR800V
     C                   IF        NOT %OPEN(B£AR800V)
     C                   EVAL      STR='CLOSED'
     C     STR           DSPLY
     C                   ENDIF

     C                   SETON                                        LR
