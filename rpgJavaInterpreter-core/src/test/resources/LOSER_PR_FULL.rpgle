     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 23/02/21  002635 BMA Creazione
     V* ==============================================================
     V* NB: tutte le /COPY incluse (DEC, RITES, £TABJATDS...) servono
     V* ==============================================================
********** PREPROCESSOR COPYSTART QILEGEN,£INIZH
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 10/10/10  V3R1    AS DECEDIT(*JOBRUN) e CCSID(*CHAR : *JOBRUN)
     V* 27/10/16  V5R1   BMA OPTION(*SRCSTMT:*NOUNREF) e DEBUG(*INPUT)
     V* ==============================================================
     H*----------------------------------------------------------------
     H* Specifiche da inserire in testa ad ogni programma RPG
     H*----------------------------------------------------------------
     H* DECEDIT(*JOBRUN) DATEDIT(*DMY/) DFTACTGRP(*NO) ACTGRP(*CALLER)
     H* OPTION(*NODEBUGIO:*SRCSTMT:*NOUNREF) CCSID(*CHAR : *JOBRUN) DEBUG(*INPUT)
********** PREPROCESSOR COPYEND QILEGEN,£INIZH
      *--------------------------------------------------------------------------------------------*
********** PREPROCESSOR COPYSTART QILEGEN,£JAX_D
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 13/05/05  V2R1    CM Raggruppamento /COPY di comunicazione
     V* 14/12/06  V2R2    AS Aggiunta £G61DS
     V* 16/04/07  V2R2    AS Aggiunta £TABUI1DS
     V* 14/06/07  V2R2    AS Aggiunta £G96DS
     V* B£30901B  V4R1    CM Aggiunta £JAX_D4 e £TABB£5DS per gestione Setup di Default
     V* 24/06/13  V3R2    BS Forzata inclusione come include
     V* 06/10/16  V5R1    PEDSTE Eliminata /COPY £JAX_F0 per nuova gestione prtf in JAJAX3
     V* 02/02/17         BMA Aggiunta £G64DS
     V* B£70524A  V5R1   BMA Rilascio modifiche precedenti
     V* 25/09/18  V5R1    ZS Aggiunta £B£UT67DS
     V* ==============================================================
      */COPY QILEGEN,£JAX_F0
