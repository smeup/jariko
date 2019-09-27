      * ------------------------------------------------------------------
     D Count           S              1  0 INZ(1)
     D N               S              1  0
     C     *entry        plist
     C                   parm                    inN               1
     C                   eval      n = %dec(inN:1:0)
     C                   dou       count > n
     C     Count         dsply
     C                   eval      count += 1
     C                   enddo
     C                   seton                                        LR
