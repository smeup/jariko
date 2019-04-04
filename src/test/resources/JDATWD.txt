      /TITLE Date calc. Week day.
      * Based on work by Claudio Neroni www.neroni.it
      *    Computes the day of week.
      *---------------------------------------------------------------------------------------------
      * Base date (a Saturday).
     D rif             s               d   inz(d'1898-12-24')
      * Work date.
     D dat             s               d
      *---------------------------------------------------------------------------------------------
      * Swaps parameters.
     C     *entry        plist
      *    Gets a numeric date in YYYYMMDD form.
     C                   parm                    ppdat             8 0          I Date YYYYMMDD
      *    Returns day of week 1=sun, 2=mon, ..., 7=sat, 8=err.
     C                   parm      wd            ppwd              1 0          O Week day (1=sun...
      * Computes day of week.
     C                   exsr      srwd
      * Retuns not terminating.
     C                   return
      *---------------------------------------------------------------------------------------------
      * Computes day of week.
     C     srwd          begsr
      * Cleans return value
     C                   clear                   wd                1 0
      * Cleans distance in days between ref date and actual date.
     C                   clear                   dif              15 0
      * Converts the number in date format.
     C                   eval      dat = %date(ppdat:*iso)
      * Computes days between two dates.
     C                   eval      dif = %diff(dat:rif:*days)
      * We are not interested in dates before the ref date: returns error.
     C                   if        dif < *zero
     C                   eval      wd = 8
     C                   leavesr
     C                   endif
      * Distance modulo 7
     C                   eval      wd = %rem(dif:7)
      * Normalized as a number between 1 and 7.
     C                   if        wd < 1
     C                   eval      wd = wd + 7
     C                   endif
     C                   endsr
      *---------------------------------------------------------------------------------------------
