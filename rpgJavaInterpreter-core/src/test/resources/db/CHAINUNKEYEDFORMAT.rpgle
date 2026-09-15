      * Reproduces the C5RATER regression: an unkeyed F-spec with no RENAME shares its native
      * record format with a keyed F-spec that RENAMEs its own format to it. A bare-format-name
      * CHAIN must resolve deterministically to the unkeyed file (the first F-spec declaring that
      * format) and be treated as a Relative Record Number chain, since it has no access fields.
     FUnkeyed   if   e             disk
     FKeyed     if   e           k disk    rename(TSTFMT:TSTFM2)
      * Record format for both files:
      *                              UNIQUE
      *  R TSTFMT
      *    KEYTST         5          COLHDG('KEY')
      *    DESTST        40          COLHDG('DESCRIPTION')
      *  K KEYTST                                    (Keyed only)
      ***************************************************
     D rrn             S              5  0
     D result          S             52    inz(*blanks)
      *
     C     *entry        plist
     C                   parm                    rrn
      * Bare format name CHAIN: must resolve to Unkeyed (first-declared F-spec for TSTFMT)
      * and succeed as an RRN chain, since Unkeyed has no access fields.
     C     rrn           chain     TSTFMT
     C                   if        %found
     c                   eval      result = 'Found: ' + DESTST
     c                   else
     C                   eval      result = 'Not found'
     C                   endif
     C                   dsply                   result
      * Closing resources.
     C                   seton                                        lr
