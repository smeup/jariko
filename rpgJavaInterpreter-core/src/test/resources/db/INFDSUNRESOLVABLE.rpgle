      * Negative case: INFDS names a DS that isn't declared anywhere - must fail fast, at
      * file-open time (DBFileMap.add), not with a crash on first read.
     FTestF     if   e             disk    infds(nosuchds)
     D result          S             52    inz(*blanks)
     C                   seton                                        lr
