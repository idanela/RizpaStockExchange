package UI;

import Facade.ConsoleFacade;
import Facade.IFacade;
import StockExchangeEngine.IStockEngine;
import Stocks.Stock;
import Transaction.*;
import UI.Command.MainMenu.*;
import UI.Command.OrderMenu.LMTBuyCommand;
import UI.Command.OrderMenu.LMTSellCommand;
import UI.Command.OrderMenu.MKTCommand;
import UI.Menu.Menu;
import UI.menuItem.MenuItem;
import UI.printUtils.PrintUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ConsoleUI implements IUI
{

    private IFacade m_Facade;
    private Scanner m_Scanner;
    private boolean m_LoadSuccessfully;
    private Menu m_MainMenu;
    private Menu m_BuyOrderMenu;
    private Menu m_SellOrderMenu;
    AllTransactionsKind m_TransactionToUse;

    public ConsoleUI() {
        this.m_Facade = new ConsoleFacade();
        this.m_Scanner = new Scanner(System.in);
        this.m_LoadSuccessfully = false;
        this.m_BuyOrderMenu = new Menu(initializeBuyOrderMenu());
        this.m_SellOrderMenu = new Menu(initializeSellOrderMenu());
        this.m_MainMenu = new Menu(initializeMainMenu());
        this.m_TransactionToUse = null;
    }

    @Override
    public Menu getMainMenu() {
        return m_MainMenu;
    }

    public ITransaction getTransactionToUse() {
        return m_TransactionToUse;
    }

    @Override
    public void setTransactionToUse(AllTransactionsKind transaction) {
            this.m_TransactionToUse = transaction;
    }

    private List<MenuItem> initializeMainMenu()
    {
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("Load system file.",new LoadSystemDetailsCommand(this)));
        menuItems.add(new MenuItem("Present all stocks in the system.",new PresentAllStocksInSystemCommand(this)));
        menuItems.add(new MenuItem("Present a single stock details.",new PresentStockCommand(this)));
        menuItems.add(new MenuItem("Preform Transaction.",new PreformTransactionCommand(this)));
        menuItems.add(new MenuItem("Present Command list for execution.",new PresentCommandListForExecutionCommand(this)));
        menuItems.add(new MenuItem("Exit",new ExitCommand(this)));

        return menuItems;
    }

    private List<MenuItem> initializeBuyOrderMenu()
    {
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("LMT",new LMTBuyCommand(this)));
        menuItems.add(new MenuItem("MKT",new MKTCommand(this)));

        return menuItems;
    }

    private List<MenuItem> initializeSellOrderMenu() {
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("LMT",new LMTSellCommand(this)));
        menuItems.add(new MenuItem("MKT",new MKTCommand(this)));

        return menuItems;
    }

    @Override
    public void loadStocksData()
    {
        Boolean hasSameSymbol = false;
        Boolean hasSameCompany = false;
        boolean loadedSuccessfully = false;
        StringBuilder msg = new StringBuilder();
        System.out.println("please enter file's full path:");
        String path = m_Scanner.next();
        if(fileIsValid(path))
        {
            loadedSuccessfully = m_Facade.loadStocksData(path,hasSameCompany,hasSameSymbol);
            printMsgAboutLoadedFile(loadedSuccessfully,hasSameCompany,hasSameSymbol);
        }
        else
        {
            printErrorsAboutFile(path);
        }
    }

    private void printErrorsAboutFile(String path) {
        StringBuilder msg = new StringBuilder("File is not valid because: "+System.lineSeparator());
        if(!Files.exists(Paths.get(path)))
        {
            msg.append("File is not exists"+System.lineSeparator());
        }
        else
        {
            if (path.endsWith("xml"))
            {
                msg.append("File is not an xml file");
            }
        }

        System.out.print(msg);
    }

    private void printMsgAboutLoadedFile(boolean loadedSuccessfully,Boolean hasSameCompany, Boolean hasSameSymbol)
    {
        StringBuilder msg = new StringBuilder();
        if(loadedSuccessfully)
        {
            m_LoadSuccessfully = true;
            msg.append("File was Loaded Successfully.");
        }
        else
        {
            if(hasSameCompany)
            {
                msg.append("The file contains two different stocks belong to the same company. ");
            }
            if(hasSameSymbol)
            {
                msg.append("File Contains two stocks with same Symbol");
            }

            System.out.println(msg);

        }
    }

    private boolean fileIsValid(String path)
    {
         return Files.exists(Paths.get(path)) && path.endsWith("xml");
    }

    @Override
    public void presentStocks() {
        if(m_LoadSuccessfully) {
            Map<String, Stock> stocksNameToStockDetails = m_Facade.getStocks();
            PrintUtils.printAllStocks(stocksNameToStockDetails);
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
            { PrintUtils.printDetailedInfoAboutStock(stock);}
    }
    @Override
    public void preformTransaction() {
        if(m_LoadSuccessfully) {
            String choice = getBuyOrSellOrder();
            System.out.println("Please enter stock name");
            String StockName = m_Scanner.next().toUpperCase();
            if (!m_Facade.isStockExists(StockName)) {
                System.out.println("There is no stock by that name.");
                return;
            }
            int amountForTransaction = getNumberOfStocksToPreformTransaction();
            preformTransactionAccordingToUserChoice(choice, StockName, amountForTransaction);
        }
        else
        {
            fileWasNotUploaded();
        }
    }

    private void preformTransactionAccordingToUserChoice(String choice, String stockName, int amountForTransaction) {

        int limit = 0;
        Stock stock = m_Facade.getStock(stockName);
        if(choice == "B")
        {
            m_BuyOrderMenu.Run(true);
            if(!(m_TransactionToUse instanceof MKTTransaction))
                limit = getLimit();
                m_TransactionToUse.setProperties(stock,limit,amountForTransaction);
            buyStocks(m_TransactionToUse);
        }
        else
        {
            m_SellOrderMenu.Run(true);
            m_TransactionToUse.setProperties(stock,limit,amountForTransaction);
            if(!(m_TransactionToUse instanceof MKTTransaction))
                limit = getLimit();
            sellStocks(m_TransactionToUse);
        }
    }


    private int getNumberOfStocksToPreformTransaction() {
        int amount =0;
        boolean isValidAmount = false;
        while(!isValidAmount) {
            try {
                System.out.println("Please insert a positive number of stocks for a transaction.");
                amount = m_Scanner.nextInt();
                if (amount <= 0)
                    System.out.println("Number of stock to trade should be positive");
                else
                    isValidAmount = true;
            } catch (InputMismatchException e) {
                System.out.println("You have entered a string instead of a positive number");
            }
        }

        return amount;
    }

    private void buyStocks(ITransaction transaction)
    {
        if(m_Facade.isStockExists(transaction.getStock().getStockName()))
        {
            List<TransactionMade> transactionsMade = m_Facade.buyStocks(m_TransactionToUse);
            PrintUtils.printDetailsAboutTransactionsMade(transactionsMade,transaction.getNumOfStocks());
        }
        else
        {
            stockNotExists(transaction.getStock().getStockName());
        }
    }


    private void stockNotExists(String stockName)
    {
        System.out.println("There is no stock called " + stockName + "in the system");
    }

    private void sellStocks(ITransaction transaction) {
        if(m_Facade.isStockExists(transaction.getStock().getStockName())) {
            List<TransactionMade> transactions = m_Facade.sellStocks(m_TransactionToUse);
            PrintUtils.printDetailsAboutTransactionsMade(transactions, m_TransactionToUse.getNumOfStocks());
        }
        else
        {
            stockNotExists(transaction.getStock().getStockName());
        }
    }

    private int getLimit()
    {
        int limit = 0;
        boolean isValidLimit = false;
        while(!isValidLimit)
        {
            System.out.println("Please enter the limit(a positive number) for the transaction");
            try {
                limit = m_Scanner.nextInt();
                if(limit > 0)
                {
                    isValidLimit = true;
                }
                else
                {
                    System.out.println("Limit Should be a positive n number.");
                }
            } catch (InputMismatchException e) {
                System.out.println("You have entered a string instead of a number.");
            }
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
            else
            {
                isValid = true;
            }
        }
        return choice;
    }

    @Override
    public void presentCommandListForExecution() {
        IStockEngine engine = m_Facade.getEngine();
        PrintUtils.printAllTransactionInSystem(engine);
    }

    public void getStockDetails()
    {
        if(m_LoadSuccessfully)
        {
            System.out.println("Please enter stock's name:");
            String stockName = m_Scanner.next();
            PresentStockDetails(stockName);
        }
        else
        {
            fileWasNotUploaded();
        }

    }

    @Override
    public void exit() {
        System.out.println("Thank you for using Rizpa stock exchange system");
    }
}
