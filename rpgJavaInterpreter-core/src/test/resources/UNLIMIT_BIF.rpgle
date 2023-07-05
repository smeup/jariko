     V* ==============================================================
     D* Purpose of this program is to test UnlimitedStringType
     D* with the BIFs
     V* ==============================================================

     D INTEGER         S             10  0
     D DECIMAL         S              5  2
     D UNL_STR         S               0
     D MSG             S             50A   VARYING


      * %INT test ****************************************************
     C                   EVAL      MSG='%INT'
     C                   EVAL      UNL_STR='1234'
     C                   EVAL      INTEGER=%INT(UNL_STR)
     C     MSG           DSPLY
     C     INTEGER       DSPLY
      ****************************************************************

      * %DEC test ****************************************************
     C                   EVAL      MSG='%DEC'
     C                   EVAL      UNL_STR='1.5'
     C                   EVAL      DECIMAL=%DEC(UNL_STR:5:2)
     C     MSG           DSPLY
     C     DECIMAL       DSPLY
      ****************************************************************
