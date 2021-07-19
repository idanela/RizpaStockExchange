package servlets;

import com.google.gson.Gson;
import stockExchangeEngine.StockExchangeEngine;
import transaction.*;
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

@WebServlet(name = "preformTransactionServlet",urlPatterns = "/preformTransaction")
public class preformTransactionServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        synchronized (getServletContext()) {
            StockExchangeEngine engine = (StockExchangeEngine) getServletContext().getAttribute("engine");
            User user = engine.getUsers().get(SessionUtils.getUsername(req));
            String stockName = req.getParameter("symbol");
            String amount = req.getParameter("amount");
            String typeOfAction = req.getParameter("typeOfAction");
            String instruction = req.getParameter("instruction");
            int limit = Integer.parseInt(req.getParameter("limit"));
            int numOfStocks = Integer.parseInt(amount);
            List<TransactionMade> transactionsMade;
            AllTransactionsKinds transactionToUse = getTransactionToUse(typeOfAction, instruction);
            transactionToUse.setProperties(engine.getStock(stockName), limit, numOfStocks, user, instruction);
            if (typeOfAction.equals("Buy"))
                transactionsMade = transactionToUse.findCounterTransaction(engine.getPendingSellTransactions(), engine.getPendingBuyTransactions(), true);
            else
                transactionsMade = transactionToUse.findCounterTransaction(engine.getPendingBuyTransactions(), engine.getPendingSellTransactions(), false);

            engine.addTransactionsMade(transactionsMade);
            sendResponse(transactionsMade, numOfStocks, resp,user,typeOfAction.equals("Buy"));
        }

    }

    private void sendResponse(List<TransactionMade> transactionsMade, int numOfStocks, HttpServletResponse resp, User user, boolean isBought) throws IOException {
        int numOfExecuted = 0;
        for (TransactionMade transactionMade : transactionsMade) {
            numOfExecuted += transactionMade.getAmountOfStocks();
        }
        int numberOfResidualStocks = numOfStocks - numOfExecuted;
        TransactionsDetails transactionDetails;

        transactionDetails = new TransactionsDetails(numberOfResidualStocks,numOfExecuted,isBought);

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(transactionDetails);
        try (PrintWriter out = resp.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
    }

/*    private void addMovements(List<TransactionMade> transactionsMade, StockExchangeEngine engine) {
        for (TransactionMade transactionMade : transactionsMade) {
            User buyer = engine.getUsers().get(transactionMade.getBuyerName());
            User seller = engine.getUsers().get(transactionMade.getSellerName());
            int buyerBalanceBefore = buyer.getBalance() + (transactionMade.getAmountOfStocks() * transactionMade.getPriceOfStock());
            int sellerBalanceBefore = seller.getBalance() - (transactionMade.getAmountOfStocks() * transactionMade.getPriceOfStock());
            buyer.getMovements().addMovement(new AccountMovement("Buy stocks", transactionMade.getStock().getStockName(), buyerBalanceBefore, buyer.getBalance()));
            seller.getMovements().addMovement(new AccountMovement("Sell stocks", transactionMade.getStock().getStockName(), sellerBalanceBefore, seller.getBalance()));
        }
    }*/

    private AllTransactionsKinds getTransactionToUse(String typeOfAction, String instruction) {
        switch (typeOfAction) {
            case "Buy":
                switch (instruction) {
                    case "LMT":
                        return new LMTBuyTransaction();
                    case "MKT":
                        return new MKTBuyTransaction();
                    case "IOC":
                        return new IOCBuyTransaction();
                    case "FOK":
                        return new FOKBuyTransaction();
                }
            case "Sell":
                switch (instruction) {
                    case "LMT":
                        return new LMTSellTransaction();
                    case "MKT":
                        return new MKTSellTransaction();
                    case "IOC":
                        return new IOCSellTransaction();
                    case "FOK":
                        return new FOKSellTransaction();
                }
        }
        return null;
    }

    class TransactionsDetails
    {
        int residual;
        int numOfExecuted;
        boolean isBought;

        public TransactionsDetails(int residual, int numOfExecuted, boolean isBought) {
            this.residual = residual;
            this.numOfExecuted = numOfExecuted;
            this.isBought = isBought;
        }
    }
}
