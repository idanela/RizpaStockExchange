package ui.main.singleTransactionTable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import stockExchangeEngine.StockExchangeEngine;
import transaction.Transaction;
import transaction.TransactionMade;
import ui.main.MainController.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import user.User;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class singleTransactionTableController implements Initializable {

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    MainController mainController;
    @FXML private TableView<Transaction> SingleStockTransactionsMadeTableView;
    @FXML private TableColumn<TransactionMade, String> dateCol;
    @FXML private TableColumn<TransactionMade, String> kindCol;
    @FXML private TableColumn<TransactionMade, Integer> amountCol;
    @FXML private TableColumn<TransactionMade, Integer> priceCol;
    @FXML private TableColumn<TransactionMade, User> initiatorCol;
    @FXML private TableColumn<TransactionMade, String> BuyerCol;
    @FXML private TableColumn<TransactionMade, String> SelletrCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dateCol.setCellValueFactory(new PropertyValueFactory<TransactionMade,String>("dateOfTransaction"));
        kindCol.setCellValueFactory(new PropertyValueFactory<TransactionMade,String>("transactionKind"));
        amountCol.setCellValueFactory(new PropertyValueFactory<TransactionMade,Integer>("amountOfStocks"));
        priceCol.setCellValueFactory(new PropertyValueFactory<TransactionMade,Integer>("priceOfStockSelledFor"));
        initiatorCol.setCellValueFactory(new PropertyValueFactory<TransactionMade,User>("UserName"));
        BuyerCol.setCellValueFactory(new PropertyValueFactory<TransactionMade,String>("buyerName"));
        SelletrCol.setCellValueFactory(new PropertyValueFactory<TransactionMade,String>("sellerName"));
    }


    public void clearTable() {
        SingleStockTransactionsMadeTableView.getItems().clear();
    }

    public void presentStocksTransactions(String selectedItem) {
        if(selectedItem != null)
        {
            StockExchangeEngine engine = mainController.getEngine();
            SingleStockTransactionsMadeTableView.refresh();
            SingleStockTransactionsMadeTableView.setItems (getTransactions(engine.getTransactionList().stream().filter(transaction -> transaction.getStock().getStockName().equals(selectedItem)).collect(Collectors.toList())));
        }
        else
        {
            SingleStockTransactionsMadeTableView.getItems().clear();
        }
    }
    private ObservableList<Transaction> getTransactions(List<Transaction> transactions)
    {
        ObservableList<Transaction> transactionCollection= FXCollections.observableArrayList();
        for(Transaction transaction: transactions)
        {
            transactionCollection.add(transaction);
        }
        return transactionCollection;
    }

   /* public void transactionPreformed() {
        SingleStockTransactionsMadeTableView.refresh();
        singleTransactionTableController.set
    }*/
}

