     DDigits           S             10    inz('0123456789')
     DDigitsDot        S             11    inz('0123456789.')
     DDigitsDotBlank   S             12    inz('0123456789. ')
     DSalary           S             15
     DN                S              2  0
     DMsg              S             50
     C                   eval      Salary = '.2000$'
     C     Digits        checkr    salary:5      N
     C                   if        %found
     C                   eval      Msg = 'Wrong char at ' + %char(N)
     C                   else
     C                   eval      Msg = 'No wrong chars ' + %char(N)
     C                   endif
     C     Msg           dsply
     ************************************************************************
     C     DigitsDot     checkr    salary:5      N
     C                   if        %found
     C                   eval      Msg = 'Wrong char at ' + %char(N)
     C                   else
     C                   eval      Msg = 'No wrong chars ' + %char(N)
     C                   endif
     C     Msg           dsply
     ************************************************************************
     C     DigitsDotBlankcheckr    salary:8      N
     C                   if        %found
     C                   eval      Msg = 'Wrong char at ' + %char(N)
     C                   else
     C                   eval      Msg = 'No wrong chars ' + %char(N)
     C                   endif
     C     Msg           dsply
     C                   SETON                                          LR
