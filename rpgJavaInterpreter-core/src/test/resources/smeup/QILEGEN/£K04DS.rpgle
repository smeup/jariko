      *IF NOT DEFINED(K04DS_INCLUDED)
      *DEFINE K04DS_INCLUDED
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
     D £K04PG          S             10                                         Program
     D £K04LC          S             10                                         Exit
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
     D £K04O_TT                       1                                         Presenza tooltip
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
      *ENDIF
