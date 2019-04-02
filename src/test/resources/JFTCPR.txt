      * Example adapted from www.neroni.it
      * Program that reads records from jffrom and writes them to jfto
      * It cleans destination file before starting
      * The record format of the 2 files is SOMERF

      * File to write to.
     Fjfto      uf a e             disk
      * File to copy from.
     Fjffrom    if   e             disk    rename(somerf:w)
      * Cleaning of destination file.
     C                   do        *hival
     C                   read      somerf
     C                   if        %eof(jfto)
     C                   leave
     C                   endif
     C                   delete    somerf
     C                   enddo
      * Copies records from source to destination file.
     C                   do        *hival
     C                   read      w
     C                   if        %eof(jffrom)
     C                   leave
     C                   endif
     C                   write     somerf
     C                   enddo
      * Closing resources.
     C                   seton                                        lr