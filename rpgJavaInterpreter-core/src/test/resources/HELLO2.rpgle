     D Msg             S             50
     D MsgEng          S             12
     D MsgRus          S             12
     D MsgCin          S              4
     C                   Eval      MsgEng  = 'Hello World'
     C                   Eval      MsgRus  = 'привет мир'
     C                   Eval      MsgCin  = '你好世界'
     C                   Eval      Msg  = MsgCin+MsgEng+MsgRus
     C                   dsply                   Msg
     C                   Eval      Msg  = 'Hello World привет мир 你好世界 !'
     C                   dsply                   Msg
     C                   SETON                                          LR
