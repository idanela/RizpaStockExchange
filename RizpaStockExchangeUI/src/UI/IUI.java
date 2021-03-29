package UI;

import Transaction.AllTransactionsKind;
import Transaction.ITransaction;
import Transaction.LMTBuyTransaction;
import UI.Menu.Menu;

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
    public void setTransactionToUse(AllTransactionsKind lmtBuyTransaction);
    public Menu getMainMenu();
}
