      * Search a record with a key composed by 2 fields
     FMyFile2   if   e           k disk
      ***************************************************
      *                              UNIQUE
      *  R TS2REC
      *
      *    KY1TST         5          COLHDG('KEY 1')
      *    KY2TST         2  0       COLHDG('KEY 2')
      *    DESTST        40          COLHDG('DESCRIPTION')
      *
      *  K KY1TST
      *  K KY2TST
      ***************************************************
     D toFind1         S              5
     D toFind2         S              2
     D result          S             52    inz(*blanks)
      *
     C     Key           KLIST
     C                   KFLD                    KY1TST
     C                   KFLD                    KY2TST
     C     *entry        plist
     C                   parm                    toFind1
     C                   parm                    toFind2
     C                   Eval      KY1TST = toFind1
     C                   Eval      KY2TST = %dec(toFind2:2:0)
      *
     C     Key           chain     MyFile2
     C                   if        %found
     c                   eval      result = 'Found: ' + DESTST
     c                   else
     C                   eval      result = 'Not found'
     C                   endif
     C                   dsply                   result
      * Closing resources.
     C                   seton                                        lr
