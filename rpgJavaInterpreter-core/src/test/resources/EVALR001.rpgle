     V* ==============================================================
     V* 13/03/2025 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * EVALR statement execution
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, jariko threw a syntax error
     V* ==============================================================
     D $SRC            S              3
     D $DST            S              5

     C                   EVAL      $SRC = 'ABC'
     C                   EVALR     $DST = $SRC
     C     $DST          DSPLY
     C                   EVALR     $DST = 'ABCDE'
     C     $DST          DSPLY
     C                   EVALR     $DST = 'ABCDEF'
     C     $DST          DSPLY
