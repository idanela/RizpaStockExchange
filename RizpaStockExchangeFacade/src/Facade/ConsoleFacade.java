package Facade;

import StockExchangeEngine.IStockEngine;
import StockExchangeEngine.StockExchangeEngine;
import Stocks.Stock;
import Transaction.PendingSellTransaction;
import Transaction.PendingTransaction;

import java.io.IOException;
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
    public boolean loadStocksData(StringBuilder msg, String path) {
        return m_Engine.getXmlContent(msg, path);
    }

    @Override
    public String sellStocks(String stockName, double limit, int amountForTransaction) {
        Stock stock = m_Engine.getStocks().get(stockName);
        PendingTransaction sellTransaction = new PendingSellTransaction(stock,limit,amountForTransaction);
        return sellTransaction.findCounterTransaction(m_Engine.getTransactionlist());
    }

    @Override
    public String buyStocks(String stockName, double limit, int amountForTransaction) {
        Stock stock = m_Engine.getStocks().get(stockName);
        PendingTransaction sellTransaction = new PendingSellTransaction(stock,limit,amountForTransaction);
        return sellTransaction.findCounterTransaction(m_Engine.getTransactionlist());
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
