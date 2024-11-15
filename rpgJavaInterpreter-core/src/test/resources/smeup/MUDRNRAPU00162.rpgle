     V* ==============================================================
     V* 12/11/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * SUBST with a side effect where the factor 2 has changed type
    O *  in `UnlimitedStringValue`.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix:
    O *  `Issue executing SubstStmt at line 22. begin 2, end 10,
    O *   length 5`
     V* ==============================================================
     D £UIBDS          DS
     D  £UIBME                       10
     D  £UIBP1                         0
     D £DECPA          S             10
     D £G40PA          S             10

     C                   EVAL      £UIBP1='ABCDE'
     C                   EVAL      £DECPA=£UIBP1
     C     £DECPA        DSPLY
     C     8             SUBST     £DECPA:3      £G40PA                         #Issue executing SubstStmt at line 22. begin 2, end 10, length 5
     C     £G40PA        DSPLY

     C                   SETON                                          LR