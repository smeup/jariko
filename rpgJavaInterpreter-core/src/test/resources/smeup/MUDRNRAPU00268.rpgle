      * Test declarations
     D £TESTDS         DS
     D  £TESTPA                      10

      * Call of a program that throws an error and setting the error indicator
     C                   CALL        'ERRORPGM'                         35

      * If call failed but the error indicator was set
      * Execution should continue and the context should reference the correct program
      * Note: We can test the context state is correct by calling a procedure
     C                   IF        P_Test(£TESTPA:'EmiMsg(':'')='Y'
     C     'ok'          DSPLY
     C                   ENDIF

      * Test procedure
     PP_Test           B
     D P_Test          Pi         30000    VARYING
     D  $A                           15    CONST
     D  $B                            1N   OPTIONS(*NOPASS)
     D  $C                            1    OPTIONS(*NOPASS)
     C                   RETURN    'Y'
     P                 E

     C                   SETON                                        LR
