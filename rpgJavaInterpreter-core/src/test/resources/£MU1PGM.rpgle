     D* ==============================================================
     D* Purpose of this source is testing if we are able to create AST
     D* for source containing /COPY directive.
     D* Original source: MUTE14_01
     D* ==============================================================
      /COPY £MU1DSPEC
      *
     C                   EXSR      IMP0
      *
1    C                   EXSR      ESEMU1
      *
     C                   EXSR      FIN0
      *
     C                   SETON                                        LR
      /COPY £MU1CSPEC
      *--------------------------------------------------------------*
    RD* £MU1 execution
      *--------------------------------------------------------------*
     C     ESEMU1        BEGSR
      *
     C                   EVAL      £MU1I_FU='001'
     C                   EVAL      £MU1I_ME=*BLANKS
     C                   EXSR      £MU1
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Initial settings
      *--------------------------------------------------------------*
     C     IMP0          BEGSR
      *
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Final settings
      *--------------------------------------------------------------*
     C     FIN0          BEGSR
      *
      *
     C                   ENDSR
      *--------------------------------------------------------------*
