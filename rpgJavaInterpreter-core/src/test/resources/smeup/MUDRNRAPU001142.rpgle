     V* ==============================================================
     V* 14/07/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program executes `ADD` operator between two Standalone
    O *  arrays with the size of the right greater than left.
     V* ==============================================================
     DARR1             S              5  2 DIM(2) INZ(1)
     DARR2             S              5  2 DIM(3) INZ(2)
     DSHOW_RES2        PR
     D                                5  2 DIM(2)
     DSHOW_RES3        PR
     D                                5  2 DIM(3)

     C                   CLEAR                   INDEX             1 0
     C                   FOR       INDEX=1 TO 2
     C                   EVAL      ARR1(INDEX)=ARR1(INDEX)
     C                                          * INDEX
     C                   ENDFOR
     C     'ARR1 ITEMS'  DSPLY
     C                   CALLP     SHOW_RES2(ARR1)
     C     'ARR2 ITEMS'  DSPLY
     C                   CALLP     SHOW_RES3(ARR2)
     C                   ADD       ARR1          ARR2
     C     'ARR2 ITEMS'  DSPLY
     C                   CALLP     SHOW_RES3(ARR2)
     C                   SETON                                          LR

     PSHOW_RES2        B
     DSHOW_RES2        PI
     DARRAY                           5  2 DIM(2)
     DINDEX            S              1  0
     DMSG              S              5
      *
     C                   FOR       INDEX=1 TO 2
     C                   EVAL      MSG=%CHAR(ARRAY(INDEX))
     C     MSG           DSPLY
     C                   ENDFOR
      *
     PSHOW_RES2        E

     PSHOW_RES3        B
     DSHOW_RES3        PI
     DARRAY                           5  2 DIM(3)
     DINDEX            S              1  0
     DMSG              S              5
      *
     C                   FOR       INDEX=1 TO 3
     C                   EVAL      MSG=%CHAR(ARRAY(INDEX))
     C     MSG           DSPLY
     C                   ENDFOR
      *
     PSHOW_RES3        E