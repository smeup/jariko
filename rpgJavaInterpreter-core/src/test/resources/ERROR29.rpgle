      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : D5_091_04
      * Sorgente di origine : SMEUP_DEV/JASRC(D5_091_04)
      * Esportato il        : 20240719 094159
      *====================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 04/09/20  V5R1    PC Creato
     V* 08/09/20  V5R1    LANKAT Rinominato da *_03 a *_04
     V* 16/09/20  PER_SUP LANKAT Rilascio da PER_SUP
     V* 26/11/20          BS Prevista funzione/metodo per elaborazione KPI
     V* 10/12/20          BS Rettifica a modifica precedente
     V* 21/12/20          BS Introdotto utilizzo £D5_139
     V* 03/09/21          LANKAT Modifica B£4
     V* ££10916B  V5R1    BS *************************
     V* ££10916B  V5R1    BS **************** Rilascio intero sorgente da srvamm a SMEDEV
     V* ££10916B  V5R1    BS *************************
     V* 15/03/22  003618  LANKAT Modifica reperimento date modello nella B£4
     V* 16/03/22  V6R1    BERNI Check-out 003618 in SMEUP_TST
     V* 16/05/22  003827  BS Ricompilato per modifica £D5C
     V* 17/05/22  V6R1    BERNI Check-out 003827 in SMEDEV
     V* ==============================================================
      * Parametri di INPUT(:
     V* ==============================================================
      * Obiettivo
      *
      * ERRORI
      *
      *
      * TO DO
      *
      *
      * SVILUPPI
      *
      *
      * TEST
      *
     D*--------------------------------------------------------------------------------------------*
     D* D5_091_03: Bowling Chart
     D*--------------------------------------------------------------------------------------------*
********** PREPROCESSOR COPYSTART QILEGEN,£INIZH
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £INIZH
      * Sorgente di origine : SMEDEV/QILEGEN(£INIZH)
      * Esportato il        : 20240719 095001
      *====================================================================
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
     H DECEDIT(*JOBRUN) DATEDIT(*DMY/) DFTACTGRP(*NO) ACTGRP(*CALLER)
     H OPTION(*NODEBUGIO:*SRCSTMT:*NOUNREF) CCSID(*CHAR : *JOBRUN) DEBUG(*INPUT)
********** PREPROCESSOR COPYEND QILEGEN,£INIZH
      *--------------------------------------------------------------------------------------------*
     FD5COSO4L  IF   E           K DISK
      *--------------------------------------------------------------------------------------------*
********** PREPROCESSOR COPYSTART QILEGEN,£JAX_D

     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 20/03/24  KOKOS   BERNI Riportata /copy da SMEDEV, tolte G61, G64 e J15
     V* ==============================================================
********** PREPROCESSOR COPYSTART QILEGEN,£JAX_D0
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* 20/03/24  KOKOS   BERNI Riportata dalla SMEDEV
     V* 21/03/24          BERNI Modificati tipi Z non gestiti
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
     D £JaxWT          S                   LIKE(£JaxWE)
      *
      * Dati ricevuti (non PING o messaggi di chiusura)
     D £JaxDtRc        S              1
      *
      * Abilitazione allo svuotamento della coda
     D £Jax_FlushEn    S              1
      *
     D £JaxDSGen       DS
     D £JaxCP                    999999    INZ VARYING
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
     D £JaxLSi                       26    INZ
     D £JaxLSt                       26    INZ
     D £JaxLEt                       26    INZ
     D £JaxLAt                       10  0 INZ
     D £JaxLCo                       40    INZ
      * Non riportare campo INPUT in Attributo Funzione dell'Header XML
     D £JAXNI          S              1
      * Campo di appoggio per la gestione del campo INPUT in Attributo Funzione dell'Header XML
     D £JAXNI_POS      S              5  0
      *
      * Tempi esecuzione Funzione
     D £JaxTimST       S             26    INZ                                  Tempo iniziale
     D £JaxTimEN       S             26    INZ                                  Tempo finale
     D £JaxTimDF       S             30  0 INZ                                  Differenza tempi
      * Indicatore per segnalare il passaggio nella £JAX_INZ
     D £JaxTimIN       S              1    INZ
********** PREPROCESSOR COPYEND QILEGEN,£JAX_D0
********** PREPROCESSOR COPYSTART QILEGEN,£JAX_D1
      *IF NOT DEFINED(JAX_D1_INCLUDED)
      *DEFINE JAX_D1_INCLUDED
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 20/03/24  KOKOS   BERNI Riportato da SMEDEV
     V* ==============================================================
********** PREPROCESSOR COPYSTART QILEGEN,£K04DS
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £K04DS
      * Sorgente di origine : SMEDEV/QILEGEN(£K04DS)
      * Esportato il        : 20240719 094537
      *====================================================================
      *IF NOT DEFINED(K04DS_INCLUDED)
      *DEFINE K04DS_INCLUDED
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
     V* 04/08/21  003109  BMA Aggiunto £K04O_TT Presenza tooltip
     V* 12/08/21  V5R1   AS Check-out 003109 in SMEUP_TST
     V* ££10916A  V5R1    BERNI **********************
     V* ££10916A  V5R1    BERNI ************* RILASCIO INTERO SORGENTE DA SMEUP_TST A SMEDEV
     V* ££10916A  V5R1    BERNI **********************
     V* 11/06/24  005759  PARLUC Aggiunta campi componenti per device
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
     D £K04O_CR                      52                                         Classe deviazione
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
     D £K04O_LC                       3                                         C.Ricerca Loocup
     D £K04O_WC                       3                                         C.Ricerca Web
     D £K04O_MC                       3                                         C.Ricerca Mobile
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
Codice*   Descrizione                  Oggetto              HLengFRComandi                  Au
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
Codice*   Descrizione                         -
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
      * Presenza tooltip
     D  £JAX3TT                       1                                         V2ONOFF
      *
      * Messaggi
     D £JaxMBf         S                   DIM(100) LIKE(£JaxDSMsg)             Schiera buffer msg
     D £JaxMBfI        S              4  0 INZ                                  Contatore messaggi
     D £JaxDSMsg       DS                                                       DS Messaggi
     D £JaxMLiv                      02    INZ                                  . livello (00-99)
     D £JaxMTxt                     198    INZ                                  . testo
      * . Tipo messaggio (I=INFO, C=CONF, Q=QUEST)
     D £JaxMTyp                      01    INZ                                  . tipo messaggio
     D £JaxMT1                       02    INZ                                  . tipo oggetto 1
     D £JaxMP1                       10    INZ                                  . param. oggetto 1
     D £JaxMK1                       15    INZ                                  . codice oggetto 1
     D £JaxMT2                       02    INZ                                  . tipo oggetto 2
     D £JaxMP2                       10    INZ                                  . param. oggetto 2
     D £JaxMK2                       15    INZ                                  . codice oggetto 2
      * . Modalità V2MSMOD
      * .. TN = Notifica Temporanea (a scomparsa)
      * .. PN = Notifica Permanente (non a scomparsa)
      * .. PM = Messaggio Permanente (modale)
      * .. HH = Messaggio nascosto (non viene emesso)
     D £JaxMMod                      02    INZ
      * . Gravità  V2MSGRA
      * .. INFO
      * .. WARNING
      * .. ERROR
     D £JaxMGra                      10    INZ
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
     D £Jax_LvlInf     C                   '00'                                 Liv. msg. info
     D £Jax_LvlWrn     C                   '40'                                 Liv. msg. warning
     D £Jax_LvlErr     C                   '70'                                 Liv. msg. errore
     D £JaxMaxLen      C                   '30000'
     D £JaxMaxStr      C                   '*HI'
     D £Jax_GraInf     C                   'INFO'                               msg. info
     D £Jax_GraWrn     C                   'WARNING'                            msg. warning
     D £Jax_GraErr     C                   'ERROR'                              msg. errore
     D £Jax_ModHH      C                   'HH'                                 Messaggio nascosto
     D £Jax_ModTN      C                   'TN'                                 Notifica temporanea
     D £Jax_ModPN      C                   'PN'                                 Notifica permanente
     D £Jax_ModPM      C                   'PM'                                 Messaggio permanente
      * Messaggi in sezione
      * . Permanente modale in sezione
     D £Jax_ModPT      C                   'PT'
      * . Permanente in sezione (da chiudere)
     D £Jax_ModPS      C                   'PS'
      * . Temporaneo in sezione (a scomparsa)
     D £Jax_ModTS      C                   'TS'
      *ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£JAX_D1
********** PREPROCESSOR COPYSTART QILEGEN,£JAX_D4
      *====================================================================
      * smeup V6R1.020DV
      * Nome sorgente       : £JAX_D4
      * Sorgente di origine : QTEMP/QILEGEN(£JAX_D4)
      * Esportato il        : 20240320 155126
      *====================================================================
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
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £TABPGMDS
      * Sorgente di origine : SMEDEV/QILEGEN(£TABPGMDS)
      * Esportato il        : 20240719 094520
      *====================================================================
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
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £TABB£5DS
      * Sorgente di origine : SMEDEV/QILEGEN(£TABB£5DS)
      * Esportato il        : 20240719 094219
      *====================================================================
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
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £TABUI1DS
      * Sorgente di origine : SMEDEV/QILEGEN(£TABUI1DS)
      * Esportato il        : 20240719 094220
      *====================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*===============================================================
     V* 25/11/22  V6R1    BUSFIO Aggiunto campo Disttiva tempo FUN
     V*===============================================================
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
      * N° 09 - Disattiva Tempo FUN  V2 SI/NO       1
     D  T$UI1F                22     22
********** PREPROCESSOR COPYEND QILEGEN,£TABUI1DS
********** PREPROCESSOR COPYSTART QILEGEN,£UIBDS
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 10/05/21  V5R1    BONMAI Creazione
     V* 18/05/24  V6R1    FORDAR Estensione
     V* /COPY £UIBDS
     V* ==============================================================
      *--------------------------------------------------------------------------------------------*
      *IF NOT DEFINED(UIBDS_INCLUDED)
      *DEFINE UIBDS_INCLUDED
     D £UIBDS          DS            10
     D £UIBPG                        10A
     D £UIBFU                          0
     D £UIBME                          0
     D £UIBT1                          0
     D £UIBP1                          0
     D £UIBK1                          0
     D £UIBT2                          0
     D £UIBP2                          0
     D £UIBK2                          0
     D £UIBT3                          0
     D £UIBP3                          0
     D £UIBK3                          0
     D £UIBPA                          0
     D £UIBT4                          0
     D £UIBP4                          0
     D £UIBK4                          0
     D £UIBT5                          0
     D £UIBP5                          0
     D £UIBK5                          0
     D £UIBT6                          0
     D £UIBP6                          0
     D £UIBK6                          0
     D £UIBSR                          0
     D £UIBCL                          0
     D £UIBRI                          0
     D £UIBSG                          0
     D £UIBSS                          0
     D £UIBSQ                          0
      *
     D £UIBSR                          0
     D £UIBCL                          0
     D £UIBRI                          0
     D £UIBSG                          0
     D £UIBSS                          0
     D £UIBSQ                          0
      *
     D £UIBPR                          0
     D £UIBPW                          0
     D £UIBSW                          0
      *
     D £UIB35                          0
     D £UIB36                          0
     D £UIBCM                          0
     D £UIBFM                          0
     D £UIBSC                          0
      *
     D £UIBD1                          0
      *ENDIF
      *--------------------------------------------------------------------------------------------*
********** PREPROCESSOR COPYEND QILEGEN,£UIBDS
********** PREPROCESSOR COPYSTART QILEGEN,£UICDS
      *====================================================================
      * smeup V6R1.020DV
      * Nome sorgente       : £UICDS
      * Sorgente di origine : QTEMP/QILEGEN(£UICDS)
      * Esportato il        : 20240320 155127
      *====================================================================
      *IF NOT DEFINED(UICDS_INCLUDED)
      *DEFINE UICDS_INCLUDED
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
      *ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£UICDS
********** PREPROCESSOR COPYSTART QILEGEN,£G96DS
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £G96DS
      * Sorgente di origine : SMEDEV/QILEGEN(£G96DS)
      * Esportato il        : 20240719 094515
      *====================================================================
     V*================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 14/06/07  V2R2    CM Creato                 T
     D*----------------------------------------------------------------
     D* OBIETTIVO
     D* Momenti di un Servizio
     D*----------------------------------------------------------------
     D £G96DS          DS           512    INZ
     D  £G96TP                       10
     D  £G96IS                       10
     D  £G96MO                       30
     D  £G96DI                        8  0
     D  £G96HI                        9  0
     D  £G96DF                        8  0
     D  £G96HF                        9  0
     D  £G96MM                       10  0
     D  £G96NS                       10  0
     D  £G96ST                       30
     D  £G96TT                        1
     D  £G96IN                        1
     D £G96SI          S           1000    VARYING
      *
     D £G96WK          S            512
      *
     D £G96ISER        DS
     D  £G96ICOMP                    10
     D  £G96ISERV                    10
     D  £G96IFUNZ                    15
     D  £G96IOGG1                    27
     D  £G96IOGG2                    27
     D  £G96IOGG3                    27
     D  £G96IOGG4                    27
     D  £G96IOGG5                    27
     D  £G96IOGG6                    27
     D  £G96IPARA                   256
********** PREPROCESSOR COPYEND QILEGEN,£G96DS
********** PREPROCESSOR COPYSTART QILEGEN,£B£UT67DS
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £B£UT67DS
      * Sorgente di origine : SMEDEV/QILEGEN(£B£UT67DS)
      * Esportato il        : 20240719 094345
      *====================================================================
     V*=====================================================================
     V* MODIFICHE Ril.  T Au  Descrizione
     V* gg/mm/aa  nn.mm i xx  Breve descrizione
     V*=====================================================================
     V* 13/08/18  000106  BMA Creazione
     V* 16/08/18  V5R1    CM Check-out 000106 Rilasciato in SMEUP_TST
     V* 25/09/18  V5R1    ZS Rilascio 000106
     V*=====================================================================
     D SCHCHK          S              1C   DIM(100)  ccsid(1200)
     D SCHSOS          S              1C   DIM(100)  ccsid(1200)
********** PREPROCESSOR COPYEND QILEGEN,£B£UT67DS
********** PREPROCESSOR COPYSTART QILEGEN,£JAX_PD1
      *IF NOT DEFINED(JAX_PD1_INCLUDED)
      *DEFINE JAX_PD1_INCLUDED
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*===============================================================
     V* 14/12/06  V2R2    AS Aggiunte istruzioni per compilazione condizionale
     V* 20/04/07  V2R2    AS Aggiunti due parametri opzionali a RxELE
     V* 31/05/07  V2R2    GR/AS Estensione campi da 256 a 2560
     V* 31/07/07  V2R3    AS Rilascio modifiche del 31/05/07
     V* 28/01/09  V2R3    AS Aggiunto parametro opzionale a RxSOS per gestione HTML
     V* 19/03/09  V2R3    BS Aggiunto parametro opzionale a RxATT per verifica individuazione
     V* 16/04/09  V2R3    BS Portate variabili RxSOS da 2560 a 30000
     V* 20/07/09  V2R3    BMA Portate variabili RxVAL da 2560 a 30000
     V* 18/09/09  V2R3    AMA Aggiunte (temporaneamente) le P_RxVALNew e P_RxATVNew
     V* 30/04/10  V2R3    AS Inserita gestione RxSOSNew
     V* 19/07/10  V3R1    AS Rilasciata RxSOSNew come RxSOS
     V* 15/10/10  V3R1    GR P_RxSPL per split colonne matrice
     V* 19/11/10  V3R1    BMA Portata variabile ritorno RxLate da 30000 a 32766
     V* 16/12/10  V3R21   RM Aggiunto parametro opzionale a RxELE
     V* 12/04/11  V3R1   BMA RxATP per restituzione lista attributi e valori elementi con parentesi
     V* 01/12/11  V3R2    AS Portato output RxATT a 30000 (il campo interno lo era già)
     V* 23/02/12  V3R2    BS Aggiunto Parametro Opzione RxATT per controllo livello 1
     V* 20/06/12  V3R2    CM Gestione stringa URL
     V* 22/12/12        ES05 Aggiunto parametro opzionale a RxLate per sostituzione singola
     V* 06/12/12  V3R2  ES05 Rilasciata modifica del 22/12/12
     V* 04/11/15  V4R1   BMA Aggiunto parametro opzionale a RxSOC per gestione HTML e Pipe
     V* 26/05/16  V4R1    BS Su XLATE aggiunto parametro per non distinguire minuscole/maiuscole
     V* 07/10/16  V5R1   BMA Aggiunto campo RxEle per indicare di cercare il tag anche in un CDATA
     V* 16/11/16  V5R1   BMA RxEle: Livelli di chiamata 48-50 riservati £JAY
     V* 16/06/17          BMA Portata variabile ritorno RxSos da 32766 a 65000 e input da 30k a 35k
     V* 19/06/17  V5R1   BMA Rilasciata modifica precedente
     V*===============================================================
      * Variabili globali delle procedure
     D £JSP            S              7  0 DIM(50)
      *--------------------------------------------------------------*
     D                 DS
     D£JAXATV                       306    DIM(200)
     D £JAXATV_A                     50    OVERLAY(£JAXATV:1)
     D £JAXATV_V                    256    OVERLAY(£JAXATV:*NEXT)
     D                 DS
     D£JAXATP                      1055    DIM(200)
     D £JAXATP_A                     50    OVERLAY(£JAXATP:1)
     D £JAXATP_V                   1000    OVERLAY(£JAXATP:*NEXT)
     D £JAXATP_L                      5  0 OVERLAY(£JAXATP:*NEXT)
      *--------------------------------------------------------------*
      * Restituisce il valore di un'attributo dei TAG
     DP_RxVAL          PR         65000    VARYING
     D $XmlTag                    65000    CONST VARYING
     D $XmlAtt                       64    CONST
      *
      * Restituisce il valore di un'attributo di tipo aaa(
     DP_RxATT          PR         30000    VARYING
     D $XmlTag                    30000    CONST VARYING
     D $XmlATT                       64    CONST VARYING
     D $XmlASS                       15    CONST
     D $XmlFND                        1N   OPTIONS(*NOPASS)
     D $XmlLIV                        1    OPTIONS(*NOPASS)
      *
      * Sostituzione dei caratteri non validi PER XML
     DP_RxSOS          PR         65000    VARYING
     D $XmlSOS                    35000    CONST VARYING
     D $StrLang                       1    CONST OPTIONS(*NOPASS)
      *
      * Sostituzione stringa in carattere
     DP_RxSOC          PR         30000    VARYING
     D $XmlSOC                    30000    CONST VARYING
     D $StrLang                       1    CONST OPTIONS(*NOPASS)
      *
      * Sostituzione dei caratteri non validi PER URL
     DP_RxURL          PR         30000    VARYING
     D $XmlURL                    30000    CONST VARYING
      *
      * Trasformazione Stringa
     DP_RxLATE         PR         32766    VARYING
     D $XmlINP                    30000    CONST VARYING
     D $XmlS01                    30000    CONST VARYING
     D $XmlS02                    30000    CONST VARYING
     D $SosSing                       1    CONST OPTIONS(*NOPASS)
     D $SosCase                       1    CONST OPTIONS(*NOPASS)
      *
      * Restituisce il contenuto dell'elemento richiesto
     DP_RxELE          PR         65000    VARYING
     D Xml_Fnd                      512    VARYING CONST
     D Xml_Met                       10    CONST
      * .livello di chiamata
      * ... livelli 48-50 riservati £JAY
     D Xml_Liv                        2  0 CONST
     D Xml_Str                    65000    VARYING CONST
     D NodIni                         5  0 OPTIONS(*NOPASS)
     D NodLen                         5  0 OPTIONS(*NOPASS)
     D Xml_Con                    65000    OPTIONS(*NOPASS) VARYING
      * . Cerca tag anche all'interno di un CDATA
     D SEACDATA                       1    OPTIONS(*NOPASS)
      *
      * Restituisce la lista degli attributi e dei valori di un elemento
     DP_RxATV          PR           306    DIM(200)
     D Xml_Str                    30000    VARYING
     D  $XmlPRG                       5  0 OPTIONS(*NOPASS)
      *
      * Restituisce la lista degli attributi e dei valori di un elemento di documentazione attiva
     DP_RxATP          PR          1055    DIM(200)
     D Xml_StrP                   30000    VARYING
     D  $XmlPRGp                      5  0 OPTIONS(*NOPASS)
      *
      * Restituisce una stringa (es. intestazione matrice) splittata su più righe
     DP_RxSPL          PR         30000    VARYING
     D $String                    30000    CONST VARYING
     D $Split                         1    CONST
      *ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£JAX_PD1
********** PREPROCESSOR COPYEND QILEGEN,£JAX_D
********** PREPROCESSOR COPYSTART QILEGEN,£TABJATDS
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £TABJATDS
      * Sorgente di origine : SMEDEV/QILEGEN(£TABJATDS)
      * Esportato il        : 20240719 095037
      *====================================================================
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
      *IF NOT DEFINED(£TABB£1DS)
      *DEFINE £TABB£1DS
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
      *ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£TABB£1DS
********** PREPROCESSOR COPYSTART QILEGEN,£TABB£4DS
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £TABB£4DS
      * Sorgente di origine : SMEDEV/QILEGEN(£TABB£4DS)
      * Esportato il        : 20240719 094426
      *====================================================================
      *      DATE PARTICOLARI
     D B£4$DS          DS           100
      * N° 03 - Forzat.particolari   V         1
     D  T$B£4A                 1      1
      * N° 04 - Tipo comparazione    V2 OPC02  2
     D  T$B£4B                 2      3
      * N° 05 - Data manuale 01      A8        8
     D  T$B£4C                 4     11
      * N° 06 - "            02      A8        8
     D  T$B£4D                12     19
      * N° 07 - Utente aggiornamento UP       10
     D  T$B£4E                20     29
      * N° 08 - Data       "         A8        8
     D  T$B£4F                30     37
      * N° 09 - Ora        "         I1 2      6
     D  T$B£4G                38     43
********** PREPROCESSOR COPYEND QILEGEN,£TABB£4DS
********** PREPROCESSOR COPYSTART QILEGEN,£TABD5SDS
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £TABD5SDS
      * Sorgente di origine : SMEDEV/QILEGEN(£TABD5SDS)
      * Esportato il        : 20240719 094426
      *====================================================================
     D*      COSTI PER OGGETTO
     D D5S$DS          DS
      * N° 03 - S/S Temi costo ogg.  SS D5O    2
     D  T$D5SA                 1      2
********** PREPROCESSOR COPYEND QILEGEN,£TABD5SDS
********** PREPROCESSOR COPYSTART QILEGEN,£TABD5ODS
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £TABD5ODS
      * Sorgente di origine : SMEDEV/QILEGEN(£TABD5ODS)
      * Esportato il        : 20240719 094426
      *====================================================================
      *      Temi cose oggetto
     D D5O$DS          DS           100
      * N° 03 - Codice 1             TA *CNTT       2
     D  T$D5O1                 1      2
      * N° 04 - Parametro codice 1   **            10
     D  T$D5OA                 3     12
      * N° 05 - Codice 2             TA *CNTT       2
     D  T$D5O2                13     14
      * N° 06 - Parametro codice 2   **            10
     D  T$D5OB                15     24
      * N° 07 - Codice 3             TA *CNTT       2
     D  T$D5O3                25     26
      * N° 08 - Parametro codice 3   **            10
     D  T$D5OC                27     36
      * N° 09 - Suf.Pgm.Spec.D5CO_xx V3 PID         2
     D  T$D5OF                37     38
      * N° 10 - S/S indici (Se STD)  SS IGI         2
     D  T$D5OD                39     40
      * N° 11 - Data/Periodo                        1
     D  T$D5OE                41     41
      * N° 12 - Tipo periodo         TA *CNPE       1
     D  T$D5OG                42     42
      * N° 13 - Punti sep./decimali  **             2
     D  T$D5OH                43     44
      * N° 14 - Programma aggiust.   **             1
     D  T$D5OI                45     45
      * N° 15 - Parametro Pgm.Spe(SV               20
     D  T$D5OP                46     65
********** PREPROCESSOR COPYEND QILEGEN,£TABD5ODS
********** PREPROCESSOR COPYSTART QILEGEN,£D5_139DS
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £D5_139DS
      * Sorgente di origine : SMEDEV/QILEGEN(£D5_139DS)
      * Esportato il        : 20240719 094424
      *====================================================================
     V*================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 20/12/20  V5R1    BS Creazione
     V* 21/12/20  V5R1    BS Aggiunta variabile di INPUT SK
     V* ££10916B  V5R1    BS *************************
     V* ££10916B  V5R1    BS **************** Rilascio intero sorgente da srvamm a SMEDEV
     V* ££10916B  V5R1    BS *************************
     V* ==============================================================
      * Input
     D £139DSI         DS          2000
     D  £139I_FU                     10                                         Funzione
     D  £139I_ME                     10                                         Metodo
     D  £139I_TP                     12                                         Tipo Oggetto
     D  £139I_CD                     15                                         Codice Oggetto
     D  £139I_SK                     15                                         Singolo KPI
      *
      * Output
     D £139DSO         DS          2000
     D  £139O_MS                      7                                         Messaggio
     D  £139O_FI                     10                                         File
     D  £139O_VM                    100                                         Variabili Messaggio
     D  £139O_35                      1N                                        Errore
     D  £139O_NK                      4P 0                                      N° Elementi Schiera
      *
     D £139DSK         DS
     D  £139K_SK                   4000    DIM(999)
     D  £139K_TP                     12    OVERLAY(£139K_SK:01)                 Tipo
     D  £139K_CD                     15    OVERLAY(£139K_SK:*NEXT)              Codice
     D  £139K_DE                    256    OVERLAY(£139K_SK:*NEXT)              Descrizione
     D  £139K_DA                     15    OVERLAY(£139K_SK:*NEXT)              Dashboard
     D  £139K_FN                     02    OVERLAY(£139K_SK:*NEXT)              Fonte
     D  £139K_ST                     03    OVERLAY(£139K_SK:*NEXT)              Struttura
     D  £139K_SC                     15    OVERLAY(£139K_SK:*NEXT)              Scheda
     D  £139K_PF                    999    OVERLAY(£139K_SK:*NEXT)              Parametri fonte
     D  £139K_FU                    256    OVERLAY(£139K_SK:*NEXT)              Funzione
     D  £139K_TF                     12    OVERLAY(£139K_SK:*NEXT)              Filler
     D  £139K_CF                     15    OVERLAY(£139K_SK:*NEXT)              Filler
     D  £139K_CL                    999    OVERLAY(£139K_SK:*NEXT)              Colonna
     D  £139K_IS                     15    OVERLAY(£139K_SK:*NEXT)              Immagine Sketch
     D  £139K_SE                    512    OVERLAY(£139K_SK:*NEXT)              Setup Componente
      *
********** PREPROCESSOR COPYEND QILEGEN,£D5_139DS
********** PREPROCESSOR COPYSTART QILEGEN,£FUNDS1
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £FUNDS1
      * Sorgente di origine : SMEDEV/QILEGEN(£FUNDS1)
      * Esportato il        : 20240719 094542
      *====================================================================
      *IF NOT DEFINED(FUNDS_INCLUDED)
      *DEFINE FUNDS_INCLUDED
     D* ==============================================================
     D* MODIFICHE Ril.  T Au Descrizione
     D* gg/mm/aa  nn.mm i xx Breve descrizione
     D* ==============================================================
     D* 16/06/00  03.00   GG Il campo FUNS3 finiva a posizione 256
     D* 16/06/00  03.00   GG Il campo FUNK6 iniz.  a posizione 326
     D* 11/12/00  04.00   GG Agginto gruppo azioni (B£H)
     D* 27/05/04  V2.R1   PC Aggiunte righe di separazione
     D*                      Aggiunte attivazione
     D* 11/03/09  V2R3    DFA Aggiunto campo x Stato Esecuzione ( B£J )
     D* 21/11/09  V2R3  i BS Aggiunte Istruzioni di Controllo Inclusione
     D* 04/01/12  V3R2    MA Modificata descrizione campo £FUNFT
     D* 05/07/17  V5R1    BS Aggiunto £FUNIF
      *----------------------------------------------------------------
     D £FUNW1          DS           512
     D £FUNW2          DS           512
      *----------------------------------------------------------------
/I   D £FUND1          DS           512    INZ
      * Contesto (V5MOVI / GMCONF / Ecc)
     D  £FUNCO                 1     10
      * Numero relativo di record
     D  £FUNNR                11     19  0
      * Griglia (Tabella B£G)
     D  £FUNGR                20     22
      * Tipo Codice 1
     D  £FUNT1                23     25
      * Par. Codice 1
     D  £FUNP1                26     35
      * Codice 1
     D  £FUNK1                36     50
      * Intestaz. Codice 1
     D  £FUNI1                51     65
      * Descriz. Codice 1
     D  £FUNS1                66    100
      * Tipo Codice 2
     D  £FUNT2               101    103
      * Par. Codice 2
     D  £FUNP2               104    113
      * Codice 2
     D  £FUNK2               114    128
      * Intestaz. Codice 2
     D  £FUNI2               129    143
      * Descriz. Codice 2
     D  £FUNS2               144    178
      * Tipo Codice 3
     D  £FUNT3               179    181
      * Par. Codice 3
     D  £FUNP3               182    191
      * Codice 3
     D  £FUNK3               192    206
      * Intestaz. Codice 3
     D  £FUNI3               207    221
      * Descriz. Codice 3
     D  £FUNS3               222    255
      * Codice 4
     D  £FUNK4               256    270
      * Intestaz. Codice 4
     D  £FUNI4               271    285
      * Codice 5
     D  £FUNK5               286    300
      * Intestaz. Codice 5
     D  £FUNI5               301    315
      * Codice 6
     D  £FUNK6               316    330
      * Intestaz. Codice 6
     D  £FUNI6               331    345
      * Parametro di passaggio funzioni
     D  £FUNP4               346    346
      * Tabella NSC
     D  £FUN0A               347    349
      * PARAMETRI Tabella C£E
     D  £FUN0B               350    352
      * LISTINI   Area listini     (SSC£L)
     D  £FUN0C               353    354
      * LISTINI   Capitolo listini (C£L+SS)
     D  £FUN0D               355    357
      * LISTINI   Sezione listini  (C£V+SS)
     D  £FUN0E               358    360
      * LEGAMI    Tipo Legami      (C£U)
     D  £FUN0F               361    365
      * DOM/CONF. Tipo Domanda     (C£B)
     D  £FUN0G               366    370
      * REGOLE    Tipo Regola      (C£A)
     D  £FUN0H               371    373
      * SVILUPPO  Tipo Sviluppo    (C£Q)
     D  £FUN0I               374    376
      * RISORSE   Tipo Risorsa     (TRG)
     D  £FUN0L               377    379
      * COSTI     Tipo Costo       (TCO)
     D  £FUN0M               380    382
      * DISTINTE  Tipo Distinta    (BRL)
     D  £FUN0N               383    385
      * T.CICLI   Tipo Ciclo       (BRT)
     D  £FUN0O               386    388
      * CONTATTI  Tipo Contatto    (BRE)
     D  £FUN0P               389    391
      * PARAMETRO per colloquio tra funzioni
     D  £FUNX1               392    392
      * Parametri specifici (LDA della B£J)
     D  £FUNPS               393    432
      * Indicatore 35
     D  £FUN35               433    433N
      * Indicatore 36
     D  £FUN36               434    434N
      * Data (AAAAMMGG)
     D  £FUNDT               435    442  0
      * Quantità  (15.5)
     D  £FUNQT               443    457  5
      * Sottosettore B£J
     D  £FUNJS               458    459
      * Elemento B£J
     D  £FUNJE               460    464
      * PARAMETRO per colloquio tra funzioni (lancio da  B£AR30)
     D  £FUNX2               465    465
      * Vincolo 1 B£J
     D  £FUNV1               466    470
      * Parametro oggetto principale
     D  £FUNPO               471    480
      * Periodo
     D  £FUNPE               481    490
      * Cond. di esecuzione
     D  £FUNV2               491    491
      * Elemento B£H
     D  £FUNJH               492    501
      * Attivazione
     D  £FUNAT               502    502
      * Righe di separazione
     D  £FUNRS               503    503  0
      * Tipo di flusso di transazione (V5) e di impostazioni specifiche
      * flussi oggetti (1-DO ecc.)
     D  £FUNFT               504    504
      * Stato esecuzione  ( B£J )
     D  £FUNES               505    505
      * Chiamata da componente IFL
     D  £FUNIF               506    506
      *
      *
      * Campo di sicurezza per prevenire CPF
     D  £FUNQ1               443    443
      *
      *----------------------------------------------------------------
     D £FUND2          DS           512    INZ
      *----------------------------------------------------------------
      *ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£FUNDS1
********** PREPROCESSOR COPYSTART QILEGEN,£PE8DS
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £PE8DS
      * Sorgente di origine : SMEDEV/QILEGEN(£PE8DS)
      * Esportato il        : 20240719 094436
      *====================================================================
     D*===============================================================
     D* MODIFICHE Ril.  T Au Descrizione
     D* gg/mm/aa  nn.mm i xx Breve descrizione
     D*===============================================================
     D* 13/03/99          MA Aggiunto £PE8DE
     D* 24/07/00          PV Aggiunto £PE8TP
     D* 11/12/02  05.00   BS Aggiunto £PE8AZ
     D* 24/07/04  V2R1    BS Aggiunto £PE8SO
     D*===============================================================
     D* Definizione campi DS
     D*              £PE8EI Esercizio input (anno)
     D*              £PE8PI Periodo input
     D*              £PE8DT Data di calcolo
     D*              £PE8EO Esercizio output (anno)
     D*              £PE8PO Periodo output
     D*              £PE8DI Data iniziale periodo
     D*              £PE8DF Data finale periodo
     D*              £PE8DE Descrizione periodo
     D*              £PE8TP Tipo periodo
     D*              £PE8AZ Azienda
     D*              £PE8SO Anno solare
      *----------------------------------------------------------------
     D*
     D*  DS Relativa periodo ed esercizio contabile
     D*
      *----------------------------------------------------------------
     D £PE8DS          DS           512
     D  £PE8EI                01     04
     D  £PE8PI                05     14
     D  £PE8DT                15     22  0
     D  §PE8DT                15     22
     D  £PE8EO                23     26
     D  £PE8PO                27     36
     D  £PE8DI                37     44  0
     D  £PE8DF                45     52  0
     D  £PE8DE                53     82
     D  £PE8TP                83     83
     D  £PE8AZ                84     85
     D  £PE8SO                86     86
********** PREPROCESSOR COPYEND QILEGEN,£PE8DS
********** PREPROCESSOR COPYSTART QILEGEN,£G43DS
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £G43DS
      * Sorgente di origine : SMEDEV/QILEGEN(£G43DS)
      * Esportato il        : 20240719 094555
      *====================================================================
     D*----------------------------------------------------------------
     D*
     D*  DS x Gestione Compos./Scompos. file CSV
     D*
     D*----------------------------------------------------------------
     D £G43RC          S          30000    VARYING
********** PREPROCESSOR COPYEND QILEGEN,£G43DS
********** PREPROCESSOR COPYSTART QILEGEN,£G43E
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £G43E
      * Sorgente di origine : SMEDEV/QILEGEN(£G43E)
      * Esportato il        : 20240719 094555
      *====================================================================
     D* Schiere x passaggio dati a £G43
     D* Valori Alfa
     D £43A            S            256    DIM(256)
     D* Valori Numerici
     D £43N            S             21  6 DIM(256)
********** PREPROCESSOR COPYEND QILEGEN,£G43E
********** PREPROCESSOR COPYSTART QILEGEN,£G40E
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £G40E
      * Sorgente di origine : SMEDEV/QILEGEN(£G40E)
      * Esportato il        : 20240719 095032
      *====================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 14/02/10  V2R3    BS Aggiunta variabile £40ANM
     V* 15/02/10  V2R3    BS Aggiunto Keyword VARYING alla variabile £40AE
     V* 15/02/10  V2R3    BS Aggiunta definizione DS £G40L0DS/L1DS/L2DS
     V* 21/02/10  V2R3    BS Rilascio in DEV modifiche del 15/02/10
     V* 21/02/10  V2R3    BS Aggiunto Campo Schema e £40TDS
     V* 28/02/10  V2R3    BS Aumentata Lunghezza Campo N° Elementi
     V* 24/08/10  V3R1    BS Aggiunto Campo £40F_MSR
     V* 31/08/10  V3R1    BS Aggiunti Campi Oggetto Secondario
     V* 06/09/10  V3R1    BS Allungata DS da 100 a 500 caratteri
     V* 14/09/15  V4R1    BS Aggiunto campo utente
     V* 09/10/18  V5R1    BS Aggiunta indicazione I/O su variabili DS
     V* 01/11/18  V5R1    BS Aggiunto campo annullata
     V* 16/08/19  001067 BMA Tolte schiere con definizione errata overlay
     V* 16/08/19  V5R1    BS Check-out 001067 in SMEDEV
     D*----------------------------------------------------------------
     D* Schiere x passaggio dati a £G40
     D*
     D* Codici oggetto
     D £40A            S             15    DIM(300)
     D £40B            S             12    DIM(300)
     D*
     D £G40DS          DS           500
     D  £G40PZ                        1                                         O Lista Dinamica
     D  £G40EX                        1                                         O Lista Estesa
     D  £G40FM                        1                                         O Formato Ext
     D  £G40AE                        1                                         I Utilizzo £40AE
     D  £G40FL                        1                                         I Applica Filtro JO
     D  £G40NS                        1                                         I Disabilita SQL
     D  £G40NE                        9S 0                                      O N° Elementi
     D  £G40SC                       15                                         O Schema
     D  £G40FO                       15                                         O Fonte
     D  £G40T2                        2                                         I Tipo Oggetto Sec.
     D  £G40P2                       10                                         I Parametro Ogg.Sec.
     D  £G40C2                       15                                         I Cod.Ogg.Sec.
     D  £G40UT                       10                                         O Utente
     D  £G40AN                        1                                         O Annullata
      *
     D £40AE           S          30000    VARYING
      *
     D £40KNM          S              5S 0 INZ(%ELEM(£40KK))
     D £40DNM          S              5S 0 INZ(%ELEM(£40D))
     D £40ANM          S              5S 0 INZ(%ELEM(£40A))
      *
      * DS in sui riversare l'output della funzione LISOG
     D £40KDS          DS         30000    INZ
     D £40KK                         15    DIM(2000)
      * DS in sui riversare l'output della funzione LISOD
     D £40DDS          DS         30000    INZ
     D £40D                          50    DIM(600)
     D  £40DK                        15    OVERLAY(£40D:1)
     D  £40DD                        35    OVERLAY(£40D:*NEXT)
      * DS in sui riversare l'output della funzione TST
     D £40TDS          DS                  INZ
     D  £40TIN                         Z
     D  £40TFI                         Z
     D  £40TMI                        7  0
     D  £40TNE                       11  0
      * DS in sui riversare l'output della funzione FON
     D £40FDS          DS                  INZ
     D  £40FDE                 1     30                                         Descrizione
     D  £40FRE                31     80                                         Tipo Record
     D  £40FNT                81     85  0                                      N° Oggetti Gestiti
     D  £40F_MSO              86     86                                         Gestione OG_SCA
     D  £40F_MWL              87     87                                         Gestione WHR_LI
     D  £40F_MWC              88     88                                         Gestione WHR_CO
     D  £40FLU                89     90                                         Livello Utente
     D  £40F_MSR              91     91                                         Gestione RE_SCA
     D  £40FNS                92     92                                         NO SQL
      *
     D  £40FTO              1096   7095    DIM(500)                             Oggetti Gestiti
     D**£40FTP              1096   2095    DIM(500)                             . Tipi Oggetto
     D**£40FPA              2096   7095    DIM(500)                             . Parametri Oggetto
********** PREPROCESSOR COPYEND QILEGEN,£G40E
********** PREPROCESSOR COPYSTART QILEGEN,£D5CE
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £D5CE
      * Sorgente di origine : SMEDEV/QILEGEN(£D5CE)
      * Esportato il        : 20240719 094424
      *====================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 22/01/03  05.00   ZZ Tolta schiera D50 definita in spec.file D5COSO
     V* 14/07/03  05.00   GG Rimessa schiera D50 e aggiunta definizione
     V*                      della DS di D5COSO
     V* 29/10/03  V3R1    GG Inizializzata D50
     V* ==============================================================
      *
      * Attenzione: D5COSO va in per ultimo per poter accodare altre
      *             specifiche (OVERLAY ecc...)
      *
      * Valori in input
     D D51             S             20  6 DIM(99)
      * Significati
     D D52             S            100    DIM(99)
      * Valori calcolati
     D D53             S             20  6 DIM(99)
      * Valori in input/output
     D D5COSO        E DS                  EXTNAME(D5COSO0F)
     D  D50                                DIM(99) LIKE(D$C001) INZ
     D                                             OVERLAY(D5COSO:88)
********** PREPROCESSOR COPYEND QILEGEN,£D5CE
********** PREPROCESSOR COPYSTART QILEGEN,£PDS
      *IF NOT DEFINED(£PDS)
      *DEFINE £PDS
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
      * Inz
     D £INZFU          S             10    INZ('INZ')
     D £INZME          S             10
     D £INZCO          S             10
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
      *ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£PDS
      *--------------------------------------------------------------*
     D TXT             S            100    CTDATA PERRCD(1) DIM(010)            _TXT
     D MATTEM          S                   CTDATA PERRCD(1)                     _TXT_S11,29
     D                                     LIKE(£JAXSWK) DIM(050)
     D MATKPI          S                   CTDATA PERRCD(1)                     _TXT_S11,29
     D                                     LIKE(£JAXSWK) DIM(050)
     D SETA            S            100    CTDATA PERRCD(1) DIM(010)            _NOTXT
      *--------------------------------------------------------------------------------------------*
      * Dati Indice
     D I§DS            DS
     D  I§IB                         21  6                                      Budget
     D  I§IC                         21  6                                      Consuntivo
     D  I§ID                         21  6                                      Delta
     D  I§IP                          5  2                                      Delta %
      *--------------------------------------------------------------------------------------------*
     D SQL_DAT         DS
     D  SQL_CODI                     15
     D  SQL_COD1                     15
     D  SQL_COD2                     15
     D  SQL_COD3                     15
      *---------------------------------------------------------------
     D STRPAR          S          30000    VARYING
     D STRSQL          S          30000
      *--------------------------------------------------------------------------------------------*
     D ATR             S             15    DIM(100)
     D §43A            S            256    DIM(256)
     D §43N            S             21  6 DIM(256)
     D O$D5OE          S                   LIKE(T$D5OE) INZ(*HIVAL)
     D O$D5OG          S                   LIKE(T$D5OG) INZ(*HIVAL)
     D OlCod           S                   LIKE(£DECCD) INZ(*HIVAL)
     D OlCod1          S                   LIKE(£DECCD) INZ(*HIVAL)
     D OlCod2          S                   LIKE(£DECCD) INZ(*HIVAL)
     D OlCod3          S                   LIKE(£DECCD) INZ(*HIVAL)
     D §DECO_DESC      S                   LIKE(£DECO_DESC)
     D £DEC1_DESC      S                   LIKE(£DECO_DESC)
     D £DEC2_DESC      S                   LIKE(£DECO_DESC)
     D £DEC3_DESC      S                   LIKE(£DECO_DESC)
     D PER             S                   LIKE(D$DTVA) DIM(99) INZ
     D DPE             S                   LIKE(£PE8DE) DIM(99) INZ
     D V01             S             21  2 DIM(99) INZ
     D V02             S             21  2 DIM(99) INZ
     D V03             S             21  2 DIM(99) INZ
     D V04             S             21  2 DIM(99) INZ
     D $P              S              5  0
     D $A              S              5  0
     D $R              S              5  0
     D $I              S              5  0
     D $$Con           S                   LIKE(D$TIPA)
     D $$K00           S                   LIKE(D$COD1)
     D $$Tem           S                   LIKE(D$TROT)
     D $$RigDif        S             10
     D $$Tem_01        S                   LIKE(D$TROT)
     D ££Tem_01        S                   LIKE(£DECDE)
     D $$Tem_02        S                   LIKE(D$TROT)
     D ££Tem_02        S                   LIKE(£DECDE)
     D $$K02           S                   LIKE(D$COD2)
     D $$K03           S                   LIKE(D$COD3)
     D $$Per           S                   LIKE(D$DTVA)
     D $$OavFlt        S                   LIKE(£OAVAT)
     D $$OavVal        S                   LIKE(£OAVOV)
     D $$Mod           S              2
     D $$Mod_01        S              2
     D $$Mod_02        S              2
     D $$Ind           S            200
     D $$Ind_01        S            200
     D $$Ind_02        S            200
     D wwInd_01        S            200
     D wwInd_02        S            200
     D ££Ind_01        S             50
     D ££Ind_02        S             50
     D $$For           S             10
     D $$Per_EPr       S             10
     D $$Per_PPr       S             10
     D $$Per_OLD       S             10
     D SMES            S             10    DIM(012)
     D $MES            S              7  0
     D NMES            S              7  0
     D XI              S              2
     D OLCon           S                   LIKE(D$TIPA) INZ(*HIVAL)
     D OLTem           S                   LIKE(D$TROT) INZ(*HIVAL)
     D OLPer           S                   LIKE(D$DTVA) INZ(*HIVAL)
     D AAA010          S             10
     D AAA012          S             12
      *--------------------------------------------------------------------------------------------*
     D $TTEM           S              7  0
     D NTTEM           S              7  0
     D $N1             S              7  0
     D A8              S              8
      *----------------------------------------------------------------*
     D                 DS
     D SMOD                       30000
     D  SMOD_RES                     15    OVERLAY(SMOD:01)                     Responsabile
     D  SMOD_STA                     50    OVERLAY(SMOD:*NEXT)                  Stato
     D  SMOD_DIR                     15    OVERLAY(SMOD:*NEXT)                  Direzione
     D  SMOD_COSAZI                   1    OVERLAY(SMOD:*NEXT)                  Azienda costruzione
      *--------------------------------------------------------------------------------------------*
     C     KD5C44        KLIST
     C                   KFLD                    D$TIPA
     C                   KFLD                    D$TROT
     C                   KFLD                    D$CODI
     C                   KFLD                    D$DTVA
      *--------------------------------------------------------------*
    RD* M A I N
      *--------------------------------------------------------------*
********** PREPROCESSOR COPYSTART QILEGEN,£INIZSQ1
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £INIZSQ1
      * Sorgente di origine : SMEDEV/QILEGEN(£INIZSQ1)
      * Esportato il        : 20240719 094547
      *====================================================================
     H*----------------------------------------------------------------
     H* Specifiche da inserire all'inizio del MAIN in ogni pgm SQLRPGLE
     H* (versione con CloSqlCsr = *ENDACTGRP :
     H* cursori e statement preparati vengono chiusi alla chiusura del gruppo di attivazione)
     H* semplificando: usare in programmi che terminano per RT
     H*----------------------------------------------------------------
     V*=====================================================================
     V* MODIFICHE Ril.  T Au  Descrizione
     V* gg/mm/aa  nn.mm i xx  Breve descrizione
     V*=====================================================================
     V* 23/07/10  V3R1    BMA Creazione
     V*
     V*=====================================================================
     C/EXEC SQL
     C+ Set Option
     C+     SQLPATH   = *LIBL,
     C+     Naming    = *SYS,
     C+     Commit    = *NONE,
     C+     UsrPrf    = *OWNER,
     C+     DynUsrPrf = *OWNER,
     C+     CloSqlCsr = *ENDACTGRP
     C/END-EXEC

********** PREPROCESSOR COPYEND QILEGEN,£INIZSQ1
      * Impostazioni iniziali
     C                   EXSR      IMP0
      *
     C                   SELECT
      * Da Temi del D5COSO
     C                   WHEN      £UIBME='MAT.TEM'
     C                   EXSR      FMATTEM
      * Da KPI
     C                   WHEN      £UIBME='MAT.KPI'
     C                   EXSR      FMATKPI
     C                   ENDSL
      *
      * Fine
     C                   EXSR      FIN0
     C     G9MAIN        TAG
     C                   SETON                                        RT
      *--------------------------------------------------------------*
********** PREPROCESSOR COPYSTART QILEGEN,£INZSR
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 28/09/00  04.00   GG Correzione data
     V* 13/06/01  04.00   CS Considera il fuso orario
     V* 06/08/02  05.00   FL Aggiunto parm a B£GGP2 per gestire la
     V*                      richiesta di abbandono (impostare a "P")
     V* 21/08/02  05.00   ZZ Esternalizzata lettura B£1/B£2 ed imposta-
     V*                      zione campi data/ora
     V* 04/09/03  V2R1    GG Eliminata definizione §§IN
     V* 17/09/03  V2R3    BS Valorizzazione variabile ££ATCO da B£2
     V* 15/12/08  V2R3    AS Aggiunta possibilità di escludere *PSSR dall'inclusione
     V* 22/07/10  V3R1    CM Per poter utilizzare la *PSSR ne deve essere dichiarata l'intenzione
     V*                      attraverso la define SI_PSSR
     V* 11/10/10  V3R1    AS Eliminato tag ££FINE
     V* 18/10/10  V3R1    AS Eliminato RCLRSC
     V* 01/07/11          CM Abilitato RCLRSC condizionandolo alla tabella B£2
     V* 09/07/11  V3R2    AS Rilascio mod. precedenti non rilasciate (segnate con versione blank)
     V* A£61103A  V5R1    AS Disaccoppiamento utente di sistema da utente applicativo
     V* ==============================================================
     V* B£00722A  V3R1    CM Rimozione Gestione Interna degli errori
     D* OBIETTIVO
     D*  Eseguire la parte standard delle operazioni di inizio program-
     D*  ma e richiamare la routine di inizio del programma specifi-
     D*  co (£INIZI)
     D*
     D* NOTA IMPORTANTE
     D*  Questa /COPY deve essere messa subito dopo la fine del MAIN,
     D*  prima di qualsiasi routine o di altre /COPY di routines.
     C*----------------------------------------------------------------
     C*   ROUTINE INIZIALE DEL PROGRAMMA  (richiamata automaticamente)
     C*----------------------------------------------------------------
     C     *INZSR        BEGSR
      * Inizializzazione programma
     C                   CALL      'B£INZ0'
     C                   PARM      'INZ'         £INZFU           10
     C                   PARM      *BLANKS       £INZME           10
     C                   PARM                    £INZDS
     C                   PARM                    £INZCO           10
      * Utente applicativo
     C                   EVAL      £PDSNU=£INZUA
      * Richiamo routine iniziale specifica del singolo programma
     C                   EXSR      £INIZI
      *
     C                   ENDSR
      *--------------------------------------------------------------
********** PREPROCESSOR COPYEND QILEGEN,£INZSR
      *--------------------------------------------------------------*
    RD*  Impostazioni iniziali
      *--------------------------------------------------------------*
     C     IMP0          BEGSR
      * Impostazioni iniziali
1    C                   IF        P_RxATT(£UIBPA:'CalExt(':' ')='Yes'
     C                   EVAL      £JAXNS='3'
1x   C                   ELSE
     C                   EVAL      £JAXNS=*BLANKS
     C                   EXSR      £JAX_IMP0
1e   C                   ENDIF
      * Parametri PA + Input
     C                   EVAL      STRPAR=%TRIM(£UIBPA)+' '+%TRIM(£UIBD1)
      *
     C                   EVAL      $$Con=P_RxATT(STRPAR:'Con(':' ')
     C                   EVAL      $$K00=P_RxATT(STRPAR:'K00(':' ')
     C                   EVAL      $$Tem=P_RxATT(STRPAR:'Tem(':' ')
     C                   EVAL      $$K02=P_RxATT(STRPAR:'K02(':' ')
     C                   EVAL      $$K03=P_RxATT(STRPAR:'K03(':' ')
     C                   EVAL      $$Per=P_RxATT(STRPAR:'Per(':' ')
     C                   EVAL      $$Mod=P_RxATT(STRPAR:'Mod(':' ')
     C                   EVAL      $$Ind=P_RxATT(STRPAR:'Ind(':' ')
1    C                   IF        £UIBT1='LI'
     C                   EVAL      AAA012=£UIBP1
1x   C                   ELSE
     C                   EVAL      AAA012=£UIBT1+£UIBP1
1e   C                   ENDIF
1    C                   IF        $$Con=*BLANKS
     C                   EVAL      $$Con=AAA012
1e   C                   ENDIF
     C                   EVAL      $$Tem_01=P_RxATT(STRPAR:'Tem01(':' ')
1    C                   IF        $$Tem_01=' '
     C                   EVAL      $$Tem_01=$$Tem
1e   C                   ENDIF
     C                   EVAL      $$Tem_02=P_RxATT(STRPAR:'Tem02(':' ')
     C                   EVAL      $$Mod_01=P_RxATT(STRPAR:'Mod01(':' ')
     C                   EVAL      $$Mod_02=P_RxATT(STRPAR:'Mod02(':' ')
     C                   EVAL      $$Ind_01=P_RxATT(STRPAR:'Ind01(':' ')
     C                   EVAL      $$Ind_02=P_RxATT(STRPAR:'Ind02(':' ')
1    C                   IF        $$Ind_01=' '
     C                   EVAL      $$Ind_01=$$Ind
1e   C                   ENDIF
1    C                   IF        $$Ind_02=' '
     C                   EVAL      $$Ind_02=$$Ind_01
1e   C                   ENDIF
      * Il modello è ricavato dal tema di base
1    C                   IF        $$Mod=*BLANKS
     C                   EVAL      $$Mod=%SUBST($$Tem:01:02)
1e   C                   ENDIF
1    C                   IF        %SUBST($$Mod:01:01)='Y'
     C                   EVAL      %SUBST($$Mod:01:01)='X'
1e   C                   ENDIF
1    C                   IF        $$Mod_01=*BLANKS
     C                   EVAL      $$Mod_01=%SUBST($$Tem_01:01:02)
1e   C                   ENDIF
     C                   EVAL      $$RigDif=P_RxATT(STRPAR:'RigDif(':' ')
      * Fine
     C                   ENDSR
      *--------------------------------------------------------------*
    RD*  Chiusura e invio su coda del buffer residuo
      *--------------------------------------------------------------*
     C     FIN0          BEGSR
     C                   EXSR      £JAX_FIN0
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Matrice
      *--------------------------------------------------------------*
     C     FMATTEM       BEGSR
      *
      * Recupero dati del contesto/tema
     C                   EXSR      REPCTE
      * Dati derivati
1    C                   IF        $$Per=*BLANKS
     C                   CALL      'D5_084_B2'
     C                   PARM      'REP.MOD'     $$FUNZ           10
     C                   PARM                    U$TIPA           12
     C                   PARM                    U$TROT            3
     C                   PARM      $$Mod_01      U$MOD            10
     C                   PARM                    ££MOD         30000
     C                   EVAL      SMOD=££MOD
     C                   IF        SMOD_COSAZI='1'
     C                   EVAL      £RITST='B£4'
     C                   ELSE
     C                   EVAL      £RITST='B£4**'
1e   C                   ENDIF
     C                   EVAL      £RITEL='D5'+$$Mod_01
     C*                  EVAL      £RITEL='D5_084_'+$$Mod_01
     C                   EXSR      £RITES
2    C                   IF        NOT(*IN35)
     C                   EVAL      B£4$DS=£RITLI
     C                   EVAL      $$Per=%SUBST(T$B£4C:01:06)
2e   C                   ENDIF
1e   C                   ENDIF
1    C                   IF        $$Per=*BLANKS
     C                   IF        T$D5OE='D'
     C                   EVAL      £DA8IN=£UDSMG
     C                   EVAL      £DA8IF='*YYMD'
     C                   EVAL      £DA8OF='*YYMD'
     C                   EVAL      £DA8A1='FGM'
     C                   EVAL      £DA8O1='31'
     C                   EXSR      £DA8
     C                   EVAL      $$Per=%EDITC(£DA8ON:'X')
     C                   ELSE
     C                   EVAL      $$Per=%SUBST(%EDITC(£UDSMG:'X'):01:06)
     C                   ENDIF
1e   C                   ENDIF
      * Aggiunte periodi
      * Se anni 5 anni
      * Se periodi 12 mesi
1    C                   IF        OLPer<>$$Per OR
1    C                             T$D5OG<>O$D5OG OR
1    C                             T$D5OE<>O$D5OE
     C                   CLEAR                   PER
     C                   CLEAR                   DPE
     C                   EVAL      $P=1
     C                   SELECT
2    C                   WHEN      T$D5OE='D'
     C                   EVAL      PER($P)=$$Per
     C                   EVAL      £DA8IA=$$PER
     C                   EVAL      £DA8IF='*YYMD'
     C                   EVAL      £DA8OF='*DMYY'
     C                   EVAL      £DA8A1='CTE'
     C                   EVAL      £DA8SP='/'
     C                   EXSR      £DA8
     C                   EVAL      DPE($P)=£DA8OA
      *
3    C                   DO        11
     C                   MOVEL(P)  PER($P)       £DA8IN
     C                   EVAL      £DA8IF='*YYMD'
     C                   EVAL      £DA8OF='*YYMD'
     C                   EVAL      £DA8A1='SM'
     C                   EVAL      £DA8A2='FGM'
     C                   EVAL      £DA8O2='31'
     C                   EVAL      £DA8QY=-1
     C                   EXSR      £DA8
     C                   ADD       1             $P
     C                   EVAL      PER($P)=%EDITC(£DA8ON:'X')
     C                   EVAL      £DA8IN=£DA8ON
     C                   EVAL      £DA8IF='*YYMD'
     C                   EVAL      £DA8OF='*DMYY'
     C                   EVAL      £DA8A1='CTE'
     C                   EVAL      £DA8SP='/'
     C                   EXSR      £DA8
     C                   EVAL      DPE($P)=£DA8OA
3e   C                   ENDDO
      *
2    C                   WHEN      T$D5OG='A'
     C                   EVAL      £PE8EI=%SUBST($$Per:01:04)
     C                   EVAL      £PE8FU='DER'
     C                   EVAL      £PE8ME='DAE'
     C                   EVAL      £PE8TP='A'
     C                   EXSR      £PE8
     C                   EVAL      PER($P)=£PE8EO
     C                   EVAL      DPE($P)=£PE8DE
3    C                   DO        4
     C                   EVAL      £PE8EI=£PE8EO
     C                   EVAL      £PE8FU='REL'
     C                   EVAL      £PE8ME='ESEPRE'
     C                   EVAL      £PE8TP='A'
     C                   EXSR      £PE8
     C                   ADD       1             $P
     C                   EVAL      PER($P)=£PE8EO
     C                   EVAL      DPE($P)=£PE8DE
3e   C                   ENDDO
2    C                   OTHER
     C                   EVAL      £PE8PI=$$Per
     C                   EVAL      £PE8FU='DER'
     C                   EVAL      £PE8ME='DAP'
     C                   EVAL      £PE8TP='P'
     C                   EXSR      £PE8
     C                   EVAL      PER($P)=£PE8PO
     C                   EVAL      DPE($P)=£PE8DE
3    C                   DO        11
     C                   EVAL      £PE8PI=£PE8PO
     C                   EVAL      £PE8FU='REL'
     C                   EVAL      £PE8ME='PERPRE'
     C                   EVAL      £PE8TP='P'
     C                   EXSR      £PE8
     C                   ADD       1             $P
     C                   EVAL      PER($P)=£PE8PO
     C                   EVAL      DPE($P)=£PE8DE
3e   C                   ENDDO
2e   C                   ENDSL
     C                   EVAL      OLPer=$$Per
     C                   EVAL      O$D5OE=T$D5OE
     C                   EVAL      O$D5OG=T$D5OG
1e   C                   ENDIF
      *
     C                   EVAL      $$OavFlt=P_RxATT(STRPAR:'OavFlt(':' ')
     C                   EVAL      $$OavVal=P_RxATT(STRPAR:'OavVal(':' ')
      * Griglia
     C                   CLEAR                   £JAXSWK
1    C                   FOR       $A=1 TO 19
     C                   EVAL      £JAXSWK($A)=MATTEM($A)
1e   C                   ENDFOR
      *
     C                   EVAL      £DECTP='OG'
     C                   EVAL      £DECPA=*BLANKS
     C                   EVAL      £DECCD=AAA012
     C                   EXSR      £DEC
     C                   EVAL      %SUBST(£JAXSWK(02):11:29)=£DECDE
     C                   EVAL      %SUBST(£JAXSWK(03):11:29)=£DECDE
      *
1    C                   IF        $$OavFlt=*BLANKS
     C                   EVAL      %SUBST(£JAXSWK(04):61:01)='H'
1x   C                   ELSE
     C                   EVAL      £OAVFU='ATT'
     C                   EVAL      £OAVME='VTS'
     C                   EVAL      £OAVT1=%SUBST(AAA012:1:2)
     C                   EVAL      £OAVP1=%SUBST(AAA012:3)
     C                   EVAL      £OAVC1=*BLANKS
     C                   EVAL      £OAVAT=$$OavFlt
     C                   EXSR      £OAV
     C                   EVAL      %SUBST(£JAXSWK(04):40:20)=£OAVOT+£OAVOP
     C                   EVAL      %SUBST(£JAXSWK(04):11:29)=£OAVSI
1e   C                   ENDIF
      * Codici extra
1    C                   IF        T$D5O1=' '
     C                   EVAL      %SUBST(£JAXSWK(06):61:01)='H'
     C                   EVAL      %SUBST(£JAXSWK(07):61:01)='H'
1x   C                   ELSE
     C                   EVAL      £DECTP='OG'
     C                   EVAL      £DECPA=*BLANKS
     C                   EVAL      £DECCD=T$D5O1+T$D5OA
     C                   EXSR      £DEC
     C                   EVAL      %SUBST(£JAXSWK(06):11:29)=£DECDE
     C                   EVAL      %SUBST(£JAXSWK(07):11:29)=£DECDE
1e   C                   ENDIF
      *
1    C                   IF        T$D5O2=' '
     C                   EVAL      %SUBST(£JAXSWK(09):61:01)='H'
     C                   EVAL      %SUBST(£JAXSWK(10):61:01)='H'
1x   C                   ELSE
     C                   EVAL      £DECTP='OG'
     C                   EVAL      £DECPA=*BLANKS
     C                   EVAL      £DECCD=T$D5O2+T$D5OB
     C                   EXSR      £DEC
     C                   EVAL      %SUBST(£JAXSWK(09):11:29)=£DECDE
     C                   EVAL      %SUBST(£JAXSWK(10):11:29)=£DECDE
1e   C                   ENDIF
      *
1    C                   IF        T$D5O3=' '
     C                   EVAL      %SUBST(£JAXSWK(12):61:01)='H'
     C                   EVAL      %SUBST(£JAXSWK(13):61:01)='H'
1x   C                   ELSE
     C                   EVAL      £DECTP='OG'
     C                   EVAL      £DECPA=*BLANKS
     C                   EVAL      £DECCD=T$D5O3+T$D5OC
     C                   EXSR      £DEC
     C                   EVAL      %SUBST(£JAXSWK(12):11:29)=£DECDE
     C                   EVAL      %SUBST(£JAXSWK(13):11:29)=£DECDE
1e   C                   ENDIF
      * Aggiunge colonne periodi
     C                   EVAL      $R=19
1    C                   FOR       $A=1 TO $P
     C                   EVAL      £JAXDSCOL=MATTEM(20)
     C                   EVAL      £JAXCCD='GRA'+%EDITC($A:'X')
     C                   EVAL      $R+=1
     C                   EVAL      £JAXSWK($R)=£JAXDSCOL
     C                   EVAL      £JAXDSCOL=MATTEM(21)
     C                   EVAL      £JAXCCD='VAL'+%EDITC($A:'X')
     C                   EVAL      £JAXCTX=DPE($A)
     C                   EVAL      $R+=1
     C                   EVAL      £JAXSWK($R)=£JAXDSCOL
1e   C                   ENDFOR
      *
     C                   EVAL      £DECTP='TA'
     C                   EVAL      £DECPA='D5O'+T$D5SA
     C                   EVAL      £DECCD=$$Tem_01
     C                   EXSR      £DEC
     C                   EVAL      ££Tem_01='Effettivo ('+%TRIM(£DECCD) +') '   COSTANTE
      *
     C                   EVAL      ££Tem_02='Target'                            COSTANTE
1    C                   IF        $$Tem_02<>' '
     C                   EVAL      £DECTP='TA'
     C                   EVAL      £DECPA='D5O'+T$D5SA
     C                   EVAL      £DECCD=$$Tem_02
     C                   EXSR      £DEC
     C                   EVAL      ££Tem_02='Target ('+%TRIM(£DECCD) +') '      COSTANTE
1e   C                   ENDIF
      * Target
     C                   EVAL      £G43FU='SCO'
     C                   EVAL      £G43ME='INC'
     C                   EVAL      £G43NC=0
     C                   EVAL      £G43CS=';'
     C                   EVAL      £G43RC=%TRIM($$Ind_02)
     C                   EXSR      £G43
     C                   EVAL      £G43FU='SCO'
     C                   EVAL      £G43ME='ELA'
     C                   EVAL      £G43CS=';'
     C                   EXSR      £G43
     C                   EVAL      §43A=£43A
     C                   EVAL      §43N=£43N
      * Actual
     C                   EVAL      £G43FU='SCO'
     C                   EVAL      £G43ME='INC'
     C                   EVAL      £G43NC=0
     C                   EVAL      £G43CS=';'
     C                   EVAL      £G43RC=%TRIM($$Ind_01)
     C                   EXSR      £G43
     C                   EVAL      £G43FU='SCO'
     C                   EVAL      £G43ME='ELA'
     C                   EVAL      £G43CS=';'
     C                   EXSR      £G43
      *
     C                   EVAL      OlCod = *HIVAL
     C                   EVAL      OlCod1= *HIVAL
     C                   EVAL      OlCod2= *HIVAL
     C                   EVAL      OlCod3= *HIVAL
      *
     C                   EXSR      £JAX_AGRI
     C                   EXSR      £JAX_ARIG_I
      * Righe Griglia
     C                   EXSR      SL_TEM
1    C                   DO        *HIVAL
     C/EXEC SQL
     C+ Fetch C1 Into :SQL_DAT
     C/END-EXEC
      * . fine
2    C                   IF        SQLCOD<0 OR SQLCOD=100
     C                   LEAVE
2e   C                   ENDIF
     C                   EXSR      FMATTEMR
1e   C                   ENDDO
      * Fine Griglia
     C                   EXSR      £JAX_ARIG_F
      * Setup
     C                   EVAL      £JAXCP='<Setup><Program><EXB> '
     C                   EXSR      £JAX_ADD
      *
1    C                   FOR       $A=1 TO %ELEM(SETA)
2    C                   IF        SETA($A)=' '
     C                   LEAVE
2e   C                   ENDIF
     C                   EVAL      £JAXCP=%TRIMR(SETA($A))
     C                   EXSR      £JAX_ADD
1e   C                   ENDFOR
      *
     C                   EVAL      £JAXCP='</EXB></Program></Setup>'
     C                   EXSR      £JAX_ADD
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Recupero dati del contesto/tema
      *--------------------------------------------------------------*
     C     REPCTE        BEGSR
      *
1    C                   IF        OLCon<>$$Con
     C                   EVAL      £RITST='D5S'
     C                   EVAL      £RITEL=$$Con
     C                   EXSR      £RITES
     C  N35              EVAL      D5S$DS=£RITLI
     C   35              CLEAR                   D5S$DS
     C                   EVAL      OLCon=$$Con
     C                   EVAL      OLTem=*HIVAL
     C                   EVAL      OLPer=*HIVAL
1e   C                   ENDIF
1    C                   IF        OLTem<>$$Tem_01
     C                   EVAL      £RITST='D5O'+T$D5SA
     C                   EVAL      £RITEL=$$Tem_01
     C                   EXSR      £RITES
     C  N35              EVAL      D5O$DS=£RITLI
     C   35              CLEAR                   D5O$DS
     C                   EVAL      OLTem=$$Tem_01
     C                   EVAL      OLPer=*HIVAL
1e   C                   ENDIF
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Matrice
      *--------------------------------------------------------------*
     C     FMATKPI       BEGSR
      *
      * Periodo iniziale?
     C                   EVAL      £DA8IN=£UDSMG
     C                   EVAL      £DA8IF='*YYMD'
     C                   EVAL      £DA8OF='*YYMD'
     C                   EVAL      £DA8A1='SM'
     C                   EVAL      £DA8A2='FGM'
     C                   EVAL      £DA8O2='31'
     C                   Z-SUB     11            £DA8QY
     C                   EXSR      £DA8
     C                   Z-ADD     £DA8ON        XDATI             8 0
      * Per quanti periodi?
     C                   Z-ADD     24            XNPER             7 0
      * Griglia
     C                   CLEAR                   £JAXSWK
1    C                   DO        *HIVAL        $A
     C                   IF        MATKPI($A)=''
     C                   EVAL      $A=$A-1
     C                   LEAVE
     C                   ENDIF
     C                   EVAL      £JAXSWK($A)=MATKPI($A)
1e   C                   ENDDO
      * Costruisco le colonne dei periodi
     C                   DO        XNPER         $P                5 0
     C                   CLEAR                   £JAXDSCOL
      *
     C                   IF        $P=1
     C                   EVAL      £JAXCCD='G'+%EDITC(XDATI:'X')
     C                   EVAL      PER($P)=%EDITC(XDATI:'X')
     C                   ELSE
     C                   EVAL      A8=PER($P-1)
     C                   MOVEL(P)  A8            £DA8IN
     C                   EVAL      £DA8IF='*YYMD'
     C                   EVAL      £DA8OF='*YYMD'
     C                   EVAL      £DA8A1='SM'
     C                   EVAL      £DA8A2='FGM'
     C                   EVAL      £DA8O2='31'
     C                   EVAL      £DA8QY=1
     C                   EXSR      £DA8
     C                   EVAL      £JAXCCD='G'+%EDITC(£DA8ON:'X')
     C                   EVAL      PER($P)=%EDITC(£DA8ON:'X')
     C                   ENDIF
      *
     C                   MOVEL(P)  PER($P)       £DA8IN
     C                   EVAL      £DA8IF='*YYMD'
     C                   EVAL      £DA8OF='*DMYY'
     C                   EVAL      £DA8SP='/'
     C                   EVAL      £DA8A1='CTE'
     C                   EXSR      £DA8
     C                   EVAL      £JAXCTX=£DA8OA
     C                   EVAL      £JAXCLU='1'
     C                   EVAL      £JAXCIO='G'
     C                   EVAL      $A=$A+1
     C                   EVAL      £JAXSWK($A)=£JAXDSCOL
      *
     C                   EVAL      $A=$A+1
     C                   EVAL      £JAXCCD='D'+%SUBST(£JAXCCD:2)
     C                   EVAL      £JAXCOG='NR'
     C                   EVAL      £JAXCLU='21'
     C                   EVAL      £JAXCIO=' '
     C                   EVAL      £JAXSWK($A)=£JAXDSCOL
      *
     C                   ENDDO
      *
     C                   EXSR      £JAX_AGRI
     C                   EXSR      £JAX_ARIG_I
      *
     C                   EVAL      £G40TP=%SUBST(£UIBP1:1:2)
     C                   EVAL      £G40PA=%SUBST(£UIBP1:3)
     C                   EVAL      £G40MV=£UIBK1
     C                   EVAL      £G40FU='SCN'
     C                   EVAL      £G40ME='POS'
      * Ciclo di scansione di scansione degli elementi della lista
     C                   DO        *HIVAL
     C                   EXSR      £G40
     C                   IF        *IN35
     C                   LEAVE
     C                   ENDIF
     C                   EVAL      £G40ME='LET'
      * . Recupero i KPI
     C****               EVAL      $TPOG=£UIBP1
     C****               EVAL      $CDOG=£40A(1)
      *
     C****               CALL      'D5_091_A3'
     C****               PARM      *BLANKS       $$FUNZ           10
     C****               PARM      *BLANKS       $$METO           10
     C****               PARM                    $TPOG            12
     C****               PARM                    $CDOG            15
     C****               PARM                    TTEM
     C****               PARM                    $TTEM
      *
     C                   EVAL      £139I_FU=''
     C                   EVAL      £139I_ME=''
     C                   EVAL      £139I_TP=£UIBP1
     C                   EVAL      £139I_CD=£40A(1)
     C                   EXSR      £D5_139
      * .. Ciclo sui KPI
1    C                   FOR       NTTEM=1 TO £139O_NK
     C                   IF        £139K_ST(NTTEM)<>'17'
     C                   ITER
     C                   ENDIF
      * .. per ora solo se fonte D5
2x   C                   IF        £139K_FN(NTTEM)<>'D5'
     C                   ITER
     C                   ENDIF
      * .. Setto le chiavi
      * ... Contesto
     C                   EVAL      $$Con=P_RxATT(£139K_PF(NTTEM):'Con(':'')
2    C                   IF        $$Con=*BLANKS
     C                   EVAL      $$Con=£UIBP1
2e   C                   ENDIF
      * ... Tema
     C                   EVAL      $$Tem=P_RxATT(£139K_PF(NTTEM):'Tem(':'')
      * ... Tema Budget
     C                   EVAL      $$Tem_02=P_RxATT(£139K_PF(NTTEM):'Tem2(':'')
      * ... Indice
     C                   EVAL      $$Ind=P_RxATT(£139K_PF(NTTEM):'Ind(':'')
      * .. Setto la decodifica dell'oggetto
     C                   EVAL      £DECTP=%SUBST(£UIBP1:1:2)
     C                   EVAL      £DECPA=%SUBST(£UIBP1:3)
     C                   EVAL      £DECCD=£40A(1)
     C                   EVAL      £DECI_DELI='1'
     C                   EXSR      £DEC
      *
     C                   DO        4             $R                5 0
      * .. Recupero dati del contesto/tema
     C                   SELECT
     C                   WHEN      $R=1
     C                   EVAL      $$Tem_01=$$TEM
     C                   EXSR      REPCTE
     C                   EVAL      £RITST='IGI'+T$D5OD
     C                   EVAL      £RITEL=$$IND
     C                   EXSR      £RITES
     C                   EVAL      XTI='TA'+£RITST
     C                   WHEN      $R=2
     C                   EVAL      $$Tem_01=$$TEM_02
     C                   EXSR      REPCTE
     C                   EVAL      £RITST='IGI'+T$D5OD
     C                   EVAL      £RITEL=$$IND
     C                   EXSR      £RITES
     C                   EVAL      XTI='TA'+£RITST
     C                   OTHER
     C                   CLEAR                   XTI              12
     C                   ENDSL
      *
     C                   EVAL      £JAXCP=%TRIMR(£UIBP1)
     C                             +'|'+%TRIMR(£40A(1))
     C                             +'|'+%TRIMR(£DECO_DESC)
     C                             +'|'+%TRIMR(XTI)
     C                             +'|'+%TRIMR($$IND)
     C                             +'|'+%TRIMR(£RITDS)
     C                             +'|'+%TRIMR(TXT($R))
      *
     C                   DO        XNPER         $P                5 0
      * ... Recupero il budget
     C                   EVAL      XI=%TRIM($$Ind)
     C                   EVAL      D$TIPA=$$Con
     C                   EVAL      D$CODI=£40A(1)
     C                   EVAL      D$TROT=$$Tem_02
     C                   MOVEL(P)  PER($P)       D$DTVA
     C     KD5C44        CHAIN     D5COSO4L
3    C                   IF        %FOUND
     C                   EXSR      REPIND
     C                   EVAL      I§IB=XNUM                                    Budget
     C                   ELSE
     C                   EVAL      I§IB=0                                       Budget
3e   C                   ENDIF
      * ... Recupero il consuntivo
     C                   EVAL      XI=%TRIM($$Ind)
     C                   EVAL      D$TIPA=$$Con
     C                   EVAL      D$CODI=£40A(1)
     C                   EVAL      D$TROT=$$Tem
     C                   MOVEL(P)  PER($P)       D$DTVA
     C     KD5C44        CHAIN     D5COSO4L
3    C                   IF        %FOUND
     C                   EXSR      REPIND
     C                   EVAL      I§IC=XNUM                                    Consuntivo
     C                   ELSE
     C                   EVAL      I§IC=0                                       Budget
3e   C                   ENDIF
      *
     C                   EVAL      I§ID=I§IC-I§IB                               Delta
      * . % Delta
3    C                   SELECT
      * .. Se tutto a zero
3x   C                   WHEN      I§IB=0 AND I§ID=0
     C                   Z-ADD     0             I§IP                           Delta %
      * .. Se consuntivo a zero
3x   C                   WHEN      I§ID=I§IB
     C                   Z-ADD     100           I§IP
      * .. Se consuntivo completo
3x   C                   WHEN      I§ID=0
     C                   Z-ADD     0             I§IP
      * .. Se budget assente
3x   C                   WHEN      I§IB=0
     C                   Z-ADD     100           I§IP
      * .. Altrimenti
3x   C                   OTHER
     C     I§ID          MULT      100           N309             30 9
     C     N309          DIV(H)    I§IB          I§IP
3e   C                   ENDSL
      *
     C                   SELECT
      * .. Budget
     C                   WHEN      $R=1
     C                   EVAL      £JAXCP=£JAXCP
     C                             +'|01001'
     C                             +'|'+%TRIM(%EDITC(I§IB:'O'))
      * .. Effettivo
     C                   WHEN      $R=2
     C                   SELECT
      * ... Rosso
4    C                   WHEN      I§ID<0
     C                   EVAL      AAA010='51700'
      * ... Giallo
5    C                   WHEN      I§IP<10 AND I§IP<>0
     C                   EVAL      AAA010='01B01'
      * ... Verde
3    C                   WHEN      I§IC<>0 OR V02($A)<>0
     C                   EVAL      AAA010='51G01'
     C                   OTHER
     C                   EVAL      AAA010='01001'
5e   C                   ENDSL
     C                   EVAL      £JAXCP=£JAXCP
     C                             +'|'+%TRIM(AAA010)
     C                             +'|'+%TRIM(%EDITC(I§IC:'O'))
      * .. Differenza
     C                   WHEN      $R=3
     C                   EVAL      £JAXCP=£JAXCP
     C                             +'|01001'
     C                             +'|'+%TRIM(%EDITC(I§ID:'O'))
      * .. Differenza %
     C                   WHEN      $R=4
     C                   EVAL      £JAXCP=£JAXCP
     C                             +'|01001'
     C                             +'|'+%TRIM(%EDITC(I§IP:'O'))
     C                   ENDSL
     C                   ENDDO
      *
     C                   EXSR      £JAX_ARIG
     C                   ENDDO
      *
1e   C                   ENDFOR
      *
     C                   ENDDO
      *
     C                   EXSR      £JAX_ARIG_F
      *
     C                   ENDSR
      *---------------------------------------------------------------
    RD* Recupero indice D5COSO
      *---------------------------------------------------------------
     C     REPIND        BEGSR
      *
1    C                   SELECT
1x   C                   WHEN      XI='01'
     C                   EVAL      XNUM=D$C001
1x   C                   WHEN      XI='02'
     C                   EVAL      XNUM=D$C002
1x   C                   WHEN      XI='03'
     C                   EVAL      XNUM=D$C003
1x   C                   WHEN      XI='04'
     C                   EVAL      XNUM=D$C004
1x   C                   WHEN      XI='05'
     C                   EVAL      XNUM=D$C005
1x   C                   WHEN      XI='06'
     C                   EVAL      XNUM=D$C006
1x   C                   WHEN      XI='07'
     C                   EVAL      XNUM=D$C007
1x   C                   WHEN      XI='08'
     C                   EVAL      XNUM=D$C008
1x   C                   WHEN      XI='09'
     C                   EVAL      XNUM=D$C009
1x   C                   WHEN      XI='10'
     C                   EVAL      XNUM=D$C010
1x   C                   WHEN      XI='11'
     C                   EVAL      XNUM=D$C011
1x   C                   WHEN      XI='12'
     C                   EVAL      XNUM=D$C012
1x   C                   WHEN      XI='13'
     C                   EVAL      XNUM=D$C013
1x   C                   WHEN      XI='14'
     C                   EVAL      XNUM=D$C014
1x   C                   WHEN      XI='15'
     C                   EVAL      XNUM=D$C015
1x   C                   WHEN      XI='16'
     C                   EVAL      XNUM=D$C016
1x   C                   WHEN      XI='17'
     C                   EVAL      XNUM=D$C017
1x   C                   WHEN      XI='18'
     C                   EVAL      XNUM=D$C018
1x   C                   WHEN      XI='19'
     C                   EVAL      XNUM=D$C019
1x   C                   WHEN      XI='20'
     C                   EVAL      XNUM=D$C020
1x   C                   WHEN      XI='21'
     C                   EVAL      XNUM=D$C021
1x   C                   WHEN      XI='22'
     C                   EVAL      XNUM=D$C022
1x   C                   WHEN      XI='23'
     C                   EVAL      XNUM=D$C023
1x   C                   WHEN      XI='24'
     C                   EVAL      XNUM=D$C024
1x   C                   WHEN      XI='25'
     C                   EVAL      XNUM=D$C025
1x   C                   WHEN      XI='26'
     C                   EVAL      XNUM=D$C026
1x   C                   WHEN      XI='27'
     C                   EVAL      XNUM=D$C027
1x   C                   WHEN      XI='28'
     C                   EVAL      XNUM=D$C028
1x   C                   WHEN      XI='29'
     C                   EVAL      XNUM=D$C029
1x   C                   WHEN      XI='30'
     C                   EVAL      XNUM=D$C030
1x   C                   WHEN      XI='31'
     C                   EVAL      XNUM=D$C031
1x   C                   WHEN      XI='32'
     C                   EVAL      XNUM=D$C032
1x   C                   WHEN      XI='33'
     C                   EVAL      XNUM=D$C033
1x   C                   WHEN      XI='34'
     C                   EVAL      XNUM=D$C034
1x   C                   WHEN      XI='35'
     C                   EVAL      XNUM=D$C035
1x   C                   WHEN      XI='36'
     C                   EVAL      XNUM=D$C036
1x   C                   WHEN      XI='37'
     C                   EVAL      XNUM=D$C037
1x   C                   WHEN      XI='38'
     C                   EVAL      XNUM=D$C038
1x   C                   WHEN      XI='39'
     C                   EVAL      XNUM=D$C039
1x   C                   WHEN      XI='40'
     C                   EVAL      XNUM=D$C040
1x   C                   WHEN      XI='41'
     C                   EVAL      XNUM=D$C041
1x   C                   WHEN      XI='42'
     C                   EVAL      XNUM=D$C042
1x   C                   WHEN      XI='43'
     C                   EVAL      XNUM=D$C043
1x   C                   WHEN      XI='44'
     C                   EVAL      XNUM=D$C044
1x   C                   WHEN      XI='45'
     C                   EVAL      XNUM=D$C045
1x   C                   WHEN      XI='46'
     C                   EVAL      XNUM=D$C046
1x   C                   WHEN      XI='47'
     C                   EVAL      XNUM=D$C047
1x   C                   WHEN      XI='48'
     C                   EVAL      XNUM=D$C048
1x   C                   WHEN      XI='49'
     C                   EVAL      XNUM=D$C049
1x   C                   WHEN      XI='50'
     C                   EVAL      XNUM=D$C050
1x   C                   WHEN      XI='51'
     C                   EVAL      XNUM=D$C051
1x   C                   WHEN      XI='52'
     C                   EVAL      XNUM=D$C052
1x   C                   WHEN      XI='53'
     C                   EVAL      XNUM=D$C053
1x   C                   WHEN      XI='54'
     C                   EVAL      XNUM=D$C054
1x   C                   WHEN      XI='55'
     C                   EVAL      XNUM=D$C055
1x   C                   WHEN      XI='56'
     C                   EVAL      XNUM=D$C056
1x   C                   WHEN      XI='57'
     C                   EVAL      XNUM=D$C057
1x   C                   WHEN      XI='58'
     C                   EVAL      XNUM=D$C058
1x   C                   WHEN      XI='59'
     C                   EVAL      XNUM=D$C059
1x   C                   WHEN      XI='60'
     C                   EVAL      XNUM=D$C060
1x   C                   WHEN      XI='61'
     C                   EVAL      XNUM=D$C061
1x   C                   WHEN      XI='62'
     C                   EVAL      XNUM=D$C062
1x   C                   WHEN      XI='63'
     C                   EVAL      XNUM=D$C063
1x   C                   WHEN      XI='64'
     C                   EVAL      XNUM=D$C064
1x   C                   WHEN      XI='65'
     C                   EVAL      XNUM=D$C065
1x   C                   WHEN      XI='66'
     C                   EVAL      XNUM=D$C066
1x   C                   WHEN      XI='67'
     C                   EVAL      XNUM=D$C067
1x   C                   WHEN      XI='68'
     C                   EVAL      XNUM=D$C068
1x   C                   WHEN      XI='69'
     C                   EVAL      XNUM=D$C069
1x   C                   WHEN      XI='70'
     C                   EVAL      XNUM=D$C070
1x   C                   WHEN      XI='71'
     C                   EVAL      XNUM=D$C071
1x   C                   WHEN      XI='72'
     C                   EVAL      XNUM=D$C072
1x   C                   WHEN      XI='73'
     C                   EVAL      XNUM=D$C073
1x   C                   WHEN      XI='74'
     C                   EVAL      XNUM=D$C074
1x   C                   WHEN      XI='75'
     C                   EVAL      XNUM=D$C075
1x   C                   WHEN      XI='76'
     C                   EVAL      XNUM=D$C076
1x   C                   WHEN      XI='77'
     C                   EVAL      XNUM=D$C077
1x   C                   WHEN      XI='78'
     C                   EVAL      XNUM=D$C078
1x   C                   WHEN      XI='79'
     C                   EVAL      XNUM=D$C079
1x   C                   WHEN      XI='80'
     C                   EVAL      XNUM=D$C080
1x   C                   WHEN      XI='81'
     C                   EVAL      XNUM=D$C081
1x   C                   WHEN      XI='82'
     C                   EVAL      XNUM=D$C082
1x   C                   WHEN      XI='83'
     C                   EVAL      XNUM=D$C083
1x   C                   WHEN      XI='84'
     C                   EVAL      XNUM=D$C084
1x   C                   WHEN      XI='85'
     C                   EVAL      XNUM=D$C085
1x   C                   WHEN      XI='86'
     C                   EVAL      XNUM=D$C086
1x   C                   WHEN      XI='87'
     C                   EVAL      XNUM=D$C087
1x   C                   WHEN      XI='88'
     C                   EVAL      XNUM=D$C088
1x   C                   WHEN      XI='89'
     C                   EVAL      XNUM=D$C089
1x   C                   WHEN      XI='90'
     C                   EVAL      XNUM=D$C090
1x   C                   WHEN      XI='91'
     C                   EVAL      XNUM=D$C091
1x   C                   WHEN      XI='92'
     C                   EVAL      XNUM=D$C092
1x   C                   WHEN      XI='93'
     C                   EVAL      XNUM=D$C093
1x   C                   WHEN      XI='94'
     C                   EVAL      XNUM=D$C094
1x   C                   WHEN      XI='95'
     C                   EVAL      XNUM=D$C095
1x   C                   WHEN      XI='96'
     C                   EVAL      XNUM=D$C096
1x   C                   WHEN      XI='97'
     C                   EVAL      XNUM=D$C097
1x   C                   WHEN      XI='98'
     C                   EVAL      XNUM=D$C098
1x   C                   WHEN      XI='99'
     C                   EVAL      XNUM=D$C099
1x   C                   OTHER
     C                   CLEAR                   XNUM             21 6
1e   C                   ENDSL
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Posizionamento
      *--------------------------------------------------------------*
     C     SL_TEM        BEGSR
      * Estrae dati
1    C                   IF        £UIBT1='LI'
     C                   EVAL      STRSQL ='SELECT '
     C                             +'D$CODI,D$COD1,D$COD2,D$COD3 '
     C                             +' FROM D5COSO0F WHERE D$TIPA='''
     C                             +%TRIM(AAA012)+''' '
     C                             +'AND D$TROT='''
     C                             +%TRIM(P_RxATT(STRPAR:'Tem(':' '))
     C                             +''' '
     C                             +' GROUP BY D$CODI,D$COD1,D$COD2,D$COD3'
     C                             +' ORDER BY D$CODI,D$COD1,D$COD2,D$COD3'
1x   C                   ELSE
     C                   EVAL      STRSQL ='SELECT '
     C                             +'D$CODI,D$COD1,D$COD2,D$COD3 '
     C                             +' FROM D5COSO0F WHERE D$TIPA='''
     C                             +%TRIM(AAA012)+''' '
     C                             +'AND D$CODI='''
     C                             +%TRIM(£UIBK1)
     C                             +''' '
     C                             +'AND D$TROT='''
     C                             +%TRIM(P_RxATT(STRPAR:'Tem(':' '))
     C                             +''' '
     C                             +' GROUP BY D$CODI,D$COD1,D$COD2,D$COD3'
     C                             +' ORDER BY D$CODI,D$COD1,D$COD2,D$COD3'
1e   C                   ENDIF
     C/EXEC SQL
     C+ Close C1
     C/END-EXEC
     C/EXEC SQL
     C+ Prepare S1 From :STRSQL
     C/END-EXEC
     C/EXEC SQL
     C+ Open C1
     C/END-EXEC
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Scrittura Riga Matrice 001
      *--------------------------------------------------------------*
     C     FMATTEMR      BEGSR
      *
     C                   CLEAR                   £JAXCP
     C                   CLEAR                   ATR
      *
1    C                   IF        $$OavFlt<>*BLANKS
     C                   EVAL      £OAVFU='ATT'
     C                   EVAL      £OAVME='V'
     C                   EVAL      £OAVT1=%SUBST(AAA012:1:2)
     C                   EVAL      £OAVP1=%SUBST(AAA012:3)
     C                   EVAL      £OAVC1=SQL_CODI
     C                   EVAL      £OAVAT=$$OavFlt
     C                   EXSR      £OAV
     C                   EVAL      ATR(03)=£OAVOV
2    C                   IF        ATR(03)<>$$OavVal
     C                   LEAVESR
2e   C                   ENDIF
1e   C                   ENDIF
      *
1    C                   IF        OlCod<>SQL_CODI
     C                   EVAL      £DECTP=%SUBST(AAA012:1:2)
     C                   EVAL      £DECPA=%SUBST(AAA012:3)
     C                   EVAL      £DECCD=SQL_CODI
     C                   EVAL      £DECI_DELI='1'
     C                   EXSR      £DEC
     C                   EVAL      §DECO_DESC=£DECO_DESC
     C                   EVAL      OlCod=SQL_CODI
1e   C                   ENDIF
      *
1    C                   IF        OlCod1<>SQL_COD1
     C                   EVAL      £DEC1_DESC=' '
2    C                   IF        SQL_COD1<>' '
     C                   EVAL      £DECTP=T$D5O1
     C                   EVAL      £DECPA=T$D5OA
     C                   EVAL      £DECCD=SQL_COD1
     C                   EVAL      £DECI_DELI='1'
     C                   EXSR      £DEC
     C                   EVAL      £DEC1_DESC=£DECO_DESC
2e   C                   ENDIF
     C                   EVAL      OlCod1=SQL_COD1
1e   C                   ENDIF
      *
1    C                   IF        OlCod2<>SQL_COD2
     C                   EVAL      £DEC2_DESC=' '
2    C                   IF        SQL_COD2<>' '
     C                   EVAL      £DECTP=T$D5O2
     C                   EVAL      £DECPA=T$D5OB
     C                   EVAL      £DECCD=SQL_COD2
     C                   EVAL      £DECI_DELI='1'
     C                   EXSR      £DEC
     C                   EVAL      £DEC2_DESC=£DECO_DESC
2e   C                   ENDIF
     C                   EVAL      OlCod2=SQL_COD2
1e   C                   ENDIF
      *
1    C                   IF        OlCod3<>SQL_COD3
     C                   EVAL      £DEC3_DESC=' '
2    C                   IF        SQL_COD3<>' '
     C                   EVAL      £DECTP=T$D5O3
     C                   EVAL      £DECPA=T$D5OC
     C                   EVAL      £DECCD=SQL_COD3
     C                   EVAL      £DECI_DELI='1'
     C                   EXSR      £DEC
     C                   EVAL      £DEC3_DESC=£DECO_DESC
2e   C                   ENDIF
     C                   EVAL      OlCod3=SQL_COD3
1e   C                   ENDIF
      * Numero Indici
1    C                   FOR       $I=1 TO £G43NC
     C                   EVAL      wwInd_01=£43A($I)
     C                   EVAL      wwInd_02=§43A($I)
2    C                   IF        wwInd_02=' '
     C                   EVAL      wwInd_02=wwInd_01
2e   C                   ENDIF
     C                   EVAL      £DECTP='TA'
     C                   EVAL      £DECPA='IGI'+T$D5OD
     C                   EVAL      £DECCD=wwInd_01
     C                   EXSR      £DEC
     C                   EVAL      ££Ind_01=£DECDE
     C                   EVAL      £DECTP='TA'
     C                   EVAL      £DECPA='IGI'+T$D5OD
     C                   EVAL      £DECCD=wwInd_02
     C                   EXSR      £DEC
     C                   EVAL      ££Ind_02=£DECDE
      * Actual
     C                   CLEAR                   V01
2    C                   FOR       $A=1 TO $P
     C                   EVAL      £D5CCO=AAA012
     C                   EVAL      £D5CCD=SQL_CODI
     C                   EVAL      £D5CPE=PER($A)
      *
     C                   EVAL      £D5CTE=$$Tem_01
     C                   EVAL      £D5CC1=SQL_COD1
     C                   EVAL      £D5CC2=SQL_COD2
     C                   EVAL      £D5CC3=SQL_COD3
     C                   EXSR      LOD_D5C
     C                   EVAL(RH)  V01($A)=D53(%INT(wwInd_01))
2e   C                   ENDFOR
      * Target
     C                   CLEAR                   V02
2    C                   IF        $$Tem_02=' '
3    C                   FOR       $A=1 TO $P
     C                   EVAL(RH)  V02($A)=%XFOOT(V01)/$P
     C                   EVAL      £JaxCP=%TRIM(£Jaxcp)+'|'
     C                             +%TRIM (%EDITC(V02($A):'K'))+'|'
3e   C                   ENDFOR
2x   C                   ELSE
3    C                   FOR       $A=1 TO $P
     C                   EVAL      £D5CCO=AAA012
     C                   EVAL      £D5CCD=SQL_CODI
     C                   EVAL      £D5CPE=PER($A)
     C                   EVAL      £D5CTE=$$Tem_02
     C                   EVAL      £D5CC1=SQL_COD1
     C                   EVAL      £D5CC2=SQL_COD2
     C                   EVAL      £D5CC3=SQL_COD3
     C                   EXSR      LOD_D5C
     C                   EVAL(RH)  V02($A)=D53(%INT(wwInd_02))
3e   C                   ENDFOR
2e   C                   ENDIF
      * Differenza
     C                   CLEAR                   V03
2    C                   FOR       $A=1 TO $P
     C                   EVAL(RH)  V03($A)=V02($A)-V01($A)
2e   C                   ENDFOR
      * Differenza
     C                   CLEAR                   V04
2    C                   FOR       $A=1 TO $P
     C                   EVAL      £PRCNU=V01($A)
     C                   EVAL      £PRCDE=V02($A)
     C                   EXSR      £PRC
     C                   EVAL      V04($A)=£PRCPD
2e   C                   ENDFOR
      * Espone dati
      * Target
     C                   EVAL      £JAXCP=
     C                              %TRIM(AAA012)                 +'|'
     C                             +%TRIMR(SQL_CODI)              +'|'
     C                             +%TRIMR(P_RXSOS(§DECO_DESC))   +'|'
     C                             +%TRIMR(ATR(03))               +'|'
     C                             +%TRIM(T$D5O1)+%TRIM(T$D5OA)   +'|'
     C                             +%TRIM(SQL_COD1)               +'|'
     C                             +%TRIM(£DEC1_DESC)             +'|'
     C                             +%TRIM(T$D5O2)+%TRIM(T$D5OB)   +'|'
     C                             +%TRIM(SQL_COD2)               +'|'
     C                             +%TRIM(£DEC2_DESC)             +'|'
     C                             +%TRIM(T$D5O3)+%TRIM(T$D5OC)   +'|'
     C                             +%TRIM(SQL_COD3)               +'|'
     C                             +%TRIM(£DEC3_DESC)             +'|'
     C                             +'TAD5O'+%TRIM(T$D5SA)         +'|'
     C                             +%TRIM($$Tem_02)               +'|'
     C                             +%TRIM(££Tem_02)               +'|'
     C                             +'TAIGI'+%TRIM(T$D5OD)         +'|'
     C                             +%TRIM(wwInd_01)               +'|'
     C                             +%TRIM(££Ind_01)               +'|'
     C                   EVAL      AAA010='01001'
2    C                   FOR       $A=1 TO $P
     C                   EVAL      £JaxCP=%TRIM(£Jaxcp)+%TRIM(AAA010)+'|'
     C                             +%TRIM (%EDITC(V02($A):'K'))+'|'
2e   C                   ENDFOR
     C                   EXSR      £JAX_ARIG
      * Actual
     C                   EVAL      £JAXCP=
     C                              %TRIM(AAA012)                 +'|'
     C                             +%TRIMR(SQL_CODI)              +'|'
     C                             +%TRIMR(P_RXSOS(§DECO_DESC))   +'|'
     C                             +%TRIMR(ATR(03))               +'|'
     C                             +%TRIM(T$D5O1)+%TRIM(T$D5OA)   +'|'
     C                             +%TRIM(SQL_COD1)               +'|'
     C                             +%TRIM(£DEC1_DESC)             +'|'
     C                             +%TRIM(T$D5O2)+%TRIM(T$D5OB)   +'|'
     C                             +%TRIM(SQL_COD2)               +'|'
     C                             +%TRIM(£DEC2_DESC)             +'|'
     C                             +%TRIM(T$D5O3)+%TRIM(T$D5OC)   +'|'
     C                             +%TRIM(SQL_COD3)               +'|'
     C                             +%TRIM(£DEC3_DESC)             +'|'
     C                             +'TAD5O'+%TRIM(T$D5SA)         +'|'
     C                             +%TRIM($$Tem_01)               +'|'
     C                             +%TRIM(££Tem_01)               +'|'
     C                             +'TAIGI'+%TRIM(T$D5OD)         +'|'
     C                             +%TRIM(wwInd_01)               +'|'
     C                             +%TRIM(££Ind_01)               +'|'
2    C                   FOR       $A=1 TO $P
     C                   EVAL      AAA010='01001'
      * Verde
3    C                   IF        V01($A)<>0 OR V02($A)<>0
     C*****              EVAL      AAA010='01G01'
     C                   EVAL      AAA010='51G01'
      * Rosso
4    C                   IF        V03($A)<0
     C*****              EVAL      AAA010='01700'
     C                   EVAL      AAA010='51700'
4x   C                   ELSE
      * Giallo
5    C                   IF        V04($A)<10 AND V04($A)<>0
     C*****              EVAL      AAA010='01B01'
     C                   EVAL      AAA010='01B01'
5e   C                   ENDIF
4e   C                   ENDIF
3e   C                   ENDIF
     C                   EVAL      £JaxCP=%TRIM(£Jaxcp)+%TRIM(AAA010)+'|'
     C                             +%TRIM (%EDITC(V01($A):'K'))+'|'
2e   C                   ENDFOR
     C                   EXSR      £JAX_ARIG
      * Differenza
2    C                   IF        $$RigDif='Yes'                               COSTANTE
     C                   EVAL      £JAXCP=
     C                              %TRIM(AAA012)                 +'|'
     C                             +%TRIMR(SQL_CODI)              +'|'
     C                             +%TRIMR(P_RXSOS(§DECO_DESC))   +'|'
     C                             +%TRIMR(ATR(03))               +'|'
     C                             +%TRIM(T$D5O1)+%TRIM(T$D5OA)   +'|'
     C                             +%TRIM(SQL_COD1)               +'|'
     C                             +%TRIM(£DEC1_DESC)             +'|'
     C                             +%TRIM(T$D5O2)+%TRIM(T$D5OB)   +'|'
     C                             +%TRIM(SQL_COD2)               +'|'
     C                             +%TRIM(£DEC2_DESC)             +'|'
     C                             +%TRIM(T$D5O3)+%TRIM(T$D5OC)   +'|'
     C                             +%TRIM(SQL_COD3)               +'|'
     C                             +%TRIM(£DEC3_DESC)             +'|'
     C                                                            +'||'
     C                             +'Differenza'                  +'|'          COSTANTE
     C                             +'TAIGI'+%TRIM(T$D5OD)         +'|'
     C                             +%TRIM(wwInd_01)               +'|'
     C                             +%TRIM(££Ind_01)               +'|'
     C                   EVAL      AAA010='01001'
3    C                   FOR       $A=1 TO $P
     C                   EVAL      £JaxCP=%TRIM(£Jaxcp)+%TRIM(AAA010)+'|'
     C                             +%TRIM (%EDITC(V03($A):'K'))+'|'
3e   C                   ENDFOR
     C                   EXSR      £JAX_ARIG
      * % Differenza
     C                   EVAL      £JAXCP=
     C                              %TRIM(AAA012)                 +'|'
     C                             +%TRIMR(SQL_CODI)              +'|'
     C                             +%TRIMR(P_RXSOS(§DECO_DESC))   +'|'
     C                             +%TRIMR(ATR(03))               +'|'
     C                             +%TRIM(T$D5O1)+%TRIM(T$D5OA)   +'|'
     C                             +%TRIM(SQL_COD1)               +'|'
     C                             +%TRIM(£DEC1_DESC)             +'|'
     C                             +%TRIM(T$D5O2)+%TRIM(T$D5OB)   +'|'
     C                             +%TRIM(SQL_COD2)               +'|'
     C                             +%TRIM(£DEC2_DESC)             +'|'
     C                             +%TRIM(T$D5O3)+%TRIM(T$D5OC)   +'|'
     C                             +%TRIM(SQL_COD3)               +'|'
     C                             +%TRIM(£DEC3_DESC)             +'|'
     C                                                            +'||'
     C                             +'Differenza %'                +'|'          COSTANTE
     C                             +'TAIGI'+%TRIM(T$D5OD)         +'|'
     C                             +%TRIM(wwInd_02)               +'|'
     C                             +%TRIM(££Ind_02)               +'|'
     C                   EVAL      AAA010='01001'
3    C                   FOR       $A=1 TO $P
     C                   EVAL      £JaxCP=%TRIM(£Jaxcp)+%TRIM(AAA010)+'|'
     C                             +%TRIM (%EDITC(V04($A):'K'))+'|'
3e   C                   ENDFOR
     C                   EXSR      £JAX_ARIG
2e   C                   ENDIF
      *
1e   C                   ENDFOR
      *
     C                   ENDSR
      *---------------------------------------------------------------------------------------------
      * Lettura dati
      *---------------------------------------------------------------------------------------------
     C     LOD_D5C       BEGSR
      * Carico valori
     C                   CLEAR                   D53
     C                   EVAL      £D5CFU='LET'
     C                   EVAL      £D5CME='   '
     C                   EXSR      £D5C
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Routine impostazioni di avvio programma
      *--------------------------------------------------------------*
     C     £INIZI        BEGSR
     C                   EXSR      £JAX_INZP
     C                   EXSR      £JAX_INZ
     C/EXEC SQL
     C+ Declare C1 cursor for S1
     C/END-EXEC
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Log Specifico applicazione
      *--------------------------------------------------------------*
     C     £JAX_LOG      BEGSR
     C                   ENDSR
      *--------------------------------------------------------------*
********** PREPROCESSOR COPYSTART QILEGEN,£RITES
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £RITES
      * Sorgente di origine : SMEDEV/QILEGEN(£RITES)
      * Esportato il        : 20240719 095036
      *====================================================================
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
1    C                   IF        £RITAM=*BLANK
     C                   EVAL      £RITAM=££AMBI
1e   C                   ENDIF
1    C                   IF        £RITCO=''
     C                   MOVEL     ££CONT        £RITCO
1e   C                   ENDIF
1    C                   IF        £RITDT=0
     C                   MOVEL     ££DATE        £RITDT
1e   C                   ENDIF
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
  M >C                   IF        ££B£2J = '1'
  M >C                   CALL      'B£DMS7'                             37
  M >C                   PARM      'COS'         £DMS7F           10
  M >C                   PARM                    £DMS7M           10
  M >C                   PARM      'TA'          £DMS7T            2
  M >C                   PARM                    £RITST
  M >C                   PARM                    £RITEL
  M >C                   PARM                    £DMS75            1
  M >C                   ELSE
     C                   EVAL      *IN37=*OFF
     C                   CALL      'B£DMS7'
     C                   PARM      'COS'         £DMS7F           10
     C                   PARM                    £DMS7M           10
     C                   PARM      'TA'          £DMS7T            2
     C                   PARM                    £RITST
     C                   PARM                    £RITEL
     C                   PARM                    £DMS75            1
  M >C                   ENDIF
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
********** PREPROCESSOR COPYSTART QILEGEN,£PRC
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £PRC
      * Sorgente di origine : SMEDEV/QILEGEN(£PRC)
      * Esportato il        : 20240719 094425
      *====================================================================
     D* ==============================================================
     D* MODIFICHE Ril.  T Au Descrizione
     D* gg/mm/aa  nn.mm i xx Breve descrizione
     D* ==============================================================
     D* 22/05/07             Prevista variabile £PR999 per assunzione 999
     V* ££10916B  V5R1    BS *************************
     V* ££10916B  V5R1    BS **************** Rilascio intero sorgente da srvamm a SMEDEV
     V* ££10916B  V5R1    BS *************************
     D*-------------------------------------------------------------------
     D* OBIETTIVO
     D*  Calcolare, dati due valori da comparare, il rapporto in per-
     D*  centuale dell'uno sull'altro, la loro differenza e la diffe-
     D*  renza percentuale.
     D*  La crescita percentuale permette di calcolare la variazione %
     D*   ad esempio anno su anno. Esempio:
     D*   Denominatore = Anno Corrente
     D*   Numeratore   = Anno Precedente
     D*   % Crescita = (Anno Corrente - Anno precedente)/Anno precedente
     D*
     D* FLUSSO
     D*  Input:
     D*   £PRCNU : Valore numeratore             (17 5)
     D*   £PRCDE : Valore denominatore           (17 5)
     D*  Output:
     D*   £PRCVA : Valore percentuale            (17 5)
     D*   £PRCDF : Differenza : £PRCDE - £PRCNU  (17 5)
     D*   £PRCPD : Differenz percentuale         (17 5)  (£PRCDE-£PRCNU)/£PRCDE
     D*   £PRCPC : Percentaule Crescita          (17 5)  (£PRCDE-£PRCNU)/£PRCNU
     D*  Note:
     D*   Se numeratore e denominatore sono 0 la percentuale è 0
     D*   Se numeratore non è 0 e denominatore è 0 la percentuale
     D*   è 9999...
     D*   Se percentuale è maggiore di 999 viene forzata a 99999...
     D*
     C*---------------------------------------------------------------------
     C* ESEMPIO DI RICHIAMO
     C*
     C* INPUT
     C*                  EVAL      £PRCNU=Numeratore
     C*                  EVAL      £PRCDE=Denominatore
     C*                  EXSR      £PRC
     C* OUTPUT
     C*                  EVAL      Percentuale=£PRCVA
     C*                  EVAL      Differenza=£PRCDF
     C*                  EVAL      Differenza_perc=£PRCPD
     C*                  EVAL      Crescita_perc=£PRCPC
     D*-------------------------------------------------------------------
     C     £PRC          BEGSR
     C*
     C* Definizione campi
     C                   Z-ADD     £PRCNU        £PRCNU           17 5
     C                   Z-ADD     £PRCDE        £PRCDE           17 5
     C                   Z-ADD     £PRCVA        £PRCVA           17 5
     C                   Z-ADD     £PRCDF        £PRCDF           17 5
     C                   Z-ADD     £PRCPD        £PRCPD           17 5
     C                   Z-ADD     £PRCPC        £PRCPC           17 5
     C*
     C*  Calcolo percentuale
1    C     £PRCDE        IFEQ      0
2    C     £PRCNU        IFEQ      0
     C                   Z-ADD     0             £PRCVA
2x   C                   ELSE
     C                   Z-ADD     *ALL'9'       £PRCVA
2e   C                   END
1x   C                   ELSE
     C     £PRCNU        DIV       £PRCDE        £PRCVA
     C                   MULT      100           £PRCVA
1e   C                   END
     C*
     C     £PRCVA        IFGT      999
     C                   Z-ADD     *ALL'9'       £PRCVA
     C                   END
     C*
     C*  Calcolo differenza
     C     £PRCDE        SUB       £PRCNU        £PRCDF
     C*
     C*  Calcolo differenza percentuale
1    C     £PRCDE        IFEQ      0
2    C     £PRCDF        IFEQ      0
     C                   Z-ADD     0             £PRCPD
2x   C                   ELSE
     C                   Z-ADD     *ALL'9'       £PRCPD
2e   C                   END
1x   C                   ELSE
     C     £PRCDF        DIV       £PRCDE        £PRCPD
     C                   MULT      100           £PRCPD
1e   C                   END
     C*  Calcolo Crescita   percentuale
1    C     £PRCNU        IFEQ      0
2    C     £PRCDF        IFEQ      0
     C                   Z-ADD     0             £PRCPC
2x   C                   ELSE
     C                   Z-ADD     *ALL'9'       £PRCPC
2e   C                   END
1x   C                   ELSE
     C     £PRCDF        DIV       £PRCNU        £PRCPC
     C                   MULT      100           £PRCPC
1e   C                   END
     C*
     C                   IF        £PR999 = ' '
     C     £PRCPC        IFGT      999
     C                   Z-ADD     *ALL'9'       £PRCPC
     C                   END
     C                   END
     C*
     C                   MOVE      ' '           £PR999            1
     C                   ENDSR
     C*--------------------------------------------------------------*
********** PREPROCESSOR COPYEND QILEGEN,£PRC
      /API QILEGEN,£DEC
********** PREPROCESSOR COPYSTART QILEGEN,£G43
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £G43
      * Sorgente di origine : SMEDEV/QILEGEN(£G43)
      * Esportato il        : 20240719 094558
      *====================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* B£00722A  V3R1    CM Rimozione Gestione Interna degli errori
     V* 31/05/18  V5R1    CM Aggiunto Enclosure character (carattere di recinzione)
     V* 01/06/18  V5R1    PEDSTE Rilascio
     D*----------------------------------------------------------------
     D* OBIETTIVO
     D*  Gestire Composizione/Scomposizione file CSV
     D* NOTA
     D*
     D* FLUSSO
     D*  prerequisiti:
     D*
     D*D/COPY QILEGEN,£G43E
     D*D/COPY QILEGEN,£G43DS
     D*
     D* FUNZIONI/METODI
     D*  COM       Composizione
     D*   .INI       Inizializzazione
     D*   .ELB       Elaborazione
     D*  SCO       Scomposizione
     D*   .INI       Inizializzazione
     D*   .ELB       Elaborazione
     D*----------------------------------------------------------------
     D* ESEMPIO DI CHIAMATA
     C*                     MOVEL'<Funz. >'£G43FU    P
     C*                     MOVEL'<Metodo>'£G43ME    P
     C*                     Z-ADD'<N°el. >'£G43NC
     C*                     MOVEL'<Carat.>'£G43CS    P
     C*                     MOVEL'<Strin.>'£G43RC    P
     C*                     EXSR £G43
     D*----------------------------------------------------------------
     C     £G43          BEGSR
      *
     C                   MOVEL     '0'           £G4335
     C                   MOVEL     '0'           £G4336
      *
  MO>C                   IF        ££B£2J = '1'
  MO>C                   CALL      'B£G43E'                             37
  MO>C                   PARM                    £G43FU           10
  MO>C                   PARM                    £G43ME           10
  MO>C                   PARM                    £G43NC            5 0
  MO>C                   PARM                    £G43CS            1
  MO>C                   PARM                    £G43RC
  MO>C                   PARM                    £43A
  MO>C                   PARM                    £43N
  MO>C                   PARM                    £G43MS            7
  MO>C                   PARM                    £G43FI           10
  MO>C                   PARM                    £G43CM            2
  MO>C                   PARM                    £G4335            1
  MO>C                   PARM                    £G4336            1
  MO>C                   PARM                    £G43EC            1
  MO> *
  MO>C                   ELSE
     C                   EVAL      *IN37=*OFF
     C                   CALL      'B£G43E'
     C                   PARM                    £G43FU           10
     C                   PARM                    £G43ME           10
     C                   PARM                    £G43NC            5 0
     C                   PARM                    £G43CS            1
     C                   PARM                    £G43RC
     C                   PARM                    £43A
     C                   PARM                    £43N
     C                   PARM                    £G43MS            7
     C                   PARM                    £G43FI           10
     C                   PARM                    £G43CM            2
     C                   PARM                    £G4335            1
     C                   PARM                    £G4336            1
     C                   PARM                    £G43EC            1
      *
  MO>C                   ENDIF
  MO>C   37              DO
  MO>C                   CALL      'B£GGP0  '
  MO>C                   PARM      'B£G43G'      £GGPNP           10
  MO>C                   PARM      *BLANKS       £GGPTP           10
  MO>C                   PARM      *BLANKS       £GGPPA          100
  MO>C                   ENDDO
      *
     C     £G4335        COMP      *ON                                    35
     C     £G4336        COMP      *ON                                    36
      *
     C                   ENDSR
********** PREPROCESSOR COPYEND QILEGEN,£G43
********** PREPROCESSOR COPYSTART QILEGEN,£G40
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £G40
      * Sorgente di origine : SMEDEV/QILEGEN(£G40)
      * Esportato il        : 20240719 095032
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
********** PREPROCESSOR COPYEND QILEGEN,£G40
********** PREPROCESSOR COPYSTART QILEGEN,£PE8
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £PE8
      * Sorgente di origine : SMEDEV/QILEGEN(£PE8)
      * Esportato il        : 20240719 094438
      *====================================================================
     D*----------------------------------------------------------------
     D* OBIETTIVO
     D*
     D*  Derivare i dati relativi al periodo o l'esercizio contabile
     D*
     D* FLUSSO
     D*
     D* Input :    £PE8FU Funzione
     D*            £PE8ME Metodo
     D*              £PE8EI Esercizio  (anno)
     D*              £PE8PI Periodo esercizio
     D*              £PE8DI Data iniziale periodo
     D*              £PE8DS     512
     D* Output :     £PE8EO Esercizio  (anno)
     D*              £PE8PO Periodo esercizio
     D*              £PE8DI Data iniziale periodo
     D*              £PE8DF Data finale periodo
     D*              £PE8DS     512
     D*
     D* PREREQUISITI
     D* --------------------
     D*D/COPY QILEGEN,£PE8DS
     D* -------------------------------------------------------------*
     D* ESEMPIO DI CHIAMATA
      * Input :
     C*                  EVAL      £PE8FU='Funzione'
     C*                  EVAL      £PE8ME='Metodo'
     C*                  EVAL      £PE8EI= Esercizio
     C*                  EVAL      £PE8PI= Periodo_esercizio
     C*                  EVAL      £PE8DI= Data_iniziale_periodo
      *
     C*                  EXSR      £PE8
      * Output :
     C*                  EVAL      A512                 = £PE8DS
     C*                  EVAL      Esercizio            = £PE8EO
     C*                  EVAL      Periodo_esercizio    = £PE8PO
     C*                  EVAL      Data_iniziale_periodo= £PE8DI
     C*                  EVAL      Data_finale_periodo  = £PE8DF
     D*--------------------------------------------------------------*
     C     £PE8          BEGSR
      *  Ricerco parametri
     C                   CALL      'B£PER8'
     C                   PARM                    £PE8FU           10
     C                   PARM                    £PE8ME           10
     C                   PARM                    £PE8DS
     C                   PARM                    £PE8MS            7
     C                   PARM                    £PE8FI           10
     C                   PARM      '0'           £PE835            1
     C                   PARM      '0'           £PE836            1
      *
     C                   MOVE      £PE835        *IN35
     C                   MOVE      £PE836        *IN36
      *
1e   C                   ENDSR
********** PREPROCESSOR COPYEND QILEGEN,£PE8
********** PREPROCESSOR COPYSTART QILEGEN,£D5C
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £D5C
      * Sorgente di origine : SMEDEV/QILEGEN(£D5C)
      * Esportato il        : 20240719 094425
      *====================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* B£50315A  V2R1  i PV Aggiunto livello di chiamata
     V* B£00722A  V3R1    CM Rimozione Gestione Interna degli errori
     V* 18/03/22  003644  LANKAT Funzione CAL.CRI
     V* 18/03/22  V6R1    BERNI Check-out 003644 in SMEUP_TST
     V* 16/05/22  003827  BS Roll-back modifica precedente
     V* 17/05/22  V6R1    BERNI Check-out 003827 in SMEDEV
     V* ==============================================================
     D* OBIETTIVO
     D* Calcolo valori del D5COSO
     D*
     D* PREREQUISITI
     D*D/COPY QILEGEN,£D5CE  (schiere)
     D*
     D* CONSIGLIATO
     D*ID5COSO    E DSD5COSO0F
     D*I                                    P  8811766D50
     D*
     D* OPZIONALE
     D*D/COPY QILEGEN,£D52DS (DS della schiera significati)
     D*
     D* FLUSSO
     D*
     D*----------------------------------------------------------------
     D*  Parametri
     D*  Input:  £D5CFU: Funzione (10)
     D*          £D5CME: Metodo   (10)
     D*          Funzioni
     D*            CAL : Calcolo valori
     D*            CAR : Calcolo valori con campi D5COSO
     D*             .    : del D5COSO
     D*             .A   : sulle riclassifiche A
     D*             .B   : sulle riclassifiche B
     D*            SCE : Scelta valori
     D*             .nn  : eventuale indice IGI
     D*            SIG : Significato dei valori -> N.B.:azzera D50 !!!
     D*             .    : del D5COSO
     D*             .A   : sulle riclassifiche A
     D*             .B   : sulle riclassifiche B
     D*
     D*  Output: schiere valori e significati
     D*          £D5CMS: Codice ritorno (7)
     D*          £D5CCM: Comando
     D*
     D* ESEMPIO DI CHIAMATA
     D*C                     MOVEL<Funzione>£D5CFU
     D*C                     MOVEL<Metodo  >£D5CME
     D*C                     MOVELD$TIPA    £D5CCO    P
     D*C                     MOVELD$TROT    £D5CTE    P
     D*C                     MOVELD$CODI    £D5CCD    P
     D*C                     MOVELD$COD1    £D5CC1    P
     D*C                     MOVELD$COD2    £D5CC2    P
     D*C                     MOVELD$COD3    £D5CC3    P
     D*C                     MOVELD$DTVA    £D5CPE    P
     D*C                     Z-ADDQTF       D50
     D*C                     EXSR £D5C
     D*C                     Z-ADDD50       QTF
     D*
     D* ESEMPIO DI CHIAMATA CON CHAIN PRECEDENTE SUL D5COSO CON DS
     D*C                     MOVEL'CAR'     £D5CFU
     D*C                     MOVEL<Metodo  >£D5CME
     D*C                     EXSR £D5C
     D*----------------------------------------------------------------
     C     £D5C          BEGSR
      * Caricamento automatico campi da record D5COSO
     C                   SELECT
     C     £D5CFU        WHENEQ    'CAR'
     C                   MOVEL(P)  D$TIPA        £D5CCO
     C                   MOVEL(P)  D$TROT        £D5CTE
     C                   MOVEL(P)  D$CODI        £D5CCD
     C                   MOVEL(P)  D$COD1        £D5CC1
     C                   MOVEL(P)  D$COD2        £D5CC2
     C                   MOVEL(P)  D$COD3        £D5CC3
     C                   MOVEL(P)  D$DTVA        £D5CPE
     C                   MOVEL(P)  'CAL'         £D5CFU
     C     £D5CFU        WHENEQ    'SIG'
     C                   Z-ADD     0             D50
     C                   ENDSL
      * Richiamo programma di calcolo
     C                   MOVEL(P)  'D5CO01E'     £D5CPG           10
     C                   CAT       £D5CLC:0      £D5CPG
  MO>C                   IF        ££B£2J = '1'
  MO>C                   CALL      £D5CPG                               37
  MO>C                   PARM                    £D5CFU           10            Funzione
  MO>C                   PARM                    £D5CME           10            Metodo
  MO>C                   PARM                    £D5CCO           12            tab D5S
  MO>C                   PARM                    £D5CCD           15            codice
  MO>C                   PARM                    £D5CTE            3            Estensione
  MO>C                   PARM                    £D5CC1           15            codice 1
  MO>C                   PARM                    £D5CC2           15            codice 2
  MO>C                   PARM                    £D5CC3           15            codice 3
  MO>C                   PARM                    £D5CPE           10            data/periodo
  MO>C                   PARM      D50           D51
  MO>C                   PARM                    D52
  MO>C     D50           PARM                    D53
  MO>C                   PARM      *BLANKS       £D5CCM            2            Ultimo Cmd
  MO>C                   PARM                    £D5CMS            7            MSG
  MO> *
  MO>C                   ELSE
     C                   EVAL      *IN37=*OFF
     C                   CALL      £D5CPG
     C                   PARM                    £D5CFU           10            Funzione
     C                   PARM                    £D5CME           10            Metodo
     C                   PARM                    £D5CCO           12            tab D5S
     C                   PARM                    £D5CCD           15            codice
     C                   PARM                    £D5CTE            3            Estensione
     C                   PARM                    £D5CC1           15            codice 1
     C                   PARM                    £D5CC2           15            codice 2
     C                   PARM                    £D5CC3           15            codice 3
     C                   PARM                    £D5CPE           10            data/periodo
     C                   PARM      D50           D51
     C                   PARM                    D52
     C     D50           PARM                    D53
     C                   PARM      *BLANKS       £D5CCM            2            Ultimo Cmd
     C                   PARM                    £D5CMS            7            MSG
      *
  MO>C                   ENDIF
  MO>C   37              DO
  MO>C                   CALL      'B£GGP0  '
  MO>C                   PARM      £D5CPG        £GGPNP           10
  MO>C                   PARM      *BLANKS       £GGPTP           10
  MO>C                   PARM      *BLANKS       £GGPPA          100
  MO>C                   ENDDO
      * Solo per dichiarazioni variabili
     C     1             IFEQ      2
     C                   MOVEL     *BLANKS       D$TIPA           12
     C                   MOVEL     *BLANKS       D$TROT            3
     C                   MOVEL     *BLANKS       D$CODI           15
     C                   MOVEL     *BLANKS       D$COD1           15
     C                   MOVEL     *BLANKS       D$COD2           15
     C                   MOVEL     *BLANKS       D$COD3           15
     C                   MOVEL     *BLANKS       D$DTVA           10
     C                   MOVEL     £D5CLC        £D5CLC            1
     C                   ENDIF
      *
     C                   ENDSR
********** PREPROCESSOR COPYEND QILEGEN,£D5C
      /API QILEGEN,£OAV
********** PREPROCESSOR COPYSTART QILEGEN,£DA8
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £DA8
      * Sorgente di origine : SMEDEV/QILEGEN(£DA8)
      * Esportato il        : 20240719 095137
      *====================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 17/06/15          AS Aggiunto Livello di chiamata
     V* 11/11/15  V4R1    AS Rilascio modifiche del 17/06/15
     V* ==============================================================
     D*----------------------------------------------------------------   FRF13
     D* OBIETTIVO
     D*
     D*  Azioni sui campi di tipo DATA:
     D*  - controlli formali
     D*  - conversioni
     D*  - determinazione caratteristiche (giorno, settimana, mese ..)
     D*  - calcoli
     D*
     D* FLUSSO
     D*  Input:
     D*    £DA8IA = Data da convertire alf.   (1) 10    Alfanumerico
     D*    £DA8IN = Data da convertire num.   (1)  8.0  Numerico
     D*    £DA8CD = Codice data               (2) 10    Alfanumerico
     D*    £DA8IF = Formato data input        (3)  8    Alfanumerico
     D*    £DA8OF = Formato data output       (3)  8    Alfanumerico
     D*    £DA8SP = Formato separat.output    (4)  7    Alfanumerico
     D*    £DA8A1 = Azione 1 su data          (5)  8    Alfanunerico
     D*    £DA8A2 = Azione 2 su data          (5)  8    Alfanunerico
     D*    £DA8A3 = Azione 3 su data          (5)  8    Alfanunerico
     D*    £DA8O1 = Opzione 1 su data              8    Alfanunerico
     D*    £DA8O2 = Opzione 2 su data              8    Alfanunerico
     D*    £DA8O3 = Opzione 3 su data              8    Alfanunerico
     D*    £DA8QY = Quantita' per azione I/D       8.0  Numerico
     D*    £DA8LC = Livello di richiamo       (6)  1
     D*
     D*    (1) Se impostati entrambi dà precedenza al numerico
     D*    (2) Elemento della tabella B£4
     D*    (3) Elemento della tabella *CN/D1
     D*        Campo non ripulito all'uscita della routine
     D*    (4) Elemento della tabella *CN/D3
     D*        Campo non ripulito all'uscita della routine
     D*    (5) Elemento della tabella *CN/D4
     D*    (6) Le repliche non sono utilizzabili liberamente. Le repliche esistenti sono
     D*        riservate per alcuni programmi specifici
     D*
     D* Output:
     D*    £DA8OA = Data nel formato convertito   10    Alfanumerico
     D*    £DA8ON = Data nel formato convertito    8.0  Numerico
     D*    £DA8GA = Giorno da inizio anno          3.0  Numerico
     D*    £DA8GM = Giorno nel mese                2.0  Numerico
     D*    £DA8GS = Giorno nella settimana         1.0  Numerico
     D*    £DA8DG = Descrizione giorno            10    Alfanumerico
     D*    £DA8MA = Mese dell'anno                 2.0  Numerico
     D*    £DA8DM = Descrizione del mese          10    Alfanumerico
     D*    £DA8SA = Settimana nell'anno            2.0  Numerico
     D*    £DA8AP = Anno di apparenenza settimana  4.0  Numerico
     D*    £DA8AN = Anno                           4.0  Numerico
     D*    £DA8NG = N.Giorni da XX.XX.XX           8.0  Numerico
     D*    £DA8RC = Messaggio d'errore             7    Alfanumerico
     D*    *IN36  = ON se da rivisualizzare
     D*
     D* ESEMPIO DI CHIAMATA
     D*C                     MOVEL<data>    £DA8IA
     D*C oppure              Z-ADD<data>    £DA8IN
     D*C                     MOVEL<cod.dat> £DA8CD
     D*C                     MOVEL<fmt_in>  £DA8IF   P
     D*C                     MOVEL<fmt_out> £DA8OF
     D*C                     MOVEL<separt.> £DA8SP   P
     D*C                     MOVEL<azione1> £DA8A1
     D*C                     MOVEL<opzion1> £DA8O1
     D*C                     MOVEL<azione2> £DA8A2
     D*C                     MOVEL<opzion2> £DA8O2
     D*C                     MOVEL<azione3> £DA8A3
     D*C                     MOVEL<opzion3> £DA8O3
     D*C                     Z-ADD<qtà>     £DA8QY
     D*C                     EXSR £DA8
     D*C                     MOVEL£DA8OA    <data a>
     D*C                     Z-ADD£DA8ON    <data n>
     D*C                     Z-ADD£DA8GA    <gio an>
     D*C                     Z-ADD£DA8GM    <giomes>
     D*C                     Z-ADD£DA8GS    <gioset>
     D*C                     MOVEL£DA8DG    <giorno>
     D*C                     Z-ADD£DA8MA    <mesann>
     D*C                     MOVEL£DA8DM    <mese  >
     D*C                     Z-ADD£DA8SA    <setann>
     D*C                     Z-ADD£DA8AP    <an.set>
     D*C                     Z-ADD£DA8AN    <anno>
     D*C                     Z-ADD£DA8NG    <numgio>
     D*C                     MOVEL£DA8RC    <errore>
     D*----------------------------------------------------------------   FRF13
     C     £DA8          BEGSR
     D*
     D* Pulizia campi di output
     C                   CLEAR                   £DA8OA
     C                   CLEAR                   £DA8ON
     C                   CLEAR                   £DA8GA
     C                   CLEAR                   £DA8GM
     C                   CLEAR                   £DA8GS
     C                   CLEAR                   £DA8DG
     C                   CLEAR                   £DA8MA
     C                   CLEAR                   £DA8DM
     C                   CLEAR                   £DA8SA
     C                   CLEAR                   £DA8AP
     C                   CLEAR                   £DA8AN
     C                   CLEAR                   £DA8NG
     C                   CLEAR                   £DA8RC
     C                   MOVEL     '0'           £DA836
     C                   SETOFF                                       36
      *
      * Indicatore lasciato volutamente in quanto il funzionamento del B£DAT8 si basa sul fatto
      * che vada in errore
     C                   EVAL      £DA8PG='B£DAT8'+£DA8LC
     C                   CALL      £DA8PG                               37
     C                   PARM                    £DA8IA                         -->
     C                   PARM                    £DA8IN                         -->
     C                   PARM                    £DA8CD                         -->
     C                   PARM                    £DA8IF                         -->
     C                   PARM                    £DA8OF                         -->
     C                   PARM                    £DA8SP                         -->
     C                   PARM                    £DA8A1                         -->
     C                   PARM                    £DA8A2                         -->
     C                   PARM                    £DA8A3                         -->
     C                   PARM                    £DA8O1                         -->
     C                   PARM                    £DA8O2                         -->
     C                   PARM                    £DA8O3                         -->
     C                   PARM                    £DA8QY                         -->
      *
     C                   PARM                    £DA8OA                         <--
     C                   PARM                    £DA8ON                         <--
     C                   PARM                    £DA8GA                         <--
     C                   PARM                    £DA8GM                         <--
     C                   PARM                    £DA8GS                         <--
     C                   PARM                    £DA8DG                         <--
     C                   PARM                    £DA8MA                         <--
     C                   PARM                    £DA8DM                         <--
     C                   PARM                    £DA8SA                         <--
     C                   PARM                    £DA8AP                         <--
     C                   PARM                    £DA8AN                         <--
     C                   PARM                    £DA8NG                         <--
     C                   PARM                    £DA8RC                         <--
     C                   PARM                    £DA836            1            <--
     D*
     C                   MOVEL     £DA836        *IN36
     D*
     D* Pulizia campi di input
     C                   CLEAR                   £DA8IA
     C                   CLEAR                   £DA8IN
     C                   CLEAR                   £DA8CD
     C                   CLEAR                   £DA8A1
     C                   CLEAR                   £DA8A2
     C                   CLEAR                   £DA8A3
     C                   CLEAR                   £DA8O1
     C                   CLEAR                   £DA8O2
     C                   CLEAR                   £DA8O3
     C                   CLEAR                   £DA8QY
     C                   CLEAR                   £DA8LC
     D*
     D* Definizione campi
     C     'A'           IFEQ      'B'
     D* ... input
     C                   MOVEL     £DA8IA        £DA8IA           10
     C                   Z-ADD     £DA8IN        £DA8IN            8 0
     C                   MOVEL     £DA8CD        £DA8CD           10
     C                   MOVEL     £DA8IF        £DA8IF            8
     C                   MOVEL     £DA8OF        £DA8OF            8
     C                   MOVEL     £DA8SP        £DA8SP            7
     C                   MOVEL     £DA8A1        £DA8A1            8
     C                   MOVEL     £DA8A2        £DA8A2            8
     C                   MOVEL     £DA8A3        £DA8A3            8
     C                   MOVEL     £DA8O1        £DA8O1            8
     C                   MOVEL     £DA8O2        £DA8O2            8
     C                   MOVEL     £DA8O3        £DA8O3            8
     C                   Z-ADD     £DA8QY        £DA8QY            8 0
     D* ... output
     C                   MOVEL     £DA8OA        £DA8OA           10
     C                   Z-ADD     £DA8ON        £DA8ON            8 0
     C                   Z-ADD     £DA8GA        £DA8GA            3 0
     C                   Z-ADD     £DA8GM        £DA8GM            2 0
     C                   Z-ADD     £DA8GS        £DA8GS            1 0
     C                   MOVEL     £DA8DG        £DA8DG           10
     C                   Z-ADD     £DA8MA        £DA8MA            2 0
     C                   MOVEL     £DA8DM        £DA8DM           10
     C                   Z-ADD     £DA8SA        £DA8SA            2 0
     C                   Z-ADD     £DA8AP        £DA8AP            4 0
     C                   Z-ADD     £DA8AN        £DA8AN            4 0
     C                   Z-ADD     £DA8NG        £DA8NG            8 0
     C                   MOVEL     £DA8RC        £DA8RC            7
     C                   MOVEL     £DA8LC        £DA8LC            1
     C                   MOVEL     £DA8PG        £DA8PG           10
     C                   ENDIF
      *
      * Intercettazione errore
     C     *IN37         IFEQ      *ON
     C                   CALL      'B£GGP0  '
     C                   PARM      £DA8PG        £GGPNP           10
     C                   PARM      *BLANKS       £GGPTP           10
     C                   PARM      *BLANKS       £GGPPA          100
     C                   ENDIF
      *
     C                   ENDSR
********** PREPROCESSOR COPYEND QILEGEN,£DA8
********** PREPROCESSOR COPYSTART QILEGEN,£D5_139
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £D5_139
      * Sorgente di origine : SMEDEV/QILEGEN(£D5_139)
      * Esportato il        : 20240719 094425
      *====================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 20/12/20  V5R1    BS Creazione
     V* ££10916A  V5R1    BERNI **********************
     V* ££10916A  V5R1    BERNI ************* RILASCIO INTERO SORGENTE DA SMEUP_TST A SMEDEV
     V* ££10916A  V5R1    BERNI **********************
     D* ==============================================================
     D*----------------------------------------------------------------
     D* OBIETTIVO
     D*
     D* PREREQUISITI
     D*    /COPY QILEGEN,£D5_139DS
     D*
     D*----------------------------------------------------------------
     C     £D5_139       BEGSR
      *
     C                   MOVEL(P)  'D5_1390'     £139PG           10
      *
     C                   CALL      £139PG
     C                   PARM                    £139DSI
     C                   PARM                    £139DSO
     C                   PARM                    £139DSK
      *
     C                   CLEAR                   £139LC            1
     C                   EVAL      *IN35=£139O_35
      *
     C                   ENDSR
     D*----------------------------------------------------------------
********** PREPROCESSOR COPYEND QILEGEN,£D5_139
********** PREPROCESSOR COPYSTART QILEGEN,£JAX_C
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 15/03/21  V6R1    BONMAI Creazione
     V* ==============================================================
      *--------------------------------------------------------------------------------------------*
      * Service Initialization
      *--------------------------------------------------------------------------------------------*
     C     £JAX_INZ      BEGSR
     C                   ENDSR
      *--------------------------------------------------------------------------------------------*
      * Smeup Data Structure - Initialization
      *--------------------------------------------------------------------------------------------*
     C     £JAX_IMP0     BEGSR
     C                   CALL      '£JAX_IMP0'
     C                   ENDSR
      *--------------------------------------------------------------------------------------------*
      * Smeup Data Structure - Finalization
      *--------------------------------------------------------------------------------------------*
     C     £JAX_FIN0     BEGSR
     C                   CALL      '£JAX_FIN0'
     C                   ENDSR
      *--------------------------------------------------------------------------------------------*
      * @Deprecated
      * Generic routine to write directly to buffer
      *--------------------------------------------------------------------------------------------*
     C     £JAX_ADD      BEGSR
     C                   CALL      '£JAX_ADD'
     C                   PARM                    £JAXCP
     C                   ENDSR
      *--------------------------------------------------------------------------------------------*
      * Smeup Tree - write a node
      *--------------------------------------------------------------------------------------------*
     C     £JAX_ADDO     BEGSR
      *
     C                   CALL      '£JAX_ADDO'
     C                   PARM                    £JAXT1
     C                   PARM                    £JAXP1
     C                   PARM                    £JAXK1
     C                   PARM                    £JAXD1
     C                   PARM                    £JAXOP
     C                   PARM                    £JAXEN
      *
     C                   CLEAR                   £JAXT1
     C                   CLEAR                   £JAXP1
     C                   CLEAR                   £JAXK1
     C                   CLEAR                   £JAXD1
     C                   CLEAR                   £JAXOP
     C                   CLEAR                   £JAXEN
      *
     C                   ENDSR
      *--------------------------------------------------------------------------------------------*
      * Smeup Tree - Close children node
      *--------------------------------------------------------------------------------------------*
     C     £JAX_CLOO     BEGSR
     C                   CALL      '£JAX_CLOO'
     C                   ENDSR
      *--------------------------------------------------------------------------------------------*
      * @OnlyForRetrocompatibility
      * Smeup Table - Write table header initialization
      *--------------------------------------------------------------------------------------------*
     C     £JAX_AGRI_I   BEGSR
     C                   ENDSR
      *--------------------------------------------------------------------------------------------*
      * @OnlyForRetrocompatibility
      * Smeup Table - Write table header finalization
      *--------------------------------------------------------------------------------------------*
     C     £JAX_AGRI_F   BEGSR
     C                   ENDSR
      *--------------------------------------------------------------------------------------------*
      * Smeup Table - Write table column
      *--------------------------------------------------------------------------------------------*
     C     £JAX_ACOL     BEGSR
      *
     C                   CALL      '£JAX_ACOL'
     C                   PARM                    £JAXDSCOL
     C                   PARM                    £JAXDSCO2
     C                   PARM                    £JAXDSCO3
      *
     C                   CLEAR                   £JAXDSCOL
     C                   CLEAR                   £JAXDSCO2
     C                   CLEAR                   £JAXDSCO3
      *
     C                   ENDSR
      *--------------------------------------------------------------------------------------------*
      * Smeup Table - Write table header
      *--------------------------------------------------------------------------------------------*
     C     £JAX_AGRI     BEGSR
      *
     C                   CALL      '£JAX_AGRI'
     C                   PARM                    £JAXSWK
      *
     C                   ENDSR
      *--------------------------------------------------------------------------------------------*
      * @OnlyForRetrocompatibility
      * Smeup Table - Write row initialization
      *--------------------------------------------------------------------------------------------*
     C     £JAX_ARIG_I   BEGSR
     C                   ENDSR
      *--------------------------------------------------------------------------------------------*
      * @OnlyForRetrocompatibility
      * Smeup Table - Write row finalization
      *--------------------------------------------------------------------------------------------*
     C     £JAX_ARIG_F   BEGSR
     C                   ENDSR
      *--------------------------------------------------------------------------------------------*
      * Smeup Table - Write row
      *--------------------------------------------------------------------------------------------*
     C     £JAX_ARIG     BEGSR
     C                   CALL      '£JAX_ARIG'
     C                   PARM                    £JAXCP
     C                   ENDSR
      *--------------------------------------------------------------------------------------------*
      * @OnlyForRetrocompatibility
      * Smeup Message - Write message initialization
      *--------------------------------------------------------------------------------------------*
     C     £JAX_AMES_I   BEGSR
     C                   ENDSR
      *--------------------------------------------------------------------------------------------*
      * @OnlyForRetrocompatibility
      * Smeup Message - Write message finalization
      *--------------------------------------------------------------------------------------------*
     C     £JAX_AMES_F   BEGSR
     C                   ENDSR
      *--------------------------------------------------------------------------------------------*
      * Smeup Message - Write message
      *--------------------------------------------------------------------------------------------*
     C     £JAX_AMES     BEGSR
     C                   CALL      '£JAX_AMES'
     C                   PARM                    £JAXDSMSG
     C                   ENDSR
      *--------------------------------------------------------------------------------------------*
      * Smeup Message - Write message to buffer
      *--------------------------------------------------------------------------------------------*
     C     £JAX_BMES     BEGSR
     C                   CALL      '£JAX_BMES'
     C                   PARM                    £JAXDSMSG
     C                   ENDSR
      *--------------------------------------------------------------------------------------------*
      * Smeup Raw Data - Write generic data
      *--------------------------------------------------------------------------------------------*
     C     £JAX_ADDCON   BEGSR
     C                   CALL      '£JAX_ADDCON'
     C                   PARM                    £JAXCP
     C                   ENDSR
      *--------------------------------------------------------------------------------------------*
********** PREPROCESSOR COPYSTART QILEGEN,£JAX_C9
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 15/03/21  V5R1    MB Creazione
     V* /COPY £JAX_C9
     V* ==============================================================
      *--------------------------------------------------------------------------------------------*
     C     £JAX_INZP     BEGSR
      *
     C     *ENTRY        PLIST
     C                   PARM                    £UIBDS
      *
     C                   ENDSR
********** PREPROCESSOR COPYEND QILEGEN,£JAX_C9
********** PREPROCESSOR COPYEND QILEGEN,£JAX_C
********** PREPROCESSOR COPYSTART QILEGEN,£JAX_O
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £JAX_O
      * Sorgente di origine : SMEDEV/QILEGEN(£JAX_O)
      * Esportato il        : 20240719 095037
      *====================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* 13/05/05  V2R1    CM Raggruppamento /COPY di comunicazione
     V* B£30901B  V4R1    CM Aggiounto prototipo J15 per gestione Setup
     V* 06/10/16  V5R1    PEDSTE Eliminata /COPY £JAX_O0 per nuova gestione prtf in JAJAX3
     V* ==============================================================
      */COPY QILEGEN,£JAX_O0
********** PREPROCESSOR COPYSTART QILEGEN,£JAX_PC1
      *IF NOT DEFINED(JAX_PC1_INCLUDED)
      *DEFINE JAX_PC1_INCLUDED
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*===============================================================
     V* 04/10/05  V2R1  i BS Portate da 4 a 5 cifre variabili numeriche P_RxLATE
     V* 05/06/06  V2R1  i CC Ottimizzazioni P_RxSOC
     V* 14/12/06  V2R2    AS Aggiunte istruzioni per compilazione condizionale
     V* 22/03/07  V2R2    AS Corretto errore in RxELE in caso di nodo cercato a fine stringa
     V* 20/04/07  V2R2    AS Aggiunti due parametri output opzionali a RxELE (inizio nodo e lungh.)
     V* 31/05/07  V2R2    GR/AS Estensione campi da 256 a 2560
     V* 31/07/07  V2R3    AS Rilascio modifiche del 31/05/07
     V* 15/05/08  V2R3    BS Attributo VARYING su variabile $XMLVAL
     V* 28/01/09  V2R3    AS Corretta gestione sostituzione per HTML e non XML in RxSOS
     V* 16/04/09  V2R3    BS Portate variabili RxSOS da 2560 a 30000
     V* 20/07/09  V2R3    BMA Portate variabili RxVAL da 2560 a 30000
     V*                      Attributo VARYING su variabile $XmlVAL in RxATT
     V* 17/09/09  V2R3    AMA+BT Ristrutturazione delle procedure
     V*                          RxATV e RxVAL (temporaneamente sono P_RxVALNew e P_RxATVNew)
     V* 29/09/09  V2R3    AMA+BT Ristrutturazione della procedure RxATT
     V*                          (temporaneamente è P_RxATTNew)
     V* 26/01/10  V2R3    AMA+BT Rilasciate nuove versioni  P_RxVAL e P_RxATV,P_RxATT
     V* 28/01/10  V2R3    BS Rettifiva P_RxVAL se stringa nulla
     V* 24/03/10  V2R3    CM Riabitata restituzione del flag di attributo trovato
     V* 30/04/10  V2R3    AS Inserita gestione RxSOSNew
     V* 03/05/10  V2R3    BT Inserito parametro aggiuntivo in P_RxATV
     V* 19/07/10  V3R1    AS Rilasciata RxSOSNew come RxSOS
     V* 30/08/10  V3R1    BS Gestione Estrazione Attributi che iniziano con uno spazio
     V* 15/10/10  V3R1    GR P_RxSPL
     V* 19/11/10  V3R1    BMA Portata variabile ritorno RxLate da 30000 a 32766
     V* 22/11/10  V3R1    BMA Se xml di input vuoto rxVal restituisce vuoto ed esce
     V* 16/12/10  V3R1    RM Aggiunto parametro output opzionale a RxELE (contenuto tag)
     V* 12/04/11  V3R1   BMA RxATP per restituzione lista attributi e valori elementi con parentesi
     V* 18/04/11  V3R1   BMA Correzione RxATP
     V* 30/09/11  V3R2   BMA Gestita sostituzione CR e LF in RxSOS e RxSOC
     V* 20/10/11  V3R2   BMA Rilasciata modifica del 30/09
     V* 01/12/11  V3R2    AS Portato output RxATT a 30000 (il campo interno lo era già)
     V* 23/02/12  V3R2    BS Aggiunto Parametro Opzione RxATT per controllo livello 1
     V* 12/03/12  V3R2    CM Gestione pipe in RxSOS
     V* 21/03/12  V3R2    CM Gestione STXT e ETXT per identificazione del testo
     V* 26/03/12  V3R2    CM Errata Estrazione STXT e ETXT se vuoti
     V* 04/04/12  V3R2   BMA Correzione per evitare risostituzione CR, LF e TAB in RxSOS
     V* 20/06/12  V3R2    CM Gestione stringa URL
     V* 22/12/12        ES05 Aggiunto parametro opzionale a RxLate per sostituzione singola
     V* 06/12/12  V3R2  ES05 Rilasciata modifica del 22/12/12
     V* 21/03/13  V3R2   BMA Correzione RxATP per attributi contenenti blank
     V* 25/03/13  V3R2   BMA Rilasciata modifica del 21/03/13
     V* A£30417A  V3R2    MF Gestione multilingua - Inserimento Commenti
     V* 19/93/14  V4R1    CM Perdita di attributi nell'estrazzione con RxATV
     V* 19/06/14  V4R1   BMA Correzione RxELE (contenuto tag)
     V* 30/09/14  V4R1   BMA Correzione RxELE (contenuto tag)
     V* 09/12/14  V4R1   BMA Pulizia schiere RxATV RxATP
     V* 04/11/15  V4R1   BMA Aggiunto parametro opzionale a RxSOC per gestione HTML e Pipe
     V* 10/02/16  V4R1   BMA Corretta gestione STXT e ETXT per identificazione del testo CDATA
     V* 26/05/16  V4R1    BS Su XLATE aggiunto parametro per non distinguire minuscole/maiuscole
     V* 31/05/16  V4R1    BS Definite come INCLUDE /COPY £G49
     V* 13/07/16          AS Aggiunti caratteri permessi in nomi variabili rxatt e rxval
     V* 25/07/16  V5R1   BMA Rilasciata modifica precedente
     V* 05/10/16  V5R1   BMA Esternalizzate RxELE, RxATT e RxVAL
     V* 06/10/16  V5R1   BMA Corretta variabile interna RxVAL definita erroneamente VARYING
     V* 07/10/16  V5R1   BMA Aggiunto campo RxEle per indicare di cercare il tag anche in un CDATA
     V* 16/11/16  V5R1   BMA RxEle: Livelli di chiamata 48-50 riservati £JAY
     V* 16/06/17          BMA Portata variabile ritorno RxSos da 32766 a 65000 e input da 30k a 35k
     V* 19/06/17  V5R1   BMA Rilasciata modifica precedente
     V* 27/11/17  V5R1   BMA Correzione per evitare doppia sostituzione in RxSOC
     V* 17/09/19  001116 PEDSTE Gestione parentesi quadre in RxSOS e RxSOC con parametro specifico
     V* 17/09/19  V5R1    CM Check-out 001116 in SMEUP_TST
     V* 16/10/19  V5R1   PEDSTE Check-out 001116 in SMEDEV
     V* 02/09/20  002113  COMFED Rettifica elaborazione P_RxSPL
     V* 03/09/20  V5R1   BMA Check-out 002113 in SMEDEV
     V* 05/09/20  VERANA Aggiunta procedura di test RxATT1
     V*===============================================================
      *--------------------------------------------------------------*
    RD* Parser XML risposta (Estrae gli attributo scelto)
      *--------------------------------------------------------------*
     PP_RxVAL          B
     D P_RxVAL         Pi         65000    VARYING
     D $XmlTAG                    65000    CONST VARYING
     D $XmlATT                       64    CONST
     D   XmlTAG        S          65000    VARYING
     D   XmlATT        S             64
     D   XmlVAL        S          65000    VARYING
      *
     C                   EVAL      XmlTAG=$XmlTAG
     C                   EVAL      XmlATT=$XmlATT
     C                   CLEAR                   XmlVAL
      *
     C                   CALL      'JAJAX2'
     C                   PARM                    XmlTAG
     C                   PARM                    XmlATT
     C                   PARM                    XmlVAL
      *
      * Restituisce VALORE
     C                   RETURN    XmlVAL
     P                 E
      *--------------------------------------------------------------*
    RD* Parser attributo di documentazione attiva - Nuova per tst
      *--------------------------------------------------------------*
     PP_RxATT1         B
     D P_RxATT         Pi         30000    VARYING
     D  $XmlTAG                   30000    CONST VARYING
     D  $XmlATT                      64    CONST VARYING
     D  $XmlASS                      15    CONST
     D  $XmlFND                       1N   OPTIONS(*NOPASS)
     D  $XmlLIV                       1    OPTIONS(*NOPASS)
     D   XmlTAG        S          30000    VARYING
     D   XmlATT        S             64    VARYING
     D   XmlASS        S             15
     D   XmlFND        S              1N
     D   XmlLIV        S              1
     D   XmlVAL        S          30000    VARYING
     D   XMLPR         S              5I 0
      *
     C                   EVAL      XmlTAG=$XmlTAG
     C                   EVAL      XmlATT=$XmlATT
     C                   EVAL      XmlASS=$XmlASS
     C                   EVAL      XmlFND=*OFF
     C                   EVAL      XmlLIV=''
     C                   CLEAR                   XmlVAL
     C                   EVAL      XMLPR=%parms
1    C                   IF        %parms>3
     C                   EVAL      XmlFND=$XmlFND
1e   C                   ENDIF
1    C                   IF        %parms>4
     C                   EVAL      XmlLIV=$XmlLIV
1e   C                   ENDIF
      *
     C                   CALL      'JAJAX1'
     C                   PARM                    XmlTAG
     C                   PARM                    XmlATT
     C                   PARM                    XmlASS
     C                   PARM                    XmlFND
     C                   PARM                    XmlLIV
     C                   PARM                    XmlVAL
     C                   PARM                    XMLPR
      *
1    C                   IF        %parms>3
     C                   EVAL      $XmlFND=XmlFND
1e   C                   ENDIF
1    C                   IF        %parms>4
     C                   EVAL      $XmlLIV=XmlLIV
1e   C                   ENDIF
      *
     C                   EVAL      XmlVAL='prova'
     C                   RETURN    XmlVAL
     P                 E
      *--------------------------------------------------------------*
    RD* Parser attributo di documentazione attiva
      *--------------------------------------------------------------*
     PP_RxATT          B
     D P_RxATT         Pi         30000    VARYING
     D  $XmlTAG                   30000    CONST VARYING
     D  $XmlATT                      64    CONST VARYING
     D  $XmlASS                      15    CONST
     D  $XmlFND                       1N   OPTIONS(*NOPASS)
     D  $XmlLIV                       1    OPTIONS(*NOPASS)
     D   XmlTAG        S          30000    VARYING
     D   XmlATT        S             64    VARYING
     D   XmlASS        S             15
     D   XmlFND        S              1N
     D   XmlLIV        S              1
     D   XmlVAL        S          30000    VARYING
     D   XMLPR         S              5I 0
      *
     C                   EVAL      XmlTAG=$XmlTAG
     C                   EVAL      XmlATT=$XmlATT
     C                   EVAL      XmlASS=$XmlASS
     C                   EVAL      XmlFND=*OFF
     C                   EVAL      XmlLIV=''
     C                   CLEAR                   XmlVAL
     C                   EVAL      XMLPR=%parms
1    C                   IF        %parms>3
     C                   EVAL      XmlFND=$XmlFND
1e   C                   ENDIF
1    C                   IF        %parms>4
     C                   EVAL      XmlLIV=$XmlLIV
1e   C                   ENDIF
      *
     C                   CALL      'JAJAX1'
     C                   PARM                    XmlTAG
     C                   PARM                    XmlATT
     C                   PARM                    XmlASS
     C                   PARM                    XmlFND
     C                   PARM                    XmlLIV
     C                   PARM                    XmlVAL
     C                   PARM                    XMLPR
      *
1    C                   IF        %parms>3
     C                   EVAL      $XmlFND=XmlFND
1e   C                   ENDIF
1    C                   IF        %parms>4
     C                   EVAL      $XmlLIV=XmlLIV
1e   C                   ENDIF
      *
     C                   RETURN    XmlVAL
     P                 E
      *--------------------------------------------------------------*
    RD* Sostituzione caratteri speciali da XML (o HTML)
      *--------------------------------------------------------------*
     PP_RxURL          B
     D P_RxURL         Pi         30000    VARYING
     D  $XmlURL                   30000    CONST VARYING
     D $XmlVAL         S          30000    VARYING
     C                   EVAL      $XmlVAL=$XmlURL
     C                   EVAL      $XmlVAL=P_RxLATE($XmlVAL:'%':'%25')
     C                   EVAL      $XmlVAL=P_RxLATE($XmlVAL:' ':'%20')
     C                   EVAL      $XmlVAL=P_RxLATE($XmlVAL:'§':'%C2%A7')
     C                   EVAL      $XmlVAL=P_RxLATE($XmlVAL:'£':'%C2%A3')
     C                   EVAL      $XmlVAL=P_RxLATE($XmlVAL:'$':'%24')
     C                   EVAL      $XmlVAL=P_RxLATE($XmlVAL:'|':'%7C')
     C                   EVAL      $XmlVAL=P_RxLATE($XmlVAL:'"':'%22')
     C                   RETURN    $XmlVAL
     P                 E
      *--------------------------------------------------------------*
    RD* Sostituzione caratteri speciali da XML (o HTML)
      *--------------------------------------------------------------*
     PP_RxSOS          B
     D P_RxSOS         Pi         65000    VARYING
     D  $XmlSOS                   35000    CONST VARYING
      * Linguaggio nella stringa da sostituire (H=HTML, P=PIPE, Q=Quadre, other XML)
     D  $StrLang                      1    CONST OPTIONS(*NOPASS)
     D $XmlVAL         S          65000    VARYING
     D $POS            S              5  0
     D $INZ            S              5  0
     D $LEN_XML        S              5  0
     D LEN             S              5  0
     D AAA001          S              1
     D AAA010          S             10    VARYING
     D AAA006          S             06
     D LEN_AMP         S              5  0 INZ(5)
     D LEN_LT          S              5  0 INZ(4)
     D LEN_GT          S              5  0 INZ(4)
     D LEN_APOS        S              5  0 INZ(6)
     D LEN_QUOT        S              5  0 INZ(6)
     D LEN_LF          S              5  0 INZ(5)
     D LEN_CR          S              5  0 INZ(5)
     D LEN_TAB         S              5  0 INZ(5)
     D LEN_PIPE        S              5  0 INZ(10)
     D LEN_QUADRE      S              5  0 INZ(5)
      * ESADECIMALE DEL CARATTERE TABULAZIONE EBCDIC
     D C_TAB           C                   CONST(X'05')
      * ESADECIMALE DEL CARATTERE CR
     D C_CR            C                   CONST(X'0D')
      * ESADECIMALE DEL CARATTERE LF
     D C_LF            C                   CONST(X'25')
      *
     C                   EVAL      $XmlVAL=$XmlSOS
     C                   EVAL      $LEN_XML=%LEN($XMLVAL)
1    C                   IF        $LEN_XML=0
     C                   GOTO      G9MAIN
1e   C                   ENDIF
      *
      * sostituzione |
1    C                   IF        %parms>1 and $StrLang='P'
     C                   EVAL      $POS=%SCAN('|':$XmlVAL)
2    C                   IF        $POS>0
     C                   EVAL      AAA001='|'
     C                   EVAL      AAA010='_$_PIPE_$_'
     C                   EVAL      LEN=LEN_PIPE
     C                   EXSR      SR0001
2e   C                   ENDIF
     C                   GOTO      G9MAIN
1e   C                   ENDIF
      * sostituzione parentesi quadre
1    C                   IF        %parms>1 and $StrLang='Q'
      * . sostituzione [
     C                   EVAL      $POS=%SCAN('[':$XmlVAL)
2    C                   IF        $POS>0
     C                   EVAL      AAA001='['
     C                   EVAL      AAA010='&#91;'
     C                   EVAL      LEN=LEN_QUADRE
     C                   EXSR      SR0001
2e   C                   ENDIF
      * . sostituzione ]
     C                   EVAL      $POS=%SCAN(']':$XmlVAL)
2    C                   IF        $POS>0
     C                   EVAL      AAA001=']'
     C                   EVAL      AAA010='&#93;'
     C                   EVAL      LEN=LEN_QUADRE
     C                   EXSR      SR0001
      *
2e   C                   ENDIF
     C                   GOTO      G9MAIN
1e   C                   ENDIF
      *
      * sostituzione &
     C                   EVAL      $POS=%SCAN('&':$XmlVAL)
1    C                   IF        $POS>0
     C                   EVAL      AAA001='&'
     C                   EVAL      AAA010='&amp;'
     C                   EVAL      LEN=LEN_AMP
     C                   EXSR      SR0001_AMP
1e   C                   ENDIF
      * sostituzione >
     C                   EVAL      $POS=%SCAN('>':$XmlVAL)
1    C                   IF        $POS>0
     C                   EVAL      AAA001='>'
     C                   EVAL      AAA010='&gt;'
     C                   EVAL      LEN=LEN_GT
     C                   EXSR      SR0001
1e   C                   ENDIF
      * sostituzione <
     C                   EVAL      $POS=%SCAN('<':$XmlVAL)
1    C                   IF        $POS>0
     C                   EVAL      AAA001='<'
     C                   EVAL      AAA010='&lt;'
     C                   EVAL      LEN=LEN_LT
     C                   EXSR      SR0001
1e   C                   ENDIF
      * sostituzione '
     C                   EVAL      $POS=%SCAN('''':$XmlVAL)
1    C                   IF        $POS>0 and
     C                             ((%parms>1 and $StrLang<>'H') OR
     C                             %parms=1)
     C                   EVAL      AAA001=''''
     C                   EVAL      AAA010='&apos;'
     C                   EVAL      LEN=LEN_APOS
     C                   EXSR      SR0001
1e   C                   ENDIF
      * sostituzione "
     C                   EVAL      $POS=%SCAN('"':$XmlVAL)
1    C                   IF        $POS>0
     C                   EVAL      AAA001='"'
     C                   EVAL      AAA010='&quot;'
     C                   EVAL      LEN=LEN_QUOT
     C                   EXSR      SR0001
1e   C                   ENDIF
      *
      * sostituzione CR
     C                   EVAL      $POS=%SCAN(C_CR:$XmlVAL)
1    C                   IF        $POS>0
     C                   EVAL      AAA001=C_CR
     C                   EVAL      AAA010='&#xD;'
     C                   EVAL      LEN=LEN_CR
     C                   EXSR      SR0001
1e   C                   ENDIF
      *
      * sostituzione LF
     C                   EVAL      $POS=%SCAN(C_LF:$XmlVAL)
1    C                   IF        $POS>0
     C                   EVAL      AAA001=C_LF
     C                   EVAL      AAA010='&#xA;'
     C                   EVAL      LEN=LEN_LF
     C                   EXSR      SR0001
1e   C                   ENDIF
      *
      * sostituzione TAB
     C                   EVAL      $POS=%SCAN(C_TAB:$XmlVAL)
1    C                   IF        $POS>0
     C                   EVAL      AAA001=C_TAB
     C                   EVAL      AAA010='&#x9;'
     C                   EVAL      LEN=LEN_TAB
     C                   EXSR      SR0001
1e   C                   ENDIF
      *
     C     G9MAIN        TAG
     C                   RETURN    $XmlVAL
      *
     C     SR0001        BEGSR
1    C                   DOW       $POS>0
     C                   EVAL      $XmlVAL=%REPLACE(AAA010:$XmlVAL:$POS:01)
     C                   EVAL      $INZ=$POS+LEN
2    C                   IF        $INZ>%LEN($XmlVAL)
     C                   LEAVE
2e   C                   ENDIF
     C                   EVAL      $POS=%SCAN(AAA001:$XmlVAL:$INZ)
1e   C                   ENDDO
     C                   ENDSR

     C     SR0001_AMP    BEGSR
1    C                   DOW       $POS>0
2    C                   SELECT
2x   C                   WHEN      $POS+LEN_AMP-1<=$LEN_XML AND
     C                             %SUBST($XmlVal:$POS:LEN_AMP)='&amp;'
     C                   EVAL      $INZ=$POS+LEN_AMP
2x   C                   WHEN      $POS+LEN_LT-1<=$LEN_XML AND
     C                             %SUBST($XmlVal:$POS:LEN_LT)='&lt;'
     C                   EVAL      $INZ=$POS+LEN_LT
2x   C                   WHEN      $POS+LEN_GT-1<=$LEN_XML AND
     C                             %SUBST($XmlVal:$POS:LEN_GT)='&gt;'
     C                   EVAL      $INZ=$POS+LEN_GT
2x   C                   WHEN      $POS+LEN_APOS-1<=$LEN_XML AND
     C                             %SUBST($XmlVal:$POS:LEN_APOS)='&apos;'
     C                   EVAL      $INZ=$POS+LEN_APOS
2x   C                   WHEN      $POS+LEN_QUOT-1<=$LEN_XML AND
     C                             %SUBST($XmlVal:$POS:LEN_QUOT)='&quot;'
     C                   EVAL      $INZ=$POS+LEN_QUOT
2x   C                   WHEN      $POS+LEN_CR-1<=$LEN_XML AND
     C                             %SUBST($XmlVal:$POS:LEN_CR)='&#xD;'
     C                   EVAL      $INZ=$POS+LEN_CR
2x   C                   WHEN      $POS+LEN_LF-1<=$LEN_XML AND
     C                             %SUBST($XmlVal:$POS:LEN_LF)='&#xA;'
     C                   EVAL      $INZ=$POS+LEN_LF
2x   C                   WHEN      $POS+LEN_TAB-1<=$LEN_XML AND
     C                             %SUBST($XmlVal:$POS:LEN_TAB)='&#x9;'
     C                   EVAL      $INZ=$POS+LEN_TAB
2x   C                   OTHER
     C                   EVAL      $XmlVAL=%REPLACE(AAA010:$XmlVAL:$POS:01)
     C                   EVAL      $INZ=$POS+LEN
     C                   EVAL      $LEN_XML=%LEN($XmlVal)
2e   C                   ENDSL
2    C                   IF        $INZ>$LEN_XML
     C                   LEAVE
2e   C                   ENDIF
     C                   EVAL      $POS=%SCAN(AAA001:$XmlVAL:$INZ)
1e   C                   ENDDO
     C                   ENDSR
     P                 E
      *--------------------------------------------------------------*
    RD* Sostuisce stringa in carattere
      *--------------------------------------------------------------*
     PP_RxSOC          B
     D P_RxSOC         Pi         30000    VARYING
     D  $XmlSOC                   30000    CONST VARYING
      * Linguaggio nella stringa da sostituire (H=HTML, P=PIPE, Q=Quadre, other XML)
     D  $StrLang                      1    CONST OPTIONS(*NOPASS)
     D $XmlVAL         S          30000    VARYING
      *
     D $POS            S              5  0
     D $INZ            S              5  0
     D $SUBLEN         S              5  0
     D AAA001          S             10    VARYING
     D AAA010          S             01
      * ESADECIMALE DEL CARATTERE TABULAZIONE EBCDIC
     D C_TAB           C                   CONST(X'05')
      * ESADECIMALE DEL CARATTERE CR
     D C_CR            C                   CONST(X'0D')
      * ESADECIMALE DEL CARATTERE LF
     D C_LF            C                   CONST(X'25')
      *
     C                   EVAL      $XmlVAL=$XmlSOC
1    C                   IF        %LEN($XMLVAL)=0
     C                   GOTO      G9MAIN
1e   C                   ENDIF
     C                   EVAL      $INZ=1
      *
      *
1    C                   IF        %SCAN('&':$XmlVAL)>0
      *
2    C                   IF        %SCAN('&quot;':$XmlVAL)>0
     C                   EVAL      AAA001='&quot;'
     C                   EVAL      AAA010='"'
     C                   EXSR      SR0001
2e   C                   ENDIF
2    C                   IF        %SCAN('&lt;':$XmlVAL)>0
     C                   EVAL      AAA001='&lt;'
     C                   EVAL      AAA010='<'
     C                   EXSR      SR0001
2e   C                   ENDIF
2    C                   IF        %SCAN('&gt;':$XmlVAL)>0
     C                   EVAL      AAA001='&gt;'
     C                   EVAL      AAA010='>'
     C                   EXSR      SR0001
2e   C                   ENDIF
2    C                   IF        %SCAN('&apos;':$XmlVAL)>0 AND
     C                             ((%parms>1 and $StrLang<>'H') OR
     C                             %parms=1)
     C                   EVAL      AAA001='&apos;'
     C                   EVAL      AAA010=''''
     C                   EXSR      SR0001
2e   C                   ENDIF
1    C                   IF        %SCAN('&#xD;':$XmlVAL)>0
     C                   EVAL      AAA001='&#xD;'
     C                   EVAL      AAA010=C_CR
     C                   EXSR      SR0001
1e   C                   ENDIF
1    C                   IF        %SCAN('&#xA;':$XmlVAL)>0
     C                   EVAL      AAA001='&#xA;'
     C                   EVAL      AAA010=C_LF
     C                   EXSR      SR0001
1e   C                   ENDIF
1    C                   IF        %SCAN('&#x9;':$XmlVAL)>0
     C                   EVAL      AAA001='&#x9;'
     C                   EVAL      AAA010=C_TAB
     C                   EXSR      SR0001
1e   C                   ENDIF
      *  &amp; deve essere sostituito per ultimo per evitare problemi di doppia sostituzione
2    C                   IF        %SCAN('&amp;':$XmlVAL)>0
     C                   EVAL      AAA001='&amp;'
     C                   EVAL      AAA010='&'
     C                   EXSR      SR0001
2e   C                   ENDIF
      *
0e   C                   ENDIF
      *
      * sostituzione |
1    C                   IF        %parms>1 and $StrLang='P'
2    C                   IF        %SCAN('_$_PIPE_$_':$XmlVAL)>0
     C                   EVAL      AAA001='_$_PIPE_$_'
     C                   EVAL      AAA010='|'
     C                   EXSR      SR0001
2e   C                   ENDIF
1e   C                   ENDIF
      * sostituzione parentesi quadre
1    C                   IF        %parms>1 and $StrLang='Q'
      * . sostituzione [
2    C                   IF        %SCAN('&#91;':$XmlVAL)>0
     C                   EVAL      AAA001='&#91;'
     C                   EVAL      AAA010='['
     C                   EXSR      SR0001
2e   C                   ENDIF
      * . sostituzione ]
2    C                   IF        %SCAN('&#93;':$XmlVAL)>0
     C                   EVAL      AAA001='&#93;'
     C                   EVAL      AAA010=']'
     C                   EXSR      SR0001
2e   C                   ENDIF
1e   C                   ENDIF
      *
     C     G9MAIN        TAG
     C                   RETURN    $XmlVAL
      *
     C     SR0001        BEGSR
     C                   EVAL      AAA001=%TRIM(AAA001)
     C                   EVAL      $POS=%SCAN(AAA001:$XmlVAL:01)
0    C                   DOW       $POS>0
     C                   EVAL      $SUBLEN=%LEN(AAA001)
     C                   EVAL      $XmlVAL=%REPLACE(AAA010:$XmlVAL:$POS:$SUBLEN)
     C                   EVAL      $INZ=$POS+%LEN(AAA010)
1    C                   IF        $INZ>%LEN($XmlVAL)
     C                   LEAVE
1e   C                   ENDIF
     C                   EVAL      $POS=%SCAN(AAA001:$XmlVAL:$INZ)
0e   C                   ENDDO
     C                   ENDSR
     P                 E
      *--------------------------------------------------------------*
    RD* Trasformo stringa in stringa
      *--------------------------------------------------------------*
     PP_RxLATE         B
     D P_RxLate        Pi         32766    VARYING
     D  $XmlInp                   30000    CONST VARYING
     D  $XmlS01                   30000    CONST VARYING
     D  $XmlS02                   30000    CONST VARYING
     D  $SosSing                      1    CONST OPTIONS(*NOPASS)
     D  $SosCase                      1    CONST OPTIONS(*NOPASS)
      * Variabili locali
     D $StrO           S          32766    VARYING
     D $Str1           S          30000    VARYING
     D $Str2           S          30000    VARYING
     D $StrFix         S          30000
     D $StrSos         S              1    VARYING
     D $StrCas         S              1    VARYING
     D $I              S              5  0
     D $LS1            S              5  0
     D $LS2            S              5  0
     D $LSO            S              5  0
      *
     D UStrO           S          32766    VARYING
     D UStr1           S          30000    VARYING
      *
********** PREPROCESSOR COPYSTART QILEGEN,£G49DS
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £G49DS
      * Sorgente di origine : SMEDEV/QILEGEN(£G49DS)
      * Esportato il        : 20240719 094555
      *====================================================================
     V*=====================================================================
     V* MODIFICHE Ril.  T Au  Descrizione
     V* gg/mm/aa  nn.mm i xx  Breve descrizione
     V*=====================================================================
     V* 20/09/16  V5R1    BMA Aggiunta stringa di input a 30000 per funzione CON_E
     V*=====================================================================
     D £G49SI          DS          1024
     D £G49SE          DS         30000
********** PREPROCESSOR COPYEND QILEGEN,£G49DS
      *
     C                   EVAL      $StrO=$XmlInp
     C                   EVAL      $Str1=$XmlS01
     C                   EVAL      $Str2=$XmlS02
      *
0    C                   IF        %parms>3 AND $SosSing='1'
     C                   EVAL      $StrSos='1'
0x   C                   ELSE
     C                   EVAL      $StrSos=*BLANKS
0e   C                   ENDIF
0    C                   IF        %parms>4 AND $SosCase='1'
     C                   EVAL      $StrCas='1'
0x   C                   ELSE
     C                   EVAL      $StrCas=*BLANKS
     C                   ENDIF
      * Solo se ricevuto
0    C                   IF        %Len($StrO)>0 AND %Len($Str1)>0 AND
     C                             %Len($Str1)<=%Len($StrO)
     C                   EVAL      $I=1
     C                   EVAL      $LS1=%Len($Str1)
     C                   EVAL      $LS2=%Len($Str2)
      * Se richiesto di non controllare i caratteri maiuscoli/minuscoli
     C                   IF        $StrCas='1'
      *
     C                   IF        $LS1<=1024
     C                   EVAL      £G49FU='CON'
     C                   EVAL      £G49ME='U_C'
     C                   EVAL      £G49SI=$STR1
     C                   EXSR      £G49
     C                   EVAL      USTR1=£G49SI
     C                   ELSE
     C                   EVAL      $StrFix=$STR1
     C                   CLEAR                   USTR1
     C                   DO        30            $X                5 0
     C                   IF        %SUBST($StrFix:(1024*($X-1))+1)=' '
     C                   LEAVE
     C                   ENDIF
     C                   EVAL      £G49FU='CON'
     C                   EVAL      £G49ME='U_C'
     C                   IF        $X<30
     C                   EVAL      £G49SI=%SUBST($StrFix:(1024*($X-1))+1
     C                             :1024)
     C                   ELSE
     C                   EVAL      £G49SI=%SUBST($StrFix:(1024*($X-1))+1
     C                             :1024)
     C                   ENDIF
     C                   EXSR      £G49
     C                   ENDDO
     C                   ENDIF
     C                   EVAL      USTR1=%SUBST(USTR1:1:$LS1)
      *
     C                   EVAL      $LSO=%Len($StrO)
     C                   IF        $LSO<=1024
     C                   EVAL      £G49FU='CON'
     C                   EVAL      £G49ME='U_C'
     C                   EVAL      £G49SI=$STRO
     C                   EXSR      £G49
     C                   EVAL      USTRO=£G49SI
     C                   ELSE
     C                   EVAL      $StrFix=$STRO
     C                   CLEAR                   USTRO
     C                   DO        30            $X                5 0
     C                   IF        %SUBST($StrFix:(1024*($X-1))+1)=' '
     C                   LEAVE
     C                   ENDIF
     C                   EVAL      £G49FU='CON'
     C                   EVAL      £G49ME='U_C'
     C                   IF        $X<30
     C                   EVAL      £G49SI=%SUBST($StrFix:(1024*($X-1))+1
     C                             :1024)
     C                   ELSE
     C                   EVAL      £G49SI=%SUBST($StrFix:(1024*($X-1))+1
     C                             :1024)
     C                   ENDIF
     C                   EXSR      £G49
     C                   EVAL      USTRO=USTRO+%SUBST(£G49SI:1:1024)
     C                   ENDDO
     C                   ENDIF
     C                   EVAL      USTRO=%SUBST(USTRO:1:$LSO)
      *
     C                   ENDIF
      *
      *. Ricerco la stringa
1    C                   DO        *HIVAL
     C                   IF        $STRCAS<>''
     C                   EVAL      $I=%SCAN(UStr1:UStrO:$I)
     C                   ELSE
     C                   EVAL      $I=%SCAN($Str1:$StrO:$I)
     C                   ENDIF
2    C                   IF        $I=0
     C                   LEAVE
2e   C                   ENDIF
      *. Eseguo la sostituzione
     C                   EVAL      $StrO=%REPLACE($Str2:$StrO:$I:$LS1)
     C                   IF        $STRCAS<>''
     C                   EVAL      UStrO=%REPLACE($Str2:UStrO:$I:$LS1)
     C                   ENDIF
      *
      *. Se devo eseguire una sola sostituzione esco
2    C                   IF        $StrSos='1'
     C                   LEAVE
2e   C                   ENDIF
      *. Sposto il puntatore
     C                   EVAL      $I=$I+$LS2
2    C                   IF        $I>%Len($StrO)
     C                   LEAVE
2e   C                   ENDIF
1e   C                   ENDDO
0e   C                   ENDIF
      *
     C                   RETURN    $StrO
      *
********** PREPROCESSOR COPYSTART QILEGEN,£G49
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £G49
      * Sorgente di origine : SMEDEV/QILEGEN(£G49)
      * Esportato il        : 20240719 094557
      *====================================================================
     V*=====================================================================
     V* MODIFICHE Ril.  T Au  Descrizione
     V* gg/mm/aa  nn.mm i xx  Breve descrizione
     V*=====================================================================
     V* 20/09/16  V5R1    BMA Aggiunta stringa di input a 30000 per funzione CON_E
     V*=====================================================================
     D*----------------------------------------------------------------
     D* OBIETTIVO
     D* Data una stringa di testo su cui operare, la routine sostitui-
     D* sce una serie di caratteri contigui contenuti in essa con un'
     D* altra serie di caratteri.
     D* Esempio:
     D*   nella stringa 'Il codice &1 non è corretto.'
     D*   la stringa '&1' deve essere sostituita con 'COD01'
     D*   il risultato sarà quindi 'Il codice COD01 non è corretto.'
     D*
     D* PREREQUISITI
     D*  D/COPY QILEGEN,£G49DS
     D*
     D* PARAMETRI
     D*  Input:  £G49FU: Funzione                   (10)
     D*          £G49ME: Metodo                     (10)
     D*          £G49SI: stringa in cui sostituire  (1024)
     D*          £G49SC: stringa da sostituire      (256)
     D*          £G49SD: nuova stringa sostituente  (256)
     D*  Output: £G49SI: stringa in cui è stato sostituito £G49SD
     D*          £G49MS: messaggio ritorno
     D*          £G49FI: file messaggi
     D*          *IN35 : Errore
     D*          *IN36 : Ricerca
     D*
     D* ESEMPIO DI CHIAMATA
     D*C                     MOVEL<funzione>£G49FU    P
     D*C                     MOVEL<metodo>  £G49ME    P
     D*C                     MOVEL<s in cui>£G49SI    P
     D*C                     MOVEL<s da sos>£G49SC    P
     D*C                     MOVEL<s nuova> £G49SD    P
     D*C                     EXSR £G49
     D*C                     MOVEL£G49SI    <campo stringa risultato>
     D*----------------------------------------------------------------
     C     £G49          BEGSR
      *
     C                   CALL      'B£G49G'
     C                   PARM                    £G49FU           10            -->
     C                   PARM                    £G49ME           10            -->
     C                   PARM                    £G49MS            7            <--
     C                   PARM                    £G49FI           10            <--
     C     *IN35         PARM      *OFF          £G4935            1            <--
     C     *IN36         PARM      *OFF          £G4936            1            <--
     C                   PARM                    £G49SI                         <-->
     C                   PARM                    £G49SC          256             -->
     C                   PARM                    £G49SD          256             -->
     C                   PARM                    £G49SE                         <-->
      *
     C                   ENDSR
********** PREPROCESSOR COPYEND QILEGEN,£G49
      *
     P                 E
      *--------------------------------------------------------------*
    RD* Restituisce la lista degli attributi e dei valori di un elemento
      *--------------------------------------------------------------*
     PP_RxATV          B
     D P_RxATV         Pi           306    DIM(200)
     D Xml_Str                    30000    VARYING
     D  $XmlPRG                       5  0 OPTIONS(*NOPASS)
     D Xml_Str_T       S          30000    VARYING
     D SAtv            S            306    DIM(200)
     D $I              S              5  0
     D $F              S              5  0
     D $X              S              5  0
     D NATV            S              5  0
      *
      *Salvo la stringa XML ricevuta che altrimenti tornerebbe tagliata
     C                   EVAL      Xml_Str_T=P_RxSOC(Xml_Str)
      * NATV è l'indice della schiera SAtv (l'output)
     C                   EVAL      NATV=0
     C                   CLEAR                   SAtv
      *
0    C                   DO        *HIVAL
      * Un XML valido deve avere min 4 caratteri ES A="" e la schiera SAtv ha 200 elementi
1    C                   IF        %LEN(Xml_Str_T)<4 OR NATV=200
     C                   LEAVE
1e   C                   ENDIF
      * cerco gli attributi solo del primo tag; l'attributo deve essere seguito da =" e dal suo
      * valore Es Expanded="Yes"; con Expanded= "Yes" l'attributo in questione e il suo valore NON
      * VENGONO RESTITUITI
     C                   EVAL      $I=%SCAN('="':Xml_Str_T)
     C                   EVAL      $X=%SCAN('>':Xml_Str_T)
      * se trovo una chiusura di tag prima dell'attributo esco perchè mi han passato più tag in fila
      * ES <SecList><Sec Lev="0" Frm="GRA_EMU" Code="General">
1    C                   IF        $X<$I AND $X>0
     C                   LEAVE
1e   C                   ENDIF
      * se non trovo l'inizio di nessun Atrributo, esco
1    C                   IF        $I=0 OR $I=%LEN(%TRIMR(Xml_Str_T))-1
     C                   LEAVE
1e   C                   ENDIF
      * se il TAG è preceduto dal suo nome non lo devo considerare ES <Setup Attr1="Valo"/>
     C                   EVAL      $X=%SCAN(' ':Xml_Str_T)
1    C                   IF        $X<$I AND $X<>0
      * se dopo aver tolto il nome la lunghezza dell'XML è Scesa sotto alla lunghezza minima esco
2    C                   IF        $X+4>%LEN(Xml_Str_T)
     C                   LEAVE
2x   C                   ELSE
      * altrimenti tolgo il nome del tag dall'inizio della stringa e ricomincio da capo
     C                   EVAL      Xml_Str_T=%SUBST(Xml_Str_T:$X+1)
     C                   ITER
2e   C                   ENDIF
1e   C                   ENDIF
      * se non trovo la fine di nessun Atrributo, esco
     C                   EVAL      $F=%SCAN('"':Xml_Str_T:$I+2)
1    C                   IF        $F=0
     C                   LEAVE
1e   C                   ENDIF
      * a questo punto valorizzo la schiera SAtv con NOME    VALORE
     C                   EVAL      NATV=NATV+1
      * estraggo il nome dell'attributo
     C                   EVAL      %SUBST(SAtv(NAtv):01:50)
     C                             =%TRIM(%SUBST(Xml_Str_T:1:$I-1))
      * estraggo il suo valore
     C                   EVAL      %SUBST(SAtv(NAtv):51)=
     C                             %TRIMR(%SUBST(Xml_Str_T:$I+2:$F-2-$I))
      * se dopo aver tolto l'attr e il valore la lunghezza dell'XML è scesa sotto 4 esco
1    C                   IF        $F+4>%LEN(Xml_Str_T)
     C                   LEAVE
1e   C                   ENDIF
     C
      * e tolgo dalla stringa il TAG estratto
     C                   EVAL      Xml_Str_T=%TRIMR(%SUBST(Xml_Str_T:$F+1))
      *
0e   C                   ENDDO
      * Restituisce matrice
0    C                   IF        %parms=2
     C                   EVAL      $XmlPRG=NAtv
0e   C                   ENDIF
     C                   RETURN    SAtv
     P                 E
      *--------------------------------------------------------------*
    RD* Restituisce la lista degli attributi e dei valori di un elemento di documentazione attiva
      *--------------------------------------------------------------*
     PP_RxATP          B
     D P_RxATP         Pi          1055    DIM(200)
     D Xml_StrP                   30000    VARYING
     D  $XmlPRGp                      5  0 OPTIONS(*NOPASS)
     D Xml_Str_T       S          30000    VARYING
     D                 DS
     D$JAXATP                      1055    DIM(200)
     D $JAXATP_A                     50    OVERLAY($JAXATP:1)
     D $JAXATP_V                   1000    OVERLAY($JAXATP:*NEXT)
     D $JAXATP_L                      5  0 OVERLAY($JAXATP:*NEXT)
     D $I              S              5  0
     D §I              S              5  0
     D $F              S              5  0
     D $X              S              5  0
     D $LIV            S              5  0
     D NATP            S              5  0
      *
      *Salvo la stringa XML ricevuta che altrimenti tornerebbe tagliata
     C                   EVAL      Xml_Str_T=P_RxSOC(Xml_StrP)
      * NATP è l'indice della schiera di output
     C                   EVAL      NATP=0
     C                   CLEAR                   $JAXATP
      *
     C                   EVAL      $LIV=0
0    C                   DO        *HIVAL
      * Un XML valido deve avere min 3 caratteri ES A() e la schiera ha 200 elementi
1    C                   IF        %LEN(Xml_Str_T)<3 OR NATP=200
     C                   LEAVE
1e   C                   ENDIF
      * cerco gli attributi solo del primo tag;
     C                   EVAL      $I=%SCAN('(':Xml_Str_T)
     *C                   EVAL      $F=%SCAN(')':Xml_Str_T)
     *C                   IF        $F<$I AND $F>0 AND $I>0
     * * se c'è una parentesi chiusa prima della parentesi aperta decremento il livello
     *C                   EVAL      $LIV=$LIV-1
     *C                   ENDIF
      * se non trovo l'inizio di nessun Atrributo, esco
     *C                   IF        $I=0 OR $I=%LEN(%TRIM(Xml_Str_T))-1
     *C                   IF        $I=0 OR $I>=%LEN(%TRIM(Xml_Str_T))-1
1    C                   IF        $I=0 OR $I>=%LEN(%TRIMR(Xml_Str_T))
     C                   LEAVE
1e   C                   ENDIF
1    C                   FOR       §I=$I DOWNTO 1
2    C                   IF        %SUBST(Xml_Str_T:§I:1)=')'
     C                   EVAL      $LIV=$LIV-1
2e   C                   ENDIF
1e   C                   ENDFOR
     C                   EVAL      $LIV=$LIV+1
1    C                   FOR       §I=$I DOWNTO 1
2    C                   IF        %SUBST(Xml_Str_T:§I:1)=' ' OR
     C                             %SUBST(Xml_Str_T:§I:1)=')'
     C                   LEAVE
2e   C                   ENDIF
1e   C                   ENDFOR
1    C                   IF        §I=0
     C                   EVAL      §I=1
1e   C                   ENDIF
      * a questo punto valorizzo la schiera con NOME, VALORE e LIVELLO
     C                   EVAL      NATP=NATP+1
      * estraggo il nome dell'attributo
     C                   EVAL      $JAXATP_A(NATP)=
     C                                       %TRIM(%SUBST(Xml_Str_T:§I:$I-§I))
      * Se il nome dell'attributo è vuoto porto indietro il contatore
1    C                   IF        $JAXATP_A(NATP)=''
     C                   EVAL      NATP=NATP-1
1x   C                   ELSE
      * estraggo il suo valore
     C                   EVAL      $JAXATP_V(NATP)=P_RXATT(Xml_Str_T:
     C                             %TRIMR($JAXATP_A(NATP))+'(':' ')
      * livello
     C                   EVAL      $JAXATP_L(NATP)=$LIV
1e   C                   ENDIF
      * e tolgo dalla stringa il TAG estratto
     C                   EVAL      Xml_Str_T=%TRIMR(%SUBST(Xml_Str_T:$I+1))
      *
0e   C                   ENDDO
      * Restituisce matrice
0    C                   IF        %parms=2
     C                   EVAL      $XmlPRGp=NAtp
0e   C                   ENDIF
     C                   RETURN    $JAXATP
     P                 E
      **-------------------------------------------------------------*
    RD* Restituisce il contenuto dell'elemento richiesto
      *--------------------------------------------------------------*
     PP_RxELE          B
     D P_RxELE         Pi         65000    VARYING
     D Xml_Fnd                      512    VARYING CONST
     D Xml_Met                       10    CONST
      * .livello di chiamata
      * ... livelli 48-50 riservati £JAY
     D Xml_Liv                        2  0 CONST
     D Xml_Str                    65000    VARYING CONST
     D NodIni                         5  0 OPTIONS(*NOPASS)
     D NodLen                         5  0 OPTIONS(*NOPASS)
     D Xml_Con                    65000    OPTIONS(*NOPASS) VARYING
      * . Cerca tag anche all'interno di un CDATA
     D SEACDATA                       1    OPTIONS(*NOPASS)
      *
     D $Xml_Str        S          65000    VARYING
     D $Xml_Out        S          65000    VARYING
     D $NodIni         S              5  0
     D $NodLen         S              5  0
     D $Xml_Con        S          65000    VARYING
     D $Parms          S              2  0
     D $Xml_Fnd        S            512    VARYING
     D $Xml_Met        S             10
     D $Xml_Liv        S              2  0
     D $SCDATA         S              1
      *
     C                   EVAL      $Parms=%parms
     C                   EVAL      $Xml_Str=Xml_Str
     C                   EVAL      $Xml_Fnd=Xml_Fnd
     C                   EVAL      $Xml_Met=Xml_Met
     C                   EVAL      $Xml_Liv=Xml_Liv
1    C                   IF        %parms>7
     C                   EVAL      $SCDATA=SEACDATA
     C                   ELSE
     C                   CLEAR                   $SCDATA
     C                   ENDIF
      *
     C                   CALL      'JAJAX0'
     C                   PARM                    $Xml_Fnd
     C                   PARM                    $Xml_Met
     C                   PARM                    $Xml_Liv
     C                   PARM                    $Xml_Str
     C                   PARM                    $Xml_Out
     C                   PARM                    £JSP
     C                   PARM                    $NodIni
     C                   PARM                    $NodLen
     C                   PARM                    $Xml_Con
     C                   PARM                    $Parms
     C                   PARM                    $SCDATA
      *
1    C                   IF        %parms>4
     C                   EVAL      NodIni=$NodIni
1e   C                   ENDIF
1    C                   IF        %parms>5
     C                   EVAL      NodLen=$NodLen
1e   C                   ENDIF
1    C                   IF        %parms>6
     C                   EVAL      Xml_Con=$Xml_Con
1e   C                   ENDIF
      *
     C                   RETURN    $Xml_Out
     P                 E
      *--------------------------------------------------------------*
    RD* Restituisce stringa splittata su più righe
      *--------------------------------------------------------------*
     PP_RxSPL          B
     D P_RxSPL         Pi         30000    VARYING
     D $String                    30000    CONST VARYING
     D $Split                         1    CONST
     D $StrOut         S          30000    VARYING
     D $S              S              5  0                                      lungh.stringa
     D $SS             S              5  0                                      primo spazio sx
     D $SSP            S              1                                         sx: punto
     D $SD             S              5  0                                      primo spazio dx
     D $SDP            S              1                                         sd: punto
     D $SP             S              5  0                                      puntatore
      *
     C                   EVAL      $StrOut=$String
     C                   EVAL      $S=%LEN(%TRIM($StrOut))
      *
0    C                   SELECT
      * 1 SPLIT, vicino a metà
0x   C                   WHEN      $Split='1'
      * . Cerca primo spazio a sx e dx di metà stringa
1    C                   DO        *HIVAL        $SP
2    C                   IF        $SP>$S
     C                   LEAVE
2e   C                   ENDIF
2    C                   IF        %SUBST($StrOut:$SP:1)='' OR
     C                             %SUBST($StrOut:$SP:1)='.'
      * . a sinistra
3    C                   IF        $SP<=($S/2)
     C                   EVAL      $SS=$SP
4    C                   IF        %SUBST($StrOut:$SP:1)='.'
     C                   EVAL      $SSP='1'
4e   C                   ENDIF
3e   C                   ENDIF
      * . a destra
3    C                   IF        $SP>($S/2) AND $SD=0
     C                   EVAL      $SD=$SP
4    C                   IF        %SUBST($StrOut:$SP:1)='.'
     C                   EVAL      $SDP='1'
4e   C                   ENDIF
     C                   LEAVE
3e   C                   ENDIF
2e   C                   ENDIF
1e   C                   ENDDO
      *
      * . Verifica dove sostituire/introdurre il '|'
     C                   CLEAR                   DOVE              1
1    C                   SELECT
      * . . c'è solo a sx
1x   C                   WHEN      $SS<>0 AND $SD=0
     C                   EVAL      DOVE='S'
      * . . c'è solo a dx
1x   C                   WHEN      $SD<>0 AND $SS=0
     C                   EVAL      DOVE='D'
      * . . c'è da entrambe le parti: il più vicino al centro (privilegiando a dx)
1x   C                   WHEN      $SD<>0 AND $SS<>0
2    C                   IF        ($SS-1)>($S-$SD)
     C                   EVAL      DOVE='S'
2x   C                   ELSE
     C                   EVAL      DOVE='D'
2e   C                   ENDIF
1e   C                   ENDSL
      *
      * . Introduce/sostituisce
1    C                   SELECT
      * . . a sinistra
1x   C                   WHEN      DOVE='S'
2    C                   IF        $SSP=*BLANKS
     C                   EVAL      %SUBST($StrOut:$SS:1)='|'                    sostituisce
2x   C                   ELSE
     C                   EVAL      $StrOut=%SUBST($StrOut:1:$SS)+'|'+           aggiunge
     C                             %SUBST($StrOut:$SS+1)
2e   C                   ENDIF
      * . . a destra
1x   C                   WHEN      DOVE='D'
2    C                   IF        $SDP=*BLANKS
     C                   EVAL      %SUBST($StrOut:$SD:1)='|'                    sostituisce
2x   C                   ELSE
     C                   EVAL      $StrOut=%SUBST($StrOut:1:$SD)+'|'+           aggiunge
     C                             %SUBST($StrOut:$SD+1)
2e   C                   ENDIF
1e   C                   ENDSL
      *
      *
0e   C                   ENDSL
      *
      *
      * Restituisce VALORE
     C                   RETURN    $StrOut
     P                 E
      *ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£JAX_PC1
********** PREPROCESSOR COPYSTART QPROGEN,£J15P
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £J15P
      * Sorgente di origine : SMEDEV/QPROGEN(£J15P)
      * Esportato il        : 20240719 095037
      *====================================================================
      *IF NOT DEFINED(J15P_INCLUDED)
      *DEFINE J15P_INCLUDED
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* B£30101B  V4R1    CM Creata
     V* ==============================================================
      *---------------------------------------------------------------------
      * Procedura esecuzione £J15
      *---------------------------------------------------------------------
     P£J15P            B
     D£J15P            PI             7
     D $J15Fu                        10        CONST                            Funzione
     D $J15Me                        10        CONST                            Metodo
     D $J15TN                        15        VALUE                            Tipo Nodo
     D £J15IM                     30000    VARYING                              Immagine
     D $J15ND                       150    OPTIONS(*NOPASS) VALUE               Nome Nodo
     D $J15LP                         1    OPTIONS(*NOPASS) VALUE               Non Analiz.Liv.Prec.
     D $J15ST                       150    OPTIONS(*NOPASS) VALUE               Struttura
     D $J15CO                       150    OPTIONS(*NOPASS) VALUE               Contesto
     D $J15SA                         1    OPTIONS(*NOPASS) VALUE               Senza attributi
     C                   CLEAR                   £J15DS
     C                   EVAL      £J15TN=$J15TN
     C                   IF        %PARMS>= 5
     C                   EVAL      £J15ND=$J15ND
     C                   ENDIF
     C                   IF        %PARMS>= 6
     C                   EVAL      £J15AP=$J15LP
     C                   ENDIF
     C                   IF        %PARMS>= 7
     C                   EVAL      £J15ST=$J15ST
     C                   ENDIF
     C                   IF        %PARMS>= 8
     C                   EVAL      £J15CO=$J15CO
     C                   ENDIF
     C                   IF        %PARMS>= 9
     C                   EVAL      £J15SA=$J15SA
     C                   ENDIF
     C                   CALL      'JAJ150'
     C                   PARM      $J15FU        £J15FU
     C                   PARM      $J15ME        £J15ME
     C                   PARM                    £J15DS
     C                   PARM                    £J15IM
     C                   PARM                    £J15MS
     C                   PARM      *BLANKS       £J15FI
     C     *IN35         PARM      *OFF          £J1535
      * Restituisce VALORE
     C                   RETURN    £J15MS
     P                 E
      *ENDIF
********** PREPROCESSOR COPYEND QPROGEN,£J15P
********** PREPROCESSOR COPYEND QILEGEN,£JAX_O
      *--------------------------------------------------------------*
** TXT
Previsto
Consuntivo
Differenza
Differenza %
** MATTEM
TIP       Classe                       OG                   H12   R
COD       Codice                       [TIP]                H15   R
DES       Descrizione                  [TIP]|[COD]           50   R
ATR_03    Filtro                                             15
TK01      Classe 1                     OG                   H12   R
CK01      Codice 1                     [TK01]               H15   R
DK01      Descrizione 1                [TK01][CK01]          50   R
TK02      Classe 2                     OG                   H12   R
CK02      Codice 2                     [TK01]                15   R
DK02      Descrizione 2                [TK01][CK01]          50   R
TK03      Classe 3                     OG                   H12   R
CK03      Codice 3                     [TK01]                15   R
DK03      Descrizione 3                [TK01][CK01]          50   R
TEMT      Tema                         OG                   H03
TEMC      Tema                         [TEMT]               H03
TEMD      Tema                         [TEMT]|[TEMC]         50
I01T      Indice                       OG                   H03
I01C      Indice                       [I01T]               H03
I01D      Indice                       [I01T]|[I01C]         50
GRA                                                         G10
VAL       Valore                       NR                    11
** MATKPI
TIP       Classe                       OG                   H12
COD       Codice                       [TIP]                H15
DES       Descrizione                  [TIP]|[COD]           256  R
INT       Tipo Indice                  OG                   H12
INK       Codice Indice                [INT]                H15
IND       Indice                       [INT]|[COD]           256  R
VAL       Valore                                             256
** SETA
<Setup Name="Base"
 Parent="A01" />


     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 15/03/21  V5R1    MB Creazione
     V* /COPY £DEC
     V* 12/04/24  V6R1    BERNI  Aggiunta DECDS
     V* 11/06/24  V6R1    GUAGIA Copia da AS400
     V* ==============================================================
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
********** PREPROCESSOR COPYSTART QILEGEN,£DECDS
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £DECDS
      * Sorgente di origine : SMEDEV/QILEGEN(£DECDS)
      * Esportato il        : 20240719 095036
      *====================================================================
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
********** PREPROCESSOR COPYSTART QILEGEN,£TABB£1DS
      *IF NOT DEFINED(£TABB£1DS)
      *DEFINE £TABB£1DS
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
      *ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£TABB£1DS
********** PREPROCESSOR COPYSTART QILEGEN,£PDS
      *IF NOT DEFINED(£PDS)
      *DEFINE £PDS
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
      * Inz
     D £INZFU          S             10    INZ('INZ')
     D £INZME          S             10
     D £INZCO          S             10
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
      *ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£PDS
      *--------------------------------------------------------------------------------------------*
     C     £DEC          BEGSR
      *
1    C                   IF        ££ATCO<>''
1    C                   IF        £DECAM=''
     C                   MOVEL     ££AMBI        £DECAM
     C                   ENDIF
1    C                   IF        £DECCO=''
     C                   MOVEL     ££CONT        £DECCO
1e   C                   ENDIF
1    C                   IF        £DECDT=0
     C                   MOVEL     ££DATE        £DECDT
1e   C                   ENDIF
1e   C                   ENDIF
      *
1    C                   IF        £DECAM=''
     C                   MOVEL     £PDSNP        £DECAM
1e   C                   ENDIF
      *
1    C     £DECR4        IFEQ      *BLANKS
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
     C                   MOVEL     *ON           *IN36
     C                   MOVEL     *ON           £DEC36
1e   C                   ENDIF
     C*
     C* Lancio funzioni su oggetto
1    C     £DECR4        IFNE      *BLANKS
     C     £DECRS        OREQ      '%'
     C     *IN35         ANDEQ     *OFF
     C*
  MO>C                   IF        ££B£2J = '1'
  MO>C                   CALL      'B£DEC5'                             37
  MO>C                   PARM                    £DECR4
  MO>C                   PARM                    £DECCD
  MO>C                   PARM                    £DECTP
  MO>C                   PARM                    £DECPA
  MO>C                   ELSE
     C                   EVAL      *IN37=*OFF
     C                   CALL      'B£DEC5'
     C                   PARM                    £DECR4
     C                   PARM                    £DECCD
     C                   PARM                    £DECTP
     C                   PARM                    £DECPA
  MO>C                   ENDIF
2e   C                   ENDIF
     C*
     C                   MOVEL     *BLANKS       £DECR4            1
     C                   MOVEL     *BLANKS       £DECRS            1
     C                   CLEAR                   £DECCO
     C                   CLEAR                   £DECDT
     C*
     C                   ENDSR
     C*-------------------------------------------------------------

     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 11/03/21  V5R1    BONMAI Creazione
     V* 12/04/24  V6R1    BERNI  Aggiunta OAVDS
     V* 11/06/24  V6R1    GUAGIA Copia da AS400
     V* ==============================================================
     D*----------------------------------------------------------------
     D* OBIETTIVO   FUNZIONI SU Oggetto/attributo/valore
     D*----------------------------------------------------------------
     C* ESEMPIO DI CHIAMATA
     C*
     C* INPUT
     C*                  EVAL      £OAVFU='Funzione'
     C*                  EVAL      £OAVME='Metodo'
     C*                  EVAL      £OAVT1=Tipo_1
     C*                  EVAL      £OAVP1=Parametro_1
     C*                  EVAL      £OAVC1=Codice_1
     C*                  EVAL      £OAVT2=Tipo_2
     C*                  EVAL      £OAVP2=Parametro_2
     C*                  EVAL      £OAVC2=Codice_2
     C*                  EVAL      £OAVAT=Attributo
     C*                  EVAL      £OAVDA=Data
     C*                  EVAL      £OAVCT=Categoria
     C*                  EXSR      £OAV
     C* OUTPUT
     C*                  EVAL      Alfanumerico=£OAVOV
     C*                  EVAL      Numerico=£OAVON
     C*                  EVAL      Inizio=£OAVOD
     C*                  EVAL      Fine=£OAVOA
     C*                  EVAL      Livello=£OAVLI
     C*                  EVAL      Tipo=£OAVOT
     C*                  EVAL      Parametro=£OAVOP
     C*                  EVAL      Intestazione=£OAVIN
     C*                  EVAL      Attributo=£OAVSI
     C*                  EVAL      Mes.live_trovato=£OAVM1
     C*                  EVAL      Mes.val_multiplo=£OAVM2
     C*
     C*---------------------------------------------------------------------
********** PREPROCESSOR COPYSTART QILEGEN,£OAVDS
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £OAVDS
      * Sorgente di origine : SMEDEV/QILEGEN(£OAVDS)
      * Esportato il        : 20240719 094601
      *====================================================================
     D* ==============================================================
     D* MODIFICHE Ril.  T Au Descrizione
     D* gg/mm/aa  nn.mm i xx Breve descrizione
     D* ==============================================================
     D* B£30510A  V3R2   BMA Creazione
     D* B£30901H  V4R1   BMA Ritorno valore esteso OAV
     V* B£51211A  V4R1   BMA Adeguamento tracciato B£SLOT
     V* 26/02/16  V4R1   BMA Autoenter in flag 5
     V* 18/03/16  V4R1   BMA Cambiata descrizione campo TEFI
     V* B£51211A  V4R1   BMA Rilasciato
     V* 20/09/16  V4R1   BMA Aumentato £OAVO_VAES e £OAVDO e £OAVDI
     V* 28/06/17  V5R1    BS Aggiunto campo parametro programma
     V* B£71108A  V5R1   BMA Modificata £OAVO_VAES a lunghezza variabile
     V* 14/01/22  003451  PC Aggiunta sezione e ordinamento
     V* 10/03/22  V6R1    BERNI Check-out 003451 in SMEDEV
     D* ==============================================================
      *
      * DS di Input
      * -----------
     D£OAVDI           DS          1024
      * Controllo autorizzazioni
     D £OAVI_CTRA                     1
      * Parametri liberi
     D £OAVI_PARM                   100
      *
      * DS di Output
      * ------------
     D£OAVDO           DS         32000
      * Non autorizzato
     D £OAVO_NOAU                     1
      * Valore esteso
     D £OAVO_VAES                 30000    VARYING
      * Nome campo (solo funzioni COS e SCA)
     D £OAVO_FLDN                    10
      * Oggetto attributo
     D £OAVO_OGGA                    50
      * Lunghezza campo attributo (solo funzione SCA)
     D £OAVO_LUNG                     5
      * Decimali campo attributo (solo funzione SCA)
     D £OAVO_DECI                     2
      * Obbligatorio: O (solo funzioni COS e SCA)
     D £OAVO_OBBL                     1
      * Non controllare tipo: V2SI/NO (solo funzioni COS e SCA)
     D £OAVO_NCTP                     1
      * Non controllare consistenza: V2SI/NO (solo funzioni COS e SCA)
     D £OAVO_NCCO                     1
      * Input/Output: K/B/O/H (solo funzioni COS e SCA)
     D £OAVO_INOU                     1
      * Case sensitive: LC/UC  (solo funzioni COS e SCA)
     D £OAVO_CASE                     2
      * Descrizione dinamica: V2SI/NO (solo funzioni COS e SCA)
     D £OAVO_TEFI                     1
      * Lunghezza Grafica (solo funzioni COS e SCA)
     D £OAVO_LUGR                     5
      * Log di modifica (solo funzioni COS e SCA)
      *                                      (Valori   = Se attivo log o notifica da oggetto
      *                                              1 = Si, sempre
      *                                              2 = Si, sempre con notifica
      *                                              3 = No, mai anche se attivo su oggetto)
      *                                              4 = No notifica, mai anche se attiv.su oggetto)
     D £OAVO_LOGM                     1
      * Campo chiave del file (solo funzioni COS e SCA)
     D £OAVO_FLKY                     1
      * Autoenter
     D £OAVO_AUEN                     1
      * Sottoparametro
     D £OAVO_SPAR                     1
      * Parametro Programma
     D £OAVO_PARC                    10
      * Sezione
     D £OAVO_SEZI                    30
      * Ordinamento
     D £OAVO_ORDI                    30
      *
      *----------------------------------------------------------------*
