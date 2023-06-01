      * UnlimitedStringType test for standalone fields
      * D spec definitions ****************************************************************
      * StringType
     D Msg             S             50

      * UnlimitedStringType
     D Unlimit         S               0

      * Initialized UnlimitedStringType
     D UnlInited       S               0   inz('UnlInited')
      **************************************************************************************

      * Initialization tests ***************************************************************
     C                   dsply                   Unlimit
     C                   dsply                   UnlInited
      **************************************************************************************

      * Assignment by string literal *******************************************************
     C                   Eval      Unlimit = 'Assignment by string literal'
     C                   dsply                   Unlimit
      **************************************************************************************

      * Assignment by reference of the same type *******************************************
     C                   Eval      UnlInited = 'Assignment by reference of the same type'
     C                   Eval      Unlimit = UnlInited
     C                   dsply                   Unlimit
      **************************************************************************************

      * Assignment from StringType to UnlimitedStringType ***********************************
     C                   Eval      Msg = 'Assignment from StringType to UnlimitedStringType'
     C                   Eval      Unlimit = Msg
     C                   dsply                   Unlimit
      **************************************************************************************

      * Assignment from UnlimitedStringType to StringType ***********************************
     C                   Eval      Unlimit = 'Assignment from StringType to UnlimitedStringType'
     C                   Eval      Msg = Unlimit
     C                   dsply                   Msg
      **************************************************************************************

      * Concat literal A with literal B ****************************************************
     C                   Eval      Unlimit = 'Concat literal A ' + 'with literal B'
     C                   dsply                   Unlimit
      **************************************************************************************

      * Test blank support *****************************************************************
     C                   Eval      Unlimit = *BLANK
     C                   Eval      Unlimit = Unlimit + 'ok blank'
     C                   dsply                   Unlimit
      **************************************************************************************

      * Concat UnlimitedStringType with StringType *****************************************
     C                   Eval      Unlimit = 'Concat UnlimitedStringType '
     C                   Eval      Msg = 'with StringType'
     C                   Eval      UnlInited = Unlimit + Msg
     C                   dsply                   UnlInited
      **************************************************************************************

      * Concat StringType with UnlimitedStringType *****************************************
     C                   Eval      Msg = 'Concat StringType '
     C                   Eval      Unlimit = 'with UnlimitedStringType'
     C                   Eval      UnlInited = Msg + Unlimit
     C                   dsply                   UnlInited
      **************************************************************************************


