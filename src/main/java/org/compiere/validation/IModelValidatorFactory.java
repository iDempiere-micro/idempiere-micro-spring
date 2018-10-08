package org.compiere.validation;

import org.compiere.validation.ModelValidator;

/**
 * 
 * @author hengsin
 *
 */
public interface IModelValidatorFactory {
	
	/**
	 * @param className
	 * @return new modelvalidator intance
	 */
	public ModelValidator newModelValidatorInstance(String className);
}