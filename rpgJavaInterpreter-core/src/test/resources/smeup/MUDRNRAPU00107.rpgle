     V* ==============================================================
     V* 08/08/2024 APU001 Creation
     V* 20/08/2024 APU001 Simplification by removing several
     V*                   fields of DS.
     V* ==============================================================
    O * PROGRAM GOAL
    O * Assignment the content of DS to an element of array that is
    O *  String type (varying) with size smaller than DS.
    O * After declaration of variable there is the assignment of
    O *  specific substring of "Lorem ipsum" to every field of DS.
    O * Then, there is the assignment of DS content to an element
    O *  of array. In AS400 there is a truncate of content for adapting
    O *  to destination, smaller than DS.
    O * The implementation is like the original source, by changing
    O *  only the name of variables and by adding the assignment
    O *  of substring to undestand about the behavior.
     V* ==============================================================
    O * JARIKO ANOMALY
    O * Before the fix, the error occurred was
    O *  `Cannot assign DataStructValue to StringType`.
     V* ==============================================================
     D A40_A50         S             50    DIM(300) VARYING
     D A40_DS          DS
     D  A40_DS_F1                    20
     D  A40_DS_F2                    20
     D  A40_DS_F3                    35
     D A40_A5          S              5  0

     D OUTPUT          S             50

     C                   EVAL      A40_DS_F1='Lorem ipsum dolor si'
     C                   EVAL      A40_DS_F2='t amet, consectetuer'
     C                   EVAL      A40_DS_F3=' adipiscing elit. Aeean'
     C                                       + ' commodo lig'

     C                   EVAL      A40_A5=1
     C                   EVAL      A40_A50(A40_A5)=A40_DS                            Jariko Runtime Error: `Cannot assign DataStructValue to StringType`
     C                   EVAL      OUTPUT=A40_A50(A40_A5)
     C     OUTPUT        DSPLY
