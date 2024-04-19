      *====================================================================
      * smeup V6R1.021DV
      * Nome sorgente       : £D5_095DS
      * Sorgente di origine : PER_SUP/QILEGEN(£D5_095DS)
      * Esportato il        : 20240418 142411
      *====================================================================
     V*================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 11/10/20          BS Creazione
     V* ££10916B  V5R1    BS *************************
     V* ££10916B  V5R1    BS **************** Rilascio intero sorgente da srvamm a SMEDEV
     V* ££10916B  V5R1    BS *************************
     V* 12/01/22          BS Aggiunto attributo grafico
     V* 27/09/23          BS Aggiunti campi disattivazione odifica e chiusura
     V* 14/01/24          BS Aggiunti campo con funzione specifica di modifica
     V* ==============================================================
      * Ds di input
     D £095DSI         DS         10000
     D  £095I_FU                     10                                         Funzione
     D  £095I_ME                     10                                         Metodo
     D  £095I_AZ                      2                                         Azienda
     D  £095I_TC                     12                                         Tipo Collaboratore
     D  £095I_CC                     15                                         Collaboratore
     D  £095I_GF                     15                                         Gruppo Fonti
     D  £095I_DI                      8  0                                      Data Inizio
     D  £095I_DF                      8  0                                      Data Fine
      *
      * Fare il "gruppo fonte"
     D**£095I_FO****                 12    DIM(100)                             Tipo fonte
     D**£095I_FF****                 15                                         Fonte
      *
     D**£095I_LI****                  1                                         Livello Inizio
     D**£095I_LF****                  1                                         Livello Fine
      *
     D  £095I_FO                     15                                         Fonte
     D  £095I_TO                     12                                         Tipo Oggetto
     D  £095I_CO                     15                                         Codice Oggetto
     D  £095I_DT                      8  0                                      Data
     D  £095I_QT                     21  6                                      Qtà ore
     D  £095I_ST                      2                                         Stato
     D  £095I_PC                      5  2                                      % Completamento
     D  £095I_IA                     15                                         Id attività
     D  £095I_HR                      6  0                                      Ora
     D  £095I_CL                      1                                         Chiusura
     D  £095I_NO                   2560                                         Nota
     D  £095I_NA                   2560                                         Nota aggiuntiva
      *
      * Ds di output
     D £095DSO         DS          5000
     D  £095O_MS                      7
     D  £095O_FI                     10
     D  £095O_VA                     45
     D  £095O_35                      1N
      *
      * Ds di input
     D £095DSR         DS          5000
     D  £095R_FO                     15                                         Fonte
     D  £095R_AZ                      2                                         Azienda
     D  £095R_TP                     12                                         Tipo origine
     D  £095R_CD                     15                                         Codice origine
     D  £095R_DE                    256                                         Descrizione
     D  £095R_LI                      1                                         Livello
      *
     D  £095R_KFG                     1                                         Gestione Riclass.
     D  £095R_RF                     12                                         Tipo Riclassifica F
     D  £095R_KF                     15                                         Cod. Riclassifica F
      *
     D  £095R_CCG                     1                                         Gestione Assegnatar.
     D  £095R_TC                     12                                         Tipo Assegnatario
     D  £095R_CC                     15                                         Codice Assegnatario
      *
     D  £095R_CRG                     1                                         Gestione Responsabil
     D  £095R_TR                     12                                         Tipo Responsabile
     D  £095R_CR                     15                                         Codice Responsabile
      *
     D  £095R_CEG                     1                                         Gestione Richiedente
     D  £095R_TE                     12                                         Tipo Richiedente
     D  £095R_CE                     15                                         Codice Richiedente
      *
     D  £095R_DIG                     1                                         Gestione Data inizio
     D  £095R_DI                      8  0                                      Data Inizio
      *
     D  £095R_OIG                     1                                         Gestione Ora I.P.
     D  £095R_OI                      6  0                                      Ora Inizio Prev.
      *
     D  £095R_DPG                     1                                         Gestione Data F.P.
     D  £095R_DP                      8  0                                      Data Fine Prevista
     D  £095R_OPG                     1                                         Gestione Ora Previst
     D  £095R_OP                      6  0                                      Ora Prevista
     D  £095R_QRG                     1                                         Gestione Ore Previst
     D  £095R_QP                     21  6                                      Ore Prevista
      *
     D  £095R_STG                     1                                         Gestione Stato
     D  £095R_SS                     12                                         Tipo oggetto stato
     D  £095R_ST                     15                                         Codice Stato
      *
     D  £095R_PRG                     1                                         Gestione priorità
     D  £095R_PR                      2                                         Priorità
      *
     D  £095R_DF                      8  0                                      Data Effettiva
      *
     D  £095R_OFG                     1
     D  £095R_OF                      6  0                                      Ora Effettiva
      *
     D  £095R_QFG                     1
     D  £095R_QF                     21  6                                      Ore Effettive
     D  £095R_PCG                     1
     D  £095R_PC                      5  2                                      % Completamento
      *
     D  £095R_CM                     10                                         Commessa
     D  £095R_CK                      6                                         Centro di costo
      *
      * Calcolati
     D  £095R_QR                     21  6                                      Ore Residue
     D  £095R_CN                      1                                         Condizione
      *
     D  £095R_IC                     70                                         Icona
      *
     D  £095R_TI                      1                                         Attività / Ore Lav.
      *
     D  £095R_KD                      1                                         Condizione Data/Ora
      *
     D  £095R_AG                     10                                         Attributo grafico
      *
     D  £095R_GM                      1                                         Disattiva modifica
      *
     D  £095R_GC                      1                                         Disattiva chiusura
      *
     D  £095R_FM                    256                                         Funzione modifica
      *
