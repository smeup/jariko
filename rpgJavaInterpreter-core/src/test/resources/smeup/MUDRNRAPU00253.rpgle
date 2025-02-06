      * Helper declarations
     D £DBG_Str        S            512

      * Declarations needed for test purpose
     D DIMSCH          C                   CONST(10000)
     D COD1            S             20    DIM(DIMSCH) BASED(SWKpt1)
     D COD2            S             20    DIM(DIMSCH) BASED(SWKpt1)
     D COD3            S             20    DIM(DIMSCH) BASED(SWKpt1)
     D COD4            S             20    DIM(DIMSCH) BASED(SWKpt1)
     D SWKpt1          S               *   INZ(*Null)
     D nElAg1          S              5I 0
     D nElAl1          S              5I 0

      * Setting up the number of element to add to SWKpt1 every time we call REALCOD
     C                   EVAL      nElAg1=250

      * Dynamic array resizing using %REALLOC must be allowed when there are multiple arrays based on the same pointer
     C                   EXSR      REALCOD
     C                   EVAL      £DBG_Str=%ELEM(COD1)
     C     £DBG_Str      DSPLY
     C                   EVAL      £DBG_Str=%ELEM(COD2)
     C     £DBG_Str      DSPLY
     C                   EVAL      £DBG_Str=%ELEM(COD3)
     C     £DBG_Str      DSPLY
     C                   EVAL      £DBG_Str=%ELEM(COD4)
     C     £DBG_Str      DSPLY

      * Subrouting that increases the number of elements in SWKpt1
     C     REALCOD       BEGSR
      *
     C                   EVAL      SWKpt1 =%ReAlloc(SWKpt1  :
     C                              %Size(COD1) * (nElAl1+nElAg1))
      *
     C                   EVAL      nElAl1=nElAl1+nElAg1
      *
     C                   ENDSR