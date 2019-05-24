     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 23/05/19  V5R1    BS Creazione
     V*=====================================================================
     H/COPY QILEGEN,£INIZH
      * File
     FV5TDOCDL  IF   E           K DISK
     F*                                    PREFIX(codiceprefisso{:numerocaratteridasostituire})
      *                                       => permette di apporre un prefisso ai campi del file
     F*                                    EXTFILE(filename)
      *                                       => permette di indirizzare il file su un file esterno
      *                                          è da considera che filename pò essere un variabile
      *                                          e che filename può indicare solo il nome di un file
      *                                          ma anche nomelibreria/nomefile
     FBRENTI0L  IF   E           K DISK
      *---------------------------------------------------------------
      * DS
     DV5TDOC         E DS                  EXTNAME(V5TDOC0F) INZ
     DBRENTI         E DS                  EXTNAME(BRENTI0F) INZ
      *                                      => la DS viene automaticamente valorizzata con i campi
      *                                         del file indicato nella parola chiave
     D*                                    PREFIX(codiceprefisso{:numerocaratteridasostituire})
      *                                      => permette di apporre un prefisso ai campi della ds
      *---------------------------------------------------------------
     D$MSG             S             52
      *---------------------------------------------------------------
     I/COPY QILEGEN,£TABB£1DS
     I/COPY QILEGEN,£PDS
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      * Operazioni
      *
     C                   CLEAR                   $X                3 0
     C                   EVAL      T§TDOC='DA'
     C                   EVAL      T§DTDO=*HIVAL
     C     KV5T2         SETGT     V5TDOCDL
     C                   DO        *HIVAL
     C     KV5T1         READPE    V5TDOCDL
     C                   IF        %EOF
     C                   LEAVE
     C                   ENDIF
     C                   IF        $X=20
     C                   LEAVE
     C                   ENDIF
     C                   EVAL      E§TRAG=T§TCCL
     C                   EVAL      E§AZIE=££B£2A
     C                   EVAL      E§CRAG=T§CDCL
     C     KBRE3         CHAIN     BRENTI0L
     C                   IF        %FOUND
     C                   IF        E§PROV='BS' OR E§PROV='MI'
     C                   EVAL      $X=$X+1
     C                   EVAL      $MSG=%EDITC($X:'X')+' '+T§NDOC+' '
     C                             +%TRIM(E§PROV) +' '+E§RAGS
     C     $MSG          DSPLY     £PDSSU
     C                   ENDIF
     C                   ENDIF
     C                   ENDDO
      *
     C                   SETON                                        RT
      *---------------------------------------------------------------
     C/COPY QILEGEN,£INZSR
      *---------------------------------------------------------------
    RD* ROUTINE INIZIALE
      *--------------------------------------------------------------*
     C     £INIZI        BEGSR
      *
     C     KV5T2         KLIST
     C                   KFLD                    T§TDOC
     C                   KFLD                    T§DTDO
      *
     C     KV5T1         KLIST
     C                   KFLD                    T§TDOC
      *
     C     KBRE3         KLIST
     C                   KFLD                    E§TRAG
     C                   KFLD                    E§AZIE
     C                   KFLD                    E§CRAG
      *
     C                   ENDSR
