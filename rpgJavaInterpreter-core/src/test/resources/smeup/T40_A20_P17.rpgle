     D NNN             S              6  0 INZ(100000)
     D £DBG_Str        S           2560
     D £DBG_Pas        S             10
     D A20_DIM_P01     S              4    DIM(200)
     D A20_DIM_P04     S              4    DIM(200)


    OA* A£.CDOP(MOVEA  )
     D* MOVEA(P) 100.000 volte
     C                   EVAL      £DBG_Pas='P17'
     C                   DO        NNN
     C                   EXSR      SUB_A20_D
     C                   ENDDO
     C                   EVAL      £DBG_Str=%TRIMR(A20_DIM_P01(1))
      *


     C     SUB_A20_D     BEGSR
     C                   MOVEA(P)  A20_DIM_P01   A20_DIM_P04
     C                   ENDSR