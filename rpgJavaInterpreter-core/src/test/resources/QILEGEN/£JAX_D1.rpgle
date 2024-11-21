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
      /COPY QILEGEN,£K04DS
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
