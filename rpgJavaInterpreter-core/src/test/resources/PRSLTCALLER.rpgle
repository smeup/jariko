      * Helper declarations
     D £DBG_Str        S           512

      * Call PRSLTCALLEE passing parameter $A
      * If a gets updated by PRSLTCALLEE, the new value must be
      * bound to the factor 1 of the param (*IN36)
     C                   CALL      'PRSLTCALLEE'
     C     *IN35         PARM      *OFF          $A                1            Should be updated
     C                   PARM      *OFF          $B                1            Should not be updated
     C                   PARM      *OFF          $C                1            Shoud have no influence

      * Updated value output
     C                   EVAL      £DBG_Str = '('+$A+','+*IN35+')'
     C     £DBG_Str      DSPLY
      * Not updated value output
     C                   EVAL      £DBG_Str = '('+$B+','+*IN36+')'
     C     £DBG_Str      DSPLY
      * No influence output
     C                   EVAL      £DBG_Str = '('+$A+','+*IN35+','+$B+','+
     C                                    *IN36+','+$C+')'
     C     £DBG_Str      DSPLY
