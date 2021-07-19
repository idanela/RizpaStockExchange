package user;
import Notification.Notification;
import accountMovments.AccountMovement;
import accountMovments.AccountMovements;
import generated.RseItem;
import generated.RseUser;
import holding.Holding;
import stockExchangeEngine.StockExchangeEngine;
import stocks.Stock;
import transaction.Transaction;
import transaction.TransactionMade;

import java.util.*;

public class  User {
    private String name;
    private int balance;
    Map<String,Holding> holdings = new HashMap<>();
    List<TransactionMade> transactionsMade = new LinkedList<>();
    List<Transaction> pendingBuyTransactions = new LinkedList<>();
    List<Transaction> pendingSellTransactions = new LinkedList<>();
    List<Notification> notifications = new ArrayList<>();
    boolean isAdmin;
    AccountMovements movements = new AccountMovements();

    public User(String name, List<Holding> holding) {
        this.name = name;
        for (Holding hold:holding) {
            this.holdings.put(hold.getStock().getStockName(),hold);
        }

        this.balance = 0;
    }

    public User(RseUser user, StockExchangeEngine engine) {
        this.name = user.getName();
        for ( RseItem hold:user.getRseHoldings().getRseItem()) {
            this.holdings.put(hold.getSymbol(), new Holding(engine.getStock(hold.getSymbol()),hold.getQuantity()));
        }

        this.balance = 0;
    }

    public User(String name,boolean isAdmin) {
        this.name = name;
        this.balance = 0;
        this.isAdmin = isAdmin;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }
    public void addNotification(Notification notification)
    {
        this.notifications.add(notification);
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public AccountMovements getMovements() {
        return movements;
    }
    public void addMovement(String kind, String symbol, int balanceBefore, int balanceAfter)
    {
        this.movements.addMovement(new AccountMovement(kind,symbol,balanceBefore,balanceAfter));
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public int getBalance() {
        return balance;
    }

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


    @Override
    public boolean equals(Object obj) {
        return name.equals(((User)obj).getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, holdings, transactionsMade, pendingBuyTransactions, pendingSellTransactions);
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

    public void addHolding(Holding holding)
    {
        this.holdings.put(holding.getStock().getStockName(),holding);
    }
    public void addShares(List<TransactionMade> transactionsMade) {
        Holding holding;
        for (TransactionMade transaction: transactionsMade)
        {
             holding = holdings.get(transaction.getStock().getStockName());
             if(holding != null) {
                 holding.setPrice(transactionsMade.get(transactionsMade.size() - 1).getPriceOfStock());
                 holding.setAmountOfStocks(holding.getAmountOfStocks() + transaction.getAmountOfStocks());
                 holding.setAmountFreeToUse(holding.getAmountFreeToUse()+ transaction.getAmountOfStocks());
                 holding.setTotalWorthOfStocks(holding.getPrice() * holding.getAmountOfStocks());
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
            holding.setTotalWorthOfStocks(holding.getAmountOfStocks()*transactionsMade.get(transactionsMade.size()-1).getPriceOfStock());
            if(holding.getAmountOfStocks()==0)
                holdings.remove(holding.getStock().getStockName());
        }
    }

    public void addTransactions(List<TransactionMade> transactionsMade) {
        for(TransactionMade transaction:transactionsMade)
        {
            this.transactionsMade.add(transaction);
        }
    }

    public void updateStocksPrice(List<TransactionMade> transactionsMade) {
        Holding holding;
        for(TransactionMade transactionMade: transactionsMade)
        {
            holding = holdings.get(transactionMade.getStock().getStockName());

            if(holding!=null)
            {
                holding.setPrice(transactionsMade.get(transactionsMade.size() - 1).getPriceOfStock());
                holding.setTotalWorthOfStocks(holding.getPrice()*holding.getAmountOfStocks());
            }
        }
    }

    public void updateFreeStocks(List<TransactionMade> userTransactions) {
        for(TransactionMade transactionMade:transactionsMade)
        {
            this.getHoldings().get(userTransactions.get(0).getStock().getStockName())
                    .setAmountFreeToUse(
                            this.getHoldings().get(userTransactions.get(0).getStock().getStockName())
                                    .getAmountFreeToUse() - transactionMade.getAmountOfStocks());
        }
    }

    public void addHoldings(autoGeneratedWeb.RseItem item, Stock stock) {
        Holding holding = new Holding(stock, item.getQuantity());
        this.holdings.put(item.getSymbol(), holding);
    }
}

