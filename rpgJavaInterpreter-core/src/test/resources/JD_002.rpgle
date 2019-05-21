     V*=====================================================================
     V* Date      Release Au Description
     V* dd/mm/yy  nn.mm   xx Brief description
     V*=====================================================================
     V* 19/10/18  V5R1    AS Created
     V* 04/02/19  V5R1    AS Comments translated to english
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
      * PARM LISTEN_FLD (Folder listener)
      * . Folder to monitor
     D §§FLD           S           1000
      * . Object name involved in the event
     D §§NAM           S           1000
      * . Object type involved in the event (FILE/FOLDER)
     D §§TIP           S             10
      * . Event type (see "Mode" variable)
     D §§OPE           S             10
      *---------------------------------------------------------------
      * PARM JD_NFYEVE (notify the event)
      * . Function
     D §§FUNZ          S             10
      * . Method
     D §§METO          S             10
      * . Array of variables
     D §§SVAR          S                   LIKE($$SVAR) DIM(%ELEM($$SVAR))
      *---------------------------------------------------------------
      * "Folder" variable (input): Folder name
     D $$FLD           S           1000
      * "Mode" variable (input): Events to monitor
      * . *ADD File or folder creation
      * . *CHG File change
      * . *DEL File or foldere deletion
     D $$MOD           S             10
      * "filter" variable (input): Filter to apply
      * . Now we can set up only a single filter and only in form *.extension
     D $$FLT           S           1000
      *---------------------------------------------------------------
      * Work variables
     D $$EST_FLT       S             10
     D OK              S              1N
     D $X              S              3  0
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
      * Invoke (empty subroutine in this case)
1x   C                   WHEN      U$FUNZ='ESE'
     C                   EXSR      FESE
      * Detach (empty subroutine in this case)
1x   C                   WHEN      U$FUNZ='CLO'
     C                   EXSR      FCLO
1e   C                   ENDSL
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
      * Every Sme.UP program encapsulates *INZSR in a /COPY.
      * So we provide £INIZI subroutine to do the same job
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
      * Variable load
     C                   EXSR      CARVAR_INZ
      * Listening $$FLD folder
1    C                   DO        *HIVAL
     C                   EVAL      §§FLD=$$FLD
     C                   CALL      'LISTEN_FLD'
     C                   PARM                    §§FLD
     C                   PARM                    §§NAM
     C                   PARM                    §§TIP
     C                   PARM                    §§OPE
      * Check if operation returned is one of those managed
     C                   EXSR      CHKOPE
2    C                   IF        NOT(OK)
     C                   ITER
2e   C                   ENDIF
      * Check if file meets the filter
     C                   EXSR      CHKFLT
2    C                   IF        NOT(OK)
     C                   ITER
2e   C                   ENDIF
      * Build variabled to notify the event
     C                   EXSR      COSVAR_EVE
      * Notify the event
     C                   CALL      'JD_NFYEVE'
     C                   PARM                    §§FUNZ
     C                   PARM                    §§METO
     C                   PARM                    §§SVAR
1e   C                   ENDDO
      * Operation is always successfull
     C                   EVAL      U$IN35=*BLANKS
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Check if operation is one of those managed
      *--------------------------------------------------------------*
     C     CHKOPE        BEGSR
      *
1    C                   IF        $$MOD=*BLANKS OR
     C                             %SCAN(%TRIM(§§OPE):$$MOD)>0
     C                   EVAL      OK=*ON
1x   C                   ELSE
     C                   EVAL      OK=*OFF
1e   C                   ENDIF
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Check if file meets the filter
      *--------------------------------------------------------------*
     C     CHKFLT        BEGSR
      *
1    C                   SELECT
      * If no filter, file is OK
1x   C                   WHEN      $$FLT=*BLANKS
     C                   EVAL      OK=*ON
      * If I have a filter and the event concerns a folder: NO OK
1x   C                   WHEN      $$FLT<>*BLANKS AND §§TIP<>'FILE'
     C                   EVAL      OK=*OFF
1x   C                   OTHER
     C                   EVAL      $$EST_FLT=%SUBST($$FLT:3)
2    C                   IF        %LEN(%TRIM(§§NAM))>%LEN(%TRIM($$EST_FLT))+1
     C                             AND
     C
     C                             %TRIM(%SUBST(%TRIM(§§NAM):%LEN(%TRIM(§§NAM))
     C                              -%LEN(%TRIM($$EST_FLT))))
     C                             ='.'+%TRIM($$EST_FLT)
     C                   EVAL      OK=*ON
2x   C                   ELSE
     C                   EVAL      OK=*OFF
2e   C                   ENDIF
1e   C                   ENDSL
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Load variables for INZ function
      *--------------------------------------------------------------*
     C     CARVAR_INZ    BEGSR
      *
      * Read the variabiles
1    C                   DO        *HIVAL        $X
2    C                   IF        $$SVARCD($X)=*BLANKS
     C                   LEAVE
2e   C                   ENDIF
2    C                   SELECT
      * Folder: Folder name
2x   C                   WHEN      $$SVARCD($X)='Folder'
     C                   EVAL      $$FLD=$$SVARVA($X)
      * Mode: Events to monitor
      * . *ADD File or folder creation
      * . *CHG File change
      * . *DEL File or foldere deletion
2x   C                   WHEN      $$SVARCD($X)='Mode'
     C                   EVAL      $$MOD=$$SVARVA($X)
      * Filter: Filter to apply
      * . Now we can set up only a single filter and only in form *.extension
2x   C                   WHEN      $$SVARCD($X)='Filter'
     C                   EVAL      $$FLT=$$SVARVA($X)
2e   C                   ENDSL
1e   C                   ENDDO
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Build variables to notify the event
      *--------------------------------------------------------------*
     C     COSVAR_EVE    BEGSR
      *
     C                   EVAL      $$SVARCD(01)='Object name'                   COSTANTE
     C                   EVAL      $$SVARVA(01)=%TRIM(§§NAM)
     C                   EVAL      $$SVARCD(02)='Object type'                   COSTANTE
     C                   EVAL      $$SVARVA(02)=%TRIM(§§TIP)
     C                   EVAL      $$SVARCD(03)='Operation type'                COSTANTE
     C                   EVAL      $$SVARVA(03)=%TRIM(§§OPE)
     C                   EVAL      §§SVAR=$$SVAR
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Invoke
      *--------------------------------------------------------------*
     C     FESE          BEGSR
      *
      * This function doesn't do anything and is always successfull
     C                   EVAL      U$IN35=*BLANKS
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
