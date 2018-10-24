package company.bigger.util

import org.idempiere.common.base.IServiceHolder
import org.idempiere.common.base.IServiceLocator
import org.idempiere.common.base.IServicesHolder
import org.idempiere.common.base.ServiceQuery

class DummyService : org.idempiere.common.base.Service() {
    override val locator: IServiceLocator
        get() = DummyServiceLocator()

    class DummyServiceLocator : IServiceLocator {
        override fun <T> locate(type: Class<T>): IServiceHolder<T> {
            return DummyServiceHolder<T>()
        }

        override fun <T> locate(type: Class<T>, query: ServiceQuery): IServiceHolder<T> {
            return DummyServiceHolder<T>()
        }

        override fun <T> locate(type: Class<T>, componentName: String, query: ServiceQuery): IServiceHolder<T> {
            return DummyServiceHolder<T>()
        }

        override fun <T> list(type: Class<T>): IServicesHolder<T> {
            return DummyServicesHolder<T>()
        }

        override fun <T> list(type: Class<T>, query: ServiceQuery): IServicesHolder<T> {
            return DummyServicesHolder<T>()
        }

        override fun <T> list(type: Class<T>, componentName: String, query: ServiceQuery): IServicesHolder<T> {
            return DummyServicesHolder<T>()
        }
    }

    companion object {
        fun setup() {
            org.idempiere.common.base.Service.instance = DummyService()
        }
    }
}