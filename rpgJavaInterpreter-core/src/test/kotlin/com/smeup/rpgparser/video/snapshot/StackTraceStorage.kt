package com.smeup.rpgparser.video.snapshot

interface StackTraceStorage : AutoCloseable {
    fun store()
    fun load()
}