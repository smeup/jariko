     D STR10           S             10
     C                   EXSR      CALLSR
     C                   CALL      'CALLDEFV2'
     C                   PARM                    $A                2
     C                   EVAL      $A='T'
     C     CALLSR        BEGSR
     C                   EVAL      STR10='ABCDE'
     C                   ENDSR