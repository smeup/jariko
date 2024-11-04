     V* ==============================================================
     D* 25/07/24
     D* Purpose: Must fire the following errors
     D* line 11 - Factor 2 not between 0 and 1
     V* ==============================================================

     D FOO             S              5  2

     C                   SETON                                            36
     C                   EVAL      FOO = 2.0
     C                   MOVE      FOO           *IN36