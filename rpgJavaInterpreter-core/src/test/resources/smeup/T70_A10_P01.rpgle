     D £DBG_Str        S            100
      *
      * Test DEFINE and IF NOT DEFINED
      *
      /IF NOT DEFINED(DEFINE_1)
      /DEFINE DEFINE_1
     C                   EVAL      £DBG_Str='PRINT'
     C     £DBG_Str      DSPLY
      /ENDIF
      *
      * Test IF NOT DEFINED WITH ELSE
      *
      /IF NOT DEFINED(DEFINE_1)
     C                   EVAL      £DBG_Str='NO_PRINT'
     C     £DBG_Str      DSPLY
      /ELSE
     C                   EVAL      £DBG_Str='PRINT'
     C     £DBG_Str      DSPLY
      /ENDIF
      *
      * Test IF DEFINED
      *
      /IF DEFINED(DEFINE_2)
     C                   EVAL      £DBG_Str='NO_PRINT'
     C     £DBG_Str      DSPLY
      /ELSE
      /UNDEFINE DEFINE_1
     C                   EVAL      £DBG_Str='PRINT'
     C     £DBG_Str      DSPLY
      /ENDIF
      *
      /IF NOT DEFINED(DEFINE_1)
     C                   EVAL      £DBG_Str='PRINT'
     C     £DBG_Str      DSPLY
      /ELSE
     C                   EVAL      £DBG_Str='NOT_PRINT'
     C     £DBG_Str      DSPLY
      /ENDIF
     C                   EVAL      £DBG_Str='PRINT'
     C     £DBG_Str      DSPLY
      *
     C                   SETON                                        LR

