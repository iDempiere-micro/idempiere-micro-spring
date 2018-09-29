/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.idempiere.org/license.html           *
 *****************************************************************************/
package org.compiere.docengine;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

import org.compiere.accounting.Doc;
import org.compiere.accounting.MAcctSchema;
import org.compiere.model.*;
import org.compiere.orm.*;
import org.compiere.process.DocAction;
import org.idempiere.orm.EventManager;
import org.idempiere.orm.EventProperty;
import org.idempiere.orm.IEventTopics;
import org.idempiere.common.exceptions.AdempiereException;
import org.idempiere.common.util.CLogger;
import org.idempiere.common.util.DB;
import org.idempiere.common.util.Env;
import org.compiere.util.Msg;
import org.idempiere.common.util.Util;
import org.eevolution.model.I_DD_Order;
import org.eevolution.model.I_HR_Process;
import org.eevolution.model.I_PP_Cost_Collector;
import org.eevolution.model.I_PP_Order;

/**
 *	Document Action Engine
 *
 *  @author Jorg Janke
 *  @author Karsten Thiemann FR [ 1782412 ]
 *  @author victor.perez@e-evolution.com www.e-evolution.com FR [ 1866214 ]  http://sourceforge.net/tracker/index.php?func=detail&aid=1866214&group_id=176962&atid=879335
 *  @version $Id: DocumentEngine.java,v 1.2 2006/07/30 00:54:44 jjanke Exp $
 */
public class DocumentEngine implements DocAction
{
	/**
	 * 	Doc Engine (Drafted)
	 * 	@param po document
	 */
	public DocumentEngine (DocAction po)
	{
		this (po, DocAction.STATUS_Drafted);
	}	//	DocActionEngine

	/**
	 * 	Doc Engine
	 * 	@param po document
	 * 	@param docStatus initial document status
	 */
	public DocumentEngine (DocAction po, String docStatus)
	{
		m_document = po;
		if (docStatus != null)
			m_status = docStatus;
	}	//	DocActionEngine

	/** Persistent Document 	*/
	private DocAction	m_document;
	/** Document Status			*/
	private String		m_status = DocAction.STATUS_Drafted;
	/**	Process Message 		*/
	private String		m_message = null;
	/** Actual Doc Action		*/
	private String		m_action = null;

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(DocumentEngine.class);

	/**
	 * 	Get Doc Status
	 *	@return document status
	 */
	public String getDocStatus()
	{
		return m_status;
	}	//	getDocStatus

	/**
	 * 	Set Doc Status - Ignored
	 *	@param ignored Status is not set directly
	 * @see DocAction#setDocStatus(String)
	 */
	public void setDocStatus(String ignored)
	{
	}	//	setDocStatus

	/**
	 * 	Document is Drafted
	 *	@return true if drafted
	 */
	public boolean isDrafted()
	{
		return DocAction.STATUS_Drafted.equals(m_status);
	}	//	isDrafted

	/**
	 * 	Document is Invalid
	 *	@return true if Invalid
	 */
	public boolean isInvalid()
	{
		return DocAction.STATUS_Invalid.equals(m_status);
	}	//	isInvalid

	/**
	 * 	Document is In Progress
	 *	@return true if In Progress
	 */
	public boolean isInProgress()
	{
		return DocAction.STATUS_InProgress.equals(m_status);
	}	//	isInProgress

	/**
	 * 	Document is Approved
	 *	@return true if Approved
	 */
	public boolean isApproved()
	{
		return DocAction.STATUS_Approved.equals(m_status);
	}	//	isApproved

	/**
	 * 	Document is Not Approved
	 *	@return true if Not Approved
	 */
	public boolean isNotApproved()
	{
		return DocAction.STATUS_NotApproved.equals(m_status);
	}	//	isNotApproved

	/**
	 * 	Document is Waiting Payment or Confirmation
	 *	@return true if Waiting Payment
	 */
	public boolean isWaiting()
	{
		return DocAction.STATUS_WaitingPayment.equals(m_status)
			|| DocAction.STATUS_WaitingConfirmation.equals(m_status);
	}	//	isWaitingPayment

	/**
	 * 	Document is Completed
	 *	@return true if Completed
	 */
	public boolean isCompleted()
	{
		return DocAction.STATUS_Completed.equals(m_status);
	}	//	isCompleted

	/**
	 * 	Document is Reversed
	 *	@return true if Reversed
	 */
	public boolean isReversed()
	{
		return DocAction.STATUS_Reversed.equals(m_status);
	}	//	isReversed

	/**
	 * 	Document is Closed
	 *	@return true if Closed
	 */
	public boolean isClosed()
	{
		return DocAction.STATUS_Closed.equals(m_status);
	}	//	isClosed

	/**
	 * 	Document is Voided
	 *	@return true if Voided
	 */
	public boolean isVoided()
	{
		return DocAction.STATUS_Voided.equals(m_status);
	}	//	isVoided

	/**
	 * 	Document Status is Unknown
	 *	@return true if unknown
	 */
	public boolean isUnknown()
	{
		return DocAction.STATUS_Unknown.equals(m_status) ||
			!(isDrafted() || isInvalid() || isInProgress() || isNotApproved()
				|| isApproved() || isWaiting() || isCompleted()
				|| isReversed() || isClosed() || isVoided() );
	}	//	isUnknown


