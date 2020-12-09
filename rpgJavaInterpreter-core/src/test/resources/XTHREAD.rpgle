      *---------------------------------------------------------------
      * ENTRY
      * . Function
     D U$FUNZ          S             10
      * . Method
     D U$METO          S             10
      * . Thread
     D U$THRE          S             20
      * . Display
     D MSG             S             40
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      *
      * Initial settings
     C                   EXSR      IMP0
      * Function / Method
     C                   EVAL      MSG = 'Fu:' + %TRIM(U$FUNZ) + ' ' +
     C                                   'Me:' + %TRIM(U$METO) + ' ' +
     C                                   'Th:' + %TRIM(U$THRE)
     C     MSG           DSPLY
      * Final settings
     C                   EXSR      FIN0
      * End
     C                   SETON                                        LR
      *---------------------------------------------------------------
     C/COPY QILEGEN,£INZSR
      *---------------------------------------------------------------
    RD* Initial subroutine (as *INZSR)
      *--------------------------------------------------------------*
     C     £INIZI        BEGSR
      *
     C     *ENTRY        PLIST
     C                   PARM                    U$FUNZ
     C                   PARM                    U$METO
     C                   PARM                    U$THRE
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Initial settings
      *--------------------------------------------------------------*
     C     IMP0          BEGSR
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Final settings
      *--------------------------------------------------------------*
     C     FIN0          BEGSR
      *
     C                   ENDSR
      *--------------------------------------------------------------*
