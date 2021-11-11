     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 04/08/21  003102  BUSFIO Creazione
     V* 05/08/21  003102  BUSFIO Aggiornato elenco mute, aggiunto numero iterazioni
     V* 06/08/21  003102  BUSFIO Creazione CSV per D5COSO
     V* 09/08/21  003102  BUSFIO Terminata creazione CSV per D5COSO
     V* 12/08/21  003102  BUSFIO Modificato per aggirare errori in jariko
     V* 09/08/21  003102  BUSFIO Pulizia codice
     V* 16/09/21  003102  BUSFIO Aggiunta nuova variabile per la Entry
     V* ==============================================================
     H/COPY QILEGEN,£INIZH
      *--------------------------------------------------------------------------------------------*
      /COPY QILEGEN,£TABB£1DS
      /COPY QILEGEN,£PDS
      *--------------------------------------------------------------------------------------------*
     D MUTE            S              9    CTDATA PERRCD(1)  DIM(18)             _NOTXT
     D SPLTESTNAME     S            100    CTDATA PERRCD(1)  DIM(17)             _NOTXT
     D CSV_INT         S            100    CTDATA PERRCD(1)  DIM(05)             _NOTXT
     D FILENAME        S              8    CTDATA PERRCD(1)  DIM(02)             _NOTXT  
      *--------------------------------------------------------------------------------------------*
     D EXEC_DB         S             36                                          Entry per DB
     D EXEC_NM         S              4  0                                       Entry Nm iteration
      *--------------------------------------------------------------------------------------------*
     D MU_TIME         S             10                                          Tempo
     D MU_TSNAME       S             45                                          Test name
     D MU_FLNAME       S             10                                          File name
     D MU_TPOPER       S             15                                          Type oper
     D MU_FAIL         S              3                                          Test Fail
      *--------------------------------------------------------------------------------------------*
     D $TIMST          S               Z   INZ
     D §ST             S            500
     D $X              S              4  0
     D $INDS           S              4  0
     D $$DB            S              2
     D $$DATABASE      S             20  
      *--Routine creazione Csv D5------------------------------------------------------------------*
     D $AVERAGE        S              8  2                                       Variabile per media
     D $SUMTIME        S              6  0                                       Variabile per somma
     D $NTIME          S              2  0                                       N time trovato dato
     D $F              S              4  0                                       Indice
     D $T              S              4  0                                       Indice
     D $NDATA          S              4  0                                       Numeratore DS DATA
     D $SPL            S             60                                          Numeratore DS DATA
     D $MSGOUT         S            500
      *--------------------------------------------------------------------------------------------*
      * Struttura DS - Lettura da CSV
     D                 DS
     D §TAB                         163    DIM(4000) INZ
     D  §TIME                        23    OVERLAY(§TAB:1)                      Time
     D  §MUNAM                        9    OVERLAY(§TAB:*NEXT)                  Mute name
     D  §TSNAM                       42    OVERLAY(§TAB:*NEXT)                  Test name
     D  §DBNAM                       15    OVERLAY(§TAB:*NEXT)                  Db name
     D  §FLNAM                        8    OVERLAY(§TAB:*NEXT)                  File name
     D  §TYOPE                       12    OVERLAY(§TAB:*NEXT)                  Type operation
     D  §TIMEE                        8    OVERLAY(§TAB:*NEXT)                  Time elapsed
     D  §DRIVE                       36    OVERLAY(§TAB:*NEXT)                  Driver
     D  §VERSI                        1    OVERLAY(§TAB:*NEXT)                  Version
     D  §ENVIR                        6    OVERLAY(§TAB:*NEXT)                  Environment
     D  §FAIL                         3    OVERLAY(§TAB:*NEXT)                  Fail
      * Struttura DS - Analisi Test name
     D                 DS
     D §DATA                         50    DIM(4000)  INZ
     D  §DATA_NM                     42    OVERLAY(§DATA:1)
     D  §DATA_TM                      8    OVERLAY(§DATA:*NEXT)
      *--------------------------------------------------------------------------------------------*
      * Impostazioni iniziali
     C                   EXSR      IMP0
      *
     C                   EXSR      EXTRES
      * Recupero tempo
     C                   TIME                    $TIMST
      * Calcolo righe per D5
     C                   EXSR      DSTD5      
      *
     C                   EXSR      FIN0
      *
     C                   SETON                                        LR
      *--------------------------------------------------------------*
     D* Estrazione tempi test
      *--------------------------------------------------------------*
     C     EXTRES        BEGSR
      * Inizializzazione
     C                   EVAL      §TAB = ' '
     C                   EVAL      $INDS=0      
     C                   TIME                    $TIMST
      * Riga intestazione 
     C                   EVAL      $MSGOUT=%TRIMR(CSV_INT(01))
     C                                    +%TRIMR(CSV_INT(02))
     C     $MSGOUT       DSPLY     £PDSNU     
      * Ciclo esecuzione MUTE
     C                   FOR       $X=1 TO %ELEM(MUTE)
      * Ciclo per n iterazioni
     C                   DO        EXEC_NM
      *
     C                   TIME                    $TIMST
      * Eseguo Mute
     C                   CALL      MUTE($X)
     C                   PARM      'SI'          MU_TIME
     C                   PARM                    MU_TSNAME
     C                   PARM                    MU_FLNAME
     C                   PARM                    MU_TPOPER
     C                   PARM                    MU_FAIL
      * Incremento indice DS
     C                   EVAL      $INDS=$INDS+1
      * valorizzo DS
     C                   EVAL      §TIME($INDS)= %SUBST(%CHAR($TIMST):1:23)
     C                   EVAL      §MUNAM($INDS)= MUTE($X)
     C                   EVAL      §TSNAM($INDS)= %SUBST(%TRIM(MU_TSNAME):4)
     C                   EVAL      §DBNAM($INDS)= %TRIM($$DATABASE)
     C                   EVAL      §FLNAM($INDS)= %TRIM(MU_FLNAME)
     C                   EVAL      §TYOPE($INDS)= %TRIM(MU_TPOPER)
     C                   EVAL      §TIMEE($INDS)= %TRIM(MU_TIME)
     C                   EVAL      §DRIVE($INDS)= %TRIM(EXEC_DB)
     C                   EVAL      §VERSI($INDS)= ' '
     C                   EVAL      §ENVIR($INDS)= 'Jariko'
     C                   EVAL      §FAIL($INDS)= %TRIM(MU_FAIL)
      *
      * Scrivo riga csv 
     C                   EVAL      $MSGOUT= §TIME($INDS) + ','+§MUNAM($INDS)+','
     C                                    +§TSNAM($INDS) + ','+§DBNAM($INDS)+','
     C                                    +§FLNAM($INDS) + ','+§TYOPE($INDS)+','
     C                                    +§TIMEE($INDS) + ','+§DRIVE($INDS)+','
     C                                    +§VERSI($INDS) + ','+§ENVIR($INDS)+','
     C                                    +§FAIL($INDS)
     C     $MSGOUT       DSPLY     £PDSNU      
     C                   ENDDO
      *
     C                   ENDFOR
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD*  Creo CSV per il D5COSO
      *--------------------------------------------------------------*
     C     DSTD5         BEGSR
      * Pulizia DS
     C                   EVAL      §DATA = ' '
      * Riga intestazione
     C                   EVAL      $MSGOUT=%TRIMR(CSV_INT(03))+%TRIMR(
     C                                   CSV_INT(04))+%TRIMR(CSV_INT(05))
     C     $MSGOUT       DSPLY     £PDSNU
      * Setto righe
     C                   FOR       $F = 1 TO %ELEM(FILENAME)
      * Variabile stringa OUTPUT
     C                   EVAL      §ST = 'OJ*FILE;'                             COSTANTE
     C                                 + FILENAME($F) + ';'                     COSTANTE
     C                                 + 'MU0;'+ $$DB +';;03;'                  COSTANTE
     C                                 + %SUBST(%CHAR($TIMST):1:4)              COSTANTE
     C                                 + %SUBST(%CHAR($TIMST):6:2) +';;'        COSTANTE
      * Cerco per FILENAME
     C                   EVAL      $NDATA = 0      
     C                   EVAL      $X = 1 
     C                   DO        $INDS         
     C                   IF        §FLNAM($X) = FILENAME($F)
     C                   EVAL      $NDATA = $NDATA +1
     C                   EVAL      §DATA($NDATA) = §TSNAM($X) + §TIMEE($X)
     C                   ENDIF
     C                   EVAL      $X = $X+1
     C                   ENDDO
      *** Analizzo DS tempi
      * Ordino per nome test
     C*                   SORTA     §DATA_NM
     C                   FOR       $T = 1 TO %ELEM(SPLTESTNAME)
     C                   EVAL      $SPL = %TRIMR(SPLTESTNAME($T))
     C                                    +%TRIM(FILENAME($F))
      * inizializzo variabili
     C                   EVAL      $X = 1
     C                   EVAL      $NTIME = 0
     C                   EVAL      $SUMTIME = 0
      * Alternativa ricerca test name
     C                   DO        $NDATA
      *     
     C                   IF        §DATA_NM($X) = $SPL
     C                   EVAL      $NTIME = $NTIME +1
     C                   EVAL      $SUMTIME = $SUMTIME + %INT(§DATA_TM($X)) 
     C                   ENDIF
     C                   EVAL      $X = $X+1
      *
     C                   ENDDO                 
      * Calcolo tempo medio
     C                   IF        $NTIME = 0
     C                   EVAL      $AVERAGE = 0
     C                   ELSE
     C                   EVAL      $AVERAGE = $SUMTIME / $NTIME
     C                   ENDIF
      * Memorizzo in stringa §ST
     C                   EVAL      §ST = %TRIM(§ST) + %CHAR($AVERAGE) + '; '
      *
     C                   ENDFOR
      * Scrivo riga csv 
     C                   EVAL      $MSGOUT= §ST
     C     $MSGOUT       DSPLY     £PDSNU
      *
     C                   ENDFOR
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD*  Impostazioni iniziali
      *--------------------------------------------------------------*
     C     IMP0          BEGSR
      *
     C     *ENTRY        PLIST
     C                   PARM                    EXEC_DB
     C                   PARM                    EXEC_NM
      * Tipo di database
     C                   SELECT
     C                   WHEN      EXEC_DB='com.ibm.as400.access.AS400JDBCDriver'
     C                   EVAL      $$DATABASE='AS400 su SRVLAB'
     C                   EVAL      $$DB='01'
     C                   WHEN      EXEC_DB='org.mariadb.jdbc.Driver'
     C                   EVAL      $$DATABASE='MariaDB'
     C                   EVAL      $$DB='02'
     C                   ENDSL
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Routine impostazioni di avvio programma
      *--------------------------------------------------------------*
     C     £INIZI        BEGSR
      *
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD*  Chiusura e invio su coda del buffer residuo
      *--------------------------------------------------------------*
     C     FIN0          BEGSR
      *
      *
     C                   ENDSR
      *--------------------------------------------------------------*
    RD* Log Specifico applicazione
      *--------------------------------------------------------------*
     C     £JAX_LOG      BEGSR
     C                   ENDSR
