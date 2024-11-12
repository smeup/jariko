     V* ==============================================================
     V* 12/11/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Assignment of UnlimitedStringValue to a StringValue where
    O *  the size of first is greater than second.
     V* ==============================================================
     D VARUNL          S               0
     D VARSTD          S              3

     C                   EVAL      VARUNL='ABCDE'
     C                   EVAL      VARSTD=VARUNL
     C     VARSTD        DSPLY

     C                   SETON                                          LR