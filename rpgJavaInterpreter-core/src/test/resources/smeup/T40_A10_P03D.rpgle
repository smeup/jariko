     D £DBG_Str        S             99                                         Stringa
     D A10_DS_P03      DS                                                               
     D  A10_DS_P03_A                  5    INZ('A')                                     
     D  A10_DS_P03_B                  4  0 INZ(44)                                      
                                                                                   
     C                   RESET                   A10_DS_P03
     C                   EVAL      £DBG_Str='Contenuto Post-RESET: '                    
     C                             +A10_DS_P03_A+'-'+%CHAR(A10_DS_P03_B)
     C* Expected: Contenuto Post-RESET: A    -44
     C     £DBG_Str      DSPLY                                                          
     C                   SETON                                        LR  