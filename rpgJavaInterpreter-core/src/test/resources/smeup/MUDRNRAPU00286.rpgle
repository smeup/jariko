     V* ==============================================================
     V* 26/03/2025 APU002 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * Perform open on F-spec with PRINTER
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, jariko crashed
     V* ==============================================================
     FPRT198    O    F  198        PRINTER OFLIND(*INOF) USROPN

     C                   OPEN      PRT198
     C     'ok'          DSPLY

     OPRT198    E            E1WRIT      1
     O                                              'Write  '                   COSTANTE
     O                       J1IDOG           +   1
     O                       J1TREC           +   1
     O                       J1ORIG           +   1
     O                       J1ASLA           +   1
     O                       J1ASUT           +   1
     O                       J1AMBI           +   1
     O                       J1WEUT           +   1
     O                       J1WEMS           +   1
     O                       J1WEFU           +   1
     O                       J1WEME           +   1
     O          E            E1UPDT      1
     O                                              'Update '                   COSTANTE
     O                       J1IDOG           +   1
     O                       J1TREC           +   1
     O                       J1ORIG           +   1
     O                       J1ASLA           +   1
     O                       J1ASUT           +   1
     O                       J1AMBI           +   1
     O                       J1WEUT           +   1
     O                       J1WEMS           +   1
     O                       J1WEFU           +   1
     O                       J1WEME           +   1
