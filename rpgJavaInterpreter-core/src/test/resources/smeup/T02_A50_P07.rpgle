     D £DBG_Str        S             10
     D $G79FU          S                   LIKE(£G79FU)
     D $G79ME          S                   LIKE(£G79ME)
      *
     C                   MOVEL(P)  '1'           £G79FU
     C                   MOVEL(P)  '2'           £G79ME
     C                   MOVEL(P)  '3'           $G79FU
     C                   MOVEL(P)  '4'           $G79ME
      *
     C                   MOVEL(P)  'T02_A50_P07B'AAA010           10
     C                   IF        '1' = '1'
     C                   CALL      AAA010                               37
     C                   PARM                    £G79FU           10
     C                   PARM                    £G79ME           10
     C                   PARM                    $G79FU
     C                   PARM                    $G79ME
     C                   ENDIF
      *
     C                   EVAL      £DBG_Str=%trim(£G79FU)+','+
     C                                      %trim(£G79ME)+','+
     C                                      %trim($G79FU)+','+
     C                                      %trim($G79ME)
     C     £DBG_Str      DSPLY
      *
     C                   SETON                                          LR