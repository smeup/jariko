   COP*  OPTIMIZE(*FULL)
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 04/10/16  V5R1   BMA Creazione
     V* 05/10/16  V5R1   BMA Ignoro i tag all'interno di un CDATA
     V* 07/10/16  V5R1   BMA Correzione calcolo posizionamento
     V* 07/10/16  V5R1   BMA Aggiunto campo RxEle per indicare di cercare il tag anche in un CDATA
     V* 16/06/17         BMA Gestita lunghezza 65000
     V* 19/06/17  V5R1   BMA Rilasciata modifica precedente
     V* 13/09/17  V5R1   BMA Correzione in caso il tag sia a fine stringa
     V* 15/09/17  V5R1   BMA Correzione in caso la chiusura del tag sia a fine stringa
     V* 13/11/17  V5R1   BMA Aggiunto OPTIMIZE(*FULL)
     V* 18/01/18         BMA Corretta gestione CDATA
     V* 19/01/18         BMA Gestiti puntatori a CDATA in schiera per livelli di chiamata
     V* 12/02/18  V5R1   BMA Rilasciate modifiche precedenti
     V*=====================================================================
     V* Revisione ed esternalizzazione P_RxEle
     V*=====================================================================
     H/COPY QILEGEN,£INIZH
      *---------------------------------------------------------------
     I/COPY QILEGEN,£TABB£1DS
     I/COPY QILEGEN,£PDS
     D Xml_Fnd         S            512    VARYING
     D Xml_Met         S             10
     D Xml_Liv         S              2  0
     D Xml_Str         S          65000    VARYING
     D £JSP            S              7  0 DIM(50)
     D $Xml_Out        S          65000    VARYING
     D NodIni          S              5  0
     D NodLen          S              5  0
     D Xml_Con         S          65000    VARYING
     D $Parms          S              2  0
      *
     DWRKSTR           S          65000    VARYING
     DWRKST0           S          65000    VARYING
     DWRKST1           S          65000    VARYING
      *
     D$LenSt           S              5  0
     D$LenTi           S              5  0
     D$LenTf           S              5  0
     D$EleIn           S            512    VARYING
     D$EleFi           S            512    VARYING
     D$LenFt           S              5  0
     D$I               S             10I 0
     D$X               S             10I 0
     D$O               S             10I 0
     D$C               S             10I 0
     D$D               S             10I 0
     D$L               S             10I 0
     DnCDTIN           S              5I 0 DIM(%ELEM(£JSP)) INZ
     DnCDTFN           S              5I 0 DIM(%ELEM(£JSP)) INZ
     D$SCDATA          S              1
     D§SCDATA          S              1
      *---------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------
     C     *ENTRY        PLIST
     C                   PARM                    Xml_Fnd
     C                   PARM                    Xml_Met
     C                   PARM                    Xml_Liv
     C                   PARM                    Xml_Str
     C                   PARM                    $Xml_Out
     C                   PARM                    £JSP
     C                   PARM                    NodIni
     C                   PARM                    NodLen
     C                   PARM                    Xml_Con
     C                   PARM                    $Parms
     C                   PARM                    $SCDATA
      *
     C                   EVAL      $EleIn='<'+Xml_Fnd
     C                   EVAL      $EleFi='</'+%TRIM(Xml_Fnd)+'>'
1    C                   IF        Xml_Met='POS'
     C                   CLEAR                   £JSP(Xml_Liv)
     C                   EVAL      nCDTIN(Xml_Liv)=0
     C                   EVAL      nCDTFN(Xml_Liv)=0
1e   C                   ENDIF
1    C                   IF        $parms>4
     C                   CLEAR                   NodIni
1e   C                   ENDIF
1    C                   IF        $parms>5
     C                   CLEAR                   NodLen
1e   C                   ENDIF
1    C                   IF        $parms>6
     C                   CLEAR                   Xml_Con
1e   C                   ENDIF
1    C                   IF        $parms>7
     C                   EVAL      §SCDATA=$SCDATA
1x   C                   ELSE
     C                   CLEAR                   §SCDATA
