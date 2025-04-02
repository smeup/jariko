     V* ==============================================================
     V* 26/03/2025 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Perform COMP between an IntValue and an AllValue
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, jariko crashed
     V* ==============================================================
     D £100DSI         DS          2000
     D  £100I_DT                      8P 0

     C                   EVAL      £100I_DT = *LOVAL
     C     £100I_DT      COMP      *ALL'9'                                09
     C     £100I_DT      COMP      *ALL'90'                               10
     C     *IN09         DSPLY
     C     *IN10         DSPLY
     C                   EVAL      £100I_DT = *HIVAL
     C     £100I_DT      COMP      *ALL'8'                              09
     C     £100I_DT      COMP      *ALL'90'                             10
     C     *IN09         DSPLY
     C     *IN10         DSPLY
     C     £100I_DT      COMP      *ALL'9'                                09
     C     £100I_DT      COMP      *ALL'90'                               10
     C     *IN09         DSPLY
     C     *IN10         DSPLY

     C                   SETON                                          LR
