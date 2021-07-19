package DTOsToWrite;

import stocks.Stock;

public class StockToAdd {
    String stockName;
    String stockHolder;
    int currentPrice;
    int transactionsWorth;
    public StockToAdd(Stock stock) {
         stockName = stock.getStockName();
         stockHolder = stock.getStockHolder();
         currentPrice = stock.getCurrentPrice();
         transactionsWorth = stock.getTransactionsWorth();
    }
}
