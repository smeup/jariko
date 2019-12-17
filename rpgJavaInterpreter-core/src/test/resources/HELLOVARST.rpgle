     D Msg             S             50
     D VarStr          S             50    Varying    Inz
     D StdStr          S             50    Inz
     D StrArray        S              3    DIM(12)
     D I               S              2  0
     C                   Eval      StrArray(1) = ' H '
     C                   Eval      StrArray(2) = ' e '
     C                   Eval      StrArray(4) = ' l '
     C                   Eval      StrArray(5) = ' l '
     C                   Eval      StrArray(6) = ' o '
     C                   Eval      StrArray(7) = ' - '
     C                   Eval      StrArray(8) = ' W '
     C                   Eval      StrArray(9) = ' o '
     C                   Eval      StrArray(10)= ' r '
     C                   Eval      StrArray(11)= ' l '
     C                   Eval      StrArray(12)= ' d '
     ********
     C*                  For       I = 1 TO 12
     C*                  Eval      StdStr = %TrimR(StdStr) + %Trim(StrArray(I))
     C*                  EndFor
     ********
     C                   For       I = 1 TO 12
     C                   Eval      VarStr = VarStr + %Trim(StrArray(I))
     C                   EndFor
     ********
     C                   If        StdStr = VarStr
     C                   Eval      Msg = 'Eq'
     C                   Else
     C                   Eval      Msg = 'Not Eq'
     C                   EndIf
     C                   Dsply                   Msg
     C                   Dsply                   VarStr
     C                   Dsply                   StdStr
     ********
     C                   SETON                                        LR
