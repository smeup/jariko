     V* ==============================================================
     V* 15/07/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program executes `ADD` operator between two Standalone
    O *  arrays with same size but different type. The first is
    O *  decimal and the second is integer.
     V* ==============================================================
     DARR1             S              5  2 DIM(2) INZ(1.2)
     DARR2             S              5  0 DIM(2) INZ(2)
     DSHOW_RES         PR
     D                                5  0 DIM(2)

     C     'ARR2 ITEMS'  DSPLY
     C                   CALLP     SHOW_RES(ARR2)
     C                   ADD       ARR1          ARR2
     C     'ARR2 ITEMS'  DSPLY
     C                   CALLP     SHOW_RES(ARR2)
     C                   SETON                                          LR

     PSHOW_RES         B
     DSHOW_RES         PI
     DARRAY                           5  0 DIM(2)
     DINDEX            S              1  0
     DMSG              S              5
      *
     C                   FOR       INDEX=1 TO 2
     C                   EVAL      MSG=%CHAR(ARRAY(INDEX))
     C     MSG           DSPLY
     C                   ENDFOR
      *
     PSHOW_RES         E
