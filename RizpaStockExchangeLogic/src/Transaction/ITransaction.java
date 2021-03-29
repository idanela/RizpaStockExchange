package Transaction;

import Stocks.Stock;

import java.util.List;

public interface ITransaction {
    Stock getStock();

    int getNumOfStocks();

    public String getDateOfTransaction();

    public int getPriceOfStock();

    public int getTransactionWorth();

    public List<TransactionMade> findCounterTransaction(List<ITransaction> transactionsToScan,List<ITransaction> toAdd);

    void setNumOfStocks(int number);

}
