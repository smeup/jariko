   COP*  OPTIMIZE(*FULL)
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 05/10/16  V5R1   BMA Creazione
     V* 16/06/17         BMA Gestita lunghezza 65000
     V* 19/06/17  V5R1   BMA Rilasciata modifica precedente
     V* 19/06/17  V5R1   BMA Gestione marcatori testo
     V* 20/07/17  V5R1   BMA Correzione modifica precedente
     V* 13/11/17  V5R1   BMA Ottimizzazione
     V* 13/11/17  V5R1   BMA Aggiunto OPTIMIZE(*FULL)
     V*=====================================================================
     V* Revisione ed esternalizzazione P_RxVAL
     V*=====================================================================
     H/COPY QILEGEN,£INIZH
      *---------------------------------------------------------------
     I/COPY QILEGEN,£TABB£1DS
     I/COPY QILEGEN,£PDS
     D $XmlTAG         S          65000    VARYING
     D $XmlATT         S             64
     D ATTRIC          S             66    VARYING
     D $XmlVAL         S          65000    VARYING
     D TXmlVAL         S          65000    VARYING
     D FNDTXT          S              1N
     D INI010          S             10
     D FIN010          S             10
     D AAA010          S             10
     D BLKSTR          S            100    VARYING
     D $I              S              5  0
     D $F              S              5  0
     D $L              S              5  0
     D §L              S              5  0
     D $TI             S              5  0
     D $TF             S              5  0
     D $T              S              5  0
     D $Y              S              5  0
     D VALO            S             74
     D NUMERI          C                   CONST('0123456789')
     D ALFMAX          C                   CONST('ABCDEFGHIJKLMNOPQRSTUVWXYZ')  COSTANTE
     D ALFMIN          C                   CONST('abcdefghijklmnopqrstuvwxyz')  COSTANTE
     D SPECIA          C                   CONST('§£$_-&*./@#%')                COSTANTE
      *
      *---------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------
     C     *ENTRY        PLIST
     C                   PARM                    $XmlTAG
     C                   PARM                    $XmlATT
     C                   PARM                    $XmlVAL
      *
     C                   EVAL      $XmlTAG=%TRIM($XmlTAG)
     C                   EVAL      FNDTXT=*OFF
1    C                   IF        %LEN($XmlTAG)>0
      * cerco marcatori testo
     C                   EVAL      $TI=%SCAN('_$_STXT_$_':$XmlTAG)
2    C                   IF        $TI>0
     C                   EVAL      $TF=%SCAN('_$_ETXT_$_':$XmlTAG)
3    C                   IF        $TF>0
     C                   EVAL      FNDTXT=*ON
3e   C                   ENDIF
2e   C                   ENDIF
      *metto in ATTRIC l'attributo passato più i caratteri speciali ="
     C                   EVAL      ATTRIC=%TRIM($XmlATT)+'="'
     C                   EVAL      $L=%LEN(ATTRIC)
     C                   EVAL      $I=%SCAN(ATTRIC:$XmlTAG)
      * se ho trovato il marcatore di incapsulamento del testo
2    C                   IF        FNDTXT=*ON
3    C                   DO        *HIVAL
4    C                   IF        $TI>0
      * . se il tag da cercare è all'interno del testo cerco il successivo
5    C                   IF        $I>$TI AND $I<$TF
     C                   EVAL      $I=%SCAN(ATTRIC:$XmlTAG:$TF)
     C                   EVAL      $TI=%SCAN('_$_STXT_$_':$XmlTAG:$TF+1)
6    C                   IF        $TI>0
     C                   EVAL      $TF=%SCAN('_$_ETXT_$_':$XmlTAG:$TI+1)
6e   C                   ENDIF
5x   C                   ELSE
     C                   LEAVE
5e   C                   ENDIF
4x   C                   ELSE
     C                   LEAVE
