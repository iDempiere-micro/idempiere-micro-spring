package company.bigger.util

import org.idempiere.common.base.IServiceHolder
import org.idempiere.common.base.IServicesHolder

class DummyServiceHolder<T> : IServiceHolder<T> {
    override val service: T?
        get() = null
}

class DummyServicesHolder<T>() : IServicesHolder<T> {
    override val services: List<T>
        get() = listOf()
}