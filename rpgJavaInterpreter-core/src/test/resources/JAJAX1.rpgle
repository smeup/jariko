   COP*  OPTIMIZE(*FULL)
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 05/10/16  V5R1   BMA Creazione
     V* 12/06/17  V5R1   BMA Tolgo marcatori testo se al primo livello
     V* 20/07/17  V5R1   BMA Correzione modifica precedente
     V* 13/11/17  V5R1   BMA Ottimizzazione
     V* 13/11/17  V5R1   BMA Aggiunto OPTIMIZE(*FULL)
     V* 05/02/17  V5R1    CM Modificato comportamento flag trovato attributo
     V*                      Se presente il Tag, anche senza valore ritorna *OFF
     V*                      se assente restituisce *ON
     V*=====================================================================
     V* Revisione ed esternalizzazione P_RxATT
     V*=====================================================================
     H/COPY QILEGEN,£INIZH
      *---------------------------------------------------------------
     I/COPY QILEGEN,£TABB£1DS
     I/COPY QILEGEN,£PDS
     D  $XmlTAG        S          30000    VARYING
     D  $XmlATT        S             64    VARYING
     D  $XmlASS        S             15
     D  $XmlFND        S              1N
     D  $XmlLIV        S              1
     D  $XMLPR         S              5I 0
     D   XmlTAG        S          30000    VARYING
     D ATTRIC          S             66    VARYING
     D $XmlVAL         S          30000    VARYING
     D TXmlVAL         S          30000    VARYING
     D BLKSTR          S            100    VARYING
     D $I              S              5  0
     D $F              S              5  0
     D $L              S              5  0
     D $M              S              5  0
     D $P              S              5  0
     D $K              S              5  0
     D $A              S              5  0
     D $C              S              5  0
     D $D              S              5  0
     D $X              S              5  0
     D $Y              S              5  0
     D $T              S              5  0
     D $E              S              5  0
     D VALO            S             74
     D $$STXT          S              1N
     D FNDTXT          S              1N
     D INI010          S             10
     D FIN010          S             10
     D NUMERI          C                   CONST('0123456789')
     D ALFMAX          C                   CONST('ABCDEFGHIJKLMNOPQRSTUVWXYZ')  COSTANTE
     D ALFMIN          C                   CONST('abcdefghijklmnopqrstuvwxyz')  COSTANTE
     D SPECIA          C                   CONST('§£$_-&*./@#%')                COSTANTE
      *---------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------
     C     *ENTRY        PLIST
     C                   PARM                    $XmlTAG
     C                   PARM                    $XmlATT
     C                   PARM                    $XmlASS
     C                   PARM                    $XmlFND
     C                   PARM                    $XmlLIV
     C                   PARM                    $XmlVAL
     C                   PARM                    $XMLPR
      *
     C                   EVAL      FNDTXT=*OFF
     C                   EVAL      XmlTAG=%TRIM($XmlTAG)
      *metto in ATTRIC l'attributo passato più "(" (se non già presente)
1    C                   IF        %SCAN('(':$XmlATT)=0
     C                   EVAL      ATTRIC=%TRIM($XmlATT)+'('
1x   C                   ELSE
     C                   EVAL      ATTRIC=%TRIM($XmlATT)
1e   C                   ENDIF
      *
     C                   EVAL      $L=%LEN(ATTRIC)
     C                   EVAL      $I=%SCAN(ATTRIC:XmlTAG)
      *
1    C                   IF        $XMLPR>4 AND $I>1
      * Se previsto controllo livello ed ho trovato l'attributo verifico se trovo
      * prima una parentesi aperta o una parentesi chiusa o nessuna delle due
2    C                   IF        $XmlLIV='1'
     C                   Z-ADD     1             $C
      *
     C                   EVAL      $M=%LEN(XmlTAG)
     C                   EVAL      $$STXT=*OFF
3    C                   DO        *HIVAL
     C                   CLEAR                   $A                5 0
     C                   EVAL      $D=$C+$I-1
4    C                   DO        $D            $X
      *. Esistenza testo
5    C                   IF        $X+9 < $M
6    C                   SELECT
      *. Inizio
