package Facade;

import Stocks.Stock;
import Transaction.ITransaction;
import Transaction.TransactionMade;
import StockExchangeEngine.IStockEngine;

import java.util.List;
import java.util.Map;

public interface IFacade
{
    public boolean isStockExists(String stockName);
    public boolean loadStocksData (String path, Boolean hasSameCompany, Boolean hasSameSymbol);
    public List<TransactionMade> sellStocks(ITransaction transaction);
    public List<TransactionMade> buyStocks(ITransaction transaction);
    public Map<String, Stock> getStocks();
    public Stock getStock(String stockName);
    //public void loadCommandListForExecution() ;
    public IStockEngine getEngine();

}
