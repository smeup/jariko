     V* ==============================================================
     V* 14/07/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program executes `ADD` operator between two Standalone
    O *  arrays with same size.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was:
    O *   `Program MUDRNRAPU001140 - Issue executing AddStmt at
    O *    line 29. ConcreteArrayValue(elements=[...],
    O *    elementType=NumberType(entireDigits=3, decimalDigits=2,
    O *    rpgType=)) should be a number
     V* ==============================================================
     DARR1             S              5  2 DIM(3) INZ(1)
     DARR2             S              5  2 DIM(3) INZ(2)
     DSHOW_RES         PR
     D                                5  2 DIM(3)

     C                   CLEAR                   INDEX             1 0
     C                   FOR       INDEX=1 TO 3
     C                   EVAL      ARR1(INDEX)=ARR1(INDEX)
     C                                          * INDEX
     C                   ENDFOR
     C     'ARR1 ITEMS'  DSPLY
     C                   CALLP     SHOW_RES(ARR1)
     C     'ARR2 ITEMS'  DSPLY
     C                   CALLP     SHOW_RES(ARR2)
     C                   ADD       ARR1          ARR2
     C     'ARR2 ITEMS'  DSPLY
     C                   CALLP     SHOW_RES(ARR2)
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
