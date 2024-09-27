     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 27/09/24  V6R1   BMA Creato
     V*=====================================================================
      *
     D DS001           DS
     D  STR001                       15A
     D  NUM001                        5S 2
     D  NUM002                        5P 2
      *
     D  DS002          DS                  LIKEDS(DS001)
      *
     D $STR            S                   LIKE(DS001)
     D MSG             S             52
      *
      /COPY QILEGEN,£TABB£1DS
      /COPY QILEGEN,£PDS
      *---------------------------------------------------------------------
    RD* MAIN
      *---------------------------------------------------------------------
      *
     C                   EVAL      STR001='HELLO'
     C                   EVAL      NUM001=123.56
     C                   EVAL      NUM002=476.98
      *
     C                   EVAL      $STR=DS001
      *
     C                   EVAL      DS002=$STR
      *
    MU* VAL1(MSG) VAL2('HELLO') COMP(EQ)
     C                   EVAL      MSG=STR001
    MU* VAL1(MSG) VAL2('HELLO') COMP(EQ)
     C                   EVAL      MSG=DS002.STR001
    MU* VAL1(MSG) VAL2('123.56') COMP(EQ)
     C                   EVAL      MSG=%CHAR(NUM001)
    MU* VAL1(MSG) VAL2('123.56') COMP(EQ)
     C                   EVAL      MSG=%CHAR(DS002.NUM001)
    MU* VAL1(MSG) VAL2('476.98') COMP(EQ)
     C                   EVAL      MSG=%CHAR(NUM002)
    MU* VAL1(MSG) VAL2('476.98') COMP(EQ)
     C                   EVAL      MSG=%CHAR(DS002.NUM002)
     C                   SETON                                        LR
      *---------------------------------------------------------------------