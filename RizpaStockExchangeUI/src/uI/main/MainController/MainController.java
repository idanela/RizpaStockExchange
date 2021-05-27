package uI.main.MainController;

import stockExchangeEngine.StockExchangeEngine;
import transaction.AllTransactionsKinds;
import transaction.TransactionMade;
import uI.main.AllStockTransactionsController.AllStockTransactionsController;
import uI.main.UsersComponent.UserController;
import uI.main.singleTransactionTable.singleTransactionTableController;
import uI.main.stockExchangeApplication.RizpaStockExchangeController;
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

    public void populateTransactionMadeTable(List<TransactionMade> transactionsMade)
    {

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
        if(isBuyOperation)
        {
            transactionsMade =  transactionToUse.findCounterTransaction(engine.getPendingSellTransactions(), engine.getPendingBuyTransactions());
            user.addShares(transactionsMade);
            removeSharesFormOtherUsers(transactionsMade);
            user.setPendingBuyTransactions(engine.getPendingBuyTransactions().stream().filter((transaction -> transaction.getInitiator().equals(user))).collect(Collectors.toList()));
        }
        else {
            transactionsMade = transactionToUse.findCounterTransaction(engine.getPendingBuyTransactions(), engine.getPendingSellTransactions());
            user.removeShares(transactionsMade);
            addSharesToOtherUsers(transactionsMade);
            user.setPendingSellTransactions(engine.getPendingSellTransactions().stream().filter(transaction -> transaction.getInitiator().equals(user)).collect(Collectors.toList()));
        }
        engine.addTransactionsMade(transactionsMade);
        user.addTransactions(transactionsMade);
      //  updatePartnersTables(transactionsMade);
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

    public void updatePartnersTables(List<TransactionMade> transactionsMade) {
        UserController controller;
        for (TransactionMade transaction: transactionsMade)
        {
            controller = getUserController(transaction.getPartner());
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


    public void transactionsPreformed() { // lets the main controller know that transaction preformed
        allStockTransactionsController.transactionPreformed();
    }

    public void presentStockTransaction(String selectedItem) {
            allStockTransactionsController.presentStockTransactions(selectedItem);
    }
}
