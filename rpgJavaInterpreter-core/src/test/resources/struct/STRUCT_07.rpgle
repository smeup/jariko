      *---------------------------------------------------------------
      * Test Template and LikeDS support
      *---------------------------------------------------------------
     DFULLNAME         DS                  TEMPLATE QUALIFIED
     D FIRST                         10
     D LAST                          10
      *
     DTEACHER          DS                  LIKEDS(FULLNAME)
     DSTUDENT          DS                  LIKEDS(FULLNAME)

      *---------------------------------------------------------------
      * M A I N
      *---------------------------------------------------------------
     C                   EVAL   TEACHER.FIRST = 'Federico'
     C                   EVAL   TEACHER.LAST  = 'Tomassetti'
     C                   EVAL   STUDENT.FIRST = 'Maurizio'
     C                   EVAL   STUDENT.LAST  = 'Taverna'
      *
    MU* VAL1(TEACHER.FIRST) VAL2('Federico') COMP(EQ)
    MU* VAL1(TEACHER.LAST)  VAL2('Tomassetti') COMP(EQ)
     C                   DSPLY                   TEACHER
    MU* VAL1(STUDENT.FIRST) VAL2('Maurizio') COMP(EQ)
    MU* VAL1(STUDENT.LAST)  VAL2('Taverna') COMP(EQ)
     C                   DSPLY                   STUDENT
      *
     C                   SETON                                        RT
