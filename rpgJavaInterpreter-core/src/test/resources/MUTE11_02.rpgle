     V*=====================================================================
     V* Date      Release Au Description
     V* dd/mm/yy  nn.mm   xx Brief description
     V*=====================================================================
     V* 19/10/18  V5R1    AS Created
     V* 04/02/19  V5R1    AS Comments translated to english
     V* 05/07/19  V5R1    BMA Renamed JD_002 in MUTE11_02
     V* 16/07/19  V5R1    AD Adjustments to work with RPG Intepreter into IOTSPI Plugin
     V* 22/07/19  V5R1    AD Adjustments to work with RPG Intepreter into IOTSPI Plugin
     V*=====================================================================
     H/COPY QILEGEN,£INIZH
      *---------------------------------------------------------------
     I/COPY QILEGEN,£TABB£1DS
     I/COPY QILEGEN,£PDS
      *---------------------------------------------------------------
     D $$SVAR          S           4096
      *---------------------------------------------------------------
      * ENTRY
      * . Function
     D U$FUNZ          S             10
      * . Method
     D U$METO          S             10
      * . Array of Variables
     D U$SVARSK        S                   LIKE($$SVAR)
      * . Return Code ('1'=ERROR / blank=OK)
     D U$IN35          S              1
      *---------------------------------------------------------------
      * PARM LISTEN_FLD (Folder listener)
      * . Folder to monitor
     D §§PTH           S           1000
      * . Object name involved in the event
     D §§NAM           S           1000
      * . Object type involved in the event (FILE/FOLDER)
     D §§TIP           S             10
      * . Event type (see "Event" variable)
     D §§OPE           S             10
      *---------------------------------------------------------------
      * PARM JD_NFYEVE (notify the event)
      * . Function
     D §§FUNZ          S             10
      * . Method
     D §§METO          S             10
      * . Array of variables
     D §§SVAR          S                   LIKE($$SVAR)
      *---------------------------------------------------------------
      * "Path" variable (input): Path name
     D $$PTH           S           1000
      * "Event" variable (input): Events to monitor
      * . *ADD File or folder creation
      * . *CHG File change
      * . *DEL File or foldere deletion
     D $$EVE           S             15
      * "filter" variable (input): Filter to apply
      * . Now we can set up only a single filter and only in form *.extension
     D $$FLT           S           1000
      * . Recursive
     D $$REC           S              5
      *---------------------------------------------------------------
      * Work variables
     D $$EST_FLT       S             10    VARYING
     D OK              S              1N
     D ADDRSK          S           4096
     D $$VAR           S           4096
     D $X              S              5  0
     D A37TAGS         S           4096
      *---------------------------------------------------------------
     D $ATTRI          S              5  0
     D $BRACK          S              5  0
     D $ATTLEN         S              5  0
     D $C              S              5  0
     D $L              S              5  0
     D $S              S              5  0
      *---------------------------------------------------------------
     D $NA             S           1024
     D $TY             S           1024
     D $OP             S           1024
      *---------------------------------------------------------------
      * Buffer received
     D BUFFER          S          30000
      * Length buffer received
     D BUFLEN          S              5  0
      * Error indicator
     D IERROR          S              1
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      *
     C     *ENTRY        PLIST
     C                   PARM                    U$FUNZ
     C                   PARM                    U$METO
     C                   PARM                    U$SVARSK
     C                   PARM                    U$IN35
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
     C                   SETON                                        RT
      *---------------------------------------------------------------
     C/COPY QILEGEN,£INZSR
      *---------------------------------------------------------------
    RD* Initial subroutine (as *INZSR)
      *--------------------------------------------------------------*
     C     £INIZI        BEGSR
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Initial settings
      *--------------------------------------------------------------*
     C     IMP0          BEGSR
      *
      * Clear error field
     C                   EVAL      U$IN35=*BLANKS
      *
     C                   EVAL      $$SVAR=U$SVARSK
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Final settings
      *--------------------------------------------------------------*
     C     FIN0          BEGSR
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Init
      *--------------------------------------------------------------*
     C     FINZ          BEGSR
      *
1    C                   SELECT
      * .Set variables
1x   C                   WHEN      U$METO='A37TAGS'
     C                   EVAL      A37TAGS=$$SVAR
      *
      * .Post Init (main program, listen to path changes and fire event)
1x   C                   WHEN      U$METO='POSTINIT'
     C                   EVAL      ADDRSK=$$SVAR
      * .PATH(c:/myFolder/xxx)|FILTER(txt;pdf;jpg;doc)|RECURSIVE(true)|EVENT(C;M;D)
     C                   EXSR      CARVAR_INZ
