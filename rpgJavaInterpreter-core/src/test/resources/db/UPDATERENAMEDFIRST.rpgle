      * A RENAME'd F-spec (Renamed, rename(TSTFMT:TSTFM2)) is declared BEFORE the F-spec that really
      * owns the format name TSTFMT (Plain, no RENAME). A bare TSTFMT must resolve to Plain, not to
      * Renamed, which only has TSTFMT as the record's native name (UPDATE TSTFMT hit this in
      * production: it picked Renamed, which had no current record).
     FRenamed   if   e           k disk    rename(TSTFMT:TSTFM2)
     FPlain     if   e           k disk
     C     'ABCDE'       chain     TSTFMT
     C                   dsply                   DESTST
      * Closing resources.
     C                   seton                                        lr
