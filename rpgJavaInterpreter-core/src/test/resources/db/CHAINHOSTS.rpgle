      * Searches a record in AS400's hosts file
      *
      * Hosts file (more or less)
     FQATOCHOST if   e           k disk
      * Record format QHOSTS
      *    A    1   15  INTERNET         IP address
      *    A   16  270  HOSTNME1         Host name 1
      *    A  271  525  HOSTNME2         Host name 2
      *    A  526  780  HOSTNME3         Host name 4
      *    A  781 1035  HOSTNME4         Host name 5
      *    B 1036 1039 0IPINTGER         IP addres: numeric form
      *    A 1040 1103  TXTDESC          Description
      *    A 1104 1152  RESERVED         Reserved
     D ipToFind        S             15
     D result          S             52    inz(*blanks)
      *
     C     *entry        plist
     C                   parm                    ipToFind
      * Reads records from file
     C     ipToFind      chain     QATOCHOST
     C                   if        %found
     c                   eval      result = hostnme1
     c                   else
     C                   eval      result = 'Not found'
     C                   endif
     C                   dsply                   result
      * Closing resources.
     C                   seton                                        lr
