package org.idempiere.common.util;

import java.util.Properties;

import org.idempiere.common.util.ContextProvider;

/**
 *
 * @author Low Heng Sin
 *
 */
public class ServerContextProvider implements ContextProvider {

	private final static Properties context = new ServerContextPropertiesWrapper();

	public final static ServerContextProvider INSTANCE = new ServerContextProvider();

	private ServerContextProvider() {}

	/**
	 * Get server context proxy
	 */
	public Properties getContext() {
		return context;
	}

	/**
	 * Show url at zk desktop
	 */
	public void showURL(String url) {
		ServerContextURLHandler handler = (ServerContextURLHandler) getContext().get(ServerContextURLHandler.SERVER_CONTEXT_URL_HANDLER);
		if (handler != null)
			handler.showURL(url);
	}
}