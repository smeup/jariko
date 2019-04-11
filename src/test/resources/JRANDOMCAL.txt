      * Adapted from neroni.it
      *
      * How to generate a random number between 1 and 193
      * CALL PGM(JRANDOMCAL) PARM(X'193F')
      *
     D max             s              3p 0
     D out             s              3p 0
     C     *entry        plist
     C                   parm                    max               3 0          I Max nr.
     C                   call      'JRANDOMA'
     C                   parm                    max
     C                   parm                    out
     C                   clear                   dsp              50
     C                   eval      dsp='Random nr:' +  %Char(out)
     C                   dsply                   dsp
     C                   seton                                        lr
