      *-------------------------------------------------------------------
      * Simple caller for JAJAX1 rpgle program to simulate call by
      * procedure P_RXATT used by MUTE15_08.
      * This test is useful to check a 'more than one call' out of
      * AS400 environment, where works fine.
      * Into Jariko, if a variable name ends with same of another one
      * the variable is not retrieved, for example 'CODSEDE' and 'SEDE'
      *-------------------------------------------------------------------
     D TAG             S            100    DIM(1) CTDATA PERRCD(1)              _NOTXT
     D ATT             S            100    DIM(2) CTDATA PERRCD(1)              _NOTXT
      *-------------------------------------------------------------------
     D   XmlTAG        S          30000    VARYING
     D   XmlATT        S             64    VARYING
     D   XmlASS        S             15
     D   XmlFND        S              1N
     D   XmlLIV        S              1
     D   XmlVAL        S          30000    VARYING
     D   XMLPR         S              5I 0
      *-------------------------------------------------------------------
     D   $I            S              2  0
      *-------------------------------------------------------------------
      * Loop over ATT array and execute call to extract related variable value
      * from TAG(01) content
     C     1             DO        2             $I
      *
     C                   EVAL      XmlTAG = TAG(01)
     C                   EVAL      XmlATT = %TRIM(ATT($I))
     C                   EVAL      XmlASS = ''
     C                   EVAL      XmlFND = *OFF
     C                   EVAL      XmlLIV = ''
     C                   EVAL      XmlVAL = ''
     C                   EVAL      XMLPR  = 3
      *
     C                   CALL      'JAJAX1'
     C                   PARM                    XmlTAG
     C                   PARM                    XmlATT
     C                   PARM                    XmlASS
     C                   PARM                    XmlFND
     C                   PARM                    XmlLIV
     C                   PARM                    XmlVAL
     C                   PARM                    XMLPR
      *
     C     XmlVAL        DSPLY
      *
     C                   ENDDO
      *
     C                   SETON                                        LR
** CTDATA TAG
DATA(20210419) MSG(Lorem) COD(ROSMAR) NOME(Rossi Mario) CODSEDE(ERB) SEDE(Erbusco)
** CTDATA ATT
CODSEDE(
SEDE(