package Transaction;

import java.text.SimpleDateFormat;
import java.util.*;

public class Transaction
{
    private String m_DateOfTransaction;
    private int m_AmountOfStocks;
    private int m_PriceOfStockSelledFor;
    private int m_TransactionWorth;

    public Transaction(int m_AmountOfStocks, int m_PriceOfStock) {
        this.m_DateOfTransaction = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        this.m_AmountOfStocks = m_AmountOfStocks;
        this.m_PriceOfStockSelledFor = m_PriceOfStock;
        this.m_TransactionWorth = m_AmountOfStocks* m_PriceOfStock;
    }


    public String getDateOfTransaction() {
        return m_DateOfTransaction;
    }

    public int getAmountOfStocks() {
        return m_AmountOfStocks;
    }

    public int getPriceOfStockSelledFor() {
        return m_PriceOfStockSelledFor;
    }

    public int getTransactionWorth() {
        return m_TransactionWorth;
    }


    @Override
    public String toString() {
        return "Transaction{" +
                "m_Date of transaction:" + m_DateOfTransaction +
                ", Amount of stocks:" + m_AmountOfStocks +
                ", m_PriceOfStock:" + m_PriceOfStockSelledFor +
                ", m_TransactionWorth:" + m_TransactionWorth +
                "}\r\n";
    }

    public static void sortTransactions(List<Transaction> transactions){
        Collections.sort(transactions,new SortByDate());
    }

}
