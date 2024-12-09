     V* ==============================================================
     V* 29/11/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program is used by 'MUDRNRAPU00173' for its purpose.
     V* ==============================================================
     D PARM1           S              3    INZ(*BLANKS)
     D PARM2           S              3    INZ(*BLANKS)

     C     PARM1         IFEQ      'FOO'
     C                   EVAL      PARM2='BAR'
     C                   ENDIF
     C                   SETON                                        RT



     C     *INZSR        BEGSR
     C     *ENTRY        PLIST
     C     PARM1         PARM      PARM2         RES               3
     C                   ENDSR
