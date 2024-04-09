      *====================================================================
      * smeup V6R1.021DV
      * Nome sorgente       : £G40
      * Sorgente di origine : SMEDEV/QILEGEN(£G40)
      * Esportato il        : 20240409 121042
      *====================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 05/09/07  V2R3    BS Gestione PARM specifica per gestione liste estese
     V* 18/02/08  V2R3    BS Gestione livello di chiamata
     V* 16/01/10  V2R3    BS Gestione ricezione nome programma
     V* 17/01/10  V2R3    BS Forzatura Programma di default in base alla funzione
     V* 06/02/10  V2R3    BS Forzatura entry estesa se programma B£G40E
     V* 17/02/10  V2R3    BS Forzatura programma di default in base alla funzione
     V* 21/02/10  V2R3    BS Rilascio modifiche a partire dal 17/01/10 in DEV
     V* 21/02/10  V2R3    BS Forzatura programma di default per funzione PRE
     V* 28/02/10  V2R3    BS Forzatura programma di default per funzione FON
     V* 07/03/10  V2R3    BS Parametri estesi se programma B£G40I
     V* 12/03/10  V2R3    BS Su funzioni DEC/SCA forzato utilizzo parametor esteso
     V* 12/07/10  V2R3    BS Forzatura su funzione GES/JOB
     V* B£00722A  V3R1    CM Rimozione Gestione Interna degli errori
     V* 14/09/15  V4R1    BS Rettifica risalita utilizzo campi estesi
     V* 15/09/15  V4R1    BS Forzatura su funzione TES
     D*----------------------------------------------------------------
     D* OBIETTIVO
     D*  Gestire e memorizzare liste di oggetti
     D* NOTA
     D*
     D* FLUSSO
     D*  prerequisiti:
     D*/COPY £G40E
     D*
     D* FUNZIONI/METODI
     D*----------------------------------------------------------------
     D* ESEMPIO DI CHIAMATA
      *C                   MOVEL     '<          >'£G40FU                         P
      *C                   MOVEL     '<          >'£G40ME                         P
      *C                   MOVEL     '<          >'£G40TP                         P
      *C                   MOVEL     '<          >'£G40PA                         P
      *C                   MOVEL     '<          >'£G40CD                         P
      *C                   MOVEL     '<          >'£G40MV                         P
      *C                   EXSR      £G40
      *C                   MOVEL     £G04DM        '<          >'
      *----------------------------------------------------------------
     C     £G40          BEGSR
      *
     C                   IF        £G40PG=' '
     C                   SELECT
     C                   WHEN      %SUBST(£G40FU:1:3)='LIS' OR £G40FU='SQL'
     C                   EVAL      £G40PG='B£G40E'
     C                   WHEN      £G40FU='PRE'
     C                   EVAL      £G40PG='B£G40P'
     C                   WHEN      £G40FU='TST'
     C                   EVAL      £G40PG='B£G40T'
     C                   WHEN      £G40FU='FON'
     C                   EVAL      £G40PG='B£G40F'
     C                   OTHER
     C                   EVAL      £G40PG='B£G40G'
     C                   ENDSL
     C                   ENDIF
      *
     C*****              IF        £G40FU='GES' AND £G40ME='JOB'
     C                   IF        £G40FU='GES' OR £G40FU='COM' OR £G40FU='TES'
     C                   EVAL      £G40AE='1'
     C                   ENDIF
      *
     C                   EVAL      £G40PG=%TRIMR(£G40PG)+£G40LC
      *
     C                   MOVEL     '0'           £G4035
     C                   MOVEL     '0'           £G4036
      *
     C                   IF        £G40AE=''
     C                             AND %SUBST(£G40PG:1:6)<>'B£G40E'
     C                             AND %SUBST(£G40PG:1:6)<>'B£G40P'
     C                             AND %SUBST(£G40PG:1:6)<>'B£G40T'
     C                             AND %SUBST(£G40PG:1:6)<>'B£G40F'
     C                             AND %SUBST(£G40PG:1:6)<>'B£G40I'
     C                             AND £G40FU<>'DEC'
     C                             AND £G40FU<>'SCA'
  MO>C                   IF        ££B£2J = '1'
  MO>C                   CALL      £G40PG                               37
  MO>C                   PARM                    £G40FU           10            -->
  MO>C                   PARM                    £G40ME           10            -->
  MO>C                   PARM                    £G40MS            7            <--
  MO>C                   PARM                    £G40FI           10            <--
  MO>C                   PARM                    £G40CM            2            <--
  MO>C                   PARM                    £G40TP            2            -->  Tipo
  MO>C                   PARM                    £G40PA           10            -->  Param.
  MO>C                   PARM                    £G40CD           15            -->  Codice
  MO>C                   PARM                    £40A                           <--> Sch.Oggetti
  MO>C                   PARM                    £40B                           <--> Sch.TipPar.
  MO>C                   PARM                    £G40MV           15            <--> Cod.MDV
  MO>C                   PARM                    £G40DM           35            <--- Des.MDV
  MO>C                   PARM                    £G4035            1            <---
  MO>C                   PARM                    £G4036            1            <---
  MO>C                   PARM                    £G40DS                         <--> Estensione
  MO>C                   ELSE
     C                   EVAL      *IN37=*OFF
     C                   CALL      £G40PG
     C                   PARM                    £G40FU           10            -->
     C                   PARM                    £G40ME           10            -->
     C                   PARM                    £G40MS            7            <--
     C                   PARM                    £G40FI           10            <--
     C                   PARM                    £G40CM            2            <--
     C                   PARM                    £G40TP            2            -->  Tipo
     C                   PARM                    £G40PA           10            -->  Param.
     C                   PARM                    £G40CD           15            -->  Codice
     C                   PARM                    £40A                           <--> Sch.Oggetti
     C                   PARM                    £40B                           <--> Sch.TipPar.
     C                   PARM                    £G40MV           15            <--> Cod.MDV
     C                   PARM                    £G40DM           35            <--- Des.MDV
     C                   PARM                    £G4035            1            <---
     C                   PARM                    £G4036            1            <---
     C                   PARM                    £G40DS                         <--> Estensione
  MO>C                   ENDIF
     C                   ELSE
  MO>C                   IF        ££B£2J = '1'
  MO>C                   CALL      £G40PG                               37
  MO>C                   PARM                    £G40FU           10            -->
  MO>C                   PARM                    £G40ME           10            -->
  MO>C                   PARM                    £G40MS            7            <--
  MO>C                   PARM                    £G40FI           10            <--
  MO>C                   PARM                    £G40CM            2            <--
  MO>C                   PARM                    £G40TP            2            -->  Tipo
  MO>C                   PARM                    £G40PA           10            -->  Param.
  MO>C                   PARM                    £G40CD           15            -->  Codice
  MO>C                   PARM                    £40A                           <--> Sch.Oggetti
  MO>C                   PARM                    £40B                           <--> Sch.TipPar.
  MO>C                   PARM                    £G40MV           15            <--> Cod.MDV
  MO>C                   PARM                    £G40DM           35            <--- Des.MDV
  MO>C                   PARM                    £G4035            1            <---
  MO>C                   PARM                    £G4036            1            <---
  MO>C                   PARM                    £G40DS                         <--> Estensione
  MO>C                   PARM                    £40AE                          <--> Estensione
  MO>C                   ELSE
     C                   EVAL      *IN37=*OFF
     C                   CALL      £G40PG
     C                   PARM                    £G40FU           10            -->
     C                   PARM                    £G40ME           10            -->
     C                   PARM                    £G40MS            7            <--
     C                   PARM                    £G40FI           10            <--
     C                   PARM                    £G40CM            2            <--
     C                   PARM                    £G40TP            2            -->  Tipo
     C                   PARM                    £G40PA           10            -->  Param.
     C                   PARM                    £G40CD           15            -->  Codice
     C                   PARM                    £40A                           <--> Sch.Oggetti
     C                   PARM                    £40B                           <--> Sch.TipPar.
     C                   PARM                    £G40MV           15            <--> Cod.MDV
     C                   PARM                    £G40DM           35            <--- Des.MDV
     C                   PARM                    £G4035            1            <---
     C                   PARM                    £G4036            1            <---
     C                   PARM                    £G40DS                         <--> Estensione
     C                   PARM                    £40AE                          <--> Estensione
  MO>C                   ENDIF
     C                   ENDIF
      *
  MO>C   37              DO
  MO>C                   CALL      'B£GGP0  '
  MO>C                   PARM      'B£G40G'      £GGPNP           10
  MO>C                   PARM      *BLANKS       £GGPTP           10
  MO>C                   PARM      *BLANKS       £GGPPA          100
  MO>C                   ENDDO
      *
     C     £G4035        COMP      *ON                                    35
     C     £G4036        COMP      *ON                                    36
      *
     C                   CLEAR                   £G40LC            1
     C                   CLEAR                   £G40PG           10
      *
     C                   ENDSR
      *----------------------------------------------------------------
