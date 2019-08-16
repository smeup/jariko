   COP* *NOUI
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* 05/04/16  V4.R1   GIAGIU Creato
     V* B£61020C  V5R1    BMA Aggiunta COP* *NOUI
     V* 12/08/19  001059  BMA Ripresa internamente £UIBDS per copy non ancora supporte.
     V*                       Tolta procedure e call programma inesistente
     V* 13/08/19  001059  BMA Tolte parti commentate
     V*=====================================================================
     V* OBIETTIVO
     V*  Programma finalizzato ai test su campi di tipo DS Qualificate
     V*
     V*=====================================================================
     D §F1             DS                  LIKEDS(£UIBDS)
      * /COPY QILEGEN,£UIDDS
     D*----------------------------------------------------------------
     D £UIBDS          DS         31000
     D  £UIBTM                       10
     D  £UIBMS                       10
     D  £UIBPG                       10                                         Componente/Programma
     D  £UIBFU                       10                                         Programma/Funzione
     D  £UIBME                       10                                         Funz.Metodo/Metodo
      *
     D  £UIBT1                        2
     D  £UIBP1                       10
     D  £UIBK1                       15
     D  £UIBT2                        2
     D  £UIBP2                       10
     D  £UIBK2                       15
     D  £UIBT3                        2
     D  £UIBP3                       10
     D  £UIBK3                       15
      *
     D  £UIBPA                      256
      *
     D  £UIB35                        1
     D  £UIB36                        1
     D  £UIBCM                        7                                         Codice Messaggio
     D  £UIBFM                       10                                         File Messaggi
     D  £UIBSC                       10
      *
     D  £UIBT4                        2
     D  £UIBP4                       10
     D  £UIBK4                       15
     D  £UIBT5                        2
     D  £UIBP5                       10
     D  £UIBK5                       15
     D  £UIBT6                        2
     D  £UIBP6                       10
     D  £UIBK6                       15
      * Campo server LoocUp di 15 caratteri
     D  £UIBSR                       15
      * Campo client LoocUp di 15 caratteri
     D  £UIBCL                       15
      * Setup Richiesto
     D  £UIBRI                       58
      * Setup Grafico
     D  £UIBSG                        4
      * Setup del Setup
     D  £UIBSS                      256
      * Numero di sequenza
     D  £UIBSQ                        3
      *
     D  £UIBD1              1001  31000
     D*----------------------------------------------------------------
     D DS1             DS                  QUALIFIED
     D  ST1                          10A
     D  NR1                          10S 0 INZ(2)
     D  AR1                          10A   DIM(10)
     D  AR2                            N   DIM(10)
      *
     C                   EVAL      DS1.ST1='ABC'
      *
     C                   CLEAR                   §F1
     C                   CLEAR                   £UIBDS
      *
     C                   EVAL      £UIBT1='CN'
     C                   EVAL      £UIBP1='CLI'
     C                   EVAL      £UIBK1='C0001'
      *
    MU* VAL1(§F1.£UIBT1) VAL2('  ') COMP(EQ)
     C                   EVAL      §F1.£UIBT1=§F1.£UIBT1
    MU* VAL1(§F1.£UIBP1) VAL2('            ') COMP(EQ)
     C                   EVAL      §F1.£UIBP1=§F1.£UIBP1
    MU* VAL1(§F1.£UIBK1) VAL2('               ') COMP(EQ)
     C                   EVAL      §F1.£UIBK1=§F1.£UIBK1
    MU* VAL1(£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      £UIBT1=£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=£UIBK1
      *
     C                   CLEAR                   §F1
     C                   CLEAR                   £UIBDS
     C                   EVAL      §F1.£UIBT1='CN'
     C                   EVAL      §F1.£UIBP1='CLI'
     C                   EVAL      §F1.£UIBK1='C0001'
    MU* VAL1(§F1.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      §F1.£UIBT1=§F1.£UIBT1
    MU* VAL1(§F1.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      §F1.£UIBP1=§F1.£UIBP1
    MU* VAL1(§F1.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      §F1.£UIBK1=§F1.£UIBK1
    MU* VAL1(£UIBT1) VAL2('  ') COMP(EQ)
     C                   EVAL      £UIBT1=£UIBT1
    MU* VAL1(£UIBP1) VAL2('            ') COMP(EQ)
     C                   EVAL      £UIBP1=£UIBP1
    MU* VAL1(£UIBK1) VAL2('               ') COMP(EQ)
     C                   EVAL      £UIBK1=£UIBK1
      *---------------------------------------------------------------------------
     C                   MOVEA     *HIVAL        DS1.AR1
    MU* VAL1(DS1.AR1(01)) VAL2('ABC') COMP(EQ)
     C                   EVAL      DS1.AR1(01)='ABC'
      *
    MU* VAL1(DS1.AR2(01)) COMP(EQ) VAL2(DS1.AR2(02))
     C                   EVAL      DS1.AR2(02)=DS1.AR2(5-4)
      *
1    C                   IF        DS1.AR2(03) AND DS1.NR1=2
     C                   CLEAR                   DS1.ST1
1e   C                   ENDIF
    MU* VAL1(DS1.NR1) COMP(EQ) VAL2(12)
     C                   EVAL      DS1.NR1=(10*2+4)/2
      * * CALL
      *
     C                   SETON                                        LR
