     C                   seton                                        lr
      * Example program that calls the italian fiscal code check program
      * Gets the fiscal code to check in JCFCOD.
      * Returns 1 in JCFFIS if it finds it's a code relate to a person
      * Returns 1 in JCFOMO if, when checking a person's code, 
      *    the syntax tells that it's a homonymy case.
      *    (The flag is not set in case of errors)
      * Returns 1 in JCFSIN if there are syntax errors in the code.
      * Returns 1 in JCFCHK if the check code is wrong.
      *    This flag is always on if there are sysntax errors in the code.
	  *
      * Program by Claudi Neroni: https://www.neroni.it/
	  *
     C                   CALL      'JCODFISD'
     C                   PARM                    JCFCOD           16            I Fiscal code   
     C                   PARM                    JCFFIS            1            O Person
     C                   PARM                    JCFOMO            1            O Homonimy
     C                   PARM                    JCFSIN            1            O Syntax error
     C                   PARM                    JCFCHK            1            O Wrong check dig
