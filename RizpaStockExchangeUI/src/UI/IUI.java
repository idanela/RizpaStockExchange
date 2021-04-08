package UI;

import StockExchangeEngine.IStockEngine;
import Transaction.AllTransactionsKinds;
import UI.menu.Menu;

import java.io.IOException;

public interface IUI
{
    public void loadStocksData();
    public void presentStocks();
    public void PresentStockDetails(String StockName);
    public void preformTransaction();
    public void presentCommandListForExecution();
    public void exit();
    public void getStockDetails();
    public void setTransactionToUse(AllTransactionsKinds lmtBuyTransaction);
    public Menu getMainMenu();
    public void readEngineFromFile();
    public void readEngineDataFromFile();
}
