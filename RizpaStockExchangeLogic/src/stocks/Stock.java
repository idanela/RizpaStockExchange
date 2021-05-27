package stocks;
import generated.RseStock;
import transaction.TransactionMade;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Stock implements Serializable {
    String stockName;
    String stockHolder;
    int currentPrice;
    int numOfTransaction;
    int transactionsWorth;
    List<TransactionMade> transactions;


    public Stock(RseStock stock) {
        this.stockName = stock.getRseSymbol();
        this.stockHolder = stock.getRseCompanyName();
        this.currentPrice = stock.getRsePrice();
        this.numOfTransaction = 0;
        this.transactionsWorth = 0;
        this.transactions = new LinkedList<>();
    }
    public Stock(String stockName, String stockHolder, int currentPrice) {
        this.stockName = stockName;
        this.stockHolder = stockHolder;
        this.currentPrice = currentPrice;
        this.numOfTransaction = 0;
        this.transactionsWorth = 0;
        this.transactions = new LinkedList<>();
    }

    public int getTransactionsWorth() {
        return transactionsWorth;
    }

    @Override
    public String toString() {
        return  "SYMBOL ='" + stockName + '\'' +
                ", StockHolder ='" + stockHolder + '\'' +
                ", Price =" + currentPrice +
                ", Number of transaction =" + numOfTransaction +'\''+
                ", Transactions worth = "+transactionsWorth+
                ", m_Transactions =" + transactions;
    }

    @Override
    public boolean equals(Object obj) {
        return stockName.equals(((Stock)obj).getStockName());
    }

    public String getStockHolder()
    {
        return this.stockHolder;
    }
    public int getCurrentPrice()
    {
        return this.currentPrice;
    }
    public int getNumOfTransaction()
    {
        return numOfTransaction;
    }

    public List<TransactionMade> getTransactions() {
        return transactions;
    }

    public int getWorthOfTransactions()
    {
        int sum = 0;
        for (TransactionMade transaction: transactions)
        {
            sum += transaction.getTransactionWorth();
        }

        transactionsWorth = sum;

        return sum;
    }

    public String getStockName()
    {
        return this.stockName;
    }

    public void setNumOfTransaction(int m_NumOfTransaction) {
        this.numOfTransaction = m_NumOfTransaction;
    }

    public void addTransactionToStocksTransactionsList(TransactionMade transaction)
    {
        transactions.add(transaction);
    }
}
