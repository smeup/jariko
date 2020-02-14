     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 11/02/20  001529 BERNI Creazione
     V* 14/02/20  001529 BMA   Revisione
     V* 14/02/20  V5R1   BMA   Check-out 001529 in SMEDEV
     V*=====================================================================
     D SEARCH          PR             5  0
     D   SEARCHIN                    50A   DIM(100) CONST VARYING
     D   ARRAYDIM                     5  0 VALUE
     D   SEARCHFOR                   50A   CONST VARYING
     D ARR1            S              1A   DIM(7) CTDATA PERRCD(1)              _NOTXT
     D ARR2            S             10A   DIM(3) CTDATA                        _TXT
     D ELEM            S              5  0
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
      *
      * Check each element of the array to see if it the same as the SearchFor.
      * Use the dimension that was passed as a parameter rather than the declared dimension.
      * Use %SUBST with the length parameter since the parameters may not have the declared length.
      *
    MU* VAL1(ELEM) VAL2('5') COMP(EQ)
     C                   EVAL      ELEM = SEARCH(ARR1 :
     C                                           %ELEM(ARR1) : '*')
     C                   EVAL      DSP=%CHAR(ELEM)
     C     DSP           DSPLY     £PDSNU
      *
    MU* VAL1(ELEM) VAL2('0') COMP(EQ)
     C                   EVAL      ELEM = SEARCH(ARR2 :
     C                                           %ELEM(ARR2) : 'pink')          COSTANTE
     C                   EVAL      DSP=%CHAR(ELEM)
     C     DSP           DSPLY     £PDSNU
      *
     C                   ENDSR
      *---------------------------------------------------------------*
    RD* Procedure
      *---------------------------------------------------------------*
     P SEARCH          B
     D SEARCH          PI             5  0
     D   SEARCHIN                    50A   DIM(100) CONST VARYING
     D   ARRAYDIM                     5  0 VALUE
     D   SEARCHFOR                   50A   CONST VARYING
     D I               S              5  0
      *
     C     1             DO        ARRAYDIM      I                 5 0
      * If this element matches SearchFor, return the index.
     C                   IF        SEARCHIN(I)=SEARCHFOR
     C                   RETURN    I
     C                   ENDIF
     C                   ENDDO
      * No matching element was found.
     C                   RETURN    0
     P SEARCH          E
** ARR1
A
2
$
@
*
j
M
** ARR2
Red
Blue
Yellow
