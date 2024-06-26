package com.smeup.rpgparser.interpreter

interface RuntimeInterpreterSnapshotManager {
    fun take(): RuntimeInterpreterSnapshot
    fun store()
    fun load(snapshot: RuntimeInterpreterSnapshot)
}
//abstract class RuntimeInterpreterSnapshotManager(
//    private val memorySliceStorage: IMemorySliceStorage
//) {
//    abstract fun take(): RuntimeInterpreterSnapshot
//    abstract fun store(): RuntimeInterpreterSnapshot
//    abstract fun load(snapshot: RuntimeInterpreterSnapshot)
//}

//
//class RISM {
//    private val memorySliceStorage: IMemorySliceStorage = TODO()
//}