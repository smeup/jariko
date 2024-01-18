     D RES5_0          S              5P 0
     D RES5_1          S              5P 1
     D NUM5_0          S              5P 0
     D NUM5_1          S              5P 1
     D £DBG_Str        S             52
      *
     C     25            DIV       11            RES5_0
     C                   MVR                     NUM5_0
     C                   EVAL      £DBG_Str=%CHAR(NUM5_0)
      * Expected '3'
     C     £DBG_Str      DSPLY
      *
     C     25            DIV       11            RES5_0
     C                   MVR                     NUM5_1
     C                   EVAL      £DBG_Str=%CHAR(NUM5_1)
      * Expected '3.0'
     C     £DBG_Str      DSPLY
      *
     C     25            DIV       11            RES5_1
     C                   MVR                     NUM5_0
     C                   EVAL      £DBG_Str=%CHAR(NUM5_0)
      * Expected '0'
     C     £DBG_Str      DSPLY
      *
     C     25            DIV       11            RES5_1
     C                   MVR                     NUM5_1
     C                   EVAL      £DBG_Str=%CHAR(NUM5_1)
      * Expected '0.8'
     C     £DBG_Str      DSPLY
      *
     C     25.5          DIV       11.5          RES5_0
     C                   MVR                     NUM5_0
     C                   EVAL      £DBG_Str=%CHAR(NUM5_0)
      * Expected '2'
     C     £DBG_Str      DSPLY
      *
     C     25.5          DIV       11.5          RES5_0
     C                   MVR                     NUM5_1
     C                   EVAL      £DBG_Str=%CHAR(NUM5_1)
      * Expected '2.5'
     C     £DBG_Str      DSPLY
      *
     C     25.5          DIV       11.5          RES5_1
     C                   MVR                     NUM5_0
     C                   EVAL      £DBG_Str=%CHAR(NUM5_0)
      * Expected '0'
     C     £DBG_Str      DSPLY
      *
     C     25.5          DIV       11.5          RES5_1
     C                   MVR                     NUM5_1
     C                   EVAL      £DBG_Str=%CHAR(NUM5_1)
      * Expected '0.2'
     C     £DBG_Str      DSPLY
      *
     C                   SETON                                        LR