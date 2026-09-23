      * Verifies a CHAIN that finds no record (Result.rrn == null) leaves INFDS's Relative Record
      * Number subfield (byte offset 397-400) at the value the previous successful read wrote,
      * instead of crashing or overwriting it - see InterpreterCore.writeInfdsRrn.
     FTestF     if   e           k disk    infds(dsinf1)
      * Record format for TestF:
      *  R TSTFMT
      *    KEYTST         5          COLHDG('KEY')
      *    DESTST        40          COLHDG('DESCRIPTION')
      *  K KEYTST
     D DSINF1          DS           400
     D XXNREU                397    400B 0
     D result          S             52    inz(*blanks)
      * Found: XXNREU gets the row's RRN.
     C     'ABCDE'       chain     TestF
     C                   dsply                   XXNREU
      * Not found: XXNREU must keep that RRN.
     C     'ZZZZZ'       chain     TestF
     C                   if        %found
     c                   eval      result = 'Found'
     c                   else
     C                   eval      result = 'Not found'
     C                   endif
     C                   dsply                   result
     C                   dsply                   XXNREU
      * Closing resources.
     C                   seton                                        lr
