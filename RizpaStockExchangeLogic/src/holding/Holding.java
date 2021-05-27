package holding;

import stocks.Stock;

public class Holding {
    Stock stock;
    int amountOfStocks;
    int price;
    int totalWorthOfStocks;

    public Holding(Stock stock, int amountOfStocks) {
        this.stock = stock;
        this.amountOfStocks = amountOfStocks;
        this.price =stock.getCurrentPrice();
        this.totalWorthOfStocks = stock.getCurrentPrice()*amountOfStocks;
    }

    public int getPrice() {
        return price;
    }

    public Holding(Stock stock, int amountOfStocks, int price)
    {
        this.stock = stock;
        this.amountOfStocks = amountOfStocks;
        this.price = price;
        this.totalWorthOfStocks = price * amountOfStocks;
    }

    public int getTotalWorthOfStocks() {
        return totalWorthOfStocks;
    }

    public Stock getStock() {
        return stock;
    }

    public int getAmountOfStocks() {
        return amountOfStocks;
    }

    public String getStockName()
    {
        return stock.getStockName();
    }

    public int getCurrentPrice()
    {
        return stock.getCurrentPrice();
    }

    public void setAmountOfStocks(int amountOfStocks) {
        this.amountOfStocks = amountOfStocks;
    }

    public void setTotalWorthOfStocks(int totalWorthOfStocks) {
        this.totalWorthOfStocks = totalWorthOfStocks;
    }
}
