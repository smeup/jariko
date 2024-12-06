     V* ==============================================================
     V* 03/12/2024 BERNIC Creation
     V* 03/12/2024 APU001 Edit by removing COPY for compatibility
     V*                   on Jariko. In addition, translated in English
     V*                   several comments.
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program is used by 'MUDRNRAPU00175' for its purpose.
     V* ==============================================================
     D* M A I N
      *---------------------------------------------------------------
      * Turning on indicator
     C                   EVAL      *IN35=*ON
      *
     C                   SETON                                        RT
      *---------------------------------------------------------------
    RD* INITIAL ROUTINE
      *--------------------------------------------------------------*
     C     *INZSR        BEGSR
      *
     C     *ENTRY        PLIST
     C                   PARM      *IN35         ££35              1
      *
     C                   ENDSR