     D £DBG_Str        S            100
      *
      * TestImporting same COPY two times
      *
       /COPY QILEGEN,£PDS
       /COPY QILEGEN,£PDS
      *
      * Test importing a /COPY with /EOF directive inside
      *
         /COPY QILEGEN,T70_A10_P0
      *
      * Test DEFINE and IF NOT DEFINED
      *
      /IF NOT DEFINED(DEFINE_1)
      /DEFINE DEFINE_1
     C                   EVAL      £DBG_Str='OK'
     C     £DBG_Str      DSPLY
      /ENDIF
      *
      * Test IF NOT DEFINED WITH ELSE
      *
      /IF NOT DEFINED(DEFINE_1)
     C                   EVAL      £DBG_Str='ERROR'
     C     £DBG_Str      DSPLY
      /ELSE
     C                   EVAL      £DBG_Str='OK'
     C     £DBG_Str      DSPLY
      /ENDIF
      *
      * Test IF DEFINED
      *
      /IF DEFINED(DEFINE_2)
     C                   EVAL      £DBG_Str='ERROR'
     C     £DBG_Str      DSPLY
      /ELSE
      /UNDEFINE DEFINE_1
     C                   EVAL      £DBG_Str='OK'
     C     £DBG_Str      DSPLY
      /ENDIF
      *
      /IF NOT DEFINED(DEFINE_1)
     C                   EVAL      £DBG_Str='OK'
     C     £DBG_Str      DSPLY
      /ELSE
     C                   EVAL      £DBG_Str='NOT_PRINT'
     C     £DBG_Str      DSPLY
      /ENDIF
      *
      * Test nested
      *
      /UNDEFINE DEFINE_1
      /UNDEFINE DEFINE_2
      /IF NOT DEFINED(DEFINE_1)
     C                   EVAL      £DBG_Str='OK'
     C     £DBG_Str      DSPLY
      /IF NOT DEFINED(DEFINE_2)
      /DEFINE DEFINE_2
     C                   EVAL      £DBG_Str='OK'
     C     £DBG_Str      DSPLY
      /ELSE
     C                   EVAL      £DBG_Str='NOT_PRINT'
     C     £DBG_Str      DSPLY
      /ENDIF
     C                   EVAL      £DBG_Str='OK'
     C     £DBG_Str      DSPLY
      /ELSE
     C                   EVAL      £DBG_Str='NOT_PRINT'
     C     £DBG_Str      DSPLY
      /ENDIF
     C                   EVAL      £DBG_Str='OK'
     C     £DBG_Str      DSPLY
      /IF NOT DEFINED(DEFINE_2)
     C                   EVAL      £DBG_Str='NOT_PRINT'
     C     £DBG_Str      DSPLY
      /ELSE
     C                   EVAL      £DBG_Str='OK'
     C     £DBG_Str      DSPLY
      /IF NOT DEFINED(DEFINE_1)
     C                   EVAL      £DBG_Str='OK'
     C     £DBG_Str      DSPLY
      /ELSE
     C                   EVAL      £DBG_Str='NOT_PRINT'
     C     £DBG_Str      DSPLY
      /ENDIF
      /ENDIF
     C                   EVAL      £DBG_Str='OK'
     C     £DBG_Str      DSPLY
      *
     C                   SETON                                        LR
