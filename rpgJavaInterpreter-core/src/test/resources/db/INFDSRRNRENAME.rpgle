      * Verifies INFDS resolution is per-F-spec, not per-shared-RENAME-format: two F-specs
      * (Unkeyed, natively TSTFMT; Keyed, rename(TSTFMT:TSTFM2)) share one record format via
      * RENAME, but each declares its OWN INFDS and must resolve to its OWN DS independently.
     FUnkeyed   if   e             disk    infds(dsinf1)
     FKeyed     if   e           k disk    rename(TSTFMT:TSTFM2) infds(dsinf2)
      * Record format for both files:
      *  R TSTFMT
      *    KEYTST         5
      *    DESTST        40
      *  K KEYTST                                    (Keyed only)
     D DSINF1          DS           400
     D XXNREU1               397    400B 0
     D DSINF2          DS           400
     D XXNREU2               397    400B 0
     D rrn             S              5  0
     C     *entry        plist
     C                   parm                    rrn
      * Unkeyed: CHAIN by Relative Record Number, gets a real Result.rrn on HSQLDB.
     C     rrn           chain     Unkeyed
      * Keyed: CHAIN by key, Result.rrn is null on HSQLDB - XXNREU2 must stay at its default.
     C     'ABCDE'       chain     Keyed
     C                   dsply                   XXNREU1
     C                   dsply                   XXNREU2
      * Closing resources.
     C                   seton                                        lr
