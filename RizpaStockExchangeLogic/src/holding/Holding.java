package holding;

import stocks.Stock;

public class Holding {
    Stock stock;
    int amountOfStocks;

    public Holding(Stock stock, int amountOfStocks) {
        this.stock = stock;
        this.amountOfStocks = amountOfStocks;
    }

    public Stock getStock() {
        return stock;
    }

    public int getAmountOfStocks() {
        return amountOfStocks;
    }
}
