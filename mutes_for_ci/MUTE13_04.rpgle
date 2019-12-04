   COP* *NOUI
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 04/10/19  001166  BERNI Creazione
     V* 16/10/19  001166  BERNI Ricompilato
     V* 16/10/19  V5R1    BMA   Check-out 001166 in SMEDEV
     V*=====================================================================
     V* OBIETTIVO
     V*  Programma finalizzato ai test sulla MOVEL
     V*
     V*=====================================================================
     D*----------------------------------------------------------------
     D AAA005          S              5
     D AAA006          S              6
     D AAA003          S              3
     D AAA010          S             10
     D DSP             S             50
      *
     D  N1             S              9  0
     D  N2             S             21  6
      *
     C                   EVAL      AAA003='AAA'
    MU* VAL1(AAA010) VAL2('AAA       ') COMP(EQ)
     C                   MOVEL     AAA003        AAA010
     C                   EVAL      DSP=AAA010
     C                   DSPLY                   DSP
     C                   EVAL      AAA006='  AAA'
    MU* VAL1(AAA010) VAL2('  AAA     ') COMP(EQ)
     C                   MOVEL     AAA006        AAA010
     C                   EVAL      DSP=AAA010
     C                   DSPLY                   DSP
     C                   MOVEL     'ABCDE'       AAA005
    MU* VAL1(AAA010) VAL2('ABCDE     ') COMP(EQ)
     C                   MOVEL     AAA005        AAA010
     C                   EVAL      DSP=AAA010
     C                   DSPLY                   DSP
     C                   CLEAR                   AAA010
     C                   EVAL      AAA010='RSTUVWXYZ '
    MU* VAL1(AAA005) VAL2('RSTUV') COMP(EQ)
     C                   MOVEL     AAA010        AAA005
     C                   EVAL      DSP=AAA005
     C                   DSPLY                   DSP
     C                   EVAL      AAA010='RSTUVWXYZ'
     C                   EVAL      AAA005='ABCDE'
    MU* VAL1(AAA010) VAL2('ABCDE     ') COMP(EQ)
     C                   MOVEL(P)  AAA005        AAA010
     C                   EVAL      DSP=AAA010
     C                   DSPLY                   DSP
     C                   EVAL      AAA010='RSTUVWXYZ'
     C                   EVAL      AAA005='ABCDE'
    MU* VAL1(AAA010) VAL2('ABCDEWXYZ ') COMP(EQ)
     C                   MOVEL     AAA005        AAA010
     C                   EVAL      DSP=AAA010
     C                   DSPLY                   DSP
      *    Facendo la movel in una stringa di un numero negativo l'ultima cifra diventa una lettera :
      *    -1      J                                                                          
      *    -2      K                                                                          
      *    -3      L                                                                          
      *    -4      M                                                                          
      *    -5      N                                                                          
      *    -6      O                                                                          
      *    -7      P                                                                          
      *    -8      Q                                                                          
      *    -9      R
    MU* VAL1(AAA006) VAL2('78425 ') COMP(EQ)                               
     C                   MOVEL     78,425        AAA006            6       
     C                   EVAL      DSP=AAA006                         
     C                   DSPLY                   DSP                       
    MU* VAL1(AAA005) VAL2('78425') COMP(EQ)                                
     C                   MOVEL     78,425        AAA005            5       
     C                   EVAL      DSP=AAA005                       
     C                   DSPLY                   DSP  
    MU* VAL1(AAA006) VAL2('78425 ') COMP(EQ)                               
     C                   MOVEL     78425         AAA006            6       
     C                   EVAL      DSP=AAA006                         
     C                   DSPLY                   DSP                       
    MU* VAL1(AAA005) VAL2('78425') COMP(EQ)                                
     C                   MOVEL     78425         AAA005            5       
     C                   EVAL      DSP=AAA005                       
     C                   DSPLY                   DSP  
      *
     C                   SETON                                        LR