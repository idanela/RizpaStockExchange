package transaction;

import stocks.Stock;
import user.User;

import java.text.SimpleDateFormat;
import java.util.*;

public class TransactionMade implements Transaction
{
    User initiator;
    User partner;
    User buyer;
    User seller;

    Stock stock;
    private String dateOfTransaction;
    private int amountOfStocks;
    private int priceOfStockSelledFor;
    private int transactionWorth;
    private String transactionKind;


    @Override
    public String getTransactionKind() {
        return transactionKind;
    }

    public int getPriceOfStockSelledFor() {
        return priceOfStockSelledFor;
    }

    public TransactionMade(Stock stock, int amountOfStocks, int priceOfStock, User initiator,User partner,String kind,boolean isBuyOperation) {
        this.stock = stock;
        this.dateOfTransaction = new SimpleDateFormat("HH:mm:ss:SSS").format(new Date());
        this.amountOfStocks = amountOfStocks;
        this.priceOfStockSelledFor = priceOfStock;
        this.transactionWorth = amountOfStocks * priceOfStock;
        this.initiator = initiator;
        this.partner= partner;
        this.transactionKind =kind;
        this.buyer =  isBuyOperation ?initiator:partner;
        this.seller = isBuyOperation ? partner:initiator;
    }

    @Override
    public void setAmountOfStocks(int number) {
        amountOfStocks = number;
    }

    @Override
    public User getBuyer() {
        return buyer;
    }
    public User getSeller() {
        return seller;
    }

    @Override
    public User getInitiator() {
        return initiator;
    }

    @Override
    public Stock getStock() {
        return stock;
    }

    @Override
    public String getUserName() {
        return initiator.getName();
    }
   public String getPartnerName()
    {
        return partner.getName();
    }

    @Override
    public String getDateOfTransaction() {
        return dateOfTransaction;
    }

    @Override
    public int getAmountOfStocks() {return amountOfStocks;}

    @Override
    public int getPriceOfStock() {return priceOfStockSelledFor;}

    @Override
    public int getTransactionWorth() {return transactionWorth;}

    @Override
    public List<TransactionMade> findCounterTransaction(List<Transaction> transactionsToScan, List<Transaction> toAdd,boolean isBuyOperation){
        return null;
    }

    @Override
    public User getPartner() {
        return this.partner;
    }


    @Override
    public String toString() {
        return "Transaction{" +
                "m_Date of transaction:" + dateOfTransaction +
                ", Amount of stocks:" + amountOfStocks +
                ", m_PriceOfStock:" + priceOfStockSelledFor +
                ", m_TransactionWorth:" + transactionWorth +
                "}\r\n";
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public String getBuyerName()
    {
        return buyer.getName();
    }

    public String getSellerName()
    {
        return seller.getName();
    }

}
