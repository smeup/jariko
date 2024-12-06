     V* ==============================================================
     V* 03/12/2024 BERNIC Creation
     V* 03/12/2024 APU001 Edit by removing COPY for compatibility
     V*                   on Jariko. In addition, translated in English
     V*                   several comments.
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program is used by 'MUDRNRAPU00175' for its purpose.
     V* ==============================================================
     D PGM_NAME        S             17    INZ('MUDRNRAPU00175_P2')
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      * Calling PGM MUDRNRAPU00175_P2
     C                   EXSR      CALL_PGM
      *
     C                   SETON                                        RT
      *---------------------------------------------------------------
    RD* CALLED SUBROUTINE
      *--------------------------------------------------------------*
     C     CALL_PGM      BEGSR
      *
     C                   CALL      PGM_NAME
     C                   PARM                    ££35
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* INITIAL ROUTINE
      *--------------------------------------------------------------*
     C     *INZSR        BEGSR
      *
     C     *ENTRY        PLIST
     C                   PARM                    ££35              1
      *
     C                   ENDSR