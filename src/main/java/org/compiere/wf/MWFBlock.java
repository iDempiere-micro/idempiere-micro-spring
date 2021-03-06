package org.compiere.wf;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.I_AD_WF_Block;
import org.idempiere.common.util.CCache;

/**
 * Work Flow Commitment Block
 *
 * @author Jorg Janke
 * @version $Id: MWFBlock.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class MWFBlock extends X_AD_WF_Block {
  /** */
  private static final long serialVersionUID = -2084396539959122888L;

  /**
   * Get MWFBlock from Cache
   *
   * @param ctx context
   * @param AD_WF_Block_ID id
   * @return MWFBlock
   */
  public static MWFBlock get(Properties ctx, int AD_WF_Block_ID) {
    Integer key = new Integer(AD_WF_Block_ID);
    MWFBlock retValue = (MWFBlock) s_cache.get(key);
    if (retValue != null) return retValue;
    retValue = new MWFBlock(ctx, AD_WF_Block_ID, null);
    if (retValue.getId() != 0) s_cache.put(key, retValue);
    return retValue;
  } //	get

  /** Cache */
  private static CCache<Integer, MWFBlock> s_cache =
      new CCache<Integer, MWFBlock>(I_AD_WF_Block.Table_Name, 20);

  /**
   * Standard Constructor
   *
   * @param ctx context
   * @param AD_WF_Block_ID id
   * @param trxName transaction
   */
  public MWFBlock(Properties ctx, int AD_WF_Block_ID, String trxName) {
    super(ctx, AD_WF_Block_ID, trxName);
  } //	MWFBlock

  /**
   * Load Constructor
   *
   * @param ctx context
   * @param rs result set
   * @param trxName transaction
   */
  public MWFBlock(Properties ctx, ResultSet rs, String trxName) {
    super(ctx, rs, trxName);
  } //	MWFBlock
} //	MWFBlock
