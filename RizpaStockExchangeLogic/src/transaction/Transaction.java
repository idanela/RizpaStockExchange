package transaction;

import stocks.Stock;
import user.User;

import java.io.Serializable;
import java.util.List;

public interface Transaction extends Serializable {
    User getInitiator();
    Stock getStock();

    int getNumOfStocks();

    public String getDateOfTransaction();

    public int getPriceOfStock();

    public int getTransactionWorth();

    public List<TransactionMade> findCounterTransaction(List<Transaction> transactionsToScan, List<Transaction> toAdd);
    public void setNumOfStocks(int number);

}
