      * Test multiple api inclusion
      * GREETING is defined in APIMULTIPLEDEF but does not must cause a conflict
      * But, PROCNAME and SUBGREETING are defined only on APIMULTIPLEDEF
     D GREETING        S             50          VARYING

     DPROCNAME         PR
     Dn                              50    CONST VARYING

      *
       /API APIMULTIPLEDEF
      *
     C                   EXSR      SUBGREETING
     C                   EVAL      GREETING = 'General Kenobi'
     C                   CALLP     PROCNAME(GREETING)
     C                   SETON                                        LR