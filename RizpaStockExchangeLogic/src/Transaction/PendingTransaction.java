package Transaction;

import Stocks.Stock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public abstract class PendingTransaction implements ITransaction{

    Stock m_Stock;
    int m_Limit;
    int m_NumOfStocks;
    String m_DateOfTransaction;

    @Override
    public Stock getStock() {
        return m_Stock;
    }

    public PendingTransaction(Stock m_Stock, int m_Limit, int numOfStocks) {
        this.m_Stock = m_Stock;
        this.m_Limit = m_Limit;
        this.m_NumOfStocks = numOfStocks;
        this.m_DateOfTransaction = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
    }
    @Override
    public int getNumOfStocks() {
        return m_NumOfStocks;
    }

    @Override
    public int getLimit() {
        return m_Limit;
    }

    @Override
    public String getStockName() {
        return m_Stock.getStockName();
    }

    protected abstract boolean checkLimit(int limitToCompareTo);
    protected abstract boolean checkTypeToCompare(ITransaction transaction);

    public List<Transaction> findCounterTransaction(List<ITransaction> transactionsToScan) {
        List<Transaction> transactionsMade = new LinkedList<>();
        for(ITransaction transaction: transactionsToScan) {
                if(m_Stock.equals(transaction.getStock())) {
                    if(checkLimit(transaction.getLimit())) {
                    if (preformTransaction(transactionsToScan, transaction ,transactionsMade))
                        break;
                    }
            }
        }

        return transactionsMade;

}



    private boolean preformTransaction(List<ITransaction> i_transactionsToScan, ITransaction transaction, List<Transaction> transactionsMade)
    {
        boolean isFinished = false;
        int numOfTransactionStocks = 0;
        Transaction newTransaction;
        if(this.m_NumOfStocks >= transaction.getNumOfStocks())
        {
            numOfTransactionStocks = m_NumOfStocks - transaction.getNumOfStocks();
            newTransaction = new Transaction(numOfTransactionStocks,transaction.getLimit());
            this.m_NumOfStocks = numOfTransactionStocks;
        }
        else
        {
            newTransaction = new Transaction(transaction.getNumOfStocks(),transaction.getLimit());
        }
        transactionsMade.add(0,newTransaction);
        this.m_Stock.addTransactionToStocksTransactionsList(newTransaction);

        if(numOfTransactionStocks == 0)
        {
            isFinished = true;
        }

        return isFinished;
    }


}
