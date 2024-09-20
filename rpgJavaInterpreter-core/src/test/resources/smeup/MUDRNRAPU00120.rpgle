     V* ==============================================================
     V* 19/09/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Assignment, with Z-ADD, a value to a S field defined
    O *  as array.
     V* ==============================================================
     D MSG             S             30
     D COUNT           S              3  0 INZ(1)
     D SUM             S             12  6 INZ(0)


     D  D50            S              5  0 DIM(99) INZ

     C                   Z-ADD     1             D50
     C                   EXSR      SHOW_RES

     C                   EVAL      SUM=0
     C                   EVAL      COUNT=1
     C                   Z-ADD     0             D50
     C                   EXSR      SHOW_RES

     C                   SETON                                          LR



     C     SHOW_RES      BEGSR

     C     100           DOUEQ     COUNT
     C                   EVAL      SUM+=D50(COUNT)
     C                   EVAL      COUNT+=1
     C                   ENDDO
     C                   EVAL      MSG=%CHAR(SUM)
     C     MSG           DSPLY

     C                   ENDSR