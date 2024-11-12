     V* ==============================================================
     V* 12/11/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Assignment of UnlimitedStringValue to a StringValue (VARYING)
    O *  where the size of first is smaller than second.
     V* ==============================================================
     D £UIBDS          DS
     D  £UIBME                       10
     D  £UIBP1                         0
     D £DECPA          S             10    VARYING

     C                   EVAL      £UIBP1='ABCDE'
     C                   EVAL      £DECPA=£UIBP1
     C                   MOVE      'FG'          £DECPA
     C     £DECPA        DSPLY

     C                   SETON                                          LR