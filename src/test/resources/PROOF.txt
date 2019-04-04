     H DFTNAME(PROOF)
     FQSYSPRT   O    F  132        PRINTER

     D ARRAY           S              8    DIM(3)
     D                                     CTDATA PERRCD(1)
     D INSTALL         C                   CONST('INSTALLATION')
     D DS              DS
     D   OK                    1      8    INZ('VERIFIED')

     C                   EVAL      OK = 'FAILED'
     C                   MOVE      ARRAY(2)      ARRAY(3)
     C                   RESET                   OK
     C                   SETON                                        LR
     OQSYSPRT   T    LR                     1  1
     O                       INSTALL             21
     O                                           24 'OF'
     O                                           28 'THE'
     O                                           36 'ILE RPG'
     O                                           45 'COMPILER'
     O                                          + 1 'IS'
     O                       OK                 + 1
**
COMPILE
TIME
ARRAY
