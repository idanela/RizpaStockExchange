package ui.main.MainController;

import holding.Holding;
import stockExchangeEngine.StockExchangeEngine;
import transaction.AllTransactionsKinds;
import transaction.Transaction;
import transaction.TransactionMade;
import ui.main.AllStockTransactionsController.AllStockTransactionsController;
import ui.main.UsersComponent.UserController;
import ui.main.singleTransactionTable.singleTransactionTableController;
import ui.main.stockExchangeApplication.RizpaStockExchangeController;
import user.User;

import java.util.*;
import java.util.stream.Collectors;

public class MainController {

    StockExchangeEngine engine;

    public void setEngine(StockExchangeEngine engine) {
        this.engine = engine;
    }

    public StockExchangeEngine getEngine() {
        return engine;
    }

    RizpaStockExchangeController rizpaStockExchangeController;
    singleTransactionTableController singleTransactionTableController;
    AllStockTransactionsController allStockTransactionsController;
    List<UserController> userControllers = new ArrayList<>();

    public void addUserController(UserController controller)
    {
        userControllers.add(controller);
    }
    public void setAllStocksTransactionsController(AllStockTransactionsController allStocksTransactionsController) {
        this.allStockTransactionsController = allStocksTransactionsController;
        allStockTransactionsController.setMainController(this);
    }

    public void setRizpaStockExchangeController(RizpaStockExchangeController rizpaStockExchangeController) {
        this.rizpaStockExchangeController = rizpaStockExchangeController;
        rizpaStockExchangeController.setMainController(this);
    }

    public void setSingleTransactionTableController(singleTransactionTableController singleTransactionTableController) {
        this.singleTransactionTableController = singleTransactionTableController;
        singleTransactionTableController.setMainController(this);
    }


    public Set<String> getAllSymbols() {
        return engine.getStocks().keySet();
    }


    public void clearSingleStockTable() {

        singleTransactionTableController.clearTable();
    }

    public List<TransactionMade> preformTransaction(AllTransactionsKinds transactionToUse, String stockName, String kind, int numOfStocks, int limit, User user,boolean isBuyOperation )
    {
        List<TransactionMade> transactionsMade;
        transactionToUse.setProperties(engine.getStock(stockName),limit,numOfStocks,user, kind);
        int numberOfStocks = transactionToUse.getAmountOfStocks();
        if(isBuyOperation)
        {
            transactionsMade =  transactionToUse.findCounterTransaction(engine.getPendingSellTransactions(), engine.getPendingBuyTransactions(),true);
            user.addShares(transactionsMade);
            removeSharesFormOtherUsers(transactionsMade);
            user.setPendingBuyTransactions(engine.getPendingBuyTransactions().stream().filter((transaction -> transaction.getInitiator().equals(user))).collect(Collectors.toList()));
        }
        else {
            transactionsMade = transactionToUse.findCounterTransaction(engine.getPendingBuyTransactions(), engine.getPendingSellTransactions(),false);
            user.removeShares(transactionsMade);
            addSharesToOtherUsers(transactionsMade);
            user.setPendingSellTransactions(engine.getPendingSellTransactions().stream().filter(transaction -> transaction.getInitiator().equals(user)).collect(Collectors.toList()));
        }
        engine.setStockNewPrice(transactionsMade);
        rizpaStockExchangeController.refreshAllStocksTable();
        engine.addTransactionsMade(transactionsMade);
        user.addTransactions(transactionsMade);
        updateFreeStocks(transactionsMade,user);
        rizpaStockExchangeController.showPopUpWithTransactionsDetails(transactionsMade ,numberOfStocks);
        return transactionsMade;
    }

    private void addSharesToOtherUsers(List<TransactionMade> transactionsMade) {
        Map<String,User> users = getPartners(transactionsMade);
        for (User user : users.values())
        {
            List<TransactionMade> userTransactions = transactionsMade.stream()
                    .filter(transactionMade -> transactionMade.getPartner().equals(user)).collect(Collectors.toList());
            user.addShares(userTransactions);
        }
    }

    private Map<String, User> getPartners(List<TransactionMade> transactionsMade) {
        UserController controller;
        Map<String, User> users = new HashMap<>();
        for (TransactionMade transaction : transactionsMade) {
            controller = getUserController(transaction.getPartner());
            User user = controller.getUser();
            if (!users.containsKey(user.getName())) {
                users.put(user.getName(), user);
            }
        }
        return users;
    }

    private void removeSharesFormOtherUsers(List<TransactionMade> transactionsMade) {
        Map<String,User> users = getPartners(transactionsMade);
        for (User user : users.values())
        {
            List<TransactionMade> userTransactions = transactionsMade.stream()
                    .filter(transactionMade -> transactionMade.getPartner().equals(user)).collect(Collectors.toList());
            user.removeShares(userTransactions);
        }

    }

    private void updateFreeStocks(List<TransactionMade> transactionsMade, User initiator) {
        Map<String, User> users = getPartners(transactionsMade);
        users.put(initiator.getName(),initiator);
        List<Transaction> pendingSellList;
        for(User user:users.values())
        {
            for(Holding holding: user.getHoldings().values())
            {
                int numOfStocks = holding.getAmountOfStocks();
                pendingSellList = engine.getPendingSellTransactions().stream().filter(transaction -> transaction.getStock().equals(holding.getStock())).filter(transaction -> transaction.getInitiator().equals(user)).collect(Collectors.toList());
                for(Transaction transaction:pendingSellList)
                {
                    numOfStocks = numOfStocks - transaction.getAmountOfStocks();
                }
                holding.setAmountFreeToUse(numOfStocks);
            }
        }
    }

    public void updatePartnersTables(List<TransactionMade> transactionsMade) {
        UserController controller;

        for(User user: getEngine().getUsers().values())
        {
            user.updateStocksPrice(transactionsMade);
            controller = getUserController(user);
            controller.refreshTable();
        }
    }

    private UserController getUserController(User user)
    {
        UserController controller = null;
        for(UserController userController: userControllers)
        {
            if(userController.getUser().equals(user))
            {
                controller = userController;
                break;
            }
        }

        return controller;
    }

    public void presentStockTransaction(String selectedItem) {
            allStockTransactionsController.presentStockTransactions(selectedItem);
    }

    public void singleStockPicked(String selectedItem) {
        singleTransactionTableController.presentStocksTransactions(selectedItem);
    }

    public void clearTables() {
        this.clearSingleStockTable();
        allStockTransactionsController.clearTables();
    }

    public void removeUsersControllers() {
        userControllers.clear();
    }

    public void clearTransactionsUI() {
        userControllers.forEach(UserController::clearTransactionUI);
    }
}
