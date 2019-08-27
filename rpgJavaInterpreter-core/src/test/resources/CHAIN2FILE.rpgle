      * Search a record in 2 files with the same
      * record format
     FFirst     if   e           k disk
     FSecond    if   e           k disk    rename(TSTREC:TSTREC2)
      * Record format for both files:
      *                              UNIQUE
      *  R TSTREC
      *
      *    KEYTST         5          COLHDG('KEY')
      *    DESTST        40          COLHDG('DESCRIPTION')
      *    NBRTST         2  0       COLHDG('NUMBER')
      *
      *  K KEYTST
      ***************************************************
     D toFind          S              5
     D result          S             52    inz(*blanks)
      *
     C     *entry        plist
     C                   parm                    toFind
      * Reads records from first file
     C     toFind        chain     First
     C                   if        %found
     c                   eval      result = '1st: ' + DESTST
     c                   else
     C                   eval      result = 'Not found in First'
     C                   endif
     C                   dsply                   result
      * Reads records from second file
     C     toFind        chain     Second
     C                   if        %found
     c                   eval      result = '2nd: ' + DESTST
     c                   else
     C                   eval      result = 'Not found in Second'
     C                   endif
     C                   dsply                   result
      * Closing resources.
     C                   seton                                        lr
