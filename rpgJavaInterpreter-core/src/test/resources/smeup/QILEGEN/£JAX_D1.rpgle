      /IF NOT DEFINED(JAX_D1_INCLUDED)
      /DEFINE JAX_D1_INCLUDED
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 20/03/24  KOKOS   BERNI Riportato da SMEDEV
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
      /ENDIF
