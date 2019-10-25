      *---------------------------------------------------------------
      * Test Field Overlay with Next
      *---------------------------------------------------------------
     DMYDS             DS
     D PARTNUM                       10
     D  FAMILY                        3A   OVERLAY(PARTNUM)
     D  SEQUENCE                      6A   OVERLAY(PARTNUM:*NEXT)
     D  LANGUAGE                      1A   OVERLAY(PARTNUM:*NEXT)

      *---------------------------------------------------------------
      * M A I N
      *---------------------------------------------------------------
     C                   DSPLY                   MYDS
      *
     C                   SETON                                        RT
