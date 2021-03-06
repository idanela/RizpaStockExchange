package transaction;

import stocks.Stock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public abstract class AllTransactionsKinds implements ITransaction{


    Stock m_Stock;
    int m_Limit;
    int m_NumOfStocks;
    String m_DateOfTransaction;

    public AllTransactionsKinds(Stock stock, int limit, int numOfStocks) {
        this.m_Stock = stock;
        this.m_Limit = limit;
        this.m_NumOfStocks = numOfStocks;
        this.m_DateOfTransaction = new SimpleDateFormat("HH:mm:ss:SSS").format(new Date());
    }

    public AllTransactionsKinds() {
    }

    public void setProperties(Stock stock, int limit, int numOfStocks) {
        this.m_Stock = stock;
        this.m_Limit = limit;
        this.m_NumOfStocks = numOfStocks;
        this.m_DateOfTransaction = new SimpleDateFormat("HH:mm:ss:SSS").format(new Date());;
    }

    @Override
    public void setNumOfStocks(int number) {
        m_NumOfStocks = number;
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
        return m_Limit * m_NumOfStocks;
    }

    protected abstract List<ITransaction> sortAndFilterTransaction(List<ITransaction> transactionsToScan, Stock m_Stock);

    @Override
    public List<TransactionMade> findCounterTransaction(List<ITransaction> transactionsToScan, List<ITransaction> toAdd) {
        boolean isFinished = false;
        List<TransactionMade> transactionsMade = new LinkedList<>();
        List<ITransaction> toRemove = new ArrayList<>();
        List<ITransaction> toScanSorted = sortAndFilterTransaction(transactionsToScan,m_Stock);
        for(ITransaction transaction: toScanSorted) {
                    if(m_NumOfStocks >= transaction.getNumOfStocks())
                        toRemove.add(transaction);
                    if (isFinished = preformTransaction(transactionsToScan, transaction ,transactionsMade))
                        break;
        }
        transactionsToScan.removeAll(toRemove);
        if(!isFinished)
        {
            AddTransactionToList(toAdd);
        }

        return transactionsMade;
    }

    private void AddTransactionToList(List<ITransaction> toAdd)
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

    protected abstract boolean compareTransactionPrice(ITransaction transaction);


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
           if(this instanceof MKTBuyTransaction)
                this.m_Limit = counterTransaction.getPriceOfStock();
        }
        else
        {
            newTransaction = new TransactionMade(m_Stock,m_NumOfStocks,counterTransaction.getPriceOfStock());
            counterTransaction.setNumOfStocks(counterTransaction.getNumOfStocks() - m_NumOfStocks);
            m_NumOfStocks = 0;
        }
        transactionsMade.add(newTransaction);
        this.m_Stock.addTransactionToStocksTransactionsList(newTransaction);

        if(this instanceof MKTTransaction)
            this.m_Limit = newTransaction.getPriceOfStock();
        if(m_NumOfStocks == 0)
            isFinished = true;

        return isFinished;
    }



}
