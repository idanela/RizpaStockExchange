package ui.printUtils;

import stocks.Stock;
import transaction.TransactionMade;
import transaction.Transaction;
import stockExchangeEngine.IStockEngine;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PrintUtils {

    public static void printAllStocks(Map<String, Stock> stocks) {
        for (Stock stock : stocks.values()) {
            printStock(stock);
        }
    }

    private static void printStock(Stock stock) {
        System.out.println("Symbol: " + stock.getStockName());
        System.out.println("Company: " + stock.getStockHolder());
        System.out.println("Price: " + stock.getCurrentPrice());
        System.out.println("Number Of Transactions made: " + stock.getNumOfTransaction());
        System.out.println("Transactions worth:" + stock.getWorthOfTransactions() + System.lineSeparator());
    }

    public static void printAllStockTransactions(Stock stock) {
        List<TransactionMade> stockTransactions = stock.getTransactions();
        Collections.reverse(stockTransactions);
        for (TransactionMade transaction : stockTransactions) {
            printTransaction(transaction);
        }
        if(stockTransactions.size()== 0)
        {
            System.out.println("Currently there is no transaction with this stock.");
        }
        System.out.println();
    }

    private static void printTransaction(Transaction transaction) {
        System.out.println("Date: " + transaction.getDateOfTransaction());
        System.out.println("Amount: " + transaction.getAmountOfStocks());
        System.out.println("Price: " + transaction.getPriceOfStock());
        System.out.println("Total worth: " + transaction.getTransactionWorth() + System.lineSeparator());
    }

    public static void printDetailedInfoAboutStock(Stock stock) {
        printStock(stock);
        printAllStockTransactions(stock);
    }

    public static void printDetailsAboutTransactionsMade(List<TransactionMade> transactionsMade, int amount) {
        if (transactionsMade.size() == 0) {
            System.out.println("Counter Transaction was not found." + System.lineSeparator());
            return;
        }

        int amountPurchase = 0;
        for (TransactionMade transaction : transactionsMade) {
            amountPurchase += transaction.getAmountOfStocks();
        }
        if (amount == amountPurchase) {
            System.out.println("Transaction completed fully." + System.lineSeparator());
        } else {
            System.out.println("Transaction made partially." + System.lineSeparator());
        }
        printTransactionsMade(transactionsMade);
    }

    private static void printTransactionsMade(List<TransactionMade> transactionsMade) {
        for (TransactionMade transaction : transactionsMade) {
            System.out.println("Date: " + transaction.getDateOfTransaction());
            System.out.println("Amount: " + transaction.getAmountOfStocks());
            System.out.println("Price: " + transaction.getPriceOfStock());
            System.out.println("Total worth: " + transaction.getTransactionWorth() + System.lineSeparator());
        }
    }

    public static void printAllTransactionInSystem(IStockEngine engine)
    {
        for (Stock stock : engine.getStocks().values())
        {
            List<Transaction> stockPendingBuyTransaction = getFilteredStockTransactions(engine.getPendingBuyTransactions(),stock);
            List<Transaction> stockPendingSellTransaction = getFilteredStockTransactions(engine.getPendingSellTransactions(),stock);
            List<Transaction> stockMadeTransaction = getFilteredStockTransactions(engine.getTransactionList(),stock);
            System.out.println("Stock's Symbol:"+ stock.getStockName());
            printTransactions("Transaction made List:",stockMadeTransaction);
            printTransactions("Pending Buy Transactions:",stockPendingBuyTransaction);
            printTransactions("Pending Sell Transactions:",stockPendingSellTransaction);
        }
    }

    private static List<Transaction> getFilteredStockTransactions(List<Transaction> Transactions, Stock stock) {
        return Transactions
                .stream()
                .filter(transaction -> transaction.getStock().equals(stock))
                .collect(Collectors.toList());
    }


    private static void printTransactions(String List, List<Transaction> transactionList)
    {
        int sumOfAllTransactions = 0;
        System.out.println(List);
        if(transactionList.isEmpty())
        {
            System.out.println("There is no transactions in that list");
        }
        else
        {
            for (Transaction transaction: transactionList)
            {
                printTransaction(transaction);
                sumOfAllTransactions+=transaction.getTransactionWorth();
            }
        }

        System.out.println("Total worth of All Transactions in the list: " + sumOfAllTransactions + System.lineSeparator());
    }

}
