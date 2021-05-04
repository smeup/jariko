      *---------------------------------------------------------------
      * Tested features:
      * RIGHT HANDLING OF %PARMS BUILT-IN FUNCTION
      * - A program can receive parameters accordling to its ''*ENTRY PLIST'
      *   statement.
      *   Parameters are positional and not mandatory, so an exception will
      *   be thrown trying to use a non received parameter.
      *   Built-in function '%PARMS' is useful to get the number of received
      *   parameters and know which can be used in program flow.
      *---------------------------------------------------------------
     D P1              S              2  0
     D P2              S              2  0
     D PARMS           S              2  0
      *---------------------------------------------------------------
      * Main
      *---------------------------------------------------------------
     C     *ENTRY        PLIST
     C                   PARM                    P1
     C                   PARM                    P2
      *
     C                   EVAL      PARMS=%PARMS
     C     PARMS         DSPLY
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