	/**
	 * 	Process actual document.
	 * 	Checks if user (document) action is valid and then process action
	 * 	Calls the individual actions which call the document action
	 *	@param processAction document action based on workflow
	 *	@param docAction document action based on document
	 *	@return true if performed
	 */
	public boolean processIt (String processAction, String docAction)
	{
		//ensure doc status not change by other session
		if (m_document instanceof PO) {
			PO docPO = (PO) m_document;
			if (docPO.get_ID() > 0 && docPO.get_TrxName() != null && docPO.get_ValueOld("DocStatus") != null) {
				DB.getDatabase().forUpdate(docPO, 30);
				String docStatusOriginal = (String) docPO.get_ValueOld("DocStatus");
				String statusSql = "SELECT DocStatus FROM " + docPO.get_TableName() + " WHERE " + docPO.get_KeyColumns()[0] + " = ? ";
				String currentStatus = DB.getSQLValueString((String)null, statusSql, docPO.get_ID());
				if (!docStatusOriginal.equals(currentStatus) && currentStatus != null) {
					currentStatus = DB.getSQLValueString(docPO.get_TrxName(), statusSql, docPO.get_ID());
					if (!docStatusOriginal.equals(currentStatus)) {
						throw new IllegalStateException(Msg.getMsg(docPO.getCtx(), "DocStatusChanged") + " " + docPO.toString());
					}
				}
			}
		}
				
		m_message = null;
		m_action = null;
		//	Std User Workflows - see MWFNodeNext.isValidFor

		if (isValidAction(processAction))	//	WF Selection first
			m_action = processAction;
		//
		else if (isValidAction(docAction))	//	User Selection second
			m_action = docAction;
		//	Nothing to do
		else if (processAction.equals(DocAction.ACTION_None)
			|| docAction.equals(DocAction.ACTION_None))
		{
			if (m_document != null)
				m_document.get_Logger().info ("**** No Action (Prc=" + processAction + "/Doc=" + docAction + ") " + m_document);
			return true;
		}
		else
		{
			throw new IllegalStateException("Status=" + getDocStatus()
				+ " - Invalid Actions: Process="  + processAction + ", Doc=" + docAction);
		}
		if (m_document != null)
			m_document.get_Logger().info ("**** Action=" + m_action + " (Prc=" + processAction + "/Doc=" + docAction + ") " + m_document);
		boolean success = processIt (m_action);
		if (m_document != null)
			m_document.get_Logger().fine("**** Action=" + m_action + " - Success=" + success);
		return success;
	}	//	process

	/**
	 * 	Process actual document - do not call directly.
	 * 	Calls the individual actions which call the document action
	 *	@param action document action
	 *	@return true if performed
	 */
	public boolean processIt (String action)
	{
		m_message = null;
		m_action = action;
		//
		if (DocAction.ACTION_Unlock.equals(m_action))
			return unlockIt();
		if (DocAction.ACTION_Invalidate.equals(m_action))
			return invalidateIt();
		if (DocAction.ACTION_Prepare.equals(m_action))
			return DocAction.STATUS_InProgress.equals(prepareIt());
		if (DocAction.ACTION_Approve.equals(m_action))
			return approveIt();
		if (DocAction.ACTION_Reject.equals(m_action))
			return rejectIt();
		if (DocAction.ACTION_Complete.equals(m_action) || DocAction.ACTION_WaitComplete.equals(m_action))
		{
			String status = null;
			if (isDrafted() || isInvalid())		//	prepare if not prepared yet
			{
				status = prepareIt();
				if (!DocAction.STATUS_InProgress.equals(status))
					return false;
			}
			status = completeIt();
			boolean ok =   DocAction.STATUS_Completed.equals(status)
						|| DocAction.STATUS_InProgress.equals(status)
						|| DocAction.STATUS_WaitingPayment.equals(status)
						|| DocAction.STATUS_WaitingConfirmation.equals(status);
			if (m_document != null && ok)
			{
				// PostProcess documents when invoice or inout (this is to postprocess the generated MatchPO and MatchInv if any)
				ArrayList<IPODoc> docsPostProcess = new ArrayList<IPODoc>();
				if (m_document instanceof I_C_Invoice || m_document instanceof I_M_InOut) {
					if (m_document instanceof I_C_Invoice) {
						docsPostProcess  = ((I_C_Invoice) m_document).getDocsPostProcess();
					}
					if (m_document instanceof I_M_InOut) {
						docsPostProcess  = ((I_M_InOut) m_document).getDocsPostProcess();
					}
				}
				if (m_document instanceof PO && docsPostProcess.size() > 0) {
					// Process (this is to update the ProcessedOn flag with a timestamp after the original document)
					for (IPODoc docafter : docsPostProcess) {
						docafter.setProcessedOn("Processed", true, false);
						docafter.saveEx();
					}
				}

				if (DocAction.STATUS_Completed.equals(status) && MClient.get(Env.getCtx()).isClientAccountingImmediate())
				{
					m_document.saveEx();
					postIt();

					if (m_document instanceof PO && docsPostProcess.size() > 0) {
						for (IPODoc docafter : docsPostProcess) {
							@SuppressWarnings("unused")
							String ignoreError = DocumentEngine.postImmediate(docafter.getCtx(), docafter.getAD_Client_ID(), docafter.get_Table_ID(), docafter.get_ID(), true, docafter.get_TrxName());
						}
					}
				}

			}
			return ok;
		}
		if (DocAction.ACTION_ReActivate.equals(m_action))
			return reActivateIt();
		if (DocAction.ACTION_Reverse_Accrual.equals(m_action))
			return reverseAccrualIt();
		if (DocAction.ACTION_Reverse_Correct.equals(m_action))
			return reverseCorrectIt();
		if (DocAction.ACTION_Close.equals(m_action))
			return closeIt();
		if (DocAction.ACTION_Void.equals(m_action))
			return voidIt();
		if (DocAction.ACTION_Post.equals(m_action))
			return postIt();
		//
		return false;
	}	//	processDocument

