      *---------------------------------------------------------------
      * Test Template and LikeDS support
      *---------------------------------------------------------------
     DFULLNAME         DS                TEMPLATE QUALIFIED
     D FIRST                         10A
     D LAST                          10A
      *
     DTEACHER          DS                LIKEDS(FULLNAME)
     DSTUDENT          DS                LIKEDS(FULLNAME)

      *---------------------------------------------------------------
      * M A I N
      *---------------------------------------------------------------
     C                   DSPLY                   STUDENT
     C                   DSPLY                   TEACHER
      *
     C                   SETON                                        RT
