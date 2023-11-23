     D MSG             S             10     VARYING
     D MSG1            S             10
     D MSG2            S             20
     D RESULT          S             50
     *********************************************************************
     C                   Eval       MSG = 'Parma'
     C                   Eval       RESULT = %EDITC(%LEN(%TRIM(MSG)):'1')
     ** Expected ?
     C     RESULT        dsply
     *********************************************************************
     C                   Eval       MSG1 = 'Piacenza'
     C                   Eval       RESULT = %EDITC(%LEN(%TRIM(MSG1)):'1')
     ** Expected ?
     C     RESULT        dsply
     *********************************************************************
     C                   Eval       MSG2 = 'Bologna'
     C                   Eval       RESULT = %EDITC(%LEN(%TRIM(MSG2)):'1')
     ** Expected ?
     C     RESULT        dsply
     *********************************************************************
     C                   Seton                                        LR