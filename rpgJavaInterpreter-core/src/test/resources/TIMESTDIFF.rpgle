      **************************************************************************
      * Does a computation showing elapsed time in microseconds
      **************************************************************************
     Dcount            s             15  0
     Dx                S             15  0 inz(0)
      **************************************************************************
     Dstart            S               z
     Delapsed          S             15  0
     C                   eval      start = %timestamp()
     C                   exsr      computation
     C                   eval      elapsed = %DIFF(%timestamp():start:*MSECONDS)
     C                   dsply                   elapsed
     C                   seton                                        lr
      **************************************************************************
     c     computation   begsr
     C                   FOR       count = 1 TO 100000000
     C                   eval      x = x + 1 * 31
     c                   endfor
     c                   endsr
