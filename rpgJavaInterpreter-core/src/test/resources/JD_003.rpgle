     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 19/10/18  V5R1   BMA Created
     V* 05/02/19  V5R1   BMA Comments translated to english
     V* 09/05/19  V5R1   BMA Corrected eval $$SVAR
     V*=====================================================================
     H/COPY QILEGEN,£INIZH
      *---------------------------------------------------------------
     I/COPY QILEGEN,£TABB£1DS
     I/COPY QILEGEN,£PDS
      /COPY QILEGEN,£JAX_PD1
      * Buffer received from socket
     D BUFFER          S          30000
      * Lenght buffer received
     D BUFLEN          S              5  0
      * XML
     D $XML            S          30000    VARYING
      * Row
     D $RIGA           S          30000    VARYING
      * License plate
     D $TARGA          S             50    VARYING
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
      * PARM JD_NFYEVE (notify the event)
      * . Function
     D §§FUNZ          S             10
      * . Method
     D §§METO          S             10
      * . Array of variables
     D §§SVAR          S                   LIKE($$SVAR) DIM(%ELEM($$SVAR))
      *---------------------------------------------------------------
     D ADDRSK          S             15
     D $X              S              5  0
     D $R              S              5  0
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      *
      * Initial settings
     C                   EXSR      IMP0
      *
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
      *--------------------------------------------------------------*
    RD* Init
      *--------------------------------------------------------------*
     C     FINZ          BEGSR
      *
     C                   CLEAR                   ADDRSK
1    C                   FOR       $X=1 TO %ELEM($$SVARCD)
      * Get address to listen to the socket
2    C                   IF        $$SVARCD($X)='SOCKET'
     C                   EVAL      ADDRSK=$$SVARVA($X)
     C                   LEAVE
2e   C                   ENDIF
1e   C                   ENDFOR
      *
     C                   IF        ADDRSK<>''
1    C                   DO        *HIVAL
      * I listen to the socket
     C                   CLEAR                   BUFFER
     C                   CLEAR                   BUFLEN
      *
     C                   CALL      'JD_RCVSCK'                          50
     C                   PARM                    ADDRSK
     C                   PARM                    BUFFER
     C                   PARM                    BUFLEN
      *
     C                   IF        *IN50=*ON
      * Socket error
     C                   EVAL      U$IN35='1'
     C                   LEAVE
     C                   ELSE
      * If buffer received
2    C                   IF        BUFLEN>0
     C                   EVAL      $XML=%SUBST(BUFFER:1:BUFLEN)
      * Search <Auto> attribute
     C                   EVAL      $RIGA=P_RxELE('Auto':'POS':01:$XML)          COSTANTE
3    C                   IF        $RIGA<>''
     C                   EVAL      $TARGA=P_RxVAL($RIGA:'Targa')                COSTANTE
4    C                   IF        $TARGA<>''
      * Search first empty element of array
     C                   EVAL      $R=%LOOKUP('':$$SVARCD)
5    C                   IF        $R>0
      * Return the license plate
     C                   EVAL      $$SVARCD($R)='Targa'                         COSTANTE
     C                   EVAL      $$SVARVA($R)=$TARGA
     C                   EVAL      §§SVAR=$$SVAR
      * Notify the event (the license plate)
     C                   CALL      'JD_NFYEVE'
     C                   PARM                    §§FUNZ
     C                   PARM                    §§METO
     C                   PARM                    §§SVAR
5e   C                   ENDIF
4e   C                   ENDIF
3e   C                   ENDIF
3e   C                   ENDIF
3e   C                   ENDIF
      *
2e   C                   ENDDO
     C                   ELSE
      * Empty address: Error
     C                   EVAL      U$IN35='1'
3e   C                   ENDIF
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Invoke
      *--------------------------------------------------------------*
     C     FESE          BEGSR
      *
      * This function doesn't do anything and is always successfull
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
      *
     C                   ENDSR
      *--------------------------------------------------------------*
      /COPY QILEGEN,£JAX_PC1
