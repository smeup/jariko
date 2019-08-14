      *---------------------------------------------------------------
      * Test Field Overlay with position
      *---------------------------------------------------------------
     DMYDS             DS
     DPARTNUM                        10A
     DFAMILY                          3A   OVERLAY(PARTNUM)
     DSEQUENCE                        6A   OVERLAY(PARTNUM:4)
     DLANGUAGE                        1A   OVERLAY(PARTNUM:10)
      *---------------------------------------------------------------
      * M A I N
      *---------------------------------------------------------------
     C                   DSPLY                   MYDS
      *
     C                   SETON                                        RT
