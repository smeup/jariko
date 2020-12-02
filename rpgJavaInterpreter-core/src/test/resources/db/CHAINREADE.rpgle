      * Sample tables created with
      * CALL QSYS.CREATE_SQL_SAMPLE ('SAMPLE')
      * See:
      * https://www.ibm.com/support/knowledgecenter/en/ssw_ibm_i_74/sqlp/rbafysamptblx.htm
      * We use here the EMPLOYEE Table:
      * https://www.ibm.com/support/knowledgecenter/en/ssw_ibm_i_74/sqlp/rbafyemployee.htm
      * ------------------------------------------------------------------
     FEMPLOYEE  if   e           k disk
     D toFind          S              3
     D msg             S             52    inz(*blanks)
      *
     C     *entry        plist
     C                   parm                    toFind
      * Reads records from file
     C     toFind        chain     EMPLOYEE
     C                   if        %found
     C                   exsr      showData
     c                   else
     C     'Not found'   dsply
     C                   endif
      * Closing resources.
     C                   seton                                        lr
      *-------------------------------------------------------------------------
     C     showData      begsr
     C                   dou       %eof
     C                   eval      msg = %trim(FIRSTNME) + ' ' + %trim(LASTNAME)
     C     msg           dsply
     C                   ReadE     EMPLOYEE
     C                   enddo
     C                   endsr
