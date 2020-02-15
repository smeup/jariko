     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 07/02/20  001529 BERNI Creazione
     V* 10/02/20  001529 BMA   Sostituita SCAN con %SCAN
     V* 11/02/20  001529 BERNI Aggiunti commenti
     V* 14/02/20  001529 BMA   Ricompilato
     V* 14/02/20  V5R1   BMA   Check-out 001529 in SMEDEV
     V*=====================================================================
     DPR_ESTR          PR            15
     D                                1    CONST
      *---------------------------------------------------------------
     D ESE1            S             30
     D ESE2            S             30
     D RIS1            S             30
     D RIS2            S             30
     D RIS3            S             30
     D WKTXT           S           1000    VARYING
     D DSP             S             52
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      *
     C                   EXSR      ELAB
      *
     C                   SETON                                        RT
      *--------------------------------------------------------------*
    RD* Routine
      *--------------------------------------------------------------*
     C     ELAB          BEGSR
      * Set variables
     C                   EVAL      ESE1='EXD;*SCO'
     C                   EVAL      ESE2='[T1];[P1];[K1]'
      *
      * First example
     C                   EVAL      WKTXT=ESE1
    MU* VAL1(RIS1) VAL2('EXD') COMP(EQ)
      * First step, Execute procedure with parameter I
     C                   EVAL      RIS1=PR_ESTR('I')
     C                   EVAL      DSP=RIS1
     C     DSP           DSPLY     £PDSNU
    MU* VAL1(RIS2) VAL2('*SCO') COMP(EQ)
      * Next step, Execute procedure with parameter Blanks
     C                   EVAL      RIS2=PR_ESTR(' ')
     C                   EVAL      DSP=RIS2
     C     DSP           DSPLY     £PDSNU
      *
      * Second example
     C                   EVAL      WKTXT=ESE2
    MU* VAL1(RIS1) VAL2('[T1]') COMP(EQ)
      * First step, Execute procedure with parameter I
     C                   EVAL      RIS1=PR_ESTR('I')
     C                   EVAL      DSP=RIS1
     C     DSP           DSPLY     £PDSNU
    MU* VAL1(RIS2) VAL2('[P1]') COMP(EQ)
      * Next step, Execute procedure with parameter Blanks
     C                   EVAL      RIS2=PR_ESTR(' ')
     C                   EVAL      DSP=RIS2
     C     DSP           DSPLY     £PDSNU
    MU* VAL1(RIS3) VAL2('[K1]') COMP(EQ)
      * Next step, Execute procedure with parameter Blanks
     C                   EVAL      RIS3=PR_ESTR(' ')
     C                   EVAL      DSP=RIS3
     C     DSP           DSPLY     £PDSNU
      *
     C                   ENDSR
      *---------------------------------------------------------------*
    RD* Extraction routine
      *---------------------------------------------------------------*
     PPR_ESTR          B
      *
     DPR_ESTR          PI            15
     DXXTIPO                          1    CONST
      *
     D $I              S              5  0
     D $L              S              5  0
     D $X              S              5  0 STATIC
     D AAA015          S             15
      *
      * Initialization of the procedure
1    C                   IF        XXTIPO='I'
     C                   EVAL      $I=1
1x   C                   ELSE
      *
     C                   EVAL      $I=$X+1
1e   C                   ENDIF
      *  If the example is 1 character long, exit after first step
1    C                   IF        $I=1 AND XXTIPO<>'I'
     C                   RETURN    AAA015
1e   C                   ENDIF
      *
      * looking for next ';'
     C                   EVAL      $X=%SCAN(';':WKTXT:$I)
      * calculate length of the element
     C                   IF        $X>0
     C                   EVAL      $L=$X-$I
1x   C                   ELSE
      * if there isn't another ';' take remaining characters
     C                   EVAL      $L=%LEN(WKTXT)-$I
1e   C                   ENDIF
      * if length is greater then 0, return the result
1    C                   IF        $L>0
     C                   EVAL      AAA015=%TRIM(%SUBST(WKTXT:$I:$L))
1e   C                   ENDIF
      *
     C                   RETURN      AAA015
     PPR_ESTR          E
