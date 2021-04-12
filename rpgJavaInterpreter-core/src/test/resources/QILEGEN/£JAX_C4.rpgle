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
