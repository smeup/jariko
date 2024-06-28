package com.smeup.rpgparser.interpreter

interface RuntimeInterpreterSnapshotManager {
    fun take(): RuntimeInterpreterSnapshot
    fun store()
    fun load(snapshot: RuntimeInterpreterSnapshot)
}