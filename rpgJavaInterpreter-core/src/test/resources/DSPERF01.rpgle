     ** DS performance comparision

     ** D spec definitions ****************************************
     D COUNT           S              4  0
     D LOOPLEN         S              4  0       INZ(999)
     D START           S               Z
     D END             S               Z
     D DSADUR          S             15  0
     D DSBDUR          S             15  0
     D PERFRATIO       S             10  2
     D MSG             S             50


     ** DSA definition ********************************************
     ** Every field is a standard string type
     D DSA             DS       1000000

     D FLDA01                    100000A
     D FLDA02                    100000A
     D FLDA03                    100000A
     D FLDA04                    100000A
     D FLDA05                    100000A
     D FLDA06                    100000A
     D FLDA07                    100000A
     D FLDA08                    100000A
     D FLDA09                    100000A
     D FLDA10                    100000A
     **************************************************************

     ** DSA definition ********************************************
     ** Every field is a unlimited string type
     D DSB             DS             0

     D FLDB01                          0
     D FLDB02                          0
     D FLDB03                          0
     D FLDB04                          0
     D FLDB05                          0
     D FLDB06                          0
     D FLDB07                          0
     D FLDB08                          0
     D FLDB09                          0
     D FLDB10                          0
     **************************************************************

     ** MEASURE DSALOOP DURATION
     C                   EVAL      MSG='STARTING DSALOOP'
     C     MSG           DSPLY
     C                   EVAL      START = %TIMESTAMP
     C                   EXSR      DSALOOP
     C                   EVAL      END = %TIMESTAMP
     C                   EVAL      DSADUR = %DIFF(END: START: *MSECONDS)/1000
     C                   EVAL      MSG = 'DSALOOP DURATION: ' + %CHAR(DSADUR)
     C     MSG           DSPLY
     **************************************************************

     ** MEASURE DSBLOOP DURATION
     C                   EVAL      MSG='STARTING DSBLOOP'
     C     MSG           DSPLY
     C                   EVAL      START = %TIMESTAMP
     C                   EXSR      DSBLOOP
     C                   EVAL      END = %TIMESTAMP
     C                   EVAL      DSBDUR = %DIFF(END: START: *MSECONDS)/1000
     C                   EVAL      MSG = 'DSBLOOP DURATION: ' + %CHAR(DSBDUR)
     C     MSG           DSPLY
     **************************************************************

     ** PERFORMANCE RATIO
     C                   EVAL      PERFRATIO=DSADUR/DSBDUR
     C                   EVAL      MSG='PERFORMANCE RATIO: ' + %CHAR(PERFRATIO)
     C     MSG           DSPLY
     **************************************************************

     ** DSALOOP IMPLEMENTATION
     C     DSALOOP       BEGSR
     C                   EVAL      DSA=*BLANK
     C                   EVAL      COUNT=0
     C                   DOU       COUNT = LOOPLEN
     C                   EVAL      FLDA01='FLDA01 CONTENT'
     C                   EVAL      FLDA02='FLDA02 CONTENT'
     C                   EVAL      FLDA03='FLDA03 CONTENT'
     C                   EVAL      FLDA03='FLDA03 CONTENT'
     C                   EVAL      FLDA04='FLDA04 CONTENT'
     C                   EVAL      FLDA05='FLDA05 CONTENT'
     C                   EVAL      FLDA06='FLDA06 CONTENT'
     C                   EVAL      FLDA07='FLDA07 CONTENT'
     C                   EVAL      FLDA08='FLDA08 CONTENT'
     C                   EVAL      FLDA09='FLDA09 CONTENT'
     C                   EVAL      FLDA10='FLDA10 CONTENT'
     C                   EVAL      COUNT=COUNT+1
     C                   ENDDO
     C                   ENDSR
     **************************************************************

     ** DSBLOOP IMPLEMENTATION
     C     DSBLOOP       BEGSR
     C                   EVAL      DSB=*BLANK
     C                   EVAL      COUNT=0
     C                   DOU       COUNT = LOOPLEN
     C                   EVAL      FLDB01='FLDB01 CONTENT'
     C                   EVAL      FLDB02='FLDB02 CONTENT'
     C                   EVAL      FLDB03='FLDB03 CONTENT'
     C                   EVAL      FLDB03='FLDB03 CONTENT'
     C                   EVAL      FLDB04='FLDB04 CONTENT'
     C                   EVAL      FLDB05='FLDB05 CONTENT'
     C                   EVAL      FLDB06='FLDB06 CONTENT'
     C                   EVAL      FLDB07='FLDB07 CONTENT'
     C                   EVAL      FLDB08='FLDB08 CONTENT'
     C                   EVAL      FLDB09='FLDB09 CONTENT'
     C                   EVAL      FLDB10='FLDB10 CONTENT'
     C                   EVAL      COUNT=COUNT+1
     C                   ENDDO
     C                   ENDSR
     **************************************************************
