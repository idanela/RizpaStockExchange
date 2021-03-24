package UI;

import java.io.IOException;

public interface IUI
{
    public  void  showMenu();
    public void  getChoice();
    public boolean loadStocksData();
    public void presentStocks();
    public void PresentStockDetails(String StockName);
    public void preformTransaction();
    public void presentCommandListForExecution() throws IOException;
    public void exit();

}