	/**
	 * 	Unlock Document.
	 * 	Status: Drafted
	 * 	@return true if success
	 * 	@see DocAction#unlockIt()
	 */
	public boolean unlockIt()
	{
		if (!isValidAction(DocAction.ACTION_Unlock))
			return false;
		if (m_document != null)
		{
			if (m_document.unlockIt())
			{
				m_status = DocAction.STATUS_Drafted;
				m_document.setDocStatus(m_status);
				return true;
			}
			return false;
		}
		m_status = DocAction.STATUS_Drafted;
		return true;
	}	//	unlockIt

	/**
	 * 	Invalidate Document.
	 * 	Status: Invalid
	 * 	@return true if success
	 * 	@see DocAction#invalidateIt()
	 */
	public boolean invalidateIt()
	{
		if (!isValidAction(DocAction.ACTION_Invalidate))
			return false;
		if (m_document != null)
		{
			if (m_document.invalidateIt())
			{
				m_status = DocAction.STATUS_Invalid;
				m_document.setDocStatus(m_status);
				return true;
			}
			return false;
		}
		m_status = DocAction.STATUS_Invalid;
		return true;
	}	//	invalidateIt

	/**
	 *	Process Document.
	 * 	Status is set by process
	 * 	@return new status (In Progress or Invalid)
	 * 	@see DocAction#prepareIt()
	 */
	public String prepareIt()
	{
		if (!isValidAction(DocAction.ACTION_Prepare))
			return m_status;
		if (m_document != null)
		{
			m_status = m_document.prepareIt();
			m_document.setDocStatus(m_status);
		}
		return m_status;
	}	//	processIt

	/**
	 * 	Approve Document.
	 * 	Status: Approved
	 * 	@return true if success
	 * 	@see DocAction#approveIt()
	 */
	public boolean  approveIt()
	{
		if (!isValidAction(DocAction.ACTION_Approve))
			return false;
		if (m_document != null)
		{
			if (m_document.approveIt())
			{
				m_status = DocAction.STATUS_Approved;
				m_document.setDocStatus(m_status);
				return true;
			}
			return false;
		}
		m_status = DocAction.STATUS_Approved;
		return true;
	}	//	approveIt

	/**
	 * 	Reject Approval.
	 * 	Status: Not Approved
	 * 	@return true if success
	 * 	@see DocAction#rejectIt()
	 */
	public boolean rejectIt()
	{
		if (!isValidAction(DocAction.ACTION_Reject))
			return false;
		if (m_document != null)
		{
			if (m_document.rejectIt())
			{
				m_status = DocAction.STATUS_NotApproved;
				m_document.setDocStatus(m_status);
				return true;
			}
			return false;
		}
		m_status = DocAction.STATUS_NotApproved;
		return true;
	}	//	rejectIt

	/**
	 * 	Complete Document.
	 * 	Status is set by process
	 * 	@return new document status (Complete, In Progress, Invalid, Waiting ..)
	 * 	@see DocAction#completeIt()
	 */
	public String completeIt()
	{
		if (!isValidAction(DocAction.ACTION_Complete))
			return m_status;
		if (m_document != null)
		{
			m_status = m_document.completeIt();
			m_document.setDocStatus(m_status);
		}
		return m_status;
	}	//	completeIt

	/**
	 * 	Post Document
	 * 	Does not change status
	 * 	@return true if success
	 */
	public boolean postIt()
	{
		if (!isValidAction(DocAction.ACTION_Post)
			|| m_document == null)
			return false;

		String error = DocumentEngine.postImmediate(Env.getCtx(), m_document.getAD_Client_ID(), m_document.get_Table_ID(), m_document.get_ID(), true, m_document.get_TrxName());
		if (DocAction.ACTION_Post.equals(m_action)) {
			// forced post via process - throw exception to inform the caller about the error
			if (! Util.isEmpty(error)) {
				throw new AdempiereException(error);
			}
		}
		return (error == null);
	}	//	postIt

