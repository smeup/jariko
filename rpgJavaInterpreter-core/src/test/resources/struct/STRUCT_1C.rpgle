      *---------------------------------------------------------------
      * Test Template and LikeDS support
      *---------------------------------------------------------------
     D FULLNAME        DS                        OCCURS(2)
     D FIRST                         10
     D LAST                          10
      *
     DTEACHERS         DS                        LIKEDS(FULLNAME)

      *---------------------------------------------------------------
      * M A I N
      *---------------------------------------------------------------
     C     1             OCCUR     TEACHERS
     C                   EVAL      TEACHERS.FIRST = 'Federico'
     C                   EVAL      TEACHERS.LAST  = 'Tomassetti'

     C     2             OCCUR     TEACHERS
     C                   EVAL      TEACHERS.FIRST = 'Marco'
     C                   EVAL      TEACHERS.LAST  = 'Lanari'
      *


    MU* VAL1(TEACHERS.FIRST) VAL2('Federico') COMP(EQ)
    MU* VAL1(TEACHERS.LAST)  VAL2('Tomassetti') COMP(EQ)
     C     1             OCCUR     TEACHERS
     C     TEACHERS      DSPLY


    MU* VAL1(TEACHERS.FIRST) VAL2('Marco') COMP(EQ)
    MU* VAL1(TEACHERS.LAST)  VAL2('Lanari') COMP(EQ)
     C     2             OCCUR     TEACHERS
     C     TEACHERS      DSPLY

     C                   SETON                                        RT