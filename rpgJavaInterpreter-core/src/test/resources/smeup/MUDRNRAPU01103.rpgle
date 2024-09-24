     V* ==============================================================
     V* 19/09/2024 APU011 Creation
     V* 25/09/2024 APU011 Edit
     V* ==============================================================
    O * PROGRAM GOAL
    O * Verifies the conversion of values to a numeric data structure
    O * when using a blank string and displays the results.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was:
    O *   Issue executing MoveLStmt at line 22. For
    O *   input string: "        "
     V* ==============================================================
     D PER$DS          DS           100
     D  T$PERI                19     26

     DK§CF             DS
     D K$DE                           8  0

     C                   EVAL      K$DE=1
     C     K$DE          DSPLY
     C                   MOVEL(P)  T$PERI        K$DE                           #MoveLStmt at line 22. For input string: "        "
     C     K$DE          DSPLY

     C                   SETON                                          LR