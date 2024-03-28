     D £DBG_Str        S             52
     D $OP             S              1N   INZ(*ON)

     D* Passo mock di EXFMT
     C                   EVAL      £DBG_Str=''
     C                   IF        $OP=*OFF
     C                   OPEN      MLNGT60V
     C                   ENDIF
     C                   IF        $OP=*OFF
     C                   EXFMT     FMT01
     C                   EVAL      £DBG_Str='Eseguito EXFMT di FMT01'
     C                   ENDIF
     C                   IF        $OP=*OFF
     C                   CLOSE     MLNGT60V
     C                   ENDIF

     D* Passo mock di READC
     C                   EVAL      £DBG_Str=''
     C                   IF        $OP=*OFF
     C                   OPEN      MLNGT60V
     C                   ENDIF
     C                   IF        $OP=*OFF
     C                   READC     SFL01
     C                   EVAL      £DBG_Str='Eseguito READC di subfile SFLR'
     C                   ENDIF
     C                   IF        $OP=*OFF
     C                   CLOSE     MLNGT60V
     C                   ENDIF
     C                   SETON                                        LR