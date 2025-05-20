     V* ==============================================================
     V* 10/04/2025 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Throw proper error on ProjectedArrayValue.getElement with
    O * invalid index
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, jariko crashed
     V* ==============================================================
     D$X               S              2  0
     D $STRUCT         DS
     D  $ARR                          2P 0 DIM(10) INZ
     C                   EVAL      $X = 0
     C                   EVAL      $X = $ARR($X)