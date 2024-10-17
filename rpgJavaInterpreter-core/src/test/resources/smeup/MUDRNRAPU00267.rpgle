      * Test declarations
     D                 DS
     D  £RITEL                 1     15

      * Declaration with DIM based on a constant whose definition is not known yet
     D CDOR            S              2    DIM(Q£DOC)
     D Q£DOC           C                   CONST(500)

      * Values setup
     C                   Z-ADD     1             J                 4 0
     C                   EVAL  £RITEL = 'ok'

      * Assign value to the definition being tested
     C                   MOVEL     £RITEL        CDOR(J)

      * Test output
     C     CDOR(J)       DSPLY