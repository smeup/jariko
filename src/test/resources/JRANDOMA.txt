      * Adapted from www.neroni.it
      * Calls API for random numbers
     D seed            s              4b 0
     D random_f        s              8f
     D random_s        s             13p 0
     D quoziente       s             13p 0
     D fc              s             12a
     D max             s              3p 0
     D out             s              3p 0
     D out4            s              4p 0
     D cnt             s              4b 0
     D dd              s              1a
     C     *entry        plist
     C                   parm                    max                            I Max value
     C                   parm                    out                            O Output random
     C                   if        max=*zero
     C                   seton                                        lr
     C                   return
     C                   endif
     C                   callb     'CEERAN0'
     C                   parm                    seed                           U Initial
     C                   parm                    random_f                       O Random number
     C                   parm                    fc                             O Feedback
     C                   eval      random_s=random_f*10**13
     C                   move      random_s      out
     C     out           div       max           quoziente
     C                   mvr                     out
     C                   add       1             out
     C                   add       out           seed
     C                   add       1             cnt
     C                   add       cnt           seed
     C                   return
