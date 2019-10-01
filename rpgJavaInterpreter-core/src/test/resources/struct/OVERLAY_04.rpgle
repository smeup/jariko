      *---------------------------------------------------------------
      * Test Field Overlay with Next
      *---------------------------------------------------------------
     DMYDS             DS
     D PARTNUM
     D  FAMILY                        3A   OVERLAY(PARTNUM)
     D  SEQUENCE                      6A   OVERLAY(PartNumber:*NEXT)
     D  LANGUAGE                      1A   OVERLAY(PartNumber:*NEXT)

      *---------------------------------------------------------------
      * M A I N
      *---------------------------------------------------------------
     C                   DSPLY                   MYDS
      *
     C                   SETON                                        RT
