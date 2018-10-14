package org.idempiere.common.base;

/**
 * This is a very simple factory for service locators
 * 
 * @author viola
 * 
 */
public abstract class Service {

	private static IServiceLocator theLocator;
	protected static Service instance;

	protected abstract IServiceLocator getLocator();
    private final static Object mutex = new Object();

	/**
	 * 
	 * @return service locator instance
	 */
	public static IServiceLocator locator() {
        synchronized (mutex) {
            if (theLocator != null) {
                return theLocator;
            } else if (instance != null) {
                theLocator = instance.getLocator();
                return theLocator;
            } else throw new IllegalStateException("Service Locator");
        }
	}
}