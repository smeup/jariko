      * Test definitions
     D £C6CIN          DS                                                       INPUT
     D  £C6CTP                       12                                         Tipo Oggetto
     D §DI             DS                  LIKEDS(£C6CIN)
     D XC6CTP          S                   LIKE(§DI.£C6CTP)

      * Test value initialization
     C                   EVAL      §DI.£C6CTP='ok'

      * LIKE on DS field with absolute path should be defined appropriately
     C                   EVAL      XC6CTP=§DI.£C6CTP

      * Test output
     C     XC6CTP        DSPLY