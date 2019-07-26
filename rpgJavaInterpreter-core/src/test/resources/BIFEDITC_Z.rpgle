     DInput1           S              7  0 Inz(0123456)
     DInput2           S              7  0 Inz(-0123456)
     DInput3           S              7  2 Inz(01234.56)
     DInput4           S              7  2 Inz(-01234.56)
     DInput5           S              7  2 Inz(00000.00)

     DEditcode_1       S             52A
     *********************************************************************
     C* Edit code "Z" to format numeric value
     C
     C                   Eval      Editcode_1 = 'x'                + ' ' +
     C                                          %EDITC(Input1:'Z') + ' ' +
     C                                          %EDITC(Input2:'Z') + ' ' +
     C                                          %EDITC(Input3:'Z') + ' ' +
     C                                          %EDITC(Input4:'Z') + ' ' +
     C                                          %EDITC(Input5:'Z') + 'X'
     C     Editcode_1    dsply
     *********************************************************************
     C                   Seton                                        LR
