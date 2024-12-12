     D NUMAZI          C                   58
     D £JAXSWK         S            100    DIM(300)

     D                 DS
      * Error:
      * java.lang.NullPointerException: Cannot invoke "com.smeup.rpgparser.RpgParser$Ds_nameContext.getText()"
      * because the return value of "com.smeup.rpgparser.RpgParser$DspecContext.ds_name()" is null
     D £JAXSW2                      100    DIM(%elem(£JAXSWK))
     D  £JAXSW2Key                   10    OVERLAY(£JAXSW2:01)