     ***********************************************************************************
     ** DS EXAMPLE FROM http://www.go4as400.com/data-structure-in-as400/data.aspx?cid=13
     ***********************************************************************************
     DCURTIMSTP        DS
     DOPN              DS
     DYY                              4A
     DFILLER1                         1A   INZ('-')
     DMM                              2A
     DFILLER2                         1A   INZ('-')
     DDD                              2A
     C                   EVAL      YY=%CHAR(2012)
     C                   EVAL      MM=%CHAR(10)
     C                   EVAL      DD=%CHAR(15)
     C* Displays 2012-10-15
     C     OPN           DSPLY
     C                   SETON                                        LR