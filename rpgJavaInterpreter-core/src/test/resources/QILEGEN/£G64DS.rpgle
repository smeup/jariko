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
