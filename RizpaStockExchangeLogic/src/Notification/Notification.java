package Notification;

public class Notification {
    boolean isBought;
    String stockName;
    int price;
    int amount;
    String userName;

    public Notification(boolean isBought, String stockName, int price, int amount,String userName) {
        this.isBought = isBought;
        this.stockName = stockName;
        this.price = price;
        this.amount = amount;
        this.userName = userName;
    }
}
