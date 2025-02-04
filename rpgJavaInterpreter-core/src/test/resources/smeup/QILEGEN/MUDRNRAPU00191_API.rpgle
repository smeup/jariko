     V* ==============================================================
     V* 31/01/2025 APU001 Creation
     V* ==============================================================
    O * This code is used for purposes of MUDRNRAPU00191 program.
     V* ==============================================================
     D VAR1            S             10    INZ('BAR')
     D VAR2            S             10

     C     APISR         BEGSR
     C                   MOVEL     VAR1          VAR2
     C                   ENDSR