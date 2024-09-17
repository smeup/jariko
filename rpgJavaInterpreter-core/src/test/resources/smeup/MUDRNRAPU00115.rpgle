     V* ==============================================================
     V* 17/09/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Assignment, with Z-ADD, a value to a DS field defined as
    O *  array and overlay.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was
    O *  Issue executing ZAddStmt at line xyz. null..
     V* ==============================================================
     D MSG             S             30
     D CONT            S              3  0 INZ(1)
     D SUM             S             12  6 INZ(0)

     D D5COSO        E DS                  EXTNAME(D5COSO0F)
     D  D50                                DIM(99) LIKE(D$C001) INZ
     D                                             OVERLAY(D5COSO:88)

     C                   Z-ADD     1             D50                            #Issue executing ZAddStmt at line 20

     C                   EXSR      CALCULATE

     C                   EVAL      SUM=0
     C                   EVAL      CONT=1
     C                   Z-ADD     0             D50
     C                   EXSR      CALCULATE

     C                   SETON                                          LR



     C     CALCULATE     BEGSR

     C     100           DOUEQ     CONT
     C                   EVAL      SUM+=D50(CONT)
     C                   EVAL      CONT+=1
     C                   ENDDO
     C                   EVAL      MSG=%CHAR(SUM)
     C     MSG           DSPLY

     C                   ENDSR