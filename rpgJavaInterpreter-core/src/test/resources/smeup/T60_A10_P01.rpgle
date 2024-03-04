     D £DBG_Str        S            250
     D $OP             S              1N   INZ(*ON)

     D* Test sugli indicatori delle chiavi KA-KY (F01-F24)
     C                   EVAL      £DBG_Str=''
     C                   IF        $OP=*OFF
     C                   OPEN      MLNGT60V
     C                   ENDIF
     C   KA              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KA(1) '
     C  NKA              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KA(0) '
     C   KB              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KB(1) '
     C  NKB              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KB(0) '
     C   KC              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KC(1) '
     C  NKC              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KC(0) '
     C   KD              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KD(1) '
     C  NKD              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KD(0) '
     C   KE              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KE(1) '
     C  NKE              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KE(0) '
     C   KF              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KF(1) '
     C  NKF              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KF(0) '
     C   KG              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KG(1) '
     C  NKG              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KG(0) '
     C   KH              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KH(1) '
     C  NKH              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KH(0) '
     C   KI              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KI(1) '
     C  NKI              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KI(0) '
     C   KJ              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KJ(1) '
     C  NKJ              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KJ(0) '
     C   KK              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KK(1) '
     C  NKK              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KK(0) '
     C   KL              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KL(1) '
     C  NKL              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KL(0) '
     C   KM              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KM(1) '
     C  NKM              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KM(0) '
     C   KN              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KN(1) '
     C  NKN              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KN(0) '
     C   KP              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KP(1) '
     C  NKP              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KP(0) '
     C   KQ              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KQ(1) '
     C  NKQ              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KQ(0) '
     C   KR              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KR(1) '
     C  NKR              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KR(0) '
     C   KS              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KS(1) '
     C  NKS              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KS(0) '
     C   KT              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KT(1) '
     C  NKT              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KT(0) '
     C   KU              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KU(1) '
     C  NKU              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KU(0) '
     C   KV              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KV(1) '
     C  NKV              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KV(0) '
     C   KW              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KW(1) '
     C  NKW              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KW(0) '
     C   KX              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KX(1) '
     C  NKX              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KX(0) '
     C   KY              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KY(1)'
     C  NKY              EVAL      £DBG_Str=%TRIM(£DBG_Str)+
     C                                  'KY(0)'
     C                   IF        $OP=*OFF
     C                   CLOSE     MLNGT60V
     C                   ENDIF
     C     £DBG_Str      DSPLY