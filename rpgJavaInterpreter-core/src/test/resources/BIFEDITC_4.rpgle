     DInput1           S              7  0 Inz(0123456)
     DInput2           S              7  0 Inz(-0123456)
     DInput3           S              7  2 Inz(01234.56)
     DInput4           S              7  2 Inz(-01234.56)
     DInput5           S              7  2 Inz(00000.00)
     DInput6           S              7  2 Inz(01234.50)

     DEditcode_1       S             52A
     *********************************************************************
     C* Edit code "2" to format numeric value
     C
     C                   Eval      Editcode_1 = 'x'                + ' ' +
     C                                          %EDITC(Input1:'4') + ' ' +
     C                                          %EDITC(Input2:'4') + ' ' +
     C                                          %EDITC(Input3:'4') + 'X'
     C     Editcode_1    dsply
     C                   Eval      Editcode_1 = 'x'                + ' ' +
     C                                          %EDITC(Input4:'4') + ' ' +
     C                                          %EDITC(Input5:'4') + 'X'
     C     Editcode_1    dsply
     C                   Eval      Editcode_1 = 'x'                + ' ' +
     C                                          %EDITC(Input6:'4') + 'X'
     C     Editcode_1    dsply
     *********************************************************************
     C                   Seton                                        LR
