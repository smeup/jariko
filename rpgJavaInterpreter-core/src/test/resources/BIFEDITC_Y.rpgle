     DInput1           S              7  0 Inz(0123456)
     DInput2           S              7  0 Inz(-0123456)
     DInput3           S              7  2 Inz(01234.56)
     DInput4           S              7  2 Inz(-01234.56)
     DInput5           S              7  2 Inz(00000.00)
     DInput6           S              7  2 Inz(01234.50)
     DINput7           S              8  0 INZ(12345678)
     DINput8           S              6  0 INZ(12)
     DInput9           S              7  0 Inz(12345)
     DEditcode_1       S             52A
     *********************************************************************
     C* Edit code "3" to format numeric value
     C
     C                   Eval      Editcode_1 = 'x'                + ' ' +
     C                                          %EDITC(Input1:'Y') + ' ' +
     C                                          %EDITC(Input2:'Y') + ' ' +
     C                                          %EDITC(Input3:'Y') + 'X'
     C     Editcode_1    dsply
     C                   Eval      Editcode_1 = 'x'                + ' ' +
     C                                          %EDITC(Input4:'Y') + ' ' +
     C                                          %EDITC(Input5:'Y') + 'X'
     C     Editcode_1    dsply
     C                   Eval      Editcode_1 = 'x'                + ' ' +
     C                                          %EDITC(Input6:'Y') + ' ' +
     C                                          %EDITC(Input7:'Y') + ' ' +
     C                                          %EDITC(Input8:'Y') + 'X'
     C     Editcode_1    dsply
     C                   Eval      Editcode_1 = 'x'                + ' ' +
     C                                          %EDITC(Input9:'Y') + 'X'
     C     Editcode_1    dsply
     *********************************************************************
     C                   Seton                                        LR
