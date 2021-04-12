     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 13/05/05  V2R1    CM Raggruppamento /COPY di comunicazione
     V* 14/12/06  V2R2    AS Aggiunta £G61DS
     V* 16/04/07  V2R2    AS Aggiunta £TABUI1DS
     V* 14/06/07  V2R2    AS Aggiunta £G96DS
     V* B£30901B  V4R1    CM Aggiunta £JAX_D4 e £TABB£5DS per gestione Setup di Default
     V* 24/06/13  V3R2    BS Forzata inclusione come include
     V* 06/10/16  V5R1    PEDSTE Eliminata /COPY £JAX_F0 per nuova gestione prtf in JAJAX3
     V* 02/02/17         BMA Aggiunta £G64DS
     V* B£70524A  V5R1   BMA Rilascio modifiche precedenti
     V* 25/09/18  V5R1    ZS Aggiunta £B£UT67DS
     V* ==============================================================
      */COPY QILEGEN,£JAX_F0
      /COPY QILEGEN,£JAX_D0
      /INCLUDE QILEGEN,£JAX_D1
      /COPY QILEGEN,£JAX_D4
      /COPY QILEGEN,£TABPGMDS
      /COPY QILEGEN,£TABB£5DS
      /COPY QILEGEN,£TABUI1DS
      /COPY QILEGEN,£UIBDS
      /COPY QILEGEN,£UICDS
      /COPY QILEGEN,£G64DS
      /COPY QILEGEN,£B£UT67DS
