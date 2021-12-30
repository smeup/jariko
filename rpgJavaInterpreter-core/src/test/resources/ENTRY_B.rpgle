      *---------------------------------------------------------------
      * Tested features:
      * 'PARM' STATEMENT OF '*ENTRY PLIST' MUST BE ABLE TO DEFINE DATA.
      * - Variables define can be done by 'C specs' or 'D specs'.
      *   When it is done by 'C specs', several statements can be used,
      *   such as 'CLEAR', 'MOVE', 'Z-ADD', 'PARM', and so on.
      *   'PARM' statement is used in 'CALL statement block' (in caller
      *   program) and in '*ENTRY PLIST statement block' too (in called
      *   program).
      *   This test check the right definition of 'P2' variable.
      *   This program is called by 'ENTRY_A'.
      *---------------------------------------------------------------
      * Main
      *---------------------------------------------------------------
     C     *ENTRY        PLIST
     C                   PARM                    P2                2 0
      *
     C     P2            DSPLY
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------