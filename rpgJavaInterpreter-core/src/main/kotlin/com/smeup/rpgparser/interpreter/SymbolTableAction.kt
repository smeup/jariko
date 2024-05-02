package com.smeup.rpgparser.interpreter

/**
 * Represent a type of interaction with the symbol table
 * @see ISymbolTable
 */
enum class SymbolTableAction {
    INIT,
    LOAD,
    STORE,
    GET,
    SET
}