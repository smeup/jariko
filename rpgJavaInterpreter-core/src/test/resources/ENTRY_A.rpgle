      *---------------------------------------------------------------
      * Tested features:
      * 'PARM' STATEMENT OF '*ENTRY PLIST' OF 'ENTRY_B' PROGRAM CALLED
      *  MUST BE ABLE TO DEFINE DATA.
      * - Variables define can be done by 'C specs' or 'D specs'.
      *   When it is done by 'C specs', several statements can be used,
      *   such as 'CLEAR', 'MOVE', 'Z-ADD', 'PARM', and so on.
      *   'PARM' statement is used in 'CALL statement block' (in caller
      *   program) and in '*ENTRY PLIST statement block' too (in called
      *   program).
      *   This test check the right definition of 'P2' variable in
      *   the called program 'ENTRY_B'.
      *---------------------------------------------------------------
      * Main
      *---------------------------------------------------------------
     C                   Z-ADD     1             P1                2 0
     C                   CLEAR                   P2                2 0
      *
     C                   CALL      'ENTRY_B'
     C                   PARM      P1            P2
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------