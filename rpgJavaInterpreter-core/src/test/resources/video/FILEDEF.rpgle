     V* ==============================================================
     D* 06/02/24  
     D* Test the file video metadata loading
     V* ==============================================================
     
     
     FB£DIR40V  CF   E             WORKSTN USROPN
     F                                     INFDS(DSSF01)

     D MSG             S             50          VARYING
     
     D WFUND1          DS
     D  WSDATA                        8  0
      * This field definition is assumed by B£DIR40V
     D  W$PERI

     C                   EVAL      W$PERI=12
     C                   EVAL      MSG='W$PERI:' + %CHAR(W$PERI)
      * EXPECTED: W$PERI:12
     C     MSG           DSPLY  

      * This field is defined in B£DIR40V
     C                   EVAL      £RASDI='HELLO_WORLD'
     C                   EVAL      MSG='£RASDI:' + %CHAR(£RASDI)
      * EXPECTED: £RASDI=:HELLO_WORLD
     C     MSG           DSPLY     
     

        



     