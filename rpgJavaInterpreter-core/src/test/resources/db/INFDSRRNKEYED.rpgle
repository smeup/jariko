      * Regression: a keyed read on DefaultSQLDialect/HSQLDB has Result.rrn == null (reload
      * doesn't wrap a keyed file's FROM in the ROW_NUMBER() derived table there - see
      * rrn-output-support-reload.md). CHAIN must still succeed and must NOT crash writing to
      * INFDS - the RRN subfield is simply left at its previous (here: default/zero) value.
     FTestF     if   e           k disk    infds(dsinf1)
      * Record format for TestF:
      *  R TSTFMT
      *    KEYTST         5          COLHDG('KEY')
      *    DESTST        40          COLHDG('DESCRIPTION')
      *  K KEYTST
     D DSINF1          DS           400
     D XXNREU                397    400B 0
     D result          S             52    inz(*blanks)
     C     'ABCDE'       chain     TestF
     C                   if        %found
     c                   eval      result = 'Found'
     c                   else
     C                   eval      result = 'Not found'
     C                   endif
     C                   dsply                   result
     C                   dsply                   XXNREU
      * Closing resources.
     C                   seton                                        lr
