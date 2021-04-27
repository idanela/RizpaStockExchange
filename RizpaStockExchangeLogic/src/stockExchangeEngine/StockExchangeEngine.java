package stockExchangeEngine;

import autoGenerated.RizpaStockExchangeDescriptor;
import autoGenerated.RseStock;
import stocks.Stock;
import transaction.*;
import xmlRizpaDataExtractor.XmlRizpaDataExtractor;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class StockExchangeEngine implements IStockEngine
{
    private Map<String,Stock> m_AllStocks;
    private List<ITransaction> m_PendingBuyTransactions;
    private List<ITransaction> m_PendingSellTransactions;
    private List<ITransaction> m_Transactions;

    public StockExchangeEngine() {
        this.m_AllStocks = null;
        this.m_Transactions = new LinkedList<>();
        this.m_PendingBuyTransactions = new LinkedList<>();
        this.m_PendingSellTransactions = new LinkedList<>();
    }

    @Override
    public List<ITransaction> getPendingSellTransactions() {
        return m_PendingSellTransactions;
    }

    @Override
    public List<ITransaction> getPendingBuyTransactions() {
        return m_PendingBuyTransactions;
    }

    @Override
    public Map<String,Stock> getStocks() {
        return this.m_AllStocks;
    }

    @Override
    public List<ITransaction> getTransactionList() {
        return m_Transactions;
    }

    @Override
    public void addTransactionsMade(List<TransactionMade> transactionsMade) {
        for (TransactionMade transactionMade:transactionsMade)
        {
            addTransactionMade(transactionMade);
        }
    }

    @Override
    public void addTransactionMade(TransactionMade transactionMade)
    {
        transactionMade.getStock().setNumOfTransaction(transactionMade.getStock().getNumOfTransaction()+1);
        m_Transactions.add(transactionMade);
    }

    @Override
    public boolean getXmlContent(String path, AtomicBoolean hasSameCompany, AtomicBoolean hasSameName) {
        boolean xmlContentLoaded = true;
        RizpaStockExchangeDescriptor descriptor = XmlRizpaDataExtractor.getStocks(path,hasSameCompany,hasSameName);
        if(descriptor == null)
        {
            xmlContentLoaded = false;
        }
        else
        {
            loadClasses(descriptor);
        }

        return xmlContentLoaded;
    }

    private void loadClasses(RizpaStockExchangeDescriptor descriptor)
    {
       List<RseStock> stocks = descriptor.getRseStocks().getRseStock();
       m_AllStocks = new HashMap<>();
       for (RseStock stock : stocks)
       {
            m_AllStocks.put(stock.getRseSymbol(),new Stock(stock));
       }
    }

}
