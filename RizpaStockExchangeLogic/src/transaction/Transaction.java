package transaction;

import stocks.Stock;
import user.User;

import java.io.Serializable;
import java.util.List;

public interface Transaction extends Serializable {
    User getInitiator();
    Stock getStock();
    public User getPartner();
    public User getBuyer();
    public String getUserName();
    public int getAmountOfStocks();

    public String getDateOfTransaction();

    public int getPriceOfStock();

    public int getTransactionWorth();
    public String getTransactionKind();

    public List<TransactionMade> findCounterTransaction(List<Transaction> transactionsToScan, List<Transaction> toAdd,boolean isBuyOperation);
    public void setAmountOfStocks(int number);

}
