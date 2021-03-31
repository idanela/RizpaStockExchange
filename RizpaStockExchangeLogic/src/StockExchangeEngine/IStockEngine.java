package StockExchangeEngine;

import Stocks.Stock;
import Transaction.ITransaction;
import Transaction.TransactionMade;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public interface IStockEngine extends Serializable {

    public Map<String,Stock> getStocks();
    public boolean getXmlContent(String path, AtomicBoolean hasSameCompany, AtomicBoolean hasSameSymbol);
    //public loadStocks();
    public List<ITransaction> getTransactionList();
    public List<ITransaction> getPendingSellTransactions();
    public List<ITransaction> getPendingBuyTransactions();

    void addTransactionMade(TransactionMade transactionMade);

    void addTransactionsMade(List<TransactionMade> transactionsMade);

    //public boolean findTransaction(String stockName, double limit, int amountForTransaction);
/*    public void addPendingSell(Stock stock);
    public void addPendingPurchase(Stock stock);
    public void preformTransaction(ITransaction toSell,ITransaction toBuy);*/
}
