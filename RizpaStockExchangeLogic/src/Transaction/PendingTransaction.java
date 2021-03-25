package Transaction;

import Stocks.Stock;

import java.text.SimpleDateFormat;
import java.util.Date;
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

    @Override
    public boolean findCounterTransaction(List<ITransaction> i_Transactions) {

        boolean isFinished = false;
        for(ITransaction transaction: i_Transactions) {
            if(checkTypeToCompare(transaction)) {
                if(m_Stock.equals(transaction.getStock())) {
                    if(checkLimit(m_Limit,transaction.getLimit())) {
                        isFinished = preformTransaction(i_Transactions, transaction);
                    }

                    if(isFinished)
                        break;
                }

            }
        }



        return isFinished;
    }

    protected abstract boolean checkLimit(double limit, double limitToCompareTo);
    protected abstract boolean checkTypeToCompare(ITransaction transaction);

    private boolean preformTransaction(List<ITransaction> i_transactions, ITransaction transaction)
    {
        boolean isFinishedBuy = false;
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
            newTransaction= new Transaction(transaction.getNumOfStocks(),transaction.getLimit());
        }

        addToTransactionList(i_transactions);
        this.m_Stock.addTransactionToStocksTransactionsList(newTransaction);

        if(numOfTransactionStocks == 0)
        {
            isFinishedBuy = true;
        }

        return isFinishedBuy;
    }

    private void addToTransactionList(List<ITransaction> i_transactions)
    {
        i_transactions.add(0,this);
    }
}
