      /IF NOT DEFINED(UICDS_INCLUDED)
      /DEFINE UICDS_INCLUDED
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
      /ENDIF
