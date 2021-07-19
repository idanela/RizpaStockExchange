package servlets;

import stockExchangeEngine.StockExchangeEngine;
import transaction.Transaction;
import user.User;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "SetNotificationsVersionServlet",urlPatterns = "/setNotificationsVersion")
public class SetNotificationsVersionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        synchronized (getServletContext()) {
            StockExchangeEngine engine = (StockExchangeEngine) getServletContext().getAttribute("engine");
            User user = engine.getUsers().get(SessionUtils.getUsername(req));
            PrintWriter writer = resp.getWriter();
            List<Transaction> transactions = engine.getTransactionList()
                    .stream()
                    .filter(transaction -> transaction.getInitiator().equals(user) || transaction.getPartner().equals(user))
                    .collect(Collectors.toList());
            writer.print(transactions.size());
        }
    }
}
