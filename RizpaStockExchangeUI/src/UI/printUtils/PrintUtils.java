package UI.printUtils;

import Stocks.Stock;
import Transaction.TransactionMade;
import Transaction.ITransaction;
import StockExchangeEngine.IStockEngine;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PrintUtils {


    public static void printAllStocks(Map<String,Stock> stocks)
    {
        for(Stock stock:stocks.values())
        {
            printStock(stock);
        }
    }
    private static void printStock(Stock stock)
    {
        System.out.println("Symbol: "+stock.getStockName());
        System.out.println("Company: "+stock.getStockHolder());
        System.out.println("Price: "+stock.getCurrentPrice());
        System.out.println("Number Of Transactions made: " + stock.getNumOfTransaction());
        System.out.println("Transactions worth:" +stock.getWorthOfTransactions());
    }

    public static void printAllStockTransactions(Stock stock)
    {
        List<TransactionMade> stockTransactions = stock.getTransactions();
        Collections.reverse(stockTransactions);
        for(TransactionMade transaction: stockTransactions)
        {
            printTransaction(transaction);
        }
    }

    private static void printTransaction(ITransaction transaction)
    {
        System.out.println("Date: "+transaction.getDateOfTransaction());
        System.out.println("Amount: "+transaction.getNumOfStocks());
        System.out.println("Price: "+transaction.getPriceOfStock());
        System.out.println("Total worth: "+transaction.getTransactionWorth()+System.lineSeparator());
    }

    public static void printDetailedInfoAboutStock(Stock stock)
    {
        printStock(stock);
        printAllStockTransactions(stock);
    }

    public static void printDetailsAboutTransactionsMade(List<TransactionMade> transactionsMade, int amount)
    {
        if(transactionsMade.size() == 0)
        {
            System.out.println("Counter Transaction was not found.");
            return;
        }

        int amountPurchase = 0;
        for(TransactionMade transaction: transactionsMade)
        {
            amountPurchase+=transaction.getNumOfStocks();
        }
        if(amount == amountPurchase)
        {
            System.out.println("Transaction made fully.");
        }
        else
        {
            System.out.println("Transaction made partially.");
        }

    }

    public static void printAllTransactionInSystem(IStockEngine engine)
    {
        printTransactions("Transaction List:",engine.getTransactionList());
        printTransactions("Pending Buy Transactions",engine.getPendingBuyTransactions());
        printTransactions("Pending Sell Transactions",engine.getPendingSellTransactions());
    }

    private static void printTransactions(String List, List<ITransaction> transactionList)
    {
        int sumOfAllTransactions = 0;
        System.out.println(List);
        if(transactionList.isEmpty())
        {
            System.out.println("There is no transaction in that list");
        }
        else
        {
            for (ITransaction transaction: transactionList)
            {
                printTransaction(transaction);
                sumOfAllTransactions+=transaction.getTransactionWorth();
            }
        }

        System.out.println("Total worth of All Transaction in the list: "+sumOfAllTransactions);
    }

}
