     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* 13/05/05  V2R1    CM Raggruppamento /COPY di comunicazione
     V* 18/12/06  V2R2    AS Aggiunte £G61, £WAIT e £JAX_C3
     V* 14/06/07  V2R2    CM Aggiunte £G96
     V* B£30901B  V4R1    CM Aggiunta £JAX_C4 per gestione Setup di Default
     V* 24/06/13  V3R2    AS £JAX_C1 è diventata nidificata, messo /INCLUDE
     V* 01/03/17         BMA Aggiunte £UIB e £UIC
     V* B£70524A  V5R1   BMA Rilascio modifiche precedenti
     V* ==============================================================
      /COPY QILEGEN,£UIB
      /COPY QILEGEN,£UIC
      /COPY QILEGEN,£JAX_C0
      /INCLUDE QILEGEN,£JAX_C1
      /COPY QILEGEN,£JAX_C3
      /COPY QILEGEN,£JAX_C4
      /COPY QILEGEN,£JAX_C9
