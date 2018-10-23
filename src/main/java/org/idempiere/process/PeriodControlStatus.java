package org.idempiere.process;

import java.util.logging.Level;

import org.compiere.accounting.MPeriodControl;
import org.compiere.model.IProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.idempiere.common.util.AdempiereUserError;
import org.idempiere.common.util.CacheMgt;


/**
 *	Open/Close Period Control
 *	
 *  @author Jorg Janke
 *  @version $Id: PeriodControlStatus.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class PeriodControlStatus extends SvrProcess
{
	/** Period Control				*/
	private int 		p_C_PeriodControl_ID = 0;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		IProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_C_PeriodControl_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt() throws Exception
	{
		if (log.isLoggable(Level.INFO)) log.info ("C_PeriodControl_ID=" + p_C_PeriodControl_ID);
		MPeriodControl pc = new MPeriodControl (getCtx(), p_C_PeriodControl_ID, get_TrxName());
		if (pc.get_ID() == 0)
			throw new AdempiereUserError("@NotFound@  @C_PeriodControl_ID@=" + p_C_PeriodControl_ID);
		//	Permanently closed
		if (MPeriodControl.PERIODACTION_PermanentlyClosePeriod.equals(pc.getPeriodStatus()))
			throw new AdempiereUserError("@PeriodStatus@ = " + pc.getPeriodStatus());
		//	No Action
		if (MPeriodControl.PERIODACTION_NoAction.equals(pc.getPeriodAction()))
			return "@OK@";
	
		//	Open
		if (MPeriodControl.PERIODACTION_OpenPeriod.equals(pc.getPeriodAction()))
			pc.setPeriodStatus(MPeriodControl.PERIODSTATUS_Open);
		//	Close
		if (MPeriodControl.PERIODACTION_ClosePeriod.equals(pc.getPeriodAction()))
			pc.setPeriodStatus(MPeriodControl.PERIODSTATUS_Closed);
		//	Close Permanently
		if (MPeriodControl.PERIODACTION_PermanentlyClosePeriod.equals(pc.getPeriodAction()))
			pc.setPeriodStatus(MPeriodControl.PERIODSTATUS_PermanentlyClosed);
		pc.setPeriodAction(MPeriodControl.PERIODACTION_NoAction);
		//
		boolean ok = pc.save();
		
		//	Reset Cache
		CacheMgt.get().reset("C_PeriodControl", 0);
		CacheMgt.get().reset("C_Period", pc.getC_Period_ID());

		if (!ok)
			return "@Error@";
		return "@OK@";
	}	//	doIt

}	//	PeriodControlStatus
