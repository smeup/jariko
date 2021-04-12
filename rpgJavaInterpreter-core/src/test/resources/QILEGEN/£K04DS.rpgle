      /IF NOT DEFINED(K04DS_INCLUDED)
      /DEFINE K04DS_INCLUDED
     D* ==============================================================
     D* MODIFICHE Ril.  T Au Descrizione
     D* gg/mm/aa  nn.mm i xx Breve descrizione
     D* ==============================================================
     D* 06/04/13  V3R2    BS Creazione
     D* 23/04/13  V3R2    BS Aggiunto campo £K04O_OD
     D* 20/05/13  V3R2    BS Ricerca Emulazione
     D* 04/06/13  V3R2    BS Aggiunto Campo Gestione
     D* 16/06/13  V3R2    BS Aggiunto Campo £K04FO
     D* 26/08/13  V4R1    CM Tipo risalita immagine
     D* 04/09/13  V4R1    BS Aggiungo Codice come Campo di Input e Codice Script Menù di Output
     D* 23/04/14  V4R1    BS Aggiunto Campo Codice Numerico
     D* 23/04/14          BS Aggiunto Campo Cartella a Menù
     D* 01/05/14          BS Aggiunti Campi Preview/Immagine a Menù
     D* 19/06/14  V4R1    BS Rilascio modifiche precedenti
     D* 19/06/14         BMA Aggiunta DS che rimappa £K04FO per restituzione valori combo
     D* 22/06/15  V4R1    BS Aggiunto Campo Codice con caratteri minuscoli
     D* 06/09/15  V4R1    BS Aggiunte nuove istanze
     D* 02/11/15  V4R1   BMA Aggiunto £K04O_VO per determinare se visualizzare cod. o desc. oggetto
     D* 16/02/16  V4R1    BS Aggiunto flag "gestione client"
     D* 21/04/16  V4R1    BS Aggiunta variabile per Trattamento Aut.Ogg.
     D* 10/06/16       BERNI Tolto trattamento autorizzazioni oggetto, aggiunta variabile numerosità
     D*                      e modalità di selezione
     D* 21/06/16          BS Ripristinata variabile per Trattamento Aut.Ogg.
     D* 11/07/16  V4R1 BERNI Rilasciate a standard modifiche 10-21/6/16
     D* 30/08/16  V5R1    BS Aggiunto campo filtro livello massimo
     D* 06/09/16  V5R1    BS Rettifica descrizione £K04O_GE
     D* 09/09/16  V5R1 PEDSTE Aggiunto numero elementi prima pagina
     D* 31/03/17  V5R1    CM Aggiunto categoria parametri e contenitore note
     D* 03/05/17  V5R1   BMA Aggiunto forza scheda
     D* 21/08/17  V5R1   BMA Aggiunto £K04O_DC per determinare come comporre decodifica della classe
     D*                      Aggiunto £K04O_EC per comporre decodifica estesa della classe
     D* 24/08/17  V5R1    BS Aggiunti £K04O_FG/PG/FO/PO
     D* 29/09/17  V5R1    AA Aggiunto £K04O_DE per attivazione descrizione estesa in £DEC
     D* 11/10/17  V5R1    BS Aggiunti £K04O_FH/PH/FP/PP
     D* 28/12/17  V5R1    BS Previsto £K04O_EF
     D* 02/01/18  V5R1    BS Previsto £K04O_SP
     D* 02/01/18  V5R1    BS Previsto £K04O_CE
     D* 20/02/18  V5R1    BS Previsto £K04O_AT
     D* 06/03/18  V5R1    BS Previsto £K04O_OB
     D* 07/03/18  V5R1    BS Previsto £K04O_LU
     D* 17/03/18  V5R1    BS Previsto £K04O_SI
     D* 19/05/18  V5R1    BS Prevista £K04O_FI
     D* 01/11/18  V5R1    BS Prevista £K04O_LS
     D* 04/11/18  000191  BS Prevista £K04O_FS
     D* 05/11/18  V5R1    CM Check-out 000191 in SMEUP_TST
     D* 03/12/18  000228  CM Oggetto gestito numerico
     V* 18/01/19  V5R1    PEDSTE Check-out 000228
     V* 29/01/19  000408  PEDSTE Aggiunti Piano di Sviluppo e Suggerimenti/Domande/Errori
     D* 30/01/19  V5R1    AS Check-out 000408 in SMEUP_TST
     V* 07/02/19  000438  PEDSTE Aggiunto Abstract
     V* 08/02/19  V5R1    CM Check-out 000438 in SMEUP_TST
     V* 20/02/20  V5R1    CM Check-out 000408 000438 in SMEDEV
     V* 06/10/20  002193  COMFED Aggiunta campo £K04O_AP
     V* 09/10/20  V5R1    AS Check-out 002193 in SMEDEV
     D* ==============================================================
      * Campi della /Copy
     D £K04FU          S             10                                         Funzione
     D £K04ME          S             10                                         Metodo
     D £K04MS          S              7                                         Msg
     D £K04FI          S             10                                         File
     D £K04VA          S             45                                         Var.msg
     D £K04CM          S              2                                         Comando
     D £K0435          S              1N                                        Indicatore 35
     D £K0436          S              1N                                        Indicatore 36
      *
      * DS di Input
      * -----------
     D£K04DI           DS          2000    INZ
     D £K04I_OG                      52                                         Classe
     D £K04I_CD                     256                                         Codice
      *
      * DS di Output
      * ------------
     D£K04DO           DS          3000    INZ
     D £K04O_TP                       2                                         Tipo
     D £K04O_PA                      50                                         Parametro
     D £K04O_RD                       1                                         Ricerca x Decodifica
     D £K04O_UL                       2                                         UsrLvl
     D £K04O_PE                       1                                         Parametro Esteso
     D £K04O_KE                       1                                         Codice Esteso
     D £K04O_CL                       1                                         Client
     D £K04O_IP                       1                                         Modalità Input Panel
     D £K04O_OD                       1                                         Presenza Decodifica
     D £K04O_RC                       1                                         Ricerca client
     D £K04O_RE                       1                                         Ricerca emulazione
     D £K04O_GE                       1                                         Funzione di gestione
     D £K04O_NC                       1                                         No Schema T/CD*
     D £K04O_RI                       1                                         Risalita immagine
      *                                                                         .1=Classe 2=Istanza
     D £K04O_EI                      10                                         Filler
     D £K04O_CR                      52                                         Filler
     D £K04O_OS                       1                                         Oggetto standard
     D £K04O_NR                       1                                         Codice Numerico
     D £K04O_FL                       1                                         Cartella a Menù
     D £K04O_PR                       1                                         Preview a Menù
     D £K04O_IM                       1                                         Immagine a Menù
     D £K04O_CM                       1                                         Caratteri minuscoli
     D £K04O_II                       1                                         N°istanze indefinito
     D £K04O_MA                       1                                         Classe Master
     D £K04O_SL                       1                                         Scansione Lenta
     D £K04O_PC                       1                                         Parametro indefinito
     D £K04O_VO                       1                                         Vis. Oggetto V2K04VO
     D £K04O_GC                       1                                         Gestione client
     D £K04O_AU                       1                                         Trattamento Aut.Ogg.
     D £K04O_NM                       1                                         Numerosità
     D £K04O_MS                       3                                         Modalità selezione
     D £K04O_LV                       1                                         Filtro liv. massimo
     D £K04O_NE                       5                                         Num.elem.1a pagina
     D £K04O_CN                       3                                         Contenitore note
     D £K04O_CP                       3                                         Categoria parametro
     D £K04O_SK                       1                                         Forza scheda no A12
     D £K04O_DC                       1                                         Dec. classe V2K04DC
     D £K04O_EC                       1                                         Dec. classe V2K04EC
     D £K04O_FG                      12                                         Forma grafica INP B
     D £K04O_PG                     256                                         Parametr.forma graf.
     D £K04O_FO                      12                                         Forma grafica INP O
     D £K04O_PO                     256                                         Parametr.forma graf.
     D £K04O_DE                       1                                         Attiva desc.estesa
     D £K04O_FH                      12                                         Forma grafica UCF B
     D £K04O_PH                     256                                         Parametr.forma graf.
     D £K04O_FP                      12                                         Forma grafica UCF O
     D £K04O_PP                     256                                         Parametr.forma graf.
     D £K04O_EF                       1                                         Escludi da B£FIND0F
     D £K04O_SP                       1                                         Comando spotlight
     D £K04O_CE                       1                                         Ctr. Errori Campi NV
     D £K04O_AT                       1                                         Applicativo/Tecnico
     D £K04O_LU                       1                                         Livello Utilizzo
     D £K04O_SI                       1                                         Scansione istanze
     D £K04O_FI                       1                                         Filtro Stringa Int.
     D £K04O_LS                      15                                         Schema su Liste
     D £K04O_FS                      15                                         Schema su Filtri
     D £K04O_GN                       1                                         Gestito Numerico
     D £K04O_PS                       1                                         Piano di Sviluppo
     D £K04O_SD                       1                                         Sugg./Domande/Errori
     D £K04O_AB                       1                                         Abstract
     D £K04O_AP                       1                                         Attributi parametro
      *
     D£K04FO           S           3000                                         Funzione
      *
     D£K04CV           DS            50                                         Valori combo
     D £K04CVCD                      15    OVERLAY(£K04CV:1)                     .Codice
     D £K04CVDE                      35    OVERLAY(£K04CV:*NEXT)                 .Descrizione
      *
     D£K04SC           DS                                                       Valori combo
     D £K04SCCD                     256                                          .Codice
     D £K04SCDE                      75                                          .Descrizione
     D £K04SCLV                       1                                          .Livello
     D £K04SCLF                       1                                          .Foglia
      *----------------------------------------------------------------*
      /ENDIF
