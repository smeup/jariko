   COP* *NOUI
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* 10/03/16  V4.R1   GIAGIU Creato
     V* B£61020C  V5R1    BMA Aggiunta COP* *NOUI
     V*=====================================================================
     V* OBIETTIVO
     V*  Programma finalizzato ai test su campi di tipo INDICATOR
     V*
     V*=====================================================================
      * Considerare i seguenti codici operativi
      *+----------+--+---------!--+
      *!RPGLE     !ST!BUILT-IN !ST!
      *+----------+--+---------!--+
      *!          !  !EVAL     !ok!
      *!CLEAR     !ok!         !  !
      *!MOVE      !ok!         !  !
      *!MOVEA     !ok!         !  !
      *!MOVEL     !ok!         !  !
      *!RESET     !  !         !  !
      *!SETON     !ok!         !  !
      *!SETOFF    !ok!         !  !
      *+----------+--+---------+--+
     DI01              S               N
    MU* VAL1(I02) VAL2('1') COMP(EQ)
     DI02              S               N   INZ(*ON)
     DI03              S               N
    MU* VAL1(I04) VAL2(*OFF) COMP(EQ)
     DI04              S               N   INZ(*OFF)
     DNS               S              1S 0
     DNP               S              1P 0
     DST               S             10A
     DDS01             DS
     D FL01                           1A
     D FL02                            N
     D FL03                           1A
     D
     D
     D
      *
     C                   EXSR      F_CLEAR
     C                   EXSR      F_EVAL
     C                   EXSR      F_MOVE
     C                   EXSR      F_MOVEA
     C                   EXSR      F_MOVEL
     C                   EXSR      F_RESET
     C                   EXSR      F_SETOFF
     C                   EXSR      F_SETON
     C                   EXSR      F_OTHER
      *
     C
      *
    MU* Type="NOXMI"
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Routine test CLEAR
      *---------------------------------------------------------------------
     C     F_CLEAR       BEGSR
    MU* VAL1(I01) VAL2('0') COMP(EQ)
     C                   CLEAR                   I01
    MU* VAL1(I02) VAL2('0') COMP(EQ)
     C                   CLEAR                   I02
    MU* VAL1(I03) VAL2('0') COMP(EQ)
     C                   CLEAR                   I03
    MU* VAL1(I04) VAL2('0') COMP(EQ)
     C                   CLEAR                   I04
    MU* VAL1(*IN(01)) VAL2('0') COMP(EQ)
     C                   CLEAR                   *IN(01)
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test EVAL
      *---------------------------------------------------------------------
     C     F_EVAL        BEGSR
    MU* VAL1(I02) VAL2(*ON) COMP(EQ)
     C                   EVAL      I02=*ON
    MU* VAL1(I04) VAL2('1') COMP(EQ)
     C                   EVAL      I04=*ON
    MU* VAL1(I01) VAL2(*ON) COMP(EQ)
     C                   EVAL      I01=I02
    MU* VAL1(I03) VAL2('1') COMP(EQ)
     C                   EVAL      I03=I04
    MU* VAL1(*IN(01)) VAL2('0') COMP(EQ)
     C                   EVAL      *IN(01)=*OFF
    MU* VAL1(*IN(01)) VAL2('1') COMP(EQ)
     C                   EVAL      *IN(01)=*ON
    MU* VAL1(*IN(01)) VAL2('0') COMP(EQ)
     C                   EVAL      *IN(01)='0'
    MU* VAL1(*IN(01)) VAL2('1') COMP(EQ)
     C                   EVAL      *IN(01)='1'
      *
     C                   EVAL      *IN='1'
     C                   DO        99            XX                3 0
    MU* VAL1(*IN(XX)) VAL2('1') COMP(EQ)
     C                   EVAL      *IN(XX)=*IN(XX)
     C                   ENDDO
      *
     C                   EVAL      *IN='0'
     C                   DO        99            XX                3 0
    MU* VAL1(*IN(XX)) VAL2('0') COMP(EQ)
     C                   EVAL      *IN(XX)=*IN(XX)
     C                   ENDDO
      *
     C                   CLEAR                   A1                1
    MU* VAL1(I01) VAL2(' ') COMP(EQ)
     C                   EVAL      I01=A1
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test MOVE
      *---------------------------------------------------------------------
     C     F_MOVE        BEGSR
    MU* VAL1(I02) VAL2('1') COMP(EQ)
     C                   MOVE      *ON           I02
    MU* VAL1(I04) VAL2('1') COMP(EQ)
     C                   MOVE      *ON           I04
    MU* VAL1(I01) VAL2(*ON) COMP(EQ)
     C                   MOVE      I02           I01
    MU* VAL1(I03) VAL2('1') COMP(EQ)
     C                   MOVE      I04           I03
    MU* VAL1(*IN(01)) VAL2('0') COMP(EQ)
     C                   MOVE      *OFF          *IN(01)
    MU* VAL1(*IN(01)) VAL2('1') COMP(EQ)
     C                   MOVE      *ON           *IN(01)
    MU* VAL1(*IN(01)) VAL2('0') COMP(EQ)
     C                   MOVE      '0'           *IN(01)
    MU* VAL1(*IN(01)) VAL2('1') COMP(EQ)
     C                   MOVE      '1'           *IN(01)
      *
    MU* VAL1(*IN(02)) VAL2('1') COMP(EQ)
     C                   MOVE      1             *IN(02)
    MU* VAL1(*IN(02)) VAL2(*OFF) COMP(EQ)
     C                   MOVE      0             *IN(02)
    MU* VAL1(I02) VAL2(*ON) COMP(EQ)
     C                   MOVE      1             I02
    MU* VAL1(I02) VAL2(*OFF) COMP(EQ)
     C                   MOVE      0             I02
      *
     C                   EVAL      NS=1
    MU* VAL1(*IN(02)) VAL2('1') COMP(EQ)
     C                   MOVE      NS            *IN(02)
    MU* VAL1(I02) VAL2(*ON) COMP(EQ)
     C                   MOVE      NS            I02
      *
     C                   EVAL      NS=0
    MU* VAL1(I02) VAL2(*OFF) COMP(EQ)
     C                   MOVE      NS            I02
    MU* VAL1(*IN(02)) VAL2(*OFF) COMP(EQ)
     C                   MOVE      NS            *IN(02)
      *
     C                   EVAL      NP=1
    MU* VAL1(*IN(02)) VAL2('1') COMP(EQ)
     C                   MOVE      NP            *IN(02)
    MU* VAL1(I02) VAL2(*ON) COMP(EQ)
     C                   MOVE      NP            I02
      *
     C                   EVAL      NP=0
    MU* VAL1(I02) VAL2(*OFF) COMP(EQ)
     C                   MOVE      NP            I02
    MU* VAL1(*IN(02)) VAL2(*OFF) COMP(EQ)
     C                   MOVE      NP            *IN(02)
      *
     C                   EVAL      ST='1111111111'
     C                   MOVE      ST            *IN
     C                   DO        99            XX                3 0
    MU* VAL1(*IN(XX)) VAL2('1') COMP(EQ)
     C                   EVAL      *IN(XX)=*IN(XX)
     C                   ENDDO
      *
     C                   MOVE      '0'           *IN
     C                   DO        99            XX                3 0
    MU* VAL1(*IN(XX)) VAL2('0') COMP(EQ)
     C                   EVAL      *IN(XX)=*IN(XX)
     C                   ENDDO
      *
     C                   MOVE      '1'           *IN
     C                   DO        99            XX                3 0
    MU* VAL1(*IN(XX)) VAL2('1') COMP(EQ)
     C                   EVAL      *IN(XX)=*IN(XX)
     C                   ENDDO
      *
     C                   MOVE      0             *IN
     C                   DO        99            XX                3 0
    MU* VAL1(*IN(XX)) VAL2('0') COMP(EQ)
     C                   EVAL      *IN(XX)=*IN(XX)
     C                   ENDDO
      *
     C                   MOVE      1             *IN
     C                   DO        99            XX                3 0
    MU* VAL1(*IN(XX)) VAL2('1') COMP(EQ)
     C                   EVAL      *IN(XX)=*IN(XX)
     C                   ENDDO
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test MOVEA
      *---------------------------------------------------------------------
     C     F_MOVEA       BEGSR
      *
     C                   MOVEA     *OFF          *IN
     C                   DO        99            XX                3 0
    MU* VAL1(*IN(XX)) VAL2('0') COMP(EQ)
     C                   EVAL      *IN(XX)=*IN(XX)
     C                   ENDDO
      *
     C                   MOVEA     '11111'       *IN(1)
    MU* VAL1(*IN(1)) VAL2('1') COMP(EQ)
     C                   EVAL      *IN(1)=*IN(1)
    MU* VAL1(*IN(2)) VAL2('1') COMP(EQ)
     C                   EVAL      *IN(2)=*IN(2)
    MU* VAL1(*IN(3)) VAL2('1') COMP(EQ)
     C                   EVAL      *IN(3)=*IN(3)
    MU* VAL1(*IN(4)) VAL2('1') COMP(EQ)
     C                   EVAL      *IN(4)=*IN(4)
    MU* VAL1(*IN(5)) VAL2('1') COMP(EQ)
     C                   EVAL      *IN(5)=*IN(5)
      *
     C                   MOVEA     '11111'       *IN(20)
    MU* VAL1(*IN(20)) VAL2('1') COMP(EQ)
     C                   EVAL      *IN(1)=*IN(20)
    MU* VAL1(*IN(21)) VAL2('1') COMP(EQ)
     C                   EVAL      *IN(2)=*IN(21)
    MU* VAL1(*IN(22)) VAL2(*ON) COMP(EQ)
     C                   EVAL      *IN(3)=*IN(22)
    MU* VAL1(*IN(23)) VAL2('1') COMP(EQ)
     C                   EVAL      *IN(4)=*IN(23)
    MU* VAL1(*IN(24)) VAL2(*ON) COMP(EQ)
     C                   EVAL      *IN(5)=*IN(24)
      *
     C                   MOVEA     '00000'       *IN(1)
    MU* VAL1(*IN(1)) VAL2('0') COMP(EQ)
     C                   EVAL      *IN(1)=*IN(1)
    MU* VAL1(*IN(2)) VAL2('0') COMP(EQ)
     C                   EVAL      *IN(2)=*IN(2)
    MU* VAL1(*IN(3)) VAL2('0') COMP(EQ)
     C                   EVAL      *IN(3)=*IN(3)
    MU* VAL1(*IN(4)) VAL2('0') COMP(EQ)
     C                   EVAL      *IN(4)=*IN(4)
    MU* VAL1(*IN(5)) VAL2('0') COMP(EQ)
     C                   EVAL      *IN(5)=*IN(5)
      *
     C                   MOVEA     '00000'       *IN(20)
    MU* VAL1(*IN(20)) VAL2('0') COMP(EQ)
     C                   EVAL      *IN(1)=*IN(20)
    MU* VAL1(*IN(21)) VAL2('0') COMP(EQ)
     C                   EVAL      *IN(2)=*IN(21)
    MU* VAL1(*IN(22)) VAL2(*OFF) COMP(EQ)
     C                   EVAL      *IN(3)=*IN(22)
    MU* VAL1(*IN(23)) VAL2('0') COMP(EQ)
     C                   EVAL      *IN(4)=*IN(23)
    MU* VAL1(*IN(24)) VAL2(*OFF) COMP(EQ)
     C                   EVAL      *IN(5)=*IN(24)
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test MOVEL
      *---------------------------------------------------------------------
     C     F_MOVEL       BEGSR
      *
    MU* VAL1(I02) VAL2('1') COMP(EQ)
     C                   MOVEL     *ON           I02
    MU* VAL1(I04) VAL2('1') COMP(EQ)
     C                   MOVEL     *ON           I04
    MU* VAL1(I01) VAL2(*ON) COMP(EQ)
     C                   MOVEL     I02           I01
    MU* VAL1(I03) VAL2('1') COMP(EQ)
     C                   MOVEL     I04           I03
    MU* VAL1(*IN(01)) VAL2('0') COMP(EQ)
     C                   MOVEL     *OFF          *IN(01)
    MU* VAL1(*IN(01)) VAL2('1') COMP(EQ)
     C                   MOVEL     *ON           *IN(01)
    MU* VAL1(*IN(01)) VAL2('0') COMP(EQ)
     C                   MOVEL     '0'           *IN(01)
    MU* VAL1(*IN(01)) VAL2('1') COMP(EQ)
     C                   MOVEL     '1'           *IN(01)
      *
    MU* VAL1(*IN(02)) VAL2('1') COMP(EQ)
     C                   MOVEL     1             *IN(02)
    MU* VAL1(*IN(02)) VAL2(*OFF) COMP(EQ)
     C                   MOVEL     0             *IN(02)
    MU* VAL1(I02) VAL2(*ON) COMP(EQ)
     C                   MOVEL     1             I02
    MU* VAL1(I02) VAL2(*OFF) COMP(EQ)
     C                   MOVEL     0             I02
      *
     C                   EVAL      NS=1
    MU* VAL1(*IN(02)) VAL2('1') COMP(EQ)
     C                   MOVEL     NS            *IN(02)
    MU* VAL1(I02) VAL2(*ON) COMP(EQ)
     C                   MOVEL     NS            I02
      *
     C                   EVAL      NS=0
    MU* VAL1(I02) VAL2(*OFF) COMP(EQ)
     C                   MOVEL     NS            I02
    MU* VAL1(*IN(02)) VAL2(*OFF) COMP(EQ)
     C                   MOVEL     NS            *IN(02)
      *
     C                   EVAL      NP=1
    MU* VAL1(*IN(02)) VAL2('1') COMP(EQ)
     C                   MOVEL     NP            *IN(02)
    MU* VAL1(I02) VAL2(*ON) COMP(EQ)
     C                   MOVEL     NP            I02
      *
     C                   EVAL      NP=0
    MU* VAL1(I02) VAL2(*OFF) COMP(EQ)
     C                   MOVEL     NP            I02
    MU* VAL1(*IN(02)) VAL2(*OFF) COMP(EQ)
     C                   MOVEL     NP            *IN(02)
      *
     C                   EVAL      ST='1111111111'
     C                   MOVEL     ST            *IN
     C                   DO        99            XX                3 0
    MU* VAL1(*IN(XX)) VAL2('1') COMP(EQ)
     C                   EVAL      *IN(XX)=*IN(XX)
     C                   ENDDO
      *
     C                   MOVEL     '0'           *IN
     C                   DO        99            XX                3 0
    MU* VAL1(*IN(XX)) VAL2('0') COMP(EQ)
     C                   EVAL      *IN(XX)=*IN(XX)
     C                   ENDDO
      *
     C                   MOVEL     '1'           *IN
     C                   DO        99            XX                3 0
    MU* VAL1(*IN(XX)) VAL2('1') COMP(EQ)
     C                   EVAL      *IN(XX)=*IN(XX)
     C                   ENDDO
      *
     C                   MOVEL     0             *IN
     C                   DO        99            XX                3 0
    MU* VAL1(*IN(XX)) VAL2('0') COMP(EQ)
     C                   EVAL      *IN(XX)=*IN(XX)
     C                   ENDDO
      *
     C                   MOVEL     1             *IN
     C                   DO        99            XX                3 0
    MU* VAL1(*IN(XX)) VAL2('1') COMP(EQ)
     C                   EVAL      *IN(XX)=*IN(XX)
     C                   ENDDO
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test RESET
      *---------------------------------------------------------------------
     C     F_RESET       BEGSR
     C
    MU* VAL1(I02) VAL2('0') COMP(EQ)
     C                   EVAL      I02=*OFF
    MU* VAL1(I04) VAL2('1') COMP(EQ)
     C                   EVAL      I04=*ON
    MU* VAL1(I02) VAL2('1') COMP(EQ)
     C                   RESET                   I02
    MU* VAL1(I04) VAL2('0') COMP(EQ)
     C                   RESET                   I04
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test SETOFF
      *---------------------------------------------------------------------
     C     F_SETOFF      BEGSR
    MU* VAL1(*IN(01)) VAL2('0') COMP(EQ)
     C                   SETOFF                                       01
    MU* VAL1(*IN(02)) VAL2(*OFF) COMP(EQ)
     C                   SETOFF                                       02
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test SETON
      *---------------------------------------------------------------------
     C     F_SETON       BEGSR
    MU* VAL1(*IN(01)) VAL2('1') COMP(EQ)
     C                   SETON                                        01
    MU* VAL1(*IN(02)) VAL2(*ON) COMP(EQ)
     C                   SETON                                        02
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test
      *---------------------------------------------------------------------
     C     F_OTHER       BEGSR
      *
     C                   CLEAR                   DS01
     C                   EVAL      FL02=*ON
     C                   EVAL      DS01='AAA'
     C                   IF        FL02=*ON
     C                   ENDIF
     C                   IF        FL02=*OFF
     C                   ENDIF
     C                   SETOFF                                       01
     C     *IN01         COMP      *ON                                0101
    MU* VAL1(*IN(01)) VAL2('1') COMP(EQ)
     C                   EVAL      *IN01=*IN01
      *
     C                   ENDSR
