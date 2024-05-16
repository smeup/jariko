      *====================================================================
      * smeup V6R1.021DV
      * Nome sorgente       : £J15P
      * Sorgente di origine : SMEDEV/QPROGEN(£J15P)
      * Esportato il        : 20240411 144724
      *====================================================================
      /IF NOT DEFINED(J15P_INCLUDED)
      /DEFINE J15P_INCLUDED
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* B£30101B  V4R1    CM Creata
     V* ==============================================================
      *---------------------------------------------------------------------
      * Procedura esecuzione £J15
      *---------------------------------------------------------------------
     P£J15P            B
     D£J15P            PI             7
     D $J15Fu                        10        CONST                            Funzione
     D $J15Me                        10        CONST                            Metodo
     D $J15TN                        15        VALUE                            Tipo Nodo
     D £J15IM                     30000    VARYING                              Immagine
     D $J15ND                       150    OPTIONS(*NOPASS) VALUE               Nome Nodo
     D $J15LP                         1    OPTIONS(*NOPASS) VALUE               Non Analiz.Liv.Prec.
     D $J15ST                       150    OPTIONS(*NOPASS) VALUE               Struttura
     D $J15CO                       150    OPTIONS(*NOPASS) VALUE               Contesto
     D $J15SA                         1    OPTIONS(*NOPASS) VALUE               Senza attributi
     C                   CLEAR                   £J15DS
     C                   EVAL      £J15TN=$J15TN
     C                   IF        %PARMS>= 5
     C                   EVAL      £J15ND=$J15ND
     C                   ENDIF
     C                   IF        %PARMS>= 6
     C                   EVAL      £J15AP=$J15LP
     C                   ENDIF
     C                   IF        %PARMS>= 7
     C                   EVAL      £J15ST=$J15ST
     C                   ENDIF
     C                   IF        %PARMS>= 8
     C                   EVAL      £J15CO=$J15CO
     C                   ENDIF
     C                   IF        %PARMS>= 9
     C                   EVAL      £J15SA=$J15SA
     C                   ENDIF
     C                   CALL      'JAJ150'
     C                   PARM      $J15FU        £J15FU
     C                   PARM      $J15ME        £J15ME
     C                   PARM                    £J15DS
     C                   PARM                    £J15IM
     C                   PARM                    £J15MS
     C                   PARM      *BLANKS       £J15FI
     C     *IN35         PARM      *OFF          £J1535
      * Restituisce VALORE
     C                   RETURN    £J15MS
     P                 E
      /ENDIF
