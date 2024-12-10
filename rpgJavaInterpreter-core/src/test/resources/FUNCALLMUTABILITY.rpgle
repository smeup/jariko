     V* ==============================================================
     V* 09/12/2024 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Test various statements accessing array access expression
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, jariko throw a mutability error
     V* ==============================================================
     D* Helper declarations
     D £DBG_Str        S             2
     D ARR             S             15    DIM(40)
     D I               S              5  0
     D$A               S              2  0
     D $LNGSTR         S             15
     C* Values initialization
     C                   EVAL      I=2
     C                   EVAL      ARR(I)=2
     C                   EVAL      $LNGSTR='long string'
     C                   EVAL      $A=2
     C* CHECK statement
     C     ARR(I)        CHECK     $LNGSTR:$A    $A
     C     $A            CHECK     $LNGSTR:$A    ARR(I)
     C* CHECKR statement
     C     ARR(I)        CHECKR    $LNGSTR:$A    $A
     C     $A            CHECKR    $LNGSTR:$A    ARR(I)
     C* Output
     C                   EVAL      £DBG_Str='ok'
     C     £DBG_Str      DSPLY