package UI.printUtils;

import Stocks.Stock;
import Transaction.Transaction;

import java.util.Collections;
import java.util.List;

public class PrintUtils {

    public static void printStock(Stock stock)
    {
        System.out.println("Symbol: "+stock.getStockName());
        System.out.println("Company: "+stock.getStockHolder());
        System.out.println("Company: "+stock.getCurrentPrice());
        System.out.println("Number Of Transactions made: " + stock.getNumOfTransaction());
        System.out.println("Transactions worth:" +stock.getWorthOfTransactions());
    }

    public static void printAllStockTransactions(Stock stock)
    {
        List<Transaction> stockTransactions = stock.getTransactions();
        Collections.reverse(stockTransactions);
        for(Transaction transaction: stockTransactions)
        {
            printTransaction(transaction);
        }
    }

    private static void printTransaction(Transaction transaction)
    {
        System.out.println("Date: "+transaction.getDateOfTransaction());
        System.out.println("Amount: "+transaction.getAmountOfStocks());
        System.out.println("Price: "+transaction.getPriceOfStockSelledFor());
        System.out.println("Total worth: "+transaction.getTransactionWorth()+System.lineSeparator());
    }

    public static void printDetailedInfoAboutStock(Stock stock)
    {
        printStock(stock);
        printAllStockTransactions(stock);
    }

}
