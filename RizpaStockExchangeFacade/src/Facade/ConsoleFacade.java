package Facade;

import StockExchangeEngine.IStockEngine;
import StockExchangeEngine.StockExchangeEngine;
import Stocks.Stock;
import Transaction.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class  ConsoleFacade implements IFacade {

    IStockEngine m_Engine;
    public ConsoleFacade() {
        m_Engine = new StockExchangeEngine();
    }

    @Override
    public boolean isStockExists(String stockName) {
        return m_Engine.getStocks().get(stockName)!= null;
    }

    @Override
    public boolean loadStocksData(String path, Boolean hasSameCompany, Boolean hasSameSymbol) {
        return m_Engine.getXmlContent(path,hasSameCompany,hasSameSymbol);
    }

    @Override
    public String sellStocks(String stockName, int limit, int amountForTransaction) {
        Stock stock = m_Engine.getStocks().get(stockName);
        PendingTransaction sellTransaction = new PendingSellTransaction(stock,limit,amountForTransaction);
       // return m_Engine.findAndPreformCounterTransaction(sellTransaction, m_Engine.getPendingBuyTransactions());
    }

    @Override
    public List<Transaction> buyStocks(String stockName, int limit, int amountForTransaction) {
        Stock stock = m_Engine.getStocks().get(stockName);
        PendingTransaction buyTransaction = new PendingBuyTransaction(stock, limit, amountForTransaction);
        return buyTransaction.findCounterTransaction(m_Engine.getPendingSellTransactions());
    }

    @Override
    public Map<String, Stock> getStocks() {
        return this.m_Engine.getStocks();
    }

    @Override
    public Stock getStock(String stockName) {
        return m_Engine.getStocks().get(stockName);
    }

    @Override
    public void preformTransaction() {

    }

    @Override
    public void loadCommandListForExecution()throws IOException {
       m_Engine.presentAllStocksTransactions();
    }

    @Override
    public void exit() {

    }
}
