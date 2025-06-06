     V* ==============================================================
     V* 05/06/2025 APU001 Creation
     V* 06/06/2025 APU001 Edit
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program is used by 'MUDRNRAPU001122' for its purpose.
     V* ==============================================================
     D DSPAMI          DS
     D  T$PAMI                 1      2  1
     D MSG             S             50

     C                   EVAL      MSG=%CHAR(T$PAMI)
     C     MSG           DSPLY

     C                   EVAL      T$PAMI=7.1

     C                   EVAL      MSG=%CHAR(T$PAMI)
     C     MSG           DSPLY

     C                   SETON                                          LR

     C     *ENTRY        PLIST
     C                   PARM                    DSPAMI