6x   C                   WHEN      %SUBST(XMLTAG:$X:10)='_$_STXT_$_'
      * non tolgo i tag (inoltre sbagliava poi il conteggio parentesi
      *C                   IF        $X+10 <= $M
      *C                   EVAL      %SUBST(XMLTAG:$X)=%SUBST(XMLTAG:$X+10)
      *C                   ELSE
      *C                   EVAL      %SUBST(XMLTAG:$X:10)=''
      *C                   ENDIF
      *C                   EVAL      $X=$X-1
      *C                   EVAL      $M=%LEN(XmlTAG)
     C                   EVAL      $$STXT=*ON
     C                   EVAL      FNDTXT=*ON
      *. Fine
6x   C                   WHEN      %SUBST(XMLTAG:$X:10)='_$_ETXT_$_'
      * non tolgo i tag (inoltre sbagliava poi il conteggio parentesi
      *C                   IF        $X+10 <= $M
      *C                   EVAL      %SUBST(XMLTAG:$X)=%SUBST(XMLTAG:$X+10)
      *C                   ELSE
      *C                   EVAL      %SUBST(XMLTAG:$X:10)=''
      *C                   ENDIF
      *C                   EVAL      $M=%LEN(XmlTAG)
     C                   EVAL      $$STXT=*OFF
6e   C                   ENDSL
5e   C                   ENDIF
      *
5    C                   SELECT
5x   C                   WHEN      %SUBST(XMLTAG:$X:1)='(' AND $$STXT = *OFF
     C                   EVAL      $A=$A+1
5x   C                   WHEN      %SUBST(XMLTAG:$X:1)=')' AND $$STXT = *OFF
     C                   EVAL      $A=$A-1
5e   C                   ENDSL
4e   C                   ENDDO
4    C                   IF        $A=0
     C                   EVAL      $I=$I+$C-1
     C                   LEAVE
4e   C                   ENDIF
     C                   EVAL      $C=$I+$C+1
     C                   EVAL      $I=%SCAN(ATTRIC:%SUBST(XmlTAG:$C))
4    C                   IF        $I=0
     C                   LEAVE
4e   C                   ENDIF
3e   C                   ENDDO
      *
2e   C                   ENDIF
1e   C                   ENDIF
      *
     C                   CLEAR                   $P
      * $M contiene la lunghezza totale del TAG
      * $K contiene la posizione iniziale del VALORE
     C*
     C                   EVAL      $M=%LEN(XmlTAG)
     C                   EVAL      $K=$I+$L
     C                   EXSR      CNTPAR
      * $I Contiene sempre l'inzio del TAG
      * $F Contiene sempre la posizione della ) di chiusura del Valore del TAG
      * $L Contiene la lunghezza senza spazi del nome del TAG più il carattere (
1    C                   SELECT
1x   C                   WHEN      $I=0 OR $F=0
      *                  se non trovo l'inizio o la fine esco
     C                   CLEAR                   $XMLVAL
1x   C                   WHEN      $I=1                                         INIZIO stringa
      * se il tag è il primo e in prima posizione lo estraggo direttamente
     C                   EVAL      $XMLVAL=%SUBST(XmlTAG:$L+1:$F-$L-1)
1x   C                   OTHER
      * se il tag non è in prima posizione deve essere preceduto da un carattere che non sia
      * numero o lettera
2    C                   DO        *HIVAL
3    C                   IF        %SCAN(%SUBST(XmlTAG:$I-1:1):VALO)=0
     C                   EVAL      $XMLVAL=%SUBST(XmlTAG:$I+$L:$F-$L-$I)
     C                   LEAVE
3x   C                   ELSE
      * se prima del tag non c'è uno spazio lo cerco di nuovo nella stringa
      * ES:       cerco 'AA' e xml= <Setup BAA='1' AA='2'>
4    C                   IF        $F+2>=%LEN(XmlTAG)
     C                   CLEAR                   $XMLVAL
     C                   LEAVE
4e   C                   ENDIF
     C                   EVAL      $I=%SCAN(ATTRIC:XmlTAG:$F+2)
     C                   EVAL      $K=$I+$L
     C                   EXSR      CNTPAR
4    C                   IF        $I=0 OR $F=0
      * se non trovo l'inizio o la fine esco
     C                   CLEAR                   $XMLVAL
     C                   LEAVE
4e   C                   ENDIF
3e   C                   ENDIF
2e   C                   ENDDO
1e   C                   ENDSL
      * Restituisce se il valore è stato trovato
1    C                   IF        $XMLPR>3
2    C*                  IF        $XmlVal <> ''
2    C                   IF        %SCAN(%TRIM(ATTRIC):XMLTAG) > 0
     C                   EVAL      $XmlFND=*OFF
2x   C                   ELSE
     C                   EVAL      $XmlFND=*ON
2e   C                   ENDIF
1e   C                   ENDIF
      * Se è presente da qualche parte il tag di incapsulamento testo, verifico se è al primo
      * livello per toglierlo
1    C                   IF        FNDTXT=*ON
     C                   EVAL      TXmlVAL=%TRIM($XmlVAL)
     C                   EVAL      $T=%LEN(TXmlVal)
2    C                   IF        $T>=20
     C                   EVAL      INI010=%SUBST(TXmlVAL:01:10)
     C                   EVAL      FIN010=%SUBST(TXmlVAL:$T-10+1)
3    C                   IF        INI010='_$_STXT_$_'
     C                             AND FIN010='_$_ETXT_$_'
     C                   CLEAR                   BLKSTR
3    C                   FOR       $Y=1 TO %LEN($XmlVAL)
     C                   IF        %SUBST($XmlVAL:$Y:10)='_$_STXT_$_'
     C                   EVAL      $XmlVAL=BLKSTR+%SUBST($XmlVAL:$Y+10)
     C                   LEAVE
     C                   ELSE
     C                   EVAL      BLKSTR=BLKSTR+' '
     C                   ENDIF
     C                   ENDFOR
     C                   CLEAR                   BLKSTR
3    C                   FOR       $Y=%LEN($XmlVAL)-9 BY 1 DOWNTO 1
     C                   IF        %SUBST($XmlVAL:$Y:10)='_$_ETXT_$_'
      *C                   EVAL      $XmlVAL=BLKSTR+%SUBST($XmlVAL:01:$Y-1)
     C                   EVAL      $XmlVAL=%SUBST($XmlVAL:01:$Y-1)+BLKSTR
     C                   LEAVE
     C                   ELSE
     C                   EVAL      BLKSTR=BLKSTR+' '
     C                   ENDIF
     C                   ENDFOR
3e   C                   ENDIF
2e   C                   ENDIF
2e   C                   ENDIF
      * Restituisce VALORE o il valore assunto se il valore è bianco
2    C                   IF        $XmlVal=*BLANKS
     C                   EVAL      $XmlVal=$XmlASS
2e   C                   ENDIF
      *
     C                   SETON                                        RT
      *---------------------------------------------------------------
     C/COPY QILEGEN,£INZSR
      *--------------------------------------------------------------------------*
    RD* conta le aperture e le chiusure delle parentesi e restituisce la posizione
    RD* dell'ultima se c'è corrispondenza tra "(" e ")", altrimenti 0
      *--------------------------------------------------------------------------*
     C     CNTPAR        BEGSR
2    C                   IF        $K>1
     C                   EVAL      $K=$K-1
2e   C                   ENDIF
     C                   EVAL      $$STXT=*OFF
      *
2    C     $K            DO        $M            $F
      *. Esistenza testo
3    C                   IF        $F+9 < $M
4    C                   SELECT
      *.. Inizio
4x   C                   WHEN      %SUBST(XMLTAG:$F:10)='_$_STXT_$_'
      * non tolgo i tag (inoltre sbagliava poi il conteggio parentesi
      *C                   IF        $F+10 <= $M
      *C                   EVAL      %SUBST(XMLTAG:$F)=%SUBST(XMLTAG:$F+10)
      *C                   ELSE
      *C                   EVAL      %SUBST(XMLTAG:$F)=''
      *C                   ENDIF
      *C                   EVAL      $F=$F-1
      *C                   EVAL      $M=%LEN(%TRIM(XmlTAG))
     C                   EVAL      $$STXT=*ON
     C                   EVAL      FNDTXT=*ON
      *.. Fine
4x   C                   WHEN      %SUBST(XMLTAG:$F:10)='_$_ETXT_$_'
      * non tolgo i tag (inoltre sbagliava poi il conteggio parentesi
      *C                   IF        $F+10 <= $M
      *C                   EVAL      %SUBST(XMLTAG:$F)=%SUBST(XMLTAG:$F+10)
      *C                   ELSE
      *C                   EVAL      %SUBST(XMLTAG:$F)=''
      *C                   ENDIF
      *C                   EVAL      $M=%LEN(%TRIM(XmlTAG))
     C                   EVAL      $$STXT=*OFF
4e   C                   ENDSL
3e   C                   ENDIF
      *
3    C                   IF        %SUBST(XmlTAG:$F:01)='(' AND $$STXT=*OFF
     C                   EVAL      $P=$P+1
3e   C                   ENDIF
3    C                   IF        %SUBST(XmlTAG:$F:01)=')' AND $$STXT=*OFF
     C                   EVAL      $P=$P-1
4    C                   IF        $P=0
     C                   LEAVE
4e   C                   ENDIF
3e   C                   ENDIF
2e   C                   ENDDO
     C
2    C                   IF        $P<>0
     C                   EVAL      $F=0
2e   C                   ENDIF
     C
     C                   ENDSR
      *---------------------------------------------------------------
    RD* ROUTINE INIZIALE
      *--------------------------------------------------------------*
     C     £INIZI        BEGSR
      *
     C                   EVAL      VALO=NUMERI+ALFMAX+ALFMIN+SPECIA
      *
     C                   ENDSR
      *--------------------------------------------------------------*
