     D £DBG_Pas        S              3
     D £DBG_Str        S            100
     D                 DS
     D SPAG                          50    DIM(2)
     D SPAG_CD                       15    OVERLAY(SPAG:01)
     D SPAG_DS                       35    OVERLAY(SPAG:*NEXT)
     D $PA                            5  0
     D NPA                            5  0
      *
     C                   EXSR      SEZ_T12_A03
     C                   SETON                                        LR
      *
      *---------------------------------------------------------------
    RD* Errori programma MULANGT12 sezione A03
      *--------------------------------------------------------------*
     C     SEZ_T12_A03   BEGSR
    OA* A£.CDOP(FOR;DS)
     D* ciclo FOR con Lettura con indice e limite da DS
     C                   EVAL      £DBG_Pas='P06'
     C                   EVAL      £DBG_Str='LOOP: '
     C                   EVAL      $PA=4
     C                   CLEAR                   SPAG
      *
     C                   FOR       NPA=1 To $PA
     C                   IF        NPA>1
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+','
     C                   ENDIF
     C                   EVAL      £DBG_Str=%TRIM(£DBG_Str)+%char(NPA)
     C                   ENDFOR
      *
     C     £DBG_Str      DSPLY
      *
     C                   ENDSR