********** PREPROCESSOR COPYEND QILEGEN,£OAVDS
********** PREPROCESSOR COPYSTART QILEGEN,£TABB£1DS
      *IF NOT DEFINED(£TABB£1DS)
      *DEFINE £TABB£1DS
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
      *ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£TABB£1DS
      *--------------------------------------------------------------------------------------------*
     C     £OAV          BEGSR
      *
     C                   SELECT
     C                   WHEN      £OAVFU='CLO'
     C                   EVAL      £OAVPG='B£OAV6'
  MO>C                   IF        ££B£2J = '1'
  MO>C                   CALL      £OAVPG                               37
     C                   PARM                    £OAVFU           10            Funzione
     C                   PARM                    £OAVME           10            Metodo
     C                   PARM                    £OAVLC            1            Livello chiamata
  MO>C                   ELSE
     C                   EVAL      *IN37=*OFF
     C                   CALL      £OAVPG
     C                   PARM                    £OAVFU           10            Funzione
     C                   PARM                    £OAVME           10            Metodo
     C                   PARM                    £OAVLC            1            Livello chiamata
     C                   ENDIF
     C                   OTHER
     C     'B£OAV0'      CAT(P)    £OAVLC:0      £OAVPG           10
      *C                   ENDSL
      *
  MO>C                   IF        ££B£2J = '1'
  MO>C                   CALL      £OAVPG                               37      B£OAV6/7
  MO>C                   PARM                    £OAVFU           10            Funzione
  MO>C                   PARM                    £OAVME           10            Metodo
  MO>C                   PARM                    £OAVT1            2            Tipo 1
  MO>C                   PARM                    £OAVP1           10            Param 1
  MO>C                   PARM                    £OAVC1           15            Codice 1
  MO>C                   PARM                    £OAVT2            2            Tipo 2
  MO>C                   PARM                    £OAVP2           10            Param 2
  MO>C                   PARM                    £OAVC2           15            Codice 2
  MO>C                   PARM                    £OAVAT           15            Cod.attr.
  MO>C                   PARM                    £OAVDA            8 0          Dta rifer.
  MO>C                   PARM      *BLANKS       £OAVOV           15            Val.attr.
  MO>C                   PARM      0             £OAVON           15 5          Num.attr.
  MO>C                   PARM      0             £OAVOD            8 0          Dta da att
  MO>C                   PARM      0             £OAVOA            8 0          Dta a  att
  MO>C                   PARM      *BLANKS       £OAVOT            2            Tipo
  MO>C                   PARM      *BLANKS       £OAVOP           10            Param
  MO>C                   PARM                    £OAVCT           10            Categoria
  MO>C                   PARM                    £OAVLI            2 0          Livello
  MO>C                   PARM                    £OAVDV           15            Deviazione
  MO>C                   PARM      *BLANKS       £OAVIN           35            Intestaz.att
  MO>C                   PARM      *BLANKS       £OAVSI           35            Signif.attr.
  MO>C                   PARM                    £OAVM1            7            Msg livello
  MO>C                   PARM                    £OAVM2            7            Msg val.mult
  MO>C                   PARM      *BLANKS       £OAVMS            7            Codice msg
  MO>C                   PARM      *BLANKS       £OAVFI           10
  MO>C                   PARM      *BLANKS       £OAVCM            2            Ultimo cmd
  MO>C     *IN35         PARM      '0'           £OAV35            1
  MO>C     *IN36         PARM      '0'           £OAV36            1
     C                   PARM                    £OAVDI
     C                   PARM                    £OAVDO
  MO> *
  MO>C                   ELSE
     C                   EVAL      *IN37=*OFF
     C                   CALL      £OAVPG                                       B£OAV6/7
     C                   PARM                    £OAVFU           10            Funzione
     C                   PARM                    £OAVME           10            Metodo
     C                   PARM                    £OAVT1            2            Tipo 1
     C                   PARM                    £OAVP1           10            Param 1
     C                   PARM                    £OAVC1           15            Codice 1
     C                   PARM                    £OAVT2            2            Tipo 2
     C                   PARM                    £OAVP2           10            Param 2
     C                   PARM                    £OAVC2           15            Codice 2
     C                   PARM                    £OAVAT           15            Cod.attr.
     C                   PARM                    £OAVDA            8 0          Dta rifer.
     C                   PARM      *BLANKS       £OAVOV           15            Val.attr.
     C                   PARM      0             £OAVON           15 5          Num.attr.
     C                   PARM      0             £OAVOD            8 0          Dta da att
     C                   PARM      0             £OAVOA            8 0          Dta a  att
     C                   PARM      *BLANKS       £OAVOT            2            Tipo
     C                   PARM      *BLANKS       £OAVOP           10            Param
     C                   PARM                    £OAVCT           10            Categoria
     C                   PARM                    £OAVLI            2 0          Livello
     C                   PARM                    £OAVDV           15            Deviazione
     C                   PARM      *BLANKS       £OAVIN           35            Intestaz.att
     C                   PARM      *BLANKS       £OAVSI           35            Signif.attr.
     C                   PARM                    £OAVM1            7            Msg livello
     C                   PARM                    £OAVM2            7            Msg val.mult
     C                   PARM      *BLANKS       £OAVMS            7            Codice msg
     C                   PARM      *BLANKS       £OAVFI           10
     C                   PARM      *BLANKS       £OAVCM            2            Ultimo cmd
     C     *IN35         PARM      '0'           £OAV35            1
     C     *IN36         PARM      '0'           £OAV36            1
     C                   PARM                    £OAVDI
     C                   PARM                    £OAVDO
      *
  MO>C                   ENDIF
     C                   ENDSL
  MO>C   37              DO
  MO>C**   £OAVPG        IFNE      'B£OAV0'
  MO>C**                 MOVEL(P)  'B£OAV0'      £OAVPG
  MO>C**                 ENDIF
  MO>C                   CALL      'B£GGP0  '
  MO>C                   PARM      £OAVPG        £GGPNP           10
  MO>C                   PARM      *BLANKS       £GGPTP           10
  MO>C                   PARM      *BLANKS       £GGPPA          100
  MO>C                   ENDDO
      *
     C                   MOVEL     £OAVLC        £OAVLC            1
      *
     C                   ENDSR
      *--------------------------------------------------------------------------------------------*

     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 15/03/21  V5R1    MB Creazione
     V* /COPY £DEC
     V* 12/04/24  V6R1    BERNI  Aggiunta DECDS
     V* 11/06/24  V6R1    GUAGIA Copia da AS400
     V* ==============================================================
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
********** PREPROCESSOR COPYSTART QILEGEN,£DECDS
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £DECDS
      * Sorgente di origine : SMEDEV/QILEGEN(£DECDS)
      * Esportato il        : 20240719 095036
      *====================================================================
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
********** PREPROCESSOR COPYSTART QILEGEN,£TABB£1DS
      *IF NOT DEFINED(£TABB£1DS)
      *DEFINE £TABB£1DS
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
      *ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£TABB£1DS
