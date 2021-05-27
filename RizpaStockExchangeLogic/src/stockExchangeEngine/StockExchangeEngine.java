package stockExchangeEngine;

import generated.RizpaStockExchangeDescriptor;
import generated.RseStock;
import generated.RseUser;
import generated.RseUsers;
import stocks.Stock;
import transaction.*;
import user.User;
import xmlRizpaDataExtractor.XmlRizpaDataExtractor;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class StockExchangeEngine implements IStockEngine
{
    Map<String, User> users;
    private Map<String,Stock> m_AllStocks;
    private List<Transaction> m_PendingBuyTransactions;
    private List<Transaction> m_PendingSellTransactions;
    private List<Transaction> m_Transactions;

    public Map<String, User> getUsers() {
        return users;
    }

    public StockExchangeEngine() {
        this.users =null;
        this.m_AllStocks = null;
        this.m_Transactions = new LinkedList<>();
        this.m_PendingBuyTransactions = new LinkedList<>();
        this.m_PendingSellTransactions = new LinkedList<>();
    }

    @Override
    public List<Transaction> getPendingSellTransactions() {
        return m_PendingSellTransactions;
    }

    @Override
    public List<Transaction> getPendingBuyTransactions() {
        return m_PendingBuyTransactions;
    }

    @Override
    public Map<String,Stock> getStocks() {
        return this.m_AllStocks;
    }

    public Stock getStock(String symbol)
    {
        Stock returnedStock = null;
        for (Stock stock : m_AllStocks.values()) {
            if(stock.getStockName().equals(symbol))
            {
                returnedStock =stock;
                break;
            }
        }
            return returnedStock;
    }

    @Override
    public List<Transaction> getTransactionList() {
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
    public boolean getXmlContent(String path, AtomicBoolean hasSameCompany, AtomicBoolean hasSameName,AtomicBoolean hasSameUser,AtomicBoolean hasInValidStock) {
        boolean xmlContentLoaded = true;
        RizpaStockExchangeDescriptor descriptor = XmlRizpaDataExtractor.getStocks(path,hasSameCompany,hasSameName,hasSameUser,hasInValidStock);
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

       List<RseUser> generatedUsers = descriptor.getRseUsers().getRseUser();
       users = new HashMap<>();
       for (RseUser user :generatedUsers)
       {
           this.users.put(user.getName(),new User(user,this));
       }

    }

}
