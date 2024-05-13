     V* ==============================================================
     D* 02/05/24
     D* Purpose: Test the right behavior in terms of error position
     D* when an error occurs after a not included copy because included earlier
     V* ==============================================================

     D* In QILEGEN,£PDS we have "IF NOT DEFINE" directive that prevents the inclusion of the same copy
       /COPY QILEGEN,£PDS
       /COPY QILEGEN,£PDS

      * Display MSG that not exists
     C     MSG           DSPLY

     