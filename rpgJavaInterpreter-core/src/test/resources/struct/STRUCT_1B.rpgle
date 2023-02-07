      *---------------------------------------------------------------
      * Force OCCUR error caused by wrong occurrence position
      *---------------------------------------------------------------
     D DS1             DS                  OCCURS(10)
     D  FLDA                   1      5
     D  FLDB                   6     80

     D RES             S              1  0

      * Here I force an error because of an occurrence setting (11) greater
      * than OCCURS(10)
     C     11            OCCUR     DS1