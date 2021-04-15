      *---------------------------------------------------------------
      * A very simple procedure call.
      * tested features:
      * - Variable scope
      *---------------------------------------------------------------
     DCALL1            PR
      *---------------------------------------------------------------
      * Main
      *---------------------------------------------------------------
      * Global variable changed in main and in procedure
     DmainVar          S             99
      * This variable is global but also local in procedure
     DsameVar          S             99
      *
     C                   EVAL      mainVar='mainVar set by main'
     C      mainVar      DSPLY
     C                   EVAL      sameVar='sameVar set by main'
     C      sameVar      DSPLY
      *
     C                   CALLP     CALL1()
      * mainVar value has to reflect changes made by  CALL1
     C      mainVar      DSPLY
      * sameVar value has not to reflect changes made by CALL1
     C      sameVar      DSPLY
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
     DsameVar          S             99     INZ('sameVar just initialized')
      *
      * A prcedure variable declared by D specs
     C                   EVAL      procVar='procVar set by proc'
     C      procVar      DSPLY
      * sameVar is also local and has to be initialized
     C      sameVar      DSPLY
     C                   EVAL      sameVar='sameVar set by proc'
     C      sameVar      DSPLY
      *
      * A procedure variable declared and initialized by D specs
     C      procVarInz   DSPLY
      *
      * Sum of two procedure variables declared and initialized by D specs
     C                   EVAL      procVarRes=
     C                                %CHAR(procVarNum1+procVarNum2)
     C      procVarRes   DSPLY
      * Changing mainVar value should be reflected to mainProgram
     C                   EVAL      mainVar='mainVar changed by proc'
      *
     PCALL1            E
      *---------------------------------------------------------------
