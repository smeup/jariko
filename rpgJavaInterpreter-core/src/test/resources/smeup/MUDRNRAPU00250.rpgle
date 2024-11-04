      * INZ on fields contained in a DS with OCCURS keyword should be supported and initialize the field
     DT$IM             DS                  OCCURS(5000)
     D T$IMPO                        17  2 DIM(40) INZ(0)                                                Importi

      * Test output
     C     T$IMPO        DSPLY