1e   C                   ENDIF
      *
     C                   CLEAR                   WRKSTR
     C                   CLEAR                   Pun_Ele           5 0
     C                   CLEAR                   Nrr_Ele           5 0
     C                   CLEAR                   Pun_Pos           5 0
     C                   CLEAR                   Pun_Ini           5 0
      *
     C                   EVAL      $LenSt=%LEN(Xml_Str)
     C                   EVAL      $LenTi=%LEN($EleIn)
     C                   EVAL      $LenTf=%LEN($EleFi)
     C                   CLEAR                   $LenFt
      *
1    C                   IF        $LenSt > 0
      *
2    C                   IF        Xml_Met='POS'
     C                   EVAL      WRKST0=Xml_Str
      * Testo la presenza di un CDATA
     C                   EVAL      nCDTIN(Xml_Liv)=0
     C                   EVAL      nCDTFN(Xml_Liv)=0
      * . Inizio CDATA
2    C                   IF        §SCDATA=''
      *  . gestiamo la presenza di un solo CDATA al'interno dell'xml ricevuto
     C                   EVAL      nCDTIN(Xml_Liv)=%SCAN('<![CDATA[':WRKST0)
3    C                   IF        nCDTIN(Xml_Liv)>0
      * . Fine CDATA
     C                   EVAL      nCDTFN(Xml_Liv)=%SCAN(']]>':WRKST0)
3e   C                   ENDIF
2e   C                   ENDIF
2x   C                   ELSE
     C                   EVAL      WRKST0=%SUBST(Xml_Str:£JSP(Xml_Liv))
2e   C                   ENDIF
     C                   EVAL      Pun_Pos=£JSP(Xml_Liv)
     C                   EVAL      $LenST = $LenST - £JSP(Xml_Liv)
      * * Testo la presenza di un CDATA
      *C                   EVAL      nCDTIN=0
      *C                   EVAL      nCDTFN=0
      * * . Inizio CDATA
2     *C                   IF        §SCDATA=''
      * *  . gestiamo la presenza di un solo CDATA al'interno dell'xml ricevuto
      *C                   EVAL      nCDTIN=%SCAN('<![CDATA[':WRKST0)
3     *C                   IF        nCDTIN>0
      * * . Fine CDATA
      *C                   EVAL      nCDTFN=%SCAN(']]>':WRKST0)
3e    *C                   ENDIF
2e    *C                   ENDIF
     C                   EVAL      $I=%SCAN($EleIn:WRKST0)
2    C                   DOW       $I>0
3    C                   IF        $I>0
4    C                   IF        %LEN(WRKST0)>=$I+$LenTi
      * Controllo che non sia un tag che inizia con il tag ricercato
5    C                   IF        %SUBST(WRKST0:$I+$LenTi:1)<>' ' AND
     C                             %SUBST(WRKST0:$I+$LenTi:1)<>'/' AND
     C                             %SUBST(WRKST0:$I+$LenTi:1)<>'>'
     C                   EVAL      $X=$I
     C                   EVAL      $I=%SCAN($EleIn:WRKST0:$X+1)
5x   C                   ELSE
     C                   LEAVE
5e   C                   ENDIF
4x   C                   ELSE
      * Se il tag è alla fine della stringa imposto 0 ed esco
     C                   EVAL      $I=0
     C                   LEAVE
4e   C                   ENDIF
3e   C                   ENDIF
2e   C                   ENDDO
      * . se ho trovato il tag all'interno del CDATA lo cerco dopo
2    C                   IF        nCDTIN(Xml_Liv)>0
3    C                   IF        $I>nCDTIN(Xml_Liv) AND $I<nCDTFN(Xml_Liv)
4    C                   IF        $LenST>nCDTFN(Xml_Liv)+3
     C                   EVAL      $I=%SCAN($EleIn:WRKST0:nCDTFN(Xml_Liv)+3)
4x   C                   ELSE
     C                   EVAL      $I=0
4e   C                   ENDIF
3e   C                   ENDIF
2e   C                   ENDIF
      * Se ho trovato il tag continuo
