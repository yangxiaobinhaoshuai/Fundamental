package io.github.wukeji.base.definitions


val Any?.neatName: String get() = this?.javaClass?.simpleName ?: "NULL"