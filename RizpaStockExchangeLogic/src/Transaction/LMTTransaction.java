package Transaction;

import Stocks.Stock;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class LMTTransaction extends AllTransactionsKind {

    public LMTTransaction(Stock m_Stock, int m_Limit, int m_NumOfStocks) {
        super(m_Stock, m_Limit, m_NumOfStocks);
    }

    public LMTTransaction() {

    }

    protected abstract boolean checkLimit(ITransaction transaction);


    @Override
    public boolean doTransaction(List<TransactionMade> transactionsMade,List<ITransaction> transactionsToScan, List<ITransaction> toAdd, List<ITransaction> toRemove)
    {
        boolean isFinished = false;
        for(ITransaction transaction: transactionsToScan) {
            if(m_Stock.equals(transaction.getStock())) {
                if(checkLimit(transaction)) {
                    if(m_NumOfStocks >= transaction.getNumOfStocks())
                        toRemove.add(transaction);
                    if (isFinished = preformTransaction(transactionsToScan, transaction ,transactionsMade))
                        break;
                }
            }
        }
        return isFinished;
    }

    private boolean preformTransaction(List<ITransaction> i_transactionsToScan, ITransaction counterTransaction, List<TransactionMade> transactionsMade)
    {
        boolean isFinished = false;
        int numOfTransactionStocks = 0;
        TransactionMade newTransaction;
        if(this.m_NumOfStocks >= counterTransaction.getNumOfStocks()) //More stocks in this transaction
        {
            numOfTransactionStocks = counterTransaction.getNumOfStocks();
            newTransaction = new TransactionMade(m_Stock,numOfTransactionStocks,counterTransaction.getPriceOfStock());
            this.m_NumOfStocks =  m_NumOfStocks - numOfTransactionStocks;
            this.m_Limit = counterTransaction.getPriceOfStock();
        }
        else
        {
            newTransaction = new TransactionMade(m_Stock,m_NumOfStocks,counterTransaction.getPriceOfStock());
            counterTransaction.setNumOfStocks(counterTransaction.getNumOfStocks() - m_NumOfStocks);
            m_NumOfStocks = 0;
        }
        transactionsMade.add(0,newTransaction);
        this.m_Stock.addTransactionToStocksTransactionsList(newTransaction);

        if(m_NumOfStocks == 0)
        {
            isFinished = true;
        }

        return isFinished;
    }
}
