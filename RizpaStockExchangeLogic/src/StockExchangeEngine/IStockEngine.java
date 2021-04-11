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
    public List<ITransaction> getTransactionList();
    public List<ITransaction> getPendingSellTransactions();
    public List<ITransaction> getPendingBuyTransactions();
    public void addTransactionMade(TransactionMade transactionMade);
    public void addTransactionsMade(List<TransactionMade> transactionsMade);
}
