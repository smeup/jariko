     V* ==============================================================
     V* 21/10/2024 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Call a program that throws an error with an error indicator and ensure the execution context is appropriate
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error was about the caller not resolving
    O * symbols in the correct compilation unit
    O *
    O * Note: The following test resembles the structure of the program where the issue was discovered
    O * The original behavior was the following:
    O * - Call a program with an error indicator
    O * - Callee errors and execution falls back to the caller
    O * - The internal context was not cleaned up
    O * - The caller thinks to be the callee
    O * - Execute a procedure defined in the caller
    O * - The procedure is not found because we search for it in the callee CU instead of the caller one
     V* ==============================================================

      * Test declarations
     D £TESTDS         DS
     D  £TESTPA                      10

      * Call of a program that throws an error and setting the error indicator
     C                   CALL        'ERRORPGM'                         35

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
