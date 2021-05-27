package user;

import generated.RseHoldings;
import generated.RseItem;
import generated.RseUser;
import holding.Holding;
import stockExchangeEngine.StockExchangeEngine;
import transaction.Transaction;
import transaction.TransactionMade;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class User {
    private String name;
    Map<String,Holding> holdings = new HashMap<>();
    List<TransactionMade> transactionsMade = new LinkedList<>();
    List<Transaction> pendingBuyTransactions = new LinkedList<>();
    List<Transaction> pendingSellTransactions = new LinkedList<>();

    public String getName() {
        return name;
    }

    public Map<String, Holding> getHoldings() {
        return holdings;
    }

    public List<TransactionMade> getTransactionsMade() {
        return transactionsMade;
    }

    public List<Transaction> getPendingBuyTransactions() {
        return pendingBuyTransactions;
    }

    public List<Transaction> getPendingSellTransactions() {
        return pendingSellTransactions;
    }

    public User(String name, List<Holding> holding) {
        this.name = name;
        for (Holding hold:holding) {
            this.holdings.put(hold.getStock().getStockName(),hold);
        }
    }

    public User(RseUser user, StockExchangeEngine engine) {
        this.name = user.getName();
        for ( RseItem hold:user.getRseHoldings().getRseItem()) {
            this.holdings.put(hold.getSymbol(), new Holding(engine.getStock(hold.getSymbol()),hold.getQuantity()));
        }    }

    @Override
    public boolean equals(Object obj) {
        return name.equals(((User)obj).getName());
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTransactionsMade(List<TransactionMade> transactionsMade) {
        this.transactionsMade = transactionsMade;
    }

    public void setPendingBuyTransactions(List<Transaction> pendingBuyTransactions) {
        this.pendingBuyTransactions = pendingBuyTransactions;
    }

    public void setPendingSellTransactions(List<Transaction> pendingSellTransactions) {
        this.pendingSellTransactions = pendingSellTransactions;
    }

    public void addShares(List<TransactionMade> transactionsMade) {
        Holding holding;
        for (TransactionMade transaction: transactionsMade)
        {
             holding = holdings.get(transaction.getStock().getStockName());
             if(holding != null)
             {
                 holding.setAmountOfStocks(holding.getAmountOfStocks() + transaction.getAmountOfStocks());
                 holding.setTotalWorthOfStocks(holding.getTotalWorthOfStocks() + transaction.getTransactionWorth());
             }
             else
             {
                 holding = new Holding(transaction.getStock() ,transaction.getAmountOfStocks(),transaction.getPriceOfStock());
                 holdings.put(transaction.getStock().getStockName(),holding);
             }
        }
    }

    public void removeShares(List<TransactionMade> transactionsMade) {
        Holding holding;
        for (TransactionMade transaction: transactionsMade)
        {
            holding = holdings.get(transaction.getStock().getStockName());
            holding.setAmountOfStocks(holding.getAmountOfStocks() - transaction.getAmountOfStocks());
            holding.setTotalWorthOfStocks(holding.getTotalWorthOfStocks() - transaction.getTransactionWorth());
        }
    }

    public void addTransactions(List<TransactionMade> transactionsMade) {
        for(TransactionMade transaction:transactionsMade)
        {
            this.transactionsMade.add(transaction);
        }
    }
}
