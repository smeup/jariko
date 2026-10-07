      * Regression for C5C6M0 (see UpdateRenamedFirstDBTest).
      *
      * Renamed and Plain are two files whose record format has the same native name, TSTFMT. In RPG
      * IV two files cannot share a format name in the same program, so Renamed gives its format a
      * new name, TSTFM2. From then on TSTFMT no longer designates Renamed's format: it can only mean
      * Plain's. Renamed is deliberately declared FIRST.
      *
      * The record is read through Plain (CHAIN by file name) and written back with UPDATE TSTFMT
      * (by format name). UPDATE must act on Plain, the file that has a current record. If TSTFMT
      * wrongly resolved to Renamed (which was never read) it would fail with "Positioning required
      * before update".
     FRenamed   uf   e           k disk    rename(TSTFMT:TSTFM2)
     FPlain     uf   e           k disk
     C     'ABCDE'       chain     Plain
     C                   eval      DESTST = 'Updated'
     C                   update    TSTFMT
     C     'ABCDE'       chain     Plain
     C                   dsply                   DESTST
      * Closing resources.
     C                   seton                                        lr
