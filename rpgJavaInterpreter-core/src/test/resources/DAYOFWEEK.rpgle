     D $TIME           S               Z
     D DayofWeek       S             10  0
      *
     C                   TIME                    $TIME
     C     $TIME         Subdur    D'1900-01-01' DayofWeek:*D
     C                   Div       7             DayOfWeek
     C                   Mvr                     DayOfWeek
     C*
     C                   Eval      DayOfWeek = DayOfWeek + 1
     C     DayOfWeek     DSPLY
      *
     C                   SETON                                        LR