**CTDATA MUTE
MUTE19_01
MUTE19_02
MUTE19_03
MUTE19_04
MUTE19_05
MUTE19_06
MUTE19_23
MUTE19_24
MUTE19_25
MUTE19_26
MUTE19_27
MUTE19_30
MUTE19_31
MUTE19_32
MUTE19_33
MUTE19_34
MUTE19_35
MUTE19_36
**CTDATA SPLTESTNAME
SetllAndReadE_SameKey_100_
SetllAndReadE_2SameKey_100_
SetllAndReadE_DiffKey_100_
SetllAndReadE_2SameKey_NoExist_
SetllAndRead_100_
SetllAndRead_2keys_100_
SetGTAndReadPE_SameKey_100_
SetGTAndReadPE_2SameKey_100_
SetGTAndReadPE_DiffKey_100_
SetGTAndReadP_100_
SetGTAndReadP_2keys_100_
CHAIN_5Keys1Time_
CHAIN_5Keys10Time_
CHAIN_NoFound_
WRITE_1Record_
UPDATE_1Record_
DELETE_1Record_
**CTDATA CSV_INT
Time,Mute name,Test name,Db name,File name,Type operation,Time elapsed (ms),Driver,
Version,Environment,Fail
I_D$TIPA;I_D$CODI;I_D$TROT;I_D$COD1;I_D$COD2;I_D$COD3;I_D$DTVA;I_D$SSIN;I_D$C001;I_D$C002;I_D$C003;
I_D$C004;I_D$C005;I_D$C006;I_D$C007;I_D$C008;I_D$C009;I_D$C010;I_D$C011;I_D$C012;I_D$C013;I_D$C014;
I_D$C015;I_D$C016;I_D$C017;
**CTDATA FILENAME
VERAPG0F
BRARTI0F