	/**
	 * 	Void Document.
	 * 	Status: Voided
	 * 	@return true if success
	 * 	@see DocAction#voidIt()
	 */
	public boolean voidIt()
	{
		if (!isValidAction(DocAction.ACTION_Void))
			return false;
		if (m_document != null)
		{
			if (m_document.voidIt())
			{
				m_status = DocAction.STATUS_Voided;
				if (!m_document.getDocStatus().equals(DocAction.STATUS_Reversed))
					m_document.setDocStatus(m_status);
				return true;
			}
			return false;
		}
		m_status = DocAction.STATUS_Voided;
		return true;
	}	//	voidIt

	/**
	 * 	Close Document.
	 * 	Status: Closed
	 * 	@return true if success
	 * 	@see DocAction#closeIt()
	 */
	public boolean closeIt()
	{
		if (m_document != null 	//	orders can be closed any time
			&& m_document.get_Table_ID() == I_C_Order.Table_ID)
			;
		else if (!isValidAction(DocAction.ACTION_Close))
			return false;
		if (m_document != null)
		{
			if (m_document.closeIt())
			{
				m_status = DocAction.STATUS_Closed;
				m_document.setDocStatus(m_status);
				return true;
			}
			return false;
		}
		m_status = DocAction.STATUS_Closed;
		return true;
	}	//	closeIt

	/**
	 * 	Reverse Correct Document.
	 * 	Status: Reversed
	 * 	@return true if success
	 * 	@see DocAction#reverseCorrectIt()
	 */
	public boolean reverseCorrectIt()
	{
		if (!isValidAction(DocAction.ACTION_Reverse_Correct))
			return false;
		if (m_document != null)
		{
			if (m_document.reverseCorrectIt())
			{
				m_status = DocAction.STATUS_Reversed;
				m_document.setDocStatus(m_status);
				return true;
			}
			return false;
		}
		m_status = DocAction.STATUS_Reversed;
		return true;
	}	//	reverseCorrectIt

	/**
	 * 	Reverse Accrual Document.
	 * 	Status: Reversed
	 * 	@return true if success
	 * 	@see DocAction#reverseAccrualIt()
	 */
	public boolean reverseAccrualIt()
	{
		if (!isValidAction(DocAction.ACTION_Reverse_Accrual))
			return false;
		if (m_document != null)
		{
			if (m_document.reverseAccrualIt())
			{
				m_status = DocAction.STATUS_Reversed;
				m_document.setDocStatus(m_status);
				return true;
			}
			return false;
		}
		m_status = DocAction.STATUS_Reversed;
		return true;
	}	//	reverseAccrualIt

	/**
	 * 	Re-activate Document.
	 * 	Status: In Progress
	 * 	@return true if success
	 * 	@see DocAction#reActivateIt()
	 */
	public boolean reActivateIt()
	{
		if (!isValidAction(DocAction.ACTION_ReActivate))
			return false;
		if (m_document != null)
		{
			if (m_document.reActivateIt())
			{
				m_status = DocAction.STATUS_InProgress;
				m_document.setDocStatus(m_status);
				return true;
			}
			return false;
		}
		m_status = DocAction.STATUS_InProgress;
		return true;
	}	//	reActivateIt


	/**
	 * 	Set Document Status to new Status
	 *	@param newStatus new status
	 */
	void setStatus (String newStatus)
	{
		m_status = newStatus;
	}	//	setStatus


	/**************************************************************************
	 * 	Get Action Options based on current Status
	 *	@return array of actions
	 */
	public String[] getActionOptions()
	{
		if (isInvalid())
			return new String[] {DocAction.ACTION_Prepare, DocAction.ACTION_Invalidate,
				DocAction.ACTION_Unlock, DocAction.ACTION_Void};

		if (isDrafted())
			return new String[] {DocAction.ACTION_Prepare, DocAction.ACTION_Invalidate, DocAction.ACTION_Complete,
				DocAction.ACTION_Unlock, DocAction.ACTION_Void};

		if (isInProgress() || isApproved())
			return new String[] {DocAction.ACTION_Complete, DocAction.ACTION_WaitComplete,
				DocAction.ACTION_Approve, DocAction.ACTION_Reject,
				DocAction.ACTION_Unlock, DocAction.ACTION_Void, DocAction.ACTION_Prepare};

		if (isNotApproved())
			return new String[] {DocAction.ACTION_Reject, DocAction.ACTION_Prepare,
				DocAction.ACTION_Unlock, DocAction.ACTION_Void};

		if (isWaiting())
			return new String[] {DocAction.ACTION_Complete, DocAction.ACTION_WaitComplete,
				DocAction.ACTION_ReActivate, DocAction.ACTION_Void, DocAction.ACTION_Close};

		if (isCompleted())
			return new String[] {DocAction.ACTION_Close, DocAction.ACTION_ReActivate,
				DocAction.ACTION_Reverse_Accrual, DocAction.ACTION_Reverse_Correct,
				DocAction.ACTION_Post, DocAction.ACTION_Void};

		if (isClosed())
			return new String[] {DocAction.ACTION_Post, DocAction.ACTION_ReOpen};

		if (isReversed() || isVoided())
			return new String[] {DocAction.ACTION_Post};

		return new String[] {};
	}	//	getActionOptions

