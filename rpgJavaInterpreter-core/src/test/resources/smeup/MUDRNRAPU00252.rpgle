      * Helper declarations
     D £DBG_Str        S            512

      * Subrouting that increases the number of elements in SWKpt1
     C     REALCOD       BEGSR
      *
     C                   EVAL      SWKpt1 =%ReAlloc(SWKpt1  :
     C                              %Size(COD) * (nElAl1+nElAg1))
      *
     C*                   EVAL      %SUBARR(COD:nElAl1+1:nElAg1)=*BLANKS
     C                   EVAL      nElAl1=nElAl1+nElAg1
      *
     C                   ENDSR

      * Declarations needed for test purpose
     D DIMSCH          C                   CONST(10000)
     D COD             S             20    DIM(DIMSCH) BASED(SWKpt1)            Codice
     D SWKpt1          S               *   INZ(*Null)
     D nElAg1          S              5I 0
     D nElAl1          S              5I 0

      * DEALLOC and %ALLOC should not crash the program
     C                   EVAL      SWKpt1 = %Alloc(%Size(COD) * nElAl1)
     C                   DEALLOC                 SWKpt1

      * Setting up the number of element to add to SWKpt1 every time we call REALCOD
     C                   EVAL      nElAg1=250

      * Dynamic array resizing using a pointer on top of it with %realloc should not crash the program
     C                   EVAL      £DBG_Str=%ELEM(COD)
     C     £DBG_Str      DSPLY

      * Test on one iteration
     C                   EXSR      REALCOD
     C                   EVAL      £DBG_Str=%ELEM(COD)
     C     £DBG_Str      DSPLY

      * Test on subsequent iteration
     C                   EXSR      REALCOD
     C                   EVAL      £DBG_Str=%ELEM(COD)
     C     £DBG_Str      DSPLY