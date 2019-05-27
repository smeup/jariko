     V*=====================================================================
     V* Date      Release Au Description
     V* dd/mm/yy  nn.mm   xx Brief description
     V*=====================================================================
     V* 27/05/19  V5R1    AD Anonymous Developer
     V*=====================================================================
     H/COPY QILEGEN,£INIZH
      *---------------------------------------------------------------
     I/COPY QILEGEN,£TABB£1DS
     I/COPY QILEGEN,£PDS
      *---------------------------------------------------------------
      * ENTRY
      * . Function
     D U$FUNZ          S             10
      * . Method
     D U$METO          S             10
      * . String
     D U$SVAR          S         210000
      * . Return Code ('1'=ERROR / blank=OK)
     D U$IN35          S              1
      *---------------------------------------------------------------
      * . Url
     D $$URL           S           1000
     D $X              S              3  0
     D $$SVAR          S                   LIKE(U$SVAR)
     D XXSVAR          S                   LIKE(U$SVAR)
      *---------------------------------------------------------------
      * Invoke Url
      * . Function
     D §§FUNZ          S             10
      * . Method
     D §§METO          S             10
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      *
     C     *ENTRY        PLIST
     C                   PARM                    U$FUNZ
     C                   PARM                    U$METO
     C                   PARM                    U$SVAR
     C                   PARM                    U$IN35
      * Initial settings
     C                   EXSR      IMP0
      * Function / Method
1    C                   SELECT
      * Init
1x   C                   WHEN      U$FUNZ='INZ'
     C                   EXSR      FINZ
      * Invoke URL
1x   C                   WHEN      U$FUNZ='ESE'
     C                   EXSR      FESE
      * Detach (empty subroutine in this case)
1x   C                   WHEN      U$FUNZ='CLO'
     C                   EXSR      FCLO
1e   C                   ENDSL
      * Final settings
     C                   EXSR      FIN0
      * End
     C                   SETON                                        RT
      *---------------------------------------------------------------
     C/COPY QILEGEN,£INZSR
      *---------------------------------------------------------------
    RD* Initial subroutine (as *INZSR)
      *--------------------------------------------------------------*
     C     £INIZI        BEGSR
      *
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Initial settings
      *--------------------------------------------------------------*
     C     IMP0          BEGSR
      *
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Final settings
      *--------------------------------------------------------------*
     C     FIN0          BEGSR
      *
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Init
      *--------------------------------------------------------------*
     C     FINZ          BEGSR
      *
     C                   EVAL      U$IN35=*BLANKS
     C                   EVAL      $$SVAR=U$SVAR
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Invoke
      *--------------------------------------------------------------*
     C     FESE          BEGSR
      *
     C                   EVAL      U$IN35=*BLANKS
      * Invoke url
     C                   EVAL      XXSVAR=%TRIMR($$SVAR)+U$SVAR
     C                   CALL      'JD_URL'
     C                   PARM                    §§FUNZ
     C                   PARM                    §§METO
     C                   PARM                    XXSVAR
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Detach
      *--------------------------------------------------------------*
     C     FCLO          BEGSR
      *
      * This function doesn't do anything and is always successfull
      *
     C                   ENDSR