	/**
	 * 	Is The Action Valid based on current state
	 *	@param action action
	 *	@return true if valid
	 */
	public boolean isValidAction (String action)
	{
		String[] options = getActionOptions();
		for (int i = 0; i < options.length; i++)
		{
			if (options[i].equals(action))
				return true;
		}
		return false;
	}	//	isValidAction

	/**
	 * 	Get Process Message
	 *	@return clear text error message
	 */
	public String getProcessMsg ()
	{
		return m_message;
	}	//	getProcessMsg

	/**
	 * 	Get Process Message
	 *	@param msg clear text error message
	 */
	public void setProcessMsg (String msg)
	{
		m_message = msg;
	}	//	setProcessMsg


	/**	Document Exception Message		*/
	private static String EXCEPTION_MSG = "Document Engine is no Document";

	/*************************************************************************
	 * 	Get Summary
	 *	@return throw exception
	 */
	public String getSummary()
	{
		throw new IllegalStateException(EXCEPTION_MSG);
	}

	/**
	 * 	Get Document No
	 *	@return throw exception
	 */
	public String getDocumentNo()
	{
		throw new IllegalStateException(EXCEPTION_MSG);
	}

	/**
	 * 	Get Document Info
	 *	@return throw exception
	 */
	public String getDocumentInfo()
	{
		throw new IllegalStateException(EXCEPTION_MSG);
	}

	/**
	 * 	Get Document Owner
	 *	@return throw exception
	 */
	public int getDoc_User_ID()
	{
		throw new IllegalStateException(EXCEPTION_MSG);
	}

	/**
	 * 	Get Document Currency
	 *	@return throw exception
	 */
	public int getC_Currency_ID()
	{
		throw new IllegalStateException(EXCEPTION_MSG);
	}

	/**
	 * 	Get Document Approval Amount
	 *	@return throw exception
	 */
	public BigDecimal getApprovalAmt()
	{
		throw new IllegalStateException(EXCEPTION_MSG);
	}

	/**
	 * 	Get Document Client
	 *	@return throw exception
	 */
	public int getAD_Client_ID()
	{
		throw new IllegalStateException(EXCEPTION_MSG);
	}

	/**
	 * 	Get Document Organization
	 *	@return throw exception
	 */
	public int getAD_Org_ID()
	{
		throw new IllegalStateException(EXCEPTION_MSG);
	}

	/**
	 * 	Get Doc Action
	 *	@return Document Action
	 */
	public String getDocAction()
	{
		return m_action;
	}

	/**
	 * 	Save Document
	 *	@return throw exception
	 */
	public boolean save()
	{
		throw new IllegalStateException(EXCEPTION_MSG);
	}

	/**
	 * 	Save Document
	 *	@return throw exception
	 */
	public void saveEx() throws AdempiereException
	{
		throw new IllegalStateException(EXCEPTION_MSG);
	}
	
	/**
	 * 	Get Context
	 *	@return context
	 */
	public Properties getCtx()
	{
		if (m_document != null)
			return m_document.getCtx();
		throw new IllegalStateException(EXCEPTION_MSG);
	}	//	getCtx

	/**
	 * 	Get ID of record
	 *	@return ID
	 */
	public int get_ID()
	{
		if (m_document != null)
			return m_document.get_ID();
		throw new IllegalStateException(EXCEPTION_MSG);
	}	//	get_ID

	/**
	 * 	Get AD_Table_ID
	 *	@return AD_Table_ID
	 */
	public int get_Table_ID()
	{
		if (m_document != null)
			return m_document.get_Table_ID();
		throw new IllegalStateException(EXCEPTION_MSG);
	}	//	get_Table_ID

	/**
	 * 	Get Logger
	 *	@return logger
	 */
	public CLogger get_Logger()
	{
		if (m_document != null)
			return m_document.get_Logger();
		throw new IllegalStateException(EXCEPTION_MSG);
	}	//	get_Logger

	/**
	 * 	Get Transaction
	 *	@return trx name
	 */
	public String get_TrxName()
	{
		return null;
	}	//	get_TrxName

	/**
	 * 	CreatePDF
	 *	@return null
	 */
	public File createPDF ()
	{
		return null;
	}

