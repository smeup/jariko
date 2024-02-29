     D £DBG_Str        S             10
     D $VARIF          S                   LIKE(£VARIF)
     D $VARDOW         S                   LIKE(£VARDOW)
     D $VARSL          S                   LIKE(£VARSL)
     D $VARFOR         S                   LIKE(£VARFOR)
     D IND             S              1  0
      *
     C                   EXSR      SUB_001
     C                   EXSR      SUB_002
     C                   SETON                                          LR
      ****************************
     C     SUB_000       BEGSR
     C                   MOVEL(P)  ' '           $VARIF
     C                   MOVEL(P)  ' '           $VARDOW
     C                   MOVEL(P)  ' '           $VARSL
     C                   MOVEL(P)  ' '           $VARFOR
     C                   ENDSR
      ****************************
     C     SUB_001       BEGSR
     C                   MOVEL(P)  '1'           $VARIF
     C                   MOVEL(P)  '2'           $VARDOW
      * test IF
     C                   IF        '1' = '1'
     C                   MOVEL(P)  '1'           £VARIF           10
     C                   ENDIF
      * test DOW
     C                   EVAL      IND=1
     C     IND           DOWEQ     1
     C                   MOVEL(P)  '2'           £VARDOW          10
     C                   EVAL      IND=2
     C                   ENDDO
      *
     C                   ENDSR
      ****************************
     C     SUB_002       BEGSR
      *
     C                   MOVEL(P)  '3'           $VARSL
     C                   MOVEL(P)  '4'           $VARFOR
      * test SELECT
     C                   SELECT
     C                   WHEN      IND = 2
     C                   MOVEL(P)  '3'           £VARSL           10
     C                   OTHER
     C                   MOVEL(P)  ' '           £VARSL
     C                   ENDSL
      * test FOR
     C                   FOR       IND = 0 TO 1
     C                   MOVEL(P)  '4'           £VARFOR          10
     C                   ENDFOR
      ******
      *
     C                   EVAL      £DBG_Str=%trim($VARIF)+','+
     C                                      %trim($VARDOW)+','+
     C                                      %trim($VARSL)+','+
     C                                      %trim($VARFOR)
     C     £DBG_Str      DSPLY
     C                   ENDSR
      *
