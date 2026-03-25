     V* ==============================================================
     D* Purpose of this program is to fix %DEC when the string
     D* contains italian decimal separator (comma instead of dot)
     V* ==============================================================

     D DECIMAL         S              9  2
     D STR             S             10A

      * %DEC with dot decimal separator ************************************
     C                   EVAL      STR='1.30'
     C                   EVAL      DECIMAL=%DEC(STR:9:2)
      * Expected:
      *  DECIMAL = 1.30
     C     DECIMAL       DSPLY
      ***********************************************************************

      * %DEC with italian decimal separator (comma) ************************
     C                   EVAL      STR='1,30'
     C                   EVAL      DECIMAL=%DEC(STR:9:2)
      * Expected:
      *  DECIMAL = 1.30
     C     DECIMAL       DSPLY
      ***********************************************************************

     C                   SETON                                        LR