2    C                   IF        ADDRSK<>''
3    C                   DO        *HIVAL
      * I listen to folder changes
     C                   CLEAR                   BUFFER
     C                   CLEAR                   BUFLEN
     C                   EVAL      IERROR=''
     C                   CALL      'JD_LSTFLD'
     C                   PARM                    ADDRSK
     C                   PARM                    BUFFER
     C                   PARM                    BUFLEN
     C                   PARM                    IERROR
4    C                   IF        IERROR<>''
     C                   EVAL      U$IN35='1'
     C                   LEAVE
4x   C                   ELSE
      * If buffer received
5    C                   IF        BUFLEN>0
      * Extract data (name, type, operation)
     C                   EXSR      EXTRACT_DTA
      * Check if operation returned is one of those managed
     C                   EXSR      CHKOPE
6    C                   IF        NOT(OK)
     C                   ITER
6e   C                   ENDIF
      * Check if file meets the filter
     C                   EXSR      CHKFLT
6    C                   IF        NOT(OK)
     C                   ITER
6e   C                   ENDIF
      * .Build variabled to notify the event
     C                   EXSR      COSVAR_EVE
     C                   EVAL      §§FUNZ='NFY'
     C                   EVAL      §§METO='EVE'
     C*****              EVAL      §§SVAR=$$VAR
      *
      * .Notify the event (the license plate)
     C                   CALL      'JD_NFYEVE'
     C                   PARM                    §§FUNZ
     C                   PARM                    §§METO
     C                   PARM                    §§SVAR
     C                   PARM                    A37TAGS
5e   C                   ENDIF
4e   C                   ENDIF
      *
3e   C                   ENDDO
2x   C                   ELSE
      * Empty address: Error
     C                   EVAL      U$IN35='1'
2e   C                   ENDIF
      *
1e   C                   ENDSL
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Check if operation is one of those managed
      *--------------------------------------------------------------*
     C     CHKOPE        BEGSR
      *
1    C                   IF        $$EVE=*BLANKS OR
     C                             %SCAN(%TRIM(§§OPE):$$EVE)>0
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
     C                   EVAL      $L=%LEN(%TRIMR(§§NAM))
      *                   FOR       $L DOWNTO 1
     C                   CLEAR                   $$EST_FLT
     C                   DO        *HIVAL
     C                   EVAL      $L=$L-1
     C                   IF        $L=0
     C                   LEAVE
     C                   ENDIF
     C                   IF        %SUBST(§§NAM:$L:1)='.'
     C                   IF        $L+1<%LEN(%TRIMR(§§NAM))
     C                   EVAL      $$EST_FLT=%SUBST(§§NAM:$L+1)
     C                   LEAVE
     C                   ENDIF
     C                   ENDIF
     C                   ENDDO
      *                  ENDFOR
     C                   EVAL      $S=%SCAN(%TRIM($$EST_FLT):$$FLT)
     C                   EVAL      $C=%LEN(%TRIM($$EST_FLT))
     C                   IF        $S>0 AND $S+$C <= %LEN($$FLT) AND
     C                             (%SUBST($$FLT:$S+$C:1)='' OR
     C                             %SUBST($$FLT:$S+$C:1)=';')
     C                   EVAL      OK=*ON
2x   C                   ELSE
     C                   EVAL      OK=*OFF
2e   C                   ENDIF
1e   C                   ENDSL
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Build variables to notify the event
      *--------------------------------------------------------------*
     C     COSVAR_EVE    BEGSR
      *
      * Extract data (name, type, operation)
     C                   EXSR      EXTRACT_DTA
      *
     C                   EVAL      $NA='Object name('+%TRIM(§§NAM)+')|'         COSTANTE
     C                   EVAL      $TY='Object type('+%TRIM(§§TIP)+')|'         COSTANTE
     C                   EVAL      $OP='Operation type('+%TRIM(§§OPE)+')'       COSTANTE
      *
     C                   EVAL      §§SVAR=%TRIM($NA)+%TRIM($TY)+%TRIM($OP)
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Load variables for INZ function
      *--------------------------------------------------------------*
     C     CARVAR_INZ    BEGSR
      *
      * Vars example:
      *
      * .PATH(c:/myFolder/xxx)|FILTER(txt;pdf;jpg;doc)|RECURSIVE(true)|EVENT(C;M;D)
      * PATH
     C                   EVAL      $ATTRI=%SCAN('PATH(':ADDRSK)                 Index of PATH(
0    C                   IF        $ATTRI>0
     C                   EVAL      $ATTRI=$ATTRI+5
     C                   EVAL      $BRACK=%SCAN(')':ADDRSK:$ATTRI)              Index of )
