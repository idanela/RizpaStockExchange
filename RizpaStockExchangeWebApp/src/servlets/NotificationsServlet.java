package servlets;

import DTOsToWrite.TransactionToAdd;
import com.google.gson.Gson;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name ="NotificationsServlet", urlPatterns = "/notifications")
public class NotificationsServlet extends HttpServlet {
    @Override
    protected synchronized void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String knownVersion = req.getParameter("version");
        int version = Integer.parseInt(knownVersion);
        StockExchangeEngine engine = (StockExchangeEngine) getServletContext().getAttribute("engine");
        User user = engine.getUsers().get(SessionUtils.getUsername(req));
        PrintWriter writer = resp.getWriter();
        List<Transaction> usersList = engine.getTransactionList().stream().
                filter(transaction -> transaction.getInitiator().equals(user) || transaction.getPartner().equals(user))
                .collect(Collectors.toList());
        List<Transaction> toWrite = usersList.subList(version,usersList.size());

        Gson gson = new Gson();
        String json = gson.toJson(new NotificationAndVersion(usersList.size(),toWrite,user));
        writer.println(json);
        writer.flush();
    }

    class NotificationAndVersion
    {
        int version;
        List<TransactionToAdd> transactions;

        public NotificationAndVersion(int version, List<Transaction> transactionsInSystem, User user) {
            this.version = version;
            this.transactions = new ArrayList<>();
            for(Transaction transaction:transactionsInSystem)
            {
                transactions.add(new TransactionToAdd(transaction,user));
            }
        }
    }
}
