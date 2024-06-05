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
      * GT - some items are greater
     C                   EVAL      INDEX=%LOOKUPGT('CCCCC':ARRAY:1:6)
     C     INDEX         DSPLY
      * GT - no item is greater
     C                   EVAL      INDEX=%LOOKUPGT('IIIII':ARRAY:1:6)
     C     INDEX         DSPLY
      * GT - last item is equal
     C                   EVAL      INDEX=%LOOKUPGT('FFFFF':ARRAY:1:6)
     C     INDEX         DSPLY
      * GE - some items are greater
     C                   EVAL      INDEX=%LOOKUPGE('CCCCC':ARRAY:1:6)
     C     INDEX         DSPLY
      * GE - no item is greater
     C                   EVAL      INDEX=%LOOKUPGE('IIIII':ARRAY:1:6)
     C     INDEX         DSPLY
      * GE - last item is equal
     C                   EVAL      INDEX=%LOOKUPGE('FFFFF':ARRAY:1:6)
     C     INDEX         DSPLY
      * LT - first item is equal
     C                   EVAL      INDEX=%LOOKUPLT('BBBBB':ARRAY:2:4)
     C     INDEX         DSPLY
      * LT - some items are less
     C                   EVAL      INDEX=%LOOKUPLT('DDDDD':ARRAY:2:4)
     C     INDEX         DSPLY
      * LT - no item is less
     C                   EVAL      INDEX=%LOOKUPLT('AAAAA':ARRAY:2:4)
     C     INDEX         DSPLY
      * LE - first item is equal
     C                   EVAL      INDEX=%LOOKUPLE('BBBBB':ARRAY:2:4)
     C     INDEX         DSPLY
      * LE - some items are less
     C                   EVAL      INDEX=%LOOKUPLE('DDDDD':ARRAY:2:4)
     C     INDEX         DSPLY
      * LE - no item is less
     C                   EVAL      INDEX=%LOOKUPLE('AAAAA':ARRAY:2:4)
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