     V* ==============================================================
     D* 06/06/24
     D* Purpose: Must fire the following errors
     D* line 8 - Incorrect by empty string
     D* line 9 - Incorrect by wrong format date
     V* ==============================================================

     D DATE1           S               D   DATFMT(*JUL) INZ(D'')
     D DATE2           S               D   DATFMT(*JUL) INZ(D'30-05-2024')