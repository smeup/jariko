     P£MUGTP           B
     D £MUGTP          PI             5  0
     D XCA                           15    CONST
      *
     D OSZ             S                   LIKE(XCF) STATIC
      *
     C                   IF        OSZ=' '
     C                   EVAL      OSZ=XCA
     C                   ELSE
      * QUESTO RETURN NON FUNZIONA !!!
     C                   RETURN    3
     C                   ENDIF
      *
     C                   IF        XCA='A'
     C                   RETURN    1
     C                   ELSE
     C                   RETURN    2
     C                   ENDIF
      *
      * MA FUNZIONA SOLO L'ULTIMO
     C                   RETURN    5
     P                 E
