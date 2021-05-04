      *-------------------------------------------------------------------
      * Simple caller for JAJAX1 rpgle program to simulate call by
      * procedure P_RXATT used by MUTE15_07.
      * This test is useful to check a 'more than one call' out of
      * AS400 environment, where works fine.
      * Into Jariko, the second call works abnormal, so no result is
      * obtained.
      *-------------------------------------------------------------------
     D TXT             S            100    DIM(15) CTDATA PERRCD(1)             _NOTXT
      *-------------------------------------------------------------------
     D   XmlTAG        S          30000    VARYING
     D   XmlATT        S             64    VARYING
     D   XmlASS        S             15
     D   XmlFND        S              1N
     D   XmlLIV        S              1
     D   XmlVAL        S          30000    VARYING
     D   XMLPR         S              5I 0
      *-------------------------------------------------------------------
      * First call.
     C                   EVAL      XmlTAG = TXT(01)
     C                   EVAL      XmlATT = 'PR1('
     C                   EVAL      XmlASS = ''
     C                   EVAL      XmlFND = *OFF
     C                   EVAL      XmlLIV = ''
     C                   EVAL      XmlVAL = ''
     C                   EVAL      XMLPR  = 3
     C                   CALL      'JAJAX1'
     C                   PARM                    XmlTAG
     C                   PARM                    XmlATT
     C                   PARM                    XmlASS
     C                   PARM                    XmlFND
     C                   PARM                    XmlLIV
     C                   PARM                    XmlVAL
     C                   PARM                    XMLPR
     C     XmlVAL        DSPLY
      * Second call.
     C                   EVAL      XmlTAG = TXT(02)
     C                   EVAL      XmlATT = 'APR1('
     C                   EVAL      XmlASS = ''
     C                   EVAL      XmlFND = *OFF
     C                   EVAL      XmlLIV = ''
     C                   EVAL      XmlVAL = ''
     C                   EVAL      XMLPR  = 3
     C                   CALL      'JAJAX1'
     C                   PARM                    XmlTAG
     C                   PARM                    XmlATT
     C                   PARM                    XmlASS
     C                   PARM                    XmlFND
     C                   PARM                    XmlLIV
     C                   PARM                    XmlVAL
     C                   PARM                    XMLPR
     C     XmlVAL        DSPLY
      *
     C                   SETON                                        LR
** CTDATA TXT
PR1(Ahi quanto a dir qual era è cosa dura,esta selva selvaggia) APR1(Lupa)
Ahi quanto a dir qual era è cosa dura,esta selva selvaggia) APR1(Lupa)