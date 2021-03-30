      *---------------------------------------------------------------
      * A very simple procedure call
      *---------------------------------------------------------------
     DCALL1            PR
      *---------------------------------------------------------------
      * Main
      *---------------------------------------------------------------
     DmainVar          S             16
      *
     C                   EVAL      mainVar='Main-scoped var!'
     C      mainVar      DSPLY
      *
     C                   CALLP     CALL1()
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
     PCALL1            B
     DCALL1            PI
      *
     DprocVar          S             32
     DprocVarInz       S             20     INZ('D specs inline init!')
     DprocVarNum1      S              5  2  INZ(123.45)
     DprocVarNum2      S              3  1  INZ(345.5)
     DprocVarRes       S              5  2  INZ
      *
      * A prcedure variable declared by D specs
     C                   EVAL      procVar='Proc-scoped var!'
     C      procVar      DSPLY
      *
      * A procedure variable declared and initialized by D specs
     C      procVarInz   DSPLY
      *
      * Sum of two procedure variables declared and initialized by D specs
     C                   EVAL      procVarRes=
     C                                %CHAR(procVarNum1+procVarNum2)
     C      procVarRes   DSPLY
      *
     C      'Done!     ' DSPLY
     PCALL1            E
      *---------------------------------------------------------------
