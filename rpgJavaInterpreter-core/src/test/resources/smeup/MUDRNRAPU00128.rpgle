     V* ==============================================================
     V* 30/09/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Assignment of scalar to a Standalone variable,
    O *  defined as integer array, with EVAL.
     V* ==============================================================
     D MSG             S             30
     D COUNT           S              3  0 INZ(1)

     D A146            S             14  6 DIM(9) INZ

     C                   EXSR      SHOR_RES
     C                   EVAL      A146=123
     C                   EXSR      SHOR_RES

     C                   SETON                                          LR



     C     SHOR_RES      BEGSR

     C                   EVAL      COUNT=1
     C     10            DOUEQ     COUNT
     C                   EVAL      MSG=%CHAR(A146(COUNT))
     C     MSG           DSPLY
     C                   EVAL      COUNT+=1
     C                   ENDDO

     C                   ENDSR