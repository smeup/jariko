     V* ==============================================================
     V* 18/09/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Assignment of an array, defined as field of DS,
    O *  to a Standalone variable with MOVEA.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was
    O *  Issue executing MoveAStmt at line 32. An operation
    O *   is not implemented: takeFirst not yet implemented
    O *   for ProjectedArrayValue.
     V* ==============================================================
     D MSG             S             30
     D COUNT           S              2  0 INZ(1)
     D AU1_SIZE        S              2  0 INZ(10)

     D AUTOAP        E DS                  EXTNAME(AUTOAP0F)
     D  AU1                   61     80         DIM(10)
     D £AUATI          S             20

     C                   EVAL      AU1(1)='AA'
     C                   EVAL      AU1(2)='BB'
     C                   EVAL      AU1(3)='CC'
     C                   EVAL      AU1(4)='DD'
     C                   EVAL      AU1(5)='EE'
     C                   EVAL      AU1(6)='FF'
     C                   EVAL      AU1(7)='GG'
     C                   EVAL      AU1(8)='HH'
     C                   EVAL      AU1(9)='II'
     C                   EVAL      AU1(10)='LL'
     C                   MOVEA     AU1           £AUATI                         #An operation is not implemented: takeFirst not yet implemented for ProjectedArrayValue

     C                   EXSR      SHOW_RES

     C                   CLEAR                   £AUATI
     C                   MOVEL     *BLANKS       AU1
     C                   MOVEA     AU1           £AUATI

     C                   EXSR      SHOW_RES

     C                   SETON                                          LR



     C     SHOW_RES      BEGSR
    *
     C                   EVAL      COUNT=1
     C     AU1_SIZE      DOULT     COUNT
     C     AU1(COUNT)    DSPLY
     C                   EVAL      COUNT+=1
     C                   ENDDO
     C     AA£O01        DSPLY
     C     AA£O02        DSPLY
     C     AA£O03        DSPLY
     C     AA£O04        DSPLY
     C     AA£O05        DSPLY
     C     AA£O06        DSPLY
     C     AA£O07        DSPLY
     C     AA£O08        DSPLY
     C     AA£O09        DSPLY
     C     AA£O10        DSPLY
     C     £AUATI        DSPLY
    *
     C                   ENDSR