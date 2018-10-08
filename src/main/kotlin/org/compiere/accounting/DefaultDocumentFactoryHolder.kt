package org.compiere.accounting

import org.compiere.model.IDocFactory
import org.idempiere.common.base.IServicesHolder

class DefaultDocumentFactoryHolder : IServicesHolder<IDocFactory> {
    override fun getServices(): MutableList<IDocFactory> {
        return mutableListOf(DefaultDocumentFactory())
    }
}