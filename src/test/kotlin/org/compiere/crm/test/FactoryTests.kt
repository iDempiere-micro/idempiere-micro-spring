package org.compiere.crm.test

import company.bigger.test.support.BaseTest
import org.compiere.crm.MUser
import org.compiere.model.I_C_BPartner
import org.compiere.orm.DefaultModelFactory
import org.compiere.orm.IModelFactory
import org.idempiere.common.util.DB
import org.idempiere.common.util.Env
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class FactoryTests : BaseTest() {
    @Before
    fun prepareEnv() {
        val ctx = Env.getCtx()
        val AD_CLIENT_ID = 11
        val AD_CLIENT_ID_s = AD_CLIENT_ID.toString()
        ctx.setProperty(Env.AD_CLIENT_ID, AD_CLIENT_ID_s)
        Env.setContext(ctx, Env.AD_CLIENT_ID, AD_CLIENT_ID_s)
    }

    @Test
    fun getUsingDefaultModelFactoryById() {
        val modelFactory: IModelFactory = DefaultModelFactory()
        val result = modelFactory.getPO(I_C_BPartner.Table_Name, 118, "pokus")
        println(result)
        assertNotNull(result)
    }

    @Test
    fun getUsingDefaultModelFactoryFromRS() {
        val AD_CLIENT_ID = 11
        val tableName = I_C_BPartner.Table_Name
        val AD_ORG_ID = 0
        val id = 118

        val sql =
            ("SELECT * FROM adempiere.\"${tableName}\" " +
                "WHERE (\"${tableName}\".ad_client_id = ? OR \"${tableName}\".ad_client_id=0) " +
                "AND (\"${tableName}\".ad_org_id = ? OR \"${tableName}\".ad_org_id=0) " +
                "AND (\"${tableName}_ID\"=?);"
                ).toLowerCase()
        println("SQL:$sql")
        val cnn = DB.getConnectionRO()
        val statement = cnn.prepareStatement(sql)
        statement.setInt(1, AD_CLIENT_ID)
        statement.setInt(2, AD_ORG_ID)
        statement.setInt(3, id)
        val rs = statement.executeQuery()
        rs.next()

        val modelFactory: IModelFactory = DefaultModelFactory()
        val result = modelFactory.getPO(tableName, rs, "pokus")
        println(result)
        assertNotNull(result)
        assertEquals(id, result._ID)

        cnn.close()
    }

    @Test
    fun `Client 11 has GardenUser and GardenAdmin`() {
        val ctx = Env.getCtx()
        val AD_CLIENT_ID = 11
        val res = MUser.getOfClient(ctx, AD_CLIENT_ID, "pokus")
        assertNotNull(res.find { it.firstName == "GardenUser" })
        assertNotNull(res.find { it.firstName == "GardenAdmin" })
    }
}