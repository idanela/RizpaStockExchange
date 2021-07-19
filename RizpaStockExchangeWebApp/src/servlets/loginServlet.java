package servlets;

import constants.constants;
import stockExchangeEngine.StockExchangeEngine;
import user.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;

@WebServlet(name = "loginServlet", urlPatterns = "/login")
public class loginServlet  extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    processRequest(req,resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String userNameFromParameter = req.getParameter(constants.USERNAME).trim();
        String userNameFromSession = SessionUtils.getUsername(req);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        if(userNameFromSession == null)// user not logged in.
        {
            if(userNameFromParameter == null || userNameFromParameter.isEmpty())
            {
                req.setAttribute("error","User "+userNameFromParameter+ " already exists");
                getServletContext().getRequestDispatcher("/pages/loginWithError/loginError.html").forward(req,resp);
            }
            else
            {
                synchronized (this) {
                    if (userManager.isUserExists(userNameFromParameter)) {
                        req.setAttribute("error","User "+userNameFromParameter+ " already exists");
                        getServletContext().getRequestDispatcher("/pages/loginWithError/loginError.html").forward(req,resp);
                    }
                    else {
                        //add the new user to the users list
                        boolean isAdmin = !req.getParameter("userKind").equals("Trader");
                        userManager.addUser(userNameFromParameter,isAdmin,(StockExchangeEngine)getServletContext().getAttribute("engine"));
                        //set the username in a session so it will be available on each request
                        //the true parameter means that if a session object does not exists yet
                        //create a new one
                        req.getSession(true).setAttribute(constants.USERNAME, userNameFromParameter);

                        if(req.getParameter("userKind").equals("Trader"))
                        resp.sendRedirect(constants.USER_AND_STOCK_PAGE);
                        else
                            resp.sendRedirect(constants.USER_AND_STOCK_PAGE_ADMIN);

                    }
                }
            }
        }
        else
        {
            if(userNameFromParameter.equals(userNameFromSession))
           resp.sendRedirect(constants.USER_AND_STOCK_PAGE);
            else
            {
                req.setAttribute("error","User "+userNameFromParameter+ " already exists");
                getServletContext().getRequestDispatcher("/pages/loginWithError/loginError.html").forward(req,resp);
            }
        }
   }
}
