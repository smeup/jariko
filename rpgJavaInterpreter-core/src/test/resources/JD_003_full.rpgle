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
     V* ==============================================================
      * Variabili globali delle procedure
     D £JSP            S              7  0 DIM(50)
      *--------------------------------------------------------------*
     D                 DS
     D£JAXATV                       306    DIM(200)
     D £JAXATV_A                     50    OVERLAY(£JAXATV:1)
     D £JAXATV_V                    256    OVERLAY(£JAXATV:*NEXT)
     D                 DS
     D£JAXATP                      1055    DIM(200)
     D £JAXATP_A                     50    OVERLAY(£JAXATP:1)
     D £JAXATP_V                   1000    OVERLAY(£JAXATP:*NEXT)
     D £JAXATP_L                      5  0 OVERLAY(£JAXATP:*NEXT)
      *--------------------------------------------------------------*
      * Restituisce il valore di un'attributo dei TAG
     DP_RxVAL          PR         30000    VARYING
     D $XmlTag                    30000    CONST VARYING
     D $XmlAtt                       64    CONST
      *
      * Restituisce il valore di un'attributo di tipo aaa(
     DP_RxATT          PR         30000    VARYING
     D $XmlTag                    30000    CONST VARYING
     D $XmlATT                       64    CONST VARYING
     D $XmlASS                       15    CONST
     D $XmlFND                        1N   OPTIONS(*NOPASS)
     D $XmlLIV                        1    OPTIONS(*NOPASS)
      *
      * Sostituzione dei caratteri non validi PER XML
     DP_RxSOS          PR         32766    VARYING
     D $XmlSOS                    30000    CONST VARYING
     D $StrLang                       1    CONST OPTIONS(*NOPASS)
      *
      * Sostituzione stringa in carattere
     DP_RxSOC          PR         30000    VARYING
     D $XmlSOC                    30000    CONST VARYING
     D $StrLang                       1    CONST OPTIONS(*NOPASS)
      *
      * Sostituzione dei caratteri non validi PER URL
     DP_RxURL          PR         30000    VARYING
     D $XmlURL                    30000    CONST VARYING
      *
      * Trasformazione Stringa
     DP_RxLATE         PR         32766    VARYING
     D $XmlINP                    30000    CONST VARYING
     D $XmlS01                    30000    CONST VARYING
     D $XmlS02                    30000    CONST VARYING
     D $SosSing                       1    CONST OPTIONS(*NOPASS)
     D $SosCase                       1    CONST OPTIONS(*NOPASS)
      *
      * Restituisce il contenuto dell'elemento richiesto
     DP_RxELE          PR         30000    VARYING
     D Xml_Fnd                      512    VARYING CONST
     D Xml_Met                       10    CONST
      * .livello di chiamata
      * ... livelli 48-50 riservati £JAY
     D Xml_Liv                        2  0 CONST
     D Xml_Str                    30000    VARYING
     D NodIni                         5  0 OPTIONS(*NOPASS)
     D NodLen                         5  0 OPTIONS(*NOPASS)
     D Xml_Con                    30000    OPTIONS(*NOPASS) VARYING
      * . Cerca tag anche all'interno di un CDATA
     D SEACDATA                       1    OPTIONS(*NOPASS)
      *
      * Restituisce la lista degli attributi e dei valori di un elemento
     DP_RxATV          PR           306    DIM(200)
     D Xml_Str                    30000    VARYING
     D  $XmlPRG                       5  0 OPTIONS(*NOPASS)
      *
      * Restituisce la lista degli attributi e dei valori di un elemento di documentazione attiva
     DP_RxATP          PR          1055    DIM(200)
     D Xml_StrP                   30000    VARYING
     D  $XmlPRGp                      5  0 OPTIONS(*NOPASS)
      *
      * Restituisce una stringa (es. intestazione matrice) splittata su più righe
     DP_RxSPL          PR         30000    VARYING
     D $String                    30000    CONST VARYING
     D $Split                         1    CONST
***************************************************************
     D £G49SI          DS          1024
     D £G49SE          DS         30000
***************************************************************
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
***************************************************************
     V*================================================================
      *--------------------------------------------------------------*
    RD* Parser XML risposta (Estrae gli attributo scelto)
      *--------------------------------------------------------------*
     PP_RxVAL          B
     D P_RxVAL         Pi         30000    VARYING
     D $XmlTAG                    30000    CONST VARYING
     D $XmlATT                       64    CONST
     D   XmlTAG        S          30000    VARYING
     D   XmlATT        S             64
     D   XmlVAL        S          30000    VARYING
      *
     C                   EVAL      XmlTAG=$XmlTAG
     C                   EVAL      XmlATT=$XmlATT
     C                   CLEAR                   XmlVAL
      *
     C                   CALL      'JAJAX2'
     C                   PARM                    XmlTAG
     C                   PARM                    XmlATT
     C                   PARM                    XmlVAL
      *
      * Restituisce VALORE
     C                   RETURN    XmlVAL
     P                 E
      *--------------------------------------------------------------*
    RD* Parser attributo di documentazione attiva
      *--------------------------------------------------------------*
     PP_RxATT          B
     D P_RxATT         Pi         30000    VARYING
     D  $XmlTAG                   30000    CONST VARYING
     D  $XmlATT                      64    CONST VARYING
     D  $XmlASS                      15    CONST
     D  $XmlFND                       1N   OPTIONS(*NOPASS)
     D  $XmlLIV                       1    OPTIONS(*NOPASS)
     D   XmlTAG        S          30000    VARYING
     D   XmlATT        S             64    VARYING
     D   XmlASS        S             15
     D   XmlFND        S              1N
     D   XmlLIV        S              1
     D   XmlVAL        S          30000    VARYING
     D   XMLPR         S              5I 0
      *
     C                   EVAL      XmlTAG=$XmlTAG
     C                   EVAL      XmlATT=$XmlATT
     C                   EVAL      XmlASS=$XmlASS
     C                   EVAL      XmlFND=*OFF
     C                   EVAL      XmlLIV=''
     C                   CLEAR                   XmlVAL
     C                   EVAL      XMLPR=%parms
