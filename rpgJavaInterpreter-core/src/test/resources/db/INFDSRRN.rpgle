      * Verifies CHAIN (by RRN, on an unkeyed file) populates INFDS's Relative Record Number
      * subfield (byte offset 397-400) with reload's Result.rrn. See docs/plans/
      * infds-rrn-support-jariko.md (companion smeuperp workspace) and .../DBFileMap.kt.
     FTestF     if   e             disk    infds(dsinf1)
      * Record format for TestF:
      *  R TSTFMT
      *    KEYTST         5          COLHDG('KEY')
      *    DESTST        40          COLHDG('DESCRIPTION')
      ***************************************************
     D DSINF1          DS           400
     D XXNREU                397    400B 0
     D rrn             S              5  0
     D result          S             52    inz(*blanks)
     C     *entry        plist
     C                   parm                    rrn
      * Unkeyed file: CHAIN by Relative Record Number.
     C     rrn           chain     TestF
     C                   if        %found
     c                   eval      result = 'Found'
     c                   else
     C                   eval      result = 'Not found'
     C                   endif
     C                   dsply                   result
     C                   dsply                   XXNREU
      * Closing resources.
     C                   seton                                        lr
