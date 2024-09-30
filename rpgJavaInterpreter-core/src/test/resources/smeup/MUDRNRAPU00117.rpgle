     V* ==============================================================
     V* 19/09/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Assignment of scalar to a Standalone variable,
    O *  defined as decimal array, with MOVEA.
    O * The left value number of digits is lower than right
    O *  decimal digits.
     V* ==============================================================
     D MSG             S             30
     D COUNT           S              3  0 INZ(1)

     D A146            S             14  6 DIM(9) INZ

     C                   EXSR      SHOR_RES
     C                   MOVEA     123           A146
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