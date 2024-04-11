      *====================================================================
      * smeup V6R1.021DV
      * Nome sorgente       : £ATRC
      * Sorgente di origine : SMEDEV/QILEGEN(£ATRC)
      * Esportato il        : 20240411 144730
      *====================================================================
     V*=====================================================================
     V* MODIFICHE Ril. Au Descrizione
     V* gg/mm/aa  VnRm xx Breve descrizione
     V*=====================================================================
     V* 13/08/03  V2R1 ZZ Aggiunto ambiente per gestione override
     V*                   colori in schiera £ATRPU (sviluppo futuro)
     V* 01/12/06  V2R2 GG Tavolozza RGB
     V* 11/03/08  V2R3 BS Aggiunta £ATRC_N
     V* 26/11/08  V2R3 GG Corretto £ATRCR.
     V*                   Aggiunto £ATRCL (CR + LF) da usare x nuova riga
     V*                   Aggiunto £ATRLC (LF + CR)
     V*=====================================================================
     V* B£00722A  V3R1    CM Rimozione Gestione Interna degli errori
     D*----------------------------------------------------------------   FRF13
     D* OBIETTIVO                                                         FRF13
     D*   Costruzione di un carattere "attributo di visualizzazione"      FRF13
     D*
     D* PARAMETRI                                                         FRF13
     D*
     D* FUNZIONE = 'LIC'
     D* METODO   =  nn  (con nn=01-15)
     D*   OUTPUT : £ATRVA del Livello nn (vedi TSTATR + F9)
     D*
     D* FUNZIONE = 'ATTVI'
     D*   INPUT  : codice V2/ATTVI nel primo elemento di £AT
     D*   OUTPUT : £ATRVA del codice
     D*
     D* FUNZIONE = Blank o altro
     D*   INPUT  : Schiera £AT di 4 elementi con:                         FRF13
     D*    Attributi                                                      FRF13
     D*           N : Normale                                             FRF13
     D*           R : Reverse                                             FRF13
     D*           H : Alta intensità                                      FRF13
     D*           U : Sottolineata                                        FRF13
     D*           B : Lampeggiamento                                      FRF13
     D*           C : Column Separator                                    FRF13
     D*           I : Non visualizzabile                                  FRF13
     D*    Colori                                                         FRF13
     D*           0 : MONOCROMATICO (DEFAULT)
     D*           1 : BLU
     D*           2 : VERDE
     D*           3 : ROSA
     D*           4 : ROSSO
     D*           5 : TURCHESE
     D*           6 : BIANCO
     D*           7 : GIALLO
     D*   OUTPUT : £ATRVA con l'attributo composto.                       FRF13
     D*                                                                   FRF13
     D* PREREQUISITI                                                      FRF13
     D*   Copy £ATRE per definizione schiera                              FRF13
     D*                                                                   FRF13
     D* ESEMPIO DI RICHIAMO                                               FRF13
     D*C                     MOVELvalore1   £AT,1
     D*C                     MOVELvalore2   £AT,2
     D*C                     MOVELvalore3   £AT,3
     D*C                     MOVELvalore4   £AT,4
     D*C                     EXSR £ATRC
     D*C                     MOVEL£ATRVA    ATR
     D*----------------------------------------------------------------   FRF13
     C     £ATRC         BEGSR
      *                                                                   FRF13
  M >C                   IF        ££B£2J = '1'
  M >C                   CALL      'B£ATRC0'                            37
  M >C                   PARM                    £ATRFU           10
  M >C                   PARM                    £ATRME           10
  M >C                   PARM                    £AT
  M >C                   PARM                    £ATRVA            1
  M >C                   PARM      *BLANKS       £ATRRC            7
  M >C                   PARM      *BLANKS       £ATRFI           10
  M >C                   PARM      *BLANKS       £ATRPU
  M >C                   PARM      £PDSNP        £ATRAM           10
  M >C                   PARM                    £ATRGB
  M >C                   PARM                    £ATRGB_N
  M >C                   PARM                    £ATRCG
  M >C                   PARM                    £ATRCG_N
  M >C                   ELSE
     C                   EVAL      *IN37=*OFF
     C                   CALL      'B£ATRC0'
     C                   PARM                    £ATRFU           10
     C                   PARM                    £ATRME           10
     C                   PARM                    £AT
     C                   PARM                    £ATRVA            1
     C                   PARM      *BLANKS       £ATRRC            7
     C                   PARM      *BLANKS       £ATRFI           10
     C                   PARM      *BLANKS       £ATRPU
     C                   PARM      £PDSNP        £ATRAM           10
     C                   PARM                    £ATRGB
     C                   PARM                    £ATRGB_N
     C                   PARM                    £ATRCG
     C                   PARM                    £ATRCG_N
  M >C                   ENDIF
      *                                                                   FRF13
     C                   MOVEL     *BLANKS       £AT
      * Includo vecchia £ATR
     C                   BITOFF    '01234567'    £ATRNO            1
     C                   BITOFF    '01234567'    £ATRIN            1
     C                   BITOFF    '01234567'    £ATRIH            1
     C                   BITOFF    '01234567'    £ATRHI            1
     C                   BITOFF    '01234567'    £ATRSO            1
     C                   BITOFF    '01234567'    £ATRNV            1
     C                   BITOFF    '01234567'    £ATRLA            1
     C                   BITOFF    '01234567'    £ATRSE            1
     C                   BITOFF    '01234567'    £ATRSH            1            SOTT/ALTA IN
     C                   BITOFF    '01234567'    £ATRLH            1            LAMP/ALTA IN
     C                   BITOFF    '01234567'    £ATRSI            1            SOTT/INVERSIONE
     C                   BITOFF    '01234567'    £ATRLI            1            LAMP/INVERSIONE
     C                   BITOFF    '01234567'    £ATRGR            1            SOTT/INVERSIONE
     C                   BITOFF    '01234567'    £ATRFT            1            SOTT/INVERSIONE
     C                   BITOFF    '01234567'    £ATRCR            1            CR          ONE
     C                   BITOFF    '01234567'    £ATRLF            1            LF          ONE
     C                   BITOFF    '01234567'    £ATRTB            1            Tab         ONE
     C*
     C                   CLEAR                   £ATRCL            2            LF          ONE
     C                   CLEAR                   £ATRLC            2            LF          ONE
     C*
     C                   BITON     '2'           £ATRNO
     C                   BITON     '27'          £ATRIN
     C                   BITON     '267'         £ATRIH
     C                   BITON     '26'          £ATRHI
     C                   BITON     '25'          £ATRSO
     C                   BITON     '2567'        £ATRNV
     C                   BITON     '24'          £ATRLA
     C                   BITON     '23'          £ATRSE
     C                   BITON     '256'         £ATRSH
     C                   BITON     '246'         £ATRLH
     C                   BITON     '257'         £ATRSI
     C                   BITON     '247'         £ATRLI
     C                   BITON     '34567'       £ATRGR
     C                   BITON     '023467'      £ATRFT
     C                   BITON     '457'         £ATRCR
     C                   BITON     '257'         £ATRLF
     C                   EVAL      £ATRCL=£ATRCR+£ATRLF
     C                   EVAL      £ATRLC=£ATRLF+£ATRCR
     C                   BITON     '57'          £ATRTB
     C*
     C                   ENDSR
     D*----------------------------------------------------------------   FRF13
     C     £ATRC_N       BEGSR
      *                                                                   FRF13
     C                   MOVEL(P)  *BLANKS       £ATRRC            7
     C                   MOVEL(P)  *BLANKS       £ATRFI           10
     C                   MOVEL(P)  *BLANKS       £ATRPU
      *
     C                   CLEAR                   £ATRNO            1
     C                   CLEAR                   £ATRIN            1
     C                   CLEAR                   £ATRIH            1
     C                   CLEAR                   £ATRHI            1
     C                   CLEAR                   £ATRSO            1
     C                   CLEAR                   £ATRNV            1
     C                   CLEAR                   £ATRLA            1
     C                   CLEAR                   £ATRSE            1
     C                   CLEAR                   £ATRSH            1            SOTT/ALTA IN
     C                   CLEAR                   £ATRLH            1            LAMP/ALTA IN
     C                   CLEAR                   £ATRSI            1            SOTT/INVERSIONE
     C                   CLEAR                   £ATRLI            1            LAMP/INVERSIONE
     C                   CLEAR                   £ATRGR            1            SOTT/INVERSIONE
     C                   CLEAR                   £ATRFT            1            SOTT/INVERSIONE
     C                   CLEAR                   £ATRCR            1            CR          ONE
     C                   CLEAR                   £ATRLF            1            LF          ONE
     C                   CLEAR                   £ATRCL            2            LF          ONE
     C                   CLEAR                   £ATRLC            2            LF          ONE
     C*
     C                   CLEAR                   £ATRNO
     C                   CLEAR                   £ATRIN
     C                   CLEAR                   £ATRIH
     C                   CLEAR                   £ATRHI
     C                   CLEAR                   £ATRSO
     C                   CLEAR                   £ATRNV
     C                   CLEAR                   £ATRLA
     C                   CLEAR                   £ATRSE
     C                   CLEAR                   £ATRSH
     C                   CLEAR                   £ATRLH
     C                   CLEAR                   £ATRSI
     C                   CLEAR                   £ATRLI
     C                   CLEAR                   £ATRGR
     C                   CLEAR                   £ATRFT
     C                   CLEAR                   £ATRCR
     C                   CLEAR                   £ATRLF
     C                   CLEAR                   £ATRCL                         LF          ONE
     C                   CLEAR                   £ATRLC                         LF          ONE
      *
     C                   ENDSR
     D*----------------------------------------------------------------   FRF13
