      * This program reproduces a bug: when it is called more than once
      * within the same activation group (each time with a different
      * argument), a *ENTRY PLIST parameter received through the
      * Factor 1 / Result form (RESULT is copied into PARM1 on entry) does
      * not reflect the value passed on the current call, but instead
      * retains the value restored from the previous call's memory slice.
      * It keeps itself activated with SETON RT, so the second call is
      * re-entered without a full reinitialization.
     D PARM1           S             10    INZ('INIT')
     D RESULT          S             10

     C     PARM1         DSPLY
     C                   SETON                                        RT

     C     *INZSR        BEGSR
     C     *ENTRY        PLIST
     C     PARM1         PARM                    RESULT
     C                   ENDSR
