      * This program reproduces a bug: when it is called more than once
      * (each time with a different number of parameters) within the same
      * activation group, the *PARMS keyword on the Program Status Data
      * Structure does not reflect the actual number of parameters received
      * on the current call, but instead retains the value from a previous
      * call.
      * Runs in its own activation group (different from the caller's), so
      * that the activation group - and the state carried over between
      * calls - is not affected by other programs/tests.
      * The Program Status Data Structure below exposes the number of
      * parameters actually received through the *PARMS keyword.
     D                SDS
     D  NBRPARMS         *PARMS

     D P1              S             10
     D P2              S             10

     C     *ENTRY        PLIST
     C                   PARM                    P1
     C                   PARM                    P2

     C     NBRPARMS      DSPLY

     C                   SETON                                        RT
