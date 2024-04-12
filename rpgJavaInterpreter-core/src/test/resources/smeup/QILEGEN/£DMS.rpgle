      *====================================================================
      * smeup V6R1.021DV
      * Nome sorgente       : £DMS
      * Sorgente di origine : SMEDEV/QILEGEN(£DMS)
      * Esportato il        : 20240411 144729
      *====================================================================
      /IF NOT DEFINED(DMS_INCLUDED)
      /DEFINE DMS_INCLUDED
      *
      *****************************************************************
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* B£00722A  V3R1    CM Rimozione Gestione Interna degli errori
     V* 12/02/11  V3R1    BS Aggiunte Specifiche per controllo inclusione
    RD* £DMSC1 --> Pulizia iniziali variabili e aree di lavoro
     D*
     D* SCOPO DELLA ROUTINE:
     D* RINFRESCA TUTTE LE VARIABILI PRIMA DI INIZIARE I CONTROLLI
     D*
     D* Valori in uscita:
     D* -----------------
     D* . £D    : Puntatore
     D* . £2    : Puntatore £D2
     D* . £4    : Puntatore £D4
     D* . £D1   : schiera contenente i codici messaggio
     D* . £D2   : schiera contenente le variabili da associare al msg
     D* . £D3   : schiera contenente i testi dei messaggi INTERNI al pgm
     D* . £D4   : schiera contenente 45 bytes da concatenare
     D* . £DMSNR: Valore puntatore
     D* . £DMSTR: Retrive da Msgf (X o G)
     D* . £DMSTP: Reperimento messaggio (Da Msgf o da Pgm)
     D*           (Se i primi 3 byte sono = 'PGM' rilevo il messaggio
     D*            dalla schiera)
     D* . £DMSME: Codice msg
     D* . £DMSVA: Stringa valori delle variabili inserite nel messaggio
     D* . £DMSEL: Testo definito nella schiera messaggi
     D* . £DMSFI: Message file AS/400
     D* . £DMSVS: Flag guida per la segnalazione degli errori:
     D*           ' ' = A Video
     D*           'V' = A Video
     D*           'S' = Su Stampa
     D*
     D* Specifiche I inizializzazione DS:
     D* ---------------------------------
     D* . £DMS1M: Testo 1° messaggio di errore
     D* . £DMS1L: Variabile utilizzata per impostare testo 1 livello
     D* . £DMS2L: Variabile utilizzata per impostare testo 2 livello
     D* . £DMSST: Contenuto: concatenazione variabili in stringa
     D*
      *****************************************************************
      *
     C     £DMSC1        BEGSR
     C                   Z-ADD     0             £D                5 0          Puntatore
     C                   Z-ADD     0             £2                5 0          Puntatore
     C                   Z-ADD     0             £4                5 0          Puntatore
     C                   MOVEL     *BLANKS       £D1                            Sch. Msg
     C                   MOVEL     *BLANKS       £D2                            Sch. Var.
     C                   MOVEL     *BLANKS       £D3                            Sch. Txt pgm
     C                   MOVEL     *BLANKS       £D4                            Sch. 45 byts
     C                   Z-ADD     0             £DMSNR            5 0          Valore punt.
     C                   Z-ADD     0             £DMS2             5 0          "          "
     C                   Z-ADD     0             £DMS4             5 0          "          "
     C                   Z-ADD     0             £DMSLA            2 0          Lunghezza
     C                   Z-ADD     0             £DMSLN            2 0          "       "
     C                   MOVEL     *BLANKS       £DMSTR            1            Retrive Msgf
     C                   MOVEL     *BLANKS       £DMSTP            3            Msgf/schiera
     C                   MOVEL     *BLANKS       £DMSME            7            Codice msg
     C                   MOVEL     *BLANKS       £DMSVA           45            Stringa
     C                   MOVEL     *BLANKS       £DMSEL           80            Txt schiera
     C                   MOVEL     *BLANKS       £DMSFI           10            Msgf
     C                   MOVEL     *BLANKS       £DMSVS            1            Video/Stampa
      *
     C                   MOVEL     *BLANKS       £DMS1M                         DS
     C                   MOVEL     *BLANKS       £DMS2L                         DS
     C                   MOVEL     *BLANKS       £DMS2L                         DS
     C                   MOVEL     *BLANKS       £DMSST                         DS
      *
     C                   ENDSR
      *
      *****************************************************************
    RD* £DMSC2 --> Registrazione errori
     D*
     D* SCOPO DELLA ROUTINE:
     D* REGISTRAZIONE ERRORI
     D*
     D* Se richiesto reperimento da Msgf i primi 3 bytes non devono
     D* essere uguali a 'PGM'.
     D* . REGISTRA IL MESSAGGIO DI ERRORE E LE VARIABILI ASSOCIATE
     D* . REGISTRA IL TESTO DI PRIMO LIVELLO
     D* . REGISTRA LE VARIABILI ASSOCIATE
     D*   (£D1 e £D2)
     D*
     D* Se richiesto reperimento da schiera Messaggi, i primi tre
     D* bytes devono essere uguali a 'PGM'
     D* . REGISTRA NEL COD. MESSAGGIO I PRIMI 7 BYTES DELLA SCHIERA
     D* . REGISTRA NEL TESTO DI PRIMO LIVELLO IL TESTO DELLA SCHIERA
     D*   (£D1 e £D3)
     D*
     D* Se riceve un valore in £DMSIE si attiva la gestione delle
     D* segnalazioni WARNING (Finestra in funzione della gravita')
     D*
     D* Valori in entrata:
     D* ------------------
     D* . £DMSME: Codice messaggio (MSGF XXXnnnn)
     D* . £DMSFI: File messaggi (se blank, assume MSGBA)
     D* . £DMSVA: Stringa di 45 bytes contenente le variabili ancora
     D*           da concatenare nello stesso ordine in cui sono state
     D*           elencate nel testo del messaggio.
     D*           (Da &1 a &99)
     D* . £DMSEL: Testo definito nella schiera messaggi
     D* . £DMSIE: Indicatore di errore per gestione WARNING
     D*
     D* Valori di lavoro:
     D* ------------------
     D* . £D    : Puntatore schiera
     D* . £DMSNR: Valore Puntatore schiera
     D* . £DMSTP: Guida il tipo di reperimento del messaggio (Msgf/Pgm)
     D*           (Se i primi 3 byte sono = 'PGM' rilevo dalla schiera;
     D*            altrimenti dal Message file AS/400)
     D*****************************************************************
      *
     C     £DMSC2        BEGSR
1    C     £DMSNR        IFLT      100
2    C     £DMSFI        IFEQ      *BLANK
     C                   MOVEL(P)  'MSGBA'       £DMSFI
2e   C                   ENDIF
2    C     £DMSTP        IFEQ      *BLANKS
     C                   MOVEL     £DMSME        £DMSTP
2e   C                   ENDIF
     C                   ADD       1             £DMSNR            5 0          Puntatore
     C                   Z-ADD     £DMSNR        £D
2    C     £DMSTP        IFEQ      'PGM'
     C                   MOVEL     *BLANKS       £D1(£D)                        ------------
     C                   MOVEL     £DMSME        £D1(£D)                        Elemeto sch.
     C                   MOVEL     *BLANKS       £D2(£D)                        ------------
     C                   MOVEL     £DMSEL        £D3(£D)                        Txt schiera
2x   C                   ELSE
     C                   MOVEL     £DMSME        £D1(£D)                        Codice Msg
     C                   MOVE      £DMSFI        £D1(£D)                        Msgf
     C                   MOVEL     £DMSVA        £D2(£D)                        Variabili
     C                   MOVEL     *BLANKS       £D3(£D)                        ------------
2e   C                   ENDIF
      * Errori di tipo WARNING
2    C     £DMSIE        IFNE      0
     C     £DMSVS        ANDNE     'S'
     C                   CALL      'B£DMS1'
     C                   PARM                    £D1
     C                   PARM                    £D2
     C                   PARM                    £D3
     C                   PARM                    £PDSNP
     C                   PARM                    £D
     C                   PARM                    £DMSIE
3    C     £DMSIE        IFEQ      0
     C                   MOVEL     *BLANKS       £D1(£D)
     C                   MOVEL     *BLANKS       £D2(£D)
     C                   MOVEL     *BLANKS       £D3(£D)
     C                   SUB       1             £DMSNR
3x   C                   ELSE
     C                   Z-ADD     £DMSIE        £D
     C                   MOVE      '1'           *IN(£D)
3e   C                   ENDIF
2e   C                   ENDIF
     C                   Z-ADD     0             £DMSIE            2 0
      *
     C                   MOVEL     *BLANKS       £DMSME                         Codice msg
     C                   MOVEL     *BLANKS       £DMSTP                         Msgf/schiera
     C                   MOVEL     *BLANKS       £DMSFI                         Msgf
     C                   MOVEL     *BLANKS       £DMSVA                         Stringa
     C                   MOVEL     *BLANKS       £DMSEL                         Txt schiera
1e   C                   ENDIF
     C     £DMS2X        ENDSR
      *
      *****************************************************************
    RD* £DMSC3 --> Elabora messaggi memorizzati
     D*
     D* SCOPO DELLA ROUTINE:
     D* ELABORA TUTTI I MESSAGGI REGISTRATI NELLA SCHIERA £D1
     D*
     D* . £D1   : schiera contenente i codici messaggio
     D* . £D2   : schiera contenente le variabili da associare al msg
     D* . £D3   : schiera contenente i testi dei messaggi da pgm
     D* . £PDSNP: Nome programma
     D*
     D* Valori in entrata:
     D* ------------------
     D* . £DMSVS: Flag guida per la segnalazione degli errori:
     D*           ' ' = A Video
     D*           'V' = A Video
     D*           'S' = Su Stampa
     D*
      *****************************************************************
      *
     C     £DMSC3        BEGSR
1    C     £D1(1)        IFNE      *BLANKS
2    C     £DMSVS        IFEQ      'S'
     C                   CALL      'B£DMS2'
     C                   PARM                    £D1
     C                   PARM                    £D2
     C                   PARM                    £D3
     C                   PARM                    £PDSNP           10
2x   C                   ELSE
     C                   CALL      'B£DMS1'
     C                   PARM                    £D1
     C                   PARM                    £D2
     C                   PARM                    £D3
     C                   PARM                    £PDSNP           10
2e   C                   ENDIF
1x   C                   ELSE
     C                   MOVEL(P)  'VIS'         £DMS7F
     C                   EXSR      £DMSC7
     C                   EXSR      £DMSC8
1e   C                   ENDIF
     C     £DMS3X        ENDSR
      *
      *****************************************************************
    RD* £DMSC4 --> Emissione 1° messaggio
     D*
     D* SCOPO DELLA ROUTINE:
     D* PREPARA IL 1° MESSAGGIO PER L'EMISSIONE A VIDEO
     D*
     D* Valori di lavoro:
     D* -----------------
     D* . £DMSTR: Tipo retrive
     D* . £D    : Puntatore schiera
     D*
     D* Valori di uscita:
     D* -----------------
     D*
     D* - Sempre
     D*   . £DMS1M: testo del 1° messaggio di errore
     D*
     D* - A disposizione:
     D*   - Se richiesto reperimento da Msgf:
     D*     . £DMS1L: testo 1 livello
     D*     . £DMS2L: testo secondo livello
     D*
     D*   - Se richiesto reperimento da schiera Messaggi:
     D*     . £DMSME: codice messaggio
     D*     . £DMSEL: testo di errore (80 caratteri)
     D*
      *****************************************************************
      *
     C     £DMSC4        BEGSR
     C     £D1(1)        CABEQ     *BLANKS       £DMS4X
      *
     C                   MOVEL     *BLANKS       £DMS1M
      *
      * Da msgf
      *
1    C     £D3(1)        IFEQ      *BLANKS
     C                   MOVEL     'X'           £DMSTR
     C                   Z-ADD     1             £D
     C                   EXSR      £DMSC5
     C                   MOVEL     £DMS1L        £DMS1M
1x   C                   ELSE
      *
      * Da schiera messaggi
      *
     C                   MOVEL     *BLANKS       £DMSME
     C                   MOVEL     £D1(1)        £DMSME
     C                   MOVEL     *BLANKS       £DMSFI
     C                   MOVEL     *BLANKS       £DMSEL
     C                   MOVEL     £D3(1)        £DMS1L
     C                   MOVEL     £DMS1L        £DMS1M
1e   C                   ENDIF
      *
     C     £DMS4X        ENDSR
      *
      *****************************************************************
    RD* £DMSC5 --> Reperimento del messaggio
     D*
    HD*  SCOPO DELLA ROUTINE:
     D*  Esecuzione retrive message per:
     D*  Se 'X' visualizzazione finestre utente finale
     D*  Se 'G' gestione del messaggio con CHGMSGD
     D*
     D* Valori in entrata:
     D* ------------------
     D* . £DMSTR: Tipo retrive
     D* . £D    : Puntatore schiera
     D*
      *****************************************************************
      *
     CSR   £DMSC5        BEGSR
     C                   MOVEL     £DMSTR        £DMSOP            1            Opzione
     C                   MOVEL     £D1(£D)       £DMSCM            7            Codice Msg
     C                   MOVE      *BLANKS       £DMS1L                         1° Livello
     C                   MOVE      £D1(£D)       £DMSMF           10            Msgf
     C                   MOVE      *BLANKS       £DMS2L                         2° Livello
     C                   MOVEL     £D2(£D)       £DMSST                         Stringa Var.
      *
     C                   CALL      'B£DMS1CL'
     C                   PARM                    £DMSOP
     C                   PARM                    £DMSCM
     C                   PARM                    £DMS1L
     C                   PARM                    £DMSMF
     C                   PARM                    £DMS2L
     C                   PARM                    £DMSST
      *
     C                   MOVEL     *BLANKS       £DMSOP                         Opzione
     C                   MOVEL     *BLANKS       £DMSCM                         Codice Msg
     C                   MOVEL     *BLANKS       £DMSMF                         Msgf
     C                   MOVEL     *BLANKS       £DMSST                         Stringa Var.
      *
     C                   MOVE      *BLANKS       £DMSTR            1            Tipo Retrive
      *
     CSR   £DMS5X        ENDSR
      *
      *****************************************************************
    RD* £DMSC6 --> Concatenazione variabili
     D*
    HD*  SCOPO DELLA ROUTINE:
     D*  Concatena le variabili da passare al MESSAGGIO PARAMETRICO
     D*  rispettando la loro lunghezza originaria (Fisica).
     D*
     D* Regola di impostazione £DMSVA
     D* -----------------------------
     D* Attraverso delle CAT, la regola di scrittura di ciascuna
     D* variabile sara' la seguente:
     D*
     D* !-----!------!---------!
     D* ! ' ' !  DD  ! XXXXXXX !
     D* !-----!------!---------!
     D*
     D* . ' ' = Spazio obbligatorio per inizio dati associati alla
     D*         variabile da concatenare
     D* . DD  = Lunghezza fisica variabile (Come da Data base)
     D*         da concatenare.
     D*         Deve essere sempre epressa sempre su due caratteri
     D* . XXX = Variabile
     D*
     D* Valori in entrata:
     D* ------------------
     D* . £D    : Messaggio in elaborazione
     D* . £DMSVA: Stringa ( <Dimensione fisica e variabile> )
     D*
      *****************************************************************
      *
     CSR   £DMSC6        BEGSR
     C                   MOVEA     £DMSVA        £D4
     C                   Z-ADD     0             £DMS4
     C                   Z-ADD     0             £DMS2
     C                   Z-ADD     0             £DMSLA
     C                   Z-ADD     0             £DMSLN
      *
      * Ciclo 01
      *
     C     £DMS61        TAG
     C                   ADD       1             £DMS4
     C     £DMS4         CABGT     45            £DMS6X
     C                   Z-ADD     £DMS4         £4
      *
      * Caricamento Effettivo della variabile
      *
1    C     £D4(£4)       IFNE      *BLANKS
     C                   ADD       1             £DMS2
     C     £DMS2         CABGT     45            £DMS6X
     C                   Z-ADD     £DMS2         £2
     C                   MOVEL     £D4(£4)       £D2(£2)
     C                   SUB       1             £DMSLN
     C                   GOTO      £DMS61
1e   C                   ENDIF
      *
      * Ciclo 02
      *
     C     £DMS62        TAG
      *
      * Caricamento del restante spazio con blanks
      *
1    C     £DMSLN        IFGT      *ZEROS
     C                   ADD       1             £DMS2
     C     £DMS2         CABGT     45            £DMS6X
     C                   Z-ADD     £DMS2         £2
     C                   MOVEL     *BLANKS       £D2(£2)
     C                   SUB       1             £DMSLN
     C                   GOTO      £DMS62
1e   C                   ENDIF
      *
      * Memorizzazione lunghezza fisica variabile
      *
     C                   MOVE      *ALL'0'       £DMSLA
     C     £DMS63        TAG
      *
      * 1° Carattere
      *
     C                   ADD       1             £DMS4
     C     £DMS4         CABGT     45            £DMS6X
     C                   Z-ADD     £DMS4         £4
     C     £D4(£4)       CABEQ     *BLANKS       £DMS63
     C                   MOVEL     £D4(£DMS4)    £DMSLA
      *
      * 2° Carattere
      *
     C                   ADD       1             £DMS4
     C     £DMS4         CABGT     45            £DMS6X
     C                   Z-ADD     £DMS4         £4
     C                   MOVE      £D4(£DMS4)    £DMSLA
     C                   GOTO      £DMS61
      *
     CSR   £DMS6X        ENDSR
     D*----------------------------------------------------------------
     C     £DMSC7        BEGSR
      *
     C     ££B£2L        IFEQ      '11'
     C     £DMS7F        IFEQ      'INI'
     C                   MOVEL     ££B£2L        ££B£2L            2
     C                   BITOFF    '01234567'    £ATRIN            1
     C                   BITON     '27'          £ATRIN
     C     £ATRIN        CAT       '>':0         W$B£2L            2
     C                   ENDIF
      *
  MO>C                   IF        ££B£2J = '1'
  MO>C                   CALL      'B£DMS7'                             37
  MO>C                   PARM                    £DMS7F           10
  MO>C                   PARM                    £DMS7M           10
  MO>C                   PARM                    £DMS7T            2
  MO>C                   PARM                    £DMS7P           10
  MO>C                   PARM                    £DMS7C           15
  MO>C                   PARM                    £DMS75            1
  MO>C                   ELSE
     C                   EVAL      *IN37=*OFF
     C                   CALL      'B£DMS7'
     C                   PARM                    £DMS7F           10
     C                   PARM                    £DMS7M           10
     C                   PARM                    £DMS7T            2
     C                   PARM                    £DMS7P           10
     C                   PARM                    £DMS7C           15
     C                   PARM                    £DMS75            1
  MO>C                   ENDIF
     C                   MOVEL     *BLANKS       £DMS7F
     C                   MOVEL     *BLANKS       £DMS7M
     C                   ENDIF
     C                   ENDSR
     D*----------------------------------------------------------------
     C     £DMSC8        BEGSR
  MO>C                   IF        ££B£2J = '1'
  MO>C                   CALL      'B£DMS8'                             37
  MO>C                   PARM                    £PDSNP
  MO>C                   PARM                    £DMSRL           10
  MO>C                   PARM                    £DMSPT           10
  MO>C                   ELSE
     C                   EVAL      *IN37=*OFF
     C                   CALL      'B£DMS8'
     C                   PARM                    £PDSNP
     C                   PARM                    £DMSRL           10
     C                   PARM                    £DMSPT           10
  MO>C                   ENDIF
     C                   ENDSR
      /ENDIF
