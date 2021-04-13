package facade;

import stocks.Stock;
import transaction.ITransaction;
import transaction.TransactionMade;
import stockExchangeEngine.IStockEngine;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public interface IFacade
{
    public boolean isStockExists(String stockName);
    public boolean loadStocksData (String path, AtomicBoolean hasSameCompany, AtomicBoolean hasSameSymbol);
    public List<TransactionMade> sellStocks(ITransaction transaction);
    public List<TransactionMade> buyStocks(ITransaction transaction);
    public Map<String, Stock> getStocks();
    public Stock getStock(String stockName);
    public IStockEngine getEngine();
    void setEngine(IStockEngine engine);


}
