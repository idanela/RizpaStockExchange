package Facade;

import Stocks.Stock;
import Transaction.Transaction;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IFacade
{
    public boolean isStockExists(String stockName);
    public boolean loadStocksData (String path, Boolean hasSameCompany, Boolean hasSameSymbol);
    public String sellStocks(String stockName, int limit, int amountForTransaction);
    public List<Transaction> buyStocks(String stockName, int limit, int amountForTransaction);



    public Map<String, Stock> getStocks();

    public Stock getStock(String stockName);

    public void preformTransaction();
    public void loadCommandListForExecution() throws IOException;
    public void exit();
}
