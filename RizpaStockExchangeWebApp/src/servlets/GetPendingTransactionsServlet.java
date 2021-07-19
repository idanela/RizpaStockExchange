package servlets;

import com.google.gson.Gson;
import stockExchangeEngine.StockExchangeEngine;
import transaction.Transaction;

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

@WebServlet(name = "GetPendingTransactionsServlet",urlPatterns = "/pullPendingTransactions")
public class GetPendingTransactionsServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);
    }

    private synchronized void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StockExchangeEngine engine = (StockExchangeEngine)getServletContext().getAttribute("engine");
        String stockName = req.getParameter("symbol");
        List<Transaction> pendingSellTransactions = engine.getPendingSellTransactions();
        List<Transaction> pendingBuyTransactions = engine.getPendingBuyTransactions();
        List<PendingTransaction> transactions = createListOfPendingTransactions(pendingBuyTransactions,pendingSellTransactions,stockName);
        PrintWriter writer = resp.getWriter();
        Gson gson = new Gson();
        String json = gson.toJson(transactions);
        writer.println(json);
        writer.flush();
    }

    private List<PendingTransaction> createListOfPendingTransactions(List<Transaction> allPendingBuyTransactions, List<Transaction> allPendingSellTransactions,String stockName) {

        List<Transaction> pendingBuyTransactions = allPendingBuyTransactions.stream().
                filter(transaction -> transaction.getStock().getStockName().equals(stockName))
                .collect(Collectors.toList());
        List<Transaction> pendingSellTransactions = allPendingSellTransactions.stream().
                filter(transaction -> transaction.getStock().getStockName().equals(stockName))
                .collect(Collectors.toList());
        List<PendingTransaction> transactions = new ArrayList<>();
        for(Transaction buyTransaction:pendingBuyTransactions)
        {
            transactions.add(
                    new PendingTransaction("Buy",buyTransaction.getTransactionKind(),
                    buyTransaction.getStock().getStockName(),buyTransaction.getAmountOfStocks(),
                            buyTransaction.getPriceOfStock(),buyTransaction.getInitiator().getName()));
        }
        for(Transaction sellTransaction:pendingSellTransactions)
        {
            transactions.add(
                    new PendingTransaction("Sell",sellTransaction.getTransactionKind(),
                    sellTransaction.getStock().getStockName(),sellTransaction.getAmountOfStocks(),
                            sellTransaction.getPriceOfStock(),sellTransaction.getInitiator().getName()));
        }

        return transactions;

    }

    class PendingTransaction
    {
            String instruction;
            String kind;
            String symbol;
            int amount;
            int price;
            String initiator;

        public PendingTransaction(String instruction, String kind, String symbol, int amount, int price, String initiator) {
            this.instruction = instruction;
            this.kind = kind;
            this.symbol = symbol;
            this.amount = amount;
            this.price = price;
            this.initiator = initiator;
        }
    }
}