	/**
	 * Get list of valid document action into the options array parameter.
	 * Set default document action into the docAction array parameter.
	 * @param docStatus
	 * @param processing
	 * @param orderType
	 * @param isSOTrx
	 * @param AD_Table_ID
	 * @param docAction
	 * @param options
	 * @param periodOpen - flag indicating if the period is Open - to avoid including Void and ReverseCorrect options in the list
	 * @return Number of valid options
	 */
	public static int getValidActions(String docStatus, Object processing,
			String orderType, String isSOTrx, int AD_Table_ID, String[] docAction, String[] options, boolean periodOpen, PO po)
	{
		if (options == null)
			throw new IllegalArgumentException("Option array parameter is null");
		if (docAction == null)
			throw new IllegalArgumentException("Doc action array parameter is null");

		int index = 0;

//		Locked
		if (processing != null)
		{
			boolean locked = "Y".equals(processing);
			if (!locked && processing instanceof Boolean)
				locked = ((Boolean)processing).booleanValue();
			if (locked)
				options[index++] = DocumentEngine.ACTION_Unlock;
		}

		//	Approval required           ..  NA
		if (docStatus.equals(DocumentEngine.STATUS_NotApproved))
		{
			options[index++] = DocumentEngine.ACTION_Prepare;
			options[index++] = DocumentEngine.ACTION_Void;
		}
		//	Draft/Invalid				..  DR/IN
		else if (docStatus.equals(DocumentEngine.STATUS_Drafted)
			|| docStatus.equals(DocumentEngine.STATUS_Invalid))
		{
			options[index++] = DocumentEngine.ACTION_Complete;
		//	options[index++] = DocumentEngine.ACTION_Prepare;
			options[index++] = DocumentEngine.ACTION_Void;
		}
		//	In Process                  ..  IP
		else if (docStatus.equals(DocumentEngine.STATUS_InProgress)
			|| docStatus.equals(DocumentEngine.STATUS_Approved))
		{
			options[index++] = DocumentEngine.ACTION_Complete;
			options[index++] = DocumentEngine.ACTION_Void;
		}
		//	Complete                    ..  CO
		else if (docStatus.equals(DocumentEngine.STATUS_Completed))
		{
			options[index++] = DocumentEngine.ACTION_Close;
		}
		//	Waiting Payment
		else if (docStatus.equals(DocumentEngine.STATUS_WaitingPayment)
			|| docStatus.equals(DocumentEngine.STATUS_WaitingConfirmation))
		{
			options[index++] = DocumentEngine.ACTION_Void;
			options[index++] = DocumentEngine.ACTION_Prepare;
		}
		//	Closed, Voided, REversed    ..  CL/VO/RE
		else if (docStatus.equals(DocumentEngine.STATUS_Closed)
			|| docStatus.equals(DocumentEngine.STATUS_Voided)
			|| docStatus.equals(DocumentEngine.STATUS_Reversed))
			return 0;

		/********************
		 *  Order
		 */
		if (AD_Table_ID == I_C_Order.Table_ID)
		{
			//	Draft                       ..  DR/IP/IN
			if (docStatus.equals(DocumentEngine.STATUS_Drafted)
				|| docStatus.equals(DocumentEngine.STATUS_InProgress)
				|| docStatus.equals(DocumentEngine.STATUS_Invalid))
			{
				options[index++] = DocumentEngine.ACTION_Prepare;
				options[index++] = DocumentEngine.ACTION_Close;
				//	Draft Sales Order Quote/Proposal - Process
				if ("Y".equals(isSOTrx)
					&& ("OB".equals(orderType) || "ON".equals(orderType)))
					docAction[0] = DocumentEngine.ACTION_Prepare;
			}
			//	Complete                    ..  CO
			else if (docStatus.equals(DocumentEngine.STATUS_Completed))
			{
				options[index++] = DocumentEngine.ACTION_Void;
				options[index++] = DocumentEngine.ACTION_ReActivate;
			}
			else if (docStatus.equals(DocumentEngine.STATUS_WaitingPayment))
			{
				options[index++] = DocumentEngine.ACTION_ReActivate;
				options[index++] = DocumentEngine.ACTION_Close;
			}
		}
		/********************
		 *  Shipment
		 */
		else if (AD_Table_ID == I_M_InOut.Table_ID)
		{
			//	Complete                    ..  CO
			if (docStatus.equals(DocumentEngine.STATUS_Completed))
			{
				if (periodOpen) {
					options[index++] = DocumentEngine.ACTION_Reverse_Correct;
				}
				options[index++] = DocumentEngine.ACTION_Reverse_Accrual;
			}
		}
		/********************
		 *  Invoice
		 */
		else if (AD_Table_ID == I_C_Invoice.Table_ID)
		{
			//	Complete                    ..  CO
			if (docStatus.equals(DocumentEngine.STATUS_Completed))
			{
				if (periodOpen) {
					options[index++] = DocumentEngine.ACTION_Reverse_Correct;
				}
				options[index++] = DocumentEngine.ACTION_Reverse_Accrual;
			}
		}
		/********************
		 *  Payment
		 */
		else if (AD_Table_ID == I_C_Payment.Table_ID)
		{
			//	Complete                    ..  CO
			if (docStatus.equals(DocumentEngine.STATUS_Completed))
			{
				if (periodOpen) {
					options[index++] = DocumentEngine.ACTION_Reverse_Correct;
				}
				options[index++] = DocumentEngine.ACTION_Reverse_Accrual;
			}
		}
		/********************
		 *  GL Journal
		 */
		else if (AD_Table_ID == I_GL_Journal.Table_ID || AD_Table_ID == I_GL_JournalBatch.Table_ID)
		{
			//	Complete                    ..  CO
			if (docStatus.equals(DocumentEngine.STATUS_Completed))
			{
				if (periodOpen) {
					options[index++] = DocumentEngine.ACTION_Reverse_Correct;
					options[index++] = DocumentEngine.ACTION_ReActivate;
				}
				options[index++] = DocumentEngine.ACTION_Reverse_Accrual;
			}
		}
		/********************
		 *  Allocation
		 */
		else if (AD_Table_ID == I_C_AllocationHdr.Table_ID)
		{
			//	Complete                    ..  CO
			if (docStatus.equals(DocumentEngine.STATUS_Completed))
			{
				if (periodOpen) {
					options[index++] = DocumentEngine.ACTION_Reverse_Correct;
				}
				options[index++] = DocumentEngine.ACTION_Reverse_Accrual;
			}
		}
		//[ 1782412 ]
		/********************
		 *  Cash
		 */
		else if (AD_Table_ID == I_C_Cash.Table_ID)
		{
			//	Complete                    ..  CO
			if (docStatus.equals(DocumentEngine.STATUS_Completed))
			{
				options[index++] = DocumentEngine.ACTION_Void;
			}
		}
		/********************
		 *  Bank Statement
		 */
		else if (AD_Table_ID == I_C_BankStatement.Table_ID)
		{
			//	Complete                    ..  CO
			if (docStatus.equals(DocumentEngine.STATUS_Completed))
			{
				if (periodOpen) {
					options[index++] = DocumentEngine.ACTION_Void;
				}
			}
		}
		/********************
		 *  Inventory Movement, Physical Inventory
		 */
		else if (AD_Table_ID == I_M_Movement.Table_ID
			|| AD_Table_ID == I_M_Inventory.Table_ID)
		{
			//	Complete                    ..  CO
			if (docStatus.equals(DocumentEngine.STATUS_Completed))
			{
				if (periodOpen) {
					options[index++] = DocumentEngine.ACTION_Reverse_Correct;
				}
				options[index++] = DocumentEngine.ACTION_Reverse_Accrual;
			}
		}
		/********************
		 *  Manufacturing Order
		 */
		else if (AD_Table_ID == I_PP_Order.Table_ID)
		{
			if (docStatus.equals(DocumentEngine.STATUS_Drafted)
					|| docStatus.equals(DocumentEngine.STATUS_InProgress)
					|| docStatus.equals(DocumentEngine.STATUS_Invalid))
				{
					options[index++] = DocumentEngine.ACTION_Prepare;
					options[index++] = DocumentEngine.ACTION_Close;
				}
				//	Complete                    ..  CO
				else if (docStatus.equals(DocumentEngine.STATUS_Completed))
				{
					options[index++] = DocumentEngine.ACTION_Void;
					options[index++] = DocumentEngine.ACTION_ReActivate;
				}
		}
		/********************
		 *  Production
		 */
		else if (AD_Table_ID == I_M_Production.Table_ID)
		{
			//	Draft                       ..  DR/IP/IN
			if (docStatus.equals(DocumentEngine.STATUS_Drafted)
					|| docStatus.equals(DocumentEngine.STATUS_InProgress)
					|| docStatus.equals(DocumentEngine.STATUS_Invalid))
			{
				options[index++] = DocumentEngine.ACTION_Prepare;
			}
			//	Complete                    ..  CO
			else if (docStatus.equals(DocumentEngine.STATUS_Completed))
			{
				if (periodOpen) {
					options[index++] = DocumentEngine.ACTION_Reverse_Correct;
				}
				options[index++] = DocumentEngine.ACTION_Reverse_Accrual;
			}

		}
		/********************
		 *  Manufacturing Cost Collector
		 */
		else if (AD_Table_ID == I_PP_Cost_Collector.Table_ID)
		{
			if (docStatus.equals(DocumentEngine.STATUS_Drafted)
					|| docStatus.equals(DocumentEngine.STATUS_InProgress)
					|| docStatus.equals(DocumentEngine.STATUS_Invalid))
				{
					options[index++] = DocumentEngine.ACTION_Prepare;
					options[index++] = DocumentEngine.ACTION_Close;
				}
				//	Complete                    ..  CO
				else if (docStatus.equals(DocumentEngine.STATUS_Completed))
				{
					options[index++] = DocumentEngine.ACTION_Void;
					options[index++] = DocumentEngine.ACTION_Reverse_Correct;
				}
		}
		/********************
		 *  Distribution Order
		 */
		else if (AD_Table_ID == I_DD_Order.Table_ID)
		{
			if (docStatus.equals(DocumentEngine.STATUS_Drafted)
					|| docStatus.equals(DocumentEngine.STATUS_InProgress)
					|| docStatus.equals(DocumentEngine.STATUS_Invalid))
				{
					options[index++] = DocumentEngine.ACTION_Prepare;
					options[index++] = DocumentEngine.ACTION_Close;
				}
				//	Complete                    ..  CO
				else if (docStatus.equals(DocumentEngine.STATUS_Completed))
				{
					options[index++] = DocumentEngine.ACTION_Void;
					options[index++] = DocumentEngine.ACTION_ReActivate;
				}
		}
		/********************
		 *  Payroll Process
		 */
		else if (AD_Table_ID == I_HR_Process.Table_ID)
		{
			if (docStatus.equals(DocumentEngine.STATUS_Drafted)
					|| docStatus.equals(DocumentEngine.STATUS_InProgress)
					|| docStatus.equals(DocumentEngine.STATUS_Invalid))
				{
					options[index++] = DocumentEngine.ACTION_Prepare;
					options[index++] = DocumentEngine.ACTION_Close;
				}
				//	Complete                    ..  CO
				else if (docStatus.equals(DocumentEngine.STATUS_Completed))
				{
					options[index++] = DocumentEngine.ACTION_Void;
					options[index++] = DocumentEngine.ACTION_ReActivate;
				}
		}
		/********************
		 *  RMA Process
		 */
		else if (AD_Table_ID == I_M_RMA.Table_ID)
		{
			if(docStatus.equals(DocumentEngine.STATUS_Completed))
			{
				// IDEMPIERE-98 - Implement void for completed RMAs - Diego Ruiz - globalqss
				options[index++] = DocumentEngine.ACTION_Void;
			}
		}

		if (po instanceof DocOptions)
			index = ((DocOptions) po).customizeValidActions(docStatus, processing, orderType, isSOTrx,
					AD_Table_ID, docAction, options, index);

		AtomicInteger indexObj = new AtomicInteger(index);
		ArrayList<String> docActionsArray = new ArrayList<String>(Arrays.asList(docAction));
		ArrayList<String> optionsArray = new ArrayList<String>(Arrays.asList(options));
		DocActionEventData eventData = new DocActionEventData(docStatus, processing, orderType, isSOTrx, AD_Table_ID, docActionsArray, optionsArray, indexObj, po);
		// TODO: DAP solve
		/*Event event = EventManager.newEvent(IEventTopics.DOCACTION,
				new EventProperty(EventManager.EVENT_DATA, eventData),
				new EventProperty("tableName", po.get_TableName()));
		EventManager.getInstance().sendEvent(event);*/
		index = indexObj.get();
		for (int i = 0; i < optionsArray.size(); i++)
			options[i] = optionsArray.get(i);
		for (int i = 0; i < docActionsArray.size(); i++)
			docAction[i] = docActionsArray.get(i);

		return index;
	}

