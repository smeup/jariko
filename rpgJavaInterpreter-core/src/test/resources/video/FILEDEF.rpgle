     V* ==============================================================
     D* 06/02/24  
     D* Test the file video metadata loading
     V* ==============================================================
     
     
     FFILEDEFV  CF   E             WORKSTN USROPN
     F                                     INFDS(DSSF01)

     D MSG             S             50          VARYING
     
     D WFUND1          DS
     D  WSDATA                        8  0
      * This field definition is assumed by FILEDEFV
     D  W$PERI

     C                   EVAL      W$PERI=12
     C                   EVAL      MSG='W$PERI:' + %CHAR(W$PERI)
      * EXPECTED: W$PERI:12
     C     MSG           DSPLY  

      * This field is defined in FILEDEFV
     C                   EVAL      £RASDI='HELLO_WORLD'
     C                   EVAL      MSG='£RASDI:' + %CHAR(£RASDI)
      * EXPECTED: £RASDI=:HELLO_WORLD
     C     MSG           DSPLY     
     

        



     