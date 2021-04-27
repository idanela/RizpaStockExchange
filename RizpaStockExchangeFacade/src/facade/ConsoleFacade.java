package facade;

import stockExchangeEngine.IStockEngine;
import stockExchangeEngine.StockExchangeEngine;
import stocks.Stock;
import transaction.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


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
    public boolean loadStocksData(String path, AtomicBoolean hasSameCompany, AtomicBoolean hasSameSymbol) {
        return m_Engine.getXmlContent(path,hasSameCompany,hasSameSymbol);
    }

    @Override
    public List<TransactionMade> sellStocks(Transaction transaction) {
        List<TransactionMade> transactionsMade = transaction.findCounterTransaction(m_Engine.getPendingBuyTransactions(), m_Engine.getPendingSellTransactions());
        m_Engine.addTransactionsMade(transactionsMade);
        return transactionsMade;
    }

    @Override
    public List<TransactionMade> buyStocks(Transaction transaction) {
        List<TransactionMade> transactionsMade = transaction.findCounterTransaction(m_Engine.getPendingSellTransactions(), m_Engine.getPendingBuyTransactions(), );
        m_Engine.addTransactionsMade(transactionsMade);
        return transactionsMade;
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
    public IStockEngine getEngine() {return m_Engine;}


    @Override
    public void setEngine(IStockEngine engine) {
        m_Engine = engine;
    }
}
