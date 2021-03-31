package Transaction;

import Stocks.Stock;

import java.io.Serializable;
import java.util.List;

public interface ITransaction extends Serializable {
    Stock getStock();

    int getNumOfStocks();

    public String getDateOfTransaction();

    public int getPriceOfStock();

    public int getTransactionWorth();

    public List<TransactionMade> findCounterTransaction(List<ITransaction> transactionsToScan,List<ITransaction> toAdd);
    public void setNumOfStocks(int number);

}
