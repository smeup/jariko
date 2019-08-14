      *---------------------------------------------------------------
      * This test defines a data structure with two fields
      * It should possible to reference the fields using the
      * qualified name (dot notation).
      *---------------------------------------------------------------
     DMYDS             DS                  QUALIFIED
     DFLD1                            5 0
     DFLD2                           10
      *---------------------------------------------------------------
      * M A I N
      *---------------------------------------------------------------
     C                   EVAL MYDS.FLD1 = 0
     C                   EVAL MYDS.FLD2 = 'ABCD'
     C                   DSPLY                   MYDS.FLD1
     C                   DSPLY                   MYDS.FLD2
      *
     C                   SETON                                        RT
