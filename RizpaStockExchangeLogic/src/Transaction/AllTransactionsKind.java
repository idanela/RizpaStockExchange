package Transaction;

import Stocks.Stock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public abstract class AllTransactionsKind  implements ITransaction{

    Stock m_Stock;
    int m_Limit;
    int m_NumOfStocks;
    String m_DateOfTransaction;

    public AllTransactionsKind(Stock stock, int limit, int numOfStocks) {
        this.m_Stock = stock;
        this.m_Limit = limit;
        this.m_NumOfStocks = numOfStocks;
        this.m_DateOfTransaction = new SimpleDateFormat("HH:mm:ss:SSS").format(new Date());
    }

    public AllTransactionsKind() {
    }

    public void setProperties (Stock stock, int limit, int numOfStocks) {
        this.m_Stock = stock;
        this.m_Limit = limit;
        this.m_NumOfStocks = numOfStocks;
        this.m_DateOfTransaction = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
    }

    @Override
    public void setNumOfStocks(int number) {
     m_NumOfStocks =number;
    }

    @Override
    public Stock getStock() {
        return m_Stock;
    }

    @Override
    public int getNumOfStocks() {
        return m_NumOfStocks;
    }

    @Override
    public String getDateOfTransaction() {
        return m_DateOfTransaction;
    }

    @Override
    public int getPriceOfStock() {
        return m_Limit;
    }

    @Override
    public int getTransactionWorth() {
        return m_Limit*m_NumOfStocks;
    }

    public abstract boolean doTransaction(List<TransactionMade> transactionsMade, List<ITransaction> transactionsToScan, List<ITransaction> toAdd, List<ITransaction> toRemove);

    @Override
    public List<TransactionMade> findCounterTransaction(List<ITransaction> transactionsToScan, List<ITransaction> toAdd) {
        List<TransactionMade> transactionsMade = new LinkedList<>();
        List<ITransaction> toRemove = new ArrayList<>();
        boolean isFinished = doTransaction(transactionsMade,transactionsToScan,toAdd,toRemove);
        transactionsToScan.removeAll(toRemove);
        if(!isFinished)
        {
            toAdd.add(0,this);
        }

        return transactionsMade;
    }
}
