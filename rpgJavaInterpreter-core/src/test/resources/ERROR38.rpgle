      * Helper declarations
     D £DBG_Str        S            512

      * Declarations needed for test purpose
     D COD1            S             20    BASED(SWKpt1)
     D COD2            S             20    DIM(500) BASED(SWKpt1)
     D COD3            S             20    DIM(500) BASED(SWKpt1)
     D COD4            S             20    DIM(500) BASED(SWKpt1)
     D SWKpt1          S               *   INZ(*Null)

      * %REALLOC is not allowed when pointer has any variable based on it that is not an array
     C                   EVAL      SWKpt1 =%ReAlloc(SWKpt1:10000)
     C                   EVAL      £DBG_Str=COD1
     C     £DBG_Str      DSPLY