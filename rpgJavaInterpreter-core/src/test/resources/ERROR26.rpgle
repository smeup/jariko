     V* ==============================================================
     D* 06/06/24
     D* Purpose: Must fire the following errors
     D* line 8 - Incorrect for left of range
     D* line 9 - Incorrect by wrong format date
     V* ==============================================================

     D DATE1           S               D   DATFMT(*ISO) INZ(D'0000-12-31')
     D DATE2           S               D   DATFMT(*ISO) INZ(D'10000-01-01')