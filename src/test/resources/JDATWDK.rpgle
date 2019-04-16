      * Based on work by Claudio Neroni www.neroni.it
      *  Calls the date of week calculator pgm and outputs the result.
      *  The input parameter is a string like 'YYYYMMDD'
      *---------------------------------------------------------------------------------------------
     D des             s              3    dim(8) ctdata
     D ppdat           S              8
     D ppdatn          S              8  0
     C     *entry        plist
     C                   parm                    ppdat                          I Date YYYYMMDD
      *
     C                   Eval      ppdatn = %Dec(ppdat : 8 : 0)
     C                   call      'JDATWD'
     C                   parm                    ppdatn            8 0          I Date YYYYMMDD
     C                   parm                    ppwd              1 0          O Week day (1=sun...
     C                   clear                   dsp              50
     C                   eval      dsp='Date: '+ ppdat +
     C                                 ' WeekDay: '+ des(ppwd)
     C                   dsply                   dsp
     C                   seton                                        lr
**
sun
mon
tue
wed
thu
fri
sat
???
