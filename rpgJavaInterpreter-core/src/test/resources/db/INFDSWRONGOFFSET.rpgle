      * Negative case: the DS named by INFDS declares a field at a byte offset other than the
      * one Relative Record Number subfield this (partial) INFDS implementation supports
      * (397-400) - must fail fast, at file-open time, rather than silently ignoring it.
     FTestF     if   e             disk    infds(dsinf1)
     D DSINF1          DS           400
     D BADFLD                 10     13B 0
     C                   seton                                        lr
