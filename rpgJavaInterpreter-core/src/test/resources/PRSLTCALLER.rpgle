      * Call PRSLTCALLEE passing parameter $A
      * If a gets updated by PRSLTCALLEE, the new value must be
      * bound to the factor 1 of the param (*IN36)
     C                   CALL      'PRSLTCALLEE'
     C     *IN36         PARM      *OFF          $A                1

      * Test output
     C     $A            DSPLY
     C     *IN36         DSPLY