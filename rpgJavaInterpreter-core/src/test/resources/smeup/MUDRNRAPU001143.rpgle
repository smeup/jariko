     V* ==============================================================
     V* 14/07/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program executes `ADD` operator between a single value
    O *  to an array.
     V* ==============================================================
     DVAL              S              5  2 INZ(5)
     DARR              S              5  2 DIM(3) INZ(2)
     DSHOW_RES         PR
     D                                5  2 DIM(3)

     C     'ARR ITEMS'   DSPLY
     C                   CALLP     SHOW_RES(ARR)
     C                   ADD       VAL           ARR
     C     'ARR ITEMS'   DSPLY
     C                   CALLP     SHOW_RES(ARR)
     C                   SETON                                          LR

     PSHOW_RES         B
     DSHOW_RES         PI
     DARRAY                           5  2 DIM(3)
     DINDEX            S              1  0
     DMSG              S              5
      *
     C                   FOR       INDEX=1 TO 3
     C                   EVAL      MSG=%CHAR(ARRAY(INDEX))
     C     MSG           DSPLY
     C                   ENDFOR
      *
     PSHOW_RES         E