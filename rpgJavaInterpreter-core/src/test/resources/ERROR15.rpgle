     V* ==============================================================
     D* 22/04/24
     D* Purpose: To demonstrate that jariko can move on also in case
     D* of syntax errors.
     D* In this case the program will display 'HELLO WORLD!!!'
     D* successfully even if the variable MSG2 is not declared.
     V* ==============================================================

     D MSG1            S             50

     C                   EVAL      MSG1 = 'HELLO WORLD!!!'
     C                   SELECT
     C                   WHEN      1 = 1
     C     MSG1          DSPLY     
     C                   OTHER
     C     MSG2          DSPLY
     C                   ENDSL


     