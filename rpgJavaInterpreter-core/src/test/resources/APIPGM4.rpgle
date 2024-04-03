      * Test multiple api inclusion
      * SUBROUTINE is already defined in APISR but does not must cause a conflict
     C     SUBROUTINE    BEGSR
     C     'HI, FRIEND'  DSPLY
     C                   ENDSR
      *
       /API APISR
      *
     C                   EXSR      SUBROUTINE
     C                   SETON                                        LR