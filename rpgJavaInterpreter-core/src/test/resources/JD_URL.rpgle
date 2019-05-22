     V*=====================================================================
     V* Date      Release Au Description
     V* dd/mm/yy  nn.mm   xx Brief description
     V*=====================================================================
     V* 22/05/19  V5R1    LOMFRN Created
     V*=====================================================================
     D                 DS
     D $$SVAR                      1050    DIM(200)
     D  $$SVARCD                     50    OVERLAY($$SVAR:1)                    Name
     D  $$SVARVA                   1000    OVERLAY($$SVAR:*NEXT)                Value
      *---------------------------------------------------------------
      * ENTRY
      * . Function
     D U$FUNZ          S             10
      * . Method
     D U$METO          S             10
      * . Array of Variables
     D U$SVARSK        S                   LIKE($$SVAR) DIM(%ELEM($$SVAR))
     D*---------------------------------------------------------
     D COUNT           S              3  0
     D DATA            S             50
     C*---------------------------------------------------------
     C*
     C*---------------------------------------------------------
     C     *ENTRY        PLIST
     C                   PARM                    U$FUNZ
     C                   PARM                    U$METO
     C                   PARM                    U$SVARSK
     C*----------------------------------------------------------------------
     C                   EVAL      DATA=U$FUNZ
     C                   DSPLY                   DATA
     C                   EVAL      DATA=U$METO
     C                   DSPLY                   DATA
     C                   EVAL      $$SVAR=U$SVARSK
     C                   FOR       COUNT = 1 to 200
     C                   EVAL      DATA = $$SVARCD(COUNT)
     C                   IF        DATA <> *BLANKS
     C                   DSPLY                   DATA
     C                   EVAL      DATA = $$SVARVA(COUNT)
     C                   DSPLY                   DATA
     C                   ENDIF
     C                   ENDFOR
     C*----------------------------------------------------------------------
     C                   SETON                                        LR
     C*----------------------------------------------------------------------
