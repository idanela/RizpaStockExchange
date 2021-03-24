package UI;

import Facade.ConsoleFacade;
import Facade.IFacade;
import Stocks.Stock;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ConsoleUI implements IUI
{
    public ConsoleUI() {
        this.m_Facade = new ConsoleFacade();
        this.m_Scanner = new Scanner(System.in);
        m_LoadSuccessfully = false;
    }

    private IFacade m_Facade;
    private Scanner m_Scanner;
    private boolean m_LoadSuccessfully;
    @Override
    public void showMenu()
    {
        String menu = String.format("Pick one of the options below:"  +
                System.lineSeparator() +
                "1.Load Stocks Details" +
                System.lineSeparator() +
                "2.Present stocks that are defined in the system" +
                System.lineSeparator() +
                "3.Present details on a specific stock" +
                System.lineSeparator() +
                "4.Preform Transaction" +
                System.lineSeparator() +
                "5.Present command list for execution" +
                System.lineSeparator() +
                "6.Exit"+System.lineSeparator());
        System.out.println(menu);
        getChoice();
    }

    @Override
    public void getChoice()
    {
        int choice = 0;
        boolean gotChoice = false;
        while(!gotChoice)
        {
            System.out.println("Please enter a number from 1-6:");
            showMenu();
            try
            {
                choice=m_Scanner.nextInt();
                if(choice >=1 && choice<=6)
                {
                    gotChoice =!gotChoice;
                }
                else
                {
                    System.out.println("The Number you've entered is not between 1-6.");
                }
            }
            catch (NumberFormatException e) {
                System.out.println("You've Entered an input which is not an integer.");
            }
        }
        preformAction(choice);
    }

    @Override
    public boolean loadStocksData()
    {
        boolean   loadedSuccessfully = false;
        StringBuilder msg = new StringBuilder();
        System.out.println("please enter file's full path:");
        String path = m_Scanner.next();
        if(checkXmlPath(path,msg))
        {
            System.out.println(msg);
        }
        else
        {
           loadedSuccessfully = m_Facade.loadStocksData(msg,path);
           System.out.println(msg);
        }

        return loadedSuccessfully;

    }

    private boolean checkXmlPath(String path, StringBuilder msg)
    {
         boolean isValidPath = true;
         if(Files.exists(Paths.get(path)))
         {
             msg.append("File does not exists"+System.lineSeparator());
         }
        if(!path.endsWith("xml"))
        {
            msg.append("This path is not a path of xml file"+System.lineSeparator());
            isValidPath= false;
        }

        return isValidPath;
    }

    @Override
    public void presentStocks() {
        if(m_LoadSuccessfully) {
            Map<String, Stock> stocksNameToStockDetails = m_Facade.getStocks();
            for (Stock stock : stocksNameToStockDetails.values()) {
                System.out.println(stock);
            }
        }
        else
        {
            fileWasNotUploaded();
        }
    }

    private void fileWasNotUploaded() {
        System.out.println("This action is not available because the data about stocks was not uploaded yet.");
    }

    @Override
    public void PresentStockDetails(String stockName) {
        Stock stock = m_Facade.getStock(stockName.toUpperCase());
        if (stock == null) //stock not exists
        {
            stockNotExists(stockName);
        }
        else
        { System.out.println(stock.getElaboratedStockInfo()); }
    }

    @Override
    public void preformTransaction() {
        if(m_LoadSuccessfully) {
            StringBuilder msg = new StringBuilder();
            String choice = getBuyOrSellOrder();
            System.out.println("Please enter stock name");
            String StockName = m_Scanner.next().toUpperCase();
            if (!m_Facade.isStockExists(StockName)) {
                System.out.println("There is no stock by that name.");
                return;
            }
            double limit = getLimit();
            if (limit <= 0) {
                System.out.println("Limit should be a positive number");
                return;
            }

            int amountForTransaction = getNumberOfStocksToPreformTransaction();
            if (amountForTransaction <= 0)
                return;

            preformTransactionAccordingToUserChoice(choice, StockName, limit, amountForTransaction);
        }
        else
        {
            fileWasNotUploaded();
        }
    }

    private void preformTransactionAccordingToUserChoice(String choice, String stockName, double limit, int amountForTransaction) {

        if(choice == "B")
        {
            buyStocks(stockName, limit,amountForTransaction);
        }
        else
        {
            sellStocks(stockName,limit,amountForTransaction);
        }
    }


    private int getNumberOfStocksToPreformTransaction() {
        int amount =0;
            try {
                System.out.println("Please insert a number of stocks for a transaction.");
                amount = m_Scanner.nextInt();
                if (amount <= 0)
                    System.out.println("Number of stock to trade should be positive");
            }catch(InputMismatchException e)
            {
                System.out.println("You have entered a string instead of a positive number");
            }

        return amount;
    }

    private void buyStocks(String stockName, double limit, int amountForTransaction) {
      boolean isTransactionSuccessful;
        if(m_Facade.isStockExists(stockName))
        {
            String msg = m_Facade.buyStocks(stockName,limit,amountForTransaction);
            System.out.println(msg);
        }
        else
        {
            stockNotExists(stockName);
        }
    }
    private void stockNotExists(String stockName)
    {
        System.out.println("There is no stock called " + stockName + "in the system");
    }

    private void sellStocks(String stockName, double limit, int amountForTransaction) {
        if(m_Facade.isStockExists(stockName))
        {
          String msg =  m_Facade.sellStocks(stockName,limit,amountForTransaction);
            System.out.println(msg);
        }
        else
        {
            stockNotExists(stockName);
        }
    }

    private double getLimit()
    {
        double limit = 0;
        System.out.println("Please enter the limit(a positive number) for the transaction");
            try {
                limit = m_Scanner.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("You have entered a string instead of a number.");
            }

        return limit;
    }
    private String getBuyOrSellOrder()
    {
        String choice = null;
        boolean isValid = false;
        while(!isValid)
        {
            System.out.println("Please insert 'B' for Buying stock or 'S' to sell it");
            choice= m_Scanner.next();
            if (!choice.toUpperCase().equals("B") && !choice.toUpperCase().equals("S"))
            {
                System.out.println("You have Entered Invalid Choice.");
            }
        }
        return choice;
    }

    @Override
    public void presentCommandListForExecution() throws IOException{
        m_Facade.loadCommandListForExecution();
        String line;
        try(BufferedReader input = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("allStocksTransaction.txt")));){
            while( (line = input.readLine())!=null)
            {
                System.out.println(line);
            }
        }

    }






    @Override
    public void exit() {
        System.out.println("Thank you for using Rizpa stock Exchange System. Come back soon :)");
    }

    boolean preformAction(int choice)
    {
        boolean exit = false;
        switch (choice)
        {
            case 1:
                if(loadStocksData())
                {
                    m_LoadSuccessfully = true;
                };
                break;
            case 2:
                    presentStocks();
                break;
            case 3:
                getStockDetails();
                break;
            case 4:
                preformTransaction();
                break;
            case 5:

                break;
            case 6:
                exit = true;
                exit();
                break;

        }

        return exit;
    }

    private void getStockDetails()
    {
        if(m_LoadSuccessfully)
        {
            String stockName = m_Scanner.next();
            PresentStockDetails(stockName);
        }
        else
        {
            fileWasNotUploaded();
        }

    }
}