	/**
	 * Fill Vector with DocAction Ref_List(135) values
	 * @param v_value
	 * @param v_name
	 * @param v_description
	 */
	public static void readReferenceList(ArrayList<String> v_value, ArrayList<String> v_name,
			ArrayList<String> v_description)
	{
		if (v_value == null)
			throw new IllegalArgumentException("v_value parameter is null");
		if (v_name == null)
			throw new IllegalArgumentException("v_name parameter is null");
		if (v_description == null)
			throw new IllegalArgumentException("v_description parameter is null");

		String sql;
		if (Env.isBaseLanguage(Env.getCtx(), "AD_Ref_List"))
			sql = "SELECT Value, Name, Description FROM AD_Ref_List "
				+ "WHERE AD_Reference_ID=? ORDER BY Name";
		else
			sql = "SELECT l.Value, t.Name, t.Description "
				+ "FROM AD_Ref_List l, AD_Ref_List_Trl t "
				+ "WHERE l.AD_Ref_List_ID=t.AD_Ref_List_ID"
				+ " AND t.AD_Language='" + Env.getAD_Language(Env.getCtx()) + "'"
				+ " AND l.AD_Reference_ID=? ORDER BY t.Name";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, DocAction.AD_REFERENCE_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				String value = rs.getString(1);
				String name = rs.getString(2);
				String description = rs.getString(3);
				if (description == null)
					description = "";
				//
				v_value.add(value);
				v_name.add(name);
				v_description.add(description);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
	}