********** PREPROCESSOR COPYSTART QILEGEN,£PDS
      *IF NOT DEFINED(£PDS)
      *DEFINE £PDS
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
      * Inz
     D £INZFU          S             10    INZ('INZ')
     D £INZME          S             10
     D £INZCO          S             10
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
      *ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£PDS
      *--------------------------------------------------------------------------------------------*
     C     £DEC          BEGSR
      *
1    C                   IF        ££ATCO<>''
1    C                   IF        £DECAM=''
     C                   MOVEL     ££AMBI        £DECAM
     C                   ENDIF
1    C                   IF        £DECCO=''
     C                   MOVEL     ££CONT        £DECCO
1e   C                   ENDIF
1    C                   IF        £DECDT=0
     C                   MOVEL     ££DATE        £DECDT
1e   C                   ENDIF
1e   C                   ENDIF
      *
1    C                   IF        £DECAM=''
     C                   MOVEL     £PDSNP        £DECAM
1e   C                   ENDIF
      *
1    C     £DECR4        IFEQ      *BLANKS
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
     C                   MOVEL     *ON           *IN36
     C                   MOVEL     *ON           £DEC36
1e   C                   ENDIF
     C*
     C* Lancio funzioni su oggetto
1    C     £DECR4        IFNE      *BLANKS
     C     £DECRS        OREQ      '%'
     C     *IN35         ANDEQ     *OFF
     C*
  MO>C                   IF        ££B£2J = '1'
  MO>C                   CALL      'B£DEC5'                             37
  MO>C                   PARM                    £DECR4
  MO>C                   PARM                    £DECCD
  MO>C                   PARM                    £DECTP
  MO>C                   PARM                    £DECPA
  MO>C                   ELSE
     C                   EVAL      *IN37=*OFF
     C                   CALL      'B£DEC5'
     C                   PARM                    £DECR4
     C                   PARM                    £DECCD
     C                   PARM                    £DECTP
     C                   PARM                    £DECPA
  MO>C                   ENDIF
