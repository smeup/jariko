     D                 DS
     D FLT                          256    DIM(999)
      * Test declarations
     D  FLT_CD                       15    OVERLAY(FLT:1)
     D $I              S              3  0
     D £IQ3FL          S             15

      * *ZERO/*ZEROS symbolic constant should be converted to numbers without any issue
     C                   Z-ADD     *ZEROS        $FL               3 0
     C                   EVAL      $I=%LOOKUP(£IQ3FL:FLT_CD:1:$FL)

      * Test output
     C     $I            DSPLY