      * UnlimitedStringType test for DS fields
      * D spec definitions ****************************************************************
     D Msg             S             50

      * DS1 definition ************************************
     D DS1             DS            70
      * StringType
     D Msg1                          50    inz('Msg1')
     D Msg2                          20    inz('Msg2')
      * UnlimitedStringType
     D Unlimit                         0
      * Initialized UnlimitedStringType
     D UnlInited                       0   inz('UnlInited')
      * Unlimited to test blank
     D UnlBlank                        0
      *****************************************************

      * DS2 definition like DS1 ***************************
     D DS2             DS                  LIKEDS(DS1)
      *****************************************************

      * Initialization tests ***************************************************************
     C                   Dsply                   DS1.Unlimit
     C                   Dsply                   DS1.UnlInited
     C                   Dsply                   DS2.Unlimit
     C                   Dsply                   DS2.UnlInited
      **************************************************************************************

      * Change DS1 and DS2 field values ****************************************************
     C                   Eval      DS1.Msg1 = 'DS1.Msg1'
     C                   Dsply                   DS1.Msg1
     
     C                   Eval      DS1.Unlimit = 'DS1.Unlimit'
     C                   Dsply                   DS1.Unlimit
     
     C                   Eval      DS2.Msg1 = 'DS2.Msg1'
     C                   Dsply                   DS2.Msg1
     
     C                   Eval      DS2.Unlimit = 'DS2.Unlimit'
     C                   Dsply                   DS2.Unlimit     
     
     C                   If        DS1 <> DS2
     C                   Eval      Msg = 'DS1 <> DS2'
     C                   Dsply                   Msg
     C                   EndIf
      **************************************************************************************

      * Assign DS1 to DS2 ******************************************************************
     C                   Eval      DS1.Msg1 = 'Assign DS1 to DS2 - DS1.Msg1'
     C                   Eval      DS1.Unlimit = 'Assign DS1 to DS2 - DS1.Unlimit'
     C                   Eval      DS2 = DS1

     C                   If        DS1.Msg1 = DS2.Msg1
     C                   Eval      Msg = 'DS1.Msg1 content = DS2.Msg content'
     C                   Dsply                   Msg
     C                   EndIf

     C                   If        DS1.Unlimit = DS2.Unlimit
     C                   Eval      Msg = 'DS1.Unlimit content = DS2.Unlimit content'
     C                   Dsply                   Msg
     C                   EndIf

     C                   If        DS1 = DS1
     C                   Eval      Msg = 'DS1 = DS2'
     C                   Dsply                   Msg
     C                   EndIf
      *****************************************************************************************

      * Compare unlimited with literal ********************************************************
     C                   Eval      DS1.Unlimit = 'Compare unlimited with literal'
     C                   If        DS1.Unlimit = 'Compare unlimited with literal'
     C                   Eval      Msg = 'Compare unlimited with literal'
     C                   Dsply                   Msg
     C                   EndIf
      *****************************************************************************************

      * Compare unlimited with limited ********************************************************
     C                   Eval      DS1.Msg1 = 'Compare unlimited with limited'
     C                   Eval      DS1.Unlimit = 'Compare unlimited with limited'
     C                   If        DS1.Unlimit = DS1.Msg1
     C                   Eval      Msg = 'Compare unlimited with limited'
     C                   Dsply                   Msg
     C                   EndIf
      *****************************************************************************************

      * Compare uninitialized unlimited with *BLANKS  *****************************************
     C                   If        DS1.UnlBlank = *BLANKS
     C                   Eval      Msg = 'Compare uninitialized unlimited with *BLANKS'
     C                   Dsply                   Msg
     C                   EndIf
      *****************************************************************************************


      * Reset an unlimited and compare with *BLANKS  ******************************************
     C                   Eval      DS1.Unlimit = *BLANKS
     C                   If        DS1.Unlimit = *BLANKS
     C                   Eval      Msg = 'Reset an unlimited and compare with *BLANKS'
     C                   Dsply                   Msg
     C                   EndIf
      *****************************************************************************************

      * Assignment from BooleanType to UnlimitedStringType in a DS field  *********************
     C                   Eval      DS1.Unlimit = *ON
     C                   If        DS1.Unlimit = '1'
     C                   Eval      Msg = 'Assignment from a boolean'
     C                   Dsply                   Msg
     C                   EndIf
      *****************************************************************************************


