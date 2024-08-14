     D £DBG_Str        S             2

     C     'C5C5L0'      CAT(P)    £C5LLC        £GGPNP
     C     *LIKE         DEFINE    £C5L36        £C5LLC
      *
     C                   IF        1
     C                   CALL      £GGPNP                               37
     C                   PARM                    £C5L36            1
     C                   ELSE
     C                   CALL      £GGPNP
     C                   PARM                    £C5L36            1
      *
     C                   ENDIF
      *
     C                   CALL      'DUMMY00245'
     C                   PARM                    £GGPNP           10

     C                   EVAL      £DBG_Str='ok'
     C     £DBG_Str      DSPLY