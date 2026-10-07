      * A RENAME'd F-spec (Renamed, rename(TSTFMT:TSTFM2)) is declared BEFORE the F-spec that really
      * owns the format name TSTFMT (Plain, no RENAME). The record is read through Plain (by file name)
      * and written back with UPDATE TSTFMT (by format name): it must resolve to Plain, not to Renamed,
      * which only has TSTFMT as the record's native name and has no current record (production case).
     FRenamed   uf   e           k disk    rename(TSTFMT:TSTFM2)
     FPlain     uf   e           k disk
     C     'ABCDE'       chain     Plain
     C                   eval      DESTST = 'Updated'
     C                   update    TSTFMT
     C     'ABCDE'       chain     Plain
     C                   dsply                   DESTST
      * Closing resources.
     C                   seton                                        lr
