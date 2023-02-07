      *---------------------------------------------------------------
      * This test must fail because I try to set occur for DS without
      * OCCURS keyword
      *---------------------------------------------------------------
     D DS2             DS
     D  FLDC                   1      6
     D  FLDD                   7     11

      * OCCUR not supported for DS2
     C     3             OCCUR     DS2
     C                   EVAL      FLDC = 'FLDC'