   COP* *NOUI
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 20/08/19  001071  BMA Creazione
     V*=====================================================================
     V* OBIETTIVO
     V*  Programma finalizzato ai test su campi di tipo DS Qualificate
     V*
     V*=====================================================================
     D §F1             DS                  LIKEDS(£UIBDS)
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
      * La definizione 10S 0 o 10  0 è equivalente.
      * In una DS il tipo numerico predefinito è S (zoned)
      * Utilizzo *HIVAL e *LOVAL insieme alle annotazioni per testare il range di valori validi
     D  NR1                          10S 0 INZ(*LOVAL)
     D  NR2                          10  0 INZ(*HIVAL)
     D  AR1                          10A   DIM(10)
      * La definizione N o 1N (cioè indicatore o indicatore di lunghezza 1 è equivalente)
     D  AR2                            N   DIM(10)
      *
     D  NN1            S              5  0
      *
      * TODO support qualified
    MU* VAL1(DS1.NR1) VAL2(-9999999999) COMP(EQ)
    MU* VAL1(DS1.NR2) VAL2(9999999999) COMP(EQ)
    MU* VAL1(DS1.AR1) VAL2('') COMP(EQ)
    MU* VAL1(DS1.AR2(01)) VAL2(' ') COMP(EQ)
    MU* VAL1(DS1.AR2(02)) VAL2(' ') COMP(EQ)
    MU* VAL1(DS1.AR2(03)) VAL2(' ') COMP(EQ)
    MU* VAL1(DS1.AR2(04)) VAL2(' ') COMP(EQ)
    MU* VAL1(DS1.AR2(05)) VAL2(' ') COMP(EQ)
    MU* VAL1(DS1.AR2(06)) VAL2(' ') COMP(EQ)
    MU* VAL1(DS1.AR2(07)) VAL2(' ') COMP(EQ)
    MU* VAL1(DS1.AR2(08)) VAL2(' ') COMP(EQ)
    MU* VAL1(DS1.AR2(09)) VAL2(' ') COMP(EQ)
    MU* VAL1(DS1.AR2(10)) VAL2(' ') COMP(EQ)
    MU* VAL1(NN1) VAL2(140) COMP(EQ)
     C                   EVAL      NN1=%LEN(DS1)
    MU* VAL1(DS1.AR2(01)) VAL2('1') COMP(EQ)
    MU* VAL1(DS1.AR2(02)) VAL2('1') COMP(EQ)
    MU* VAL1(DS1.AR2(03)) VAL2('1') COMP(EQ)
    MU* VAL1(DS1.AR2(04)) VAL2('1') COMP(EQ)
    MU* VAL1(DS1.AR2(05)) VAL2('1') COMP(EQ)
    MU* VAL1(DS1.AR2(06)) VAL2('1') COMP(EQ)
    MU* VAL1(DS1.AR2(07)) VAL2('1') COMP(EQ)
    MU* VAL1(DS1.AR2(08)) VAL2('1') COMP(EQ)
    MU* VAL1(DS1.AR2(09)) VAL2('1') COMP(EQ)
    MU* VAL1(DS1.AR2(10)) VAL2('1') COMP(EQ)
      * EVAL su un array senza specificare l'indice dell'elemento valorizza tutti gli elementi
     C                   EVAL      DS1.AR2=*ON
    MU* VAL1(DS1.AR2(01)) VAL2('0') COMP(EQ)
    MU* VAL1(DS1.AR2(02)) VAL2('0') COMP(EQ)
    MU* VAL1(DS1.AR2(03)) VAL2('0') COMP(EQ)
    MU* VAL1(DS1.AR2(04)) VAL2('0') COMP(EQ)
    MU* VAL1(DS1.AR2(05)) VAL2('0') COMP(EQ)
    MU* VAL1(DS1.AR2(06)) VAL2('0') COMP(EQ)
    MU* VAL1(DS1.AR2(07)) VAL2('0') COMP(EQ)
    MU* VAL1(DS1.AR2(08)) VAL2('0') COMP(EQ)
    MU* VAL1(DS1.AR2(09)) VAL2('0') COMP(EQ)
    MU* VAL1(DS1.AR2(10)) VAL2('0') COMP(EQ)
     C                   EVAL      DS1.AR2=*OFF
      *
    MU* VAL1(§F1.£UIBDS) VAL2('') COMP(EQ)
    MU* VAL1(§F1.£UIBT1) VAL2('') COMP(EQ)
    MU* VAL1(§F1.£UIBP1) VAL2('') COMP(EQ)
    MU* VAL1(§F1.£UIBK1) VAL2('') COMP(EQ)
    MU* VAL1(§F1.£UIBD1) VAL2('') COMP(EQ)
     C                   CLEAR                   §F1
    MU* VAL1(£UIBDS) VAL2('') COMP(EQ)
    MU* VAL1(£UIBT1) VAL2('') COMP(EQ)
    MU* VAL1(£UIBP1) VAL2('') COMP(EQ)
    MU* VAL1(£UIBK1) VAL2('') COMP(EQ)
    MU* VAL1(£UIBD1) VAL2('') COMP(EQ)
     C                   CLEAR                   £UIBDS
    MU* VAL1(§F1.£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      §F1.£UIBT1='CN'
    MU* VAL1(§F1.£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      §F1.£UIBP1='CLI'
    MU* VAL1(§F1.£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      §F1.£UIBK1='C0001'
    MU* VAL1(£UIBT1) VAL2('CN') COMP(EQ)
     C                   EVAL      £UIBT1=§F1.£UIBT1
    MU* VAL1(£UIBP1) VAL2('CLI') COMP(EQ)
     C                   EVAL      £UIBP1=§F1.£UIBP1
    MU* VAL1(£UIBK1) VAL2('C0001') COMP(EQ)
     C                   EVAL      £UIBK1=§F1.£UIBK1
      *
     C                   SETON                                        LR
