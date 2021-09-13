      ***********************************************************
      * Test to check the '/COPY' directive is properly ignored
      * due to presence of char '*' in 7th position that means:
      * "this line is a comment."
      ***********************************************************
      *    /COPY £XAIDOJ
      *   /COPY £XAIDOJ
      *  /COPY £XAIDOJ
      * /COPY £XAIDOJ
      */COPY £XAIDOJ
      *               /COPY £XAIDOJ
      *               //COPY £XAIDOJ
      **/COPY £XAIDOJ
      *********               /COPY £XAIDOJ
***************               /COPY £XAIDOJ
*******               /COPY £XAIDOJ
     C     'Success!'    DSPLY
     C                   SETON                                        LR
