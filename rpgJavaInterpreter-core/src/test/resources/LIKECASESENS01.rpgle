     D ARR1            S            10     DIM(300)
      * the argument passed to LIKE is handled in case sensitive way
     D A150            S                   LIKE(arr1)
      *
      * data reference A150 not resolved is caused by the previous mishandling
     C                   EVAL      A150(1) = 'hello'
     C     A150(1)       DSPLY
