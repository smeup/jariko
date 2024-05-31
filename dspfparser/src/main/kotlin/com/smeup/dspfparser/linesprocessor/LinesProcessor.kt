package com.smeup.dspfparser.linesprocessor

import java.io.BufferedReader

private enum class ProcessorLineState {
    OPEN,
    CLOSE,
}

internal class LinesProcessor(private val buffer: BufferedReader) {
    private var state: ProcessorLineState = ProcessorLineState.CLOSE
    private var isLineDDS: Boolean = false
    private var isLineOpened: Boolean = false
    private val unterminatedLinesSubstrings: MutableList<DSPFLineSubstrings> = mutableListOf()
    val linesSubstrings: MutableList<DSPFLineSubstrings> = mutableListOf()
    val lines: MutableList<DSPFLine> = mutableListOf()

    private fun updateLinesSubstrings(lineSubstrings: DSPFLineSubstrings) {
        this.linesSubstrings.add(lineSubstrings)
    }

    private fun updateUnterminatedLinesSubstringsAndThenEmpty(linesSubstrings: DSPFLineSubstrings) {
        this.unterminatedLinesSubstrings.add(linesSubstrings)
        this.linesSubstrings.add(merge(this.unterminatedLinesSubstrings)!!)
        this.unterminatedLinesSubstrings.clear()
    }

    private fun updateUnterminatedLinesSubstrings(linesSubstrings: DSPFLineSubstrings) {
        this.unterminatedLinesSubstrings.add(linesSubstrings)
    }

    private fun handleComment(lineSubstrings: DSPFLineSubstrings) {
        if (!this.isLineDDS) {
            this.state = ProcessorLineState.CLOSE
            this.updateLinesSubstrings(lineSubstrings)
        }
    }

    private fun handleOpenLine(lineSubstrings: DSPFLineSubstrings) {
        if (this.isLineDDS && this.isLineOpened) {
            this.state = ProcessorLineState.OPEN
            this.updateUnterminatedLinesSubstrings(lineSubstrings)
        }
    }

    private fun handleClosedLineOnClosedState(lineSubstrings: DSPFLineSubstrings) {
        if (this.isLineDDS && !this.isLineOpened && this.state == ProcessorLineState.CLOSE) {
            this.updateLinesSubstrings(lineSubstrings)
        }
    }

    private fun handleClosedLineOnOpenState(lineSubstrings: DSPFLineSubstrings) {
        if (this.isLineDDS && !this.isLineOpened && this.state == ProcessorLineState.OPEN) {
            this.state = ProcessorLineState.CLOSE
            this.updateUnterminatedLinesSubstringsAndThenEmpty(lineSubstrings)
        }
    }

    private fun handle(lineSubstrings: DSPFLineSubstrings) {
        this.isLineDDS = !lineSubstrings.isComment()
        this.isLineOpened = lineSubstrings.doesContinue()

        // order is important, do not change it
        this.handleComment(lineSubstrings)
        this.handleOpenLine(lineSubstrings)
        this.handleClosedLineOnClosedState(lineSubstrings)
        this.handleClosedLineOnOpenState(lineSubstrings)
    }

    private fun forEachBufferLine(callback: (String, Int) -> Unit) {
        var count = 1
        this.buffer.forEachLine { callback(it, count++) }
    }

    private fun categorize() {
        this.state = ProcessorLineState.CLOSE
        this.forEachBufferLine { line, count -> this.handle(DSPFLineSubstrings.from(count, line)) }
    }

    fun createLinesSubstrings() {
        this.linesSubstrings.clear()
        this.categorize()
    }

    fun createLines() {
        this.lines.clear()
        this.createLinesSubstrings()
        this.linesSubstrings.forEach { if (!it.isComment()) this.lines.add(DSPFLine.from(it)) }
        this.linesSubstrings.clear()
    }
}
