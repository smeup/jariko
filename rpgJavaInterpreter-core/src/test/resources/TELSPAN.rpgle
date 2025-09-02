   COP* *TRACE
     DA                S              8  0 INZ(5)
     DB                S              8  0 INZ(8)
     D RESULT          S              8  0 INZ(0)
      *
      *@StartTrace M(_SPANID1) "COMMENT"
     C                   EVAL      RESULT = A + B
      *@StopTrace M(_SPANID1)