4e   C                   ENDIF
3e   C                   ENDDO
      * . una volta determinato dove inizia il tag, verifico se poi c'è un marcatore di testo
      * . (testo all'interno del tag)
     C                   EVAL      FNDTXT=*OFF
     C                   EVAL      $TI=0
     C                   EVAL      $TF=0
     C                   EVAL      $TI=%SCAN('_$_STXT_$_':$XmlTAG:$I+1)
3    C                   IF        $TI>0
     C                   EVAL      $TF=%SCAN('_$_ETXT_$_':$XmlTAG:$I+1)
4    C                   IF        $TF>0
     C                   EVAL      FNDTXT=*ON
4e   C                   ENDIF
3e   C                   ENDIF
2e   C                   ENDIF
      *
     C                   EVAL      §L=%LEN($XmlTAG)
2    C                   IF        §L<$I+$L
     C                   CLEAR                   $F
2x   C                   ELSE
     C                   EVAL      $F=%SCAN('"':$XmlTAG:$I+$L)
3    C                   IF        FNDTXT=*ON
      * . se la chiusura tag da cercare è all'interno del testo cerco la successiva
4    C                   IF        $F>$TI AND $F<$TF
     C                   EVAL      $F=%SCAN('"':$XmlTAG:$TF)
4e   C                   ENDIF
3e   C                   ENDIF
2e   C                   ENDIF
      *$I Contiene sempre l'inzio del TAG
      *$F Contiene sempre la posizione delle " di chiusura del Valore del TAG
      *$L Contiene la lunghezza senza spazi del nome del TAG più i caratteri ="
2    C                   SELECT
2x   C                   WHEN      $I=0 OR $F=0
      *  se non trovo l'inizio o la fine esco
     C                   CLEAR                   $XMLVAL
2x   C                   WHEN      $I=1                                         INIZIO stringa
      *  se il tag è il primo e in prima posizione lo estraggo direttamente
     C                   EVAL      $XMLVAL=%SUBST($XmlTAG:$L+1:$F-$L-1)
2x   C                   OTHER
      *  se il tag non è in prima posizione deve essere preceduto da spazio o '<'
      *  o dal marcatore di chiusura testo
3    C                   DO        *HIVAL
     C                   CLEAR                   AAA010
4    C                   IF        $I>10
     C                   EVAL      AAA010=%SUBST($XmlTAG:$I-10:10)
4e   C                   ENDIF
4    C                   IF        %SCAN(%SUBST($XmlTAG:$I-1:1):VALO)=0
     C                             OR AAA010='_$_ETXT_$_'
     C                   EVAL      $XMLVAL=%SUBST($XmlTAG:$I+$L:$F-$L-$I)
     C                   LEAVE
4x   C                   ELSE
      *  se prima del tag non c'è uno spazio lo cerco di nuovo nella stringa
      *  ES:       cerco 'AA' e xml= <Setup BAA='1' AA='2'>
5    C                   IF        $F<%LEN($XmlTAG)
     C                   EVAL      $I=%SCAN(ATTRIC:$XmlTAG:$F+2)
     C                   EVAL      $F=%SCAN('"':$XmlTAG:$I+$L)
6    C                   IF        FNDTXT=*ON
      * . se la chiusura tag da cercare è all'interno del testo cerco la successiva
7    C                   IF        $F>$TI AND $F<$TF
     C                   EVAL      $F=%SCAN('"':$XmlTAG:$TF)
7e   C                   ENDIF
6e   C                   ENDIF
6    C                   IF        $I=0 OR $F=0
      *  se non trovo l'inizio o la fine esco
     C                   CLEAR                   $XMLVAL
     C                   LEAVE
6e   C                   ENDIF
5x   C                   ELSE
     C                   EVAL      $L=0
     C                   EVAL      $I=0
     C                   EVAL      $F=0
     C                   CLEAR                   $XMLVAL
     C                   LEAVE
5e   C                   ENDIF
4e   C                   ENDIF
3e   C                   ENDDO
2e   C                   ENDSL
1x   C                   ELSE
     C                   CLEAR                   $XMLVAL
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
4    C                   FOR       $Y=1 TO %LEN($XmlVAL)
5    C                   IF        %SUBST($XmlVAL:$Y:10)='_$_STXT_$_'
     C                   EVAL      $XmlVAL=BLKSTR+%SUBST($XmlVAL:$Y+10)
     C                   LEAVE
5x   C                   ELSE
     C                   EVAL      BLKSTR=BLKSTR+' '
5e   C                   ENDIF
4e   C                   ENDFOR
     C                   CLEAR                   BLKSTR
4    C                   FOR       $Y=%LEN($XmlVAL)-9 BY 1 DOWNTO 1
5    C                   IF        %SUBST($XmlVAL:$Y:10)='_$_ETXT_$_'
      *C                   EVAL      $XmlVAL=BLKSTR+%SUBST($XmlVAL:01:$Y-1)
     C                   EVAL      $XmlVAL=%SUBST($XmlVAL:01:$Y-1)+BLKSTR
     C                   LEAVE
5x   C                   ELSE
     C                   EVAL      BLKSTR=BLKSTR+' '
5e   C                   ENDIF
4e   C                   ENDFOR
3e   C                   ENDIF
2e   C                   ENDIF
1e   C                   ENDIF
      * Restituisce VALORE
      *
     C                   SETON                                        RT
      *---------------------------------------------------------------
     C/COPY QILEGEN,£INZSR
      *---------------------------------------------------------------
    RD* ROUTINE INIZIALE
      *--------------------------------------------------------------*
     C     £INIZI        BEGSR
      *
     C                   EVAL      VALO=NUMERI+ALFMAX+ALFMIN+SPECIA
      *
     C                   ENDSR
      *--------------------------------------------------------------*
