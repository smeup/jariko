     V* ==============================================================
     D* 12/01/24  nn.mm author
     V* ==============================================================

     D MSG             S             50          VARYING
     
     D A1              S             30
     D A2              S             30          VARYING
     D N1              S              5  0
     D N2              S              5  2
    
     D DS              DS                  
     D  DSA1                        30      
     D  DSA2                        30
     D  DSN1                          5  0
     D  DSN2                          5  2

     C                   EVAL      A1 = 'A1'
     C                   EVAL      A2 = 'A2'
     C                   EVAL      N1 = 1
     C                   EVAL      N2 = 1.1

     C     C             EVAL      DSA1='DSA1'
     C     C             EVAL      DSA2='DSA2'
     C     C             EVAL      DSN1=1
     C     C             EVAL      DSN2=1.1

     C                   RESET     A1
     C                   RESET     A2
     C                   RESET     N1
     C                   RESET     N2
     C                   RESET     DS


     C                   EVAL      MSG='A1:' + A1
     C     MSG           DSPLY      

     C                   EVAL      MSG='A2:' + A2
     C     MSG           DSPLY     
     
     C                   EVAL      MSG='N1:' + %CHAR(N1)
     C     MSG           DSPLY     
           
     C                   EVAL      MSG='N2:' + %CHAR(N2)      
     C     MSG           DSPLY     
     
     C                   EVAL      MSG='DSA1:' + DSA1
     C     MSG           DSPLY      
     
     C                   EVAL      MSG='DSA2:' + DSA2
     C     MSG           DSPLY     

     C                   EVAL      MSG='DSN1:' + %CHAR(DSN1)
     C     MSG           DSPLY     

     C                   EVAL      MSG='DSN2:' + %CHAR(DSN2)
     C     MSG           DSPLY      
     
     C                   SETON                                        LR

         
    
        
        

