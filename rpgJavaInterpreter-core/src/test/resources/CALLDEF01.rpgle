     C                   EXSR      IMP0
     C     G9MAIN        TAG
     C                   EXSR      FIN0
     C                   SETON                                        RT
      *--------------------------------------------------------------*
     C     IMP0          BEGSR
     C                   GOTO      G9MAIN
     C                   ENDSR
      *--------------------------------------------------------------*
     C     FIN0          BEGSR
     C                   MOVEL(P)  'XA'          £CRNA
     C                   MOVEL(P)  'XA'          £CRNB
     C                   EXSR      £CRN
     C                   DSPLY                   £CRNA
     C                   ENDSR
      *--------------------------------------------------------------*
     C     £CRN          BEGSR
      *
     C                   SELECT
     C     £CRNA         WHENEQ    'ZZ'
     C                   CALL      'CALLDEFV2'
     C                   PARM                    £CRNB             2
      *
     C                   OTHER
     C                   CALL      'CALLDEFV2'
     C                   PARM                    £CRNA             2
     C                   ENDSL
     C                   MOVEL(P)  'OK'          £CRNA
      *
     C     G9£CRN        ENDSR
