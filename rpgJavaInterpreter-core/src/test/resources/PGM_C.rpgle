      *---------------------------------------------------------------
      * Tested features:
      * USED BY 'CALLER_P' PROGRAM TO CHECK VARIABLE DECLARATION BY
      * 'PARM' STATEMENT
      *---------------------------------------------------------------
     D PARM1           S             15
     D PARM2           S             30
      *---------------------------------------------------------------
      * Main
      *---------------------------------------------------------------
     C     *ENTRY        PLIST
     C                   PARM                    PARM1
     C                   PARM                    PARM2
      * Received values
     C                   EVAL      PARM1='Echo P1: '+%TRIM(PARM1)
     C                   EVAL      PARM2='Echo P2:' +%TRIM(PARM2)
     C     PARM1         DSPLY
     C     PARM2         DSPLY
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
