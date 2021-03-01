     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 23/02/21  002635 BMA Creazione
     V* ==============================================================
     V* NB: tutte le /COPY incluse (DEC, RITES, £TABJATDS...) servono
     V* ==============================================================
     H/COPY QILEGEN,£INIZH
      *--------------------------------------------------------------------------------------------*
      /COPY QILEGEN,£JAX_D
      /COPY QILEGEN,£TABJATDS
      /COPY QILEGEN,£TABB£1DS
      /COPY QILEGEN,£DECDS
      /COPY QILEGEN,£PDS
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
      /COPY QILEGEN,£RITES
      /COPY QILEGEN,£DEC
      *
      /COPY QILEGEN,£JAX_C
** SWKESE
XXESE1    Colonna 1                    AR                    15      ASSE
XXESE2    Colonna 2                    NR                    12      SERIE
XXESE3    Colonna 3                    TABRE                H05
XXESE4    Colonna 4                    [XXESE3]              999     SERIE
XXESE5    Colonna 5                    [XXESE3]|[XXESE4]     15
