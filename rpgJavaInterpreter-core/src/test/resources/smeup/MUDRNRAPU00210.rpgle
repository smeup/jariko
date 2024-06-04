     D  ARRAY          S              5    DIM(10) PERRCD(1) CTDATA ASCEND             _NOTXT
     D  INDEX          S              2  0
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
     C                   EXSR      ROUTINE
     C                   SETON                                        LR
      *---------------------------------------------------------------
     D* Routine
      *---------------------------------------------------------------
     C     ROUTINE       BEGSR
      * GT - last item is greater
     C                   EVAL      INDEX=%LOOKUPGT('AAAAA':ARRAY:1:3)
     C     INDEX         DSPLY
      * GT - last item is equal
     C                   EVAL      INDEX=%LOOKUPGT('CCCCC':ARRAY:1:3)
     C     INDEX         DSPLY
      * GT - no item found
     C                   EVAL      INDEX=%LOOKUPGT('DDDDD':ARRAY:1:3)
     C     INDEX         DSPLY
      * GE - last item is greater
     C                   EVAL      INDEX=%LOOKUPGE('BBBBB':ARRAY:1:3)
     C     INDEX         DSPLY
      * GE - last item is equal
     C                   EVAL      INDEX=%LOOKUPGE('CCCCC':ARRAY:1:3)
     C     INDEX         DSPLY
      * GE - no item found
     C                   EVAL      INDEX=%LOOKUPGE('DDDDD':ARRAY:1:3)
     C     INDEX         DSPLY
      * LT - no item found
     C                   EVAL      INDEX=%LOOKUPLT('BBBBB':ARRAY:2:2)
     C     INDEX         DSPLY
      * LT - one item found
     C                   EVAL      INDEX=%LOOKUPLT('DDDDD':ARRAY:2:2)
     C     INDEX         DSPLY
      * LT - one item equal
     C                   EVAL      INDEX=%LOOKUPLT('CCCCC':ARRAY:2:2)
     C     INDEX         DSPLY
      * LE - no item found
     C                   EVAL      INDEX=%LOOKUPLE('BBBBB':ARRAY:2:2)
     C     INDEX         DSPLY
      * LE - one item found
     C                   EVAL      INDEX=%LOOKUPLE('DDDDD':ARRAY:2:2)
     C     INDEX         DSPLY
      * LE - one item equal
     C                   EVAL      INDEX=%LOOKUPLE('CCCCC':ARRAY:2:2)
     C     INDEX         DSPLY
      *
     C                   ENDSR
      *---------------------------------------------------------------
** TXT1
AAAAA
BBBBB
CCCCC
DDDDD
EEEEE
FFFFF
GGGGG
HHHHH
IIIII