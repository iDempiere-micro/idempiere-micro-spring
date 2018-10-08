package org.idempiere.common.base;

import java.util.List;

/**
 * 
 * @author hengsin
 *
 * @param <T>
 */
public interface IServicesHolder<T> {
	
	/**
	 * 
	 * @return list of service instance. null if not available or no matching service found
	 */
	public List<T> getServices();

}