     D Msg             S             52
     D up              S             26    INZ('ABCDEFGHIJKLMNOPQRSTUVWXYZ')
     D lo              S             26    INZ('abcdefghijklmnopqrstuvwxyz')
     C                   Eval      Msg  = %XLATE(lo:up:'rpg dept')
     C     Msg           dsply
     C                   Eval      Msg  = %XLATE(up:lo:'RPG DEPT':6)
     C     Msg           dsply
     C                   SETON                                          LR
