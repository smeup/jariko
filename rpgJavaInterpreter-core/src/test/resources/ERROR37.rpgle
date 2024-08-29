      * Helper declarations
     D £DBG_Str        S            512

      * Declarations needed for test purpose
     D COD1            S             20    BASED(SWKpt1)
     D SWKpt1          S               *   INZ(*Null)

      * %REALLOC is not allowed when pointer has variables based on it that are not arrays
     C                   EVAL      SWKpt1 =%ReAlloc(SWKpt1:10000)
     C                   EVAL      £DBG_Str=COD1
     C     £DBG_Str      DSPLY