2    C                   IF        $I>0
     C                   EVAL      WRKST0= %SUBST(WRKST0:$I)
     C                   EVAL      WRKST1=WRKST0
     C                   EVAL      $L=$I-1
2x   C                   ELSE
      * Se non ho trovato il tag esco
     C                   EVAL      £JSP(Xml_Liv)=%LEN(Xml_Str)
     C                   GOTO      G8ELE
2e   C                   ENDIF
      *C                   EVAL      $LenSt = $LenSt - $L
     C                   EVAL      $LenSt = %LEN(WRKST0)
2    C                   IF        Xml_Met='POS'
      * Se posizionamento, il punto è dove ho trovato il tag cercato
     C                   EVAL      Pun_Pos=$I
2x   C                   ELSE
      * Se lettura, il punto è la partenza + il punto dove ho trovato il tag cercato -1
     C                   EVAL      Pun_Pos=£JSP(Xml_Liv)+$L
2e   C                   ENDIF
      *
2    C                   DOW       $LenSt > 0
      * Cerca Elemento
3    C                   SELECT
      * . Inizio Elemento richiesto
3x   C                   WHEN      $LenTi<=$LenSt AND
     C                             Pun_Ini=0 AND
     C                             (%SUBST(WRKST0:1:$LenTi)=$EleIn)
     C                   EVAL      WRKST1=WRKST0
     C                   EVAL      Pun_Ini = Pun_Pos
     C                   CLEAR                   Nrr_Ele
      * . Fine Elemento richiesto
      * .. Forma completa
3x   C                   WHEN      $LenTf<=$LenSt AND
     C                             Pun_Ini>0 AND
     C                             %SUBST(WRKST0:1:$LenTf)=$EleFi
     C                   EVAL      Pun_Ele=Pun_Pos+$LenTf-Pun_Ini
     C                   EVAL      WRKSTR=%SUBST(WRKST1:1:Pun_Ele)
     C                   EVAL      £JSP(Xml_Liv)=Pun_Pos
     C                   LEAVE
      * .. Forma implicita (/>)
3x   C                   WHEN      2<=$LenSt AND
     C                             Pun_Ini>0 AND
     C                             %SUBST(WRKST0:1:2)='/>'
4    C                   IF        Nrr_Ele = 0
     C                   EVAL      Pun_Ele=Pun_Pos+2-Pun_Ini
     C                   EVAL      WRKSTR=%SUBST(WRKST1:1:Pun_Ele)
     C                   EVAL      £JSP(Xml_Liv)=Pun_Pos
     C                   LEAVE
4x   C                   ELSE
     C                   EVAL      Nrr_Ele = Nrr_Ele - 1
4e   C                   ENDIF
      * .. Forma completa ma diversa (</xx>)
3x   C                   WHEN      2<=$LenSt AND
     C                             Pun_Ini>0 AND
     C                             %SUBST(WRKST0:1:2)='</'
     C                   EVAL      Nrr_Ele = Nrr_Ele - 1
      * . Elemento interno (<)
3x   C                   WHEN      Pun_Ini>0 AND %SUBST(WRKST0:1:1)='<'
     C                   EVAL      Nrr_Ele = Nrr_Ele + 1
3e   C                   ENDSL
      *
3    C                   IF        $LenSt>1
      * Salto alla prossima apertura o chiusura
     C                   EVAL      $I=0
     C                   EVAL      $O=2
4    C                   DO        *HIVAL
     C                   EVAL      $C=%SCAN('<':WRKST0:$O)
     C                   EVAL      $D=%SCAN('/>':WRKST0:$O)
5    C                   SELECT
5x   C                   WHEN      $C=0 AND $D=0
     C                   EVAL      $O=0
5x   C                   WHEN      $C>0 AND $D=0
     C                   EVAL      $O=$C
5x   C                   WHEN      $C>0 AND $C<$D
     C                   EVAL      $O=$C
5x   C                   WHEN      $D>0 AND $C=0
     C                   EVAL      $O=$D
5x   C                   WHEN      $D>0 AND $D<$C
     C                   EVAL      $O=$D
5e   C                   ENDSL
      *C                   FOR       $O=2 TO $C
