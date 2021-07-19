package transaction;

import Notification.Notification;
import stocks.Stock;
import user.User;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AllTransactionsKinds implements Transaction {

    User initiator;
    Stock stock;
    int limit;
    int amountOfStocks;
    String dateOfTransaction;
    String transactionKind;

    public AllTransactionsKinds(Stock stock, int limit, int amountOfStocks, User initiator, String kind) {
        this.stock = stock;
        this.limit = limit;
        this.amountOfStocks = amountOfStocks;
        this.dateOfTransaction = new SimpleDateFormat("HH:mm:ss:SSS").format(new Date());
        this.initiator = initiator;
        this.transactionKind = kind;
    }

    public AllTransactionsKinds() {
    }

    public int getLimit() {
        return limit;
    }

    public String getKind() {
        return transactionKind;
    }

    public void setProperties(Stock stock, int limit, int numOfStocks, User user, String kind) {
        this.stock = stock;
        this.limit = limit;
        this.amountOfStocks = numOfStocks;
        this.dateOfTransaction = new SimpleDateFormat("HH:mm:ss:SSS").format(new Date());
        this.initiator = user;
        this.transactionKind = kind;
    }

    @Override
    public void setAmountOfStocks(int number) {
        amountOfStocks = number;
    }

    @Override
    public User getInitiator() {
        return this.initiator;
    }
    @Override
    public Stock getStock() {
        return stock;
    }

    @Override
    public int getAmountOfStocks() {
        return amountOfStocks;
    }

    @Override
    public String getDateOfTransaction() {
        return dateOfTransaction;
    }

    @Override
    public int getPriceOfStock() {
        return limit;
    }

    @Override
    public int getTransactionWorth() {
        return limit * amountOfStocks;
    }

    @Override
    public String getUserName() {
        return initiator.getName();
    }

    @Override
    public String getTransactionKind() {
        return transactionKind;
    }

    protected abstract List<Transaction> sortAndFilterTransaction(List<Transaction> transactionsToScan, Stock m_Stock);

    @Override
    public List<TransactionMade> findCounterTransaction(List<Transaction> transactionsToScan, List<Transaction> toAdd,boolean isBuyOperation) {

        if(!isBuyOperation && !(this instanceof FOKSellTransaction) && !(this instanceof IOCSellTransaction))
        {
            int oldAmount = initiator.getHoldings().get(this.stock.getStockName()).getAmountFreeToUse();
            initiator.getHoldings().get(this.stock.getStockName()).setAmountFreeToUse(oldAmount - this.amountOfStocks);
        }
        boolean isFinished = false;
        List<TransactionMade> transactionsMade = new LinkedList<>();
        List<Transaction> toRemove = new ArrayList<>();
        List<Transaction> toScanSorted = sortAndFilterTransaction(transactionsToScan, stock)
                .stream().filter(transaction -> !transaction.getInitiator().equals(this.getInitiator()))
                        .collect(Collectors.toList());
        for(Transaction transaction: toScanSorted) {
                    if(amountOfStocks >= transaction.getAmountOfStocks())
                        toRemove.add(transaction);
                    if (isFinished = preformTransaction(transactionsToScan, transaction ,transactionsMade,isBuyOperation))
                        break;
        }
        transactionsToScan.removeAll(toRemove);
        updateUsersShares(transactionsMade);
        if(!isFinished)
        {
            if(!(this instanceof TransactionToDispose))
                AddTransactionToList(toAdd);
        }

        return transactionsMade;
    }

    private void updateUsersShares(List<TransactionMade> transactionsMade) {
        User buyer;
        User seller;
        List<TransactionMade> tm = new ArrayList<>();
        // todo: find more delicate way to update holdings.
        for(TransactionMade transactionMade:transactionsMade)
        {
            tm.clear();
            tm.add(transactionMade);
            buyer = transactionMade.getBuyer();
            seller = transactionMade.getSeller();
            seller.removeShares(tm);
            buyer.addShares(tm);
        }
    }

    private void AddTransactionToList(List<Transaction> toAdd)
    {
        for (int i = 0; i <toAdd.size() ; i++) {
            if(compareTransactionPrice(toAdd.get(i)))
            {
                toAdd.add(i,this);
                return;
            }
        }

        toAdd.add(this);
    }

    protected abstract boolean compareTransactionPrice(Transaction transaction);

    private boolean preformTransaction(List<Transaction> transactionsToScan, Transaction counterTransaction, List<TransactionMade> transactionsMade,boolean isBuyOperation)
    {
        boolean isFinished = false;
        int numOfTransactionStocks = 0;
        TransactionMade newTransaction;
        if(this.amountOfStocks >= counterTransaction.getAmountOfStocks()) //More stocks in this transaction
        {
            numOfTransactionStocks = counterTransaction.getAmountOfStocks();
            newTransaction = new TransactionMade(stock,numOfTransactionStocks,counterTransaction.getPriceOfStock(),initiator,counterTransaction.getInitiator(), transactionKind,isBuyOperation);
            this.amountOfStocks =  amountOfStocks - numOfTransactionStocks;
            if(this instanceof MKTBuyTransaction)
                this.limit = counterTransaction.getPriceOfStock();
        }
        else
        {
            newTransaction = new TransactionMade(stock, amountOfStocks,counterTransaction.getPriceOfStock(),initiator,counterTransaction.getInitiator(), transactionKind,isBuyOperation);
            counterTransaction.setAmountOfStocks(counterTransaction.getAmountOfStocks() - amountOfStocks);
            amountOfStocks = 0;
        }
        addNotifications(newTransaction);
        this.stock.setCurrentPrice(counterTransaction.getPriceOfStock());
        transactionsMade.add(newTransaction);
        this.stock.addTransactionToStocksTransactionsList(newTransaction);

        if(this instanceof MKTTransaction)
            this.limit = newTransaction.getPriceOfStock();
        if(amountOfStocks == 0)
            isFinished = true;

        updateBalanceAndAddMovements(newTransaction);

        return isFinished;
    }

    private void addNotifications(TransactionMade newTransaction) {
        User seller = newTransaction.getSeller();
        User buyer = newTransaction.getBuyer();
        String name = newTransaction.getStock().getStockName();
        seller.addNotification(new Notification(false,name,newTransaction.getPriceOfStock(),newTransaction.getAmountOfStocks(), newTransaction.getBuyerName()));
        buyer.addNotification(new Notification(true,name,newTransaction.getPriceOfStock(),newTransaction.getAmountOfStocks(),newTransaction.getSellerName()));
    }

    private void updateBalanceAndAddMovements(TransactionMade transactionMade)
    {
        User buyer = transactionMade.getBuyer();
        User seller = transactionMade.getSeller();
        int sellerBalanceBefore =seller.getBalance();
        int buyerBalance = buyer.getBalance();
        int amount = transactionMade.getAmountOfStocks();
        int differenceInBalance = transactionMade.getPriceOfStock() * amount;
        buyer.setBalance(buyerBalance - differenceInBalance);
        seller.setBalance(seller.getBalance() + differenceInBalance);
        seller.addMovement("Sell Stock",transactionMade.getStock().getStockName(),sellerBalanceBefore,seller.getBalance());
        buyer.addMovement("Buy Stock",transactionMade.getStock().getStockName(),buyerBalance,buyer.getBalance());
    }
}
