     V* ==============================================================
     V* 27/11/2024 APU001 Creation
     V* ==============================================================
    O * PROGRAM GOAL
    O * This test measures the execution time required to perform
    O *  repeated lookups of standalone fields and data structure
    O *  fields in a symbol table created from an Abstract
    O *  Syntax Tree (AST).
    O * It verifies that the AST can be produced successfully and
    O *  evaluates the performance of symbol table lookups for
    O *  specific fields.
    O * The goal is to evaluate the efficiency of symbol table lookups
    O *  and ensure performance is within an acceptable range.
     V* ==============================================================
     D VAR1            S             10
     D DS1             DS
     D  DS1_FLD1                     50
     D  DS1_FLD2                     50
     D  DS1_FLD3                     50
     D  DS1_FLD4                     50
     D  DS1_FLD5                     50
     D  DS1_FLD6                     50
     D  DS1_FLD7                     50
     D  DS1_FLD8                     50
     D  DS1_FLD9                     50
     D  DS1_FLD10                    50
     D  DS1_FLD11                    50
     D  DS1_FLD12                    50
     D  DS1_FLD13                    50
     D  DS1_FLD14                    50
     D  DS1_FLD15                    50
     D  DS1_FLD16                    50
     D  DS1_FLD17                    50
     D  DS1_FLD18                    50
     D  DS1_FLD19                    50
     D  DS1_FLD20                    50
     D  DS1_FLD21                    50
     D  DS1_FLD22                    50
     D  DS1_FLD23                    50
     D  DS1_FLD24                    50
     D  DS1_FLD25                    50

     C                   SETON                                          LR