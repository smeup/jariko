     V* ==============================================================
     D* 12/01/24  nn.mm author
     V* ==============================================================

     D MSGOK           S             50          VARYING
     D MSGKO           S             50          VARYING
     
     D A1              S             30
     D A2              S             30          VARYING
     D A3              S              1          INZ('A3')
     D N1              S              5  0
     D N2              S              5  2
     D N3              S              5  2       INZ(1.1)

     D A1_INZ          S             30
     D A2_INZ          S             30          VARYING
     D A3_INZ          S              1          INZ('A3')
     D N1_INZ          S              5  0
     D N2_INZ          S              5  2
     D N3_INZ          S              5  2       INZ(1.1)

   
     D DS              DS                  
     D  DSA1                        30      
     D  DSA2                         4           INZ('DSA2')

     D DS_INZ          DS                  
     D  DSA1_INZ                    30      
     D  DSA2_INZ                     4           INZ('DSA2')



     C                   EVAL      A1 = 'A1'
     C                   EVAL      A2 = 'A2'
     C                   EVAL      N1 = 1
     C                   EVAL      N2 = 1.1

     C                   EVAL      DSA1='DSA1'
     C                   EVAL      DSA2='DSA2'

     C                   RESET                   A1
     C                   RESET                   A2
     C                   RESET                   N1
     C                   RESET                   N2
     C                   RESET                   DS

     C                   EVAL      MSGOK='A1_OK'
     C                   EVAL      MSGKO='A1_KO'
     C     A1            IFEQ      A1_INZ
     C     MSGOK         DSPLY
     C                   ELSE
     C     MSGOK         DSPLY
     C                   ENDIF     
     
     C                   EVAL      MSGOK='A2_OK'
     C                   EVAL      MSGKO='A2_KO'
     C     A2            IFEQ      A2_INZ
     C     MSGOK         DSPLY     
     C                   ELSE
     C     MSGKO         DSPLY      
     C                   ENDIF      
     
     C                   EVAL      MSGOK='A3_OK'
     C                   EVAL      MSGKO='A3_KO'
     C     A3            IFEQ      A3_INZ
     C     MSGOK         DSPLY     
     C                   ELSE
     C     MSGKO         DSPLY     
     C                   ENDIF     
        
     C                   EVAL      MSGOK='N1_OK'
     C                   EVAL      MSGKO='N1_KO'
     C     N1            IFEQ      N1_INZ
     C     MSGOK         DSPLY     
     C                   ELSE
     C     MSGKO         DSPLY     
     C                   ENDIF      

     C                   EVAL      MSGOK='N2_OK'    
     C                   EVAL      MSGKO='N2_KO'
     C     N2            IFEQ      N2_INZ
     C     MSGOK         DSPLY     
     C                   ELSE
     C     MSGKO         DSPLY      
     C                   ENDIF      
     
     C                   EVAL      MSGOK='N3_OK'
     C                   EVAL      MSGKO='N3_KO'
     C     N3            IFEQ      N3_INZ
     C     MSGOK         DSPLY     
     C                   ELSE
     C     MSGKO         DSPLY     
     C                   ENDIF     
            

     C                   EVAL      MSGOK='DS_OK'
     C                   EVAL      MSGKO='DS_KO'
     C     DS            IFEQ      DS_INZ
     C     MSGOK         DSPLY     
     C                   ELSE
     C     MSGKO         DSPLY     
     C                   ENDIF     

     C                   EVAL      MSGOK='DSA1_OK'
     C                   EVAL      MSGKO='DSA1_KO'
     C     DSA1          IFEQ      DSA1_INZ
     C     MSGOK         DSPLY     
     C                   ELSE
     C     MSGKO         DSPLY     
     C                   ENDIF     

     C                   EVAL      MSGOK='DSA2_OK'
     C                   EVAL      MSGKO='DSA2_KO'
     C     DSA2          IFEQ      DSA2_INZ
     C     MSGOK         DSPLY     
     C                   ELSE
     C     MSGKO         DSPLY     
     C                   ENDIF     
     
     C                   SETON                                        LR