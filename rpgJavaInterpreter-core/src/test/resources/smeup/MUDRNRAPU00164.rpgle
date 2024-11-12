     V* ==============================================================
     V* 12/11/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Assignment of UnlimitedStringValue to a StringValue where
    O *  the size of first is smaller than second.
     V* ==============================================================
     D VARUNL          S               0
     D VARSTD          S             10

     C                   EVAL      VARUNL='ABCDE'
     C                   EVAL      VARSTD=VARUNL
     C                   MOVE      'FG'          VARSTD
     C     VARSTD        DSPLY

     C                   SETON                                          LR