      * Two calls of the same program in the same execution: the file state (CHAIN) of the first call
      * must not leak into the second one (program ended with LR, so its files are closed).
     D OP              S              1
      *
     C                   EVAL      OP = 'C'
     C                   CALL      'db/RDAFTCHC'
     C                   PARM                    OP
     C                   EVAL      OP = 'R'
     C                   CALL      'db/RDAFTCHC'
     C                   PARM                    OP
     C                   SETON                                        LR
