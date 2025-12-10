package io.github.wukeji.base.definitions


typealias VoidAction = () -> Unit
typealias TypedAction <T> = T.() -> Unit
typealias ParamedAction <T> = (T) -> Unit
typealias ReturnedAction <T> = () -> Unit