2e   C                   ENDIF
     C*
     C                   MOVEL     *BLANKS       £DECR4            1
     C                   MOVEL     *BLANKS       £DECRS            1
     C                   CLEAR                   £DECCO
     C                   CLEAR                   £DECDT
     C*
     C                   ENDSR
     C*-------------------------------------------------------------

     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 11/03/21  V5R1    BONMAI Creazione
     V* 12/04/24  V6R1    BERNI  Aggiunta OAVDS
     V* 11/06/24  V6R1    GUAGIA Copia da AS400
     V* ==============================================================
     D*----------------------------------------------------------------
     D* OBIETTIVO   FUNZIONI SU Oggetto/attributo/valore
     D*----------------------------------------------------------------
     C* ESEMPIO DI CHIAMATA
     C*
     C* INPUT
     C*                  EVAL      £OAVFU='Funzione'
     C*                  EVAL      £OAVME='Metodo'
     C*                  EVAL      £OAVT1=Tipo_1
     C*                  EVAL      £OAVP1=Parametro_1
     C*                  EVAL      £OAVC1=Codice_1
     C*                  EVAL      £OAVT2=Tipo_2
     C*                  EVAL      £OAVP2=Parametro_2
     C*                  EVAL      £OAVC2=Codice_2
     C*                  EVAL      £OAVAT=Attributo
     C*                  EVAL      £OAVDA=Data
     C*                  EVAL      £OAVCT=Categoria
     C*                  EXSR      £OAV
     C* OUTPUT
     C*                  EVAL      Alfanumerico=£OAVOV
     C*                  EVAL      Numerico=£OAVON
     C*                  EVAL      Inizio=£OAVOD
     C*                  EVAL      Fine=£OAVOA
     C*                  EVAL      Livello=£OAVLI
     C*                  EVAL      Tipo=£OAVOT
     C*                  EVAL      Parametro=£OAVOP
     C*                  EVAL      Intestazione=£OAVIN
     C*                  EVAL      Attributo=£OAVSI
     C*                  EVAL      Mes.live_trovato=£OAVM1
     C*                  EVAL      Mes.val_multiplo=£OAVM2
     C*
     C*---------------------------------------------------------------------
