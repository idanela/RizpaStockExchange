package stocks;
import generated.RseStock;
import transaction.TransactionMade;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Stock implements Serializable {
    String m_Name;
    String m_StockHolder;
    int m_CurrentPrice;
    int m_NumOfTransaction;
    List<TransactionMade> m_Transactions;


    public Stock(RseStock stock) {
        this.m_Name = stock.getRseSymbol();
        this.m_StockHolder = stock.getRseCompanyName();
        this.m_CurrentPrice = stock.getRsePrice();
        this.m_NumOfTransaction = 0;
        this.m_Transactions = new LinkedList<>();
    }
    public Stock(String m_Name, String m_StockHolder, int m_CurrentPrice) {
        this.m_Name = m_Name;
        this.m_StockHolder = m_StockHolder;
        this.m_CurrentPrice = m_CurrentPrice;
        this.m_NumOfTransaction = 0;
        this.m_Transactions = new LinkedList<>();
    }

    @Override
    public String toString() {
        return  "SYMBOL ='" + m_Name + '\'' +
                ", StockHolder ='" + m_StockHolder + '\'' +
                ", Price =" + m_CurrentPrice +
                ", Number of transaction =" + m_NumOfTransaction +
                ", m_Transactions =" + m_Transactions;
    }

    @Override
    public boolean equals(Object obj) {
        return m_Name.equals(((Stock)obj).getStockName());
    }

    public String getStockHolder()
    {
        return this.m_StockHolder;
    }
    public int getCurrentPrice()
    {
        return this.m_CurrentPrice;
    }
    public int getNumOfTransaction()
    {
        return m_NumOfTransaction;
    }

    public List<TransactionMade> getTransactions() {
        return m_Transactions;
    }

    public int getWorthOfTransactions()
    {
        int sum = 0;
        for (TransactionMade transaction:m_Transactions)
        {
            sum += transaction.getTransactionWorth();
        }

        return sum;
    }

    public String getStockName()
    {
        return this.m_Name;
    }

    public void setNumOfTransaction(int m_NumOfTransaction) {
        this.m_NumOfTransaction = m_NumOfTransaction;
    }

    public void addTransactionToStocksTransactionsList(TransactionMade transaction)
    {
        m_Transactions.add(transaction);
    }
}
