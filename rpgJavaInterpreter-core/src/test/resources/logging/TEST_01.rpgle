     DFIELD1           S              8  0
     DCOUNT            S              8  0
     D NBR             S              8  0 INZ(10)
     D RESULT          S              8  0 INZ(0)
     DA                S              8  0 INZ(0)
     DB                S              8  0 INZ(1)
      *
     C                   CLEAR                   FIELD1
      * Test EqualityExpr
     C                   IF        FIELD1 = 0
     C                   ENDIF
      * Test DifferentThanExpr
     C                   IF        FIELD1 <> 0
     C                   ENDIF
      * Test GreaterThanExpr
     C                   IF        FIELD1 > 0
     C                   ENDIF
      * Test GreaterEqualThanExpr
     C                   IF        FIELD1 >= 0
     C                   ENDIF
      * Test LessEqualThanExpr
     C                   IF        FIELD1 <= 0
     C                   ENDIF
      * Test LessThanExpr
     C                   IF        FIELD1 <  0
     C                   ENDIF
      * Test LogicalAnd
     C                   IF        FIELD1 > 0
     C                             AND
     C                             RESULT = 0
      * Test ElseIf
     C                   ELSEIF FIELD1 < 0
     C
     C                   ENDIF
      * Test LogicalOr
     C                   IF        FIELD1 > 0
     C                             OR
     C                             RESULT = 0
      * Test ElseIf
     C                   ELSEIF FIELD1 < 0
     C
     C                   ENDIF
     C                   SETON                                        LR
      *--------------------------------------------------------------*



















