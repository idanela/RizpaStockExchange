package Transaction;

import Stocks.Stock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public abstract class MKTOrLMTTransaction extends AllTransactionsKind{

    public MKTOrLMTTransaction(Stock m_Stock, int m_Limit, int m_NumOfStocks) {
        super(m_Stock, m_Limit, m_NumOfStocks);
    }

    public MKTOrLMTTransaction() {

    }

    protected abstract boolean checkLimit(int limitToCompareTo);

    @Override
    public List<TransactionMade> findCounterTransaction(List<ITransaction> transactionsToScan, List<ITransaction> toAdd) {
        boolean isFinished = false;
        List<TransactionMade> transactionsMade = new LinkedList<>();
        List<ITransaction> toRemove = new ArrayList<>();
        for(ITransaction transaction: transactionsToScan) {
            if(m_Stock.equals(transaction.getStock())) {
                if(checkLimit(transaction.getPriceOfStock())) {
                    if(m_NumOfStocks >= transaction.getNumOfStocks())
                        toRemove.add(transaction);
                    if (isFinished = preformTransaction(transactionsToScan, transaction ,transactionsMade))
                        break;
                }
            }
        }
        transactionsToScan.removeAll(toRemove);
        if(!isFinished)
        {
            toAdd.add(0,this);
        }

        return transactionsMade;
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
            if(this instanceof MKTTransaction)
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
