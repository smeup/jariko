      *---------------------------------------------------------------
     D ADD_CALLP       PR
     D A1                            10  2
     D A2                            10  2
     D A_RETURN                      10  2
      *---------------------------------------------------------------
     D MULT_CALLP      PR
     D M1                            10  2
     D M2                            10  2
     D M_RETURN                      10  2
      *---------------------------------------------------------------
     D CONCAT_CALLP    PR
     D C1                            10
     D C2                            10
     D C_RETURN                      20
      *---------------------------------------------------------------
     D TOTAL_CALLP     PR
     D T1                            10  2 DIM(10)
     D T2                            10  2 DIM(10)
     D T_RETURN                      10  2 DIM(10)
      *---------------------------------------------------------------
     D NEGATE_CALLP    PR
     D N1                              N
     D N_RETURN                        N
      *---------------------------------------------------------------
     D ADD_EVAL        PR            10  2
     D A1                            10  2
     D A2                            10  2
      *---------------------------------------------------------------
     D MULT_EVAL       PR            10  2
     D M1                            10  2
     D M2                            10  2
      *---------------------------------------------------------------
     D CONCAT_EVAL     PR            20
     D C1                            10
     D C2                            10
      *---------------------------------------------------------------
     D TOTAL_EVAL      PR            10  2 DIM(10)
     D T1                            10  2 DIM(10)
     D T2                            10  2 DIM(10)
      *---------------------------------------------------------------
     D NEGATE_EVAL     PR              N
     D N1                              N
      *---------------------------------------------------------------
     D A1P             S             10  2
     D A2P             S             10  2
     D A_RETURNP       S             10  2
     D M1P             S             10  2
     D M2P             S             10  2
     D M_RETURNP       S             10  2
     D C1P             S             10
     D C2P             S             10
     D C_RETURNP       S             20
     D T1P             S             10  2 DIM(10)
     D T2P             S             10  2 DIM(10)
     D T_RETURNP       S             10  2 DIM(10)
     D N1P             S               N
     D N_RETURNP       S               N
      *
     D RETURN_CHAR     S             50
      *---------------------------------------------------------------
      * Main
      *---------------------------------------------------------------
      * Initialize data for 'CALLP' procedure call
     C                   EVAL      A1P=12,34
     C                   EVAL      A2P=56,78
     C                   EVAL      A_RETURNP=0
      *
     C                   EVAL      M1P=0,15
     C                   EVAL      M2P=3,94
     C                   EVAL      M_RETURNP=0
      *
     C                   EVAL      C1P='12345     '
     C                   EVAL      C2P='     54321'
     C                   EVAL      C_RETURNP=''
      *
     C                   Z-ADD     0             X                10 0
     C                   CLEAR                   T1P
     C                   CLEAR                   T2P
     C     1             DO        10            X
     C                   EVAL      T1P(X)=3,16+T2P(X)
     C                   EVAL      T2P(X)=0,98+T1P(X)
     C                   ENDDO
      *
     C                   EVAL      N1P=*OFF
      *
      * Run procedures by 'CALLP' statement
      *
     C                   CALLP     ADD_CALLP(A1P:A2P:A_RETURNP)
      * Must be 69.12'
     C                   EVAL      RETURN_CHAR=%CHAR(A_RETURNP)
     C     RETURN_CHAR   DSPLY
      *
     C                   CALLP     MULT_CALLP(M1P:M2P:M_RETURNP)
      * Must be '.59'
     C                   EVAL      RETURN_CHAR=%CHAR(M_RETURNP)
     C     RETURN_CHAR   DSPLY
      *
     C                   CALLP     CONCAT_CALLP(C1P:C2P:C_RETURNP)
      * Must be '12345          54321'
     C     C_RETURNP     DSPLY
      *
     C                   CALLP     TOTAL_CALLP(T1P:T2P:T_RETURNP)
     C                   Z-ADD     0             SUM              10 2
     C                   Z-ADD     0             X                10 0
     C     1             DO        10            X
     C                   EVAL      SUM+=T_RETURNP(X)
     C                   ENDDO
      * Must be '73.00'
     C                   EVAL      RETURN_CHAR=%CHAR(SUM)
     C     RETURN_CHAR   DSPLY
      *
     C                   CALLP     NEGATE_CALLP(N1P:N_RETURNP)
      * Must be '1' (means true)
     C     N_RETURNP     DSPLY

      * Reinitialize data for 'EVAL' procedure call
     C                   EVAL      A1P=12,34
     C                   EVAL      A2P=56,78
     C                   EVAL      A_RETURNP=0
      *
     C                   EVAL      M1P=0,15
     C                   EVAL      M2P=3,94
     C                   EVAL      M_RETURNP=0
      *
     C                   EVAL      C1P='12345     '
     C                   EVAL      C2P='     54321'
     C                   EVAL      C_RETURNP=''
      *
     C                   CLEAR                   T1P
     C                   CLEAR                   T2P
     C     1             DO        10            X                10 0
     C                   EVAL      T1P(X)=3,16+T2P(X)
     C                   EVAL      T2P(X)=0,98+T1P(X)
     C                   ENDDO
      *
     C                   EVAL      N1P=*OFF
      * Run procedures by 'EVAL' statement
      *
     C                   EVAL      A_RETURNP=ADD_EVAL(A1P:A2P)
      * Must be '69.12'
     C                   EVAL      RETURN_CHAR=%CHAR(A_RETURNP)
     C     RETURN_CHAR   DSPLY
      *
     C                   EVAL      M_RETURNP=MULT_EVAL(M1P:M2P)
      * Must be '.59'
     C                   EVAL      RETURN_CHAR=%CHAR(M_RETURNP)
     C     RETURN_CHAR   DSPLY
      *
     C                   EVAL      C_RETURNP=CONCAT_EVAL(C1P:C2P)
      * Must be '12345          54321'
     C     C_RETURNP     DSPLY
      *
     C                   EVAL      T_RETURNP=TOTAL_EVAL(T1P:T2P)
     C                   Z-ADD     0             SUM              10 2
     C                   Z-ADD     0             X                10 0
     C     1             DO        10            X
     C                   EVAL      SUM+=T_RETURNP(X)
     C                   ENDDO
     C                   CLEAR                   T_RETURNP
      * Must be '73.00'
     C                   EVAL      RETURN_CHAR=%CHAR(SUM)
     C     RETURN_CHAR   DSPLY
      *
     C                   EVAL      N_RETURNP=NEGATE_EVAL(N1P)
      * Must be '1' (means true)
     C     N_RETURNP     DSPLY
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
     P ADD_CALLP       B
     D ADD_CALLP       PI
     D A1                            10  2
     D A2                            10  2
     D A_RETURN                      10  2
      *
     C                   EVAL      A_RETURN=A1+A2
      *
     P ADD_CALLP       E
      *---------------------------------------------------------------
     P MULT_CALLP      B
     D MULT_CALLP      PI
     D M1                            10  2
     D M2                            10  2
     D M_RETURN                      10  2
      *
     C                   EVAL      M_RETURN=M1*M2
      *
     P MULT_CALLP      E
      *---------------------------------------------------------------
     P CONCAT_CALLP    B
     D CONCAT_CALLP    PI
     D C1                            10
     D C2                            10
     D C_RETURN                      20
      *
     C                   EVAL      C_RETURN=C1+C2
      *
     P CONCAT_CALLP    E
      *---------------------------------------------------------------
     P TOTAL_CALLP     B
     D TOTAL_CALLP     PI
     D T1                            10  2 DIM(10)
     D T2                            10  2 DIM(10)
     D T_RETURN                      10  2 DIM(10)
      *
     C                   Z-ADD     0             X                10 0
     C     1             DO        10            X
     C                   EVAL      T_RETURN(X)=T1(X)+T2(X)
     C                   ENDDO
      *
     P TOTAL_CALLP     E
      *---------------------------------------------------------------
     P NEGATE_CALLP    B
     D NEGATE_CALLP    PI
     D N1                              N
     D N_RETURN                        N
      *
     C                   EVAL      N_RETURN=NOT(N1)
      *
     P NEGATE_CALLP    E
      *---------------------------------------------------------------
     P ADD_EVAL        B
     D ADD_EVAL        PI            10  2
     D A1                            10  2
     D A2                            10  2
      *
     C                   RETURN    A1+A2
      *
     P ADD_EVAL        E
      *---------------------------------------------------------------
     P MULT_EVAL       B
     D MULT_EVAL       PI            10  2
     D M1                            10  2
     D M2                            10  2
      *
     C                   RETURN    M1*M2
      *
     P MULT_EVAL       E
      *---------------------------------------------------------------
     P CONCAT_EVAL     B
     D CONCAT_EVAL     PI            20
     D C1                            10
     D C2                            10
      *
     C                   RETURN    C1+C2
      *
     P CONCAT_EVAL     E
      *---------------------------------------------------------------
     P TOTAL_EVAL      B
     D TOTAL_EVAL      PI            10  2 DIM(10)
     D T1                            10  2 DIM(10)
     D T2                            10  2 DIM(10)
      *
     D T_RETURN        S             10  2 DIM(10)
      *
     C                   Z-ADD     0             X                10 0
     C     1             DO        10            X
     C                   EVAL      T_RETURN(X)=T1(X)+T2(X)
     C                   ENDDO
     C                   RETURN    T_RETURN
      *
     P TOTAL_EVAL      E
      *---------------------------------------------------------------
     P NEGATE_EVAL     B
     D NEGATE_EVAL     PI              N
     D N1                              N
      *
     C                   RETURN    NOT(N1)
      *
     P NEGATE_EVAL     E
      *---------------------------------------------------------------