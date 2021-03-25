package Transaction;

import Stocks.Stock;

import java.util.List;

public interface ITransaction {
    boolean findCounterTransaction(List<ITransaction> i_Transactions);
    String getStockName();
    int getNumOfStocks();
    int getLimit();
    Stock getStock();
}
