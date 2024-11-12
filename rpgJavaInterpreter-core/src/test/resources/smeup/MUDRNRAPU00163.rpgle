     V* ==============================================================
     V* 12/11/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Assignment of UnlimitedStringValue to a StringValue where
    O *  the size of first is greater than second.
     V* ==============================================================
     D £UIBDS          DS
     D  £UIBME                       10
     D  £UIBP1                         0
     D £DECPA          S              3

     C                   EVAL      £UIBP1='ABCDE'
     C                   EVAL      £DECPA=£UIBP1
     C     £DECPA        DSPLY

     C                   SETON                                          LR