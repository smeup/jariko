      *====================================================================
      * smeup V6R1.020DV
      * Nome sorgente       : £G96DS
      * Sorgente di origine : QTEMP/QILEGEN(£G96DS)
      * Esportato il        : 20240320 155127
      *====================================================================
     V*================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 14/06/07  V2R2    CM Creato                 T
     D*----------------------------------------------------------------
     D* OBIETTIVO
     D* Momenti di un Servizio
     D*----------------------------------------------------------------
     D £G96DS          DS           512    INZ
     D  £G96TP                       10
     D  £G96IS                       10
     D  £G96MO                       30
     D  £G96DI                        8  0
     D  £G96HI                        9  0
     D  £G96DF                        8  0
     D  £G96HF                        9  0
     D  £G96MM                       10  0
     D  £G96NS                       10  0
     D  £G96ST                       30
     D  £G96TT                        1
     D  £G96IN                        1
     D £G96SI          S           1000    VARYING
      *
     D £G96WK          S            512
      *
     D £G96ISER        DS
     D  £G96ICOMP                    10
     D  £G96ISERV                    10
     D  £G96IFUNZ                    15
     D  £G96IOGG1                    27
     D  £G96IOGG2                    27
     D  £G96IOGG3                    27
     D  £G96IOGG4                    27
     D  £G96IOGG5                    27
     D  £G96IOGG6                    27
     D  £G96IPARA                   256
