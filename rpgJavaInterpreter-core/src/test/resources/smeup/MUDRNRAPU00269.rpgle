     V* ==============================================================
     V* 30/10/2024 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Call a program with an error indicator that calls another program
    O * that throws an error and ensure the execution context is appropriate
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error was about the caller not resolving
    O * symbols in the correct compilation unit
    O *
    O * Note: see MUDRNRAPU00268.rpgle for a deeper explanation
    O * on how the error was reproduced
     V* ==============================================================

      * Test declarations
     D £TESTDS         DS
     D  £TESTPA                      10

      * Call of a program that throws an error and sets the error indicator
     C                   CALL        'ERRORPGM2'                        35

      * If call failed but the error indicator was set
      * Execution should continue and the context should reference the correct program
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
