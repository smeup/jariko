     V* ==============================================================
     V* 03/12/2024 BERNIC Creation
     V* 03/12/2024 APU001 Edit by removing COPY for compatibility
     V*                   on Jariko. In addition, translated in English
     V*                   several comments.
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program is used by 'MUDRNRAPU00176' for its purpose.
     V* ==============================================================
     D* M A I N
      *---------------------------------------------------------------
      * Turning on indicator
     C                   EVAL      *IN35=*ON
      *
      * Assigning value of indicator to result of PLIST.
     C                   EVAL      ££35=*IN35
      *
     C                   SETON                                        RT
      *---------------------------------------------------------------
    RD* INITIAL ROUTINE
      *--------------------------------------------------------------*
     C     *INZSR        BEGSR
      *
     C     *ENTRY        PLIST
     C                   PARM                    ££35              1
      *
     C                   ENDSR