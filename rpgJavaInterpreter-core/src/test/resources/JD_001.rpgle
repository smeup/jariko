     V*=====================================================================
     V* Date      Release Au Description
     V* dd/mm/yy  nn.mm   xx Brief description
     V*=====================================================================
     V* 24/10/18  V5R1    PEDSTE Created
     V* 06/02/19  V5R1    PEDSTE Comments translated to english and added return code
     V* 07/02/19  V5R1    PEDSTE Array of Variables in initialisation
     V* 27/03/19  V5R1    CM Ricompilato
     V* 21/05/19  V5R1    BMA Funzione EXE rinominata ESE per uniformità
     V*=====================================================================
     H/COPY QILEGEN,£INIZH
      *---------------------------------------------------------------
     I/COPY QILEGEN,£TABB£1DS
     I/COPY QILEGEN,£PDS
      *---------------------------------------------------------------
     D                 DS
     D $$SVAR                      1050    DIM(200)
     D  $$SVARCD                     50    OVERLAY($$SVAR:1)                    Name
     D  $$SVARVA                   1000    OVERLAY($$SVAR:*NEXT)                Value
      *---------------------------------------------------------------
      * ENTRY
      * . Function
     D U$FUNZ          S             10
      * . Method
     D U$METO          S             10
      * . Array of Variables
     D U$SVARSK        S                   LIKE($$SVAR) DIM(%ELEM($$SVAR))
      * . Return Code ('1'=ERROR / blank=OK)
     D U$IN35          S              1
      *---------------------------------------------------------------
      * . Url
     D $$URL           S           1000
     D $X              S              3  0
      * . Array of Variables in initialisation
     D U$SVARSK_INI    S                   LIKE($$SVAR) DIM(%ELEM($$SVAR))
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
     C     *ENTRY        PLIST
     C                   PARM                    U$FUNZ
     C                   PARM                    U$METO
     C                   PARM                    U$SVARSK
     C                   PARM                    U$IN35
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Initial settings
      *--------------------------------------------------------------*
     C     IMP0          BEGSR
      *
     C                   EVAL      $$SVAR=U$SVARSK
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
      * Load array of Variables in initialisation
     C                   EVAL      U$SVARSK_INI=$$SVAR
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Invoke
      *--------------------------------------------------------------*
     C     FESE          BEGSR
      *
     C                   EVAL      U$IN35=*BLANKS
      *
      * Search element with Url
     C                   EVAL      $X=%LOOKUP('Url':$$SVARCD)                   COSTANTE
      * Not found is an Error
5    C                   IF        $X = 0
     C                   EVAL      U$IN35='1'
5    C                   ELSE
     C                   EVAL      $$URL=$$SVARVA($X)
      * Replace all variables of execution in url
     C                   EXSR      REPVAR
      * Replace all variables of initialisation in url
     C                   EVAL      $$SVAR=U$SVARSK_INI
     C                   EXSR      REPVAR
      * Invoke url
     C                   CLEAR                   $$SVAR
     C                   EVAL      $$SVARCD(01)='Url'                           COSTANTE
     C                   EVAL      $$SVARVA(01)=$$URL
     C                   CALL      'JD_URL'
     C                   PARM                    §§FUNZ
     C                   PARM                    §§METO
     C                   PARM                    $$SVAR
     C                   ENDIF
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Replace variables
      *--------------------------------------------------------------*
     C     REPVAR        BEGSR
     C     2             DO        *HIVAL        $X
     C                   IF        $$SVARCD($X)=*BLANKS
     C                   LEAVE
     C                   ENDIF
     C                   EVAL      $$URL=%XLATE($$SVARCD($X):
     C                             $$SVARVA($X):
     C                             $$URL)
     C                   ENDDO
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Detach
      *--------------------------------------------------------------*
     C     FCLO          BEGSR
      *
      * This function doesn't do anything and is always successfull
      *
     C                   ENDSR