********** PREPROCESSOR COPYSTART QILEGEN,£JAX_D0
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* 24/10/03          CM Aggiunta variabile £JAXSC
     V* 16/05/06          GR Aggiunta variabile £JAXNS per "servizi" senza scritture nel buffer
     V* 14/12/06  V2R2    AS Aggiunte variabili £JaxWT e £JaxDtRc
     V* 20/04/07  V2R2    AS Aggiunta variabile £Jax_FlushEn
     V* 25/02/08  V2R3    CC Aggiunte specifiche D per £JAX_BSETUP in £JAX_C3
     V* 12/03/08  V2R3    CC Aggiunte specifiche D per gestione campi setup in  £JAX_BSETUP
     V* 20/03/08  V2R3    CC Elim. metodo £JAX_BSETUP. Gestione campo XS di setup fatto da LOOCUP
     V* 20/11/10  V3R1    AS Aggiunta variabile £JAXNI per omettere INPUT dall'XML
     V* 08/05/12  V3R2    AS Aggiunta variabile £JAXNI_POS per contemporanee modifiche a £JAX_C0
     V* 19/06/17  V5R1   BMA Rilasciata modifica precedente
     V* 27/06/17  V5R1   BMA Portata £JAXCL da 35000 a 40000
     V* ==============================================================
      *
      * Generale
     D £JaxSC          S             10    INZ
     D £JaxRC          S             10    INZ
     D £JaxIB          S              1    INZ
      * Numero caratteri Massimi ricevibili da Looc.UP, se dovesse essere variato
      * . bisogna modificare questo valore e ricompilare tutti i pgm che lo utilizzano.
      * . Aggiungere 1 alla lunghezza per omaggiare la posizione iniziale.
      * . Es( Eval Q$STRI=%SUBST(Q$STRI:$I:£JaxMCR-$I)
     D £JaxMCR         S              5  0 INZ(25001)
      *
      * tempo di attesa sulla coda (Waiting Time):
     D £JaxWE          S              5P 0 INZ
      *
      * Dati ricevuti (non PING o messaggi di chiusura)
     D £JaxDtRc        S              1
      *
      * Abilitazione allo svuotamento della coda
     D £Jax_FlushEn    S              1
      *
     D £JaxDSGen       DS
     D £JaxCP                     30000    INZ VARYING
     D £JaxCL                     40000    INZ VARYING
     D £JaxWS                     65000    INZ VARYING
     D £Jax35                         1    INZ
      *
      * Scrittura Coda
     D £JaxNS          S              1    INZ                                  Non scrivere buffer
     D £JaxDSCoda      DS
     D £JaxCT                        10    INZ
     D £JaxBF                     30000    INZ VARYING
     D £JaxSQ                         3  0 INZ
     D £JaxLU                         5P 0 INZ
     D £JaxLB                        10    INZ
     D £JaxWE                         5P 0 INZ
      *
      * Log
     D £JaxDSLog       DS
     D £JaxLTr                       20    INZ
     D £JaxLTrU                      20    INZ
     D £JaxLLb                        5  0 INZ
     D £JaxLLc                        5  0 INZ
     D £JaxLBu                      140    INZ
     D***> £JaxLSi                         Z   INZ
     D***> £JaxLSt                         Z   INZ
     D***> £JaxLEt                         Z   INZ
     D £JaxLSi                       10  0 INZ
     D £JaxLSt                       10  0 INZ
     D £JaxLEt                       10  0 INZ
     D £JaxLAt                       10  0 INZ
     D £JaxLCo                       40    INZ
      * Non riportare campo INPUT in Attributo Funzione dell'Header XML
     D £JAXNI          S              1
      * Campo di appoggio per la gestione del campo INPUT in Attributo Funzione dell'Header XML
     D £JAXNI_POS      S              5  0
********** PREPROCESSOR COPYEND QILEGEN,£JAX_D0
********** PREPROCESSOR COPYSTART QILEGEN,£JAX_D1
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 25/01/05  V2R1    GR Esteso messaggi, variabili
     V* 27/04/06  V2R2    AS Aggiunti campi £JaxLF e £JaxFL a £JaxDSOGG
     V* 13/12/06  V2R2    AS Aggiunte istruzioni per compilazione condizionale
     V* 23/02/07  V2R2    AS Aggiunto campo £JaxMTyp (tipo messaggio)
     V* 30/05/07  V2R2    GR Aggiunti campi per ritorno pop.up a G99
     V* 31/07/07  V2R3    GG Rilasciata modifica del 30/05/07
     V* 11/10/07  V2R3    GR Aggiunto campo £JaxVarTip x gestione tipo variabile
     V* 05/11/08  V2R3    AS Descritto il significato di alcuni campi
     V* 21/01/09  V2R3    AS Aggiunto campo £JaxGP (Grp) a £JaxDSOGG
     V* 22/01/09  V2R3    AS Aggiunti campi £JaxSI (ShowIcon) e £JaxST (ShowText) a £JaxDSOGG
     V* 23/04/09  V2R3    BS Aggiunto £JaxKMO alla DS £JaxDSKey
     V* 24/08/09  V2R3    CM Aumentato il buffer destinato alle variabili e trasformato in Varying
     V* 21/09/09  V2R3    RM Attributo Mode su <Oggetto
     V* 04/05/10  V2R3    BS Aggiunte alcune Descrizioni
     V* 11/06/10  V2R3    BS Aggiunto Campi Formula nella DSCOL
     V* 08/09/10  V2R3    GR Modifica £JaxDsPop per ordinamento esplicito
     V* 15/03/11  V3R1    AS Aggiunto £JaxRowHt per altezza righe
     V* 15/10/11  V3R2    BS Aggiunta Descrizione su Tipo Messaggio
     V* 20/03/12  V3R2    BS Modifica £JaxDsPop tolti 30 caratteri da Fun attribuiti a Nuova Ico
     V* 21/05/12  V3R2   BMA Gestito attributo Grp nella colonna di matrice
     V* 07/04/12  V3R2    BS Aggiunta Variabile su DSOgg JAXEC per attributo "Stato" dell'albero
     V* 19/09/12  V3R2    BS Aggiunto Attributo Style su DSOgg
     V* 04/04/13  V3R2    AS Aggiunte costanti per gravità messaggi
     V* 29/05/13  V3R2    BS Aggiunta £K04DS
     V* 30/05/12  V3R2   BMA Aggiunta schiera £JAXSW2 e DS £JAXDSCO2 per campi aggiuntivi
     V* 29/09/15         BMA Aggiunta schiera £JAXSW3 e DS £JAXDSCO3 per componente grafico
     V* 06/04/16  V4R1   BMA Rilasciate modifiche di settembre 2015
     V* 30/06/16  V4R1   BMA Aggiunti T1/P1/K1 a messaggi
     V* 08/07/16  V4R1   BMA Aggiunti T2/P2/K2 a messaggi
     V* 07/06/17  V5R1   BMA Aggiunte costanti lunghezze, aumentato £JayFLU a 5,0
     V* 17/10/17  V5R1    BP Aggiunte variabile £JaxSB Setup bottone
     V* 17/10/17  V5R1    BS Aggiunte variabili funzione di controllo a £JAXDSCO3
     V* 13/12/17  V5R1    BS Aggiunta variabile funzione verifica di controllo a £JAXDSCO3
     V* 23/01/18  V5R1    BS Aggiunte note di commento su £JaxSb
     V* JA80807A  V5R1   BMA Aggiunta modalità e gravità messaggio e testo completo
     V* 06/03/18  000539  PEDSTE Aggiunte costanti messaggi interni al componente
     V* 07/03/18  000539  BMA Cambiati commenti descrittivi messaggi in sezione
     V* 06/03/19  V5R1   CM Check-out 000539 in SMEUP_TST
     V* 20/02/20  V5R1   PEDSTE Check-out 000539 in SMEDEV
     V* ==============================================================
********** PREPROCESSOR COPYSTART QILEGEN,£K04DS
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
********** PREPROCESSOR COPYEND QILEGEN,£K04DS
     V* ==============================================================
     D £JaxDSOgg       DS
      * Tipo:
     D £JaxT1                        20    INZ
      * Parametro:
     D £JaxP1                        20    INZ
      * Codice:
     D £JaxK1                       256    INZ VARYING
      * Descrizione:
     D £JaxD1                      2560    INZ VARYING
      * Funzione associata:
     D £JaxOP                      2560    INZ VARYING
      * . Modo esecuzione funzione
     D £JaxMD                        10    INZ
      * Work (attributi aggiuntivi e specifici del nodo):
     D £JaxWK                      2560    INZ VARYING
      * End (chiusura del nodo):
     D £JaxEN                      2560    INZ VARYING
      * Grafica:
     D £JaxGR                       256    INZ VARYING
      * icona:
     D £JaxIC                       256    INZ VARYING
     D £JaxLV                         2    INZ
      * Nome del nodo
     D £JaxNM                       256    INZ VARYING
      * . Attributo Fld:
     D £JaxFL                      2560    INZ VARYING
      * . Attributo Leaf:
     D £JaxLF                       256    INZ VARYING
     D £JaxRI                         1    INZ                                  ricarica G99
     D £JaxCO                         1    INZ                                  ricarica G99
      * Attributo Grp (gruppo di bottoni):
     D £JaxGP                         4    INZ
      * Attributo ShowIcon (mostra icona, valido solo per i bottoni)
     D £JaxSI                         3    INZ
      * Attributo ShowText (mostra testo, valido solo per i bottoni)
     D £JaxST                         3    INZ
      * Attributo Stato del Nodo (EXP=Espanso, COL=Collassato)
     D £JaxEC                         3    INZ
      * Attributo Style del Nodo
     D £JaxSY                        10    INZ
      * Attributo Setup Bottoni del Nodo (BtnSet)
     D £JaxSB                       150    INZ
      * I(T;P;K) = Icona
      * H(testo) = Tooltip
      * B(No) = Presenta come Link e non come bottone
      * P(pp) = Posizione, con i seguenti valori: TR, TL, BR, BL
      *         (testata destra, testata sinistra, piede destra, piede sinistra)
      * S(xxxx) = Style (si può indicare il codice di uno stile)
      *
      * DS per ritorno schiera pop.up
     D £JaxDSPop       DS
     D £JaxPOrd                      10    INZ                                  Cod.az.ges.non usato
     D £JaxPCod                       2    INZ                                  Cod.az.ges.non usato
     D £JaxPDes                      50    INZ                                  Descrizione
     D £JaxPFun                     418    INZ                                  Funzione
     D £JaxPIco                      30    INZ                                  Icone
     D £JaxPFog                       1    INZ                                  Foglia
     D £JaxPRic                       1    INZ                                  Ricarica dopo exec
     D £JaxPopN        S              5  0 INZ
      *
      * Elementi di semaforo o gauge
     D £JaxDSEle       DS
      * . Minimo (solo gauge)
     D £JaxMn                       256    INZ VARYING
      * . Massimo (solo gauge)
     D £JaxMx                       256    INZ VARYING
      * . Soglia 1
     D £JaxS1                       256    INZ VARYING
      * . Soglia 2
     D £JaxS2                       256    INZ VARYING
      * . Valore
     D £JaxVA                       256    INZ VARYING
      * . Inversione (solo gauge)
     D £JaxIN                         1    INZ
      * Righe
     D £JaxDSRig       DS
     D £JaxRDt                        8    INZ VARYING
     D £JaxRBf                     2560    INZ VARYING
      * Buttons
     D £JaxDSBut       DS
     D £JaxBSv                        1    INZ
     D £JaxBRn                        1    INZ
     D £JaxBDe                        1    INZ
      * Key
     D £JaxDSKey       DS
     D £JaxKId                       20    INZ VARYING
     D £JaxKT1                       20    INZ VARYING
     D £JaxKP1                       20    INZ VARYING
     D £JaxKK1                      256    INZ VARYING
     D £JaxKD1                      256    INZ VARYING
     D £JaxKOB                       20    INZ VARYING
     D £JaxKMO                        1    INZ VARYING
      *
     D £JaxKIn         S              3  0 INZ
      *
     D £JAXSWK         S            100    DIM(300)
Cod ce*   Descrizione                  Oggetto              HLengFRComandi                  Au
     D £JAXDSCOL       DS
     D  £JAXCCD                      10                                         Codice
     D  £JAXCTX                      29                                         Descrizione
     D  £JAXCOG                      21                                         Tipo/Parametro ogg.
      * Gestisce parentesi quadre su campo della tabella
     D  £JAXCIO                      01                                         B/O/H/G/K
      *                                                                         .K=Key
      *                                                                         .B=Input/Output
      *                                                                         .O=Output
      *                                                                         .H=Hidden
      *                                                                         .G=Cella Grafica
      * Input/output/hidden
     D  £JAXCLU                      04                                         Lunghezza
     D  £JAXCAL                      01                                         Als(da decodificare)
     D                                                                          .F Formula
     D                                                                          .E Esteso
     D                                                                          .H Gant
     D                                                                          .C Relazioni
     D                                                                          .G Grafico
     D  £JAXCDY                      01                                         Forma grafica
      *                                                                         .R=Non ripete il cod
      * Esempi (Per grafico Asse/Serie)
     D  £JAXCFI                      10                                         Spec.Comp.(Da def.)
      * Esempi (R Emette l'icona ecc)
     D  £JAXCTP                      16                                         Spec.Comp.(Da def.)
      * Autorizzazioni
     D  £JAXCLA                      02                                         Spec.Comp.(Da def.)
      * Filler
     D  £JAXCFL                      05                                         Spec.Comp.(Da def.)
      * Campi Aggiuntivi
     D  £JAXCFO                     100                                         Formula
      * Testo esteso, solo se il testo è vuoto
     D  £JAXCTXE                    300                                         Descrizione Estesa
      *
      * Schiera Campi Aggiuntivi
     D                 DS
     D £JAXSW2                      100    DIM(%elem(£JAXSWK))
     D  £JAXSW2Key                   10    OVERLAY(£JAXSW2:01)                  Campo per %lookup
Cod ce*   Descrizione                         -
      * DS Campi Aggiuntivi
     D £JAXDSCO2       DS
     D  £JAX2CD                      10                                         Codice (chiave)
     D  £JAX2ET                      35                                         Desc. Aggiuntiva
      *
      * Schiera Campi Aggiuntivi (gestiti a programma e non da schiera a tempo di compilazione)
     D                 DS
     D £JAXSW3                     1000    DIM(%elem(£JAXSWK))
     D  £JAXSW3Key                   10    OVERLAY(£JAXSW3:01)                  Campo per %lookup
      *
      * DS Campi Aggiuntivi (gestiti a programma e non da schiera a tempo di compilazione)
     D £JAXDSCO3       DS          1000
     D  £JAX3CD                      10                                         Codice (chiave)
      * Componente
     D  £JAX3CO                       3                                         Componente
      * Configurazione componente
     D  £JAX3EC                     246                                         Configurazione Cmp
      * Tipo funzione controllo
     D  £JAX3TK                       3                                         Tfk SCP_LAY
      * Parametri funzione controllo
     D  £JAX3PK                     256                                         Pfk SCP_LAY
      * Funzione verifica controllo server
     D  £JAX3SK                       1                                         Componente
      *
      * Messaggi
     D £JaxMBf         S                   DIM(100) LIKE(£JaxDSMsg)             Schiera buffer msg
     D £JaxMBfI        S              4  0 INZ                                  Contatore messaggi
     D £JaxDSMsg       DS                                                       DS Messaggi
     D £JaxMLiv                      02                                         . livello (00-99)
     D £JaxMTxt                     198                                         . testo
      * . Tipo messaggio (I=INFO, C=CONF, Q=QUEST)
     D £JaxMTyp                      01                                         . tipo messaggio
     D £JaxMT1                       02                                         . tipo oggetto 1
     D £JaxMP1                       10                                         . param. oggetto 1
     D £JaxMK1                       15                                         . codice oggetto 1
     D £JaxMT2                       02                                         . tipo oggetto 2
     D £JaxMP2                       10                                         . param. oggetto 2
     D £JaxMK2                       15                                         . codice oggetto 2
      * . Modalità V2MSMOD
      * .. TN = Notifica Temporanea (a scomparsa)
      * .. PN = Notifica Permanente (non a scomparsa)
      * .. PM = Messaggio Permanente (modale)
      * .. HH = Messaggio nascosto (non viene emesso)
     D £JaxMMod                      02
      * . Gravità  V2MSGRA
      * .. INFO
      * .. WARNING
      * .. ERROR
     D £JaxMGra                      10
      * Testo completo (secondo livello)
     D £JaxMTx2        S          20000    VARYING
      * Variabili
     D £JaxVBf         S           3023    DIM(100) VARYING                     Schiera buffer var
     D £JaxVBfI        S              4  0 INZ                                  Contatore variabili
     D £JaxDSVar       DS                                                       DS Variabili
     D £JaxVarNam                    20    INZ                                  . nome variabile
     D £JaxVarTip                     3    INZ                                  . tipo var - dft:Sch
     D £JaxVarVal                  3000    INZ VARYING                          . valore variabile
      * Esempi (da completare)
      * Attributi di Riga
     D £JaxRowHt       S             10    INZ                                  Altezza di riga
      * Comando grafico £JAXCAL = 'G'
     D £JaxDSG         DS            26
     D  £JaxDSGObb                    1                                         Obblicatorio V2SI/NO
     D  £JaxDSGNCt                    1                                         Tipo control.V2A£FOC
     D  £JaxDSGMul                    1                                         Multiplo     V2SI/NO
      * COSTANTI
     D £Jax_LvlInf     C                   CONST('00')                            Liv. msg. info
     D £Jax_LvlWrn     C                   CONST('40')                            Liv. msg. warning
     D £Jax_LvlErr     C                   CONST('70')                            Liv. msg. errore
     D £JaxMaxLen      C                   CONST('30000')
     D £JaxMaxStr      C                   CONST('XX')
     D £Jax_GraInf     C                   CONST('INFO')                           msg. info
     D £Jax_GraWrn     C                   CONST('WARNING')                        msg. warning
     D £Jax_GraErr     C                   CONST('ERROR')                          msg. errore
     D £Jax_ModHH      C                   CONST('HH')                             Messaggio nascosto
     D £Jax_ModTN      C                   CONST('TN')                             Notifica temporanea
     D £Jax_ModPN      C                   CONST('PN')                             Notifica permanente
     D £Jax_ModPM      C                   CONST('PM')                             Messaggio permanente
      * Messaggi in sezione
      * . Permanente modale in sezione
     D £Jax_ModPT      C                   CONST('PT')
      * . Permanente in sezione (da chiudere)
     D £Jax_ModPS      C                   CONST('PS')
      * . Temporaneo in sezione (a scomparsa)
     D £Jax_ModTS      C                   CONST('TS')
********** PREPROCESSOR COPYEND QILEGEN,£JAX_D1
********** PREPROCESSOR COPYSTART QILEGEN,£JAX_D4
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* B£30901B  V4R1    CM Creato
     V* ==============================================================
     D £JaxCSC         S              1    INZ                                  1=Contesto creato
     D                                                                          2=Contes.disabilitat
     D                                                                          3=Contes.interno
     D £JaxCS          S              1    INZ                                  Costruisci il setup
      *      1 = Si                                                             programm di default
      *        = No
********** PREPROCESSOR COPYEND QILEGEN,£JAX_D4
********** PREPROCESSOR COPYSTART QILEGEN,£TABPGMDS
      *      Modo esecuzione programmi
     D PGM$DS          DS           100
      * N° 03 - Solo simulazione     V2 SI/NO       1
     D  T$PGMA                 1      1
      * N° 04 - Log                                 1
     D  T$PGMB                 2      2
      * N° 05 - Stampa condizioni                   1
     D  T_PGMC                 3      3
      * N° 06 - Attiva performance                  1
     D  T_PGMD                 4      4
      * N° 07 - Condizioni log                     30
     D  T_PGME                 5     34
      * N° 08 - Attivaz. log £GPE                   1
     D  T_PGMF                35     35
********** PREPROCESSOR COPYEND QILEGEN,£TABPGMDS
********** PREPROCESSOR COPYSTART QILEGEN,£TABB£5DS
      *      Personalizzazione B£
     D B£5$DS          DS           100
      * N° 03 - Suff. pgm Exit £GPE  **             1
     D  T$B£5A                 1      1
      * N° 04 - Pgm chiusura sistema OJ            10
     D  T$B£5B                 2     11
      * N° 05 - Libreria pgm chius.  OJ            10
     D  T$B£5C                12     21
      * N° 06 - Azione chiusura      **             1
     D  T$B£5D                22     22
      * N° 07 - Salvataggi via O.S.  V2 SI/NO       1
     D  T$B£5E                23     23
      * N° 08 - Menù ad Albero       V2 SI/NO       1
     D  T$B£5F                24     24
      * N° 09 - Pgm di Supp.Compil   V2 SI/NO       1
     D  T$B£5G                25     25
      * N° 10 - Log Attiv.Utente     V2 SI/NO       1
     D  T$B£5H                26     26
      * N° 11 - Compil pgm in lingua V              1
     D  T$B£5I                27     27
      * N° 12 - Ctr.costanti pgm     V              1
     D  T$B£5J                28     28
      * N° 13 - Agg.Mod.Dinamico pgm V2 SI/NO       1
     D  T$B£5K                29     29
      * N° 14 - Attivaz. log £GPE    V2 SI/NO       1    1
     D  T$B£5M                31     31
      * N° 15 - Suff. oggetto GPE    **             1
     D  T$B£5N                32     32
      * N° 16 - SV-Att Setup default V2 SI/NO       1    1
     D  T$B£5P                34     34
      * N° 17 - Specif.precompilat.                 2
     D  T$B£5O                35     36
      * N° 18 - Attiva funzioni AOP  V2 SI/NO       1
     D  T$B£5Q                37     37
********** PREPROCESSOR COPYEND QILEGEN,£TABB£5DS
********** PREPROCESSOR COPYSTART QILEGEN,£TABUI1DS
      /IF NOT DEFINED(TABUI1DS_INCLUDED)
      /DEFINE TABUI1DS_INCLUDED
      *      Main User Interface
     D UI1$DS          DS           100
      * N° 03 - Ambiente             TA *CNAA       2
     D  T$UI1A                 1      2
      * N° 04 - Coda lavoro          OJ            10
     D  T$UI1B                 3     12
      * N° 05 - Timeout code         **             1
     D  T$UI1C                13     13
      * N° 06 - Chiusura Job Emulat. V2 SI/NO       1    5
     D  T$UI1H                19     19
      * N° 07 - Emulatore esteso  SV V2 SI/NO       1
     D  T$UI1D                20     20
      * N° 08 - Exit JACFG1          **             1
     D  T$UI1E                21     21
      /ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£TABUI1DS
********** PREPROCESSOR COPYSTART QILEGEN,£UIBDS
      /IF NOT DEFINED(UIBDS_INCLUDED)
      /DEFINE UIBDS_INCLUDED
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 14/12/06  V2R2    AS Aggiunte istruzioni per compilazione condizionale
     V* 25/02/08  V2R3    CC Aggiunto campo XS per passaggio setup
     V* 20/03/08  V2R3    CC Elim. campo XS. Gestione di tale campo setup lasciata a LOOCUP
     V* 04/07/08  V2R3    RL Aggiunti i campi £UIBRI e £UIBSG
     V* 02/12/09  V2R3    CM Aggiunto Setup del Setup
     V* 19/11/10  V3R1    AS Aggiunta £UibPR_Long per gestione INPUT
     V* 27/05/16  V4R1   BMA Aggiunta £UIB_C_3F per sostituzione caratteri non validi
     V* 01/03/17         BMA Aggiunto numero di sequenza
     V* B£70524A  V5R1   BMA Aggiunta £UIBDO per output £UIB e £UIBDI per input £UIB
     V* 20/09/17  V5R1   BMA Aggiunto programma chiamante
     V* 04/01/18  V5R1   BMA Aggiunta £UIB_S_3F per sostituzione caratteri non validi
     V* ==============================================================
     D*----------------------------------------------------------------
     D*
     D*----------------------------------------------------------------
     D £UIBDW          DS         31000
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
     D £UIBPR          S           2000    VARYING
     D £UIBSU          S           2000    VARYING
     D £UIBPW          S           2000    VARYING
     D £UIBSW          S           2000    VARYING
     D £UibPR_long     S          32766    VARYING
     D £UIB_C_3F       C                   CONST(x'3F')
      * Carattere sostitutivo per il 3F. Viene valorizzato nela £JAX_INZ tramite richiamo del B£UT67
      * Qui viene inizializzato a 3F per far emergere casi in cui non sia stata fatta la £JAX_INZ
     D £UIB_S_3F       S              1    INZ(x'3F')
     D*----------------------------------------------------------------
     D £UIBDI          DS           512
      * Lunghezza in byte da inviare
     D  £UIBDI_LU                     5  0
      * Programma chiamante
     D  £UIBDI_P1                    10
     D*----------------------------------------------------------------
     D £UIBDO          DS           512
      * Lunghezza in byte restituita dalla coda
     D  £UIBDO_LU                     5  0
      * Lunghezza in byte dell'INPUT restituito nella £UIBD1
     D  £UIBDO_LI                     5  0
     D*----------------------------------------------------------------
      /ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£UIBDS
********** PREPROCESSOR COPYSTART QILEGEN,£UICDS
      /IF NOT DEFINED(UICDS_INCLUDED)
      /DEFINE UICDS_INCLUDED
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 14/12/06  V2R2    AS Aggiunte istruzioni per compilazione condizionale
     V* B£70524A  V5R1   BMA Aggiunta £UICDO per output £UIC e £UICDI per input £UIC
     V* 20/09/17  V5R1   BMA Aggiunto programma chiamante
     V* 10/08/18  V5R1    ZS Aggiunto nuovo campo £UICDO_ID alla DS £UICDO
     V* ==============================================================
     D*----------------------------------------------------------------
     D*
     D*----------------------------------------------------------------
     D £UICDS          DS         30073
     D  £UICIR                        3
     D  £UICT1                        2
     D  £UICP1                       10
     D  £UICK1                       15
     D  £UICK2                       15
     D  £UICLV                       15
     D  £UICSQ                        3
     D  £UICCT                       10
     D  £UICD1                    30000
     D*----------------------------------------------------------------
     D £UICDI          DS           512
      * Lunghezza in byte da inviare
     D  £UICDI_LU                     5  0
      * Programma chiamante
     D  £UICDI_P1                    10
     D*----------------------------------------------------------------
     D £UICDO          DS           512
      * Lunghezza in byte restituita dalla coda
     D  £UICDO_LU                     5  0
      * Lunghezza in byte restituita nella £UICD1
     D  £UICDO_LI                     5  0
      * Identificativo funzione
     D  £UICDO_ID                    15
     D*----------------------------------------------------------------
      /ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£UICDS
********** PREPROCESSOR COPYSTART QILEGEN,£G64DS
      /IF NOT DEFINED(G64DS_INCLUDED)
      /DEFINE G64DS_INCLUDED
     D* ==============================================================
     D* MODIFICHE Ril.  T Au Descrizione
     D* gg/mm/aa  nn.mm i xx Breve descrizione
     D* 22/05/08  V2.R2   RM DS Output per informazioni
     D* 20/04/12  V3.R2  BMA Ampliata £G64CO da 5000 a 31000
     D* 14/05/14  V4.R1  BMA Gestita restituzione lunghezza voce e messaggio su lettura
     D* 06/06/14  V4.R1  BMA Rilasciata modifica del 14/05 in SMEDEV
     V* B£70206A         BMA Revisione entry
     V* B£70524A  V5R1   BMA Rilascio modifiche precedenti
     V* 20/09/17  V5R1   BMA Aggiunto programma chiamante livello 1 e 2
     V* 14/06/18  000021  CM Nuona funzionalità per £G64 (£TDO183831)
     V* 22/06/18  000036  PEDSTE Gestione carattere default in sostituzione 3F
     V* 09/07/18  V5R1    PEDSTE Rilascio 000036 in TST
     V* 25/09/18  V5R1    ZS Rilascio 000036 e 000021 in DEV
     D* ==============================================================
      * Input / Output
     D £G64CO          S          31000                                         Contenuto della coda
      *
      * ds Input
     D £G64DI          DS          1024
    >D  £G64CD                       10                                         Nome coda
    >D  £G64CL                       10                                         Libreria coda
    >D  £G64AT                        5  0                                      Secondi attesa
    >D  £G64OL                        2                                         Operatore Logico
    >D  £G64KD                      256                                         Chiave
     D  £G64LV                        5  0                                      Lun. voce
     D  £G64SL                        5    OVERLAY(£G64LV:1)                    Lun. voce stringa
     D  £G64P1                       10                                         Pgm chiamante 1
     D  £G64P2                       10                                         Pgm chiamante 2
     D  £G64TC                        1                                         Tipo conversione 3F
     D*                                                                            1=Snd 2=Rcv 3=ALL
     D  £G64CS                        1                                         Carattere sostit.3F
     D  £G64DC                        1                                         Sostit.con default
      *
      * ds Proprietà della coda
     D £G64DP          DS          1024
     D  £G64ILB                      10                                         Libreria
     D  £G64ITC                       1                                         Tipo coda
     D  £G64ILK                       5  0                                      Lun. chiave
     D  £G64ILV                       5  0                                      Lun. voce max
     D  £G64IVP                       9  0                                      Voci presenti
     D  £G64IVM                       9  0                                      Numero massimo voci
     D  £G64IIM                       1                                         Inclusione mittente
      *
      * ds OUTPUT
     D £G64DO          DS          1024
     D  £G64OMS                       7                                         Messaggio
     D  £G64OFI                       7                                         File Messaggio
     D  £G64OLV                       5  0                                      Lun. voce
    >D  £G64CK                      256                                         Contenuto chiave
    >D  £G6435                        1N                                        35
    >D  £G6436                        1N                                        36
      /ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£G64DS
********** PREPROCESSOR COPYSTART QILEGEN,£B£UT67DS
     V*=====================================================================
     V* MODIFICHE Ril.  T Au  Descrizione
     V* gg/mm/aa  nn.mm i xx  Breve descrizione
     V*=====================================================================
     V* 13/08/18  000106  BMA Creazione
     V* 16/08/18  V5R1    CM Check-out 000106 Rilasciato in SMEUP_TST
     V* 25/09/18  V5R1    ZS Rilascio 000106
     V*=====================================================================
     D SCHCHK          S              1S   DIM(100)  ccsid(1200)
     D SCHSOS          S              1S   DIM(100)  ccsid(1200)
********** PREPROCESSOR COPYEND QILEGEN,£B£UT67DS
********** PREPROCESSOR COPYEND QILEGEN,£JAX_D
********** PREPROCESSOR COPYSTART QILEGEN,£TABJATDS
      *      Funzioni grafiche
     D JAT$DS          DS           100
      * N° 03 - Costruttore          V3 PJ1         2
     D  T$JATA                 1      2
      * N° 04 - Param. costruttore   **            15
     D  T$JATB                 3     17
      * N° 05 - Oggetto riferimento  OG            12
     D  T$JATC                18     29
      * N° 06 - Modo gestione cache  V2 JATGC       1
     D  T$JATD                30     30
      * N° 07 - Forma grafica        V2 JATFG       1
     D  T$JATE                31     31
      * N° 08 - Par. forma grafica                 15
     D  T$JATF                32     46
      * N° 09 - Scadenza cache       V2 JATSC       1
     D  T$JATG                47     47
********** PREPROCESSOR COPYEND QILEGEN,£TABJATDS
********** PREPROCESSOR COPYSTART QILEGEN,£TABB£1DS
      /IF NOT DEFINED(£TABB£1DS)
      /DEFINE £TABB£1DS
      *      PERSONALIZZAZIONE B£
     D B£1$DS          DS           100
      * N° 03 - Nome azienda                       15
     D  £RASDI                 1     15
      * N° 04 - Clienti              TA *CNAA       2
     D  ££ANCL                16     17
      * N° 05 - Fornitori            TA *CNAA       2
     D  ££ANFO                18     19
      * N° 06 - Conti Co.Ge.         TA *CNAA       2
     D  ££PICO                20     21
      * N° 07 - Articoli             TA *CNAA       2
     D  ££ANAR                22     23
      * N° 08 - St.magaz.            TA *CNAA       2
     D  ££SAMG                24     25
      * N° 09 - Costi std            TA *CNAA       2
     D  ££COST                26     27
      * N° 10 - Ge.vendite           TA *CNAA       2
     D  ££STVE                28     29
      * N° 11 - Ge.acquisti          TA *CNAA       2
     D  ££STAC                30     31
      * N° 12 - St.produz.           TA *CNAA       2
     D  ££STPR                32     33
      * N° 13 - Ge.produz.           TA *CNAA       2
     D  ££GEPR                34     35
      * N° 14 - Ce.costo             TA *CNAA       2
     D  ££CDCO                36     37
      * N° 15 - Vo.spesa             TA *CNAA       2
     D  ££VDSP                38     39
      * N° 16 - Ce.lavoro            TA *CNAA       2
     D  ££CDLA                40     41
      * N° 17 - Ge.personale         TA *CNAA       2
     D  ££GEPE                42     43
      * N° 18 - Macchine             TA *CNAA       2
     D  ££MACC                44     45
      * N° 19 - Ubicazioni           TA *CNAA       2
     D  ££UBIC                46     47
      * N° 20 - Lotti/rintrac.       TA *CNAA       2
     D  ££LOTT                48     49
      * N° 21 - Attrezzature         TA *CNAA       2
     D  ££ATTR                50     51
      * N° 22 - D/Base               TA *CNAA       2
     D  ££DIBA                52     53
      * N° 23 - Cicli lav.           TA *CNAA       2
     D  ££CILA                54     55
      * N° 24 - Commesse             TA *CNAA       2
     D  ££GCOM                56     57
      * N° 25 - Giacenza             TA *CNAA       2
     D  ££ANSA                58     59
      * N° 26 - Colli/confez.        TA *CNAA       2
     D  ££IFLC                60     61
      * N° 27 - Magazzino            TA *CNAA       2
     D  ££IFMG                62     63
      * N° 28 - R.acquisto           TA *CNAA       2
     D  ££IFPC                64     65
      * N° 29 - Matricole            TA *CNAA       2
     D  ££IFMT                66     67
      * N° 30 - Esp.mod.             TA *CNAA       2
     D  ££ESMO                68     69
      * N° 31 - Impianti             TA *CNAA       2
     D  ££IFMM                70     71
      * N° 32 - Cespiti              TA *CNAA       2
     D  ££IFCE                72     73
      * N° 33 - Cambi                TA *CNAA       2
     D  ££CMBV                74     75
      * N° 34 - Par.Pianif.          TA *CNAA       2
     D  ££B£1A                76     77
      * N° 35 - Rich.movim.          TA *CNAA       2
     D  ££B£1B                78     79
      * N° 36 - Viaggi               TA *CNAA       2
     D  ££B£1C                80     81
      * N° 37 - Co.Generale          TA *CNAA       2
     D  ££B£1D                82     83
      * N° 38 - Co.Analitica         TA *CNAA       2
     D  ££B£1E                84     85
      * N° 39 - Non conformità       TA *CNAA       2
     D  ££B£1F                86     87
      * N° 40 - Imp.Risorse          TA *CNAA       2
     D  ££B£1G                88     89
      * N° 41 - Rich.Interv.         TA *CNAA       2
     D  ££B£1H                90     91
      *      PERSONALIZ. (IN CODA A B£1)
     D B£2$DS          DS           100
      * N° 03 - U.Mis.fraz.tempo C/S                1
     D  ££UMHR                 1      1
      * N° 04 - Arrot. ore (minuti)  NR             2
     D  ££PAMI                 2      3  0
      * N° 05 - Magazzino unico      MG             3
     D  ££MAGD                 4      6
      * N° 06 - Att.LOG util.oggetti                2
     D  ££B£2L                 7      8
      * N° 07 - Att.PROTEZ.Liv.campo                2
     D  ££B£2P                 9     10
      * N° 08 - Att.SCENARI multipli                2
     D  ££B£2S                11     12
      * N° 09 - Azienda              AZ             2
     D  ££B£2A                13     14
      * N° 10 - Modo present.istanti V2 TI_I1       1    1
     D  ££B£2B                16     16
      * N° 11 - Escl.contr.comp.st.  V2 SI/NO       1
     D  ££B£2C                17     17
      * N° 12 - Cod.divisa corrente  TA VAL         4
     D  ££B£2D                18     21
      * N° 13 - Cod.divisa Euro      TA VAL         4
     D  ££B£2E                22     25
      * N° 14 - Descr.estesa assunta TA LIN         3
     D  ££B£2F                26     28
      * N° 15 - Magazzino competenza MG             3
     D  ££B£2G                29     31
      * N° 16 - Nazione assunta      TA V§N         6
     D  ££B£2H                32     37
      * N° 17 - Lingua assunta       TA V§L         3
     D  ££B£2I                38     40
      * N° 18 - Formato data         V2 FMDAT       1
     D  ££B£2M                41     41
      * N° 19 - Divisa alternativa   TA VAL         4
     D  ££B£2N                42     45
      * N° 20 - Divisore             V2 SI/NO       1
     D  ££B£2O                46     46
      * N° 21 - Tipo cambio          TA TCA         3
     D  ££B£2Q                47     49
      * N° 22 - Separatore decimale  **             1
     D  ££B£2R                50     50
      * N° 23 - Time Zone As400      NR             4 2
     D  ££B£2T                51     54  2
      * N° 24 - Time Zone Utenti     NR             4 2
     D  ££B£2U                55     58  2
      * N° 25 - Suff.Agg.     £INZDS **             1
     D  ££B£2V                59     59
      * N° 26 - Costruz.automatica   V2 SI/NO       1
     D  ££B£2W                60     60
      * N° 27 - Nuova gest.azioni    **             1
     D  ££B£2X                61     61
      * N° 28 - Gest.Contesto        V2 SI/NO       1
     D  ££B£2Y                62     62
      * N° 29 - Ambiente di Test     V2 SI/NO       1
     D  ££B£2Z                63     63
      * N° 30 - Abilita Monitor API  V2 SI/NO       1
     D  ££B£2J                64     64
      * N° 31 - Suff.Agg.Amb. B£1/2  **             1
     D  ££B£2K                65     65
      * N° 32 - Attiva RCLRSC        V2 SI/NO       1
     D  ££B£20                66     66
      * N° 33 - Attiva mascheramento V2 SI/NO       1
     D  ££B£21                67     67
      * N° 34 - Fuso orario utenti   OJ *TIMZON    10
     D  ££B£22                68     77
      /ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£TABB£1DS
********** PREPROCESSOR COPYSTART QILEGEN,£DECDS
     D* ==============================================================
     D* MODIFICHE Ril.  T Au Descrizione
     D* gg/mm/aa  nn.mm i xx Breve descrizione
     D* ==============================================================
     D* 10/09/13  V4R1   GG  Creazione
     D* 28/08/16  V5R1   BS  Campo decodifica completa e livello
     D* 17/08/17  V5R1  BMA  Campi dati statistici
     D* 27/09/18  000155 BMA Condizionata restituzione campi output estesi
     D* B£80928A  V5R1   BMA Condizionata restituzione campi output estesi
     D* ==============================================================
      *
      * DS di Input
      * -----------
     D£DECDI           DS           512
      * Restituisci decodifica estesa e livello
     D £DECI_DELI                     1
      * Restituisci campi estesi (tutti)
     D £DECI_ROES                     1
      *
      * DS di Output
      * ------------
     D£DECDO           DS          2048
      * Oggetto parametro
     D £DECO_TPAR                    12
      * Codice parametro
     D £DECO_CPAR                    15
      * Decodifica Completa
     D £DECO_DESC                   256
      * Livello
     D £DECO_LIVE                     1
      * Data inserimento
     D £DECO_DTIN                     8
      * Ora inserimento
     D £DECO_ORIN                     6
      * Utente inserimento
     D £DECO_USIN                    10
      * Origine inserimento
     D £DECO_OGIN                    10
      * Data aggiornamento
     D £DECO_DTAG                     8
      * Ora aggiornamento
     D £DECO_ORAG                     6
      * Utente aggiornamento
     D £DECO_USAG                    10
      * Origine aggiornamento
     D £DECO_OGAG                    10
      *
      *----------------------------------------------------------------*
********** PREPROCESSOR COPYEND QILEGEN,£DECDS
********** PREPROCESSOR COPYSTART QILEGEN,£PDS
      /IF NOT DEFINED(£PDS)
      /DEFINE £PDS
     D* ==============================================================
     D* MODIFICHE Ril.  T Au Descrizione
     D* gg/mm/aa  nn.mm i xx Breve descrizione
     D* ==============================================================
     D* B£90630A  02.00   MA Aggiunto campo £PDSDS
     D* B£20806A  05.00   ZZ Aggiunta ds £INZDS inizializzazione pgm
     D* 16/04/03  05.00   ZZ Agg. identificativi job che ha sottomesso
     D* 24/12/04  V2R1    MA il 02.12.03 sono stati inseriti i campi £PDSSI
     D*                      e £PDSNL ed i campi 986/992 non sono liberi
     D*                    ->  Definiti campi £UDU5T e £UDU5A
     D* 10/09/07  V2R3    BS Aggiunte variabili di contesto/ambiente/data riferimento
     D* 17/09/07  V2R3    BS Variabile di attivazione del contesto
     D* 21/03/08  V2R3    BS Aggiunta Variabile VARYING solo per ripresa definizione (Vedi £G25)
     D* 15/09/10  V3R1    MA Aggiunta Variabile £VSELE (numero elementi campi visualizzatori)
     D* 03/12/15  V4R1    AS Eliminata variabile inutilizzta £PDSVY
     D* 23/09/16  V5R1    AS Aggiunta £INZMU (esecuzione in multipiattaforma)
     D* 28/10/16  V5R1   BMA Aggiunto £PDSMI (Message ID)
     V* A£61103A  V5R1    AS Disaccoppiamento utente di sistema da utente applicativo
     V* A£61103A  V5R1   BMA Modificato commento
     V* 21/06/17  V5R1    AS Aggiunto £INZDB (Esecuzione in ambiente con shift-in e shift-out)
     D*----------------------------------------------------------------
     D* OBIETTIVO
     D*  Copy che definisce i campi della DS che contiene
     D*  i dati principali dello stato del programma, la definizione
     D*  della UDS ed i suoi campi standard.
     D*
     D*  Va inserita in tutti i programmi, tra le specifiche I, nella
     D*  zona delle DS, prima dei sottocampi della LDA.
     D*----------------------------------------------------------------
      * Variabili di contesto
     D ££ATCO          S              1                                         Attivazione contesto
     D ££CONT          S             10                                         Contesto
     D ££AMBI          S             10                                         Ambiente
     D ££DATE          S              8  0 INZ                                  Data Riferimento
     D*----------------------------------------------------------------
      * Utente applicativo (valorizzato da B£INZ0/£INZSR)
     D £PDSNU          S             10
     D*----------------------------------------------------------------
      * DS esterna per dialogo pgm
     D £PDSDS          DS          2000
      * Variabile di definizione VARYING
     D**** £PDSVY          S          30000    VARYING
      * Nr elementi nelle schiere campi gestite nei visualizzatori £VS (V5TDOC etc.)
     D £VSELE          C                   CONST(400)
      *----------------------------------------------------------------
      * DS di inizializzazione ambiente programma
     D £INZDS          DS           512    INZ
      * Ds tabella B£1/B£2
     D  £INZ£1                 1    100
     D  £INZ£2               101    200
      * Dati temporali istantanei (considerano time zone da B£2)
      * ..ora HHMMSS (6,0)
     D  £UTIME               201    206  0
      * ..anno AA (2,0)
     D  UYEAR£               207    208  0
      * ..anno AAAA (4,0)
     D  UYEAS£               209    212  0
      * ..mese MM (2,0)
     D  UMONT£               213    214  0
      * ..giorno GG (2,0)
     D  UDAY£                215    216  0
      * ..data AAMMGG (6,0)
     D  £UDAMG               217    222  0
      * ..data AAAAMMGG (8,0)
     D  £UDSMG               223    230  0
      * ..data GGMMAA/MMGGAA (6,0) in formato definito B£2
     D  £UDGMA               231    236  0
      * ..data GGMMAA (6,0) in formato fisso europeo
     D  £UDEUA               237    242  0
      * ..data GGMMAAAA/MMGGAAAA (8,0) in formato definito B£2
     D  £UDGMS               243    250  0
      * ..data GGMMAAAA (8,0) in formato fisso europeo
     D  £UDEUS               251    258  0
      * Job type / subtype
     D  £INZJT               259    259
     D  £INZJS               260    260
      * Nome job che ha effettuato la sottomissione
      * ..job
     D  £INZSJ               261    270
      * ..utente
     D  £INZSU               271    280
      * ..numero
     D  £INZSN               281    286
      * Esecuzione in multipiattaforma
     D  £INZMU               287    287
      * Utente applicativo
     D  £INZUA               288    297
      * Esecuzione in ambiente con shift-in e shift-out (tipicamente DBCS)
     D  £INZDB               298    298
      *----------------------------------------------------------------
     D                SDS
     D*
      *                  Nome del programma
     D  £PDSNP                 1     10
      *                  Specifica ILE in errore
     D  £PDSSI                21     28
      *                  ..nome modulo per oggetti ILE
      *                    (viene invertito in £INZSR)
     D  £PDSNM               334    343
      *                  Libreria del programma
     D  £PDSNL                81     90
      *                  Message ID
     D  £PDSMI                40     46
      *                  Message Text
     D  £PDSTX                91    170
      *                  Status CODE
     D  £PDSSC           *STATUS
      *                  Nome del file per errore
     D  £PDSNF               201    208
      *                  Informazioni file
     D  £PDSIF               209    243
      *                  Nome del lavoro (terminale se interattivo)
     D  £PDSJN               244    253
      *                  Nome dell'utente (di sistema)
     D  £PDSSU               254    263
      *                  Numero del lavoro
     D  £PDSJZ               264    269  0
      *                  Numero parametri passati dall'esterno
     D  £PDSPR           *PARMS
      *----------------------------------------------------------------
      * DS di comodo per definizione stringa qryslt
      *  (impostata da £przq ed utilizzata da opnqryf)
     D £PRZQS          DS           300
      *----------------------------------------------------------------
      * Definizione campi fissi di LDA
      * ------------------------------
     D £UDLDA          DS
      *
      * Sottocampi di £PRZQS
     D  £PRZQ1               597    696
     D  £PRZQ2               697    896
      * Nome membro ambiente di lavoro
     D  £UDMBR               897    906
      * Descrizione ambiente di lavoro
     D  £UDDMB               907    936
      * Nome job lanciante (terminale)
     D  £UDNJL               937    946
      * Nome programma da lanciare
     D  £UDNPG               947    956
      * Tipo stampa del programma:
      *      0     : No stampa
      *      <>0   : Stampa
     D  £UDTPS               957    959  0
      * Esecuzione in interattivo
     D  £UDJON               960    960
      * Nome coda di lavoro
     D  £UDJQN               961    970
      * Priorità in coda lavori
     D  £UDJQP               971    971  0
      * Congelamento in coda lavori
     D  £UDJQH               972    972
      * Data schedulazione
     D  £UDJQD               973    981
      * Ora schedulazione
     D  £UDJQT               982    985
      *  986/987 usato dai flussi per SS B£J        B£GFP15
      *  988/992 usato dai flussi per codice azione
      * Tipo utilizzo B£UT50  ' ' Utente  'G' azione di gruppo
     D  £UDU5T               993    993
      * Codice ambiente B£UT55   Suffisso elemento  B§1
     D  £UDU5A               994    998
      * Coda di stampa
     D  £UDO2N               999   1008
      * Priorità stampa
     D  £UDO2P              1009   1009  0
      * Congelamento stampa
     D  £UDO2H              1010   1010
      * Salvataggio stampa
     D  £UDO2S              1011   1011
      * Numero copie
     D  £UDO2C              1012   1013  0
      * Nome modulo
     D  £UDO2F              1014   1023
      * Stampa immediata
     D  £UDO2I              1024   1024
      /ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£PDS
     D  U$FATT                 1      1
     D  U$NCRE                 2      2
     D  UICODF                13     27
     D  UFCODF                28     42
     D  UINFAT                43     57
     D  UIDFAT                63     70  0
     D  UFDFAT                71     78  0
      * 79/80 libere
     D  U$TCDF                81     83
      * libero                               84  84
     D  U$V51F                85     94
      *  Fattura singola da 100
     D  U$FL19               101    101
     D  U$DFAT               112    119  0
     D  U$CODF               120    134
     D  U$OPZI               135    135
     D  U$ERRO               136    136
     D  U$MESS               140    154
     D  U$NFAT               155    169
     D  UFNFAT               170    184
     D  U$PFAT               195    204
      *  Opzioni estrazione
     D  U$AZ10               205    205
     D  U$AZ11               206    206
     D  U$AZ12               207    207
     D  U$AZ13               208    208
      *--------------------------------------------------------------------------------------------*
      * Schiera contenente una griglia di esempio
     D SWKESE          S                   CTDATA PERRCD(1)                     _TXT_S11,29
     D                                     LIKE(£JAXSWK) DIM(012)
      *--------------------------------------------------------------------------------------------*
      * Impostazioni iniziali
     C                   EXSR      IMP0
      * Scelta di funzione e metodo
1    C                   SELECT
1x   C                   WHEN      %SUBST(£UIBME:1:3)='XXX'
1    C                   SELECT
1x   C                   WHEN      %SUBST(£UIBME:5:3)='YYY'
     C                   EXSR      FXXXYYY
1e   C                   ENDSL
1e   C                   ENDSL
      *                                      *
     C     G9MAIN        TAG
     C                   EXSR      FIN0
      *
     C                   SETON                                        RT
     C*/COPY QILEGEN,£INZSR
      *--------------------------------------------------------------*
    RD*  Impostazioni iniziali
      *--------------------------------------------------------------*
     C     IMP0          BEGSR
     C                   EXSR      £JAX_IMP0
     C                   ENDSR
      *--------------------------------------------------------------*
    RD*  Chiusura e invio su coda del buffer residuo
      *--------------------------------------------------------------*
     C     FIN0          BEGSR
     C                   EXSR      £JAX_FIN0
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Routine impostazioni di avvio programma
      *--------------------------------------------------------------*
     C     £INIZI        BEGSR
      *
     C                   EXSR      £JAX_INZP
     C                   EXSR      £JAX_INZ
      *
     C                   ENDSR
      *---------------------------------------------------------------*
    RD*
      *---------------------------------------------------------------*
     C     FXXXYYY       BEGSR
      *
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Log Specifico applicazione
      *--------------------------------------------------------------*
     C     £JAX_LOG      BEGSR
     C                   ENDSR
********** PREPROCESSOR COPYSTART QILEGEN,£RITES
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* B£00722A  V3R1    CM Rimozione Gestione Interna degli errori
     D* ==============================================================
     D* MODIFICHE Ril.  T Au Descrizione
     D* gg/mm/aa  nn.mm i xx Breve descrizione
     D* ==============================================================
     D* 10/09/07  V2R3    BS Aggiunte variabili di contesto
     D* 25/09/07  V2R3    BS Aggiunto campo per annullamento
     D* 03/10/12  V3R2    SP Aggiunti utente/data/ora inserimento e aggiornamento
     D* 15/11/12  V3R2   BMA Rilasciata da TST
     D*----------------------------------------------------------------
     D* OBIETTIVO
     D*  Eseguire il controllo validità/decodifica dell'elemento di una
     D*  tabella SMEUP, oppure eseguire la ricerca alfabetica, con la
     D*  possibilità di parzializzare.
     D*  Inoltre, impostando il campo £RITRS, si possono eseguire altre
     D*  funzioni quali posizionamento e scansione o aggiornamento.
     D*
     D* PREREQUISITI
     D* Per avere le parzializzazioni su TTLIBE:
     D* D/COPY QILEGEN,£RITESDS
     D*
     D* Input : £RITST : Settore
     D*         £RITEL : Elemento
     D*         £RITMA : Se diverso da ' ', viene permessa solo la
     D*                  ricerca alfabetica (lancia il pgm B£AR10A, che
     D*                  è una copia del pgm B£AR10, per permettere di
     D*                  eseguire una ricerca tabelle durante la manut.
     D*                  di una tabella, evitando la ricorsività)
     D*         £RITRS : Funzione sulle tabelle:
     D*                  Funzioni standard
     D*                            Ricerca / decodifica / lettura
     D*                  C         Lettura senza ricerca  (CHAIN)
     D*                  Posizionam. e scansione per codice
     D*                  P         Posiz. e lettura (SETLL+READE)
     D*                  S         Posizionamento         (SETLL)
     D*                  G         Posizion. su maggiore  (SETGT)
     D*                  L         Lettura successivo     (READE)
     D*                  R         Lettura precedente     (REDPE)
     D*                  Posizionam. e scansione per descrizione
     D*                  p         Posiz. e lettura (SETLL+READE)
     D*                  s         Posizionamento         (SETLL)
     D*                  g         Posizion. su maggiore  (SETGT)
     D*                  l         Lettura successivo     (READE)
     D*                  r         Lettura precedente     (REDPE)
     D*                  Aggiornamento tabella
     D*                  W         Scrittura nuovo record (WRITE)
     D*                  U         Aggiornamento record   (UPDAT)
     D*                  D         Eliminazione record    (DELET)
     D*                  K         Lock su record per agg.(CHAIN)
     D*                  O         Rilascio record in lock(UNLCK)
     D*         £RITPA : Stringa di parzializzazione su TTLIBE
     D*         £RITLC : Livello di chiamata ( evita la ricorsione )
     D* Output: £RITDS : Descrizione elemento
     D*         £RITLI : TTLIBE
     D*         £RITLU : TTUSER
     D*         £RITFL : 20 flag (in un campo unico lungo 20 car)
     D*         *IN35  : Errore
     D*         *IN36  : Richiesta ricerca
     D*
     D* ESEMPIO DI CHIAMATA
     D*C                     MOVEL<settore> £RITST
     D*C                     MOVEL<elemento>£RITEL
     D*C                     MOVEL<Parzial.>£RITPA
     D*C                     EXSR £RITES
     D*C  N35                MOVEL£RITDS    <campo descrizione>
     D*C  N35                MOVEL£RITLI    <DS Format TTLIBE>
     D*C  N35 36             MOVEL£RITEL    <campo elemento>
     D*----------------------------------------------------------------
     C     £RITES        BEGSR
      *
1    C                   IF        ££ATCO<>''
2    C                   IF        £RITAM=*BLANK
     C                   EVAL      £RITAM=££AMBI
2e   C                   ENDIF
2    C                   IF        £RITCO=''
     C                   MOVEL     ££CONT        £RITCO
2e   C                   ENDIF
2    C                   IF        £RITDT=0
     C                   MOVEL     ££DATE        £RITDT
2e   C                   ENDIF
1e   C                   ENDIF
      *
1    C                   IF        £RITAM=*BLANK
     C                   EVAL      £RITAM=£PDSNP
1e   C                   ENDIF
      * Salva campo funzione su tabelle (x scelta se ritornare in36)
     C                   MOVEL     £RITRS        £RITR§            1
      * Imposta nome pgm da lanciare
1    C     £RITMA        IFEQ      ' '
     C     'B£AR10'      CAT(P)    £RITLC        £RITPG           10
1x   C                   ELSE
     C                   MOVEL(P)  'B£AR10A'     £RITPG
1e   C                   END
      * Lancio
     C                   CALL      £RITPG
     C                   PARM                    £RITST            5            Settore
     C                   PARM                    £RITEL           15            Elemento
     C                   PARM                    £RITDS           30            Descrizione
     C                   PARM      0             £RIN35            1 0          Ind. errore
     C                   PARM      0             £RIN36            1 0          Ind. ricerca
     C                   PARM                    £RITMA            1            Flag manutenzione
     C                   PARM                    £RITRS            1            Funzione base
     C                   PARM                    £RITLI          100            Campo dati std
     C                   PARM                    £RITPA          100            DS parzializzazione
     C                   PARM                    £RITLU          200            Campo dati utente
     C                   PARM                    £RITFL           20            Flags
     C                   PARM                    £RITFU           10            Funzione estesa
     C                   PARM                    £RITME           10            Metodo funz.estesa
     C                   PARM                    £RITAM           10            Ambiente (pgm call)
     C                   PARM                    £RITCO           10            Contesto
     C                   PARM                    £RITRI            9 0          Nrr input
     C                   PARM                    £RITRO            9 0          Nrr output
     C                   PARM                    £RITMS            7            Rtn code (messaggio)
     C                   PARM                    £RITFI           10            File messaggi
     C                   PARM                    £RITVA           45            Valore rtn code
     C                   PARM                    £RITCM            2            Comando
     C                   PARM                    £RITDT            8 0          Data Riferimento
     C                   PARM                    £RITTA            1            Annullamento
     C                   PARM                    £RITUI           10            Utente Inserim
     C                   PARM                    £RITDI            8 0          Data   Inserim
     C                   PARM                    £RITOI            6 0          Ora    Inserim
     C                   PARM                    £RITUA           10            Utente Aggiornam
     C                   PARM                    £RITDA            8 0          Data   Aggiornam
     C                   PARM                    £RITOA            6 0          Ora    Aggiornam
      * .. riporta in35
     C                   MOVE      £RIN35        *IN(35)
      * .. riporta in36 solo se non eseguita una funzione su file
1    C     £RITR§        IFEQ      ' '
     C                   MOVE      £RIN36        *IN(36)
1e   C                   ENDIF
      *
      * LOG se attivo da tabella B£2 e/o da programma
1    C  N35££B£2L        IFEQ      '11'
     C                   MOVEL     ££B£2L        ££B£2L            2
2   >C                   IF        ££B£2J = '1'
    >C                   CALL      'B£DMS7'                             37
    >C                   PARM      'COS'         £DMS7F           10
    >C                   PARM                    £DMS7M           10
    >C                   PARM      'TA'          £DMS7T            2
    >C                   PARM                    £RITST
    >C                   PARM                    £RITEL
    >C                   PARM                    £DMS75            1
2x  >C                   ELSE
     C                   EVAL      *IN37=*OFF
     C                   CALL      'B£DMS7'
     C                   PARM      'COS'         £DMS7F           10
     C                   PARM                    £DMS7M           10
     C                   PARM      'TA'          £DMS7T            2
     C                   PARM                    £RITST
     C                   PARM                    £RITEL
     C                   PARM                    £DMS75            1
2e  >C                   ENDIF
     C                   MOVEL     *BLANKS       £DMS7F           10
     C                   MOVEL     *BLANKS       £DMS7M           10
1e   C                   ENDIF
      * Pulisce campo parzializzazioni e livello di chiamata
     C                   MOVE      *BLANK        £RITPA
     C                   MOVE      *BLANK        £RITLC            1
     C                   CLEAR                   £RITCO
     C                   CLEAR                   £RITDT
      *
     C                   ENDSR
     C*-----------------------------------------------------------
********** PREPROCESSOR COPYEND QILEGEN,£RITES
********** PREPROCESSOR COPYSTART QILEGEN,£DEC
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* B£00722A  V3R1    CM Rimozione Gestione Interna degli errori
     D* ==============================================================
     D* MODIFICHE Ril.  T Au Descrizione
     D* gg/mm/aa  nn.mm i xx Breve descrizione
     D* ==============================================================
     D* 10/09/07  V2R3    BS Aggiunte variabili di contesto
     D* 02/10/07  V2R3    AS Eliminata £CRI su B£DEC5
     D* 10/09/13  V4R1    GG Aggiunte DS di input e output
     D*-------------------------------------------------------------------
     D* OBIETTIVO
     D*  Decodifica un codice, dato un tipo codice (tab. *CNTT)
     D*  eseguendo la realtiva ricerca alfabetica e decodifica
     D*                                                                ---
     D* PREREQUISITI
     D* D/COPY QILEGEN,£DECDS
     D* Aggiunta in automatico dal precompilatore
     D*
     D*
     D* FLUSSO
     D*  Input : £DECCD - Codice da decodificare
     D*          £DECTP - Tipo codice (da tabella *CNTT)
     D*          £DECPA - Parametro nel caso serva ('T_' -> settore)
     D*                                            ('OJ' -> objtype)
     D*          £DECR1 - S = Eseguire le decodifiche su *CNTT
     D*                       (default = 'N')                          ---
     D*          N.B. se scelto S il programma controlla la lunghezza
     D*               del campo passato (lunghezza dichiarata nella
     D*               tabella *CNTT)
     D*               Esempio : Vds 123456........  (campo video 15)   ---
     D*                  richiesto  12345678......   errore            ---
     D*                                                                ---
     D*                                                                ---
     D*          £DECR2 - S = Eseguire la ricerca tabelle con la possi-
     D*                       bilità di aggiornamento:
     D*                       se NON specificato 'S' la routine £RITSM
     D*                        effettua la sola ricerca senza modifica,
     D*                       necessario se £DEC è richiamata dal pgm
     D*                       gestione tabelle (vedi nota sotto)
     D*                       (default = 'N')                          ---
     D*                   C = Solo memorizzare per oggetti di 2°livello
     D*                       Es. Ubicazioni di un articolo
     D*                           Articoli di un ente
     D*                       Normalmente i seguenti oggetti lasciano  ---
     D*                       automaticamente traccia per interfaccia a---
     D*                       oggetti multipli:                        ---
     D*                       - Articolo                               ---
     D*                       - Magazzino                              ---
     D*                       - Contatto                               ---
     D*          £DECR3 - Livello di richiamo
     D*                   Suffisso della copia di B£DEC da richiamare
     D*                   al fine di evitare la ricorsione
     D*                                                                ---
     D*          £DECR4 - Se diverso da blanks esegue solo la
     D*                   presentazione delle funzioni                 ---
     D*                                                                ---
     D*          £OAVDI - DS di input
     D*                                                                ---
     D*  Output: £DECCD - Codice da ricerca alfabetica
     D*          £DECDE - Descrizione codice
     D*          £DECIN - Intestazione tipo campo (da tabella *CNTT)
     D*          *IN35  - On=Errore
     D*          *IN36  - On=Eseguita ricerca alfabetica
     D*          £OAVDO - DS di output
     D*
     D* ESEMPIO DI RICHIAMO
     D*C                     MOVEL<codice>  £DECCD
     D*C                     MOVEL<tipo cod>£DECTP
     D*C                     MOVEL<param.>  £DECPA
     D*C                     MOVEL<S/N>     £DECR1
     D*C                     MOVEL<S/N>     £DECR2
     D*C                     MOVEL< /A/B/C> £DECR3
     D*C                     EXSR £DEC
     D*C     N35             MOVEL£DECDE    <campo decodifica>
     D*C     N35 36          MOVEL£DECCD    <campo codice>
     D*C     N35 36          SETON                     10
     D*-------------------------------------------------------------------
     C     £DEC          BEGSR
      *
1    C                   IF        ££ATCO<>''
2    C                   IF        £DECAM=''
     C                   MOVEL     ££AMBI        £DECAM
2e   C                   ENDIF
2    C                   IF        £DECCO=''
     C                   MOVEL     ££CONT        £DECCO
2e   C                   ENDIF
2    C                   IF        £DECDT=0
     C                   MOVEL     ££DATE        £DECDT
2e   C                   ENDIF
1e   C                   ENDIF
      *
1    C                   IF        £DECAM=''
     C                   MOVEL     £PDSNP        £DECAM
1e   C                   ENDIF
      *
1    C     £DECR4        IFEQ      'XX'
     C                   MOVEL     £DECCD        £DECRS            1
2    C     £DECRS        IFEQ      '%'
     C                   MOVE      £DECCD        £DECRD           14
     C                   MOVEL(P)  £DECRD        £DECCD
     C                   MOVEL     'I'           £DECR4
2e   C                   ENDIF
     C     'B£DEC0'      CAT(P)    £DECR3:0      AAA010           10
     C                   MOVEL     *BLANKS       £DECR3            1
     C                   SETOFF                                       3536
     C                   CALL      AAA010
     C                   PARM                    £DECCD           15
     C                   PARM                    £DECTP            2
     C                   PARM                    £DECPA           10
     C                   PARM                    £DECR1            1
     C                   PARM                    £DECR2            1
     C                   PARM                    £DECDE           35
     C                   PARM                    £DECIN           15
     C                   PARM                    £DEC35            1
     C                   PARM                    £DEC36            1
     C                   PARM                    £DECAM           10
     C                   PARM                    £DECCO           10
     C                   PARM                    £DECDT            8 0
     C                   PARM                    £DECDI
     C                   PARM                    £DECDO
     C                   MOVE      £DEC35        *IN35
     C                   MOVE      £DEC36        *IN36
1e   C                   ENDIF
     C*
1    C     £DECRS        IFEQ      '%'
     C                   MOVEL     1             *IN36
     C                   MOVEL     1             £DEC36
1e   C                   ENDIF
     C*
     C* Lancio funzioni su oggetto
1    C     £DECR4        IFNE      'XX'
     C     £DECRS        OREQ      '%'
     C     'XX'          ANDEQ     'XX'
     C*
2   >C                   IF        ££B£2J = '1'
    >C                   CALL      'B£DEC5'                             37
    >C                   PARM                    £DECR4
    >C                   PARM                    £DECCD
    >C                   PARM                    £DECTP
    >C                   PARM                    £DECPA
2x  >C                   ELSE
     C                   EVAL      *IN37=*OFF
     C                   CALL      'B£DEC5'
     C                   PARM                    £DECR4
     C                   PARM                    £DECCD
     C                   PARM                    £DECTP
     C                   PARM                    £DECPA
2e  >C                   ENDIF
1e   C                   ENDIF
     C*
     C                   MOVEL     *BLANKS       £DECR4            1
     C                   MOVEL     *BLANKS       £DECRS            1
     C                   CLEAR                   £DECCO
     C                   CLEAR                   £DECDT
     C*
     C                   ENDSR
     C*-------------------------------------------------------------
********** PREPROCESSOR COPYEND QILEGEN,£DEC
      *
********** PREPROCESSOR COPYSTART QILEGEN,£JAX_C
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* 13/05/05  V2R1    CM Raggruppamento /COPY di comunicazione
     V* 18/12/06  V2R2    AS Aggiunte £G61, £WAIT e £JAX_C3
     V* 14/06/07  V2R2    CM Aggiunte £G96
     V* B£30901B  V4R1    CM Aggiunta £JAX_C4 per gestione Setup di Default
     V* 24/06/13  V3R2    AS £JAX_C1 è diventata nidificata, messo /INCLUDE
     V* 01/03/17         BMA Aggiunte £UIB e £UIC
     V* B£70524A  V5R1   BMA Rilascio modifiche precedenti
     V* ==============================================================
********** PREPROCESSOR COPYSTART QILEGEN,£UIB
      /IF NOT DEFINED(UIB_INCLUDED)
      /DEFINE UIB_INCLUDED
     D* ==============================================================
     D* MODIFICHE Ril.  T Au Descrizione
     D* gg/mm/aa  nn.mm i xx Breve descrizione
     D* ==============================================================
     D* 18/11/14         BMA Creazione
     V* B£70524A  V5R1   BMA Rilascio modifiche precedenti
     D* 05/06/17  V5R1   BMA Non reimposto il 35
     V* 20/09/17  V5R1   BMA Valorizzato programma chiamante
     D* ==============================================================
     D*----------------------------------------------------------------
     D* OBIETTIVO
     D*
     D* FUNZIONI/METODI
     D*
     D* PREREQUISITI
     D*----------------------------------------------------------------
     D*I/COPY QILEGEN,£UIBDS
     D*
     D*
     D*  Input:  £UIBFN: Funzione                 (10)
     D*          £UIBMT: Metodo                   (10)
     D*          £UIBDQ: Coda dati                (10)
     D*          £UIBLB: Libreria coda dati       (10)
     D*          £UIBDS: Funzione (se invio su coda)
     D*          £UIBIB: ' '=Risultato in £UIBDS; '1'=Risultato in £UIBD1 (equivale a £JaxIB)
     D*
     D*  Output
     D*          £UIBER: Indicatore errore
     D*          £UIBLU: Lunghezza restituita £UIBDS (se lettura su coda) in DS £UIBDO
     D*          £UIBLI: Lunghezza restituita £UIBD1 (se lettura su coda) in DS £UIBDO
     D*          £UIBDS: Funzione (se lettura su coda)
     D*
     D*
     D*----------------------------------------------------------------
     C     £UIB          BEGSR
      *
     C                   EVAL      £UIBDI_P1=£PDSNP
      *
    >C                   CALL      'B£UIB0'
    >C                   PARM                    £UIBFN           10            -->
    >C                   PARM                    £UIBMT           10            -->
    >C                   PARM                    £UIBDQ           10            -->
    >C                   PARM                    £UIBLB           10            -->
     C                   PARM                    £UIBIB            1            -->
    >C                   PARM                    £UIBWE            5 0          -->
    >C                   PARM                    £UIBDS                         <->
    >C                   PARM                    £UIBDI                         <--
    >C                   PARM      '0'           £UIBER            1            <--
    >C                   PARM                    £UIBDO                         <--
      *
      * Non reimposto il 35 per non sovrascrivere usi del chiamante
      * Per testare l'errore dovrà essere controllato £UIBER
     C****               EVAL      *IN35=£UIBER
      *
     C                   ENDSR
      *----------------------------------------------------------------
      /ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£UIB
********** PREPROCESSOR COPYSTART QILEGEN,£UIC
      /IF NOT DEFINED(UIC_INCLUDED)
      /DEFINE UIC_INCLUDED
     D* ==============================================================
     D* MODIFICHE Ril.  T Au Descrizione
     D* gg/mm/aa  nn.mm i xx Breve descrizione
     D* ==============================================================
     D* 01/03/17         BMA Creazione
     V* B£70524A  V5R1   BMA Rilascio modifiche precedenti
     D* 05/06/17  V5R1   BMA Non reimposto il 35
     V* 20/09/17  V5R1   BMA Valorizzato programma chiamante
     D* ==============================================================
     D*----------------------------------------------------------------
     D* OBIETTIVO
     D*
     D* FUNZIONI/METODI
     D*
     D* PREREQUISITI
     D*----------------------------------------------------------------
     D*I/COPY QILEGEN,£UICDS
     D*
     D*
     D*  Input:  £UICFN: Funzione                 (10)
     D*          £UICMT: Metodo                   (10)
     D*          £UICDQ: Coda dati                (10)
     D*          £UICLB: Libreria coda dati       (10)
     D*          £UICDS: Funzione (se invio su coda)
     D*          £UICIB: ' '=Risultato in £UICDS; '1'=Risultato in £UICD1 (equivale a £JaxIB)
     D*
     D*  Output
     D*          £UICER: Indicatore errore
     D*          £UICLU: Lunghezza restituita £UICDS (se lettura su coda) in DS £UICDO
     D*          £UICLI: Lunghezza restituita £UICD1 (se lettura su coda) in DS £UICDO
     D*          £UICDS: Funzione (se lettura su coda)
     D*
     D*
     D*----------------------------------------------------------------
     C     £UIC          BEGSR
      *
     C                   EVAL      £UICDI_P1=£PDSNP
      *
    >C                   CALL      'B£UIC0'
    >C                   PARM                    £UICFN           10            -->
    >C                   PARM                    £UICMT           10            -->
    >C                   PARM                    £UICDQ           10            -->
    >C                   PARM                    £UICLB           10            -->
     C                   PARM                    £UICIB            1            -->
    >C                   PARM                    £UICWE            5 0          -->
    >C                   PARM                    £UICDS                         <->
    >C                   PARM                    £UICDI                         -->
    >C                   PARM      '0'           £UICER            1            <--
    >C                   PARM                    £UICDO                         <--
      *
      * Non reimposto il 35 per non sovrascrivere usi del chiamante
      * Per testare l'errore dovrà essere controllato £UICER
     C****               EVAL      *IN35=£UICER
      *
     C                   ENDSR
      *----------------------------------------------------------------
      /ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£UIC
********** PREPROCESSOR COPYSTART QILEGEN,£JAX_C0
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* 24/10/03          CM Sostituita £UIBSC con £JAXSC e pulito buffer
     V*                      in ricezione
     V* 19/05/04  V2R1    AA Sostituito *INU1 con *IN01
     V* 03/06/04  V2R1    BS Sostituito controllo *IN01
     V* 18/12/06  V2R2    AS Aggiunti monitor su QRCVDTAQ, QSNDDTAQ
     V*                      Settata attesa su coda in £JAX_INZ
     V*                      Aggiunte istruzioni per compilazione condizionale
     V* 01/02/07  V2R2    AS Corretto editcode in £JAX_INZ_T
     V* 16/04/07  V2R2    AS Impostato timeout code da tabella UI1 (default 500)
     V* 20/04/07  V2R2    AS Abilitazione di £Jax_FlushEn all'init dei servizi
     V* 09/05/07  V2R2    AS Irrobustimento settaggio timeout code
     V* 30/05/07  V2R2    GR Gestione chiamate cieche pop.up (£JaxNS='2')
     V* 14/06/07  V2R2    CM Aggiunta gestione Momenti con £G96
     V* 12/07/07  V2R2    CM Se assente /COPY £G96 ne inserisco una fantasme
     V* 31/07/07  V2R3    GG Rilasciata modifica del 30/05/07
     V* 27/11/07  V2R3    GR Gestione momenti anche su chiamate cieche
     V* 10/07/09  V2R3    AS Gestione overflow contatore "finestre" XML
     V* 22/07/09  V2R3    BS Spostato settaggio £JAXNS se emulazione o componente EMU da £JAX_IMP0
     V*                      Controllo Routine finale quando £JAXNS<>''
     V* 03/08/09  V2R3    CM Anticipata gestione del log e dei momenti, non veniva inizializzato il
     V*                      momento iniziale causando un'errore durante la stampa del log finale
     V* 17/10/09  V2R3    BS Esternalizzate in routine £JAX_INZC specifiche inizializzazione coda
     V* 19/11/09  V3R1    AS Aggiunta gestione di INPUT nella Funzione di £UIBPR
     V* 20/11/09  V3R1    AS Aggiunto flag per omettere campo INPUT da risposta XML
     V* 02/03/12  V3R2    GR Correzione settaggio JaxNS='2' per G99 per nuovi passaggi parametri
     V* 08/05/12  V3R2    AS Correzione in composizione attributo INPUT molto lungo
     V* 25/07/12  V3R2    CM Aggiunta gestione JaxNS='3' al servizio della /COPY £J03
     V* 12/06/13  V3R2    BS Rettifica Sostituzione Speciali dell'INPUT
     V* A£30417A  V3R2    MF Gestione multilingua - Inserimento Commenti
     V* B£30901B  V4R1    CM Recupero flag di impostazione del Setup di Default
     V* 19/09/13  V4R1   BMA Pulito £JaxBF dopo restituzione in £UIBD1
     V* 07/11/13  V4R1    CM Pulito £JaxCP dopo averla aggiunta al buffer (£JAX_ADD)
     V* 27/05/16  V4R1   BMA Sostituzione 3F per caratteri non validi header funzione
     V* 06/10/16  V5R1    PEDSTE Eliminata open PRT198 e sostituite EXCEPT con chiamate a JAJAX3
     V* 02/02/17         BMA Sostituita QRCVDTAQ £UIBDS con £UIB - Aggiunta /INCLUDE £UIB
     V* 17/02/17  V5R1    BMA Per identificare richiamo proveniente da £G99 passato £G99(1) nel
     V*                       £UIBSS oltre a G99 nei primi 3 caratteri di £UIBD1
     V* 01/03/17         BMA Sostituita QSNDDTAQ con £UIC
     V* B£70524A  V5R1   BMA Rilascio modifiche precedenti
     V* 04/01/18  V5R1   BMA Sostituzione 3F con carattere impostato da B£UT67 nella £JAX_INZ
     V* 05/09/18  V5R1   BMA Aggiunto contesto e pgm chiamante B£UT67
     V* 25/09/18  V5R1    ZS Modificata entry di chiamata al B£UT67
     V* ==============================================================
     D*-------------------------------------------------------------------
     D* OBIETTIVO
     D*
     D* Esternizzare le funzioni di prepazazione stringa XML
     D*
     D* Prerequisiti
     D*
     D* Se impostata la variabile £JAXIB verrà inviato/ricevuto il
     D*    solo buffer senza informazioni di architettura.
     C*----------------------------------------------------------------
    RD* Funzione Buffeizzazione a 29000
     C*----------------------------------------------------------------
     C     £JAX_ADD      BEGSR
      * Verifico se devo scrivere il buffer (no per servizi ciechi!!!)
1    C                   IF        %Len(£JaxCP)+%Len(£JaxBF) > 30000 AND
     C                             £JaxNS=*BLANKS
     C                   EXSR      £JAX_SND
1e   C                   ENDIF
      * Log di controllo
1    C                   IF        T$PGMB='1'
     C*  OF              EXCEPT    £JaxLgTe
     C*                  EVAL      *INOF = *OFF
     C                   Eval      £JaxLTr = 'Aggiungo'
     C                   Eval      £JaxLLb=%Len(£JaxBF)
     C                   Eval      £JaxLLc=%Len(£JaxCP)
     C                   Eval      £JaxLBu=£JaxCP
     C*                  EXCEPT    £JaxLgRi
     C                   CALL      'JAJAX3'
     C                   PARM                    £Jax3Fun         10
     C                   PARM                    £Jax3Met         10
     C                   PARM                    £PDSNP
     C                   PARM      '£JaxLgRi'    £Jax3Exc         10
     C                   PARM                    £JaxDSLog
     C                   PARM                    £Jax3Msg         10
     C                   Eval      £JaxLTrU=''
     C                   Exsr      £JAX_LOG
1e   C                   ENDIF
     C                   Eval      £JaxBF=£JaxBF+£JaxCP
      *
     C                   EVAL      £JaxCP=''
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Funzione Invio
     C*----------------------------------------------------------------
     C     £JAX_SND      BEGSR
     C
1    C                   IF        £JaxSc <> *Blanks and (£JaxCT = 'FINE' or
     C                             £JaxCT <> 'FINE' and £JaxBF <> *Blanks)
      *C                   IF        £JaxSQ=999
      *C                   Eval      £JaxSQ = 1
      *C                   ELSE
      *C                   Eval      £JaxSQ = £JaxSQ + 1
      *C                   ENDIF
     C                   CLEAR                   £UICDS
     C                   EVAL      £UicIR=£UibPG
     C                   EVAL      £UicT1=£UibT1
     C                   EVAL      £UicP1=£UibP1
     C                   EVAL      £UicK1=£UibK1
     C                   EVAL      £UicK2=£UibK2
     C                   EVAL      £UicLV=*BLANKS
      *C                   MOVEL     £JaxSQ        £UicSQ
     C                   EVAL      £UicCT=£JaxCT
     C                   EVAL      £UicD1=£JaxBF
     C                   EVAL      £JaxLU=%Len(£JaxBF)+73
      * Log di controllo
2    C                   IF        T$PGMB='1'
     C*  OF              EXCEPT    £JaxLgTe
     C*                  EVAL      *INOF = *OFF
     C                   EVAL      £UICFN='SEQ'
     C                   EVAL      £UICMT='RET'
     C                   EXSR      £UIC
     C                   EVAL      £JaxSQ=%INT(£UicSQ)
     C                   Eval      £JaxLTr = 'Invio '+%Trim(£JaxCT)+
     C                                       %EditC(£JaxSQ:'Z')
3    C                   IF        £JaxIB = ' '
     C                   Eval      £JaxLLb=£JaxLU
3x   C                   Else
     C                   EVAL      £JaxLLb=%Len(£JaxBF)
3e   C                   ENDIF
     C                   Eval      £JaxLLc=0
3    C                   If        %Len(£JaxBF) >= 140
     C                   Eval      £JaxLBu=%SubST(£UicD1:%Len(£JaxBF)-139)
3x   C                   Else
     C                   Eval      £JaxLBu=£UicD1
3e   C                   ENDIF
     C*                  EXCEPT    £JaxLgRi
     C                   CALL      'JAJAX3'
     C                   PARM                    £Jax3Fun         10
     C                   PARM                    £Jax3Met         10
     C                   PARM                    £PDSNP
     C                   PARM      '£JaxLgRi'    £Jax3Exc         10
     C                   PARM                    £JaxDSLog
     C                   PARM                    £Jax3Msg         10
     C                   Eval      £JaxLTrU=''
     C                   Exsr      £JAX_LOG
2e   C                   EndIF
      *
     C                   MOVEL(P)  'SMEUPUIDQ'   £JaxLB
      *
2    C                   IF        £JaxIB<>' '
     C                   EVAL      £JaxLU=%Len(£JaxBF)
2e   C                   ENDIF
      *
     C                   EVAL      £UICFN='SND'
     C                   EVAL      £UICMT=''
     C                   EVAL      £UICDQ=£JaxSc
     C                   EVAL      £UICLB=£JaxLB
     C                   EVAL      £UICIB=£JaxIB
     C                   EVAL      £UICDI_LU=£JaxLU
     C                   EXSR      £UIC
      *C                   IF        £JaxIB = ' '
      *C                   MONITOR
      *C                   CALL      'QSNDDTAQ'
      *C                   PARM                    £JaxSc
      *C                   PARM      'SMEUPUIDQ'   £JaxLB
      *C                   PARM                    £JaxLU
      *C                   PARM                    £UicDS
      *C                   ON-ERROR  *ALL
      *C                   ENDMON
      *C                   Else
      *C                   EVAL      £JaxLU=%Len(£JaxBF)
      *C                   MONITOR
      *C                   CALL      'QSNDDTAQ'
      *C                   PARM                    £JaxSc
      *C                   PARM      'SMEUPUIDQ'   £JaxLB
      *C                   PARM                    £JaxLU
      *C                   PARM                    £JaxBF
      *C                   ON-ERROR  *ALL
      *C                   ENDMON
      *C                   EndIF
     C
1e   C                   EndIF
     C
     C                   EVAL      £JaxBF = ''
     C
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Funzione Ricezione
     C*----------------------------------------------------------------
     C     £JAX_RCV      BEGSR
     C     G0JAXRCV      TAG
     C
     C                   Eval      £Jax35 = *ON
1    C                   IF        £JaxRC <> *Blanks and £UibCM='CONT'
     C                             or £JaxIB <> ' '
     C                   Eval      £Jax35 = *OFF
2    C                   IF        £JaxIB = ' '
      *C                   MONITOR
      *C                   CALL      'QRCVDTAQ'
      *C                   PARM                    £JaxRC
      *C                   PARM      'SMEUPUIDQ'   £JaxLB
      *C                   PARM      31000         £JaxLU
      *C                   PARM      *BLANKS       £UibDS
      *C                   PARM      £JaxWT        £JaxWE
      *C                   ON-ERROR  *ALL
      *C                   CLEAR                   £UibDS
      *C                   EVAL      £JAXLU=0
      *C                   ENDMON
     C                   MOVEL(P)  'SMEUPUIDQ'   £JaxLB
     C                   EVAL      £JaxWE=£JaxWT
     C                   EVAL      £UIBFN='RCV'
     C                   EVAL      £UIBMT=''
     C                   EVAL      £UIBDQ=£JaxRC
     C                   EVAL      £UIBLB=£JaxLB
     C                   EVAL      £UIBWE=£JaxWE
     C                   CLEAR                   £UIBDS
     C                   CLEAR                   £UIBDI_LU
     C                   EVAL      £UIBIB=' '
     C                   EXSR      £UIB
     C                   EVAL      £JaxLU=£UIBDO_LU
3    C                   IF        £JaxLU>0
     C                   EVAL      £UibDS=%SUBST(£UibDS:1:£JaxLU)
3e   C                   ENDIF
2x   C                   Else
      *C                   MONITOR
      *C                   CALL      'QRCVDTAQ'
      *C                   PARM                    £JaxRC
      *C                   PARM      'SMEUPUIDQ'   £JaxLB
      *C                   PARM      31000         £JaxLU
      *C                   PARM      *BLANKS       £UibD1
      *C                   PARM      £JaxWT        £JaxWE
      *C                   ON-ERROR  *ALL
      *C                   CLEAR                   £UibD1
      *C                   EVAL      £JAXLU=0
      *C                   ENDMON
     C                   EVAL      £JaxWE=£JaxWT
     C                   EVAL      £JaxLB='SMEUPUIDQ'
     C                   EVAL      £UIBFN='RCV'
     C                   EVAL      £UIBMT=''
     C                   EVAL      £UIBDQ=£JaxRC
     C                   EVAL      £UIBLB=£JaxLB
     C                   EVAL      £UIBWE=£JaxWE
     C                   CLEAR                   £UIBD1
     C                   CLEAR                   £UIBDI_LU
     C                   EVAL      £UIBIB='1'
     C                   EXSR      £UIB
     C                   EVAL      £JaxLU=£UIBDO_LI
3    C                   IF        £JaxLU>0
     C                   EVAL      £UibD1=%SUBST(£UibD1:1:£JaxLU)
3e   C                   ENDIF
2e   C                   EndIF
      * Verifico se PING
2    C                   IF        £JaxIB=' ' AND £JAXLU=4 AND
     C                             £UIBDS='PING' OR
     C                             £JaxIB<>' ' AND £JAXLU=4 AND
     C                             £UIBD1='PING'
     C                   GOTO      G0JAXRCV
2e   C                   ENDIF
      * Log di controllo
2    C                   IF        T$PGMB='1'
     C*  OF              EXCEPT    £JaxLgTe
     C*                  EVAL      *INOF = *OFF
     C                   Eval      £JaxLTr = 'Ricevuto '+%Trim(£UibCM)
     C                   Eval      £JaxLLb=£JaxLU
     C                   Eval      £JaxLLc=0
3    C                   If        £JaxLU >= 140
     C                   Eval      £JaxLBu=%SubST(£UibD1:£JaxLU-139)
3x   C                   Else
3e   C                   ENDIF
     C*                  EXCEPT    £JaxLgRi
     C                   CALL      'JAJAX3'
     C                   PARM                    £Jax3Fun         10
     C                   PARM                    £Jax3Met         10
     C                   PARM                    £PDSNP
     C                   PARM      '£JaxLgRi'    £Jax3Exc         10
     C                   PARM                    £JaxDSLog
     C                   PARM                    £Jax3Msg         10
     C                   Eval      £JaxLTrU=''
     C                   Exsr      £JAX_LOG
2e   C                   ENDIF
     C
1e   C                   EndIF
     C
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Impostazioni iniziali
     C*----------------------------------------------------------------
     C     £JAX_IMP      BEGSR
     C
     C                   CLEAR                   £JaxDSGen
     C                   CLEAR                   £JaxDSCoda
     C                   CLEAR                   £JaxNS
      * Se richiesta la stampa del log
1    C                   IF        T$PGMB='1'
     C                   TIME                    £JaxLSt
      *. Inizio
     C                   Eval      £JaxLTr = 'Inizio'
     C*  OF              EXCEPT    £JaxLgTe
     C*                  EVAL      *INOF = *OFF
     C                   Eval      £JaxLBu = £UIBPG+' '+£UIBFU+' '+£UIBME
     C                             + £UIBT1+£UIBP1+£UIBK1
     C                             + £UIBT2+£UIBP2+£UIBK2
     C                             + £UIBT3+£UIBP3+£UIBK3
     C                             + £UIBT4+£UIBP4+£UIBK4
     C                             + £UIBT5+£UIBP5+£UIBK5
     C                             + £UIBT6+£UIBP6+£UIBK6
     C*                  EXCEPT    £JaxLgRi
     C                   CALL      'JAJAX3'
     C                   PARM                    £Jax3Fun         10
     C                   PARM                    £Jax3Met         10
     C                   PARM                    £PDSNP
     C                   PARM      '£JaxLgRi'    £Jax3Exc         10
     C                   PARM                    £JaxDSLog
     C                   PARM                    £Jax3Msg         10
      *. Parametri
2    C                   IF        £UibPA<>' '
     C                   Eval      £JaxLTr = 'Parametro'
     C                   Eval      £JaxLBu = £UibPA
     C*  OF              EXCEPT    £JaxLgTe
     C*                  EVAL      *INOF = *OFF
     C*                  EXCEPT    £JaxLgRi
     C                   CALL      'JAJAX3'
     C                   PARM                    £Jax3Fun         10
     C                   PARM                    £Jax3Met         10
     C                   PARM                    £PDSNP
     C                   PARM      '£JaxLgRi'    £Jax3Exc         10
     C                   PARM                    £JaxDSLog
     C                   PARM                    £Jax3Msg         10
2e   C                   ENDIF
      *. Dati
2    C                   IF        £UibD1<>' '
     C                   Eval      £JaxLTr = 'Dati Coda'
     C                   Eval      £JaxLBu = £UibD1
     C*  OF              EXCEPT    £JaxLgTe
     C*                  EVAL      *INOF = *OFF
     C*                  EXCEPT    £JaxLgRi
     C                   CALL      'JAJAX3'
     C                   PARM                    £Jax3Fun         10
     C                   PARM                    £Jax3Met         10
     C                   PARM                    £PDSNP
     C                   PARM      '£JaxLgRi'    £Jax3Exc         10
     C                   PARM                    £JaxDSLog
     C                   PARM                    £Jax3Msg         10
2e   C                   ENDIF
1e   C                   ENDIF
      * Gestione dei momenti
      *
     C****               IF        £UIBD1='G99'
     C****               EVAL      £JaxNS='2'
     C****               CLEAR                   £JaxDSPop
     C****               CLEAR                   £JaxPopN
     C****               GOTO      G9JAXIMP
     C****               ENDIF
      *
1    C                   SELECT
1x   C                   WHEN      £UIBPG='J03'
     C                   EVAL      £JaxNS='3'
     C                   CLEAR                   £JaxDSPop
     C                   CLEAR                   £JaxPopN
     C                   LEAVESR
1x   C                   WHEN      £INZJT='I' OR £INZJT='L' OR
     C                             £UIBPG='EMU'
     C                   EVAL      £JaxNS='1'
     C                   CLEAR                   £JaxDSPop
     C                   CLEAR                   £JaxPopN
     C                   LEAVESR
1e   C                   ENDSL
      *
     C                   Eval      £JaxCT = 'CONTINUA'
      *
1    C**                 IF        T$PGMB='1'
     C**                 TIME                    £JaxLSt
      *
     C**                 Eval      £JaxLTr = 'Inizio'
     C** OF              EXCEPT    £JaxLgTe
     C**                 EVAL      *INOF = *OFF
     C**                 Eval      £JaxLBu = £UIBPG+' '+£UIBFU+' '+£UIBME
     C**                           + £UIBT1+£UIBP1+£UIBK1
     C**                           + £UIBT2+£UIBP2+£UIBK2
     C**                           + £UIBT3+£UIBP3+£UIBK3
     C**                           + £UIBT4+£UIBP4+£UIBK4
     C**                           + £UIBT5+£UIBP5+£UIBK5
     C**                           + £UIBT6+£UIBP6+£UIBK6
     C**                 EXCEPT    £JaxLgRi
      *
     C**                 IF        £UibPA<>' '
     C**                 Eval      £JaxLTr = 'Parametro'
     C**                 Eval      £JaxLBu = £UibPA
     C** OF              EXCEPT    £JaxLgTe
     C**                 EVAL      *INOF = *OFF
     C**                 EXCEPT    £JaxLgRi
1e   C**                 ENDIF
      *
     C**                 IF        £UibD1<>' '
     C**                 Eval      £JaxLTr = 'Dati Coda'
     C**                 Eval      £JaxLBu = £UibD1
     C** OF              EXCEPT    £JaxLgTe
     C**                 EVAL      *INOF = *OFF
     C**                 EXCEPT    £JaxLgRi
1e   C**                 ENDIF
1e   C**                 ENDIF
      * Buffer Previous
      * . Se attivo Flag £JAXNI (no input), non trascrivo l'INPUT nell'XML di risposta
1    C                   IF        £JaxNI='1'
      *C                   EVAL      £UibPR=P_RxLATE(£UibPR:'INPUT(_&_INPUT_&_)'  COSTANTE
      *C                                    :' ')
      *C                   EVAL      £UibPR=%XLATE(£UIB_C_3F:'?':£UibPR)
     C                   EVAL      £UibPR=%XLATE(£UIB_C_3F:£UIB_S_3F:£UibPR)
1e   C                   ENDIF

     C**********         EVAL      £UibPR_Long=%TRIMR(P_RxSOS(£UIBD1))
     C                   EVAL      £UibPR_Long=%TRIMR(£UIBD1)
      * Se il £UIBD1 è maggiore di 30000, devo troncarlo perché la RxLATE gestisce
      * un input da 30000 e potrebbe troncarlo in un posto che rende l'XML finale
      * invalido
1    C                   IF        %LEN(£UibPR_Long)>30000
      * La troncatura la devo fare all'ultimo blank (o ;)all'interno dei 30000, per evitare che
      * risultino cose del tipo &quo che non sono valide per l'XML
      * in realtà la faccio all'interno dei 29987 (30000-13) perché in coda aggiungo
      * *NotComplete*
     C                   EVAL      £UibPR_Long=%SUBST(£UibPR_Long:1:29987)
2    C                   FOR       £JAXNI_POS=29987 DOWNTO 1
3    C                   IF        %SUBST(£UibPR_Long:£JAXNI_POS:1)=*BLANKS
     C                             OR %SUBST(£UibPR_Long:£JAXNI_POS:1)=';'
     C                   LEAVE
3e   C                   ENDIF
2e   C                   ENDFOR
     C                   EVAL      £UibPR_Long=%SUBST(£UibPR_Long:1:£JAXNI_POS) COSTANTE
     C                                         +'*NotComplete*'                 COSTANTE
1e   C                   ENDIF
      *C                   EVAL      £UibPR_Long=P_RxLATE(£UibPR:'_&_INPUT_&_':   COSTANTE
      *C                                                  £UibPR_Long)
      *C                   EVAL      £UibPR_Long=%XLATE(£UIB_C_3F:'?':£UibPR_Long)
     C                   EVAL      £UibPR_Long=%XLATE(£UIB_C_3F
     C                             :£UIB_S_3F:£UibPR_Long)
1    C                   IF        £UibPR_Long <> ''
      * Anche la RxLATE potrebbe far aumentare la dimensione oltre i 30000, in caso
      * spezzo l'invio
2    C                   IF        %LEN(£uibPR_Long)>30000
     C                   Eval      £JaxCP = %TRIM(%SUBST(£UibPR_Long:1:30000))
     C                   EXSR      £JAX_ADD
     C                   Eval      £JaxCP = %SUBST(£UibPR_Long:30001)
     C                   EXSR      £JAX_ADD
2x   C                   ELSE
     C                   Eval      £JaxCP = £UibPR_Long
     C                   EXSR      £JAX_ADD
2e   C                   ENDIF
1e   C                   ENDIF
      *
     C**   G9JAXIMP      TAG
      * Gestione dei momenti
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Start program
     C*----------------------------------------------------------------
     C     £JAX_INZ      BEGSR
      *
     C                   EXSR      £JAX_INZC
     C                   CLEAR                   £JaxDSLog
      *
     C                   CALL      'B£UT67'
     C                   PARM                    £UIB_S_3F
     C                   PARM      '£JAX'        £JAXUT67CO       10
     C                   PARM      £PDSNP        £JAXUT67PG       10
     C                   PARM                    SCHCHK
     C                   PARM                    SCHSOS
      *
     C                   Eval      £RITST = 'PGM'
     C                   Eval      £RITEL = £PDSNP
     C                   Exsr      £Rites
     C  N35              Eval      PGM$DS = £RitLi
     C   35              Eval      PGM$DS = ''
      * Tipo gestione del setup
     C                   EVAL      £RITST='B£5'
     C                   EVAL      £RITEL='*'
     C                   EXSR      £RITES
     C  N35              EVAL      B£5$DS=£RITLI
     C   35              CLEAR                   B£5$DS
     C                   EVAL      £JAXCS=T$B£5P
     C*****T$PGMB*****   COMP      '1'                                    01
     C
1    C*                  IF        T$PGMB='1'
     C*                  OPEN      PRT198
     C*                  EVAL      *INOF = *ON
     C*                  MOVEL     *ALL'-'       £S1198          198
1e   C*                  ENDIF
      * Imposto tempo di attesa
     C                   Eval      £RITST = 'UI1'
     C                   Eval      £RITEL = '*'
     C                   Exsr      £Rites
     C  N35              Eval      UI1$DS = £RitLi
     C   35              CLEAR                   UI1$DS
      *
1    C                   SELECT
1x   C                   WHEN      T$UI1C=*BLANKS
     C                   EVAL      £JAXWT=500
1x   C                   WHEN      T$UI1C='A'
     C                   EVAL      £JAXWT=120
1x   C                   WHEN      T$UI1C='B'
     C                   EVAL      £JAXWT=2000
1x   C                   WHEN      T$UI1C='C'
     C                   EVAL      £JAXWT=30000
1x   C                   WHEN      T$UI1C='D'
     C                   EVAL      £JAXWT=-1
1x   C                   OTHER
     C                   EVAL      £JAXWT=500
1e   C                   ENDSL
      * Abilito il flushing della coda
     C                   EVAL      £Jax_FlushEn='1'
     C
     C                   ENDSR
      *----------------------------------------------------------------
    RD* Inizializzazione Variabili della coda
      *----------------------------------------------------------------
     C     £JAX_INZC     BEGSR
      *
1    C                   If        £UibSC <> *Blanks
     C                   Eval      £JaxSc = £UibSC
     C                   Eval      £JaxRc = £UibSC
     C                   MOVEL     'ECTS'        £JaxRc
1e   C                   EndIF
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Start program per transazioni
     C*----------------------------------------------------------------
     C     £JAX_INZ_T    BEGSR
      *
      * Eseguo start comune
     C                   EXSR      £JAX_INZ
      * Imposto opportunamente le code
     C                   EVAL      £JaxRC='TCTS'+%TRIM(%EDITC(£PDSJZ:'X'))
     C                   EVAL      £JaxSC='TSTC'+%TRIM(%EDITC(£PDSJZ:'X'))
      * imposto tempo di attesa infinito (l'eventuale scatto del timeout è gestito dal JAJAS1
      * che all'occorrenza lo inoltra)
     C                   EVAL      £JaxWT=-1
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Fine programma
     C*----------------------------------------------------------------
     C     £JAX_FIN      BEGSR
      *
     C****               IF        £JaxNS='2'
     C****               EVAL      £UIBD1=£JaxBF
     C****               GOTO      G9JAXFIN
     C****               ENDIF
      *
1    C                   SELECT
1x   C                   WHEN      £JaxNS='1'
     C                   GOTO      G9JAXFIN
1x   C                   WHEN      £JaxNS='2'
     C                   EVAL      £UIBD1=£JaxBF
     C                   CLEAR                   £JAXBF
     C                   GOTO      G9JAXFIN
1x   C                   WHEN      £JaxNS='3'
     C                   EVAL      £UIBD1=£JaxBF
     C                   CLEAR                   £JAXBF
     C                   GOTO      G9JAXFIN
1e   C                   ENDSL
      *
1    C                   If        £UibSU <> ''
     C                   Eval      £JaxCP = £UibSU
     C                   EXSR      £JAX_ADD
1e   C                   EndIF
     C
     C                   Eval      £JaxCT = 'FINE'
     C                   EXSR      £JAX_SND
      *
     C     G9JAXFIN      TAG
1    C                   IF        T$PGMB='1'
     C                   TIME                    £JaxLEt
     C     £JaxLEt       SUBDUR    £JaxLSt       £JaxLAt:*MS
     C*  OF              EXCEPT    £JaxLgTe
     C*                  EVAL      *INOF = *OFF
     C*                  EXCEPT    £JaxLgTm
     C                   CALL      'JAJAX3'
     C                   PARM                    £Jax3Fun         10
     C                   PARM                    £Jax3Met         10
     C                   PARM                    £PDSNP
     C                   PARM      '£JaxLgTm'    £Jax3Exc         10
     C                   PARM                    £JaxDSLog
     C                   PARM                    £Jax3Msg         10
1e   C                   ENDIF
      * Gestione dei momenti
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Emetti Log del Tempo
     C*----------------------------------------------------------------
     C     £JAX_TIME     BEGSR
      *
1    C                   IF        T$PGMB='1'
     C                   TIME                    £JaxLEt
     C     £JaxLEt       SUBDUR    £JaxLSi       £JaxLAt:*MS
     C*  OF              EXCEPT    £JaxLgTe
     C*                  EVAL      *INOF = *OFF
     C*                  EXCEPT    £JaxLgTi
     C                   CALL      'JAJAX3'
     C                   PARM                    £Jax3Fun         10
     C                   PARM                    £Jax3Met         10
     C                   PARM                    £PDSNP
     C                   PARM      '£JaxLgTi'    £Jax3Exc         10
     C                   PARM                    £JaxDSLog
     C                   PARM                    £Jax3Msg         10
1e   C                   ENDIF
      *
     C                   ENDSR
********** PREPROCESSOR COPYEND QILEGEN,£JAX_C0
********** PREPROCESSOR COPYSTART QILEGEN,£JAX_C1
     V*================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* A£30417A  V3R2    MF Gestione multilingua - Inserimento Commenti
     V*================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 03/06/04  V2R1    BS Controllo caratteri speciali £JAXCP in agg.riga
     V* 19/03/04  V2R1    CM Eliminata sostituzione caratteri speciali in CDATA
     V* 11/10/04  V2R1    GR Decodifica automatica in £JAX_ADDO
     V* 28/10/04  V2R1    CM Aggiunto TRIMR su Oggetti.
     V* 18/11/04  V2R1    GR Aggiunge Exec se non c'è in £JaxOP
     V* 02/12/04  V2R1    GR £JAX_ADDE
     V* 10/12/04  V2R1    GR Chiusura automatica oggetti se non specificata
     V* 27/01/05  V2R1    CM Aggiunta chiusura oggetto
     V* 30/09/05  V2R1    GR Pulizia £JAXCP dopo ARIG_I/F AGRI_I/F
     V* 09/12/05  V2R1    BS Gestione Tabelle Correlate
     V* 25/01/05  V2R1    GR Estensione messaggi, creazione variabili
     V* 05/04/06  V2R2    GR Chiusura opzionale nodo messaggio
     V* 27/04/06  V2R2    AS Aggiunta gesiotne campi £JaxFL e £JaxLF
     V* 24/11/06  V2R2    RM Errore in costruzione griglia per numeor colonne = 0
     V* 13/12/06  V2R2    AS Aggiunte istruzioni per compilazione condizionale
     V* 23/02/07  V2R2    AS Aggiunta possibilità di specificare il tipo messaggio CONF/INFO
     V* 30/05/07  V2R2    GR Gestione chiamate cieche pop.up (£JaxNS='2')
     V* 31/07/07  V2R3    GG Rilasciata modifica del 30/05/07
     V* 11/10/07  V2R3    GR Gestione tipo variabili in £JAX_AVAR
     V* 14/11/07  V2R3    GR Riempie con il codice azione £JaxDsPop
     V* 19/01/09  V2R3    AS Corretto limite ciclo in £JAX_BVAR
     V* 20/01/09  V2R3    LS Gestione formule
     V* 21/01/09  V2R2    AS Aggiunta gestione campo £JaxGP in £JAX_ADDO
     V* 06/03/09  V2R3    AS Irrobustita £JAX_ADDO
     V* 02/04/09  V2R3    BMA Modiifica per sfondamento indici schiera in £JAX_AGRI
     V* 07/04/09  V2R3    PC Altezza cella Gantt
     V* 23/04/09  V2R3    BS Aggiunto attributo £JAXKMO nella routine £JAX_AKEY
     V* 21/09/09  V2R3    RM Attributo Mode su <Oggetto
     V* 31/03/10  V2R3    BMA Sostituito %TRIM con %TRIMR per £JAXCFI
     V* 11/06/10  V2R3    BS Gestione Campo Formula
     V* 16/11/10  V3R1    GR Valore Ssc per variabili di sottoscheda
     V* 16/02/11  V3R1    CM Aggiunto tipo messaggio Q (QUEST), in questi casi non verrà
     V*                      ritornato errore se lo stato e minore o uguale a 30
     V*                      Nella gestione del messaggio QUEST, è stata aggiunta la possibilità
     V*                      di passare una funzione ed il proprio testo, questa possibilità non
     V*                      è gestibile nei messaggi gestiti nel richiamo finale
     V* 15/03/11  V3R1    AS Gestione attributo £JaxRowHt (altezza di riga)
     V* 22/03/11  V3R1    AS Corretta gestione attributo £JaxRowHt (altezza di riga)
     V* 15/03/12  V3R2    BS In Modalità Costruzione Albero PopUP Aggiunta Forzatura su Ordinamento
     V* 15/03/12  V3R2    BS Se Settaggio JaxNS='2' (per G99) non esegue chiusura dei nodi
     V* 20/03/12  V3R2    BS Introdotta Variabile £JaxpICo
     V* 21/05/12  V3R2   BMA Gestito attributo Grp nella colonna di matrice
     V* 04/07/12  V3R2    BS Implementato Attributo Stato sull'Albero (Forza Espanso/Collassato)
     V* 19/09/12  V3R2    BS Aggiunto Attributo Style su DSOgg
     V* 10/05/13  V3R2    BS Aggiunto Richiamo K04
     V* A£30417A  V3R2    MF Gestione multilingua - Inserimento Commenti
     V* 29/05/13  V3R2    BS Rilascio modifica del 10/05/13
     V* A£30417A  V3R2    MF Gestione multilingua - Inserimento Commenti
     V* 30/05/12  V3R2   BMA Gestite schiera £JAXSW2 e DS £JAXDSCO2 per desc. aggiuntiva (tooltip)
     V* 05/06/13  V3R2    BS Asteriscate clear schiere messaggi/variabili
     V* 21/06/13  V3R2    BS Sostituzione Caratteri speciali per £JaxOP e £JaxFL
     V* 24/06/13  V3R2    BS Rettifica alla modifica precedente
     V* 18/07/14  V4R1    BS Aggiunto messaggio di raggiungimento lunghezza massima popup
     V* 09/12/14  V4R1    CM Durante la costruzione della colonna se il tetso è vuoto assume il
     V*                      testo esteso JAXCTXE
     V* 29/09/15         BMA Gestite schiera £JAXSW3 e DS £JAXDSCO3 per componente grafico
     V* 02/10/15         BMA Evito di aggiungere proprietà vuote alla griglia
     V* 04/02/16  V4R1   BMA In £JAX_BMES corretta gestione overflow schiera £JaxMBF
     V* 06/04/16  V4R1   BMA Rilasciate modifiche di settembre e ottobre 2015
     V* 30/06/16  V4R1   BMA Aggiunti T1/P1/K1 a messaggi
     V* 08/07/16  V4R1   BMA Aggiunti T2/P2/K2 a messaggi
     V* 14/09/16  V5R1   BMA Gestiti 3 tasti funzionali/funzioni in £JAX_AMES
     V* 07/06/17  V5R1   BMA Gestita lunghezza massima
     V* 16/06/17         BMA Gestita paginazione £JAX_ARIG, aggiunta £JAX_ARIG_L (che usa £JAXCL
     V*                      invece di £JAXCP) per aggiungere righe lunghe fino a 35000
     V* 19/06/17  V5R1   BMA Rilasciata modifica precedente
     V* 27/06/17  V5R1   BMA Apertura aggiunta riga lunga con £JAX_ARIG_LI
     V*                      e chiusura con £JAX_ARIG_LF. Portato £JAXCL a 40000
     V* B£70329A  V5R1    CM Sostituito OJ*USRPRF con UT
     V* 13/10/17  V5R1    BS Risalita a forma grafica oggetto
     V* 17/10/17  V5R1    BP Aggiunto gestione Setup Bottoni
     V* 17/10/17  V5R1    BS Aggiunte variabili funzione di controllo a £JAXDSCO3
     V* 06/12/17  V5R1   BMA Aumentato £JaxOP a 25000 e £JAXOP_LEN a 5 0
     V* 17/10/17  V5R1    BS Aggiunte variabili funzione di controllo a £JAXDSCO3
     V* 13/12/17  V5R1    BS Aggiunta variabile funzione verifica di controllo a £JAXDSCO3
     V* 12/04/18  V5R1   BMA Confronto con *HI con variabile trimmata
     V* JA80807A  V5R1   BMA Revisione gestione messaggi
     V* 09/08/18  V5R1   BMA Forzature temporanee
     V* 04/09/18  V5R1   BMA Forzatura Mode PM per tipo QUEST e CONF
     V* 17/09/18  V5R1   BMA Tolte forzature temporanee
     V* 03/10/18  V5R1   BMA Correzione su esito errore
     V* 06/11/18  000200 PEDSTE Forzatura Mode PM per tipo QUEST e CONF se non è PT
     V* 08/11/18  000200  BS Check-out 000200
     V* 16/12/19  000200 PEDSTE Check-out 000200 in SMEDEV
     V* ==============================================================
     D*-------------------------------------------------------------------
     D* OBIETTIVO
     D*
     D* Esternizzare le funzioni di prepazazione stringa XML orientato
     D* all'oggetto.
     C*----------------------------------------------------------------
********** PREPROCESSOR COPYSTART QILEGEN,£K04
      /IF NOT DEFINED(K04_INCLUDED)
      /DEFINE K04_INCLUDED
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 06/04/13  V3R2    BS Creazione
     V* 16/04/13  V3R2    BS Aggiunto Campo £K04FO
     V* 06/09/15  V4R1    BS Deviazione B£K04S per funzioni SCA
     V* 22/08/17  V5R1   BMA Aggiunto livello chiamata
     D* ==============================================================
     D*----------------------------------------------------------------
     D* OBIETTIVO
     D*
     D* PREREQUISITI
     D*   D/COPY QILEGEN,£K04DS
     D*
     D*----------------------------------------------------------------
     C     £K04          BEGSR
      *
1    C                   IF        £K04PG=' '
2    C                   IF        %SUBST(£K04FU:1:3)='SCA'
     C                   EVAL      £K04PG='B£K04S'+£K04LC
2x   C                   ELSE
     C                   EVAL      £K04PG='B£K04G'+£K04LC
2e   C                   ENDIF
1e   C                   ENDIF
      *
1   >C                   IF        ££B£2J = '1'
    >C                   CALL      £K04PG                               37
    >C                   PARM                    £K04FU
    >C                   PARM                    £K04ME
    >C                   PARM                    £K04MS
    >C                   PARM                    £K04FI
    >C                   PARM                    £K04VA
    >C                   PARM                    £K04CM
    >C     *IN35         PARM      *OFF          £K0435
    >C     *IN36         PARM      *OFF          £K0436
    >C                   PARM                    £K04DI
    >C                   PARM                    £K04DO
    >C                   PARM                    £K04FO
  M > *
1x  >C                   ELSE
     C                   EVAL      *IN37=*OFF
     C                   CALL      £K04PG
     C                   PARM                    £K04FU
     C                   PARM                    £K04ME
     C                   PARM                    £K04MS
     C                   PARM                    £K04FI
     C                   PARM                    £K04VA
     C                   PARM                    £K04CM
    >C     *IN35         PARM      *OFF          £K0435
    >C     *IN36         PARM      *OFF          £K0436
     C                   PARM                    £K04DI
     C                   PARM                    £K04DO
    >C                   PARM                    £K04FO
      *
1e  >C                   ENDIF
      *
1   >C   37              DO        37
    >C                   CALL      'B£GGP0  '
    >C                   PARM      £K04PG        £GGPNP           10
    >C                   PARM      *BLANKS       £GGPTP           10
    >C                   PARM      *BLANKS       £GGPPA          100
1e  >C                   ENDDO
      *
     C                   CLEAR                   £K04PG           10
     C                   CLEAR                   £K04LC            1
      *
     C                   ENDSR
      /ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£K04
     C*----------------------------------------------------------------
    RD* Aggiungo al buffer l'oggetto
     C*----------------------------------------------------------------
     C     £JAX_ADDO     BEGSR
      *
      * Ritorno a G99
1    C                   IF        £JaxNS='2'
     C                   ADD       1             £JaxPopN
     C                   CLEAR                   £JaxDSPop
     C                   EVAL      £JaxPOrd=£JaxK1
     C                   EVAL      £JaxPCod=£JaxK1
     C                   EVAL      £JaxPDes=£JaxD1
     C                   EVAL      £JaxPFun=£JaxOP
2    C                   SELECT
2x   C                   WHEN      £JAXEC='EXP'
     C                   EVAL      £JaxPFog='E'
2x   C                   WHEN      £JAXEC='COL'
     C                   EVAL      £JaxPFog='C'
2x   C                   WHEN      £JaxLF='Yes'                                 COSTANTE
     C                   EVAL      £JaxPFog='1'
2e   C                   ENDSL
     C                   EVAL      £JaxPIco=£JaxIc
     C                   EVAL      £JaxPRic=£JaxRI
     C                   EVAL      £JaxBF=£JaxBF+£JaxDSPop
     C                   EXSR      £JAX_IMPO
2    C                   IF        £JaxPopN=58
     C                   EVAL      £UIBCM='POPNXT'
2e   C                   ENDIF
     C                   GOTO      G9JAXADDO
1e   C                   ENDIF
      * Analisi delle informazioni
1    C                   IF        £JaxD1='' and £JaxT1<>*BLANKS
     C                   EVAL      £DECTP=£JaxT1
     C                   EVAL      £DECPA=£JaxP1
     C                   EVAL      £DECCD=£JaxK1
     C                   EXSR      £DEC
     C  N35              EVAL      £JaxD1=£DECDE
1e   C                   ENDIF
      * Costruzione
     C                   EVAL      £JaxCP=
     C                             '<Oggetto '+
     C                             'Nome="'+%TRIM(£JaxNM)+'" '+
     C                             'Tipo="'+%TRIM(£JaxT1)+'" '+
     C                             'Parametro="'+%TRIM(£JaxP1)+'" '+
     C                             'Codice="'+%TRIMR(£JaxK1)+'" '+
     C                             'Testo="'+%TRIM(£JaxD1)+'"'
      *
1    C                   SELECT
1x   C                   WHEN      £JaxOP<>*BLANKS AND
     C                             %LEN(%TRIM(£JaxOP))>4 AND
     C                             %SUBST(%TRIM(£JaxOP):1:4)='Exec'             COSTANTE
      *C                   CLEAR                   £JAXOP_LEN        4 0
     C                   CLEAR                   £JAXOP_LEN        5 0
     C                   EVAL      £JAXOP_LEN=%LEN(%TRIMR(£JAXOP))
      *
2    C                   IF        £JAXOP_LEN>6
3    C                   IF        %SUBST(£JAXOP:£JAXOP_LEN:1)='"' AND
     C                             %SUBST(£JAXOP:1:6)='Exec="'
     C                   EVAL      £JAXOP='Exec="'
     C                             +        %SUBST(£JAXOP:7:£JAXOP_LEN-7) +'"'
3e   C                   ENDIF
2e   C                   ENDIF
      *
     C                   EVAL      £JaxCP=%TRIM(£JaxCP)+' '+
     C                             %TRIM(£JaxOP)
1x   C                   WHEN      £JaxOP<>*BLANKS
     C                   EVAL      £JaxCP=%TRIM(£JaxCP)+' '+
     C                             'Exec="'+%TRIM(        £JaxOP) +'"'          COSTANTE
1e   C                   ENDSL
      * Modo esecuzione
1    C                   IF        £JaxMD<>*BLANKS
     C                   EVAL      £JaxCP=%TRIM(£JaxCP)+' '+
     C                             'Mode="'+%TRIM(£JaxMD)+'"'
1e   C                   ENDIF
      * Chiusura automatica
1    C                   IF        £JaxEN=*BLANKS
     C                   EVAL      £JaxEN='/>'
1e   C                   ENDIF
      * Gruppo di bottoni
1    C                   IF        £JaxGP<>*BLANKS
     C                   EVAL      £JaxCP=%TRIM(£JaxCP)+' '+
     C                             'Grp="'+%TRIM(£JaxGP)+'"'
1e   C                   ENDIF
      * Mostra Icona (per bottoni)
1    C                   IF        £JaxSI<>*BLANKS
     C                   EVAL      £JaxCP=%TRIM(£JaxCP)+' '+
     C                             'ShowIcon="'+%TRIM(£JaxSI)+'"'
1e   C                   ENDIF
      * Mostra Testo (per bottoni)
1    C                   IF        £JaxST<>*BLANKS
     C                   EVAL      £JaxCP=%TRIM(£JaxCP)+' '+
     C                             'ShowText="'+%TRIM(£JaxST)+'"'
1e   C                   ENDIF
      * Stato (EXP=Forza espanso, COL=Forza collassato)
1    C                   IF        £JaxEC<>*BLANKS
     C                   EVAL      £JaxCP=%TRIM(£JaxCP)+' '+
     C                             'Stato="'+%TRIM(£JaxEC)+'"'
1e   C                   ENDIF
      * Style
1    C                   IF        £JaxSY<>*BLANKS
     C                   EVAL      £JaxCP=%TRIM(£JaxCP)+' '+
     C                             'Style="'+%TRIM(£JaxSY)+'"'
1e   C                   ENDIF
      * Setup Bottoni
1    C                   IF        £JaxSB<>*BLANKS
     C                   EVAL      £JaxCP=%TRIM(£JaxCP)+' '+
     C                             'BtnSet="'+%TRIM(£JaxSB)+'"'
1e   C                   ENDIF
      *
      *
     C                   EVAL      £JaxCP=%TRIM(£JaxCP)+' '+
     C                             %TRIM(£JaxWK)+' '+
     C                             %TRIM(£JaxGR)+' '+
     C                             %TRIM(£JaxIC)+' '+
     C                             'Fld="'+%TRIM(£JaxFL)+'" '+
     C                             'Leaf="'+%TRIM(£JaxLF)+'"'+
     C                             %TRIM(£JaxEN)
      * Pulisco oggetto
     C                   EXSR      £JAX_IMPO
      * In buffer
     C                   EXSR      £JAX_ADD
      *
     C     G9JAXADDO     ENDSR
     C*----------------------------------------------------------------
    RD* Chiudo oggetto
     C*----------------------------------------------------------------
     C     £JAX_CLOO     BEGSR
      *
1    C                   IF        £JAXNS='2'
     C                   LEAVESR
1e   C                   ENDIF
      *
     C                   EVAL      £JaxCP='</Oggetto>'
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Aggiungo al buffer un elemento di semaforo o gauge
     C*----------------------------------------------------------------
     C     £JAX_ADDE     BEGSR
      *
      * Costruzione
     C                   EVAL      £JaxCP=
     C                             '<Elemento '+
     C                             'Valore="'+%TRIM(£JaxVA)+'" '+
     C                             'Soglia1="'+%TRIM(£JaxS1)+'" '+
     C                             'Soglia2="'+%TRIM(£JaxS2)+'" '
      * Minimo (serve solo per gauge)
1    C                   IF        £JaxMn<>*BLANKS
     C                   EVAL      £JaxCP=%TRIM(£JaxCP)+' '+
     C                             'Min="'+%TRIM(£JaxMN)+'" '
1e   C                   ENDIF
      * Massimo (serve solo per gauge)
1    C                   IF        £JaxMX<>*BLANKS
     C                   EVAL      £JaxCP=%TRIM(£JaxCP)+' '+
     C                             'Max="'+%TRIM(£JaxMX)+'" '
1e   C                   ENDIF
      * Inversione (serve solo per gauge)
1    C                   IF        £JaxIN<>*BLANKS
     C                   EVAL      £JaxCP=%TRIM(£JaxCP)+' '+
     C                             'Inv="Yes"'
1e   C                   ENDIF
     C                   EVAL      £JaxCP=%TRIM(£JaxCP)+' '+'/>'
      * Pulisco elemento
     C                   EXSR      £JAX_IMPO
      * In buffer
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Impostazioni iniziali
     C*----------------------------------------------------------------
     C     £JAX_IMPO     BEGSR
     C
     C                   CLEAR                   £JaxDSOgg
     C                   CLEAR                   £JaxDSEle
     C                   CLEAR                   £JAX_$CO          3 0
     C
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Inizializzazione griglia
     C*----------------------------------------------------------------
     C     £JAX_AGRI_I   BEGSR
      *
     C                   EVAL      £JAXCP='<Griglia>'
     C                   EXSR      £JAX_ADD
     C                   CLEAR                   £JAXCP
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Aggiungo griglia
     C*----------------------------------------------------------------
     C     £JAX_AGRI     BEGSR
      *
     C                   EXSR      £JAX_AGRI_I
      *
     C                   CLEAR                   £JAX_NMAX         5 0
     C                   EVAL      £JAX_NMAX=%ELEM(£JAXSWK)
     C                   CLEAR                   £JAX2NFUL         5 0
1    C                   DO        £JAX_NMAX     £JAX_N50          5 0
2    C                   IF        £JAXSW2Key(£JAX_N50)<>''
     C                   EVAL      £JAX2NFUL=£JAX2NFUL+1
2e   C                   ENDIF
1e   C                   ENDDO
     C                   CLEAR                   £JAX3NFUL         5 0
1    C                   DO        £JAX_NMAX     £JAX_N50          5 0
2    C                   IF        £JAXSW3Key(£JAX_N50)<>''
     C                   EVAL      £JAX3NFUL=£JAX3NFUL+1
2e   C                   ENDIF
1e   C                   ENDDO
1     *C                   DO        *HIVAL        £JAX_N50          5 0
1    C                   DO        £JAX_NMAX     £JAX_N50          5 0
2    C                   IF        £JAXSWK(£JAX_N50)=*BLANKS
     C                   LEAVE
2e   C                   ENDIF
      * Eventali XML inclusi
2    C                   IF        %SUBST(£JAXSWK(£JAX_N50):01:01)='.'
     C                   EVAL      £JAXCP=%TRIM(%SUBST(£JAXSWK(£JAX_N50):02))
     C                   EXSR      £JAX_ADD
2x   C                   ELSE
3    C                   IF        £JAX_N50<>1
     C                   EVAL      £JAXCP='</Colonna>'
     C                   EXSR      £JAX_ADD
3e   C                   ENDIF
     C                   EVAL      £JAXDSCOL=£JAXSWK(£JAX_N50)
     C                   CLEAR                   £JAX2N50          5 0
3    C                   IF        £JAXCCD<>'' AND £JAX2NFUL>0
     C                   EVAL      £JAX2N50=%LOOKUP(£JAXCCD:£JAXSW2Key:1)
3e   C                   ENDIF
3    C                   IF        £JAX2N50>0
     C                   EVAL      £JAXDSCO2=£JAXSW2(£JAX2N50)
     C                   EVAL      £JAX2NFUL=£JAX2NFUL-1
3x   C                   ELSE
     C                   CLEAR                   £JAXDSCO2
3e   C                   ENDIF
     C                   CLEAR                   £JAX3N50          5 0
3    C                   IF        £JAXCCD<>'' AND £JAX3NFUL>0
     C                   EVAL      £JAX3N50=%LOOKUP(£JAXCCD:£JAXSW3Key:1)
3e   C                   ENDIF
3    C                   IF        £JAX3N50>0
     C                   EVAL      £JAXDSCO3=£JAXSW3(£JAX3N50)
     C                   EVAL      £JAX3NFUL=£JAX3NFUL-1
3x   C                   ELSE
     C                   CLEAR                   £JAXDSCO3
3e   C                   ENDIF
      * Segnala di lasciare aperta la colonna
     C                   MOVEL     '1'           £JAXAC
     C                   EXSR      £JAX_ACOL
2e   C                   ENDIF
1e   C                   ENDDO
      *
1    C                   IF        £JAX_N50<>1
     C                   EVAL      £JAXCP='</Colonna>'
     C                   EXSR      £JAX_ADD
1e   C                   ENDIF
      *
     C                   EXSR      £JAX_AGRI_F
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Finalizzazione Griglia
     C*----------------------------------------------------------------
     C     £JAX_AGRI_F   BEGSR
      *
     C                   EVAL      £JAXCP='</Griglia>'
     C                   EXSR      £JAX_ADD
     C                   CLEAR                   £JAXCP
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Aggiungo colonna
     C*----------------------------------------------------------------
     C     £JAX_ACOL     BEGSR
      *
     C                   ADD       1             £JAX_$CO
      *
3    C******             IF        £JAXCAL<>' '
     C******             EVAL      £JAX_A50='Als="Decodifica"'
3x   C******             ELSE
     C******             CLEAR                   £JAX_A50         50
3e   C******             ENDIF
      *
1    C                   IF        £JAXCIO<>''
     C                   EVAL      £JAX_A1=£JAXCIO
1x   C                   ELSE
     C                   MOVEL     'O'           £JAX_A1           1
1e   C                   ENDIF
      * Se previsto campo in input panel risalgo a comportamento standard
     C*******            IF        £JAX_A1='B' AND £UIBPG='INP'
1    C                   IF        £UIBPG='INP'
     C                   EVAL      £K04FU='DAT'
     C                   EVAL      £K04ME=' '
     C                   EVAL      £K04I_OG=£JAXCOG
     C                   EXSR      £K04
2    C                   IF        £JAX_A1='B'
     C                   EVAL      £JAX_A1=£K04O_IP
2e   C                   ENDIF
1e   C                   ENDIF
      * Se ho voluto forzare di non utilizzare le combo ripristino la modalità B
1    C                   IF        £JAX_A1='b'
     C                   EVAL      £JAX_A1='B'
1e   C                   ENDIF
      *
     C                   EVAL      £JAXCP='<Colonna'
      *
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' Cod="'
     C                             +%TRIM(£JAXCCD)
     C                             +'"'
      *
1    C                   IF        £JAXCTX = '' AND £JAXCTXE <> ''
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' Txt="'
     C                             +%TRIM(£JAXCTXE)
     C                             +'"'
     C                   EVAL      £JAXCTXE=''
1x   C                   ELSE
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' Txt="'
     C                             +%TRIM(£JAXCTX)
     C                             +'"'
1e   C                   ENDIF
      * Formule
1    C                   IF        £JAXCAL='F'
2    C                   IF        £JAXCFO<>' '
     C                   EVAL      £JAXCP=£JAXCP+' Formula="'
     C                             +%TRIMR(£JAXCFO)+'"'
2x   C                   ELSE
     C                   EVAL      £JAXCP=£JAXCP+' Formula="'
     C                             +%TRIMR(£JAXCFI+£JAXCTP)+'"'
     C                   CLEAR                   £JAXCFI
     C                   CLEAR                   £JAXCTP
2e   C                   ENDIF
     C                   CLEAR                   £JAXCAL
     C                   CLEAR                   £JAXCFO
1e   C                   ENDIF
      * Esteso
1    C                   IF        £JAXCAL='E' AND £JAXCFI+£JAXCTP <> ''
     C                   EVAL      £JAXCP=£JAXCP+' Extended="'
     C                             +%TRIMR(£JAXCFI+£JAXCTP)+'"'
     C                   CLEAR                   £JAXCFI
     C                   CLEAR                   £JAXCTP
1e   C                   ENDIF
      * Grafica
1    C                   IF        £JAXCAL='G' AND £JAXCFI+£JAXCTP <> ''
      * . mettere GLUN(nn) in £JAXCFI+£JAXCTP per sovrascrivere la larghezza del campo dell'input
      * . panel in caratteri (Lunghezza grafica)
     C                   EVAL      £JAXCP=£JAXCP+' Grp="'
     C                             +%TRIMR(£JAXCFI+£JAXCTP)+'"'
     C                   CLEAR                   £JAXCFI
     C                   CLEAR                   £JAXCTP
1e   C                   ENDIF
      *
1    C                   IF        £JAXCTP<>''
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' Tip="'
     C                             +%TRIM(£JAXCTP)
     C                             +'"'
1e   C                   ENDIF
      * Se *HI imposto 30000
1    C                   IF        %trim(£JAXCLU)=£JaxMaxStr
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' Lun="'+£JaxMaxLen
     C                             +'"'
1x   C                   ELSE
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' Lun="'
     C                             +%TRIM(£JAXCLU)
     C                             +'"'
1e   C                   ENDIF
      *
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' IO="'
     C                             +£JAX_A1
     C                             +'"'
      *
1    C                   IF        £JAXCOG<>''
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' Ogg="'
     C                             +%TRIMR(£JAXCOG)
     C                             +'"'
1e   C                   ENDIF
      *
1    C                   IF        £JAXCDY<>''
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' Dpy="'
     C                             +%TRIM(£JAXCDY )
     C                             +'"'
1e   C                   ENDIF
      * Altezza cella Gantt
1    C                   IF        £JAXCAL='H'
     C                   EVAL      £JAXCP=£JAXCP+' Fill="'
     C                             +%TRIMR(£JAXCFI+£JAXCTP+£JAXCLA)+'"'
     C                   CLEAR                   £JAXCAL
     C                   CLEAR                   £JAXCFI
     C                   CLEAR                   £JAXCTP
     C                   CLEAR                   £JAXCLA
1x   C                   ELSE
      *
2    C                   IF        £JAXCFI<>''
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' Fill="'
     C                             +%TRIMR(£JAXCFI)
     C                             +'"'
     C******                       +' '+%TRIM(£JAX_A50)
2e   C                   ENDIF
1e   C                   ENDIF
      *
1    C                   IF        £JAXCLA<>''
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' Aut="'
     C                             +%TRIM(£JAXCLA)
     C                             +'"'
1e   C                   ENDIF
      * Campi aggiuntivi
1    C                   IF        £JAX2ET<>''
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' ETxt="'
     C                             +%TRIM(£JAX2ET)
     C                             +'"'
1e   C                   ENDIF
      *
1    C                   IF        £JAX3CO<>''
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' Cmp="'
     C                             +%TRIM(£JAX3CO)
     C                             +'"'
1e   C                   ENDIF
      *
1    C                   IF        £JAX3EC<>''
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' Ext="'
     C                             +%TRIM(£JAX3EC)
     C                             +'"'
1e   C                   ENDIF
      *
1    C                   IF        £UIBPG='INP' AND £JAX3CO=''
     C                             AND £JAX3CD=''
2    C                   SELECT
2x   C                   WHEN      £JAX_A1<>'O' AND £JAX_A1<>'H' AND
     C                             £K04O_FG<>''
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' Cmp="'
     C                             +%TRIM(£K04O_FG)
     C                             +'"'
3    C                   IF        £K04O_PG<>''
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' Ext="'
     C                             +%TRIM(£K04O_PG)
     C                             +'"'
3e   C                   ENDIF
2x   C                   WHEN      £JAX_A1='O' AND
     C                             £K04O_FO<>''
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' Cmp="'
     C                             +%TRIM(£K04O_FO)
     C                             +'"'
3    C                   IF        £K04O_PO<>''
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' Ext="'
     C                             +%TRIM(£K04O_PO)
     C                             +'"'
3e   C                   ENDIF
2e   C                   ENDSL
1e   C                   ENDIF
      *
1    C                   IF        £JAX3TK<>''
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' TfK="'
     C                             +%TRIM(£JAX3TK)
     C                             +'"'
2    C                   IF        £JAX3PK<>''
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' PfK="'
     C                             +%TRIM(£JAX3PK)
     C                             +'"'
2e   C                   ENDIF
2    C                   IF        £JAX3SK<>''
     C                   EVAL      £JAXCP=£JAXCP
     C                             +' SfK="'
     C                             +%TRIM(£JAX3SK)
     C                             +'"'
2e   C                   ENDIF
1e   C                   ENDIF
      *
1    C                   SELECT
1x   C                   WHEN      £JAXAC<>'' AND £JAXCAL<>'C'
     C                   EVAL      £JAXCP=£JAXCP+'>'
1x   C                   WHEN      £JAXAC='' AND £JAXCAL<>'C'
     C                   EVAL      £JAXCP=£JAXCP+'/>'
1x   C                   WHEN      £JAXCAL='C'
     C                   EVAL      £JAXCP=£JAXCP+'>'
     C                   EVAL      £JAXCP=£JAXCP+' '
     C                             +'<Relazioni Tabella="'
     C                             +%TRIM(%EDITW(£JAX_$CO:'0   '))+' '
     C                             +%TRIM(£JAXCTX)
     C                             +'"> '
     C                             +'<Relazione Campo="C'
     C                             +%TRIM(%EDITW(£JAX_$CO:'0   '))
     C                             +'" Valore="['
     C                             +%TRIM(£JAXCCD)+']"/> '
     C                             +'<Relazione Campo="T'
     C                             +%TRIM(%EDITW(£JAX_$CO:'0   '))
     C                             +'" Valore="'+%TRIM(£JAXCOG)
     C                             +'"/></Relazioni>'
     C                             +' </Colonna>'
1e   C                   ENDSL
      * Sblanka il flag di 'non chiudere la colonna'
     C                   MOVEL     *BLANKS       £JAXAC            1
      *
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Inizializzazione scrittura delle righe
     C*----------------------------------------------------------------
     C     £JAX_ARIG_I   BEGSR
      *
     C                   EVAL      £JAXCP='<Righe>'
     C                   EXSR      £JAX_ADD
     C                   CLEAR                   £JAXCP
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Aggiungo riga
     C*----------------------------------------------------------------
     C     £JAX_ARIG     BEGSR
      *
      * Nome tag ed eventuale tipo par
1    C                   IF        £JAXT1<>*BLANKS
     C                   EVAL      £JAXWS='<Riga '
     C                             +'Tipo="'+%TRIM(£JaxT1)+'" '
     C                             +'Parametro="'+%TRIM(£JaxP1)+'" '
     C                             +'Codice="'+%TRIMR(£JaxK1)+'" '
     C                             +'Testo="'+%TRIM(£JaxD1)+'" '
     C                             +'Fld="'+£JAXCP +'"'
1x   C                   ELSE
     C                   EVAL      £JAXWS='<Riga '
     C                             +'Fld="'+£JAXCP+'"'
1e   C                   ENDIF
      * Eventuale altezza di riga
1    C                   IF        £JaxRowHt<>*BLANKS
     C                   EVAL      £JAXWS=%TRIM(£JAXWS)
     C                             +' RowHeight="'+%TRIM(£JaxRowHt)+'"'
     C                   CLEAR                   £JaxRowHt
1e   C                   ENDIF
      *
     C                   EVAL      £JAXWS=%TRIM(£JAXWS)+'/>'
      *
1    C                   IF        %LEN(£JAXWS)>30000
     C                   EVAL      £JAXCP=%SUBST(£JAXWS:1:30000)
     C                   EXSR      £JAX_ADD
     C                   EVAL      £JAXCP=%SUBST(£JAXWS:30001)
     C                   EXSR      £JAX_ADD
2    C                   IF        %LEN(£JAXWS)>60000
     C                   EVAL      £JAXCP=%SUBST(£JAXWS:60001)
     C                   EXSR      £JAX_ADD
2e   C                   ENDIF
1x   C                   ELSE
     C                   EVAL      £JAXCP=£JAXWS
     C                   EXSR      £JAX_ADD
1e   C                   ENDIF
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Aggiungo riga lunga (Inizio)
     C*----------------------------------------------------------------
     C     £JAX_ARIG_LI  BEGSR
      *
      * Nome tag ed eventuale tipo par
1    C                   IF        £JAXT1<>*BLANKS
     C                   EVAL      £JAXCP='<Riga '
     C                             +'Tipo="'+%TRIM(£JaxT1)+'" '
     C                             +'Parametro="'+%TRIM(£JaxP1)+'" '
     C                             +'Codice="'+%TRIMR(£JaxK1)+'" '
     C                             +'Testo="'+%TRIM(£JaxD1)+'" '
     C                             +'Fld="'
1x   C                   ELSE
     C                   EVAL      £JAXCP='<Riga '
     C                             +'Fld="'
1e   C                   ENDIF
      *
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Aggiungo riga lunga (Fine)
     C*----------------------------------------------------------------
     C     £JAX_ARIG_LF  BEGSR
      *
      * Chiudo Fld="
     C                   EVAL      £JAXCP='"'
      * Eventuale altezza di riga
1    C                   IF        £JaxRowHt<>*BLANKS
     C                   EVAL      £JAXCP=%TRIM(£JAXCP)
     C                             +' RowHeight="'+%TRIM(£JaxRowHt)+'"'
     C                   CLEAR                   £JaxRowHt
1e   C                   ENDIF
      *
     C                   EVAL      £JAXCP=%TRIM(£JAXCP)+'/>'
      *
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Aggiungo riga lunga (contenuto della riga in £JAXCL invece che in £JAXCP)
     C*----------------------------------------------------------------
     C     £JAX_ARIG_L   BEGSR
      *
     C                   EVAL      £JAXWS=£JAXCL
      *
1    C                   IF        %LEN(£JAXWS)>30000
     C                   EVAL      £JAXCP=%SUBST(£JAXWS:1:30000)
     C                   EXSR      £JAX_ADD
     C                   EVAL      £JAXCP=%SUBST(£JAXWS:30001)
     C                   EXSR      £JAX_ADD
2    C                   IF        %LEN(£JAXWS)>60000
     C                   EVAL      £JAXCP=%SUBST(£JAXWS:60001)
     C                   EXSR      £JAX_ADD
2e   C                   ENDIF
1x   C                   ELSE
     C                   EVAL      £JAXCP=£JAXWS
     C                   EXSR      £JAX_ADD
1e   C                   ENDIF
     C                   EVAL      £JAXCL=''
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Finalizzazione scrittura delle righe
     C*----------------------------------------------------------------
     C     £JAX_ARIG_F   BEGSR
      *
     C                   EVAL      £JaxCP='</Righe>'
     C                   EXSR      £JAX_ADD
     C                   CLEAR                   £JAXCP
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Inizializzazione Buffer messaggi
     C*----------------------------------------------------------------
     C     £JAX_BMES_I   BEGSR
      *
     C                   Z-ADD     0             £JaxMBfI
     C******             CLEAR                   £JaxMBf
     C                   CLEAR                   £JaxDSMsg
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Aggiungo a Buffer messaggi
     C*----------------------------------------------------------------
     C     £JAX_BMES     BEGSR
      *
1     *C                   IF        £JaxMBfI<200
1    C                   IF        £JaxMBfI<%ELEM(£JaxMBF)
     C                   EVAL      £JaxMBfI=£JaxMBfI+1
      * Scrittura diretta, mantenuta per compatibilità
2    C                   IF        £JaxDSMsg=*BLANKS
     C                   EVAL      £JaxMLiv=%SUBST(£JaxCP:1:2)
     C                   EVAL      £JaxMTxt=%SUBST(£JaxCP:3)
2e   C                   ENDIF
     C                   EVAL      £JaxMBF(£JaxMBfI)=£JaxDSMsg
     C                   CLEAR                   £JaxDSMsg
1e   C                   ENDIF
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Svuoto Buffer messaggi
     C*----------------------------------------------------------------
     C     £JAX_BMES_F   BEGSR
      *
1    C                   IF        £JaxMBFI>0
     C                   EXSR      £JAX_AMES_I
2    C                   DO        £JaxMBFI      £JAX_N50
     C                   EVAL      £JaxDSMsg=£JaxMBf(£JAX_N50)
     C                   EXSR      £JAX_AMES
2e   C                   ENDDO
     C                   EXSR      £JAX_AMES_F
1e   C                   ENDIF
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Inizializzazione scrittura messaggi
     C*----------------------------------------------------------------
     C     £JAX_AMES_I   BEGSR
      *
     C                   CLEAR                   £JAX_MXT          1
     C                   CLEAR                   £JAX_MX           2
      * Se a '1' lascia aperto il nodo
     C                   CLEAR                   £JaxMOpn          1
      * Funzione su messaggio QUEST
     C                   CLEAR                   £JaxMKFun        10
     C                   CLEAR                   £JaxMTFun        30
     C                   CLEAR                   £JaxMFun        512
     C                   CLEAR                   £JaxMKFu2        10
     C                   CLEAR                   £JaxMTFu2        30
     C                   CLEAR                   £JaxMFu2        512
     C                   CLEAR                   £JaxMKFu3        10
     C                   CLEAR                   £JaxMTFu3        30
     C                   CLEAR                   £JaxMFu3        512
     C                   CLEAR                   £JaxMTx2
      *
     C                   EVAL      £JAXCP='<Messaggi>'
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Aggiungo messaggio
     C*----------------------------------------------------------------
     C     £JAX_AMES     BEGSR
      *
      * Compatibilità con vecchia chiamata diretta
1    C                   IF        £JaxDSMsg=*BLANKS
     C                   EVAL      £JaxMLiv=£JaxLV
     C                   EVAL      £JaxMTxt=£JaxCP
1e   C                   ENDIF
1    C                   IF        £JaxDSMsg<>*BLANKS
      * Imposto il tipo di messaggio
     C                   CLEAR                   £JaxMsgType       5
2    C                   SELECT
      * . I=Messaggio informativo
2x   C                   WHEN      £JaxMTyp='I'
     C                   EVAL      £JaxMsgType='INFO'
      * . C=Messaggio con richiesta di conferma
2x   C                   WHEN      £JaxMTyp='C'
     C                   EVAL      £JaxMsgType='CONF'
      * . Q=Messaggio con richiesta di conferma con funzioni associate
2x   C                   WHEN      £JaxMTyp='Q'
     C                   EVAL      £JaxMsgType='QUEST'
     C                   EVAL      £Jax_MXT='Q'
3    C                   IF        £JaxMFun <> '' AND £JaxMOpn=''
     C                   EVAL      £JaxMOpn='Q'
3e   C                   ENDIF
      * . N=Notifica con esecuzione di funzione
2x   C                   WHEN      £JaxMTyp='N'
     C                   EVAL      £JaxMsgType='NOTIF'
     C                   EVAL      £Jax_MXT='Q'
3    C                   IF        £JaxMFun <> '' AND £JaxMOpn=''
     C                   EVAL      £JaxMOpn='Q'
3e   C                   ENDIF
      * . Default: Messaggio informnativo
2x   C                   OTHER
     C                   EVAL      £JaxMsgType='INFO'
2e   C                   ENDSL
      * Compatibilità con utilizzo livello (invece che gravità e modalità)
2    C                   SELECT
2x   C                   WHEN      £JaxMLiv<>'' AND  £JaxMGra=''
3    C                   IF        £JaxMLiv>£JAX_MX
     C                   EVAL      £JAX_MX=£JaxMLiv
3e   C                   ENDIF
      * retrocompatibilità loocup: > 20 WARNING, > 60 ERROR, resto INFO
3    C                   SELECT
3x   C                   WHEN      £JaxMLiv>'60'
     C                   EVAL      £JaxMGra=£Jax_GraErr
3x   C                   WHEN      £JaxMLiv>'20'
     C                   EVAL      £JaxMGra=£Jax_GraWrn
3x   C                   OTHER
     C                   EVAL      £JaxMGra=£Jax_GraInf
3e   C                   ENDSL
2x   C                   WHEN      £JaxMGra<>''
3    C                   SELECT
3x   C                   WHEN      £JaxMGra=£Jax_GraErr
      * Forzo il livello solo se errore se gestione nuova (serve per esito)
4    C                   IF        £Jax_LvlErr>£JAX_MX
     C                   EVAL      £JAX_MX=£Jax_LvlErr
4e   C                   ENDIF
3e   C                   ENDSL
2e   C                   ENDSL
2    C                   IF        £JaxMLiv<>'' AND  £JaxMMod=''
      * retrocompatibilità loocup: > 25 modale, 00 non visibile, resto notifica a scomparsa
3    C                   SELECT
3x   C                   WHEN      £JaxMLiv='00'
      * messaggio nascosto
     C                   EVAL      £JaxMMod=£Jax_ModHH
3x   C                   WHEN      £JaxMLiv>'25'
      * messaggio permanente (modale)
     C                   EVAL      £JaxMMod=£Jax_ModPM
3x   C                   WHEN      £JaxMLiv>'00'
      * notifica temporanea (a scomparsa)
     C                   EVAL      £JaxMMod=£Jax_ModTN
3e   C                   ENDSL
2e   C                   ENDIF
2    C                   IF        £JaxMGra=''
     C                   EVAL      £JaxMGra=£Jax_GraInf
2e   C                   ENDIF
2    C                   IF        £JaxMMod=''
      * messaggio nascosto
     C                   EVAL      £JaxMMod=£Jax_ModHH
2e   C                   ENDIF
      * Forzatura Mode PM per tipo QUEST e CONF (se devo cliccare su un pulsante è più sicuro
      * forzare un messaggio modale)
2    C                   IF        £JaxMsgType='QUEST' OR £JaxMsgType='CONF'
      * Forzatura Mode PM per tipo QUEST e CONF se non è PT
3    C                   IF        £JaxMMod<>'PT'
     C                   EVAL      £JaxMMod=£Jax_ModPM
3e   C                   ENDIF
2e   C                   ENDIF
      *
     C                   EVAL      £JAXCP='<Messaggio Testo="'
     C                             +%TRIM(£JaxMTxt )
     C                             +'" TestoCompleto="'
     C                             +%TRIMR(£JaxMTx2)
     C                             +'" Gravity="'+%TRIM(£JaxMGra)
     C                             +'" Mode="'+%TRIM(£JaxMMod)
     C                             +'" Tipo="'+%TRIM(£JaxMsgType)+'"'
2    C                   IF        £JaxMT1<>'' OR £JaxMP1<>'' OR £JaxMK1<>''
     C                   EVAL      £JAXCP=%TRIM(£JAXCP)
     C                             +' T1="'+%TRIMR(£JaxMT1)+'" '
     C                             +' P1="'+%TRIMR(£JaxMP1)+'" '
     C                             +' K1="'+%TRIMR(£JaxMK1)+'" '
2e   C                   ENDIF
2    C                   IF        £JaxMT2<>'' OR £JaxMP2<>'' OR £JaxMK2<>''
     C                   EVAL      £JAXCP=%TRIM(£JAXCP)
     C                             +' T2="'+%TRIMR(£JaxMT2)+'" '
     C                             +' P2="'+%TRIMR(£JaxMP2)+'" '
     C                             +' K2="'+%TRIMR(£JaxMK2)+'" '
2e   C                   ENDIF
2    C                   IF        £JaxMOpn=*BLANKS
     C                   EVAL      £JAXCP=%TRIM(£JAXCP)+'/>'
2x   C                   ELSE
     C                   EVAL      £JAXCP=%TRIM(£JAXCP)+'>'
2e   C                   ENDIF
     C                   EXSR      £JAX_ADD
      * . Default: Messaggio QUEST con funzione
2    C                   IF        £JaxMFun <> ''
     C                   EXSR      £JAX_APOP_I
     C                   EVAL      £JaxT1='J1'
     C                   EVAL      £JaxP1='KEY'
3    C                   IF        £JaxMKFun=''
     C                   EVAL      £JaxK1='*ENTER'
3x   C                   ELSE
     C                   EVAL      £JaxK1=£JaxMKFun
3e   C                   ENDIF
     C                   EVAL      £JaxD1=£JaxMTFun
     C                   EVAL      £JaxOP=£JaxMFun
     C                   EXSR      £JAX_APOP
3    C                   IF        £JaxMFu2 <> '' AND £JaxMKFu2 <> ''
     C                   EVAL      £JaxT1='J1'
     C                   EVAL      £JaxP1='KEY'
     C                   EVAL      £JaxK1=£JaxMKFu2
     C                   EVAL      £JaxD1=£JaxMTFu2
     C                   EVAL      £JaxOP=£JaxMFu2
     C                   EXSR      £JAX_APOP
3e   C                   ENDIF
3    C                   IF        £JaxMFu3 <> '' AND £JaxMKFu3 <> ''
     C                   EVAL      £JaxT1='J1'
     C                   EVAL      £JaxP1='KEY'
     C                   EVAL      £JaxK1=£JaxMKFu3
     C                   EVAL      £JaxD1=£JaxMTFu3
     C                   EVAL      £JaxOP=£JaxMFu3
     C                   EXSR      £JAX_APOP
3e   C                   ENDIF
     C                   EXSR      £JAX_APOP_F
2e   C                   ENDIF
     C                   EVAL      £JaxMFun=''
     C                   EVAL      £JaxMTFun=''
     C                   EVAL      £JaxMKFun=''
     C                   EVAL      £JaxMFu2=''
     C                   EVAL      £JaxMTFu2=''
     C                   EVAL      £JaxMKFu2=''
     C                   EVAL      £JaxMFu3=''
     C                   EVAL      £JaxMTFu3=''
     C                   EVAL      £JaxMKFu3=''
2    C                   IF        £JaxMOpn = 'Q'
     C                   EXSR      £JAX_CMES
2e   C                   ENDIF
     C                   CLEAR                   £JaxMOpn
      *
     C                   CLEAR                   £JaxDSMsg
     C                   CLEAR                   £JaxMTx2
1e   C                   ENDIF
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Chiudo nodo messaggio
     C*----------------------------------------------------------------
     C     £JAX_CMES     BEGSR
      *
     C                   EVAL      £JAXCP='</Messaggio>'
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Finalizzazione scrittura dei messaggi
     C*----------------------------------------------------------------
     C     £JAX_AMES_F   BEGSR
      *
1    C                   IF        £JAX_MX>'30' AND £JAX_MXT  = 'Q' OR
     C                             £JAX_MX>'10' AND £JAX_MXT <> 'Q'
     C                   EVAL      £JAXCP='<Esito Stato="ERRORE"/>'
     C                   EXSR      £JAX_ADD
1x   C                   ELSE
     C                   EVAL      £JAXCP='<Esito Stato="OK"/>'
     C                   EXSR      £JAX_ADD
1e   C                   ENDIF
      *
     C                   EVAL      £JaxCP='</Messaggi>'
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
      *----------------------------------------------------------------
    RD* Inizializzazione KEY
      *----------------------------------------------------------------
     C     £JAX_AKEY_I   BEGSR
      *
     C                   EVAL      £JaxKIn=0
     C                   EVAL      £JAXCP='<Key>'
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Aggiungo griglia
     C*----------------------------------------------------------------
     C     £JAX_AKEY     BEGSR
     C                   EVAL      £JaxKIn=£JaxKIN+1
      * Analisi delle informazioni
1    C                   IF        £JaxD1='' and £JaxT1<>*BLANKS
     C                   EVAL      £DECTP='OG'
     C                   EVAL      £DECCD=%TRIM(£JaxT1)+%TRIM(£JaxP1)
     C                   EXSR      £DEC
     C  N35              EVAL      £JaxD1=£DECDE
1e   C                   ENDIF
1    C                   IF        %TRIM(£JaxKId)=''
     C                   EVAL      £JaxKId='K'+%TRIM(%EDITC(£JaxKIn:'Z'))
1e   C                   ENDIF
      * Assumo oggetto automatico
1    C                   IF        £JaxKOb='3'
2    C                   SELECT
2x   C                   WHEN      £JaxKT1='OJ' AND £JaxKP1='*USRPRF'
     C                   EVAL      £JaxKK1=%TRIM(£PDSNU)
2x   C                   WHEN      £JaxKT1='UT'
     C                   EVAL      £JaxKK1=%TRIM(£PDSNU)
2x   C                   WHEN      £JaxKT1='TA' AND £JaxKP1='B£U'
     C                   EVAL      £JaxKK1=%TRIM(£PDSNU)
2x   C                   WHEN      £JaxKT1='UP'
     C                   EVAL      £JaxKK1=%TRIM(£PDSNU)
2x   C                   WHEN      £JaxKT1='OJ' AND £JaxKP1='*PGM'
     C                   EVAL      £JaxKK1=%TRIM(£PDSNP)
2x   C                   WHEN      £JaxKT1='V2' AND £JaxKP1='STAR'
     C                   EVAL      £JaxKK1='**'
2e   C                   ENDSL
1e   C                   ENDIF
      * Costruzione
     C                   EVAL      £JaxCP=
     C                             '<'+%TRIM(£JaxKId)+' '+
     C                             'Tipo="'+%TRIM(£JaxKT1)+'" '+
     C                             'Parametro="'+%TRIM(£JaxKP1)+'" '+
     C                             'Codice="'+%TRIMR(£JaxKK1)+'" '+
     C                             'Testo="'+%TRIM(£JaxKD1)+'" ' +
     C                             'Obb="'+%TRIM(£JaxKOb)+'" ' +
     C                             'Mod="'+%TRIM(£JaxKMO)+'" ' +
     C                             '/>'
      * Pulisco Key
     C                   CLEAR                   £JaxDSKey
      * In buffer
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Finalizzazione Griglia
     C*----------------------------------------------------------------
     C     £JAX_AKEY_F   BEGSR
      *
     C                   EVAL      £JAXCP='</Key>'
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
      *----------------------------------------------------------------
    RD* Inizializzazione POPUP
      *----------------------------------------------------------------
     C     £JAX_APOP_I   BEGSR
      *
     C                   EVAL      £JAXCP='<UIPopup>'
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
      *----------------------------------------------------------------
    RD* Inizializzazione POPUP
      *----------------------------------------------------------------
     C     £JAX_APOP     BEGSR
      *
      * Analisi delle informazioni
1    C                   IF        £JaxD1='' and £JaxT1<>*BLANKS
     C                   EVAL      £DECTP='OG'
     C                   EVAL      £DECCD=%TRIM(£JaxT1)+%TRIM(£JaxP1)
     C                   EXSR      £DEC
     C  N35              EVAL      £JaxD1=£DECDE
1e   C                   ENDIF
      * Scrivo la riga
     C                   EXSR      £JAX_ADDO
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Finalizzazione Griglia
     C*----------------------------------------------------------------
     C     £JAX_APOP_F   BEGSR
      *
     C                   EVAL      £JAXCP='</UIPopup>'
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Aggiungo griglia
     C*----------------------------------------------------------------
     C     £JAX_ABUT     BEGSR
      *
     C                   EVAL      £JAXCP='<Buttons>'
     C                   EXSR      £JAX_ADD
      *
1    C                   IF        £JaxBSv='1'
     C                   EVAL      £JaxCP=
     C                             '<Button '+
     C                             'Name="Salva" '+
     C                             'Status="1" '+
     C                             '/>'
     C                   EXSR      £JAX_ADD
1e   C                   ENDIF
1    C                   IF        £JaxBRn='1'
     C                   EVAL      £JaxCP=
     C                             '<Button '+
     C                             'Name="Salva con nome" '+
     C                             'Status="1" '+
     C                             '/>'
     C                   EXSR      £JAX_ADD
1e   C                   ENDIF
1    C                   IF        £JaxBDe='1'
     C                   EVAL      £JaxCP=
     C                             '<Button '+
     C                             'Name="Cancella" '+
     C                             'Status="1" '+
     C                             '/>'
     C                   EXSR      £JAX_ADD
1e   C                   ENDIF
      *
     C                   EVAL      £JAXCP='</Buttons>'
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Aggiungo riga editor
     C*----------------------------------------------------------------
     C     £JAX_ARIGE    BEGSR
      *
      * Costruzione
     C                   EVAL      £JaxCP=
     C                             '<Riga '+
     C                             'Dat="'+%TRIM(£JaxRDt)+'">'+
     C                             '<![CDATA['+%TRIM(£JaxRBf)+']]></Riga>'
      * Pulisco Key
     C                   CLEAR                   £JaxDSBut
      * In buffer
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
      *----------------------------------------------------------------
    RD* Inizializzazione Lista valori
      *----------------------------------------------------------------
     C     £JAX_ALIS_I   BEGSR
      *
     C                   EVAL      £JAXCP='<LisValori>'
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
      *----------------------------------------------------------------
    RD* Inizializzazione lista valori
      *----------------------------------------------------------------
     C     £JAX_ALIS     BEGSR
      *
      * Costruzione
     C                   EVAL      £JaxCP=
     C                             '<Val '+
     C                             'Dat="'+%TRIM(£JaxRDt)+'">'+
     C                             '<![CDATA['+%TRIM(£JaxRBf)+']]></Val>'
      * In buffer
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Finalizzazione Lista valori
     C*----------------------------------------------------------------
     C     £JAX_ALIS_F   BEGSR
      *
     C                   EVAL      £JAXCP='</LisValori>'
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Inizializzazione Buffer variabili
     C*----------------------------------------------------------------
     C     £JAX_BVAR_I   BEGSR
      *
     C****               CLEAR                   £JaxVBf
     C                   Z-ADD     0             £JaxVBfI
     C                   CLEAR                   £JaxDSVar
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Aggiungo a Buffer variabili
     C*----------------------------------------------------------------
     C     £JAX_BVAR     BEGSR
      *
1    C                   IF        £JaxVBfI<100
     C                   EVAL      £JaxVBfI=£JaxVBfI+1
     C                   EVAL      £JaxVBf(£JaxVBfI)=£JaxDSVar
     C                   CLEAR                   £JaxDSVar
1e   C                   ENDIF
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Svuoto Buffer variabili
     C*----------------------------------------------------------------
     C     £JAX_BVAR_F   BEGSR
      *
1    C                   IF        £JaxVBfI>0
     C                   EXSR      £JAX_AVAR_I
2    C                   DO        £JaxVBfI      £JAX_N50
     C                   EVAL      £JaxDSVar=£JaxVBf(£JAX_N50)
     C                   EXSR      £JAX_AVAR
2e   C                   ENDDO
     C                   EXSR      £JAX_AVAR_F
1e   C                   ENDIF
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Inizializzazione scrittura variabili
     C*----------------------------------------------------------------
     C     £JAX_AVAR_I   BEGSR
      *
     C                   EVAL      £JAXCP='<Variables>'
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Aggiungo variabile
     C*----------------------------------------------------------------
     C     £JAX_AVAR     BEGSR
      *
1    C                   IF        £JaxDSVar<>*BLANKS
      * Tipo variabile assunto: Sch
2    C                   IF        £JaxVarTip=*BLANKS OR                        COSTANTE
     C                             (£JaxVarTip<>'Sec' AND                       COSTANTE
     C                              £JaxVarTip<>'Sch' AND                       COSTANTE
     C                              £JaxVarTip<>'Ssc' AND                       COSTANTE
     C                              £JaxVarTip<>'Com' AND                       COSTANTE
     C                              £JaxVarTip<>'Loo')                          COSTANTE
     C                   EVAL      £JaxVarTip='Sch'                             COSTANTE
2e   C                   ENDIF
     C
     C                   EVAL      £JaxCP='<SetVar '+£JaxVarTip+
     C                             '.Var="'+
     C                             %TRIM(£JaxVarNam)+
     C                             '('+%TRIM(£JaxVarVal)
     C                             +')" />'
     C                   EXSR      £Jax_ADD
      *
     C                   CLEAR                   £JaxDSVar
1e   C                   ENDIF
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Finalizzazione scrittura variabili
     C*----------------------------------------------------------------
     C     £JAX_AVAR_F   BEGSR
      *
     C                   EVAL      £JaxCP='</Variables>'
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
     C*----------------------------------------------------------------
********** PREPROCESSOR COPYEND QILEGEN,£JAX_C1
********** PREPROCESSOR COPYSTART QILEGEN,£JAX_C3
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 18/12/06  V2R2    AS Creazione
     V* 20/04/07  V2R2    AS Aggiunte £JAX_FLUSH, £JAX_FIN0, £JAX_IMP0
     V* 25/02/08  V2R3    CC Aggiunta £JAX_BSETUP
     V* 12/03/08  V2R3    CC Gestione invio setup diversi per ogni tipo componente
     V* 20/03/08  V2R3    CC Elim. metodo £JAX_BSETUP. Gestione campo XS di setup fatto da LOOCUP
     V* 19/04/09  V2R3    BS Forzatura £JAX_NS='2' se JOB di emulazione
     V* 20/04/09  V2R3    BS Forzatura £JAX_NS='2' se Componente EMU
     V* 29/05/09  V2R3    AS Corretto settaggio £JAXNS se emulazione o componente EMU
     V* 22/07/09  V2R3    BS Spostato settaggio £JAXNS se emulazione o componente EMU in £JAX_IMP
     V* 14/06/09  V2R3    BS Richiamo £JAX_INZC se non eseguita nella £INIZI
     V* A£30417A  V3R2    MF Gestione multilingua - Inserimento Commenti
     V* B£30901B  V4R1    CM Gestione del Setup di Default
     V*                      La gestione è attiva solo sul componente EXB (Matrice).
     V*                      Il campo £JAXCS viene derivato dalla tabella PGM e in risalita alla
     V*                      tabella B£5, viene indicato se si vuole attivare la gestione del setup
     V*                      di default 1=Si
     V*                      Nel caso in cui il contesto sia definito dal servizio, dopo averlo
     V*                      genearato, bisogna impostare il campo £JAXCSC='1'.
     V* 05/10/16  V5R1    PEDSTE Sostituite EXCEPT con chiamate a JAJAX3
     V* 02/02/17         BMA Sostituita QRCVDTAQ £UIBDS con £UIB
     V* B£70524A  V5R1   BMA Rilascio modifiche precedenti
     V* JA80807A  V5R1   BMA Revisione gestione messaggi
     V* 02/01/20  001408 BMA Sostituzione caratteri speciali nel contesto
     V* 03/01/20  V5R1    BS Check-out 001408 in SMEDEV
     V* ==============================================================
     D*-------------------------------------------------------------------
     D* NOTA:
     D*
     D* Questa /COPY è un'estensione della £JAX_C0.
     D*
     C*----------------------------------------------------------------
    RD* Funzione Ricezione con ciclo di attesa completo (tralascio PING, chiudo lavoro ...)
     C*----------------------------------------------------------------
     C     £JAX_DTARCV   BEGSR
      *
      * imposto flag
     C                   CLEAR                   £JaxDtRc
      *
      * Resto in attesa fino a quando non ho ricevuto dei dati da processare
1    C                   DOW       £JaxDtRc=*BLANKS
2     *C                   MONITOR
      *C                   CALL      'QRCVDTAQ'
      *C                   PARM                    £JaxRC
      *C                   PARM      'SMEUPUIDQ'   £JaxLB
      *C                   PARM      31000         £JaxLU
      *C                   PARM      *BLANKS       £UibDS
      *C                   PARM      £JaxWT        £JaxWE
      * * Se errore di ricezione pulisco la DS e imposto la lunghezza a 0
2x    *C                   ON-ERROR  *ALL
      *C                   CLEAR                   £UibDS
      *C                   EVAL      £JAXLU=0
2e    *C                   ENDMON
      *
     C                   EVAL      £JaxWE=£JaxWT
     C                   EVAL      £JaxLB='SMEUPUIDQ'
     C                   EVAL      £UIBFN='RCV'
     C                   EVAL      £UIBMT=''
     C                   EVAL      £UIBDQ=£JaxRC
     C                   EVAL      £UIBLB=£JaxLB
     C                   EVAL      £UIBWE=£JaxWE
     C                   CLEAR                   £UIBDS
     C                   CLEAR                   £UIBDI_LU
     C                   EVAL      £UIBIB=' '
     C                   EXSR      £UIB
     C                   EVAL      £JaxLU=£UIBDO_LU
      *
      * Se scatta il timeout, oppure la ricezione ha dato un errore,
      * oppure ho ricevuto il messaggio di chiusura
2    C                   IF        £JAXLU = 0 OR
     C                             (£UIBMS='COM' AND £UIBPG='COL' AND
     C                             £UIBFU='F-E')
      * . Chiudo i servizi base
      * . . Elimino Coda Server/Client
     C                   EVAL      £SBM='DLTDTAQ DTAQ(SMEUPUIDQ/'+              COSTANTE
     C                             %TRIM(£JaxSC)+')'                            COSTANTE
3    C*                   MONITOR
     C                   CALL      'QCMDEXC'
     C                   PARM                    £SBM            256
     C                   PARM      256           $LGH             15 5
3x   C*                   ON-ERROR  *ALL
3e   C*                   ENDMON
      * . . Elimino Coda Client/Server
     C                   EVAL      £SBM='DLTDTAQ DTAQ(SMEUPUIDQ/'+              COSTANTE
     C                             %TRIM(£JaxRC)+')'                            COSTANTE
3    C*                   MONITOR
     C                   CALL      'QCMDEXC'
     C                   PARM                    £SBM            256
     C                   PARM      256           $LGH             15 5
3x   C*                   ON-ERROR  *ALL
3e   C*                   ENDMON
      * Se non è scattato il timeout, non c'è stato errore di ricezione e
      * non è arrivato il messaggio di chiusura
2x   C                   ELSE
      * . Se non ho ricevuto un PING, imposto l'uscita dal ciclo di attesa (ho ricevuto dati da
      *   processare)
3    C                   IF        Not(£JaxLU=4 AND £UIBDS='PING')
     C                   EVAL      £JaxDtRc='1'
3e   C                   ENDIF
2e   C                   ENDIF
1e   C                   ENDDO
      *
     C                   EVAL      £UibDS=%SUBST(£UibDS:1:£JaxLU)
      *
      * Log di controllo
1    C                   IF        T$PGMB='1'
     C*  OF              EXCEPT    £JaxLgTe
     C*                  EVAL      *INOF = *OFF
     C                   Eval      £JaxLTr = 'Ricevuto '+%Trim(£UibCM)
     C                   Eval      £JaxLLb=£JaxLU
     C                   Eval      £JaxLLc=0
2    C                   If        £JaxLU >= 140
     C                   Eval      £JaxLBu=%SubST(£UibD1:£JaxLU-139)
2x   C                   Else
2e   C                   ENDIF
     C*                  EXCEPT    £JaxLgRi
     C                   CALL      'JAJAX3'
     C                   PARM                    £Jax3Fun         10
     C                   PARM                    £Jax3Met         10
     C                   PARM                    £PDSNP
     C                   PARM      '£JaxLgRi'    £Jax3Exc         10
     C                   PARM                    £JaxDSLog
     C                   PARM                    £Jax3Msg         10
     C                   Eval      £JaxLTrU=''
     C                   Exsr      £JAX_LOG
1e   C                   ENDIF
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Flushing della coda
     C*----------------------------------------------------------------
     C     £JAX_FLUSH    BEGSR
      *
      * Se flushing abilitato e XML ancora presente nella CODA
1    C                   IF        £Jax_FlushEn='1' AND £UIBCM='CONT'
      * . Messaggio di flushing della coda
     C                   EVAL      £JaxMTxt='Some data ignored'                 COSTANTE
      *C                   EVAL      £JaxMLiv='00'
     C                   EVAL      £JaxMGra=£Jax_GraInf
     C                   EVAL      £JaxMMod=£Jax_ModHH
     C                   EVAL      £JaxMTyp='I'
     C                   EXSR      £JAX_BMES
      *
      * . Flushing della coda
2    C                   DOW       £UIBCM='CONT'
     C                   EXSR      £JAX_DTARCV
2e   C                   ENDDO
1e   C                   ENDIF
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Funzioni standard di chiusura dei servizi
     C*----------------------------------------------------------------
     C     £JAX_FIN0     BEGSR
      *
      * Cancellazione dell'eventuale XML eccedente non gestito
     C                   EXSR      £JAX_FLUSH
      * Inserimento messaggi
     C                   EXSR      £JAX_BMES_F
      * Chiusure finali (spedizione residuo XML, ecc..)
     C                   EXSR      £JAX_FIN
      *
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Funzioni standard di inizializzazione dei servizi
     C*----------------------------------------------------------------
     C     £JAX_IMP0     BEGSR
      * Creazione del contesto manuale
     C                   EVAL      £JAXCSC=''
      *
1    C                   IF        £UIBSC<>'' AND £JAXSC=' '
     C                   EXSR      £JAX_INZC
1e   C                   ENDIF
      * Impostazioni iniziali
     C                   EXSR      £JAX_IMP
     C                   EXSR      £JAX_IMPO
      * Inizializzazione messaggi
     C                   EXSR      £JAX_BMES_I
      *
     C                   ENDSR
      *
********** PREPROCESSOR COPYEND QILEGEN,£JAX_C3
********** PREPROCESSOR COPYSTART QILEGEN,£JAX_C4
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* B£30901B  V4R1    CM Creata
     V* ==============================================================
     D*-------------------------------------------------------------------
     D* OBIETTIVO
     C*----------------------------------------------------------------
    RD* Costruzione XML di setup
     D* Passare le variabili  £J15ST = Strutture e
     D*                       £J15CO = Contesto
     C*----------------------------------------------------------------
     C     £JAX_ISET     BEGSR
     C                   EVAL      £JaxCSC='1'
     C                   EVAL      £JAXCP=''
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Costruzione XML di setup
     D* Passare le variabili  £JAXCP = Immagine del setup senza nodo <Setup
     C*----------------------------------------------------------------
     C     £JAX_ASET     BEGSR
     C                   EVAL      £JAXCP=''
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Costruzione XML di setup multipli
     D* Passare le variabili  £JAXCP = Immagine del setup senza nodo <Setup
     C*----------------------------------------------------------------
     C     £JAX_ASETM    BEGSR
     C                   EVAL      £JAXCP=''
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Costruzione XML per disabilitare dal pop.up il setup di default
     D* Passare le variabili  £J15ST = Strutture e
     D*                       £J15CO = Contesto
     D* Disabilita la gestione del setup tramite J15
     C*----------------------------------------------------------------
     C     £JAX_DSET     BEGSR
     C                   EVAL      £JaxCSC='3'
     C                   EVAL      £JAXCP=''
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Costruzione XML del setup di default
     D* Passare le variabili  £J15ST = Strutture e
     D*                       £J15CO = Contesto
     D* Disabilita la gestione del setup tramite J15
     C*----------------------------------------------------------------
     C     £JAX_USET     BEGSR
     C                   EVAL      £JaxCSC='3'
     C                   ENDSR
     C*----------------------------------------------------------------
    RD* Costruzione XML per disabilitare la comunicazione in JAX_FIN0
     C*----------------------------------------------------------------
     C     £JAX_DSEP     BEGSR
     C                   EVAL      £JaxCSC='3'
     C                   EVAL      £JAXCP=''
     C                   ENDSR
********** PREPROCESSOR COPYEND QILEGEN,£JAX_C4
********** PREPROCESSOR COPYSTART QILEGEN,£JAX_C9
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 13/12/06  V2R2    AS Aggiunte istruzioni per compilazione condizionale
     V* 02/02/17         BMA Aggiunta £UIBDO a entry servizi
     V* B£70524A  V5R1   BMA Rilascio modifiche precedenti
     V* ==============================================================
     D*-------------------------------------------------------------------
     D* OBIETTIVO
     D*
     D* Esternizzare le funzioni di prepazazione stringa XML
     D*
     D* Prerequisiti
     D*
     C*----------------------------------------------------------------
    RD* Parametri in ingresso
     C*----------------------------------------------------------------
     C     £JAX_INZP     BEGSR
     C
     C     *ENTRY        PLIST
     C                   PARM                    £UIBDS
     C                   PARM                    JAT$DS
     C                   PARM                    £UibPR
     C                   PARM                    £UibSU
     C                   PARM                    £UIBDO
     C
     C                   ENDSR
********** PREPROCESSOR COPYEND QILEGEN,£JAX_C9
********** PREPROCESSOR COPYEND QILEGEN,£JAX_C
** SWKESE
XXESE1    Colonna 1                    AR                    15      ASSE
XXESE2    Colonna 2                    NR                    12      SERIE
XXESE3    Colonna 3                    TABRE                H05
XXESE4    Colonna 4                    [XXESE3]              999     SERIE
XXESE5    Colonna 5                    [XXESE3]|[XXESE4]     15