1    C                   IF        $BRACK>0
     C                   EVAL      $ATTLEN=$BRACK-$ATTRI                        Value length
     C                   EVAL      $$PTH=%SUBST(ADDRSK:$ATTRI:$ATTLEN)
1e   C                   ENDIF
0e   C                   ENDIF
      * FILTER
     C                   EVAL      $ATTRI=%SCAN('FILTER(':ADDRSK)               Index of FILTER(
0    C                   IF        $ATTRI>0
     C                   EVAL      $ATTRI=$ATTRI+7
     C                   EVAL      $BRACK=%SCAN(')':ADDRSK:$ATTRI)              Index of )
1    C                   IF        $BRACK>0
     C                   EVAL      $ATTLEN=$BRACK-$ATTRI                        Value length
     C                   EVAL      $$FLT=%SUBST(ADDRSK:$ATTRI:$ATTLEN)
1e   C                   ENDIF
0e   C                   ENDIF
      * RECURSIVE
     C                   EVAL      $ATTRI=%SCAN('RECURSIVE(':ADDRSK)            Index of RECURSIVE(
0    C                   IF        $ATTRI>0
     C                   EVAL      $ATTRI=$ATTRI+10
     C                   EVAL      $BRACK=%SCAN(')':ADDRSK:$ATTRI)              Index of )
1    C                   IF        $BRACK>0
     C                   EVAL      $ATTLEN=$BRACK-$ATTRI                        Value length
     C                   EVAL      $$REC=%SUBST(ADDRSK:$ATTRI:$ATTLEN)
1e   C                   ENDIF
0e   C                   ENDIF
      * EVENT
     C                   EVAL      $ATTRI=%SCAN('EVENT(':ADDRSK)                Index of EVENT(
0    C                   IF        $ATTRI>0
     C                   EVAL      $ATTRI=$ATTRI+6
     C                   EVAL      $BRACK=%SCAN(')':ADDRSK:$ATTRI)              Index of )
1    C                   IF        $BRACK>0
     C                   EVAL      $ATTLEN=$BRACK-$ATTRI                        Value length
     C                   EVAL      $$EVE=%SUBST(ADDRSK:$ATTRI:$ATTLEN)
1e   C                   ENDIF
0e   C                   ENDIF
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Extract data from response
      *--------------------------------------------------------------*
     C     EXTRACT_DTA   BEGSR
      *
     C                   EVAL      $$VAR=%SUBST(BUFFER:1:BUFLEN)
      * Vars example:
      * NAME(c:/myFolder/xxx)|TYPE(FILE)|OPERATION(C)
      *
      * NAME
     C                   EVAL      $ATTRI=%SCAN('NAME(':$$VAR)                  Index of NAME(
0    C                   IF        $ATTRI>0
     C                   EVAL      $ATTRI=$ATTRI+5
     C                   EVAL      $BRACK=%SCAN(')':$$VAR:$ATTRI)               Index of )
1    C                   IF        $BRACK>0
     C                   EVAL      $ATTLEN=$BRACK-$ATTRI                        Value length
     C                   EVAL      §§NAM=%SUBST($$VAR:$ATTRI:$ATTLEN)
1e   C                   ENDIF
0e   C                   ENDIF
      * TYPE
     C                   EVAL      $ATTRI=%SCAN('TYPE(':$$VAR)                  Index of TYPE(
0    C                   IF        $ATTRI>0
     C                   EVAL      $ATTRI=$ATTRI+5
     C                   EVAL      $BRACK=%SCAN(')':$$VAR:$ATTRI)               Index of )
1    C                   IF        $BRACK>0
     C                   EVAL      $ATTLEN=$BRACK-$ATTRI                        Value length
     C                   EVAL      §§TIP=%SUBST($$VAR:$ATTRI:$ATTLEN)
1e   C                   ENDIF
0e   C                   ENDIF
      * OPERATION
     C                   EVAL      $ATTRI=%SCAN('OPERATION(':$$VAR)             Index of OPERATION(
0    C                   IF        $ATTRI>0
     C                   EVAL      $ATTRI=$ATTRI+10
     C                   EVAL      $BRACK=%SCAN(')':$$VAR:$ATTRI)               Index of )
1    C                   IF        $BRACK>0
     C                   EVAL      $ATTLEN=$BRACK-$ATTRI                        Value length
     C                   EVAL      §§OPE=%SUBST($$VAR:$ATTRI:$ATTLEN)
1e   C                   ENDIF
0e   C                   ENDIF
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