********** PREPROCESSOR COPYSTART QILEGEN,£OAVDS
      *====================================================================
      * smeup V6R1.024DV
      * Nome sorgente       : £OAVDS
      * Sorgente di origine : SMEDEV/QILEGEN(£OAVDS)
      * Esportato il        : 20240719 094601
      *====================================================================
     D* ==============================================================
     D* MODIFICHE Ril.  T Au Descrizione
     D* gg/mm/aa  nn.mm i xx Breve descrizione
     D* ==============================================================
     D* B£30510A  V3R2   BMA Creazione
     D* B£30901H  V4R1   BMA Ritorno valore esteso OAV
     V* B£51211A  V4R1   BMA Adeguamento tracciato B£SLOT
     V* 26/02/16  V4R1   BMA Autoenter in flag 5
     V* 18/03/16  V4R1   BMA Cambiata descrizione campo TEFI
     V* B£51211A  V4R1   BMA Rilasciato
     V* 20/09/16  V4R1   BMA Aumentato £OAVO_VAES e £OAVDO e £OAVDI
     V* 28/06/17  V5R1    BS Aggiunto campo parametro programma
     V* B£71108A  V5R1   BMA Modificata £OAVO_VAES a lunghezza variabile
     V* 14/01/22  003451  PC Aggiunta sezione e ordinamento
     V* 10/03/22  V6R1    BERNI Check-out 003451 in SMEDEV
     D* ==============================================================
      *
      * DS di Input
      * -----------
     D£OAVDI           DS          1024
      * Controllo autorizzazioni
     D £OAVI_CTRA                     1
      * Parametri liberi
     D £OAVI_PARM                   100
      *
      * DS di Output
      * ------------
     D£OAVDO           DS         32000
      * Non autorizzato
     D £OAVO_NOAU                     1
      * Valore esteso
     D £OAVO_VAES                 30000    VARYING
      * Nome campo (solo funzioni COS e SCA)
     D £OAVO_FLDN                    10
      * Oggetto attributo
     D £OAVO_OGGA                    50
      * Lunghezza campo attributo (solo funzione SCA)
     D £OAVO_LUNG                     5
      * Decimali campo attributo (solo funzione SCA)
     D £OAVO_DECI                     2
      * Obbligatorio: O (solo funzioni COS e SCA)
     D £OAVO_OBBL                     1
      * Non controllare tipo: V2SI/NO (solo funzioni COS e SCA)
     D £OAVO_NCTP                     1
      * Non controllare consistenza: V2SI/NO (solo funzioni COS e SCA)
     D £OAVO_NCCO                     1
      * Input/Output: K/B/O/H (solo funzioni COS e SCA)
     D £OAVO_INOU                     1
      * Case sensitive: LC/UC  (solo funzioni COS e SCA)
     D £OAVO_CASE                     2
      * Descrizione dinamica: V2SI/NO (solo funzioni COS e SCA)
     D £OAVO_TEFI                     1
      * Lunghezza Grafica (solo funzioni COS e SCA)
     D £OAVO_LUGR                     5
      * Log di modifica (solo funzioni COS e SCA)
      *                                      (Valori   = Se attivo log o notifica da oggetto
      *                                              1 = Si, sempre
      *                                              2 = Si, sempre con notifica
      *                                              3 = No, mai anche se attivo su oggetto)
      *                                              4 = No notifica, mai anche se attiv.su oggetto)
     D £OAVO_LOGM                     1
      * Campo chiave del file (solo funzioni COS e SCA)
     D £OAVO_FLKY                     1
      * Autoenter
     D £OAVO_AUEN                     1
      * Sottoparametro
     D £OAVO_SPAR                     1
      * Parametro Programma
     D £OAVO_PARC                    10
      * Sezione
     D £OAVO_SEZI                    30
      * Ordinamento
     D £OAVO_ORDI                    30
      *
      *----------------------------------------------------------------*