1    C                   IF        %parms>3
     C                   EVAL      XmlFND=$XmlFND
1e   C                   ENDIF
1    C                   IF        %parms>4
     C                   EVAL      XmlLIV=$XmlLIV
1e   C                   ENDIF
      *
     C                   CALL      'JAJAX1'
     C                   PARM                    XmlTAG
     C                   PARM                    XmlATT
     C                   PARM                    XmlASS
     C                   PARM                    XmlFND
     C                   PARM                    XmlLIV
     C                   PARM                    XmlVAL
     C                   PARM                    XMLPR
      *
1    C                   IF        %parms>3
     C                   EVAL      $XmlFND=XmlFND
1e   C                   ENDIF
1    C                   IF        %parms>4
     C                   EVAL      $XmlLIV=XmlLIV
1e   C                   ENDIF
      *
     C                   RETURN    XmlVAL
     P                 E
      *--------------------------------------------------------------*
    RD* Sostituzione caratteri speciali da XML (o HTML)
      *--------------------------------------------------------------*
     PP_RxURL          B
     D P_RxURL         Pi         30000    VARYING
     D  $XmlURL                   30000    CONST VARYING
     D $XmlVAL         S          30000    VARYING
     C                   EVAL      $XmlVAL=$XmlURL
     C                   EVAL      $XmlVAL=P_RxLATE($XmlVAL:'%':'%25')
     C                   EVAL      $XmlVAL=P_RxLATE($XmlVAL:' ':'%20')
     C                   EVAL      $XmlVAL=P_RxLATE($XmlVAL:'§':'%C2%A7')
     C                   EVAL      $XmlVAL=P_RxLATE($XmlVAL:'£':'%C2%A3')
     C                   EVAL      $XmlVAL=P_RxLATE($XmlVAL:'$':'%24')
     C                   EVAL      $XmlVAL=P_RxLATE($XmlVAL:'|':'%7C')
     C                   EVAL      $XmlVAL=P_RxLATE($XmlVAL:'"':'%22')
     C                   RETURN    $XmlVAL
     P                 E
      *--------------------------------------------------------------*
    RD* Sostituzione caratteri speciali da XML (o HTML)
      *--------------------------------------------------------------*
     PP_RxSOS          B
     D P_RxSOS         Pi         32766    VARYING
     D  $XmlSOS                   30000    CONST VARYING
      * Linguaggio nella stringa da sostituire (H=HTML, P=PIPE, other XML)
     D  $StrLang                      1    CONST OPTIONS(*NOPASS)
     D $XmlVAL         S          32766    VARYING
     D $POS            S              5  0
     D $INZ            S              5  0
     D $LEN_XML        S              5  0
     D LEN             S              5  0
     D AAA001          S              1
     D AAA010          S             10    VARYING
     D AAA006          S             06
     D LEN_AMP         S              5  0 INZ(5)
     D LEN_LT          S              5  0 INZ(4)
     D LEN_GT          S              5  0 INZ(4)
     D LEN_APOS        S              5  0 INZ(6)
     D LEN_QUOT        S              5  0 INZ(6)
     D LEN_LF          S              5  0 INZ(5)
     D LEN_CR          S              5  0 INZ(5)
     D LEN_TAB         S              5  0 INZ(5)
     D LEN_PIPE        S              5  0 INZ(10)
      * ESADECIMALE DEL CARATTERE TABULAZIONE EBCDIC
     D C_TAB           C                   X'05'
      * ESADECIMALE DEL CARATTERE CR
     D C_CR            C                   X'0D'
      * ESADECIMALE DEL CARATTERE LF
     D C_LF            C                   X'25'
      *
     C                   EVAL      $XmlVAL=$XmlSOS
     C                   EVAL      $LEN_XML=%LEN($XMLVAL)
1    C                   IF        $LEN_XML=0
     C                   GOTO      G9MAIN
1e   C                   ENDIF
      *
      * sostituzione |
1    C                   IF        %parms>1 and $StrLang='P'
     C                   EVAL      $POS=%SCAN('|':$XmlVAL)
2    C                   IF        $POS>0
     C                   EVAL      AAA001='|'
     C                   EVAL      AAA010='_$_PIPE_$_'
     C                   EVAL      LEN=LEN_PIPE
     C                   EXSR      SR0001
2e   C                   ENDIF
     C                   GOTO      G9MAIN