	/**
	 * Checks the access rights of the given role/client for the given document actions.
	 * @param clientId
	 * @param roleId
	 * @param docTypeId
	 * @param options
	 * @param maxIndex
	 * @return number of valid actions in the String[] options
	 */
	public static int checkActionAccess(int clientId, int roleId, int docTypeId, String[] options, int maxIndex) {
		return MRole.get(Env.getCtx(), roleId).checkActionAccess(clientId, docTypeId, options, maxIndex);
	}

	/**
	 *  Post Immediate
	 *
	 *	@param	ctx Client Context
	 *  @param  AD_Client_ID    Client ID of Document
	 *  @param  AD_Table_ID     Table ID of Document
	 *  @param  Record_ID       Record ID of this document
	 *  @param  force           force posting
	 *  @param  trxName			ignore, retained for backward compatibility
	 *  @return null, if success or error message
	 */
	public static String postImmediate (Properties ctx,
		int AD_Client_ID, int AD_Table_ID, int Record_ID, boolean force, String trxName)
	{
		// Ensure the table has Posted column / i.e. GL_JournalBatch can be completed but not posted
		if (MColumn.getColumn_ID(MTable.getTableName(ctx, AD_Table_ID), "Posted") <= 0)
			return null;

		String error = null;
		if (log.isLoggable(Level.INFO)) log.info ("Table=" + AD_Table_ID + ", Record=" + Record_ID);
		MAcctSchema[] ass = MAcctSchema.getClientAcctSchema(ctx, AD_Client_ID);
		error = Doc.postImmediate(ass, AD_Table_ID, Record_ID, force, trxName);
		return error;
	}	//	postImmediate

	/**
	 * Process document.  This replaces DocAction.processIt().
	 * @param doc
	 * @param processAction 
	 * @return true if performed
	 */
	public static boolean processIt(DocAction doc, String processAction) {
		boolean success = false;
		
		DocumentEngine engine = new DocumentEngine(doc, doc.getDocStatus());
		success = engine.processIt(processAction, doc.getDocAction());

		return success;
	}
}	//	DocumentEnine
