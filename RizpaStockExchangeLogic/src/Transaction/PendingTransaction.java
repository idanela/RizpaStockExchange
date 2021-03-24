package Transaction;

import Stocks.Stock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public abstract class PendingTransaction implements ITransaction{

    Stock m_Stock;
    double m_Limit;
    int m_NumOfStocks;
    String m_DateOfTransaction;

    @Override
    public Stock getStock() {
        return m_Stock;
    }

    public PendingTransaction(Stock m_Stock, double m_Limit, int numOfStocks) {
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
    public double getLimit() {
        return m_Limit;
    }

    @Override
    public String getStockName() {
        return m_Stock.getStockName();
    }

    @Override
    public String findCounterTransaction(List<ITransaction> i_Transactions) {
        boolean isFinished=false;
        StringBuilder msg = new StringBuilder();
        for(ITransaction transaction: i_Transactions) {
            if(checkTypeToCompare(transaction)) {
                if(m_Stock.equals(transaction.getStock())) {
                    if(checkLimit(m_Limit,transaction.getLimit())) {
                        isFinished = preformTransaction(i_Transactions, transaction,msg);
                        if(isFinished) {
                            msg.append("Transaction completed fully");
                            break;
                        }
                    }
                }
            }
        }
        if(!isFinished)
            msg.append("Transaction completed Partially");

        return msg.toString();
    }

    protected abstract boolean checkLimit(double limit, double limitToCompareTo);
    protected abstract boolean checkTypeToCompare(ITransaction transaction);

    private boolean preformTransaction(List<ITransaction> i_transactions, ITransaction transaction,StringBuilder msg)
    {
        boolean isFinishedBuy = false;
        int numOfTransactionStocks = 0;
        Transaction newTransaction;
        if(this.m_NumOfStocks >= transaction.getNumOfStocks())
        {
            numOfTransactionStocks = m_NumOfStocks - transaction.getNumOfStocks();
            newTransaction = new Transaction(numOfTransactionStocks,transaction.getLimit());
            msg.append(transaction.toString());
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
            isFinishedBuy =!isFinishedBuy;
        }

        return isFinishedBuy;
    }

    private void addToTransactionList(List<ITransaction> i_transactions)
    {
        i_transactions.add(0,this);
    }
}
