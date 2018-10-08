package org.idempiere.common.base;

/**
 * 
 * @author hengsin
 *
 * @param <T>
 */
public interface IServiceHolder<T> {

	/**
	 * 
	 * @return service instance. null if not available or no matching service found
	 */
	public T getService();
	
}