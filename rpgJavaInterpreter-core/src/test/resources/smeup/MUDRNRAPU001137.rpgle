     V* ==============================================================
     V* 03/07/2025 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * This program tests the indicator evaluation between
    O *  the main program and procedure.
     V* ==============================================================
     DPR0              PR             1  0
     D                                1  0
     DPR1              PR             1  0
     DRES              S              1  0
     DPROC_VAL         S              1  0 INZ(1)

     C                   EVAL      *IN35=*ON
     C                   EVAL      RES=%CHAR(*IN35)
     C     RES           DSPLY

     C                   CALLP     PR0(PROC_VAL)

     C                   EVAL      RES=%CHAR(*IN35)
     C     RES           DSPLY

     C                   SETON                                          LR


      * Procedure implementation
     PPR0              B
     DPR0              PI             1  0
     DPR0_F1                          1  0
      *
     C                   EVAL      RES=%CHAR(*IN35)
     C     RES           DSPLY

     C                   EVAL      *IN35=*OFF

     C                   EVAL      RES=%CHAR(*IN35)
     C     RES           DSPLY

     C                   RETURN    PR0_F1
      *
     PPR0              E