package servlets;

import stockExchangeEngine.StockExchangeEngine;
import user.User;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "setSecondPageNotificationsVersionServlet",urlPatterns = "/notificationsSecondScreen")
public class setSecondPageNotificationsVersionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        synchronized (getServletContext())
        {
            StockExchangeEngine engine = (StockExchangeEngine) getServletContext().getAttribute("engine");
            User user = engine.getUsers().get(SessionUtils.getUsername(req));
            PrintWriter writer = resp.getWriter();
            writer.print(user.getNotifications().size());
        }
    }
}
