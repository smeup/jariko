      * Helper declarations
     D £DBG_Str        S           512

      * A variant of PRSLTCALLER binding the same identifier (*IN35) to parameters $A and $B
      * Assignment should happen sequentially from first to last on call exit

      * Only update $A
     C                   CALL      'PRSLTCALLEE'
     C     *IN35         PARM      *OFF          $A                1            Should be updated
     C     *IN35         PARM      *OFF          $B                1            Should not be updated
     C                   PARM      *OFF          $C                1            Should have no influence

      * $A is the last value updated output
     C     *IN35         DSPLY

      * Update $A then $B
     C                   CALL      'PRSLTCALLEE2'
     C     *IN35         PARM      *OFF          $A                1            Should be updated
     C     *IN35         PARM      *OFF          $B                1            Should be updated
     C                   PARM      *OFF          $C                1            Should have no influence

      * $B is the last value updated output
     C     *IN35         DSPLY

      * Update $A then $B then $A again
     C                   CALL      'PRSLTCALLEE3'
     C     *IN35         PARM      *OFF          $A                1            Should be updated
     C     *IN35         PARM      *OFF          $B                1            Should be updated
     C                   PARM      *OFF          $C                1            Should have no influence

      * Mixed update test
     C     *IN35         DSPLY

      * Update $A then $B then $A again without duplicate binding
     C                   CALL      'PRSLTCALLEE3'
     C     *IN35         PARM      *OFF          $A                1            Should be updated
     C                   PARM      *OFF          $B                1            Should be updated
     C                   PARM      *OFF          $C                1            Should have no influence

      * Mixed update test
     C     *IN35         DSPLY