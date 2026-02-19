package org.stkachenko.propertymanagement.core.common.network

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val pmDispatcher: PmDispatchers)

enum class PmDispatchers {
    Default,
    IO,
}