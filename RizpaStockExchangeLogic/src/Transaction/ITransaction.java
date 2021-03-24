package Transaction;

import Stocks.Stock;

import java.util.List;

public interface ITransaction {
    String findCounterTransaction(List<ITransaction> i_Transactions);
    String getStockName();
    int getNumOfStocks();
    double getLimit();
    Stock getStock();
}
