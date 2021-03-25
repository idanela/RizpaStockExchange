package Transaction;

import Stocks.Stock;

import java.util.List;

public interface ITransaction {
    Stock getStock();
    String getStockName();
    int getNumOfStocks();
    int getLimit();
}
