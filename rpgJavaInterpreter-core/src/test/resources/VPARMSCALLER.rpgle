     D £DBG_Str        S             2

     D £40A            S             15    DIM(300)
     D £40B            S             12    DIM(300)

     D £G40DS          DS           500
     D  £G40PZ                        1                                         O Lista Dinamica
     D  £G40EX                        1                                         O Lista Estesa
     D  £G40FM                        1                                         O Formato Ext
     D  £G40AE                        1                                         I Utilizzo £40AE
     D  £G40FL                        1                                         I Applica Filtro JO
     D  £G40NS                        1                                         I Disabilita SQL
     D  £G40NE                        9S 0                                      O N° Elementi
     D  £G40SC                       15                                         O Schema
     D  £G40FO                       15                                         O Fonte
     D  £G40T2                        2                                         I Tipo Oggetto Sec.
     D  £G40P2                       10                                         I Parametro Ogg.Sec.
     D  £G40C2                       15                                         I Cod.Ogg.Sec.
     D  £G40UT                       10                                         O Utente
     D  £G40AN                        1                                         O Annullata

     C                   CALL      'VPARMSCALLEE'
     C                   PARM                    £G40FU           10            -->
     C                   PARM                    £G40ME           10            -->
     C                   PARM                    £G40MS            7            <--
     C                   PARM                    £G40FI           10            <--
     C                   PARM                    £G40CM            2            <--
     C                   PARM                    £G40TP            2            -->  Tipo
     C                   PARM                    £G40PA           10            -->  Param.
     C                   PARM                    £G40CD           15            -->  Codice
     C                   PARM                    £40A                           <--> Sch.Oggetti
     C                   PARM                    £40B                           <--> Sch.TipPar.
     C                   PARM                    £G40MV           15            <--> Cod.MDV
     C                   PARM                    £G40DM           35            <--- Des.MDV
     C                   PARM                    £G4035            1            <---
     C                   PARM                    £G4036            1            <---
     C                   PARM                    £G40DS                         <--> Estensione

     C                   EVAL      £DBG_Str='ok'
     C     £DBG_Str      DSPLY