5     *C                   IF        $O<$C
6     *C                   IF        %SUBST(WRKST0:$O:1)='<' OR
      *C                             %SUBST(WRKST0:$O:2)='/>'
     C                   IF        $O>0
      * . se sono all'interno del CDATA itero e cerco la prossima
7    C                   IF        nCDTIN(Xml_Liv)>0
8    C                   IF        ($O+Pun_Pos)>=nCDTIN(Xml_Liv)
     C                             AND ($O+Pun_Pos)<nCDTFN(Xml_Liv)
     C                   EVAL      $O=$O+1
     C                   ITER
8e   C                   ENDIF
7e   C                   ENDIF
7e   C                   ENDIF
     C                   EVAL      $I=$O
     C                   LEAVE
6e    *C                   ENDIF
5x    *C                   ELSE
6     *C                   IF        %SUBST(WRKST0:$O:1)='<'
      * * . se sono all'interno del CDATA itero e cerco la prossima
7     *C                   IF        nCDTIN>0
8     *C                   IF        ($O+Pun_Pos)>=nCDTIN
      *C                             AND ($O+Pun_Pos)<nCDTFN
      *C                   ITER
8e    *C                   ENDIF
7e    *C                   ENDIF
      *C                   EVAL      $I=$O
      *C                   LEAVE
6e    *C                   ENDIF
5e    *C                   ENDIF
      *C                   ENDFOR
4e   C                   ENDDO
4    C                   IF        $I>0
     C                   EVAL      WRKST0= %SUBST(WRKST0:$I)
     C                   EVAL      $L=$I-1
4x   C                   ELSE
     C                   LEAVE
4e   C                   ENDIF
3x   C                   ELSE
     C                   LEAVE
3e   C                   ENDIF
      *C                   EVAL      $LenSt = $LenSt - $L
     C                   EVAL      $LenSt = %LEN(WRKST0)
     C                   EVAL      Pun_Pos=Pun_Pos+$L
     C                   EVAL      £JSP(Xml_Liv)=Pun_Pos
2e   C                   ENDDO
      *
1e   C                   ENDIF
     C     G8ELE         TAG
      * se passato il quinto parametro (e/o il sesto) restituisco i rispettivi valori
1    C                   SELECT
      * . se passato il parametro NodIni restituisco la posizione inziale del nodo rilevato
1x   C                   WHEN      $parms=5 AND WRKSTR<>*BLANKS
     C                   EVAL      NodIni = Pun_Ini
      * . se passato anche il parametro NodLen restituisco la lunghezza del nodo rilevato
1x   C                   WHEN      $parms>5 AND WRKSTR<>*BLANKS
     C                   EVAL      NodIni = Pun_Ini
     C                   EVAL      NodLen=Pun_Ele
1e   C                   ENDSL
      * . se passato anche il contenuto tag
1    C                   IF        $parms>6 AND NodLen<>*ZEROS
     C                   EVAL      Xml_Con=%subst(Xml_Str:NodIni:NodLen)
      * Trova la fine dell'apertura del tag
     C                   EVAL      $LenFt=%SCAN('>':Xml_Str:NodIni+1)
2    C                   IF        $LenFt<(NodLen+NodIni) AND $LenFt>0
     C                             AND
     C                             (NodLen-($LenFt-NodIni)-%len($EleFi)-1)>0
      * Sottostringo a partire dalla fine dell'apertura + 1 per la lunghezza - apertura - chiusura
     C                   EVAL      Xml_Con=%subst(Xml_Str
     C                                           :$LenFt+1
     C                                           :NodLen-($LenFt-NodIni)
     C                                            -%len($EleFi)-1)
2e   C                   ENDIF
1e   C                   ENDIF
      *
     C                   EVAL      $Xml_Out=WRKSTR
     C                   SETON                                        RT
      *---------------------------------------------------------------
     C/COPY QILEGEN,£INZSR
      *---------------------------------------------------------------
    RD* ROUTINE INIZIALE
      *--------------------------------------------------------------*
     C     £INIZI        BEGSR
      *
      *
     C                   ENDSR
      *--------------------------------------------------------------*
