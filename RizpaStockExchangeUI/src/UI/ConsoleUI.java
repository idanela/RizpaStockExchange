package UI;

import Facade.ConsoleFacade;
import Facade.IFacade;
import StockExchangeEngine.IStockEngine;
import Stocks.Stock;
import Transaction.*;
import UI.Command.MainMenu.*;
import UI.Command.OrderMenu.LMTBuyCommand;
import UI.Command.OrderMenu.LMTSellCommand;
import UI.Command.OrderMenu.MKTBuyCommand;
import UI.Command.OrderMenu.MKTSellCommand;
import UI.menu.Menu;
import UI.menuItem.MenuItem;
import UI.printUtils.PrintUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConsoleUI implements IUI
{

    private IFacade m_Facade;
    private Scanner m_Scanner;
    private boolean m_LoadSuccessfully;
    private Menu m_MainMenu;
    private Menu m_BuyOrderMenu;
    private Menu m_SellOrderMenu;
    private AllTransactionsKinds m_TransactionToUse;
    private static final String FILE_TEXT_PATH_NAME = "EnginePathSaver.txt";
    private static final String FILE_OBJECT_NAME = "EngineData.dat";
    private IStockEngine engine;


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
    public void setTransactionToUse(AllTransactionsKinds transaction) {
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
        menuItems.add(new MenuItem("Load system previous status.",new LoadPreviousSystemStatus(this)));
        menuItems.add(new MenuItem("Exit",new ExitCommand(this)));

        return menuItems;
    }

    private List<MenuItem> initializeBuyOrderMenu()
    {
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("LMT",new LMTBuyCommand(this)));
        menuItems.add(new MenuItem("MKT",new MKTBuyCommand(this)));

        return menuItems;
    }

    private List<MenuItem> initializeSellOrderMenu() {
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("LMT",new LMTSellCommand(this)));
        menuItems.add(new MenuItem("MKT",new MKTSellCommand(this)));

        return menuItems;
    }

    @Override
    public void loadStocksData() {
        try {
            AtomicBoolean hasSameSymbol = new AtomicBoolean(false);
            AtomicBoolean hasSameCompany = new AtomicBoolean(false);
            boolean loadedSuccessfully = false;
            StringBuilder msg = new StringBuilder();
            System.out.println("please enter file's full path:");
            String path = m_Scanner.nextLine();
            if (fileIsValid(path)) {
                loadedSuccessfully = m_Facade.loadStocksData(path, hasSameCompany, hasSameSymbol);
                printMsgAboutLoadedFile(loadedSuccessfully, hasSameCompany, hasSameSymbol);
            } else {
                printErrorsAboutFile(path);
            }
        }
        catch (InvalidPathException e)
        {
            System.out.println("you inserted invalid character to path");
        }
    }

    private void printErrorsAboutFile(String path) {
        StringBuilder msg = new StringBuilder("File is not valid because: " + System.lineSeparator());
            if (!Files.exists(Paths.get(path))) {
                msg.append("File is not exists" + System.lineSeparator());
            } else {
                if (!path.endsWith("xml")) {
                    msg.append("File is not an xml file" + System.lineSeparator());
                }
            }

            System.out.print(msg);
    }

    private void printMsgAboutLoadedFile(boolean loadedSuccessfully, AtomicBoolean hasSameCompany, AtomicBoolean hasSameSymbol)
    {
        StringBuilder msg = new StringBuilder();
        if(loadedSuccessfully)
        {
            m_LoadSuccessfully = true;
            msg.append("File was Loaded Successfully.");
        }
        else
        {
            System.out.println("File was failed to load because:");
            if(hasSameCompany.get())
            {
                msg.append("The file contains two different stocks belong to the same company. ");
            }
            if(hasSameSymbol.get())
            {
                msg.append("File Contains two stocks with same Symbol");
            }
        }

        System.out.println(msg +System.lineSeparator());

    }

    private boolean fileIsValid(String path) {
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
            System.out.println("Please enter stock's symbol:");
            String StockName = m_Scanner.nextLine().toUpperCase();
            if (!m_Facade.isStockExists(StockName)) {
                System.out.println("There is no stock by that name.");
                return;
            }
            int amountForTransaction = getNumberOfStocksToPreformTransaction();
            preformTransactionAccordingToUserChoice(choice.toUpperCase(), StockName, amountForTransaction);
        }
        else
        {
            fileWasNotUploaded();
        }
    }

    private void preformTransactionAccordingToUserChoice(String choice, String stockName, int amountForTransaction) {

        int limit = 0;
        Stock stock = m_Facade.getStock(stockName);
        if(choice.equals("B"))
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
            if(!(m_TransactionToUse instanceof MKTTransaction))
                limit = getLimit();
            m_TransactionToUse.setProperties(stock,limit,amountForTransaction);
            sellStocks(m_TransactionToUse);
        }
    }

    private int getNumberOfStocksToPreformTransaction() {
        int amount =0;
        boolean isValidAmount = false;
        while(!isValidAmount) {
            try {
                System.out.println("Please insert number of stocks for a transaction( a positive number).");
                Scanner scanner = new Scanner(System.in);
                amount = scanner.nextInt();
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

    private void buyStocks(ITransaction transaction) {
        int amount = m_TransactionToUse.getNumOfStocks();
        if(m_Facade.isStockExists(transaction.getStock().getStockName()))
        {
            List<TransactionMade> transactionsMade = m_Facade.buyStocks(m_TransactionToUse);
            PrintUtils.printDetailsAboutTransactionsMade(transactionsMade,amount);
        }
        else
        {
            stockNotExists(transaction.getStock().getStockName());
        }
    }

    private void sellStocks(ITransaction transaction) {
        int amount = m_TransactionToUse.getNumOfStocks();
        if(m_Facade.isStockExists(transaction.getStock().getStockName())) {
            List<TransactionMade> transactions = m_Facade.sellStocks(m_TransactionToUse);
            PrintUtils.printDetailsAboutTransactionsMade(transactions, amount);
        }
        else
        {
            stockNotExists(transaction.getStock().getStockName());
        }
    }

    private void stockNotExists(String stockName)
    {
        System.out.println("There is no stock called " + stockName + " in the system");
    }

    private int getLimit()
    {
        int limit = 0;
        boolean isValidLimit = false;
        while(!isValidLimit)
        {
            System.out.println("Please enter the limit(a positive number) for the transaction");
            try {
                Scanner scanner = new Scanner(System.in);
                limit = scanner.nextInt();
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
            choice= m_Scanner.nextLine();
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
            System.out.println("Please enter stock's Symbol:");
            String stockName = m_Scanner.nextLine();
            PresentStockDetails(stockName);
        }
        else
        {
            fileWasNotUploaded();
        }

    }

    @Override
    public void exit() {

            System.out.println("Would you like to save current system's status?(Y/N)");
            String choice = getUserSavingStatusChoice();

        if (m_LoadSuccessfully) {
            if (choice.toUpperCase().equals("Y")) {
                writeSystemEngineToFile();
            }
        }
        else
            System.out.println("There is nothing to save currently(data about the system was never loaded)");
            System.out.println("Thank you for using Rizpa stock exchange system");
    }

    private String getUserSavingStatusChoice() {
        String choice = null;
        boolean isValidChoice = false;
        while(!isValidChoice)
        {
            choice = m_Scanner.nextLine();
            if(choice.toUpperCase().equals("Y") || choice.toUpperCase().equals("N"))
            {
                isValidChoice = true;
            }
            else
            {
                System.out.println("you've inserted something that is not 'Y' or 'N'");
                System.out.println("Please insert 'Y' if you like to save system's current status otherwise insert 'N'");
            }
        }

        return choice;
    }

    public void writeSystemEngineToFile()
    {
        boolean isValidPath = false;
        while(!isValidPath)
        {
            System.out.println("Please insert full path for a file to save current system status.");
            String path = m_Scanner.nextLine();
            isValidPath = writeEngine(path);
        }

        System.out.println("Data of system was saved successfully");
    }

    private boolean writeEngine(String path) {
        boolean isValidPath = true;
        try(ObjectOutputStream out =
                    new ObjectOutputStream(
                            new FileOutputStream(path)))
        {
            out.writeObject(m_Facade.getEngine());
            out.flush();
        }
        catch (FileNotFoundException e)
        {
            System.out.println("path to save file at is not exists in this computer");
            isValidPath = false;
        }
        catch (Exception e) {
            e.printStackTrace();
            isValidPath = false;
        }
        return isValidPath;
    }

    @Override
    public void readEngineDataFromFile() {
        try {
            System.out.println("Enter a path for a file to read previous system data from ");
            String path = m_Scanner.nextLine();
        if(Files.isDirectory(Paths.get(path)))
        {
            System.out.println("path is not a file but a directory");
            return;
        }
            if (Files.exists(Paths.get(path))) {
                readEngine(path);
                if(m_LoadSuccessfully)
                System.out.println("Previous data was loaded successfully." + System.lineSeparator());
            } else {
                System.out.println("Path: " + path+ " doesn't exists on this computer." + System.lineSeparator());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void readEngine(String path) {

        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream(path))) {
            try {
                IStockEngine EngineFromFile = (IStockEngine) in.readObject();
                m_Facade.setEngine(EngineFromFile);
                m_LoadSuccessfully = true;
            } catch (ClassNotFoundException e) {
                System.out.println("Class to load was not found in the system.");
            }
        } catch (StreamCorruptedException e) {
            System.out.println("File is not suitable to load data from it to system (doesn't contain data about the system)");
        }
        catch (EOFException e)
        {
            System.out.println("File is not suitable for load data from it to system (doesn't contain data about the system)");
        }
        catch (IOException e) {
            System.out.println("File to read from can not be accessed.");
        }
    }
}


