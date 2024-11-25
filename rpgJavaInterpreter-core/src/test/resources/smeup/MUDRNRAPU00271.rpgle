     V* ==============================================================
     D* 19/11/24  nn.mm lanmam
     V* ==============================================================
     D COUNTER         S              5  0

     C                   EVAL      COUNTER=0

     C                   EXSR      MEM_DT

     C     MEM_DT        BEGSR
     C     RILALTRO      TAG
     C                   IF        COUNTER < 10000
     C                   EVAL      COUNTER=COUNTER + 1
     C                   GOTO      RILALTRO
     C                   ENDIF
     C     'OK'          DSPLY
     C                   ENDSR