package com.strumenta.kolasu.model

fun <T : Node> Node.ancestor(klass: Class<T>): T? {
    this.parent ?: return null
    return when {
        klass.isInstance(this.parent) -> this.parent as T
        else -> this.parent!!.ancestor(klass)
    }
}
