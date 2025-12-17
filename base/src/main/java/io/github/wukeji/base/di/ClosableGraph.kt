package io.github.wukeji.base.di

interface GraphProvider<P, G : ClosableGraph> {
    fun createRootGraph(): G
}

interface ChildGraphProvider<P, G : ClosableGraph> {
    fun createChildGraph(parent: P): G
}

interface ClosableGraph : AutoCloseable