package com.alexey.minay.lib

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

abstract class Actor<Action : Any, Effect : Any, State : Any, Result : Any> {

    protected val coroutineScope get() = mCoroutineScope

    private lateinit var mEffect: (Effect) -> Unit
    private lateinit var mReduce: (Result) -> Unit
    private var mCoroutineScope: CoroutineScope? = null

    fun init(
        event: (Effect) -> Unit,
        reduce: (Result) -> Unit,
        coroutineScope: CoroutineScope
    ) {
        this.mEffect = event
        this.mReduce = reduce
        this.mCoroutineScope = coroutineScope
    }

    fun dispose() {
        mEffect = {}
        mReduce = {}
        mCoroutineScope?.cancel()
        mCoroutineScope = null
    }

    protected fun emit(effectProvider: () -> Effect) {
        mEffect(effectProvider())
    }

    protected fun reduce(resultProvider: () -> Result) {
        mReduce(resultProvider())
    }

    abstract suspend fun execute(action: Action, getState: () -> State)

}