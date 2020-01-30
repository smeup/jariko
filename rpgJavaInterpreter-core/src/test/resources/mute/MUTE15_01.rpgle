     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 28/01/20  001488 BMA Creazione
     V*=====================================================================
      *--------------------------------------------------------------------
    RD* Scompatto da stringa a schiera
      *--------------------------------------------------------------------
     Dp_£G43E          PR           256    DIM(500)
      * Stringa di input da scomporre
     D  $Val                      30000    CONST
      * Separatore
     D  $Sep                          1    CONST
      *--------------------------------------------------------------------
      * Numero di elementi restituiti
     D £G43ENR         S              5I 0
     D £G43ESK         S            256    DIM(500)
      *---------------------------------------------------------------
     D $STRING         S           1000    VARYING
     D DSP             S             52
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
     C                   EXSR      ELAB
      *
     C                   SETON                                        RT
      *--------------------------------------------------------------*
    RD*    Elaborazione
      *--------------------------------------------------------------*
     C     ELAB          BEGSR
      *
     C                   EVAL      $STRING='A1;B;C3;D;E4;F;G00;H777;IY;6L;'
     C                   EVAL      £G43ESK=p_£G43E($STRING:';')
    MU* VAL1(£G43ENR) VAL2(11) COMP(EQ)
     C                   EVAL      DSP=%CHAR(£G43ENR)
     C                   DSPLY                   DSP
    MU* VAL1(£G43ESK(1)) VAL2('A1') COMP(EQ)
     C                   EVAL      DSP=£G43ESK(1)
     C                   DSPLY                   DSP
    MU* VAL1(£G43ESK(2)) VAL2('B') COMP(EQ)
     C                   EVAL      DSP=£G43ESK(2)
     C                   DSPLY                   DSP
    MU* VAL1(£G43ESK(3)) VAL2('C3') COMP(EQ)
     C                   EVAL      DSP=£G43ESK(3)
     C                   DSPLY                   DSP
    MU* VAL1(£G43ESK(4)) VAL2('D') COMP(EQ)
     C                   EVAL      DSP=£G43ESK(4)
     C                   DSPLY                   DSP
    MU* VAL1(£G43ESK(5)) VAL2('E4') COMP(EQ)
     C                   EVAL      DSP=£G43ESK(5)
     C                   DSPLY                   DSP
    MU* VAL1(£G43ESK(6)) VAL2('F') COMP(EQ)
     C                   EVAL      DSP=£G43ESK(6)
     C                   DSPLY                   DSP
    MU* VAL1(£G43ESK(7)) VAL2('G00') COMP(EQ)
     C                   EVAL      DSP=£G43ESK(7)
     C                   DSPLY                   DSP
    MU* VAL1(£G43ESK(8)) VAL2('H777') COMP(EQ)
     C                   EVAL      DSP=£G43ESK(8)
     C                   DSPLY                   DSP
    MU* VAL1(£G43ESK(9)) VAL2('IY') COMP(EQ)
     C                   EVAL      DSP=£G43ESK(9)
     C                   DSPLY                   DSP
    MU* VAL1(£G43ESK(10)) VAL2('6L') COMP(EQ)
     C                   EVAL      DSP=£G43ESK(10)
     C                   DSPLY                   DSP
    MU* VAL1(£G43ESK(11)) VAL2('') COMP(EQ)
     C                   EVAL      DSP=£G43ESK(11)
     C                   DSPLY                   DSP
    MU* VAL1(£G43ESK(12)) VAL2('') COMP(EQ)
     C                   EVAL      DSP=£G43ESK(12)
     C                   DSPLY                   DSP
    MU* VAL1(£G43ESK(13)) VAL2('') COMP(EQ)
     C                   EVAL      DSP=£G43ESK(13)
     C                   DSPLY                   DSP
      *
     C                   ENDSR
      *--------------------------------------------------------------------
    RD* Scompatto da stringa a schiera
      *--------------------------------------------------------------------
     Pp_£G43E          B
     D p_£G43E         Pi           256    DIM(500)
      * Stringa di input da scomporre
     D $Val                       30000    CONST
      * Separatore
     D $Sep                           1    CONST
      * Schiera d'appoggio da restituire
     D SCH             S            256    DIM(500)
     D $P              S              5I 0
     D $L              S              5I 0
     D $Z              S              5I 0
     D $I              S              5I 0
      *
     C                   CLEAR                   SCH
     C                   EVAL      $P=1
     C                   EVAL      $L=0
     C                   EVAL      $Z=0
     C                   EVAL      $I=0
      *
     C                   DO        *HIVAL
      * scompongo stringa
     C                   EVAL      $I=%SCAN($Sep:$Val:$P)
     C                   IF        $I=0
     C                   LEAVE
     C                   ENDIF
     C                   EVAL      $L=$I-$P
     C                   IF        %SUBST($VAL:$P+1)<>' ' OR $SEP<>' '
     C                   EVAL      $Z=$Z+1
     C                   EVAL      SCH($Z)=%SUBST($Val:$P:$L)
     C                   ENDIF
     C                   EVAL      $P=$P+$L+1
     C                   ENDDO
      * flush ultimo valore
     C                   EVAL      $Z=$Z+1
     C                   EVAL      SCH($Z)=%SUBST($Val:$P)
      * Numero di elementi restituiti
     C                   EVAL      £G43ENR=$Z
      *
      * Schiera d'appoggio da restituire
     C                   RETURN    SCH
     P                 E
