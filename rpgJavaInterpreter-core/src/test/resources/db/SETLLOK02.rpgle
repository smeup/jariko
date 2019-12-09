      * Sample tables created with
      * CALL QSYS.CREATE_SQL_SAMPLE ('SAMPLE')
      * See:
      * https://www.ibm.com/support/knowledgecenter/en/ssw_ibm_i_74/sqlp/rbafysamptblx.htm
      * We use here the EMPLOYEE Table:
      * https://www.ibm.com/support/knowledgecenter/en/ssw_ibm_i_74/sqlp/rbafyemployee.htm
      * ------------------------------------------------------------------
     FXEMP2     if   e           k disk
     D toFind          S              3
     D msg             S             52    inz(*blanks)
      *
     C     *entry        plist
     C                   parm                    toFind
      *-------------------------------------------------------------------------
     C     'Searching'   dsply
     C     toFind        Setll     XEMP2
     c*                  if        %found
     C                   dow       not %eof
     C     toFind        reade     XEMP2
     C                   IF        not %eof
     C                   eval      msg = %trim(FIRSTNME) + ' ' + %trim(LASTNAME)
     C     msg           dsply
     C                   endif
     C                   enddo
     C*                  endif
      * Closing resources.
     C                   seton                                        lr
