package stockExchangeEngine;

import stocks.Stock;
import transaction.AllTransactionsKinds;
import transaction.Transaction;
import transaction.TransactionMade;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public interface IStockEngine extends Serializable {

    public Map<String,Stock> getStocks();
    public boolean getXmlContent(String path, AtomicBoolean hasSameCompany, AtomicBoolean hasSameSymbol);
    public List<Transaction> getTransactionList();
    public List<AllTransactionsKinds> getPendingSellTransactions();
    public List<AllTransactionsKinds> getPendingBuyTransactions();
    public void addTransactionMade(TransactionMade transactionMade);
    public void addTransactionsMade(List<TransactionMade> transactionsMade);
}
