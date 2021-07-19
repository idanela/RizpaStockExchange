package utils;

import stockExchangeEngine.StockExchangeEngine;
import user.UserManager;

import javax.servlet.ServletContext;

public class ServletUtils {
    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";

    private static final Object userManagerLock = new Object();

    public static UserManager getUserManager(ServletContext servletContext) {

        synchronized (userManagerLock) {
            if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
                  UserManager manager = new UserManager();
                  servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, manager);
                  servletContext.setAttribute("engine",new StockExchangeEngine());
            }
        }
        return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }
}
