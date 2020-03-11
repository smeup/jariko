     D Msg             S             10
     D AAA             S              3    INZ('AAA')
     D ZZZ             S              3    INZ('ZZZ')
     **********************************************************************
     C                   EVAL      Msg = 'Test OK'
     C     ZZZ           CAB       AAA           SHOW                 414243
     C                   EVAL      Msg = 'Test KO'
     **********************************************************************
     C     Msg           DSPLY
     C                   GOTO      fine
     **********************************************************************
     C     SHOW          TAG
     C     Msg           DSPLY
     C     Fine          Tag
     C   41'41ON'        DSPLY
     C   42'42ON'        DSPLY
     C   43'43ON'        DSPLY
     C                   SETON                                          LR
