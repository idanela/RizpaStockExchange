package transaction;

import stocks.Stock;
import user.User;

import java.text.SimpleDateFormat;
import java.util.*;

public class TransactionMade implements Transaction
{
    User initiator;
    User partner;


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

    public TransactionMade(Stock stock, int amountOfStocks, int priceOfStock, User initiator,User partner,String kind) {
        this.stock = stock;
        this.dateOfTransaction = new SimpleDateFormat("HH:mm:ss:SSS").format(new Date());
        this.amountOfStocks = amountOfStocks;
        this.priceOfStockSelledFor = priceOfStock;
        this.transactionWorth = amountOfStocks * priceOfStock;
        this.initiator = initiator;
        this.partner= partner;
        this.transactionKind =kind;
    }

    @Override
    public void setAmountOfStocks(int number) {
        amountOfStocks = number;
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
    public List<TransactionMade> findCounterTransaction(List<Transaction> transactionsToScan, List<Transaction> toAdd){
        return null;
    }

    public User getPartner() {
        return partner;
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
}
