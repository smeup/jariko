     D £G90DS          DS           512
     D  £G90TG                       20                                         Tag
     D £G90SO          S          30000    VARYING
     D £DBG_Str        S             2

     C     ':':'.'       XLATE     £G90TG        £G90TG
     C     ':':'.'       XLATE     £G90SO        £G90SO

     C                   EVAL      £DBG_Str='ok'
     C     £DBG_Str      DSPLY