1e   C                   ENDIF
      *
      * sostituzione &
     C                   EVAL      $POS=%SCAN('&':$XmlVAL)
1    C                   IF        $POS>0
     C                   EVAL      AAA001='&'
     C                   EVAL      AAA010='&amp;'
     C                   EVAL      LEN=LEN_AMP
     C                   EXSR      SR0001_AMP
1e   C                   ENDIF
      * sostituzione >
     C                   EVAL      $POS=%SCAN('>':$XmlVAL)
1    C                   IF        $POS>0
     C                   EVAL      AAA001='>'
     C                   EVAL      AAA010='&gt;'
     C                   EVAL      LEN=LEN_GT
     C                   EXSR      SR0001
1e   C                   ENDIF
      * sostituzione <
     C                   EVAL      $POS=%SCAN('<':$XmlVAL)
1    C                   IF        $POS>0
     C                   EVAL      AAA001='<'
     C                   EVAL      AAA010='&lt;'
     C                   EVAL      LEN=LEN_LT
     C                   EXSR      SR0001
1e   C                   ENDIF
      * sostituzione '
     C                   EVAL      $POS=%SCAN('''':$XmlVAL)
1    C                   IF        $POS>0 and
     C                             ((%parms>1 and $StrLang<>'H') OR
     C                             %parms=1)
     C                   EVAL      AAA001=''''
     C                   EVAL      AAA010='&apos;'
     C                   EVAL      LEN=LEN_APOS
     C                   EXSR      SR0001
1e   C                   ENDIF
      * sostituzione "
     C                   EVAL      $POS=%SCAN('"':$XmlVAL)
1    C                   IF        $POS>0
     C                   EVAL      AAA001='"'
     C                   EVAL      AAA010='&quot;'
     C                   EVAL      LEN=LEN_QUOT
     C                   EXSR      SR0001
1e   C                   ENDIF
      *
      * sostituzione CR
     C                   EVAL      $POS=%SCAN(C_CR:$XmlVAL)
1    C                   IF        $POS>0
     C                   EVAL      AAA001=C_CR
     C                   EVAL      AAA010='&#xD;'
     C                   EVAL      LEN=LEN_CR
     C                   EXSR      SR0001
1e   C                   ENDIF
      *
      * sostituzione LF
     C                   EVAL      $POS=%SCAN(C_LF:$XmlVAL)
1    C                   IF        $POS>0
     C                   EVAL      AAA001=C_LF
     C                   EVAL      AAA010='&#xA;'
     C                   EVAL      LEN=LEN_LF
     C                   EXSR      SR0001
1e   C                   ENDIF
      *
      * sostituzione TAB
     C                   EVAL      $POS=%SCAN(C_TAB:$XmlVAL)
1    C                   IF        $POS>0
     C                   EVAL      AAA001=C_TAB
     C                   EVAL      AAA010='&#x9;'
     C                   EVAL      LEN=LEN_TAB
     C                   EXSR      SR0001
1e   C                   ENDIF
      *
     C     G9MAIN        TAG
     C                   RETURN    $XmlVAL
      *
     C     SR0001        BEGSR
1    C                   DOW       $POS>0
     C                   EVAL      $XmlVAL=%REPLACE(AAA010:$XmlVAL:$POS:01)
     C                   EVAL      $INZ=$POS+LEN
2    C                   IF        $INZ>%LEN($XmlVAL)
     C                   LEAVE
2e   C                   ENDIF
     C                   EVAL      $POS=%SCAN(AAA001:$XmlVAL:$INZ)
1e   C                   ENDDO
     C                   ENDSR

     C     SR0001_AMP    BEGSR
1    C                   DOW       $POS>0
2    C                   SELECT
2x   C                   WHEN      $POS+LEN_AMP-1<=$LEN_XML AND
     C                             %SUBST($XmlVal:$POS:LEN_AMP)='&amp;'
     C                   EVAL      $INZ=$POS+LEN_AMP
2x   C                   WHEN      $POS+LEN_LT-1<=$LEN_XML AND
     C                             %SUBST($XmlVal:$POS:LEN_LT)='&lt;'
     C                   EVAL      $INZ=$POS+LEN_LT
2x   C                   WHEN      $POS+LEN_GT-1<=$LEN_XML AND
     C                             %SUBST($XmlVal:$POS:LEN_GT)='&gt;'
     C                   EVAL      $INZ=$POS+LEN_GT
2x   C                   WHEN      $POS+LEN_APOS-1<=$LEN_XML AND
     C                             %SUBST($XmlVal:$POS:LEN_APOS)='&apos;'
     C                   EVAL      $INZ=$POS+LEN_APOS
2x   C                   WHEN      $POS+LEN_QUOT-1<=$LEN_XML AND
     C                             %SUBST($XmlVal:$POS:LEN_QUOT)='&quot;'
     C                   EVAL      $INZ=$POS+LEN_QUOT
2x   C                   WHEN      $POS+LEN_CR-1<=$LEN_XML AND
     C                             %SUBST($XmlVal:$POS:LEN_CR)='&#xD;'
     C                   EVAL      $INZ=$POS+LEN_CR
2x   C                   WHEN      $POS+LEN_LF-1<=$LEN_XML AND
     C                             %SUBST($XmlVal:$POS:LEN_LF)='&#xA;'
     C                   EVAL      $INZ=$POS+LEN_LF
2x   C                   WHEN      $POS+LEN_TAB-1<=$LEN_XML AND
     C                             %SUBST($XmlVal:$POS:LEN_TAB)='&#x9;'
     C                   EVAL      $INZ=$POS+LEN_TAB
2x   C                   OTHER
     C                   EVAL      $XmlVAL=%REPLACE(AAA010:$XmlVAL:$POS:01)
     C                   EVAL      $INZ=$POS+LEN
     C                   EVAL      $LEN_XML=%LEN($XmlVal)
2e   C                   ENDSL
2    C                   IF        $INZ>$LEN_XML
     C                   LEAVE
2e   C                   ENDIF
     C                   EVAL      $POS=%SCAN(AAA001:$XmlVAL:$INZ)
1e   C                   ENDDO
     C                   ENDSR
     P                 E
      *--------------------------------------------------------------*
    RD* Sostuisce stringa in carattere
      *--------------------------------------------------------------*
     PP_RxSOC          B
     D P_RxSOC         Pi         30000    VARYING
     D  $XmlSOC                   30000    CONST VARYING
      * Linguaggio nella stringa da sostituire (H=HTML, P=PIPE, other XML)
     D  $StrLang                      1    CONST OPTIONS(*NOPASS)
     D $XmlVAL         S          30000    VARYING
      *
     D $POS            S              5  0
     D $INZ            S              5  0
     D $SUBLEN         S              5  0
     D AAA001          S             10    VARYING
     D AAA010          S             01
      * ESADECIMALE DEL CARATTERE TABULAZIONE EBCDIC
     D C_TAB           C                   X'05'
      * ESADECIMALE DEL CARATTERE CR
     D C_CR            C                   X'0D'
      * ESADECIMALE DEL CARATTERE LF
     D C_LF            C                   X'25'
      *
     C                   EVAL      $XmlVAL=$XmlSOC
1    C                   IF        %LEN($XMLVAL)=0
     C                   GOTO      G9MAIN
1e   C                   ENDIF
     C                   EVAL      $INZ=1
      *
      *
1    C                   IF        %SCAN('&':$XmlVAL)>0
      *
2    C                   IF        %SCAN('&quot;':$XmlVAL)>0
     C                   EVAL      AAA001='&quot;'
     C                   EVAL      AAA010='"'
     C                   EXSR      SR0001
2e   C                   ENDIF
2    C                   IF        %SCAN('&amp;':$XmlVAL)>0
     C                   EVAL      AAA001='&amp;'
     C                   EVAL      AAA010='&'
     C                   EXSR      SR0001
2e   C                   ENDIF
2    C                   IF        %SCAN('&lt;':$XmlVAL)>0
     C                   EVAL      AAA001='&lt;'
     C                   EVAL      AAA010='<'
     C                   EXSR      SR0001
2e   C                   ENDIF
2    C                   IF        %SCAN('&gt;':$XmlVAL)>0
     C                   EVAL      AAA001='&gt;'
     C                   EVAL      AAA010='>'
     C                   EXSR      SR0001
2e   C                   ENDIF
2    C                   IF        %SCAN('&apos;':$XmlVAL)>0 AND
     C                             ((%parms>1 and $StrLang<>'H') OR
     C                             %parms=1)
     C                   EVAL      AAA001='&apos;'
     C                   EVAL      AAA010=''''
     C                   EXSR      SR0001
2e   C                   ENDIF
1    C                   IF        %SCAN('&#xD;':$XmlVAL)>0
     C                   EVAL      AAA001='&#xD;'
     C                   EVAL      AAA010=C_CR
     C                   EXSR      SR0001
1e   C                   ENDIF
1    C                   IF        %SCAN('&#xA;':$XmlVAL)>0
     C                   EVAL      AAA001='&#xA;'
     C                   EVAL      AAA010=C_LF
     C                   EXSR      SR0001
1e   C                   ENDIF
1    C                   IF        %SCAN('&#x9;':$XmlVAL)>0
     C                   EVAL      AAA001='&#x9;'
     C                   EVAL      AAA010=C_TAB
     C                   EXSR      SR0001
1e   C                   ENDIF
      *
0e   C                   ENDIF
      *
      * sostituzione |
1    C                   IF        %parms>1 and $StrLang='P'
2    C                   IF        %SCAN('_$_PIPE_$_':$XmlVAL)>0
     C                   EVAL      AAA001='_$_PIPE_$_'
     C                   EVAL      AAA010='|'
     C                   EXSR      SR0001
2e   C                   ENDIF
1e   C                   ENDIF
      *
     C     G9MAIN        TAG
     C                   RETURN    $XmlVAL
      *
     C     SR0001        BEGSR
     C                   EVAL      AAA001=%TRIM(AAA001)
     C                   EVAL      $POS=%SCAN(AAA001:$XmlVAL:01)
0    C                   DOW       $POS>0
     C                   EVAL      $SUBLEN=%LEN(AAA001)
     C                   EVAL      $XmlVAL=%REPLACE(AAA010:$XmlVAL:$POS:$SUBLEN)
     C                   EVAL      $INZ=$POS+%LEN(AAA010)
1    C                   IF        $INZ>%LEN($XmlVAL)
     C                   LEAVE
1e   C                   ENDIF
     C                   EVAL      $POS=%SCAN(AAA001:$XmlVAL:$INZ)
0e   C                   ENDDO
     C                   ENDSR
     P                 E
      *--------------------------------------------------------------*
    RD* Trasformo stringa in stringa
      *--------------------------------------------------------------*
     PP_RxLATE         B
     D P_RxLate        Pi         32766    VARYING
     D  $XmlInp                   30000    CONST VARYING
     D  $XmlS01                   30000    CONST VARYING
     D  $XmlS02                   30000    CONST VARYING
     D  $SosSing                      1    CONST OPTIONS(*NOPASS)
     D  $SosCase                      1    CONST OPTIONS(*NOPASS)
      * Variabili locali
     D $StrO           S          32766    VARYING
     D $Str1           S          30000    VARYING
     D $Str2           S          30000    VARYING
     D $StrFix         S          30000
     D $StrSos         S              1    VARYING
     D $StrCas         S              1    VARYING
     D $I              S              5  0
     D $LS1            S              5  0
     D $LS2            S              5  0
     D $LSO            S              5  0
      *
     D UStrO           S          32766    VARYING
     D UStr1           S          30000    VARYING
      *
      *
     C                   EVAL      $StrO=$XmlInp
     C                   EVAL      $Str1=$XmlS01
     C                   EVAL      $Str2=$XmlS02
      *
0    C                   IF        %parms>3 AND $SosSing='1'
     C                   EVAL      $StrSos='1'
0x   C                   ELSE
     C                   EVAL      $StrSos=*BLANKS
0e   C                   ENDIF
0    C                   IF        %parms>4 AND $SosCase='1'
     C                   EVAL      $StrCas='1'
0x   C                   ELSE
     C                   EVAL      $StrCas=*BLANKS
     C                   ENDIF
      * Solo se ricevuto
0    C                   IF        %Len($StrO)>0 AND %Len($Str1)>0 AND
     C                             %Len($Str1)<=%Len($StrO)
     C                   EVAL      $I=1
     C                   EVAL      $LS1=%Len($Str1)
     C                   EVAL      $LS2=%Len($Str2)
      * Se richiesto di non controllare i caratteri maiuscoli/minuscoli
     C                   IF        $StrCas='1'
      *
     C                   IF        $LS1<=1024
     C                   EVAL      £G49FU='CON'
     C                   EVAL      £G49ME='U_C'
     C                   EVAL      £G49SI=$STR1
     C                   EXSR      £G49
     C                   EVAL      USTR1=£G49SI
     C                   ELSE
     C                   EVAL      $StrFix=$STR1
     C                   CLEAR                   USTR1
     C                   DO        30            $X                5 0
     C                   IF        %SUBST($StrFix:(1024*($X-1))+1)=' '
     C                   LEAVE
     C                   ENDIF
     C                   EVAL      £G49FU='CON'
     C                   EVAL      £G49ME='U_C'
     C                   IF        $X<30
     C                   EVAL      £G49SI=%SUBST($StrFix:(1024*($X-1))+1
     C                             :1024)
     C                   ELSE
     C                   EVAL      £G49SI=%SUBST($StrFix:(1024*($X-1))+1
     C                             :1024)
     C                   ENDIF
     C                   EXSR      £G49
     C                   ENDDO
     C                   ENDIF
     C                   EVAL      USTR1=%SUBST(USTR1:1:$LS1)
      *
     C                   EVAL      $LSO=%Len($StrO)
     C                   IF        $LSO<=1024
     C                   EVAL      £G49FU='CON'
     C                   EVAL      £G49ME='U_C'
     C                   EVAL      £G49SI=$STRO
     C                   EXSR      £G49
     C                   EVAL      USTRO=£G49SI
     C                   ELSE
     C                   EVAL      $StrFix=$STRO
     C                   CLEAR                   USTRO
     C                   DO        30            $X                5 0
     C                   IF        %SUBST($StrFix:(1024*($X-1))+1)=' '
     C                   LEAVE
     C                   ENDIF
     C                   EVAL      £G49FU='CON'
     C                   EVAL      £G49ME='U_C'
     C                   IF        $X<30
     C                   EVAL      £G49SI=%SUBST($StrFix:(1024*($X-1))+1
     C                             :1024)
     C                   ELSE
     C                   EVAL      £G49SI=%SUBST($StrFix:(1024*($X-1))+1
     C                             :1024)
     C                   ENDIF
     C                   EXSR      £G49
     C                   EVAL      USTRO=USTRO+%SUBST(£G49SI:1:1024)
     C                   ENDDO
     C                   ENDIF
     C                   EVAL      USTRO=%SUBST(USTRO:1:$LSO)
      *
     C                   ENDIF
      *
      *. Ricerco la stringa
1    C                   DO        *HIVAL
     C                   IF        $STRCAS<>''
     C                   EVAL      $I=%SCAN(UStr1:UStrO:$I)
     C                   ELSE
     C                   EVAL      $I=%SCAN($Str1:$StrO:$I)
     C                   ENDIF
2    C                   IF        $I=0
     C                   LEAVE
2e   C                   ENDIF
      *. Eseguo la sostituzione
     C                   EVAL      $StrO=%REPLACE($Str2:$StrO:$I:$LS1)
     C                   IF        $STRCAS<>''
     C                   EVAL      UStrO=%REPLACE($Str2:UStrO:$I:$LS1)
     C                   ENDIF
      *
      *. Se devo eseguire una sola sostituzione esco
2    C                   IF        $StrSos='1'
     C                   LEAVE
2e   C                   ENDIF
      *. Sposto il puntatore
     C                   EVAL      $I=$I+$LS2
2    C                   IF        $I>%Len($StrO)
     C                   LEAVE
2e   C                   ENDIF
1e   C                   ENDDO
0e   C                   ENDIF
      *
     C                   RETURN    $StrO
      *
     D/INCLUDE QILEGEN,£G49
      *
     P                 E
      *--------------------------------------------------------------*
    RD* Restituisce la lista degli attributi e dei valori di un elemento
      *--------------------------------------------------------------*
     PP_RxATV          B
     D P_RxATV         Pi           306    DIM(200)
     D Xml_Str                    30000    VARYING
     D  $XmlPRG                       5  0 OPTIONS(*NOPASS)
     D Xml_Str_T       S          30000    VARYING
     D SAtv            S            306    DIM(200)
     D $I              S              5  0
     D $F              S              5  0
     D $X              S              5  0
     D NATV            S              5  0
      *
      *Salvo la stringa XML ricevuta che altrimenti tornerebbe tagliata
     C                   EVAL      Xml_Str_T=P_RxSOC(Xml_Str)
      * NATV è l'indice della schiera SAtv (l'output)
     C                   EVAL      NATV=0
     C                   CLEAR                   SAtv
      *
0    C                   DO        *HIVAL
      * Un XML valido deve avere min 4 caratteri ES A="" e la schiera SAtv ha 200 elementi
1    C                   IF        %LEN(Xml_Str_T)<4 OR NATV=200
     C                   LEAVE
1e   C                   ENDIF
      * cerco gli attributi solo del primo tag; l'attributo deve essere seguito da =" e dal suo
      * valore Es Expanded="Yes"; con Expanded= "Yes" l'attributo in questione e il suo valore NON
      * VENGONO RESTITUITI
     C                   EVAL      $I=%SCAN('="':Xml_Str_T)
     C                   EVAL      $X=%SCAN('>':Xml_Str_T)
      * se trovo una chiusura di tag prima dell'attributo esco perchè mi han passato più tag in fila
      * ES <SecList><Sec Lev="0" Frm="GRA_EMU" Code="General">
1    C                   IF        $X<$I AND $X>0
     C                   LEAVE
1e   C                   ENDIF
      * se non trovo l'inizio di nessun Atrributo, esco
1    C                   IF        $I=0 OR $I=%LEN(%TRIMR(Xml_Str_T))-1
     C                   LEAVE
1e   C                   ENDIF
      * se il TAG è preceduto dal suo nome non lo devo considerare ES <Setup Attr1="Valo"/>
     C                   EVAL      $X=%SCAN(' ':Xml_Str_T)
1    C                   IF        $X<$I AND $X<>0
      * se dopo aver tolto il nome la lunghezza dell'XML è Scesa sotto alla lunghezza minima esco
2    C                   IF        $X+4>%LEN(Xml_Str_T)
     C                   LEAVE
2x   C                   ELSE
      * altrimenti tolgo il nome del tag dall'inizio della stringa e ricomincio da capo
     C                   EVAL      Xml_Str_T=%SUBST(Xml_Str_T:$X+1)
     C                   ITER
2e   C                   ENDIF
1e   C                   ENDIF
      * se non trovo la fine di nessun Atrributo, esco
     C                   EVAL      $F=%SCAN('"':Xml_Str_T:$I+2)
1    C                   IF        $F=0
     C                   LEAVE
1e   C                   ENDIF
      * a questo punto valorizzo la schiera SAtv con NOME    VALORE
     C                   EVAL      NATV=NATV+1
      * estraggo il nome dell'attributo
     C                   EVAL      %SUBST(SAtv(NAtv):01:50)
     C                             =%TRIM(%SUBST(Xml_Str_T:1:$I-1))
      * estraggo il suo valore
     C                   EVAL      %SUBST(SAtv(NAtv):51)=
     C                             %TRIMR(%SUBST(Xml_Str_T:$I+2:$F-2-$I))
      * se dopo aver tolto l'attr e il valore la lunghezza dell'XML è scesa sotto 4 esco
1    C                   IF        $F+4>%LEN(Xml_Str_T)
     C                   LEAVE
1e   C                   ENDIF
     C
      * e tolgo dalla stringa il TAG estratto
     C                   EVAL      Xml_Str_T=%TRIMR(%SUBST(Xml_Str_T:$F+1))
      *
0e   C                   ENDDO
      * Restituisce matrice
0    C                   IF        %parms=2
     C                   EVAL      $XmlPRG=NAtv
0e   C                   ENDIF
     C                   RETURN    SAtv
     P                 E
      *--------------------------------------------------------------*
    RD* Restituisce la lista degli attributi e dei valori di un elemento di documentazione attiva
      *--------------------------------------------------------------*
     PP_RxATP          B
     D P_RxATP         Pi          1055    DIM(200)
     D Xml_StrP                   30000    VARYING
     D  $XmlPRGp                      5  0 OPTIONS(*NOPASS)
     D Xml_Str_T       S          30000    VARYING
     D                 DS
     D$JAXATP                      1055    DIM(200)
     D $JAXATP_A                     50    OVERLAY($JAXATP:1)
     D $JAXATP_V                   1000    OVERLAY($JAXATP:*NEXT)
     D $JAXATP_L                      5  0 OVERLAY($JAXATP:*NEXT)
     D $I              S              5  0
     D §I              S              5  0
     D $F              S              5  0
     D $X              S              5  0
     D $LIV            S              5  0
     D NATP            S              5  0
      *
      *Salvo la stringa XML ricevuta che altrimenti tornerebbe tagliata
     C                   EVAL      Xml_Str_T=P_RxSOC(Xml_StrP)
      * NATP è l'indice della schiera di output
     C                   EVAL      NATP=0
     C                   CLEAR                   $JAXATP
      *
     C                   EVAL      $LIV=0
0    C                   DO        *HIVAL
      * Un XML valido deve avere min 3 caratteri ES A() e la schiera ha 200 elementi
1    C                   IF        %LEN(Xml_Str_T)<3 OR NATP=200
     C                   LEAVE
1e   C                   ENDIF
      * cerco gli attributi solo del primo tag;
     C                   EVAL      $I=%SCAN('(':Xml_Str_T)
     ‚*C                   EVAL      $F=%SCAN(')':Xml_Str_T)
     ‚*C                   IF        $F<$I AND $F>0 AND $I>0
     ‚* * se c'è una parentesi chiusa prima della parentesi aperta decremento il livello
     ‚*C                   EVAL      $LIV=$LIV-1
     ‚*C                   ENDIF
      * se non trovo l'inizio di nessun Atrributo, esco
     ‚*C                   IF        $I=0 OR $I=%LEN(%TRIM(Xml_Str_T))-1
     ‚*C                   IF        $I=0 OR $I>=%LEN(%TRIM(Xml_Str_T))-1
1    C                   IF        $I=0 OR $I>=%LEN(%TRIMR(Xml_Str_T))
     C                   LEAVE
1e   C                   ENDIF
1    C                   FOR       §I=$I DOWNTO 1
2    C                   IF        %SUBST(Xml_Str_T:§I:1)=')'
     C                   EVAL      $LIV=$LIV-1
2e   C                   ENDIF
1e   C                   ENDFOR
     C                   EVAL      $LIV=$LIV+1
1    C                   FOR       §I=$I DOWNTO 1
2    C                   IF        %SUBST(Xml_Str_T:§I:1)=' ' OR
     C                             %SUBST(Xml_Str_T:§I:1)=')'
     C                   LEAVE
2e   C                   ENDIF
1e   C                   ENDFOR
1    C                   IF        §I=0
     C                   EVAL      §I=1
1e   C                   ENDIF
      * a questo punto valorizzo la schiera con NOME, VALORE e LIVELLO
     C                   EVAL      NATP=NATP+1
      * estraggo il nome dell'attributo
     C                   EVAL      $JAXATP_A(NATP)=
     C                                       %TRIM(%SUBST(Xml_Str_T:§I:$I-§I))
      * Se il nome dell'attributo è vuoto porto indietro il contatore
1    C                   IF        $JAXATP_A(NATP)=''
     C                   EVAL      NATP=NATP-1
1x   C                   ELSE
      * estraggo il suo valore
     C                   EVAL      $JAXATP_V(NATP)=P_RXATT(Xml_Str_T:
     C                             %TRIMR($JAXATP_A(NATP))+'(':' ')
      * livello
     C                   EVAL      $JAXATP_L(NATP)=$LIV
1e   C                   ENDIF
      * e tolgo dalla stringa il TAG estratto
     C                   EVAL      Xml_Str_T=%TRIMR(%SUBST(Xml_Str_T:$I+1))
      *
0e   C                   ENDDO
      * Restituisce matrice
0    C                   IF        %parms=2
     C                   EVAL      $XmlPRGp=NAtp
0e   C                   ENDIF
     C                   RETURN    $JAXATP
     P                 E
      **-------------------------------------------------------------*
    RD* Restituisce il contenuto dell'elemento richiesto
      *--------------------------------------------------------------*
     PP_RxELE          B
     D P_RxELE         Pi         30000    VARYING
     D Xml_Fnd                      512    VARYING CONST
     D Xml_Met                       10    CONST
      * .livello di chiamata
      * ... livelli 48-50 riservati £JAY
     D Xml_Liv                        2  0 CONST
     D Xml_Str                    30000    VARYING
     D NodIni                         5  0 OPTIONS(*NOPASS)
     D NodLen                         5  0 OPTIONS(*NOPASS)
     D Xml_Con                    30000    OPTIONS(*NOPASS) VARYING
      * . Cerca tag anche all'interno di un CDATA
     D SEACDATA                       1    OPTIONS(*NOPASS)
      *
     D $Xml_Out        S          30000    VARYING
     D $NodIni         S              5  0
     D $NodLen         S              5  0
     D $Xml_Con        S          30000    VARYING
     D $Parms          S              2  0
     D $Xml_Fnd        S            512    VARYING
     D $Xml_Met        S             10
     D $Xml_Liv        S              2  0
     D $SCDATA         S              1
      *
     C                   EVAL      $Parms=%parms
     C                   EVAL      $Xml_Fnd=Xml_Fnd
     C                   EVAL      $Xml_Met=Xml_Met
     C                   EVAL      $Xml_Liv=Xml_Liv
1    C                   IF        %parms>7
     C                   EVAL      $SCDATA=SEACDATA
     C                   ELSE
     C                   CLEAR                   $SCDATA
     C                   ENDIF
      *
     C                   CALL      'JAJAX0'
     C                   PARM                    $Xml_Fnd
     C                   PARM                    $Xml_Met
     C                   PARM                    $Xml_Liv
     C                   PARM                    Xml_Str
     C                   PARM                    $Xml_Out
     C                   PARM                    £JSP
     C                   PARM                    $NodIni
     C                   PARM                    $NodLen
     C                   PARM                    $Xml_Con
     C                   PARM                    $Parms
     C                   PARM                    $SCDATA
      *
1    C                   IF        %parms>4
     C                   EVAL      NodIni=$NodIni
1e   C                   ENDIF
1    C                   IF        %parms>5
     C                   EVAL      NodLen=$NodLen
1e   C                   ENDIF
1    C                   IF        %parms>6
     C                   EVAL      Xml_Con=$Xml_Con
1e   C                   ENDIF
      *
     C                   RETURN    $Xml_Out
     P                 E
      *--------------------------------------------------------------*
    RD* Restituisce stringa splittata su più righe
      *--------------------------------------------------------------*
     PP_RxSPL          B
     D P_RxSPL         Pi         30000    VARYING
     D $String                    30000    CONST VARYING
     D $Split                         1    CONST
     D $StrOut         S          30000    VARYING
     D $S              S              5  0                                      lungh.stringa
     D $SS             S              5  0                                      primo spazio sx
     D $SSP            S              1                                         sx: punto
     D $SD             S              5  0                                      primo spazio dx
     D $SDP            S              1                                         sd: punto
     D $SP             S              5  0                                      puntatore
      *
     C                   EVAL      $StrOut=$String
     C                   EVAL      $S=%LEN(%TRIM($StrOut))
      *
0    C                   SELECT
      * 1 SPLIT, vicino a metà
0x   C                   WHEN      $Split='1'
      * . Cerca primo spazio a sx e dx di metà stringa
1    C                   DO        *HIVAL        $SP
2    C                   IF        $SP>$S
     C                   LEAVE
2e   C                   ENDIF
2    C                   IF        %SUBST($StrOut:$SP:1)='' OR
     C                             %SUBST($StrOut:$SP:1)='.'
      * . a sinistra
3    C                   IF        $SP<=($S/2)
     C                   EVAL      $SS=$SP
4    C                   IF        %SUBST($StrOut:$SP:1)='.'
     C                   EVAL      $SSP='1'
4e   C                   ENDIF
3e   C                   ENDIF
      * . a destra
3    C                   IF        $SP>($S/2) AND $SD=0
     C                   EVAL      $SD=$SP
4    C                   IF        %SUBST($StrOut:$SP:1)='.'
     C                   EVAL      $SDP='1'
4e   C                   ENDIF
     C                   LEAVE
3e   C                   ENDIF
2e   C                   ENDIF
1e   C                   ENDDO
      *
      * . Verifica dove sostituire/introdurre il '|'
     C                   CLEAR                   DOVE              1
1    C                   SELECT
      * . . c'è solo a sx
1x   C                   WHEN      $SS<>0 AND $SD=0
     C                   EVAL      DOVE='S'
      * . . c'è solo a dx
1x   C                   WHEN      $SD<>0 AND $SS=0
     C                   EVAL      DOVE='D'
     C                   EVAL      %SUBST($StrOut:$SD:1)='|'
      * . . c'è da entrambe le parti: il più vicino al centro (privilegiando a dx)
1x   C                   WHEN      $SD<>0 AND $SS<>0
2    C                   IF        ($S-$SS)>($SD-$S)
     C                   EVAL      DOVE='S'
2x   C                   ELSE
     C                   EVAL      DOVE='D'
2e   C                   ENDIF
1e   C                   ENDSL
      *
      * . Introduce/sostituisce
1    C                   SELECT
      * . . a sinistra
1x   C                   WHEN      DOVE='S'
2    C                   IF        $SSP=*BLANKS
     C                   EVAL      %SUBST($StrOut:$SS:1)='|'                    sostituisce
2x   C                   ELSE
     C                   EVAL      $StrOut=%SUBST($StrOut:1:$SS)+'|'+           aggiunge
     C                             %SUBST($StrOut:$SS+1)
2e   C                   ENDIF
      * . . a destra
1x   C                   WHEN      DOVE='D'
2    C                   IF        $SDP=*BLANKS
     C                   EVAL      %SUBST($StrOut:$SD:1)='|'                    sostituisce
2x   C                   ELSE
     C                   EVAL      $StrOut=%SUBST($StrOut:1:$SD-1)+'|'+         aggiunge
     C                             %SUBST($StrOut:$SD)
2e   C                   ENDIF
1e   C                   ENDSL
      *
      *
0e   C                   ENDSL
      *
      *
      * Restituisce VALORE
     C                   RETURN    $StrOut
     P                 E

