     D £DBG_Str        S             10
     C     *ENTRY        PLIST
     C                   PARM                    £G79FU           10
     C                   PARM                    £G79ME           10
     C                   PARM                    $G79FU           10
     C                   PARM                    $G79ME           10
      *
     C                   EVAL      £DBG_Str=%trim(£G79FU)+','+
     C                                      %trim(£G79ME)+','+
     C                                      %trim($G79FU)+','+
     C                                      %trim($G79ME)
      *
     C     £DBG_Str      DSPLY
      *
     C                   MOVEL(P)  '4'           £G79FU
     C                   MOVEL(P)  '3'           £G79ME
     C                   MOVEL(P)  '2'           $G79FU
     C                   MOVEL(P)  '1'           $G79ME
      *
     C                   SETON                                        LR