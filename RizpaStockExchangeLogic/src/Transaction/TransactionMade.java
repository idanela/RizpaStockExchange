package Transaction;

import Stocks.Stock;

import java.text.SimpleDateFormat;
import java.util.*;

public class TransactionMade implements ITransaction
{
    Stock m_stock;
    private String m_DateOfTransaction;
    private int m_AmountOfStocks;
    private int m_PriceOfStockSelledFor;
    private int m_TransactionWorth;

    public TransactionMade(Stock stock,int amountOfStocks, int priceOfStock) {
        this.m_stock =stock;
        this.m_DateOfTransaction = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        this.m_AmountOfStocks = amountOfStocks;
        this.m_PriceOfStockSelledFor = priceOfStock;
        this.m_TransactionWorth = amountOfStocks * priceOfStock;
    }

    @Override
    public void setNumOfStocks(int number) {
        m_AmountOfStocks=number;
    }

    @Override
    public Stock getStock() {
        return m_stock;
    }
    @Override
    public String getDateOfTransaction() {
        return m_DateOfTransaction;
    }

    @Override
    public int getNumOfStocks() {return m_AmountOfStocks;}

    @Override
    public int getPriceOfStock() {return m_PriceOfStockSelledFor;}

    @Override
    public int getTransactionWorth() {return m_TransactionWorth;}

    @Override
    public List<TransactionMade> findCounterTransaction(List<ITransaction> transactionsToScan, List<ITransaction> toAdd){
        return null;
    }

    /*@Override
    public List<TransactionMade> findCounterTransaction(List<ITransaction> transactionsToScan,List<ITransaction> toAdd) {
        return null;
    }*/

    @Override
    public String toString() {
        return "Transaction{" +
                "m_Date of transaction:" + m_DateOfTransaction +
                ", Amount of stocks:" + m_AmountOfStocks +
                ", m_PriceOfStock:" + m_PriceOfStockSelledFor +
                ", m_TransactionWorth:" + m_TransactionWorth +
                "}\r\n";
    }






}
