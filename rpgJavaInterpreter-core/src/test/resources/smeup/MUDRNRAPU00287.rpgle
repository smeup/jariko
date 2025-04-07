     V* ==============================================================
     V* 26/03/2025 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Perform COMP between an IntValue and an AllValue
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, jariko crashed
     V* ==============================================================
     DMSG              S              3
     D £100DSI         DS          2000
     D  £100I_DT                      8P 0

     C                   EVAL      £100I_DT = *LOVAL
     C* Compare to higher single char
     C     £100I_DT      COMP      *ALL'9'                            091011
     C                   EVAL      MSG=%CHAR(*IN09)+%CHAR(*IN10)+%CHAR(*IN11)
     C     MSG           DSPLY
     C* Compare to higher multiple char
     C     £100I_DT      COMP      *ALL'90'                           091011
     C                   EVAL      MSG=%CHAR(*IN09)+%CHAR(*IN10)+%CHAR(*IN11)
     C     MSG           DSPLY
     C                   EVAL      £100I_DT = *HIVAL
     C* Compare to lower single char
     C     £100I_DT      COMP      *ALL'8'                            091011
     C                   EVAL      MSG=%CHAR(*IN09)+%CHAR(*IN10)+%CHAR(*IN11)
     C     MSG           DSPLY
     C* Compare to lower multiple char
     C     £100I_DT      COMP      *ALL'90'                           091011
     C                   EVAL      MSG=%CHAR(*IN09)+%CHAR(*IN10)+%CHAR(*IN11)
     C     MSG           DSPLY
     C                   SETON                                          LR
