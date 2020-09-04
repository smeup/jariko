     C                   EXSR      MYSUB1
     C                   SETON                                          LR
     **********************************************************************
     C     MYSUB1        BEGSR
     C                   Eval      MyPar = ' '
     C                   dsply                   MyPar
     C                   EXSR      MYSUB2
     C                   dsply                   MyPar
     C                   ENDSR
     **********************************************************************
     C     MYSUB2        BEGSR
     C                   CALL      'CALLDEFV2'
     C                   PARM                    MyPar             1
     C                   ENDSR