********** PREPROCESSOR COPYEND QILEGEN,£OAVDS
********** PREPROCESSOR COPYSTART QILEGEN,£TABB£1DS
      *IF NOT DEFINED(£TABB£1DS)
      *DEFINE £TABB£1DS
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
      *ENDIF
********** PREPROCESSOR COPYEND QILEGEN,£TABB£1DS
      *--------------------------------------------------------------------------------------------*
     C     £OAV          BEGSR
      *
     C                   SELECT
     C                   WHEN      £OAVFU='CLO'
     C                   EVAL      £OAVPG='B£OAV6'
  MO>C                   IF        ££B£2J = '1'
  MO>C                   CALL      £OAVPG                               37
     C                   PARM                    £OAVFU           10            Funzione
     C                   PARM                    £OAVME           10            Metodo
     C                   PARM                    £OAVLC            1            Livello chiamata
  MO>C                   ELSE
     C                   EVAL      *IN37=*OFF
     C                   CALL      £OAVPG
     C                   PARM                    £OAVFU           10            Funzione
     C                   PARM                    £OAVME           10            Metodo
     C                   PARM                    £OAVLC            1            Livello chiamata
     C                   ENDIF
     C                   OTHER
     C     'B£OAV0'      CAT(P)    £OAVLC:0      £OAVPG           10
      *C                   ENDSL
      *
  MO>C                   IF        ££B£2J = '1'
  MO>C                   CALL      £OAVPG                               37      B£OAV6/7
  MO>C                   PARM                    £OAVFU           10            Funzione
  MO>C                   PARM                    £OAVME           10            Metodo
  MO>C                   PARM                    £OAVT1            2            Tipo 1
  MO>C                   PARM                    £OAVP1           10            Param 1
  MO>C                   PARM                    £OAVC1           15            Codice 1
  MO>C                   PARM                    £OAVT2            2            Tipo 2
  MO>C                   PARM                    £OAVP2           10            Param 2
  MO>C                   PARM                    £OAVC2           15            Codice 2
  MO>C                   PARM                    £OAVAT           15            Cod.attr.
  MO>C                   PARM                    £OAVDA            8 0          Dta rifer.
  MO>C                   PARM      *BLANKS       £OAVOV           15            Val.attr.
  MO>C                   PARM      0             £OAVON           15 5          Num.attr.
  MO>C                   PARM      0             £OAVOD            8 0          Dta da att
  MO>C                   PARM      0             £OAVOA            8 0          Dta a  att
  MO>C                   PARM      *BLANKS       £OAVOT            2            Tipo
  MO>C                   PARM      *BLANKS       £OAVOP           10            Param
  MO>C                   PARM                    £OAVCT           10            Categoria
  MO>C                   PARM                    £OAVLI            2 0          Livello
  MO>C                   PARM                    £OAVDV           15            Deviazione
  MO>C                   PARM      *BLANKS       £OAVIN           35            Intestaz.att
  MO>C                   PARM      *BLANKS       £OAVSI           35            Signif.attr.
  MO>C                   PARM                    £OAVM1            7            Msg livello
  MO>C                   PARM                    £OAVM2            7            Msg val.mult
  MO>C                   PARM      *BLANKS       £OAVMS            7            Codice msg
  MO>C                   PARM      *BLANKS       £OAVFI           10
  MO>C                   PARM      *BLANKS       £OAVCM            2            Ultimo cmd
  MO>C     *IN35         PARM      '0'           £OAV35            1
  MO>C     *IN36         PARM      '0'           £OAV36            1
     C                   PARM                    £OAVDI
     C                   PARM                    £OAVDO
  MO> *
  MO>C                   ELSE
     C                   EVAL      *IN37=*OFF
     C                   CALL      £OAVPG                                       B£OAV6/7
     C                   PARM                    £OAVFU           10            Funzione
     C                   PARM                    £OAVME           10            Metodo
     C                   PARM                    £OAVT1            2            Tipo 1
     C                   PARM                    £OAVP1           10            Param 1
     C                   PARM                    £OAVC1           15            Codice 1
     C                   PARM                    £OAVT2            2            Tipo 2
     C                   PARM                    £OAVP2           10            Param 2
     C                   PARM                    £OAVC2           15            Codice 2
     C                   PARM                    £OAVAT           15            Cod.attr.
     C                   PARM                    £OAVDA            8 0          Dta rifer.
     C                   PARM      *BLANKS       £OAVOV           15            Val.attr.
     C                   PARM      0             £OAVON           15 5          Num.attr.
     C                   PARM      0             £OAVOD            8 0          Dta da att
     C                   PARM      0             £OAVOA            8 0          Dta a  att
     C                   PARM      *BLANKS       £OAVOT            2            Tipo
     C                   PARM      *BLANKS       £OAVOP           10            Param
     C                   PARM                    £OAVCT           10            Categoria
     C                   PARM                    £OAVLI            2 0          Livello
     C                   PARM                    £OAVDV           15            Deviazione
     C                   PARM      *BLANKS       £OAVIN           35            Intestaz.att
     C                   PARM      *BLANKS       £OAVSI           35            Signif.attr.
     C                   PARM                    £OAVM1            7            Msg livello
     C                   PARM                    £OAVM2            7            Msg val.mult
     C                   PARM      *BLANKS       £OAVMS            7            Codice msg
     C                   PARM      *BLANKS       £OAVFI           10
     C                   PARM      *BLANKS       £OAVCM            2            Ultimo cmd
     C     *IN35         PARM      '0'           £OAV35            1
     C     *IN36         PARM      '0'           £OAV36            1
     C                   PARM                    £OAVDI
     C                   PARM                    £OAVDO
      *
  MO>C                   ENDIF
     C                   ENDSL
  MO>C   37              DO
  MO>C**   £OAVPG        IFNE      'B£OAV0'
  MO>C**                 MOVEL(P)  'B£OAV0'      £OAVPG
  MO>C**                 ENDIF
  MO>C                   CALL      'B£GGP0  '
  MO>C                   PARM      £OAVPG        £GGPNP           10
  MO>C                   PARM      *BLANKS       £GGPTP           10
  MO>C                   PARM      *BLANKS       £GGPPA          100
  MO>C                   ENDDO
      *
     C                   MOVEL     £OAVLC        £OAVLC            1
      *
     C                   ENDSR
      *--------------------------------------------------------------------------------------------*