     P£MUGTP           B
     D £MUGTP          PI             5  0
     D XCA                           15    CONST
      *
     D OSZ             S                   LIKE(XCF) STATIC
      *
     C                   IF        OSZ=' '
     C                   EVAL      OSZ=XCA
     C                   ELSE
     C                   RETURN    3
     C                   ENDIF
      *
     C                   IF        XCA='A'
     C                   RETURN    1
     C                   ELSE
     C                   RETURN    2
     C                   ENDIF
      *
     P                 E