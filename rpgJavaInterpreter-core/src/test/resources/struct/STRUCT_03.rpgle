     ***********************************************************************************
     ** DS EXAMPLE FROM http://www.go4as400.com/data-structure-in-as400/data.aspx?cid=13
     ***********************************************************************************
     DCURTIMSTP        DS
     DCURTIMDATE               1      8S 0
     DCURRYEAR                 1      4S 0
     DCURRMONTH                5      6S 0
     DCURRDAY                  7      8S 0
     DCURRHRS                  9     10S 0
     DCURRMINS                11     12S 0
     DCURRSECS                13     16S 0
     Dtime_is          S               z
     Dtime_isO         S             20
     ** CALCULATE THE TIME FOR THE CURRENT SYSTEM DATE
     C                   EVAL      TIME_IS=%TIMESTAMP()
     C                   EVAL      TIME_ISO=%char(TIME_IS:*iso0)
     C     TIME_ISO      DSPLY
     C                   EVAL      CURRYEAR=%dec(%SUBST(TIMe_ISo:1:4):4:0)
     C                   EVAL      CURRMONTH=%dec(%SUBST(TIMe_ISo:5:2):2:0)
     C                   EVAL      CURRDAY=%dec(%SUBST(TIMe_ISo:7:2):2:0)
     C                   EVAL      CURRHRS=%dec(%SUBST(TIMe_ISo:9:2):2:0)
     C                   EVAL      CURRMINS=%dec(%SUBST(TIMe_ISo:11:2):2:0)
     C                   EVAL      CURRSECS=%dec(%SUBST(TIMe_ISo:13:4):4:0)
     C     CURTIMSTP     DSPLY
     C     CURTIMDATE    DSPLY
     C     CURRYEAR      DSPLY
     C     CURRMONTH     DSPLY
     C     CURRDAY       DSPLY
     C     CURRHRS       DSPLY
     C     CURRMINS      DSPLY
     C     CURRSECS      DSPLY
     C                   SETON                                        LR
