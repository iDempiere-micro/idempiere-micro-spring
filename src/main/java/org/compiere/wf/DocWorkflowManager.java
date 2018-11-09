package org.compiere.wf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.process.ProcessInfo;
import org.compiere.util.SystemIDs;
import org.idempiere.orm.DocWorkflowMgr;
import org.idempiere.orm.PO;
import org.idempiere.common.util.CLogger;
import org.idempiere.common.util.DB;
import org.idempiere.common.util.Env;
import org.idempiere.common.util.Evaluator;


/**
 *	Document Workflow Manager
 *	
 *  @author Jorg Janke
 *  @version $Id: DocWorkflowManager.java,v 1.2 2006/07/30 00:51:05 jjanke Exp $
 */
public class DocWorkflowManager implements DocWorkflowMgr
{
	/**
	 * 	Get Document Workflow Manager
	 *	@return mgr
	 */
	public static DocWorkflowManager get()
	{
		if (s_mgr == null)
			s_mgr = new DocWorkflowManager();
		return s_mgr;
	}	//	get

/* DAP
	//	Set PO Workflow Manager
	static {
		PO.setDocWorkflowMgr(get());
	}*/
	
	/**	Document Workflow Manager		*/
	private volatile static DocWorkflowManager	s_mgr = null;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(DocWorkflowManager.class);
	
	
	/**
	 * 	Doc Workflow Manager
	 */
	private DocWorkflowManager ()
	{
		super ();
		if (s_mgr == null)
			s_mgr = this;
	}	//	DocWorkflowManager

	private int	m_noCalled = 0;
	private int	m_noStarted = 0;
	
	/**
	 * 	Process Document Value Workflow
	 *	@param document document
	 *	@param AD_Table_ID table
	 *	@return true if WF started
	 */
	public boolean process (PO document, int AD_Table_ID)
	{
		m_noCalled++;
		MWorkflow[] wfs = MWorkflow.getDocValue (document.getCtx(), 
			document.getADClientID(), AD_Table_ID
			, document.get_TrxName() // Bug 1568766 Trx should be kept all along the road	
		);
		if (wfs == null || wfs.length == 0)
			return false;
		
		boolean started = false;
		for (int i = 0; i < wfs.length; i++)
		{
			MWorkflow wf = wfs[i];
			//	We have a Document Workflow
			String logic = wf.getDocValueLogic();
			if (logic == null || logic.length() == 0)
			{
				log.severe ("Workflow has no Logic - " + wf.getName());
				continue;
			}
		
			//	Re-check: Document must be same Client as workflow
			if (wf.getADClientID() != document.getADClientID())
				continue;
		
			//	Check Logic
			boolean sql = logic.startsWith("SQL=");
			if (sql && !testStart(wf, document))
			{
				if (log.isLoggable(Level.FINE)) log.fine("SQL Logic evaluated to false (" + logic + ")");
				continue;
			}
			if (!sql && !Evaluator.evaluateLogic(document, logic))
			{
				if (log.isLoggable(Level.FINE)) log.fine("Logic evaluated to false (" + logic + ")");
				continue;
			}
		
			//	Start Workflow
			log.fine(logic);
			int AD_Process_ID = SystemIDs.PROCESS_AD_WORKFLOW_DOCVALUE;		//	HARDCODED
			ProcessInfo pi = new ProcessInfo (wf.getName(), AD_Process_ID,
				AD_Table_ID, document.getId());
			pi.setAD_User_ID (Env.getAD_User_ID(document.getCtx()));
			pi.setADClientID(document.getADClientID());
			//
			if (wf.start(pi, document.get_TrxName()) != null)
			{
				if (log.isLoggable(Level.CONFIG)) log.config(wf.getName());
				m_noStarted++;
				started = true;
			}
		}
		return started;
	}	//	process

	/**
	 * 	Test Start condition
	 *	@param wf workflow
	 *	@param document document
	 *	@return true if WF should be started
	 */
	private boolean testStart (MWorkflow wf, PO document)
	{
		boolean retValue = false;
		String logic = wf.getDocValueLogic();
		logic = logic.substring(4);		//	"SQL="
		//
		String tableName = document.get_TableName();
		String[] keyColumns = document.get_KeyColumns();
		if (keyColumns.length != 1)
		{
			log.severe("Tables with more then one key column not supported - " 
				+ tableName + " = " + keyColumns.length);
			return false;
		}
		String keyColumn = keyColumns[0];
		StringBuffer sql = new StringBuffer("SELECT ")
			.append(keyColumn).append(" FROM ").append(tableName)
			.append(" WHERE AD_Client_ID=? AND ")		//	#1
				.append(keyColumn).append("=? AND ")	//	#2
			.append(logic)
		//	Duplicate Open Workflow test
			.append(" AND NOT EXISTS (SELECT * FROM AD_WF_Process wfp ")
				.append("WHERE wfp.AD_Table_ID=? AND wfp.Record_ID=")	//	#3
				.append(tableName).append(".").append(keyColumn)
				.append(" AND wfp.AD_Workflow_ID=?")	//	#4
				.append(" AND SUBSTR(wfp.WFState,1,1)='O')");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql.toString(), document.get_TrxName());
			pstmt.setInt (1, wf.getADClientID());
			pstmt.setInt (2, document.getId());
			pstmt.setInt (3, document.get_Table_ID());
			pstmt.setInt (4, wf.getAD_Workflow_ID());
			rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = true;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, "Logic=" + logic
				+ " - SQL=" + sql.toString(), e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; 
			pstmt = null;
		}

		return retValue;
	}	//	testStart
	
	
	/**
	* 	String Representation
	*	@return info
	*/
	public String toString()
	{
		StringBuilder sb = new StringBuilder("DocWorkflowManager[");
		sb.append("Called=").append(m_noCalled)
			.append(",Stated=").append(m_noStarted)
			.append("]");
		return sb.toString();
	}	//	toString
}	//	DocWorkflowManager