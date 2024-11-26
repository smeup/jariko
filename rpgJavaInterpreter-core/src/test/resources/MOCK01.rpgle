     V* ==============================================================
     D* This program executes some mock statements and expressions
     V* ==============================================================
     D PTR1            S               *   INZ(*Null)
     C                   BITOFF    '01234567'    $A                1
     C                   BITON     '27'          $A
     C                   EVAL      PTR1 = %Alloc(50)
     C                   EVAL      PTR1 = %ReAlloc(PTR1:100)
     C                   DEALLOC                 PTR1
