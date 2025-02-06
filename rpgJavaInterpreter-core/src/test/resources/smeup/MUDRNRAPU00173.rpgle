     V* ==============================================================
     V* 29/11/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program tests the behaviour of `CALL` and `PLIST` when is
    O *  used the Params for both between caller and called.
    O * In accord to documentation:
    O * - when `CALL` is processed, the content of Factor 2 is placed
    O *   in the Result field;
    O * - when control transfers to called program, the contents
    O *   of the Result field is placed in the Factor 1 field.
     V* ==============================================================
     D PGM_NAME        S             17    INZ('MUDRNRAPU00173_P')
     D PARM1           S              3    INZ(*BLANKS)

     C                   CALL      PGM_NAME
     C     PARM1         PARM      'FOO'         RES               3
     C     PARM1         IFEQ      'BAR'
     C     PARM1         DSPLY
     C                   ENDIF
     C                   SETON                                          LR
