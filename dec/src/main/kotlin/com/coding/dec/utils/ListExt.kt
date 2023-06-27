package com.coding.dec.utils

fun <T> MutableList<T>.put(param: T): MutableList<T> {
    this.add(param)
    return this
}

fun <T> MutableList<T>.put(vararg params: T): MutableList<T> {
    for (item in params) {
        this.add(item)
    }
    return this
}

fun <T> MutableList<T>.put(boolean: Boolean, vararg params: T): MutableList<T> {
    if (boolean) {
        this.put(*params)
    }
    return this
}