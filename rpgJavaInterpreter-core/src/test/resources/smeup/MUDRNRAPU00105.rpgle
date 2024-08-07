     D £40A            S             15    DIM(300)
     D £40B            S             12    DIM(300)

     D £40ANM          S              5S 0 INZ(%ELEM(£40A))

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

     C     'OK'          DSPLY

     C     *ENTRY        PLIST
     C                   PARM                    £40A                           <--> Schiera
     C                   PARM                    £G40DS
