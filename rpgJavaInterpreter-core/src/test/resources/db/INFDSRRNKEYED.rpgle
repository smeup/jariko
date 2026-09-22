      * Verifies a CHAIN by KEY (on a keyed file) populates INFDS's Relative Record Number
      * subfield (byte offset 397-400): reload projects the table's __RNN as Result.rrn on keyed
      * reads too (see SQLDialect.rrnSelectExpression in reload).
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
