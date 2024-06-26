     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 15/03/21  V6R1    BONMAI Creazione
     V* ==============================================================
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
      /COPY QILEGEN,£JAX_C9