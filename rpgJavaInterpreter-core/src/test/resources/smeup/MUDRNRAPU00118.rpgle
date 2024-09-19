     D MSG             S             30
     D COUNT           S              3  0 INZ(1)

     D A142            S             14  2 DIM(9) INZ

     C                   EXSR      SHOR_RES
     C                   MOVEA     12345         A142
     C                   EXSR      SHOR_RES

     C                   SETON                                          LR



     C     SHOR_RES      BEGSR

     C                   EVAL      COUNT=1
     C     10            DOUEQ     COUNT
     C                   EVAL      MSG=%CHAR(A142(COUNT))
     C     MSG           DSPLY
     C                   EVAL      COUNT+=1
     C                   ENDDO

     C                   ENDSR