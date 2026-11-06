     V* ==============================================================
     V* Array access with a name written in a different case than its
     V* declaration (RPG names are case-insensitive)
     V* ==============================================================
     D MyArr           S             10    DIM(5)
     D MSG             S             50
      *
     C                   EVAL      MYARR(2)='ABCDEFGHIJ'
     C                   EVAL      %SUBST(Myarr(2):3:1)=' '
     C                   EVAL      %SUBST(myARR(1+1):5:2)='XY'
     C                   EVAL      MSG=MyArr(2)
     C     MSG           DSPLY
     C                   SETON                                        LR
