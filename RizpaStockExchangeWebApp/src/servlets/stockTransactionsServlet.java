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

@WebServlet(name = "stockTransactionsServlet" , urlPatterns = "/transactions")
public class stockTransactionsServlet extends HttpServlet {
    @Override
    protected synchronized void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);

    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StockExchangeEngine engine = (StockExchangeEngine) getServletContext().getAttribute("engine");
        String version = req.getParameter("version");
        String symbol = req.getParameter("symbol");
        User user = engine.getUsers().get(SessionUtils.getUsername(req));
        int transactionVersion = Integer.parseInt(version);
        List<Transaction> transactionsMade =  engine.getTransactionList().stream().filter(transaction -> transaction.getStock().getStockName().equals(symbol)).collect(Collectors.toList());
        int currentVersion = transactionsMade.size();
        TransactionsAndVersion tav = new TransactionsAndVersion(transactionsMade,transactionVersion,currentVersion,user);
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(tav);
        try (PrintWriter out = resp.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
    }

    class TransactionsAndVersion
    {
        List<TransactionToAdd> transactions;
        int version;

        public TransactionsAndVersion(List<Transaction> transactionsInSystem, int previousVersion,int version,User user) {
            transactions = new ArrayList<>();

            for(Transaction transaction:transactionsInSystem)
            {
                this.transactions.add(new TransactionToAdd(transaction, user));
            }

            this.transactions = transactions.subList(previousVersion,version);

            this.version = version;
        }
    }
}
